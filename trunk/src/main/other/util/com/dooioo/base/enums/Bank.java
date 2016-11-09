package com.dooioo.base.enums;

/**
 * 银行
 * @author dingxin
 *
 */
public enum Bank {

	/**
	 * 中国银行
	 */
	BOC("boc","BOC_NET","中国银行","业务编号：0000([0-9a-zA-Z]{8})"),
	/**
	 * 中国工商银行
	 */
	ICBC("icbc","ICBC_CMP","中国工商银行","")
	;
	
	private String bank;
	private String bankCode;
	private String bankName;
	private String regex;
	
	private Bank(String bank,String bankCode,String bankName,String regex) {
		this.bank = bank;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.regex = regex;
	}

	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	
	
	
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * 根据bankCode 获得银行enum
	 * @param bankCode
	 * @return
	 */
	public static Bank parseBankCode(String bankCode){
		Bank[] banks = Bank.values();
		for(Bank bank:banks){
			if(bank.getBankCode().equals(bankCode)){
				return bank;
			}
		}
		return null;
	}
	
}
