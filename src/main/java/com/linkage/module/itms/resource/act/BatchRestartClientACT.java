package com.linkage.module.itms.resource.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.itms.resource.bio.BatchRestartClientBIO;
import com.linkage.module.itms.resource.dao.BatchRestartClientDAO;

public class BatchRestartClientACT extends splitPageAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BatchRestartClientACT.class);
	/**
	 * ************************新疆 ITMS系统批量重启终端****************************
	 * *****
	 */
	/** 任务id */
	private String taskId = "";
	/** 任务名 */
	private String taskName = "";

	private String ajax = "";

	/** 文件路径 */
	private String filePath = "";
	/** 实际上传文件 */
	private File uploadCustomer;
	/** 上传业务用户文件名 */
	private String uploadFileName4Customer;
	/** 任务 */
	private List tasklist;

	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	// 定制人
	private String accName;
	// 类型
	private Integer batchType;
	// 终端接入数
	private Integer netServNum;
	// 上网方式
	private Integer netWanType;
	// 任务结果
	private List taskResultList = null;

	private InputStream exportCustStreamExcel;

	private InputStream exportCustStreamTxt;
	private BatchRestartClientDAO dao;
	private BatchRestartClientBIO bio;
	//属地
	private String gwShare_cityId = null;
	HttpServletRequest request = null;
	private String gwShare_vendorId = null;
	private String gwShare_deviceModelId = null;
	private String gwShare_queryType = null;
	private String gwShare_devicetypeId=null;
	private String currencyType = null;

	private String gwShare_queryResultType = null;

	private String gwShare_queryParam = null;

	private String gwShare_queryField = null;

	private String gwShare_onlineStatus = null;

	private String gwShare_bindType = null;

	private String gwShare_deviceSerialnumber = null;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String gwShare_fileName = null;
	private String gwShare_msg = null;
	private int total = 0;
	private  String  gw_type;
	private String fileName;
	private List deviceList = null;
	//回传消息
	private String msg = null;
	/** 1，代表软件升级需求   */
	private String isBatch;
	private String deviceIds;
	private String param;
	
