package com.dooioo.ereceipt.common.service;

import com.dooioo.ereceipt.common.model.Upload;

public interface IUploadService {

	int save(Upload upload);

	String uploadFile(String fileName, byte[] bytes);

	Upload findByBillId(int id);

}
