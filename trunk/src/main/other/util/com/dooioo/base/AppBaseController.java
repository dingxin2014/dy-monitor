package com.dooioo.base;

import com.dooioo.ereceipt.exception.AppException;
import com.dooioo.json.JsonUtil;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.util.AppUtils;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import com.dooioo.web.exception.FunctionException;
import com.dooioo.web.exception.IllegalOperationException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class AppBaseController  {
	private static Log logger = LogFactory.getLog(AppBaseController.class);

	private static final int PAGE_INDEX_DEFAULT = 1;
	private static final int PAGE_SIZE_DEFAULT = 20;
	/**
	 * <p>
	 * description:获取页面参数的查询页面序列页
	 * </p>
	 *
	 * @return 查询页面序列号 默认返回1
	 */
	public int getPageIndex() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String pageIndex = request.getParameter("pageNo");
		if (AppUtils.isEmpty(pageIndex)) {
			return PAGE_INDEX_DEFAULT;
		}
		try {
			return Integer.parseInt(pageIndex);
		} catch (NumberFormatException e){
			return PAGE_INDEX_DEFAULT;
		}

	}

	/**
	 * <p>
	 * description:获取每页查询的数量，默认为20条
	 * </p>
	 *
	 * @return 查询的数量，默认20
	 */
	public int getPageSize() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String pageSize = request.getParameter("pageSize");
		if (AppUtils.isEmpty(pageSize)) {
			return PAGE_SIZE_DEFAULT;
		}
		try{
			return Integer.parseInt(pageSize);
		} catch (NumberFormatException e){
			return PAGE_SIZE_DEFAULT;
		}

	}

	@ModelAttribute(value = "config")
	public Configuration instance() {
		return Configuration.getInstance();
	}

	public String getParameter(String name) {
		return getParameter(name, false);
	}

	public String getParameter(String name, boolean setToRequest) {
		return getParameter(name, setToRequest, true);
	}

	public String getParameter(String name, boolean setToRequest, boolean decode) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String val = request.getParameter(name);
		if(val != null && "undefined".equals(val)){
			val = "";
		}
		if (!AppUtils.isEmpty(val)) {
			try {
				if (decode) {
					String tmp = new String(val.getBytes("iso8859-1"), "utf-8");
					if (!tmp.contains("??")) {
						val = tmp;
					}
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("不支持的编码", e);
			}
		}
		if (setToRequest && !AppUtils.isEmpty(val)) {
			request.setAttribute(name, val);
		}
		return val;
	}

	public <T> List<T> getParametersFromRequest(Class<T> t, String prefix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		@SuppressWarnings("unchecked")
		Enumeration<String> em = request.getParameterNames();
		String name ;
		String val ;
		Map<String, T> detailMap = new HashMap<>();
		T obj ;
		String index ;
		String col ;
		List<T> rs = new ArrayList<>();
		String prefixName = prefix + ".";
		while (em.hasMoreElements()) {
			name = em.nextElement();
			if (name.startsWith(prefixName)) {
				val = request.getParameter(name);
				name = name.substring((prefixName).length());
				col = name.substring(0, name.indexOf("_"));
				index = name.substring(name.indexOf("_") + 1);
				if (detailMap.containsKey(index)) {
					obj = detailMap.get(index);
					AppUtils.setValue(obj, col, val);
				} else {
					try {
						obj = t.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						logger.fatal(e.getMessage(), e);
						throw new AppException(e.getMessage(), e);
					}
					AppUtils.setValue(obj, col, val);
					detailMap.put(index, obj);
				}
			}
		}
		if (detailMap.size() > 0) {
			detailMap.values().stream().forEach(rs::add);
		}
		return rs;
	}


	
	/**
	 * @Description: 获取请求中的所有参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRequestParams() {
		Map<String, Object> params = new HashMap<>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> em = request.getParameterNames();
		String key = null;
		while (em.hasMoreElements()) {
			key = em.nextElement();
			params.put(key, request.getParameter(key));
		}
		return params;
	}

	public void setAttribute(String name, Object val) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		request.setAttribute(name, val);
	}

	/**
	 * 获取客户端ip地址
	 * @return 客户端ip地址 获取失败返回空字符串
	 */
	public static String getIp() {
		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		// 获取客户端IP
		String ipAddress = httpRequest.getHeader("Cdn-Src-Ip");//增加CDN获取ip
		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("X-Forwarded-For");
		}

		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("HTTP_CLIENT_IP");
		}
		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("Proxy-Client-IP");
		}
		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getHeader("WL-Proxy-Client-IP");
		}
		if (AppUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httpRequest.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")
					|| ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				try {
					ipAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					logger.warn(e.getMessage(),e);
				}

			}

		}
		return ipAddress;
	}

	/**
	 * 操作成功，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @return 成功
	 */
	public JSONObject ok(){
		return ok("操作成功！");
	}

	/**
	 * 操作成功，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @param msg 反馈的消息
	 * @return 成功附带信息
	 */
	public JSONObject ok(String msg){
		return ok(msg,null);
	}

	/**
	 * 操作成功，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @param msg 反馈的消息
	 * @param data 返回数据
	 * @return 成功附带信息与数据, 如数据为null则不返回数据字段
	 */
	public JSONObject ok(String msg,Object data){
		return rs(msg,1,data);
	}

	/**
	 * 操作失败，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @return 失败
	 */
	public JSONObject fail(){
		return fail("操作失败！");
	}

	/**
	 * 操作失败，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @param msg 反馈的消息
	 * @return 失败附带信息
	 */
	public JSONObject fail(String msg){
		return fail(msg,null);
	}

	/**
	 * 操作失败，返回数据中含有三个key；status：状态，1表示成功，0表示失败；message：反馈的消息;data:表示需要传达的参数，默认data为null
	 * @param msg 反馈的消息
	 * @param data 返回数据
	 * @return 失败附带信息, 如数据为null则不返回数据字段
	 */
	public JSONObject fail(String msg,Object data){
		return rs(msg,0,data);
	}


	private JSONObject rs(String msg,int status,Object data){
		JSONObject rs = new JSONObject();
		rs.put("status", status);
		rs.put("message", msg);
		if(data != null){
			try{
				JSONObject json = JSONObject.fromObject(data, JsonUtil.getJsonConfig());
				rs.put("data", json);
			}catch(Exception e){
				rs.put("data", data);
			}
		}else{
			rs.put("data", "");
		}
		return rs;
	}

	/**
	 * 拦截参数异常，返回JSONObject
	 *
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public JSONObject illegalArgumentException(IllegalArgumentException ex) {
		logger.error("IllegalArgumentException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截参数异常，返回JSONObject
	 *
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JSONObject runtimeException(RuntimeException ex) {
		logger.error("RuntimeException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}


	/**
	 * 拦截方法异常，返回JSONObject
	 *
	 * @see FunctionException
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(FunctionException.class)
	@ResponseBody
	public JSONObject functionException(FunctionException ex) {
		logger.error("FunctionException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截非法操作异常，返回JSONObject
	 *
	 * @see IllegalOperationException
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(IllegalOperationException.class)
	@ResponseBody
	public JSONObject illegalOperationException(IllegalOperationException ex) {
		logger.error("IllegalOperationException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截数据库操作异常，返回JSONObject
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(DataAccessException.class)
	@ResponseBody
	public JSONObject dataAccessException(DataAccessException ex) {
		logger.error("DataAccessException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截空指针异常，返回JSONObject
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseBody
	public JSONObject nullPointerException(NullPointerException ex) {
		logger.error("NullPointerException => " + ex.getMessage(), ex);
		return fail(ex.getMessage());
	}
	

	public Employee getSesionUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if(request == null){
			return null;
		}
		HttpSession session = request.getSession();
		if (session == null)
			return null;
		else
			return (Employee) session.getAttribute(Constants.SESSION_USER);
	}
}
