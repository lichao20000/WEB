
package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.service.dao.ErrorBssSheetDAO;

/**
 * @author zhangshimin(工号) Tel:��
 * @version 1.0
 * @since 2011-5-26 下午09:11:32
 * @category com.linkage.module.itms.service.bio
 * @copyright 南京联创科技 网管科技部
 */
public class ErrorBssSheetBIO
{

	private ErrorBssSheetDAO dao;
	private int maxPage_splitPage;
	public List<Map<String, String>> queryErrSheet(String reciveDateStart,
			String reciveDateEnd, String cityId, String username, String sheetType,
			String resultSheet, int curPage_splitPage, int num_splitPage)
	{
		maxPage_splitPage = dao.countErrSheet(reciveDateStart, reciveDateEnd, cityId, username,
				sheetType, resultSheet,curPage_splitPage,num_splitPage);
		return dao.queryErrSheet(reciveDateStart, reciveDateEnd, cityId, username,
				sheetType, resultSheet,curPage_splitPage,num_splitPage);
	}

	
	public int getMaxPage_splitPage()
	{
		return maxPage_splitPage;
	}

	
	public void setMaxPage_splitPage(int maxPage_splitPage)
	{
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public ErrorBssSheetDAO getDao()
	{
		return dao;
	}

	public void setDao(ErrorBssSheetDAO dao)
	{
		this.dao = dao;
	}
}
