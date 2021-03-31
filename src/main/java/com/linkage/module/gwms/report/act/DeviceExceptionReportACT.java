/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.interf.I_DeviceExceptionReportBIO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-4
 * @category com.linkage.module.gwms.report.act
 * 
 */
public class DeviceExceptionReportACT extends splitPageAction implements ServletRequestAware {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bio
	 */
	I_DeviceExceptionReportBIO deviceExceptionReportBIO;

	/**
	 * 属地列表
	 */
	private List cityList = null;

	/**
	 * 属地
	 */
	private String cityId = null;

	/**
	 * 时间的秒数格式
	 */
	private String longData = null;
	
	/**
	 * 日报表日期
	 */
	private String hourData = null;
	
	/**
	 * 日报表日期
	 */
	private String hourDataEnd = null;
	
	/**
	 * 日报表日期
	 */
	private String dayData = null;

	/**
	 * 日报表统计结束日期
	 */
	private String dayDataEnd = null;

	/**
	 * 周报表开始日期
	 */
	private String weekData = null;

	/**
	 * 周报表结束日期
	 */
	private String weekDataEnd = null;

	/**
	 * 月报表开始日期
	 */
	private String monthData = null;

	/**
	 * 月报表结束日期
	 */
	private String monthDataEnd = null;

	/**
	 * 报表类型
	 */
	private String reportType = null;

	/**
	 * 报表结果数据
	 */
	private List exceptinList = null;

	/**
	 * 报表定制列
	 */
	private String bookparam = null;
	
	private String displayusername = "none";
	private String displaytime = "none";
	private String displaycity_name = "none";
	private String displayoui = "none";
	private String displaydevice_serialnumber = "none";
	
	/**
	 * 是否是报表
	 */
	private String isReport = null;
	
	//下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//table tile
	private String[] tbTitle = null;
	//需要取的列
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	/**
	 * 定制报表是否显示
	 */
	private String booked = "";
	
	// request取登陆帐号使用
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * 入口方法
	 */
	public String execute() throws Exception {

		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat sdf2 = new SimpleDateFormat(DATE_2_FORMAT);
		
		if(null==longData || "".equals(longData)){
			Calendar cal = Calendar.getInstance();
			this.hourData = sdf.format(cal.getTime());
			this.dayData = sdf2.format(cal.getTime());
			this.weekData = sdf2.format(cal.getTime());
			this.monthData = sdf2.format(cal.getTime());
		}else{
			this.hourData = sdf.format(Long.parseLong(longData)*1000);
			this.dayData = sdf2.format(Long.parseLong(longData)*1000);
			this.weekData = sdf2.format(Long.parseLong(longData)*1000);
			this.monthData = sdf2.format(Long.parseLong(longData)*1000);
		}

		if(null==this.reportType || "".equals(this.reportType)){
			this.reportType = "1";
		}

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		this.cityList = deviceExceptionReportBIO.getAllCity(curUser.getCityId());
		
		if(null==this.cityId || "".equals(this.cityId)){
			this.cityId = curUser.getCityId();
		}

		return SUCCESS;
	}

	public String getDayReport() throws ParseException {

		String date1 = " 00:00:00";
		String date2 = " 23:59:59";
		
		if(null!=this.bookparam && !"".equals(this.bookparam)){
			String tempParam[] = this.bookparam.split("\\$"); 
			for(int i=0;i<tempParam.length;i++){
				if("username".equals(tempParam[i])){
					this.displayusername = "";
				}
				if("time".equals(tempParam[i])){
					this.displaytime = "";
				}
				if("city_name".equals(tempParam[i])){
					this.displaycity_name = "";
				}
				if("oui".equals(tempParam[i])){
					this.displayoui = "";
				}
				if("device_serialnumber".equals(tempParam[i])){
					this.displaydevice_serialnumber = "";
				}
			}
		}else{
			this.displayusername = "";
			this.displaytime = "";
			this.displaycity_name = "";
			this.displayoui = "";
			this.displaydevice_serialnumber = "";
		}
		
		long startDate = 0;
		long endDate = 0;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//小时报表
		if ("1".equals(reportType)) {

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			startDate = cal.getTimeInMillis();
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
			endDate = cal.getTimeInMillis();
			this.hourDataEnd = simpleDateFormat2.format(endDate);

		}
		// 日报表
		if ("2".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);
			startDate = simpleDateFormat2.parse(temp + date1).getTime();
			endDate = 0;
			if(calnow.get(Calendar.DATE)>cal.get(Calendar.DATE)){
				endDate = simpleDateFormat2.parse(temp + date2).getTime();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}else{
				endDate = cal.getTimeInMillis();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}

		}
		// 周报表
		if ("3".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);
			
