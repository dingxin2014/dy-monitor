package com.dooioo.web.tag;

import com.dooioo.plus.oms.dyUser.model.Privilege;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Map;

public class MenuTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3746226992154389082L;


	private static final Log logger = LogFactory.getLog(MenuTag.class);



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
		try {

			JSONArray menus = Configuration.getInstance().getPermMenus();
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("<table width='100%' cellpadding='0' cellspacing='0' class='navTab1th'>");
			buffer.append("<tbody>");
			buffer.append("<tr align='center'>");
			
			JSONObject json = null;
			JSONObject permJson = null;
			for (Object obj : menus) {
				if(JSONNull.getInstance().equals(obj)){
					continue;
				}
				 permJson = null;
				//子菜单有，就显示父类菜单
				json = (JSONObject)obj;
				if(json.containsKey("child") && json.get("child") != null){
					JSONArray childMenus = json.getJSONArray("child");
					for(Object subObj : childMenus){
						JSONObject subJson = (JSONObject)subObj;
						permJson = getMenuInfo(subJson,json);
						if(permJson != null){
							break;
						}
					}
				}else{
					permJson = getMenuInfo(json,null);
				}
				
				if(permJson != null){
					buffer.append("<td ").append("width='").append(permJson.getString("width")).append("'>");
					buffer.append("<a href='").append(permJson.getString("url")).append("' ");
					if(permJson.containsKey("click")){
						String clk = permJson.getString("click");
						if(clk != null && !"".equals(clk.trim())){
							buffer.append(" ng-click='").append(clk).append("'");
						}
					}
					buffer.append(" code='").append(permJson.getString("code")).append("' ");
					buffer.append(">");
					buffer.append(permJson.getString("name"));
					buffer.append("</a>");
					buffer.append("</td>");
				}
			}
		
			buffer.append("</tr></tbody></table>");
			body = buffer.toString();
		
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
	
	private JSONObject getMenuInfo(JSONObject json,JSONObject parentJson){
		JSONObject rs = null;
		Map<String,Privilege> permissionMenus = getMenus();
		String perm = json.containsKey("perm") ? json.getString("perm") : "";
		if(perm == null || "".equals(perm.trim())){
			return null;
		}
		String name  = null;
		String url = null;
		String clk = null;
		String code = null;
		perm = perm.trim();
		String width  = "";
		//perm 为空，则说明不需要权限
		if(permissionMenus.containsKey(perm) || "ano".equals(perm)){
			url = json.containsKey("url") ?  json.getString("url") : null;
			clk = json.containsKey("click") ? json.getString("click") : null;
			name = json.containsKey("name") ? json.getString("name") : null;
			code = json.containsKey("code") ? json.getString("code") : null;
			width = json.containsKey("width") ? json.getString("width") : null;
			rs = new JSONObject();
			rs.put("url", url);
			rs.put("click", clk);
			rs.put("name", name);
			rs.put("code", code);
			rs.put("width", width);
			if(parentJson != null){
				name = parentJson.containsKey("name") ? parentJson.getString("name") : null;
				code = parentJson.containsKey("code") ? parentJson.getString("code") : null;
				width = parentJson.containsKey("width") ? parentJson.getString("width") : null;
				rs.put("name", name);
				rs.put("code", code);
				rs.put("width", width);
			}
		}
		
		return rs;
	}

	
	private Map<String,Privilege> getMenus() {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		//List<Privilege> rentPerms = new ArrayList<Privilege>();
		Map<String, Privilege> map = (Map<String, Privilege>) req.getSession()
				.getAttribute(Constants.PERMISSIONS);
		return map;
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


}
