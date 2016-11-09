package com.dooioo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dooioo.ereceipt.exception.EReceiptException;

/**
 * <p>w3c XmlUtils</p>
 * util for loading && write xml template
 * @author dingxin
 *
 */
public class XmlUtils {

	private static Log logger = LogFactory.getLog(XmlUtils.class);
	
	public static Document loadDocument(String source){
		File file=new File(source);  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db;  
        org.w3c.dom.Document document = null;
		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage(), e);
			throw new EReceiptException(e.getMessage(), e );
		} 
		return document;
	}
	
	public static String xml2String(Document document,String charset){
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, charset);
			transformer.transform(new DOMSource(document), new StreamResult(bos));
			return bos.toString();
		} catch (TransformerException e) {
			logger.error(e.getMessage(), e);
			throw new EReceiptException(e.getMessage(), e );
		}
	}
	
	public static Document string2Xml(String xml){
		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage(), e);
			throw new EReceiptException(e.getMessage(), e );
		}
		return document;
	}
	
	public static NodeList setValue(NodeList nodeList,String[] keys, String[] values){
		Assert.notNull(new Object[]{nodeList, keys, values}, new String[]{"NodeList is null!", "keys is null!", "values is null!"});
		Assert.notTrue(keys.length != values.length, "keys's length not equal values's length!");
		int length = nodeList.getLength();
		int size = keys.length;
		if(length == 0 || size == 0)
			return nodeList;
		Map<String,String> map = new HashMap<>(size);;
		for(int i = 0;i < size;i++){
			map.put(keys[i], values[i]);
		}
		for(int i = 0;i < length;i++){
			org.w3c.dom.Node node = nodeList.item(i);
			if(node != null && map.containsKey(node.getNodeName())){
				node.setTextContent(map.get(node.getNodeName()));
			}else if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				setValue(childNodes, keys, values);
			}
		}
		return nodeList;
	}
	
	public static Document setValue(Document document,String[] keys,String[] values){
		NodeList nodeList = document.getChildNodes();
		Assert.notNull(new Object[]{nodeList, keys, values}, new String[]{"NodeList is null!", "keys is null!", "values is null!"});
		Assert.notTrue(keys.length != values.length, "keys's length not equal values's length!");
		int length = nodeList.getLength();
		int size = keys.length;
		if(length == 0 || size == 0)
			return document;
		Map<String,String> map = new HashMap<>(size);;
		for(int i = 0;i < size;i++){
			map.put(keys[i], values[i]);
		}
		for(int i = 0;i < length;i++){
			org.w3c.dom.Node node = nodeList.item(i);
			if(node != null && map.containsKey(node.getNodeName())){
				node.setTextContent(map.get(node.getNodeName()));
			}else if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				setValue(childNodes, keys, values);
			}
		}
		return document;
	}
	
	public static String getValue(NodeList nodeList,String key){
		Assert.notNull(new Object[]{nodeList, key}, new String[]{"NodeList is null!", "key is null!"});
		int length = nodeList.getLength();
		for(int i = 0;i < length;i++){
			org.w3c.dom.Node node = nodeList.item(i);
			if(node != null && key.equals(node.getNodeName())){
				return node.getTextContent();
			}else if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				return getValue(childNodes,key);
			}
		}
		return null;
	}
	
	public static Map<String,String> getValue(NodeList nodeList,String[] keys){
		Assert.notNull(new Object[]{nodeList, keys}, new String[]{"NodeList is null!", "keys is null!"});
		int length = nodeList.getLength();
		List<String> list = Arrays.asList(keys);
		Map<String,String> map = new HashMap<>(keys.length);
		Map<String,String> subMap = null;
		for(int i = 0;i < length;i++){
			org.w3c.dom.Node node = nodeList.item(i);
			if(node != null && list.contains(node.getNodeName())){
				map.put(node.getNodeName(), node.getTextContent());
			}else if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				subMap = getValue(childNodes,keys);
			}
		}
		if(subMap != null)
			map.putAll(subMap);
		return map;
	}
	
	public static Map<String,String> getValue(String xmlStr, String[] keys){
		Assert.notNull(new Object[]{xmlStr, keys}, new String[]{"xmlStr is null!", "keys is null!"});
		Document document = XmlUtils.string2Xml(xmlStr);
		NodeList nodeList = document.getChildNodes();
		int length = nodeList.getLength();
		List<String> list = Arrays.asList(keys);
		Map<String,String> map = new HashMap<>(keys.length);
		Map<String,String> subMap = null;
		for(int i = 0;i < length;i++){
			org.w3c.dom.Node node = nodeList.item(i);
			if(node != null && list.contains(node.getNodeName())){
				map.put(node.getNodeName(), node.getTextContent());
			}else if(node.hasChildNodes()){
				NodeList childNodes = node.getChildNodes();
				subMap = getValue(childNodes,keys);
			}
		}
		if(subMap != null)
			map.putAll(subMap);
		return map;
	}
}
