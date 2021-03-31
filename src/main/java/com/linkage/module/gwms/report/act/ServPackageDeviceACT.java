/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.act.interf.I_ServPackageDeviceACT;
import com.linkage.module.gwms.report.bio.interf.I_ServPackageDeviceBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category action.report
 * 
 */
@SuppressWarnings("unchecked")
public class ServPackageDeviceACT extends ActionSupport implements
		ServletRequestAware, I_ServPackageDeviceACT {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bio
	 */
	I_ServPackageDeviceBIO servPackageDeviceBIO;

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
	private String[][] reportResult = null;

	/**
	 * 是否是报表
	 */
	private String isReport = null;
	
	/**
	 * pdf文件名
	 */
	private String pdfFileName = null;
	
	/**
	 * pdf文件标题
	 */
	private String pdfTitle = null;
	
	/**
	 * 定制报表是否显示
	 */
	private String booked = "";
	
	private String bookparamfirst = null;
	/**
	 * 报表定制列
	 */
	private String bookparam = null;
	
	/**
	 * pdf数据源
	 */
	private String[][] pdfArrAata = null;
	
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
		this.cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		
		if(null==this.cityId || "".equals(this.cityId)){
			this.cityId = curUser.getCityId();
		}

		List list = servPackageDeviceBIO.getGwServPackage();	
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map one = (Map)list.get(i);
			if(i!=0){
				bf.append("$");
			}
			bf.append(one.get("serv_package_id"));
			bf.append("|");
			bf.append(one.get("serv_package_name"));
		}
		bookparamfirst = bf.toString();
		return SUCCESS;
	}

	public String getDayReport() throws ParseException {

		String date1 = " 00:00:00";
		String date2 = " 23:59:59";
		
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//小时报表
		if ("1".equals(reportType)) {

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			long startDate = cal.getTimeInMillis();
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
			long endDate = cal.getTimeInMillis();
			this.hourDataEnd = simpleDateFormat2.format(endDate);
			
			this.reportResult = servPackageDeviceBIO.getReportData(cityId,startDate, endDate,bookparam);

		}
		// 日报表
		if ("2".equals(reportType)) {

			Calendar calnow = Calendar.getInstance();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(Long.parseLong(longData)*1000);
			String temp = simpleDateFormat1.format(Long.parseLong(longData)*1000);
			long startDate = simpleDateFormat2.parse(temp + date1).getTime();
			long endDate = 0;
			if(calnow.get(Calendar.DATE)>cal.get(Calendar.DATE)){
				endDate = simpleDateFormat2.parse(temp + date2).getTime();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}else{
				endDate = cal.getTimeInMillis();
				this.dayDataEnd = simpleDateFormat2.format(endDate);
			}
			this.reportResult = servPackageDeviceBIO.getReportData(cityId,startDate, endDate,bookparam);

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

			long startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			long endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();
			this.reportResult = servPackageDeviceBIO.getReportData(cityId,
					startDate, endDate,bookparam);

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

			long startDate = simpleDateFormat2.parse(strStartDate.toString())
					.getTime();
			long endDate = simpleDateFormat2.parse(strEndDate.toString())
					.getTime();

			this.reportResult = servPackageDeviceBIO.getReportData(cityId,
					startDate, endDate,bookparam);
		}

		if("excel".equals(isReport)){
			return "excelReport";
		}else if("pdf".equals(isReport)){
			
			//小时报表
			if ("1".equals(reportType)) {
				this.pdfTitle = "套餐对应终端小时统计";
				this.pdfFileName = "servPackage";
				this.pdfArrAata = this.reportResult;
			}
			//日报表
			if ("2".equals(reportType)) {
				this.pdfTitle = "套餐对应终端日统计";
				this.pdfFileName = "servPackage";
				this.pdfArrAata = this.reportResult;
			}
			//周报表
			if ("3".equals(reportType)) {
				this.pdfTitle = "套餐对应终端周统计";
				this.pdfFileName = "servPackage";
				this.pdfArrAata = this.reportResult;
			}
			//月报表
			if ("4".equals(reportType)) {
				this.pdfTitle = "套餐对应终端月统计";
				this.pdfFileName = "servPackage";
				this.pdfArrAata = this.reportResult;
			}
			
			return "pdfByStr";
		}else if("print".equals(isReport)){
			return "print";
		}else{
			return "list";
		}
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
	 * @return the reportResult
	 */
	public String[][] getReportResult() {
		return reportResult;
	}

	/**
	 * @param reportResult the reportResult to set
	 */
	public void setReportResult(String[][] reportResult) {
		this.reportResult = reportResult;
	}

	/**
	 * @return the servPackageDeviceBIO
	 */
	public I_ServPackageDeviceBIO getServPackageDeviceBIO() {
		return servPackageDeviceBIO;
	}

	/**
	 * @param servPackageDeviceBIO the servPackageDeviceBIO to set
	 */
	public void setServPackageDeviceBIO(I_ServPackageDeviceBIO servPackageDeviceBIO) {
		this.servPackageDeviceBIO = servPackageDeviceBIO;
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
	 * @return the pdfArrAata
	 */
	public String[][] getPdfArrAata() {
		return pdfArrAata;
	}

	/**
	 * @param pdfArrAata the pdfArrAata to set
	 */
	public void setPdfArrAata(String[][] pdfArrAata) {
		this.pdfArrAata = pdfArrAata;
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
	 * @return the bookparamfirst
	 */
	public String getBookparamfirst() {
		return bookparamfirst;
	}

	/**
	 * @param bookparamfirst the bookparamfirst to set
	 */
	public void setBookparamfirst(String bookparamfirst) {
		this.bookparamfirst = bookparamfirst;
	}
	
}
