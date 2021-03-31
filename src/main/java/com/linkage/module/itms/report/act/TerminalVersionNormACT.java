package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.TerminalVersionNormBIO;
public class TerminalVersionNormACT extends splitPageAction  implements SessionAware {
	private static final long serialVersionUID = 972144923683847180L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(TerminalVersionNormACT.class);
	/** session */
	private Map session;
	/** 设备厂商 */
	private String vendorId = null;
	/** 设备型号 */
	private String deviceModelId = null;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地ID */
	private String cityId = "-1";
	/**上行接入方式*/
	private String accessType ;
	/**终端类型*/
	private String userType ;
	/**是否活跃用户*/
	private String isActive;
	/** 是否规范版本**/
	private String flag = "";
	/**终端列表*/
	private List<Map> deviceList ;
	/** 导出数据 */
	private List<Map> data;
	/** 导出文件列标题 */
	private String[] title;
	/** 导出文件列 */
	private String[] column;
	/** 导出文件名 */
	private String fileName;
    /**区分ITMS和BBMS*/
	private String gw_type;
	
	
	private TerminalVersionNormBIO terminalVersionNormBIO;
	/**
	 * 初始化时间
	 * @return
	 */
	public String init(){
		logger.debug("init()");
		this.initTime();
		return "init";
	}
	/**
	 * 查询终端版本规范率
	 * @return
	 */
	public String queryList(){
		logger.debug("queryList()");
		UserRes curUser = (UserRes)session.get("curUser");
		Map cityMap = CityDAO.getCityIdCityNameMap();
		logger.warn("终端版本规范查询   操作人ID："+curUser.getUser().getId()+
			    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
			    "   属地："+cityMap.get(cityId)+"上行接入方式" +accessType+"终端类型"+userType);
		this.setTime();
		data = terminalVersionNormBIO.queryResultList(vendorId,deviceModelId,cityId,accessType,userType,starttime1,endtime1,gw_type,isActive);
		return "list";
	}
	/**
	 * 
	 * @return 返回终端版本信息
	 */
	public String queryDeviceList(){
		logger.debug("queryDevcieList()");
		deviceList = terminalVersionNormBIO.queryDeviceList(flag,vendorId,deviceModelId,cityId,gw_type,accessType,userType,starttime1,endtime1,isActive, curPage_splitPage, num_splitPage);
		maxPage_splitPage = terminalVersionNormBIO.queryDeviceListCount(flag,vendorId,deviceModelId,cityId,gw_type,accessType,userType,starttime1,endtime1,isActive,curPage_splitPage, num_splitPage);
		return "deviceList";
	}
     /**
      * 
      * @return 导出终端版本规范率结果
      */
	public String getAllResultExcel(){
		logger.debug("getAllResultExcel()");
		title = new String[]{"属地","不规范版本终端数","规范版本终端数","终端总数","规范比率"};
		column = new String[]{"cityName","notNormNum","normNum","totalNum","normRate"};
		fileName = "terminalVersionNorm";
		data = terminalVersionNormBIO.queryResultList(vendorId, deviceModelId,  cityId,accessType,userType, starttime1, endtime1,gw_type,isActive);
		return "excel";
	}
	/**
	 * 
	 * @return 导出终端版本信息列表
	 */
	public String getDeviceResultExcel(){
		logger.debug("getDeviceResultExcel()");
		logger.debug("getAllResultExcel()");
		title = new String[]{"设备厂商","设备型号","软件版本","设备属地","设备序列号","设备逻辑SN","终端类型","注册时间","最近上报时间"};
		column = new String[]{"vendorName","devicemodel","softwareversion","city_name","deviceSerialnumber","deviceIdEx","device_type","completeTime","lastTime"};
		fileName = "terminalVersionDetail";
		data = terminalVersionNormBIO.queryDeviceExcel(flag,vendorId, deviceModelId,  cityId,accessType,userType, starttime1, endtime1,gw_type,isActive);
		return "excel";
	}
	/**
	 * 初始化时间
	 */
	public void initTime(){
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		
		starttime = dt.getFirtDayOfMonth();   //获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
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
	
	public TerminalVersionNormBIO getTerminalVersionNormBIO() {
		return terminalVersionNormBIO;
	}
	public void setTerminalVersionNormBIO(
			TerminalVersionNormBIO terminalVersionNormBIO) {
		this.terminalVersionNormBIO = terminalVersionNormBIO;
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
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getStarttime1() {
		return starttime1;
	}
	public void setStarttime1(String starttime1) {
		this.starttime1 = starttime1;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getEndtime1() {
		return endtime1;
	}
	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
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
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
	
}
