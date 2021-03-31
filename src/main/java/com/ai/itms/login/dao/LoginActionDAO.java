package com.ai.itms.login.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;



public class LoginActionDAO extends SuperDAO{

	Logger logger = LoggerFactory.getLogger(LoginActionDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUserPhone(String username){
		
		PrepareSQL psql = new PrepareSQL();
		psql.append(" select a.acc_oid,a.acc_loginname,b.per_mobile from tab_accounts a ,tab_persons b where    ");
		psql.append(" a.acc_oid=b.per_acc_oid and a.acc_loginname=? ");
		psql.setString(1, username);
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 查询发送短信日志
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMessageLog(String acc_oid){
		List<Map<String, Object>> list=null;
		try{
			PrepareSQL psql = new PrepareSQL();
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				psql.append(" select acc_oid,acc_loginname,per_mobile,update_time,num from tab_messageyzm_log where acc_oid='"+acc_oid+"' ");
			}
			else {
				psql.append(" select * from  tab_messageyzm_log where acc_oid='"+acc_oid+"' ");
			}
			list=this.jt.queryForList(psql.getSQL());
		}catch (Exception e) {
			 e.printStackTrace();
		}
		return list;
	}
	
	public void updateMessageLog(String acc_oid,String per_mobile){
		long createtime=System.currentTimeMillis()/1000;
		try{
			PrepareSQL psql = new PrepareSQL("update tab_messageyzm_log set update_time=?,per_mobile=?,num=num+1 where acc_oid=?");
			psql.setLong(1, createtime);
			psql.setString(2, per_mobile);
			psql.setString(3, acc_oid);
			jt.update(psql.getSQL());
			
		}catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	
	public void insertMessageLog(String acc_oid,String acc_loginname,String per_mobile){
		long createtime=System.currentTimeMillis()/1000;
		PrepareSQL psql = new PrepareSQL();
		psql.append(" insert into tab_messageyzm_log(acc_oid,acc_loginname,per_mobile,update_time,num)");
		psql.append("values(?,?,?,?,?)");
		psql.setString(1, acc_oid);
		psql.setString(2, acc_loginname);
		psql.setString(3, per_mobile);
		psql.setLong(4, createtime);
		psql.setInt(5, 1);
		try{
			this.jt.update(psql.getSQL());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
