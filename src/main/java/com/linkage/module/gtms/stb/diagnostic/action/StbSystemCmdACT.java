/**
 * 
 */

package com.linkage.module.gtms.stb.diagnostic.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.diagnostic.serv.StbSystemCmdBIO;


/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 */
public class StbSystemCmdACT implements ServletRequestAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StbSystemCmdACT.class);
	@SuppressWarnings("unused")
	private HttpServletRequest request = null;
	StbSystemCmdBIO cmdio = null;
	// device ID
	private String deviceId = null;
	private String rebootFlag = null;
	private String restoreFlag = null;
	/** 1:ITMS 2:BBMS 4:STB */
	private String gw_type = null;
	private String ajax = null;

	/**
	 * execute
	 */
	public String execute() throws Exception
	{
		logger.debug("execute()");
		return "success";
	}

	public String reboot()
	{
		logger.debug("reboot()");
		this.rebootFlag = cmdio.reboot(deviceId, gw_type);
		return "reboot";
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @return
	 */
	public String restore()
	{
		restoreFlag = cmdio.restore(deviceId, gw_type);
		return "restore";
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
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
	 * @return the cmdio
	 */
	public StbSystemCmdBIO getCmdio()
	{
		return cmdio;
	}

	/**
	 * @param cmdio
	 *            the cmdio to set
	 */
	public void setCmdio(StbSystemCmdBIO cmdio)
	{
		this.cmdio = cmdio;
	}

	/**
	 * @return the rebootFlag
	 */
	public String getRebootFlag()
	{
		return rebootFlag;
	}

	/**
	 * @param rebootFlag
	 *            the rebootFlag to set
	 */
	public void setRebootFlag(String rebootFlag)
	{
		this.rebootFlag = rebootFlag;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getRestoreFlag()
	{
		return restoreFlag;
	}

	public void setRestoreFlag(String restoreFlag)
	{
		this.restoreFlag = restoreFlag;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
