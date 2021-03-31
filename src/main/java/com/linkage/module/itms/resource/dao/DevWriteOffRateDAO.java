
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年4月11日
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevWriteOffRateDAO extends SuperDAO
{

	/**
	 * 根据属地分组 获取属地及其对应的人工核销数 或者 自动核销数
	 * 
	 * @param writeOffType
	 *            核销方式
	 * @param cityId
	 *            属地ID
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getWriteOffCount(String writeOffType, String cityId,
			String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id,count(1) writeOffcount from  tab_gw_automaticlist");
		psql.append(" where  use_type = ? and use_time>=? and use_time<=?");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setInt(1, StringUtil.getIntegerValue(writeOffType));
		psql.setLong(2, new Long(starttime));
		psql.setLong(3, new Long(endtime));
		psql.setString(4, cityId);
		psql.setString(5, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取属地及其对应的人工核销数 或者 自动核销数
	 * 
	 * @param writeOffType
	 *            核销方式
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getWriteOffCountFor00(String writeOffType,
			String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id, count(1) writeOffcount  from tab_gw_automaticlist");
		psql.append(" where  use_type = ? and use_time>=? and use_time<=? and city_id <> null ");
		psql.append(" group by city_id");
		psql.setInt(1, StringUtil.getIntegerValue(writeOffType));
		psql.setLong(2, new Long(starttime));
		psql.setLong(3, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取属地及其对应的核销总数
	 * 
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCountFor00(String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount from  tab_gw_automaticlist");
		psql.append(" where  use_time>=? and use_time<=? and city_id <> null ");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取属地及其对应的核销总数
	 * 
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCount(String cityId, String starttime,
			String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount  from  tab_gw_automaticlist");
		psql.append(" where use_time>=? and use_time<=?");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		psql.setString(3, cityId);
		psql.setString(4, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据核销方式获取清单
	 * 
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @param writeOffType
	 *            核销方式
	 * @return 清单list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseDetail(String cityId, String starttime, String endtime,
			String writeOffType)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select username,bill_no,term_serial,inst_addr,install_person,group_name,use_time,use_type,city_id,spec_name_term,project_nature,orgname,area ");
		psql.append(" from  tab_gw_automaticlist ");
		psql.append(" where 1=1 and city_id <> null ");
		if ((!StringUtil.IsEmpty(starttime)))
		{
			psql.append("   and use_time>=");
			psql.append(starttime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(endtime)))
		{
			psql.append("   and use_time<=");
			psql.append(endtime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(writeOffType)))
		{
			psql.append("   and use_type=");
			psql.append(writeOffType);
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
		psql.append(" order by use_time");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city", Global.G_CityId_CityName_Map.get(StringUtil
						.getStringValue(rs.getString("city_id"))));
				// 核销方式
				if ("1".equals(StringUtil.getStringValue(rs.getString("use_type"))))
				{
					map.put("use_type", "自动核销");
				}
				else
				{
					map.put("use_type", "人工核销");
				}
				// 核销日期
				if (StringUtil.getLongValue(rs.getString("use_time")) * 1000l == 0)
				{
					map.put("use_time", "");
				}
				else
				{
					DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("use_time")) * 1000l);
					map.put("use_time", dt.getLongDate());
				}
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("group_name",
						StringUtil.getStringValue(rs.getString("group_name")));
				map.put("install_person",
						StringUtil.getStringValue(rs.getString("install_person")));
				map.put("inst_addr", StringUtil.getStringValue(rs.getString("inst_addr")));
				map.put("term_serial",
						StringUtil.getStringValue(rs.getString("term_serial")));
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				map.put("spec_name_term", StringUtil.getStringValue(rs.getString("spec_name_term")));
				map.put("project_nature", StringUtil.getStringValue(rs.getString("project_nature")));
				map.put("orgname", StringUtil.getStringValue(rs.getString("orgname")));
				map.put("area", StringUtil.getStringValue(rs.getString("area")));
				return map;
			}
		});
		return list;
	}

	/**
	 * 根据属地分组 获取更换规范的终端数(剔除测试用)
	 * 
	 * @param starttime
	 *            起始时间
	 * @param endtime
	 *            结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevChangeFor00(String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select city_id, count(1) normalCount  from tab_exchange_terminator");
		psql.append(" where  is_test = 0 and is_normal = 1 and update_time>=? and update_time<=? and city_id <> null ");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取更换终端总数(剔除测试用)
	 * 
	 * @param starttime
	 *            起始时间
	 * @param endtime
	 *            结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalChangeCountFor00(String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount from  tab_exchange_terminator");
		psql.append(" where   is_test = 0 and update_time>=? and update_time<=? and city_id <> null ");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取更换规范的终端数(剔除测试用)
	 * 
	 * @param cityId
	 *            属地ID
	 * @param starttime
	 *            起始时间
	 * @param endtime
	 *            结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevChange(String cityId, String starttime,
			String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id,count(1) normalCount from  tab_exchange_terminator");
		psql.append(" where  is_test = 0 and is_normal = 1 and update_time>=? and update_time<=?");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		psql.setString(3, cityId);
		psql.setString(4, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地分组 获取更换终端总数(剔除测试用)
	 * 
	 * @param cityId
	 *            属地ID
	 * @param starttime
	 *            起始时间
	 * @param endtime
	 *            结束时间
	 * @return map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevChangeTotalCount(String cityId, String starttime,
			String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount  from  tab_exchange_terminator");
		psql.append(" where is_test = 0 and update_time>=? and update_time<=?");
		psql.append(" and city_id in (select city_id from tab_city where city_id=? or parent_id=?) group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		psql.setString(3, cityId);
		psql.setString(4, cityId);
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 根据属地获取不规范终端清单
	 * 
	 * @param cityId
	 *            属地ID
	 * @param starttime
	 *            起始时间
	 * @param endtime
	 *            结束时间
	 * @return map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseChangeDetail(String cityId, String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select city_id, username, net_account, dev_sn_after, dev_sn_before, bind_time, update_time, serv_no, bill_no, addr, buss_type, deal_person, deal_group, orgname, area, revert_time from tab_exchange_terminator ");
		}
		else {
			psql.append("select * from tab_exchange_terminator ");
		}
		// 剔除测试loid
		psql.append(" where is_test = 0 and is_normal = 0 and city_id <> null ");
		if ((!StringUtil.IsEmpty(starttime)))
		{
			psql.append("   and update_time>=");
			psql.append(starttime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(endtime)))
		{
			psql.append("   and update_time<=");
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
		psql.append(" order by update_time");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city", Global.G_CityId_CityName_Map.get(StringUtil
						.getStringValue(rs.getString("city_id"))));
				// "用户LOID";
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				// "宽带账号";
				map.put("net_account",
						StringUtil.getStringValue(rs.getString("net_account")));
				// "绑定设备";
				map.put("dev_sn_after",
						StringUtil.getStringValue(rs.getString("dev_sn_after")));
				// "解绑设备";
				map.put("dev_sn_before",
						StringUtil.getStringValue(rs.getString("dev_sn_before")));
				DateTimeUtil dt = null;
				// "绑定时间";
				if (StringUtil.getLongValue(rs.getString("bind_time")) * 1000l == 0)
				{
					map.put("bind_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("bind_time")) * 1000l);
					map.put("bind_time", dt.getLongDate());
				}
				// "解绑时间";
				if (StringUtil.getLongValue(rs.getString("update_time")) * 1000l == 0)
				{
					map.put("update_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("update_time")) * 1000l);
					map.put("update_time", dt.getLongDate());
				}
				// "业务号码";
				map.put("serv_no", StringUtil.getStringValue(rs.getString("serv_no")));
				// "工单号";
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				// "地址";
				map.put("addr", StringUtil.getStringValue(rs.getString("addr")));
				// "业务类型";
				map.put("buss_type", StringUtil.getStringValue(rs.getString("buss_type")));
				// "处理人";
				map.put("deal_person",
						StringUtil.getStringValue(rs.getString("deal_person")));
				// "处理组";
				map.put("deal_group",
						StringUtil.getStringValue(rs.getString("deal_group")));
				// "本地网";
				map.put("orgname", StringUtil.getStringValue(rs.getString("orgname")));
				// "区域";
				map.put("area", StringUtil.getStringValue(rs.getString("area")));
				// "回单时间（竣工时间）";
				if (StringUtil.getLongValue(rs.getString("revert_time")) * 1000l == 0)
				{
					map.put("revert_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("revert_time")) * 1000l);
					map.put("revert_time", dt.getLongDate());
				}
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 根据属地获取全量终端清单
	 * @author 岩 
	 * @date 2016-8-16
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseChangeAllDetail(String cityId, String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();

		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select city_id, username, is_normal, net_account, dev_sn_after, dev_sn_before, bind_time, update_time, serv_no, bill_no, addr, buss_type, deal_person, deal_group, orgname, area, revert_time from tab_exchange_terminator ");
		}
		else {
			psql.append("select * from tab_exchange_terminator ");
		}
		// 剔除测试loid
		psql.append(" where is_test = 0  and city_id <> null ");
		if ((!StringUtil.IsEmpty(starttime)))
		{
			psql.append("   and update_time>=");
			psql.append(starttime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(endtime)))
		{
			psql.append("   and update_time<=");
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
		psql.append(" order by update_time");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city", Global.G_CityId_CityName_Map.get(StringUtil
						.getStringValue(rs.getString("city_id"))));
				// "用户LOID";
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				if (1 == StringUtil.getIntegerValue(rs.getString("is_normal"))){
					map.put("is_normal", "是");
				}else{
					map.put("is_normal", "否");
				}
				// "宽带账号";
				map.put("net_account",
						StringUtil.getStringValue(rs.getString("net_account")));
				// "绑定设备";
				map.put("dev_sn_after",
						StringUtil.getStringValue(rs.getString("dev_sn_after")));
				// "解绑设备";
				map.put("dev_sn_before",
						StringUtil.getStringValue(rs.getString("dev_sn_before")));
				DateTimeUtil dt = null;
				// "绑定时间";
				if (StringUtil.getLongValue(rs.getString("bind_time")) * 1000l == 0)
				{
					map.put("bind_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("bind_time")) * 1000l);
					map.put("bind_time", dt.getLongDate());
				}
				// "解绑时间";
				if (StringUtil.getLongValue(rs.getString("update_time")) * 1000l == 0)
				{
					map.put("update_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("update_time")) * 1000l);
					map.put("update_time", dt.getLongDate());
				}
				// "业务号码";
				map.put("serv_no", StringUtil.getStringValue(rs.getString("serv_no")));
				// "工单号";
				map.put("bill_no", StringUtil.getStringValue(rs.getString("bill_no")));
				// "地址";
				map.put("addr", StringUtil.getStringValue(rs.getString("addr")));
				// "业务类型";
				map.put("buss_type", StringUtil.getStringValue(rs.getString("buss_type")));
				// "处理人";
				map.put("deal_person",
						StringUtil.getStringValue(rs.getString("deal_person")));
				// "处理组";
				map.put("deal_group",
						StringUtil.getStringValue(rs.getString("deal_group")));
				// "本地网";
				map.put("orgname", StringUtil.getStringValue(rs.getString("orgname")));
				// "区域";
				map.put("area", StringUtil.getStringValue(rs.getString("area")));
				// "回单时间（竣工时间）";
				if (StringUtil.getLongValue(rs.getString("revert_time")) * 1000l == 0)
				{
					map.put("revert_time", "");
				}
				else
				{
					dt = new DateTimeUtil(StringUtil.getLongValue(rs
							.getString("revert_time")) * 1000l);
					map.put("revert_time", dt.getLongDate());
				}
				return map;
			}
		});
		return list;
	}
}
