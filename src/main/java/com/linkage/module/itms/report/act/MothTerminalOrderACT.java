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

import action.maintain.maintainMaxUserNumAction;
import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.MothTerminalOrderBIO;

public class MothTerminalOrderACT extends splitPageAction implements
		ServletRequestAware, SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(MothTerminalOrderACT.class);
	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String startOpenDate = "";
	// 转换的 时间
	private String startOpenDate1 = "";
	// 转换的 时间
	private String startOpenDate0 = "";
	// 结束时间
	private String endOpenDate = "";
	// 装换的时间
	private String endOpenDate1 = "";

	// 装换的时间
	private String endOpenDate0 = "";
	// 属地
	private String city_id = null;

	// 导出属地
	private String cityId = "";

	private String time = null;
	// 属地列表
	private List<Map<String, String>> cityList = null;
	private List<Map> mothOrderList = null;
	private List<Map> devList = null;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private String ajax;
	private String isMonth;
	private String flag;
	private MothTerminalOrderBIO mothTerminalOrderBIO;

	public String init() {
		logger.debug("MothTerminalOrderACT=>init()");
		UserRes curUser = (UserRes) session.get("curUser");
		city_id = curUser.getCityId();
		String ism = request.getParameter("isMonth");
		if (StringUtil.IsEmpty(ism) || !"false".equals(ism)) {
			isMonth = "true";
		}
		if ("false".equals(isMonth)) {
			startOpenDate0 = getStartDate1();
			endOpenDate0 = getEndDate1();
		} else {
			endOpenDate = this.getEndDateInit();
		}
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}

	public String countMothTerminalOrder() {
		logger.debug("MothTerminalOrderACT=>countMothTerminalOrder");
		String bindDate = null;
		if (!"false".equals(isMonth)) {
			startOpenDate = this.getStartDate();
			endOpenDate = this.getEndDate();
			bindDate = this.getBinddate();
		}
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id)) {
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		mothOrderList = mothTerminalOrderBIO.countMothTerminalOrder(
				startOpenDate1, endOpenDate1, bindDate, city_id, isMonth);
		return "orderlist";
	}

	public String countMothTerminalOrderExcel() {
		logger.debug("MothTerminalOrderACT=>countMothTerminalOrder");
		String bindDate = null;
		if (!"false".equals(isMonth)) {
			startOpenDate = this.getStartDate();
			endOpenDate = this.getEndDate();
			bindDate = this.getBinddate();
		}
		this.setTime();
		if (cityId == null || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		mothOrderList = mothTerminalOrderBIO.countMothTerminalOrder(
				startOpenDate1, endOpenDate1, bindDate, cityId, isMonth);
		String excelCol = "city_name#khNum#yyNum#khWBdNum#khBdNum#yyBdNum#percentage";
		String excelTitle = "本地网#开户工单总数#语音业务工单数#未绑定终端的开户工单总数#开户工单绑定终端数#语音业务工单绑定终端数#终端管理率";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "TerminalOrderList";
		data = mothOrderList;
		return "excel";
	}

	public String getDevListForWbdTerminal() {
		logger.debug("MothTerminalOrderACT=>getDevListForWbdTerminal");
		if (!"false".equals(isMonth)) {
			time = endOpenDate;
			startOpenDate = this.getStartDate();
			endOpenDate = this.getEndDate();
		}
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id)) {
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		devList = mothTerminalOrderBIO.getDeviceListForWBdTerminal(city_id,
				startOpenDate1, endOpenDate1,flag, curPage_splitPage, num_splitPage);
		maxPage_splitPage = mothTerminalOrderBIO
				.getDeviceListForWBdTerminalCount(city_id, startOpenDate1,
						endOpenDate1,flag, curPage_splitPage, num_splitPage);
		return "devlist";
	}

	public String getMothTerminalOrderExcel() {
		logger.debug("MothTerminalOrderACT=>getMothTerminalOrderExcel");
		if (!"false".equals(isMonth)) {
			endOpenDate = time;
			startOpenDate = this.getStartDate();
			endOpenDate = this.getEndDate();
		}
		this.setTime();
		if (city_id == null || "".equals(city_id) || "-1".equals(city_id)) {
			UserRes curUser = (UserRes) session.get("curUser");
			city_id = curUser.getCityId();
		}
		devList = mothTerminalOrderBIO.getDeviceListForTerminalExcel(city_id,flag,
				startOpenDate1, endOpenDate1);
		String excelCol = "username#city_name#user_type#opendate";
		String excelTitle = "用户账号#属地#用户来源#开户时间";
		column = excelCol.split("#");
		title = excelTitle.split("#");
		fileName = "userList";
		data = devList;
		return "excel";
	}

	private String getStartDate() {
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		GregorianCalendar now = new GregorianCalendar();
		String[] s = endOpenDate.split("-");
		now.set(Calendar.YEAR, Integer.parseInt(s[0]));
		now.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getEndDate() {
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.US);
		Date date = new Date();
		try {	
			date = format.parse(endOpenDate);
		} catch (ParseException e) {
			logger.debug("日期类型装换失败");
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		GregorianCalendar now = new GregorianCalendar();
		String[] s = endOpenDate.split("-");
		now.set(Calendar.YEAR, Integer.parseInt(s[0]));
		now.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
		now.set(Calendar.DAY_OF_MONTH, count);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getBinddate() {
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.US);
		Date date = new Date();
		try {
			date = format.parse(endOpenDate);
		} catch (ParseException e) {
			logger.debug("日期类型装换失败");
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		GregorianCalendar now = new GregorianCalendar();
		String[] s = endOpenDate.split("-");
		now.set(Calendar.YEAR, Integer.parseInt(s[0]));
		now.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
		now.set(Calendar.DAY_OF_MONTH, count + 3);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	private String getEndDateInit() {
		DateTimeUtil dt = new DateTimeUtil();
		int month = dt.getMonth();
		int year = dt.getYear();
		int day = dt.getDay();
		System.out.println(day);
		if (day < 4) {
			month = month - 2;
			if (month == 0) {
				year = year - 1;
				month = 12;
			}
		} else {
			month = month - 1;
			if (month == 0) {
				year = year - 1;
				month = 12;
			}
		}
		return year + "-" + month;
	}

	/**
	 * 时间转化
	 */
	private void setTime() {
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if ("false".equals(isMonth)) {
			if (startOpenDate0 == null || "".equals(startOpenDate0)) {
				startOpenDate1 = null;
			} else {
				dt = new DateTimeUtil(startOpenDate0);
				startOpenDate1 = String.valueOf(dt.getLongTime());
			}
			if (endOpenDate0 == null || "".equals(endOpenDate0)) {
				endOpenDate1 = null;
			} else {
				dt = new DateTimeUtil(endOpenDate0);
				endOpenDate1 = String.valueOf(dt.getLongTime());
			}
		} else {
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
	}

	// 当前年的1月1号
	private String getStartDate1() {
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
	private String getEndDate1() {
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

	public String getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(String startOpenDate) {
		this.startOpenDate = startOpenDate;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Map> getMothOrderList() {
		return mothOrderList;
	}

	public void setMothOrderList(List<Map> mothOrderList) {
		this.mothOrderList = mothOrderList;
	}

	public MothTerminalOrderBIO getMothTerminalOrderBIO() {
		return mothTerminalOrderBIO;
	}

	public void setMothTerminalOrderBIO(
			MothTerminalOrderBIO mothTerminalOrderBIO) {
		this.mothTerminalOrderBIO = mothTerminalOrderBIO;
	}

	public List<Map> getDevList() {
		return devList;
	}

	public void setDevList(List<Map> devList) {
		this.devList = devList;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getIsMonth() {
		return isMonth;
	}

	public void setIsMonth(String isMonth) {
		this.isMonth = isMonth;
	}

	public String getEndOpenDate0() {
		return endOpenDate0;
	}

	public void setEndOpenDate0(String endOpenDate0) {
		this.endOpenDate0 = endOpenDate0;
	}

	public String getStartOpenDate0() {
		return startOpenDate0;
	}

	public void setStartOpenDate0(String startOpenDate0) {
		this.startOpenDate0 = startOpenDate0;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
