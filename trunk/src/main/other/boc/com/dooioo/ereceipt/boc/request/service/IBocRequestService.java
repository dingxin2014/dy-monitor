package com.dooioo.ereceipt.boc.request.service;

import com.dooioo.base.handler.EreceiptHandler.HandlerCallBack;
import com.dooioo.ereceipt.common.model.Request;

public interface IBocRequestService {

	Request[] request(Request[] requests, HandlerCallBack<String, Object> callBack);

}
