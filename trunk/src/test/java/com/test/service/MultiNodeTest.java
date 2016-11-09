package com.test.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dooioo.bank.service.IBankBillService;
import com.dooioo.ereceipt.boc.request.service.IBocRequestService;
import com.dooioo.ereceipt.common.service.IRequestService;
import com.dooioo.ereceipt.jobs.service.IJobService;
import com.test.base.BaseTest;

/**
 * 
 * @author dingxin
 *
 */
public class MultiNodeTest extends BaseTest{

	
	@Autowired
	private IJobService jobService;
	@Autowired
	private IBankBillService bankBillService;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IBocRequestService bocRequestService;
	
	private CountDownLatch signal = new CountDownLatch(1);
	private final int threadSize = 20;
	private CountDownLatch endSignal = new CountDownLatch(threadSize);
	private List<Long> list = new ArrayList<>();
	
	@Test
	public void test(){
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
				jobService.fetchEreceiptBill(Calendar.getInstance().getTime());
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
