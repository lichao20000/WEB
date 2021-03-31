/**
 * 
 */
package com.linkage.module.gtms.stb.report.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.report.bio.SheetReasonReportBIO;

/**
 * 工单报表
 * fanjm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.act
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class SheetReasonReportACT extends splitPageAction implements SessionAware, ServletResponseAware
  {

	//	日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetReasonReportACT.class);
	
	private Map session;
	
	private HttpServletResponse response;
	
	// bio
	private SheetReasonReportBIO bio;
	
	/** 开始时间 **/
	private String startOpenDate;
	
	/** 结束时间 **/
	private String endOpenDate;
	
	/** 地市 **/
	private String cityId;
	
	/** 地市集合 **/
	private List<Map<String,String>> cityList;
	
	/** 终端类型 **/
	private String deviceType;
	
	/** 业务类型 **/
	private String servTypeId;
	
	/** 开通状态 **/
	private String openStatus;
	
	private List<Map<String,String>> statsReportList;
	
	public List<Map> detailReportList;

	public List<Map> getDetailReportList() {
		return detailReportList;
	}

	public void setDetailReportList(List<Map> detailReportList) {
		this.detailReportList = detailReportList;
	}

	public String init()
	{
		logger.debug("init()");
		//UserRes curUser = (UserRes) session.get("curUser");
		//cityId = curUser.getCityId();
		//cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	/**
	 * 查询数据
	 * @return
	 */
	public String getStatsReport()
	{
		logger.warn("getStatsReport()");
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		statsReportList = bio.getStatsReport(cityId, startOpenDate, endOpenDate);
		return "result";
	}
	
	/**
	 * 查看详细
	 * @return
	 */
	/*public String getDetailReport()
	{
		logger.debug("getDetailReport");
		this.setTime();
		detailReportList = bio.getDetailReport(cityId,openStatus,deviceType, servTypeId, startOpenDate, endOpenDate, curPage_splitPage,
				num_splitPage);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		return "detail";
	}
	*/
	/**
	 * 统计结果excel导出
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	/*public String getExcel() throws Exception
	{
		logger.debug("getExcel");
		this.setTime();
		statsReportList = bio.getStatsReport(deviceType, cityId, servTypeId, startOpenDate, endOpenDate);
		ExportExcelUtil util = new ExportExcelUtil("工单统计报表查询结果", new String[]{"属地", "成功数", "失败数", "未做数", "成功率"});
		util.export(response, new ArrayList(statsReportList), new String[]{"cityName", "succNum", "failNum", "notNum", "percent" }, "stats");
		return null;
	}*/
	
	/**
	 * 详细信息excel导出
	 * @return
	 * @throws Exception 
	 */
	/*public String getDetailExcel() throws Exception
	{
		logger.debug("getDetailExcel");
		//this.setTime();
		detailReportList = bio.getDetailReport(cityId,openStatus,deviceType, servTypeId, startOpenDate, endOpenDate);
		
		// 转换数据,方便导出
		bio.transData(detailReportList);
		ExportExcelUtil util = new ExportExcelUtil("工单统计报表详细查询", new String[]{"逻辑SN", "属地", "BSS受理时间", "业务类型", "设备序列号", "开通状态", "BSS终端类型"});
		util.export(response, detailReportList, new String[]{"username", "city_name", "dealdate", "serv_type_name", "device_serialnumber", "open_status", "type_id" }
			, "detail");
		return null;
	}*/
	
	private String getStartDate()
	{
		GregorianCalendar   now   =   new   GregorianCalendar(); 
		SimpleDateFormat   fmtrq   =   new   SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",Locale.US); 
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time =  fmtrq.format(now.getTime());
		return time;
	}
	
	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar   now   =   new   GregorianCalendar(); 
		SimpleDateFormat   fmtrq   =   new   SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",Locale.US); 
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time =  fmtrq.format(now.getTime());
		return time;
	}
	
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate = String.valueOf(dt.getLongTime());
		}
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate = String.valueOf(dt.getLongTime());
		}
	}
	
	//** getters and setters **//
	
	public SheetReasonReportBIO getBio() {
		return bio;
	}

	public void setBio(SheetReasonReportBIO bio) {
		this.bio = bio;
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

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public List<Map<String, String>> getStatsReportList() {
		return statsReportList;
	}

	public void setStatsReportList(List<Map<String, String>> statsReportList) {
		this.statsReportList = statsReportList;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public void setServletResponse(HttpServletResponse resp) {
		this.response = resp;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
}
