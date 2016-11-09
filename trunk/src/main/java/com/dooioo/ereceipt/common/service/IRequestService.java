package com.dooioo.ereceipt.common.service;

import java.util.List;
import java.util.Map;

import com.dooioo.ereceipt.common.model.Request;

public interface IRequestService {

	public int save(Request request);

	int update(Request request);

	List<Request> getRequests(Map<String, Object> map);

	List<Request> getRequestList(Map<String, Object> map);
}
