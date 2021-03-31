
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-20
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PonNetWorkDegraInfoDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory
			.getLogger(PonNetWorkDegraInfoDAO.class);

	public Map<String, String> getE8cdata(String city_id)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getE8cdata");
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,count(1) as num from tab_hgwcustomer where device_id is not null ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		sql.append("group  by city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("city_id")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	public Map<String, String> getDegreadata(String start_time, String end_time)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegreadata");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select area_name,count(1) as num from tab_pon_netWork a , tab_hgwcustomer b where a.loid=b.username and a.count_num>=5 and b.device_id is not null and a.time>=to_date(?,'yyyy-mm-dd') and a.time<=to_date(?,'yyyy-mm-dd')  group by a.area_name");
		}
		else
		{
			sql.append("select area_name,count(1) as num from tab_pon_netWork a , tab_hgwcustomer b where a.loid=b.username and a.count_num>=5 and b.device_id is not null  and a.time>convert(date,?) and a.time<convert(date,?)  group by a.area_name ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, start_time);
		psql.setString(2, end_time);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("area_name")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	public Map<String, String> getDegreadataLess(String start_time, String end_time)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegreadata");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select area_name,count(1) as num from tab_pon_netWork a , tab_hgwcustomer b where a.loid=b.username and a.count_num>=5 and b.device_id is not null and  to_number(a.rx_power)<to_number('-30.00') and a.time>=to_date(?,'yyyy-mm-dd') and a.time<=to_date(?,'yyyy-mm-dd')  group by a.area_name");
		}
		else
		{
			sql.append("select area_name,count(1) as num from tab_pon_netWork a , tab_hgwcustomer b where a.loid=b.username and a.count_num>=5 and b.device_id is not null and  convert(numeric,a.rx_power)<convert(numeric,'-30.00') and a.time>convert(date,?) and a.time<convert(date,?)  group by a.area_name ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, start_time);
		psql.setString(2, end_time);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if (null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i).get("area_name")),
						StringUtil.getStringValue(list.get(i).get("num")));
			}
		}
		return map;
	}

	public List<Map> getDegraContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContext");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null ");
		}
		else
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// if(!StringUtil.IsEmpty(city_id)){
		// sql.append(" and b.city_id='").append(city_id).append("'  ");
		// }
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				// sql.append(" and a.TIME>convert(date,'").append(start_time).append("')");
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				// sql.append(" and a.TIME<convert(date,'").append(end_time).append("')");
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("area_name", rs.getString("area_name"));
				map.put("subarea_name", rs.getString("subarea_name"));
				map.put("loid", rs.getString("loid"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("tx_power", rs.getString("tx_power"));
				map.put("rx_power", rs.getString("rx_power"));
				map.put("linkaddress", rs.getString("linkaddress"));
				map.put("olt_name", rs.getString("olt_name"));
				map.put("olt_ip", rs.getString("olt_ip"));
				map.put("pon_id", rs.getString("pon_id"));
				map.put("ont_id", rs.getString("ont_id"));
				map.put("count_num", rs.getString("count_num"));
				return map;
			}
		});
		return list;
	}

	public List<Map> getDegraContextExcel(String city_id, String start_time,
			String end_time)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContextExcel");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null ");
		}
		else
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("area_name",
						StringUtil.getStringValue(list.get(i).get("area_name")));
				list.get(i).put("subarea_name",
						StringUtil.getStringValue(list.get(i).get("subarea_name")));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put("tx_power",
						StringUtil.getStringValue(list.get(i).get("tx_power")));
				list.get(i).put("rx_power",
						StringUtil.getStringValue(list.get(i).get("rx_power")));
				list.get(i).put("linkaddress",
						StringUtil.getStringValue(list.get(i).get("linkaddress")));
				list.get(i).put("olt_name",
						StringUtil.getStringValue(list.get(i).get("olt_name")));
				list.get(i).put("olt_ip",
						StringUtil.getStringValue(list.get(i).get("olt_ip")));
				list.get(i).put("pon_id",
						StringUtil.getStringValue(list.get(i).get("pon_id")));
				list.get(i).put("ont_id",
						StringUtil.getStringValue(list.get(i).get("ont_id")));
				list.get(i).put("count_num",
						StringUtil.getStringValue(list.get(i).get("count_num")));
			}
		}
		return list;
	}

	public int countDegraContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContext");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select count(1) from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null  ");
		}
		else
		{
			sql.append("select count(1) from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and b.device_id is not null ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// if(!StringUtil.IsEmpty(city_id)){
		// sql.append(" and b.city_id='").append(city_id).append("' ");
		// }
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				// sql.append(" and a.TIME>convert(date,'").append(start_time).append("')");
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				// sql.append(" and a.TIME<convert(date,'").append(end_time).append("')");
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
	public List<Map> getDegraLessContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContext");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and to_number(a.rx_power)<to_number('-30.00') and b.device_id is not null ");
		}
		else
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and convert(numeric,a.rx_power)<convert(numeric,'-30.00') and b.device_id is not null ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				// sql.append(" and a.TIME>convert(date,'").append(start_time).append("')");
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				// sql.append(" and a.TIME<convert(date,'").append(end_time).append("')");
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("area_name", rs.getString("area_name"));
				map.put("subarea_name", rs.getString("subarea_name"));
				map.put("loid", rs.getString("loid"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("tx_power", rs.getString("tx_power"));
				map.put("rx_power", rs.getString("rx_power"));
				map.put("linkaddress", rs.getString("linkaddress"));
				map.put("olt_name", rs.getString("olt_name"));
				map.put("olt_ip", rs.getString("olt_ip"));
				map.put("pon_id", rs.getString("pon_id"));
				map.put("ont_id", rs.getString("ont_id"));
				map.put("count_num", rs.getString("count_num"));
				return map;
			}
		});
		return list;
	}
	
	
	public int countDegraLessContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContext");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select count(1) from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5  and to_number(a.rx_power)<to_number('-30.00') and b.device_id is not null  ");
		}
		else
		{
			sql.append("select count(1) from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and convert(numeric,a.rx_power)<convert(numeric,'-30.00') and b.device_id is not null ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// if(!StringUtil.IsEmpty(city_id)){
		// sql.append(" and b.city_id='").append(city_id).append("' ");
		// }
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				// sql.append(" and a.TIME>convert(date,'").append(start_time).append("')");
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				// sql.append(" and a.TIME<convert(date,'").append(end_time).append("')");
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
	public List<Map> getDegraLessContextExcel(String city_id, String start_time,
			String end_time)
	{
		logger.debug("PonNetWorkDegraInfoDAO->getDegraContextExcel");
		StringBuffer sql = new StringBuffer();
		if (LipossGlobals.isOracle())
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and to_number(a.rx_power)<to_number('-30.00') and b.device_id is not null  ");
		}
		else
		{
			sql.append("select a.area_name,a.subarea_name,a.loid,b.device_serialnumber,a.tx_power,a.rx_power,b.linkaddress,a.olt_name,a.olt_ip,a.pon_id,a.ont_id,a.count_num from tab_pon_netWork  a,tab_hgwcustomer b   ");
			sql.append(" where a.loid=b.username and a.count_num>=5 and convert(numeric,a.rx_power)<convert(numeric,'-30.00') and b.device_id is not null  ");
		}
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and b.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		// 判断是不是oracle数据库
		if (LipossGlobals.isOracle())
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>=to_date('").append(start_time)
						.append("', 'yyyy-mm-dd')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<=to_date('").append(end_time)
						.append("', 'yyyy-mm-dd')");
			}
		}
		else
		{
			if (!StringUtil.IsEmpty(start_time))
			{
				sql.append(" and a.time>convert(date,'").append(start_time).append("')");
			}
			if (!StringUtil.IsEmpty(end_time))
			{
				sql.append(" and a.time<convert(date,'").append(end_time).append("')");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				list.get(i).put("area_name",
						StringUtil.getStringValue(list.get(i).get("area_name")));
				list.get(i).put("subarea_name",
						StringUtil.getStringValue(list.get(i).get("subarea_name")));
				list.get(i).put("loid",
						StringUtil.getStringValue(list.get(i).get("loid")));
				list.get(i)
						.put("device_serialnumber",
								StringUtil.getStringValue(list.get(i).get(
										"device_serialnumber")));
				list.get(i).put("tx_power",
						StringUtil.getStringValue(list.get(i).get("tx_power")));
				list.get(i).put("rx_power",
						StringUtil.getStringValue(list.get(i).get("rx_power")));
				list.get(i).put("linkaddress",
						StringUtil.getStringValue(list.get(i).get("linkaddress")));
				list.get(i).put("olt_name",
						StringUtil.getStringValue(list.get(i).get("olt_name")));
				list.get(i).put("olt_ip",
						StringUtil.getStringValue(list.get(i).get("olt_ip")));
				list.get(i).put("pon_id",
						StringUtil.getStringValue(list.get(i).get("pon_id")));
				list.get(i).put("ont_id",
						StringUtil.getStringValue(list.get(i).get("ont_id")));
				list.get(i).put("count_num",
						StringUtil.getStringValue(list.get(i).get("count_num")));
			}
		}
		return list;
	}
	
}
