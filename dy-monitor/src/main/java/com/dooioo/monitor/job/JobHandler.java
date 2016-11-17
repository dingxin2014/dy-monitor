package com.dooioo.monitor.job;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.util.Assert;


/**
 * Job Handler 
 * @author dingxin
 * @email being_away@qq.com
 *
 */
public class JobHandler implements InitializingBean , ApplicationContextAware{

	private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;
	
	private ApplicationContext applicationContext;
	
	private static final Log logger = LogFactory.getLog(JobHandler.class);

	private JobSocketClient jobSocketClient 			= null;
	private final SimpleTriggerContext triggerContext 	= new SimpleTriggerContext();
	private ScheduledTaskRegistrar registrar 			= null;
	private final static String REGISTRAR 				= "registrar";
	private final static String EXPRESSION 				= "expression";
	private final static String TRIGGER 				= "trigger";
	private final static String SEQUENCEGENERATOR		= "sequenceGenerator";
	private final static String CRONTASKS 				= "cronTasks";
	private final static String SCHEDULEDFUTURES 		= "scheduledFutures";
	private final static String TIMEZONE 				= "timeZone";
	private boolean init 								= false;
	private Vector<String> suspendVector 				= new Vector<>();
	
	private String appCode;
	
	private String server;
	
	/**
	 * 客户端心跳频率    ping-interval
	 */
	private Integer interval;
	
