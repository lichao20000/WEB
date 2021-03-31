
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 回收终端使用率
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-26
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class RecycleDevRateDAO extends SuperDAO
{

	private int queryCount;

	/**
	 * 获取回收终端使用数
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getRecycleDevCount(String cityId, String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id,count(1) recycleDevcount from tab_gw_recoverlist");
		psql.append(" where revert_time>=? and revert_time<=? and is_recover = 1 and is_used =1 ");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		psql.setString(3, cityId);
		psql.setString(4, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report" );
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取江苏省中心回收终端使用数
	 * @author 岩 
	 * @date 2016-4-26
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getRecycleDevCountFor00( String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id, count(1) recycleDevcount  from tab_gw_recoverlist");
		psql.append(" where revert_time>=? and revert_time<=? and city_id <> null and is_recover = 1 and is_used =1 ");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report" );
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取江苏省中心回收终端总数
	 * @author 岩 
	 * @date 2016-4-26
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCountFor00(String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount from  tab_gw_recoverlist");
		psql.append(" where  revert_time>=? and revert_time<=? and city_id <> null and is_recover = 1 ");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report" );
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取回收终端总数
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCount(String cityId, String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount  from  tab_gw_recoverlist");
		psql.append(" where revert_time>=? and revert_time<=? and is_recover = 1 ");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		psql.setString(3, cityId);
		psql.setString(4, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report" );
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 导出详细清单
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseDetail(String cityId, String starttime, String endtime )
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id,is_used,username,orgname,area,device_serial_A,spec_name_A, ");
		psql.append(" serv_no,bill_no,inst_addr,install_person,group_name,revert_time,net_account ");
		psql.append(" from  tab_gw_recoverlist ");
		psql.append(" where 1=1 and city_id <> null and is_recover =1 ");
		if ((!StringUtil.IsEmpty(starttime)))
		{
			psql.append("   and revert_time>=");
			psql.append(starttime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(endtime)))
		{
			psql.append("   and revert_time<=");
			psql.append(endtime);
			psql.append(" ");
		}

		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id='");
			psql.append(cityId);
			psql.append("' or parent_id='");
			psql.append(cityId);
			psql.append("') ");
		}
		psql.append(" order by revert_time");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city", Global.G_CityId_CityName_Map.get(StringUtil
						.getStringValue(rs.getString("city_id"))));
				// 核销方式
				if ("1".equals(StringUtil.getStringValue(rs.getString("is_used"))))
				{
					map.put("is_used", "已使用");
				}
				else
				{
					map.put("is_used", "未使用");
				}
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("orgname", StringUtil.getStringValue(rs.getString("orgname")));
				map.put("area", StringUtil.getStringValue(rs.getString("area")));
				map.put("device_serialnumber_A", StringUtil.getStringValue(rs.getString("device_serial_A")));
				map.put("spec_name_A", StringUtil.getStringValue(rs.getString("spec_name_A")));
				
				map.put("serv_no", StringUtil.getStringValue(rs.getString("serv_no")));
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				map.put("inst_addr", StringUtil.getStringValue(rs.getString("inst_addr")));
				map.put("install_person", StringUtil.getStringValue(rs.getString("install_person")));
				map.put("group_name", StringUtil.getStringValue(rs.getString("group_name")));
				// 核销日期
				if (StringUtil.getLongValue(rs.getString("revert_time")) * 1000l == 0)
				{
					map.put("revert_time", "");
				}
				else
				{
					DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("revert_time")) * 1000l);
					map.put("revert_time", dt.getLongDate());
				}
				map.put("net_account", StringUtil.getStringValue(rs.getString("net_account")));

				return map;
			}
		});
		return list;
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
