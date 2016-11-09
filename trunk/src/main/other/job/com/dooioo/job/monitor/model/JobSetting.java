package com.dooioo.job.monitor.model;

import com.dooioo.monitor.job.CronTaskPoJo;

import java.util.Date;

public class JobSetting extends CronTaskPoJo{

	private Integer id;
	private Integer status;
	private Integer deleteFlag;
	private String appName;
	private String remark;
	private Integer creator;
	private Date createTime;
	private Integer updator;
	private Date updateTime;
	private Integer suspendFlag;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the deleteFlag
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the creator
	 */
	public Integer getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updator
	 */
	public Integer getUpdator() {
		return updator;
	}
	/**
	 * @param updator the updator to set
	 */
	public void setUpdator(Integer updator) {
		this.updator = updator;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getSuspendFlag() {
		return suspendFlag;
	}

	public void setSuspendFlag(Integer suspendFlag) {
		this.suspendFlag = suspendFlag;
	}
}
