package com.linkage.module.gtms.config.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.StackRefreshToolsBIO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Administrator
 * 
 */
public class StackRefreshToolsACT extends splitPageAction implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(StackRefreshToolsACT.class);
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
	private StackRefreshToolsBIO bio;
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
	private String countMax;
	private long countMaxL;
	private long upMaxL;
	private String upMax;
	private String maxActive;
	/** 任务名称*/
	private String taskName;
	/** 定制的WAN: 1:宽带；2:iptv*/
	private String task_wan_type;
	/** 参数路径*/
	private String paramPath;
	/** 参数值*/
	private String paramValue;
	/** 参数类型*/
	private String paramType;
	
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
	
	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll() throws IOException {
		logger.warn("StackRefreshToolsACT.doConfigAll()");
		getMaxLong();
		long todayCount = bio.getTodayCount();
		//if (30000 < todayCount) {
		logger.warn("countMax:{}",countMaxL);
		if (countMaxL < todayCount) {
			ajax = "今日执行用户数已满，不能超过3万个";
			return "ajax";
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		List<String> list = new ArrayList<String>();

		if (StringUtil.IsEmpty(deviceIds) || StringUtil.IsEmpty(netusernames) || StringUtil.IsEmpty(city_ids)	
				|| StringUtil.IsEmpty(loids) || StringUtil.IsEmpty(ouis) || StringUtil.IsEmpty(devSns) || StringUtil.IsEmpty(userIds)) {
			ajax = "用户信息为空";
			return "ajax";
		}

		deviceIds = deviceIds.replace("\n", "").trim();
		netusernames = netusernames.replace("\n", "").trim();
		city_ids = city_ids.replace("\n", "").trim();
		loids = loids.replace("\n", "").trim();
		ouis = ouis.replace("\n", "").trim();
		devSns = devSns.replace("\n", "").trim();
		userIds = userIds.replace("\n", "").trim();

		if (deviceIds.contains(",") && netusernames.contains(",") && city_ids.contains(",") 
				&& loids.contains(",") && ouis.contains(",") && devSns.contains(",") && userIds.contains(",")) { // 多个设备
			String[] deviceIdArr = deviceIds.split(",");
			String[] netusernamesArr = netusernames.split(",");
			String[] city_idsArr = city_ids.split(",");
			String[] loidsArr = loids.split(",");
			String[] ouisArr = ouis.split(",");
			String[] devSnsArr = devSns.split(",");
			String[] userIdsArr = userIds.split(",");

			for (int i = 0; i < deviceIdArr.length; i++) {
				deviceIdArr[i] = StringUtil.getStringValue(deviceIdArr[i]).replace("\n", "").trim();
				netusernamesArr[i] = StringUtil.getStringValue(netusernamesArr[i]).replace("\n", "").trim();
				city_idsArr[i] = StringUtil.getStringValue(city_idsArr[i]).replace("\n", "").trim();
				loidsArr[i] = StringUtil.getStringValue(loidsArr[i]).replace("\n", "").trim();
				ouisArr[i] = StringUtil.getStringValue(ouisArr[i]).replace("\n", "").trim();
				devSnsArr[i] = StringUtil.getStringValue(devSnsArr[i]).replace("\n", "").trim();
				userIdsArr[i] = StringUtil.getStringValue(userIdsArr[i]).replace("\n", "").trim();
				
				list.add(deviceIdArr[i] + "##" + netusernamesArr[i] + "##" + city_idsArr[i] 
						+ "##" + loidsArr[i]+ "##" + ouisArr[i] + "##" + devSnsArr[i] + "##" + userIdsArr[i]);
			}
		} else { // 单个设备
			list.add(deviceIds + "##" + netusernames + "##" + city_ids + "##" + loids + "##" + ouis + "##" + devSns + "##" + userIds);
		}
		ajax = bio.doConfig(curUser, list, type, faultList);
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
		logger.debug("StackRefreshToolsACT=>getDeviceList()");
		UserRes curUser = (UserRes) session.get("curUser");
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_cityId.trim();
		} else {
			this.gwShare_cityId = curUser.getCityId();
		}
		if (!StringUtil.IsEmpty(gwShare_cityId)) {
			gwShare_fileName.trim();
		}
		//List<HashMap<String, String>> deviceListTmp = bio.getDeviceList(curUser, gwShare_cityId, gwShare_fileName, faultList);
		getMaxLong();
		List<HashMap<String, String>> deviceListTmp = bio.getDeviceList(curUser, gwShare_cityId, gwShare_fileName, faultList,countMaxL,upMaxL);
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
	
	
	
	private void getMaxLong() {
		if(StringUtil.IsEmpty(countMax) || "null".equals(countMax)){
			countMaxL = 30000l;
		}else{
			countMaxL = Long.valueOf(countMax);
		}
		if(StringUtil.IsEmpty(upMax) || "null".equals(upMax)){
			upMaxL = 2000l;
		}else{
			upMaxL = Long.valueOf(upMax);
			
		}
	}


	/**
	 * 配置
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doConfigAll4JX() throws IOException {
		logger.warn("StackRefreshToolsACT.doConfigAll4JX()");
		
		long todayCount = bio.getTodayCount();
		if (1000000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过100万个";
			return "ajax";
		}
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		
		if (StringUtil.IsEmpty(gwShare_fileName) || gwShare_fileName.length() < 4) {
			ajax =  "上传的文件名不正确！";
			return "ajax";
		}
		String fileName_ = gwShare_fileName.substring(gwShare_fileName.length() - 3, gwShare_fileName.length());
		if (!"xls".equals(fileName_) && !"txt".equals(fileName_)) {
			ajax = "上传的文件格式不正确！";
			return "ajax";
		}
		int count = 0;
		try {
			if ("txt".equals(fileName_)) {
				count = bio.getImportDataByTXT4JX(gwShare_fileName);
			} else {
				count = bio.getImportDataByXLS4JX(gwShare_fileName);
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", gwShare_fileName);
			ajax = "文件没找到！";
			return "ajax";
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "文件解析出错！";
			return "ajax";
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", gwShare_fileName);
			ajax = "文件解析出错！";
			return "ajax";
		}

		if (count > 30000){
			ajax = "文件行数不要超过30000行";
			return "ajax";
		}
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String filePath = "http://" + request.getLocalAddr() + ":" + request.getServerPort() + "/itms/temp/" + gwShare_fileName;
		logger.warn("上传文件路径[{}]", filePath);
		
		ajax = bio.doConfig4JX(curUser, type, filePath);

		return "ajax";
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
		
		ajax = bio.addTask4NX(time, accoid, deviceIds, param, strategy_type, type);
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
		else if("false50000".equals(ajax))
		{
			ajax = "定制设备超过50000条，定制失败！";
		}
		return "ajax";
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
		
		ajax = bio.addTask4CQ(time, accoid, deviceIds, param, strategy_type, type, taskName, task_wan_type, paramPath, paramValue, paramType);
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
//	public String queryTaskGyCity() {
//		taskByCityList = bio.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
//		maxPage_splitPage = bio.queryTaskGyCityCount(task_id, task_name, curPage_splitPage, num_splitPage);
//		return "batchConfigListGyCity4cq";
//	}
	
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
//	public String queryTaskGyCityExcel() {
//		logger.debug("queryTaskGyCityExcel()");
//		taskByCityList = bio.queryTaskGyCityList(task_id, task_name, curPage_splitPage, num_splitPage);
//		String excelCol = null;
//		String excelTitle = null;
//		excelTitle = "任务名称#属地#总数#成功数#失败数#未做数";
//		excelCol = "task_name#city_name#totalNum#succNum#failNum#unDoneNum";
//		
//		column = excelCol.split("#");
//		title = excelTitle.split("#");
//		fileName = "taskGyCity";
//		data = taskByCityList;
//		logger.warn("....."+data.toString());
//		return "excel";
//	}
	
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

	public StackRefreshToolsBIO getBio() {
		return bio;
	}

	public void setBio(StackRefreshToolsBIO bio) {
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
		StackRefreshToolsACT.isSuccess = isSuccess;
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
	
	public String getCountMax() {
		return countMax;
	}


	public void setCountMax(String countMax) {
		this.countMax = countMax;
	}


	public long getCountMaxL() {
		return countMaxL;
	}


	public void setCountMaxL(long countMaxL) {
		this.countMaxL = countMaxL;
	}


	public long getUpMaxL() {
		return upMaxL;
	}


	public void setUpMaxL(long upMaxL) {
		this.upMaxL = upMaxL;
	}


	public String getUpMax() {
		return upMax;
	}


	public void setUpMax(String upMax) {
		this.upMax = upMax;
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


	public String getTask_wan_type() {
		return task_wan_type;
	}


	public void setTask_wan_type(String task_wan_type) {
		this.task_wan_type = task_wan_type;
	}


	public String getParamPath() {
		return paramPath;
	}


	public void setParamPath(String paramPath) {
		this.paramPath = paramPath;
	}


	public String getParamValue() {
		return paramValue;
	}


	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}


	public String getParamType() {
		return paramType;
	}


	public void setParamType(String paramType) {
		this.paramType = paramType;
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

	
	
	
}
