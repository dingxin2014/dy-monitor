package com.dooioo.ereceipt.jobs.service.impl;

import static com.dooioo.util.AppUtils.copyFile;
import static com.dooioo.util.AppUtils.deleteFile;
import static com.dooioo.util.AppUtils.getBeginOfDay;
import static com.dooioo.util.AppUtils.getBytes;
import static com.dooioo.util.AppUtils.getEndOfDay;
import static com.dooioo.util.AppUtils.getUsercode;
import static com.dooioo.util.AppUtils.isnull;
import static com.dooioo.util.AppUtils.toUpperFirst;
import static com.dooioo.util.ZipUtil.gunzip;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dooioo.bank.model.BankBill;
import com.dooioo.bank.service.IBankBillService;
import com.dooioo.base.EReceiptBaseService;
import com.dooioo.base.enums.Bank;
import com.dooioo.base.handler.EreceiptHandler;
import com.dooioo.base.handler.EreceiptHandler.HandlerCallBack;
import com.dooioo.core.utils.UUID;
import com.dooioo.eas.model.EasBill;
import com.dooioo.eas.service.IEasService;
import com.dooioo.ereceipt.common.model.Bill;
import com.dooioo.ereceipt.common.model.Log;
import com.dooioo.ereceipt.common.model.Request;
import com.dooioo.ereceipt.common.model.Upload;
import com.dooioo.ereceipt.common.service.IBillService;
import com.dooioo.ereceipt.common.service.ILogService;
import com.dooioo.ereceipt.common.service.IRequestService;
import com.dooioo.ereceipt.common.service.IUploadService;
import com.dooioo.lock.DistributedSynchronized;
import com.dooioo.util.Assert;
import com.dooioo.util.Constants;
import com.dooioo.util.PdfHandle;

import net.sf.json.JSONObject;

/**
 * JobService for e-receipt
 * <p>
 * Step1:
 * 		fetchEreceiptBill
 * Step2:
 * 		sendRequest
 * Step3:
 * 		syncFile
 * Step4:
 * 		uploadFile
 * </p>
 * @author dingxin   134069
 * @email being_away@qq.com
 *
 */
