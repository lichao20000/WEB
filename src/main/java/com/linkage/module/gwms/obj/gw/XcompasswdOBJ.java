/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */

package com.linkage.module.gwms.obj.gw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;

/**
 * object:X_COM_PASSWD.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 17, 2009
 * @see
 * @since 1.0
 */
public class XcompasswdOBJ implements I_DevConfOBJ
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(XcompasswdOBJ.class);
	/** device_id */
	private String deviceId = null;
	/** gather_time */
	private long gatherTime = 0;
	/** enable */
	private final int enable = 1;
	/** x_com_passwd */
	private String xcompasswd = null;
	/**
	 * 设备序列号
	 */
	private String deviceSn = null;

	/**
	 * get:
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		logger.debug("getDeviceId()");
		return deviceId;
	}

	/**
	 * set:
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		logger.debug("setDeviceId({})", deviceId);
		this.deviceId = deviceId;
	}

	/**
	 * get:
	 * 
	 * @return the gatherTime
	 */
	public long getGatherTime()
	{
		logger.debug("getGatherTime()");
		return gatherTime;
	}

	/**
	 * set:
	 * 
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(long gatherTime)
	{
		logger.debug("setGatherTime({})", gatherTime);
		this.gatherTime = gatherTime;
	}

	/**
	 * get:
	 * 
	 * @return the enable
	 */
	public int getEnable()
	{
		logger.debug("getEnable()");
		return enable;
	}

	/**
	 * get:
	 * 
	 * @return the xcompasswd
	 */
	public String getXcompasswd()
	{
		logger.debug("getXcompasswd()");
		return xcompasswd;
	}

	/**
	 * set:
	 * 
	 * @param xcompasswd
	 *            the xcompasswd to set
	 */
	public void setXcompasswd(String xcompasswd)
	{
		logger.debug("setXcompasswd({})", xcompasswd);
		this.xcompasswd = xcompasswd;
	}

	/** string to object */
	public String toString()
	{
		logger.debug("toString()");
		return "[" + deviceId + "] " + this.xcompasswd;
	}

	public String getDeviceSn()
	{
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn)
	{
		this.deviceSn = deviceSn;
	}
}
