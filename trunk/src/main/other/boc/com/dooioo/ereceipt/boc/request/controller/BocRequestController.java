package com.dooioo.ereceipt.boc.request.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dooioo.base.EReceiptBaseController;
import com.dooioo.ereceipt.boc.request.service.IBocRequestService;
import com.dooioo.ereceipt.common.model.Request;

@RestController
@RequestMapping(value = "/bocRequest")
public class BocRequestController extends EReceiptBaseController {

	@Autowired
	private IBocRequestService bocRequestService;
	
	@RequestMapping(value = "/")
	public Json request(){
		Date now = Calendar.getInstance().getTime();
		Request request = new Request();
//		bocRequestService.request(new Request[]{request});
		return ok;
	}
}
