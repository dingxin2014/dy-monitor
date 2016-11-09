package com.dooioo.ereceipt.common.dao;

import org.springframework.stereotype.Repository;

import com.dooioo.ereceipt.common.model.Upload;

@Repository
public interface UploadMapper {

	int save(Upload upload);

	Upload queryUploadByBillId(int billId);

	Upload findByBillId(int id);

}
