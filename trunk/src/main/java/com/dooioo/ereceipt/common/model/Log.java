package com.dooioo.ereceipt.common.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dooioo.base.EReceiptBaseModel;

public class Log extends EReceiptBaseModel{

	private String operate;
	private String result;
	private String remark;
	private Integer operator;
	private Date operateTime;
	/**
	 * @return the operate
	 */
	public String getOperate() {
		return operate;
	}
	/**
	 * @param operate the operate to set
	 */
	public void setOperate(String operate) {
		this.operate = operate;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the operator
	 */
	public Integer getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	/**
	 * @return the operateTime
	 */
	public Date getOperateTime() {
		return operateTime;
	}
	/**
	 * @param operateTime the operateTime to set
	 */
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
}
