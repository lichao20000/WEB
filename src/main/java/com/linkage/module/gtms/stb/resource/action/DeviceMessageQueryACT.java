package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import action.splitpage.splitPageAction;

import com.linkage.module.gtms.stb.resource.serv.DeviceMessageQueryBIO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-21
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DeviceMessageQueryACT extends splitPageAction
{
	private DeviceMessageQueryBIO bio;
	private String oui;
	private String device_model;
	private List<Map> date;
	
	public String querymessage()
	{
		date=bio.querymessage(oui, device_model, curPage_splitPage, num_splitPage);
		maxPage_splitPage=bio.getMaxPage_splitPage();
		return "success";
	}

	
	public DeviceMessageQueryBIO getBio()
	{
		return bio;
	}

	
	public void setBio(DeviceMessageQueryBIO bio)
	{
		this.bio = bio;
	}

	
	public String getOui()
	{
		return oui;
	}

	
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	
	public String getDevice_model()
	{
		return device_model;
	}

	
	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}

	
	public List<Map> getDate()
	{
		return date;
	}

	
	public void setDate(List<Map> date)
	{
		this.date = date;
	}
	
}
