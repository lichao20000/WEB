package com.linkage.module.gtms.blocTest.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.blocTest.serv.ApDeviceBusinessConfigServ;


public class ApDeviceBusinessConfigActionImp implements ApDeviceBusinessConfigAction {
	
	private static Logger logger = LoggerFactory
			.getLogger(ApDeviceBusinessConfigActionImp.class);
	
	
	private String deviceId = null;
	
	private String servTypeId = null;
	
	private String gw_type = null;
	
	private String ssid = null;
	
	private String templatePara = null;
	
	private String ajax = null;
	
	private ApDeviceBusinessConfigServ bio;

	
	
	
	
	
	
	public String init(){
		return "init";
	}
	
	
	
	public String doBusiness(){
		
		logger.debug("ApDeviceBusinessConfigActionImp==>doBusiness()");
		
		ajax = bio.doBusiness(deviceId, servTypeId, gw_type, ssid, templatePara);
		
		return "ajax";
	}
	
	
	
	
	public String getDeviceId() {
		return deviceId;
	}

	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	public String getServTypeId() {
		return servTypeId;
	}

	
	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	
	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	
	public String getTemplatePara() {
		return templatePara;
	}

	
	public void setTemplatePara(String templatePara) {
		try {
			this.templatePara = java.net.URLDecoder.decode(templatePara, "UTF-8");
		} catch (Exception e) {
			this.templatePara = templatePara;
		}
	}

	
	public String getAjax() {
		return ajax;
	}

	
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	
	public ApDeviceBusinessConfigServ getBio() {
		return bio;
	}

	
	public void setBio(ApDeviceBusinessConfigServ bio) {
		this.bio = bio;
	}
	
}
