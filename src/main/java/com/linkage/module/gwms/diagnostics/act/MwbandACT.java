/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.act;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.diagnostics.act.interf.I_AdvanceSearchACT;
import com.linkage.module.gwms.diagnostics.bio.MwbandBIO;
import com.linkage.module.gwms.obj.gw.MwbandOBJ;

/**
 * action:advance search.MWBAN.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class MwbandACT implements ServletRequestAware, I_AdvanceSearchACT {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(MwbandACT.class);

	/** BIO */
	private MwbandBIO bio;

	/** type of strategy:do now */
	private final int strategyType = 0;

	/** service_id */
	private final int serviceId = 101;

	/** request */
	private HttpServletRequest request;

	/** session */
	private HttpSession session;

	/** device_id */
	private String deviceId;

	/** mode */
	private int mode = 0;

	/** total_number */
	private int totalNumber = 0;

	/** ajax */
	private String ajax = "";

	/** result */
	private String result = "";
	
	/** 终端类型 */
	private String gw_type = null;

	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute() {
		logger.debug("execute()");

		logger.warn("=======gw_type==="+gw_type+"==========");
		
		MwbandOBJ obj = bio.getData(deviceId);
		if (obj != null) {
			logger.debug("obj != null");
			this.mode = obj.getMode();
			this.totalNumber = obj.getTotalNumber();
		} else {
			logger.warn("get data null");
		}
		this.result = bio.getResult();

		return "success";
	}

	/**
	 * config dev.
	 * 
	 * @return
	 */
	public String config() {
		logger.debug("config()");

		session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long accOId = curUser.getUser().getId();

		MwbandOBJ obj = new MwbandOBJ();
		obj.setDeviceId(deviceId);
		obj.setMode(mode);
		obj.setTotalNumber(totalNumber);

		ajax = bio.configDev(obj, accOId, strategyType,serviceId, gw_type);
		this.result = bio.getResult();

		ajax += "|" + result;
		
		return "ajax";
	}

	/*
	 * set request
	 * 
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		logger.debug("setServletRequest(HttpServletRequest)");

		this.request = arg0;
	}

	/**
	 * set: BIO
	 * 
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(MwbandBIO bio) {
		logger.debug("setBio(MwbandBIO)");

		this.bio = bio;
	}

	/**
	 * get:deviceId
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId() {
		logger.debug("getDeviceId()");

		return deviceId;
	}

	/**
	 * get:mode
	 * 
	 * @return the mode
	 */
	public int getMode() {
		logger.debug("getDeviceId()");

		return mode;
	}

	/**
	 * get:totalNumber
	 * 
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		logger.debug("getDeviceId()");

		return totalNumber;
	}

	/**
	 * set:deviceId
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		logger.debug("setDeviceId({})", deviceId);

		this.deviceId = deviceId;
	}

	/**
	 * set:mode
	 * 
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode) {
		logger.debug("setMode({})", mode);

		this.mode = mode;
	}

	/**
	 * set: totalNumber
	 * 
	 * @param totalNumber
	 *            the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		logger.debug("setTotalNumber({})", totalNumber);

		this.totalNumber = totalNumber;
	}

	/**
	 * get:
	 * 
	 * @return the ajax
	 */
	public String getAjax() {
		logger.debug("getAjax()");

		return ajax;
	}

	/**
	 * get:
	 * 
	 * @return the result
	 */
	public String getResult() {
		logger.debug("getResult()");

		return result;
	}

	/**
	 * set:
	 * 
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax) {
		logger.debug("setAjax({})", ajax);

		this.ajax = ajax;
	}

	/**
	 * set:
	 * 
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		logger.debug("setResult({})", result);

		this.result = result;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
