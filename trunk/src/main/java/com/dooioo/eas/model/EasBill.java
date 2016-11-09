package com.dooioo.eas.model;


import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dooioo.base.EReceiptBaseModel;

/**
 * 金蝶单据
 * @author dingxin
 *
 */
public class EasBill extends EReceiptBaseModel{

	private String easSerialNumber;
	private String easBillNumber;
	private String billNumber;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