	private int port;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		scheduledAnnotationBeanPostProcessor = applicationContext.getBean(ScheduledAnnotationBeanPostProcessor.class);
		Assert.notNull(scheduledAnnotationBeanPostProcessor, "init jobhandler failed! scheduledAnnotationBeanPostProcessor is null");
		Field processorField = scheduledAnnotationBeanPostProcessor.getClass().getDeclaredField(REGISTRAR);
		Assert.notNull(processorField, "REGISTRAR is null!");
		processorField.setAccessible(true);
		registrar = (ScheduledTaskRegistrar) processorField.get(scheduledAnnotationBeanPostProcessor);
		init = true;
		if(logger.isInfoEnabled())
			logger.info("Job console init successfully!");
		initClient();
	}
	
	public void initClient(){
		if(logger.isInfoEnabled())
			logger.info("初始化客户端！");
		try {
			jobSocketClient = new JobSocketClient(this,appCode,server,port);
			if(interval != null && interval > 0)
				jobSocketClient.setInterval(interval);
			jobSocketClient.start();
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * @return the jobSocketClient
	 */
	public JobSocketClient getJobSocketClient() {
		return jobSocketClient;
	}



	/**
	 * @param jobSocketClient the jobSocketClient to set
	 */
	public void setJobSocketClient(JobSocketClient jobSocketClient) {
		this.jobSocketClient = jobSocketClient;
	}



	/**
	 * reset all threadPoolTaskScheduler's task and init threadPoolTaskScheduler;
	 */
	public synchronized void reset(){
		Assert.notNull(registrar,"registrar must not be null!");
		TaskScheduler taskScheduler = null;
		while((taskScheduler = registrar.getScheduler()) == null
				&& Thread.currentThread().getName().startsWith("job-monitor-client")){
			if(logger.isWarnEnabled())
				logger.warn("等待Spring TaskScheduler初始化...");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				break;
			}
		}
		org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler threadPoolTaskScheduler = null;
		if(taskScheduler instanceof org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler){
			threadPoolTaskScheduler = (ThreadPoolTaskScheduler) taskScheduler;
			threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
			threadPoolTaskScheduler.destroy();
			threadPoolTaskScheduler.initialize();
			threadPoolTaskScheduler.setRemoveOnCancelPolicy(false);
			scheduledAnnotationBeanPostProcessor.destroy();
		}else
			logger.warn("Unsupported Quartz Type! Only support [ThreadPoolTaskScheduler]!");
	}
	
	
	/**
	 * destroy job
	 * @return
	 */
	public synchronized boolean destroy(){
		Assert.notNull(registrar,"registrar must not be null!");
		TaskScheduler taskScheduler = null;
		while((taskScheduler = registrar.getScheduler()) == null
				&& Thread.currentThread().getName().startsWith("job-monitor-client")){
			if(logger.isWarnEnabled())
				logger.warn("等待Spring TaskScheduler初始化...");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				break;
			}
		}
		org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler threadPoolTaskScheduler = null;
		if(taskScheduler instanceof org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler){
			threadPoolTaskScheduler = (ThreadPoolTaskScheduler) taskScheduler;
			threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
			threadPoolTaskScheduler.destroy();
			threadPoolTaskScheduler.setRemoveOnCancelPolicy(false);
			scheduledAnnotationBeanPostProcessor.destroy();
			suspendVector = new Vector<>();
			if(logger.isInfoEnabled())
				logger.info("destroy sheduler job successfully!");
			return true;
		}else{
			if(logger.isInfoEnabled())
				logger.info("Unsupported Quartz Type! Only support [ThreadPoolTaskScheduler]!");
			return false;
		}
	}
	
	/**
	 * after destroy,restart job
	 */
	public synchronized void restart(){
		modifyCronTask(null, null);
	}
	
	/**
	 * 终止某个job
	 */
	public synchronized void suspend(String suspendMethodName){
		List<CronTask> list = getCronTasks();
		if(list.stream().filter(cronTask -> (((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getDeclaringClass().getName()+"."+
				((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getName()).equals(suspendMethodName)).count() == 0){
			return;
		}
		if(!suspendVector.contains(suspendMethodName)){
			suspendVector.add(suspendMethodName);
			refreshCronTask();
		}else{
			if(logger.isInfoEnabled())
				logger.info("中止方法"+suspendMethodName+"失败！此方法已经被中止，无法重复中止！");
		}
	}

	/**
	 * 恢复Job
	 * @param resumeMethodName
	 */
	public synchronized void resume(String resumeMethodName) {
		if(suspendVector.contains(resumeMethodName)){
			suspendVector.remove(resumeMethodName);
			refreshCronTask();
		}else{
			if(logger.isInfoEnabled())
				logger.info("恢复方法"+resumeMethodName+"失败！此方法并未被中止，无法恢复！");
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<CronTask> getCronTasks(){
		Assert.notNull(registrar,"registrar must not be null!");
		Field registrarField = null;
		try {
			registrarField = registrar.getClass().getDeclaredField(CRONTASKS);
		} catch (NoSuchFieldException | SecurityException e) {
			if(logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		}
		if(registrarField != null){
			registrarField.setAccessible(true);
			try {
				return (List<CronTask>) registrarField.get(registrar);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage(),e);
			}
		}
		return null;
	}
	
	private void setScheduledFutures(Set<ScheduledFuture<?>> scheduledFutures){
		Assert.notNull(registrar,"registrar must not be null!");
		Field registrarField = null;
		try {
			registrarField = registrar.getClass().getDeclaredField(SCHEDULEDFUTURES);
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error(e.getMessage(), e);
		}
		if(registrarField != null){
			try {
				registrarField.setAccessible(true);
				registrarField.set(registrar, scheduledFutures);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
	
	public synchronized void refreshCronTask(){
		modifyCronTask(null, null);
	}
	
	/**
	 * 修改定时任务Cron表达式,如果本机job已经destroy，此函数会重新初始化本机的job并运行
	 * @param methodName
	 * @param newCron
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public synchronized void modifyCronTask(String methodName, String newCron) {
		List<CronTask> originList = getCronTasks();
		if(originList != null && originList.size() > 0){
			reset();
			Set<ScheduledFuture<?>> scheduledFutures = new LinkedHashSet<ScheduledFuture<?>>();
			int size = originList.size();
			TaskScheduler taskScheduler = registrar.getScheduler();
			List<CronTask> list = new ArrayList<CronTask>(size);
			for(int i = 0;i < size;i++){
				CronTask cronTask = originList.get(i);
				
				ScheduledMethodRunnable scheduledMethodRunnable = (ScheduledMethodRunnable) cronTask.getRunnable();
				Method method = scheduledMethodRunnable.getMethod();
				
				if(suspendVector.contains(((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getDeclaringClass().getName()+"."+
						((ScheduledMethodRunnable)cronTask.getRunnable()).getMethod().getName())){
					continue;
				}
				
				try {
					parseCronTask(list, method, cronTask, methodName, newCron);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
				
				scheduledFutures.add(taskScheduler.schedule(
						cronTask.getRunnable(), cronTask.getTrigger()));
			}
			setScheduledFutures(scheduledFutures);
		}
	}
	
	
	/**
	 * parse CronTask
	 * @param list
	 * @param method
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void parseCronTask(List<CronTask> list, Method method, CronTask originCrontask,String methodName,String newCron) throws IllegalArgumentException, IllegalAccessException{
		Assert.notNull(list, "The crontask list must not be null!");
		Assert.notNull(method,"The method must not be null!");
		Assert.notNull(originCrontask,"The origin crontask must not be null!");
		if((method.getDeclaringClass().getName()+"."+method.getName()).equals(methodName)){
			Field cronTaskField = null;
			String oldCron = null;
			try {
				cronTaskField = CronTask.class.getDeclaredField(EXPRESSION);
			} catch (NoSuchFieldException | SecurityException e) {
				logger.error(e.getMessage(),e);
			}
			if(cronTaskField != null){
				cronTaskField.setAccessible(true);
				oldCron = (String) cronTaskField.get(originCrontask);
				cronTaskField.set(originCrontask, newCron);
			}
			
			Field triggerTaskField = null;
			try {
				triggerTaskField = TriggerTask.class.getDeclaredField(TRIGGER);
			} catch (NoSuchFieldException | SecurityException e) {
				logger.error(e.getMessage(),e);
			}
			if(triggerTaskField != null){
				triggerTaskField.setAccessible(true);
				org.springframework.scheduling.support.CronTrigger cronTrigger = (CronTrigger) triggerTaskField.get(originCrontask);
				Field cronTriggerField = null;
				try {
					cronTriggerField = org.springframework.scheduling.support.CronTrigger.class.getDeclaredField(SEQUENCEGENERATOR);
				} catch (NoSuchFieldException | SecurityException e) {
					logger.error(e.getMessage(), e);
				}
				CronSequenceGenerator newCronSequenceGenerator = null;
				if(cronTriggerField != null){
					cronTriggerField.setAccessible(true);
					CronSequenceGenerator cronSequenceGenerator = (CronSequenceGenerator) cronTriggerField.get(cronTrigger);
					Field cronSequenceGeneratorField = null;
					try {
						cronSequenceGeneratorField = CronSequenceGenerator.class.getDeclaredField(TIMEZONE);
					} catch (NoSuchFieldException | SecurityException e) {
						if(logger.isErrorEnabled())
							logger.error(e.getMessage(), e);
					}
					if(cronSequenceGeneratorField != null){
						cronSequenceGeneratorField.setAccessible(true);
						TimeZone timeZone = (TimeZone) cronSequenceGeneratorField.get(cronSequenceGenerator);
						try{
							newCronSequenceGenerator = new CronSequenceGenerator(newCron, timeZone);
							cronTrigger.nextExecutionTime(triggerContext);
							cronTriggerField.set(cronTrigger, newCronSequenceGenerator);
						}catch(java.lang.IllegalArgumentException e){
							if(oldCron != null){
								cronTaskField.set(originCrontask, oldCron);
							}
							if(logger.isInfoEnabled())
								logger.info("错误的不可用的Cron设置 "+e.getMessage());
						}
					}
				}
				triggerTaskField.set(originCrontask, cronTrigger);
			}
		}
		list.add(originCrontask);
	}

	/**
	 * @return the init
	 */
	public boolean isInit() {
		return init;
	}
	
	public ScheduledTaskRegistrar getRegistrar() {
		return registrar;
	}
	

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	public String getAppCode() {
		return appCode;
	}


	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}


	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
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
	 * @return the interval
	 */
	public Integer getInterval() {
		return interval;
	}


	/**
	 * @param interval the interval to set
	 */
	public void setInterval(Integer interval) {
		this.interval = interval;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	/**
	 * @return the suspendVector
	 */
	public Vector<String> getSuspendVector() {
		return suspendVector;
	}

	
	
}
