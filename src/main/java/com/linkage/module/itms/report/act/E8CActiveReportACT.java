package com.linkage.module.itms.report.act;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.E8CActiveReportBIO;

public class E8CActiveReportACT extends splitPageAction implements ServletRequestAware,
		SessionAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(E8CActiveReportACT.class);
	HttpServletRequest request = null;
	private E8CActiveReportBIO bio;
	// session
	private Map<String, Object> session;
	private String startDealdate="";
	
	private String startDealdate1="";
	
	private String endDealdate="";
	
	private String endDealdate1="";
	
	private String cityId;
	
	private String isActive;
	
	private String type;
	
	private String ttitle1;
	private String ttitle2;
	private String ttitle3;
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	private List<Map> list = new ArrayList<Map>();
	private Map<String, String> map = new HashMap<String, String>();
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	@Override
	public String execute() throws Exception {
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		initTime();
		return "init";
	}
	
	public String getE8CData(){
		this.setTime();
		if (isActive.equals("0")){
			ttitle1="E8C活跃竣工用户数";
			ttitle2="终端不活跃的用户数";
			ttitle3="不活跃率";
		}else{
			ttitle1="E8C活跃竣工用户数";
			ttitle2="绑定活跃终端的用户数";
			ttitle3="活跃率";
		}
		list=bio.getE8CNum(startDealdate1, endDealdate1, cityId, isActive);
		return "dataList";
	}
	public String getCityExcel(){
		this.setTime();
		if (isActive.equals("0")){
			ttitle1="E8C活跃竣工用户数";
			ttitle2="终端不活跃的用户数";
			ttitle3="不活跃率";
		}else{
			ttitle1="E8C活跃竣工用户数";
			ttitle2="绑定活跃终端的用户数";
			ttitle3="活跃率";
		}
		column = new String[] { "city_name","totals", "total","percent" };
		title = new String[] { "属  地", ttitle1, ttitle2, ttitle3};
		fileName = "E8-C活跃终端管理率";
		data = bio.getE8CNum(startDealdate1, endDealdate1, cityId, isActive);
		return "excel";
	}
	/**
	 * 用户详细信息
	 * @return
	 */
	public String getCustomerList(){
		list=bio.getCustomerLists(startDealdate1, endDealdate1, cityId, isActive,type, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.queryCusCount(startDealdate1, endDealdate1, cityId, isActive, type, curPage_splitPage, num_splitPage);
		return "cusList";
	}
	/**
	 * 分页功能
	 * @return
	 */
	public String goPage(){
		list=bio.getCustomerLists(startDealdate1, endDealdate1, cityId, isActive,type, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.queryCusCount(startDealdate1, endDealdate1, cityId, isActive,type, curPage_splitPage, num_splitPage);
		return "cusList";
	}
	
	/**
	 * 用户信息报表
	 */
	public String getCustomerListExcel(){
		column = new String[] { "LOID", "city_name", "active_name", "serial","opendate", "last_time" };
		title = new String[] { "用户账号", "属  地", "用户是否活跃", "设备序列号", "开户时间", "最后上线时间" };
		fileName = "E8-C活跃终端管理率用户清单";
		data = bio.getCustomerExcel(startDealdate1, endDealdate1, cityId, isActive,type);
		return "excel";
	}
	
	// 当前年的1月1号
		/**
		 * 初始化时间
		 */
		public void initTime(){
			DateTimeUtil dt = new DateTimeUtil();
			endDealdate = dt.getDate();  // 获取当前时间
			
			dt = new DateTimeUtil(endDealdate);
			long end_time = dt.getLongTime();
			dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
			endDealdate = dt.getLongDate();
			
			startDealdate = dt.getFirtDayOfMonth();   //获取开始时间，为当月时间的第一天
			dt = new DateTimeUtil(startDealdate);
			long start_time = dt.getLongTime();
			dt = new DateTimeUtil((start_time) * 1000);
			startDealdate = dt.getLongDate();
		}
	
		// 当前时间的23:59:59,如 2011-05-11 23:59:59
		private String getEndDate() {
			GregorianCalendar now = new GregorianCalendar();
			SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);
			now.set(Calendar.HOUR_OF_DAY, 23);
			now.set(Calendar.MINUTE, 59);
			now.set(Calendar.SECOND, 59);
			String time = fmtrq.format(now.getTime());
			return time;
		}
		
		/**
		 * 时间转化
		 */
		private void setTime() {
			logger.debug("setTime()" + startDealdate);
			DateTimeUtil dt = null;// 定义DateTimeUtil
			if (startDealdate == null || "".equals(startDealdate)) {
				startDealdate1 = null;
			} else {
				dt = new DateTimeUtil(startDealdate);
				startDealdate1 = String.valueOf(dt.getLongTime());
			}
			if (endDealdate == null || "".equals(endDealdate)) {
				endDealdate1 = null;
			} else {
				dt = new DateTimeUtil(endDealdate);
				endDealdate1 = String.valueOf(dt.getLongTime());
			}
		}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request= request;
		
	}
	public String getStartDealdate() {
		return startDealdate;
	}
	public void setStartDealdate(String startDealdate) {
		this.startDealdate = startDealdate;
	}
	public String getStartDealdate1() {
		return startDealdate1;
	}
	public void setStartDealdate1(String startDealdate1) {
		this.startDealdate1 = startDealdate1;
	}
	public String getEndDealdate() {
		return endDealdate;
	}
	public void setEndDealdate(String endDealdate) {
		this.endDealdate = endDealdate;
	}
	public String getEndDealdate1() {
		return endDealdate1;
	}
	public void setEndDealdate1(String endDealdate1) {
		this.endDealdate1 = endDealdate1;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTtitle1() {
		return ttitle1;
	}
	public void setTtitle1(String ttitle1) {
		this.ttitle1 = ttitle1;
	}
	public String getTtitle2() {
		return ttitle2;
	}
	public void setTtitle2(String ttitle2) {
		this.ttitle2 = ttitle2;
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
	public List<Map> getList() {
		return list;
	}
	public void setList(List<Map> list) {
		this.list = list;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public List<Map<String, String>> getCityList() {
		return cityList;
	}
	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public E8CActiveReportBIO getBio() {
		return bio;
	}

	public void setBio(E8CActiveReportBIO bio) {
		this.bio = bio;
	}

	public String getTtitle3() {
		return ttitle3;
	}

	public void setTtitle3(String ttitle3) {
		this.ttitle3 = ttitle3;
	}

	
}