package com.test.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dooioo.bank.service.IBankBillService;
import com.dooioo.eas.model.EasBill;
import com.dooioo.eas.service.IEasService;
import com.dooioo.ereceipt.boc.BocGlobal;
import com.dooioo.ereceipt.boc.request.service.IBocRequestService;
import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.service.IRequestService;
import com.dooioo.ereceipt.jobs.service.IJobService;
import com.dooioo.ftp.IFtpClient;
import com.dooioo.ftp.SftpClient;
import com.dooioo.util.Assert;
import com.dooioo.util.ZipUtil;
import com.test.base.BaseTest;

public class BillServiceTest extends BaseTest{

	@Autowired
	private IEasService easService;
	@Autowired
	private IJobService jobService;
	@Autowired
	private IBankBillService bankBillService;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IBocRequestService bocRequestService;
	
	@Test
	public void test(){
		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, 2016);
//		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.DAY_OF_MONTH, 25);
		System.out.println("-->"+cal.getTime());
		jobService.fetchEreceiptBill(cal.getTime());
	}
	
	@Test
	public void test2(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2016);
		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.DAY_OF_MONTH, 25);
		jobService.sendRequest(cal.getTime(),cal.getTime());
	}
	
	@Test
	public void test3(){
		EasBill easBill = easService.getEasPayBill("48410760833302541");
		System.out.println("over");
	}
	
	@Test
	public void test4(){
		
	}
}
