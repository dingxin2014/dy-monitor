package com.dooioo.util.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.druid.support.http.StatViewServlet;

/**
 * 继承 druid 为实现监控账号密码加密
 * @author dingxin
 *
 */
public class DruidStatViewServlet extends StatViewServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if (!isPermittedRequest(request)) {
            path = "/nopermit.html";
            returnResourceFile(path, uri, response);
            return;
        }

        if ("/submitLogin".equals(path)) {
            String usernameParam = DigestUtils.md5Hex(request.getParameter(PARAM_NAME_USERNAME));
            String passwordParam = DigestUtils.md5Hex(request.getParameter(PARAM_NAME_PASSWORD));
            
            if (username.equals(usernameParam) && password.equals(passwordParam)) {
                request.getSession().setAttribute(SESSION_USER_KEY, username);
                response.getWriter().print("success");
            } else {
                response.getWriter().print("error");
            }
            return;
        }

        if (isRequireAuth() //
            && !ContainsUser(request)//
            && !("/login.html".equals(path) //
                 || path.startsWith("/css")//
                 || path.startsWith("/js") //
            || path.startsWith("/img"))) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/login.html");
            } else {
                if ("".equals(path)) {
                    response.sendRedirect("druid/login.html");
                } else {
                    response.sendRedirect("login.html");
                }
            }
            return;
        }

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/index.html");
            } else {
                response.sendRedirect("druid/index.html");
            }
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
            response.getWriter().print(process(fullUrl));
            return;
        }

        // find file in resources path
        returnResourceFile(path, uri, response);
    }
	
	
}
