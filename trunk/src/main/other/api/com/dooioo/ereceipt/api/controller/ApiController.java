package com.dooioo.ereceipt.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.dooioo.base.EReceiptBaseController;
import com.dooioo.ereceipt.api.service.IApiService;
import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.model.Upload;
import com.dooioo.ereceipt.common.service.IBillService;
import com.dooioo.ereceipt.common.service.IUploadService;
import com.dooioo.util.AppUtils;



@Controller
@RequestMapping(value = "/api")
public class ApiController extends EReceiptBaseController {


    private static final Log logger = LogFactory.getLog(ApiController.class);
    
    @Autowired
    private IApiService apiService;
    @Autowired
    private IBillService billService;
    @Autowired
    private IUploadService uploadService;
    
    @RequestMapping(value = "/ping")
    public Json index(HttpServletRequest request){
    	logger.info("ping success");
    	return ok;
    }
    
    @RequestMapping(value = "/getVirtualPath")
    public Json getVirtualPath(HttpServletRequest request, String easBillNumber){
    	Bill bill = billService.getBillByEasBillNumber(easBillNumber);
    	Upload upload = uploadService.findByBillId(bill.getId());
    	return ok.put("virtualPath", AppUtils.isnull(upload.getVirtualPath(),""));
    }



}