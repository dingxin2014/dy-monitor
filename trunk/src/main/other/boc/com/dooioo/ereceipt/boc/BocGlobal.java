package com.dooioo.ereceipt.boc;

import com.dooioo.util.Configuration;

/**
 * 全局变量
 * @author dingxin
 *
 */
public class BocGlobal {

	/**
	 * global variable
	 * <code>token</code> of boc.
	 */
	public static String token = new String("");
	
	public static final String BOC_XML_SIGN_SOURCE     		= "template/xml/"+Configuration.getInstance().getEnv()+".boc.sign.xml";
	public static final String BOC_XML_REQUEST_SOURCE		= "template/xml/"+Configuration.getInstance().getEnv()+".boc.request.xml";
	public static final String BOC_CHARSET 	   				= "UTF-8";
	public static final String BOC_SIGN_URL					= "boc.sign.url";
	public static final String BOC_REQUEST_URL				= "boc.request.url";
	public static final String BOC_TOKEN					= "token";
	public static final String BOC_RESPONSE_CODE 			= "rspcod";
	public static final String BOC_RESPONSE_MESSAGE			= "rspmsg";
	public static final String BOC_RESPONSE_CODE_OK 		= "B001";
	public static final String BOC_RESPONSE_NO_TOKEN		= "B107,1022";
	public static final String BOC_RESPONSE_RS				= "b2e0500-rs";
	public static final String BOC_RESPONSE_FILENAME		= "filename";
	public static final String BOC_REQUEST_BODY_TAG     	= "trans";
	public static final String BOC_REQUEST_BODY_TRN			= "trn-b2e0500-rq";
	public static final String BOC_REQUEST_BODY_FROM		= "from";      
	public static final String BOC_REQUEST_BODY_TO			= "to";
	public static final String BOC_REQUEST_BODY_INSID   	= "insid";
	public static final String BOC_REQUEST_BODY_PAYACC		= "actacn";
	public static final String BOC_REQUEST_BODY_RECEIPTACC  = "transact";
	public static final String BOC_DATEFORMAT 				= "yyyyMMdd";
	public static final Integer BOC_DEFAULT_DELAY			= 5000;
	
	public static final String BOC_SFTP_HOST 				= Configuration.getInstance().getProperty("boc.sftp.host");
	public static final Integer BOC_SFTP_PORT 				= Integer.valueOf(Configuration.getInstance().getProperty("boc.sftp.port"));
	public static final String BOC_SFTP_USERNAME 			= Configuration.getInstance().getProperty("boc.sftp.username");
	public static final String BOC_SFTP_PWD 				= Configuration.getInstance().getProperty("boc.sftp.password");
}
