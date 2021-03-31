
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.BindWayReportBIO;
import com.linkage.module.itms.report.bio.DeviceCountReportBIO;

/**
 * 设备信息统计
 * 
 * @author liyl
 */
@SuppressWarnings("unchecked")
public class DeviceCountReportACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceCountReportACT.class);
	// session
	private Map session;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	/**终端类型*/
	private String accessstyle;

	// 导出数据
	private List<Map<String,String>> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	//运行商
	private String telecom;
	private DeviceCountReportBIO deviceCountReportBio;
	private String userTypeId;
	private String userline;
	private String isChkBind;
	private List<Map<String,String>> detailList = null;
	private List<Map<String, String>> countList;
	private HttpServletRequest request;
	private String ajax="";
	/**
	 * 初始化页面
	 * 
	 * @author liyl10
	 * @date Nov 24, 2014
	 * @param
	 * @return String
	 */
	public String init(){
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}
	
	/**
	 * 统计已绑定设备
	 * 
	 * @author liyl10
	 * @date Nov 24, 2014
	 * @parm 
	 * @return String
	 */
	public String countDevice(){
		logger.warn("DeviceCountReportACT().countDevice()");
		countList = deviceCountReportBio.countDevice(starttime,endtime,cityId,accessstyle);
		return "countList";
	}
	
	/**
	 * 
	 * @author liyl10
	 * @date Nov 25, 2014
	 * @parm
	 * @return String
	 */
	public String getDetail(){
		logger.warn("DeviceCountReportACT().getDetail()");
		
		detailList = deviceCountReportBio.getDetailForPage(curPage_splitPage,num_splitPage,cityId,accessstyle,starttime,endtime);
		maxPage_splitPage = deviceCountReportBio.getDetailCount(num_splitPage, cityId, accessstyle, starttime, endtime);
		
		return "detail";
	}
	
	public String goPage() throws Exception {
		logger.warn("DeviceCountReportACT().goPage()");
		detailList = deviceCountReportBio.getDetailForPage(curPage_splitPage,num_splitPage,cityId,accessstyle,starttime,endtime);

		maxPage_splitPage = deviceCountReportBio.getDetailCount(num_splitPage, cityId, accessstyle, starttime, endtime);

		return "detail";
	}

	
	/**
	 * 导出设备统计excel
	 * 
	 * @author liyl10
	 * @date Nov 25, 2014
	 * @param
	 * @return String
	 */
	public String countDeviceToExcel(){
		logger.debug("countDeviceToExcel()");
		fileName = "countDevice";
		title = new String[6];
		column = new String[6];
		title[0] = "属地";
		title[1] = "ADSL";
		title[2] = "LAN";
		title[3] = "EPON";
		title[4] = "GPON";
		title[5] = "总数";
		
		column[0] = "city_name";
		column[1] = "adslNum";
		column[2] = "lanNum";
		column[3] = "eponNum";
		column[4] = "gponNum";
		column[5] = "totalNum";
		data = deviceCountReportBio.countDevice(starttime,endtime,cityId,accessstyle);
		return "excel";
	}
	
	/**
	 * 导出设备详细excel
	 * 
	 * @author liyl10
	 * @date Nov 25, 2014
	 * @param
	 * @return String
	 */
	public String countDeviceDetailToExcel(){
		logger.debug("countDeviceDetailToExcel()");
		fileName = "countDeviceDetail";
		title = new String[9];
		column = new String[9];
		title[0] = "属地";
		title[1] = "入网时间";
		title[2] = "设备厂商";
		title[3] = "型号";
		title[4] = "终端规格";
		title[5] = "LOID";
		title[6] = "用户账号";
		title[7] = "软件版本";
		title[8] = "设备序列号";
		
		column[0] = "city_name";
		column[1] = "time";
		column[2] = "vendor_name";
		column[3] = "device_model";
		column[4] = "device_type";
		column[5] = "loid";
		column[6] = "username";
		column[7] = "softwareversion";
		column[8] = "device_serialnumber";
		
		//data = deviceCountReportBio.getDetail(cityId,accessstyle,starttime,endtime);
		data = deviceCountReportBio.getDetailForPage(curPage_splitPage,num_splitPage,cityId,accessstyle,starttime,endtime);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	/**
	 * 双栈开通数据统计
	 *
	 * @return
	 */
	public String getDualStack()
	{
		logger.warn("getDualStack");
		ajax = deviceCountReportBio.getDualStack();
		return "ajax";
	}

	/**
	 * @return the session
	 */
	public Map getSession(){
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session){
		this.session = session;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime(){
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime){
		this.starttime = starttime;
	}

	/**
	 * @return the starttime1
	 */
	public String getStarttime1(){
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1){
		this.starttime1 = starttime1;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList(){
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId(){
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	/**
	 * @return the data
	 */
	public List<Map<String,String>> getData(){
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map<String,String>> data){
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle(){
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title){
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn(){
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column){
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName(){
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	/**
	 * @return the userTypeId
	 */
	public String getUserTypeId(){
		return userTypeId;
	}

	/**
	 * @param userTypeId
	 *            the userTypeId to set
	 */
	public void setUserTypeId(String userTypeId){
		this.userTypeId = userTypeId;
	}

	/**
	 * @return the userline
	 */
	public String getUserline(){
		return userline;
	}

	/**
	 * @param userline
	 *            the userline to set
	 */
	public void setUserline(String userline){
		this.userline = userline;
	}

	/**
	 * @return the isChkBind
	 */
	public String getIsChkBind(){
		return isChkBind;
	}

	/**
	 * @param isChkBind
	 *            the isChkBind to set
	 */
	public void setIsChkBind(String isChkBind){
		this.isChkBind = isChkBind;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime(){
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime){
		this.endtime = endtime;
	}

	/**
	 * @return the endtime1
	 */
	public String getEndtime1(){
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1){
		this.endtime1 = endtime1;
	}

	public DeviceCountReportBIO getDeviceCountReportBio() {
		return deviceCountReportBio;
	}



	public void setDeviceCountReportBio(DeviceCountReportBIO deviceCountReportBio) {
		this.deviceCountReportBio = deviceCountReportBio;
	}



	public List<Map<String,String>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<String,String>> detailList) {
		this.detailList = detailList;
	}
	
	/**
	 * @return the countList
	 */
	public List<Map<String, String>> getCountList(){
		return countList;
	}

	
	public String getAccessstyle() {
		return accessstyle;
	}

	public void setAccessstyle(String accessstyle) {
		this.accessstyle = accessstyle;
	}
	/**
	 * @param countList
	 *            the countList to set
	 */
	public void setCountList(List<Map<String, String>> countList){
		this.countList = countList;
	}
	
	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
}
