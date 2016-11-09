package com.dooioo.ereceipt.common.service.impl;

import com.dooioo.base.AppBaseService;
import com.dooioo.ereceipt.common.dao.RegisterMapper;
import com.dooioo.ereceipt.common.model.AccessSetting;
import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.web.common.Paginate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * @author jixueping
 *
 */
@Service
public class RegisterService extends AppBaseService implements com.dooioo.ereceipt.common.service.IRegisterService{

	@Autowired
	private RegisterMapper registerMapper;


	public Paginate paginateRegisterList(int pageNo, int pageSize) {
		PageBounds pageBounds = new PageBounds(pageNo, pageSize);
		List<Bill> list = registerMapper.queryRegisterList(pageBounds);
		Paginate paginate = createPaginate(pageNo, pageSize, list);
		return paginate;
	}

	@Override
	public boolean insertRegister(AccessSetting accessSetting) {
		return registerMapper.insertRegister(accessSetting);
	}

	@Override
	public boolean updateRegister(AccessSetting accessSetting) {
		return registerMapper.updateRegister(accessSetting);
	}

	@Override
	public AccessSetting getAccessSettingByAppCode(String appCode) {
		return registerMapper.getAccessSettingByAppCode(appCode);
	}

}
