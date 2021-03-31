
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.ExportSipUserReportBIO;

/**
 * 导出sip用户信息
 * 
 * @author 向正良
 */
@SuppressWarnings("rawtypes")
public class ExportSipUserReportACT extends splitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2165906582283193726L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ExportSipUserReportACT.class);
	// session
	private Map<String,Object> session;
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
	/**用户类型*/
	private String usertype;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private ExportSipUserReportBIO reportBio;
	private String userTypeId;
	private String userline;
	private List<Map> hgwList = null;
	private List<Map<String, String>> titleList;

	/**
	 * 初始化页面
	 * 
	 * @author xiangzl
	 * @param
	 * @return String
	 */
	public String init(){
		logger.debug("allinit()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		return "init";
	}


	/**
	 * 所以sip用户列表
	 * 
	 * @author xiangzl
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String getSipUserInfo(){
		logger.debug("getHgw2()");
		this.setTime();
		try
		{
			hgwList = reportBio.getHgwList(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, usertype);
			logger.warn("list.size:" + hgwList.size());
			maxPage_splitPage = reportBio.getHgwCount(starttime1, endtime1, cityId, curPage_splitPage, num_splitPage, usertype);
			logger.warn("maxPage_split" + maxPage_splitPage);
		}
		catch (Exception e)
		{
			logger.warn("错误");
			e.printStackTrace();
		}
		return "hgwlist";
	}

	/**
	 * 所有sip用户列表导出
	 * 
	 * @author xiangzl
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getSipUserInfoExcel(){
		logger.debug("getHgwExcel2()");
		fileName = "SIP用户";
		title = new String[] { "逻辑ID", "属  地", "Voip认证号码", "电话号码", "协议类型", "是否下发数图" };
		column = new String[] { "username", "city_name", "voip_username", "voip_phone",
				"protocol", "is_config" };
		this.setTime();
		data = reportBio.getHgwExcel(starttime1, endtime1, cityId, usertype);
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
	 * @return the session
	 */
	public Map<String,Object> getSession(){
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map<String,Object> session){
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
	public List<Map> getData(){
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data){
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
	 * @return the hgwList
	 */
	public List<Map> getHgwList(){
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List<Map> hgwList){
		this.hgwList = hgwList;
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

	/**
	 * @return the titleList
	 */
	public List<Map<String, String>> getTitleList(){
		return titleList;
	}

	
	public String getUsertype(){
		return usertype;
	}

	
	public void setUsertype(String usertype){
		this.usertype = usertype;
	}

	/**
	 * @param titleList
	 *            the titleList to set
	 */
	public void setTitleList(List<Map<String, String>> titleList){
		this.titleList = titleList;
	}
	
	public ExportSipUserReportBIO getReportBio()
	{
		return reportBio;
	}
	
	public void setReportBio(ExportSipUserReportBIO reportBio)
	{
		this.reportBio = reportBio;
	}
}
