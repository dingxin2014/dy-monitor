package com.dooioo.ereceipt.common.model;

import java.util.Date;

/**
 * Bill from eas & bank
 * <p>Bill extends BankBill & easBill at the same time</p>
 * @author dingxin
 *
 */
public class AccessSetting {
    /**主键id*/
	private Integer id;
	/**IP地址,用,隔开*/
	private String ipAddress;
	/**appCode*/
	private String appCode;
	/**appName*/
	private String appName;
	/**备注*/
	private String remark;
	/**删除标识1删除0未删除*/
	private int deleteFlag;
	/**状态1有效0无效*/
	private int status;
	/**创建人*/
	private int creator;
	/**创建时间*/
	private Date createTime;
	/**修改人*/
	private int updator;
	/**修改时间*/
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getUpdator() {
		return updator;
	}

	public void setUpdator(int updator) {
		this.updator = updator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