			String[] weekTemp = temp.split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(weekTemp[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(weekTemp[1]) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(weekTemp[2]));
			int dayOFweek = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.DATE, 1 - dayOFweek);
			StringBuffer strStartDate = new StringBuffer();
			strStartDate.append(cal.get(Calendar.YEAR));
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.MONTH) + 1);
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.DATE));
			strStartDate.append(date1);
			cal.add(Calendar.DATE, 6);

			StringBuffer strEndDate = new StringBuffer();

			if (calnow.after(cal)) {
				strEndDate.append(cal.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.DATE));
				strEndDate.append(date2);
			} else {
				strEndDate.append(calnow.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.DATE));
				strEndDate.append(" ");
				strEndDate.append(calnow.get(Calendar.HOUR_OF_DAY));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.MINUTE));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.SECOND));
			}

			
			this.weekDataEnd = strEndDate.toString();

			startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();
		}
		// 月报表
		if ("4".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);

			String[] monthTemp = temp.split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(monthTemp[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(monthTemp[1]) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(monthTemp[2]));
			int dayOFweek = cal.get(Calendar.DAY_OF_MONTH);
			cal.add(Calendar.DATE, 1 - dayOFweek);
			StringBuffer strStartDate = new StringBuffer();
			strStartDate.append(cal.get(Calendar.YEAR));
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.MONTH) + 1);
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.DATE));
			strStartDate.append(date1);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			StringBuffer strEndDate = new StringBuffer();

			if (calnow.after(cal)) {
				strEndDate.append(cal.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.DATE));
				strEndDate.append(date2);
			} else {
				strEndDate.append(calnow.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.DATE));
				strEndDate.append(" ");
				strEndDate.append(calnow.get(Calendar.HOUR_OF_DAY));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.MINUTE));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.SECOND));
			}

			this.monthDataEnd = strEndDate.toString();

			startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();
		}

		if("excel".equals(isReport)){
			
			if(null!=this.bookparam && !"".equals(this.bookparam)){
				String tempParam[] = this.bookparam.split("\\$"); 
				
				this.title =  new String[tempParam.length];
				this.column = new String[tempParam.length];
				for(int i=0;i<tempParam.length;i++){
					if("username".equals(tempParam[i])){
						title[i] = "用户帐号";
						column[i] = "username";
					}
					if("time".equals(tempParam[i])){
						title[i] = "时间";
						column[i] = "time";
					}
					if("city_name".equals(tempParam[i])){
						title[i] = "属地";
						column[i] = "city_name";
					}
					if("oui".equals(tempParam[i])){
						title[i] = "厂商";
						column[i] = "oui";
					}
					if("device_serialnumber".equals(tempParam[i])){
						title[i] = "设备序列号";
						column[i] = "device_serialnumber";
					}
				}
			}else{
				this.title =  new String[5];
				title[0] = "用户帐号";
				title[1] = "时间";
				title[2] = "属地";
				title[3] = "厂商";
				title[4] = "设备序列号";
				this.column = new String[5];
				column[0] = "username";
				column[1] = "time";
				column[2] = "city_name";
				column[3] = "oui";
				column[4] = "device_serialnumber";
			}
			
			this.data = new ArrayList();
			List temp = deviceExceptionReportBIO.getReportData(cityId,startDate, endDate);
			for(int i=0;i<temp.size();i++){
				this.data.add((Map)temp.get(i));
			}
			this.fileName = "异常设备统计";
			
			return "excel";
		}else if("pdf".equals(isReport)){
			
			//报表
			if ("1".equals(reportType)) {
				this.pdfTitle = "异常设备小时统计";
				this.pdfFileName = "deviceStatus";
			}
			//日报表
			if ("2".equals(reportType)) {
				this.pdfTitle = "异常设备日统计";
				this.pdfFileName = "deviceStatus";
			}
			//周报表
			if ("3".equals(reportType)) {
				this.pdfTitle = "异常设备周统计";
				this.pdfFileName = "deviceStatus";
			}
			//月报表
			if ("4".equals(reportType)) {
				this.pdfTitle = "异常设备月统计";
				this.pdfFileName = "deviceStatus";
			}
			
			if(null!=this.bookparam && !"".equals(this.bookparam)){
				String tempParam[] = this.bookparam.split("\\$"); 
				
				this.tbTitle =  new String[tempParam.length];
				this.tbName = new String[tempParam.length];
				for(int i=0;i<tempParam.length;i++){
					if("username".equals(tempParam[i])){
						tbTitle[i] = "用户帐号";
						tbName[i] = "username";
					}
					if("time".equals(tempParam[i])){
						tbTitle[i] = "时间";
						tbName[i] = "time";
					}
					if("city_name".equals(tempParam[i])){
						tbTitle[i] = "属地";
						tbName[i] = "city_name";
					}
					if("oui".equals(tempParam[i])){
						tbTitle[i] = "厂商";
						tbName[i] = "oui";
					}
					if("device_serialnumber".equals(tempParam[i])){
						tbTitle[i] = "设备序列号";
						tbName[i] = "device_serialnumber";
					}
				}
			}else{
				this.tbTitle =  new String[5];
				tbTitle[0] = "用户帐号";
				tbTitle[1] = "时间";
				tbTitle[2] = "属地";
				tbTitle[3] = "厂商";
				tbTitle[4] = "设备序列号";
				this.tbName = new String[5];
				tbName[0] = "username";
				tbName[1] = "time";
				tbName[2] = "city_name";
				tbName[3] = "oui";
				tbName[4] = "device_serialnumber";
			}
			
			this.pdfListData = deviceExceptionReportBIO.getReportData(cityId,startDate, endDate);
			
			return "pdfByList";
		}else if("print".equals(isReport)){
			this.exceptinList = deviceExceptionReportBIO.getReportData(curPage_splitPage, num_splitPage,cityId,startDate, endDate);
			return "print";
		}else{
			this.exceptinList = deviceExceptionReportBIO.getReportData(curPage_splitPage, num_splitPage,cityId,startDate, endDate);
			this.maxPage_splitPage = deviceExceptionReportBIO.getServStrategyCount(curPage_splitPage, num_splitPage,cityId,startDate, endDate);
			return "list";
		}
	}

	public String goPage() throws Exception {
		
		String date1 = " 00:00:00";
		String date2 = " 23:59:59";
		
		if(null!=this.bookparam && !"".equals(this.bookparam)){
			String tempParam[] = this.bookparam.split("\\$"); 
			for(int i=0;i<tempParam.length;i++){
				if("username".equals(tempParam[i])){
					this.displayusername = "";
				}
				if("time".equals(tempParam[i])){
					this.displaytime = "";
				}
				if("city_name".equals(tempParam[i])){
					this.displaycity_name = "";
				}
				if("oui".equals(tempParam[i])){
					this.displayoui = "";
				}
				if("device_serialnumber".equals(tempParam[i])){
					this.displaydevice_serialnumber = "";
				}
			}
		}else{
			this.displayusername = "";
			this.displaytime = "";
			this.displaycity_name = "";
			this.displayoui = "";
			this.displaydevice_serialnumber = "";
		}
		
		long startDate = 0;
		long endDate = 0;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//小时报表
		if ("1".equals(reportType)) {

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			startDate = cal.getTimeInMillis();
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
			endDate = cal.getTimeInMillis();
			this.hourDataEnd = simpleDateFormat2.format(endDate);

		}
		// 日报表
		if ("2".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);
			startDate = simpleDateFormat2.parse(temp + date1).getTime();
			endDate = 0;
			if(calnow.get(Calendar.DATE)>cal.get(Calendar.DATE)){
				endDate = simpleDateFormat2.parse(temp + date2).getTime();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}else{
				endDate = cal.getTimeInMillis();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}

		}
		// 周报表
		if ("3".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);
			
			String[] weekTemp = temp.split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(weekTemp[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(weekTemp[1]) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(weekTemp[2]));
			int dayOFweek = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.DATE, 1 - dayOFweek);
			StringBuffer strStartDate = new StringBuffer();
			strStartDate.append(cal.get(Calendar.YEAR));
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.MONTH) + 1);
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.DATE));
			strStartDate.append(date1);
			cal.add(Calendar.DATE, 6);

			StringBuffer strEndDate = new StringBuffer();

			if (calnow.after(cal)) {
				strEndDate.append(cal.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.DATE));
				strEndDate.append(date2);
			} else {
				strEndDate.append(calnow.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.DATE));
				strEndDate.append(" ");
				strEndDate.append(calnow.get(Calendar.HOUR_OF_DAY));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.MINUTE));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.SECOND));
			}

			
			this.weekDataEnd = strEndDate.toString();

			startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();
		}
		// 月报表
		if ("4".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);

			String[] monthTemp = temp.split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(monthTemp[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(monthTemp[1]) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(monthTemp[2]));
			int dayOFweek = cal.get(Calendar.DAY_OF_MONTH);
			cal.add(Calendar.DATE, 1 - dayOFweek);
			StringBuffer strStartDate = new StringBuffer();
			strStartDate.append(cal.get(Calendar.YEAR));
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.MONTH) + 1);
			strStartDate.append("-");
			strStartDate.append(cal.get(Calendar.DATE));
			strStartDate.append(date1);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			StringBuffer strEndDate = new StringBuffer();

			if (calnow.after(cal)) {
				strEndDate.append(cal.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(cal.get(Calendar.DATE));
				strEndDate.append(date2);
			} else {
				strEndDate.append(calnow.get(Calendar.YEAR));
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.MONTH) + 1);
				strEndDate.append("-");
				strEndDate.append(calnow.get(Calendar.DATE));
				strEndDate.append(" ");
				strEndDate.append(calnow.get(Calendar.HOUR_OF_DAY));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.MINUTE));
				strEndDate.append(":");
				strEndDate.append(calnow.get(Calendar.SECOND));
			}

			this.monthDataEnd = strEndDate.toString();

			startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();
		}
		
		this.exceptinList = deviceExceptionReportBIO.getReportData(curPage_splitPage, num_splitPage,cityId,startDate, endDate);
		this.maxPage_splitPage = deviceExceptionReportBIO.getServStrategyCount(curPage_splitPage, num_splitPage,cityId,startDate, endDate);
		
		return "list";
	}
	
	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the cityList
	 */
	public List getCityList() {
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the dayData
	 */
	public String getDayData() {
		return dayData;
	}

	/**
	 * @param dayData
	 *            the dayData to set
	 */
	public void setDayData(String dayData) {
		this.dayData = dayData;
	}

	/**
	 * @return the monthDataEnd
	 */
	public String getMonthDataEnd() {
		return monthDataEnd;
	}

	/**
	 * @param monthDataEnd
	 *            the monthDataEnd to set
	 */
	public void setMonthDataEnd(String monthDataEnd) {
		this.monthDataEnd = monthDataEnd;
	}

	/**
	 * @return the monthData
	 */
	public String getMonthData() {
		return monthData;
	}

	/**
	 * @param monthData
	 *            the monthData to set
	 */
	public void setMonthData(String monthData) {
		this.monthData = monthData;
	}

	/**
	 * @return the weekData
	 */
	public String getWeekData() {
		return weekData;
	}

	/**
	 * @param weekData
	 *            the weekData to set
	 */
	public void setWeekData(String weekData) {
		this.weekData = weekData;
	}

	/**
	 * @return the weekDataEnd
	 */
	public String getWeekDataEnd() {
		return weekDataEnd;
	}

	/**
	 * @param weekDataEnd
	 *            the weekDataEnd to set
	 */
	public void setWeekDataEnd(String weekDataEnd) {
		this.weekDataEnd = weekDataEnd;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType
	 *            the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the isReport
	 */
	public String getIsReport() {
		return isReport;
	}

	/**
	 * @param isReport the isReport to set
	 */
	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}

	/**
	 * @return the pdfFileName
	 */
	public String getPdfFileName() {
		return pdfFileName;
	}

	/**
	 * @param pdfFileName the pdfFileName to set
	 */
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	/**
	 * @return the pdfTitle
	 */
	public String getPdfTitle() {
		return pdfTitle;
	}

	/**
	 * @param pdfTitle the pdfTitle to set
	 */
	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	/**
	 * @return the dayDataEnd
	 */
	public String getDayDataEnd() {
		return dayDataEnd;
	}

	/**
	 * @param dayDataEnd the dayDataEnd to set
	 */
	public void setDayDataEnd(String dayDataEnd) {
		this.dayDataEnd = dayDataEnd;
	}

	/**
	 * @return the hourData
	 */
	public String getHourData() {
		return hourData;
	}

	/**
	 * @param hourData the hourData to set
	 */
	public void setHourData(String hourData) {
		this.hourData = hourData;
	}

	/**
	 * @return the hourDataEnd
	 */
	public String getHourDataEnd() {
		return hourDataEnd;
	}

	/**
	 * @param hourDataEnd the hourDataEnd to set
	 */
	public void setHourDataEnd(String hourDataEnd) {
		this.hourDataEnd = hourDataEnd;
	}

	/**
	 * @return the longData
	 */
	public String getLongData() {
		return longData;
	}

	/**
	 * @param longData the longData to set
	 */
	public void setLongData(String longData) {
		this.longData = longData;
	}

	/**
	 * @return the booked
	 */
	public String getBooked() {
		return booked;
	}

	/**
	 * @param booked the booked to set
	 */
	public void setBooked(String booked) {
		this.booked = booked;
	}

	/**
	 * @return the exceptinList
	 */
	public List getExceptinList() {
		return exceptinList;
	}

	/**
	 * @param exceptinList the exceptinList to set
	 */
	public void setExceptinList(List exceptinList) {
		this.exceptinList = exceptinList;
	}

	/**
	 * @return the deviceExceptionReportBIO
	 */
	public I_DeviceExceptionReportBIO getDeviceExceptionReportBIO() {
		return deviceExceptionReportBIO;
	}

	/**
	 * @param deviceExceptionReportBIO the deviceExceptionReportBIO to set
	 */
	public void setDeviceExceptionReportBIO(
			I_DeviceExceptionReportBIO deviceExceptionReportBIO) {
		this.deviceExceptionReportBIO = deviceExceptionReportBIO;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String[] column) {
		this.column = column;
	}

	/**
	 * @return the data
	 */
	public ArrayList<Map> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the pdfListData
	 */
	public List<Map<String, String>> getPdfListData() {
		return pdfListData;
	}

	/**
	 * @param pdfListData the pdfListData to set
	 */
	public void setPdfListData(List<Map<String, String>> pdfListData) {
		this.pdfListData = pdfListData;
	}

	/**
	 * @return the tbName
	 */
	public String[] getTbName() {
		return tbName;
	}

	/**
	 * @param tbName the tbName to set
	 */
	public void setTbName(String[] tbName) {
		this.tbName = tbName;
	}

	/**
	 * @return the tbTitle
	 */
	public String[] getTbTitle() {
		return tbTitle;
	}

	/**
	 * @param tbTitle the tbTitle to set
	 */
	public void setTbTitle(String[] tbTitle) {
		this.tbTitle = tbTitle;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}

	/**
	 * @return the bookparam
	 */
	public String getBookparam() {
		return bookparam;
	}

	/**
	 * @param bookparam the bookparam to set
	 */
	public void setBookparam(String bookparam) {
		this.bookparam = bookparam;
	}

	/**
	 * @return the displaycity_name
	 */
	public String getDisplaycity_name() {
		return displaycity_name;
	}

	/**
	 * @param displaycity_name the displaycity_name to set
	 */
	public void setDisplaycity_name(String displaycity_name) {
		this.displaycity_name = displaycity_name;
	}

	/**
	 * @return the displaydevice_serialnumber
	 */
	public String getDisplaydevice_serialnumber() {
		return displaydevice_serialnumber;
	}

	/**
	 * @param displaydevice_serialnumber the displaydevice_serialnumber to set
	 */
	public void setDisplaydevice_serialnumber(String displaydevice_serialnumber) {
		this.displaydevice_serialnumber = displaydevice_serialnumber;
	}

	/**
	 * @return the displayoui
	 */
	public String getDisplayoui() {
		return displayoui;
	}

	/**
	 * @param displayoui the displayoui to set
	 */
	public void setDisplayoui(String displayoui) {
		this.displayoui = displayoui;
	}

	/**
	 * @return the displaytime
	 */
	public String getDisplaytime() {
		return displaytime;
	}

	/**
	 * @param displaytime the displaytime to set
	 */
	public void setDisplaytime(String displaytime) {
		this.displaytime = displaytime;
	}

	/**
	 * @return the displayusername
	 */
	public String getDisplayusername() {
		return displayusername;
	}

	/**
	 * @param displayusername the displayusername to set
	 */
	public void setDisplayusername(String displayusername) {
		this.displayusername = displayusername;
	}
	
}
