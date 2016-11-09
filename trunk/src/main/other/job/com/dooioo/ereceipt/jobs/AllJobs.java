package com.dooioo.ereceipt.jobs;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dooioo.ereceipt.jobs.service.IJobService;

import static com.dooioo.util.AppUtils.putUsercode;
import static com.dooioo.util.AppUtils.removeUsercode;

@Component
public class AllJobs {

	private static Log logger = LogFactory.getLog(AllJobs.class);
	
	@Autowired
	private IJobService jobService;
	
	/**
	 * 抓取金蝶/银企单据 
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void sync(){
		logger.info("----开始抓取电子回单----");
		putUsercode(Thread.currentThread().getId(), Integer.valueOf(80001));
		Calendar calendar = Calendar.getInstance();
		jobService.fetchEreceiptBill(calendar.getTime());
		jobService.sendRequest(calendar.getTime(), calendar.getTime());
		jobService.syncFile();
		jobService.uploadFile();
		removeUsercode(Thread.currentThread().getId());
		logger.info("----结束抓取电子回单----");
	}
	
	@Scheduled(cron = "0 */1 * * * ?")
	public void test1(){
		System.out.println("i'm test1");
	}
	
	@Scheduled(cron = "0 */2 * * * ?")
	public void test2(){
		System.err.println("i'm test2");
	}
	
}
