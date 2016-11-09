package com.dooioo.ereceipt.common.dao;

import org.springframework.stereotype.Repository;

import com.dooioo.ereceipt.common.model.Log;

@Repository
public interface LogMapper {

	int save(Log log);
}
