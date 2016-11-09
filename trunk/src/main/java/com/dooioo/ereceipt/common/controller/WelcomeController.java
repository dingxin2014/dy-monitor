package com.dooioo.ereceipt.common.controller;

import com.dooioo.base.AppBaseController;
import com.dooioo.json.JsonUtil;
import com.dooioo.plus.oms.dyUser.model.Privilege;
import com.dooioo.util.Configuration;
import com.dooioo.util.Constants;
import com.dooioo.web.annotation.Anonymous;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@Anonymous
public class WelcomeController extends AppBaseController {

	private static final Log logger = LogFactory.getLog(WelcomeController.class);

	/**
	 * 程序主入口
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/", "/index" })
	public String index(HttpServletRequest request, HttpSession session, Model model) {
		logger.debug("access index");
		return "index";
	}
	
	/**
	 * 取得当前用户的权限列表
	 * @param session
	 * @return
	 */
	@RequestMapping(value = { "/loadPrivileges" }, method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getPrivileges(HttpSession session){
		if(session==null)
			return fail();
		Map<String,Privilege> permissionMap = (Map<String, Privilege>) session.getAttribute(Constants.PERMISSIONS);
		Map<String,Boolean> resultMap = new HashMap<String, Boolean>();
		if(permissionMap!=null){
			Iterator<String> it = permissionMap.keySet().iterator();
			while (it.hasNext()) {
				resultMap.put(it.next(), true);
			}
		}
		return ok("ok",JSONObject.fromObject(resultMap, JsonUtil.getJsonConfig()));
	}
	
	/**
	 * 是否有某个权限
	 * @param menu
	 * @param privileges
	 * @return
	 */
	private boolean hasPrivilege(JSONObject menu, Map<String, Privilege> privileges) {
		if(!menu.containsKey("perm")){
			return false;
		}
		String perm = menu.getString("perm");
		if("ano".equals(perm) || privileges.containsKey(perm)){
			return true;
		}
		if(Configuration.getInstance().isDevelopment()){
			return true;
		}
		return false;
	}
	
}
