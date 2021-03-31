
package com.linkage.module.itms.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 异常绑定统计
 * 
 * @author 王森博
 */
public class BindExceptionReportDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BindExceptionReportDAO.class);
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;

	/**
	 * 统计未绑定用户中 IPOSS没有同步到相应用户记录的数量
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
	 * @param
	 * @return Map
	 */
	public Map countNoBindUserNoIposs(String starttime1, String endtime1, String cityId)
	{
		logger.debug("countNoBindUserNoIposs({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			//TODO wait
			sql.append("select a.city_id, count(*) as total ");
		}else{
			sql.append("select a.city_id, count(1) as total ");
		}
		sql.append("from tab_hgwcustomer a ");
		sql.append("where a.user_state='1' and a.opmode='1' ");
		sql.append("and not exists (select 1 from gw_binded_by_mac b where a.username=b.username) ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.onlinedate>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.onlinedate<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 统计未绑定用户中 IPOSS同步到用户对应的MAC地址在ITMS中不存在的数量
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
	 * @param
	 * @return Map
	 */
	public Map countNoBindUserNoMac(String starttime1, String endtime1, String cityId)
	{
		logger.debug("countNoBindUserNoMac({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select city_id, count(*) as total ");
		}else{
			sql.append("select city_id, count(1) as total ");
		}
		sql.append("from gw_binded_by_mac where result_id=8 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and gather_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and gather_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append(" group by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 统计未绑定终端中 终端未上报MAC地址的数量
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
	 * @param
	 * @return Map
	 */
	public Map countNoBindDevcieNoMac(String starttime1, String endtime1, String cityId,
			String gw_type)
	{
		logger.debug("countNoBindUserNoMac({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select city_id, count(*) as total ");
		}else{
			sql.append("select city_id, count(1) as total ");
		}
		sql.append("from tab_gw_device where cpe_mac is null and cpe_allocatedstatus=0 ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and complete_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and complete_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and gw_type=").append(gw_type);
		}
		sql.append(" group by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}

	/**
	 * 统计未绑定终端中 终端上报的MAC地址，在IPOSS同步数据中没有相应记录的数量
	 * 
	 * @author wangsenbo
	 * @date Mar 3, 2010
	 * @param
	 * @return Map
	 */
	public Map countNoBindDeviceNoIposs(String starttime1, String endtime1,
			String cityId, String gw_type)
	{
		logger.debug("countNoBindDeviceNoIposs({},{},{})", new Object[] { starttime1,
				endtime1, cityId });
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id, count(*) as total ");
		}else{
			sql.append("select a.city_id, count(1) as total ");
		}
		sql.append("from tab_gw_device a where a.cpe_mac is not null and a.cpe_allocatedstatus=0 ");
		sql.append("and not exists (select 1 from gw_binded_by_mac b where a.cpe_mac=b.cpe_mac) ");
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sql.append(" and a.complete_time>=").append(starttime1);
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sql.append(" and a.complete_time<=").append(endtime1);
		}
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		if (!StringUtil.IsEmpty(gw_type))
		{
			sql.append(" and gw_type=").append(gw_type);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
		if (false == list.isEmpty())
		{
			for (int i = 0; i < list.size(); i++)
			{
				Map rmap = (Map) list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")),
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
}
