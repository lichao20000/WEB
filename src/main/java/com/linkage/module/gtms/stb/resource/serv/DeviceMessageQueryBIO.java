package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dao.DeviceMessageQueryDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-21
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DeviceMessageQueryBIO
{
	private DeviceMessageQueryDAO dao;
	private int maxPage_splitPage;
	public List<Map> querymessage(String oui,String device_model,int curPage_splitPage, int num_splitPage )
	{
		maxPage_splitPage=dao.getquerypaging(oui, device_model, curPage_splitPage, num_splitPage);
		return dao.querymessage(oui, device_model, curPage_splitPage, num_splitPage);
	}
	
	public DeviceMessageQueryDAO getDao()
	{
		return dao;
	}
	
	public void setDao(DeviceMessageQueryDAO dao)
	{
		this.dao = dao;
	}
	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}
	
	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
	
}
