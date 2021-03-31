package com.linkage.module.gwms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.DeviceBindReportBIO;

public class DeviceBindReportACT extends splitPageAction implements SessionAware{
	
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(DeviceBindReportACT.class);
	
	/** session */
	private Map session = null;
	
	/** 设备厂商 */
	private String vendorId = null;
	
	/** 设备型号 */
	private String deviceModelId = null;
	
	/** 设备版本 */
	private String deviceTypeId = null;
	
	/** 设备属地 */
	private String cityId = null;
	
	/** 接入方式 */
	private String accessType = null;
	
	/** 终端类型 */
	private String usertype = null;
	
	/** 注册时间 */
	private String starttime = null;
	
	private String starttime1 = null;
	
	/** 注册时间 */
	private String endtime = null;
	
	private String endtime1 = null;
	
	private String flag = null;

	/** 导出数据 */
	private List<Map> data = null;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/** 绑定用户列表 */
	private List<Map> hgwList = null;  
	
	/** 设备列表 */
	private List<Map> deviceList = null;
	
	private DeviceBindReportBIO deviceBindReportBIO;
	
	
	
	public String init() {
		
		logger.debug("inti()");
		
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		
		int year = dt.getYear();
		int month = dt.getMonth()-1;
		int day = dt.getDay();
		
		dt = new DateTimeUtil(year+"-"+month+"-"+day);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
		
		return "init";
	}
	
	
	
	public String countAll() {
		
		logger.debug("countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		String userTypeName = "";
		if("1".equals(usertype)){
			userTypeName = "E8-B";
		}else if("2".equals(usertype)){
			userTypeName = "E8-C";
		}
		logger.warn("终端绑定情况统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId)+"   用户终端类型："+userTypeName);
		
		this.setTime();
		
		data = deviceBindReportBIO.countAll(vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
		
		return "alllist";
	}
	
	
	/**
	 * 获取设备列表
	 * @return
	 */
	public String getBindDeviceList(){
		
		logger.debug("getDeviceView()");
		
		deviceList = deviceBindReportBIO.getDeviceList(flag, cityId, starttime1, endtime1, vendorId,
				deviceModelId, deviceTypeId, accessType, usertype, curPage_splitPage, num_splitPage);
		
		maxPage_splitPage = deviceBindReportBIO.getDeviceListCount(flag, cityId, starttime1, endtime1, vendorId,
				deviceModelId, deviceTypeId, accessType, usertype, curPage_splitPage, num_splitPage);
		
		return "allDeviceList";
	}
	
	
	/**
	 * 获取绑定用户列表
	 * @return
	 */
	public String getUserList() {
		
		logger.debug("getUserList()");
		
		hgwList = deviceBindReportBIO.getUserList(flag, cityId, starttime1, endtime1, vendorId,
				deviceModelId, deviceTypeId, accessType, usertype, curPage_splitPage, num_splitPage);
		
		maxPage_splitPage = deviceBindReportBIO.getUserListCount(flag, cityId, starttime1, endtime1, vendorId,
				deviceModelId, deviceTypeId, accessType, usertype, curPage_splitPage, num_splitPage);
		
		return "allUserList";
		
	}
	
	
	/**
	 *  导出统计结果
	 * @return
	 */
	public String getAllBindWayExcel(){
		
		logger.debug("getAllBindWayExcel()");
		
		fileName = "allDeviceBindWay";
		
		title = new String[5];
		column = new String[5];
		
		title[0] = "属地";
		title[1] = "注册终端数";
		title[2] = "近期活跃数";
		title[3] = "自动绑定用户数";
		title[4] = "绑定率";
		
		column[0] = "city_name";
		column[1] = "haveRegisteDeviceNum";
		column[2] = "recentActiveDeviceNum";
		column[3] = "autoBindUserNum";
		column[4] = "percent";
		
		data = deviceBindReportBIO.countAll(vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
		
		return "excel";
	}
	
	
	/**
	 * 设备列表导出Excel
	 * @return
	 */
	public String getDeviceExcel(){
		
		fileName = "deviceSubExcel";
		
		title = new String[9];
		column = new String[9];
		
		title[0] = "设备厂商";
		title[1] = "设备型号";
		title[2] = "软件版本";
		title[3] = "设备属地";
		title[4] = "设备序列号";
		title[5] = "设备逻辑SN";
		title[6] = "终端类型";
		title[7] = "注册时间";
		title[8] = "最近上报时间";
			
		column[0] = "vendorName";
		column[1] = "devicemodel";
		column[2] = "softwareversion";
		column[3] = "city_name";
		column[4] = "deviceSerialnumber"; 
		column[5] = "deviceIdEx";
		column[6] = "device_type";
		column[7] = "completeTime";
		column[8] = "lastTime";
		
		logger.warn("====deviceBindReportBIO=====cityId===="+cityId+"============");
		data = deviceBindReportBIO.getDeviceExcel(flag, vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
		
		return "excel";
	}
	
	
	/**
	 * 已经绑定用户导出Excel
	 * 已绑定用户为 设备sn自动绑定、桥接帐号自动绑定、逻辑SN自动绑定、路由帐号自动绑定
	 * @return
	 */
	public String getUserExcel() {
		
		fileName = "userSubExcel";
		
		title = new String[6];
		column = new String[6];
		
		title[0] = "用户帐号";
		title[1] = "属地";
		title[2] = "用户来源";
		title[3] = "绑定设备";
		title[4] = "开户时间";
		title[5] = "绑定方式";
			
		column[0] = "username";
		column[1] = "city_name";
		column[2] = "user_type";
		column[3] = "device";
		column[4] = "opendate"; 
		column[5] = "bindtype";
		
		data = deviceBindReportBIO.getUserExcel(flag, vendorId, deviceTypeId, deviceModelId,cityId, accessType, usertype, starttime1, endtime1);
		
		return "excel";
	}
	
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	
	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
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

	public List<Map> getHgwList() {
		return hgwList;
	}

	public void setHgwList(List<Map> hgwList) {
		this.hgwList = hgwList;
	}

	public DeviceBindReportBIO getDeviceBindReportBIO() {
		return deviceBindReportBIO;
	}

	public void setDeviceBindReportBIO(DeviceBindReportBIO deviceBindReportBIO) {
		this.deviceBindReportBIO = deviceBindReportBIO;
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

	public String getStarttime1() {
		return starttime1;
	}

	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}

	public String getEndtime1() {
		return endtime1;
	}

	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}

	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}



	public List<Map> getDeviceList() {
		return deviceList;
	}



	public void setDeviceList(List<Map> deviceList) {
		this.deviceList = deviceList;
	}

}
