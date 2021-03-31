package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class ManuallyConfigurePortBusinessDAO extends SuperDAO 
{

	public int qyeryLoid(String loid) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_hgwcustomer where username =");
		sql.append("'" + loid + "'");
		sql.append("and device_id is not null");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}

	public int wlanNum(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.wlan_num as num  from tab_bss_dev_port a,tab_hgwcustomer b "
				+ "where a.id=b.spec_id and b.user_id=(select user_id from tab_hgwcustomer where username ='"
				+ loid + "')");
		return jt.queryForInt(psql.getSQL());
	}

	public int lanNum(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.lan_num as num  from tab_bss_dev_port a,tab_hgwcustomer b "
				+ "where a.id=b.spec_id and b.user_id=(select user_id from tab_hgwcustomer where username ='"
				+ loid + "')");
		return jt.queryForInt(psql.getSQL());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> internetList(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.bind_port,b.username,b.user_id,a.device_id,a.oui,a.device_serialnumber from tab_hgwcustomer a , "
				+ "hgwcust_serv_info b where a.user_id = b.user_id and a.username = '"
				+ loid + "' and b.serv_type_id=10");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				map.put("bind_port", rs.getString("bind_port"));
				map.put("username", rs.getString("username"));
				map.put("user_id", rs.getString("user_id"));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map> itvList(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.bind_port,b.username,b.user_id,a.device_id,a.oui,a.device_serialnumber from tab_hgwcustomer a , "
				+ "hgwcust_serv_info b where a.user_id = b.user_id and a.username = '"
				+ loid + "' and b.serv_type_id=11");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				map.put("bind_port", rs.getString("bind_port"));
				map.put("username", rs.getString("username"));
				map.put("user_id", rs.getString("user_id"));
				return map;
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map> tianyiList(String loid) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.bind_port,b.username,b.user_id,a.device_id,a.oui,a.device_serialnumber from tab_hgwcustomer a , "
				+ "hgwcust_serv_info b where a.user_id = b.user_id and a.username = '"
				+ loid + "' and b.serv_type_id=25");
		List<Map> list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				map.put("oui", rs.getString("oui"));
				map.put("device_serialnumber",
						rs.getString("device_serialnumber"));
				map.put("bind_port", rs.getString("bind_port"));
				map.put("username", rs.getString("username"));
				map.put("user_id", rs.getString("user_id"));
				return map;
			}
		});
		return list;
	}

	public int update(String id, String loid, String tempVal, int lan_Num,
			String userid, String username) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("update hgwcust_serv_info set bind_port='" + tempVal
				+ "',open_status=0,serv_num=" + lan_Num
				+ " where serv_type_id=" + id + "");
		if (!StringUtil.IsEmpty(userid)) {
			psql.append(" and user_id=" + userid + "");
		}
		if (!StringUtil.IsEmpty(userid)) {
			psql.append(" and username='" + username + "'");
		}
		return jt.update(psql.getSQL());
	}

	public List<HashMap<String, String>> getInternet(String deviceId) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port,b.sess_type,");
		sql.append("b.ip_type,b.conn_type,b.username ");
		sql.append("from gw_wan_conn a,gw_wan_conn_session b ");
		sql.append("where a.device_id=b.device_id and a.wan_id=b.wan_id ");
		sql.append("and a.wan_conn_id=b.wan_conn_id and b.serv_list='INTERNET' and a.device_id=? ");
		sql.setString(1, deviceId);
		return DBOperation.getRecords(sql.getSQL());
	}

	public List<HashMap<String, String>> getIPTV(String deviceId) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port,b.username ");
		sql.append("from gw_wan_conn a,gw_wan_conn_session b ");
		sql.append("where a.device_id=b.device_id and a.wan_id=b.wan_id ");
		sql.append("and a.wan_conn_id=b.wan_conn_id and a.device_id=? ");
		sql.append("and (b.serv_list='Other' or b.serv_list='OTHER') ");
		sql.setString(1, deviceId);
		return DBOperation.getRecords(sql.getSQL());
	}

	public List<HashMap<String, String>> getTianyi(String deviceId) 
	{
		PrepareSQL sql = new PrepareSQL();
		sql.append("select a.vpi_id,a.vci_id,a.vlan_id,b.bind_port,");
		sql.append("b.sess_type,b.ip_type,b.conn_type,b.username ");
		sql.append("from gw_wan_conn a,gw_wan_conn_session b ");
		sql.append("where a.device_id=b.device_id and a.wan_id=b.wan_id ");
		sql.append("and a.wan_conn_id=b.wan_conn_id and b.serv_list='INTERNET' and a.device_id=? ");
		sql.setString(1, deviceId);
		return DBOperation.getRecords(sql.getSQL());
	}

	public String getUserName(String tempUserid, String vlId) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select username from hgwcust_serv_info ");
		psql.append("where user_id="+tempUserid+" and serv_type_id=10 and vlanid='" + vlId + "'");
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0) {
			Map map = (Map) list.get(0);
			String username = (String) map.get("username");
			if (username != null) {
				return username;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public int realwlanNum(String loid) 
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.wlan_num as num " +
				"from tab_gw_device a,tab_devicetype_info b,tab_bss_dev_port c,tab_hgwcustomer d "
				+ "where a.devicetype_id = b.devicetype_id and b.spec_id = c.id "
				+ "and a.device_id = d.device_id " +
				"and d.user_id=(select user_id from tab_hgwcustomer where username ='"+ loid + "')");
		return jt.queryForInt(psql.getSQL());
	}

	public int reallanNum(String loid) 
	{
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		PrepareSQL psql = new PrepareSQL();
		psql.append("select c.lan_num as num from " +
				"tab_gw_device a,tab_devicetype_info b,tab_bss_dev_port c,tab_hgwcustomer d " +
				"where a.devicetype_id = b.devicetype_id and b.spec_id = c.id  "
				+ "and a.device_id = d.device_id " +
				"and d.user_id=(select user_id from tab_hgwcustomer where username ='"+ loid + "')");
		return jt.queryForInt(psql.getSQL());
	}

}
