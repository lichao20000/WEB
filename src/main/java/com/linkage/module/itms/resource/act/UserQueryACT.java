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
import com.linkage.module.itms.resource.bio.UserQueryBIO;

public class UserQueryACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	private static final long serialVersionUID = -7787526862261263533L;
	private static Logger logger = LoggerFactory
			.getLogger(UserQueryACT.class);
	private UserQueryBIO bio;
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
	// 属地
	private String city_id = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;

	private String deviceType = "";

	private String isActive = "";

	private String specName = "";

	private String moreinter = "";

	private String moreitv = "";

	private String morevoip = "";

	private String moretianyi = "";

	private String temval = "";

	private String querytype = "";

	private String ajax = "";

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
		logger.debug("UserQueryACT=>execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	public String queryUserQuery() {
		if ("2".equals(temval)) {
			moreinter = "";
			moreitv = "";
			morevoip = "";
			moretianyi = "";
		}
		this.setTime();
		deployList = bio.queryUserQuery(city_id, startOpenDate1, endOpenDate1,
				deviceType, isActive, specName, moreinter, moreitv, morevoip,
				moretianyi, querytype);
		return "list";
	}

	public String queryUserQueryExcel() {
		if ("2".equals(temval)) {
			moreinter = "";
			moreitv = "";
			morevoip = "";
			moretianyi = "";
		}
		this.setTime();
		deployList = bio.queryUserQuery(city_id, startOpenDate1, endOpenDate1,
				deviceType, isActive, specName, moreinter, moreitv, morevoip,
				moretianyi, querytype);
		String excelCol = "city_name#deploy_total";
		String excelTitle = "区域#用户查询";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "用户查询";
		data = deployList;
		return "excel";
	}

	public String queryUserQueryDev() {
		if ("2".equals(temval)) {
			moreinter = "";
			moreitv = "";
			morevoip = "";
			moretianyi = "";
		}
		this.setTime();
		deployDevList = bio
				.queryUserQueryList(city_id, startOpenDate1, endOpenDate1,
						deviceType, isActive, specName, moreinter, moreitv,
						morevoip, moretianyi, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.countQueryUserQueryList(city_id,
				startOpenDate1, endOpenDate1, deviceType, isActive, specName,
				moreinter, moreitv, morevoip, moretianyi, curPage_splitPage,
				num_splitPage);
		return "devlist";
	}

	public String queryUserQueryDevExcel() {
		if ("2".equals(temval)) {
			moreinter = "";
			moreitv = "";
			morevoip = "";
			moretianyi = "";
		}
		this.setTime();
		deployDevList = bio.excelQueryUserQueryList(city_id, startOpenDate1,
				endOpenDate1, deviceType, isActive, specName, moreinter,
				moreitv, morevoip, moretianyi);
		String excelCol = "city_name#loid#interusername#itvusername#voipusername#dealdate";
		String excelTitle = "属地#LOID#宽带账号#ITV账号#VOIP账号#受理时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "用户查询详细信息";
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
		if ("1".equals(querytype)) {
			fileName = "userQueryDayByCity_" + city_id + "_line1_1_"
					+ startOpenDate + ".csv";
		}
		if ("2".equals(querytype)) {
			fileName = "userQueryWeekByCity_" + city_id + "_line2_2_" + week
					+ ".csv";
		}
		if ("3".equals(querytype)) {
			fileName = "userQueryMonthByCity_" + city_id + "_line3_3_" + month
					+ ".csv";
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

	/**
	 * 时间转化
	 */
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

	public UserQueryBIO getBio() {
		return bio;
	}

	public void setBio(UserQueryBIO bio) {
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getMoreinter() {
		return moreinter;
	}

	public void setMoreinter(String moreinter) {
		this.moreinter = moreinter;
	}

	public String getMoreitv() {
		return moreitv;
	}

	public void setMoreitv(String moreitv) {
		this.moreitv = moreitv;
	}

	public String getMorevoip() {
		return morevoip;
	}

	public void setMorevoip(String morevoip) {
		this.morevoip = morevoip;
	}

	public String getMoretianyi() {
		return moretianyi;
	}

	public void setMoretianyi(String moretianyi) {
		this.moretianyi = moretianyi;
	}

	public String getTemval() {
		return temval;
	}

	public void setTemval(String temval) {
		this.temval = temval;
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
