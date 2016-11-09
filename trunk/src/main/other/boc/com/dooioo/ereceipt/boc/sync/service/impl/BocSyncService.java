package com.dooioo.ereceipt.boc.sync.service.impl;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dooioo.base.handler.EreceiptHandler.HandlerCallBack;
import com.dooioo.ereceipt.boc.BocGlobal;
import com.dooioo.ereceipt.boc.sync.service.IBocSyncService;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.ftp.IFtpClient;
import com.dooioo.ftp.SftpClient;
import com.dooioo.util.Assert;
import com.dooioo.util.Constants;

/**
 * 同步PDF文件 
 * @author dingxin
 *
 */
@Service
@Scope(value = "singleton")
public class BocSyncService implements IBocSyncService {

	private static Log logger = LogFactory.getLog(BocSyncService.class);
	
	@Override
	public void sync(Request[] requests, HandlerCallBack<String,Object> callBack) {
		String compressPath = (String) callBack.get(Constants.ORIGIN_FILE_PATH);
		try (IFtpClient sftpClient = new SftpClient(BocGlobal.BOC_SFTP_HOST, BocGlobal.BOC_SFTP_PORT, BocGlobal.BOC_SFTP_USERNAME, BocGlobal.BOC_SFTP_PWD)){
			for(Request request: requests){
				try {
					Assert.notNull(request,"request must not be null!");
					Assert.notNull(request.getRespFileName(), "fileName must not be null!");
					sftpClient.download(request.getRespFileName(), compressPath+File.separator+request.getRespFileName());
					callBack.success(request, compressPath+File.separator+request.getRespFileName());
				} catch (Exception e) {
					logger.info(e.getMessage(),e);
					callBack.fail(request, e);
				}
				callBack.complete(request, null);
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
	}

}
