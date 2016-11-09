package com.dooioo.job.monitor;

import com.dooioo.job.monitor.model.JobSetting;
import com.dooioo.job.monitor.service.IJobSettingService;
import com.dooioo.monitor.job.CronTaskPoJo;
import com.dooioo.monitor.job.IProtocol;
import com.dooioo.util.Configuration;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * server for job
 * @author dingxin
 *
 */
@Component
@Scope("singleton")
public class JobSocketServer implements InitializingBean,IProtocol,ApplicationContextAware{

	private static Log logger = LogFactory.getLog(JobSocketServer.class);
	
	private static Map<String,List<CronTaskPoJo>> allJobs = new ConcurrentHashMap<String,List<CronTaskPoJo>>();
	
	private ServerSocket serverSocket;
	
	private IJobSettingService jobSettingService;
	
	private List<ClientGetHandler> inThreadList = new ArrayList<ClientGetHandler>();
	private List<ClientSendHandler> outThreadList = new ArrayList<ClientSendHandler>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Configuration config = Configuration.getInstance();
		Set<String> ips = com.dooioo.core.utils.IPUtil.getLocalIpSet();
		
		if (!config.isDevelopment()
				&& !ips.stream().anyMatch(ip -> config.getMasterNode().contains(ip))) {
			return;
		}
		Thread daemonThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(Integer.parseInt(config.getProperty("job.socket.server.port")));	
					for(;;){
						Socket socket = serverSocket.accept();
						ClientGetHandler clientGetHandler = new ClientGetHandler(socket);
						
						ClientSendHandler clientSendHandler = new ClientSendHandler(socket);
						
						clientGetHandler.setClientSendHandler(clientSendHandler);
						clientSendHandler.setClientGetHandler(clientGetHandler);
						clientGetHandler.start();
						clientSendHandler.start();
						synchronized (inThreadList) {
							inThreadList.add(clientGetHandler);
						}
						synchronized (outThreadList) {
							outThreadList.add(clientSendHandler);
						}
					}
				} catch (IOException e) {
				}
			}
		},"job-monitor-server");
		daemonThread.setDaemon(true);
		daemonThread.start();
	}
	
	public void interruptHandler(Thread thread){
		if(thread == null)
			return;
		if(thread instanceof ClientGetHandler){
			ClientGetHandler clientGetHandler = (ClientGetHandler)thread;
			ClientSendHandler clientSendHandler = clientGetHandler.getClientSendHandler();
			synchronized (inThreadList) {
				inThreadList.remove(clientGetHandler);
			}
			synchronized (outThreadList) {
				outThreadList.remove(clientSendHandler);
			}
			String appCode = clientGetHandler.getAppCode();
			String ip = clientGetHandler.getIp();
			Integer port = clientGetHandler.getPort();
			clientGetHandler.setRun(false);
			clientSendHandler.setRun(false);
			clientSendHandler.sendMessage("");
			allJobs.remove(appCode+"|"+ip+"|"+port);
			Map<String,Object> params = new HashMap<>(2);
			params.put("appCode", appCode);
			params.put("ip", ip);
			List<JobSetting> list = jobSettingService.queryJobSettingList(params);
			list.stream().forEach(jobSetting -> {
				if(jobSetting.getStatus() != null && jobSetting.getStatus().intValue() == 1){
					jobSetting.setStatus(0);
					jobSettingService.update(jobSetting);
				}
			});
		}else if(thread instanceof ClientSendHandler){
			ClientSendHandler clientSendHandler = (ClientSendHandler)thread;
			ClientGetHandler clientGetHandler = clientSendHandler.getClientGetHandler();
			synchronized (inThreadList) {
				inThreadList.remove(clientGetHandler);
			}
			synchronized (outThreadList) {
				outThreadList.remove(clientSendHandler);
			}
			String appCode = clientSendHandler.getAppCode();
			String ip = clientSendHandler.getIp();
			Integer port = clientSendHandler.getPort();
			clientGetHandler.setRun(false);
			clientSendHandler.setRun(false);
			clientSendHandler.sendMessage("");
			allJobs.remove(appCode+"|"+ip+"|"+port);
			Map<String,Object> params = new HashMap<>(2);
			params.put("appCode", appCode);
			params.put("ip", ip);
			List<JobSetting> list = jobSettingService.queryJobSettingList(params);
			list.stream().forEach(jobSetting -> {
				if(jobSetting.getStatus() != null && jobSetting.getStatus().intValue() == 1){
					jobSetting.setStatus(0);
					jobSettingService.update(jobSetting);
				}
			});
		}
	}
	
	/**
	 * 修改JOB
	 * @param appCode
	 * @param ip
	 * @param method
	 * @param cron
	 */
	public void modifyTask(String appCode,String ip ,String method,String cron){
		Assert.notNull(appCode,"appCode can not be null!");
		Assert.notNull(ip,"ip can not be null!");
		Assert.notNull(method,"method can not be null!");
		logger.info("修改"+appCode+"系统"+ip+"节点的"+method+"定时任务Cron表达式为"+cron);
		synchronized (inThreadList) {
			for(ClientGetHandler clientGetHandler :inThreadList){
				if(appCode.equals(clientGetHandler.getAppCode())
						&& ip.equals(clientGetHandler.getIp())){
					for(CronTaskPoJo cronTaskPoJo:clientGetHandler.getCronTasks()){
						if(method.equals(cronTaskPoJo.getMethod())){
							cronTaskPoJo.setCron(cron);
							clientGetHandler.getClientSendHandler().sendMessage(IProtocolPrefix.ModifyJob.getCode()+JSONObject.fromObject(cronTaskPoJo).toString());
						}
					}
				}
			}
		}
	}
	
	public void suspendJob(String appCode,String ip,String suspendMethodName){
		Assert.notNull(appCode,"appCode can not be null!");
		Assert.notNull(ip,"ip can not be null!");
		Assert.notNull(suspendMethodName, "suspendMethodName can not be null!");
		synchronized (inThreadList) {
			for(ClientGetHandler clientGetHandler :inThreadList){
				if(appCode.equals(clientGetHandler.getAppCode())
						&& ip.equals(clientGetHandler.getIp())){
					clientGetHandler.getClientSendHandler().sendMessage(IProtocolPrefix.SuspendJob.getCode()+suspendMethodName);
				}
			}
		}
	}
	
	public void resumeJob(String appCode,String ip,String suspendMethodName){
		Assert.notNull(appCode,"appCode can not be null!");
		Assert.notNull(ip,"ip can not be null!");
		Assert.notNull(suspendMethodName, "suspendMethodName can not be null!");
		synchronized (inThreadList) {
			for(ClientGetHandler clientGetHandler :inThreadList){
				if(appCode.equals(clientGetHandler.getAppCode())
						&& ip.equals(clientGetHandler.getIp())){
					clientGetHandler.getClientSendHandler().sendMessage(IProtocolPrefix.ResumeJob.getCode()+suspendMethodName);
				}
			}
		}
	}
	
	/**
	 * 发送线程
	 * @author dingxin
	 *
	 */
	public class ClientSendHandler extends Thread{

		private Socket socket;
		private OutputStream out;
		
		private String appCode;
		private String ip;
		private int port;
		
		private boolean run = true;
		
		private ClientGetHandler clientGetHandler;
		
		private LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
		
		public ClientSendHandler(Socket socket){
			this.socket = socket;
			this.ip = socket.getInetAddress().getHostAddress();
			this.port = socket.getPort();
			super.setName("send-client-"+ip+"-"+port);
			try {
				this.out = this.socket.getOutputStream();
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
		
		public void setClientGetHandler(ClientGetHandler clientGetHandler) {
			this.clientGetHandler = clientGetHandler;
		}
		
		public ClientGetHandler getClientGetHandler() {
			return clientGetHandler;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(this == obj)
				return true;
			if(obj instanceof ClientSendHandler){
				ClientSendHandler other = (ClientSendHandler)obj;
				if(!Objects.equals(this.ip, other.ip)){
					return false;
				}
				if(!Objects.equals(this.port, other.port)){
					return false;
				}
				if(!Objects.equals(this.appCode, other.appCode)){
					return false;
				}
			}else{
				return false;
			}
			return true;
		}
		
		@Override
		public void run() {
			while(run){
				try {
					String str = messageQueue.take();
					out.write((str+CRLF).getBytes());
					out.flush();
				} catch (IOException | InterruptedException e) {
					logger.info(e.getMessage(),e);
				} catch (NullPointerException e) {
					interruptHandler(this);
					logger.info("系统"+appCode+"的"+ip+","+port+"节点断开连接");
					
					break;
				}
			}
		}
		
		public void sendMessage(String message){
			try {
				messageQueue.put(message);
			} catch (InterruptedException e) {
				logger.info(e.getMessage(),e);
			}
		}
		
		/**
		 * @return the run
		 */
		public boolean isRun() {
			return run;
		}

		/**
		 * @param run the run to set
		 */
		public void setRun(boolean run) {
			this.run = run;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}

		/**
		 * @return the appCode
		 */
		public String getAppCode() {
			return appCode;
		}

		/**
		 * @param appCode the appCode to set
		 */
		public void setAppCode(String appCode) {
			this.appCode = appCode;
		}

	}
	
	/**
	 * 接收线程
	 * @author dingxin
	 *
	 */
	public class ClientGetHandler extends Thread{
		
		private Socket socket;
		private InputStream in;
		
		private ClientSendHandler clientSendHandler;
		
		private String appCode;
		private String ip;
		private int port;
		private List<CronTaskPoJo> cronTasks;
		
		private boolean run = true;
		
		public ClientGetHandler(Socket socket){
			this.socket = socket;
			this.ip = socket.getInetAddress().getHostAddress();
			this.port = socket.getPort();
			super.setName("get-client-"+ip+"-"+port);
			try {
				this.in = this.socket.getInputStream();
			} catch (IOException e) {
				logger.info(e.getMessage(), e);
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(this == obj)
				return true;
			if(obj instanceof ClientGetHandler){
				ClientGetHandler other = (ClientGetHandler)obj;
				if(!Objects.equals(this.ip, other.ip)){
					return false;
				}
				if(!Objects.equals(this.port, other.port)){
					return false;
				}
				if(!Objects.equals(this.appCode, other.appCode)){
					return false;
				}
			}else{
				return false;
			}
			return true;
		}
		
		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			while(run){
				try {
					readLine = br.readLine();
					if(run)
						parseMessage(readLine);
					else
						break;
				} catch (IOException e) {
					logger.info(e.getMessage(), e);
				} catch (NullPointerException e){
					logger.info("系统"+appCode+"的"+ip+","+port+"节点断开连接");
					interruptHandler(this);
					break;
				}
			}  
		}
		
		public void setClientSendHandler(ClientSendHandler clientSendHandler) {
			this.clientSendHandler = clientSendHandler;
		}
		
		public ClientSendHandler getClientSendHandler() {
			return clientSendHandler;
		}
		
		@SuppressWarnings("unchecked")
		private void parseMessage(String message){
			if(message.length() <= 4)
				return;
			Integer code = Integer.parseInt(message.substring(0, 4));
			IProtocolPrefix type = IProtocolPrefix.getIProtocolPrefixByCode(code);
			switch (type) {
			case Connect:
				appCode = message.substring(4);
				logger.info("注册 AppCode 为" + appCode + "节点 IP 为"+ip);
				clientSendHandler.setAppCode(appCode);
				break;
				
			case JobList:
				if(appCode == null || "".equals(appCode)){
					clientSendHandler.sendMessage(String.valueOf(IProtocolPrefix.AskAppCode.getCode()));
					break;
				}
				JSONArray array = JSONArray.fromObject(message.substring(4));
				cronTasks = JSONArray.toList(array,new CronTaskPoJo(),new JsonConfig());
				for(int i = 0;i < cronTasks.size();i++){
					logger.info(appCode+"系统"+ip+"节点的任务"+(i+1)+"为方法"+cronTasks.get(i).getMethod()+" Cron表达式为"+cronTasks.get(i).getCron());
					cronTasks.get(i).setIp(ip);
					cronTasks.get(i).setAppCode(appCode);
				}
				Map<String,Object> map = new HashMap<>();
				map.put("ip", ip);
				map.put("appCode", appCode);
				List<JobSetting> list = jobSettingService.queryJobSettingList(map);
				list.stream().filter(jobSetting -> !cronTasks.stream().anyMatch(cronTask 
						-> jobSetting.getAppCode().equals(cronTask.getAppCode()) 
						&& jobSetting.getMethod().equals(cronTask.getMethod())
						&& jobSetting.getIp().equals(ip))).forEach(jobSetting -> {
							jobSettingService.deleteMonitor(jobSetting);
						});
				cronTasks.stream().forEach(cronTask -> {
					JobSetting jobSetting = jobSettingService.getJobSetting(cronTask.getAppCode(), ip , cronTask.getMethod());
					if(jobSetting == null){
						jobSetting = new JobSetting();
						jobSetting.setAppCode(cronTask.getAppCode());
						jobSetting.setIp(ip);
						jobSetting.setMethod(cronTask.getMethod());
						jobSetting.setSuspendFlag(cronTask.getSuspendFlag());
						jobSetting.setCron(cronTask.getCron());
						jobSetting.setDeleteFlag(Integer.valueOf(0));
						jobSetting.setCreator(Integer.valueOf(80001));
						jobSetting.setCreateTime(new java.util.Date());
						jobSetting.setStatus(Integer.valueOf(1));
						jobSettingService.save(jobSetting);
					}else{
						Integer suspendFlag = jobSetting.getSuspendFlag();
						if(suspendFlag == null){
							jobSetting.setSuspendFlag(cronTask.getSuspendFlag());
							jobSetting.setUpdator(Integer.valueOf(80001));
							jobSetting.setStatus(Integer.valueOf(1));
							jobSetting.setUpdateTime(new java.util.Date());
							jobSettingService.update(jobSetting);
						}else{
							if(cronTask.getSuspendFlag() != null && !cronTask.getSuspendFlag().equals(jobSetting.getSuspendFlag())){
								if(jobSetting.getSuspendFlag().intValue() == 0){
									logger.info("矫正"+appCode+"节点"+ip+"的方法"+jobSetting.getMethod()+"为可以运行");
									resumeJob(jobSetting.getAppCode(), ip, jobSetting.getMethod());
								}else{
									logger.info("矫正"+appCode+"节点"+ip+"的方法"+jobSetting.getMethod()+"为不可运行");
									suspendJob(jobSetting.getAppCode(), ip, jobSetting.getMethod());
								}
							}
						}
						if(cronTask.getCron() != null && !cronTask.getCron().equals(jobSetting.getCron())
								&  jobSetting.getSuspendFlag() != null && jobSetting.getSuspendFlag().intValue() == 0){
							logger.info("矫正"+appCode+"节点"+ip+"的方法"+jobSetting.getMethod()+"的Cron为"+jobSetting.getCron());
							modifyTask(jobSetting.getAppCode(), ip, jobSetting.getMethod(), jobSetting.getCron());
						}
						if(jobSetting.getStatus() == null || jobSetting.getStatus().intValue() == 0){
							jobSetting.setStatus(Integer.valueOf(1));
							jobSettingService.update(jobSetting);
						}
					}
				});
				allJobs.put(appCode+"|"+ip+"|"+port, cronTasks);
				break;
				
			case ReplyTip:
				logger.info("[Server] GET message from ["+ appCode + " | " + ip + "]" +message.substring(4));

			default:
				break;
			}
		}

		/**
		 * @return the run
		 */
		public boolean isRun() {
			return run;
		}

		/**
		 * @param run the run to set
		 */
		public void setRun(boolean run) {
			this.run = run;
		}

		/**
		 * @return the appCode
		 */
		public String getAppCode() {
			return appCode;
		}

		/**
		 * @param appCode the appCode to set
		 */
		public void setAppCode(String appCode) {
			this.appCode = appCode;
		}

		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}
		
		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @return the cronTasks
		 */
		public List<CronTaskPoJo> getCronTasks() {
			return cronTasks;
		}

		/**
		 * @param cronTasks the cronTasks to set
		 */
		public void setCronTasks(List<CronTaskPoJo> cronTasks) {
			this.cronTasks = cronTasks;
		}
		
		
	}
	
	public  Map<String,List<CronTaskPoJo>> getAllJobs() {
		return allJobs;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		jobSettingService = applicationContext.getBean(IJobSettingService.class);
	}
	
}