//	public String hello() {
//		System.out.println("----------------------hello--------------------------");
//		UserRes curUser = WebUtil.getCurrentUser();
//		long acc_oid = curUser.getUser().getId();
//		String cityId = StringUtil.getStringValue(curUser.getCityId());
//		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
//				startTime, endTime, cityId, taskName, acc_oid, accName, batchType);
//		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
//				num_splitPage, startTime, endTime, cityId, taskName, acc_oid,
//				accName, batchType);
//		return "hello";
//	}

	
	public String initImport() {
		logger.warn("initImport");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				startTime, endTime, cityId, taskName, acc_oid, accName, batchType);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage, startTime, endTime, cityId, taskName, acc_oid,
				accName, batchType);
		return "initImport";
	}

	/**
	 * 山东联通RMS平台宽带IPTV测速支持需求
	 * 
	 * @return
	 */
	public String saveBatchRestartClientTask() {
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		int ier=0;
		logger.warn(
				"saveBatchRestartClientTask([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])",
				new Object[] { acc_oid, taskName, filePath, 0 });
		if("1".equals(batchType))
		{
			BacthRestartTerminal();
		}
		else if("3".equals(batchType))
		{
			 BatchChangeNetPlayMode();
		}
		else
		{
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		if("3".equals(gwShare_queryType))
		{
			saveBatchRestart();
		}else
		{
		//String acc_cityId = StringUtil.getStringValue(curUser.getCityId());
		logger.warn("param=============="+gwShare_queryType);
		logger.warn("gwShare_onlineStatus============"+gwShare_onlineStatus);
		logger.warn("gwShare_vendorId============"+gwShare_vendorId);
		logger.warn("gwShare_deviceModelId============"+gwShare_deviceModelId);
		logger.warn("gwShare_bindType============"+gwShare_bindType);
		logger.warn("gwShare_deviceSerialnumber============"+gwShare_deviceSerialnumber);
		logger.warn("netServNum========="+netServNum);
		logger.warn("batchType========="+batchType);
		logger.warn("taskName========="+taskName);
		ajax = bio.saveBatchRestartClient(taskId, taskName, acc_oid, addTime, gwShare_cityId, batchType, netServNum, gwShare_queryType,
						gwShare_onlineStatus,gwShare_vendorId , gwShare_deviceModelId, gwShare_bindType, gwShare_deviceSerialnumber);
				//bio.delecttable(taskId);
			
		//devicetype_id= bio.getDevicetype(deviceId_array);
		if(batchType == null){
			ajax = "类型为空,定制任务失败!";
			return "ajax";
		}
		}
		}
		// ajax = "任务定制成功!";
		logger.warn("ajax============== "+ajax);
		return "ajax";
	}
	/**
	 * 批量重启终端
	 * 针对简单查询
	 * 高级查询
	 * @return
	 */
	public String BacthRestartTerminal()
	{
		
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		logger.warn(
				"BacthRestartTerminal([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])",
				new Object[] { acc_oid, taskName, filePath, 0 });
		if("3".equals(gwShare_queryType))
		{
			saveBatchRestart();
		}
		long taskId = currTime;
		long addTime = currTime;
		if(batchType == null){
			ajax = "类型为空,重启任务失败!";
			return "ajax";
		}
		ajax=bio.BacthRestartTerminal(taskId, acc_oid, addTime, gwShare_cityId, startTime, endTime, batchType, gwShare_queryType, 
				gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId, gwShare_bindType, gwShare_deviceSerialnumber);
		return "ajax";
	}
	/**
	 * 批量变更上网方式
	 * @return
	 */
	public String BatchChangeNetPlayMode()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		logger.warn(
				"BatchChangeNetPlayMode([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])",
				new Object[] { acc_oid, taskName, filePath, 0 });
		if("3".equals(gwShare_queryType))
		{
			saveBatchRestart();
		}
		long taskId = currTime;
		long addTime = currTime;
				if(batchType == null){
					ajax = "类型为空,变更任务失败!";
					return "ajax";
				}
				ajax=bio.BatchChangeNetPlayMode(taskId, acc_oid, addTime, gwShare_cityId, startTime, endTime, batchType, gwShare_queryType,
						gwShare_onlineStatus, gwShare_vendorId, gwShare_deviceModelId, gwShare_bindType, gwShare_deviceSerialnumber, netWanType);
		//devicetype_id= bio.getDevicetype(deviceId_array);
		return "ajax";
	}
	
	//针对导入查询
	public String saveBatchRestart()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();

		logger.warn(
				"saveBatchRestartClientTask([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])",
				new Object[] { acc_oid, taskName, filePath, 0 });
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		String acc_cityId = StringUtil.getStringValue(curUser.getCityId());
		if(batchType == null){
			ajax = "类型为空,定制任务失败!";
			return "ajax";
		}
		ajax = bio.saveBatchRestartClientTask(taskId, taskName, acc_oid, addTime,
				gwShare_fileName, uploadCustomer, uploadFileName4Customer, acc_cityId, startTime, endTime,batchType,netServNum,netWanType);
		// ajax = "任务定制成功!";
		return "ajax";
	}

	/**
	 * 查看结果
	 * 
	 * @return
	 */
	public String getTestSpeedTaskResult() {
		taskResultList = bio.getTaskResult(taskId, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage,
				num_splitPage, taskId);
		return "taskResult";
	}

	/**
	 * 删除定制任务
	 * 
	 * @return
	 */
	public String doDelete() {
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long delTime = new Date().getTime() / 1000L;

		logger.warn("doDelete([acc_oid={}],[taskId={}],[delTime={}])", new Object[] {
				acc_oid, taskId, delTime });

		ajax = bio.doDelete(taskId);
		return "ajax";
	}

	/**
	 * 下载模版 downloadTemplateCustExcel
	 * 
	 * @return
	 */
	public String downloadTemplateCustExcel() {
		return "toExportCustExcel";
	}

	// 下载模版exportCustStreamExcel
	public InputStream getExportCustStreamExcel() {
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "itms" + separa + "resource/config" + separa
				+ "batchCustUPtemplate.xls";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	/**
	 * 下载模版 downloadTemplateCustExcel
	 * 
	 * @return
	 */
	public String downloadTemplateCustTxt() {
		return "toExportCustTxt";
	}

	// 下载模版exportCustStreamExcel
	public InputStream getExportCustStreamTxt() {
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "itms" + separa + "resource/config" + separa
				+ "batchCustUPtemplate.txt";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}

	//查询属地 
	public String getCityNextChild()
	{
		logger.debug("BatchRestartClientACT=>getCityNextChild()");
		if (null == this.gwShare_cityId) {

			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.gwShare_cityId = curUser.getCityId();
		}
		this.ajax = bio.getCity(gwShare_cityId);
		return "ajax";
	}
	/**
	 * 查询设备厂商
	 * @return
	 */
	public String getVendor() {
		logger.debug("BatchRestartClientACT=>getVendor()");
		this.ajax = bio.getVendor();
		return "ajax";
	}
	/**
	 * 查询设备型号
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("BatchRestartClientACT=>getDeviceModel()");
		this.ajax = bio.getDeviceModel(gwShare_vendorId);
		return "ajax";
	}
	/**
	 * 查询设备版本
	 * 
	 * @return
	 */
	public String getDevicetype() {
		logger.debug("BatchRestartClientACT=>getDevicetype()");
		this.ajax = bio.getDevicetype(gwShare_deviceModelId,isBatch);
		return "ajax";
	}
	
	/**
	 * 查询
	 * @return
	 *//*
	public String queryDeviceList() {
		logger.debug("GwDeviceQueryACT=>getDeviceList()");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaId = curUser.getAreaId();
		if(null!=gwShare_queryType){
			gwShare_queryType.trim();
		}
		if(null!=gwShare_cityId){
			gwShare_cityId.trim();
		}else{
			this.gwShare_cityId = curUser.getCityId();
		}
		if("3".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}
			this.deviceList = bio.getDeviceList(gw_type,areaId, gwShare_queryType, gwShare_cityId, gwShare_fileName);
			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = bio.getMsg();
			}
			total = deviceList == null?0:this.deviceList.size();
			return "shareList0";

		}
		if("4".equals(gwShare_queryType)){
			if(null!=gwShare_fileName){
				gwShare_fileName.trim();
			}

			if(gwShare_fileName.length()<4){
				this.msg = "上传的文件名不正确！";
				return null;
			}
			String fileName_ = gwShare_fileName.substring(gwShare_fileName.length()-3, gwShare_fileName.length());
			logger.debug("fileName_;{}",fileName_);
			if(!"xls".equals(fileName_) && !"txt".equals(fileName_)){
				this.msg = "上传的文件格式不正确！";
				return null;
			}
			List<String> dataList = null;
			try{
				if("txt".equals(fileName_)){
					dataList = getImportDataByTXT(gwShare_fileName);
				}else{
					dataList = getImportDataByXLS(gwShare_fileName);
				}
			}catch(FileNotFoundException e){
				logger.warn("{}文件没找到！",gwShare_fileName);
				this.msg = "文件没找到！";
				return null;
			}catch(IOException e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}catch(Exception e){
				logger.warn("{}文件解析出错！",gwShare_fileName);
				this.msg = "文件解析出错！";
				return null;
			}

			if(null==this.deviceList || this.deviceList.size()<1){
				this.gwShare_msg = this.msg;
			}
			this.total = dataList.size();
			ajax = String.valueOf(total);
			return "ajax";
		}
		else{
			if(null!=gwShare_queryParam){
				gwShare_queryParam.trim();
			}
			if(null!=gwShare_queryField){
				gwShare_queryField.trim();
			}
			if(null!=gwShare_onlineStatus){
				gwShare_onlineStatus.trim();
			}
			if(null!=gwShare_vendorId){
				gwShare_vendorId.trim();
			}
			if(null!=gwShare_deviceModelId){
				gwShare_deviceModelId.trim();
			}
			if(null!=gwShare_devicetypeId){
				gwShare_devicetypeId.trim();
			}
			if(null!=gwShare_bindType){
				gwShare_bindType.trim();
			}
			if(null!=gwShare_deviceSerialnumber){
				gwShare_deviceSerialnumber.trim();
			}
			total = bio.getDeviceListCount(gw_type,areaId, gwShare_queryType,
					gwShare_queryParam, gwShare_queryField,gwShare_cityId, gwShare_onlineStatus,
					gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId, 
					gwShare_bindType, gwShare_deviceSerialnumber);

			if(total<=50){
				this.deviceList = bio.getDeviceList(gw_type,areaId, gwShare_queryType,
						gwShare_queryParam, gwShare_queryField,	gwShare_cityId, 
						gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList0";
			}else{
				this.deviceList = bio.getDeviceList(gw_type,curPage_splitPage,num_splitPage,
						areaId, gwShare_queryType,gwShare_queryParam, gwShare_queryField,
						gwShare_cityId, gwShare_onlineStatus,gwShare_vendorId,gwShare_deviceModelId,
						gwShare_devicetypeId, gwShare_bindType, gwShare_deviceSerialnumber);
				if (total % num_splitPage == 0) {
					maxPage_splitPage = total / num_splitPage;
				} else {
					maxPage_splitPage = total / num_splitPage + 1;
				}
				if(null==this.deviceList || this.deviceList.size()<1){
					this.gwShare_msg = bio.getMsg();
				}
				return "shareList";
			}
		}
	}*/
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setBio(BatchRestartClientBIO bio) {
		this.bio = bio;
	}

	public BatchRestartClientBIO getBio() {
		return bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public File getUploadCustomer() {
		return uploadCustomer;
	}

	public void setUploadCustomer(File uploadCustomer) {
		this.uploadCustomer = uploadCustomer;
	}

	public String getUploadFileName4Customer() {
		return uploadFileName4Customer;
	}

	public void setUploadFileName4Customer(String uploadFileName4Customer) {
		this.uploadFileName4Customer = uploadFileName4Customer;
	}

	public void setExportCustStreamExcel(InputStream exportCustStreamExcel) {
		this.exportCustStreamExcel = exportCustStreamExcel;
	}

	public void setExportCustStreamTxt(InputStream exportCustStreamTxt) {
		this.exportCustStreamTxt = exportCustStreamTxt;
	}

	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public List getTaskResultList() {
		return taskResultList;
	}

	public void setTaskResultList(List taskResultList) {
		this.taskResultList = taskResultList;
	}
	public Integer getBatchType() {
		return batchType;
	}

	public void setBatchType(Integer batchType) {
		this.batchType = batchType;
	}

	public Integer getNetServNum() {
		return netServNum;
	}

	public void setNetServNum(Integer netServNum) {
		this.netServNum = netServNum;
	}

	public Integer getNetWanType() {
		return netWanType;
	}

	public void setNetWanType(Integer netWanType) {
		this.netWanType = netWanType;
	}

	
	public String getGwShare_cityId()
	{
		return gwShare_cityId;
	}

	
	public void setGwShare_cityId(String gwShare_cityId)
	{
		this.gwShare_cityId = gwShare_cityId;
	}

	
	public HttpServletRequest getRequest()
	{
		return request;
	}

	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	
	public String getGwShare_vendorId()
	{
		return gwShare_vendorId;
	}

	
	public void setGwShare_vendorId(String gwShare_vendorId)
	{
		this.gwShare_vendorId = gwShare_vendorId;
	}

	
	public String getGwShare_deviceModelId()
	{
		return gwShare_deviceModelId;
	}

	
	public void setGwShare_deviceModelId(String gwShare_deviceModelId)
	{
		this.gwShare_deviceModelId = gwShare_deviceModelId;
	}

	
	public String getIsBatch()
	{
		return isBatch;
	}

	
	public void setIsBatch(String isBatch)
	{
		this.isBatch = isBatch;
	}

	
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}

	
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}

	
	public String getGwShare_devicetypeId()
	{
		return gwShare_devicetypeId;
	}

	
	public void setGwShare_devicetypeId(String gwShare_devicetypeId)
	{
		this.gwShare_devicetypeId = gwShare_devicetypeId;
	}

	
	public String getCurrencyType()
	{
		return currencyType;
	}

	
	public void setCurrencyType(String currencyType)
	{
		this.currencyType = currencyType;
	}

	
	public String getGwShare_queryResultType()
	{
		return gwShare_queryResultType;
	}

	
	public void setGwShare_queryResultType(String gwShare_queryResultType)
	{
		this.gwShare_queryResultType = gwShare_queryResultType;
	}

	
	public String getGwShare_queryParam()
	{
		return gwShare_queryParam;
	}

	
	public void setGwShare_queryParam(String gwShare_queryParam)
	{
		this.gwShare_queryParam = gwShare_queryParam;
	}

	
	public String getGwShare_queryField()
	{
		return gwShare_queryField;
	}

	
	public void setGwShare_queryField(String gwShare_queryField)
	{
		this.gwShare_queryField = gwShare_queryField;
	}

	
	public String getGwShare_onlineStatus()
	{
		return gwShare_onlineStatus;
	}

	
	public void setGwShare_onlineStatus(String gwShare_onlineStatus)
	{
		this.gwShare_onlineStatus = gwShare_onlineStatus;
	}

	
	public String getGwShare_bindType()
	{
		return gwShare_bindType;
	}

	
	public void setGwShare_bindType(String gwShare_bindType)
	{
		this.gwShare_bindType = gwShare_bindType;
	}

	
	public String getGwShare_deviceSerialnumber()
	{
		return gwShare_deviceSerialnumber;
	}

	
	public void setGwShare_deviceSerialnumber(String gwShare_deviceSerialnumber)
	{
		this.gwShare_deviceSerialnumber = gwShare_deviceSerialnumber;
	}

	
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	
	public String getGwShare_msg()
	{
		return gwShare_msg;
	}

	
	public void setGwShare_msg(String gwShare_msg)
	{
		this.gwShare_msg = gwShare_msg;
	}

	
	public int getTotal()
	{
		return total;
	}

	
	public void setTotal(int total)
	{
		this.total = total;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public List getDeviceList()
	{
		return deviceList;
	}

	
	public void setDeviceList(List deviceList)
	{
		this.deviceList = deviceList;
	}

	
	public String getMsg()
	{
		return msg;
	}

	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	
	public String getDeviceIds()
	{
		return deviceIds;
	}

	
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	
	public GwDeviceQueryBIO getGwDeviceQueryBio()
	{
		return gwDeviceQueryBio;
	}

	
	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio)
	{
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}

	
	public String getParam()
	{
		return param;
	}

	
	public void setParam(String param)
	{
		this.param = param;
	}

	
	public BatchRestartClientDAO getDao()
	{
		return dao;
	}

	
	public void setDao(BatchRestartClientDAO dao)
	{
		this.dao = dao;
	}
	
}
