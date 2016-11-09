package com.dooioo.util;

import com.dooioo.plus.util.GlobalConfigUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 公共配置文件
 * 
 * @author ChaiKun
 */
public class Configuration {

	private static Configuration configuration = null;

	private static Object lock = new Object();

	private PropertiesConfiguration propConfig;

	private String env;

	private String envName;

	private String appCode;
	
	private String masterNode;
	
	private String zookeeperConfig;
	
	// url访问地址
	private String urlSuffix;


	private String pdfUrl;
	
	// 菜单对应的按钮
	private JSONArray permMenus = null;

	// key:url,value:jsonobject
	private Map<String, JSONObject> menuMap = null;

	/**
	 * 版本号
	 */
	private String version;

	public static final String ENV_TEST = "test";

	public static Configuration getInstance() {
		if (configuration == null) {
			synchronized (lock) {
				if (configuration == null) {
					configuration = new Configuration();
				}
			}
		}
		return configuration;
	}

	private Configuration() {

		propConfig = new PropertiesConfiguration();
		try {
			propConfig.setEncoding("utf-8");
			propConfig.load("global.properties");
			env = GlobalConfigUtil.getCurrentEnv();
			// 初始化日志文件
			String logFile = propConfig.getFile().getParent() + "/config/log/"
					+ env + "-log4j.properties";
			PropertyConfigurator.configure(logFile);
			appCode = propConfig.getString("appCode");
			propConfig
					.load("config/run/" + env + "." + appCode + ".properties");
			version = GlobalConfigUtil.getCurrentVersion();
			
			masterNode = propConfig.getString("master_ip");
			zookeeperConfig = propConfig.getString("zookeeper.server") + ":" +
					propConfig.getString("zookeeper.port");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		urlSuffix = propConfig.getString("url_Suffix");
		pdfUrl= propConfig.getString("pdfUrl");
		loadMenus();
	}

	private String getFileContent(String filename) {
		BufferedReader br = null;
		try {
			String path = "config/json/" + filename;
			InputStream in = Configuration.class.getClassLoader()
					.getResourceAsStream(path);
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(in), "UTF-8"));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private void loadMenus() {
		if (permMenus != null && permMenus.size() > 0) {
			return;
		}
		permMenus = new JSONArray();
		String jsonString = getFileContent("menu-perm.json");
		permMenus = JSONArray.fromObject(jsonString);
		JSONObject json = null;
		menuMap = new HashMap<String, JSONObject>();
		for (int i = 0; i < permMenus.size(); i++) {
			json = permMenus.getJSONObject(i);
			menuMap.put(json.getString("code"), json);
		}
	}

	public String getString(String url) {
		return propConfig.getString(url);
	}

	public String getEnv() {
		return env;
	}

	public String getEnvName() {
		return envName;
	}

	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getProperty(String name) {
		return this.propConfig.getString(name);
	}

	public Map<String, String> getProperties(String prefix) {
		Map<String, String> rs = new HashMap<String, String>();
		String key = null;
		for (Iterator<String> iter = propConfig.getKeys(); iter.hasNext();) {
			key = iter.next();
			if (key.startsWith(prefix)) {
				rs.put(key, propConfig.getString(key));
			}

		}
		return rs;
	}

	/**
	 * 开发时，更新本地开发库的js版本号，其它与发布的版本号一致
	 * @return the version
	 */
	public String getVersion() {
		if(isDevelopment()){
			//return String.valueOf(System.currentTimeMillis());
		}
		return version;
	}
	/**
	 * 开发时给系统js文件作为版本使用，不必每次都加载系统js文件
	 * @return
	 */
	public String getSysVersion() {
		return version;
	}

	public JSONArray getPermMenus() {
		return permMenus;
	}

	public void setPermMenus(JSONArray permMenus) {
		this.permMenus = permMenus;
	}

	public Map<String, JSONObject> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, JSONObject> menuMap) {
		this.menuMap = menuMap;
	}

	public Boolean isDevelopment() {
		return GlobalConfigUtil.isDevelopmentEnv();
	}

	public Boolean isTest() {
		return ENV_TEST.equals(this.getEnv());
	}

	public Boolean isIntegration() {
		return GlobalConfigUtil.isIntegrationEnv();
	}

	public Boolean isProduction() {
		return GlobalConfigUtil.isProductionEnv();
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	/**
	 * @return the masterNode
	 */
	public String getMasterNode() {
		return masterNode;
	}

	/**
	 * @return the zookeeperConfig
	 */
	public String getZookeeperConfig() {
		return zookeeperConfig;
	}

	/**
	 * @return
	 */
	public Properties getProperties() {
		Properties pros = new Properties();
		for(Iterator<String> iter = propConfig.getKeys() ; iter.hasNext() ; ){
			String key = iter.next();
			pros.put(key, propConfig.getProperty(key));
		}
		return pros;
	}


	
}
