
package com.linkage.module.itms.resource.bio;

import java.util.List;

import com.linkage.module.itms.resource.dao.CountFtthDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-11-18 下午03:27:24
 * @category com.linkage.module.itms.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class CountFtthBIO
{

	private CountFtthDAO dao;
	private int maxPage_splitPage;

	public List queryUnbind(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		maxPage_splitPage = dao.getCount(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
		return dao.queryUnbind(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
	}
	
	public List queryUnbind(String startTime, String endTime, String cityId)
	{
		
		return dao.queryUnbind(startTime, endTime, cityId);
	}
	
	public List queryFTTHDetail(String device_id){
		return dao.queryFTTHDetail(device_id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public List queryUnloid(String startTime, String endTime, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		maxPage_splitPage = dao.getUnloidCount(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
		return dao.queryUnloid(startTime, endTime, cityId, curPage_splitPage,
				num_splitPage);
	}
	
	
	public List queryUnloid(String startTime, String endTime, String cityId)
	{
		
		return dao.queryUnloid(startTime, endTime, cityId);
	}
	

	
	

	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	public CountFtthDAO getDao()
	{
		return dao;
	}

	public void setDao(CountFtthDAO dao)
	{
		this.dao = dao;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}
}
