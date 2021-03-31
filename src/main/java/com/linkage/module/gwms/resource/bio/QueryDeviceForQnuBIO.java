
package com.linkage.module.gwms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.resource.dao.QueryDeviceForQnuDAO;

/**
 * @author yinlei3 (73167.)
 * @version 1.0
 * @since 2016年1月5日
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class QueryDeviceForQnuBIO
{

	private QueryDeviceForQnuDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> getImpDeviceList(int curPage_splitPage, int num_splitPage,
			String device_serialnumber)
	{
		return dao.getImpDeviceList(curPage_splitPage, num_splitPage,
				device_serialnumber);
	}

	public int getImpDeviceCount(int curPage_splitPage, int num_splitPage,
			String device_serialnumber)
	{
		return dao.getImpDeviceCount(curPage_splitPage, num_splitPage,
				device_serialnumber);
	}

	public QueryDeviceForQnuDAO getDao()
	{
		return dao;
	}

	public void setDao(QueryDeviceForQnuDAO dao)
	{
		this.dao = dao;
	}
}
