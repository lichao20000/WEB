
package com.linkage.module.gwms.resource.act;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.RefDelDevBIO;

/**
 * 刷新、删除设备，同时更新内存
 * 
 * @author 王森博
 */
public class RefDelDevACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(RefDelDevACT.class);
	// session
	private Map session;
	private String ajax;
	private String deviceId;
	private String oui;
	private String deviceSN;
	private String gw_type;
	/**
	 * 是否绑定，1为已绑定，0为未绑定
	 */
	private String cpe_allocatedstatus;
	private RefDelDevBIO refDelDevBio;

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
	 * 设备刷新，通知ACS刷新内存
	 * 
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param
	 * @return String
	 */
	public String refresh()
	{
		logger.debug("refresh()");
		UserRes curUser = (UserRes) session.get("curUser");
		logger.warn("gw_type({})", gw_type);
		if(StringUtil.IsEmpty(gw_type)){
			gw_type = LipossGlobals.getGw_Type(deviceId);
		}
		ajax = refDelDevBio.refresh(deviceId, curUser.getUser().getAccount(), curUser
				.getUser().getPasswd(), oui, deviceSN, gw_type);
		return "ajax";
	}

	/**
	 * 设备删除，先删除设备后通知ACS刷新内存
	 * 
	 * @author wangsenbo
	 * @date Apr 14, 2010
	 * @param
	 * @return String
	 */
	public String delete()
	{
		logger.warn("delete()");
		UserRes curUser = (UserRes) session.get("curUser");
		gw_type = LipossGlobals.getGw_Type(deviceId);
		ajax = refDelDevBio.delete(deviceId, oui, deviceSN, curUser
				.getUser().getAccount(), curUser.getUser().getPasswd(), gw_type);
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
	 * @return the cpe_allocatedstatus
	 */
	public String getCpe_allocatedstatus()
	{
		return cpe_allocatedstatus;
	}

	/**
	 * @param cpe_allocatedstatus
	 *            the cpe_allocatedstatus to set
	 */
	public void setCpe_allocatedstatus(String cpe_allocatedstatus)
	{
		this.cpe_allocatedstatus = cpe_allocatedstatus;
	}

	/**
	 * @return the refDelDevBio
	 */
	public RefDelDevBIO getRefDelDevBio()
	{
		return refDelDevBio;
	}

	/**
	 * @param refDelDevBio
	 *            the refDelDevBio to set
	 */
	public void setRefDelDevBio(RefDelDevBIO refDelDevBio)
	{
		this.refDelDevBio = refDelDevBio;
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
	 * @return the deviceSN
	 */
	public String getDeviceSN()
	{
		return deviceSN;
	}

	/**
	 * @param deviceSN
	 *            the deviceSN to set
	 */
	public void setDeviceSN(String deviceSN)
	{
		this.deviceSN = deviceSN;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}
}
