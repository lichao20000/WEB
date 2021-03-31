package com.linkage.liposs.action.aaa;

import java.util.List;

import com.linkage.liposs.dao.aaa.LogQueryDAO;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 显示设备的详细信息
 * @author benyp(5260) email:benyp@lianchaung.com
 * @version 1.0
 * @since 2007-12-3
 */
public class devDetailshowAction extends ActionSupport
{
	private LogQueryDAO lqd;
	//******************************
	private String device_id;		//设备id(用于查询关联的设备帐号)
	private String lookback_ip;		//设备IP(传入的参数)
	private String device_name;		//设备名称(传入的参数)
	private String authen_prtc;		//认证协议1:tracts+2:radius
	private String tac_key;			//共享密钥
	private List userList;			//认证账户列表
	//******************************
	/**
	 * 取得该设备下的用户信息
	 */
	public String execute() throws Exception
	{
		userList=lqd.getUserInfoByDev(device_id);
		return SUCCESS;
	}

	public String getDevice_id()
	{
		return device_id;
	}

	public String getLookback_ip()
	{
		return lookback_ip;
	}

	public String getDevice_name()
	{
		return device_name;
	}

	public String getAuthen_prtc()
	{
		return authen_prtc==null||authen_prtc.trim().equals("null")?"":authen_prtc;
	}

	public String getTac_key()
	{
		return tac_key;
	}

	public List getUserList()
	{
		return userList;
	}


	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public void setLookback_ip(String lookback_ip)
	{
		this.lookback_ip = lookback_ip;
	}

	public void setDevice_name(String device_name)
	{
		this.device_name=device_name;
	}

	public void setAuthen_prtc(String authen_prtc)
	{
		this.authen_prtc=authen_prtc;
	}

	public void setTac_key(String tac_key)
	{
		this.tac_key = tac_key;
	}

	public void setLqd(LogQueryDAO lqd)
	{
		this.lqd = lqd;
	}

}
