
package com.linkage.module.itms.service.obj;

/**
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2020年12月31日
 * @category com.linkage.module.itms.service.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SheetObj4GSStb
{

	private String userID;
	private String macAddress;
	private String password;
	private int platform;
	private int action;
	private String cityId;
	private String dealtime;

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public String getMacAddress()
	{
		return macAddress;
	}

	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getPlatform()
	{
		return platform;
	}

	public void setPlatform(int platform)
	{
		this.platform = platform;
	}

	public int getAction()
	{
		return action;
	}

	public void setAction(int action)
	{
		this.action = action;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getDealtime()
	{
		return dealtime;
	}

	public void setDealtime(String dealtime)
	{
		this.dealtime = dealtime;
	}
}
