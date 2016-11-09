package com.dooioo.ereceipt.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dooioo.ereceipt.common.model.Request;

@Repository
public interface RequestMapper {

	int save(Request request);

	int update(Request request);

	List<Request> getRequests(Map<String, Object> map);

	List<Request> getRequestList(Map<String, Object> map);

}
