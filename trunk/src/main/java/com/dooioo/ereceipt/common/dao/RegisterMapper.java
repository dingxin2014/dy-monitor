package com.dooioo.ereceipt.common.dao;

import com.dooioo.ereceipt.common.model.AccessSetting;
import com.dooioo.ereceipt.common.model.Bill;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterMapper {

	List<Bill> queryRegisterList(PageBounds pageBounds);

	boolean insertRegister(AccessSetting accessSetting);

	boolean updateRegister(AccessSetting accessSetting);
	
	AccessSetting getAccessSettingByAppCode(String appCode);
}
