package com.dooioo.ereceipt.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.ereceipt.common.dao.RequestMapper;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.ereceipt.common.service.IRequestService;

/**
 * 
 * @author dingxin
 *
 */
@Service
public class RequestService extends EReceiptBaseService implements IRequestService{
	
	@Autowired
	private RequestMapper requestMapper;
	
	@Override
	public int save(Request request) {
		return requestMapper.save(request);
	}
	
	@Override
	public int update(Request request){
		return requestMapper.update(request);
	}
	
	@Override
	public List<Request> getRequests(Map<String,Object> map){
		return requestMapper.getRequests(map);
	}
	
	@Override
	public List<Request> getRequestList(Map<String,Object> map){
		return requestMapper.getRequestList(map);
	}
	
}
