package com.linkage.module.itms.service.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-7-7
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
@SuppressWarnings("unchecked")
public class BssSheetDelDao extends SuperDAO
{
	
	public List<Map<String,String>> querySipVoipMessage(String loid) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select b.VOIP_USERNAME, b.VOIP_PORT " +
				"from tab_hgwcustomer a, tab_voip_serv_param b " +
				"where a.user_id = b.user_id and b.PROTOCOL in (0,1) " +
				"and a.username='" + loid + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public List<Map<String,String>> queryH248VoipMessage(String loid) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select b.VOIP_PHONE " +
				"from tab_hgwcustomer a, tab_voip_serv_param b " +
				"where a.user_id = b.user_id and b.PROTOCOL = 2 " +
				"and a.username = '" + loid + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public List<Map<String,String>> queryIptvMessage(String loid) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select b.username, b.real_bind_port " +
				"from tab_hgwcustomer a ,hgwcust_serv_Info b " +
				"where a.user_id = b.user_id and b.serv_type_id = 11 " +
				"and a.username = '" + loid + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public List<Map<String,String>> getIptvPort(String iptvUsername, String loid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select b.real_bind_port " +
				"from tab_hgwcustomer a ,hgwcust_serv_Info b " +
				"where a.user_id = b.user_id and b.serv_type_id = 11 " +
				"and a.username = '" + loid + "' and b.username = '" + iptvUsername + "'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}
	
	public int qyeryLoid(String loid) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from tab_hgwcustomer where username ='" + loid + "'");
		}else{
			sql.append("select count(1) from tab_hgwcustomer where username ='" + loid + "'");
		}
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForInt(psql.getSQL());
	}
}
