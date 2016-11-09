package com.dooioo.job.monitor.controller;

import com.dooioo.base.AppBaseController;
import com.dooioo.job.monitor.model.JobSetting;
import com.dooioo.job.monitor.service.IJobSettingService;
import com.dooioo.json.JsonUtil;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.web.annotation.Anonymous;
import com.dooioo.web.annotation.AuthRef;
import com.dooioo.web.common.Paginate;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Anonymous
@RequestMapping(value = "/monitor")
@Controller
public class MonitorController extends AppBaseController {

	@Autowired
	private IJobSettingService jobSettingService;

	@ResponseBody
	@RequestMapping(value = "/paginateMonitorList")
	@AuthRef(ref={"jobMonitor_list"})
	public JSONObject paginateRegisterList(HttpServletRequest request, HttpServletResponse response){
		Paginate paginate = jobSettingService.paginateMonitorList(this.getPageIndex(),this.getPageSize());
		return this.ok("ok",JSONObject.fromObject(paginate, JsonUtil.getJsonConfig()) );
	}

	@ResponseBody
	@RequestMapping(value = "/ModifyJob")
	@AuthRef(ref={"jobMonitor_update"})
	public JSONObject monitorUpdate(HttpServletRequest request,HttpServletResponse response,JobSetting jobSetting){
		Employee emp = getSesionUser();
		if (jobSetting == null){
			request.setAttribute("_resp_message", "参数获取异常");
			return fail("参数获取异常");
		}
		jobSetting.setUpdator(emp.getUserCode());
		jobSettingService.updateMonitor(jobSetting);
		request.setAttribute("_resp_message", "ok");
		return ok();
	}

	@ResponseBody
	@RequestMapping(value = "/DeleteJob")
	@AuthRef(ref={"jobMonitor_delete"})
	public JSONObject deleteMonitor(HttpServletRequest request,HttpServletResponse response,JobSetting jobSetting){
		if (jobSetting == null){
			request.setAttribute("_resp_message", "参数获取异常");
			return fail("参数获取异常");
		}
		jobSettingService.deleteMonitor(jobSetting);
		request.setAttribute("_resp_message", "ok");
		return ok();
	}

}
