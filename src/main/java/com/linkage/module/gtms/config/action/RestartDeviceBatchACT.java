package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.RestartDeviceBatchBIO;

/**
 * @author Administrator
 * 
 */
public class RestartDeviceBatchACT extends splitPageAction implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(RestartDeviceBatchACT.class);
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
	private RestartDeviceBatchBIO bio;
	private String ajax = "";
	private long userId;
	private String gw_type = "";
	// 属地
	private String cityId = "00";
	// 符合条件的LOID
	private static String isSuccess = "1";
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
	/** 宽带账号 */
	private String netusernames = "";
	/** 属地 */
	private String city_ids = "";
	/** 逻辑sn */
	private String loids = "";
	/** oui*/
	private String ouis = "";
	/** 设备序列号*/
	private String devSns = "";
	/** 类型*/
	private String type = "";
	/** 错误账号*/
	private String faultList = "";
	/** 用户id*/
	private String userIds = "";
	/** sqlSpell*/
	private String param;
	private String maxActive;
	/** 任务名称*/
	private String taskName;
	
	private String task_name_query;

	private String status_query;
	/** 开始时间 **/
	private String startOpenDate;
	
	/** 结束时间 **/
	private String endOpenDate;
	
	private List<Map> taskList = new ArrayList<Map>();
	
	private String task_id;
	
	private String task_name;
	
	private List<Map> taskByCityList;
	
	private String city_id;
	
	private String countNum;//查询详情时直接将数量传过来
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private String restartNum;// 每次重启数量
	private String restartInterval;// 重启间隔时间
	

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
	
	
	public String addTaskInfo4CQ()
	{
		long todayCount = bio.getTodayCount();
		if (500000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过50万个";
			return "ajax";
		}
		
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long time = new DateTimeUtil().getLongTime();
							
		ajax = bio.addTask4CQ(time, taskName, accoid, deviceIds, param, restartNum, restartInterval);
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
	
	/**
	 * 定制页面初始化
	 * @return
	 */
	public String init4CQ()
	{
		maxActive = LipossGlobals.getLipossProperty("cqBatchSoftMaxActive");
		return "batch4cq";
	}
	
	
	/**
	 * 管理页面初始化
	 * @return
	 */
	public String initMana4CQ()
	{
		DateTimeUtil dt = new DateTimeUtil();
		this.endtime = dt.getDate();
		this.starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(this.endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 86400L - 1L) * 1000L);
		this.endtime = dt.getLongDate();
		dt = new DateTimeUtil(this.starttime);
		this.starttime = dt.getLongDate();
		return "initMana4cq";
	}
	
	/**
	 * 任务查询
	 * @return
	 */
	public String queryList() {

		taskList = bio.queryTaskList(task_name_query, status_query, startOpenDate, endOpenDate,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryTaskCount(task_name_query, status_query, startOpenDate, endOpenDate,
				curPage_splitPage, num_splitPage);

		return "batchList4cq";
	}
	
	/**
	 * 根据属地分组查询统计情况
	 * @return
	 */
	public String queryTaskGyCity() {
	taskByCityList = bio.queryTaskGyCityList(task_id, task_name);
	return "batchConfigListGyCity4cq";
	}
	
	public String queryDevList4CQ() {
		taskList = bio.queryDevList4CQ(task_id, city_id, type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.queryDevCount4CQ(countNum, curPage_splitPage, num_splitPage);
		return "batchConfigDevList4cq";
	}
	
	/**
	 * 导出任务属地分组列表
	 * @return
	 */
	public String queryTaskGyCityExcel() {
		logger.debug("queryTaskGyCityExcel()");
		taskByCityList = bio.queryTaskGyCityList(task_id, task_name);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "任务名称#属地#总数#成功数#失败数#未做数";
		excelCol = "task_name#city_name#totalNum#succNum#failNum#unDoneNum";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "taskGyCity";
		data = taskByCityList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	/**
	 * 导出设备列表
	 * @return
	 */
	public String queryDevListExcel4CQ() {
		logger.debug("queryDevListExcel4CQ()");
		taskList = bio.queryDevList4CQ(task_id, city_id, type, 0, 0);
		String excelCol = null;
		String excelTitle = null;
		excelTitle = "设备序列号#厂商#型号#区域#版本#执行时间#执行结果#结果描述";
		excelCol = "device_serialnumber#vendor_id#device_model_id#city_name#version#settime#result_id#result_desc";
		
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "devList";
		data = taskList;
		logger.warn("....."+data.toString());
		return "excel";
	}
	
	public String updateCount() {
		int num = bio.updateCount(task_id,type);
		ajax = "ok";
		if(num>400000){
		//if(num>2){
			ajax = "tooMuch";
		}
		
		return "ajax";
	}
	
	public String update() {
		ajax = bio.update(task_id,type);
		return "ajax";
	}
	
	public String del() {
		ajax = bio.del(task_id);
		return "ajax";
	}
	
	public String delCount() {
		int num = bio.delCount(task_id);
		ajax = "ok";
		if(num>400000){
			ajax = "tooMuch";
		}
		
		return "ajax";
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

	public RestartDeviceBatchBIO getBio() {
		return bio;
	}

	public void setBio(RestartDeviceBatchBIO bio) {
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
		RestartDeviceBatchACT.isSuccess = isSuccess;
	}

	public String getStrategy_type() {
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
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

	public String getNetusernames() {
		return netusernames;
	}

	public void setNetusernames(String netusernames) {
		this.netusernames = netusernames;
	}

	public String getCity_ids() {
		return city_ids;
	}

	public void setCity_ids(String city_ids) {
		this.city_ids = city_ids;
	}

	public String getLoids() {
		return loids;
	}
	public void setLoids(String loids) {
		this.loids = loids;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getOuis() {
		return ouis;
	}


	public void setOuis(String ouis) {
		this.ouis = ouis;
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


	public String getUserIds() {
		return userIds;
	}


	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}


	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}




	public String getMaxActive() {
		return maxActive;
	}


	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getTask_name_query() {
		return task_name_query;
	}


	public void setTask_name_query(String task_name_query) {
		this.task_name_query = task_name_query;
	}


	public String getStatus_query() {
		return status_query;
	}


	public void setStatus_query(String status_query) {
		this.status_query = status_query;
	}


	public String getStartOpenDate() {
		return startOpenDate;
	}


	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}


	public String getEndOpenDate() {
		return endOpenDate;
	}


	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}


	public List<Map> getTaskList() {
		return taskList;
	}


	public void setTaskList(List<Map> taskList) {
		this.taskList = taskList;
	}


	public String getTask_id() {
		return task_id;
	}


	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}


	public String getTask_name() {
		return task_name;
	}


	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}


	public List<Map> getTaskByCityList() {
		return taskByCityList;
	}


	public void setTaskByCityList(List<Map> taskByCityList) {
		this.taskByCityList = taskByCityList;
	}


	public String getCity_id() {
		return city_id;
	}


	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}


	public String getCountNum() {
		return countNum;
	}


	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}


	public List<Map> getData() {
		return data;
	}


	public void setData(List<Map> data) {
		this.data = data;
	}


	public String[] getColumn() {
		return column;
	}


	public void setColumn(String[] column) {
		this.column = column;
	}


	public String[] getTitle() {
		return title;
	}


	public void setTitle(String[] title) {
		this.title = title;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getRestartNum() {
		return restartNum;
	}


	public void setRestartNum(String restartNum) {
		this.restartNum = restartNum;
	}


	public String getRestartInterval() {
		return restartInterval;
	}


	public void setRestartInterval(String restartInterval) {
		this.restartInterval = restartInterval;
	}

	
}
