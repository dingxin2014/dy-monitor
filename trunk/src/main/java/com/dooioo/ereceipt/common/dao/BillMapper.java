package com.dooioo.ereceipt.common.dao;

import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.model.Request;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BillMapper {

	void batchSave(Map<String, Object> map);
	
	
	int save(Bill bill);


	int update(Bill bill);


	List<Bill> getBillList(Map<String, Object> map);


	int existBill(String easSerialNumber);


	List<Bill> getBillListByAccount(Map<String, Object> params);


	Bill getBillBySerialNumber(String serialNumber);


	List<Bill> queryBankBillList(Map<String,Object> map,PageBounds pageBounds);


	List<Request> billDistinctToRequest(Map<String, Object> map);


	Bill getBillByEasBillNumber(String easBillNumber);
}
