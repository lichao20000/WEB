
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author Reno (Ailk No.)
 * @version 1.0
 * @since 2015年10月21日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevRepairTestInfoDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(DevRepairTestInfoDAO.class);
	private int queryCount;

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getVendor()
	{
		PrepareSQL psql = new PrepareSQL(
				"select distinct vendor_id,vendor_name from tab_repair_vendor");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, String>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("repair_vendor_id",
						StringUtil.getStringValue(queryMap.get("vendor_id")));
				map.put("vendor_name", queryMap.get("vendor_name"));
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getBatchNum()
	{
		PrepareSQL psql = new PrepareSQL(
				"select distinct batch_number from tab_repair_device_info");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, String>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("batch_number", queryMap.get("batch_number"));
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarTotalCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.total_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id ,count(*) as total_count from tab_repair_device_archives where city_id is not null");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarIsTestCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.istest_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as istest_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and is_test = '1' group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarNullBindCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.nullbindtime_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as nullbindtime_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and recent_binddate = 0 group by city_id) as b on a.city_id = b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getNoUseCountByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
			throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.nouse_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as nouse_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and (is_test = '1' or recent_binddate = 0) group by city_id) as b on a.city_id = b.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarTotalCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.total_count from (select distinct vendor_id from tab_repair_vendor) as a"
				+ " left join (select repair_vendor_id,count(*) as total_count from tab_repair_device_archives where city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarIsTestCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.istest_count from (select distinct vendor_id from tab_repair_vendor) as a"
				+ " left join (select repair_vendor_id,count(*) as istest_count from tab_repair_device_archives where city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and is_test = '1' group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRateBarNullBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id,b.nullbindtime_count from (select distinct vendor_id from tab_repair_vendor) as a"
				+ " left join (select repair_vendor_id,count(*) as nullbindtime_count from tab_repair_device_archives where city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and recent_binddate = 0 group by repair_vendor_id ) as b on a.vendor_id = b.repair_vendor_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getNoUseCountByCity(String cityId, String starttime,
			String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id,b.nouse_count from (select distinct vendor_id from tab_repair_vendor) as a"
				+ " left join (select repair_vendor_id,count(*) as nouse_count from tab_repair_device_archives where city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and (is_test = '1' or recent_binddate = 0) group by repair_vendor_id ) as b on a.vendor_id = b.repair_vendor_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedCountByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
			throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.qualify_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as qualify_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and qualified_status = '1' group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedIsTestCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.istest_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id ,count(*) as istest_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and is_test = '1' group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getNoQualifiedByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
			throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.noqualified_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id ,count(*) as noqualified_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and (qualified_status != '1'and is_test != '1' and ( bind_status = 2 or bind_status = 3 )) group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedNoBindCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.nobind_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as nobind_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and bind_status = 3 group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedBindCountByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
			throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.bind_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as bind_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and bind_status = 2 group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedAddBussCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.city_id, b.addBuss_count from (select distinct city_id from tab_city where parent_id <> '-1' ");
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		else if (null != cityId && !"".equals(cityId) && "00".equals(cityId))
		{
			sql.append(" and parent_id = '00'");
		}
		sql.append(" ) as a left join (select city_id,count(*) as addBuss_count from tab_repair_device_archives where city_id is not null ");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and add_business = '1' group by city_id) as b on a.city_id = b.city_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifyCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.qualify_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ "left join (select repair_vendor_id ,count(*) as qualify_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and qualified_status = '1' group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedNoBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id,b.nobind_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ " left join (select repair_vendor_id ,count(*) as nobind_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and bind_status = 3 group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id,b.bind_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ " left join (select repair_vendor_id,count(*) as bind_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and bind_status = 2 group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedAddBussCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.addBuss_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ " left join (select repair_vendor_id,count(*) as addBuss_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and add_business = '1' group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedIsTestCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.istest_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ " left join (select repair_vendor_id ,count(*) as istest_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and is_test = '1' group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getNoQualifiedCountByCity(String cityId,
			String starttime, String endtime, String batchNum) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.vendor_id, b.noqualified_count from (select distinct vendor_id  from tab_repair_vendor) as a "
				+ " left join (select repair_vendor_id ,count(*) as noqualified_count from tab_repair_device_archives where ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		sql.append(" and qualified_status != '1'and is_test != '1' and ( bind_status = 2 or bind_status = 3 ) group by repair_vendor_id) as b on a.vendor_id = b.repair_vendor_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieTotalCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as total_count from  tab_repair_device_archives  where city_id <> '' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieIsTestCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as istest_count from tab_repair_device_archives  where city_id <>  '' and is_test = '1' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieNullBindCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as nullbindtime_count from tab_repair_device_archives  where city_id <> '' and recent_binddate = 0 ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieTotalCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as total_count from tab_repair_device_archives where city_id <> '' ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieIsTestCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as istest_count from tab_repair_device_archives  where city_id <> '' and is_test = '1' ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUseRatePieNullBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as nullbindtime_count from tab_repair_device_archives  where city_id <> '' and recent_binddate = 0 ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieCountByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as qualify_count from tab_repair_device_archives  where city_id <> '' and qualified_status = '1' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieIsTestCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as istest_count from tab_repair_device_archives  where city_id <> '' and  is_test = '1' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieBindCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as bind_count from tab_repair_device_archives  where city_id <> '' and  bind_status = 2 ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieNoBindCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as  nobind_count from tab_repair_device_archives  where city_id <> '' and  bind_status = 3 ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieAddBussCountByVendor(
			long repair_vendor_id, String starttime, String endtime, String batchNum,
			String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as addBuss_count from tab_repair_device_archives  where city_id <> '' and  add_business = '1' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "')");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedRatePieCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as qualify_count from tab_repair_device_archives where city_id <> '' and qualified_status = '1' ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieNoBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as nobind_count from tab_repair_device_archives where city_id <> '' and bind_status = 3 ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieBindCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as bind_count from tab_repair_device_archives where city_id <> '' and bind_status = 2 ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieAddBussCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as addBuss_count from tab_repair_device_archives where city_id <> '' and add_business = '1' ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getQualifiedPieIsTestCountByCity(String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as istest_count from tab_repair_device_archives where city_id <> '' and is_test = '1' ");
		addSqlByCityPie(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getVendorList(long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct repair_vendor_id from tab_repair_device_archives where 1=1");
		if (!(StringUtil.IsEmpty(cityId)) && !("-1".equals(cityId)))
		{
			sql.append(" and city_id = '" + cityId + "'");
		}
		if (-1 != repair_vendor_id)
		{
			sql.append(" and repair_vendor_id = " + repair_vendor_id);
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getTotalList(String city_id, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as total_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and city_id  ='" + city_id + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getQualifiedCountList(String city_id, long repair_vendor_id,
			String cityId, String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as qualify_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and city_id  ='" + city_id + "' and qualified_status = '1'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getNoBindCountList(String city_id, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as nobind_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and city_id  ='" + city_id + "' and bind_status = 3");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getBindCountList(String city_id, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as bind_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and city_id  ='" + city_id + "' and bind_status = 2");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getIsTestCountList(String city_id, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as istest_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and city_id  ='" + city_id + "' and is_test = '1'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "rawtypes" })
	public List getNoUseList(String city_id, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as nouse_count from tab_repair_device_archives where city_id <> ''");
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		sql.append(" and (is_test = '1'or recent_binddate = 0) and city_id ='" + city_id
				+ "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getNoUseListByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,repair_vendor,device_serialnumber,is_test,recent_binddate from tab_repair_device_archives where  (is_test = '1' or recent_binddate = 0) and city_id <> '' ");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "') ");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", StringUtil.getStringValue(CityDAO.getCityName(rs
						.getString("city_id"))));
				map.put("vendor_name", rs.getString("repair_vendor"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				String recent_binddate = rs.getString("recent_binddate");
				String is_test = rs.getString("is_test");
				if ("0".equals(recent_binddate))
				{
					map.put("status", "未绑定");
				}
				else if ("1".equals(is_test))
				{
					map.put("status", "已到货测试");
				}
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getNoUseListByCity(String cityId, String starttime, String endtime,
			String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,repair_vendor,device_serialnumber,is_test,recent_binddate from tab_repair_device_archives where  (is_test = '1' or recent_binddate = 0) and city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", StringUtil.getStringValue(CityDAO.getCityName(rs
						.getString("city_id"))));
				map.put("vendor_name", rs.getString("repair_vendor"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				String recent_binddate = rs.getString("recent_binddate");
				String is_test = rs.getString("is_test");
				if ("0".equals(recent_binddate))
				{
					map.put("status", "未绑定");
				}
				else if ("1".equals(is_test))
				{
					map.put("status", "已到货测试");
				}
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getQualifiedListByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,repair_vendor,device_serialnumber,recent_binddate,recent_unbinddate from tab_repair_device_archives where  qualified_status != '1'and is_test != '1' and ( bind_status = 2 or bind_status = 3 ) and city_id <> ''");
		if (!"00".equals(cityId))
		{
			List<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in ('" + StringUtils.weave(cityArray, "','") + "') ");
			cityArray = null;
		}
		addSqlByVendor(sql, repair_vendor_id, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", StringUtil.getStringValue(CityDAO.getCityName(rs
						.getString("city_id"))));
				map.put("vendor_name", rs.getString("repair_vendor"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				DateTimeUtil recent_binddate = new DateTimeUtil(StringUtil
						.getLongValue(rs.getString("recent_binddate")) * 1000l);
				map.put("recent_binddate", recent_binddate.getLongDate());
				DateTimeUtil recent_unbinddate = new DateTimeUtil(StringUtil
						.getLongValue(rs.getString("recent_unbinddate")) * 1000l);
				map.put("recent_unbinddate", recent_unbinddate.getLongDate());
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getQualifiedListByCity(String cityId, String starttime,
			String endtime, String batchNum)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_id,repair_vendor,device_serialnumber,recent_binddate,recent_unbinddate from tab_repair_device_archives where  qualified_status != '1'and is_test != '1' and ( bind_status = 2 or bind_status = 3 ) and city_id <> '' and ");
		addSqlByCity(sql, cityId, starttime, endtime, batchNum);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_name", StringUtil.getStringValue(CityDAO.getCityName(rs
						.getString("city_id"))));
				map.put("vendor_name", rs.getString("repair_vendor"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				DateTimeUtil recent_binddate = new DateTimeUtil(StringUtil
						.getLongValue(rs.getString("recent_binddate")) * 1000l);
				map.put("recent_binddate", recent_binddate.getLongDate());
				DateTimeUtil recent_unbinddate = new DateTimeUtil(StringUtil
						.getLongValue(rs.getString("recent_binddate")) * 1000l);
				map.put("recent_unbinddate", recent_unbinddate.getLongDate());
				return map;
			}
		});
		return list;
	}

	/**
	 * 根据属地统计sql
	 * 
	 * @param sql
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 */
	public void addSqlByVendor(StringBuffer sql, long repair_vendor_id, String starttime,
			String endtime, String batchNum)
	{
		if (repair_vendor_id != -1)
		{
			sql.append(" and repair_vendor_id = " + repair_vendor_id);
		}
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and manufacture_date >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and manufacture_date <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		if (!StringUtil.IsEmpty(batchNum) && !"-1".equals(batchNum))
		{
			sql.append(" and batch_number = '" + batchNum + "'");
		}
	}

	/**
	 * 根据厂商统计sql
	 * 
	 * @param sql
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 */
	public void addSqlByCity(StringBuffer sql, String cityId, String starttime,
			String endtime, String batchNum)
	{
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" manufacture_date >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and manufacture_date <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId) && !"00".equals(cityId))
		{
			sql.append(" and city_id = '" + cityId + "'");
		}
		if (!StringUtil.IsEmpty(batchNum) && !"-1".equals(batchNum))
		{
			sql.append(" and batch_number = '" + batchNum + "'");
		}
	}

	/**
	 * 根据厂商统计饼图sql
	 * 
	 * @param sql
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 */
	public void addSqlByCityPie(StringBuffer sql, String cityId, String starttime,
			String endtime, String batchNum)
	{
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and manufacture_date >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and manufacture_date <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
		{
			sql.append(" and city_id = '" + cityId + "'");
		}
		if (!StringUtil.IsEmpty(batchNum) && !"-1".equals(batchNum))
		{
			sql.append(" and batch_number = '" + batchNum + "'");
		}
	}

	/**
	 * 统计sql
	 * 
	 * @param sql
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 */
	public void addSqlByVendor(StringBuffer sql, String starttime, String endtime,
			String batchNum)
	{
		if (!StringUtil.IsEmpty(starttime) && !"".equals(starttime))
		{
			sql.append(" and manufacture_date >= "
					+ new DateTimeUtil(starttime).getLongTime());
		}
		if (!StringUtil.IsEmpty(endtime) && !"".equals(endtime))
		{
			sql.append(" and manufacture_date <= "
					+ new DateTimeUtil(endtime).getLongTime());
		}
		if (!StringUtil.IsEmpty(batchNum) && !"-1".equals(batchNum))
		{
			sql.append(" and batch_number = '" + batchNum + "'");
		}
	}

	/**
	 * 查询终端维修检测情况
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 终端维修检测情况列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryRepairDev(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryRepairDev()");
		attribution_city = CityDAO.getCityName(attribution_city);
		send_city = CityDAO.getCityName(send_city);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.repair_vendor,a.insurance_status,a.device_serialnumber,a.device_vendor,a.device_model, "
				+ "  a.check_result,a.send_city,a.attribution_city,a.manufacture_date,b.qualified_status ");
		psql.append(" from  tab_repair_device_info a left join tab_repair_device_archives b ");
		psql.append(" on a.device_serialnumber = b.device_serialnumber where 1=1");
		if ((!StringUtil.IsEmpty(device_serialnumber)))
		{
			psql.append("   and a.device_serialnumber like '%");
			psql.append(device_serialnumber);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(repair_vendor)) && (!"-1".equals(repair_vendor)))
		{
			psql.append("   and a.repair_vendor = '");
			psql.append(repair_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_vendor)) && (!"-1".equals(device_vendor)))
		{
			psql.append("   and a.device_vendor = '");
			psql.append(device_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_model)) && (!"-1".equals(device_model)))
		{
			psql.append("   and a.device_model = '");
			psql.append(device_model);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(attribution_city)) && (!"-1".equals(attribution_city))
				&& (!"江苏省中心".equals(attribution_city)))
		{
			psql.append("   and a.attribution_city = '");
			psql.append(attribution_city);
			psql.append("'");
		}
		if ((!StringUtil.IsEmpty(send_city)) && (!"-1".equals(send_city)))
		{
			psql.append("   and a.send_city = '");
			psql.append(send_city);
			psql.append("'");
		}
		if (!StringUtil.IsEmpty(manufacture_date_start))
		{
			psql.append("   and a.manufacture_date >= ");
			psql.append(manufacture_date_start);
		}
		if (!StringUtil.IsEmpty(manufacture_date_end))
		{
			psql.append("   and a.manufacture_date <= ");
			psql.append(manufacture_date_end);
		}
		psql.append(" order by a.manufacture_date desc");
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 维修厂家
				map.put("repair_vendor",
						StringUtil.getStringValue(rs.getString("repair_vendor")));
				// 保内保外
				if ("1".equals(StringUtil.getStringValue(rs.getString("insurance_status"))))
				{
					map.put("insurance_status", "保内");
				}
				else
				{
					map.put("insurance_status", "保外");
				}
				// 设备序列号
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				// 终端厂家
				map.put("device_vendor",
						StringUtil.getStringValue(rs.getString("device_vendor")));
				// 设备型号
				map.put("device_model",
						StringUtil.getStringValue(rs.getString("device_model")));
				// 检测结果
				if ("1".equals(StringUtil.getStringValue(rs.getString("check_result"))))
				{
					map.put("check_result", "合格");
				}
				else
				{
					map.put("check_result", "不合格");
				}
				// 发往城市
				map.put("send_city", StringUtil.getStringValue(rs.getString("send_city")));
				// 归属城市
				map.put("attribution_city",
						StringUtil.getStringValue(rs.getString("attribution_city")));
				// 出厂时间
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("manufacture_date")) * 1000l);
				map.put("manufacture_date", dt.getLongDate());
				// 现网使用合格情况
				if ("1".equals(StringUtil.getStringValue(rs.getString("qualified_status"))))
				{
					map.put("qualified_status", "合格");
				}
				else if ("0".equals(StringUtil.getStringValue(rs
						.getString("qualified_status"))))
				{
					map.put("qualified_status", "不合格");
				}
				else
				{
					map.put("qualified_status", "");
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 导出终端维修检测情况
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @return 终端维修检测情况excel
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseExcel(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end)
	{
		logger.debug("queryRepairDev()");
		attribution_city = CityDAO.getCityName(attribution_city);
		send_city = CityDAO.getCityName(send_city);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.repair_vendor,a.insurance_status,a.device_serialnumber,a.device_vendor,a.device_model, "
				+ "  a.check_result,a.send_city,a.attribution_city,a.manufacture_date,b.qualified_status,b.city_area,b.recent_binddate,b.recent_unbinddate,b.bind_loid,b.is_test ");
		psql.append(" from  tab_repair_device_info a left join tab_repair_device_archives b ");
		psql.append(" on a.device_serialnumber = b.device_serialnumber where 1=1");
		if ((!StringUtil.IsEmpty(device_serialnumber)))
		{
			psql.append("   and a.device_serialnumber like '%");
			psql.append(device_serialnumber);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(repair_vendor)) && (!"-1".equals(repair_vendor)))
		{
			psql.append("   and a.repair_vendor = '");
			psql.append(repair_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_vendor)) && (!"-1".equals(device_vendor)))
		{
			psql.append("   and a.device_vendor = '");
			psql.append(device_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_model)) && (!"-1".equals(device_model)))
		{
			psql.append("   and a.device_model = '");
			psql.append(device_model);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(attribution_city)) && (!"-1".equals(attribution_city))
				&& (!"江苏省中心".equals(attribution_city)))
		{
			psql.append("   and a.attribution_city = '");
			psql.append(attribution_city);
			psql.append("'");
		}
		if ((!StringUtil.IsEmpty(send_city)) && (!"-1".equals(send_city)))
		{
			psql.append("   and a.send_city = '");
			psql.append(send_city);
			psql.append("'");
		}
		if (!StringUtil.IsEmpty(manufacture_date_start))
		{
			psql.append("   and a.manufacture_date >= ");
			psql.append(manufacture_date_start);
		}
		if (!StringUtil.IsEmpty(manufacture_date_end))
		{
			psql.append("   and a.manufacture_date <= ");
			psql.append(manufacture_date_end);
		}
		psql.append(" order by a.manufacture_date desc");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 维修厂家
				map.put("repair_vendor",
						StringUtil.getStringValue(rs.getString("repair_vendor")));
				// 保内保外
				if ("1".equals(StringUtil.getStringValue(rs.getString("insurance_status"))))
				{
					map.put("insurance_status", "保内");
				}
				else
				{
					map.put("insurance_status", "保外");
				}
				// 设备序列号
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				// 终端厂家
				map.put("device_vendor",
						StringUtil.getStringValue(rs.getString("device_vendor")));
				// 设备型号
				map.put("device_model",
						StringUtil.getStringValue(rs.getString("device_model")));
				// 检测结果
				if ("1".equals(StringUtil.getStringValue(rs.getString("check_result"))))
				{
					map.put("check_result", "合格");
				}
				else
				{
					map.put("check_result", "不合格");
				}
				// 发往城市
				map.put("send_city", StringUtil.getStringValue(rs.getString("send_city")));
				// 归属城市
				map.put("attribution_city",
						StringUtil.getStringValue(rs.getString("attribution_city")));
				// 出厂时间
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
						.getString("manufacture_date")) * 1000l);
				map.put("manufacture_date", dt.getLongDate());
				// 现网使用合格情况
				if ("1".equals(StringUtil.getStringValue(rs.getString("qualified_status"))))
				{
					map.put("qualified_status", "合格");
				}
				else if ("0".equals(StringUtil.getStringValue(rs
						.getString("qualified_status"))))
				{
					map.put("qualified_status", "不合格");
				}
				else
				{
					map.put("qualified_status", "");
				}
				// 使用地区
				map.put("city_area", StringUtil.getStringValue(CityDAO.getCityName(rs
						.getString("city_area"))));
				// 最新绑定时间
				if (StringUtil.getLongValue(rs.getString("recent_binddate")) * 1000l == 0)
				{
					map.put("recent_binddate", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("recent_binddate")) * 1000l);
					map.put("recent_binddate", dt.getLongDate());
				}
				// 最新解绑时间
				if (StringUtil.getLongValue(rs.getString("recent_unbinddate")) * 1000l == 0)
				{
					map.put("recent_unbinddate", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("recent_unbinddate")) * 1000l);
					map.put("recent_unbinddate", dt.getLongDate());
				}
				// 绑定loid
				map.put("bind_loid", StringUtil.getStringValue(rs.getString("bind_loid")));
				// 到货测试
				String is_test = StringUtil.getStringValue(rs.getString("is_test"));
				if (StringUtil.IsEmpty(is_test))
				{
					map.put("is_test", "");
				}
				else if ("1".equals(is_test))
				{
					map.put("is_test", "是");
				}
				else if ("0".equals(is_test))
				{
					map.put("is_test", "否");
				}
				else
				{
					map.put("is_test", "");
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询终端维修检测情况总数
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 终端维修检测情况总数
	 */
	public int countQueryRepairDev(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countQueryRepairDev()");
		attribution_city = CityDAO.getCityName(attribution_city);
		send_city = CityDAO.getCityName(send_city);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) from  tab_repair_device_info a left join tab_repair_device_archives b ");
		psql.append(" on a.device_serialnumber = b.device_serialnumber where 1=1");
		if ((!StringUtil.IsEmpty(device_serialnumber)))
		{
			psql.append("   and a.device_serialnumber like '%");
			psql.append(device_serialnumber);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(repair_vendor)) && (!"-1".equals(repair_vendor)))
		{
			psql.append("   and a.repair_vendor = '");
			psql.append(repair_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_vendor)) && (!"-1".equals(device_vendor)))
		{
			psql.append("   and a.device_vendor = '");
			psql.append(device_vendor);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(device_model)) && (!"-1".equals(device_model)))
		{
			psql.append("   and a.device_model = '");
			psql.append(device_model);
			psql.append("' ");
		}
		if ((!StringUtil.IsEmpty(attribution_city)) && (!"-1".equals(attribution_city))
				&& (!"江苏省中心".equals(attribution_city)))
		{
			psql.append("   and a.attribution_city = '");
			psql.append(attribution_city);
			psql.append("'");
		}
		if ((!StringUtil.IsEmpty(send_city)) && (!"-1".equals(send_city)))
		{
			psql.append("   and a.send_city = '");
			psql.append(send_city);
			psql.append("'");
		}
		if (!StringUtil.IsEmpty(manufacture_date_start))
		{
			psql.append("   and a.manufacture_date >= ");
			psql.append(manufacture_date_start);
		}
		if (!StringUtil.IsEmpty(manufacture_date_end))
		{
			psql.append("   and a.manufacture_date <= ");
			psql.append(manufacture_date_end);
		}
		queryCount = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (queryCount % num_splitPage == 0)
		{
			maxPage = queryCount / num_splitPage;
		}
		else
		{
			maxPage = queryCount / num_splitPage + 1;
		}
		return maxPage;
	}

	/**
	 * 查询终端独立档案
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @return 终端独立档案
	 */
	@SuppressWarnings("rawtypes")
	public Map queryRepairDevDetail(String device_serialnumber, String attribution_city)
	{
		logger.debug("queryRepairDevDetail()");
		Map<String, String> devDetailMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL();
		psql.append("select * from  tab_repair_device_archives where 1=1");
		if (!StringUtil.IsEmpty(device_serialnumber))
		{
			psql.append("   and device_serialnumber = '");
			psql.append(device_serialnumber);
			psql.append("'");
			devDetailMap = DBOperation.getRecord(psql.getSQL(), "proxool.xml-report");
		}
		if (devDetailMap != null && !devDetailMap.isEmpty())
		{
			// 发往城市
			devDetailMap.put("send_city",
					StringUtil.getStringValue(devDetailMap, "send_city"));
			// 保内保外
			if ("1".equals(StringUtil.getStringValue(devDetailMap, "insurance_status")))
			{
				devDetailMap.put("insurance_status", "保内");
			}
			else
			{
				devDetailMap.put("insurance_status", "保外");
			}
			// 使用地区
			devDetailMap.put("city_area", StringUtil.getStringValue(CityDAO
					.getCityName(StringUtil.getStringValue(devDetailMap, "city_area"))));
			// 归属地市
			devDetailMap.put("city_id", attribution_city);
			// 出厂日期
			DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(StringUtil
					.getStringValue(devDetailMap, "manufacture_date")) * 1000l);
			devDetailMap.put("manufacture_date", dt.getLongDate());
			// 生产日期
			if (StringUtil.getLongValue(StringUtil.getStringValue(devDetailMap,
					"production_date")) * 1000l == 0)
			{
				devDetailMap.put("production_date", "");
			}
			else
			{
				dt = new DateTimeUtil(StringUtil.getLongValue(StringUtil.getStringValue(
						devDetailMap, "production_date")) * 1000l);
				devDetailMap.put("production_date", dt.getLongDate());
			}
			// 最新绑定时间
			if (StringUtil.getLongValue(StringUtil.getStringValue(devDetailMap,
					"recent_binddate")) * 1000l == 0)
			{
				devDetailMap.put("recent_binddate", "");
			}
			else
			{
				dt = new DateTimeUtil(StringUtil.getLongValue(StringUtil.getStringValue(
						devDetailMap, "recent_binddate")) * 1000l);
				devDetailMap.put("recent_binddate", dt.getLongDate());
			}
			// 终端状态
			String bind_status = StringUtil.getStringValue(devDetailMap, "bind_status");
			if ("1".equals(bind_status))
			{
				devDetailMap.put("bind_status", "未上线");
			}
			else if ("2".equals(bind_status))
			{
				devDetailMap.put("bind_status", "绑定");
			}
			else if ("3".equals(bind_status))
			{
				devDetailMap.put("bind_status", "解绑");
			}
			else
			{
				devDetailMap.put("bind_status", "");
			}
			// 最新解绑时间
			if (StringUtil.getLongValue(StringUtil.getStringValue(devDetailMap,
					"recent_unbinddate")) * 1000l == 0)
			{
				devDetailMap.put("recent_unbinddate", "");
			}
			else
			{
				dt = new DateTimeUtil(StringUtil.getLongValue(StringUtil.getStringValue(
						devDetailMap, "recent_unbinddate")) * 1000l);
				devDetailMap.put("recent_unbinddate", dt.getLongDate());
			}
			// 3月内合格
			String qualified_status = StringUtil.getStringValue(devDetailMap,
					"qualified_status");
			if (StringUtil.IsEmpty(qualified_status))
			{
				devDetailMap.put("qualified_status", "");
			}
			else if ("1".equals(qualified_status))
			{
				devDetailMap.put("qualified_status", "合格");
			}
			else if ("0".equals(qualified_status))
			{
				devDetailMap.put("qualified_status", "不合格");
			}
			else
			{
				devDetailMap.put("qualified_status", "");
			}
			// 绑定loid
			if (StringUtil.IsEmpty(StringUtil.getStringValue(devDetailMap, "bind_loid")))
			{
				devDetailMap.put("bind_loid", "");
			}
			// LOID状态
			String loid_status = StringUtil.getStringValue(devDetailMap, "loid_status");
			if (StringUtil.IsEmpty(loid_status))
			{
				devDetailMap.put("loid_status", "");
			}
			else if ("1".equals(loid_status))
			{
				devDetailMap.put("loid_status", "在用");
			}
			else if ("-1".equals(loid_status))
			{
				devDetailMap.put("loid_status", "销户");
			}
			else
			{
				devDetailMap.put("loid_status", "");
			}
			// 是否增加业务
			String add_business = StringUtil.getStringValue(devDetailMap, "add_business");
			if (StringUtil.IsEmpty(add_business))
			{
				devDetailMap.put("add_business", "");
			}
			else if ("1".equals(add_business))
			{
				devDetailMap.put("add_business", "是");
			}
			else if ("0".equals(add_business))
			{
				devDetailMap.put("add_business", "否");
			}
			else
			{
				devDetailMap.put("add_business", "");
			}
			// 到货测试
			String is_test = StringUtil.getStringValue(devDetailMap, "is_test");
			if (StringUtil.IsEmpty(is_test))
			{
				devDetailMap.put("is_test", "");
			}
			else if ("1".equals(is_test))
			{
				devDetailMap.put("is_test", "是");
			}
			else if ("0".equals(is_test))
			{
				devDetailMap.put("is_test", "否");
			}
			else
			{
				devDetailMap.put("is_test", "");
			}
		}
		return devDetailMap;
	}

	/**
	 * 获取维修厂家列表
	 * 
	 * @return 维修厂家列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getRepairVendor()
	{
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name from tab_repair_vendor");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, String>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("repair_vendor_id",
						StringUtil.getStringValue(queryMap, "vendor_id"));
				map.put("repair_vendor",
						StringUtil.getStringValue(queryMap, "vendor_name"));
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 终端厂家列表
	 * 
	 * @return 终端厂家列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDevVendor()
	{
		PrepareSQL psql = new PrepareSQL(
				"select distinct device_vendor from tab_repair_device_info order by device_vendor");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, String>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("device_vendor", queryMap.get("device_vendor"));
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 设备型号列表
	 * 
	 * @return 设备型号列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDevModel()
	{
		PrepareSQL psql = new PrepareSQL(
				"select distinct device_model from tab_repair_device_info order by device_model");
		List<Map<String, String>> queryList = jt.queryForList(psql.getSQL());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		if (queryList != null && queryList.size() > 0)
		{
			for (int i = 0; i < queryList.size(); i++)
			{
				map = new HashMap<String, String>();
				Map<String, String> queryMap = queryList.get(i);
				map.put("device_model", queryMap.get("device_model"));
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获取所有维修厂商map 维修厂商Map<vendor_id,vendor_name>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getRepairDevMap()
	{
		String strSQL = "select vendor_id,vendor_name from tab_repair_vendor";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Map map = DBOperation.getMap(strSQL, "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取所有终端厂商map 维修厂商Map<vendor_id,vendor_add>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevVendorMap()
	{
		String strSQL = "select vendor_id,vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Map map = DBOperation.getMap(strSQL, "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	public int getQueryCount()
	{
		return queryCount;
	}

	public List<Map<String, String>> getCityName()
	{
		return null;
	}
}
