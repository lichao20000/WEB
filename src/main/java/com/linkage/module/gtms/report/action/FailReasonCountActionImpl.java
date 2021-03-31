package com.linkage.module.gtms.report.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.report.serv.FailReasonCountServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class FailReasonCountActionImpl implements SessionAware, FailReasonCountAction {
	private static Logger logger = LoggerFactory.getLogger(FailReasonCountActionImpl.class);
	
	/** session */
	private Map session = null;
	
	/**  用户属地 */
	private String cityId = null;
	
	/** 开始时间 */
	private String starttime = null;
	
	/** 结束时间 */
	private String endtime = null;
	/**月份*/
	private String monthDate = null;
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	
	/** 导出数据 */
	private List<Map<String,String>> data = null;
	
	/**
	 * 查询结果的标题
	 */
	private List<String> titleList = null;
	
	/**
	 * 返回类型
	 */
	private String isReport = null;

	private FailReasonCountServ bio ;
	
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("FailReasonCountActionImpl==>init()");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		DateTimeUtil dt = new DateTimeUtil();
		monthDate = dt.getYear()+"-"+ dt.getMonth();
		return "init";
	}
	
	
	/**
	 * 统计结果页面
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String countAll() {
		
		logger.debug("FailReasonCountActionImpl==>countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		logger.warn("错误原因统计   操作人ID："+curUser.getUser().getId()+
				    "   属地："+cityMap.get(cityId)+"==isReport="+isReport);
		this.setTime();
		cityList = CityDAO.getNextCityListByCityPid(cityId);
		this.titleList = new ArrayList<String>();
		this.titleList.add("原因\\属地");
		for(int i=0;i<cityList.size();i++){
			this.titleList.add(String.valueOf(((Map)cityList.get(i)).get("city_name")));
		}
		data = bio.failReasonCount(cityId, starttime,endtime);
		if("excel".equals(isReport)){
			return "excel";
		}
		return "showList";
	}
	
	

	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("FailReasonCountActionImpl==>setTime()" + starttime);
		//该月第一天
		String[] monthTemp = monthDate.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(monthTemp[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(monthTemp[1]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		//该月最后一天
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, Integer.parseInt(monthTemp[0]));
		ca.set(Calendar.MONTH, Integer.parseInt(monthTemp[1]));
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.DATE, 0);
		if (monthDate.isEmpty()){
			starttime = null;
			endtime = null;
		}else{
			starttime = String.valueOf(new DateTimeUtil(cal.getTime()).getLongTime());
			endtime   = String.valueOf(new DateTimeUtil(ca.getTime()).getLongTime());
		}
	}
	public Map getSession() {
		return session;
	}
	
	public void setSession(Map session) {
		this.session = session;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public List<Map<String, String>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	
	public List<Map<String, String>> getData() {
		return data;
	}


	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}


	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMonthDate() {
		return monthDate;
	}
	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}


	public String getStarttime() {
		return starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public List<String> getTitleList() {
		return titleList;
	}


	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}


	public String getIsReport() {
		return isReport;
	}


	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}


	public FailReasonCountServ getBio() {
		return bio;
	}


	public void setBio(FailReasonCountServ bio) {
		this.bio = bio;
	}
	
}
