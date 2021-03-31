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

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.diagnostics.act.interf.I_AdvanceSearchACT;
import com.linkage.module.gwms.diagnostics.bio.XcompasswdBIO;
import com.linkage.module.gwms.obj.gw.XcompasswdOBJ;
import com.linkage.module.gwms.util.MathUtil;

/**
 * action:advance search.X_COM_PASSWD.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class XcompasswdACT implements ServletRequestAware, I_AdvanceSearchACT {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(XcompasswdACT.class);

	/** BIO */
	private XcompasswdBIO bio;

	/** type of strategy:do now */
	private int strategyType = 0;

	/** service_id */
	private final int serviceId = 104;

	/** request */
	private HttpServletRequest request;

	/** session */
	private HttpSession session;

	/** device_id */
	private String deviceId;

	/** random passwd */
	private String randompasswd = "";

	/** xcompasswd */
	private String xcompasswd = "";

	/** ajax */
	private String ajax = "";

	/** result */
	private String result = "";
	
	/** 终端类型  */
	private String gw_type = null;
	
	/**
	 * 设备序列号
	 */
	private String deviceSn = "";

	/**
	 * get data.
	 * 
	 * @return
	 */
	public String execute()
	{
		logger.debug("execute()");
		XcompasswdOBJ obj = bio.getData(deviceId);
		if (obj != null)
		{
			logger.debug("obj != null");
			this.xcompasswd = obj.getXcompasswd();
			this.deviceSn = obj.getDeviceSn();
			if (LipossGlobals.SystemType() == 2)
			{
				this.randompasswd = "sme" + MathUtil.getRandom(3);
			}
			else
			{
				String telecom = LipossGlobals.getLipossProperty("telecom");
				if("CUC".equals(telecom)){
					this.randompasswd = "cuadmin" + MathUtil.getRandom(3);
				}else{
					this.randompasswd = "telecomadmin" + MathUtil.getRandom(3);
				}
				
			}
		}
		else
		{
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

		XcompasswdOBJ obj = new XcompasswdOBJ();
		obj.setDeviceId(deviceId);
		obj.setXcompasswd(xcompasswd);

		if (true == bio.configDev(obj, accOId, this.strategyType,
				this.serviceId, gw_type)) {
			logger.debug("true");

			this.ajax = "true";
		} else {
			this.ajax = "false";
		}
		
		this.result = bio.getResult();

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
	public void setBio(XcompasswdBIO bio) {
		logger.debug("setBio(XcompasswdBIO)");

		this.bio = bio;
	}

	/**
	 * get:deviceId
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId() {
		logger.debug("getDeviceId");

		return deviceId;
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

	/**
	 * get:
	 * @return the xcompasswd
	 */
	public String getXcompasswd() {
		logger.debug("getXcompasswd()");

		return xcompasswd;
	}

	/**
	 * set:
	 * @param xcompasswd the xcompasswd to set
	 */
	public void setXcompasswd(String xcompasswd) {
		logger.debug("setXcompasswd({})", xcompasswd);

		this.xcompasswd = xcompasswd;
	}

	/**
	 * get:
	 * 
	 * @return the randompasswd
	 */
	public String getRandompasswd() {
		logger.debug("getRandompasswd()");

		return randompasswd;
	}

	/**
	 * set:
	 * 
	 * @param randompasswd
	 *            the randompasswd to set
	 */
	public void setRandompasswd(String randompasswd) {
		logger.debug("setRandompasswd({})", randompasswd);

		this.randompasswd = randompasswd;
	}

	/**
	 * get:
	 * @return the strategyType
	 */
	public int getStrategyType() {
		logger.debug("getStrategyType()");
		
		return strategyType;
	}

	/**
	 * set:
	 * @param strategyType the strategyType to set
	 */
	public void setStrategyType(int strategyType) {
		logger.debug("setStrategyType({})", strategyType);
		
		this.strategyType = strategyType;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	
	public String getDeviceSn()
	{
		return deviceSn;
	}

}
