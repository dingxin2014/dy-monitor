package com.dooioo.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import com.dooioo.web.exception.FunctionException;
import com.dooioo.web.exception.IllegalOperationException;

/**
 * base controller for e-receipt. inner class <code>Json</code> for RestController
 * @author dingxin
 *
 */
public abstract class EReceiptBaseController {
	
	private static Log logger = LogFactory.getLog(EReceiptBaseController.class);

	protected Employee getSessionUser(HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute(Constants.SESSION_USER);
		return employee;
	}

	protected Json ok = new Json("ok");
	protected Json fail = new Json("fail");

	protected Json ok() {
		return ok("");
	}

	protected Json ok(String msg) {
		return ok(msg, "");
	}

	protected Json ok(String msg, String detail) {
		return new Json("ok", msg, detail);
	}

	protected Json fail() {
		return fail("");
	}

	protected Json fail(String msg) {
		return fail(msg, "");
	}

	protected Json fail(String msg, String detail) {
		return new Json("fail", msg, detail);
	}

	public Employee getEmployee() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();
		Employee emp = (Employee) session.getAttribute(Constants.SESSION_USER);
		return emp;
	}

	public class Json extends JSONObject implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Json(String status) {
			super.put("status", status);
			super.put("message", "");
			super.put("detail", "");
		}

		public Json(String status, String message) {
			super.put("status", status);
			super.put("message", message);
			super.put("detail", "");
		}

		public Json(String status, String message, Object detail) {
			super.put("status", status);
			super.put("message", message);
			super.put("detail", detail);
		}

		public Json put(String key, Object obj) {
			super.put(key, obj);
			return this;
		}

		public Json remove(String key) {
			super.remove(key);
			return this;
		}
		
		public Json putall(Map<? extends String, ? extends Object> m){
			super.putAll(m);
			return this;
		}
	}
	

	/**
	 * 拦截参数异常，返回Json
	 *
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public Json illegalArgumentException(IllegalArgumentException ex) {
		logger.error("IllegalArgumentException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截参数异常，返回Json
	 *
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Json runtimeException(RuntimeException ex) {
		logger.error("RuntimeException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}


	/**
	 * 拦截方法异常，返回Json
	 *
	 * @see com.dooioo.web.exception.FunctionException
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(FunctionException.class)
	@ResponseBody
	public Json functionException(FunctionException ex) {
		logger.error("FunctionException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截非法操作异常，返回Json
	 *
	 * @see com.dooioo.web.exception.IllegalOperationException
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(IllegalOperationException.class)
	@ResponseBody
	public Json illegalOperationException(IllegalOperationException ex) {
		logger.error("IllegalOperationException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截数据库操作异常，返回Json
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(DataAccessException.class)
	@ResponseBody
	public Json dataAccessException(DataAccessException ex) {
		logger.error("DataAccessException => "+ex.getMessage(), ex);
		return fail(ex.getMessage());
	}

	/**
	 * 拦截空指针异常，返回Json
	 * @param ex 错误
	 * @return 返回失败json
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseBody
	public Json nullPointerException(NullPointerException ex) {
		logger.error("NullPointerException => " + ex.getMessage(), ex);
		return fail(ex.getMessage());
	}
	
	/**
	   * 设置当前系统环境到MODEL
	   * @return
	   */
	  @ModelAttribute(value="config")
	  public Configuration instance(){
	    return Configuration.getInstance();
	  }
	
	
}
