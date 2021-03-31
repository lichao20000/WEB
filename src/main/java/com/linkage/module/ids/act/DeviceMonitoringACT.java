package com.linkage.module.ids.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.ids.bio.DeviceMonitoringBIO;

public class DeviceMonitoringACT extends splitPageAction {
	private static final long serialVersionUID = 422020104804843601L;
	private static Logger logger = LoggerFactory
			.getLogger(DeviceMonitoringACT.class);
	private String ajax;
	private DeviceMonitoringBIO bio = null;

	// 开始时间
	private String startTime = null;

	// 结束时间
	private String endTime = null;

	// 查询时间类型
	private String quertTimeType = null;

	// 设备序列号
	private String deviceSerialnumber = null;

	// loid编号
	private String loid = null;

	// 开始日期
	private String startDate = null;

	// 结束日期
	private String endDate = null;

	// 物理状态监控数据
	private List ponstatusList = null;

	// 宽带业务监控数据
	private List netparamList = null;

	// 语音状态监控数据
	private List voicestatusList = null;
	
	
	private List lanList = null;
	
	private List ponList = null;

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
	 * 查询物理状态监控数据
	 * 
	 * @return
	 */
	public String queryPonstatus() {
		logger.debug("queryPonstatus()");
		this.ponstatusList = this.bio.queryPonstatusList(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.queryPonstatusCount(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		return "ponstatusdetail";
	}

	/**
	 * 通过elasticSearch技术来做文件的搜索 物理状态监控数据
	 * 
	 * @return
	 */
	public String queryPonStatusByES() {
		logger.debug("queryPonStatusByES()");

		String indexName = "ids";
		String indexType = "ponstatus";

		ponstatusList = bio.queryPonstatusList1(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryPonstatusCount1(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);

		return "ponstatusdetail";
	}

	/**
	 * 查询宽带业务监控数据
	 * 
	 * @return
	 */
	public String queryNetparam() {
		logger.debug("queryNetparam()");
		logger.warn("into queryNetparam");
		this.netparamList = this.bio.queryNetparamList(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.queryNetparamCount(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		return "netparamdetail";
	}
	/**
	 * 查询宽带业务监控数据
	 * elasticSearch
	 * 
	 * @return
	 */
	public String queryNetparamByES() {
		logger.debug("queryNetparam()");
		logger.warn("into queryNetparamByES");
		String indexName = "ids";
		String indexType = "netparam";
		this.netparamList =  bio.queryNetparamList1(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryNetparamCount1(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		logger.warn("netparamList:{}"+netparamList.size());
		return "netparamdetail";
	}
	/**
	 * 查询语音状态监控数据
	 * 
	 * @return
	 */
	public String queryVoicestatus() {
		logger.debug("queryVoicestatus()");
		this.voicestatusList = this.bio.queryVoicestatusList(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		this.maxPage_splitPage = this.bio.queryVoicestatusCount(this.startTime,
				this.endTime, this.quertTimeType, this.deviceSerialnumber,
				this.loid, this.curPage_splitPage, this.num_splitPage);
		return "voicestatusdetail";
	}
	
	/**
	 * 查询语音状态监控数据
	 * 
	 * @return
	 */
	public String queryVoicestatusByES() {
		logger.debug("queryVoicestatus()");
		
		String indexName = "ids";
		String indexType = "voicestatus";
		voicestatusList = bio.queryVoicestatusList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryVoicestatusCount1(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		return "voicestatusdetail";
	}
	
	public String queryLANByES(){
		String indexName = "ids";
		String indexType = "laninfo";
		lanList = bio.queryLANList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryLANCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		return "landetail";
	}
	
	public String queryPONByES(){
		String indexName = "ids";
		String indexType = "poninfo";
		ponList = bio.queryPONList(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		maxPage_splitPage = bio.queryPONCount(indexName, indexType,
				curPage_splitPage, num_splitPage, startTime, endTime,
				quertTimeType, deviceSerialnumber, loid);
		return "pondetail";
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

	public String getAjax() {
		return this.ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public DeviceMonitoringBIO getBio() {
		return this.bio;
	}

	public void setBio(DeviceMonitoringBIO bio) {
		this.bio = bio;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getQuertTimeType() {
		return this.quertTimeType;
	}

	public void setQuertTimeType(String quertTimeType) {
		this.quertTimeType = quertTimeType;
	}

	public String getLoid() {
		return this.loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public List getPonstatusList() {
		return this.ponstatusList;
	}

	public void setPonstatusList(List ponstatusList) {
		this.ponstatusList = ponstatusList;
	}

	public List getNetparamList() {
		return this.netparamList;
	}

	public void setNetparamList(List netparamList) {
		this.netparamList = netparamList;
	}

	public List getVoicestatusList() {
		return this.voicestatusList;
	}

	public void setVoicestatusList(List voicestatusList) {
		this.voicestatusList = voicestatusList;
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

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public List getLanList() {
		return lanList;
	}

	public void setLanList(List lanList) {
		this.lanList = lanList;
	}

	public List getPonList() {
		return ponList;
	}

	public void setPonList(List ponList) {
		this.ponList = ponList;
	}
}