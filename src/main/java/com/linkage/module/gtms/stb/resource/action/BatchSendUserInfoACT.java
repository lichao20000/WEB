package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.BatchSendUserInfoBIO;
import com.linkage.module.itms.resource.bio.BatchRestartClientBIO;

import action.splitpage.splitPageAction;

public class BatchSendUserInfoACT extends splitPageAction {

	/**
	 * JXDX (根据业务账号进行批量业务下发功能需求) 20160726
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BatchSendUserInfoACT.class);
	
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

	private String startTime;
	// 查询结束时间
	private String endTime;
	// 定制人
	private String accName;
	// 是否下发业务账号及密码
	private String configType;
	// 任务结果
	private List taskResultList = null;

	private InputStream exportCustStreamExcel;

	private InputStream exportCustStreamTxt;

	private BatchSendUserInfoBIO bio;
	//是否活跃
	private String isActive;
	//任务详细信息
	private Map<String, String> taskDetailMap = null;

	public String initImport()
	{
		logger.debug("initImport");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,isActive);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,isActive);
		return "initImport";
	}


	/**
	 * 山东联通RMS平台宽带IPTV测速支持需求
	 * 
	 * @return
	 */
	public String saveBatchSendUserInfoTask() {
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();

		logger.warn(
				"saveBatchSendUserInfoTask([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])",
				new Object[] { acc_oid, taskName, filePath, 0 });
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		String acc_cityId = StringUtil.getStringValue(curUser.getCityId());

		
		ajax = bio.saveBatchSendUserInfoTask(taskId, taskName, acc_oid, addTime,
				filePath, uploadCustomer, uploadFileName4Customer, acc_cityId,configType);
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
		String filePath = path + "gtms" + separa + "stb/resource/config" + separa
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
		String filePath = path + "gtms" + separa + "stb/resource/config" + separa
				+ "batchCustUPtemplate.txt";
		try {
			fio = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.warn("读取文件异常", e);
		}
		return fio;
	}
	
	/**
	 * 设置任务失效
	 * 
	 * @return
	 */
	public String beNotActived() {
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long time = new Date().getTime() / 1000L;

		logger.warn("beNotActived([acc_oid={}],[taskId={}],[Time={}])", new Object[] {
				acc_oid, taskId, time });

		ajax = bio.beNotActived(taskId);
		return "ajax";
	}
	
	/**
	 * 查看结果
	 * @return
	 */
	public String getShowBatchSendUserInfoTaskResult()
	{
		taskResultList = bio.getTaskResult(taskId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		return "taskResult";
	}
	
	/**
	 * 查看详细
	 * @return
	 */
	public String getShowBatchSendUserInfoTaskDetail()
	{
		taskDetailMap = bio.getTaskDetail(taskId);
		return "taskDetail";
	}

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

	
	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public BatchSendUserInfoBIO getBio() {
		return bio;
	}
	public void setBio(BatchSendUserInfoBIO bio) {
		this.bio = bio;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public Map<String, String> getTaskDetailMap() {
		return taskDetailMap;
	}


	public void setTaskDetailMap(Map<String, String> taskDetailMap) {
		this.taskDetailMap = taskDetailMap;
	}
	
}
