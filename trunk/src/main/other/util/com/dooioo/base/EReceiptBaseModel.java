package com.dooioo.base;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author dingxin
 *
 */
public abstract class EReceiptBaseModel {

	
	private Integer id;
	
	private Integer deleteFlag;
	
	private Integer creator;
	
	private Date createTime;
	
	private Integer updator;
	
	private Date updateTime;
	

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

	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
	
	
	
}
