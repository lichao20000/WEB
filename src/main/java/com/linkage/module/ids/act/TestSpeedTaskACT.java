package com.linkage.module.ids.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.ids.bio.TestSpeedTaskBIO;

public class TestSpeedTaskACT extends splitPageAction  {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(TestSpeedTaskACT.class);
	/**
	 * ************************山东联通RMS平台宽带IPTV测速支持需求*********************************
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
	//定制人
	private String accName;
	//任务结果
	private List taskResultList = null;

	private InputStream exportCustStreamExcel;
	
	private InputStream exportCustStreamTxt;
	
	private TestSpeedTaskBIO bio;
	
	
	public String initImport()
	{
		logger.debug("initImport");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName);
		return "initImport";
	}

	/**
	 * 山东联通RMS平台宽带IPTV测速支持需求
	 * @return
	 */
	public String saveTestSpeedTask()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		
		logger.warn("saveTestSpeedTask([acc_oid={}],[taskName={}],[file_path={}],[test_status={}])", new Object[] { acc_oid, taskName, filePath,0});		
		
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		String acc_cityId = StringUtil.getStringValue(curUser.getCityId());
		
		ajax = bio.saveTestSpeedTask(taskId, taskName, acc_oid, addTime,filePath,uploadCustomer,uploadFileName4Customer,acc_cityId);
		//ajax = "任务定制成功!";
		return "ajax";
	}
	
	/**
	 * 查看结果
	 * @return
	 */
	public String getTestSpeedTaskResult()
	{
		taskResultList = bio.getTaskResult(taskId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		return "taskResult";
	}
	
	
	/**
	 * 删除定制任务
	 * @return
	 */
	public String doDelete(){
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		
		logger.warn("doDelete([acc_oid={}],[taskId={}])", new Object[] { acc_oid, taskId });		
		
	    ajax = bio.doDelete(taskId);
		return "ajax";
	}
	
	/**
	 * 下载模版 downloadTemplateCustExcel
	 * 
	 * @return
	 */
	public String downloadTemplateCustExcel()
	{
		return "toExportCustExcel";
	}
	
	// 下载模版exportCustStreamExcel
		public InputStream getExportCustStreamExcel()
		{
			FileInputStream fio = null;
			String path = ServletActionContext.getServletContext().getRealPath("/");
			String separa = System.getProperty("file.separator");
			String filePath = path + "ids" + separa + "config" + separa
					+ "batchCustUPtemplate.xls";
			try
			{
				fio = new FileInputStream(filePath);
			}
			catch (FileNotFoundException e)
			{
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
		public String downloadTemplateCustTxt()
		{
			return "toExportCustTxt";
		}
		
		// 下载模版exportCustStreamExcel
			public InputStream getExportCustStreamTxt()
			{
				FileInputStream fio = null;
				String path = ServletActionContext.getServletContext().getRealPath("/");
				String separa = System.getProperty("file.separator");
				String filePath = path + "ids" + separa + "config" + separa
						+ "batchCustUPtemplate.txt";
				try
				{
					fio = new FileInputStream(filePath);
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					logger.warn("读取文件异常", e);
				}
				return fio;
			}
		
	
	
	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	public void setBio(TestSpeedTaskBIO bio)
	{
		this.bio = bio;
	}

	public TestSpeedTaskBIO getBio() {
		return bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
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
	
}
