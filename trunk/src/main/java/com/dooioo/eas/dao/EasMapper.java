package com.dooioo.eas.dao;

import org.springframework.stereotype.Repository;

import com.dooioo.eas.model.EasBill;

@Repository
public interface EasMapper {

	
	EasBill getEasPayBill(String easSerialNumber);
	
}
