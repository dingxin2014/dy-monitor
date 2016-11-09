package com.dooioo.eas.service;

import com.dooioo.eas.model.EasBill;


public interface IEasService {

	
	/**
	 * 获取金蝶付款单
	 * @return
	 */
	EasBill getEasPayBill(String easSerialNumber);
	
}
