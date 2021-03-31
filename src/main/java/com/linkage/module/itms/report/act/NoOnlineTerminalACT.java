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

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.NoOnlineTerminalBIO;

public class NoOnlineTerminalACT extends splitPageAction implements
		ServletRequestAware, SessionAware {
	
	private static Logger logger = LoggerFactory.getLogger(NoOnlineTerminalACT.class);
	
	private NoOnlineTerminalBIO noOnlineTerminalBIO;
	
	// 开始时间 
	private String startOpenDate = "";
	//转换的 时间
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	// 属地
	private String city_id = null;
	
	//导出属地
	private String cityId = "";
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	private String isJSITMS = "0";
	private List<Map> devList = null;
	
	private  List<Map> noOnlineList = null;
	
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
		return "init";
	}
	
	public String getNoOnlineTerminalInfo(){
		logger.debug("getNoOnlineTerminalInfo()");
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		noOnlineList = noOnlineTerminalBIO.getNoOnlineTerminalInfo(city_id, endOpenDate);
		return "list";
	}
	
	public String getNoOnlineTerminalInfoExcel(){
		
		logger.debug("getNoOnlineTerminalInfo()");
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId))
		{
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		noOnlineList = noOnlineTerminalBIO.getNoOnlineTerminalInfo(cityId, endOpenDate);
		return "excelList";
	}
	
	public String getDeviceList(){
		logger.debug("getDeviceList()");
		startOpenDate = getStartDate();
		this.setTime();
		devList = noOnlineTerminalBIO.getDeviceList(city_id, startOpenDate1, curPage_splitPage, num_splitPage);
		maxPage_splitPage = noOnlineTerminalBIO.getDevCount(city_id, startOpenDate1, curPage_splitPage,num_splitPage);
		return "devlist";
	}
	
	public String getDeviceExcel()
	{
		logger.debug("getDeviceExcel()");
		startOpenDate = getStartDate();
		this.setTime();
		fileName = "不活跃设备列表";
		title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP", "绑定账号" };
		column = new String[] { "city_name", "vendor_add", "device_model",
				"softwareversion", "device_serialnumber", "loopback_ip", "username" };
		if (LipossGlobals.IsITMS())
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				title = new String[] { "属地", "设备厂商", "型号", "软件版本", "设备序列号", "域名或IP",
						"绑定账号", "是否开启iTV" };
				column = new String[] { "city_name", "vendor_add", "device_model",
						"softwareversion", "device_serialnumber", "loopback_ip",
						"username", "iTV" };
			}
		}
		data = noOnlineTerminalBIO.getDevExcel(city_id, startOpenDate1);
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
	}

	
	public NoOnlineTerminalBIO getNoOnlineTerminalBIO() {
		return noOnlineTerminalBIO;
	}

	public void setNoOnlineTerminalBIO(NoOnlineTerminalBIO noOnlineTerminalBIO) {
		this.noOnlineTerminalBIO = noOnlineTerminalBIO;
	}

	public String getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public List<Map> getNoOnlineList() {
		return noOnlineList;
	}

	public void setNoOnlineList(List<Map> noOnlineList) {
		this.noOnlineList = noOnlineList;
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

	public String getIsJSITMS() {
		return isJSITMS;
	}

	public void setIsJSITMS(String isJSITMS) {
		this.isJSITMS = isJSITMS;
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
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

	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	private String getStartDate(){
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
		String time  = "";
		try {
			Date start = fmtrq.parse(endOpenDate);
			start.setMonth(start.getMonth()-3);
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
			time = fmtrq.format(start.getTime());
		} catch (ParseException e) {
			logger.error("NoOnlineTerminalACT时间类型装换失败："+endOpenDate);
		}
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

}
