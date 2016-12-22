package com.dooioo.monitor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * job client   
 * @author dingxin
 *
 */
public class JobSocketClient extends Thread implements IProtocol{
	
	private String appCode = null;
	private String server = null;
	private Integer port = null;
	
	private volatile boolean run = false;
	
	private static final int DEFAULT_INTERVAL = 10000;
	
	/**
	 *  心跳频率  ping-interval
	 */
	private int interval;  //ms
	private Socket socket = null;
	private OutputStream out = null;
	private InputStream in = null;
	
	private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
	
	private static final Log logger = LogFactory.getLog(JobSocketClient.class);
	
	private final JobHandler jobHandler;
	
	private Thread receiveThread;
	
	public JobSocketClient(JobHandler jobHandler,String appCode,String server,int port){
		this(jobHandler, appCode, server, port,  DEFAULT_INTERVAL);
	}
	
	public JobSocketClient(JobHandler jobHandler,String appCode,String server,int port, int interval){
		this.jobHandler = jobHandler;
		this.appCode = appCode;
		this.server = server;
		this.port = port;
		this.interval = interval;
		setName("job-monitor-client-sender");
		setDaemon(true);
	}
	
	public void addMessage(String message){
		messageQueue.add(message);
	}
	
	public void init(){
		try {
			if(appCode == null)
				throw new RuntimeException("appCode is null! can not create connection with job monitor server!");
			this.socket = new Socket(server, port);
			logger.info("Connection successfully!");
			run = true;
			out = socket.getOutputStream();
			in = socket.getInputStream();
			receiveThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String readLine = null;
					while(run){
						try {
							readLine = br.readLine();
							if(readLine == null || readLine.length() <= 4)
								continue;
							Integer code = null;
							try {
								code = Integer.parseInt(readLine.substring(0, 4));
							} catch (NumberFormatException e) {
								logger.info("非法信息-->"+readLine);
								continue;
							}
							IProtocolPrefix type = IProtocolPrefix.getIProtocolPrefixByCode(code);
							String message = null;
							switch (type) {
							case ModifyJob:
								message = readLine.substring(4);
								CronTaskPoJo cronTaskPoJo = (CronTaskPoJo) JSONObject.toBean(JSONObject.fromObject(message), CronTaskPoJo.class);
								Vector<String> suspendVector = jobHandler.getSuspendVector();
								if(cronTaskPoJo != null && cronTaskPoJo.getMethod() != null && cronTaskPoJo.getCron() != null
										&& !suspendVector.contains(cronTaskPoJo.getMethod())){
									if(logger.isInfoEnabled())
										logger.info("本地修改方法 "+message);
									jobHandler.modifyCronTask(cronTaskPoJo.getMethod(), cronTaskPoJo.getCron());
								}else
									messageQueue.add(IProtocolPrefix.ReplyTip.getCode()+"方法"+cronTaskPoJo.getMethod()+"已经被中止，不可以修改JOB的Cron表达式！");
								break;
	
							case AskAppCode:
								messageQueue.add(IProtocolPrefix.Connect.getCode()+appCode);
								break;
								
							case SuspendJob:
								message = readLine.substring(4);
								if(logger.isInfoEnabled())
									logger.info("本地中止方法 "+message);
								jobHandler.suspend(message);
								break;
								
							case ResumeJob:
								message = readLine.substring(4);
								if(logger.isInfoEnabled())
									logger.info("本地恢复方法 "+message);
								jobHandler.resume(message);
								break;
								
								
							case ReplyTip:
								if(logger.isInfoEnabled())
									logger.info("[Server]"+readLine.substring(4));
								break;
								
							default:
								break;
								
							}
						} catch (IOException | NullPointerException e) {
							if(logger.isDebugEnabled())
								logger.debug(e.getMessage(),e);
							run = false;
						} 						
					}
				}
			},"job-monitor-client-receiver");
			receiveThread.setDaemon(true);
			receiveThread.start();
			if(logger.isInfoEnabled())
				logger.info("连接JOB服务器成功!");
		} catch (IOException e) {
			try {
				Thread.sleep(DEFAULT_INTERVAL);
			} catch (InterruptedException e1) {
			}
			if(logger.isInfoEnabled())
				logger.info(e.getMessage());
		}
	}

	@Override
	public void run() {
		for (;;){
			try {
				if(!run)
					init();
				if(socket == null || !run){
					try {
						Thread.sleep(DEFAULT_INTERVAL);
					} catch (InterruptedException e) {
					}
					continue;
				}
				//注册本系统  发送AppCode
				byte[] bytes = (IProtocolPrefix.Connect.getCode()+appCode+CRLF).getBytes();
				out.write(bytes);
				out.flush();
				break;
			} catch (IOException e) {
				logger.info("Connect Failed!");
			}
		}
		while(run){
			while(!messageQueue.isEmpty()){
				try {
					String message = null;
					if((message = messageQueue.poll()) != null)
						out.write((message+CRLF).getBytes());
				} catch (IOException e) {
					if(logger.isDebugEnabled())
						logger.debug(e.getMessage(),e);
				}
			}
			try {
				List<CronTask> list = jobHandler.getCronTasks();
				if(list != null){
					List<CronTaskPoJo> pojoList = convertCronTask2PoJo(list);
					Vector<String> suspendVector = jobHandler.getSuspendVector();
					pojoList.stream().forEach(cronTaskPojo -> {
						if(suspendVector.contains(cronTaskPojo.getMethod())){
							cronTaskPojo.setSuspendFlag(1);
						}else{
							cronTaskPojo.setSuspendFlag(0);
						}
					});
					StringBuffer message = new StringBuffer();
					message.append(IProtocolPrefix.JobList.getCode());
					JSONArray json = JSONArray.fromObject(pojoList);
					message.append(json.toString()).append(CRLF);
					out.write(message.toString().getBytes());
					out.flush();
				}
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					if(logger.isDebugEnabled())
						logger.debug(e.getMessage(), e);
				}

			} catch (IOException e) {
				if(logger.isInfoEnabled())
					logger.info("与服务器断开连接!");
				run = false;
				if(in != null){
					try {
						in.close();
					} catch (IOException e1) {
					}
				}
				if(out != null){
					try {
						out.close();
					} catch (IOException e1) {
					}
				}
				break;
			}
		}
		if(logger.isInfoEnabled())
			logger.info("尝试重新连接！");
		jobHandler.initClient();
	}
	
	
	private List<CronTaskPoJo> convertCronTask2PoJo(List<CronTask> list){
		Vector<String> suspendVector = jobHandler.getSuspendVector();
		return list.stream().map(cronTask -> 
			{	
				CronTaskPoJo cronTaskPoJo = new CronTaskPoJo();
				cronTaskPoJo.setMethod(((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getDeclaringClass().getName()+"."+((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getName());
				if(suspendVector.contains(cronTaskPoJo.getMethod()))
					cronTaskPoJo.setSuspendFlag(Integer.valueOf(1));
				else
					cronTaskPoJo.setSuspendFlag(Integer.valueOf(0));
				cronTaskPoJo.setCron(cronTask.getExpression());
				return cronTaskPoJo; 
			}).collect(Collectors.toList());
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
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		if(interval > 0)
			this.interval = interval;
	}
	
	
	
	
}
