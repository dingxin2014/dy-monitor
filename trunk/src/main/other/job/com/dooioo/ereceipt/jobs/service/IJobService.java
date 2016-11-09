package com.dooioo.ereceipt.jobs.service;

import java.util.Date;

public interface IJobService {

	void fetchEreceiptBill(Date date, String bankCode);

	void sendRequest(Date from, Date to);

	void syncFile();

	void uploadFile();

	void fetchEreceiptBill(Date date);

}
