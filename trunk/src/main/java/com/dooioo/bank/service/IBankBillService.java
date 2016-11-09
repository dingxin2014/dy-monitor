package com.dooioo.bank.service;

import com.dooioo.bank.model.BankBill;

import java.util.Date;
import java.util.List;

public interface IBankBillService {

	
	int save(BankBill bankBill);

	/**
	 * get bank billList from bank db
	 * @param date
	 * @return
	 */
	List<BankBill> getBankBillList(Date date);

	List<BankBill> getBankBillList(Date date, String bankCode);

}
