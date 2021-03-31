/**
 * @(#)LogOut.java 2006-1-21
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 输出日志.将日志输出到tab_logs
 *
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class LogOut {
	private static Logger m_logger = LoggerFactory.getLogger(LogOut.class);
	/**
	 * Constrator:
	 */
	public LogOut() {

	}

	/**
	 * 日志输出到tab_logs
	 *
	 * @param 日志类型
	 *            <UL>
	 *            <LI>1：访问日志
	 *            <LI>2：操作资源日志
	 *            <LI>3：更改配置
	 *            </UL>
	 * @param 日志级别
	 *            <UL>
	 *            <LI>1：访问日志
	 *            <LI>3：修改
	 *            <LI>4：增加和删除
	 *            </UL>
	 * @param 日志内容(String).
	 * @param 用户ID
	 */
	public static boolean out(int _log_type, int _log_level,
			String _log_context, int _acc_oid) {
		boolean result = false;
		String sqlLog = "insert into tab_logs values (?,?,?,?,?,?)";
		long id = DataSetBean.getMaxId("tab_logs", "id");
		Date now = new Date();
		long log_date = now.getTime();

		PrepareSQL pSQL = new PrepareSQL(sqlLog);
		pSQL.setLong(1, id);
		pSQL.setLong(5, log_date);
		pSQL.setInt(2, _log_type);
		pSQL.setInt(3, _log_level);
		pSQL.setString(4, _log_context);
		pSQL.setInt(6, _acc_oid);
		DataSetBean.executeUpdate(pSQL.getSQL());

		m_logger.debug("log:" + _log_context);
		return result;
	}

	public static boolean out(String message)
	{
		m_logger.debug("log"+message);
		return true;


	}


        public static boolean out(int message)
        {
        	m_logger.debug("log"+message);
                return true;


        }

        public static boolean out(long message)
                {
        	m_logger.debug("log"+message);
                        return true;


                }

}
