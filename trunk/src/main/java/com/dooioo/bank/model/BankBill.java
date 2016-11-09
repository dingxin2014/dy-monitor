package com.dooioo.bank.model;

import com.dooioo.base.EReceiptBaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author dingxin
 *
 */
public class BankBill extends EReceiptBaseModel {

	private String payInfoId;   
	private String bankCode;
	private BigDecimal amount;
	private String easSerialNumber;
	private Date payInfoCreateTime;
	private String explanation;
	private String payBankAccount;
	private String payBankAccountName;
	private String payBankName;
	private String receiptBankAccount;
	private String receiptBankAccountName;
	private String receiptBankName;
	private String serialNumber;
	private Date payFinishTime;
	private String bankStatus;
	private String bankMessage;


	/**
	 * @return the payInfoId
	 */
	public String getPayInfoId() {
		return payInfoId;
	}
	/**
	 * @param payInfoId the payInfoId to set
	 */
	public void setPayInfoId(String payInfoId) {
		this.payInfoId = payInfoId;
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
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the easSerialNumber
	 */
	public String getEasSerialNumber() {
		return easSerialNumber;
	}
	/**
	 * @param easSerialNumber the easSerialNumber to set
	 */
	public void setEasSerialNumber(String easSerialNumber) {
		this.easSerialNumber = easSerialNumber;
	}

	/**
	 * @return the payInfoCreateTime
	 */
	public Date getPayInfoCreateTime() {
		return payInfoCreateTime;
	}
	/**
	 * @param payInfoCreateTime the payInfoCreateTime to set
	 */
	public void setPayInfoCreateTime(Date payInfoCreateTime) {
		this.payInfoCreateTime = payInfoCreateTime;
	}
	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}
	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
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
	 * @return the receiptBankName
	 */
	public String getReceiptBankName() {
		return receiptBankName;
	}
	/**
	 * @param receiptBankName the receiptBankName to set
	 */
	public void setReceiptBankName(String receiptBankName) {
		this.receiptBankName = receiptBankName;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	 * @return the payBankName
	 */
	public String getPayBankName() {
		return payBankName;
	}
	/**
	 * @param payBankName the payBankName to set
	 */
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	/**
	 * @return the payFinishTime
	 */
	public Date getPayFinishTime() {
		return payFinishTime;
	}
	/**
	 * @param payFinishTime the payFinishTime to set
	 */
	public void setPayFinishTime(Date payFinishTime) {
		this.payFinishTime = payFinishTime;
	}
	/**
	 * @return the bankStatus
	 */
	public String getBankStatus() {
		return bankStatus;
	}
	/**
	 * @param bankStatus the bankStatus to set
	 */
	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}
	/**
	 * @return the bankMessage
	 */
	public String getBankMessage() {
		return bankMessage;
	}
	/**
	 * @param bankMessage the bankMessage to set
	 */
	public void setBankMessage(String bankMessage) {
		this.bankMessage = bankMessage;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
