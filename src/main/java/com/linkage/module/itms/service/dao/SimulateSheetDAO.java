package com.linkage.module.itms.service.dao;

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
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author zhangshimin(工号)
 * @version 1.0
 * @since 2011-5-26 上午09:42:22
 * @category com.linkage.module.itms.service.dao
 * @copyright 南京联创科技 网管科技部
 * 
 */
public class SimulateSheetDAO extends SuperDAO {
	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(SimulateSheetDAO.class);

	public List<Map<String, String>> getUserInfo(String username, String gw_type) {
		logger.debug("getUserInfo({})", username);
		StringBuffer sql = new StringBuffer();
		if (gw_type.equals("1")) {
			sql.append(
					"select access_style_id from tab_hgwcustomer where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		} else if (gw_type.equals("2")) {
			sql.append(
					"select access_style_id from tab_egwcustomer where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.query(psql.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderType", rs.getString("access_style_id"));
				return map;
			}
		});
		return list;
	}

	public int qyeryLoid(String loid) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_hgwcustomer where username='" + loid + "'");
		
		return jt.queryForInt(psql.getSQL());
	}
	
	public Map<String, String> qryLoid4CQ(String loid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.access_style_id,a.city_id,b.type_id ");
		sql.append("from tab_hgwcustomer a,gw_cust_user_dev_type b ");
		sql.append("where a.user_id=b.customer_id and a.username='" + loid + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return DBOperation.getRecord(psql.getSQL());
	}

	public int checkIptvUser(String iptvUserName, String loid) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_hgwcustomer a,hgwcust_serv_info b where "
				+ "a.user_id = b.user_id and a.username = '"
				+ loid
				+ "' and b.username = '" + iptvUserName + "'");
		psql.append("and b.serv_type_id = 11");
		
		return jt.queryForInt(psql.getSQL());
	}

	public void insertSheet(String id, String username, String serv_account,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id,String occ_ip) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log (id,username,serv_account,city_id,serv_type_id,"
				+ "oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(1, id);
		psql.setString(2, username);
		psql.setString(3, serv_account);
		psql.setString(4, city_id);
		psql.setInt(5, serv_type_id);
		psql.setInt(6, oper_type);
		psql.setInt(7, result_id);
		psql.setString(8, result_desc);
		psql.setLong(9, oper_time);
		psql.setLong(10, occ_id);
		psql.setString(11, occ_ip);
		jt.update(psql.getSQL());
	}
	
	
	public void insertSheet4CQ(String id, String username,
			String city_id, int serv_type_id, int oper_type, int result_id,
			String result_desc, long oper_time, long occ_id,String occ_ip) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("insert into tab_handsheet_log (id,username,city_id,serv_type_id,"
				+ "oper_type,result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?) ");
		psql.setString(1, id);
		psql.setString(2, username);
		psql.setString(3, city_id);
		psql.setInt(4, serv_type_id);
		psql.setInt(5, oper_type);
		psql.setInt(6, result_id);
		psql.setString(7, result_desc);
		psql.setLong(8, oper_time);
		psql.setLong(9, occ_id);
		psql.setString(10, occ_ip);
		jt.update(psql.getSQL());
	}
	
	
	
	
	public Map<String, String> queryUserInfo(int userType, String username) {
		logger.debug("queryUserInfo({})", username);
		if (StringUtil.IsEmpty(username)) {
			logger.error("username is Empty");
			return null;
		}
		String table_customer = "tab_hgwcustomer";
		String table_serv_info = "hgwcust_serv_info";
		String table_voip = "tab_voip_serv_param";

		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.username,a.device_id,a.oui,a.device_serialnumber,a.city_id,a.userline,a.access_style_id");
		
		switch (userType) {
		case 1:
			psql.append(" from " + table_customer + " a, " + table_serv_info + " b ");
			psql.append(" where a.user_id=b.user_id and b.serv_status= 1");
			psql.append(" and b.username='" + username + "' and b.serv_type_id = 10  order by a.updatetime desc ");
			break;
		case 3:
			psql.append(" from " + table_customer + " a, " + table_serv_info + " b ");
			psql.append(" where a.user_id=b.user_id and b.serv_status= 1");
			psql.append(" and b.username='" + username + "' and b.serv_type_id = 11  order by a.updatetime desc ");
			break;
		case 4:
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			}*/
			psql.append(" from " + table_customer + " a," + table_serv_info + " b," + table_voip + " c");
			psql.append(" where a.user_id=b.user_id and b.user_id=c.user_id");
			psql.append(" and c.voip_phone='" + username + "'  order by a.updatetime desc ");
			break;
		case 5:
			/**	if(DBUtil.GetDB()==3){
				//TODO wait
			}else{
				
			}*/
			psql.append(" from " + table_customer + " a," + table_serv_info + " b," + table_voip + " c");
			psql.append(" where a.user_id=b.user_id and b.user_id=c.user_id");
			psql.append(" and c.voip_username='" + username + "'  order by a45.updatetime desc ");
			break;
		default:
			psql.append(" from " + table_customer + " a where a.user_state = '1'");
			psql.append(" and a.device_serialnumber like '%" + username + "'");
		}
		return DBOperation.getRecord(psql.getSQL());
	}
	
	
	public String qryVoipPort(String sipuri, String loid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select p.voip_port from tab_voip_serv_param p,tab_hgwcustomer a ");
		sql.append("where p.voip_phone='"+sipuri+"' and a.user_id=p.user_id and a.username='"+loid+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		Map res = DBOperation.getRecord(psql.getSQL());
		return StringUtil.getStringValue(res, "voip_port", "");
	}
}
