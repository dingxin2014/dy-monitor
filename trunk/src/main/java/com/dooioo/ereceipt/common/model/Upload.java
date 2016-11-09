package com.dooioo.ereceipt.common.model;

import java.util.Date;

import com.dooioo.base.EReceiptBaseModel;

/**
 * 
 * @author dingxin
 *
 */
public class Upload extends EReceiptBaseModel{

	private int billId;
	private int requestId;
	private String fileName;
	private String virtualPath;
	private Integer uploadFlag;
	private Integer creator;
	private Date createTime;
	private Integer updator;
	private Date updateTime;
	/**
	 * @return the billId
	 */
	public int getBillId() {
		return billId;
	}
	/**
	 * @param billId the billId to set
	 */
	public void setBillId(int billId) {
		this.billId = billId;
	}
	/**
	 * @return the requestId
	 */
	public int getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the virtualPath
	 */
	public String getVirtualPath() {
		return virtualPath;
	}
	/**
	 * @param virtualPath the virtualPath to set
	 */
	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
	/**
	 * @return the uploadFlag
	 */
	public Integer getUploadFlag() {
		return uploadFlag;
	}
	/**
	 * @param uploadFlag the uploadFlag to set
	 */
	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
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
	
	
	
}
