package com.linkage.module.gwms.diagnostics.act;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.module.gwms.diagnostics.bio.UpnpBIO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.opensymphony.xwork2.ActionSupport;

public class UpnpACT extends ActionSupport{

	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(UpnpACT.class);
	
	/** BIO */
	private UpnpBIO bio;
	
	/** 终端类型 */
	private String gw_type = null;
	
	/** device_id */
	private String deviceId;
	
	/** parmVaule */
	private String upnpParamValue;	
	
	/** result */
	private String result = "成功！";
	
	private String ajax = "";
	
	
	/**
	 * 获取upnp配置参数
	 * return String
	 */
	public String execute(){
		
		logger.debug("execute()");
		logger.warn("=======gw_type==="+gw_type+"==========");
		
		// 获取upnp配置参数
		ArrayList<ParameValueOBJ> upnpList = bio.getUpnp(deviceId, gw_type);
		if (upnpList != null) {
			logger.debug("upnpList != null");
			this.upnpParamValue = upnpList.get(0).getValue();
		} else {
			this.result = "失败！";
			logger.warn("get data null");
		}
		return "success";
	}

	/**
	 * 配置upnp
	 * @return String
	 */
	public String config() {
		logger.debug("config()");
		
		// 配置upnp参数
		int saveResult = bio.setUpnp(deviceId, gw_type, this.upnpParamValue);
		
		switch (saveResult) {
		case 0 | 1:
			this.ajax = "成功";
			break;
		case -1:
			this.ajax = "设备连接失败";
			break;
		case -6:
			this.ajax = "设备正被操作";
			break;
		case -7:
			this.ajax = "系统参数错误";
			break;
		case -9:
			this.ajax = "系统内部错误";
			break;
		default:
			this.ajax = "其它:TR069错误";
			break;
		}		
		return "ajax";
	}
	
	public UpnpBIO getBio() {
		return bio;
	}

	public void setBio(UpnpBIO bio) {
		this.bio = bio;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUpnpParamValue() {
		return upnpParamValue;
	}

	public void setUpnpParamValue(String upnpParamValue) {
		this.upnpParamValue = upnpParamValue;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}	
}
