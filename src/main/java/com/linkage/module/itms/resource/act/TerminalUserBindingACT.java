package com.linkage.module.itms.resource.act;

import java.io.File;
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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.bio.TerminalUserBindingBIO;

public class TerminalUserBindingACT extends splitPageAction implements
		SessionAware, ServletRequestAware {

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(TerminalUserBindingACT.class);
	private TerminalUserBindingBIO bio;
	@SuppressWarnings("unused")
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	// 开始时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 结束时间
	private String endOpenDate1 = "";

	private String specName = "";

	private String operation;

	private String hiddenVal;

	private String querytype;

	private String city_id = null;

	private String ajax;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	@SuppressWarnings("rawtypes")
	private List<Map> deployList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> deployDevList = null;

	private static final String FILEPATH = LipossGlobals.getLipossProperty("jsdxDownLoadURL");

	/**
	 * 初始化页面数据
	 */
	@Override
	public String execute() throws Exception {
		logger.debug("TerminalUserBindingACT=>execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	public String queryTerminalUserBinding() {
		logger.debug("TerminalUserBindingACT=>queryTerminalUserBinding()");
		this.setTime();
		deployList = bio.queryTerminalUserBinding(city_id, startOpenDate1,
				endOpenDate1, specName, operation, hiddenVal, querytype);
		return "list";
	}

	public String queryTerminalUserBindingExcel() {
		logger.debug("TerminalUserBindingACT=>queryTerminalUserBindingExcel()");
		this.setTime();
		deployList = bio.queryTerminalUserBinding(city_id, startOpenDate1,
				endOpenDate1, specName, operation, hiddenVal, querytype);
		String excelCol = "city_name#deploy_total";
		String excelTitle = "";
		if ("1".equals(hiddenVal)) {
			excelTitle = "属地#用户数量";
		}
		if ("2".equals(hiddenVal)) {
			excelTitle = "属地#设备数量";
		}
		column = excelCol.split("#");
		title = excelTitle.split("#");
		if ("1".equals(hiddenVal)) {
			fileName = "用户统计";
		}
		if ("2".equals(hiddenVal)) {
			fileName = "设备统计";
		}
		data = deployList;
		return "excel";
	}

	public String queryTerminalUserBindingDev() {
		logger.debug("TerminalUserBindingACT=>queryTerminalUserBindingDev()");
		this.setTime();
		deployDevList = bio.queryTerminalUserBindingList(city_id,
				startOpenDate1, endOpenDate1, specName, operation, hiddenVal,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryTerminalUserBindingList(city_id,
				startOpenDate1, endOpenDate1, specName, operation, hiddenVal,
				curPage_splitPage, num_splitPage);
		return "devlist";
	}

	public String queryTerminalUserBindingDevExcel() {
		logger.debug("TerminalUserBindingACT=>queryTerminalUserBindingDevExcel()");
		this.setTime();
		deployDevList = bio.excelQueryTerminalUserBindingList(city_id,
				startOpenDate1, endOpenDate1, specName, operation, hiddenVal);
		String excelCol = "";
		String excelTitle = "";
		if ("1".equals(hiddenVal)) {
			excelCol = "city_name#username#device_serialnumber#dealdate#gw_type#oper_type#binddate#dealstaff";
			excelTitle = "属地#用户LOID#设备序列号#受理时间#终端规格#操作方式#操作时间#操作人";
		}
		if ("2".equals(hiddenVal)) {
			excelCol = "city_name#device_serialnumber#username#complete_time#oper_type#binddate#dealstaff";
			excelTitle = "属地#设备序列号#用户LOID#注册时间#操作方式#操作时间#操作人";
		}
		column = excelCol.split("#");
		title = excelTitle.split("#");
		if ("1".equals(hiddenVal)) {
			fileName = "用户统计详细信息";
		}
		if ("2".equals(hiddenVal)) {
			fileName = "设备统计详细信息";
		}
		data = deployDevList;
		return "excel";
	}
	
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		now.add(Calendar.DAY_OF_MONTH, -1);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public String downloadFile() {
		SimpleDateFormat weekDateFormat = new SimpleDateFormat("yyyy-ww");
		String week = weekDateFormat.format(new Date(
				lastWeekStartTime(new Date(Long
						.valueOf(getLongTime(startOpenDate)) * 1000)) * 1000));
		SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM");
		String month = monthDateFormat.format(new Date(
				lastMonthStartTime(new Date(Long
						.valueOf(getLongTime(startOpenDate)) * 1000)) * 1000));
		String fileName = null;
		if ("1".equals(hiddenVal)) {
			if ("1".equals(querytype)) {
				fileName = "userbindingDayByCity_" + city_id + "_line1_1_"
						+ startOpenDate + ".csv";
			}
			if ("2".equals(querytype)) {
				fileName = "userbindingWeekByCity_" + city_id + "_line2_2_"
						+ week + ".csv";
			}
			if ("3".equals(querytype)) {
				fileName = "userbindingMonthByCity_" + city_id + "_line3_3_"
						+ month + ".csv";
			}
		}
		if ("2".equals(hiddenVal)) {
			if ("1".equals(querytype)) {
				fileName = "terminalDayByCity_" + city_id + "_line1_1_"
						+ startOpenDate + ".csv";
			}
			if ("2".equals(querytype)) {
				fileName = "terminalWeekByCity_" + city_id + "_line2_2_" + week
						+ ".csv";
			}
			if ("3".equals(querytype)) {
				fileName = "terminalMonthByCity_" + city_id + "_line3_3_"
						+ month + ".csv";
			}
		}
		File file = new File(FILEPATH + "/" + fileName);
		if ((file.exists() && file.isFile())) {
			ajax = "1@" + fileName;
		} else {
			ajax = "2@" + fileName;
		}
		return "ajax";
	}

	private long lastWeekStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1); 
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);   
		return cal.getTimeInMillis() / 1000;
	}

	public long lastMonthStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}

	private String getLongTime(String time) {
		DateTimeUtil dt = new DateTimeUtil(time);
		return String.valueOf(dt.getLongTime());
	}

	private void setTime() {
		logger.debug("setTime()" + endOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate)) {
			startOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate)) {
			endOpenDate1 = null;
		} else {
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public TerminalUserBindingBIO getBio() {
		return bio;
	}

	public void setBio(TerminalUserBindingBIO bio) {
		this.bio = bio;
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

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployList() {
		return deployList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployList(List<Map> deployList) {
		this.deployList = deployList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeployDevList() {
		return deployDevList;
	}

	@SuppressWarnings("rawtypes")
	public void setDeployDevList(List<Map> deployDevList) {
		this.deployDevList = deployDevList;
	}

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public String getStartOpenDate1() {
		return startOpenDate1;
	}

	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}

	public String getEndOpenDate1() {
		return endOpenDate1;
	}

	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getHiddenVal() {
		return hiddenVal;
	}

	public void setHiddenVal(String hiddenVal) {
		this.hiddenVal = hiddenVal;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

}
