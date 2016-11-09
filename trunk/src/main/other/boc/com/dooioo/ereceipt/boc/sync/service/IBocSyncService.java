package com.dooioo.ereceipt.boc.sync.service;

import com.dooioo.base.handler.EreceiptHandler.HandlerCallBack;
import com.dooioo.ereceipt.common.model.Request;

public interface IBocSyncService {

	public void sync(Request[] requests, HandlerCallBack<String,Object> callBack);
}
