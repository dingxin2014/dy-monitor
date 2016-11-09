package com.dooioo.bank.dao;


import com.dooioo.bank.model.BankBill;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BankBillMapper {

	List<BankBill> getBankBillList(Map<String, Object> params);

	int save(BankBill bankBill);

}
