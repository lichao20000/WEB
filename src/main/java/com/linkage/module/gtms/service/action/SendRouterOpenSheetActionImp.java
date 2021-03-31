package com.linkage.module.gtms.service.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.service.serv.SendRouterOpenSheetServ;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class SendRouterOpenSheetActionImp implements SendRouterOpenSheetAction,
		SessionAware {
	
	private static Logger logger = LoggerFactory
			.getLogger(SendRouterOpenSheetActionImp.class);
	
	// 业务类型
	private String servTypeId = null;
	
	// 操作类型
	private String operateType = null;
	
	// 属地
	private String cityId = null;
	
	// 宽带帐号
	private String netUsername = null;
	
	// 宽带密码
	private String netPassword = null;
	
	// Session
	private Map session;
	
	// 属地列表
	private List<Map<String, String>> cityList = null;
	
	private String gw_type = null;
	
	private String ajax = null;

	private SendRouterOpenSheetServ bio ;
	
	
	
	public String init(){
		
		logger.debug("SendRouterOpenSheetActionImp==>init()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		
		return "init";
	}
	
	
	
	
	public String sendSheet(){
		
		logger.debug("SendRouterOpenSheetActionImp==>sendSheet()");
		
		if (null == gw_type || "".equals(gw_type)) {
			gw_type = "1";
		}
		
		ajax = bio.sendSheet(servTypeId, operateType, cityId, netUsername, netPassword, gw_type);
		
		return "ajax";
	}
	
	
	public String getServTypeId() {
		return servTypeId;
	}

	
	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	
	public String getOperateType() {
		return operateType;
	}

	
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	
	public String getCityId() {
		return cityId;
	}

	
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	
	public String getNetUsername() {
		return netUsername;
	}

	
	public void setNetUsername(String netUsername) {
		this.netUsername = netUsername;
	}

	
	public String getNetPassword() {
		return netPassword;
	}

	
	public void setNetPassword(String netPassword) {
		this.netPassword = netPassword;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	
	public SendRouterOpenSheetServ getBio() {
		return bio;
	}
	
	public void setBio(SendRouterOpenSheetServ bio) {
		this.bio = bio;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
}
