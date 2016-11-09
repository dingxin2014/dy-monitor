package com.dooioo.util.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class ReadOnlyInterceptor extends HandlerInterceptorAdapter {

	/* (non-Javadoc)
	 * @see HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			Method m = hm.getMethod();
			Annotation anno = m.getAnnotation(ReadOnly.class);
			if (anno != null) { // 不为空表明有只读注解
				RoutingDatasourceHolder.setCurrentDatabase(RoutingDatasourceHolder.READ_DB); // 开启只读数据库
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		//关闭设置的只读数据库
		RoutingDatasourceHolder.clear();
	}

}
