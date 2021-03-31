/**
 * 
 */
package com.linkage.module.bbms.report.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.bio.EVDOCountReportBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category action.report
 * 
 */
@SuppressWarnings("unchecked")
public class EVDOCountReportACT extends ActionSupport implements ServletRequestAware {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bio
	 */
	EVDOCountReportBIO evdoBIO;

	/**
	 * 属地列表
	 */
	private List cityList = null;

	/**
	 * 属地
	 */
	private String cityId = null;

	/**
	 * 开始时间
	 */
	private String startTime = null;
	
	/**
	 * 结束时间
	 */
	private String endTime = null;

	/**
	 * 报表结果数据
	 */
	private List reportResult = null;

	/**
	 * 是否是报表
	 */
	private String isReport = null;
	
	//下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//需要取的列
	private String[] tbTitle = null;
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	// request取登陆帐号使用
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	/**
	 * 入口方法
	 */
	public String execute() throws Exception {
		
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf2 = new SimpleDateFormat(DATE_2_FORMAT);
		
		Calendar cal = Calendar.getInstance();
		this.endTime = sdf2.format(cal.getTime());
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		this.startTime = sdf2.format(cal.getTime());
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if(null==this.cityId || "".equals(this.cityId)){
			this.cityId = curUser.getCityId();
		}
		
		this.cityList = CityDAO.getNextCityListByCityPid(this.cityId);
		
		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String getDataReport() throws ParseException {

		String date1 = " 00:00:00";
		String date2 = " 23:59:59";
		
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long startDate = simpleDateFormat2.parse(this.startTime + date1).getTime()/1000;
		long endDate = simpleDateFormat2.parse(this.endTime + date2).getTime()/1000;
		
		this.reportResult = evdoBIO.getReportData(cityId, startDate, endDate);
		
		if("excel".equals(isReport)){
			this.fileName = "EVDO网关统计";
			this.title = evdoBIO.getTbTitle() ;
			this.column = evdoBIO.getTbName();
			this.data = new ArrayList();
			for(int i=0;i<this.reportResult.size();i++){
				Map cityMap = (Map) this.reportResult.get(i);
				data.add(cityMap);
			}
			return "excel";
		}else if("pdf".equals(isReport)){
			this.pdfListData = this.reportResult;
			this.pdfFileName = "EVDO网关统计";
			this.pdfTitle = "EVDO网关统计";
			this.tbTitle = evdoBIO.getTbTitle() ;
			this.tbName = evdoBIO.getTbName();
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
	 * @param cityId the cityId to set
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
	 * @param cityList the cityList to set
	 */
	public void setCityList(List cityList) {
		this.cityList = cityList;
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
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the evdoBIO
	 */
	public EVDOCountReportBIO getEvdoBIO() {
		return evdoBIO;
	}

	/**
	 * @param evdoBIO the evdoBIO to set
	 */
	public void setEvdoBIO(EVDOCountReportBIO evdoBIO) {
		this.evdoBIO = evdoBIO;
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
	 * @return the reportResult
	 */
	public List getReportResult() {
		return reportResult;
	}

	/**
	 * @param reportResult the reportResult to set
	 */
	public void setReportResult(List reportResult) {
		this.reportResult = reportResult;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
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

	
}
