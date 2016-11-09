package com.dooioo.job.monitor.service;

import com.dooioo.job.monitor.model.JobSetting;
import com.dooioo.web.common.Paginate;

import java.util.List;
import java.util.Map;

public interface IJobSettingService {

	List<JobSetting> queryJobSettingList(Map<String, Object> params);

	int save(JobSetting jobSetting);

	JobSetting getJobSetting(String appCode, String ip, String method);

	int update(JobSetting jobSetting);

	Paginate paginateMonitorList(int pageNo, int pageSize);

	boolean updateMonitor(JobSetting jobSetting);

	boolean deleteMonitor(JobSetting jobSetting);

}
