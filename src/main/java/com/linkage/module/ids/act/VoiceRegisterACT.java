package com.linkage.module.ids.act;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.ids.bio.VoiceRegisterBIO;
import com.opensymphony.xwork2.ActionSupport;

public class VoiceRegisterACT extends ActionSupport implements
		ServletRequestAware {
	private static final long serialVersionUID = 8277510997795174346L;
	private static Logger logger = LoggerFactory
			.getLogger(VoiceRegisterACT.class);
	HttpServletRequest request = null;

	private VoiceRegisterBIO bio = null;
	
	//0代表成功，不是0代表有错误
	private  String code;
	
	private String servicesip;

	public String queryVoiceRegister() {
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		map = bio.queryVoiceRegister(oui, deviceno, city_id, servicesip);
		code = map.get("RstCode");
		return "success";
	}

	public VoiceRegisterBIO getBio() {
		return bio;
	}

	public void setBio(VoiceRegisterBIO bio) {
		this.bio = bio;
	}

	private String ajax;

	private String oui;

	private String deviceno;

	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	private Map<String, String> map = null;

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getServicesip() {
		return servicesip;
	}

	public void setServicesip(String servicesip) {
		this.servicesip = servicesip;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
