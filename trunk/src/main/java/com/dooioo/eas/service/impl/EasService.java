package com.dooioo.eas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.eas.dao.EasMapper;
import com.dooioo.eas.model.EasBill;
import com.dooioo.eas.service.IEasService;

@Service
public class EasService extends EReceiptBaseService implements IEasService{

	@Autowired
	private EasMapper easMapper;
	
	@Override
	public EasBill getEasPayBill(String easSerialNumber) {
		return easMapper.getEasPayBill(easSerialNumber);
	}

	
}
