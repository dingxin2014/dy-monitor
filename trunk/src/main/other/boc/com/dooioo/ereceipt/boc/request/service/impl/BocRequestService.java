package com.dooioo.ereceipt.boc.request.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.base.handler.EreceiptHandler.HandlerCallBack;
import com.dooioo.ereceipt.boc.BocGlobal;
import com.dooioo.ereceipt.boc.request.service.IBocRequestService;
import com.dooioo.ereceipt.boc.sign.service.IBocSignService;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.util.Assert;
import com.dooioo.util.Configuration;
import com.dooioo.util.HttpClientUtil;
import com.dooioo.util.XmlUtils;
import static com.dooioo.util.AppUtils.isnull;
import static com.dooioo.util.AppUtils.getUsercode;
import static com.dooioo.ereceipt.boc.BocGlobal.token;

/**
 * boc request
 * @author dingxin
 *
 */
@Service
@Scope("singleton")
@Transactional
public class BocRequestService extends EReceiptBaseService implements IBocRequestService{

	
	private static Log logger = LogFactory.getLog(BocRequestService.class);

	private static final String NODELIST 		 = "nodeList";
	private static final String DOCUMENT		 = "document";
	
	@Autowired
	private IBocSignService bocSignService;
	
	@Override
	public Request[] request(Request[] requests, HandlerCallBack<String,Object> callBack){
		Assert.notNull(new Object[]{requests ,callBack}, new String[]{"requests must not be null!", "callBack must not be null!"});
		synchronized (token) {
			if(StringUtils.isEmpty(token))
				bocSignService.sign();
			DateFormat dateFormat = new SimpleDateFormat(BocGlobal.BOC_DATEFORMAT);
			Configuration configuration = Configuration.getInstance();
			Map<String,Object> xmlMap = getRequestXmlTemplate();;
			NodeList childNodeList = (NodeList) xmlMap.get(NODELIST);
			Document document = (Document) xmlMap.get(DOCUMENT);
			Calendar calendar = Calendar.getInstance();
			for(Request request :requests){
				if(request == null || Integer.valueOf(1) == request.getFlag()
						|| Integer.valueOf(1) == request.getDownloadFlag()){
					continue;
				}
				Assert.notNull(new Object[]{request.getFromDate(), request.getToDate()}, new String[]{"fromDate must not be null!","toDate must not be null!"});
				String uuid = request.getCustomNo();
				String __from__ = dateFormat.format(request.getFromDate());
				String __to__ = dateFormat.format(request.getToDate());
				Assert.notNull(new Object[]{request.getId(),uuid,__from__,__to__,request.getPayBankAccount()}, 
						new String[]{"id must not be null!","uuid must not be null!",
								"fromDate must not be null!","toDate must not be null!","bankAccount must not be null!"});
				XmlUtils.setValue(childNodeList, 
						new String[]{BocGlobal.BOC_REQUEST_BODY_FROM,BocGlobal.BOC_REQUEST_BODY_TO,BocGlobal.BOC_REQUEST_BODY_INSID,BocGlobal.BOC_REQUEST_BODY_PAYACC,BocGlobal.BOC_REQUEST_BODY_RECEIPTACC}, 
						new String[]{__from__,__to__,uuid,request.getPayBankAccount(),request.getReceiptBankAccount()});
				String xml = XmlUtils.xml2String(document, BocGlobal.BOC_CHARSET);
				logger.info("generate request xml --->" + xml);
				String result = HttpClientUtil.sendXml(configuration.getString(BocGlobal.BOC_REQUEST_URL), xml, BocGlobal.BOC_CHARSET);
				Assert.notNull(result, "request boc failed! result is null!");
				Map<String,String> map = XmlUtils.getValue(result, new String[]{BocGlobal.BOC_RESPONSE_CODE,BocGlobal.BOC_RESPONSE_MESSAGE,BocGlobal.BOC_RESPONSE_FILENAME});
 				if(map != null && map.containsKey(BocGlobal.BOC_RESPONSE_CODE) && BocGlobal.BOC_RESPONSE_CODE_OK.equals(map.get(BocGlobal.BOC_RESPONSE_CODE))){
					String fileName = map.get(BocGlobal.BOC_RESPONSE_FILENAME);
					if(null == fileName)
						continue;
					request.setRespCode(map.get(BocGlobal.BOC_RESPONSE_CODE));
					request.setRespMessage(map.get(BocGlobal.BOC_RESPONSE_MESSAGE));
					callBack.success(request, fileName);
				}
				else if(map != null && map.containsKey(BocGlobal.BOC_RESPONSE_CODE) && Arrays.asList(BocGlobal.BOC_RESPONSE_NO_TOKEN.split(",")).contains(map.get(BocGlobal.BOC_RESPONSE_CODE))){
					bocSignService.sign();
					request.setFailTimes(isnull(request.getFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
					XmlUtils.setValue(document, new String[]{BocGlobal.BOC_TOKEN}, new String[]{token});
					xml = XmlUtils.xml2String(document, BocGlobal.BOC_CHARSET);
					result = HttpClientUtil.sendXml(configuration.getString(BocGlobal.BOC_REQUEST_URL), xml, BocGlobal.BOC_CHARSET);
					Assert.notNull(result, "request boc failed! result is null!");
					map = XmlUtils.getValue(result, new String[]{BocGlobal.BOC_RESPONSE_CODE,BocGlobal.BOC_RESPONSE_MESSAGE,BocGlobal.BOC_RESPONSE_FILENAME});
					if(map != null && map.containsKey(BocGlobal.BOC_RESPONSE_CODE) && BocGlobal.BOC_RESPONSE_CODE_OK.equals(map.get(BocGlobal.BOC_RESPONSE_CODE))){
						String fileName = map.get(BocGlobal.BOC_RESPONSE_FILENAME);
						if(StringUtils.isEmpty(fileName))
							continue;
						request.setRespCode(map.get(BocGlobal.BOC_RESPONSE_CODE));
						request.setRespMessage(map.get(BocGlobal.BOC_RESPONSE_MESSAGE));
						callBack.success(request, fileName);
					}else{
						request.setRespCode(map.get(BocGlobal.BOC_RESPONSE_CODE));
						request.setRespMessage(map.get(BocGlobal.BOC_RESPONSE_MESSAGE));
						String message = map.containsKey(BocGlobal.BOC_RESPONSE_MESSAGE) ? map.get(BocGlobal.BOC_RESPONSE_MESSAGE) : "null";
						logger.info("bankAccount "+request.getPayBankAccount()+" request failed! message is ["+message+"]");
						callBack.fail(request, "response code is not OK!");
					}
				}
				else{
					request.setRespCode(map.get(BocGlobal.BOC_RESPONSE_CODE));
					request.setRespMessage(map.get(BocGlobal.BOC_RESPONSE_MESSAGE));
					String message = map.containsKey(BocGlobal.BOC_RESPONSE_MESSAGE) ? map.get(BocGlobal.BOC_RESPONSE_MESSAGE) : "null";
					logger.info("bankAccount "+request.getPayBankAccount()+" request failed! message is ["+message+"]");
					callBack.fail(request, "response code is not OK!");
				}
				
				request.setCreateTime(calendar.getTime());
				request.setCreator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
				callBack.complete(request, null);
			}
		}
		return requests;
	}
	
	/**
	 * get request xml template
	 * @return
	 */
	private Map<String,Object> getRequestXmlTemplate(){
		String dirPath = this.getClass().getResource("/").getPath();
		Document document = XmlUtils.loadDocument(dirPath+BocGlobal.BOC_XML_REQUEST_SOURCE);
		Assert.notNull(document, "load request xml failed!");
		NodeList nodeList = document.getElementsByTagName(BocGlobal.BOC_TOKEN);
		Assert.notNull(nodeList, "error boc request xml template!");
		org.w3c.dom.Node tokenNode = nodeList.item(0);
		tokenNode.setTextContent(token);
		NodeList body = document.getElementsByTagName(BocGlobal.BOC_REQUEST_BODY_TAG);
		NodeList bodyNodeList = document.getElementsByTagName(BocGlobal.BOC_REQUEST_BODY_TRN);
		org.w3c.dom.Node tranNodes = body.item(0);
		org.w3c.dom.Node tranNode = bodyNodeList.item(0);
		Assert.notNull(new Object[]{tranNodes, tranNode}, new String[]{"error boc request xml template!","error boc request xml template!"});
		NodeList childNodeList = tranNodes.getChildNodes();
		Map<String,Object> map = new HashMap<String, Object>(2);
		map.put(NODELIST, childNodeList);
		map.put(DOCUMENT, document);
		return map;
	}
	
}
