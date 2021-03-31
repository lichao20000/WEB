package com.linkage.module.itms.service.dao;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceContextHolder;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.init.obj.CpeFaultcodeOBJ;
import com.linkage.module.itms.resource.enums.AHLTDevVersionTypeEnum;

@SuppressWarnings({"unchecked","rawtypes"})
public class BssSheetServDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(BssSheetServDAO.class);
	/** 上网方式业务类型对应值为10 */
	private static final String WAN_SERV_TYPE = "10";
	private Map<String, String> cityMap = null;
	private Map<String, String> bssdevMap = null;
	private Map<String, String> servTypeMap = null;
	private Map<String, String> serviceCodeMap = new HashMap<String, String>();
	private Map<String, String> status_map = new HashMap<String, String>();
	private Map<String, String> usertype_map = new HashMap<String, String>();

	public BssSheetServDAO()
	{
		status_map.put("0", "等待执行");
		status_map.put("1", "预读PVC");
		status_map.put("2", "预读绑定端");
		status_map.put("3", "预读无线");
		status_map.put("4", "业务下发");
		status_map.put("100", "执行完成");
		// usertype_map.put("1", "现场安装");
		// usertype_map.put("2", "BSS工单");
		// usertype_map.put("3", "手工添加");
		// usertype_map.put("4", "BSS同步");
		// usertype_map.put("5", "统一网元库");
	}
	
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

	/**
	 * 根据逻辑SN查询用户
	 */
	public List getUserBySN(String username, String gw_type, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getUserBySN({},{})", username, gw_type);
		String table="tab_hgwcustomer";
		if (Global.GW_TYPE_BBMS.equals(gw_type)){
			table="tab_egwcustomer";
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select user_id from "+table+" where username='"+username+"'");
		
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据宽带号码或IPTV号码查询用户
	 */
	public List getUserByServ(String username, String servstauts, String gw_type,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getUserByServ({},{},{})", username, servstauts,gw_type);
		String table="hgwcust_serv_info";
		if (Global.GW_TYPE_BBMS.equals(gw_type)){
			table="egwcust_serv_info";
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(Global.GSDX.equals(Global.instAreaShortName) && "10".equals(servstauts)){
			psql.append("select user_id from hgwcust_serv_info where username like '" + username + "%' and serv_type_id=" + servstauts);
		}else{
			psql.append("select user_id from "+table);
			psql.append(" where username='"+username+"' and serv_type_id="+servstauts);
		}
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据VoIP认证号码或VoIP电话号码查询用户
	 */
	public List getUserByVoip(String voipUsername, String voipPhone, String gw_type,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getUserByVoip({},{})", voipUsername, voipPhone);
		if (StringUtil.IsEmpty(voipUsername) && StringUtil.IsEmpty(voipPhone)){
			logger.debug("VoIP认证号码和VoIP电话号码都为空");
			return null;
		}
		
		String table="tab_voip_serv_param";
		if (Global.GW_TYPE_BBMS.equals(gw_type)){
			table="tab_egw_voip_serv_param";
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from "+table+" where 1=1");
		
		if (!StringUtil.IsEmpty(voipUsername)){
			sql.append(" and voip_username='").append(voipUsername).append("'");
		}
		
		if (!StringUtil.IsEmpty(voipPhone))
		{
			if (LipossGlobals.isXJDX()){
				sql.append(" and voip_phone like '%").append(voipPhone).append("'");
			}else{
				sql.append(" and voip_phone='").append(voipPhone).append("'");
			}
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List getUserByVoipEID(String voipEID, String gw_type, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getUserByVoipEID({})", voipEID);
		if (StringUtil.IsEmpty(voipEID) && StringUtil.IsEmpty(voipEID)){
			logger.debug("VoIPEID为空");
			return null;
		}
		
		String table="tab_voip_serv_param";
		if (Global.GW_TYPE_BBMS.equals(gw_type)){
			table="tab_egw_voip_serv_param";
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from "+table+" where 1=1");
		if (!StringUtil.IsEmpty(voipEID)){
			sql.append(" and eid='").append(voipEID).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询业务信息
	 */
	public List<Map> getBssSheetServInfo(String cityId, String startOpenDate,
			String endOpenDate, String userId, String servTypeId, String openstatus,
			String devicetype, int curPage_splitPage, int num_splitPage, String gw_type,
			String voipProtocalType, String spec_id, String cust_type_id,
			String user_type_id, String isRealtimeQuery, String wanProtocalType,String deviceVersionType)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{},{},{})", new Object[] {
					cityId, startOpenDate, endOpenDate, userId, servTypeId, openstatus,
					curPage_splitPage, num_splitPage, spec_id });
		
		String sql = getBssSheetServSQL(gw_type, servTypeId, userId, startOpenDate,
				endOpenDate, openstatus, cityId, devicetype, voipProtocalType, spec_id,
				cust_type_id, user_type_id, wanProtocalType,deviceVersionType);
		PrepareSQL psql = new PrepareSQL(sql);
		cityMap = CityDAO.getCityIdCityNameMap();
		bssdevMap = Global.G_BssDev_PortName_Map;
		servTypeMap = getServType(isRealtimeQuery);
		final String gw_typeTemp=gw_type;
		if (!LipossGlobals.inArea(Global.SDLT)){
			usertype_map = Global.G_UserTypeId_UserName_Map;
		}
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				String spec_id = rs.getString("spec_id");
				String spec_name = StringUtil.getStringValue(bssdevMap.get(spec_id));
				map.put("spec_name", spec_name);
				if (LipossGlobals.inArea(Global.SDLT))
				{
					String tempData = StringUtil.getStringValue(rs.getString("device_serialnumber"));
					if (tempData.length() > 32){
						map.put("device_serialnumber", tempData.substring(0, 26)+ "......");
					}else{
						map.put("device_serialnumber", tempData);
					}
				}
				else
				{
					map.put("device_serialnumber", rs.getString("device_serialnumber"));
				}
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				String tempserusername = rs.getString("serusername");
				if (LipossGlobals.inArea(Global.SDLT))
				{
					String tempServTypeId = rs.getString("serv_type_id");
					if ("14".equals(tempServTypeId)){
						tempserusername = rs.getString("voip_phone");
					}
				}
				
				if ( LipossGlobals.inArea(Global.JLDX))
				{
					String tempServTypeId = rs.getString("serv_type_id");
					if ("14".equals(tempServTypeId)){
						map.put("newSerUsername", rs.getString("voip_phone"));
					}
				}
				
				if ( LipossGlobals.inArea(Global.SXLT))
				{
					String tempServTypeId = rs.getString("serv_type_id");
					if ("14".equals(tempServTypeId)){
						map.put("newSerUsername", rs.getString("voip_username"));
					}
				}
				map.put("serUsername", tempserusername);
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				String tmp = "-";
				if (!StringUtil.IsEmpty(serv_type_id))
				{
					tmp = servTypeMap.get(serv_type_id);
					if (Global.GW_TYPE_BBMS.equals(gw_typeTemp))
					{
						if (LipossGlobals.inArea(Global.NMGDX) || LipossGlobals.inArea(Global.JLDX))
						{
							String real_type_id = rs.getString("real_type_id");
							if ("27".equals(real_type_id)){
								tmp = "云专线宽带";
							}
						}
						
						if (LipossGlobals.inArea(Global.AHDX))
						{
							String real_type_id = rs.getString("real_type_id");
							if ("40".equals(real_type_id) || "40".equals(serv_type_id)){
								tmp = "校园宽带";
							}
						}
					}
				}
				
				if (!LipossGlobals.inArea(Global.SDLT)){
					map.put("user_type_id",usertype_map.get(rs.getString("user_type_id")));
				}
				
				if (LipossGlobals.inArea(Global.SXLT)){
					map.put("vlanid", rs.getString("vlanid"));
				}
				map.put("serv_type", tmp);
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("serv_status", rs.getString("serv_status"));
				map.put("wan_type", rs.getString("wan_type"));
				map.put("type_id", rs.getString("type_id"));
				map.put("type_name",StringUtil.IsEmpty(rs.getString("type_id")) ? 
						(LipossGlobals.inOperator("CUC") ? "hgu" : "e8-c") : 
							(StringUtil.IsEmpty(com.linkage.litms.Global.G_DeviceTypeID_Name_Map.get(rs.getString("type_id"))) ? 
									"e8-c" : com.linkage.litms.Global.G_DeviceTypeID_Name_Map.get(rs.getString("type_id"))));
				if ("11".equals(rs.getString("serv_type_id")) 
						&& Global.CQDX.equals(Global.instAreaShortName))
				{
					map.put("real_bind_port", rs.getString("real_bind_port"));
				}
				if ( LipossGlobals.inArea(Global.AHLT))
				{
					String devVersionTypeId = rs.getString("device_version_type");
					map.put("devVersionType",StringUtil.IsEmpty(devVersionTypeId) ? "" : AHLTDevVersionTypeEnum.getNameByCode(devVersionTypeId));
					logger.warn("now map:{}",map);
				}

				return map;
			}
		});
		cityMap = null;
		return list;
	}

	private String getBssSheetServSQL(String gw_type, String servTypeId, String userId,
			String startOpenDate, String endOpenDate, String openstatus, String cityId,
			String devicetype, String voipProtocalType, String spec_id,
			String cust_type_id, String user_type_id, String wanProtocalType,String deviceVersionType)
	{
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			return getBssSheetServSQLByBBMS(userId, startOpenDate, endOpenDate,
					servTypeId, voipProtocalType, openstatus, cityId, devicetype,
					spec_id, cust_type_id, user_type_id,wanProtocalType,gw_type,deviceVersionType);
		}
			// 当用户类型为家庭或者家庭政企融合时，使用家庭逻辑处理
		return getBssSheetServSQLByITMS(servTypeId, voipProtocalType, userId,
				startOpenDate, endOpenDate, openstatus, cityId, devicetype, spec_id,
				cust_type_id, user_type_id, wanProtocalType,gw_type,deviceVersionType);
	}

	private String getBssSheetServSQLByITMS(String servTypeId, String voipProtocalType,
			String userId, String startOpenDate, String endOpenDate, String openstatus,
			String cityId, String devicetype, String spec_id, String cust_type_id,
			String user_type_id, String wanProtocalType,String gw_type,String deviceVersionType)
	{
		StringBuffer selectAndTableSql = new StringBuffer(" select ");
		if (!StringUtil.IsEmpty(voipProtocalType)
				&& Global.SERVER_TYPE_ID.equals(servTypeId))
		{
			selectAndTableSql.append(" distinct");
		}
		selectAndTableSql.append(" a.user_id,a.username,a.city_id,a.device_serialnumber,");
		selectAndTableSql.append("a.oui,a.device_id,a.spec_id,b.serv_type_id,");
		if (LipossGlobals.inArea(Global.JLDX)){
			selectAndTableSql.append(" e.voip_phone, ");
		}else if (LipossGlobals.inArea(Global.SDLT)){
			selectAndTableSql.append(" d.voip_phone, ");
		}else if (LipossGlobals.inArea(Global.SXLT)){
			selectAndTableSql.append(" b.vlanid, d.voip_username, ");
		}
		if (Global.CQDX.equals(Global.instAreaShortName)){
			selectAndTableSql.append(" a.user_type_id,b.serv_status,b.dealdate,b.opendate,");
			selectAndTableSql.append("b.open_status,b.wan_type,b.username as serUsername,b.real_bind_port,c.type_id ");
			selectAndTableSql.append("from tab_hgwcustomer a left join hgwcust_serv_info b ");
			selectAndTableSql.append("on (a.user_id=b.user_id  and b.serv_type_id not in (17)");
		}
		else
		{
			selectAndTableSql.append(" a.user_type_id,b.serv_status,b.dealdate,b.opendate,");
			selectAndTableSql.append("b.open_status,b.wan_type,b.username as serUsername,");
			selectAndTableSql.append("(case when c.type_id='1' then 'E8-B' when c.type_id='7' then '悦me' else 'E8-C' end) as type_id");
			if(Global.AHLT.equals(Global.instAreaShortName)){
				selectAndTableSql.append(",v.device_version_type");
			}
			selectAndTableSql.append(" from tab_hgwcustomer a left join hgwcust_serv_info b ");
			selectAndTableSql.append("on (a.user_id=b.user_id and b.serv_type_id not in (17)");
			
			if (!StringUtil.IsEmpty(wanProtocalType) 
					&& WAN_SERV_TYPE.equals(servTypeId)
					&& LipossGlobals.inArea(Global.JLDX))
			{
				selectAndTableSql.append(" and b.wan_type="+wanProtocalType);
			}
		}
		// 如果有用户信息，既不使用时间查询    安徽联通优化：时间筛选调整至 where子句
		if (!LipossGlobals.inArea(Global.JLDX) && !LipossGlobals.inArea(Global.AHLT) && userId == null)
		{
			if (!StringUtil.IsEmpty(startOpenDate)){
				selectAndTableSql.append(" and b.dealdate>=").append(startOpenDate);
			}
			if (!StringUtil.IsEmpty(endOpenDate)){
				selectAndTableSql.append(" and b.dealdate<=").append(endOpenDate);
			}
		}
		selectAndTableSql.append(")");
		if (LipossGlobals.inArea(Global.SDLT) || LipossGlobals.inArea(Global.SXLT)){
			selectAndTableSql.append(" left join tab_voip_serv_param d on (b.user_id = d.user_id and b.serv_type_id = 14) ");
		}
		selectAndTableSql.append(" left join gw_cust_user_dev_type c on (a.user_id=c.user_id) ");
		if (LipossGlobals.inArea(Global.JLDX)){
			selectAndTableSql.append("left join tab_voip_serv_param e on b.user_id = e.user_id");
		}
		if (!StringUtil.IsEmpty(servTypeId) 
				&& !StringUtil.IsEmpty(voipProtocalType)
				&& Global.SERVER_TYPE_ID.equals(servTypeId))
		{
			selectAndTableSql.append(" inner join tab_voip_serv_param d on (a.user_id=d.user_id and d.protocol=")
					.append(voipProtocalType).append(")");
		}
		if (Global.AHLT.equals(Global.instAreaShortName))
		{
			selectAndTableSql.append(" left join tab_gw_device d on a.device_id = d.device_id " +
					"left join tab_device_version_attribute v on d.devicetype_id = v.devicetype_id");
		}
		String whereSql = "";
		if (Global.JLDX.equals(Global.instAreaShortName))
		{
			whereSql = getBssSheetServSQLConditionJl(servTypeId, openstatus, userId,
					cityId, devicetype, spec_id, cust_type_id, user_type_id,startOpenDate,endOpenDate);
		}
		else
		{
			whereSql = getBssSheetServSQLCondition(servTypeId, openstatus, userId,
					cityId, devicetype, spec_id, cust_type_id, user_type_id,gw_type,startOpenDate,endOpenDate,deviceVersionType);
		}
		return selectAndTableSql.append(whereSql).toString();
	}

	private String getBssSheetServSQLByBBMS(String userId, String startOpenDate,
			String endOpenDate, String servTypeId, String voipProtocalType,
			String openstatus, String cityId, String devicetype, String spec_id,
			String cust_type_id, String user_type_id, String wanProtocalType,String gw_type,String deviceVersionType)
	{
		StringBuffer selectAndTableSql = new StringBuffer();
		if (Global.CQDX.equals(Global.instAreaShortName))
		{
			selectAndTableSql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.spec_id,a.user_type_id,b.serv_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,b.real_bind_port,c.type_id"
							+ " from tab_egwcustomer a left join egwcust_serv_info b on (a.user_id=b.user_id");
		}
		else if(Global.JLDX.equals(Global.instAreaShortName))
		{
			selectAndTableSql.append("select a.user_id,a.username,a.city_id,e.voip_phone,a.device_serialnumber,a.oui,a.device_id,a.spec_id,a.user_type_id,b.serv_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,"
					+ "(case when c.type_id='1' then 'E8-B' else 'E8-C' end) as type_id,b.real_type_id"
					+ " from tab_egwcustomer a left join egwcust_serv_info b on (a.user_id=b.user_id ");
					
			if (!StringUtil.IsEmpty(wanProtocalType) && WAN_SERV_TYPE.equals(servTypeId)){
				selectAndTableSql.append(" and b.wan_type = ").append(wanProtocalType).append(" ");
			}
		}
		else if (Global.NMGDX.equals(Global.instAreaShortName)) 
		{
			selectAndTableSql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.spec_id,a.user_type_id,b.serv_type_id,b.real_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,"
					+ "(case when c.type_id='1' then 'E8-B' else 'E8-C' end) as type_id"
					+ " from tab_egwcustomer a left join egwcust_serv_info b on (a.user_id=b.user_id");
		}
		else if(Global.AHDX.equals(Global.instAreaShortName))
		{
			selectAndTableSql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.spec_id,a.user_type_id,b.serv_type_id,b.real_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,"
							+ "(case when c.type_id='1' then 'E8-B' else 'E8-C' end) as type_id"
							+ " from tab_egwcustomer a left join egwcust_serv_info b on (a.user_id=b.user_id");
		}
		else
		{
			selectAndTableSql.append("select a.user_id,a.username,a.city_id,a.device_serialnumber,a.oui,a.device_id,a.spec_id,a.user_type_id,b.serv_type_id,b.serv_status,b.dealdate,b.opendate,b.open_status,b.wan_type,b.username as serUsername,"
							+ "(case when c.type_id='1' then 'E8-B' else 'E8-C' end) as type_id"
							+ " from tab_egwcustomer a left join egwcust_serv_info b on (a.user_id=b.user_id");
		}
		// 如果有用户信息，既不使用时间查询
		if (!Global.JLDX.equals(Global.instAreaShortName) 
				&& userId == null)
		{
			if (!StringUtil.IsEmpty(startOpenDate))
			{
				selectAndTableSql.append(" and b.dealdate>=").append(startOpenDate);
			}
			if (!StringUtil.IsEmpty(endOpenDate))
			{
				selectAndTableSql.append(" and b.dealdate<=").append(endOpenDate);
			}
		}
		selectAndTableSql.append(") left join gw_cust_user_dev_type c on (a.user_id=c.user_id) ");
		if (Global.JLDX.equals(Global.instAreaShortName)){
			selectAndTableSql.append("left join tab_egw_voip_serv_param e on b.user_id = e.user_id");
		}
		if (!StringUtil.IsEmpty(voipProtocalType)
				&& Global.SERVER_TYPE_ID.equals(servTypeId))
		{
			selectAndTableSql.append(" inner join tab_egw_voip_serv_param d on (a.user_id=d.user_id and d.protocol=")
					.append(voipProtocalType).append(")");
		}
		String whereSql = "";
		if (Global.JLDX.equals(Global.instAreaShortName)){
			whereSql = getBssSheetServSQLConditionJl(servTypeId, openstatus, userId,
					cityId, devicetype, spec_id, cust_type_id, user_type_id,startOpenDate,endOpenDate);
		}else{
			whereSql = getBssSheetServSQLCondition(servTypeId, openstatus, userId,
					cityId, devicetype, spec_id, cust_type_id, user_type_id,gw_type,startOpenDate,endOpenDate,deviceVersionType);
		}
		return selectAndTableSql.append(whereSql).toString();
	}

	/** 查询BSS工单业务列表时根据页面输入查询条件拼接SQL where查询条件 */
	private String getBssSheetServSQLConditionJl(String servTypeId, String openstatus,
			String userId, String cityId, String devicetype, String spec_id,
			String cust_type_id, String user_type_id,String startOpenDate, String endOpenDate)
	{
		StringBuffer where = new StringBuffer(" where 1 = 1 ");
		if (!StringUtil.IsEmpty(user_type_id) && !"-1".equals(user_type_id)){
			where.append(" and a.user_type_id='").append(user_type_id).append("'  ");
		}
		if (userId == null)
		{
			if (!StringUtil.IsEmpty(startOpenDate)){
				where.append(" and b.dealdate>=").append(startOpenDate);
			}
			if (!StringUtil.IsEmpty(endOpenDate)){
				where.append(" and b.dealdate<=").append(endOpenDate);
			}
		}
		if (!StringUtil.IsEmpty(servTypeId))
		{	
			if (servTypeId.equals("27")) {
				where.append(" and b.serv_type_id=10 and b.real_type_id=").append(servTypeId);
			}else {
				where.append(" and b.serv_type_id=").append(servTypeId);
			}
		}
		if (!StringUtil.IsEmpty(openstatus)){
			where.append(" and b.open_status=").append(openstatus);
		}
		if (!StringUtil.IsEmpty(userId)){
			where.append(" and a.user_id in ").append(userId);
		}
		where.append(getCity(cityId));
		if (!StringUtil.IsEmpty(devicetype) && !"0".equals(devicetype)){
			where.append(" and c.type_id='" + devicetype + "'");
		}
		// 如果用户规格不会空，则不根据用户类型查询
		if (!StringUtil.IsEmpty(spec_id)){
			where.append(" and a.spec_id = ").append(spec_id);
		}
		else
		{
			// 如果没有用户规格查询条件，则根据设备规格表中的用户查询
			// 由于tab_hgwcustomer.cust_type_id字段数据不正确，所以关联设备规格查询
			if ("1".equals(cust_type_id)){
				// 如果是政企
				where.append(" and exists (select 1 from tab_bss_dev_port dp where dp.id = a.spec_id and dp.gw_type = '2')");
			}else if ("2".equals(cust_type_id)){
				// 家庭
				where.append(" and exists (select 1 from tab_bss_dev_port dp where dp.id = a.spec_id and dp.gw_type = '1')");
			}
		}
		return where.toString();
	}
	
	/** 查询BSS工单业务列表时根据页面输入查询条件拼接SQL where查询条件 */
	private String getBssSheetServSQLCondition(String servTypeId, String openstatus,
			String userId, String cityId, String devicetype, String spec_id,
			String cust_type_id, String user_type_id,String gw_type,String startOpenDate,String endOpenDate,String deviceVersionType)
	{
		StringBuffer where = new StringBuffer(" where 1 = 1 ");
		// 如果有用户信息，既不使用时间查询
		if (userId == null && Global.AHLT.equals(Global.instAreaShortName)) {
			if (!StringUtil.IsEmpty(startOpenDate)) {
				where.append(" and b.dealdate>=").append(startOpenDate);
			}
			if (!StringUtil.IsEmpty(endOpenDate)) {
				where.append(" and b.dealdate<=").append(endOpenDate);
			}
		}
		if (!StringUtil.IsEmpty(user_type_id) && !"-1".equals(user_type_id)){
			where.append(" and a.user_type_id='").append(user_type_id).append("'  ");
		}
		if (!StringUtil.IsEmpty(servTypeId))
		{
			if(Global.NMGDX.equals(Global.instAreaShortName))
			{
				if (Global.GW_TYPE_BBMS.equals(gw_type))
				{
					if (servTypeId.equals("27")) {
						where.append(" and b.serv_type_id=10 and b.real_type_id=").append(servTypeId);
					}else {
						where.append(" and b.serv_type_id=").append(servTypeId);
					}
				}else {
					where.append(" and b.serv_type_id=").append(servTypeId);
				}
			}else {
				where.append(" and b.serv_type_id=").append(servTypeId);
			}
			
		}
		if (!StringUtil.IsEmpty(openstatus)){
			where.append(" and b.open_status=").append(openstatus);
		}
		//NXDX-NX-BUG-20180730-LiuX-001（家庭网关BSS业务查询-开通状态） BUG单修改  511-518
		else
		{
			if(Global.NXDX.equals(Global.instAreaShortName))
			{
				//界面只能展示 成功 失败 未作  过滤 开通状态为空的数据
				where.append(" and b.open_status is not null");
			}
		}
		if (!StringUtil.IsEmpty(userId)){
			where.append(" and a.user_id in ").append(userId);
		}
		where.append(getCity(cityId));
		if (!StringUtil.IsEmpty(devicetype) && !"0".equals(devicetype)){
			where.append(" and c.type_id='" + devicetype + "'");
		}
		// 如果用户规格不会空，则不根据用户类型查询
		if (!StringUtil.IsEmpty(spec_id)){
			where.append(" and a.spec_id = ").append(spec_id);
		}
		else
		{
			// 如果没有用户规格查询条件，则根据设备规格表中的用户查询
			// 由于tab_hgwcustomer.cust_type_id字段数据不正确，所以关联设备规格查询
			if ("1".equals(cust_type_id)){
				// 如果是政企
				where.append(" and exists (select 1 from tab_bss_dev_port dp where dp.id = a.spec_id and dp.gw_type = '2')");
			}else if ("2".equals(cust_type_id)){
				// 家庭
				where.append(" and exists (select 1 from tab_bss_dev_port dp where dp.id = a.spec_id and dp.gw_type = '1')");
			}
		}
		if(!StringUtil.IsEmpty(deviceVersionType) && !"-1".equals(deviceVersionType) && LipossGlobals.inArea(Global.AHLT)){
			//安徽联通增加 设备版本类型筛选条件
			where.append(" and v.device_version_type = '").append(deviceVersionType).append("'");
		}
		if (LipossGlobals.inArea(Global.SDLT) || LipossGlobals.inArea(Global.AHLT) || LipossGlobals.inArea(Global.NXLT) ){
			where.append(" order by b.dealdate desc");
		}
		return where.toString();
	}

	/**
	 * 查询业务信息计数<Br>
	 * 在BSS工单业务查询Sql上，增加统计查询，只需要维护一个查询列表。
	 */
	public int countBssSheetServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String userId, String servTypeId, String openstatus,
			String devicetype, String gw_type, String voipProtocalType, String spec_id,
			String cust_type_id, String user_type_id, String isRealtimeQuery,
			String wanProtocalType,String deviceVersionType)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{})", new Object[] { cityId,
				startOpenDate1, endOpenDate1, userId, servTypeId, openstatus });
		String bssSql = getBssSheetServSQL(gw_type, servTypeId, userId, startOpenDate1,
				endOpenDate1, openstatus, cityId, devicetype, voipProtocalType, spec_id,
				cust_type_id, user_type_id, wanProtocalType,deviceVersionType);
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from (").append(bssSql).append(") intab");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	/**
	 * 查询业务信息为了导出
	 */
	public List<Map> getBssSheetServInfoExcel(String cityId, String startOpenDate1,
			String endOpenDate1, String userId, String servTypeId, String openstatus,
			String devicetype, String gw_type, String voipProtocalType, String spec_id,
			String cust_type_id, String user_type_id, String isRealtimeQuery,
			String wanProtocalType,String deviceVersionType)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{},{})",
					new Object[] { cityId, startOpenDate1, endOpenDate1, userId,
							servTypeId, openstatus });
		String sql = getBssSheetServSQL(gw_type, servTypeId, userId, startOpenDate1,
				endOpenDate1, openstatus, cityId, devicetype, voipProtocalType, spec_id,
				cust_type_id, user_type_id, wanProtocalType,deviceVersionType);
		PrepareSQL psql = new PrepareSQL(sql);
		cityMap = CityDAO.getCityIdCityNameMap();
		servTypeMap = getServType(isRealtimeQuery);
		bssdevMap = Global.G_BssDev_PortName_Map;
		if (!LipossGlobals.inArea(Global.SDLT)){
			usertype_map = Global.G_UserTypeId_UserName_Map;
		}
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				String city_id = (String) list.get(i).get("city_id");
				list.get(i).put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					list.get(i).put("city_name", city_name);
				}else{
					list.get(i).put("city_name", "");
				}
				String serv_type_id = StringUtil.getStringValue(list.get(i),"serv_type_id");
				String tmp = "";
				if (false == StringUtil.IsEmpty(serv_type_id)){
					tmp = servTypeMap.get(serv_type_id);
				}
				list.get(i).put("serv_type", tmp);
				String sn = StringUtil.getStringValue(list.get(i),"device_serialnumber");
				if (LipossGlobals.inArea(Global.SDLT))
				{
					if (false == StringUtil.IsEmpty(sn))
					{
						if (sn.length() > 32){
							list.get(i).put("device_serialnumber",
									sn.substring(0, 28) + "......");
						}else{
							list.get(i).put("device_serialnumber", sn);
						}
					}
					else
					{
						list.get(i).put("device_serialnumber", "");
					}
				}
				else
				{
					if (false == StringUtil.IsEmpty(sn)){
						list.get(i).put("device_serialnumber", sn);
					}else{
						list.get(i).put("device_serialnumber", "");
					}
				}
				
				String tempserusername = StringUtil.getStringValue(list.get(i),"serusername");
				if (LipossGlobals.inArea(Global.SDLT) || LipossGlobals.inArea(Global.JLDX) )
				{
					String tempServTypeId = StringUtil.getStringValue(list.get(i),"serv_type_id");
					if ("14".equals(tempServTypeId)){
						tempserusername = StringUtil.getStringValue(list.get(i),"voip_phone");
					}
				}
				if ( LipossGlobals.inArea(Global.SXLT))
				{
					String tempServTypeId = String.valueOf(list.get(i).get("serv_type_id"));
					if ("14".equals(tempServTypeId))
					{
						tempserusername = String.valueOf(list.get(i).get("voip_username"));
					}
				}
				list.get(i).put("serUsername", tempserusername);
				String specId = String.valueOf(list.get(i).get("spec_id"));
				if (!StringUtil.IsEmpty(specId)){
					list.get(i).put("spec_name", bssdevMap.get(specId));
				}else{
					list.get(i).put("spec_name", "");
				}
				list.get(i).put("dealdate", transDate(list.get(i).get("dealdate")));
				list.get(i).put("opendate", transDate(list.get(i).get("opendate")));
				if (!LipossGlobals.inArea(Global.SDLT))
				{
					list.get(i).put("user_type_id",
							usertype_map.get(StringUtil.getStringValue(list.get(i),"user_type_id")));
				}
				String open_status = StringUtil.getStringValue(list.get(i).get("open_status"));
				if ("1".equals(open_status)){
					list.get(i).put("open_status", "成功");
				}else if ("0".equals(open_status)){
					list.get(i).put("open_status", "未做");
				}else if ("-1".equals(open_status)){
					list.get(i).put("open_status", "失败");
				}else if ("".equals(open_status)){
					list.get(i).put("open_status", "");
				}
			}
			cityMap = null;
		}
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
	 * 查询service_id
	 */
	public String getServiceId(String servTypeId, String servstauts, String wanType,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getServiceId({},{},{})",servstauts, servstauts,wanType);
		StringBuffer sql = new StringBuffer();
		sql.append("select service_id from tab_service where serv_type_id=")
				.append(servTypeId).append(" and oper_type_id=").append(servstauts);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map map = queryForMap(psql.getSQL());
		return map == null ? null : StringUtil.getStringValue(map.get("service_id"));
	}

	/**
	 * 查询配置信息
	 */
	public List getConfigInfo(String gw_type, String deviceId, String serviceId,
			String isRealtimeQuery, String serUsername)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select id,service_id,start_time,end_time,status,result_id,result_desc from  "
						+ getTableName(gw_type) + "  where device_id='").append(deviceId)
				.append("'  and service_id=").append(serviceId);
		if (!StringUtil.IsEmpty(serUsername) && !"5101".equals(serviceId) && !"4701".equals(serviceId) ){
			sql.append(" and username='" + serUsername + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		serviceCodeMap = getServiceCode();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				logger.warn("查询serv：id={},service={},status={},result={}",new Object[]{rs.getString("id"),rs.getString("service_id"),rs.getString("status"),rs.getString("result_id")});
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("serviceName", serviceCodeMap.get(rs.getString("service_id")));
				map.put("service_id", rs.getString("service_id"));
				map.put("start_time", transDate(rs.getString("start_time")));
				map.put("end_time", transDate(rs.getString("end_time")));
				String result_id = rs.getString("result_id");
				map.put("result_id", result_id);
				String status=rs.getString("status");
				String state = status_map.get(rs.getString("status"));
				if(LipossGlobals.inArea(Global.GSDX)) {
					if ("100".equals(status)) {
						if (result_id.equals("1")) {
							state="执行成功";
						}else {
							state="执行失败";
						}
					}
				}
				map.put("status", state);
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null)
				{
					map.put("fault_reason", "");
					map.put("solutions", "");
					map.put("fault_desc", "");
				}
				else
				{
					map.put("fault_reason",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultReason());
					map.put("solutions",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getSolutions());
					map.put("fault_desc",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultDesc());
				}
				map.put("result_desc", rs.getString("result_desc"));
				return map;
			}
		});
		return list;
	}

	public String getSolutionInfo(String result_id)
	{
		logger.debug("BssSheetServDAO-->getSolutionInfo");
		CpeFaultcodeOBJ obj = Global.G_Fault_Map.get(result_id);
		if (null != obj)
		{
			String solution = obj.getSolutions();
			if (StringUtil.IsEmpty(solution)){
				return "无处理意见";
			}else{
				return solution;
			}
		}
		return "无处理意见";
	}

	/**
	 * 查询配置信息 软件升级
	 */
	public List getSoftUpInfo(String gw_type, String deviceId, String serviceId,
			String isRealtimeQuery, String serUsername)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select id,service_id,start_time,end_time,status,result_id,result_desc,client_id from "
				+ getTableName(gw_type) + " where device_id='");
		sql.append(deviceId).append("'  and service_id= 5 and client_id = 14")
				.append(" and username='" + serUsername + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		serviceCodeMap = getServiceCode();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("serviceName", serviceCodeMap.get(rs.getString("service_id")));
				map.put("service_id", rs.getString("service_id"));
				map.put("client_id", rs.getString("client_id"));
				map.put("start_time", transDate(rs.getString("start_time")));
				map.put("end_time", transDate(rs.getString("end_time")));
				map.put("result_id", rs.getString("result_id"));
				map.put("status", status_map.get(rs.getString("status")));
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null)
				{
					map.put("fault_reason", "");
					map.put("solutions", "");
					map.put("fault_desc", "");
				}
				else
				{
					map.put("fault_reason",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultReason());
					map.put("solutions",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getSolutions());
					map.put("fault_desc",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultDesc());
				}
				map.put("result_desc", rs.getString("result_desc"));
				return map;
			}
		});
		return list;
	}

	public Map<String, String> getFaultCode(String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		String sql = "select fault_code, fault_desc from tab_cpe_faultcode";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		Map faultCodeMap = new HashMap<String, String>();
		for (Map map : list)
		{
			faultCodeMap.put(StringUtil.getStringValue(map.get("fault_code")),
					StringUtil.getStringValue(map.get("fault_desc")));
		}
		faultCodeMap.put("1", "成功");
		faultCodeMap.put("0", "未做");
		faultCodeMap.put("2", "正在执行");
		return faultCodeMap;
	}

	// public Map<String, Map<String, String>> getFaultCodeMap()
	// {
	// String sql =
	// "select fault_code, fault_desc,fault_reason,solutions from tab_cpe_faultcode";
	// PrepareSQL psql = new PrepareSQL(sql);
	// List<Map> list = jt.queryForList(psql.getSQL());
	// Map<String, Map<String, String>> faultCodeMap = new HashMap<String,
	// Map<String,String>>();
	// Map<String, String> faultInfoMap = null;
	// for (Map map : list)
	// {
	// faultInfoMap = new HashMap<String, String>();
	// faultInfoMap.put("fault_desc",
	// StringUtil.getStringValue(map.get("fault_desc")));
	// faultInfoMap.put("fault_reason",
	// StringUtil.getStringValue(map.get("fault_reason")));
	// faultInfoMap.put("solutions",
	// StringUtil.getStringValue(map.get("solutions")));
	// faultCodeMap.put(StringUtil.getStringValue(map.get("fault_code")),faultInfoMap);
	// }
	//
	// return faultCodeMap;
	// }
	
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
	 * 查询配置详细信息
	 */
	public List<Map> getConfigDetail(String gw_type, String strategyId,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select id,sheet_id,service_id,result_id,sheet_para from  "
						+ getTableName(gw_type) + "  where id=").append(strategyId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("sheet_id", rs.getString("sheet_id"));
				map.put("service_id", rs.getString("service_id"));
				String sheet_para = rs.getString("sheet_para");
				String encoding = "UTF-8";
				Document doc;
				try
				{
					doc = DocumentHelper.parseText(sheet_para);
					StringWriter writer = new StringWriter();
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setIndent(true);
					format.setEncoding(encoding);
					XMLWriter xmlwriter = new XMLWriter(writer, format);
					xmlwriter.write(doc);
					sheet_para = writer.toString();
					// 有宽带密码、语音密码、用户姓名、地址等信息的页面，信息需要屏蔽
					if (LipossGlobals.inArea(Global.JSDX))
					{
						if (sheet_para.indexOf("<Password>") > 0)
						{
							String password = sheet_para.substring(
									sheet_para.indexOf("<Password>"),
									sheet_para.indexOf("</Password>"));
							sheet_para = sheet_para.replace(password, "<Password>");
							logger.warn("Password:" + password);
						}
						if (sheet_para.indexOf("<AuthPassword>") > 0)
						{
							String authPassword = sheet_para.substring(
									sheet_para.indexOf("<AuthPassword>"),
									sheet_para.indexOf("</AuthPassword>"));
							logger.warn("AuthPassword:" + authPassword);
							sheet_para = sheet_para.replace(authPassword,
									"<AuthPassword>");
						}
					}
					sheet_para = sheet_para.replace("&", "&amp;");
					sheet_para = sheet_para.replace("<", "&lt;");
					sheet_para = sheet_para.replace(">", "&gt;");
					sheet_para = sheet_para.replace("\r\n", "\n");
					sheet_para = sheet_para.replace("\n", "<br>\n");
					sheet_para = sheet_para.replace("\t", "    ");
					sheet_para = sheet_para.replace("  ", " &nbsp;");
					map.put("sheet_para", sheet_para);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					map.put("sheet_para", "");
				}
				map.put("result_id", rs.getString("result_id"));
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null)
				{
					map.put("fault_reason", "");
					map.put("solutions", "");
					map.put("fault_desc", "");
				}
				else
				{
					map.put("fault_reason",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultReason());
					map.put("solutions",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getSolutions());
					map.put("fault_desc",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultDesc());
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询配置历史信息
	 */
	public List getConfigLogInfo(String gw_type, String deviceId, String serviceId,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select id,service_id,start_time,end_time,status,result_id,result_desc from "
						+ getTableNameLog(gw_type) + " where device_id='")
				.append(deviceId).append("'  and service_id=").append(serviceId);
		
		// 2020/04/26 山西联通新增按时间排序
		if(Global.instAreaShortName.equals(Global.SXLT)) {
			sql.append(" order by start_time desc");
		}
				
		PrepareSQL psql = new PrepareSQL(sql.toString());
		serviceCodeMap = getServiceCode();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("serviceName", serviceCodeMap.get(rs.getString("service_id")));
				map.put("start_time", transDate(rs.getString("start_time")));
				map.put("end_time", transDate(rs.getString("end_time")));
				map.put("status", status_map.get(rs.getString("status")));
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null)
				{
					map.put("result_id", "");
				}
				else
				{
					map.put("result_id",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultDesc());
				}
				map.put("result_desc", rs.getString("result_desc"));
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询配置历史详细信息
	 */
	public List<Map> getConfigLogDetail(String gw_type, String strategyId,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append("select id,sheet_id,service_id,result_id,sheet_para from "
						+ getTableNameLog(gw_type) + " where id=").append(strategyId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("sheet_id", rs.getString("sheet_id"));
				map.put("service_id", rs.getString("service_id"));
				String sheet_para = rs.getString("sheet_para");
				String encoding = "UTF-8";
				Document doc;
				try
				{
					doc = DocumentHelper.parseText(sheet_para);
					StringWriter writer = new StringWriter();
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setIndent(true);
					format.setEncoding(encoding);
					XMLWriter xmlwriter = new XMLWriter(writer, format);
					xmlwriter.write(doc);
					sheet_para = writer.toString();
					sheet_para = sheet_para.replace("&", "&amp;");
					sheet_para = sheet_para.replace("<", "&lt;");
					sheet_para = sheet_para.replace(">", "&gt;");
					sheet_para = sheet_para.replace("\r\n", "\n");
					sheet_para = sheet_para.replace("\n", "<br>\n");
					sheet_para = sheet_para.replace("\t", "    ");
					sheet_para = sheet_para.replace("  ", " &nbsp;");
					map.put("sheet_para", sheet_para);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					map.put("sheet_para", "");
				}
				map.put("result_id", rs.getString("result_id"));
				if (Global.G_Fault_Map.get(StringUtil.getIntegerValue(rs.getString("result_id"))) == null)
				{
					map.put("fault_reason", "");
					map.put("solutions", "");
					map.put("fault_desc", "");
				}
				else
				{
					map.put("fault_reason",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultReason());
					map.put("solutions",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getSolutions());
					map.put("fault_desc",
							Global.G_Fault_Map.get(StringUtil.getIntegerValue(
									rs.getString("result_id"))).getFaultDesc());
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询VoIP工单详细信息
	 */
	public List<Map> getBssSheetVoIP(String cityId, String userId, final String gw_type,
			String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheet({},{})", new Object[] { cityId, userId });
		PrepareSQL psql = new PrepareSQL();
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
			psql.append(" b.opendate, b.open_status, b.serv_status, c.voip_phone, c.voip_username,c.voip_passwd, c.line_id, c.rtp_prefix, ");
			psql.append(" d.prox_serv, d.prox_port,d.stand_prox_serv, d.stand_prox_port,d.regi_serv,d.regi_port,");
			psql.append(" d.stand_regi_serv,d.stand_regi_port,d.out_bound_proxy,d.out_bound_port,d.stand_out_bound_proxy,d.stand_out_bound_port,");
			psql.append(" b.wan_type, b.ipaddress, b.ipmask, b.gateway, b.adsl_ser, c.protocol,");
			psql.append(" c.reg_id_type, c.reg_id, c.voip_port, b.vlanid ");
			if (LipossGlobals.inArea(Global.GSDX)){
				psql.append(",c.open_status as serOneStatues,c.exec_time as serOneTime ");
			}
			psql.append(" from tab_hgwcustomer a left join tab_voip_serv_param c on a.user_id = c.user_id, ");
			psql.append(" hgwcust_serv_info b , tab_sip_info d");
			psql.append(" where a.user_id = b.user_id ");
			psql.append("  and c.sip_id = d.sip_id");
			psql.append("  and a.user_id = " + userId);
			psql.append("  and b.serv_type_id = 14");
		}
		psql.append(getCity(cityId));
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
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				String tmp = "-";
				if (false == StringUtil.IsEmpty(serv_type_id)){
					tmp = servTypeMap.get(serv_type_id);
				}
				map.put("serv_type", tmp);
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("error_code", "1");// 1、正常 0：异常
				map.put("voip_phone", rs.getString("voip_phone"));
				map.put("voip_username", rs.getString("voip_username"));
				
				String password = rs.getString("voip_passwd");
				if(!StringUtil.IsEmpty(password) && Global.NXDX.equals(Global.instAreaShortName)){
					password = Base64.decode(password);
				}
				map.put("voip_passwd", password);
				map.put("line_id", rs.getString("line_id"));
				if (!Global.GW_TYPE_BBMS.equals(gw_type)) {
					if (rs.findColumn("rtp_prefix") > 0) {
						map.put("rtp_prefix", rs.getString("rtp_prefix"));
					}
				}
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
				if ("0".equals(protocol)){
					map.put("protocol", "IMS SIP");
				}else if ("1".equals(protocol)){
					map.put("protocol", "软交换 SIP");
				}else if ("2".equals(protocol)){
					map.put("protocol", "H248");
				}else if ("3".equals(protocol)){
					map.put("protocol", "IMS H248");
				}else{
					map.put("protocol", "-");
				}
				
				if ("2".equals(protocol) && "3".equals(protocol))
				{
					map.put("error_code", "1");// 1、正常 0：异常
				}
				else
				{
					if (Global.CQDX.equals(Global.instAreaShortName) 
							|| Global.SXLT.equals(Global.instAreaShortName)
							|| Global.GSDX.equals(Global.instAreaShortName))
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
									&& !StringUtil.IsEmpty(rs.getString("stand_out_bound_proxy"))
									&& !StringUtil.IsEmpty(rs.getString("stand_out_bound_port")))
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
				}
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				String wan_type = StringUtil.getStringValue(rs.getString("wan_type"));
				if ("3".equals(wan_type)){
					map.put("wan_type", "STATIC");
				}else if ("4".equals(wan_type)){
					map.put("wan_type", "DHCP");
				}else{
					map.put("wan_type", "-");
				}
				String reg_id_type = StringUtil.getStringValue(rs.getString("reg_id_type"));
				if ("0".equals(reg_id_type)){
					map.put("reg_id_type", "IP地址");
				}else if ("1".equals(reg_id_type)){
					map.put("reg_id_type", "域名");
				}else if ("2".equals(reg_id_type)){
					map.put("reg_id_type", "设备名");
				}else{
					map.put("reg_id_type", "-");
				}
				map.put("ipaddress", StringUtil.getStringValue(rs.getString("ipaddress")));
				map.put("ipmask", StringUtil.getStringValue(rs.getString("ipmask")));
				map.put("gateway", StringUtil.getStringValue(rs.getString("gateway")));
				map.put("adsl_ser", StringUtil.getStringValue(rs.getString("adsl_ser")));
				map.put("reg_id", StringUtil.getStringValue(rs.getString("reg_id")));
				map.put("voip_phone",StringUtil.getStringValue(rs.getString("voip_phone")));
				map.put("voip_port", StringUtil.getStringValue(rs.getString("voip_port")));
				map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
				// 新增业务参数表字段查询业务参数表中业务下发时间,下发状态
				if (LipossGlobals.inArea(Global.GSDX)){
					map.put("serOneTime", transDate(rs.getString("serOneTime")));
					logger.warn("rs.getInt(serOneStatues):{}",rs.getInt("serOneStatues"));
					logger.warn("rs.getInt(serOneTime):{}",transDate(rs.getString("serOneTime")));
					if(0 == rs.getInt("serOneStatues")){
						map.put("serOneStatues","未做" );
					}
					if(1 == rs.getInt("serOneStatues")){
						map.put("serOneStatues","成功" );
					}
					if(-1 == rs.getInt("serOneStatues")){
						map.put("serOneStatues","失败" );
					}
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询宽带上网工单详情信息
	 */
	public List<Map> getInternetBssSheet(String gw_type, String userId,
			String servTypeId, String serUsername, String cityId, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			if (LipossGlobals.inArea(Global.AHDX))
			{
				sql.append(
						"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,"
								+"b.vlanid,c.ip_type,c.dslite_enable,b.wan_type,b.real_type_id,c.capacity,c.enable,"
								+"c.controllerAddress,c.backupEnable,c.backupControllerAddress"
								+ " from tab_egwcustomer a,egwcust_serv_info b,tab_egw_net_serv_param c"
								+ " where a.user_id=b.user_id and b.user_id = c.user_id and b.serv_type_id = c.serv_type_id" 
								+ " and b.username = c.username and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId)
						.append(" and b.username='").append(serUsername).append("'");
			}
			else
			{
				sql.append(
						"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type"
								+ " from tab_egwcustomer a,egwcust_serv_info b,tab_egw_net_serv_param c"
								+ " where a.user_id=b.user_id and b.user_id = c.user_id and b.serv_type_id = c.serv_type_id and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId)
						.append(" and b.username='").append(serUsername).append("'");
			}
		}
		else
		{
			if (LipossGlobals.inArea(Global.SDLT)){
				sql.append(
						"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type from tab_hgwcustomer a,hgwcust_serv_info b, tab_net_serv_param c where a.user_id=b.user_id and b.user_id=c.user_id and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId)
						.append(" and b.username='").append(serUsername).append("'");
			}else if(LipossGlobals.inArea(Global.GSDX)){
				sql.append(
						"select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type from tab_hgwcustomer a,hgwcust_serv_info b, tab_net_serv_param c where a.user_id=b.user_id and b.user_id=c.user_id and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId);
			}
			else
			{
				// 当用户类型为家庭或者家庭政企融合时，使用家庭处理方式。
				// modify by zhangsb 2014年5月15日
				// 由于出现多宽带业务，所以tab_net_serv_param中同一个用户会有多个宽带信息 所以增加distinct 去重
				sql.append("select distinct  a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,c.ip_type,c.dslite_enable,b.wan_type");

				//新疆增加上行带宽和下行带宽 add on 2019.11.06 by lm
				if(LipossGlobals.inArea(Global.XJDX)){
					sql.append(",c.down_bandwidth");
				}
				sql.append(" from tab_hgwcustomer a,hgwcust_serv_info b, tab_net_serv_param c"
						+ " where a.user_id=b.user_id and b.user_id=c.user_id and b.serv_type_id = c.serv_type_id and b.username=c.username and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId);

				if (!StringUtil.IsEmpty(serUsername)){
					sql.append(" and b.username='").append(serUsername).append("'");
				}
			}
		}
		sql.append(getCity(cityId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		final Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		final Map<String, String> servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = null;
		if (Global.GW_TYPE_BBMS.equals(gw_type) && LipossGlobals.inArea(Global.AHDX))
		{
			list = jt.query(psql.getSQL(), new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", rs.getString("username"));
					String city_name = cityMap.get(rs.getString("city_id"));
					map.put("city_name", StringUtil.IsEmpty(city_name) ? "" : city_name);
					String serv_type_id = rs.getString("serv_type_id");
					map.put("serv_type_id", serv_type_id);
					map.put("serv_type",StringUtil.IsEmpty(serv_type_id) ? 
											"-" : servTypeMap.get(serv_type_id));
					map.put("opendate", transDate(rs.getString("opendate")));
					map.put("account", rs.getString("account"));
					map.put("serv_status", transServStatus(rs.getString("serv_status")));
					map.put("vlanid", rs.getString("vlanid"));
					map.put("ip_type", transIpType_1(rs.getString("ip_type")));
					map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ? 
												"否" : "是");
					map.put("wan_type", transWanType(rs.getString("wan_type")));
					map.put("real_type_id",rs.getString("real_type_id"));
					map.put("capacity",rs.getString("capacity"));
					map.put("enable",rs.getString("enable"));
					map.put("controllerAddress",rs.getString("controlleraddress"));
					map.put("backupEnable",rs.getString("backupenable"));
					map.put("backupControllerAddress",rs.getString("backupcontrolleraddress"));
					return map;
				}
			});
		}
		else
		{
			list = jt.query(psql.getSQL(), new RowMapper()
			{
				public Object mapRow(ResultSet rs, int arg1) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("username", rs.getString("username"));
					String city_name = cityMap.get(rs.getString("city_id"));
					map.put("city_name", StringUtil.IsEmpty(city_name) ? "" : city_name);
					String serv_type_id = rs.getString("serv_type_id");
					map.put("serv_type_id", serv_type_id);
					map.put("serv_type",StringUtil.IsEmpty(serv_type_id) ? 
											"-" : servTypeMap.get(serv_type_id));
					map.put("opendate", transDate(rs.getString("opendate")));
					map.put("account", rs.getString("account"));
					map.put("serv_status", transServStatus(rs.getString("serv_status")));
					map.put("vlanid", rs.getString("vlanid"));
					map.put("ip_type", transIpType_1(rs.getString("ip_type")));
					map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ? 
													"否"	: "是");
					map.put("wan_type", transWanType(rs.getString("wan_type")));
					if(LipossGlobals.inArea(Global.XJDX)){
						map.put("downBandwidth",rs.getString("down_bandwidth"));
					}
					return map;
				}
			});
		}
		return list;
	}

	public List<Map> getCloudNetBssSheet(String gw_type, String userId,
			String servTypeId, String serUsername, String cityId, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		String router="";
		String sql1="select b.v4router_list,b.v6router_list from tab_hgw_router a ,tab_broad_band_router b where a.router_id=b.router_id and a.user_id="+userId+" ";
		PrepareSQL psql1 = new PrepareSQL(sql1.toString());
		List routeList = jt.queryForList(psql1.getSQL());
		if (routeList!=null && routeList.size()>0) {
			for (int i = 0; i < routeList.size(); i++) {
				Map<String, String> map=(Map<String, String>) routeList.get(i);
				String v4router_list = StringUtil.getStringValue(map, "v4router_list");
				String v6router_list = StringUtil.getStringValue(map, "v6router_list");
				if (i==0) {
					router+=v4router_list;
					if (!StringUtil.IsEmpty(v6router_list)) {
						router+=","+v6router_list;
					}
				}else {
					router+=","+v4router_list;
					if (!StringUtil.IsEmpty(v6router_list)) {
						router+=","+v6router_list;
					}
				}
			}
		}
		final String router_List=router;
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append(" select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,b.wan_type,c.ipoe_upbandwidth,");
			sql.append(" c.ipoe_downbandwidth,c.app_type,c.open_status");
			sql.append(" from tab_egwcustomer a, egwcust_serv_info b, tab_broad_band_param c"
					+ " where a.user_id = b.user_id and b.user_id = c.user_id and b.serv_type_id = c.serv_type_id and b.username = c.username and a.user_id =")
					.append(userId).append(" and b.serv_type_id=").append(servTypeId);

			if (!StringUtil.IsEmpty(serUsername)){
				sql.append(" and b.username='").append(serUsername).append("'");
			}
		}
		else
		{
			sql.append(" select a.username,a.city_id,b.serv_type_id,b.opendate,b.serv_status,b.username as account,b.vlanid,b.wan_type,c.ipoe_upbandwidth,");
			sql.append(" c.ipoe_downbandwidth,c.app_type,c.open_status");
			sql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_broad_band_param c"
					+ " where a.user_id = b.user_id and b.user_id = c.user_id and b.serv_type_id = c.serv_type_id and b.username = c.username and a.user_id =")
					.append(userId).append(" and b.serv_type_id=").append(servTypeId);

			if (!StringUtil.IsEmpty(serUsername)){
				sql.append(" and b.username='").append(serUsername).append("'");
			}
		}
		sql.append(getCity(cityId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		final Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		final Map<String, String> servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = null;
		list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("username", rs.getString("username"));
				String city_name = cityMap.get(rs.getString("city_id"));
				map.put("city_name", StringUtil.IsEmpty(city_name) ? "" : city_name);
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				map.put("serv_type",StringUtil.IsEmpty(serv_type_id) ? 
										"-" : servTypeMap.get(serv_type_id));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				map.put("account", rs.getString("account"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("wan_type", transWanType(rs.getString("wan_type")));
				map.put("ipoe_upbandwidth",rs.getString("ipoe_upbandwidth"));
				map.put("ipoe_downbandwidth",rs.getString("ipoe_downbandwidth"));
				map.put("app_type",rs.getString("app_type"));
				String open_status=StringUtil.getStringValue(rs.getString("open_status"));
				if ("1".equals(open_status)) {
					open_status="路由配置成功";
				}else if ("-1".equals(open_status)) {
					open_status="路由配置失败";
				}else if ("0".equals(open_status)) {
					open_status="未做";
				}
				map.put("router_List",router_List);
				map.put("open_status",open_status);
				return map;
			}
		});
		return list;
	}

	
	
	/**
	 * 将查询的操作人、ip地址、时间和查看的用户账号入表
	 */
	public int insertA8log(String voip_passwd,String voip_username,String userip,UserRes curUser)
	{
		PrepareSQL sql = new PrepareSQL(
				"insert into tab_view_voip_password(operate_username,operate_time,ip_address,user_account) values(?,?,?,?)");
		long query_time = new DateTimeUtil().getLongTime();
		User user = curUser.getUser();
		sql.setString(1, String.valueOf(user.getAccount()));
		sql.setString(2, String.valueOf(query_time));
		sql.setString(3, userip);
		sql.setString(4, voip_username);
		return jt.update(sql.getSQL());
	}

	/**
	 * 查询IPTV或宽带上网工单详细信息
	 */
	public List<Map> getBssSheetByServtype(String cityId, final String userId,
										   String servTypeId, String serUsername, String gw_type,
										   String isRealtimeQuery, String realBindPort)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetByServtype({},{},{})",cityId, userId,servTypeId);
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append(
					"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid"
							+ " from tab_egwcustomer a,egwcust_serv_info b"
							+ " where a.user_id=b.user_id and a.user_id=").append(userId)
					.append(" and b.serv_type_id=").append(servTypeId)
					.append(" and b.username='").append(serUsername).append("'");
			if (!StringUtil.IsEmpty(realBindPort)
					&& Global.CQDX.equals(Global.instAreaShortName))
			{
				sql.append(" and b.real_bind_port='").append(realBindPort).append("'");
			}
		}
		else
		{
			// 当用户类型为家庭或者家庭政企融合时，使用家庭处理方式。
			if (!"20".equals(servTypeId))
			{
				if(Global.XJDX.equals(Global.instAreaShortName) && "32".equals(servTypeId)){
					sql.append(
							"select b.sy_vendor,a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid"
									+ " from tab_hgwcustomer a,hgwcust_serv_info b"
									+ " where a.user_id=b.user_id and a.user_id=")
							.append(userId).append(" and b.serv_type_id=").append(servTypeId);
				}
				else{
					// 山西联通增加全路由查询字段
					if(LipossGlobals.inArea(Global.SXLT)){
						sql.append(
								"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid,b.all_route"
										+ " from tab_hgwcustomer a,hgwcust_serv_info b"
										+ " where a.user_id=b.user_id and a.user_id=")
								.append(userId).append(" and b.serv_type_id=").append(servTypeId);
					}else{
						sql.append(
								"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid"
										+ " from tab_hgwcustomer a,hgwcust_serv_info b"
										+ " where a.user_id=b.user_id and a.user_id=")
								.append(userId).append(" and b.serv_type_id=").append(servTypeId);
					}
				}
			}
			else
			{
				sql.append(
						"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid,c.ssid,c.max_net_num "
								+ " from tab_hgwcustomer a,hgwcust_serv_info b,tab_net_serv_param c where a.user_id=b.user_id "
								+ " and a.user_id=c.user_id and b.serv_type_id=c.serv_type_id and a.user_id=")
						.append(userId).append(" and b.serv_type_id=").append(servTypeId);
			}
			if (!StringUtil.IsEmpty(serUsername)){
				sql.append(" and b.username='").append(serUsername).append("'");
			}
			if (!StringUtil.IsEmpty(realBindPort)
					&& Global.CQDX.equals(Global.instAreaShortName))
			{
				sql.append(" and b.real_bind_port='").append(realBindPort).append("'");
			}
		}
		sql.append(getCity(cityId));
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		servTypeMap = getServType(isRealtimeQuery);
		final String servTypeIdTemp = servTypeId;
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = cityMap.get(city_id);
				if (!StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				
				if("10".equals(serv_type_id) && LipossGlobals.inArea(Global.JLLT))
				{
					PrepareSQL psql = new PrepareSQL();
					psql.append("select rate from tab_speed_dev_rate ");
					psql.append("where serv_type_id=10 and pppoe_name=? and user_id = ? ");
					psql.setString(1, rs.getString("account"));
					psql.setLong(2,StringUtil.getLongValue(userId));

					List l=jt.queryForList(psql.getSQL());
					String rate="";
					if(l!=null && !l.isEmpty())
					{
						long rate_num=StringUtil.getLongValue((Map)l.get(0),"rate",0);
						if(rate_num==0){
							rate="";
						}else if(rate_num<1024 || rate_num%1024>0){
							rate=rate_num+"K";
						}else{
							rate=rate_num/1024 +"M";
						}
					}
					map.put("rate",rate);
				}
				
				map.put("serv_type_id", serv_type_id);
				map.put("serv_type",
						StringUtil.IsEmpty(serv_type_id) ? 
								"-" : servTypeMap.get(serv_type_id));
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("account", rs.getString("account"));
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				map.put("passwd", rs.getString("passwd"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("multicast_vlanid", rs.getString("multicast_vlanid"));
				map.put("serv_num", rs.getString("serv_num"));
				map.put("bind_port", dealLongPort(rs.getString("bind_port")));
				map.put("ip_type", transIpType_1(rs.getString("ip_type")));
				map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ?
												"否" : "是");
				map.put("wan_type", transWanType(rs.getString("wan_type")));
				if(LipossGlobals.inArea(Global.SXLT)){
					map.put("wan_type", transWanTypeByRoute(rs.getString("wan_type"),rs.getString("all_route")));
				}
				if ("20".equals(servTypeIdTemp))
				{
					map.put("ssid", StringUtil.getStringValue(rs.getString("ssid")));
					map.put("max_net_num",StringUtil.getStringValue(rs.getString("max_net_num")));
				}
				if(Global.XJDX.equals(Global.instAreaShortName) && "32".equals(servTypeIdTemp)){
//					9	溯源厂家	必须	1	必须  1：广州巨泰wifidog	2：广州巨泰nowifidog  3：任子行wifidog  4：任子行nowifidog  5：深信服 sxf
					String sy_vendor_tmp = StringUtil.getStringValue(rs.getString("sy_vendor"));
					if("1".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "广州巨泰wifidog";
					}else if("2".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "广州巨泰nowifidog";
					}else if("3".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "任子行wifidog";
					}else if("4".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "任子行nowifidog";
					}else if("5".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "深信服 sxf";
					}else{
						sy_vendor_tmp = "==";
					}
					
					map.put("sy_vendor", sy_vendor_tmp);
				}
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 查询IPTV或宽带上网工单详细信息
	 */
	public List<Map> getBssSheetByServtypeVlan(String cityId, String userId,
			String servTypeId, String serUsername, String gw_type,
			String isRealtimeQuery, String realBindPort, String vlanid)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetByServtype({},{},{})",cityId, userId,servTypeId);
		StringBuffer sql = new StringBuffer();

		sql.append(
				"select a.user_id,a.username,a.city_id,a.device_serialnumber,a.device_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status,b.serv_status,b.username as account,b.passwd,b.vlanid,b.bind_port,b.serv_num,b.ip_type,b.dslite_enable,b.wan_type,b.multicast_vlanid,b.all_route "
						+ " from tab_hgwcustomer a,hgwcust_serv_info b"
						+ " where a.user_id=b.user_id and a.user_id=")
				.append(userId).append(" and b.serv_type_id=").append(servTypeId);
		if (!StringUtil.IsEmpty(serUsername)){
			sql.append(" and b.username='").append(serUsername).append("'");
		}
		
		sql.append(getCity(cityId));
		
		if (!StringUtil.IsEmpty(vlanid)){
			sql.append(" and b.vlanid='").append(vlanid).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		servTypeMap = getServType(isRealtimeQuery);
		final String servTypeIdTemp = servTypeId;
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				String city_name = cityMap.get(city_id);
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("device_id", rs.getString("device_id"));
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				map.put("serv_type",StringUtil.IsEmpty(serv_type_id) ? 
										"-" : servTypeMap.get(serv_type_id));
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("opendate", transDate(rs.getString("opendate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("account", rs.getString("account"));
				map.put("serv_status", transServStatus(rs.getString("serv_status")));
				map.put("passwd", rs.getString("passwd"));
				map.put("vlanid", rs.getString("vlanid"));
				map.put("multicast_vlanid", rs.getString("multicast_vlanid"));
				map.put("serv_num", rs.getString("serv_num"));
				map.put("bind_port", dealLongPort(rs.getString("bind_port")));
				map.put("ip_type", transIpType_1(rs.getString("ip_type")));
				map.put("dslite_enable", "0".equals(rs.getString("dslite_enable")) ? 
												"否" : "是");
				map.put("wan_type", transWanType(rs.getString("wan_type")));
				if(LipossGlobals.inArea(Global.SXLT)){
					map.put("wan_type", transWanTypeByRoute(rs.getString("wan_type"),rs.getString("all_route")));
				}
				if ("20".equals(servTypeIdTemp))
				{
					map.put("ssid", StringUtil.getStringValue(rs.getString("ssid")));
					map.put("max_net_num",
							StringUtil.getStringValue(rs.getString("max_net_num")));
				}
				if(Global.XJDX.equals(Global.instAreaShortName) && "32".equals(servTypeIdTemp)){
//					9	溯源厂家	必须	1	必须  1：广州巨泰wifidog	2：广州巨泰nowifidog  3：任子行wifidog  4：任子行nowifidog  5：深信服 sxf
					String sy_vendor_tmp = StringUtil.getStringValue(rs.getString("sy_vendor"));
					if("1".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "广州巨泰wifidog";
					}else if("2".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "广州巨泰nowifidog";
					}else if("3".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "任子行wifidog";
					}else if("4".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "任子行nowifidog";
					}else if("5".equals(sy_vendor_tmp)){
						sy_vendor_tmp = "深信服 sxf";
					}else{
						sy_vendor_tmp = "==";
					}
					
					map.put("sy_vendor", sy_vendor_tmp);
				}
				return map;
			}
		});
		return list;
	}

	/**
	 * 查询bssVOIP原始工单信息
	 */
	// public List<Map> getBssPara(String username, String servTypeId)
	// {
	// StringBuffer sql = new StringBuffer();
	// sql.append(
	// "select sheet_para_desc from tab_egw_bsn_open_original where username='")
	// .append(username).append("'");
	// if ("10".equals(servTypeId))
	// {
	// sql.append(" and product_spec_id='22' ");
	// }else if ("11".equals(servTypeId)){
	// sql.append(" and product_spec_id='21' ");
	// }else if("16".equals(servTypeId)){
	// sql.append(" and product_spec_id='90' ");
	// } else{
	// sql.append(" and product_spec_id='14' ");
	// }
	// PrepareSQL psql = new PrepareSQL(sql.toString());
	// return jt.queryForList(psql.getSQL());
	// }
	/**
	 * 查询bss建设原始工单信息
	 */
	// public List<Map> getBssPara2(String username)
	// {
	// StringBuffer sql = new StringBuffer();
	// sql
	// .append(
	// "select sheet_para_desc from tab_egw_bsn_open_original where product_spec_id='20' and username='")
	// .append(username).append("'");
	// PrepareSQL psql = new PrepareSQL(sql.toString());
	// return jt.queryForList(psql.getSQL());
	// }
	/**
	 * 根据逻辑SN检查设备上报了没
	 */
	public String checkdevice(String username, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select device_serialnumber,device_id from tab_gw_device where device_id_ex='")
				.append(username).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() < 1){
			return "";
		}
		if (StringUtil.getStringValue(list.get(0), "device_id") == null){
			return "";
		}
		return StringUtil.getStringValue(list.get(0), "device_id");
	}

	/**
	 * 处理太长的端口字符串，进行分割换行
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
		if (input != null){
			return input.replaceAll(",", "\n");
		}
		return "";
	}

	/** begin add by chenjie(67371) 20111-5-11 **/
	public List<Map> getBssCustomerServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String userId, String devicetype, int curPage_splitPage,
			int num_splitPage, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, userId, curPage_splitPage,
				num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber, a.user_id,a.username,a.city_id,a.dealdate,(case when c.type_id='1' then 'E8-B' when c.type_id='7' then '悦me' else 'E8-C' end) as type_id"
				+ " from tab_hgwcustomer a, gw_cust_user_dev_type c where a.user_id=c.user_id");
		// and city_id=? and type_id=? and dealdate<=? and dealdate>=?
		boolean hasUser = userId != null ? true : false;
		// 如果有用户信息，既不使用时间查询
		if (!hasUser)
		{
			if (false == StringUtil.IsEmpty(startOpenDate1)){
				sql.append(" and a.dealdate>=").append(startOpenDate1);
			}
			if (false == StringUtil.IsEmpty(endOpenDate1)){
				sql.append(" and a.dealdate<=").append(endOpenDate1);
			}
		}
		if (false == StringUtil.IsEmpty(userId)){
			sql.append(" and a.user_id in ").append(userId);
		}
		sql.append(getCity(cityId));
		if (false == StringUtil.IsEmpty(devicetype) && !"0".equals(devicetype)){
			sql.append(" and c.type_id='" + devicetype + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)){
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("type_id", rs.getString("type_id"));
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}

	/**
	 * excel导出
	 */
	public List<Map> getBssCustomerServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String userId, String devicetype, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, userId });
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_serialnumber, a.user_id,a.username,a.city_id,a.dealdate,(case when c.type_id='1' then 'E8-B' when c.type_id='7' then '悦me' else 'E8-C' end) as type_id"
				+ " from tab_hgwcustomer a, gw_cust_user_dev_type c where a.user_id=c.user_id");
		// and city_id=? and type_id=? and dealdate<=? and dealdate>=?
		boolean hasUser = userId != null ? true : false;
		// 如果有用户信息，既不使用时间查询
		if (!hasUser)
		{
			if (false == StringUtil.IsEmpty(startOpenDate1)){
				sql.append(" and a.dealdate>=").append(startOpenDate1);
			}
			if (false == StringUtil.IsEmpty(endOpenDate1)){
				sql.append(" and a.dealdate<=").append(endOpenDate1);
			}
		}
		if (false == StringUtil.IsEmpty(userId)){
			sql.append(" and a.user_id in ").append(userId);
		}
		sql.append(getCity(cityId));
		if (false == StringUtil.IsEmpty(devicetype) && !"0".equals(devicetype)){
			sql.append(" and c.type_id='" + devicetype + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		for (int i = 0; i < list.size(); i++)
		{
			String city_id = StringUtil.getStringValue(list.get(i).get("city_id"));
			String city_name = StringUtil.getStringValue(cityMap.get(city_id));
			if (false == StringUtil.IsEmpty(city_name)){
				list.get(i).put("city_name", city_name);
			}else{
				list.get(i).put("city_name", "");
			}
			String sn = StringUtil.getStringValue(list.get(i),"device_serialnumber");
			if (false == StringUtil.IsEmpty(sn)){
				list.get(i).put("device_serialnumber", sn);
			}else{
				list.get(i).put("device_serialnumber", "");
			}
			list.get(i).put("dealdate", transDate(list.get(i).get("dealdate")));
		}
		/*
		 * List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
		 * + 1, num_splitPage, new RowMapper() {
		 * 
		 * public Object mapRow(ResultSet rs, int arg1) throws SQLException { Map<String,
		 * String> map = new HashMap<String, String>(); map.put("user_id",
		 * rs.getString("user_id")); map.put("username", rs.getString("username")); String
		 * city_id = rs.getString("city_id"); map.put("city_id", city_id); String
		 * city_name = StringUtil.getStringValue(cityMap.get(city_id)); if (false ==
		 * StringUtil.IsEmpty(city_name)) { map.put("city_name", city_name); } else {
		 * map.put("city_name", ""); } // 将dealdate转换成时间 try { long dealdate =
		 * StringUtil.getLongValue(rs.getString("dealdate")); DateTimeUtil dt = new
		 * DateTimeUtil(dealdate * 1000); map.put("dealdate", dt.getLongDate()); } catch
		 * (NumberFormatException e) { map.put("dealdate", ""); } catch (Exception e) {
		 * map.put("dealdate", ""); } map.put("type_id", rs.getString("type_id"));
		 * map.put("device_serialnumber", rs.getString("device_serialnumber")); return
		 * map; } }); cityMap = null;
		 */
		return list;
	}

	public int countBssCustomerServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String userId, String devicetype, int curPage_splitPage,
			int num_splitPage, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getBssSheetServInfo({},{},{},{},{},{},{},{})", new Object[] {
				cityId, startOpenDate1, endOpenDate1, userId, curPage_splitPage,
				num_splitPage });
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_hgwcustomer a, gw_cust_user_dev_type c where a.user_id=c.user_id");
		boolean hasUser = userId != null ? true : false;
		// 如果有用户信息，既不使用时间查询
		if (!hasUser)
		{
			if (false == StringUtil.IsEmpty(startOpenDate1)){
				sql.append(" and a.dealdate>=").append(startOpenDate1);
			}
			if (false == StringUtil.IsEmpty(endOpenDate1)){
				sql.append(" and a.dealdate<=").append(endOpenDate1);
			}
		}
		if (false == StringUtil.IsEmpty(userId)){
			sql.append(" and a.user_id in ").append(userId);
		}
		sql.append(getCity(cityId));
		if (false == StringUtil.IsEmpty(devicetype) && !"0".equals(devicetype)){
			sql.append(" and c.type_id='" + devicetype + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0){
			maxPage = total / num_splitPage;
		}else{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> getServByUser(String userId, String gw_type, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		String sql = "";
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql = "select a.device_serialnumber, c.serv_type_name, b.dealdate, b.open_status, a.user_id, a.oui, a.device_id,"
					+ " b.serv_type_id, b.serv_status, b.wan_type,m.device_model,t.specversion,t.hardwareversion,t.softwareversion,t.access_style_relay_id "
					+ " from tab_egwcustomer a, egwcust_serv_info b,tab_gw_serv_type c,tab_gw_device d,tab_devicetype_info t,gw_device_model m "
					+ " where a.user_id=b.user_id and b.serv_type_id=c.serv_type_id and d.device_id=a.device_id and d.devicetype_id=t.devicetype_id "
					+ " and d.device_model_id=m.device_model_id and a.user_id =?";
		}
		else
		{
			// 当用户类型为家庭或者家庭政企融合时，使用家庭处理逻辑
			sql = "select a.device_serialnumber, c.serv_type_name, b.dealdate, b.open_status, a.user_id, a.oui, a.device_id,"
					+ " b.serv_type_id, b.serv_status, b.wan_type,m.device_model,t.specversion,t.hardwareversion,t.softwareversion,t.access_style_relay_id "
					+ " from tab_hgwcustomer a, hgwcust_serv_info b,tab_gw_serv_type c,tab_gw_device d,tab_devicetype_info t,gw_device_model m "
					+ " where a.user_id=b.user_id and b.serv_type_id=c.serv_type_id and d.device_id=a.device_id and d.devicetype_id=t.devicetype_id "
					+ " and d.device_model_id=m.device_model_id and a.user_id =?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, Integer.parseInt(userId));
		servTypeMap = getServType(isRealtimeQuery);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("serv_type_name", rs.getString("serv_type_name"));
				map.put("dealdate", transDate(rs.getString("dealdate")));
				map.put("open_status", rs.getString("open_status"));
				map.put("user_id", String.valueOf(rs.getInt("user_id")));
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				map.put("serv_type_id", rs.getString("serv_type_id"));
				map.put("serv_status", rs.getString("serv_status"));
				map.put("wan_type", rs.getString("wan_type"));
				map.put("device_model", rs.getString("device_model"));
				map.put("specversion", rs.getString("specversion"));
				map.put("hardwareversion", rs.getString("hardwareversion"));
				map.put("softwareversion", rs.getString("softwareversion"));
				map.put("access_style_relay_id", rs.getString("access_style_relay_id"));
				return map;
			}
		});
		return list;
	}

	/**
	 * 更新用户的业务开通状态
	 * @param userId :用户ID； servTypeId:业务类型ID
	 */
	public int updateServOpenStatus(long userId, int servTypeId, String isRealtimeQuery)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("updateServOpenStatus({}, {})", userId, servTypeId);
		String strSQL = "update hgwcust_serv_info set open_status=0 "
				+ " where serv_status=1 and user_id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		if (0 != servTypeId){
			psql.append(" and serv_type_id=" + servTypeId);
		}
		return jt.update(psql.getSQL());
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
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	/**
	 * 1:PPPoE(桥接) 2:PPPoE(路由) 3:STATIC 4:DHCP
	 */
	private String transWanType(String wanType)
	{
		if ("1".equals(wanType)){
			return "PPPoE(桥接)";
		}else if ("2".equals(wanType)){
			return "PPPoE(路由)";
		}else if ("3".equals(wanType)){
			return "静态IP";
		}else if ("4".equals(wanType)){
			return "DHCP";
		}else if ("5".equals(wanType)){
			return "桥接+路由";
		}
		return "";
	}

	private String transWanTypeByRoute(String wanType, String allRoute)
	{
		final String ALL_ROUTE = "1";
		if(ALL_ROUTE.equals(allRoute)){
			return "PPPoE(全路由)";
		}else if ("1".equals(wanType)){
			return "PPPoE(桥接)";
		}else if ("2".equals(wanType)){
			return "PPPoE(路由)";
		}else if ("3".equals(wanType)){
			return "静态IP";
		}else if ("4".equals(wanType)){
			return "DHCP";
		}else if ("5".equals(wanType)){
			return "桥接+路由";
		}
		return "";
	}

	private String transServStatus(String servStatus)
	{
		if ("1".equals(servStatus)){
			return "开通";
		}else if ("2".equals(servStatus)){
			return "暂停";
		}else if ("3".equals(servStatus)){
			return "销户";
		}
		
		return "-";
	}

	@SuppressWarnings("unused")
	private String transIpType(String ipType)
	{
		if ("1".equals(ipType)){
			return "IPv4";
		}else if ("2".equals(ipType)){
			return "IPv6";
		}else if ("3".equals(ipType)){
			return "IPv4+IPv6";
		}else if ("6".equals(ipType)){
			return "LAFT6";
		}
		return "IPv4";
	}

	private String transIpType_1(String ipType)
	{
		String tempIpType = "";
		if ("1".equals(ipType)){
			tempIpType = "公网单栈";
		}else if ("2".equals(ipType)){
			tempIpType = "纯IPV6";
		}else if ("3".equals(ipType)){
			tempIpType = "公网双栈";
		}else if ("4".equals(ipType)){
			tempIpType = "DS-Lite";
		}else if ("6".equals(ipType)){
			tempIpType = "LAFT6";
		}else{
			tempIpType = "公网单栈";
		}
		return tempIpType;
	}

	/**
	 * 上网/iptv
	 */
	public Map<String, String> getNetIptvIssuedConfig(String gw_type, String user_id,
			String serv_type_id, String isRealtimeQuery, String serUsername)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		Map<String, String> map_1 = new HashMap<String, String>();
		String sql_1 = "select access_style_id from  tab_hgwcustomer where user_id="
				+ user_id;
		String sql_2 = null;
		if (gw_type.equals("1"))
		{
			sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from hgwcust_serv_info where  user_id="
					+ user_id
					+ " and serv_type_id="
					+ serv_type_id
					+ " and username='"
					+ serUsername + "'";
		}
		else if (gw_type.equals("2"))
		{
			sql_1 = "select access_style_id from  tab_egwcustomer where user_id="
					+ user_id;
			sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from egwcust_serv_info where  user_id="
					+ user_id
					+ " and serv_type_id="
					+ serv_type_id
					+ " and username='"
					+ serUsername + "'";
		}
		PrepareSQL psq_l = new PrepareSQL(sql_1);
		Map map = jt.queryForMap(psq_l.getSQL());
		String access_style_id = "";
		if (null != map){
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
	 */
	public Map<String, String> getVoipIssuedConfig(String gw_type, String user_id,
			String serv_type_id, String isRealtimeQuery, String serUsername)
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		Map<String, String> map_1 = new HashMap<String, String>();
		String sql_1 = "select access_style_id from  tab_hgwcustomer where user_id="+ user_id;
		String sql_2 = null;
		if (gw_type.equals("1"))
		{
			sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from   hgwcust_serv_info "
					+ "where  user_id="+user_id
					+ " and serv_type_id="+ serv_type_id
					+ " and username ='"+ serUsername + "'";
		}
		else if (gw_type.equals("2"))
		{
			sql_1 = "select access_style_id from  tab_egwcustomer where user_id="+ user_id;
			sql_2 = "select wan_type,vpiid,vciid,vlanid,username,passwd,bind_port,ipaddress,ipmask,gateway,adsl_ser from egwcust_serv_info "
					+ "where  user_id="+user_id
					+ " and serv_type_id="+ serv_type_id
					+ " and username ='"+ serUsername + "'";
		}
		PrepareSQL psq_l = new PrepareSQL(sql_1);
		Map map = jt.queryForMap(psq_l.getSQL());
		String access_style_id = "";
		if (null != map){
			access_style_id = StringUtil.getStringValue(map.get("access_style_id"));
		}
		PrepareSQL psq_2 = new PrepareSQL(sql_2);
		map_1 = jt.queryForMap(psq_2.getSQL());
		map_1.put("access_style_id", access_style_id);
		String sql_3 = "select protocol,voip_username,voip_passwd,voip_port,reg_id,reg_id_type,uri,sip_id "
						+ "from tab_voip_serv_param where user_id="+ user_id;
		if("2".equals(gw_type))
		{
			sql_3  = "select protocol,voip_username,voip_passwd,voip_port,reg_id,reg_id_type,uri,sip_id "
						+ "from tab_egw_voip_serv_param where user_id="+ user_id;
		}
		PrepareSQL psql_3 = new PrepareSQL(sql_3);
		Map map_2 = jt.queryForMap(psql_3.getSQL());
		String sip_id = "";
		if (null != map_2){
			sip_id = StringUtil.getStringValue(map_2.get("sip_id"));
		}
		map_1.putAll(map_2);
		if (!StringUtil.IsEmpty(sip_id))
		{
			String sql_4 = "select prox_serv,prox_port,stand_prox_serv,stand_prox_port,regi_serv,regi_port,stand_regi_serv,stand_regi_port,out_bound_proxy " 
							+ "from tab_sip_info where sip_id="+ sip_id;
			PrepareSQL psql_4 = new PrepareSQL(sql_4);
			Map map_3 = jt.queryForMap(psql_4.getSQL());
			map_1.putAll(map_3);
		}
		logger.warn("map_1:" + map_1.toString());
		return map_1;
	}

	/**
	 * 拆分策略表 <strategy_tabname> <serv>gw_serv_strategy_serv</serv>
	 * <soft>gw_serv_strategy_soft</soft> <batch>gw_serv_strategy_batch</batch>
	 * <strategy>gw_serv_strategy</strategy> </strategy_tabname>
	 */
	/*
	 * private String getTableName(){ String tableName =
	 * LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname");
	 * if(StringUtil.IsEmpty(tableName)){ tableName = "gw_serv_strategy"; } return
	 * tableName; }
	 */
	/**
	 * modify by litao 如果是家庭网关 ,寻找对应的策略,如果是企业网关则直接使用原来的业务策略表！
	 */
	private String getTableName(String gw_type)
	{
		if (!StringUtil.IsEmpty(gw_type) && gw_type.equals("1"))
		{
			String tableName = LipossGlobals.getLipossProperty("strategy_tabname.serv.tabname");
			/** 如果在策略表中没有找到对应的策略，则使用默认的gw_serv_strategy **/
			if (StringUtil.IsEmpty(tableName))
			{
				tableName = LipossGlobals.getLipossProperty("strategy_tabname.serv");
				if (StringUtil.IsEmpty(tableName)){
					tableName = "gw_serv_strategy";
				}
			}
			return tableName;
		}
		return "gw_serv_strategy";
	}

	/**
	 * 拆分策略日志表
	 */
	private String getTableNameLog(String gw_type)
	{
		return getTableName(gw_type)+ "_log";
	}

	/**
	 * get cmd list.
	 */
	public ArrayList<HashMap<String, String>> getSheetCmdList(String sheetId) 
	{
		String sql = "select * from tab_sheet_cmd where  sheet_id='"
				+ sheetId + "' order by rpc_order";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return DBOperation.getRecords(pSQL.getSQL());
	}

	/**
	 * get cmd list.
	 */
	public String getFaultDesc(String sheetId) 
	{
		String sql = "select fault_desc from tab_sheet_report where sheet_id='" + sheetId + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"fault_desc");
	}

	/**
	 * get cmd list.
	 */
	public String getSheetId(String strategyId) 
	{
		String sql = "select sheet_id from gw_serv_strategy_serv where id='" + strategyId + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"sheet_id");
	}

	public String getDeviceId(String devSN) 
	{
		String sql = "select device_id from tab_gw_device where device_serialnumber='" + devSN + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"device_id");
	}

	/**
	 * get cmd list.
	 */
	public String getLogSheetId(String strategyId) 
	{
		String sql = "select sheet_id from gw_serv_strategy_serv_log where id='" + strategyId + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"sheet_id");
	}

	/**
	 * get para by sheet_id and tc_serial
	 */
	public ArrayList<HashMap<String, String>> getSheetParaList(String sheetId, String rpcOrder) 
	{
		String sql = "select * from tab_sheet_para where sheet_id='" + sheetId
				+ "' and rpc_order=" + rpcOrder + " order by para_serial";
		if("1".equals(LipossGlobals.getLipossProperty("IsSplitTabSheetPara")))
		{
			sql = sql.replace("tab_sheet_para", "tab_sheet_para_" 
					+ new DateTimeUtil(StringUtil.getLongValue(sheetId.split("_")[0])).getYYYYMM());
		}
		PrepareSQL pSQL = new PrepareSQL(sql);
		return DBOperation.getRecords(pSQL.getSQL());
	}

	public List<Map> getUserByDeviceSN(String username, String gw_type, String isRealtimeQuery) 
	{
		setDataSourceType(isRealtimeQuery, this.getClass().getName());
		logger.debug("getUserByDeviceSN({},{})", username, gw_type);
		StringBuffer sql = new StringBuffer();
		if (Global.GW_TYPE_BBMS.equals(gw_type))
		{
			sql.append("select user_id from tab_egwcustomer a,TAB_GW_DEVICE b where a.device_id = b.device_id and b.device_serialnumber ='")
					.append(username).append("'");
		}
		else
		{
			String subName = username;
			if(username.length()>6) subName = username.substring(username.length()-6);
			// 当用户类型为家庭或者家庭政企融合时，用户信息都保持在该表中
			// select user_id from tab_egwcustomer a,TAB_GW_DEVICE b where a.device_id = b.device_id and b.device_serialnumber = 'HWTCD848EC9A'
			sql.append("select user_id from tab_hgwcustomer a,TAB_GW_DEVICE b where a.device_id = b.device_id and( b.device_serialnumber like '%")
					.append(username).append("'").append(" and b.dev_sub_sn='").append(subName).append("')");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public String getSheetParaById(String id) 
	{
		String sql = "select sheet_para from gw_serv_strategy_serv where id='" + id + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"sheet_para");
	}
	
	public String getSheetParaLogById(String id) 
	{
		String sql = "select sheet_para from gw_serv_strategy_serv_log where id='" + id + "'";
		PrepareSQL pSQL = new PrepareSQL(sql);
		return StringUtil.getStringValue(DBOperation.getRecord(pSQL.getSQL()),"sheet_para");
	}
	
	/**
	 * 获取属地
	 */
	private String getCity(String cityId)
	{
		StringBuffer sb=new StringBuffer();
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sb.append(" and a.city_id in ("+StringUtils.weave(cityIdList)+")");
			cityIdList = null;
		}
		return sb.toString();
	}

	/**
	 * 根据Loid获取详情 安徽联通更改资料开户时间
	 */
	public List<Map<String,String>> getBssUserInfoByLoid(String loid){
		String sqlBuild = "select user_id,username,city_id,device_serialnumber,oui,device_id,opendate,dealdate,open_status " +
				" from tab_hgwcustomer" +
				" where username='" + loid + "'";
		PrepareSQL psql = new PrepareSQL(sqlBuild);
		return jt.queryForList(psql.getSQL());
	}

	public int updateOpenDateByUserId(String userId, long opendateSec){
		String sqlStr= "update tab_hgwcustomer set opendate = " + opendateSec + " where user_id = '" + userId + "'";
		PrepareSQL psql = new PrepareSQL(sqlStr);
		return jt.update(psql.getSQL());
	}
}
