
package com.linkage.module.itms.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.itms.config.dao.NatStatusQueryDAO;
import com.linkage.module.itms.config.util.GetherWanPathUtil;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-3
 * @category com.linkage.module.itms.config.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class NatStatusQueryBIO
{

	private NatStatusQueryDAO dao;

	public String getherWanPath(String deviceId, String gw_type)
	{
		ACSCorba corba = new ACSCorba(gw_type);
		GetherWanPathUtil util = new GetherWanPathUtil();
		String name = util.getVoipwanCon(deviceId, corba);
		String show = name + "NATEnabled";
		List<ParameValueOBJ> list = corba.getValue(deviceId, show);
		String value = "";
		if (null == list || list.isEmpty())
		{
			return "";
		}
		List<Map<String, String>> listshow = new ArrayList<Map<String, String>>();
		for (ParameValueOBJ obj : list)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", obj.getValue());
			listshow.add(map);
		}
		for (int i = 0; i < listshow.size(); i++)
		{
			value = listshow.get(i).get("value") + "#";
		}
		return value;
	}

	public String close(String deviceId, String natstatuss, String gw_type)
	{
		ACSCorba corba = new ACSCorba(gw_type);
		ArrayList<ParameValueOBJ> objList = new ArrayList<ParameValueOBJ>();
		String test = "";
		GetherWanPathUtil util = new GetherWanPathUtil();
		String name = util.getVoipwanCon(deviceId, corba);
		String show = name + "NATEnabled";
		objList.add(new ParameValueOBJ(show, natstatuss, "1"));
		int str = corba.setValue(deviceId, objList);
		if (str == 0)
		{
			test = "失败";
		}
		if (str == 1)
		{
			test = "成功";
		}
		if (str == -7)
		{
			test = "系统参数错误";
		}
		if (str == -6)
		{
			test = "设备正被操作";
		}
		if (str == -1)
		{
			test = "设备连接失败";
		}
		if (str == -9)
		{
			test = "系统内部错误";
		}
		return test;
	}

	public NatStatusQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(NatStatusQueryDAO dao)
	{
		this.dao = dao;
	}
}
