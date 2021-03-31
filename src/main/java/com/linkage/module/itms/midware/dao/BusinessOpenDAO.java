
package com.linkage.module.itms.midware.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class BusinessOpenDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BusinessOpenDAO.class);

	public int getServUserCount(String account, String serviceCode)
	{
		logger.debug("getServUserCount({},{})",account,serviceCode);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) as num from gw_user_midware_serv where username=? and serv_type_id=?");
		}else{
			psql.append("select count(1) as num from gw_user_midware_serv where username=? and serv_type_id=?");
		}
		psql.setString(1, account);
		psql.setInt(2, StringUtil.getIntegerValue(serviceCode));
		jt.queryForInt(psql.getSQL());
		return jt.queryForInt(psql.getSQL());
	}

	public int updateServ(int resultCode, long nowtime, String account, String serviceCode)
	{
		String sql = "update gw_user_midware_serv set oper_type_id=6,stat=?,oper_time=? where username=? and serv_type_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, resultCode);
		psql.setLong(2, nowtime);
		psql.setString(3, account);
		psql.setInt(4, StringUtil.getIntegerValue(serviceCode));
		
		return jt.update(psql.getSQL());
	}

	public int insertServ(int resultCode, long nowtime, String account, String serviceCode)
	{
		String sql = "insert into gw_user_midware_serv(username,serv_type_id,oper_type_id,stat,oper_time) values(?,?,6,?,?)";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, account);
		psql.setInt(2, StringUtil.getIntegerValue(serviceCode));
		psql.setInt(3, resultCode);
		psql.setLong(4, nowtime);
		return jt.update(psql.getSQL());
	}
}
