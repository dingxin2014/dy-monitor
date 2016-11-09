package com.dooioo.web.interceptor;

import com.dooioo.plus.oms.dyEnums.Company;
import com.dooioo.plus.oms.dyEnums.EmployeeInfoType;
import com.dooioo.plus.oms.dyEnums.OrganizationMethods;
import com.dooioo.plus.oms.dyEnums.OrganizationStatus;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.plus.oms.dyUser.model.Organization;
import com.dooioo.plus.oms.dyUser.model.Privilege;
import com.dooioo.plus.oms.dyUser.service.EmployeeService;
import com.dooioo.plus.oms.dyUser.service.OrganizationService;
import com.dooioo.plus.oms.dyUser.service.PrivilegeService;
import com.dooioo.util.AppUtils;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import com.lianjia.sh.se.login.common.model.LoginedEmployee;
import com.lianjia.sh.se.login.common.util.LoginedEmployees;
import com.lianjia.sh.se.login.common.web.interceptor.LoginCallback;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Component
public class LoginInterceptor extends com.lianjia.sh.se.login.common.web.interceptor.SSOLoginInterceptor implements
		LoginCallback {

	private static final Log logger = LogFactory.getLog(LoginInterceptor.class);

	private static final String URL_PREFIX = "repeat_uri_";
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private PrivilegeService privilegeService;

	private ThreadLocal<Long> time = new ThreadLocal<Long>();

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		if(!super.preHandle(request, response, handler)){
			 HttpSession session = request.getSession();
			 @SuppressWarnings("unchecked")
	     	 Enumeration<String> enumeration = session.getAttributeNames();
			 String current = null;
			 while(enumeration.hasMoreElements()){
				 current = enumeration.nextElement();
				 if(current.startsWith(URL_PREFIX))
					 session.removeAttribute(current);
			 }
			 return false;
		}
		time.set(System.currentTimeMillis());
		String uri = request.getRequestURI();
		
		Employee emp = null;
		if((emp = ((Employee) request.getSession().getAttribute(Constants.SESSION_USER))) == null){
			LoginedEmployee ssoEmployee = LoginedEmployees.current(request);
			emp = employeeService.getEmployee(ssoEmployee.getCompanyId(),ssoEmployee.getUserCode(),EmployeeInfoType.Privilege);
			HttpSession session = request.getSession();
			session.setAttribute(Constants.PERMISSIONS, emp.getPrivilegeMap());
			session.setAttribute(Constants.SESSION_USER, emp);
		}
		
		String ip = AppUtils.getIp(request);
		
		 long tid = Thread.currentThread().getId();
		 AppUtils.putEmp(tid, emp);
		 AppUtils.putUsercode(tid, emp.getUserCode());
		 AppUtils.putUsername(tid, emp.getUserName());
		 AppUtils.putCompanyId(tid, emp.getCompanyId());
		 AppUtils.putUserIP(tid, ip);
		 
		if (multRequest(request, response)) {
			logger.info("mult request---"+uri+"\tuserCode:"+ emp.getUserCode()+"("+emp.getUserName()+")");
			return false;
		}
		return true;

	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unused")
	private void sendRedirect(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		String sendUrl = employeeService.getLogoutUrl(getCompany(request));
		StringBuffer sb = request.getRequestURL();
        if(StringUtils.isNotEmpty(request.getQueryString()))
            sb.append("?").append(request.getQueryString());
        response.sendRedirect(sendUrl + (sendUrl.contains("?") ? "&" : "?")
        		+"ref="+URLEncoder.encode(sb.toString(), "utf-8"));
	}

	private Company getCompany(HttpServletRequest request){
		int companyId = employeeService.getCookieCompany(request);
		Company company_enums = null;
		if(Company.Dooioo.companyIdentity() == companyId){
			 company_enums = Company.Dooioo;
		}else if( Company.iDerong.companyIdentity() == companyId){
			company_enums = Company.iDerong;
		}else if(Company.suZhoulj.companyIdentity() == companyId){
			company_enums = Company.suZhoulj;
		}else{
			company_enums = Company.Dooioo;
		}
		return company_enums;
	}
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
	/**
	 * 判断是否为ajax请求，如果为ajax请求，判断是不是重复请求，如果是重复请求，则等待
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	private boolean multRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (isAjax(request)) {
			HttpSession session = request.getSession();
			String uri = request.getRequestURI();
			Date date = null;
			uri = URL_PREFIX+uri;
			if ((date = (Date)session.getAttribute(uri)) != null) {
				ajaxResponse(request,response,"不能重复访问 ["+dateFormat.format(date)+"]");
				return true;
			}
			session.setAttribute(uri, new java.util.Date());
		}
		return false;
	}

	/**
	 * @param response
	 * @throws IOException 
	 */
	private void ajaxResponse(HttpServletRequest request,HttpServletResponse response,String info) throws IOException {
		setResponseHeader(response);
		String contentType = request.getContentType();
		response.setContentType(contentType);
		Writer out = response.getWriter();
		String result = result(info);
		out.write(result);
		out.flush();
	}

	/**
	 * @return
	 */
	private String result(String info) {
		JSONObject rs = new JSONObject();
		rs.put("status", 0);
		rs.put("message", info);
		rs.put("data", "{}");
		return rs.toString();
	}

	private boolean isAjax(HttpServletRequest httpRequest) {
		return (null != httpRequest.getHeader("X-Requested-With") && httpRequest
				.getHeader("X-Requested-With").equals("XMLHttpRequest"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
	 * afterCompletion(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		removeAjaxUri(request);
		long tid = Thread.currentThread().getId();
		String uri = request.getRequestURI();
		String ip = AppUtils.getIp(request);
		long t = 0;
		if(time.get() != null){
			t = time.get();
			time.set(null);
		}
		logger.info(ip + "---completion uri-->" + uri + "\tuserCode="
				+ AppUtils.getUsercode(tid)+"("+AppUtils.getUsername(tid)+")" + "\ttime="
				+ (System.currentTimeMillis() - t) + "ms");
		AppUtils.removeEmp(tid);
		AppUtils.removeCompanyId(tid);
		AppUtils.removeUsercode(tid);
		AppUtils.removeUsername(tid);
		AppUtils.removeCompanyId(tid);
		AppUtils.removeUserIP(tid);
	}
	
	private void removeAjaxUri(HttpServletRequest request){
		if (isAjax(request)) {
			HttpSession session = request.getSession();
			String uri = URL_PREFIX+request.getRequestURI();
			Object obj = session.getAttribute(uri);
			if (obj != null) {
				session.removeAttribute(uri);
			}
		}
	}
	
	private void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
	}

	@Override
	public void after(LoginExecutionContext context) throws Exception {
		logger.info("------after:start------");
		LoginedEmployee user = context.getLoginedEmployee();
		HttpSession session = context.getSession();
		Employee emp = this.employeeService.getEmployee(user.getUserCode());
		List<Organization> orgs = organizationService.getOrgs(
				OrganizationMethods.getPartitionOrgsByUserCode,String.valueOf(user.getUserCode()), new OrganizationStatus[] {
						OrganizationStatus.Normal, OrganizationStatus.Pause },
				null, null);
		emp.setPartitionOrgs(orgs);
		if (emp.getPrivileges() == null || emp.getPrivileges().size()==0) {
			//读取应用下的权限
			List<Privilege> privileges = privilegeService.getPrivilegeByUserId(
					emp.getCompanyId(), user.getUserCode(), Configuration.getInstance().getAppCode());
			emp.setPrivileges(privileges);
		}
		session.setAttribute(Constants.PERMISSIONS, emp.getPrivilegeMap());
		session.setAttribute(Constants.SESSION_USER, emp);
		logger.info("------after:end------");
//		time.set(null);
	}
	

}
