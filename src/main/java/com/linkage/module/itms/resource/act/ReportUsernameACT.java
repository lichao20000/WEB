
package com.linkage.module.itms.resource.act;

import java.util.List;
import java.util.Map;

import action.splitpage.splitPageAction;

import com.linkage.module.itms.resource.bio.ReportUsernameBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-28 下午04:51:34
 * @category com.linkage.module.itms.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class ReportUsernameACT extends splitPageAction
{

	private ReportUsernameBIO bio;
	private String[] idArray;
	private String[] deviceSnArray;
	private List<Map> userNameList;

	public String execute()
	{
		return "init";
	}

	public String queryUsername()
	{
		String idStr = idArray[0];
		String deviceSnStr = deviceSnArray[0];
		userNameList = bio.queryUsername(idStr, deviceSnStr);
		return "list";
	}

	public List<Map> getUserNameList()
	{
		return userNameList;
	}

	public void setUserNameList(List<Map> userNameList)
	{
		this.userNameList = userNameList;
	}

	public String[] getIdArray()
	{
		return idArray;
	}

	public void setIdArray(String[] idArray)
	{
		this.idArray = idArray;
	}

	public String[] getDeviceSnArray()
	{
		return deviceSnArray;
	}

	public void setDeviceSnArray(String[] deviceSnArray)
	{
		this.deviceSnArray = deviceSnArray;
	}

	public ReportUsernameBIO getBio()
	{
		return bio;
	}

	public void setBio(ReportUsernameBIO bio)
	{
		this.bio = bio;
	}
}
