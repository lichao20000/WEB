/**
 * 
 */
package com.linkage.module.gwms.report.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.report.bio.BindMonthCountReportBIO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-11-13
 * @category com.linkage.module.gwms.report.act
 * 
 */
public class BindMonthCountReportACT extends ActionSupport implements ServletRequestAware{

	Logger logger = LoggerFactory.getLogger(BindMonthCountReportACT.class);
	
	/**
	 * aaaaaaaaaaa
	 */
	private static final long serialVersionUID = 1L;

	// request取登陆帐号使用
	private HttpServletRequest request;
	
	/**
	 * BIO
	 */
	private BindMonthCountReportBIO bmcBIO = null;
	
	/**
	 * 查询的属地
	 */
	private String cityId = null;
	
	/**
	 * 统计时间（YYYY-MM-DD格式）
	 */
	private String endData = null;
	
	/**
	 * 统计时间（偏移量到秒）
	 */
	private String endDataInt = null;
	
	/**
	 * 返回结果
	 */
	private List countList = null;

	/**
	 * 报表类型
	 */
	private String gwType = null;
	
	/**
	 * 导出报表的类型
	 */
	private String reportType = null;
	
	/**
	 * ECXCEL报表相关
	 */
	private String fileName =  null;
	private String[] title = null;
	private String[] column =  null;
	private ArrayList<Map> data;
	
	/**
	 * 初始化入口，初始化统计时间，统计时间为当前时间的1号
	 * 
	 * @return
	 */
	public String execute(){
		
		logger.debug("execute()");
		
		if(null==this.cityId || "".equals(this.cityId)){
			logger.debug("BindMonthCountReportACT=>getReportData()传入的cityId为空！");
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_2_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		this.endData = sdf.format(cal.getTime());
		
		this.gwType = String.valueOf(LipossGlobals.SystemType());
		
		return SUCCESS;
	}
	
	/**
	 * 统计数据
	 */
	@SuppressWarnings("unchecked")
	public String getReportData(){
		logger.debug("getReportData()");
		
		if(null==this.cityId || "".equals(this.cityId)){
			logger.debug("BindMonthCountReportACT=>getReportData()传入的cityId为空！");
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			this.cityId = curUser.getCityId();
		}
		
		this.gwType = String.valueOf(LipossGlobals.SystemType());
		
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_2_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(endDataInt)*1000);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		this.endData = sdf.format(cal.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		this.countList = bmcBIO.getData(cityId,cal.getTimeInMillis()/1000);
		
		if("excel".equals(reportType)){
			this.fileName = this.endData+"绑定率月报表";
			this.title = new String[4];
			this.column = new String[4];
			this.title[0] = "属地";
			this.title[1] = "上报上来的与用户有绑定关系的资源数量";
			this.title[2] = "CRM工单过来的在用用户数";
			this.title[3] = "设备占用户比例";
			this.column[0] = "city_name";
			this.column[1] = "deviceCount";
			this.column[2] = "userCount";
			this.column[3] = "rate";
			this.data = new ArrayList();
			for(int i=0;i<this.countList.size();i++){
				this.data.add((Map)this.countList.get(i));
			}
			return "excel";
		}else{
			return "list";
		}
	}
	
	/**
	 * set
	 */
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;		
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
	 * @return the endData
	 */
	public String getEndData() {
		return endData;
	}

	/**
	 * @param endData the endData to set
	 */
	public void setEndData(String endData) {
		this.endData = endData;
	}

	/**
	 * @return the endDataInt
	 */
	public String getEndDataInt() {
		return endDataInt;
	}

	/**
	 * @param endDataInt the endDataInt to set
	 */
	public void setEndDataInt(String endDataInt) {
		this.endDataInt = endDataInt;
	}

	/**
	 * @return the countList
	 */
	public List getCountList() {
		return countList;
	}

	/**
	 * @param countList the countList to set
	 */
	public void setCountList(List countList) {
		this.countList = countList;
	}

	/**
	 * @return the bmcBIO
	 */
	public BindMonthCountReportBIO getBmcBIO() {
		return bmcBIO;
	}

	/**
	 * @param bmcBIO the bmcBIO to set
	 */
	public void setBmcBIO(BindMonthCountReportBIO bmcBIO) {
		this.bmcBIO = bmcBIO;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
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
	 * @return the gwType
	 */
	public String getGwType() {
		return gwType;
	}

	/**
	 * @param gwType the gwType to set
	 */
	public void setGwType(String gwType) {
		this.gwType = gwType;
	}
	
}
