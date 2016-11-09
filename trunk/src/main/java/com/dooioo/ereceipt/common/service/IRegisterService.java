package com.dooioo.ereceipt.common.service;

import com.dooioo.ereceipt.common.model.AccessSetting;
import com.dooioo.web.common.Paginate;

public interface IRegisterService {

    Paginate paginateRegisterList(int pageNo, int pageSize);

    boolean insertRegister(AccessSetting accessSetting);

    boolean updateRegister(AccessSetting accessSetting);
    
	AccessSetting getAccessSettingByAppCode(String appCode);
}
