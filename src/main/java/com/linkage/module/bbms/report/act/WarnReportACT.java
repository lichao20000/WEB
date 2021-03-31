/**
 * 
 */
package com.linkage.module.bbms.report.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.WarnReportBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 需求挖掘和预警
 * @author chenjie
 * @date 2012-12-04
 */
public class WarnReportACT extends splitPageAction implements SessionAware,ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8537949010590751867L;

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(WarnReportACT.class);
	
	// request
	private HttpServletRequest request;
	
	// bio
	private WarnReportBIO warnReportBio;
	
	// session
	private Map session;
	
	private String cityId;
	List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	
	private String industry;
	
	private String servTypeId;
	
	private String deviceType;
	
	private String adsl_hl;
	
	private String flowMin;
	private String flowMax;
	
	private String onlinedateMin;
	private String onlinedateMax;
	
	/**
	 * 查询开始时间和结束时间
	 */
	private String startOpenDate;
	private String startOpenDate1 = "";
	private String endOpenDate;
	private String endOpenDate1 = "";
	
	private String vendorId;
	private List<Map<String,String>> vendorList = new ArrayList<Map<String, String>>();
	
	private String linkphone;
	
	private String customerAddress;
	
	private String productType;
	
	private String warningReason;
	
	private String startWarningDate;
	private String startWarningDate1 = "";
	private String endWarningDate;
	private String endWarningDate1 = "";
	
	
	// 需求挖掘查询结果
	private List<Map> warnRequireReportList;
	// 维挽预警查询结果
	private List<Map> warnReportList;
	
	/**
	 * 页面初始化
	 * 
	 * @return String
	 */
	public String init()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		// DateTimeUtil dt = new DateTimeUtil();
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		
		// 需求挖掘
		String flag = request.getParameter("flag");
		if("1".equalsIgnoreCase(flag))
		{
			return "warnRequireInit";
		}
		// 维挽预警
		else
		{
			startWarningDate = getStartDate();
			endWarningDate = getEndDate();
			vendorList = warnReportBio.getVendorList();
			return "warnInit";
		}
	}
	
	/**
	 * 查询需求挖掘
	 * @return
	 */
	public String queryWarnRequireReport()
	{
		logger.debug("queryWarnRequireReport()");
		setTime();
		
		warnRequireReportList = warnReportBio.queryWarnRequireReport(cityId, industry, servTypeId,
				deviceType, adsl_hl, flowMin, flowMax, startOpenDate1, endOpenDate1, onlinedateMin, onlinedateMax,
				curPage_splitPage, num_splitPage);
		maxPage_splitPage = warnReportBio.getMaxPage_splitPage();
		
		return "queryWarnRequireReport";
	}
	
	public String queryWarnReport()
	{
		logger.debug("queryWarnReport()");
		setTime(); // 设定第一个时间段
		setTime2(); //设定第二个时间段
		
		warnReportList = warnReportBio.queryWarnReport(cityId, linkphone, customerAddress,
				productType, vendorId, adsl_hl, industry, deviceType, warningReason, 
				startOpenDate1, endOpenDate1, startWarningDate1, endWarningDate1,
				curPage_splitPage, num_splitPage);
		
		maxPage_splitPage = warnReportBio.getMaxPage_splitPage();
		
		return "queryWarnReport";
	}
	
	// 当前年的1月1号
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
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
	
	/**
	 * 时间转化
	 */
	private void setTime2()
	{		
		logger.debug("setTime()" + startWarningDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startWarningDate == null || "".equals(startWarningDate))
		{
			startWarningDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startWarningDate);
			startWarningDate1 = String.valueOf(dt.getLongTime());
		}
		if (endWarningDate == null || "".equals(endWarningDate))
		{
			endWarningDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endWarningDate);
			endWarningDate1 = String.valueOf(dt.getLongTime());
		}
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}
	
	public Map getSession()
	{
		return session;
	}

	public WarnReportBIO getWarnReportBio() {
		return warnReportBio;
	}

	public void setWarnReportBio(WarnReportBIO warnReportBio) {
		this.warnReportBio = warnReportBio;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
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

	public String getAdsl_hl() {
		return adsl_hl;
	}

	public void setAdsl_hl(String adsl_hl) {
		this.adsl_hl = adsl_hl;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getFlowMax() {
		return flowMax;
	}

	public void setFlowMax(String flowMax) {
		this.flowMax = flowMax;
	}

	public String getFlowMin() {
		return flowMin;
	}

	public void setFlowMin(String flowMin) {
		this.flowMin = flowMin;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getOnlinedateMax() {
		return onlinedateMax;
	}

	public void setOnlinedateMax(String onlinedateMax) {
		this.onlinedateMax = onlinedateMax;
	}

	public String getOnlinedateMin() {
		return onlinedateMin;
	}

	public void setOnlinedateMin(String onlinedateMin) {
		this.onlinedateMin = onlinedateMin;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public List<Map> getWarnReportList() {
		return warnReportList;
	}

	public void setWarnReportList(List<Map> warnReportList) {
		this.warnReportList = warnReportList;
	}

	public List<Map> getWarnRequireReportList() {
		return warnRequireReportList;
	}

	public void setWarnRequireReportList(List<Map> warnRequireReportList) {
		this.warnRequireReportList = warnRequireReportList;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getEndWarningDate() {
		return endWarningDate;
	}

	public void setEndWarningDate(String endWarningDate) {
		this.endWarningDate = endWarningDate;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getStartWarningDate() {
		return startWarningDate;
	}

	public void setStartWarningDate(String startWarningDate) {
		this.startWarningDate = startWarningDate;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public List<Map<String, String>> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		this.vendorList = vendorList;
	}

	public String getWarningReason() {
		return warningReason;
	}

	public void setWarningReason(String warningReason) {
		this.warningReason = warningReason;
	}
}
