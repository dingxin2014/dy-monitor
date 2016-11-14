package com.dooioo.monitor.job;


public class CronTaskPoJo{
	
	private String ip;
	private String appCode;
	private String method;
	private String cron;
	private Integer suspendFlag;
	
	public CronTaskPoJo(){
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the cron
	 */
	public String getCron() {
		return cron;
	}

	/**
	 * @param cron the cron to set
	 */
	public void setCron(String cron) {
		this.cron = cron;
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

	/**
	 * @return the suspendFlag
	 */
	public Integer getSuspendFlag() {
		return suspendFlag;
	}

	/**
	 * @param suspendFlag the suspendFlag to set
	 */
	public void setSuspendFlag(Integer suspendFlag) {
		this.suspendFlag = suspendFlag;
	}
	
	
	
	
}