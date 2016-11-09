package com.dooioo.ereceipt.boc;

import com.dooioo.base.handler.EreceiptHandler;
import com.dooioo.context.util.SpringObjectFactory;
import com.dooioo.ereceipt.boc.request.service.IBocRequestService;
import com.dooioo.ereceipt.boc.sync.service.IBocSyncService;
import com.dooioo.ereceipt.common.model.Request;

/**
 * 
 * @author dingxin
 *
 */
public class BocHandler implements EreceiptHandler{

	private IBocRequestService bocRequestService;
	
	private IBocSyncService bocSyncService;
	
	public BocHandler() {
		bocRequestService = (IBocRequestService) SpringObjectFactory.getBean("bocRequestService");
		bocSyncService = (IBocSyncService) SpringObjectFactory.getBean("bocSyncService");
	}
	
	@Override
	public void sendRequest(Request[] requests,HandlerCallBack<String ,Object> callBack) {
		bocRequestService.request(requests,callBack);
	}

	@Override
	public void syncFile(Request[] requests, HandlerCallBack<String ,Object> callBack) {
		bocSyncService.sync(requests, callBack);
	}

	

}
