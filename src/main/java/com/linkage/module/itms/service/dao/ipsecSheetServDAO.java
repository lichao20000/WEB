
package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.service.act.ipsecSheetServACT;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-26
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ipsecSheetServDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(ipsecSheetServACT.class);
	private Map<String, String> cityMap = null;
	private Map<String, String> bssdevMap = null;
	/**
	 * 根据loid查询用户
	 * 
	 * @param username
	 * @param type
	 * @return
	 */
	public List<Map> getUserBySN(String username, String type)
	{
		String tableName = "";
		if (type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from " + tableName + " where username='")
				.append(username).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据宽带账号查询用户
	 * 
	 * @param username
	 * @param type
	 * @return
	 */
	public List getUserByServ(String username, String type)
	{
		String tableName = "";
		if (type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id from " + tableName + " where username='")
				.append(username).append("'");
		sql.append(" and serv_type_id=10");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	public List<Map> getIpsecSheetServInfo(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username, String openstatus,
			int curPage_splitPage, int num_splitPage, final String type, String userId)
	{
		StringBuffer sql = new StringBuffer();
		String tableName = "";
		if (type.equals("1")){
			tableName="tab_hgwcustomer";
		}else if(type.equals("2")){
			tableName = " tab_egwcustomer ";
		}
		
		sql.append("select a.user_id,a.username,a.city_id,a.spec_id,a.device_serialnumber,");
		sql.append("a.oui,a.device_id,b.serv_status,b.open_date,b.serv_type_id,");
		sql.append("b.updatetime,b.open_status,b.username as serUsername ");
		sql.append("from "+tableName+" a left join tab_ipsec_serv_param b");
		sql.append(" on (a.user_id=b.user_id  and b.serv_type_id=27)  where 1 = 1");
		
		if (!StringUtil.IsEmpty(userId))
		{
			sql.append(" and a.user_id='").append(userId).append("'");
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and b.open_date>='").append(startOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and b.open_date<='").append(endOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(openstatus))
		{
			sql.append(" and b.open_status=").append(openstatus);
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
		bssdevMap = Global.G_BssDev_PortName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				String spec_id = rs.getString("spec_id");
				String spec_name = StringUtil.getStringValue(bssdevMap.get(spec_id));
				map.put("spec_name", spec_name);
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				String tempserusername = rs.getString("serusername");
				map.put("serUsername", tempserusername);
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				String tmp = "ipsec";
				map.put("serv_type", tmp);
				map.put("opendate", transDate(rs.getString("open_date")));
				map.put("open_status", rs.getString("open_status"));
				map.put("serv_status", rs.getString("serv_status"));
				map.put("gw_type", type);
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	/**
	 * 获取某台设备信息
	 */
	public List<Map<String,String>> getSingleDeviceInfo(String device_id,long m_AreaId)
	{
		PrepareSQL psql=new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select a.oui,a.vendor_id,a.device_id,a.device_serialnumber,a.cpe_mac,");
			psql.append("a.spec_name,a.loopback_ip,a.complete_time,a.online_status,");
			psql.append("a.device_name,a.devicetype_id,a.device_status,a.maxenvelopes,");
			psql.append("a.gw_type,a.city_id,a.device_type,a.customer_id,a.device_url,a.device_area_id");
			psql.append("? as area_id,b.spec_id ");
		}else{
			psql.append("select a.*,? as area_id,b.spec_id ");
		}
		psql.append("from tab_gw_device a ");
		psql.append("left join tab_devicetype_info b on (a.devicetype_id=b.devicetype_id) ");
		psql.append("where a.device_id=? ");
		psql.setLong(1, m_AreaId);
		psql.setString(2, device_id);
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取在线状态
	 * @param device_id
	 * @return
	 */
	public List<Map<String,String>> getonline_status(String device_id)
	{
		PrepareSQL psql=new PrepareSQL("select online_status,last_time,oper_time from gw_devicestatus where device_id=?");
		psql.setString(1, device_id);
		return jt.queryForList(psql.getSQL());
	}
	
	public List<Map<String,String>> getTabNetServParamByUserId(String user_id)
	{
		PrepareSQL psql=new PrepareSQL(" select ip_type from tab_net_serv_param where user_id =?");
		psql.setString(1, user_id);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 获取设备开通的业务信息
	 * @param vendor_id
	 * @param devicetype_id
	 * @return
	 */
	public List<Map<String,String>> getServiceStatByDevice(String device_id){
		PrepareSQL psql=new PrepareSQL("select b.serv_type_id, a.serv_state,b.serv_type_name " +
				"from gw_dev_serv a, tab_gw_serv_type b "
				+ "where a.device_id = ? and a.serv_type_id = b.serv_type_id");
		psql.setString(1, device_id);
		return jt.queryForList(psql.getSQL());
	}
	public List<Map<String,String>> getDeviceModelVersion(String vendor_id,String devicetype_id)
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		PrepareSQL psql=new PrepareSQL(" select devicetype_id,vendor_name,specversion,hardwareversion," +
				"softwareversion,is_normal,is_check,device_model " +
				"from tab_devicetype_info a, tab_vendor b, gw_device_model c "
				+ " where devicetype_id=? and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id");
		psql.setStringExt(1, devicetype_id, false);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 获取设备的用户信息(用户名，帐号，电话，地址，Email)
	 */
	public  List<Map<String,String>> getCustomerOfDev(String device_id, String gw_type)
	{
		String tab_name = "tab_hgwcustomer";
		 if (gw_type == null || gw_type.equals(""))
		 {
		 tab_name = "tab_hgwcustomer";
		 }
		 else
		 {
		 if (gw_type.equals("1") || gw_type.equals("3") )
		 {
		 tab_name = "tab_hgwcustomer";
		 }
		 else
		 {
		 tab_name = "tab_egwcustomer";
		 }
		 }
		String sql=" select user_id,username,serv_type_name as service_name from "
				+ tab_name
				+ "  a,tab_gw_serv_type b where a.serv_type_id=b.serv_type_id and device_id=? and user_state in('1','2')";
		PrepareSQL psql=new PrepareSQL(sql.toString());
		psql.setString(1, device_id);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 查询客户名称
	 */
	public List<Map<String,String>> getCustomerName(String e_id)
	{
		PrepareSQL psql=new PrepareSQL(" select customer_name from tab_customerinfo where customer_id =?");
		psql.setString(1, e_id);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 获取域id与名称映射关系
	 */
	public List<Map<String,String>> getAreaIdMapName()
	{
		PrepareSQL psql=new PrepareSQL();
		psql.append("select area_id,area_name from tab_area");
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 查询当前配置参数
	 */
	public List<Map<String,String>> x_username(String device_id)
	{
		PrepareSQL psql=new PrepareSQL(" select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,acs_username,acs_passwd from tab_gw_device where device_id =?");
		psql.setString(1, device_id);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 检测在线状态
	 */
	public List<Map<String,String>> queryConnCondition(String deviceid)
	{
		PrepareSQL psql=new PrepareSQL("select online_status from gw_devicestatus where 1=1 and device_id =?");
		psql.setString(1, deviceid);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 获取某厂商某设备型号的模板
	 */
	public List<Map<String,String>> getDeviceModelTemplate(String oui, String devicetype_id)
	{
		PrepareSQL psql=new PrepareSQL();
		psql.append("select template_id,template_name,template_desc from tab_template where devicetype_id=? order by template_id desc");
		psql.setString(1, devicetype_id);
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 更新用户的业务开通状态
	 * @param userId
	 * @param servTypeId
	 * @param isRealtimeQuery
	 * @return
	 */
	public int updateServOpenStatus(long userId, int servTypeId)
	{
		logger.debug("updateServOpenStatus({}, {})", userId, servTypeId);
		// 更新SQL语句
		String strSQL = "update hgwcust_serv_info set open_status=0 "
				+ " where serv_status=1 and user_id=? ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, userId);
		if (0 != servTypeId)
		{
			psql.append(" and serv_type_id=" + servTypeId);
		}
		// 执行查询
		int reti = jt.update(psql.getSQL());
		return reti;
	}
	/**
	 * 导出
	 * @param cityId
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param usernameType
	 * @param username
	 * @param openstatus
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param type
	 * @param userId
	 * @return
	 */
	public List<Map> getIpsecExcel(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username, String openstatus, String type, String userId)
	{
		StringBuffer sql = new StringBuffer();
		String tableName = "";
		if (type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		sql.append("select a.user_id,a.username,a.city_id,a.spec_id,a.device_serialnumber,a.oui,a.device_id,b.serv_status,b.open_date,b.serv_type_id,");
		sql.append(" b.updatetime,b.open_status,b.username as serUsername from "+tableName+" a left join tab_ipsec_serv_param b");
		sql.append(" on (a.user_id=b.user_id  and b.serv_type_id=27)  where 1 = 1");
		
		if (!StringUtil.IsEmpty(userId))
		{
			sql.append(" and a.user_id='").append(userId).append("'");
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and b.open_date>='").append(startOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and b.open_date<='").append(endOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(openstatus))
		{
			sql.append(" and b.open_status=").append(openstatus);
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
		bssdevMap = Global.G_BssDev_PortName_Map;
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", rs.getString("user_id"));
				map.put("username", rs.getString("username"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name))
				{
					map.put("city_name", city_name);
				}
				else
				{
					map.put("city_name", "");
				}
				String spec_id = rs.getString("spec_id");
				String spec_name = StringUtil.getStringValue(bssdevMap.get(spec_id));
				map.put("spec_name", spec_name);
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("oui", rs.getString("oui"));
				map.put("device_id", rs.getString("device_id"));
				String tempserusername = rs.getString("serusername");
				map.put("serUsername", tempserusername);
				String serv_type_id = rs.getString("serv_type_id");
				map.put("serv_type_id", serv_type_id);
				String tmp = "ipsec";
				map.put("serv_type", tmp);
				map.put("opendate", transDate(rs.getString("open_date")));
				if(rs.getString("open_status").equals("1"))
				{
					map.put("open_status", "成功");
				}else if(rs.getString("open_status").equals("0"))
				{
					map.put("open_status", "未做");
				}
				else if(rs.getString("open_status").equals("-1"))
				{
					map.put("open_status", "失败");
				}else
				{
					map.put("open_status", "其他");
				}
				map.put("serv_status", rs.getString("serv_status"));
				return map;
			}
		});
		cityMap = null;
		return list;
	}
	/**
	 * 查询用户类型
	 */
	public List<Map> getUserDevType(String user_id)
	{
		StringBuffer sql=new StringBuffer();
		sql.append(" select b.type_name from gw_cust_user_dev_type a,gw_dev_type b where a.type_id=b.type_id ");
		if(!StringUtil.IsEmpty(user_id))
		{
			sql.append(" and a.user_id='"+user_id+"'");
		}
		PrepareSQL psql=new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	/**
	 * 分页
	 * @param cityId
	 * @param startOpenDate1
	 * @param endOpenDate1
	 * @param usernameType
	 * @param username
	 * @param openstatus
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param type
	 * @param userId
	 * @return
	 */
	public int getquerypaging(String cityId, String startOpenDate1,
			String endOpenDate1, String usernameType, String username, String openstatus,
			int curPage_splitPage, int num_splitPage, String type, String userId)
	{
		StringBuffer sql = new StringBuffer();
		String tableName = "";
		if (type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		sql.append("select count(*) from  "+tableName+" a left join tab_ipsec_serv_param b on (a.user_id=b.user_id  and b.serv_type_id=27)  where 1 = 1"); 
		if (!StringUtil.IsEmpty(userId))
		{
			sql.append(" and a.user_id='").append(userId).append("'");
		}
		if (!StringUtil.IsEmpty(startOpenDate1))
		{
			sql.append(" and b.open_date>='").append(startOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(endOpenDate1))
		{
			sql.append(" and b.open_date<='").append(endOpenDate1).append("'");
		}
		if (!StringUtil.IsEmpty(openstatus))
		{
			sql.append(" and b.open_status=").append(openstatus);
		}
		if (!StringUtil.IsEmpty(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		int total = list.size();
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
	/**
	 * 查询用户基本信息
	 * @param seconds
	 * @return
	 */
	public List<Map<String,Object>> queryUserRelatedBaseInfo(String userId,String gw_type)
	{
		StringBuffer sql = new StringBuffer();
		String tableName="";
		if (gw_type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(gw_type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		sql.append("select a.username,a.user_id,a.realname,a.user_state,a.user_type_id,");
		sql.append("a.cred_type_id,a.credno,a.city_id,a.office_id,a.device_ip,a.dealdate,");
		sql.append("a.opendate,a.onlinedate,a.pausedate,a.closedate,a.updatetime,");
		sql.append("a.is_active,a.email,a.linkphone,a.linkaddress,a.address,a.cust_type_id,");
		sql.append("a.device_id,a.oui, a.device_serialnumber,a.binddate,a.userline,a.is_chk_bind,");
		sql.append("b.cpe_mac,ac.type_name,c.spec_name ");
		sql.append("from gw_access_type ac,tab_bss_dev_port c,"+tableName+" a ");
		sql.append("left join tab_gw_device b on a.device_id=b.device_id ");
		sql.append("where ac.type_id=a.access_style_id and a.spec_id=c.id ");
		
		if(!StringUtil.IsEmpty(userId))
		{
			sql.append(" a.user_id='"+userId+"'");
		}
		PrepareSQL psql=new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 用户当前拥有业务
	 */
	public List<Map<String,Object>> getServiceInfo(String user_id,String gw_type)
	{
		String tableName = null;
		if (gw_type.equals("1"))
		{
			tableName = "hgwcust_serv_info";
		}else if(gw_type.equals(""))
		{
			tableName = "egwcust_serv_info";
		}
		StringBuffer sql=new StringBuffer();
		sql.append("select a.serv_type_id,a.serv_type_name,b.username,b.wan_type,b.serv_status,b.passwd ");
		sql.append("from tab_gw_serv_type a, "+tableName+" b where a.serv_type_id=b.serv_type_id ");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public Map<String, String> getServType()
	{
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
	 * 查询宽带上网工单详情
	 */
	public List<Map<String,Object>> getIpsecInfeoInternet(String user_id,String gw_type)
	{
		String tableName = null;
		if (gw_type.equals("1"))
		{
			tableName = "hgwcust_serv_info";
		}else if(gw_type.equals(""))
		{
			tableName = "egwcust_serv_info";
		}
		StringBuffer sql=new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select b.username,b.serv_type_id,b.serv_status,b.enable ");
			sql.append("b.request_id,b.ipsec_type,b.remote_domain,b.remote_subnet,");
			sql.append("b.local_subnet,b.remote_ip,b.exchange_mode,b.ike_auth_algorithm,");
			sql.append("b.ike_auth__method,b.ike_encryption_algorithm,b.ike_dhgroup,b.ike_idtype,");
			sql.append("b.ike_localname,b.ike_remotename,b.ike_presharekey,b.ipsec_out_interface,");
			sql.append("b.ipsec_encapsulation_mode,b.ipsec_transform,b.esp_auth_algorithem,");
			sql.append("b.esp_encrypt_algorithm,b.ipsec_pfs,b.ike_saperiod,b.ipsec_satime_period,");
			sql.append("b.ipsec_satraffic_period,b.ah_auth_algorithm,b.dpd_enable,b.dpd_threshold,");
			sql.append("b.dpd_retry,b.open_date,b.updatetime,b.completedate,b.open_date,b.open_status ");
		}else{
			sql.append("select b.* ");
		}
		sql.append("from "+tableName+" a,tab_ipsec_serv_param b where 1=1");
		sql.append(" and a.user_id=b.user_id");
		if(!StringUtil.IsEmpty(user_id))
		{
			sql.append(" and a.user_id='"+user_id+"'");
		}
		PrepareSQL psql=new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	/**
	 * 取得局向标识
	 */
	public List<Map<String,String>> getofficename(String office_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select office_id,office_name from tab_office where office_id='"+office_id+"' order by office_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	return jt.queryForList(psql.getSQL());
	}
	/**
	 * 得到所选用户的管理域
	 * 
	 * 
	 * @return
	 */
	public List<Map> getUserArea(String user_id,String gw_type) {

		StringBuffer sql = new StringBuffer();
		String tableName="";
		if (gw_type.equals("1"))
		{
			tableName="tab_hgwcustomer";
		}else if(gw_type.equals("2"))
		{
			tableName = " tab_egwcustomer ";
		}
		sql.append("select a.area_name from tab_area a, tab_gw_res_area b "
				+ " where a.area_id = b.area_id and b.res_type = 1 and b.res_id = (select d.device_id from "
				+ tableName+" c,tab_gw_device d where c.user_id ="
				+ user_id
				+ " and c.device_id=d.device_id)");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	return jt.queryForList(psql.getSQL());
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
	public Map<String, String> getCityMap()
	{
		return cityMap;
	}

	
	public void setCityMap(Map<String, String> cityMap)
	{
		this.cityMap = cityMap;
	}

	
	public Map<String, String> getBssdevMap()
	{
		return bssdevMap;
	}

	
	public void setBssdevMap(Map<String, String> bssdevMap)
	{
		this.bssdevMap = bssdevMap;
	}
	
}
