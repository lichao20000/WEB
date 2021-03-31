
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.resource.dto.CustomerDTO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-8-26
 * @category com.linkage.module.lims.itv.zeroconf.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */

@SuppressWarnings({"rawtypes","unchecked"})
public class CustomerDAOImpl extends SuperDAO implements CustomerDAO
{
	//内蒙古导出报表最大行数
	private int maxmum = 50000;


	/**
	 * 根据页面查询条件，统计满足条件的记录条数，用于分页
	 *
	 * @param dto
	 * @return 返回满足页面查询条件的总记录条数
	 */
	public int countCustomer(CustomerDTO dto)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select count(*) from stb_tab_customer a left join stb_tab_gw_device b on");
		pSql.append(" a.customer_id = b.customer_id");
		pSql.append(queryCustomerCondition(dto,1));
		return jt.queryForInt(pSql.getSQL());
	}

	/**
	 * 根据页面查询条件，查询用户业务信息列表
	 *
	 * @param dto
	 * @return 返回根据列表页面分页后的列表集合
	 */

	public List<Map> queryCustomerList(CustomerDTO dto, int firstRecord, int pageSize)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.customer_id,a.cust_account,a.serv_account,a.pppoe_user,a.city_id,");
		pSql.append("a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status,b.oui");

		// 山东联通、宁夏电信 机顶盒管理的工单管理的用户业务查询页面新增mac地址展示
		String instArea = Global.instAreaShortName;
		if (Global.SDLT.equals(instArea)){
			pSql.append(",a.stbuptyle,a.cpe_mac");
		}else if(Global.JXDX.equals(instArea)){
			pSql.append(",a.cust_account");
		}else if (Global.NXDX.equals(instArea)){
			pSql.append(",a.cpe_mac ");
		}else if (Global.JLDX.equals(instArea)){
			pSql.append(",a.cust_type,a.cpe_mac,a.loid");
		}else if(Global.HBLT.equals(instArea) || Global.SXLT.equals(instArea)){
			pSql.append(",a.loid,a.order_no");
		}else if(Global.HNLT.equals(instArea)){
			pSql.append(",a.belong,a.user_grp");
		}

		pSql.append(" from stb_tab_customer a left join stb_tab_gw_device b on a.customer_id=b.customer_id ");
		pSql.append(queryCustomerCondition(dto,1));
		pSql.append(" order by a.openUserdate desc");

		List<Map> list=querySP(pSql.getSQL(), firstRecord, pageSize, new CustomerRowMapper());

		if(Global.HNLT.equals(instArea))
		{
			pSql=null;
			pSql=new PrepareSQL();
			pSql.append("select platform_id,platform_name from stb_serv_platform ");
			List<Map> platList= jt.queryForList(pSql.getSQL());
			Map<String,String> platMap=new HashMap<String,String>();
			if(platList!=null && !platList.isEmpty()){
				for(Map map:platList){
					platMap.put(StringUtil.getStringValue(map,"platform_id"),
									StringUtil.getStringValue(map,"platform_name"));
				}
			}

			pSql=null;
			pSql=new PrepareSQL();
			pSql.append("select group_id,group_name from stb_customer_group ");
			List<Map> groupList= jt.queryForList(pSql.getSQL());
			Map<String,String> groupMap=new HashMap<String,String>();
			if(groupList!=null && !groupList.isEmpty()){
				for(Map map:groupList){
					groupMap.put(StringUtil.getStringValue(map,"group_id"),
									StringUtil.getStringValue(map,"group_name"));
				}
			}

			if(list!=null && !list.isEmpty()){
				for(Map map:list){
					map.put("belongName",platMap.get(StringUtil.getStringValue(map,"belong")));
					map.put("groupName",groupMap.get(StringUtil.getStringValue(map,"user_grp")));
				}
			}
		}

		return list;
	}




	/**
	 * 根据页面查询条件，导出所有查询结果
	 *
	 * @param dto
	 */
	public List<Map> exportCustomerList(CustomerDTO dto)
	{
		String instArea = Global.instAreaShortName;
		PrepareSQL pSql = new PrepareSQL();
		if(Global.NMGDX.equals(instArea)&&DBUtil.GetDB() == 2){
			pSql.append("select top "+maxmum);
		}else{
			pSql.append("select");
		}
		pSql.append(" a.serv_account,a.pppoe_user,a.city_id,a.addressing_type,b.device_serialnumber,a.openUserdate,a.user_status");

		if (Global.SDLT.equals(instArea)){
			pSql.append(",a.stbuptyle,a.cpe_mac");
		}else if(Global.JXDX.equals(instArea)){
			pSql.append(",a.cust_account");
		}else if (Global.NXDX.equals(instArea)){
			pSql.append(",a.cpe_mac ");
		}else if (Global.JLDX.equals(instArea)){
			pSql.append(",a.cust_type,a.cpe_mac,a.loid");
		}else if(Global.HBLT.equals(instArea) || Global.SXLT.equals(instArea)){
			pSql.append(",a.loid,a.order_no");
		}else if(Global.HNLT.equals(instArea)){
			pSql.append(",a.belong,a.user_grp");
		}
		pSql.append(" from stb_tab_customer a left join stb_tab_gw_device b on a.customer_id=b.customer_id");

		pSql.append(queryCustomerCondition(dto,2));
		pSql.append(" order by a.openUserdate desc");

		List list = jt.queryForList(pSql.getSQL());
		List lis = new ArrayList();
		if (list != null)
		{
			Map<String,String> platMap=new HashMap<String,String>();
			Map<String,String> groupMap=new HashMap<String,String>();

			if(Global.HNLT.equals(instArea))
			{
				pSql=null;
				pSql=new PrepareSQL();
				pSql.append("select platform_id,platform_name from stb_serv_platform ");
				List<Map> platList= jt.queryForList(pSql.getSQL());

				if(platList!=null && !platList.isEmpty()){
					for(Map map:platList){
						platMap.put(StringUtil.getStringValue(map,"platform_id"),
										StringUtil.getStringValue(map,"platform_name"));
					}
				}

				pSql=null;
				pSql=new PrepareSQL();
				pSql.append("select group_id,group_name from stb_customer_group ");
				List<Map> groupList= jt.queryForList(pSql.getSQL());

				if(groupList!=null && !groupList.isEmpty()){
					for(Map map:groupList){
						groupMap.put(StringUtil.getStringValue(map,"group_id"),
										StringUtil.getStringValue(map,"group_name"));
					}
				}
			}

			for (int i = 0; i < list.size(); i++)
			{
				Map map = (Map) list.get(i);
				Map<String, String> result = new HashMap<String, String>();
				if (Global.JXDX.equals(instArea)){
					result.put("custAccount", StringUtil.getStringValue(map,"cust_account"));
				}
				result.put("servAccount", StringUtil.getStringValue(map, "serv_account"));
				result.put("pppoeUser", StringUtil.getStringValue(map, "pppoe_user"));
				result.put("cityName",CityDAO.getCityName(StringUtil.getStringValue(map, "city_id")));
				result.put("addressingType",StringUtil.getStringValue(map, "addressing_type"));
				if (Global.SDLT.equals(instArea))
				{
					String stbuptyleName = "";
					String stbuptyle = StringUtil.getStringValue(map, "stbuptyle");
					if ("1".equals(stbuptyle)){
						stbuptyleName = "FTTH";
					}else if ("2".equals(stbuptyle)){
						stbuptyleName = "FTTB";
					}else if ("3".equals(stbuptyle)){
						stbuptyleName = "LAN";
					}else if ("4".equals(stbuptyle)){
						stbuptyleName = "HGW";
					}
					result.put("stbuptyle", stbuptyleName);
					result.put("mac", StringUtil.getStringValue(map, "cpe_mac"));
				}

				if (Global.JLDX.equals(instArea))
				{
					String custTypeName = "";
					String custType = StringUtil.getStringValue(map, "cust_type");
					if ("4".equals(custType)){
						custTypeName = "家庭机顶盒";
					}else if ("5".equals(custType)){
						custTypeName = "政企机顶盒";
					}
					result.put("custType", custTypeName);
					result.put("mac", StringUtil.getStringValue(map, "cpe_mac"));
					result.put("loid", StringUtil.getStringValue(map, "loid"));
				}
				if(Global.SXLT.equals(instArea)){
					result.put("loid", StringUtil.getStringValue(map, "loid"));
					result.put("orderNo",StringUtil.getStringValue(map, "order_no"));
				}
				if(Global.HNLT.equals(instArea)){
					result.put("belongName",platMap.get(StringUtil.getStringValue(map,"belong")));
					result.put("groupName",groupMap.get(StringUtil.getStringValue(map,"user_grp")));
				}
				result.put("deviceSN",StringUtil.getStringValue(map, "device_serialnumber"));
				result.put("openUserdate", DateUtil.transTime(
						StringUtil.getLongValue(map, "openuserdate"),"yyyy-MM-dd HH:mm:ss"));
				result.put("userStatus",transUserStatus(StringUtil.getStringValue(map, "user_status")));
				lis.add(result);
				map = null;
				result = null;
			}
		}
		return lis;
	}

	/**
	 * <pre>
	 * 根据用户ID查询用户信息
	 * 由于一个用户可以关联多个设备，所以需要根据设备ID查询唯一信息
	 * </pre>
	 *
	 * @param customerId
	 *            用户ID，never null
	 * @param deviceId
	 *            设备ID，如果用户未关联设备，则为null
	 * @return 返回用户信息Map
	 */
	public Map queryCustomerDetail(String customerId, String deviceId, String stauts)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.customer_id,a.serv_account,a.cust_account,a.pppoe_user,a.cust_account,");
		pSql.append("a.city_id,a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status,b.oui");
		// 山东联通、宁夏电信 机顶盒管理的工单管理的用户业务查询页面新增mac地址展示
		String instArea = Global.instAreaShortName;
		if (Global.SDLT.equals(instArea))
		{
			pSql.append(",a.stbuptyle");
			pSql.append(",a.cpe_mac");
		}
		else if (Global.NXDX.equals(instArea))
		{
			pSql.append(",a.cpe_mac");
		}
		else if (Global.JLDX.equals(instArea))
		{
			pSql.append(",a.cust_type");
			pSql.append(",a.cpe_mac ");
			pSql.append(",a.loid ");
		}
		else if(Global.HBLT.equals(instArea) || Global.SXLT.equals(instArea)){
			pSql.append(",a.loid ");
			pSql.append(",a.order_no ");
		}
		pSql.append(" from stb_tab_customer a left join stb_tab_gw_device b on");
		if ("1".equals(stauts))
		{
			pSql.append(" a.customer_id = b.customer_id ");
		}
		else
		{
			pSql.append(" a.serv_account = b.serv_account ");
		}
		if (!StringUtil.IsEmpty(deviceId))
		{
			pSql.append(" and b.device_id = '");
			pSql.append(deviceId);
			pSql.append("'");
		}
		pSql.append(" where a.customer_id = ");
		pSql.append(customerId);
		return (Map) jt.queryForObject(pSql.getSQL(), new CustomerRowMapper());
	}

	public Map querySTBCustomerDetail(String serv_account, String deviceId, String stauts)
	{
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("select a.customer_id,a.serv_account,a.cust_account,a.pppoe_user,a.cust_account,");
		pSql.append("a.city_id,a.addressing_type,b.device_id,b.device_serialnumber,a.openUserdate,a.user_status,b.oui");
		// 山东联通、宁夏电信 机顶盒管理的工单管理的用户业务查询页面新增mac地址展示
		String instArea = Global.instAreaShortName;
		if (Global.SDLT.equals(instArea))
		{
			pSql.append(",a.stbuptyle");
			pSql.append(",a.cpe_mac");
		}
		else if (Global.NXDX.equals(instArea))
		{
			pSql.append(",a.cpe_mac");
		}
		else if (Global.JLDX.equals(instArea))
		{
			pSql.append(",a.cust_type");
			pSql.append(",a.cpe_mac ");
			pSql.append(",a.loid ");
		}
		else if(Global.HBLT.equals(instArea) || Global.SXLT.equals(instArea)){
			pSql.append(",a.loid ");
			pSql.append(",a.order_no ");
		}
		pSql.append(" from stb_tab_customer a left join stb_tab_gw_device b on");
		if ("1".equals(stauts))
		{
			pSql.append(" a.customer_id = b.customer_id ");
		}
		else
		{
			pSql.append(" a.serv_account = b.serv_account ");
		}
		if (!StringUtil.IsEmpty(deviceId))
		{
			pSql.append(" and b.device_id = '");
			pSql.append(deviceId);
			pSql.append("'");
		}
		pSql.append(" where a.serv_account = '");
		pSql.append(serv_account);
		pSql.append("'");
		return (Map) jt.queryForObject(pSql.getSQL(), new CustomerRowMapper());
	}
	private static class ZeroRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("serv_account", rs.getString("serv_account"));
			result.put("fail_time",
					DateUtil.transTime(rs.getLong("fail_time"), "yyyy-MM-dd HH:mm:ss"));
			result.put("device_id", rs.getString("device_id"));
			result.put("reason_desc", rs.getString("reason_desc"));
			return result;
		}
	}

	/**
	 * 根据用户关联的设备ID查询设备信息
	 *
	 * @param deviceId
	 *            设备ID，never null
	 * @return 返回包含设备信息Map
	 */
	public Map queryDeviceDetail(String deviceId)
	{
		PrepareSQL pSql = new PrepareSQL();// TODO wait (more table related)
		pSql.append("select b.loopback_ip,b.cpe_mac,f.end_time,d.vendor_name,e.device_model"
				+ " from stb_tab_gw_device b left join "+getTableName()+" f on b.device_id = f.device_id,"
				+ " stb_tab_devicetype_info c,stb_tab_vendor d, stb_gw_device_model e"
				+ " where b.devicetype_id = c.devicetype_id and b.vendor_id = d.vendor_id and c.device_model_id = e.device_model_id"
				+ " and b.device_id = ? ");
		int index = 0;
		pSql.setString(++index, deviceId);
		List result = jt.query(pSql.getSQL(), new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> result = new HashMap<String, String>();
				result.put("loopback_ip", rs.getString("loopback_ip"));
				result.put("cpe_mac", rs.getString("cpe_mac"));
				long endTime = rs.getLong("end_time");
				result.put("end_time",
						endTime > 0 ? DateUtil.transTime(endTime, "yyyy-MM-dd HH:mm:ss")
								: "");
				result.put("vendor_name", rs.getString("vendor_name"));
				result.put("device_model", rs.getString("device_model"));
				return result;
			}
		});
		return result != null && result.size() > 0 ? (Map) result.get(0) : new HashMap();
	}

	private  String getTableName(){
		String tableName = LipossGlobals.getLipossProperty("strategy_tabname.stb.serv");

		if (StringUtil.IsEmpty(tableName))
		{
			tableName = "stb_gw_serv_strategy";
			return tableName;
		}else{
			return tableName;
		}
	}
	//type:区分查询还是导出操作 1:查询 2:导出
	private String queryCustomerCondition(CustomerDTO dto,int type)
	{
		StringBuffer result = new StringBuffer();
		result.append(" where 1 = 1");
		if (!StringUtil.IsEmpty(dto.getCityId()))
		{
			if (CustomerDTO.SUBORDINATE_YES.equals(dto.getSubordinate()))
			{
				// 如果查询省中心，即查询所有区域，不拼接city_id查询条件
				if (!"00".equals(dto.getCityId()))
				{
					// 获取所有子属地
					List<String> cities = CityDAO.getAllNextCityIdsByCityPid(dto
							.getCityId());
					String weaveCities = StringUtils.weave(cities);
					if (!StringUtil.IsEmpty(weaveCities))
					{
						result.append(" and a.city_id in(").append(weaveCities)
								.append(")");
					}
				}
			}
			else
			{
				result.append(" and a.city_id='").append(dto.getCityId()).append("'");
			}
		}
		// 业务账号
		if (!StringUtil.IsEmpty(dto.getServAccount()))
		{
			result.append(" and a.serv_account = '").append(dto.getServAccount())
					.append("'");
		}
		// 接入账号
		if (!StringUtil.IsEmpty(dto.getPppoeUser()))
		{
			result.append(" and a.pppoe_user = '").append(dto.getPppoeUser()).append("'");
		}
		// 开通时间
		if (!StringUtil.IsEmpty(dto.getStartTime()))
		{
			result.append(" and a.openUserdate >= ").append(
					DateUtil.getTimeInSecond(dto.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		if (!StringUtil.IsEmpty(dto.getEndTime()))
		{
			result.append(" and a.openUserdate <= ").append(
					DateUtil.getTimeInSecond(dto.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		// 开通状态
		if (!StringUtil.IsEmpty(dto.getUserStatus()))
		{
			result.append(" and a.user_status = ").append(dto.getUserStatus());
		}
		// 接入方式
		if (!StringUtil.IsEmpty(dto.getStbuptyle()) && !("-1".equals(dto.getStbuptyle())))
		{
			result.append(" and a.stbuptyle = ").append(dto.getStbuptyle());
		}
		// 客户类型
		if (!StringUtil.IsEmpty(dto.getCustType()))
		{
			if("-1".equals(dto.getCustType()))
			{
				result.append(" and a.cust_type in(4,5) ");
			}else{
				result.append(" and a.cust_type = ").append(dto.getCustType());
			}

		}
		// 吉林电信加loid
		String instArea = Global.instAreaShortName;
		if (!StringUtil.IsEmpty(dto.getLoid()) && (Global.JLDX.equals(instArea)||Global.HBLT.equals(instArea)||Global.SXLT.equals(instArea)))
		{
			result.append(" and a.loid = '").append(dto.getLoid()).append("'");
		}

		// 2020/04/30 山西联通增加设备序列号
		if(!StringUtil.IsEmpty(dto.getDeviceSerialnumber())) {
			result.append(" and b.device_serialnumber like '%").append(dto.getDeviceSerialnumber()).append("%'");
		}

		if(2==type && Global.NMGDX.equals(instArea)){
			if(DBUtil.GetDB() == 1){
				result.append(" and rownum < "+maxmum);
			}
			else if(DBUtil.GetDB() == 3)
			{// mysql
				result.append(" limit " + maxmum);
			}
		}

		return result.toString();
	}

	private static class CustomerRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("customerId", rs.getString("customer_id"));
			result.put("servAccount", rs.getString("serv_account"));
			result.put("pppoeUser", rs.getString("pppoe_user"));
			result.put("cust_account", rs.getString("cust_account"));
			result.put("cityName", CityDAO.getCityName(rs.getString("city_id")));
			result.put("addressingType", rs.getString("addressing_type"));
			// 山东联通、宁夏电信 机顶盒管理的工单管理的用户业务查询页面新增mac地址展示
			String instArea = Global.instAreaShortName;
			if (Global.SDLT.equals(instArea))
			{
				String stbuptyleName = "";
				String stbuptyle = rs.getString("stbuptyle");
				if ("1".equals(stbuptyle))
				{
					stbuptyleName = "FTTH";
				}
				else if ("2".equals(stbuptyle))
				{
					stbuptyleName = "FTTB";
				}
				else if ("3".equals(stbuptyle))
				{
					stbuptyleName = "LAN";
				}
				else if ("4".equals(stbuptyle))
				{
					stbuptyleName = "HGW";
				}
				result.put("stbuptyle", stbuptyleName);
				result.put("mac", rs.getString("cpe_mac"));
			}
			else if (Global.NXDX.equals(instArea))
			{
				result.put("mac", rs.getString("cpe_mac"));
			}
			else if (Global.JLDX.equals(instArea))
			{
				String custTypeName = "";
				String custType = rs.getString("cust_type");
				if ("4".equals(custType))
				{
					custTypeName = "家庭机顶盒";
				}
				else if ("5".equals(custType))
				{
					custTypeName = "政企机顶盒";
				}
				result.put("custTypeInt", custType);
				result.put("custType", custTypeName);
				result.put("mac", rs.getString("cpe_mac"));
				result.put("loid", rs.getString("loid"));
			}
			else if (Global.HBLT.equals(instArea) || Global.SXLT.equals(instArea))
			{
				result.put("loid", rs.getString("loid"));
				result.put("order_no", rs.getString("order_no"));
			}else if (Global.HNLT.equals(instArea)){
				result.put("belong", rs.getString("belong"));
				result.put("user_grp", rs.getString("user_grp"));
			}
			result.put("deviceId", rs.getString("device_id"));
			result.put("deviceSN", rs.getString("device_serialnumber"));
			result.put("openUserdate",
					DateUtil.transTime(rs.getLong("openUserdate"), "yyyy-MM-dd HH:mm:ss"));
			result.put("userStatus", transUserStatus(rs.getString("user_status")));
			result.put("oui", rs.getString("oui"));
			return result;
		}
	}

	private static String transUserStatus(String userStatus)
	{
		if ("-1".equals(userStatus))
		{
			return "失败";
		}
		else if ("1".equals(userStatus))
		{
			return "成功";
		}
		return "未做";
	}

	@Override
	public List<Map> queryZeroDetail(String customerId, String deviceId)
	{
		PrepareSQL pSql = new PrepareSQL();
		if (LipossGlobals.isOracle())
		{
			pSql.append("select a.serv_account,a.device_id,b.fail_time,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=to_char(b.device_id) and b.fail_reason_id= c.reason_id ");
		}
		else if(DBUtil.GetDB() == 3)
		{// TODO wait (more table related)
			pSql.append("select a.serv_account,a.device_id,b.fail_time,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=cast(b.device_id as char) and b.fail_reason_id= c.reason_id ");
		}
		else
		{
			pSql.append("select a.serv_account,a.device_id,b.fail_time,c.reason_desc from stb_tab_gw_device a,stb_tab_zeroconfig_fail b,"
					+ "stb_tab_zeroconfig_reason c where a.device_id=convert(varchar,b.device_id) and b.fail_reason_id= c.reason_id ");
		}
		if (!StringUtil.IsEmpty(customerId))
		{
			pSql.append(" and b.customer_id= ");
			pSql.append(customerId);
		}
		if (!StringUtil.IsEmpty(deviceId))
		{
			pSql.append(" and b.device_id= ");
			pSql.append(deviceId);
		}
		return jt.query(pSql.getSQL(), new ZeroRowMapper());
	}

	@Override
	public List<Map> queryWorkDetail(String customerId)
	{
		PrepareSQL pSql = new PrepareSQL();// TODO wait (more table related)
		pSql.append("select a.serv_account,a.pppoe_user,a.browser_url1,a.browser_url2,a.cpe_mac,");
		pSql.append("a.addressing_type,a.stbuptyle,c.username from stb_tab_customer a ");
		pSql.append("left join hgwcust_serv_info b on (a.pppoe_user=b.username and b.serv_type_id=11) ");
		pSql.append("left join tab_hgwcustomer c on b.user_id=c.user_id ");
		pSql.append("where 1=1 ");
		if (!StringUtil.IsEmpty(customerId))
		{
			pSql.append("and a.customer_id= " + customerId);
		}
		return jt.query(pSql.getSQL(), new WorkRowMapper());
	}

	private static class WorkRowMapper implements RowMapper
	{

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Map<String, String> result = new HashMap<String, String>();
			result.put("serv_account", rs.getString("serv_account"));
			result.put("pppoe_user", rs.getString("pppoe_user"));
			result.put("browser_url1", rs.getString("browser_url1"));
			result.put("browser_url2", rs.getString("browser_url2"));
			String stbuptyleName = "";
			String stbuptyle = rs.getString("stbuptyle");
			if ("1".equals(stbuptyle))
			{
				stbuptyleName = "FTTH";
			}
			else if ("2".equals(stbuptyle))
			{
				stbuptyleName = "FTTB";
			}
			else if ("3".equals(stbuptyle))
			{
				stbuptyleName = "LAN";
			}
			else if ("4".equals(stbuptyle))
			{
				stbuptyleName = "HGW";
			}
			result.put("stbuptyle", stbuptyleName);
			result.put("cpe_mac", rs.getString("cpe_mac"));
			result.put("addressing_type", rs.getString("addressing_type"));
			result.put("username", rs.getString("username"));
			return result;
		}
	}

	/** 根据loid得到userId */
	@Override
	public String getUserIdByLoid(String loid, String gw_type) {
		String tableName = "tab_hgwcustomer";

		if("5".equals(gw_type)){
			tableName = "tab_egwcustomer";
		}

		PrepareSQL pSql = new PrepareSQL();

		pSql.append(" select a.user_id from "+tableName+" a ");
		pSql.append(" where a.username= '"+loid+"'");
		List list = jt.queryForList(pSql.getSQL());

		if(list==null || list.size()==0 || list.get(0)==null){
			return "";
		}
		Map map = (Map) list.get(0);
		if(map.get("user_id")==null){
			return "";
		}
		return map.get("user_id").toString();
	}
}
