package com.dooioo.ereceipt.common.controller;

import com.dooioo.base.AppBaseController;
import com.dooioo.ereceipt.common.model.AccessSetting;
import com.dooioo.ereceipt.common.service.IRegisterService;
import com.dooioo.json.JsonUtil;
import com.dooioo.plus.oms.dyUser.model.Employee;
import com.dooioo.web.annotation.AuthRef;
import com.dooioo.web.common.Paginate;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/register")
public class registerController extends AppBaseController {

    @Autowired
    private IRegisterService registerService;

    @ResponseBody
    @RequestMapping(value = "/paginateRegisterList")
    @AuthRef(ref={"appRegister_list"})
    public JSONObject paginateRegisterList(HttpServletRequest request, HttpServletResponse response){
        Paginate paginate = registerService.paginateRegisterList(this.getPageIndex(),this.getPageSize());
        return this.ok("ok",JSONObject.fromObject(paginate, JsonUtil.getJsonConfig()) );
    }

    @ResponseBody
    @RequestMapping(value = "/registerAddOrUpdate")
    @AuthRef(ref={"appRegister_add", "appRegister_update"})
    public JSONObject registerAddOrUpdate(HttpServletRequest request,HttpServletResponse response,AccessSetting accessSetting){
        Employee emp = getSesionUser();
        if (accessSetting == null){
            request.setAttribute("_resp_message", "参数获取异常");
            return fail("参数获取异常");
        }
        if (accessSetting.getId() == null || accessSetting.getId() == 0){
            accessSetting.setCreator(emp.getUserCode());
            registerService.insertRegister(accessSetting);
        } else {
            accessSetting.setUpdator(emp.getUserCode());
            registerService.updateRegister(accessSetting);
        }
        request.setAttribute("_resp_message", "ok");
        return ok();
    }

}
