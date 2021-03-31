
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.resource.dao.ReportUsernameDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-28 下午04:54:06
 * @category com.linkage.module.itms.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class ReportUsernameBIO
{

	private ReportUsernameDAO dao;
	private int maxPage_splitPage;

	public List<Map> queryUsername(String idStr,String deviceSnStr)
	{
		
		return dao.queryUsername(idStr,deviceSnStr);
	}

	public ReportUsernameDAO getDao()
	{
		return dao;
	}

	public void setDao(ReportUsernameDAO dao)
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
