package com.linkage.module.gtms.resource.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.resource.serv.ConfigVoiceProcessGainNodeServ;


public class ConfigVoiceProcessGainNodeActionImp implements
		ConfigVoiceProcessGainNodeAction {
	
	
	private static Logger logger = LoggerFactory
			.getLogger(ConfigVoiceProcessGainNodeActionImp.class);
	
	
	private String ajax = null;
	
	private String gw_type = null;
	
	private String deviceId = null;
	
	/** 节点一：InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.Line.  */
	private String node_1 = null;
	
	/** 节点二：VoiceProcessing.TransmitGain */
	private String node_2 = null;
	
	/** 节点三：VoiceProcessing.ReceiveGain */
	private String node_3 = null;
	
	/** 前台页面传来的增益节点及需要下发的增益节点的值 */
	private String gainValue = null;
	
//	private String config = null;
	
	
	private ConfigVoiceProcessGainNodeServ bio = null;
	
	
	/**
	 * 初始化界面
	 */
	public String init(){
		
		logger.debug("ConfigVoiceProcessGainNodeActionImp==>init()");
		
		return "init";
	}
	
	
	
	// 获取增益节点
	public String getGainNode(){
		
		logger.debug("ConfigVoiceProcessGainNodeActionImp==>getGainNode()");
		
		ajax = bio.getGainNode(deviceId, gw_type, node_1, node_2, node_3);
		
		return "ajax";
	}
	
	
	// 下发增益节点值
	public String doConfigGainNode(){
		
		logger.debug("ConfigVoiceProcessGainNodeActionImp==>getGainNode()");
		
		ajax = bio.doConfigGainNode(deviceId, gw_type, gainValue);
		
		return "ajax";
	}

	
	public String getDeviceId() {
		return deviceId;
	}

	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	
	public String getAjax() {
		return ajax;
	}


	
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}



	
	public ConfigVoiceProcessGainNodeServ getBio() {
		return bio;
	}



	
	public void setBio(ConfigVoiceProcessGainNodeServ bio) {
		this.bio = bio;
	}



	
	public String getGw_type() {
		return gw_type;
	}



	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}



	
	public String getNode_1() {
		return node_1;
	}



	
	public void setNode_1(String node_1) {
		this.node_1 = node_1;
	}



	
	public String getNode_2() {
		return node_2;
	}



	
	public void setNode_2(String node_2) {
		this.node_2 = node_2;
	}



	
	public String getNode_3() {
		return node_3;
	}



	
	public void setNode_3(String node_3) {
		this.node_3 = node_3;
	}



	
	public String getGainValue() {
		return gainValue;
	}



	
	public void setGainValue(String gainValue) {
		this.gainValue = gainValue;
	}
	
}
