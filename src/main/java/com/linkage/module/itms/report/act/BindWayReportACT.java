
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.BindWayReportBIO;

/**
 * 用户绑定信息统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BindWayReportACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BindWayReportACT.class);
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
	/**用户类型*/
	private String usertype;
	/**是否活跃 1：是 ，0：否**/
	private String is_active="";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	//运行商
	private String telecom;
	private BindWayReportBIO bindWayReportBio;
	private String userTypeId;
	private String userline;
	private String isChkBind;
	private List<Map> hgwList = null;
	private List<Map<String, String>> titleList;

	/**
	 * 初始化页面
	 * 
	 * @author wangsenbo
	 * @date Mar 1, 2010
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
	 * 统计
	 * 
	 * @author wangsenbo
	 * @date Mar 1, 2010
	 * @param
	 * @return String
	 */
	public String execute(){
		logger.debug("execute()");
		this.setTime();
		data = bindWayReportBio.countBindWay(starttime1, endtime1, cityId,null,is_active);
		return "list";
	}

	/**
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getExcel(){
		logger.debug("getExcel()");
		fileName = "BINDWAY";
		this.setTime();
		title = new String[] { "属地", "总开户数", "MAC比对新建用户", "手工绑定", "自助绑定", "MAC比对绑定",
				"有效绑定数", "自助绑定MAC认证", "手工绑定MAC认证" };
		column = new String[] { "city_name", "allopened", "macuser", "handbind",
				"selfbind", "macbind", "effectivebind", "handmac", "selfmac" };
		data = bindWayReportBio.countBindWay(starttime1, endtime1, cityId,null,is_active);
		return "excel";
	}

	/**
	 * 获取用户列表
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getHgw(){
		logger.debug("getHgw()");
		hgwList = bindWayReportBio.getHgwList(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, curPage_splitPage, num_splitPage, null,null,is_active);
		maxPage_splitPage = bindWayReportBio.getHgwCount(starttime1, endtime1, cityId,
				userTypeId, userline, isChkBind, curPage_splitPage, num_splitPage, null,null,is_active);
		return "hgwlist";
	}

	/**
	 * 用户信息导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getHgwExcel(){
		logger.debug("getHgw()");
		fileName = "IPTV用户";
		title = new String[] { "用户帐号", "属  地", "用户来源", "绑定设备", "开户时间", "竣工状态" };
		column = new String[] { "username", "city_name", "user_type", "device",
				"opendate", "opmode" };
		data = bindWayReportBio.getHgwExcel(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, null,null,is_active);
		return "excel";
	}

	/**
	 * 所有绑定方式初始化页面
	 * 
	 * @author wangsenbo
	 * @date May 18, 2010
	 * @param
	 * @return String
	 */
	public String allinit(){
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
		telecom = LipossGlobals.getLipossProperty("telecom");
		return "allinit";
	}

	/**
	 * 所有绑定方式统计
	 * 
	 * @author wangsenbo
	 * @date May 18, 2010
	 * @param
	 * @return String
	 */
	public String countAll(){
		logger.debug("countAll()");
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		String userTypeName = "";
		if("1".equals(usertype)){
			userTypeName = "E8-B";
		}else if("2".equals(usertype)){
			userTypeName = "E8-C";
		}
		logger.warn("用户绑定方式统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId)+"   用户终端类型："+userTypeName);
		this.setTime();
		titleList = bindWayReportBio.getAllBindWay();
		data = bindWayReportBio.countAllBindWay(starttime1, endtime1, cityId,usertype,is_active);
		return "alllist";
	}

	/**
	 * 所有绑定方式导出
	 * 
	 * @author wangsenbo
	 * @date May 18, 2010
	 * @param
	 * @return String
	 */
	public String getAllBindWayExcel(){
		logger.debug("getAllBindWayExcel()");
		fileName = "BINDWAY";
		titleList = bindWayReportBio.getAllBindWay();
		title = new String[titleList.size() + 5];
		column = new String[titleList.size() + 5];
		title[0] = "属地";
		title[1] = "总开户数";
		title[2] = "同步用户数";
		
		column[0] = "city_name";
		column[1] = "allopened";
		column[2] = "synCount";
		for (int i = 0; i < titleList.size(); i++)
		{
			Map<String, String> map = titleList.get(i);
			title[i + 3] = map.get("type_name");
			column[i + 3] = map.get("type_name");
		}
		title[titleList.size() + 3] = "已绑定用户数";
		column[titleList.size() + 3] = "bindnum";
		title[titleList.size() + 4] = "自动绑定率";
		column[titleList.size() + 4] = "percent";
		data = bindWayReportBio.countAllBindWay(starttime1, endtime1, cityId,usertype,is_active);
		return "excel";
	}

	/**
	 * 所有绑定方式用户列表
	 * 
	 * @author wangsenbo
	 * @date May 19, 2010
	 * @param
	 * @return String
	 */
	public String getHgw2(){
		logger.debug("getHgw2()");
		hgwList = bindWayReportBio.getHgwList(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, curPage_splitPage, num_splitPage, null,usertype,is_active);
		maxPage_splitPage = bindWayReportBio.getHgwCount(starttime1, endtime1, cityId,
				userTypeId, userline, isChkBind, curPage_splitPage, num_splitPage, null,usertype,is_active);
		return "allhgwlist";
	}

	/**
	 * 所有绑定方式用户列表导出
	 * 
	 * @author wangsenbo
	 * @date Mar 2, 2010
	 * @param
	 * @return String
	 */
	public String getHgwExcel2(){
		logger.debug("getHgwExcel2()");
		fileName = "IPTV用户";
		title = new String[] { "用户帐号", "属  地", "用户来源", "绑定设备", "开户时间", "绑定方式" };
		column = new String[] { "username", "city_name", "user_type", "device",
				"opendate", "bindtype" };
		data = bindWayReportBio.getHgwExcel(starttime1, endtime1, cityId, userTypeId,
				userline, isChkBind, null,usertype,is_active);
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

	/**
	 * @return the bindWayReportBio
	 */
	public BindWayReportBIO getBindWayReportBio(){
		return bindWayReportBio;
	}

	/**
	 * @param bindWayReportBio
	 *            the bindWayReportBio to set
	 */
	public void setBindWayReportBio(BindWayReportBIO bindWayReportBio){
		this.bindWayReportBio = bindWayReportBio;
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
	
	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	
	public String getIs_active()
	{
		return is_active;
	}

	
	public void setIs_active(String is_active)
	{
		this.is_active = is_active;
	}
	
}
