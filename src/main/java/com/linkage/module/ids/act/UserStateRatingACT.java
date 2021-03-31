package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.ids.bio.UserStateRatingBIO;

public class UserStateRatingACT extends splitPageAction {
	private static final long serialVersionUID = 422020104804843601L;
	private static Logger logger = LoggerFactory
			.getLogger(UserStateRatingACT.class);
	private String ajax;
	private UserStateRatingBIO bio = null;

	// 开始时间
	private String startTime = null;

	// 结束时间
	private String endTime = null;

	// 查询时间类型
	private String quertTimeType = null;

	// 设备序列号
	private String deviceSerialnumber = null;

	// LOID编号
	private String loid = null;

	// 开始日期
	private String startDate = null;

	// 结束日期
	private String endDate = null;

	// 物理状态监控数据
	private List<Map<String, Object>> userStatusList = null;


	/**
	 * 初始化页面时间
	 * 
	 * @return
	 */
	public String init() {
		logger.debug("init()");
		startDate = getStartTempDate();
		endDate = getEndTempDate();
		return "success";
	}


	/**
	 * 通过elasticSearch技术来做文件的搜索 物理状态监控数据
	 * 
	 * @return
	 */
	public String queryPonStatusByES() {
		logger.warn("queryPonStatusByES()");
		
		String indexName = "ids";
		String indexType = "ponstatus";

		userStatusList = bio.queryPonstatusList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryUserStatusCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

		return "usertatusList";
	}


	// 当前年的1月1号
	private String getStartTempDate() {
		DateTimeUtil dt = new DateTimeUtil(new Date());
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, dt.getMonth() - 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndTempDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		UserStateRatingACT.logger = logger;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public UserStateRatingBIO getBio() {
		return bio;
	}

	public void setBio(UserStateRatingBIO bio) {
		this.bio = bio;
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

	public String getQuertTimeType() {
		return quertTimeType;
	}

	public void setQuertTimeType(String quertTimeType) {
		this.quertTimeType = quertTimeType;
	}

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getLoid() {
		return loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Map<String, Object>> getUserStatusList() {
		return userStatusList;
	}

	public void setUserStatusList(List<Map<String, Object>> userStatusList) {
		this.userStatusList = userStatusList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}