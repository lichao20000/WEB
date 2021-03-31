package com.linkage.module.gtms.stb.resource.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.BatchRebootBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({"rawtypes"})
public class BatchRebootACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(BatchRebootACT.class);
	private Map session;
	private HttpServletRequest request;
	private String ajax;
	private BatchRebootBIO bio;
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "00";
	private List vendorList;
	private String vendorId = "-1";
	private String deviceTypeIds = "";
	/** 数据类型，0：范围定制，1：导入业务账号，2：导入mac*/
	private String data_type;
	private String taskId;
	private Map taskResultMap = null;
	private List taskResultMapList = null;
	/**湖南联通特制 	传入参数showType；
	 * 1：展示查询信息，以及“查看结果”、“查看详细”；
	 * 2：在1的基础上增加“激活\失效”按钮权限；
	 * 3：在2的基础上增加“删除”按钮权限。*/
	private String showType="3";
	
	private String status;
	private List resultList;
	private File file;// 实际上传文件

	// 任务详细
	private String taskDetail;
	// 任务简述
	private String taskDesc;
	// 导出数据
	private String[] title;
	private String[] column;
	private String fileName = "";
	private List data = null;
	


	/**
	 * 定制初始化页面
	 * @return
	 */
	public String addInit()
	{
		cityList = CityDAO.getNextCityListByCityPidCore("00");
		vendorList = bio.getVendor();
		return "init";
	}
	
	/**
	 * 根据厂商获取目标版本
	 */
	public String getTargetVersion() 
	{
		ajax = bio.getTargetVersion(vendorId);
		return "ajax";
	}
	
	/**
	 * 任务范围定制,任务导入定制
	 * @return
	 */
	public String addTask()
	{
		LOG.warn("addTask({},{},{},{},{},{},{})",
				new Object[]{cityId,vendorId,deviceTypeIds,
				data_type,file,taskDesc,taskDetail});
		
		//任务入库结果，默认失败
		int res=0;
		ajax= "任务定制失败！";
		
		long taskId=System.currentTimeMillis()/1000L;
		String filePath = "";
		//日志描述
		String desc="";
		if("1".equals(data_type) || "2".equals(data_type))
		{
			vendorId=null;
			deviceTypeIds=null;
			filePath = "/temp";
			
			String targetDirectory = "";
			String targetFileName = "";
			@SuppressWarnings("unused")
			HttpServletRequest request = null;
			try {
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext()
										.getRealPath(filePath);
				if("1".equals(data_type)){
					targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS()
							+ "_servaccount_"+ taskId+".xls";
					desc="批量导入业务账号重启任务定制["+taskId+"]";
				}else{
					targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS()
							+ "_mac_"+ taskId+".xls";
					desc="批量导入mac地址重启任务定制["+taskId+"]";
				}
				
				File target = new File(targetDirectory, targetFileName);
				FileUtils.copyFile(file, target);
			} catch (IOException e) {
				LOG.error("批量导入重启，上传文件时出错");
				return "addTaskResult";
			}
			
//			filePath = LipossGlobals.getLipossProperty("batchReboot") + request.getContextPath()  
//								+ filePath+ "/" + targetFileName;
			filePath = LipossGlobals.getLipossProperty("batchReboot")
								+ filePath+ "/" + targetFileName;
		}
		
		long  user_id=((UserRes) session.get("curUser")).getUser().getId();
		String user_ip=request.getRemoteHost();
		
		res=bio.addTask(user_id,taskId,cityId,vendorId,deviceTypeIds,
				data_type,filePath,taskDesc,taskDetail);
		if (res == 1) {
			ajax= "任务定制成功!";
		}
		
		bio.addLog(user_id,user_ip,desc,res+"");
		return "addTaskResult";
	}
	
	/**
	 * 任务查询
	 * @return
	 */
	public String queryInit()
	{
		LOG.warn("queryInit({},{},{},{},{})",
				curPage_splitPage,num_splitPage,cityId,vendorId,status);
		cityList = CityDAO.getNextCityListByCityPidCore("00");
		vendorList = bio.getVendor();
		
		data = bio.getTask(curPage_splitPage,num_splitPage,cityId,vendorId,status);
		maxPage_splitPage = bio.countTask(num_splitPage,cityId,vendorId,status);
		return "list";
	}
	
	/**
	 * 异常账号数
	 * @return
	 */
	public String getErrData()
	{
		LOG.warn("getErrData({})",taskId);
		ajax=bio.getErrData(taskId);
		return "ajax";
	}
	
	/**
	 * 激活、取消、删除任务
	 */
	public String updateTask() 
	{
		LOG.warn("updateTask({},{})",taskId, status);
		ajax=bio.updateTask(taskId, status);
		
		long user_id=((UserRes) session.get("curUser")).getUser().getId();
		String user_ip=request.getRemoteHost();
		String desc="";
		if("-2".equals(status)){
			desc="批量机顶盒重启任务["+taskId+"]失效";
		}else if("1".equals(status)){
			desc="批量机顶盒重启任务["+taskId+"]激活";
		}else if("2".equals(status)){
			desc="批量机顶盒重启任务["+taskId+"]删除";
		}
		
		bio.addLog(user_id,user_ip,desc,ajax.split(",")[0]);
		LOG.warn("updateTask result:"+ajax);
		return "ajax";
	}
	
	/**
	 * 任务详细
	 * @return
	 */
	public String getTaskInfo()
	{
		LOG.warn("getTaskInfo({})",taskId);
		taskResultMap=bio.getTaskInfo(taskId);
		data_type=StringUtil.getStringValue(taskResultMap,"data_type");
		return "taskInfo";
	}
	
	/**
	 * 导入的账号数据
	 * @return
	 */
	public String exportTaskServAccount()
	{
		LOG.warn("exportTaskServAccount({},{},{})",taskId,data_type,status);
		if("1".equals(data_type)){
			title = new String[]{"业务账号"};
			column = new String[]{"serv_account"};
		}else if("2".equals(data_type)){
			title = new String[]{"MAC地址"};
			column = new String[]{"mac"};
		}
		
		fileName = "导入账号_"+taskId;
		if("-2".equals(status)){
			fileName = "异常账号_"+taskId;
		}
		
		data=bio.exportTaskServ(taskId,data_type,status);
		return "excel";
	}
	
	/**
	 * 结果统计
	 * @return
	 */
	public String count()
	{
		LOG.warn("count({})",taskId);
		data=bio.getCount(taskId);
		return "count";
	}
	
	/**
	 * 结果导出
	 * @return
	 */
	public String exportTaskResult()
	{
		LOG.warn("exportTaskResult({})",taskId);
		title = new String[]{"属地", "任务触发成功", "待触发", 
								"不支持", "小计","总占比量", "成功占比"};
		column = new String[]{"city_name","suss","unDo",
								"noDo","count","countPerc","sussPerc"};
		fileName = "批量重启统计结果";
		data=bio.getCount(taskId);
		return "excel";
	}
	
	/**
	 * 设备清单
	 * @return
	 */
	public String queryDevList()
	{
		LOG.warn("queryDevList({},{},{},{},{})",
				curPage_splitPage,num_splitPage,taskId,cityId,status);
		data = bio.getDevList(taskId,cityId,status,curPage_splitPage,num_splitPage);
		maxPage_splitPage = bio.countDevList(taskId,cityId,status,num_splitPage);
		return "devList";
	}
	
	/**
	 * 设备清单导出
	 * @return
	 */
	public String exportDevList()
	{
		LOG.warn("exportDevList({})",taskId);
		title = new String[]{"属地","APK版本","设备序列号","业务账号",
				"MAC地址","厂商","型号","硬件版本","软件版本"};
		column = new String[]{"cityName","version","device_serialnumber","serv_account",
				"mac","vendorName","deviceModel","hardwareversion","softwareversion"};
		fileName = "批量重启设备清单";
		data = bio.getDevList(taskId,cityId,status,-1,-1);
		return "excel";
	}
	
	

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public BatchRebootBIO getBio() {
		return bio;
	}

	public void setBio(BatchRebootBIO bio) {
		this.bio = bio;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List getVendorList() {
		return vendorList;
	}

	public void setVendorList(List vendorList) {
		this.vendorList = vendorList;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map getTaskResultMap() {
		return taskResultMap;
	}

	public void setTaskResultMap(Map taskResultMap) {
		this.taskResultMap = taskResultMap;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(String deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getTaskDetail() {
		return taskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
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

	public List getTaskResultMapList() {
		return taskResultMapList;
	}

	public void setTaskResultMapList(List taskResultMapList) {
		this.taskResultMapList = taskResultMapList;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
