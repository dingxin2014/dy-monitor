package com.dooioo.ereceipt.common.service.impl;

import com.dooioo.base.AppBaseService;
import com.dooioo.ereceipt.common.dao.BillMapper;
import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.web.common.Paginate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author dingxin
 *
 */
@Service
public class BillService extends AppBaseService implements com.dooioo.ereceipt.common.service.IBillService{

	@Autowired
	private BillMapper billMapper;

	@Override
	public int save(Bill bill){
		return billMapper.save(bill);
	}

	@Override
	public void batchSave(List<Bill> list) {
		@SuppressWarnings("rawtypes")
		List<List> _list_ = pagingBatchOperationList(list,40);
		_list_.stream().forEach(_list -> {
			if(!CollectionUtils.isEmpty(_list)){
				Map<String, Object> map = new HashMap<String, Object>(1);  
				map.put("list", _list); 
				billMapper.batchSave(map);
			}
		});
	}
	
	@Override
	public int update(Bill bill){
		return billMapper.update(bill);
	}
	
	@Override
	public List<Bill> getBillList(Date from,Date to,int flag){
		Map<String,Object> map = new HashMap<>(3);
		map.put("fromPayTime", from);
		map.put("toPayTime", to);
		map.put("flag", flag);
		return billMapper.getBillList(map);
	}
	
	@Override
	public List<Request> billDistinctToRequest(Date from,Date to,int flag){
		Map<String,Object> map = new HashMap<>(3);
		map.put("fromPayTime", from);
		map.put("toPayTime", to);
		map.put("flag", flag);
		return billMapper.billDistinctToRequest(map);
	}
	
	@Override
	public boolean existBill(String easSerialNumber){
		return billMapper.existBill(easSerialNumber) > 0;
	}
	
	@Override
	public List<Bill> getBillListByAccount(Date from,Date to, int flag,String payBankAccount,String receiptBankAccount){
		Map<String,Object> params = new HashMap<>(5);
		params.put("fromPayTime", from);
		params.put("toPayTime", to);
		params.put("payBankAccount", payBankAccount);
		params.put("receiptBankAccount", receiptBankAccount);
		params.put("flag", flag);
		return billMapper.getBillListByAccount(params);
	}
	
	@Override
	public Bill getBillBySerialNumber(String serialNumber){
		return billMapper.getBillBySerialNumber(serialNumber);
	}
	
	@Override
	public Bill getBillByEasBillNumber(String easBillNumber){
		return billMapper.getBillByEasBillNumber(easBillNumber);
	}

	/**
	 * 查询电子回单 返回list
	 * @return
	 */
	public Paginate paginateBankBillList(Map<String,Object> map, int pageNo, int pageSize) {
		PageBounds pageBounds = new PageBounds(pageNo, pageSize);
		List<Bill> list = billMapper.queryBankBillList(map,pageBounds);
		Paginate paginate = createPaginate(pageNo, pageSize, list);
		return paginate;
	}

}
