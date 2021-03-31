
package com.linkage.module.bbms.config.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.config.bio.FTPConfigBIO;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class FTPConfigACT extends ActionSupport implements SessionAware
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(FTPConfigACT.class);
	// session
	private Map session;
	private String ajax;
	private FTPConfigBIO ftpConfigBio;
	private String deviceId;
	private String ftpenable;
	private String ftpUserName;
	private String ftpPassword;
	private String ftpPort;

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
	 * @author wangsenbo
	 * @date Jan 25, 2010
	 * @return String
	 */
	public String execute()
	{
		return "success";
	}

	/**
	 * 获取FTP服务信息
	 * 
	 * @author wangsenbo
	 * @date Jan 25, 2010
	 * @param
	 * @return String
	 */
	public String getFTP()
	{
		logger.debug("getFTP({})");
		ajax = ftpConfigBio.getFTP(deviceId);
		return "ajax";
	}

	/**
	 * 配置FTP服务
	 * 
	 * @author wangsenbo
	 * @date Jan 25, 2010
	 * @return String
	 */
	public String ftpConfigSave()
	{
		logger.debug("ftpConfigSave({})");
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		ajax = ftpConfigBio.ftpConfig(accoid, deviceId, ftpenable, ftpUserName,
				ftpPassword, ftpPort);
		return "ajax";
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
	 * @return the ftpConfigBio
	 */
	public FTPConfigBIO getFtpConfigBio()
	{
		return ftpConfigBio;
	}

	/**
	 * @param ftpConfigBio
	 *            the ftpConfigBio to set
	 */
	public void setFtpConfigBio(FTPConfigBIO ftpConfigBio)
	{
		this.ftpConfigBio = ftpConfigBio;
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
	 * @return the ftpenable
	 */
	public String getFtpenable()
	{
		return ftpenable;
	}

	/**
	 * @param ftpenable
	 *            the ftpenable to set
	 */
	public void setFtpenable(String ftpenable)
	{
		this.ftpenable = ftpenable;
	}

	/**
	 * @return the ftpUserName
	 */
	public String getFtpUserName()
	{
		return ftpUserName;
	}

	/**
	 * @param ftpUserName
	 *            the ftpUserName to set
	 */
	public void setFtpUserName(String ftpUserName)
	{
		this.ftpUserName = ftpUserName;
	}

	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword()
	{
		return ftpPassword;
	}

	/**
	 * @param ftpPassword
	 *            the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword)
	{
		this.ftpPassword = ftpPassword;
	}

	/**
	 * @return the ftpPort
	 */
	public String getFtpPort()
	{
		return ftpPort;
	}

	/**
	 * @param ftpPort
	 *            the ftpPort to set
	 */
	public void setFtpPort(String ftpPort)
	{
		this.ftpPort = ftpPort;
	}
}
