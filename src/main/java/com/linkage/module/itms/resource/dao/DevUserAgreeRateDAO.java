
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 回收终端使用率
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-26
 * @category com.linkage.module.itms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevUserAgreeRateDAO extends SuperDAO
{

	private int queryCount;

	/**
	 * 获取终端和用户规格匹配数
	 * @author 岩 
	 * @date 2016-5-10
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param userSpecId
	 * @param deviceSpecId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevAgreeCount(String cityId, String starttime,
			String endtime, String userSpecId, String deviceSpecId)
	{
		PrepareSQL psql = new PrepareSQL();
		List<HashMap<String, String>> userSpecList = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> deviceSpecList = new ArrayList<HashMap<String, String>>();
		
		
		psql.append("select city_id, count(1) agreecount  from ( ");
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append(" select a.city_id,a.dealdate,a.spec_id userspec_id,a.device_id,a.blue_status,b.devicetype_id from ( ");
			psql.append(" select m.city_id,m.dealdate,m.spec_id,m.device_id,n.blue_status ");
			psql.append(" from tab_hgwcustomer m,tab_bss_dev_port_bak n where device_id <> null ");
			psql.append(" and m.spec_id = n.id and n.blue_status<>null");
		}else{
			psql.append(" select a.city_id,a.dealdate,a.spec_id userspec_id,a.device_id,b.devicetype_id from ( ");
			psql.append(" select * from tab_hgwcustomer where device_id <> null ");
		}

		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id= '");
			psql.append(cityId);
			psql.append("' or ");
			psql.append("parent_id= '");
			psql.append(cityId);
			psql.append("') ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and dealdate >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and dealdate <= ");
			psql.append(endtime);
		}
		if ((!StringUtil.IsEmpty(userSpecId)) && (!"-1".equals(userSpecId)))
		{
			userSpecList = getOwnSpecId(userSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
			
		}else if ((!StringUtil.IsEmpty(userSpecId)) && ("-1".equals(userSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			userSpecList = getOtherSpecId(deviceSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		psql.append(" ) a left join tab_gw_device b on a.device_id =b.device_id ) c, ");
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append(" ( select p.spec_id,p.devicetype_id,q.blue_status from tab_devicetype_info p,tab_bss_dev_port_bak q ");
			psql.append(" where p.spec_id = q.id and q.blue_status<>null ) d");
			psql.append(" where  c.devicetype_id =d.devicetype_id and d.blue_status<>c.blue_status");
		}else{
			psql.append(" tab_devicetype_info d where  c.devicetype_id =d.devicetype_id  ");
		}
		


		if ((!StringUtil.IsEmpty(deviceSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			deviceSpecList = getOwnSpecId(deviceSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}else if ((!StringUtil.IsEmpty(deviceSpecId)) && ("-1".equals(deviceSpecId)) && (!"-1".equals(userSpecId)))
		{
			deviceSpecList = getOtherSpecId(userSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		psql.append(" group by city_id  order by c.dealdate desc");
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");// 
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取省中心终端和用户规格匹配数
	 * @author 岩 
	 * @date 2016-5-10
	 * @param starttime
	 * @param endtime
	 * @param userSpecId
	 * @param deviceSpecId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDevAgreeCountFor00(String starttime, String endtime,
			String userSpecId, String deviceSpecId)
	{
		PrepareSQL psql = new PrepareSQL();
		List<HashMap<String, String>> userSpecList = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> deviceSpecList = new ArrayList<HashMap<String, String>>();
		
		psql.append("select city_id, count(1) agreecount  from ( ");
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append(" select a.city_id,a.dealdate,a.spec_id userspec_id,a.device_id,a.blue_status,b.devicetype_id from ( ");
			psql.append(" select m.city_id,m.dealdate,m.spec_id,m.device_id,n.blue_status ");
			psql.append(" from tab_hgwcustomer m,tab_bss_dev_port_bak n where device_id <> null ");
			psql.append(" and m.spec_id = n.id and n.blue_status<>null");
		}else{
			psql.append(" select a.city_id,a.dealdate,a.spec_id userspec_id,a.device_id,b.devicetype_id from ( ");
			psql.append(" select * from tab_hgwcustomer where device_id <> null ");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			psql.append("   and dealdate >= ");
			psql.append(starttime);
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			psql.append("   and dealdate <= ");
			psql.append(endtime);
		}
		if ((!StringUtil.IsEmpty(userSpecId)) && (!"-1".equals(userSpecId)))
		{
			userSpecList = getOwnSpecId(userSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
			
		}else if ((!StringUtil.IsEmpty(userSpecId)) && ("-1".equals(userSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			userSpecList = getOtherSpecId(deviceSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		psql.append(" ) a left join tab_gw_device b on a.device_id =b.device_id ) c, ");
		
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append(" ( select p.spec_id,p.devicetype_id,q.blue_status from tab_devicetype_info p,tab_bss_dev_port_bak q ");
			psql.append(" where p.spec_id = q.id and q.blue_status<>null ) d");
			psql.append(" where  c.devicetype_id =d.devicetype_id and d.blue_status<>c.blue_status");
		}else{
			psql.append(" tab_devicetype_info d where  c.devicetype_id =d.devicetype_id  ");
		}

		if ((!StringUtil.IsEmpty(deviceSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			deviceSpecList = getOwnSpecId(deviceSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}else if ((!StringUtil.IsEmpty(deviceSpecId)) && ("-1".equals(deviceSpecId)) && (!"-1".equals(userSpecId)))
		{
			deviceSpecList = getOtherSpecId(userSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		psql.append(" group by city_id  order by c.dealdate desc");
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	
	/**
	 * 获取规格表字段
	 * @author 岩 
	 * @date 2016-5-10
	 * @return
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public Map<String, String> getSpecStatus()
//	{
//		PrepareSQL psql = new PrepareSQL();
//		psql.append("select distinct blue_status, blue_status as id  from tab_bss_dev_port_bak where blue_status<>null ");
//		
//		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
//		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
//		resultMap.putAll(map);
//		return resultMap;
//	}
	
	
	/**
	 * 获取省中心用户总数
	 * @author 岩 
	 * @date 2016-5-10
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCountFor00(String starttime, String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount from  tab_hgwcustomer");
		psql.append(" where  dealdate>=? and dealdate<=? and city_id <> null and device_id <> null");
		psql.append(" group by city_id");
		psql.setLong(1, new Long(starttime));
		psql.setLong(2, new Long(endtime));
		Map map = DBOperation.getMap(psql.getSQL(), "proxool.xml-report");
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}

	/**
	 * 获取用户总数
	 * @author 岩 
	 * @date 2016-5-10
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getTotalCount(String cityId, String starttime,
			String endtime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select  city_id, count(1) totalCount  from  tab_hgwcustomer ");
		psql.append(" where dealdate>=? and dealdate<=? and device_id <> null");
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
	 * 导出不匹配清单
	 * @author 岩 
	 * @date 2016-5-10
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param userSpecId
	 * @param deviceSpecId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> parseDetail(String cityId, String starttime, String endtime,
			String userSpecId, String deviceSpecId)
	{
		PrepareSQL psql = new PrepareSQL();
		List<HashMap<String, String>> userSpecList = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> deviceSpecList = new ArrayList<HashMap<String, String>>();
		
		psql.append("select c.user_id,c.city_id,c.username,c.userspec_id,c.device_serialnumber,d.spec_id ");
		
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append("from (  select a.user_id,a.city_id,a.username,a.dealdate,a.spec_id userspec_id," +
					" a.device_id,a.blue_status,b.devicetype_id ,b.device_serialnumber from ( ");
			psql.append(" select m.user_id,m.city_id,m.username,m.dealdate,m.spec_id,m.device_id,n.blue_status ");
			psql.append(" from tab_hgwcustomer m,tab_bss_dev_port_bak n where device_id <> null ");
			psql.append(" and m.spec_id = n.id and n.blue_status<>null and city_id<>null  ");
		}else{
			psql.append(" from (  select a.user_id,a.city_id,a.username,a.dealdate,a.spec_id userspec_id,a.device_id,b.devicetype_id ,b.device_serialnumber ");
			psql.append(" from (  select * from tab_hgwcustomer where device_id <> null and city_id<>null  ");
		}
		if ((!StringUtil.IsEmpty(starttime)))
		{
			psql.append("   and dealdate>=");
			psql.append(starttime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(endtime)))
		{
			psql.append("   and dealdate<=");
			psql.append(endtime);
			psql.append(" ");
		}
		if ((!StringUtil.IsEmpty(userSpecId)) && (!"-1".equals(userSpecId)))
		{
			userSpecList = getOwnSpecId(userSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
			
		}else if ((!StringUtil.IsEmpty(userSpecId)) && ("-1".equals(userSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			userSpecList = getOtherSpecId(deviceSpecId);
			psql.append(" and spec_id in (");
			psql.append(StringUtil.getStringValue(userSpecList.get(0).get("id")));
			for (int i =1;i<userSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(userSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		if ((!StringUtil.IsEmpty(cityId)) && (!"00".equals(cityId)))
		{
			psql.append("   and city_id in (select city_id from tab_city where city_id='");
			psql.append(cityId);
			psql.append("' or parent_id='");
			psql.append(cityId);
			psql.append("') ");
		}
		psql.append(" ) a left join tab_gw_device b on a.device_id =b.device_id ) c, ");
		
		if(("-1".equals(userSpecId)) && ("-1".equals(deviceSpecId))){
			psql.append(" ( select p.spec_id,p.devicetype_id,q.blue_status from tab_devicetype_info p,tab_bss_dev_port_bak q ");
			psql.append(" where p.spec_id = q.id and q.blue_status<>null ) d");
			psql.append(" where  c.devicetype_id =d.devicetype_id and d.blue_status<>c.blue_status");
		}else{
			psql.append(" tab_devicetype_info d where  c.devicetype_id =d.devicetype_id  ");
		}
		
		if ((!StringUtil.IsEmpty(deviceSpecId)) && (!"-1".equals(deviceSpecId)))
		{
			deviceSpecList = getOwnSpecId(deviceSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}else if ((!StringUtil.IsEmpty(deviceSpecId)) && ("-1".equals(deviceSpecId)) && (!"-1".equals(userSpecId)))
		{
			deviceSpecList = getOtherSpecId(userSpecId);
			psql.append(" and d.spec_id in (");
			psql.append(StringUtil.getStringValue(deviceSpecList.get(0).get("id")));
			for (int i =1;i<deviceSpecList.size();i++)
			{
				psql.append(","+StringUtil.getStringValue(deviceSpecList.get(i).get("id")));
			}
			psql.append(")");
		}
		psql.append(" order by c.dealdate");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				// 属地
				map.put("city", Global.G_CityId_CityName_Map.get(StringUtil
						.getStringValue(rs.getString("city_id"))));
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("broadbandaccount", StringUtil
						.getStringValue(getBroadBandAccount(rs.getString("user_id"))));
				map.put("itvaccount",
						StringUtil.getStringValue(getITVAccount(rs.getString("user_id"))));
				map.put("voipaccount",
						StringUtil.getStringValue(getVoipAccount(rs.getString("user_id"))));
				map.put("userspec_id", StringUtil.getStringValue(getSpecName(rs
						.getString("userspec_id"))));
				map.put("device_serialnumber",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("spec_id",
						StringUtil.getStringValue(getSpecName(rs.getString("spec_id"))));
				return map;
			}
		});
		return list;
	}

	/**
	 * 获取规格表字段
	 * 页面下拉框显示部分
	 * @author 岩 
	 * @date 2016-5-10
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getSpecName()
	{
		String strSQL = "select distinct blue_status, blue_status as id from tab_bss_dev_port_bak where blue_status<>null";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		// List<HashMap<String, String>> list = DBOperation.getRecords(strSQL);
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null)
		{
			list = new ArrayList<HashMap<String, String>>();
		}
		return list;
	}

	/**
	 * 根据userId获取规格
	 * @author 岩 
	 * @date 2016-5-10
	 * @param specId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getSpecName(String specId)
	{
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select gw_type, lan_num, voice_num from tab_bss_dev_port_bak  ");
		}
		else {
			psql.append("select * from tab_bss_dev_port_bak  ");
		}
		psql.append("where 1=1 and blue_status<>null ");
		if ((!StringUtil.IsEmpty(specId)))
		{
			psql.append("  and id = ");
			psql.append(specId);
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null || list.isEmpty())
		{
			list = new ArrayList<HashMap<String, String>>();
		}
		String specName = "";
		if ("2".equals(StringUtil.getStringValue(list.get(0).get("gw_type"))))
		{
			specName = "政企" + StringUtil.getStringValue(list.get(0).get("lan_num")) + "+"
					+ StringUtil.getStringValue(list.get(0).get("voice_num"));
		}
		else
		{
			specName = StringUtil.getStringValue(list.get(0).get("lan_num")) + "+"
					+ StringUtil.getStringValue(list.get(0).get("voice_num"));
		}
		return specName;
	}

	/**
	 * 根据userId获取宽带账号
	 * @author 岩 
	 * @date 2016-5-10
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getBroadBandAccount(String user_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select username,user_id,serv_type_id from hgwcust_serv_info ");
		psql.append("where 1=1 and serv_type_id=10 ");
		if ((!StringUtil.IsEmpty(user_id)))
		{
			psql.append("  and user_id = ");
			psql.append(user_id);
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		String broadBandAccount = "";
		if (list == null || list.isEmpty())
		{
			broadBandAccount = "";
		}
		else
		{
			broadBandAccount = StringUtil.getStringValue(list.get(0).get("username"));
		}
		return broadBandAccount;
	}

	/**
	 * 根据userId获取ITV账号
	 * @author 岩 
	 * @date 2016-5-10
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getITVAccount(String user_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select username,user_id,serv_type_id from hgwcust_serv_info ");
		psql.append("where 1=1 and serv_type_id=11 ");
		if ((!StringUtil.IsEmpty(user_id)))
		{
			psql.append("  and user_id = ");
			psql.append(user_id);
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		String itvAccount = "";
		if (list == null || list.isEmpty())
		{
			itvAccount = "";
		}
		else
		{
			itvAccount = StringUtil.getStringValue(list.get(0).get("username"));
		}
		return itvAccount;
	}
	
	/**
	 * 根据userId获取语音账号
	 * @author 岩 
	 * @date 2016-5-10
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getVoipAccount(String user_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select voip_phone from tab_voip_serv_param ");
		psql.append("where 1=1 ");
		if ((!StringUtil.IsEmpty(user_id)))
		{
			psql.append("  and user_id = ");
			psql.append(user_id);
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		String voipAccount = "";
		if (list == null || list.isEmpty())
		{
			voipAccount = "";
		}
		else
		{
			voipAccount = StringUtil.getStringValue(list.get(0).get("voip_phone"));
		}
		return voipAccount;
	}
	
	/**
	 * 根据blue_status获取所属的规格Id
	 * @author 岩 
	 * @date 2016-6-20
	 * @param specId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getOwnSpecId(String specId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select id from tab_bss_dev_port_bak where blue_status<>null ");
		if ((!StringUtil.IsEmpty(specId)) && (!"-1".equals(specId)))
		{
			psql.append(" and blue_status = '");
			psql.append(specId);
			psql.append("'");
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null)
		{
			list = new ArrayList<HashMap<String, String>>();
		}
		return list;
	}
	
	/**
	 * 根据blue_status获取其他规格的规格Id
	 * @author 岩 
	 * @date 2016-6-20
	 * @param specId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getOtherSpecId(String specId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select id from tab_bss_dev_port_bak where blue_status<>null ");
		if ((!StringUtil.IsEmpty(specId)) && (!"-1".equals(specId)))
		{
			psql.append(" and blue_status <> '");
			psql.append(specId);
			psql.append("'");
		}
		List<HashMap<String, String>> list = jt.queryForList(psql.getSQL());
		if (list == null)
		{
			list = new ArrayList<HashMap<String, String>>();
		}
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
