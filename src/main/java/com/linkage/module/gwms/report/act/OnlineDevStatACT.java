package com.linkage.module.gwms.report.act;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.act.interf.I_OnlineDevStatACT;
import com.linkage.module.gwms.report.bio.interf.I_OnlineDevStatBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2009-4-29
 */
public class OnlineDevStatACT extends ActionSupport implements SessionAware,I_OnlineDevStatACT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger log = LoggerFactory
			.getLogger(OnlineDevStatACT.class);

	// 报表类型
	private String reportType;
	// 日期
	private long shortDate = 0;
	// 属地
	private String cityId;
	// 属地下拉列表
	private List cityList;
	//月报表的类型
	private String chartType;
	//月报表的时间点
	private String time_point;
	/**执行状态
	 * 1表示执行成功，0表示未执行，-1表示执行失败，100全部
	 * */
	@SuppressWarnings("unused")
	private String exeStatus;

	// BIO
	private I_OnlineDevStatBIO onlineStatBio;
	// session用户取登陆用户属地信息
	private Map session;
	
	//统计数据
	private String ajax;

	private String shortDateSrc;
	
	/**
	 * isReport
	 */
	private String isReport = null;
	
	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
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
	
	
	public String execute() {
		// 如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
			log.debug("curUser.city_id: " + cityId);
		}
		// 属地下拉列表
		cityList = CityDAO.getAllNextCityListByCityPid(cityId);
		// 返回页面
		return "success";
	}

	public String statExecute() {
		
		//如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		
		long shortDateEnd = 0;
		
		if("0".equals(this.reportType)){
			
			if(0==shortDate){
				
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
				
			}
			shortDateEnd = shortDate + 24*60*60;
			
			//List<List<Map<String, String>>> statList = onlineStatBio.getStatList(cityId, shortDate,shortDateEnd);
			//ajax = onlineStatBio.listToString(statList);
			
			ajax = onlineStatBio.getDataOnLine(isReport,reportType,chartType, cityId, shortDate, shortDateEnd);
			
			return "ajax";
		}else{
			
			if(0==shortDate){
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
				
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(shortDate*1000);
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
				shortDate = cal.getTimeInMillis()/1000;
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
			}
			
			ajax = onlineStatBio.getMonthOnLine(isReport,reportType,chartType, cityId, shortDate, shortDateEnd, time_point);
			
			return "ajax";
			
		}
		
	}

	public String excel(){
		
		//如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		
		long shortDateEnd = 0;
		
		if("0".equals(this.reportType)){
			
			if(0==shortDate){
				
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
				
			}
			shortDateEnd = shortDate + 24*60*60;
			this.fileName = "在线设备日报表";
			this.title = new String[3];
			this.column = new String[3];
			title[0] = "设备属地";
			title[1] = "采集时间点";
			title[2] = "在线设备数量";
			column[0] = "city_name";
			column[1] = "r_time";
			column[2] = "r_count";
			this.data = onlineStatBio.getDataOnLineData(reportType,chartType, cityId, shortDate, shortDateEnd);

		}else{
			
			if(0==shortDate){
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
				
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(shortDate*1000);
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
				shortDate = cal.getTimeInMillis()/1000;
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
			}

			this.fileName = "在线设备月报表";
			this.title = new String[3];
			this.column = new String[3];
			title[0] = "设备属地";
			title[1] = "采集时间点";
			title[2] = "在线设备数量";
			column[0] = "city_name";
			column[1] = "r_time";
			column[2] = "r_count";
			this.data = onlineStatBio.getMonthOnLineData(reportType,chartType, cityId, shortDate, shortDateEnd, time_point);

		}
		return "excel";
	}
	
	public String pdf(){
		
		//如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		
		long shortDateEnd = 0;
		
		if("0".equals(this.reportType)){
			
			if(0==shortDate){
				
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
				
			}
			shortDateEnd = shortDate + 24*60*60;
			this.pdfFileName = "在线设备日报表";
			this.pdfTitle = "在线设备日报表";
			this.tbTitle = new String[3];
			this.tbName = new String[3];
			tbTitle[0] = "设备属地";
			tbTitle[1] = "采集时间点";
			tbTitle[2] = "在线设备数量";
			tbName[0] = "city_name";
			tbName[1] = "r_time";
			tbName[2] = "r_count";
			
			ArrayList<Map> arrayList = onlineStatBio.getDataOnLineData(reportType,chartType, cityId, shortDate, shortDateEnd);
			
			this.pdfListData = new ArrayList<Map<String,String>>();

			for(int i=0;i<arrayList.size();i++){
				this.pdfListData.add(arrayList.get(i));
			}

		}else{
			
			if(0==shortDate){
				Calendar cal = Calendar.getInstance();
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
		        shortDate = cal.getTimeInMillis()/1000;
		        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
				
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(shortDate*1000);
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,0,0,0);
				shortDate = cal.getTimeInMillis()/1000;
				cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1,0,0,0);
				shortDateEnd = cal.getTimeInMillis()/1000;
			}
			
			this.pdfFileName = "在线设备月报表";
			this.pdfTitle = "在线设备月报表";
			this.tbTitle = new String[3];
			this.tbName = new String[3];
			tbTitle[0] = "设备属地";
			tbTitle[1] = "采集时间点";
			tbTitle[2] = "在线设备数量";
			tbName[0] = "city_name";
			tbName[1] = "r_time";
			tbName[2] = "r_count";
			
			ArrayList<Map> arrayList = onlineStatBio.getMonthOnLineData(reportType,chartType, cityId, shortDate, shortDateEnd, time_point);
			
			
			this.pdfListData = new ArrayList<Map<String,String>>();
			
			for(int i=0;i<arrayList.size();i++){
				this.pdfListData.add(arrayList.get(i));
			}
			
		}
		return "pdfByList";
	}
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public long getShortDate() {
		return shortDate;
	}

	public void setShortDate(long shortDate) {
		this.shortDate = shortDate;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}

	/**
	 * @return the onlineStatBio
	 */
	public I_OnlineDevStatBIO getOnlineStatBio() {
		return onlineStatBio;
	}

	/**
	 * @param onlineStatBio the onlineStatBio to set
	 */
	public void setOnlineStatBio(I_OnlineDevStatBIO onlineStatBio) {
		this.onlineStatBio = onlineStatBio;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	public String getAjax() {
		return ajax;
	}

	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}

	public String getShortDateSrc() {
		return shortDateSrc;
	}

	public void setShortDateSrc(String shortDateSrc) {
		this.shortDateSrc = shortDateSrc;
	}

	/**
	 * @return the chartType
	 */
	public String getChartType() {
		return chartType;
	}

	/**
	 * @param chartType the chartType to set
	 */
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	/**
	 * @return the time_point
	 */
	public String getTime_point() {
		return time_point;
	}

	/**
	 * @param time_point the time_point to set
	 */
	public void setTime_point(String time_point) {
		this.time_point = time_point;
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
	 * @param column the column to set
	 */
	public void setColumn(String[] column) {
		this.column = column;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
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
	 * @return the column
	 */
	public String[] getColumn() {
		return column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
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

}
