package com.dooioo.job.monitor.dao;

import com.dooioo.job.monitor.model.JobSetting;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JobSettingMapper {

	List<JobSetting> queryJobSettingList(Map<String,Object> params);

	int save(JobSetting jobSetting);

	JobSetting getJobSetting(Map<String, Object> map);

	int update(JobSetting jobSetting);

	List<JobSetting> queryMonitorList(PageBounds pageBounds);

	boolean updateMonitor(JobSetting jobSetting);

	boolean deleteMonitor(JobSetting jobSetting);

	void delete(int id);
}
