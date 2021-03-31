
package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dao.ConfigInfoDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-2-28 上午09:48:39
 * @category com.linkage.module.lims.stb.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class ConfigInfoBIO
{

	private ConfigInfoDAO dao;

	public List<Map> deviceIds(String servAccount, String serialnumber)
	{
		return dao.deviceIds(servAccount, serialnumber);
	}

	public List<Map> query(String servAccount, String serialnumber,
			List<Map> deviceIdsList, long startTime, long endTime, int curPage_splitPage,
			int num_splitPage)
	{
		return dao.query(servAccount, serialnumber, deviceIdsList, startTime, endTime,
				curPage_splitPage, num_splitPage);
	}

	public int getCount(String servAccount, String serialnumber, List<Map> deviceIdsList,
			long startTime, long endTime, int curPage_splitPage, int num_splitPage)
	{
		return dao.getCount(servAccount, serialnumber, deviceIdsList, startTime, endTime,
				curPage_splitPage, num_splitPage);
	}

	public Map queryDetail(String device_id, long startTime, long endTime)
	{
		return dao.queryDetail(device_id, startTime, endTime);
	}

	public ConfigInfoDAO getDao()
	{
		return dao;
	}

	public void setDao(ConfigInfoDAO dao)
	{
		this.dao = dao;
	}
}
