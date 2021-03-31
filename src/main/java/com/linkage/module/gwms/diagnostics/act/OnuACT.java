package com.linkage.module.gwms.diagnostics.act;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.diagnostics.bio.OnuBIO;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.opensymphony.xwork2.ActionSupport;

public class OnuACT extends ActionSupport{

	private static final long serialVersionUID = 1L;

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(OnuACT.class);
	
	/** BIO */
	private OnuBIO bio;
	
	/** 终端类型 */
	private String gw_type = null;
	
	/** device_id */
	private String deviceId;
	
	/** result */
	private String result = "成功！";
	
	private String ajax = "";
	
	/** 时间服务器地址*/
	private String ntpserver1;
	
	private String ntpserver2;
	
	private String ntpserver3;
	
	private String ntpserver4;
	
	private String ntpserver5;
	
	/** 同步间隔时间*/
	private String ntpinterval;
	
	/** 时间同步方式*/
	private String ntpservertype;
		
	/**
	 * 获取upnp配置参数
	 * return String
	 */
	public String execute(){
		
		logger.debug("execute()");
		logger.warn("=======gw_type==="+gw_type+"==========");
		
		// 获取upnp配置参数
		ArrayList<ParameValueOBJ> onuList = bio.getOnu(deviceId, gw_type);
		if (onuList != null) {
			logger.debug("upnpList != null");
			for(ParameValueOBJ parameValueOBJ : onuList){
				if("InternetGatewayDevice.Time.NTPServer1".equals(parameValueOBJ.getName())){
					this.ntpserver1 = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.NTPServer2".equals(parameValueOBJ.getName())){
					this.ntpserver2 = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.NTPServer3".equals(parameValueOBJ.getName())){
					this.ntpserver3 = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.NTPServer4".equals(parameValueOBJ.getName())){
					this.ntpserver4 = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.NTPServer5".equals(parameValueOBJ.getName())){
					this.ntpserver5 = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.X_CT-COM_NTPInterval".equals(parameValueOBJ.getName())){
					this.ntpinterval = parameValueOBJ.getValue();
				}
				if("InternetGatewayDevice.Time.X_CT-COM_NTPServerType".equals(parameValueOBJ.getName())){
					this.ntpservertype = parameValueOBJ.getValue();
				}
			}
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
		
		List<String> onuParamList = new ArrayList<String>();
		onuParamList.add(this.ntpserver1);
		onuParamList.add(this.ntpserver2);
		onuParamList.add(this.ntpserver3);
		onuParamList.add(this.ntpserver4);
		onuParamList.add(this.ntpserver5);
		onuParamList.add(this.ntpinterval);
		onuParamList.add(this.ntpservertype);
		
		// 配置onu时间同步参数
		int saveResult = bio.setOnu(deviceId, gw_type, onuParamList);
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
	
	public OnuBIO getBio() {
		return bio;
	}

	public void setBio(OnuBIO bio) {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getNtpserver1() {
		return ntpserver1;
	}

	public void setNtpserver1(String ntpserver1) {
		this.ntpserver1 = ntpserver1;
	}

	public String getNtpserver2() {
		return ntpserver2;
	}

	public void setNtpserver2(String ntpserver2) {
		this.ntpserver2 = ntpserver2;
	}

	public String getNtpserver3() {
		return ntpserver3;
	}

	public void setNtpserver3(String ntpserver3) {
		this.ntpserver3 = ntpserver3;
	}

	public String getNtpserver4() {
		return ntpserver4;
	}

	public void setNtpserver4(String ntpserver4) {
		this.ntpserver4 = ntpserver4;
	}

	public String getNtpserver5() {
		return ntpserver5;
	}

	public void setNtpserver5(String ntpserver5) {
		this.ntpserver5 = ntpserver5;
	}

	public String getNtpinterval() {
		return ntpinterval;
	}

	public void setNtpinterval(String ntpinterval) {
		this.ntpinterval = ntpinterval;
	}

	public String getNtpservertype() {
		return ntpservertype;
	}

	public void setNtpservertype(String ntpservertype) {
		this.ntpservertype = ntpservertype;
	}		
}
