
package com.linkage.module.itms.report.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author songxq
 * @version 1.0
 * @date 2021/1/14 14:40
 */
public class BridgeAndRouteRecordDao extends SuperDAO
{

	private static final String ONE = "1";
	private static final String TWO = "2";
	private static final String LOID = " and loid = ? ";
	private static final String USER_NAME = "  and username = ?  ";
	private static final String ADD_TIME_RIGHT = "  and add_time>=  ";
	private static final String ADD_TIME_LEFT = "  and add_time<=  ";

	public List<Map> getRecord(String userNameType, String userName, String startOpenDate,
			String endOpenDate, int curPageSplitPage, int numSplitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append(
				"select  loid,username,oper_action,oper_origon,oper_staff,add_time,oper_result,result_desc from bridge_route_oper_log where 1=1 ");
		if (ONE.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(LOID);
		}
		if (TWO.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(USER_NAME);
		}
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			psql.append(ADD_TIME_RIGHT + startOpenDate);
		}
		if (!StringUtil.IsEmpty(endOpenDate))
		{
			psql.append(ADD_TIME_LEFT + endOpenDate);
		}
		psql.append(" order by add_time desc ");
		if (!StringUtil.IsEmpty(userName))
		{
			psql.setString(1, userName);
		}
		return querySP(psql.getSQL(), (curPageSplitPage - 1) * numSplitPage + 1,
				numSplitPage);
	}

	public int getRecordCount(String userNameType, String userName, String startOpenDate,
			String endOpenDate)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1)  from bridge_route_oper_log where 1=1  ");
		if (ONE.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(LOID);
		}
		if (TWO.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(" and username = ? ");
		}
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			psql.append(" and add_time>= " + startOpenDate);
		}
		if (!StringUtil.IsEmpty(endOpenDate))
		{
			psql.append(" and add_time<=  " + endOpenDate);
		}
		if (!StringUtil.IsEmpty(userName))
		{
			psql.setString(1, userName);
		}
		return jt.queryForInt(psql.getSQL());
	}

	public List<Map> queryForExcel(String userNameType, String userName,
			String startOpenDate, String endOpenDate)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append(
				"select  loid,username,oper_action,oper_origon,oper_staff,add_time,oper_result,result_desc from bridge_route_oper_log where 1=1 ");
		if (ONE.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(LOID);
		}
		if (TWO.equals(userNameType) && !StringUtil.IsEmpty(userName))
		{
			psql.append(" and username = ? ");
		}
		if (!StringUtil.IsEmpty(startOpenDate))
		{
			psql.append(" and add_time>= " + startOpenDate);
		}
		if (!StringUtil.IsEmpty(endOpenDate))
		{
			psql.append(" and add_time<=  " + endOpenDate);
		}
		psql.append(" order by add_time desc ");
		if (!StringUtil.IsEmpty(userName))
		{
			psql.setString(1, userName);
		}
		return jt.queryForList(psql.getSQL());
	}
}
