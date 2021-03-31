package com.linkage.module.gtms.blocTest.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.blocTest.serv.ApDeviceSoftUpGradeServ;


public class ApDeviceSoftUpGradeActionImp implements ApDeviceSoftUpGradeAction {
	
	private static Logger logger = LoggerFactory
			.getLogger(ApDeviceSoftUpGradeActionImp.class);
	
	/** 终端类型  1表示ITMS   2表示BBMS */
	private String gw_type = null;
	
	/** 设备ID */
	private String deviceId = null;
	
	/** 模板参数 */
	private String templatePara = null;
	
	/** 版本文件路径 */
	private String filePath = null;
	
	/** ajax */
	private String ajax = null;
	
	
	private ApDeviceSoftUpGradeServ bio;

	
	
	
	
	
	
	
	public String init(){
		return "init";
	}
	
	
	/**
	 * 应用终端软件升级
	 */
	public String doConfig(){
		logger.debug("ApDeviceSoftUpGradeActionImp==>doConfig()");
		
		ajax = bio.doConfig(deviceId, templatePara, gw_type, filePath);
		 
		 return "ajax";
	}
	
	/**
	 * 目标版本文件下拉框
	 */
	public String getSelectListBox(){
		logger.debug("ApDeviceSoftUpGradeActionImp==>getSelectListBox()");
		ajax = bio.getSelectListBox();
		return "ajax";
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

	
	public ApDeviceSoftUpGradeServ getBio() {
		return bio;
	}

	
	public void setBio(ApDeviceSoftUpGradeServ bio) {
		this.bio = bio;
	}

	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
