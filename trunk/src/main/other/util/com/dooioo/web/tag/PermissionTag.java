package com.dooioo.web.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dooioo.plus.oms.dyUser.model.Privilege;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;

public class PermissionTag extends BodyTagSupport {

	private static final Log logger = LogFactory.getLog(PermissionTag.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6915607732151888928L;

	/**
	 * 权限编码
	 */
	private String code;

	/**
	 * 是否为导航,值为true、1时，为导航，否则为其他页面，导航不显示
	 */
	private String navigation;

	private String display;

	private String body = "";

	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	public int doAfterBody() throws JspException {
		BodyContent bc = this.getBodyContent();
		body = bc != null ? bc.getString() : "";
		if (bc != null) {
			bc.clearBody();
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		try {
			if (isNav()) {
				Map<String, JSONObject> menus = Configuration.getInstance()
						.getMenuMap();
				List<Privilege> permissionMenus = getMenus();
				StringBuffer buffer = new StringBuffer();
				JSONObject json = null;
				String uri = req.getRequestURI();
				String ctx = req.getContextPath();
				if (!"".equals(ctx) && uri.startsWith(ctx)) {
					uri = uri.length() > ctx.length() ? uri.substring(ctx
							.length() + 1) : "";
					uri = uri.length() > 0 && uri.contains("/") ? uri
							.substring(0, uri.indexOf("/")) : uri;
				}
				// boolean flag = true;
				String model = null;
				for (Privilege p : permissionMenus) {
					json = menus.get(p.getPrivilegeUrl());
					if (json == null) {
						continue;
					}
					model = p.getPrivilegeUrl().split("_")[2];
					buffer.append("<a href=\"").append(json.getString("url"));
					// if ("".equals(uri) && flag) {
					// buffer.append(" class='current' ");
					// flag = false;
					// }
					// if (uri.equals(model)) {
					// buffer.append(" class='current' ");
					// }
					buffer.append("\" ");
					buffer.append(" model='" + model + "' ");
					buffer.append(" >");
					buffer.append(json.getString("name")).append("</a>");
				}
				body = buffer.toString();
			} else if (hasPermission(code)) {
				// do nothing
			} else {
				body = "";
			}
			out.print(body);
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e);
			try {
				out.println();
			} catch (IOException e1) {
			}
		}
		return super.doEndTag();
	}

	private List<Privilege> getMenus() {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		List<Privilege> rentPerms = new ArrayList<Privilege>();
		Map<String, Privilege> map = (Map<String, Privilege>) req.getSession()
				.getAttribute(Constants.PERMISSIONS);
		String key = null;
		String appCode = Configuration.getInstance().getAppCode() + "_menu_";
		Privilege pri = null;
		String url = null;
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			key = iter.next();
			if (key.startsWith(appCode)) {
				pri = map.get(key);
				url = pri.getPrivilegeUrl();
				try {
					String[] ua = url.split("_");
					Double.parseDouble(ua[ua.length - 1]);
					rentPerms.add(pri);
				} catch (Exception e) {
					logger.fatal(
							"url-->" + pri.getPrivilegeUrl() + "\t"
									+ e.getMessage(), e);
				}

			}
		}
		Collections.sort(rentPerms, new Comparator<Privilege>() {
			public int compare(Privilege o1, Privilege o2) {
				String u1 = o1.getPrivilegeUrl();
				String u2 = o2.getPrivilegeUrl();
				String[] s1 = u1.split("_");
				String[] s2 = u2.split("_");
				return Double.parseDouble(s1[s1.length - 1]) >= Double
						.parseDouble(s2[s2.length - 1]) ? 1 : -1;
			}

		});
		return rentPerms;
	}

	private boolean hasPermission(String url) {
		if (url == null || "".equals(url)) {
			return false;
		}
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		Map<String, Privilege> map = (Map<String, Privilege>) req.getSession()
				.getAttribute(Constants.PERMISSIONS);
		if (url.contains(",")) {
			String[] codes = url.split(",");
			for (String c : codes) {
				if (map.containsKey(c)) {
					return true;
				}
			}
		}
		return map.containsKey(url);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNavigation() {
		return navigation;
	}

	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}

	public boolean isNav() {
		return "1".equals(navigation) || new Boolean(navigation);
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

}
