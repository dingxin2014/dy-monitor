/**
 * 
 */
package com.dooioo.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.dooioo.ereceipt.exception.EReceiptException;


/**
 * @author liguohui
 *
 */
public class HttpClientUtil {

	private static Log logger = LogFactory.getLog(HttpClientUtil.class);
	
	public static String sendXml(String url,String xml,String charset) {
		logger.debug("url:"+url+"\tcharset:"+charset+"\ncontent:"+xml);
		if(charset == null){
			charset = Consts.ISO_8859_1.toString();
		}
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost method = new HttpPost(url);
		ContentType contentType = ContentType.create("text/xml", charset);
		HttpEntity entity = new StringEntity(xml, contentType);
		method.setEntity(entity);
		try{
			HttpResponse response = client.execute(method);
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity responseEntity = response.getEntity();
				String rs = IOUtils.readFully(responseEntity.getContent(), charset);
				logger.debug("response url："+url+"\nresponseBody:"+rs);
				return rs;
			}
			throw new EReceiptException("请求失败："+url+"\tstatusCode:"+status.getStatusCode());
		}catch(Exception e){
			logger.fatal(e.getMessage(),e);
			throw new EReceiptException(e.getMessage(),e);
		}
	}
	
}
