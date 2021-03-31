
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.resource.dao.DeviceToUsernameDAO;

public class DeviceToUsernameBIO
{

	private DeviceToUsernameDAO dao;

	/**
	 * 通过设备查询账号
	 * 
	 * @author wangsenbo
	 * @param tabName 
	 * @date May 6, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwByDeviceList(
			String deviceId, String deviceSn, String oui, int curPage_splitPage,
			int num_splitPage, String tabName)
	{
		// TODO Auto-generated method stub
		return dao.getHgwByDeviceList( deviceId, deviceSn, oui,
				curPage_splitPage, num_splitPage,tabName);
	}

	/**
	 * 通过设备查询账号统计条数
	 * 
	 * @author wangsenbo
	 * @param tabName 
	 * @date May 6, 2010
	 * @param
	 * @return List<Map>
	 */
	public int getHgwByDeviceCount( String deviceId,
			int curPage_splitPage, int num_splitPage, String tabName)
	{
		// TODO Auto-generated method stub
		return dao.getHgwByDeviceCount(deviceId, curPage_splitPage,
				num_splitPage,tabName);
	}

	/**
	 * @return the dao
	 */
	public DeviceToUsernameDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(DeviceToUsernameDAO dao)
	{
		this.dao = dao;
	}

	public int isHaveTable(String tabName)
	{
		return dao.isHaveTable(tabName);
	}
}
