package com.dooioo.eas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dooioo.base.EReceiptBaseController;
import com.dooioo.eas.model.EasBill;
import com.dooioo.eas.service.IEasService;

@Controller
@ResponseBody
@RequestMapping(value = "/eas")
public class EasController extends EReceiptBaseController{

	@Autowired
	private IEasService easService;
	
	@RequestMapping(value = "/test")
	public Json test(String easSerialNumber){
		EasBill easBill = easService.getEasPayBill(easSerialNumber);
		return ok.put("bill",easBill);
	}
	
}
