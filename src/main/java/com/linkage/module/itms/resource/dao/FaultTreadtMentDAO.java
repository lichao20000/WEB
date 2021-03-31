
package com.linkage.module.itms.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 故障处理场景
 * 
 * @author os_hanzz date 2015-06-05
 */
@SuppressWarnings("unchecked")
public class FaultTreadtMentDAO extends SuperDAO
{

	private void setDataSourceType(String isRealtimeQuery, String key)
	{
		DataSourceContextHolder.clearDBType();
		if ("false".equals(isRealtimeQuery))
		{
			String type = null;
			type = DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
					key + "ChangeDB");
			if (!StringUtil.IsEmpty(type))
			{
				logger.warn("类：" + this.getClass().getName() + "的数据源类型配置为：" + type);
				DataSourceContextHolder.setDBType(type);
			}
		}
	}

	// 日志操作
	public static Logger logger = LoggerFactory.getLogger(FaultTreadtMentDAO.class);
	private Map<String, String> bssdevMap;
	private Map<String, String> servTypeMap;
	private Map<String, String> cityMap;
	private Map<String, String> serviceCodeMap = new HashMap<String, String>();
	private Map<String, String> status_map = new HashMap<String, String>();

	public FaultTreadtMentDAO()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
	}

	/**
	 * 根据userid获取用户信息
	 * 
	 * @return
	 */
	public Map queryUserInfo(String userid)
	{
		Map map = null;
		bssdevMap = Global.G_BssDev_PortName_Map;
		PrepareSQL psql = new PrepareSQL();
		psql.append("select distinct a.username,a.spec_id,a.user_id,a.cust_type_id,a.user_type_id,a.city_id,b.type_id,c.protocol from tab_hgwcustomer a left join gw_cust_user_dev_type b on a.user_id=b.user_id left join tab_voip_serv_param c on a.user_id=c.user_id where a.user_id="
				+ userid);
		// psql.setInt(1, StringUtil.getIntegerValue(userid));
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list)
		{
			if (list.size() > 0)
			{
				// 用户规格
				String spec_id = StringUtil.getStringValue(list.get(0).get("spec_id"));
				String spec_name = bssdevMap.get(spec_id);
				// 客户类型
				String cust_type_id = StringUtil.getStringValue(list.get(0).get(
						"cust_type_id"));
				String cust_type_name = "";
				if (cust_type_id.equals("0"))
				{
					cust_type_name = "公司客户";
				}
				else if (cust_type_id.equals("1"))
				{
					cust_type_name = "网吧客户";
				}
				else if (cust_type_id.equals("2"))
				{
					cust_type_name = "个人客户";
				}
				// 用户工单来源
				String user_type_id = StringUtil.getStringValue(list.get(0).get(
						"user_type_id"));
				String user_type_name = "";
				if (user_type_id.equals("1"))
				{
					user_type_name = "现场安装";
				}
				else if (user_type_id.equals("2"))
				{
					user_type_name = "BSS工单";
				}
				else if (user_type_id.equals("3"))
				{
					user_type_name = "手工添加";
				}
				else if (user_type_id.equals("4"))
				{
					user_type_name = "BSS同步";
				}
				// 终端类型
				// String type_id = StringUtil.getStringValue(list.get(0).get("type_id"));
				// String type_name = "";
				// if (type_id.equals("1"))
				// {
				// type_name = "e8-b";
				// }
				// else
				// {
				// type_name = "e8-c";
				// }
				list.get(0).put("spec_name", spec_name);
				list.get(0).put("cust_type_name", cust_type_name);
				list.get(0).put("user_type_name", user_type_name);
				list.get(0).put(
						"type_name",
						getUserDevType(StringUtil.getStringValue(list.get(0).get(
								"user_id"))));
				// 协议类型
				String protocol = StringUtil.getStringValue(list.get(0).get("protocol"));
				if ("0".equals(protocol))
				{
					list.get(0).put("protocol", "IMS SIP");
				}
				else if ("1".equals(protocol))
				{
					list.get(0).put("protocol", "软交换 SIP");
				}
				else if ("2".equals(protocol))
				{
					list.get(0).put("protocol", "H248");
				}
				else if ("3".equals(protocol))
				{
					list.get(0).put("protocol", "IMS H248");
				}
				else
				{
					list.get(0).put("protocol", "-");
				}
				map = list.get(0);
			}
		}
		return map;
	}
	public String queryConnCondition(String device_id){
		logger.warn("FaultTreadtMentDAO->queryConnCondition()");
		String online_status = "";
		PrepareSQL psql = new PrepareSQL();
		psql.append("select online_status from gw_devicestatus where device_id = '"+device_id+"'");
		List list = jt.queryForList(psql.getSQL());
		Map map = (Map) list.get(0);
		online_status = map.get("online_status")+"";
		return online_status;
	}

	/**
	 * 根据device_id获取设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryDeviceInfo(String device_id)
	{
		Map map = null;
		bssdevMap = Global.G_BssDev_PortName_Map;
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == 3) {
			psql.append("select a.city_id, a.cpe_allocatedstatus, a.device_status, c.vendor_name,d.device_model,b.is_check,b.spec_id,b.softwareversion,b.hardwareversion ");
		}
		else {
			psql.append("select a.*,c.vendor_name,d.device_model,b.is_check,b.spec_id,b.softwareversion,b.hardwareversion ");
		}
		psql.append(" from tab_gw_device a,tab_devicetype_info b,tab_vendor c,gw_device_model d ");
		psql.append(" where a.devicetype_id=b.devicetype_id and a.vendor_id=c.vendor_id and a.device_model_id=d.device_model_id and a.device_id='"
				+ device_id + "'");
		// psql.setString(1, device_id);
		List<Map> list = jt.queryForList(psql.getSQL());
		if (null != list)
		{
			if (list.size() > 0)
			{
				map = list.get(0);
				// 属地
				String city_name = cityMap.get(StringUtil.getStringValue(map
						.get("city_id")));
				// 设备规格
				String spec_name = bssdevMap.get(StringUtil.getStringValue(map
						.get("spec_id")));
				// 绑定状态
				String cpe_allocatedstatus = "";
				if (StringUtil.getStringValue(map.get("cpe_allocatedstatus")).equals("1"))
				{
					cpe_allocatedstatus = "已绑定";
				}
				else if (StringUtil.getStringValue(map.get("cpe_allocatedstatus"))
						.equals("0"))
				{
					cpe_allocatedstatus = "未绑定";
				}
				else
				{
					cpe_allocatedstatus = "未知状态";
				}
				// 确认状态
				String device_status = "";
				if (StringUtil.getStringValue(map.get("device_status")).equals("1"))
				{
					device_status = "已确认";
				}
				else
				{
					device_status = "未确认";
				}
				// 版本是否规范
				String is_normal = "";
				if (StringUtil.getStringValue(map.get("is_check")).equals("1"))
				{
					is_normal = "是";
				}
				else
				{
					is_normal = "否";
				}
				String haveNew = haveNew(device_id);
				if (haveNew.equals("true"))
				{
					map.put("haveNew", "否");
				}
				else
				{
					map.put("haveNew", "是");
				}
				map.put("device_id", device_id);
				map.put("spec_name", spec_name);
				map.put("city_name", city_name);
				map.put("cpe_allocatedstatus", cpe_allocatedstatus);
				map.put("device_status", device_status);
				map.put("is_normal", is_normal);
			}
		}
		return map;
	}

	/**
	 * 查询工单信息
	 * 
	 * @param queryParam
	 * @param queryType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryBssInfo(String userid, String isRealtimeQuery)
	{
		List<Map> list = null;
		servTypeMap = getServType(isRealtimeQuery);
		cityMap = CityDAO.getCityIdCityNameMap();
		PrepareSQL psql = new PrepareSQL();
		if (!StringUtil.IsEmpty(userid))
		{
			psql.append(" select  a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.spec_id,b.serv_type_id, a.user_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,c.type_id from tab_hgwcustomer a left join hgwcust_serv_info b on (a.user_id=b.user_id  and b.serv_type_id not in (17)) left join gw_cust_user_dev_type c on (a.user_id=c.user_id)  where 1 = 1   and c.type_id='2' and a.user_id = "
					+ userid);
			list = jt.queryForList(psql.getSQL());
			if (null != list)
			{
				if (list.size() > 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
//						String tempserusername = StringUtil.getStringValue(list.get(i)
//								.get("serusername"));
						String serv_type_id = StringUtil.getStringValue(list.get(i).get(
								"serv_type_id"));
						// if ("14".equals(serv_type_id))
						// {
						// tempserusername =
						// StringUtil.getStringValue(list.get(i).get("voip_phone"));
						// }
						//list.get(i).put("serUsername", tempserusername);
						list.get(i).put("serv_type_id", serv_type_id);
						String serv_type_name = servTypeMap.get(StringUtil
								.getStringValue(list.get(i).get("serv_type_id")));
						String city_name = cityMap.get(list.get(i).get("city_id"));
						String wan_type = StringUtil.getStringValue(list.get(i).get(
								"wan_type"));
						if ("1".equals(wan_type))
						{
							wan_type = "桥接";
						}
						else if ("2".equals(wan_type))
						{
							wan_type = "路由";
						}
						list.get(i).put("wan_type", wan_type);
						// 用户工单来源
						String user_type_id = StringUtil.getStringValue(list.get(i).get(
								"user_type_id"));
						String user_type_name = "";
						if (user_type_id.equals("1"))
						{
							user_type_name = "现场安装";
						}
						else if (user_type_id.equals("2"))
						{
							user_type_name = "BSS工单";
						}
						else if (user_type_id.equals("3"))
						{
							user_type_name = "手工添加";
						}
						else if (user_type_id.equals("4"))
						{
							user_type_name = "BSS同步";
						}
						String open_status = StringUtil.getStringValue(list.get(i).get(
								"open_status"));
						String open_status_name = "";
						if (open_status.equals("0"))
						{
							open_status_name = "未做";
						}
						else if (open_status.equals("1"))
						{
							open_status_name = "成功";
						}
						else if (open_status.equals("-1"))
						{
							open_status_name = "失败";
						}
						long opendate = StringUtil.getLongValue(list.get(i).get(
								"opendate"));
						// 业务类型
						list.get(i).put("serv_type_name", serv_type_name);
						// 属地
						list.get(i).put("city_name", city_name);
						// 业务工单来源
						list.get(i).put("user_type_name", user_type_name);
						// 开通状态
						list.get(i).put("open_status_name", open_status_name);
						// 开通时间
						list.get(i).put("opendate",
								DateUtil.transTime(opendate, "yyyy-MM-dd HH:mm:ss"));
						// 终端类型
						String type_id = StringUtil.getStringValue(list.get(0).get(
								"type_id"));
						String type_name = "";
						if (type_id.equals("1"))
						{
							type_name = "e8-b";
						}
						else
						{
							type_name = "e8-c";
						}
						list.get(i).put("type_name", type_name);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 根据参数，参数类型获取userid，deviceid
	 * 
	 * @param queryParam
	 * @param queryType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryDeviceInfo(String queryParam, String queryType)
	//public Map<String, String> queryDeviceInfo(String queryParam, String queryType)
	{
		logger.warn("queryDeviceInfo->queryParam={}", queryParam);
		PrepareSQL psql = new PrepareSQL();
		if (queryType.equals("username"))
		{
			psql.append("select b.user_id,b.device_id,b.username,b.city_id,b.user_type_id,b.device_serialnumber,b.opendate,a.device_id_ex from tab_gw_device a right join tab_hgwcustomer b on a.device_id=b.device_id where b.username='"
					+ queryParam + "'");
			logger.warn("queryDeviceInfo->sql={}", psql.getSQL());
			// psql.setString(1, queryParam);
		}
		else if (queryType.equals("devicesn"))
		{
			String sub_sn = queryParam.substring(queryParam.length()-6);
			psql.append("select b.user_id,a.device_id,b.username,b.city_id,b.user_type_id,b.device_serialnumber,b.opendate,a.device_id_ex from tab_gw_device a right join tab_hgwcustomer b on a.device_id=b.device_id where a.device_serialnumber like '%"
					+ queryParam + "' and a.dev_sub_sn = '" + sub_sn + "'");
			// psql.setString(1, queryParam);
		}
		else if (queryType.equals("netusername") || queryType.equals("iptvsn"))
		{
			psql.append("select a.user_id,a.device_id,a.username,a.city_id,a.user_type_id,a.device_serialnumber,a.opendate,c.device_id_ex from tab_hgwcustomer a left join tab_gw_device c on a.device_id=c.device_id join hgwcust_serv_info b on a.user_id=b.user_id where b.username='"
					//psql.append("select a.user_id,a.device_id,a.username,a.city_id,a.user_type_id,a.device_serialnumber,a.opendate,c.device_id_ex from tab_hgwcustomer a,hgwcust_serv_info b,tab_gw_device c where a.user_id=b.user_id and a.device_id=c.device_id and b.username='"
					+ queryParam + "'");
			// psql.setString(1, queryParam);
			// 10代表宽带账号，11代表IPTV账号
			if (queryType.equals("netusername"))
			{
				psql.append(" and b.serv_type_id=10");
			}
			else
			{
				psql.append(" and b.serv_type_id=11");
			}
			// psql.setString(1, queryParam); 
		}
		else if (queryType.equals("telephone"))
		{
			psql.append("select a.user_id,a.device_id,a.username,a.city_id,a.user_type_id,a.device_serialnumber,a.opendate,c.device_id_ex from tab_hgwcustomer a left join tab_gw_device c on a.device_id=c.device_id join tab_voip_serv_param b on a.user_id=b.user_id  where b.voip_phone='"
			//psql.append("select a.user_id,a.device_id,a.username,a.city_id,a.user_type_id,a.device_serialnumber,a.opendate,c.device_id_ex from tab_hgwcustomer a,tab_voip_serv_param b,tab_gw_device c where a.user_id=b.user_id and a.device_id=c.device_id and b.voip_phone='"
					+ queryParam + "'");
			// psql.setString(1, queryParam);
		}
		List<Map<String,String>> list = jt.queryForList(psql.getSQL());
		return list;
	}

	public Map<String, String> getServType(String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		String sql = "select serv_type_id,serv_type_name from tab_gw_serv_type";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> servTypeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			servTypeMap.put(StringUtil.getStringValue(map.get("serv_type_id")),
					StringUtil.getStringValue(map.get("serv_type_name")).toUpperCase());
		}
		return servTypeMap;
	}

	/**
	 * 获取最新版本
	 * 
	 * @param device_id
	 * @return
	 */
	public Map<String, String> querySoftVersion(String device_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.softwarefile_id,c.softwarefile_name,d.softwareversion from tab_software_file c,tab_devicetype_info d where c.devicetype_id=d.devicetype_id and c.softwarefile_isexist = 1  and c.devicetype_id in ");
		psql.append("(select b.devicetype_id from tab_gw_device a,gw_soft_upgrade_temp_map b where a.devicetype_id=b.devicetype_id_old and b.temp_id=1 and a.device_id='"
				+ device_id + "')");
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, String> map = null;
		if (null != list)
		{
			if (list.size() > 0)
			{
				map = new HashMap<String, String>();
				String softwarefile_id = StringUtil.getStringValue(list.get(0).get(
						"softwarefile_id"));
				String softwarefile_name = StringUtil.getStringValue(list.get(0).get(
						"softwarefile_name"));
				String softwareversion = StringUtil.getStringValue(list.get(0).get(
						"softwareversion"));
				map.put("device_id", device_id);
				map.put("softwarefile_id", softwarefile_id);
				map.put("softwarefile_name", softwarefile_name);
				map.put("softwareversion", softwareversion);
			}
		}
		return map;
	}

	/**
	 * 是否有最新版本
	 * 
	 * @param device_id
	 * @return
	 */
	public String haveNew(String device_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(1) from tab_gw_device a,gw_soft_upgrade_temp_map b where a.devicetype_id=b.devicetype_id_old and b.temp_id=1 and a.device_id='"
				+ device_id + "'");
		int count = jt.queryForInt(psql.getSQL());
		if (count == 0)
		{
			return "false";
		}
		else
		{
			return "true";
		}
	}

	/**
	 * （所包含的）业务类型
	 * 
	 * @param userid
	 * @param isRealtimeQuery
	 * @return
	 */
	public Map<String, String> serList(String userid, String isRealtimeQuery)
	{
		List<Map> list = null;
		servTypeMap = getServType(isRealtimeQuery);
		Map<String, String> serMap = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.serv_type_id from tab_hgwcustomer a,hgwcust_serv_info b where a.user_id=b.user_id and a.user_id="
				+ userid);
		list = jt.queryForList(psql.getSQL());
		if (list != null)
		{
			if (list.size() > 0)
			{
				for (int i = 0; i < list.size(); i++)
				{
					String serv_type_id = StringUtil.getStringValue(list.get(i).get(
							"serv_type_id"));
					String serv_type_name = servTypeMap.get(serv_type_id);
					serMap.put(serv_type_id, serv_type_name);
				}
			}
		}
		return serMap;
	}

	public String getServiceId(String servTypeId, String servstauts, String wanType,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getServiceId({},{},{})", new Object[] { servstauts, servstauts,
				wanType });
		StringBuffer sql = new StringBuffer();
		sql.append("select service_id from tab_service where serv_type_id=")
		.append(servTypeId).append(" and oper_type_id=").append(servstauts);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map map = queryForMap(psql.getSQL());
		return map == null ? null : StringUtil.getStringValue(map.get("service_id"));
	}

	/**
	 * 查询配置信息
	 * 
	 * @author wangsenbo
	 * @date Sep 13, 2010
	 * @param
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getConfigInfo(String deviceId, String serviceId, String serUsername)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select id,service_id,start_time,end_time,status,result_id,result_desc,device_serialnumber from  "
						+ getTableName() + "  where device_id='").append(deviceId)
						.append("'  and service_id=").append(serviceId)
						.append(" and username='" + serUsername + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		// faultInfoMap = getFaultCodeMap();
		serviceCodeMap = getServiceCode();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("serviceName", serviceCodeMap.get(rs.getString("service_id")));
				map.put("deviceSN",
						StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("service_id", rs.getString("service_id"));
				map.put("start_time", DateUtil.transTime(
						StringUtil.getLongValue(rs.getString("start_time")),
						"yyyy-MM-dd HH:mm:ss"));
				map.put("end_time", DateUtil.transTime(
						StringUtil.getLongValue(rs.getString("end_time")),
						"yyyy-MM-dd HH:mm:ss"));
				map.put("result_id", rs.getString("result_id"));
				map.put("status", status_map.get(rs.getString("status")));
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs
						.getString("result_id"))) == null)
				{
					map.put("fault_reason", "");
					map.put("solutions", "");
					map.put("fault_desc", "");
				}
				else
				{
					map.put("fault_reason",
							Global.G_Fault_Map
							.get(StringUtil.getIntegerValue(rs
									.getString("result_id"))).getFaultReason());
					map.put("solutions",
							Global.G_Fault_Map
							.get(StringUtil.getIntegerValue(rs
									.getString("result_id"))).getSolutions());
					map.put("fault_desc",
							Global.G_Fault_Map
							.get(StringUtil.getIntegerValue(rs
									.getString("result_id"))).getFaultDesc());
				}
				map.put("result_desc", rs.getString("result_desc"));
				return map;
			}
		});
		return list;
	}

	/**
	 * 拆分策略表 <strategy_tabname> <serv>gw_serv_strategy_serv</serv>
	 * <soft>gw_serv_strategy_soft</soft> <batch>gw_serv_strategy_batch</batch>
	 * <strategy>gw_serv_strategy</strategy> </strategy_tabname>
	 * 
	 * @return
	 */
	private String getTableName()
	{
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname");
		if (StringUtil.IsEmpty(tableName))
		{
			tableName = "gw_serv_strategy";
		}
		return tableName;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getServiceCode()
	{
		String sql = "select distinct service_id,service_name from tab_service ";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map serviceCodeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			serviceCodeMap.put(StringUtil.getStringValue(map.get("service_id")),
					StringUtil.getStringValue(map.get("service_name")));
		}
		return serviceCodeMap;
	}

	/**
	 * 查询宽带上网工单详情信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getInternetBssSheet(String gw_type, String userId,
			String servTypeId, String serUsername, String cityId, String isRealtimeQuery)
			{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append(
					"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type"
							+ " from tab_egwcustomer a,egwcust_serv_info b,tab_egw_net_serv_param c"
							+ " where a.user_id=b.user_id and b.user_id = c.user_id and b.serv_type_id = c.serv_type_id and a.user_id=")
							.append(userId).append(" and b.serv_type_id=").append(servTypeId)
							.append(" and b.username='").append(serUsername).append("'");
		}
		else
		{
			if (LipossGlobals.inArea(Global.SDLT))
			{
				sql.append(
						"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type from tab_hgwcustomer a,hgwcust_serv_info b, tab_net_serv_param c where a.user_id=b.user_id and b.user_id=c.user_id and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId)
						.append(" and b.username='").append(serUsername).append("'");
			}
			else
			{
				// 当用户类型为家庭或者家庭政企融合时，使用家庭处理方式。
				// modify by zhangsb 2014年5月15日
				// 由于出现多宽带业务，所以tab_net_serv_param中同一个用户会有多个宽带信息 所以增加distinct 去重
				sql.append(
						"select distinct  a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type"
								+ " from tab_hgwcustomer a,hgwcust_serv_info b, tab_net_serv_param c"
								+ " where a.user_id=b.user_id and b.user_id=c.user_id and b.serv_type_id = c.serv_type_id and b.username=c.username and a.user_id=")
								.append(userId).append(" and b.serv_type_id=").append(servTypeId)
								.append(" and b.username='").append(serUsername).append("'");
			}
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		final Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		final Map<String, String> servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_name = cityMap.get(rs.getString("city_id"));
				map.put("city_name", StringUtil.IsEmpty(city_name) ? "" : city_name);
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				map.put("serv_type",
						StringUtil.IsEmpty(serv_type_id) ? "-" : servTypeMap
								.get(serv_type_id));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("account", rs.getString("account"));
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("ip_type", transIpType_1(rs.getString("ip_type")));
				map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ? "否"
						: "是");
				map.put("wan_type", transWanType(rs.getString("wan_type")));
				return map;
			}
		});
		return list;
			}

	/**
	 * 查询IPTV或宽带上网工单详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getBssSheetByServtype(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type, String isRealtimeQuery)
			{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetByServtype({},{},{})", new Object[] { cityId, userId,
				servTypeId });
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append(
					"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type"
							+ " from tab_egwcustomer a,egwcust_serv_info b"
							+ " where a.user_id=b.user_id and a.user_id=").append(userId)
							.append(" and b.serv_type_id=").append(servTypeId)
							.append(" and b.username='").append(serUsername).append("'");
		}
		else
		{
			// 当用户类型为家庭或者家庭政企融合时，使用家庭处理方式。
			sql.append(
					"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type"
							+ " from tab_hgwcustomer a,hgwcust_serv_info b"
							+ " where a.user_id=b.user_id and a.user_id=").append(userId)
							.append(" and b.serv_type_id=").append(servTypeId)
							.append(" and b.username='").append(serUsername).append("'");
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = cityMap.get(city_id);
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				map.put("serv_type",
						StringUtil.IsEmpty(serv_type_id) ? "-" : servTypeMap
								.get(serv_type_id));
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("account", rs.getString("account"));
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				map.put("passwd", rs.getString("passwd"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("serv_num", rs.getString("serv_num"));
				map.put("bind_port", dealLongPort(rs.getString("bind_port")));
				map.put("ip_type", transIpType_1(rs.getString("ip_type")));
				map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ? "否"
						: "是");
				map.put("wan_type", transWanType(rs.getString("wan_type")));
				return map;
			}
		});
		return list;
			}

	/**
	 * 查询VoIP工单详细信息
	 * 
	 * @author wangsenbo
	 * @date Sep 15, 2010
	 * @param
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getBssSheetVoIP(String cityId, String userId, String gw_type,
			String isRealtimeQuery)
			{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		PrepareSQL psql = new PrepareSQL();
		// add by zhangchy 2012-02-22
		// 增加了 IP获取方式(b.wan_type), 协议类型(c.protocol),IP地址(b.ipaddress) 等等
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			psql.append("select a.user_id, a.username, a.city_id, a.device_serialnumber, a.device_id, b.serv_type_id, b.dealdate,");
			psql.append(" b.opendate, b.open_status, b.serv_status, c.voip_phone, c.voip_username,c.voip_passwd, c.line_id,");
			psql.append(" d.prox_serv, d.prox_port,d.stand_prox_serv, d.stand_prox_port,d.regi_serv,d.regi_port,");
			psql.append(" d.stand_regi_serv,d.stand_regi_port,d.out_bound_proxy,d.out_bound_port,d.stand_out_bound_proxy,d.stand_out_bound_port,");
			psql.append(" b.wan_type, b.ipaddress, b.ipmask, b.gateway, b.adsl_ser, c.protocol,");
			psql.append(" c.reg_id_type, c.reg_id, c.voip_port, b.vlanid ");
			psql.append("  from tab_egwcustomer a  left join tab_egw_voip_serv_param c on a.user_id = c.user_id, ");
			psql.append("  egwcust_serv_info b, tab_sip_info d ");
			psql.append("  where a.user_id = b.user_id ");
			psql.append("  and c.sip_id = d.sip_id");
			psql.append("  and a.user_id = " + userId);
			psql.append("  and b.serv_type_id = 14");
		}
		else
		{
			// 当用户类型为家庭或者家庭政企融合时，使用家庭处理方式。
			psql.append("select a.user_id, a.username, a.city_id, a.device_serialnumber, a.device_id, b.serv_type_id, b.dealdate,");
			psql.append(" b.opendate, b.open_status, b.serv_status, c.voip_phone, c.voip_username,c.voip_passwd, c.line_id,");
			psql.append(" d.prox_serv, d.prox_port,d.stand_prox_serv, d.stand_prox_port,d.regi_serv,d.regi_port,");
			psql.append(" d.stand_regi_serv,d.stand_regi_port,d.out_bound_proxy,d.out_bound_port,d.stand_out_bound_proxy,d.stand_out_bound_port,");
			psql.append(" b.wan_type, b.ipaddress, b.ipmask, b.gateway, b.adsl_ser, c.protocol,");
			psql.append(" c.reg_id_type, c.reg_id, c.voip_port, b.vlanid ");
			psql.append(" from tab_hgwcustomer a left join tab_voip_serv_param c on a.user_id = c.user_id, ");
			psql.append(" hgwcust_serv_info b , tab_sip_info d");
			psql.append(" where a.user_id = b.user_id ");
			psql.append("  and c.sip_id = d.sip_id");
			psql.append("  and a.user_id = " + userId);
			psql.append("  and b.serv_type_id = 14");
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append("   and a.city_id in (");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" order by c.line_id asc");
		cityMap = CityDAO.getCityIdCityNameMap();
		servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = cityMap.get(city_id);
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				String tmp = "-";
				if (false == StringUtil.IsEmpty(serv_type_id))
				{
					tmp = servTypeMap.get(serv_type_id);
				}
				map.put("serv_type", tmp);
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("error_code", "1");// 1、正常 0：异常
				map.put("voip_phone", rs.getString("voip_phone"));
				map.put("voip_username", rs.getString("voip_username"));
				map.put("voip_passwd", rs.getString("voip_passwd"));
				map.put("line_id", rs.getString("line_id"));
				map.put("prox_serv", rs.getString("prox_serv"));
				map.put("prox_port", rs.getString("prox_port"));
				map.put("stand_prox_serv", rs.getString("stand_prox_serv"));
				map.put("stand_prox_port", rs.getString("stand_prox_port"));
				map.put("regi_serv", rs.getString("regi_serv"));
				map.put("regi_port", rs.getString("regi_port"));
				map.put("stand_regi_serv", rs.getString("stand_regi_serv"));
				map.put("stand_regi_port", rs.getString("stand_regi_port"));
				map.put("out_bound_proxy", rs.getString("out_bound_proxy"));
				map.put("out_bound_port", rs.getString("out_bound_port"));
				map.put("stand_out_bound_proxy", rs.getString("stand_out_bound_proxy"));
				map.put("stand_out_bound_port", rs.getString("stand_out_bound_port"));
				String protocol = StringUtil.getStringValue(rs.getString("protocol"));
				if ("0".equals(protocol))
				{
					map.put("protocol", "IMS SIP");
				}
				else if ("1".equals(protocol))
				{
					map.put("protocol", "软交换 SIP");
				}
				else if ("2".equals(protocol))
				{
					map.put("protocol", "H248");
				}
				else if ("3".equals(protocol))
				{
					map.put("protocol", "IMS H248");
				}
				else
				{
					map.put("protocol", "-");
				}
				if ("2".equals(protocol) && "3".equals(protocol))
				{
					map.put("error_code", "1");// 1、正常 0：异常
				}
				else
				{
					if (!StringUtil.IsEmpty(rs.getString("voip_phone"))
							&& !StringUtil.IsEmpty(rs.getString("voip_username"))
							&& !StringUtil.IsEmpty(rs.getString("voip_passwd"))
							&& !StringUtil.IsEmpty(rs.getString("line_id")))
					{
						if (!StringUtil.IsEmpty(rs.getString("prox_serv"))
								&& !StringUtil.IsEmpty(rs.getString("prox_port"))
								&& !StringUtil.IsEmpty(rs.getString("stand_prox_serv"))
								&& !StringUtil.IsEmpty(rs.getString("stand_prox_port"))
								&& !StringUtil.IsEmpty(rs.getString("regi_serv"))
								&& !StringUtil.IsEmpty(rs.getString("regi_port"))
								&& !StringUtil.IsEmpty(rs.getString("stand_regi_serv"))
								&& !StringUtil.IsEmpty(rs.getString("stand_regi_port"))
								&& !StringUtil.IsEmpty(rs.getString("out_bound_proxy"))
								&& !StringUtil.IsEmpty(rs.getString("out_bound_port"))
								&& !StringUtil.IsEmpty(rs
										.getString("stand_out_bound_proxy"))
										&& !StringUtil.IsEmpty(rs
												.getString("stand_out_bound_port")))
						{
							map.put("error_code", "1");// 1、正常 0：异常
						}
						else
						{
							map.put("error_code", "0");// 1、正常 0：异常
							map.put("error_desc", "工单里没有SIP服务器地址");
						}
					}
					else
					{
						map.put("error_code", "0");
						map.put("error_desc", "工单里没有VOIP相关业务参数");
					}
				}
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				String wan_type = StringUtil.getStringValue(rs.getString("wan_type"));
				if ("3".equals(wan_type))
				{
					map.put("wan_type", "STATIC");
				}
				else if ("4".equals(wan_type))
				{
					map.put("wan_type", "DHCP");
				}
				else
				{
					map.put("wan_type", "-");
				}
				String reg_id_type = StringUtil.getStringValue(rs
						.getString("reg_id_type"));
				if ("0".equals(reg_id_type))
				{
					map.put("reg_id_type", "IP地址");
				}
				else if ("1".equals(reg_id_type))
				{
					map.put("reg_id_type", "域名");
				}
				else if ("2".equals(reg_id_type))
				{
					map.put("reg_id_type", "设备名");
				}
				else
				{
					map.put("reg_id_type", "-");
				}
				map.put("ipaddress", StringUtil.getStringValue(rs.getString("ipaddress")));
				map.put("ipmask", StringUtil.getStringValue(rs.getString("ipmask")));
				map.put("gateway", StringUtil.getStringValue(rs.getString("gateway")));
				map.put("adsl_ser", StringUtil.getStringValue(rs.getString("adsl_ser")));
				map.put("reg_id", StringUtil.getStringValue(rs.getString("reg_id")));
				map.put("voip_phone",
						StringUtil.getStringValue(rs.getString("voip_phone")));
				map.put("voip_port", StringUtil.getStringValue(rs.getString("voip_port")));
				map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
				return map;
			}
		});
		return list;
			}

	/**
	 * 家庭网关查询用户信息
	 * 
	 * @param cityId
	 *            属地为必须，否则以省中心来处理
	 * @param username
	 *            |
	 * @param deviceSN
	 *            |username、deviceSN必须传一个，否则返回size为0的List实例 nameType "1" 用户账号 "2"业务账号
	 *            "3"Voip账号 "4"Voip电话号码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfoByITMS(String cityId, String username,
			String nameType, String deviceSN)
			{
		logger.debug(
				"UserInstReleaseDAO=>getUserInfoByITMS(cityId:{},username:{},deviceSN:{})",
				new Object[] { cityId, username, deviceSN });
		if (null == username || "".equals(username))
		{
			if (null == deviceSN || "".equals(deviceSN))
			{
				return new ArrayList<Map<String, String>>();
			}
		}
		PrepareSQL ppSQL = new PrepareSQL();
		if ("1".equals(nameType) || null == nameType)
		{
			ppSQL.setSQL(" select a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a where a.user_state='1' ");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			}
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
		}
		else if ("2".equals(nameType))
		{
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state='1' and b.serv_type_id=10");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			// if (null != username && !"".equals(username)) {
			// ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			// }
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("b.username", PrepareSQL.EQUEAL, username);
			}
		}
		else if ("3".equals(nameType))
		{
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,hgwcust_serv_info b where a.user_id=b.user_id and a.user_state='1' and b.serv_type_id=11");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			// if (null != username && !"".equals(username)) {
			// ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			// }
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("b.username", PrepareSQL.EQUEAL, username);
			}
		}
		else if ("4".equals(nameType))
		{
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state='1' ");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("b.voip_username", PrepareSQL.EQUEAL, username);
			}
		}
		else if ("5".equals(nameType))
		{
			ppSQL.setSQL(" select distinct a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a ,tab_voip_serv_param b where a.user_id=b.user_id and a.user_state='1' ");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("b.voip_phone", PrepareSQL.EQUEAL, username);
			}
		}
		else
		{
			ppSQL.setSQL(" select a.user_id,a.city_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.is_chk_bind from tab_hgwcustomer a where a.user_state='1' ");
			// if (null != cityId && !"".equals(cityId) && !"00".equals(cityId)) {
			//
			// List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			// List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			// list.addAll(list1);
			// ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			// list = null;
			// list1 = null;
			// }
			if (null != username && !"".equals(username))
			{
				ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
			}
			if (null != deviceSN && !"".equals(deviceSN))
			{
				ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE,
						deviceSN);
			}
		}
		List rs = jt.queryForList(ppSQL.toString());
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++)
		{
			Map one = (Map) rs.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("user_id", String.valueOf(one.get("user_id")));
			map.put("username", String.valueOf(one.get("username")));
			map.put("type_name", this.getUserDevType(String.valueOf(one.get("user_id"))));
			String device_id = String.valueOf(one.get("device_id"));
			String oui = String.valueOf(one.get("oui"));
			String device_serialnumber = String.valueOf(one.get("device_serialnumber"));
			if ("null".equals(device_id) || null == device_id)
			{
				device_id = "";
			}
			if ("null".equals(oui) || null == oui)
			{
				oui = "";
			}
			if ("null".equals(device_serialnumber) || null == device_serialnumber)
			{
				device_serialnumber = "";
			}
			map.put("device_id", device_id);
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name", cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", oui);
			map.put("device_serialnumber", device_serialnumber);
			String is_chk_bind = String.valueOf(one.get("is_chk_bind"));
			if ("2".equals(is_chk_bind))
			{
				map.put("is_chk_bind", "已精确验证");
			}
			else
			{
				map.put("is_chk_bind", "");
			}
			result.add(map);
		}
		cityMap = null;
		return result;
			}

	/**
	 * 商务领航查询用户信息
	 * 
	 * @param cityId
	 *            属地为必须，否则以省中心来处理
	 * @param username
	 *            |
	 * @param deviceSN
	 *            |username、deviceSN必须传一个，否则返回size为0的List实例
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfoByBBMS(String cityId, String username,
			String deviceSN)
			{
		logger.debug(
				"UserInstReleaseDAO=>getUserInfoByBBMS(cityId:{},username:{},deviceSN:{})",
				new Object[] { cityId, username, deviceSN });
		if (null == username || "".equals(username))
		{
			if (null == deviceSN || "".equals(deviceSN))
			{
				return new ArrayList<Map<String, String>>();
			}
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber,b.customer_id,b.city_id from tab_egwcustomer a,tab_customerinfo b where a.user_state='1' and a.customer_id=b.customer_id ");
		if (null != username && !"".equals(username))
		{
			ppSQL.appendAndString("a.username", PrepareSQL.EQUEAL, username);
		}
		if (null != deviceSN && !"".equals(deviceSN))
		{
			ppSQL.appendAndString("a.device_serialnumber", PrepareSQL.L_LIKE, deviceSN);
		}
		if (null != cityId && !"".equals(cityId) && !"00".equals(cityId))
		{
			List<String> list = CityDAO.getAllNextCityIdsByCityPid(cityId);
			List<String> list1 = CityDAO.getAllPcityIdByCityId(cityId);
			list.addAll(list1);
			ppSQL.append(PrepareSQL.AND, "a.city_id", list);
			list = null;
			list1 = null;
		}
		List rs = jt.queryForList(ppSQL.toString());
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < rs.size(); i++)
		{
			Map one = (Map) rs.get(i);
			Map<String, String> map = new HashMap<String, String>();
			String device_id = String.valueOf(one.get("device_id"));
			String oui = String.valueOf(one.get("oui"));
			String device_serialnumber = String.valueOf(one.get("device_serialnumber"));
			if ("null".equals(device_id) || null == device_id)
			{
				device_id = "";
			}
			if ("null".equals(oui) || null == oui)
			{
				oui = "";
			}
			if ("null".equals(device_serialnumber) || null == device_serialnumber)
			{
				device_serialnumber = "";
			}
			map.put("user_id", String.valueOf(one.get("user_id")));
			map.put("username", String.valueOf(one.get("username")));
			map.put("device_id", device_id);
			map.put("customer_id", String.valueOf(one.get("customer_id")));
			map.put("city_id", String.valueOf(one.get("city_id")));
			map.put("city_name", cityMap.get(String.valueOf(one.get("city_id"))));
			map.put("oui", oui);
			map.put("device_serialnumber", device_serialnumber);
			result.add(map);
		}
		cityMap = null;
		return result;
			}

	/**
	 * 获取用户终端类型
	 * 
	 * @param userId
	 * @return
	 */
	private String getUserDevType(String userId)
	{
		logger.debug("UserInstReleaseDAO=>getUserDevType(userId:{})",
				new Object[] { userId });
		PrepareSQL ppSQL = new PrepareSQL(
				" select b.type_name from gw_cust_user_dev_type a,gw_dev_type b where a.type_id=b.type_id and a.user_id=? ");
		ppSQL.setLong(1, Long.parseLong(userId));
		List list = jt.queryForList(ppSQL.toString());
		if (null == list || list.size() == 0 || null == list.get(0))
		{
			return "e8-b";
		}
		Map map = (Map) list.get(0);
		if (null == map.get("type_name"))
		{
			return "e8-b";
		}
		return map.get("type_name").toString();
	}

	/**
	 * 上网/iptv
	 * 
	 * @param user_id
	 * @param serv_type_id
	 * @return
	 */
	public Map<String, String> getNetIptvIssuedConfig(String user_id,
			String serv_type_id, String isRealtimeQuery, String serUsername)
			{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		Map<String, String> map_1 = new HashMap<String, String>();
		String sql_1 = "select access_style_id from  tab_hgwcustomer where user_id="
				+ user_id;
		String sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from   hgwcust_serv_info where  user_id="
				+ user_id
				+ "and serv_type_id="
				+ serv_type_id
				+ " and username='"
				+ serUsername + "'";
		PrepareSQL psq_l = new PrepareSQL(sql_1);
		Map map = jt.queryForMap(psq_l.getSQL());
		String access_style_id = "";
		if (null != map)
		{
			access_style_id = StringUtil.getStringValue(map.get("access_style_id"));
		}
		PrepareSQL psq_2 = new PrepareSQL(sql_2);
		map_1 = jt.queryForMap(psq_2.getSQL());
		map_1.put("access_style_id", access_style_id);
		logger.warn("map_1:" + map_1.toString());
		return map_1;
			}

	/**
	 * VOIP
	 * 
	 * @param user_id
	 * @param serv_type_id
	 * @return
	 */
	public Map<String, String> getVoipIssuedConfig(String user_id, String serv_type_id,
			String isRealtimeQuery, String serUsername)
			{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		Map<String, String> map_1 = new HashMap<String, String>();
		String sql_1 = "select access_style_id from  tab_hgwcustomer where user_id="
				+ user_id;
		String sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from   hgwcust_serv_info where  user_id="
				+ user_id
				+ "and serv_type_id="
				+ serv_type_id
				+ " and username ='"
				+ serUsername + "'";
		PrepareSQL psq_l = new PrepareSQL(sql_1);
		Map map = jt.queryForMap(psq_l.getSQL());
		String access_style_id = "";
		if (null != map)
		{
			access_style_id = StringUtil.getStringValue(map.get("access_style_id"));
		}
		PrepareSQL psq_2 = new PrepareSQL(sql_2);
		map_1 = jt.queryForMap(psq_2.getSQL());
		map_1.put("access_style_id", access_style_id);
		String sql_3 = "select protocol,voip_username,voip_passwd,voip_port,reg_id,reg_id_type,uri,sip_id from tab_voip_serv_param where user_id="
				+ user_id;
		PrepareSQL psql_3 = new PrepareSQL(sql_3);
		Map map_2 = jt.queryForMap(psql_3.getSQL());
		String sip_id = "";
		if (null != map_2)
		{
			sip_id = StringUtil.getStringValue(map_2.get("sip_id"));
		}
		map_1.putAll(map_2);
		if (!StringUtil.IsEmpty(sip_id))
		{
			String sql_4 = "select prox_serv,prox_port,stand_prox_serv,stand_prox_port,regi_serv,regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy from tab_sip_info where sip_id="
					+ sip_id;
			PrepareSQL psql_4 = new PrepareSQL(sql_4);
			Map map_3 = jt.queryForMap(psql_4.getSQL());
			map_1.putAll(map_3);
		}
		logger.warn("map_1:" + map_1.toString());
		return map_1;
			}

	/**
	 * 处理太长的端口字符串，进行分割换行
	 * 
	 * @param input
	 * @return
	 */
	private String dealLongPort(String input)
	{
		logger.warn("into dealLongPort({})", input);
		/**
		 * // 每隔x个字符换行 int internal = 100; // 长度比较短的，直接返回 if(input.length() <= internal)
		 * return input; // 需要分割几次 int time = input.length() / internal; // 拼接字符
		 * StringBuffer sb = new StringBuffer(); int count = 0; for(int i = 0; i < time;
		 * i++) { sb.append(input.substring(count,count + internal)); sb.append("\r");
		 * count += internal; } sb.append(input.substring(count));
		 * logger.warn("out dealLongPort({}) " + "return new str:" + sb.toString());
		 **/
		if (input != null)
			return input.replaceAll(",", "\n");
		else
			return "";
	}

	/**
	 * 1:PPPoE(桥接) 2:PPPoE(路由) 3:STATIC 4:DHCP
	 * 
	 * @param wanType
	 * @return
	 */
	private String transWanType(String wanType)
	{
		if ("1".equals(wanType))
		{
			return "PPPoE(桥接)";
		}
		else if ("2".equals(wanType))
		{
			return "PPPoE(路由)";
		}
		else if ("3".equals(wanType))
		{
			return "静态IP";
		}
		else if ("4".equals(wanType))
		{
			return "DHCP";
		}
		return "";
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	private String transServStatus(String servStatus)
	{
		if ("1".equals(servStatus))
		{
			return "开通";
		}
		else if ("2".equals(servStatus))
		{
			return "暂停";
		}
		else if ("3".equals(servStatus))
		{
			return "销户";
		}
		else
		{
			return "-";
		}
	}

	private String transIpType_1(String ipType)
	{
		String tempIpType = "";
		if ("1".equals(ipType))
		{
			tempIpType = "公网单栈";
		}
		else if ("2".equals(ipType))
		{
			tempIpType = "纯IPV6";
		}
		else if ("3".equals(ipType))
		{
			tempIpType = "公网双栈";
		}
		else if ("4".equals(ipType))
		{
			tempIpType = "DS-Lite";
		}
		else if ("6".equals(ipType))
		{
			tempIpType = "LAFT6";
		}
		else
		{
			tempIpType = "公网单栈";
		}
		return tempIpType;
	}

	public List<Map> queryDevice(String deviceinfo, String nameType, String gw_type)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_id,a.city_id,a.oui,a.device_serialnumber,a.vendor_id,a.device_model_id,a.devicetype_id,a.loopback_ip,b.vendor_add,c.device_model,d.softwareversion,a.cpe_allocatedstatus,a.device_type from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id ");
		if (!StringUtil.IsEmpty(deviceinfo))
		{
			if (nameType.equals("1"))
			{
				psql.append(" and a.device_serialnumber = '" + deviceinfo + "'");
			}
			else
			{
				psql.append(" and a.loopback_ip = '" + deviceinfo + "'");
			}
		}
		if (!StringUtil.IsEmpty(gw_type))
		{
			psql.append(" and a.gw_type=" + gw_type);
		}
		return jt.queryForList(psql.toString());
	}

	public String getCityIdByIP(String fillIP)
	{
		String sql = "select a.city_id,country from gw_ipmain a,gw_subnets b"
				+ " where a.subnet=b.subnet and a.inetmask=b.inetmask"
				+ " and a.city_id=b.subnetgrp and flowaddress<='" + fillIP + "'"
				+ " and fhighaddress>='" + fillIP + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map<String, String> map = queryForMap(sql);
		sql = null;
		if (null == map)
		{
			return "0000";
		}
		else
		{
			if (null == map.get("country")
					|| map.get("country").toString().trim().length() == 0)
			{
				if (null == map.get("city_id")
						|| map.get("city_id").toString().trim().length() == 0)
				{
					return "0000";
				}
				else
				{
					return map.get("city_id").toString().trim();
				}
			}
			else
			{
				return map.get("country").toString().trim();
			}
		}
	}

	/**
	 * 是否支持awifi开通
	 * 
	 * @param deviceId
	 * @return
	 */
	public String isAwifi(String deviceId)
	{
		String is_awifi = "0";
		Map<String, String> map = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(
				"select b.is_awifi from tab_gw_device a,tab_devicetype_info b where a.devicetype_id = b.devicetype_id"
						+ " and a.device_id = '" + deviceId + "'");
		map = jt.queryForMap(psql.getSQL());
		if (null != map)
		{
			is_awifi = StringUtil.getStringValue(map.get("is_awifi"));
			if (is_awifi.equals("1"))
			{
				is_awifi = "1";
			}
			else
			{
				is_awifi = "0";
			}
		}
		return is_awifi;
	}

	/**
	 * 执行策略
	 * 
	 * @param userId
	 * @param list
	 * @param gwType
	 * @param serviceId
	 * @param strategy_type
	 * @param vlanIdMark
	 * @param ssid
	 * @param time
	 * @param wireless_port
	 * @param buss_level
	 * @param channel
	 * @param awifi_type
	 */
	public void doConfig(long userId, List<String> list, String gwType, String serviceId,
			String strategy_type, String vlanIdMark, String ssid, long time,
			String wireless_port, String buss_level, String channel, String awifi_type)
	{
		ArrayList<String> sqllist = new ArrayList<String>();
		//
		PrepareSQL psql = new PrepareSQL(
				"insert into tab_wirelesst_task(task_id,acc_oid,add_time,service_id,vlan_id,ssid,strategy_type,wireless_port,buss_level,channel,wireless_type) values(?,?,?,?,?,?,?,?,?,?,?)");
		psql.setLong(1, time);
		psql.setLong(2, userId);
		psql.setLong(3, time);
		psql.setInt(4, StringUtil.getIntegerValue(serviceId));
		psql.setInt(5, StringUtil.getIntegerValue(vlanIdMark));
		psql.setString(6, ssid);
		psql.setString(7, strategy_type);
		psql.setInt(8, StringUtil.getIntegerValue(wireless_port));
		psql.setInt(9, StringUtil.getIntegerValue(buss_level));
		if ("".equals(channel) || null == channel)
		{
			psql.setString(10, "");
		}
		else
		{
			psql.setString(10, channel);
		}
		psql.setInt(11, StringUtil.getIntegerValue(awifi_type));
		sqllist.add(psql.getSQL());
		String deviceIds = "";
		if (null != list && list.size() > 0)
		{
			// for (String dev : list) {
			deviceIds += "'" + StringUtil.getStringValue(list.get(0)) + "'" + ",";
			// logger.warn("---hanzz---dev:{}",dev);
			// }
		}
		if (deviceIds.length() > 0)
		{
			deviceIds = deviceIds.substring(0, deviceIds.length() - 1);
		}
		logger.warn("---hanzz---deviceIds:{}", deviceIds);
		StringBuffer sb = new StringBuffer();
		sb.append("select a.device_id ,a.oui,a.device_serialnumber,b.username as loid from tab_gw_device a ,tab_hgwcustomer b ");
		sb.append(" where a.device_id = b.device_id ");
		sb.append(" and a.device_id in(");
		sb.append(deviceIds).append(")");
		PrepareSQL sql = new PrepareSQL(sb.toString());
		List<Map<String, String>> lt = jt.queryForList(sql.getSQL());
		if (null != lt && lt.size() > 0)
		{
			for (Map<String, String> map : lt)
			{
				psql = new PrepareSQL(
						" insert into tab_wirelesst_task_dev(task_id,device_id,oui,device_serialnumber,loid,result_id,status) values(?,?,?,?,?,?,?)");
				psql.setLong(1, time);
				psql.setString(2, StringUtil.getStringValue(map.get("device_id")));
				psql.setString(3, StringUtil.getStringValue(map.get("oui")));
				psql.setString(4,
						StringUtil.getStringValue(map.get("device_serialnumber")));
				psql.setString(5, StringUtil.getStringValue(map.get("loid")));
				psql.setInt(6, 0);
				psql.setInt(7, 0);
				sqllist.add(psql.getSQL());
			}
		}
		jt.batchUpdate((String[]) sqllist.toArray(new String[0]));
	}

	/**
	 * 查看用户是否存在
	 * 
	 * @param deviceId
	 * @param gwType
	 * @return
	 */
	public Map<String, String> checkUserExsists(String deviceId, String gwType)
	{
		logger.debug("checkUserExsists({},{})", deviceId, gwType);
		Map map = null;
		StringBuffer sql = new StringBuffer();
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			sql.append("select spec_id from  tab_hgwcustomer  ");
			sql.append("  where device_id='" + deviceId + "'");
		}
		else if (gwType.equals(Global.GW_TYPE_BBMS))
		{
			sql.append("select spec_id from  tab_egwcustomer   ");
			sql.append("  where device_id='").append(deviceId).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = (Map) list.get(0);
		}
		return map;
	}

	/**
	 * 获取终端规格名称
	 * 
	 * @param specId
	 * @return
	 */
	public String getSpecName(String specId)
	{
		String specName = "";
		PrepareSQL psql = new PrepareSQL(
				"select spec_name from tab_bss_dev_port where id = " + specId);
		Map<String, String> map = jt.queryForMap(psql.getSQL());
		if (null != map)
		{
			specName = StringUtil.getStringValue(map.get("spec_name"));
		}
		return specName;
	}

	/**
	 * 判断是否有关闭业务策略
	 * 
	 * @param deviceId
	 * @return
	 */
	public String isHaveStrategy(String deviceId)
	{
		logger.debug("WirelessbusinessCtrDAO-->isHaveStrategy({})",
				new Object[] { deviceId });
		String strategy = "";
		PrepareSQL psql = new PrepareSQL(
				"select id from gw_serv_strategy_batch where device_id = '" + deviceId
				+ "' and service_id = 2003 and status = 0 ");// and
		// status
		// =
		// 0
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0)
		{
			strategy = "已有关闭业务策略";
		}
		return strategy;
	}

	public String getLoid(String username, String gwType)
	{
		logger.debug("getUserByServ({},{})", username, gwType);
		String result = "";
		Map map = null;
		StringBuffer sql = new StringBuffer();
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			sql.append("select a.user_id,b.username from hgwcust_serv_info a right join tab_hgwcustomer  b on ");
			sql.append("  a.user_id = b.user_id  where a.username='").append(username)
			.append("' and a.serv_type_id=10 ");
		}
		else if (gwType.equals(Global.GW_TYPE_BBMS))
		{
			sql.append("select a.user_id,b.username from egwcust_serv_info a right join tab_hgwcustomer  b on ");
			sql.append("  a.user_id = b.user_id  where a.username='").append(username)
			.append("' and a.serv_type_id=10 ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (null != list && list.size() > 0)
		{
			map = (Map) list.get(0);
			result = StringUtil.getStringValue(map.get("username"));
		}
		return result;
	}

	public List isExists(String username, String gwType)
	{
		logger.debug("getUserByServ({},{})", username, gwType);
		StringBuffer sql = new StringBuffer();
		if (gwType.equals(Global.GW_TYPE_ITMS))
		{
			sql.append("select a.user_id,b.device_id from hgwcust_serv_info a right join tab_hgwcustomer  b on ");
			sql.append("  a.user_id = b.user_id  where a.username='").append(username)
			.append("' and a.serv_type_id=10 ");
		}
		else if (gwType.equals(Global.GW_TYPE_BBMS))
		{
			sql.append("select a.user_id,b.device_id from egwcust_serv_info a right join tab_hgwcustomer  b on ");
			sql.append("  a.user_id = b.user_id  where a.username='").append(username)
			.append("' and a.serv_type_id=10 ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
}
