package com.dooioo.web.interceptor;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dooioo.plus.oms.dyUser.model.Privilege;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import com.dooioo.web.annotation.Anonymous;
import com.dooioo.web.annotation.AuthRef;

/**
 * <p>
 * Title: [子系统名称]_[模块名]
 * </p>
 * <p>
 * Description: 权限校验
 * </p>
 * 
 * @author: jack lee
 * @version: v1.0
 * @author: (lastest modification by jack lee)
 * @since: 2015年10月31日下午7:15:54
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

	private static final Log logger = LogFactory
			.getLog(AuthorityInterceptor.class);

	private String[] excludes;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Configuration config = Configuration.getInstance();
		if (config.isDevelopment()) {
			return true;
		}
		if (handler instanceof HandlerMethod) {//权限仅仅保护controller入口程序，不对资源文件进行保护
			HandlerMethod hm = (HandlerMethod) handler;
			Method m = hm.getMethod();
			Class targetBean = hm.getBean().getClass();
			Annotation anno = targetBean.getAnnotation(Anonymous.class);
			if (anno != null) {
				return true;
			}
			anno = m.getAnnotation(Anonymous.class);
			if (anno != null) {
				return true;
			}
			String className = targetBean.getName();
			AuthRef authRef = m.getAnnotation(AuthRef.class);

			String longMethod = className + "." + m.getName();
			if (excludes != null && excludes.length > 0) {
				for (String ex : excludes) {
					if (matches(longMethod, ex)) {
						return true;
					}
				}
			}
			if (authRef == null) {
				logger.fatal("not found authority in method-->" + className
						+ "." + m.getName());
//				request.getRequestDispatcher("/error/noAuthorityMethod")
//						.forward(request, response);
				dispatcher(request,response,"未定义权限，在controller的方法中 ==>"+hm.toString());
				return false;
			}
			String[] codes = authRef.ref();
			if (codes == null || codes.length == 0) {
				return false;
			}
			HttpSession session = request.getSession();
			Map<String, Privilege> pris = (Map<String, Privilege>) session
					.getAttribute(Constants.PERMISSIONS);
			for (String k : codes) {
				if (pris.containsKey(k)) {
					return true;
				}
			}
			dispatcher(request,response,"你没操作权限");
			return false;
		}
//		request.getRequestDispatcher("/error/noPrivilege").forward(request,
//				response);
		return true;
	}
	
	private void dispatcher(HttpServletRequest request,
			HttpServletResponse response,String msg) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		JSONObject json = rs(msg,0,"");
		Writer out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();
	}

	public String[] getExcludes() {
		return excludes;
	}

	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}

	private boolean matches(String str, String reg) {
		if (str == null || "".equals(str))
			return false;
		Pattern localPattern = Pattern.compile(reg);
		Matcher localMatcher = localPattern.matcher(str);
		return localMatcher.matches();
	}
	
	private JSONObject rs(String msg,int status,Object data){
		JSONObject rs = new JSONObject();
		rs.put("status", status);
		rs.put("message", msg);
		if(data != null){
			try{
				JSONObject json = JSONObject.fromObject(data,new JsonConfig());
				rs.put("data", json);
			}catch(Exception e){
				rs.put("data", data);
			}
		}
		return rs;
	}

}
