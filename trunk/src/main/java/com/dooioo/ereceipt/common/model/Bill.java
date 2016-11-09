package com.dooioo.ereceipt.common.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dooioo.bank.model.BankBill;

/**
 * Bill from eas & bank
 * <p>Bill extends BankBill & easBill at the same time</p>
 * @author dingxin
 *
 */
public class Bill extends BankBill{

	private String easSerialNumber;
	private String easBillNumber;
	private String billNumber;
	private Integer flag;   // -1/0/1
	private Integer requestId;
	private Integer id;
	private String virtualPath;
	
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
	 * @return the easBillNumber
	 */
	public String getEasBillNumber() {
		return easBillNumber;
	}
	/**
	 * @param easBillNumber the easBillNumber to set
	 */
	public void setEasBillNumber(String easBillNumber) {
		this.easBillNumber = easBillNumber;
	}
	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}
	/**
	 * @param billNumber the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
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
	 * @return the requestId
	 */
	public Integer getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
}
