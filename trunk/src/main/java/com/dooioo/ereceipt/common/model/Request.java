package com.dooioo.ereceipt.common.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dooioo.base.EReceiptBaseModel;

import jersey.repackaged.com.google.common.base.Objects;

/**
 * Request
 * @author dingxin
 *
 */
public class Request extends EReceiptBaseModel{

	private String customNo;
	private String bankCode;
	private String payBankAccount;
	private String payBankAccountName;
	private String receiptBankAccount;
	private String receiptBankAccountName;
	private String respCode;
	private String respMessage;
	private String respFileName;
	private Integer flag;
	private Integer failTimes;
	private Date fromDate;
	private Date toDate;
	private int downloadFlag;
	private Integer downloadFailTimes;
	private Integer creator;
	private Date createTime;
	
	private List<Bill> billList;
	
	
	/**
	 * @return the customNo
	 */
	public String getCustomNo() {
		return customNo;
	}
	/**
	 * @param customNo the customNo to set
	 */
	public void setCustomNo(String customNo) {
		this.customNo = customNo;
	}

	
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the payBankAccount
	 */
	public String getPayBankAccount() {
		return payBankAccount;
	}
	/**
	 * @param payBankAccount the payBankAccount to set
	 */
	public void setPayBankAccount(String payBankAccount) {
		this.payBankAccount = payBankAccount;
	}
	/**
	 * @return the receiptBankAccount
	 */
	public String getReceiptBankAccount() {
		return receiptBankAccount;
	}
	/**
	 * @param receiptBankAccount the receiptBankAccount to set
	 */
	public void setReceiptBankAccount(String receiptBankAccount) {
		this.receiptBankAccount = receiptBankAccount;
	}
	/**
	 * @return the flag
	 */
	public Integer getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	/**
	 * @return the failTimes
	 */
	public Integer getFailTimes() {
		return failTimes;
	}
	/**
	 * @param failTimes the failTimes to set
	 */
	public void setFailTimes(Integer failTimes) {
		this.failTimes = failTimes;
	}
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * @return the respMessage
	 */
	public String getRespMessage() {
		return respMessage;
	}
	/**
	 * @param respMessage the respMessage to set
	 */
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	/**
	 * @return the respFileName
	 */
	public String getRespFileName() {
		return respFileName;
	}
	/**
	 * @param respFileName the respFileName to set
	 */
	public void setRespFileName(String respFileName) {
		this.respFileName = respFileName;
	}
	/**
	 * @return the downloadFlag
	 */
	public int getDownloadFlag() {
		return downloadFlag;
	}
	/**
	 * @param downloadFlag the downloadFlag to set
	 */
	public void setDownloadFlag(int downloadFlag) {
		this.downloadFlag = downloadFlag;
	}
	
	/**
	 * @return the payBankAccountName
	 */
	public String getPayBankAccountName() {
		return payBankAccountName;
	}
	/**
	 * @param payBankAccountName the payBankAccountName to set
	 */
	public void setPayBankAccountName(String payBankAccountName) {
		this.payBankAccountName = payBankAccountName;
	}
	/**
	 * @return the receiptBankAccountName
	 */
	public String getReceiptBankAccountName() {
		return receiptBankAccountName;
	}
	/**
	 * @param receiptBankAccountName the receiptBankAccountName to set
	 */
	public void setReceiptBankAccountName(String receiptBankAccountName) {
		this.receiptBankAccountName = receiptBankAccountName;
	}
	/**
	 * @return the downloadFailTimes
	 */
	public Integer getDownloadFailTimes() {
		return downloadFailTimes;
	}
	/**
	 * @param downloadFailTimes the downloadFailTimes to set
	 */
	public void setDownloadFailTimes(Integer downloadFailTimes) {
		this.downloadFailTimes = downloadFailTimes;
	}
	
	/**
	 * @return the billList
	 */
	public List<Bill> getBillList() {
		return billList;
	}
	/**
	 * @param billList the billList to set
	 */
	public void setBillList(List<Bill> billList) {
		this.billList = billList;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		if(obj instanceof Request){
			Request other = (Request)obj;
			if(!Objects.equal(this.bankCode, other.getBankCode()))
				return false;
			if(!Objects.equal(this.payBankAccount, other.getPayBankAccount()))
				return false;
			if(!Objects.equal(this.payBankAccountName, other.getPayBankAccountName()))
				return false;
			if(!Objects.equal(this.receiptBankAccount, other.getReceiptBankAccount()))
				return false;
			if(!Objects.equal(this.receiptBankAccountName, other.getReceiptBankAccountName()))
				return false;
			if(!Objects.equal(this.fromDate, other.getFromDate()))
				return false;
			if(!Objects.equal(this.toDate, other.getToDate()))
				return false;
			if(!Objects.equal(this.flag, other.getFlag()))
				return false;
			if(!Objects.equal(this.failTimes, other.getFailTimes()))
				return false;
			if(!Objects.equal(this.respCode, other.getRespCode()))
				return false;
			if(!Objects.equal(this.respFileName, other.getRespFileName()))
				return false;
			if(!Objects.equal(this.respMessage, other.getRespMessage()))
				return false;
		}else
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
