package com.linkage.module.itms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.ActiveTerminalBIO;


public class ActiveTerminalACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	private static Logger logger = LoggerFactory.getLogger(ActiveTerminalACT.class);
	
	private ActiveTerminalBIO activeTerminalBIO = null;
	
	// 开始时间 
	private String startOpenDate = "";
	//转换的 时间
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	//装换的时间
	private String endOpenDate1 = "";
	
	// 属地
	private String city_id = null;
	
	//导出属地
	private String cityId = "";
	
	//终端类型
	private String device_type = null;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	private List<Map> activeTermainlList = null;
	private List<Map> devList = null;
	
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String ajax;
	
	
	// session
	private Map session = null;
	private HttpServletRequest request;
	
	public String init(){
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		startOpenDate = getStartDate();
		return "init";
	}
	
	public String getActiveTerminalInfo(){
		logger.debug("getActiveTerminalInfo()");
		//startOpenDate = getStartDate();
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		activeTermainlList = activeTerminalBIO.getActiveTerminalInfo(city_id, startOpenDate1, endOpenDate1, device_type);
		return "list";
	}
	
	public String getActiveTerminalInfoExcel(){
		logger.debug("getActiveTerminalInfo()");
		//startOpenDate = getStartDate();
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		fileName = "终端活跃数";
		title = new String[] { "属地", "终端总数", "活跃终端数", "活跃率"};
		column = new String[] { "city_name", "terminal_total", "terminal_activeTotal","percentage"};
		data = activeTerminalBIO.getActiveTerminalInfo(cityId, startOpenDate1, endOpenDate1, device_type);
		return "excel";
	}
	
	
	//总设备数查询
	public String getDeviceListForAll(){
		logger.warn("getActiveTerminalInfoCount()");
		devList = activeTerminalBIO.getDeviceListForAll(city_id, device_type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = activeTerminalBIO.countDeviceListForAll(city_id, device_type, curPage_splitPage, num_splitPage);
		return "devlistall";
	}
	
	public String getDevForAllExcel(){
		logger.warn("getDevForAllExcel()");
		fileName = "设备列表";
		title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP", "绑定账号","宽带账号" };
		column = new String[] { "city_name", "vendor_add", "device_model",
				"softwareversion", "device_serialnumber", "loopback_ip", "username","broadband" };
		data = activeTerminalBIO.getDevForAllExcel(city_id, device_type);
		return "excel";
	}
	
	public String getDeviceListForTime(){
		logger.warn("getDeviceListForTime");
		//startOpenDate = getStartDate();
		this.setTime();
		devList = activeTerminalBIO.getDeviceListForTime(city_id, startOpenDate1, endOpenDate1, device_type, curPage_splitPage, num_splitPage);
		maxPage_splitPage = activeTerminalBIO.getDeviceListForTimeCount(city_id, startOpenDate1, endOpenDate1, device_type, curPage_splitPage, num_splitPage);
		return "devlisttime";
	}
	
	public String getDeviceListForTimeExcel(){
		logger.warn("getDeviceListForTimeExcel");
		//startOpenDate = getStartDate();
		this.setTime();
		fileName = "活跃设备列表";
		title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP", "绑定账号","宽带账号" };
		column = new String[] { "city_name", "vendor_add", "device_model",
				"softwareversion", "device_serialnumber", "loopback_ip", "username","broadband" };
		data = activeTerminalBIO.getDeviceListForTimeExcel(city_id, startOpenDate1, endOpenDate1, device_type);
		return "excel";
	}
	
	
	
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	
	private String getStartDate(){
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		String time  = "";
		Date start = new Date();
		start.setMonth(start.getMonth()-1);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		time = fmtrq.format(start.getTime());
		return time;
	}

	
	private String getEndDate(){
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}
	
	public void setServletRequest(HttpServletRequest req)
	{
		this.request = req;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public ActiveTerminalBIO getActiveTerminalBIO() {
		return activeTerminalBIO;
	}

	public void setActiveTerminalBIO(ActiveTerminalBIO activeTerminalBIO) {
		this.activeTerminalBIO = activeTerminalBIO;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}
	
	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map> getActiveTermainlList() {
		return activeTermainlList;
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


	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public void setActiveTermainlList(List<Map> activeTermainlList) {
		this.activeTermainlList = activeTermainlList;
	}

	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	
	
}
