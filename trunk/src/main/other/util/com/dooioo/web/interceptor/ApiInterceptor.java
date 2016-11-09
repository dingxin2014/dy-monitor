package com.dooioo.web.interceptor;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dooioo.ereceipt.common.model.AccessSetting;
import com.dooioo.ereceipt.common.service.IRegisterService;
import com.dooioo.plus.util.IPUtil;

@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

	private static final Log logger = LogFactory.getLog(ApiInterceptor.class);

	@Autowired
	private IRegisterService registerService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String ip = IPUtil.getIp(request);
		String appCode = request.getParameter("appCode");
		logger.info("request ["+ip+","+appCode==null?"null":appCode+"]");
		if(appCode == null)
			return false;
		AccessSetting accessSetting = registerService.getAccessSettingByAppCode(appCode);
		if(accessSetting == null)
			return false;
		return Arrays.asList(accessSetting.getIpAddress().split(",")).contains(ip);
	}


	

}
