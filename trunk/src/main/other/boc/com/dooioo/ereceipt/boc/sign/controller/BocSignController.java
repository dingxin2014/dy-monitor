package com.dooioo.ereceipt.boc.sign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dooioo.base.EReceiptBaseController;
import com.dooioo.ereceipt.boc.BocGlobal;
import com.dooioo.ereceipt.boc.sign.service.IBocSignService;

@RestController
@RequestMapping(value = "/bocSign")
public class BocSignController extends EReceiptBaseController{

	@Autowired
	private IBocSignService bocSignService;
	
	@RequestMapping(value="/")
	public Json sign(){
		bocSignService.sign();
		return ok.put("token", BocGlobal.token);
	}
}
