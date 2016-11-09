/**
 * 
 */
package com.dooioo.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dooioo.util.Configuration;

/**
 * @author liguohui
 *
 */
public class Log4jListener implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String webAppRootKey = (String)sce.getServletContext().getInitParameter("webAppRootKey");
		System.setProperty("appPath", webAppRootKey);
		// 初始化配置文件
		Configuration.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
