package com.dooioo.ereceipt.boc.sign.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dooioo.base.EReceiptBaseService;
import com.dooioo.ereceipt.boc.BocGlobal;
import com.dooioo.ereceipt.boc.sign.service.IBocSignService;
import com.dooioo.util.Assert;
import com.dooioo.util.Configuration;
import com.dooioo.util.HttpClientUtil;
import com.dooioo.util.XmlUtils;
import static com.dooioo.ereceipt.boc.BocGlobal.token;

/**
 * BOC 签到服务
 * @author dingxin
 *
 */
@Service
@Scope(value = "singleton")
public class BocSignService extends EReceiptBaseService implements IBocSignService{
	
	private static Log logger 			= LogFactory.getLog(BocSignService.class);
	
	@Override
	public void sign() {
		synchronized (token) {
			String dirPath = this.getClass().getResource("/").getPath();
			Document document = XmlUtils.loadDocument(dirPath+BocGlobal.BOC_XML_SIGN_SOURCE);
			Assert.notNull(document, "load boc sign xml failed!");
			String xml = XmlUtils.xml2String(document, BocGlobal.BOC_CHARSET);
			Configuration configuration = Configuration.getInstance();
			String result = HttpClientUtil.sendXml(configuration.getString(BocGlobal.BOC_SIGN_URL), xml, BocGlobal.BOC_CHARSET);
			Assert.notNull(result, "sign boc failed! result is null!");
			Document outDocument = XmlUtils.string2Xml(result);
			NodeList codeNodeList = outDocument.getElementsByTagName(BocGlobal.BOC_RESPONSE_CODE);
			NodeList messageNodeList = outDocument.getElementsByTagName(BocGlobal.BOC_RESPONSE_MESSAGE);
			NodeList tokenNodeList = outDocument.getElementsByTagName(BocGlobal.BOC_TOKEN);
			Assert.notNull(new Object[]{codeNodeList,messageNodeList,tokenNodeList}, 
					new String[]{"not found code node from sign result xml!",
							"not found message node from sign result xml!",
							"not found token node from sign result xml!"});
			org.w3c.dom.Node tokenNode = tokenNodeList.item(0);
			org.w3c.dom.Node codeNode = codeNodeList.item(0);
			org.w3c.dom.Node messageNode = messageNodeList.item(0);
			Assert.notNull(new Object[]{tokenNode
					,codeNode
					,messageNode}, 
					new String[]{"token node is null!"
					,"code node is null!"
					,"message node is null!"});
			Assert.notNull(new Object[]{tokenNode.getTextContent()
					,codeNode.getTextContent()
					,messageNode.getTextContent()}, 
					new String[]{"token is null!"
					,"code is null!"
					,"message is null!"});
			try {
				Assert.notTrue(!BocGlobal.BOC_RESPONSE_CODE_OK.equals(codeNode.getTextContent()),
						"sign failed! response code is ["+codeNode.getTextContent()+"],"
								+ "response message is ["+new String(messageNode.getTextContent().getBytes(),BocGlobal.BOC_CHARSET)+"]");
			} catch (DOMException | UnsupportedEncodingException e) {
				logger.info(e.getMessage());
			}
			BocGlobal.token = tokenNode.getTextContent();
			try {
				Thread.sleep(BocGlobal.BOC_DEFAULT_DELAY);
			} catch (InterruptedException e) {
				logger.info(e.getMessage(),e);
			}
		}
	}

}
