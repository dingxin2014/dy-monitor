package com.test.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dooioo.ereceipt.boc.sign.service.IBocSignService;
import com.test.base.BaseTest;

/**
 * test boc sign
 * @author dingxin
 *
 */
public class SignTest extends BaseTest{

	private static final Log logger = LogFactory.getLog(SignTest.class);
	@Autowired
	private IBocSignService bocSignService;
	
	@Test
	public void sign(){
		bocSignService.sign();
		logger.info("over");
	}
}
