package com.dooioo.ereceipt.common.controller;

import com.dooioo.base.AppBaseController;
import com.dooioo.ereceipt.common.service.IBillService;
import com.dooioo.json.JsonUtil;
import com.dooioo.web.annotation.AuthRef;
import com.dooioo.web.common.Paginate;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/bill")
public class BillController extends AppBaseController {

    @Autowired
    private IBillService billService;

    @ResponseBody
    @RequestMapping(value = "/paginateBankBillList")
    @AuthRef(ref={"ereceipt_list"})
    public JSONObject paginateBankBillList(HttpServletRequest request, HttpServletResponse response,
                                           String bankCode, String billNumber, String payDateFrom, String payDateTo,
                                           String serialNumber, String receiptBankAccountName, String receiptBankAccount
    ){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("bankCode", bankCode);
        map.put("billNumber", billNumber);
        map.put("payDateFrom", payDateFrom);
        map.put("payDateTo", payDateTo);
        map.put("serialNumber", serialNumber);
        map.put("receiptBankAccountName", receiptBankAccountName);
        map.put("receiptBankAccount", receiptBankAccount);
        Paginate paginate = billService.paginateBankBillList(map,this.getPageIndex(),this.getPageSize());
        return this.ok("ok",JSONObject.fromObject(paginate, JsonUtil.getJsonConfig()) );
    }
}
