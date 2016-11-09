package com.dooioo.ereceipt.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.ereceipt.common.dao.LogMapper;
import com.dooioo.ereceipt.common.model.Log;
import com.dooioo.ereceipt.common.service.ILogService;

@Service
public class LogService extends EReceiptBaseService implements ILogService{

	@Autowired
	private LogMapper logMapper;
	
	@Override
	public int log(Log log) {
		if(log.getRemark().length() > 2000)
			log.setRemark(log.getRemark().substring(0, 2000));
		return logMapper.save(log);
	}

	
}
