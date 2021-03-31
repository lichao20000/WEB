package com.linkage.module.ids.act;

import java.util.Map;

import com.linkage.module.ids.bio.VoiceQualityMonitoringBIO;
import com.opensymphony.xwork2.ActionSupport;

public class VoiceQualityMonitoringACT extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String city_id;
	private String device_id;
	private String wanType;
	private String oui;
	private String device_sn;
	VoiceQualityMonitoringBIO bio = null;
	private String code;
	private Map<String, String> voiceMonitoringMap;
	
	@Override
	public String execute() throws Exception
	{
		// TODO Auto-generated method stub
		return "init";
	}
	
	public String queryVoiceMonitoring(){
		voiceMonitoringMap = bio.queryVoiceMonitoring(oui,device_sn,city_id);
		code = voiceMonitoringMap.get("code");
		return "list";
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDevice_sn() {
		return device_sn;
	}

	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}

	public VoiceQualityMonitoringBIO getBio() {
		return bio;
	}

	public void setBio(VoiceQualityMonitoringBIO bio) {
		this.bio = bio;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getVoiceMonitoringMap() {
		return voiceMonitoringMap;
	}

	public void setVoiceMonitoringMap(Map<String, String> voiceMonitoringMap) {
		this.voiceMonitoringMap = voiceMonitoringMap;
	}	
}
