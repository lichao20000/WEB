package com.linkage.litms.common.database;

/**
 * <p>Title: SyLimitRowQuery</p>
 * <p>Description: 利用连接池类封装数据库操作_有关Sybase限制行输出</p>
 * <p>Copyright: 南京联创公司版权所有Copyright (c) 2002</p>
 * <p>Company: 南京联创系统集成股份有限公司</p>
 * @author Dolphin
 * @version 1.0
 **/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;

public class SyLimitRowQuery {
	/**
	 * 根据sql语句产生一个数据库游标 返回值： 数据库游标 com.linkagesoftware.network.database.Cursor
	 * 参数： sql : sql语句
	 */
	public static Cursor getCursor(String sql, int rowNum) {
		Connection conn = null;
		// boolean exist;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;

		Cursor cursor = new Cursor();
		try {
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			// stmt.execute("set rowcount " + rowNum);
			rs = stmt.executeQuery(sql);
			String value;
			while (rs.next() && rowNum > 0) {
				metadata = rs.getMetaData();
				Map fields = new Hashtable();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnName(i));
					if (value == null)
						value = "";
					fields.put(metadata.getColumnName(i).toLowerCase(), value);
				}
				cursor.add(fields);

				rowNum--;
			}
			// stmt.execute("set rowcount 0");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				rs = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cursor;
	}
}
