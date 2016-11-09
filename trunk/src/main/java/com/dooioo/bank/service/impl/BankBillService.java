package com.dooioo.bank.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dooioo.bank.dao.BankBillMapper;
import com.dooioo.bank.model.BankBill;
import com.dooioo.bank.service.IBankBillService;
import com.dooioo.base.AppBaseService;

@Service
@Transactional
public class BankBillService extends  AppBaseService implements IBankBillService{

	@Autowired
	private BankBillMapper bankBillMapper;
	
	@Override
	public List<BankBill> getBankBillList(Date date) {
		return getBankBillList(date, null);
	}
	
	@Override
	public List<BankBill> getBankBillList(Date date,String bankCode) {
		Map<String,Object> params = new HashMap<String, Object>(2);
		params.put("myDate", date);
		params.put("bankCode", bankCode);
		return bankBillMapper.getBankBillList(params);
	}

	@Override
	public int save(BankBill bankBill){
		return bankBillMapper.save(bankBill);
	}

}
