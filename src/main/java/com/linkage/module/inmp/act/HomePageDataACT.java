/**
 * 
 */
package com.linkage.module.inmp.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.inmp.bio.HomePageDataBIO;

/**
 * @author liyl(67371)
 * @date 2014-9-25
 * 
 *       获取智能网管首页展示数据
 */
public class HomePageDataACT implements SessionAware{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(HomePageDataACT.class);

	private HomePageDataBIO bio;
	
	// Session
	private Map session;

	private String ajax;
	
	// 属地
	private String cityId = "00";
	
	private long userId;
	
	//获取年份
	private String year;
	//获取月份
	private String month;
	
	/** 饼图报表类型 1:终端，2：用户，3：版本，4：管控终端*/
	private String report_type; 
	
	private String sort;
	
	public String init(){
		logger.warn("HomePageDataACT ===> init");
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		logger.warn("cityId=" + cityId);
		return "init";
	}

	public String getPieData() {
		logger.warn("HomePageDataACT ===> getPieData() ==>终端");
		ajax = bio.getPieData(report_type);
		
		logger.warn("getPieData(" + report_type + ")--->" + ajax);

		return "ajax";
	}
	
	public String getAutoBindGaugeData() {//自动绑定仪表盘
		logger.warn("HomePageDataACT ===> getAutoBindGaugeData({},{}) ==>自动绑定仪表盘",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getAutoBindGaugeData(cityId,year,month);
		
		logger.warn("getAutoBindBarData()--->" + ajax);
		
		return "ajax";
	}

	public String getAutoBindBarData() {//自动绑定柱状图
		logger.warn("HomePageDataACT ===> getAutoBindBarData({},{}) ==>自动绑定",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getAutoBindBarData(cityId,year,month,sort);
		
		logger.warn("getAutoBindBarData()--->" + ajax);
		
		return "ajax";
	}
	
	
	public String getBussGaugeData() {//业务一次下发仪表盘
		logger.warn("HomePageDataACT ===> getBussGaugeData({},{}) ==>业务一次下发",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getBussGaugeData(cityId,year,month);

		logger.warn("getBussGaugeData()--->" + ajax);
		
		return "ajax";
	}

	public String getBussBarData() {//业务下发柱状图
		logger.warn("HomePageDataACT ===> getBussBarData({},{}) ==>业务一次下发",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getBussBarData(cityId,year,month,sort);

		logger.warn("getBussBarData()--->" + ajax);
		
		return "ajax";
	}
	
	public String getPvcGaugeData() {//pvc仪表盘
		logger.warn("HomePageDataACT ===> getPvcGaugeData({},{}) ==>多pvc部署",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getPvcGaugeData(cityId,year,month);
		
		logger.warn("getPvcGaugeData()--->" + ajax);
		
		return "ajax";
	}
	
	
	public String getPvcBarData() {//pvc柱状图
		logger.warn("HomePageDataACT ===> getPvcBarData({},{}) ==>多pvc部署",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getPvcBarData(cityId,year,month,sort);
		
		logger.warn("getPvcBarData()--->" + ajax);
		
		return "ajax";
	}
	
	
	public String getVersionDevGaugeData() {//终端规范率仪表盘
		logger.warn("HomePageDataACT ===> getVersionDevBarData({},{}) ==>规范版本终端",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getVersionDevGaugeData(cityId,year,month);
		
		logger.warn("getVersionDevBarData()--->" + ajax);
		
		return "ajax";
	}

	public String getVersionDevBarData() {//终端规范率柱状图
		logger.warn("HomePageDataACT ===> getVersionDevBarData({},{}) ==>规范版本终端",new Object[]{year,month});
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		userId = curUser.getUser().getId();
		ajax = bio.getVersionDevBarData(cityId,year,month,sort);
		
		logger.warn("getVersionDevBarData()--->" + ajax);
		
		return "ajax";
	}
	
	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public HomePageDataBIO getBio() {
		return bio;
	}

	public void setBio(HomePageDataBIO bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	

}