@Service
public class JobService extends EReceiptBaseService implements com.dooioo.ereceipt.jobs.service.IJobService
	{
	
	@Autowired
	private IBankBillService bankBillService;
	@Autowired
	private IEasService easService;
	@Autowired
	private IBillService billService;
	@Autowired
	private IRequestService requestService;
	@Autowired
	private IUploadService uploadService;
	@Autowired
	private ILogService logService;
	
	private static org.apache.commons.logging.Log logger = LogFactory.getLog(JobService.class);
	private static String uploadCache          			 = null;
	private static String reUploadCache					 = null;
	private static String compressCache					 = null;
	
	static{
		logger.info("检查缓存目录并初始化！");
		String dir = System.getProperty("user.home");
		String fileName = dir + File.separator + "eReceiptCache";
		uploadCache = fileName + File.separator + "uploadCache";
		reUploadCache = fileName + File.separator + "reUploadCache";
		compressCache = fileName + File.separator + "compressCache";
		File file = new File(fileName);
		File uploadCacheFile = new File(uploadCache);
		File reUploadCacheFile = new File(reUploadCache);
		File compressCacheFile = new File(compressCache);
		if(!file.exists())
			file.mkdirs();
		if(!uploadCacheFile.exists())
			uploadCacheFile.mkdirs();
		if(!reUploadCacheFile.exists())
			reUploadCacheFile.mkdirs();
		if(!compressCacheFile.exists())
			compressCacheFile.mkdirs();
		Bank[] banks = Bank.values();
		for(Bank bank: banks){
			if(bank == null)
				continue;
			String bankUploadCache = uploadCache + File.separator + bank.getBank();
			String bankReuploadCache = reUploadCache + File.separator + bank.getBank();
			File bankUploadCacheFile = new File(bankUploadCache);
			File bankReuploadCacheFile = new File(bankReuploadCache);
			if(!bankUploadCacheFile.exists())
				bankUploadCacheFile.mkdirs();
			if(!bankReuploadCacheFile.exists())
				bankReuploadCacheFile.mkdirs();
		}
	}
	
	@Override
	@DistributedSynchronized
	public void fetchEreceiptBill(Date date){
		fetchEreceiptBill(date, null);
	}
	
	/**
	 * 从金蝶和银企平台抓取单据
	 * <p>
	 * 1. 从银企平台数据库抓取正确状态单据
	 * 2. 判断单据是否在金蝶中存在
	 * 3. 持久化到数据库bill表
	 * </p>
	 */
	@Override
	@DistributedSynchronized
	public void fetchEreceiptBill(Date date, String bankCode){
		logger.info("开始抓取单据！");
		Assert.notNull(date, "date must not be null!");
		List<BankBill> bankBillList = bankBillService.getBankBillList(date, bankCode);
		Assert.notNull(bankBillList, "bankBillList must not be null!");
		List<Bill> list = bankBillList.stream().
				filter(bankBill -> bankBill.getEasSerialNumber() != null).
				filter(bankBill -> !billService.existBill(bankBill.getEasSerialNumber())).
				map(bankBill ->{
					JSONObject json = JSONObject.fromObject(bankBill);
					Bill bill = (Bill) JSONObject.toBean(json, Bill.class);
					if(bill.getEasSerialNumber() != null){
						EasBill easBill = easService.getEasPayBill(bill.getEasSerialNumber());
						if(easBill != null){
							bill.setEasBillNumber(easBill.getEasBillNumber());
							bill.setBillNumber(easBill.getBillNumber());
							bill.setCreator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							bill.setCreateTime(new Date());
							return bill;
						}
					}
					return null;
				}).filter(bill -> bill != null).collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(list)){
			billService.batchSave(list);
		}
		logger.info("结束抓取单据！");
	}
	
	/**
	 * 发送请求，获取单据对应的文件名
	 * <p>
	 * 1. 从bill表取出状态为0的单据及其所对应的distinct后的请求
	 * 2. 将对应请求填充并按照银行分类
	 * 3. 按照不同银行分别操作
	 * 4. 取出请求状态为-1的失败次数小于阈值的过往请求
	 * 5. 将新的请求状态从0置为-1  假如成功则置为1 
	 * 6. 合并过往请求和最新请求
	 * 7. 发送请求，根据回调持久化到数据库
	 * </p>
	 */
	@Override
	@DistributedSynchronized
	public void sendRequest(Date _from_,Date _to_){
		logger.info("开始发送请求！");
		Assert.notNull(new Object[]{_from_, _to_}, new String[]{"fromDate must not be null!","toDate must not be null!"});
		final Date from = new Date(getBeginOfDay(_from_));
		final Date to = new Date(getEndOfDay(_to_));
		List<Request> myRequestList = billService.billDistinctToRequest(from, to, 0);
		List<Bill> list = billService.getBillList(from, to, 0);
		Bank[] banks = Bank.values();
		Map<Bank,List<Request>> map = new HashMap<>(banks.length);
		for(Bank bank: banks){
			List<Request> currentBankList = myRequestList.stream().
					filter(request -> request.getBankCode() != null && request.getPayBankAccount() != null 
					&& request.getReceiptBankAccount() != null
					&& bank.getBankCode().equals(request.getBankCode())).
					map(request -> {
						Bill bill = list.stream().filter(_bill_ -> _bill_.getBankCode() != null 
								&& request.getBankCode().equals(_bill_.getBankCode())
								&& request.getPayBankAccount().equals(_bill_.getPayBankAccount())
								&& request.getReceiptBankAccount().equals(_bill_.getReceiptBankAccount())
						).findFirst().get();
						if(bill != null){
							request.setPayBankAccountName(bill.getPayBankAccountName());
							request.setReceiptBankAccountName(bill.getReceiptBankAccountName());
						}else{
							return null;
						}
						request.setFromDate(from);
						request.setToDate(to);
						request.setFailTimes(Integer.valueOf(0));
						request.setFlag(Integer.valueOf(0));
						return request;
					}).filter(request -> request != null).collect(Collectors.toList());
			map.put(bank, currentBankList);
		}
		map.forEach((bank, requestList) -> 
		{
			Map<String,Object> param = new HashMap<>();
			param.put("failTimes", Integer.valueOf(10));
			param.put("bankCode", bank.getBankCode());
			param.put("flag", Integer.valueOf(0));
			List<Request> _list_ = requestService.getRequests(param);
			requestList = requestList.stream().filter(request -> 
				!_list_.stream().anyMatch(_request_ -> {
					if(request.getBankCode().equals(_request_.getBankCode())
						&& request.getPayBankAccount().equals(_request_.getPayBankAccount())
						&& request.getReceiptBankAccount().equals(_request_.getReceiptBankAccount())){
						_request_.setFailTimes(Integer.valueOf(0));
						requestService.update(_request_);
						return true;
					}
					return false;
				})
			).map(request -> 
			{
				request.setCustomNo(UUID.randomUUID());
				request.setCreator(isnull(getUsercode(Thread.currentThread().getId()),Integer.valueOf(80001)));
				request.setCreateTime(new Date());
				requestService.save(request);
				List<Bill> billList = list.stream().filter(bill 
						-> bill.getPayBankAccount().equals(request.getPayBankAccount()) 
						&& bill.getReceiptBankAccount().equals(request.getReceiptBankAccount())
						&& bill.getPayFinishTime().getTime() <= getEndOfDay(request.getToDate())
						&& bill.getPayFinishTime().getTime() >= getBeginOfDay(request.getFromDate()) 
						&& bill.getBankCode().equals(request.getBankCode())).collect(Collectors.toList());
				billList.stream().forEach(bill -> {
					bill.setFlag(Integer.valueOf(-1));
					bill.setRequestId(request.getId());
					billService.update(bill);});
				request.setBillList(billList);
				return request;
			}).collect(Collectors.toList());
			requestList.addAll(_list_);
			String handlerStr = "com.dooioo.ereceipt."+bank.getBank()+"."+toUpperFirst(bank.getBank())+"Handler";
			try {
				Class<?> clazz = Class.forName(handlerStr);
				if(clazz != null){
					EreceiptHandler handler = (EreceiptHandler) clazz.newInstance();
					Request[] requests = requestList.toArray(new Request[requestList.size()]);
					handler.sendRequest(requests,new HandlerCallBack<String,Object>() {

						
						/**
						 * 
						 */
						private static final long serialVersionUID = 4308094462310035882L;

						@Override
						public void success(Request request, Object obj) {
							Assert.notNull(obj, "respFileName must not be null!");
							request.setFlag(Integer.valueOf(1));
							request.setDownloadFlag(Integer.valueOf(0));
							request.setDownloadFailTimes(Integer.valueOf(0));
							request.setRespFileName((String)obj);
								
							request.getBillList().stream().forEach(bill -> {
								bill.setUpdator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
								bill.setUpdateTime(new Date());
								bill.setFlag(Integer.valueOf(1));
								billService.update(bill);
							});
							
							StringBuffer sb = new StringBuffer();
							sb.append("send request successfully! ").append("request is ["+request.toString()+"] ");
							Log log = new Log();
							log.setOperate("发送"+bank.getBank()+"请求");
							log.setResult(SUCCESS);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}

						@Override
						public void fail(Request request, Object obj) {
							request.setFlag(Integer.valueOf(0));
							request.setDownloadFlag(Integer.valueOf(0));
							request.setFailTimes(isnull(request.getFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
							
							StringBuffer sb = new StringBuffer();
							sb.append("send request failed! ").append("request is ["+request.toString()+"] ");
							if(obj != null && obj instanceof String)
								sb.append(obj);
							if(obj != null && obj instanceof Exception)
								sb.append(((Exception)obj).getMessage());
							logger.info(sb.toString());
							Log log = new Log();
							log.setOperate("发送"+bank.getBank()+"请求");
							log.setResult(FAIL);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}

						@Override
						public void error(Request request, Object obj) {
							request.setFlag(Integer.valueOf(0));
							request.setDownloadFlag(Integer.valueOf(0));
							request.setFailTimes(isnull(request.getFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
							
							StringBuffer sb = new StringBuffer();
							sb.append("send request error! ").append("request is ["+request.toString()+"] ");
							if(obj != null && obj instanceof String)
								sb.append(obj);
							if(obj != null && obj instanceof Exception)
								sb.append(((Exception)obj).getMessage());
							logger.info(sb.toString());
							Log log = new Log();
							log.setOperate("发送"+bank.getBank()+"请求");
							log.setResult(FAIL);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}

						@Override
						public void complete(Request request, Object obj) {
							request.setUpdator(isnull(getUsercode(Thread.currentThread().getId()),Integer.valueOf(80001)));
							request.setUpdateTime(new Date());
							requestService.update(request);
						}

					});
				}
			} 
			catch (Exception e) {
				if(e instanceof ClassNotFoundException)
					logger.warn("Class ["+handlerStr+"] not found");
				else
					logger.error(e.getMessage(), e);
				requestList.stream().forEach(request ->{
					request.setFailTimes(isnull(request.getFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
					requestService.update(request);
				});
			}
		});
		logger.info("结束发送请求！");
	}
	
	/**
	 * 根据抓取的文件名从 SFTP/FTP服务器 抓取文件并缓存到本地
	 * <p>
	 * 1. 取出请求状态为1（已完成）,下载状态为0（未下载）的请求
	 * 2. 下载文件
	 * </p>
	 */
	@Override
	@DistributedSynchronized
	public void syncFile(){
		logger.info("开始从 FTP/SFTP 服务器抓取文件！");
		Map<String,Object> params = new HashMap<>(2);
		params.put("flag", Integer.valueOf(1));
		params.put("downloadFlag", Integer.valueOf(0));
		List<Request> list = requestService.getRequestList(params);
		Bank[] banks = Bank.values();
		for(Bank bank: banks){
			String handlerStr = "com.dooioo.ereceipt."+bank.getBank()+"."+toUpperFirst(bank.getBank())+"Handler";
			List<Request> _list_ = list.stream().filter(request -> bank.getBankCode().equals(request.getBankCode())).collect(Collectors.toList());
			Class<?> clazz;
			try {
				clazz = Class.forName(handlerStr);
				if(clazz != null){
					EreceiptHandler handler = (EreceiptHandler) clazz.newInstance();
					Request[] requests = _list_.toArray(new Request[_list_.size()]);
					handler.syncFile(requests, new HandlerCallBack<String,Object>() {

						/**
						 * 
						 */
						private static final long serialVersionUID = -397133658723283107L;

						@Override
						public void success(Request request, Object obj) {
							request.setDownloadFlag(Integer.valueOf(1));
							request.setDownloadFailTimes(isnull(request.getDownloadFailTimes(), Integer.valueOf(0)));
							
							String fileName = (String) obj;
							gunzip(fileName, uploadCache+File.separator+bank.getBank());
							deleteFile(new File(fileName));
							
							Log log = new Log();
							StringBuffer sb = new StringBuffer();
							sb.append("syncFile successfully! ").append("request is ["+request.toString()+"] ");
							log.setOperate("抓取"+bank.getBank()+"文件");
							log.setResult(SUCCESS);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}
						
						@Override
						public void fail(Request request,Object obj) {
							request.setDownloadFailTimes(isnull(request.getDownloadFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
							request.setDownloadFlag(Integer.valueOf(0));
							
							StringBuffer sb = new StringBuffer();
							sb.append("syncFile failed! ").append("request is ["+request.toString()+"] ");
							if(obj != null && obj instanceof String)
								sb.append(obj);
							if(obj != null && obj instanceof Exception)
								sb.append(((Exception)obj).getMessage());
							logger.info(sb.toString());
							Log log = new Log();
							log.setOperate("抓取"+bank.getBank()+"文件");
							log.setResult(FAIL);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}
						
						@Override
						public void error(Request request,Object obj) {
							request.setDownloadFailTimes(isnull(request.getDownloadFailTimes(),Integer.valueOf(0))+Integer.valueOf(1));
							request.setDownloadFlag(Integer.valueOf(0));
							
							StringBuffer sb = new StringBuffer();
							sb.append("syncFile error! ").append("request is ["+request.toString()+"] ");
							if(obj != null && obj instanceof String)
								sb.append(obj);
							if(obj != null && obj instanceof Exception)
								sb.append(((Exception)obj).getMessage());
							logger.info(sb.toString());
							Log log = new Log();
							log.setOperate("抓取"+bank.getBank()+"文件");
							log.setResult(FAIL);
							log.setRemark(sb.toString());
							log.setOperateTime(new Date());
							log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
							logService.log(log);
						}
						
						@Override
						public void complete(Request request,Object obj) {
							request.setUpdator(isnull(getUsercode(Thread.currentThread().getId()),Integer.valueOf(80001)));
							request.setUpdateTime(new Date());
							requestService.update(request);
						}

					}.putValue(Constants.ORIGIN_FILE_PATH, compressCache));
				}
			} catch (ClassNotFoundException e) {
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		logger.info("结束从 FTP/SFTP 服务器抓取文件！");
	}

	/**
	 * 上传到公司的fs文件缓存服务器
	 * <p>
	 * 1. 按照银行列出缓存文件和备份缓存文件中的所有文件
	 * 2. 文件上传至fs服务器，缓存文件上传失败则剪切至备份缓存文件，备份缓存文件上传失败则删除，上传成功则删除原文件
	 * 3. 持久化远程虚拟路径至数据库
	 * </p>
	 */
	@Override
	@DistributedSynchronized
	public void uploadFile(){
		logger.info("开始上传文件！");
		Bank[] banks = Bank.values();
		for(Bank bank: banks){
			File fileDir = new File(uploadCache+File.separator+bank.getBank());
			File bakFileDir = new File(reUploadCache+File.separator+bank.getBank());
			Assert.notNull(new Object[]{fileDir,bakFileDir}, 
					new String[]{"uploadCache file must not be null!","reuploadCache file must not be null!"});
			File[] files = fileDir.listFiles();
			File[] bakFiles = bakFileDir.listFiles();
			Map<File,Boolean> map = new HashMap<>(files.length+bakFiles.length);
			Arrays.asList(files).stream().forEach(file -> map.put(file, Boolean.valueOf(false)));
			Arrays.asList(bakFiles).stream().forEach(file -> map.put(file, Boolean.valueOf(true)));
			map.forEach((file, isBak) -> {
				String content;
				try {
					content = PdfHandle.getText(file.getPath());
				} catch (Exception e) {
					content = "";
				}
				if(content != null){
					Pattern pattern = Pattern.compile(bank.getRegex());
					Matcher matcher = pattern.matcher(content);
					String serialNumber = null;
					while(matcher.find()){
						serialNumber = matcher.group(1);
						break;
					}
					Bill bill = null;
					String virtualPath = null;
					StringBuffer sb = new StringBuffer();
					Log log = new Log();
					log.setOperate("上传"+bank.getBank()+"文件");
					log.setOperateTime(new Date());
					log.setOperator(isnull(getUsercode(Thread.currentThread().getId()), Integer.valueOf(80001)));
					if(!StringUtils.isEmpty(serialNumber) 
							&& (bill = billService.getBillBySerialNumber(serialNumber)) != null
							&& (virtualPath = uploadService.uploadFile(file.getName(), getBytes(file.getPath()))) != null
							&& uploadService.findByBillId(bill.getId()) == null){
						Upload upload = new Upload();
						upload.setBillId(bill.getId());
						upload.setRequestId(bill.getRequestId());
						upload.setDeleteFlag(Integer.valueOf(0));
						upload.setVirtualPath(virtualPath);
						upload.setFileName(file.getName());
						uploadService.save(upload);
						log.setResult(SUCCESS);
					}else{
						log.setResult(FAIL);
						if(!isBak)
							copyFile(file.getPath(), reUploadCache+File.separator+bank.getBank()+File.separator+file.getName());
					}
					if(isBak)
						sb.append("re");
					sb.append("upload "+bank.getBank()+" file ")
						.append(file.getName())
						.append("[serialNumber] = ")
						.append(serialNumber)
						.append(",")
						.append("[bill] = ")
						.append(bill != null ? bill.toString():"null")
						.append(",")
						.append("[virtualPath] = ")
						.append(virtualPath != null ? virtualPath:"null");
					log.setRemark(sb.toString());
					logService.log(log);
					deleteFile(file);
				}else{
					logger.info(file.getPath()+" content is null!");
				}
			});
		}
		logger.info("结束上传文件！");
	}
	
}
