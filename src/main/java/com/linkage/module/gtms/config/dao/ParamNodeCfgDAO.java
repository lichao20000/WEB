
package com.linkage.module.gtms.config.dao;

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
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class ParamNodeCfgDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ParamNodeCfgDAO.class);

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> querySingleDeviceList(String deviceIds, String gwType)
	{
		logger.debug("ParamNodeCfgDAO=>querySingleDeviceList({},{})", new Object[] {
				deviceIds, gwType });
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		String tableName = "tab_hgwcustomer";
		if (!StringUtil.IsEmpty(gwType) && "2".equals(gwType))
		{
			tableName = "tab_egwcustomer";
		}
		sql.append("select a.device_id,a.device_model_id,a.vendor_id,a.device_serialnumber,"
				+ "a.city_id,a.loopback_ip,b.hardwareversion,b.softwareversion,c.username,d.vlanid,"
				+ "d.username as kdname,e.protocol,e.voip_phone,f.prox_serv,f.prox_port,"
				+ "f.stand_prox_serv,f.stand_prox_port from tab_gw_device a left join "
				+ "tab_devicetype_info b on a.devicetype_id = b.devicetype_id left join ");
		sql.append(tableName);
		sql.append(" c on a.device_id = c.device_id left join hgwcust_serv_info d "
				+ "on c.username = d.username left join tab_voip_serv_param e "
				+ "on c.user_id = e.user_id left join tab_sip_info f on e.sip_id = f.sip_id where 1 = 1 ");
		if (null != deviceIds && !"".equals(deviceIds))
		{
			sql.append("and a.device_id = '");
			sql.append(deviceIds);
			sql.append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		final Map<String, String> devModelMap = getDeviceModelMap();
		final Map<String, String> vendorlMap = getVednorMap();
		return jt.query(psql.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultMap(map, rs);
			}

			public Object resultMap(Map<String, String> map, ResultSet rs)
			{
				try
				{
					map.put("device_id", rs.getString("device_id"));
					map.put("device_model", devModelMap.get(StringUtil.getStringValue(rs
							.getString("device_model_id"))));
					map.put("vendor_name", vendorlMap.get(StringUtil.getStringValue(rs
							.getString("vendor_id"))));
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
					map.put("hardwareversion", rs.getString("hardwareversion"));
					map.put("softwareversion", rs.getString("softwareversion"));
					map.put("username", rs.getString("username"));
					map.put("city_name", StringUtil.getStringValue(CityDAO.getCityName(rs
							.getString("city_id"))));
					map.put("vlanid", rs.getString("vlanid"));
					if ("0".equals(rs.getString("protocol")))
					{
						map.put("protocol", "IMS SIP");
					}
					else if ("1".equals(rs.getString("protocol")))
					{
						map.put("protocol", "软交换 SIP");
					}
					else if ("2".equals(rs.getString("protocol")))
					{
						map.put("protocol", "H248");
					}
					else
					{
						map.put("protocol", "IMS H248");
					}
					map.put("vlanid", rs.getString("vlanid"));
					map.put("prox_serv", rs.getString("prox_serv"));
					map.put("prox_port", rs.getString("prox_port"));
					map.put("stand_prox_serv", rs.getString("stand_prox_serv"));
					map.put("stand_prox_port", rs.getString("stand_prox_port"));
				}
				catch (SQLException e)
				{
					logger.error(e.getMessage());
				}
				return map;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getNodeTypeList()
	{
		logger.debug("ParamNodeCfgDAO=>getNodeTypeList()");
		PrepareSQL psql = new PrepareSQL(
				"select distinct conf_type_id,conf_type_name from tab_conf_type ");
		return jt.queryForList(psql.getSQL());
	}

	@SuppressWarnings("rawtypes")
	public String getSelectOptiones(List<Map<String, String>> mapList, String value,
			String textarea)
	{
		logger.debug("ParamNodeCfgDAO=>getSelectOptiones({},{},{})", new Object[] {
				mapList, value, textarea });
		if (mapList == null || mapList.size() <= 0)
		{
			return "";
		}
		else
		{
			StringBuffer sb = new StringBuffer();
			int size = mapList.size();
			Map map = null;
			for (int i = 0; i < size; i++)
			{
				map = (Map) mapList.get(i);
				if (map != null && !map.isEmpty())
				{
					sb.append("<option value='");
					sb.append(map.get(value));
					sb.append("'>");
					sb.append(map.get(textarea));
					sb.append("</option>");
				}
			}
			return sb.toString();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getConfParam(int conf_type_id)
	{
		logger.debug("ParamNodeCfgDAO=>getConfParam({})", new Object[] { conf_type_id });
		StringBuffer sql = new StringBuffer();
		if(Global.AHDX.equals(Global.instAreaShortName))
		{
			sql.append("select distinct node_id,node_path,node_name, node_value_type,pre_ijk,"
							+ "input_type,type_check,remark from tab_conf_node where conf_type_id = "
							+ conf_type_id +" and rownum<=3");
		}
		else
		{
			sql.append(
					"select distinct node_id,node_path,node_name, node_value_type,pre_ijk,"
							+ "input_type,type_check,remark from tab_conf_node where conf_type_id = "
							+ conf_type_id);
		}

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.query(psql.toString(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				return resultMap(map, rs);
			}

			public Object resultMap(Map<String, String> map, ResultSet rs)
			{
				try
				{
					map.put("node_id", rs.getString("node_id"));
					map.put("node_path", rs.getString("node_path"));
					map.put("node_name", rs.getString("node_name"));
					map.put("node_value_type", rs.getString("node_value_type"));
					map.put("pre_ijk", rs.getString("pre_ijk"));
					map.put("input_type", rs.getString("input_type"));
					map.put("type_check", rs.getString("type_check"));
					map.put("remark", rs.getString("remark"));
				}
				catch (SQLException e)
				{
					logger.error(e.getMessage());
				}
				return map;
			}
		});
	}

	public int checkDevExist(String devId, int conf_type_id, List<String> nodeIdList)
	{// TODO wait (more table related)
		PrepareSQL psql = new PrepareSQL(
				"select count(*)  from tab_batch_conf_dev a,tab_conf_node b,tab_batch_conf_value c "
						+ "where a.dev_id = ? and b.conf_type_id = ? ");
		psql.setString(1, devId);
		psql.setInt(2, conf_type_id);
		String nodeIdStr = "";
		for (int i = 0; i < nodeIdList.size(); i++)
		{
			nodeIdStr += nodeIdList.get(i) + ",";
		}
		int endIndex = nodeIdStr.lastIndexOf(",");
		nodeIdStr = nodeIdStr.substring(0, endIndex);
		psql.append(" and a.status = 1 and c.node_id in (" + nodeIdStr + ")");
		psql.append(" and a.task_id = c.task_id and b.node_id= c.node_id");
		return jt.queryForInt(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getCheckVal(String nodeCheckId)
	{
		PrepareSQL psql = new PrepareSQL(
				"select type_check from tab_conf_node where node_id = ? ");
		psql.setString(1, nodeCheckId);
		return jt.queryForList(psql.getSQL());
	}

	public String saveTask(long taskId, long userId, long currTime, int conf_type_id,
			String do_type)
	{
		PrepareSQL psql = new PrepareSQL("insert into tab_batch_conf_task("
				+ "task_id,acc_oid,add_time,conf_type_id,status,do_type,query_method,"
				+ "query_method_param,remark) values(?,?,?,?,?,?,?,?,?) ");
		psql.setLong(1, taskId);
		psql.setLong(2, userId);
		psql.setLong(3, currTime);
		psql.setInt(4, conf_type_id);
		psql.setInt(5, 0);
		psql.setInt(6, 0);
		if ("1".equals(do_type))
		{
			psql.setInt(7, 1);
		}
		else if ("2".equals(do_type))
		{
			psql.setInt(7, 2);
		}
		else
		{
			psql.setInt(7, 3);
		}
		psql.setString(8, "unknown");
		psql.setString(9, "");
		return psql.getSQL();
	}

	public String saveConfig(long taskId, int node_id, int conf_type_id, String node_value)
	{
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_batch_conf_value(task_id,conf_type_id,node_id,"
						+ "node_value) values(?,?,?,?) ");
		psql.setLong(1, taskId);
		psql.setInt(2, conf_type_id);
		psql.setInt(3, node_id);
		psql.setString(4, node_value);
		return psql.getSQL();
	}

	public String saveDev(long taskId, long currTime, String device_serialnumber,
			String deviceIds)
	{
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_batch_conf_dev(task_id,dev_id,dev_sn,remark) values(?,?,?,?)");
		psql.setLong(1, taskId);
		psql.setString(2, deviceIds);
		psql.setString(3, device_serialnumber);
		psql.setString(4, "");
		return psql.getSQL();
	}

	public String saveImportTask(long taskId, long userId, long currTime,
			int conf_type_id, String remark, String filePath)
	{
		PrepareSQL psql = new PrepareSQL("insert into tab_batch_conf_task("
				+ "task_id,acc_oid,add_time,conf_type_id,status,do_type,query_method,"
				+ "query_method_param,remark) values(?,?,?,?,?,?,?,?,?) ");
		psql.setLong(1, taskId);
		psql.setLong(2, userId);
		psql.setLong(3, currTime);
		psql.setInt(4, conf_type_id);
		psql.setInt(5, 0);
		psql.setInt(6, 0);
		psql.setInt(7, 2);
		psql.setString(8, filePath);
		psql.setString(9, "");
		return psql.getSQL();
	}

	public String doSeniorConfig(long taskId, long currTime, int conf_type_id,
			long userId, String cityId, String onlineStatus, String vendorId,
			String deviceModelId, String devicetypeId, String bindType,
			String device_serialnumber)
	{
		PrepareSQL psql = new PrepareSQL("insert into tab_batch_conf_task("
				+ "task_id,acc_oid,add_time,conf_type_id,status,do_type,query_method,"
				+ "query_method_param,remark) values(?,?,?,?,?,?,?,?,?) ");
		psql.setLong(1, taskId);
		psql.setLong(2, userId);
		psql.setLong(3, currTime);
		psql.setInt(4, conf_type_id);
		psql.setInt(5, 0);
		psql.setInt(6, 0);
		psql.setInt(7, 1);
		StringBuffer sql = new StringBuffer();// TODO wait (more table related)
		sql.append("select a.device_id,a.device_serialnumber "
				+ "from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d,"
				+ "gw_devicestatus e where 1 = 1 ");
		if (!StringUtil.IsEmpty(cityId) && !"-1".equals(cityId))
		{
			ArrayList<String> cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String cityStr = cityId + "','" + StringUtils.weave(cityArray, "','");
			sql.append("and a.city_id in ('" + cityStr + "') ");
		}
		if (!StringUtil.IsEmpty(onlineStatus) && !"-1".equals(onlineStatus))
		{
			sql.append(" and a.device_status = " + onlineStatus);
		}
		if (!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId))
		{
			sql.append(" and b.vendor_id = '" + vendorId + "'");
		}
		if (!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId))
		{
			sql.append(" and c.device_model_id = '" + deviceModelId + "'");
		}
		if (!StringUtil.IsEmpty(devicetypeId) && !"-1".equals(devicetypeId))
		{
			sql.append(" and d.devicetype_id = " + devicetypeId);
		}
		if (!StringUtil.IsEmpty(bindType) && !"-1".equals(bindType))
		{
			sql.append(" and a.cpe_allocatedstatus = " + bindType);
		}
		if (!StringUtil.IsEmpty(device_serialnumber) && !"".equals(device_serialnumber))
		{
			String sub = device_serialnumber.substring(device_serialnumber.length() - 6);
			sql.append(" and a.device_serialnumber like '%" + device_serialnumber
					+ "' and a.dev_sub_sn = '" + sub + "'");
		}
		sql.append(" and a.vendor_id = b.vendor_id and a.device_model_id = c.device_model_id "
				+ "and a.devicetype_id = d.devicetype_id and a.device_id = e.device_id ");
		psql.setString(8, sql.toString());
		psql.setString(9, "");
		return psql.getSQL();
	}

	/**
	 * 获取设备型号map
	 *
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDeviceModelMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select device_model_id,device_model from gw_device_model ");
		Map<String, String> map = null;
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++)
			{
				map.put(StringUtil.getStringValue(list.get(i), "device_model_id"),
						StringUtil.getStringValue(list.get(i), "device_model"));
			}
		}
		return map;
	}

	/**
	 * 获取设备厂商map
	 *
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getVednorMap()
	{
		PrepareSQL psql = new PrepareSQL(
				"select vendor_id,vendor_name, vendor_add from tab_vendor ");
		Map<String, String> map = null;
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++)
			{
				String vendor_add = StringUtil.getStringValue(list.get(i), "vendor_add");
				if (false == StringUtil.IsEmpty(vendor_add))
				{
					map.put(StringUtil.getStringValue(list.get(i), "vendor_id"),
							vendor_add);
				}
				else
				{
					map.put(StringUtil.getStringValue(list.get(i), "vendor_id"),
							StringUtil.getStringValue(list.get(i), "vendor_name"));
				}
			}
		}
		return map;
	}
}
