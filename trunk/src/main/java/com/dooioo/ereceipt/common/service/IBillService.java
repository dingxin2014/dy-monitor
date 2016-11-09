package com.dooioo.ereceipt.common.service;

import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.web.common.Paginate;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IBillService {


    void batchSave(List<Bill> list);

    int save(Bill bill);

    int update(Bill bill);

    boolean existBill(String easSerialNumber);

    List<Bill> getBillList(Date from, Date to, int flag);

    List<Bill> getBillListByAccount(Date from, Date to, int flag, String payBankAccount, String receiptBankAccount);

    Bill getBillBySerialNumber(String serialNumber);

    /**
     * 查询电子回单 返回list
     *
     * @return
     */
    Paginate paginateBankBillList(Map<String, Object> map, int pageNo, int pageSize);

	List<Request> billDistinctToRequest(Date from, Date to, int flag);

	Bill getBillByEasBillNumber(String easBillNumber);

}
