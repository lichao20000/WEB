package com.linkage.module.itms.resource.act;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;
import com.linkage.module.itms.resource.bio.GwDeviceQueryBIO;

public class GwDeviceQueryACT extends splitPageAction implements ServletRequestAware {
	
	/**
	 *  serial
	 */
	private static final long serialVersionUID = 1L;

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(GwDeviceQueryACT.class);

	GwDeviceQueryBIO gwDeviceBio = null;
	
	private String ajax = null;

	private String gwShare_vendorId = null;

	HttpServletRequest request = null;
	

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	public String getVendor() {
		logger.debug("GwDeviceQueryACT=>getVendor()");
		this.ajax = gwDeviceBio.getVendor();
		return "ajax";
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId
	 * @return
	 */
	public String getDeviceModel() {
		logger.debug("GwDeviceQueryACT=>getDeviceModel()");
		this.ajax = gwDeviceBio.getDeviceModel(gwShare_vendorId);
		return "ajax";
	}


	public String getGwShare_vendorId() {
		return gwShare_vendorId;
	}

	public void setGwShare_vendorId(String gwShare_vendorId) {
		this.gwShare_vendorId = gwShare_vendorId;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public GwDeviceQueryBIO getGwDeviceBio() {
		return gwDeviceBio;
	}

	public void setGwDeviceBio(GwDeviceQueryBIO gwDeviceBio) {
		this.gwDeviceBio = gwDeviceBio;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}
	
}
