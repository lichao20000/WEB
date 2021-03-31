
package com.linkage.module.itms.resource.bio;

import java.util.List;

import com.linkage.module.itms.resource.dao.HgwByMacDAO;

public class HgwByMacBIO
{

	private HgwByMacDAO hgwByMacDao;

	public List getHgwByMacList(String starttime1, String endtime1, String username,
			String mac, int curPage_splitPage, int num_splitPage)
	{
		return hgwByMacDao.getHgwByMacList(starttime1, endtime1, username, mac,
				curPage_splitPage, num_splitPage);
	}

	public int getHgwByMacCount(String starttime1, String endtime1, String username,
			String mac, int curPage_splitPage, int num_splitPage)
	{
		return hgwByMacDao.getHgwByMacCount(starttime1, endtime1, username, mac,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * @return the hgwByMacDao
	 */
	public HgwByMacDAO getHgwByMacDao()
	{
		return hgwByMacDao;
	}

	/**
	 * @param hgwByMacDao
	 *            the hgwByMacDao to set
	 */
	public void setHgwByMacDao(HgwByMacDAO hgwByMacDao)
	{
		this.hgwByMacDao = hgwByMacDao;
	}
}
