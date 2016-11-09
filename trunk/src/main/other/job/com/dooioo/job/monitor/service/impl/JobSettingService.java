package com.dooioo.job.monitor.service.impl;

import com.dooioo.base.AppBaseService;
import com.dooioo.job.monitor.dao.JobSettingMapper;
import com.dooioo.job.monitor.model.JobSetting;
import com.dooioo.web.common.Paginate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class JobSettingService extends AppBaseService implements com.dooioo.job.monitor.service.IJobSettingService{

	@Autowired
	private JobSettingMapper jobSettingMapper;

	@Override
	public List<JobSetting> queryJobSettingList(Map<String,Object> params){
		return jobSettingMapper.queryJobSettingList(params);
	}

	@Override
	public int save(JobSetting jobSetting){
		return jobSettingMapper.save(jobSetting);
	}

	public Paginate paginateMonitorList(int pageNo, int pageSize) {
		PageBounds pageBounds = new PageBounds(pageNo, pageSize);
		List<JobSetting> list = jobSettingMapper.queryMonitorList(pageBounds);
		Paginate paginate = createPaginate(pageNo, pageSize, list);
		return paginate;
	}
	@Override
	public boolean updateMonitor(JobSetting jobSetting) {
		return jobSettingMapper.updateMonitor(jobSetting);
	}

	@Override
	public boolean deleteMonitor(JobSetting jobSetting) {
		return jobSettingMapper.deleteMonitor(jobSetting);
	}

	@Override
	public JobSetting getJobSetting(String appCode,String ip,String method){
		Map<String,Object> map = new HashMap<String,Object>(3);
		map.put("method", method);
		map.put("ip", ip);
		map.put("appCode", appCode);
		return jobSettingMapper.getJobSetting(map);
	}

	@Override
	public int update(JobSetting jobSetting){
		return jobSettingMapper.update(jobSetting);
	}

}
