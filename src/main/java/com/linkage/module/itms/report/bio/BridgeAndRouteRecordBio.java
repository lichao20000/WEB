
package com.linkage.module.itms.report.bio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.dao.BridgeAndRouteRecordDao;

/**
 * @author songxq
 * @version 1.0
 * @date 2021/1/14 14:40
 */
public class BridgeAndRouteRecordBio implements Serializable
{

	private static final long serialVersionUID = 1905122041950251207L;
	private transient BridgeAndRouteRecordDao dao;
	private int maxPageSplitPage;
	private int total;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String OPERACTION = "operaction";
	private static final String OPERRESULT = "operresult";
	private static final String ONE = "1";
	private static final String TWO = "2";
	private static final String THREE = "3";
	private static final String FOUR = "4";
	public BridgeAndRouteRecordDao getDao()
	{
		return dao;
	}

	public void setDao(BridgeAndRouteRecordDao dao)
	{
		this.dao = dao;
	}

	public List<Map> getRocord(String userNameType, String userName, String startOpenDate,
			String endOpenDate, int curPageSplitPage, int numSplitPage)
	{
		List<Map> result = dao.getRecord(userNameType, userName, startOpenDate,
				endOpenDate, curPageSplitPage, numSplitPage);
		for (Map map : result)
		{
			map.put("addtime",
					sdf.format(StringUtil.getLongValue(map.get("add_time")) * 1000));
			dealMap(map);
		}
		int total1 = this.getRecordCount(userNameType, userName, startOpenDate,
				endOpenDate);
		maxPageSplitPage = (total1 + numSplitPage - 1) / numSplitPage;
		this.total = total1;
		return result;
	}

	private void dealMap(Map map)
	{
		String action = StringUtil.getStringValue(map.get("oper_action"));
		if (ONE.equals(action))
		{
			map.put(OPERACTION, "路由改桥");
		}
		else if (TWO.equals(action))
		{
			map.put(OPERACTION, "桥改路由");
		}
		else if (THREE.equals(action))
		{
			map.put(OPERACTION, "桥接改桥接");
		}
		else if (FOUR.equals(action))
		{
			map.put(OPERACTION, "路由改路由");
		}
		String operResult = StringUtil.getStringValue(map.get("oper_result"));
		if (ONE.equals(operResult))
		{
			map.put(OPERRESULT, "成功");
		}
		else
		{
			map.put(OPERRESULT, "失败");
		}
	}

	private int getRecordCount(String userNameType, String userName, String startOpenDate,
			String endOpenDate)
	{
		return dao.getRecordCount(userNameType, userName, startOpenDate, endOpenDate);
	}

	public List<Map> queryForExcel(String userNameType, String userName,
			String startOpenDate, String endOpenDate)
	{
		List<Map> result = dao.queryForExcel(userNameType, userName, startOpenDate,
				endOpenDate);
		for (Map map : result)
		{
			map.put("addtime",
					sdf.format(StringUtil.getLongValue(map.get("add_time")) * 1000));
			dealMap(map);
		}
		return result;
	}

	public int getMaxPageSplitPage()
	{
		return maxPageSplitPage;
	}

	public void setMaxPageSplitPage(int maxPageSplitPage)
	{
		this.maxPageSplitPage = maxPageSplitPage;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}
}
