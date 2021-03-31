
package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.OpenDeviceShowPictureBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class OpenDeviceShowPictureACT extends splitPageAction 
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory
			.getLogger(OpenDeviceShowPictureACT.class);
	/**
	 * ************************定制*********************************
	 */
	/** 任务id */
	private String taskId = "";
	/** 任务名 */
	private String taskName = "";
	/** 厂商 */
	private String vendorId = "";
	/** 型号 */
	private String deviceModelIds = "";
	/** 软件版本 */
	private String deviceTypeIds = "";
	/** 下级属地 */
	private String cityIds = "";
	/** 属地 */
	private String cityId = "";
	/** 是否启用IP地址段 */
	private String ipCheck = "1";
	/** 是否启用MAC地址段 */
	private String macCheck = "1";
	/** 是否加入业务用户维度(当业务帐号不需要从WEB这边导入数据库时，这个条件已经没有用) */
	private String custCheck="";
	/** IP地址段 */
	private String ipSG = "";
	/** MAC地址段 */
	private String macSG = "";
	/** 手工输入的用户信息 */
	private String custSG = "";
	/** 节点路径 */
	private String ajax = "";
	
	private InputStream exportExcelStream;
	
	private InputStream exportCustStream ;
	/** 文件路径 */
	private String filePath = "";
	/** 实际上传文件 */
	private File uploadIP;
	/** 上传文件名 */
	private String uploadFileName4IP;
	/** 实际上传文件 */
	private File uploadMAC;
	/** 上传MAC地址段文件名 */
	private String uploadFileName4MAC;
	/** 上传业务用户文件 */
	private File uploadCustomer;
	/** 上传业务用户文件名 */
	private String uploadFileName4Customer;
	
	private File bootFile;
	private File startFile;
	private File authFile;
	
	private String bootFileName;
	private String startFileName;
	private String authFileName;
	
	
	/** 导入or手输IP byBatchIP：导入 */
	private String btnValue4IP;
	/** 导入or手输MAC byBatchMAC 导入 */
	private String btnValue4MAC;
	/** 导入or手输MAC byBatchCustomer 导入 */
	private String btnValue4Customer;
	
	/** 初始化 */
	private List<Map<String, String>> cityList = null;
	/** 厂商 */
	private List vendorList;
	/**
	 * *************************查询********************************
	 */
	// 导出数据
	private String[] title;
	private String[] column;
	private String fileName = "";
	private List data = null;
	
	// 查询开始时间
	private String startTime;
	// 查询结束时间
	private String endTime;
	//定制人
	private String accName;
	
	private List tasklist;
	private List taskResultList = null;
	private List ipList = null;
	private List macList = null;
	private Map<String, String> taskDetailMap = null;
	
	
	private OpenDeviceShowPictureBIO bio;
	private String isLocked;

	/**
	 * @author 
	 * @return
	 */
	public String initBatchConfig()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		this.vendorList = bio.getVendorList();
		return "initOpenDevice";
	}
	/**
	 * @author chenxj
	 * @return
	 */
	  public String initForEdition()
	  {
	    UserRes curUser = WebUtil.getCurrentUser();
	    this.cityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
	    this.vendorList = this.bio.getVendorList();
	    return "initForEdition";
	  }
	
	/**
	 * 根据vendorId查询型号
	 * 
	 * @return
	 */
	public String getDeviceModel()
	{
		ajax = bio.getDeviceModel(vendorId);
		return "ajax";
	}
	
	/**
	 * 根据cityId查询下级属地
	 * 
	 * @return
	 */
	public String getNextCity()
	{
		ajax = bio.getNextCity(cityId);
		return "ajax";
	}

	/**
	 * 根据型号查询年版本
	 * 
	 * @return
	 */
	public String getSoftVersion()
	{
		ajax = bio.getSoftVersion(deviceModelIds);
		return "ajax";
	}

	/**
	 * 机顶盒任务定制
	 * @return
	 */
	public String importConfig()
	{
		logger.warn("importConfig([cityId={}],[vendorId={}],[taskName={}],[deviceModelIds={}],[devicetypeids={}],[cityIds={}],[custCheck={}])", new Object[] { cityId, vendorId,
				taskName, deviceModelIds, deviceTypeIds,cityIds,custCheck});
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		long currTime = new Date().getTime() / 1000L;
		long taskId = currTime;
		long addTime = currTime;
		if (btnValue4IP.equals("byBatchIP"))
		{
			ipSG = getIPString();
		}
		if (btnValue4MAC.equals("byBatchMAC"))
		{
			macSG = getMACString();
		}
		if(btnValue4Customer.equals("byBatchCust")){
			custSG = "";
		}else{
			
		}
		if(cityIds != null && !"".equals(cityIds)){
			cityId = cityIds;
		}
		ajax = bio.OpenDeviceShowPicConfig(taskId, cityId, vendorId, ipCheck, macCheck, taskName,
				acc_oid, addTime, ipSG, macSG, deviceModelIds, deviceTypeIds,custCheck,custSG,uploadIP,uploadFileName4IP,
				uploadMAC,uploadFileName4MAC,uploadCustomer,uploadFileName4Customer,bootFile,startFile,authFile,bootFileName,
				startFileName,authFileName,isLocked);
		//ajax = "任务定制成功!";
		return "ajax";
	}
	/**
	 
	 * 
	 * @author
	 * @date 2014年6月26日 
	 * @param
	 * @return String
	 */
	public String initImport()
	{
		logger.debug("initImport");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		String cityId = StringUtil.getStringValue(curUser.getCityId());
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, cityId, taskName,acc_oid,accName,isLocked);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage,
				num_splitPage,startTime, endTime, cityId, taskName,acc_oid,accName,isLocked);
		return "initImport";
	}

	/**
	 * 查看结果
	 * 2014年6月26日 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getShowPictureConfigResult()
	{
		taskResultList = bio.getTaskResult(taskId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		return "taskResult";
	}
	/**
	 * 下载获取设备详细信息
	 * @return
	 */
	public String exportShowDeviceExcelList(){
		logger.warn("exportShowDeviceExcelList()");
		title = new String[]{"属地","厂商","硬件型号","软件版本","设备序列号","业务帐号","ip地址","mac地址","配置结果","操作时间"};
		column = new String[]{"cityName","vendorName","deviceModel","deviceTypeName","deviceSerialnumber","serv_account","loopback_ip","cpe_mac","load_status","update_time"};
		fileName = "开机画面结果";
		data =bio.exportShowDeviceExcelList(taskId);
		return "excel";
	}
	/**
	 * 查看详细
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getShowPictureConfigDetail()
	{
		taskDetailMap = bio.getTaskDetail(taskId);
		ipList = bio.getIpList(taskId);
		macList = bio.getMacList(taskId);
		return "taskDetail";
	}
	public String doDelete(){
		logger.warn("doDelete()");
	    ajax = bio.doDelete(taskId);
		return "ajax";
	}
	
	/**
	 * 解析导入文件
	 * 
	 * @return
	 */
	public String getIPString()
	{
		logger.warn("getIPList");
		String targetDirectory = ServletActionContext.getServletContext().getRealPath(
				"/accountFile");
		return bio.getIPString(targetDirectory, uploadFileName4IP, uploadIP);
	}

	/**
	 * 解析导入文件
	 * 
	 * @return
	 */
	public String getMACString()
	{
		logger.warn("getMACList");
		String targetDirectory = ServletActionContext.getServletContext().getRealPath(
				"/accountFile");
		return bio.getIPString(targetDirectory, uploadFileName4MAC, uploadMAC);
	}
	/**
	 * 下载模版
	 * 
	 * @return
	 */
	public String downloadTemplate()
	{
		return "toExportAdd";
	}
	/**
	 * 下载模版
	 * 
	 * @return
	 */
	public String downloadTemplateCust()
	{
		return "toExportCust";
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getDeviceModelIds()
	{
		return deviceModelIds;
	}

	public void setDeviceModelIds(String deviceModelIds)
	{
		this.deviceModelIds = deviceModelIds;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public List getVendorList()
	{
		return vendorList;
	}

	public void setVendorList(List vendorList)
	{
		this.vendorList = vendorList;
	}

	public void setBio(OpenDeviceShowPictureBIO bio)
	{
		this.bio = bio;
	}

	public String getDeviceTypeIds()
	{
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(String deviceTypeIds)
	{
		this.deviceTypeIds = deviceTypeIds;
	}	
	
	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getIpCheck()
	{
		return ipCheck;
	}

	public void setIpCheck(String ipCheck)
	{
		this.ipCheck = ipCheck;
	}

	public String getMacCheck()
	{
		return macCheck;
	}

	public void setMacCheck(String macCheck)
	{
		this.macCheck = macCheck;
	}

	public String getIpSG()
	{
		return ipSG;
	}

	public void setIpSG(String ipSG)
	{
		this.ipSG = ipSG;
	}

	public String getMacSG()
	{
		return macSG;
	}

	public void setMacSG(String macSG)
	{
		this.macSG = macSG;
	}


	public File getUploadMAC()
	{
		return uploadMAC;
	}

	public void setUploadMAC(File uploadMAC)
	{
		this.uploadMAC = uploadMAC;
	}

	public String getBtnValue4IP()
	{
		return btnValue4IP;
	}

	public void setBtnValue4IP(String btnValue4IP)
	{
		this.btnValue4IP = btnValue4IP;
	}

	public String getBtnValue4MAC()
	{
		return btnValue4MAC;
	}

	public void setBtnValue4MAC(String btnValue4MAC)
	{
		this.btnValue4MAC = btnValue4MAC;
	}

	public File getUploadIP()
	{
		return uploadIP;
	}

	public void setUploadIP(File uploadIP)
	{
		this.uploadIP = uploadIP;
	}

	public String getUploadFileName4IP()
	{
		return uploadFileName4IP;
	}

	public void setUploadFileName4IP(String uploadFileName4IP)
	{
		byte[] tempName;
		String newName = "";
		try
		{
			tempName = uploadFileName4IP.getBytes("GBK");
			newName = new String(tempName, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		this.uploadFileName4IP = newName;
	}

	public String getUploadFileName4MAC()
	{
		return uploadFileName4MAC;
	}

	public void setUploadFileName4MAC(String uploadFileName4MAC)
	{
		byte[] tempName;
		String newName = "";
		try
		{
			tempName = uploadFileName4MAC.getBytes("GBK");
			newName = new String(tempName, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		this.uploadFileName4MAC = newName;
	}

	public void setExportExcelStream(InputStream exportExcelStream)
	{
		this.exportExcelStream = exportExcelStream;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCustCheck() {
		return custCheck;
	}

	public void setCustCheck(String custCheck) {
		this.custCheck = custCheck;
	}

	public String getCustSG() {
		return custSG;
	}

	public void setCustSG(String custSG) {
		this.custSG = custSG;
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

	public File getBootFile() {
		return bootFile;
	}

	public void setBootFile(File bootFile) {
		this.bootFile = bootFile;
	}

	public File getStartFile() {
		return startFile;
	}

	public void setStartFile(File startFile) {
		this.startFile = startFile;
	}

	public File getAuthFile() {
		return authFile;
	}

	public void setAuthFile(File authFile) {
		this.authFile = authFile;
	}

	public String getBootFileName() {
		return bootFileName;
	}

	public void setBootFileName(String bootFileName) {
		this.bootFileName = bootFileName;
	}

	public String getStartFileName() {
		return startFileName;
	}

	public void setStartFileName(String startFileName) {
		this.startFileName = startFileName;
	}

	public String getAuthFileName() {
		return authFileName;
	}

	public void setAuthFileName(String authFileName) {
		this.authFileName = authFileName;
	}

	public String getBtnValue4Customer() {
		return btnValue4Customer;
	}

	public void setBtnValue4Customer(String btnValue4Customer) {
		this.btnValue4Customer = btnValue4Customer;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
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
	
	public List getTasklist() {
		return tasklist;
	}

	public void setTasklist(List tasklist) {
		this.tasklist = tasklist;
	}

	public List getTaskResultList() {
		return taskResultList;
	}

	public void setTaskResultList(List taskResultList) {
		this.taskResultList = taskResultList;
	}

	public List getIpList() {
		return ipList;
	}

	public void setIpList(List ipList) {
		this.ipList = ipList;
	}

	public List getMacList() {
		return macList;
	}

	public void setMacList(List macList) {
		this.macList = macList;
	}

	public Map<String, String> getTaskDetailMap() {
		return taskDetailMap;
	}

	public void setTaskDetailMap(Map<String, String> taskDetailMap) {
		this.taskDetailMap = taskDetailMap;
	}

	// 下载模版
	public InputStream getExportExcelStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "stb" + separa + "config" + separa
				+ "batchPicUPtemplate.xls";
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
	// 下载模版exportCustStream
	public InputStream getExportCustStream()
	{
		FileInputStream fio = null;
		String path = ServletActionContext.getServletContext().getRealPath("/");
		String separa = System.getProperty("file.separator");
		String filePath = path + "stb" + separa + "config" + separa
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

	public void setExportCustStream(InputStream exportCustStream) {
		this.exportCustStream = exportCustStream;
	}

	public String getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}
	
}
