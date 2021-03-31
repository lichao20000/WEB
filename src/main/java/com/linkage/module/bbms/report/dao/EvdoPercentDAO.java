
package com.linkage.module.bbms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.bbms.report.bio.EvdoPercentBIO;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.StringUtils;

public class EvdoPercentDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EvdoPercentBIO.class);

	/**
	 * 获取按本地网的总网关数
	 *
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return Map
	 */
	public Map getCityTotal(String starttime1, String endtime1)
	{
		logger.debug("getCityTotal({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		// 按属地查询总网关数
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) toatl,b.city_id from tab_egwcustomer a,tab_customerinfo b where a.customer_id=b.customer_id");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.opendate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.opendate<=").append(endtime1);
		}
		sql.append(" group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();

		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("toatl")));
			}
		}
		return map;
	}

	/**
	 * 获取按本地网的EVDO网关数
	 *
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return Map
	 */
	public Map getCityEvdoTotal(String starttime1, String endtime1)
	{
		logger.debug("getCityEvdoTotal({},{})", new Object[] { starttime1, endtime1 });
		Map<String, String> map = new HashMap<String, String>();
		// 按属地查询EVDO网关数
		StringBuffer sql = new StringBuffer();
		// TODO wait (more table related)
		sql.append("select count(*) toatl,b.city_id from tab_egwcustomer a,tab_customerinfo b,hgwcust_serv_info c where a.user_id=c.user_id and a.customer_id=b.customer_id");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.opendate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.opendate<=").append(endtime1);
		}
		sql.append("  and c.serv_type_id=70 group by b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("toatl")));
			}
		}
		return map;
	}

	/**
	 * 获取按行业的网关总数
	 *
	 * @author wangsenbo
	 * @param cityList
	 * @date Nov 19, 2009
	 * @return Map
	 */
	public Map getWayTotal(String starttime1, String endtime1, ArrayList<String> cityList)
	{
		logger.debug("getWayTotal({},{},{})", new Object[] { starttime1, endtime1,
				cityList });
		Map<String, String> map = new HashMap<String, String>();
		// 按行业查询总网关数
		StringBuffer sql = new StringBuffer();
		sql
				.append("select count(*) toatl,b.customer_type from tab_egwcustomer a,tab_customerinfo b where a.customer_id=b.customer_id");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.opendate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.opendate<=").append(endtime1);
		}
		if (cityList != null)
		{
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityList)).append(
					")");
		}
		sql.append(" group by b.customer_type");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("customer_type")), StringUtil
						.getStringValue(rmap.get("toatl")));
			}
		}
		return map;
	}

	/**
	 * 获取按行业的EVDO网关总数
	 *
	 * @author wangsenbo
	 * @param cityList
	 * @date Nov 19, 2009
	 * @return Map
	 */
	public Map getWayEvdoTotal(String starttime1, String endtime1,
			ArrayList<String> cityList)
	{
		logger.debug("getWayEvdoTotal({},{},{})", new Object[] { starttime1, endtime1,
				cityList });
		Map<String, String> map = new HashMap<String, String>();
		// 按属地查询EVDO网关数
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql
				.append("select count(*) toatl,b.customer_type from tab_egwcustomer a,tab_customerinfo b,hgwcust_serv_info c where a.user_id=c.user_id and a.customer_id=b.customer_id");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.opendate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.opendate<=").append(endtime1);
		}
		if (cityList != null)
		{
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityList)).append(
					")");
		}
		sql.append("  and c.serv_type_id=70 group by b.customer_type");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("customer_type")), StringUtil
						.getStringValue(rmap.get("toatl")));
			}
		}
		return map;
	}
}
