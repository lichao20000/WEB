
package com.linkage.module.bbms.config.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.TR069ConfigBIO;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.opensymphony.xwork2.ActionSupport;

public class TR069ConfigACT extends ActionSupport implements SessionAware
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(TR069ConfigACT.class);
	// session
	private Map session;
	private TR069ConfigBIO tr069ConfigBio;
	private String ajax;
	private String deviceId;
	private String acsurl;
	private String userName;
	private String password;
	private String cpeUsername;
	private String cpePasswd;
	private String periodicInformEnable;
	private String periodicInformInterval;
	private String manageFlag;

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @author wangsenbo
	 * @date Jan 27, 2010
	 * @param
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute({})");
		return "success";
	}

	/**
	 * @author wangsenbo
	 * @date Jan 27, 2010
	 * @param
	 * @return String
	 */
	public String getTR069()
	{
		logger.debug("getTR069({})");
		ajax = tr069ConfigBio.getTR069(deviceId);
		return "ajax";
	}

	public String TR069ConfigSave()
	{
		logger.debug("TR069ConfigSave({})");
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		GwTr069OBJ obj = new GwTr069OBJ();
		obj.setDeviceId(deviceId);
		obj.setUrl(acsurl);
		obj.setAcsUsername(userName);
		obj.setAcsPasswd(password);
		obj.setCpeUsername(cpeUsername);
		obj.setCpePasswd(cpePasswd);
		obj.setPeriInformEnable(periodicInformEnable);
		obj.setPeriInformInterval(periodicInformInterval);
		ajax = tr069ConfigBio.TR069ConfigSave(accoid, obj, manageFlag);
		return "ajax";
	}

	/**
	 * @return the tr069ConfigBio
	 */
	public TR069ConfigBIO getTr069ConfigBio()
	{
		return tr069ConfigBio;
	}

	/**
	 * @param tr069ConfigBio
	 *            the tr069ConfigBio to set
	 */
	public void setTr069ConfigBio(TR069ConfigBIO tr069ConfigBio)
	{
		this.tr069ConfigBio = tr069ConfigBio;
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
	 * @return the acsurl
	 */
	public String getAcsurl()
	{
		return acsurl;
	}

	/**
	 * @param acsurl
	 *            the acsurl to set
	 */
	public void setAcsurl(String acsurl)
	{
		this.acsurl = acsurl;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
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
	 * @return the cpeUsername
	 */
	public String getCpeUsername()
	{
		return cpeUsername;
	}

	/**
	 * @param cpeUsername
	 *            the cpeUsername to set
	 */
	public void setCpeUsername(String cpeUsername)
	{
		this.cpeUsername = cpeUsername;
	}

	/**
	 * @return the cpePasswd
	 */
	public String getCpePasswd()
	{
		return cpePasswd;
	}

	/**
	 * @param cpePasswd
	 *            the cpePasswd to set
	 */
	public void setCpePasswd(String cpePasswd)
	{
		this.cpePasswd = cpePasswd;
	}

	/**
	 * @return the periodicInformEnable
	 */
	public String getPeriodicInformEnable()
	{
		return periodicInformEnable;
	}

	/**
	 * @param periodicInformEnable
	 *            the periodicInformEnable to set
	 */
	public void setPeriodicInformEnable(String periodicInformEnable)
	{
		this.periodicInformEnable = periodicInformEnable;
	}

	/**
	 * @return the periodicInformInterval
	 */
	public String getPeriodicInformInterval()
	{
		return periodicInformInterval;
	}

	/**
	 * @param periodicInformInterval
	 *            the periodicInformInterval to set
	 */
	public void setPeriodicInformInterval(String periodicInformInterval)
	{
		this.periodicInformInterval = periodicInformInterval;
	}

	/**
	 * @return the manageFlag
	 */
	public String getManageFlag()
	{
		return manageFlag;
	}

	/**
	 * @param manageFlag
	 *            the manageFlag to set
	 */
	public void setManageFlag(String manageFlag)
	{
		this.manageFlag = manageFlag;
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}
}
