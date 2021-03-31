
package com.linkage.module.itms.config.act;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.module.itms.config.bio.NatStatusQueryBIO;

import action.splitpage.splitPageAction;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-3
 * @category com.linkage.module.itms.config.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class NatStatusQueryACT extends splitPageAction implements SessionAware
{

	private NatStatusQueryBIO bio;
	private String ajax;
	//设备id
	private String deviceId;
	private Map session;
	//类型
	private String gw_type;
	// 状态
	private String natstatus;
	/**
	 * 获取nat状态
	 * @return
	 */
	public String gatherWanPath()
	{
		ajax = bio.getherWanPath(deviceId, gw_type);
		return "ajax";
	}
	/**
	 * 修改nat状态
	 * @return
	 */
	public String open()
	{
		ajax = bio.close(deviceId, natstatus, gw_type);
		return "ajax";
	}

	public Map getSession()
	{
		return session;
	}

	@Override
	public void setSession(Map session)
	{
		this.session = session;
	}

	public NatStatusQueryBIO getBio()
	{
		return bio;
	}

	public void setBio(NatStatusQueryBIO bio)
	{
		this.bio = bio;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getNatstatus()
	{
		return natstatus;
	}

	public void setNatstatus(String natstatus)
	{
		this.natstatus = natstatus;
	}

	public String getGw_type()
	{
		return gw_type;
	}

	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
}
