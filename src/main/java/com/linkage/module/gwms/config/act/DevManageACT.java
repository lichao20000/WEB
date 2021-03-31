
package com.linkage.module.gwms.config.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.config.bio.DevManageBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class DevManageACT extends ActionSupport implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(DevManageACT.class);
	private String acsUrl;
	private String deviceIds;
	private String ajax;
	private DevManageBIO bio;
	// session
	private Map session;
	private String softStrategyHTML;
	private String softStrategy_type;

	/**
	 * itms设备转换成bbms设备
	 * 
	 * @author wangsenbo
	 * @date 2009-12-08
	 * @return String
	 */
	public String execute()
	{
		logger.debug("execute()");
		if (true == StringUtil.IsEmpty(deviceIds))
		{
			logger.debug("任务中没有设备");
		}
		else
		{
			String[] deviceId_array = deviceIds.split(",");
			UserRes curUser = (UserRes) session.get("curUser");
			long accoid = curUser.getUser().getId();
			bio.ItmsToBbms(accoid, deviceId_array, acsUrl);
		}
		return "result";
	}

	public String init()
	{
		softStrategyHTML = bio.getStrategyList("softStrategy_type", new String[] { "4",
				"5", "0" });
		return "success";
	}

	/**
	 * itms设备转换成bbms设备
	 * 
	 * @author wangsenbo
	 * @date 2009-12-08
	 * @return String
	 */
	public String ipToUrl()
	{
		logger.debug("execute()");
		if (true == StringUtil.IsEmpty(deviceIds))
		{
			logger.debug("任务中没有设备");
		}
		else
		{
			String[] deviceId_array = deviceIds.split(",");
			UserRes curUser = (UserRes) session.get("curUser");
			long accoid = curUser.getUser().getId();
			bio.ipToUrl(accoid, deviceId_array, softStrategy_type, acsUrl);
		}
		return "result";
	}

	/**
	 * @return the acsUrl
	 */
	public String getAcsUrl()
	{
		return acsUrl;
	}

	/**
	 * @param acsUrl
	 *            the acsUrl to set
	 */
	public void setAcsUrl(String acsUrl)
	{
		this.acsUrl = acsUrl;
	}

	/**
	 * @return the deviceIds
	 */
	public String getDeviceIds()
	{
		return deviceIds;
	}

	/**
	 * @param deviceIds
	 *            the deviceIds to set
	 */
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
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
	 * @return the bio
	 */
	public DevManageBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(DevManageBIO bio)
	{
		this.bio = bio;
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
	 * @return the softStrategyHTML
	 */
	public String getSoftStrategyHTML()
	{
		return softStrategyHTML;
	}

	/**
	 * @param softStrategyHTML
	 *            the softStrategyHTML to set
	 */
	public void setSoftStrategyHTML(String softStrategyHTML)
	{
		this.softStrategyHTML = softStrategyHTML;
	}

	/**
	 * @return the softStrategy_type
	 */
	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}

	/**
	 * @param softStrategy_type
	 *            the softStrategy_type to set
	 */
	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}
}
