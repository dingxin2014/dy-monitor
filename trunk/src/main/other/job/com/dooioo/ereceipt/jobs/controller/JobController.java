package com.dooioo.ereceipt.jobs.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dooioo.base.EReceiptBaseController;
import com.dooioo.base.enums.Bank;
import com.dooioo.ereceipt.jobs.service.IJobService;
import com.dooioo.web.annotation.Anonymous;

@RestController
@Anonymous
@RequestMapping(value = "/job")
public class JobController extends EReceiptBaseController {

	private static final Log logger = LogFactory.getLog(JobController.class);
	
	@Autowired
	private IJobService jobService;

	@RequestMapping(value = "/fetchBill")
	public Json fetchBill(HttpServletRequest request, Date date) {
		jobService.fetchEreceiptBill(date, null);
		return ok("抓取完毕");
	}

	@RequestMapping(value = "/sendRequest")
	public Json sendRequest(HttpServletRequest request, Date from, Date to) {
		jobService.sendRequest(from, to);
		return ok("请求发送完毕");
	}

	@RequestMapping(value = "/syncFile")
	public Json syncFile(HttpServletRequest request) {
		jobService.syncFile();
		return ok("文件同步完毕");
	}

	@RequestMapping(value = "/uploadFile")
	public Json uploadFile(HttpServletRequest request) {
		jobService.uploadFile();
		return ok("文件上传完毕");
	}

	@RequestMapping(value = "/sync")
	public Json sync(HttpServletRequest request, Date from, Date to) {
		if (from.after(to))
			return fail("from must before to!");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		int days = 0;
		try {
			days = daysBetween(from, to)+1;
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		for(int i=0;i < days;i++){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			logger.info("fetch date ->"+calendar.getTime());
			jobService.fetchEreceiptBill(calendar.getTime(),Bank.BOC.getBankCode());
		}
		jobService.sendRequest(from, to);
		jobService.syncFile();
		jobService.uploadFile();
		return ok;
	}

	private static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	

	@Anonymous
	@RequestMapping(value = "/test")
	public Json test(HttpServletRequest request, int size) {
		threadSize = size;
		endSignal = new CountDownLatch(size);
		list = new ArrayList<>(size);
		multiThreadTest();
		return ok;
	}
	
	private CountDownLatch signal = new CountDownLatch(1);
	private int threadSize;
	private CountDownLatch endSignal;
	private List<Long> list;
	
	public void multiThreadTest(){
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					signal.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("线程"+Thread.currentThread().getId()+"开始运行");
				long start = System.currentTimeMillis();
				jobService.fetchEreceiptBill(Calendar.getInstance().getTime(), null);
				long end = System.currentTimeMillis();
				list.add(end-start);
				System.err.println("还剩 "+endSignal.getCount());
				endSignal.countDown();
			}
		};
		multiThreadTest(runnable);
		signal.countDown();
		try {
			endSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("测试完毕");
		getExeTime();
	}
	
	private void multiThreadTest(Runnable runnable){
		for(int i=0;i<threadSize;i++){
			Thread t = new Thread(runnable);
			t.start();
		}
	}
	
	private void getExeTime() {
		int size = list.size();
		java.util.List<Long> _list = new ArrayList<Long>(size);
		_list.addAll(list);
		Collections.sort(_list);
		long min = _list.get(0);
		long max = _list.get(size-1);
		long sum = 0L;
		for (Long t : _list) {
			sum += t;
		}
		long avg = sum/size;
		System.out.println("min: " + min);
		System.out.println("max: " + max);
		System.out.println("avg: " + avg);
	}

}
