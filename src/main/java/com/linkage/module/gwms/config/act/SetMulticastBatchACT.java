package com.linkage.module.gwms.config.act;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.bio.SetMulticastBatchBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator
 * 
 */
public class SetMulticastBatchACT extends splitPageAction implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SetMulticastBatchACT.class);
	// 传参
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	private String gwShare_fileName = "";
	private String gwShare_cityId = "";
	// 查询方式
	private String gwShare_queryField = "";
	private int total;
	// 查询参数
	private String gwShare_queryParam = "";
	private String gwShare_msg = "";
	// 查询宽带帐号
	private List<HashMap<String, String>> deviceList;
	private SetMulticastBatchBIO bio;
	private String ajax = "";
	private long userId;
	private String gw_type = "";
	// 属地
	private String cityId = "00";
	// 符合条件的LOID
	private static String isSuccess = "1";
	// 3:终端重启; 4:下次连接到系统
	private String strategy_type = "";
	// 状态
	private String status = "";
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 查询列表
	private List<Map> list = new ArrayList<Map>();
	/** 获取device_id */
	private List<Map> device_idList = null;
	/** 条件内容 */
	private String condition = "";
	/** 开通状态 */
	private String openState = "";
	/** 条件 */
	private String con = "";
	/** 属地 */
	private String city_ids = "";
	/** 设备序列号*/
	private String devSns = "";
	/** 类型*/
	private String type = "";
	/** 错误账号*/
	private String faultList = "";
	/** 任务名称*/
	private String taskname;
	/** 导入账号*/
	private String fileUsernames;
	
	/** 如下查询用 */
	/** 任务id */
	private String taskId = "";
	/** 任务 */
	private List tasklist;
	/** 查询结束时间 */
	private String startTime;
	/** 查询结束时间 */
	private String endTime;
	/** 定制人 */
	private String accName;
	/** 任务结果 */
	private List taskResultList = null;
	/** 任务详细信息 */
	private Map<String, String> taskDetailMap = null;
	
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	// sqlSpell
	private String param;
	
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	
	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll() throws IOException {
		logger.warn("SetMulticastBatchACT.doConfigAll()");
		
		long todayCount = bio.getTodayCount();
		if (100000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过10万个";
			return "ajax";
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();

		if (StringUtil.IsEmpty(city_ids) || StringUtil.IsEmpty(deviceIds) || StringUtil.IsEmpty(devSns) || StringUtil.IsEmpty(fileUsernames)) {
			ajax = "用户信息为空";
			return "ajax";
		}

		city_ids = city_ids.replace("\n", "").trim();
		deviceIds = deviceIds.replace("\n", "").trim();
		devSns = devSns.replace("\n", "").trim();
		fileUsernames = fileUsernames.replace("\n", "").trim();

		if (city_ids.contains(",") && deviceIds.contains(",") && devSns.contains(",") && fileUsernames.contains(",")) { // 多个设备
			String[] city_idsArr = city_ids.split(",");
			String[] deviceIdArr = deviceIds.split(",");
			String[] devSnsArr = devSns.split(",");
			String[] fileUsernamesArr = fileUsernames.split(",");

			for (int i = 0; i < deviceIdArr.length; i++) {
				city_idsArr[i] = StringUtil.getStringValue(city_idsArr[i]).replace("\n", "").trim();
				deviceIdArr[i] = StringUtil.getStringValue(deviceIdArr[i]).replace("\n", "").trim();
				devSnsArr[i] = StringUtil.getStringValue(devSnsArr[i]).replace("\n", "").trim();
				fileUsernamesArr[i] = StringUtil.getStringValue(fileUsernamesArr[i]).replace("\n", "").trim();
				
				list.add(city_idsArr[i] + "##" + deviceIdArr[i] + "##" + devSnsArr[i] + "##" + fileUsernamesArr[i]);
			}
		} else { // 单个设备
			list.add(city_ids + "##" + deviceIds + "##" + devSns + "##" + fileUsernames);
		}
		ajax = bio.doConfig(curUser, list, type, faultList, taskname);
		return "ajax";
	}


	public void setTime() {
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}

	/**
	 * 查询设备（带列表）
	 * 
	 * @return
	 */
	public String queryDeviceList() {
		logger.debug("SetMulticastBatchACT=>getDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_fileName.trim();
		}
		List<HashMap<String, String>> deviceListTmp = bio.getDeviceList(curUser, gwShare_cityId, gwShare_fileName);
		if (null == deviceListTmp || deviceListTmp.size() < 1) {
			this.gwShare_msg = bio.getMsg();
		}else{
			this.faultList = deviceListTmp.get(deviceListTmp.size()-1).get("faultMap");
			this.deviceList = new ArrayList<HashMap<String, String>>();
			for(int i=0;i<deviceListTmp.size()-1;i++){
				HashMap<String, String> map = deviceListTmp.get(i);
				this.deviceList.add(map);
			}
		}
		
		total = deviceList == null ? 0 : this.deviceList.size();
		return "shareList0";
	}
	
	/** 如下查询用*/
	public String initImport()
	{
		logger.debug("initImport");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		tasklist = bio.getOrderTaskList(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname, acc_oid, accName);
		maxPage_splitPage = bio.countOrderTask(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname, acc_oid, accName);
		return "initImport";
	}
	
	
	/**
	 * 查看结果
	 * @return
	 */
	public String getShowSetMulticastBatchTaskResult()
	{
		taskResultList = bio.getTaskResult(taskId,curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countDeviceTask(curPage_splitPage, num_splitPage, taskId);
		return "taskResult";
	}
	
	/**
	 * 查看详细
	 * @return
	 */
	public String getShowSetMulticastBatchTaskDetail()
	{
		taskDetailMap = bio.getTaskDetail(taskId);
		return "taskDetail";
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
	
	
	public String getInfoExcelDevice() throws Exception
	{
		logger.debug("getInfoExcel()");
		this.setTime();
		title = new String[7];
		title[0] = "设备序列号";
		title[1] = "属地";
		title[2] = "定制时间";
		title[3] = "执行时间";
		title[4] = "下发结果";
		data = bio.getInfoExcelDevice(taskId);
		column = new String[7];
		column[0] = "device_serialnumber";
		column[1] = "city_id";
		column[2] = "add_time";
		column[3] = "update_time";
		column[4] = "res";
		fileName = "setMulticastResult";
		return "excel";
	}
	
	
	public String addTaskInfo4NX(){
		
		long todayCount = bio.getTodayCount();
		if (500000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过50万个";
			return "ajax";
		}
		
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long time = new DateTimeUtil().getLongTime();
		
		ajax = bio.addTask4NX(taskname, time, accoid, deviceIds, param, strategy_type);
		if("true".equals(ajax))
		{
			ajax = "批量定制任务执行成功！";
		} 
		else if ("false".equals(ajax))
		{
			ajax = "批量定制任务执行失败！";
		}
		else if("false10000".equals(ajax))
		{
			ajax = "定制设备超过10000条，定制失败！";
		}
		
		return "ajax";
	}
	
	public String queryModifyVlanInfo()
	  {
	    logger.debug("queryModifyVlanInfo()");
	    UserRes curUser = (UserRes)this.session.get("curUser");
	    this.cityId = curUser.getCityId();
	    this.data = this.bio.queryModifyVlanInfo(this.type,this.cityId);
	    logger.debug("queryModifyVlanInfo(data):{}", this.data);
	    return "shouRefreshInfoList";
	  }

	  public String getInfoExcel() {
	    this.data = this.bio.queryModifyVlanInfo(this.type,this.cityId);
	    this.fileName = "修改IPTV组播参数统计";
	    this.title = new String[] { "属地", "总配置数", "成功", "未触发", "失败" };
	    this.column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum" };
	    return "excel";
	  }

	  public String getDev()
	  {
	    logger.debug("getDev()");
	    this.data = this.bio.getDev(this.type, this.status, this.cityId, this.curPage_splitPage, this.num_splitPage);
	    this.maxPage_splitPage = this.bio.getDevCount(this.type, this.status, this.cityId, this.curPage_splitPage, this.num_splitPage);
	    return "devList";
	  }

	  public String getDevExcel() {
	    logger.debug("getDevExcel()");
	    this.fileName = "修改IPTV组播参数统计";
	    this.data = this.bio.getDevExcel(this.type, this.cityId, this.status);
	    this.title = new String[] { "属 地", "厂商", "型号", "版本", "设备序列号", "IP或域", "失败原因" };
	    this.column = new String[] { "city_name", "vendor_add", "device_model", "softwareversion", "device", "loopback_ip", 
	      "fault_desc" };
	    return "excel";
	  }
	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		starttime = dt.getDate();
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil(start_time * 1000);
		starttime = dt.getLongDate();
		dt = new DateTimeUtil((start_time+24*3600-1) * 1000);
		endtime = dt.getLongDate();
		return "init";
	}
	/**
	 * 统计结果查询
	 * 
	 * @return
	 */
	public String queryModifyVlanInfoByTime() {
		logger.debug("queryModifyVlanInfo()");
		this.setTime2();
		data = bio.queryModifyVlanInfo(starttime1, endtime1, type, cityId);
		logger.debug("queryModifyVlanInfo(data):{}",data);
		return "shouRefreshInfoListByTime";
	}

	public String getInfoExcelByTime() {
		this.setTime2();
		data = bio.queryModifyVlanInfo(starttime1, endtime1, type, cityId);
		this.fileName = "修改IPTV组播参数统计";
		title = new String[] { "属地", "总配置数", "成功", "未触发", "失败" };
		column = new String[] { "city_name", "allup", "successnum", "noupnum", "failnum" };
		return "excel";
	}

	/**
	 * 结果详情页带时间
	 * 
	 * @return
	 */
	public String getDevByTime() {
		logger.debug("getDev()");
		this.setTime2();
		data = bio.getDev(type, status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
		maxPage_splitPage = bio.getDevCount(type, status, cityId, curPage_splitPage, num_splitPage, starttime1, endtime1);
		logger.debug("getDev(data):{}",data);
		return "devListByTime";
	}

	public String getDevExcelByTime() {
		logger.debug("getDevExcel()");
		this.setTime2();
		this.fileName = "修改IPTV组播参数统计";
		data = bio.getDevExcel(type, cityId, status, starttime1, endtime1);
		title = new String[] { "属 地", "厂商", "型号", "版本", "LOID", "宽带账号", "设备序列号", "IP或域", "失败原因" };
		column = new String[] { "city_name", "vendor_add", "device_model", "softwareversion", "loid", "serv_account","device", "loopback_ip",
				"fault_desc" };
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime2()
	{
		logger.debug("setTime2()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public SetMulticastBatchBIO getBio() {
		return bio;
	}

	public void setBio(SetMulticastBatchBIO bio) {
		this.bio = bio;
	}

	public String getGwShare_queryField() {
		return gwShare_queryField;
	}

	public void setGwShare_queryField(String gwShare_queryField) {
		this.gwShare_queryField = gwShare_queryField;
	}

	public String getGwShare_queryParam() {
		return gwShare_queryParam;
	}

	public void setGwShare_queryParam(String gwShare_queryParam) {
		this.gwShare_queryParam = gwShare_queryParam;
	}

	public List<HashMap<String, String>> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<HashMap<String, String>> deviceList) {
		this.deviceList = deviceList;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public static String getIsSuccess() {
		return isSuccess;
	}

	public static void setIsSuccess(String isSuccess) {
		SetMulticastBatchACT.isSuccess = isSuccess;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public List<Map> getList() {
		return list;
	}

	public void setList(List<Map> list) {
		this.list = list;
	}

	public List<Map> getDevice_idList() {
		return device_idList;
	}

	public void setDevice_idList(List<Map> device_idList) {
		this.device_idList = device_idList;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOpenState() {
		return openState;
	}

	public void setOpenState(String openState) {
		this.openState = openState;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getGwShare_msg() {
		return gwShare_msg;
	}

	public void setGwShare_msg(String gwShare_msg) {
		this.gwShare_msg = gwShare_msg;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getGwShare_cityId() {
		return gwShare_cityId;
	}

	public void setGwShare_cityId(String gwShare_cityId) {
		this.gwShare_cityId = gwShare_cityId;
	}

	public String getCity_ids() {
		return city_ids;
	}

	public void setCity_ids(String city_ids) {
		this.city_ids = city_ids;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDevSns() {
		return devSns;
	}

	public void setDevSns(String devSns) {
		this.devSns = devSns;
	}

	public String getFaultList() {
		return faultList;
	}

	public void setFaultList(String faultList) {
		this.faultList = faultList;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getFileUsernames() {
		return fileUsernames;
	}

	public void setFileUsernames(String fileUsernames) {
		this.fileUsernames = fileUsernames;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
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


	public Map<String, String> getTaskDetailMap() {
		return taskDetailMap;
	}


	public void setTaskDetailMap(Map<String, String> taskDetailMap) {
		this.taskDetailMap = taskDetailMap;
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


	public List<Map> getData() {
		return data;
	}


	public void setData(List<Map> data) {
		this.data = data;
	}


	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}


	public String getStrategy_type() {
		return strategy_type;
	}


	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
	}


	
	
}
