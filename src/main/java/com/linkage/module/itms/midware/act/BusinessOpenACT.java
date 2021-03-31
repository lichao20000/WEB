
package com.linkage.module.itms.midware.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.midware.bio.BusinessOpenBIO;

/**
 * 中间件
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BusinessOpenACT extends splitPageAction implements SessionAware
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BusinessOpenACT.class);
	// session
	private Map session;
	private String cityId = null;
	private String deviceModel = null;
	private String oui;
	private String deviceId;
	private String deviceSn;
	private String account;
	private String username;
	private String password;
	private String b_type;
	private String op_type;
	private String ajax;
	private BusinessOpenBIO businessOpenBio;

	/**
	 * @author wangsenbo
	 * @date Apr 8, 2010
	 * @param
	 * @return String
	 */
	public String save()
	{
		logger.debug("save()");
		ajax = businessOpenBio.getMidMsg(op_type, deviceId, oui, deviceSn, b_type, account, password, username,deviceModel,cityId);
		return "ajax";
	}

	public String execute()
	{
		logger.debug("execute()");
		return "open";
	}

	/**
	 * 开启中间件终端模块
	 * 
	 * @param String
	 * 
	 * @return String 
	 */
	public String midMareDevOpen(){
		logger.debug("midMareDevOpen()");
		ajax = businessOpenBio.midMareDevOpen(deviceId);
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
	 * @return the oui
	 */
	public String getOui()
	{
		return oui;
	}

	/**
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	/**
	 * @return the deviceSn
	 */
	public String getDeviceSn()
	{
		return deviceSn;
	}

	/**
	 * @param deviceSn
	 *            the deviceSn to set
	 */
	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
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
	 * @return the account
	 */
	public String getAccount()
	{
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account)
	{
		try
		{
			this.account = java.net.URLDecoder.decode(account, "UTF-8");
		}
		catch (Exception e)
		{
			this.account = account;
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the b_type
	 */
	public String getB_type()
	{
		return b_type;
	}

	/**
	 * @param b_type
	 *            the b_type to set
	 */
	public void setB_type(String b_type)
	{
		try
		{
			this.b_type = java.net.URLDecoder.decode(b_type, "UTF-8");
		}
		catch (Exception e)
		{
			this.b_type = b_type;
		}
	}

	/**
	 * @return the op_type
	 */
	public String getOp_type()
	{
		return op_type;
	}

	/**
	 * @param op_type
	 *            the op_type to set
	 */
	public void setOp_type(String op_type)
	{
		try
		{
			this.op_type = java.net.URLDecoder.decode(op_type, "UTF-8");
		}
		catch (Exception e)
		{
			this.op_type = op_type;
		}
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

	/**
	 * @return the businessOpenBio
	 */
	public BusinessOpenBIO getBusinessOpenBio()
	{
		return businessOpenBio;
	}

	/**
	 * @param businessOpenBio
	 *            the businessOpenBio to set
	 */
	public void setBusinessOpenBio(BusinessOpenBIO businessOpenBio)
	{
		this.businessOpenBio = businessOpenBio;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		try
		{
			this.username = java.net.URLDecoder.decode(username, "UTF-8");
		}
		catch (Exception e)
		{
			this.username = username;
		}
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	
}
