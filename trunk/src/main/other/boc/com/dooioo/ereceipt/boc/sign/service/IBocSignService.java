package com.dooioo.ereceipt.boc.sign.service;

/**
 * 中国银行签到服务
 * @author dingxin
 *
 */
public interface IBocSignService {

	/**
	 * sign BOC & get Boc token
	 * @return
	 */
	public void sign();
	
}
