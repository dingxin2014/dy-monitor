package com.dooioo.ereceipt.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.ereceipt.common.dao.UploadMapper;
import com.dooioo.ereceipt.common.model.Upload;
import com.dooioo.ereceipt.common.service.IUploadService;
import com.dooioo.fs.facade.Category;
import com.dooioo.fs.facade.IFsFacade;
import com.dooioo.fs.facade.UploadRequest;
import com.dooioo.util.Configuration;

/**
 * 
 * @author dingxin
 *
 */
@Service
public class UploadService extends EReceiptBaseService implements IUploadService {

	@Autowired
	private UploadMapper uploadMapper;
	@Autowired
	private IFsFacade iFsFacade;
	
	@Override
	public int save(Upload upload){
		return uploadMapper.save(upload);
	}
	
	@Override
	public String uploadFile(String fileName,byte[] bytes){
		UploadRequest uploadRequest = new UploadRequest();
		uploadRequest.setCategory(Category.OTHER);
		uploadRequest.setDomain(Configuration.getInstance().getAppCode());
		uploadRequest.setFileName(fileName);
		uploadRequest.setFileprops(null);
		uploadRequest.setRemark("电子回单系统pdf上传");
		com.dooioo.fs.facade.UploadResult newResult = iFsFacade.upload(bytes, uploadRequest);
		if(newResult.getState() == com.dooioo.fs.facade.UploadResult.STATE_FAIL){
			return null;
		}
		return newResult.getVirtualPath();
	}
	
	@Override
	public Upload findByBillId(int id){
		return uploadMapper.findByBillId(id);
	}

}
