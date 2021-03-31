
package com.linkage.module.itms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Base64;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChangeConnectionTypeDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ChangeConnectionTypeDAO.class);

	/**
	 * 判断该设备是否绑定用户
	 * 
	 * @param deviceId
	 * @return
	 * 
	 * 注释 by zhangchy 2011-08-24 为了精简SQL，减轻对数据库的压力，此处的判断设备是否绑定用户已经不再使用，此功能合并到下面的checkService() 方法中
	 */
//	public int checkBind(String deviceId){
//		logger.debug("checkBind({})", deviceId);
//		String strSQL = "select cpe_allocatedstatus from tab_gw_device where device_id='"   // 注释by zhangchy 2011-08-23
//				+ deviceId + "'";
//		PrepareSQL psql = new PrepareSQL(strSQL);
//		return jt.queryForInt(psql.getSQL());
//	}
	
	private String vlanId;
	
	/**
	 * 宁夏电信查询方法  需要带vlanid查询
	 * @param device_id
	 * @param gw_type
	 * @param vlanId
	 * @return
	 */
	public List checkService(String device_id, String gw_type, String vlanId) {
		this.vlanId = vlanId;
		return checkService(device_id, gw_type);
	}

	/**
	 * 用于判断设备是否绑定用户，以及通过wan_type来判断改用户是否有桥接上网业务
	 * 
	 * modify by zhangchy 2011-08-24 
	 * 
	 */
	public List checkService(String device_id, String gw_type)
	{
		logger.debug("checkService({})", device_id);
//		String strSQL = "select a.access_style_id,b.vpiid,b.vciid,b.vlanid from tab_hgwcustomer a,hgwcust_serv_info b "
//				+ " where a.user_id=b.user_id and b.serv_type_id=10 and a.device_id='"
//				+ deviceId + "'";
		if(null == device_id || "".equals(device_id)){
			return null;
		}
		PrepareSQL psql;
		

		if(gw_type.equals(Global.GW_TYPE_ITMS))
		{
			psql = new PrepareSQL("select a.access_style_id, b.vpiid, b.vciid, b.vlanid, b.username, b.passwd, b.wan_type, b.user_id "+
											 "  from tab_hgwcustomer a, hgwcust_serv_info b " +
											 " where a.user_id = b.user_id " +
											 "   and b.serv_type_id = 10 " +
											 "   and a.device_id = '"+device_id+"'");
		}
		else
		{
			psql = new PrepareSQL("select a.access_style_id, b.vpiid, b.vciid, b.vlanid, b.username, b.passwd, b.wan_type, b.user_id "+
					 "  from tab_egwcustomer a, egwcust_serv_info b " +
					 " where a.user_id = b.user_id " +
					 "   and b.serv_type_id = 10 " +
					 "   and a.device_id = '"+device_id+"'");
		}
		if (LipossGlobals.inArea(Global.NXDX)) {
			psql.append(" and b.vlanid = '" + vlanId + "'");
		}
		final StringBuffer vlanIds = new StringBuffer();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				vlanIds.append(StringUtil.getStringValue(rs.getString("vlanid"))).append("|");
				map.put("access_style_id",StringUtil.getStringValue(rs.getInt("access_style_id")));
				map.put("vpiid", StringUtil.getStringValue(rs.getString("vpiid")));
				map.put("vciid", StringUtil.getStringValue(rs.getInt("vciid")));
				map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				
				String passwad = StringUtil.getStringValue(rs.getString("passwd"));
				if(LipossGlobals.inArea(Global.NXDX) && !StringUtil.IsEmpty(passwad)){
					passwad = Base64.decode(passwad);
				} 
				map.put("passwd", passwad);
				map.put("wan_type", StringUtil.getStringValue(rs.getInt("wan_type")));
				map.put("user_id", StringUtil.getStringValue(rs.getString("user_id")));
				return map;
			}
		});
		if (list != null && list.size() > 0) {
			list.get(0).put("vlanIds", vlanIds.toString());
		}
		return list;
	}
	
	
	public List<Map> getServInfo(String deviceId) {
		logger.debug("checkService({})", deviceId);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.username as loid, b.username, b.vlanid, b.wan_type, b.user_id ");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b ");
		psql.append(" where a.user_id = b.user_id and b.serv_type_id = 10");
		psql.append(" and a.device_id = '" + deviceId + "'");
		psql.append(" and b.vlanid != '82'");
		
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				int wanType = rs.getInt("wan_type");
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("vlanid", StringUtil.getStringValue(rs.getString("vlanid")));
				map.put("wan_type", StringUtil.getStringValue(wanType));
				map.put("user_id", StringUtil.getStringValue(rs.getString("user_id")));
				switch (wanType) {
				case 1:
					map.put("wan_type_str", "桥接");
					break;
				case 2:
					map.put("wan_type_str", "路由");
					break;
				case 3:
					map.put("wan_type_str", "STATIC");
					break;
				default:
					map.put("wan_type_str", "DHCP");
					break;
				}
				return map;
			}
		});
		return list;
	}

	public int updatePvc(String user_id, String routeAccount, String routePasswd,String pvc,String vlan,String bindPort, String gw_type,String connType){
		logger.debug("checkService({})", user_id);
		
		if(!"".equals(user_id) && null!=user_id){
			String[] pvcArr = null;
			if(!"".equals(pvc) && null != pvc){
				pvcArr = pvc.split("/");
			}
			
			StringBuffer sqlBuffer = new StringBuffer();
			if(gw_type.equals(Global.GW_TYPE_ITMS))
            {
				sqlBuffer.append("update hgwcust_serv_info ")
						 .append("   set wan_type ="+ connType +",open_status = 0");
            }else
            {
            	sqlBuffer.append("update egwcust_serv_info ")
				 .append("   set wan_type = "+ connType +",open_status = 0");
            }
			
			if(!"".equals(bindPort) && null != bindPort){
				sqlBuffer.append(",bind_port = '"+bindPort+"'");
			}
			if(!"".equals(routeAccount) && null != routeAccount){
				sqlBuffer.append(",username = '"+routeAccount+"'");
			}
			if(!"".equals(routePasswd) && null != routePasswd){
				if (LipossGlobals.inArea(Global.NXDX)) {
					routePasswd = Base64.encode(routePasswd);
				}
				sqlBuffer.append(",passwd = '"+routePasswd+"'");
			}
			if(pvcArr != null && pvcArr.length==2){
				if(!"".equals(StringUtil.getStringValue(pvcArr[0])) && null != StringUtil.getStringValue(pvcArr[0])){
					sqlBuffer.append(",vpiid = '"+StringUtil.getStringValue(pvcArr[0])+"'");
				}
				if(!"".equals(StringUtil.getStringValue(pvcArr[1])) && null != StringUtil.getStringValue(pvcArr[1])){
					sqlBuffer.append(",vciid = "+StringUtil.getStringValue(pvcArr[1]));
				}
			}
			if(!"".equals(vlan) && null != vlan){
				sqlBuffer.append(",vlanid = '"+vlan+"'");
			}
			
			sqlBuffer.append(" where serv_type_id = 10 and user_id = "+user_id);
			sqlBuffer.append(" and vlanid = '"+vlan+"'");
			
			PrepareSQL pSQL = new PrepareSQL(sqlBuffer.toString());
			
			return jt.update(pSQL.getSQL());
		}else{
			logger.warn("操作失败！user_id为空值或为null！");
			return -1;
		}
		
	}
	public List getPVC(String cityId)
	{
		logger.debug("getPVC()");
		String strSQL = "select convert(varchar(4),vpi_id)+'/'+convert(varchar(4),vci_id) pvc from r_dev_pre_conf  ";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select to_char(vpi_id,'9999') || '/' || to_char(vci_id,'9999') pvc from r_dev_pre_conf  ";
		}
		
		if(DBUtil.GetDB()==3){
			strSQL = "select concat(cast(vpi_id as char),'/',cast(vci_id as char)) pvc from r_dev_pre_conf  ";
		}
		
		strSQL = strSQL + " where access_type='DSL' and serv_name='INTERNET' and city_id='"
				+ CityDAO.getLocationCityIdByCityId(cityId) + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		List<String> list = jt.query(psql.getSQL(), new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("pvc", rs.getString("pvc"));
				return map;
			}
		});
		return list;
	}
	
	/**
	 * 获取WLAN
	 * 
	 * @param device_id
	 * @return
	 */
	public List getData(String deviceId)
	{
		logger.debug("WanObj[] getWlan({})", deviceId);
		if (deviceId == null)
		{
			logger.debug("deviceId == null");
			return null;
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select lan_wlan_id,lan_id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_lan_wlan where device_id='" + deviceId + "'");
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 终端版本是否支持路由业务
	 * 
	 * @param devicetypeId
	 * @return
	 */
	
	public String getRouteData(String deviceId){
		logger.debug("getRouteData()");
		
		String sql = "select v.is_route from tab_gw_device t, tab_route_version v where t.devicetype_id = v.devicetype_id and t.device_id='"+deviceId+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String,String> map = DBOperation.getRecord(psql.getSQL());
		String route = null;
		if(null==map||map.isEmpty()){
			route = "0";
		}else{
			route = map.get("is_route");
		}
		return route;
	}
	
	

}
