
package com.linkage.module.gwms.diagnostics.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.DevIADDiagCAO;
import com.opensymphony.xwork2.ActionSupport;

public class IADDiagACT extends ActionSupport implements SessionAware
{

	private static Logger logger = LoggerFactory.getLogger(IADDiagACT.class);
	// session
	private Map session;
	private String deviceId;
	// 注册服务器，取值范围：1：主用服务器，2：备用服务器
	private String iserv;
	private String ajax;
	
	private String gw_type = null;

	public String init()
	{
		logger.debug("IADinit");
		return "success";
	}

	/**
	 * IAD诊断
	 * 
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param
	 * @return String
	 */
	public String iadDiag()
	{
		logger.debug("iadDiag()");
		if (!"1".equals(iserv) && !"2".equals(iserv))
		{
			logger.error("IAD诊断注册服务器错误！");
		}
		DevIADDiagCAO iadCao = new DevIADDiagCAO(deviceId, StringUtil
				.getIntegerValue(iserv), gw_type);
		if (iadCao.diagIAD())
		{
			ajax = iadCao.getResult() + "," + iadCao.getReasonDesc();
		}
		else
		{
			// 错误代码
			int faultCode = iadCao.getDiagResult();
			ajax = "-1," + Global.G_Fault_Map.get(faultCode).getFaultReason();
		}
		return "ajax";
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the iserv
	 */
	public String getIserv()
	{
		return iserv;
	}

	/**
	 * @param iserv
	 *            the iserv to set
	 */
	public void setIserv(String iserv)
	{
		this.iserv = iserv;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
