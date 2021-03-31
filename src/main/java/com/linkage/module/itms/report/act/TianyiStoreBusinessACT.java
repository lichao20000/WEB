package com.linkage.module.itms.report.act;

import java.text.SimpleDateFormat;
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
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.TianyiStoreBusinessBIO;

@SuppressWarnings("serial")
public class TianyiStoreBusinessACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(TianyiStoreBusinessACT.class);
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	// 转换的 时间
	private String startOpenDate1 = "";
	// 结束时间
	private String endOpenDate = "";
	// 装换的时间
	private String endOpenDate1 = "";
	// 属地
	private String city_id = null;

	// 导出属地
	private String cityId = "";

	private String userType = "";

	private Map<String, String> userTypeMap;

	// 属地列表
	private List<Map<String, String>> cityList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> presetList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> devList = null;
	@SuppressWarnings("rawtypes")
	private List<Map> data;
	private String[] title;
	private String[] column;
	private String fileName;
	private String ajax;

	private TianyiStoreBusinessBIO bio;

	public String init() {
		logger.debug("TianyiStoreBusinessACT=>init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		userTypeMap = bio.getUserType();
		return "init";
	}

	public String countTianyiStoreBusiness() {
		logger.debug("TianyiStoreBusinessACT=>countTianyiStoreBusiness");
		this.setTime();
		data = bio.countTianyiStoreBusiness(startOpenDate1, endOpenDate1,
				city_id, userType);
		return "list";
	}

	public String getExcel() {
		fileName = "天翼看店统计信息";
		this.setTime();
		title = new String[] { "属地", "天翼看店用户数量" };
		column = new String[] { "city_name", "ninum" };
		data = bio.countTianyiStoreBusiness(startOpenDate1, endOpenDate1,
				city_id, userType);
		return "excel";
	}

	public String getDevListForWbdTerminal() {
		logger.debug("TianyiStoreBusinessACT=>getDevListForWbdTerminal");
		this.setTime();
		devList = bio.getDeviceListForWBdTerminal(city_id, startOpenDate1,
				endOpenDate1,userType, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getDeviceListForWBdTerminalCount(city_id,
				startOpenDate1, endOpenDate1,userType, curPage_splitPage, num_splitPage);
		return "deviceList";
	}

	public String getDevExcel() {
		logger.debug("getDevExcel()");
		fileName = "天翼看店统计详细信息";
		data = bio.getDevExcel(city_id, startOpenDate1, endOpenDate1,userType);
		title = new String[] { "属地", "LOID", "用户终端规格", "BSS受理时间" };
		column = new String[] { "city_name", "username", "spec_id", "opendate" };
		return "excel";
	}

	private void setTime() {
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id)) {
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		DateTimeUtil dt = null;
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

	// 当前年的1月1号
	private String getStartDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(GregorianCalendar.DATE, 1);
		now.set(GregorianCalendar.MONTH, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate() {
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getPresetList() {
		return presetList;
	}

	public Map<String, String> getUserTypeMap() {
		return userTypeMap;
	}

	public void setUserTypeMap(Map<String, String> userTypeMap) {
		this.userTypeMap = userTypeMap;
	}

	@SuppressWarnings("rawtypes")
	public void setPresetList(List<Map> presetList) {
		this.presetList = presetList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevList() {
		return devList;
	}

	@SuppressWarnings("rawtypes")
	public void setDevList(List<Map> devList) {
		this.devList = devList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
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

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public TianyiStoreBusinessBIO getBio() {
		return bio;
	}

	public void setBio(TianyiStoreBusinessBIO bio) {
		this.bio = bio;
	}

}
