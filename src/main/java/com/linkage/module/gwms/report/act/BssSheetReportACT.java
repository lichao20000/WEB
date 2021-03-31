/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.act.interf.I_BssSheetReportACT;
import com.linkage.module.gwms.report.bio.interf.I_BssSheetReportBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category action.report
 * 
 */
@SuppressWarnings("unchecked")
public class BssSheetReportACT extends ActionSupport implements
		ServletRequestAware, I_BssSheetReportACT {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bio
	 */
	I_BssSheetReportBIO bssSheetReportBio;

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
	private List reportResult = null;

	/**
	 * 是否是报表
	 */
	private String isReport = null;
	
	/**
	 * 定制报表是否显示
	 */
	private String booked = "";

	private String sunm1display = "none";
	private String sunm2display = "none";
	private String sunm3display = "none";
	private String sunm4display = "none";
	private String sunm5display = "none";
	private String sunm6display = "none";
	private String sunm7display = "none";
	private String sunm8display = "none";
	
	/**
	 * 报表定制列
	 */
	private String bookparam = null;
	
	//下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//table tile
	//需要取的列
	private String[] tbTitle = null;
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
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
		
		return SUCCESS;
	}

	public String getDayReport() throws ParseException {

		String date1 = " 00:00:00";
		String date2 = " 23:59:59";
		
		if(null!=this.bookparam && !"".equals(this.bookparam)){
			String tempParam[] = this.bookparam.split("\\$"); 
			for(int i=0;i<tempParam.length;i++){
				if("sum1".equals(tempParam[i])){
					this.sunm1display = "";
				}
				if("sum2".equals(tempParam[i])){
					this.sunm2display = "";
				}
				if("sum3".equals(tempParam[i])){
					this.sunm3display = "";
				}
				if("sum4".equals(tempParam[i])){
					this.sunm4display = "";
				}
				if("sum5".equals(tempParam[i])){
					this.sunm5display = "";
				}
				if("sum6".equals(tempParam[i])){
					this.sunm6display = "";
				}
				if("sum7".equals(tempParam[i])){
					this.sunm7display = "";
				}
				if("sum8".equals(tempParam[i])){
					this.sunm8display = "";
				}
			}
		}else{
			this.sunm1display = "";
			this.sunm2display = "";
			this.sunm3display = "";
			this.sunm4display = "";
			this.sunm5display = "";
			this.sunm6display = "";
			this.sunm7display = "";
			this.sunm8display = "";
		}
		
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
			
			this.reportResult = bssSheetReportBio.getReportData(cityId,startDate, endDate);

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
			this.reportResult = bssSheetReportBio.getReportData(cityId,startDate, endDate);

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

			long startDate = simpleDateFormat2.parse(strStartDate.toString()).getTime();
			long endDate = simpleDateFormat2.parse(strEndDate.toString()).getTime();
			
			this.reportResult = bssSheetReportBio.getReportData(cityId,startDate, endDate);

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

			this.reportResult = bssSheetReportBio.getReportData(cityId,
					startDate, endDate);
		}
		
		if("excel".equals(isReport)){
			return "excelReport";
		}else if("pdf".equals(isReport)){
			
			if(null!=this.bookparam && !"".equals(this.bookparam)){
				String tempParam[] = this.bookparam.split("\\$"); 
				this.pdfListData = new ArrayList();
				for(int j=0;j<this.reportResult.size();j++){
					Map one = (Map)this.reportResult.get(j);
					Map<String,String> oneNew = new HashMap<String,String>();
					oneNew.put("city_name", (String) one.get("city_name"));
					oneNew.put("city_id", (String)one.get("city_id"));
					oneNew.put("haschild", (String)one.get("haschild"));
					oneNew.put("sum9", (String)one.get("sum9"));
					oneNew.put("sum10",(String) one.get("sum10"));
					oneNew.put("sum11", (String)one.get("sum11"));
					for(int i=0;i<tempParam.length;i++){
						oneNew.put(tempParam[i], (String)one.get(tempParam[i]));
					}
					this.pdfListData.add(oneNew);
				}
				
				this.tbTitle = new String[4+tempParam.length];
				this.tbName = new String[4+tempParam.length];
				this.tbTitle[0] = "属地";
				this.tbTitle[1+tempParam.length] = "统计工单总数";
				this.tbTitle[2+tempParam.length] = "成功工单数";
				this.tbTitle[3+tempParam.length] = "成功率(%)";
				
				this.tbName[0] = "city_name";
				this.tbName[1+tempParam.length] = "sum9";
				this.tbName[2+tempParam.length] = "sum10";
				this.tbName[3+tempParam.length] = "sum11";
				
				for(int i=0;i<tempParam.length;i++){
					this.tbName[i+1] = tempParam[i];
					if("sum1".equals(tempParam[i])){
						this.tbTitle[i+1] = "开户";
					}
					if("sum2".equals(tempParam[i])){
						this.tbTitle[i+1] = "暂停";
					}
					if("sum3".equals(tempParam[i])){
						this.tbTitle[i+1] = "销户";
					}
					if("sum4".equals(tempParam[i])){
						this.tbTitle[i+1] = "复机";
					}
					if("sum5".equals(tempParam[i])){
						this.tbTitle[i+1] = "更改速率";
					}
					if("sum6".equals(tempParam[i])){
						this.tbTitle[i+1] = "更改账号";
					}
					if("sum7".equals(tempParam[i])){
						this.tbTitle[i+1] = "更换设备";
					}
					if("sum8".equals(tempParam[i])){
						this.tbTitle[i+1] = "更改IP";
					}
				}
				
			}else{
				this.pdfListData = this.reportResult;
				this.tbTitle = bssSheetReportBio.getTbTitle() ;
				this.tbName = bssSheetReportBio.getTbName();
			}
			
			//小时报表
			if ("1".equals(reportType)) {
				this.pdfTitle = "工单执行情况小时统计";
				this.pdfFileName = "bssSheet";
			}
			//日报表
			if ("2".equals(reportType)) {
				this.pdfTitle = "工单执行情况日统计";
				this.pdfFileName = "bssSheet";
			}
			//周报表
			if ("3".equals(reportType)) {
				this.pdfTitle = "工单执行情况周统计";
				this.pdfFileName = "bssSheet";
			}
			//月报表
			if ("4".equals(reportType)) {
				this.pdfTitle = "工单执行情况月统计";
				this.pdfFileName = "bssSheet";
			}
			
			return "pdfByList";
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
	public List getReportResult() {
		return reportResult;
	}

	/**
	 * @param reportResult
	 *            the reportResult to set
	 */
	public void setReportResult(List reportResult) {
		this.reportResult = reportResult;
	}

	/**
	 * @return the bssSheetReportBio
	 */
	public I_BssSheetReportBIO getBssSheetReportBio() {
		return bssSheetReportBio;
	}

	/**
	 * @param bssSheetReportBio
	 *            the bssSheetReportBio to set
	 */
	public void setBssSheetReportBio(I_BssSheetReportBIO bssSheetReportBio) {
		this.bssSheetReportBio = bssSheetReportBio;
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
	 * @return the sunm1display
	 */
	public String getSunm1display() {
		return sunm1display;
	}

	/**
	 * @param sunm1display the sunm1display to set
	 */
	public void setSunm1display(String sunm1display) {
		this.sunm1display = sunm1display;
	}

	/**
	 * @return the sunm2display
	 */
	public String getSunm2display() {
		return sunm2display;
	}

	/**
	 * @param sunm2display the sunm2display to set
	 */
	public void setSunm2display(String sunm2display) {
		this.sunm2display = sunm2display;
	}

	/**
	 * @return the sunm3display
	 */
	public String getSunm3display() {
		return sunm3display;
	}

	/**
	 * @param sunm3display the sunm3display to set
	 */
	public void setSunm3display(String sunm3display) {
		this.sunm3display = sunm3display;
	}

	/**
	 * @return the sunm4display
	 */
	public String getSunm4display() {
		return sunm4display;
	}

	/**
	 * @param sunm4display the sunm4display to set
	 */
	public void setSunm4display(String sunm4display) {
		this.sunm4display = sunm4display;
	}

	/**
	 * @return the sunm5display
	 */
	public String getSunm5display() {
		return sunm5display;
	}

	/**
	 * @param sunm5display the sunm5display to set
	 */
	public void setSunm5display(String sunm5display) {
		this.sunm5display = sunm5display;
	}

	/**
	 * @return the sunm6display
	 */
	public String getSunm6display() {
		return sunm6display;
	}

	/**
	 * @param sunm6display the sunm6display to set
	 */
	public void setSunm6display(String sunm6display) {
		this.sunm6display = sunm6display;
	}

	/**
	 * @return the sunm7display
	 */
	public String getSunm7display() {
		return sunm7display;
	}

	/**
	 * @param sunm7display the sunm7display to set
	 */
	public void setSunm7display(String sunm7display) {
		this.sunm7display = sunm7display;
	}

	/**
	 * @return the sunm8display
	 */
	public String getSunm8display() {
		return sunm8display;
	}

	/**
	 * @param sunm8display the sunm8display to set
	 */
	public void setSunm8display(String sunm8display) {
		this.sunm8display = sunm8display;
	}
	
}
