package com.linkage.module.ids.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.ids.bio.VoiceRegStatusAnalysisRepBIO;


public class VoiceRegStatusAnalysisRepACT  implements SessionAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6891428680024639963L;
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegStatusAnalysisRepACT.class);
	
	private HttpServletRequest request;
	
	private Map session;
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
	// 查询出语音注册信息列表
	private List<Map<String,String>> voicePortCityList;
	// 查询出语音设备信息列表
	private List<Map> voiceDeviceList;
	// 查询出语音设备信息列表
	private List<Map<String,String>> ppoeFailList;
	
	private List<Map<String,String>> voiceRegFailedReasonList;
	
	private List<Map<String,String>> deviceTempList;
	// 设备型号
	private String dev_type;
	//报表类型,天，周，月
	private String reportType;
	//哪种类型的语音端口状况
	private String numInfo;
	
	private String deviceModelId;
	
	private VoiceRegStatusAnalysisRepBIO bio;
	
	
	private String filePath ="";
	
	private String ajax ="";
	/**
	 * 语音端口使用情况（地域）
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception
	{
		logger.warn("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "initVoiceCity";
	}
	/**
	 * 语音端口使用情况(型号)
	 * @return
	 */
	
	public String initVoiceModel()
	{
		logger.warn("initVoiceModel()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "initVoiceModel";
	}
	/**
	 * PPPOE失败原因统计
	 * @return
	 */
	public String initPPPOE()
	{
		logger.warn("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "initPPPOE";
	}
	/**
	 * 语音注册失败原因
	 * @return
	 */
	public String initVoiceRegFail()
	{
		logger.warn("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "initVoiceRegFail";
	}
	/**
	 * 终端环境
	 * @return
	 */
	public String initDeviceTemperature()
	{
		logger.warn("execute()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		endOpenDate = getEndDate();
		return "initDeviceTemperature";
	}
	/**
	 * 查询语音端口使用情况(地域)	
	 * @return
	 */
	public String voiceRegQueryInfoByCity()
	{
		logger.debug("voiceRegQueryInfoByCity()");
		this.setTime();
		voicePortCityList = bio.voiceRegQueryInfo(startOpenDate1, endOpenDate1,reportType);
		return "voiceCityList";
	}
	/**
	 * 查询语音端口使用情况(型号)	
	 * @return
	 */
	public String voiceRegQueryInfoByModel()
	{
		logger.debug("voiceRegQueryInfoByModel()");
		this.setTime();
		voicePortCityList = bio.voiceRegQueryInfoByModel(startOpenDate1, endOpenDate1,reportType);
		return "voiceModelList";
	}
	/**
	 * PPPOE失败原因统计
	 * @return
	 */
	public String pppoeFailStatus(){
		logger.debug("ppoeFailStatus()");
		this.setTime();
		ppoeFailList = bio.getPPPOEFailStatus(startOpenDate1,endOpenDate1,reportType);
		return "pppoeList";
	}
	/**
	 * 语音注册失败原因按区域分组统计
	 * @return
	 */
	public String voiceRegFailedReasonQuery(){
		logger.debug("voiceRegFailedReasonQuery()");
		this.setTime();
		voiceRegFailedReasonList = bio.getVoicRegFailedReasonQuery(startOpenDate1,endOpenDate1,reportType);
		return "voiceRegList";
	}
	/**
	 * 在原终端环境统计查询条件中，有温度、电流、电压条件。
	 * 由于采用后台统计模块统计后，现将温度、电流、电压条件在配置文件中配置，统计模块根据配置条件再做统计
	 * @return
	 */
	public String deviceTempQuery(){
		logger.debug("deviceTempQuery()");
		this.setTime();
		deviceTempList = bio.getDeviceTempQuery(startOpenDate1,endOpenDate1,reportType);
		return "deviceTemp";
	}
	/**
	 * 下载文件 获取文件的公网地址
	 * @return
	 */
	public String downloadFile(){
		logger.debug("VoiceRegStatusAnalysisRepACT->downloadFile(filePath={})",filePath);
		String pathSuffer = bio.getFilePath();
		
		String absPathSuffer = LipossGlobals.getLipossProperty("webPath");
		ajax = bio.fileIsExist(absPathSuffer,pathSuffer,filePath);
		return "ajax";
	}
	//当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		DateTimeUtil now = new DateTimeUtil();
		now = new DateTimeUtil(now.getDate() + " 00:00:00");
		// 今天的0点
		long todayTime = now.getLongTime();
		// 昨天的0点
		long yesterdayTime = todayTime - 24 * 60 * 60;
		
		return new DateTimeUtil(yesterdayTime * 1000).getDate();
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + endOpenDate);
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			startOpenDate1 = null;
			endOpenDate1 = null;
		}
		else
		{
			//日报表
			if("1".equals(reportType)){
				String start = endOpenDate + " 00:00:00";
				DateTimeUtil st = new DateTimeUtil(start);
				startOpenDate1 = String.valueOf(st.getLongTime());
				String end = endOpenDate + " 23:59:59";
				DateTimeUtil et = new DateTimeUtil(end);
				endOpenDate1 = String.valueOf(et.getLongTime());
			}else if("2".equals(reportType)){
				
				DateTimeUtil st = new DateTimeUtil(endOpenDate);
				String start = st.getFirstDayOfWeek("CN") +" 00:00:00";
				String end =  st.getLastDayOfWeek("CN") + " 23:59:59";
				st = new DateTimeUtil(start);
				startOpenDate1 = String.valueOf(st.getLongTime());
				DateTimeUtil et = new DateTimeUtil(end);
				endOpenDate1 = String.valueOf(et.getLongTime());
			}else if ("3".equals(reportType)){
				String start = endOpenDate + "-01 00:00:00";
				DateTimeUtil st = new DateTimeUtil(start);
				startOpenDate1 = String.valueOf(st.getLongTime());
				String end = st.getLastDayOfMonth() + " 23:59:59";
				DateTimeUtil et = new DateTimeUtil(end);
				endOpenDate1 = String.valueOf(et.getLongTime());
			}
//			logger.warn("开始时间：" + start + "  结束时间：" + end);
		}
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session  = session;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getStartOpenDate1() {
		return startOpenDate1;
	}
	public void setStartOpenDate1(String startOpenDate1) {
		this.startOpenDate1 = startOpenDate1;
	}
	public String getEndOpenDate() {
		return endOpenDate;
	}
	public void setEndOpenDate(String endOpenDate) {
		this.endOpenDate = endOpenDate;
	}
	public String getEndOpenDate1() {
		return endOpenDate1;
	}
	public void setEndOpenDate1(String endOpenDate1) {
		this.endOpenDate1 = endOpenDate1;
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
	public List<Map<String, String>> getVoicePortCityList() {
		return voicePortCityList;
	}
	public void setVoicePortCityList(List<Map<String, String>> voicePortCityList) {
		this.voicePortCityList = voicePortCityList;
	}
	public List<Map> getVoiceDeviceList() {
		return voiceDeviceList;
	}
	public void setVoiceDeviceList(List<Map> voiceDeviceList) {
		this.voiceDeviceList = voiceDeviceList;
	}
	public List<Map<String, String>> getPpoeFailList() {
		return ppoeFailList;
	}
	public void setPpoeFailList(List<Map<String, String>> ppoeFailList) {
		this.ppoeFailList = ppoeFailList;
	}
	public String getDev_type() {
		return dev_type;
	}
	public void setDev_type(String dev_type) {
		this.dev_type = dev_type;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getNumInfo() {
		return numInfo;
	}
	public void setNumInfo(String numInfo) {
		this.numInfo = numInfo;
	}
	public String getDeviceModelId() {
		return deviceModelId;
	}
	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}
	public void setBio(VoiceRegStatusAnalysisRepBIO bio) {
		this.bio = bio;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public List<Map<String, String>> getVoiceRegFailedReasonList() {
		return voiceRegFailedReasonList;
	}
	public void setVoiceRegFailedReasonList(
			List<Map<String, String>> voiceRegFailedReasonList) {
		this.voiceRegFailedReasonList = voiceRegFailedReasonList;
	}
	public List<Map<String, String>> getDeviceTempList() {
		return deviceTempList;
	}
	public void setDeviceTempList(List<Map<String, String>> deviceTempList) {
		this.deviceTempList = deviceTempList;
	}
	
}
