package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.config.serv.WifiPwdManageServ;

public class WifiPwdManageAction  extends splitPageAction implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1191968051621735455L;
	private static Logger logger = LoggerFactory
			.getLogger(WifiPwdManageAction.class);
	// Session
	private Map<String, Object> session;
	/** 帐号 **/
	private String username;
	/** LOID **/
	private String device_serialnumber;
	private WifiPwdManageServ bio;
	private String ajax = "";
	private String resmessage = "";
	private String wifipath = "";
	private String wifipwd = "";
	private String wifiname = "";
	private String deviceid = "";
	/** 业务信息列表 */
	private List<Map<String, String>> servList;
	/**
	 * 根据宽带帐号查询用户是否存在 该帐号为业务路宽带
	 * 
	 * @return
	 */
	public String getServUserInfo() {
		logger.warn("getServUserInfo({})", username);
		ajax = bio.getServUserInfo(username);
		return "ajax";
	}
	
	public String queryAllInfo(){
		List<Map<String,String>> res = new ArrayList<Map<String,String>>();
		resmessage = bio.getAllInfo(device_serialnumber,res);
		servList = res;
		return "list";
	}
	public String updateWifiPwd(){
		ajax = bio.updateWifiPwd(wifipath,wifipwd,deviceid);
		return "ajax";
	}
	public String getwifiInfoUpdate(){
		
		return "wifiupdateInfo";
	}
	
	
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public WifiPwdManageServ getBio() {
		return bio;
	}

	public void setBio(WifiPwdManageServ bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getResmessage() {
		return resmessage;
	}

	public void setResmessage(String resmessage) {
		this.resmessage = resmessage;
	}

	public String getWifipath() {
		return wifipath;
	}

	public void setWifipath(String wifipath) {
		this.wifipath = wifipath;
	}

	public String getWifipwd() {
		return wifipwd;
	}

	public void setWifipwd(String wifipwd) {
		this.wifipwd = wifipwd;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public List<Map<String, String>> getServList() {
		return servList;
	}

	public void setServList(List<Map<String, String>> servList) {
		this.servList = servList;
	}

	public String getWifiname() {
		return wifiname;
	}

	public void setWifiname(String wifiname) {
		this.wifiname = wifiname;
	}

}
