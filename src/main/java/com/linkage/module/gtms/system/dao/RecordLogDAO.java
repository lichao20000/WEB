
package com.linkage.module.gtms.system.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;

/**
 * 日志记录操作类
 * 
 * @author zhaixf(63412)
 * @version 1.0
 * @since 2010-12-9 下午05:09:48
 * @category com.linkage.module.ims.system.dao
 * @copyright 南京联创科技 网管科技部
 */
public class RecordLogDAO
{

	// 系统操作日志SQL
	private static final String saveOperLog = "insert into tr_web_oper_log (log_time, acc_loginname, log_ip, host_name, acc_oid, item_id, oper_cont, oper_type_id) values (?,?,?,?,?,?,?,?)";

	/**
	 * 记录系统操作日志SQL
	 * 
	 * @param userAccount
	 * @param logip
	 * @param hostname
	 * @param accOid
	 * @param itemId
	 * @param oprCont
	 * @return
	 */
	public void recordOperLog(String userAccount, String logip, String hostname,
			long accOid, String itemId, String oprCont)
	{
		PrepareSQL psql = new PrepareSQL(saveOperLog);
		psql.setLong(1, System.currentTimeMillis() / 1000);
		psql.setString(2, userAccount);
		psql.setString(3, logip);
		psql.setString(4, hostname);
		psql.setLong(5, accOid);
		psql.setString(6, itemId);
		psql.setString(7, oprCont);
		psql.setInt(8, 1);
		DBOperation.executeUpdate(psql.getSQL());
	}
}
