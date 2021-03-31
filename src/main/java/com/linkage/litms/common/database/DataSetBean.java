/*
 * @(#)DataSetBean.java	1.00 1/5/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

/**
 * 封装对数据库记录增加、删除、修改操作，所有方法都是静态的,所有字段名称都以小写填充。
 * 
 * @author yuht
 * @version 1.00, 1/5/2006
 * @since Liposs 2.1
 */
public class DataSetBean {

	/** log */
	private static Logger log = LoggerFactory
			.getLogger(DataSetBean.class);

	/**
	 * 根据sql语句获取数据中第一条记录。
	 * 
	 * @param sql
	 *            sql语句
	 * @return 以HashMap形式表示的数据库表中的记录， 若有多条记录，返回第一条 若没有对应记录，返回 null
	 */
	public static HashMap getRecord(String sql) {
		Connection conn = null;
		HashMap result = new HashMap();
		boolean exist = false;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
//			log.debug(sql);
			exist = rs.next();
			if (exist) {
				metadata = rs.getMetaData();
				String value;
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					result.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
			}
		} catch (SQLException sqle) {
			log.error(sqle.getMessage() + " SQL:" + sql, sqle);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getRecord(String sql)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getRecord(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getRecord(String sql)");
			}
		}
		if (exist)
			return result;
		else
			return null;
	}
	/**
	 * 根据sql语句获取数据中第一条记录。
	 * 
	 * @param sql
	 *            sql语句
	 * @return 以HashMap形式表示的数据库表中的记录， 若有多条记录，返回第一条 若没有对应记录，返回 null
	 */
	public static HashMap getRecord(String sql,String dataSource) {
		Connection conn = null;
		HashMap result = new HashMap();
		boolean exist = false;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection(dataSource);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
//			log.debug(sql);
			exist = rs.next();
			if (exist) {
				metadata = rs.getMetaData();
				String value;
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					result.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
			}
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getRecord(String sql)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getRecord(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getRecord(String sql)");
			}
		}
		if (exist)
			return result;
		else
			return null;
	}

	/**
	 * 根据sql语句获取一批数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql) {
		
		return getCursor(sql,"");
	}
	
	public static int getDbType(String aliases){
		String dbtype = DBAdapter.getDriverURL(aliases);
		
		if(dbtype.indexOf("oracle")!=-1){
			return 1;
		}
		else if(dbtype.indexOf("mysql")!=-1){
			return 3;
		}
		else{
			return 2;
		}
	}
	
	/**
	 * 根据sql语句获取一批数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql,String dataSource) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		Cursor cursor = new Cursor();
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection(dataSource);
			stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
			rs = stmt.executeQuery(sql);
//			log.debug(sql);
			String value;
			while (rs.next()) {
				metadata = rs.getMetaData();
				HashMap fields = new HashMap();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					fields.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
				cursor.add(fields);
			}
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getCursor(String sql)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getCursor(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getCursor(String sql)");
			}
		}
		return cursor;
	}
	/**
	 * 用于Sybase数据库根据sql语句获取一批指定长度数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @param maxrow
	 *            返回的数量
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql, int maxrow) {
		return getCursor(sql, maxrow, null);
	}
	/**
	 * 用于Sybase数据库根据sql语句获取一批指定长度数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @param maxrow
	 *            返回的数量
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql, int maxrow ,String dataSource) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		Cursor cursor = new Cursor();
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection(dataSource);
			stmt = conn.createStatement();
			// stmt.execute("set rowcount " + maxrow);
			rs = stmt.executeQuery(sql);
//			log.debug(sql);
			String value;
			while (rs.next() && maxrow > 0) {
				metadata = rs.getMetaData();
				Hashtable fields = new Hashtable();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					fields.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
				maxrow--;
				cursor.add(fields);
			}
			// stmt.execute("set rowcount 0");
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error "
						+ "in getCursor(String sql, int maxrow)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error "
						+ "in getCursor(String sql, int maxrow)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error "
						+ "in getCursor(String sql, int maxrow)");
			}
		}
		return cursor;
	}
	/**
	 * 用于Sybase数据库根据sql语句获取一批固定范围内的数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @param start
	 *            起始位置
	 * @param len
	 *            获取数据长度
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql, int start, int len) {
		return getCursor(sql, start, len, null);
	}
	/**
	 * 用于Sybase数据库根据sql语句获取一批固定范围内的数据
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @param start
	 *            起始位置
	 * @param len
	 *            获取数据长度
	 * @return 返回自定义游标类
	 */
	public static Cursor getCursor(String sql, int start, int len,String dataSource) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		Cursor cursor = new Cursor();
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection(dataSource);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);
			// stmt.execute("set rowcount 0");
			rs = stmt.executeQuery(sql);
			log.info(sql);
			String value;
			int ln = 1;
//			while (ln < start) {
//				rs.next();
//				ln++;
//			}
			
			if(start > 1){
				if(false == rs.absolute(start - 1)){
					log.error("move out of the db result set! start is " + start);
				}
			}
			
			for (int count = 0; rs.next() && count < len; count++) {
				metadata = rs.getMetaData();
				Hashtable fields = new Hashtable();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					fields.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
				cursor.add(fields);
			}
			
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getCursor(String sql, int start, int len)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getCursor(String sql, int start, int len)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getCursor(String sql, int start, int len)");
			}
		}
		return cursor;
	}

	/**
	 * 根据SQL语句对数据库做INSERT、DELETE、UPDATE操作
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @return 返回操作的记录条数
	 */
	public synchronized static int executeUpdate(String sql) {
		Connection conn = null;
		Statement stmt = null;
		int result = -1;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
			result = stmt.executeUpdate(sql);
			log.debug(sql);
			// mysql有自动提交，不需要重复提交
			if(DBUtil.GetDB() != Global.DB_MYSQL) {
				conn.commit();
			}
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "executeUpdate(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "executeUpdate(String sql)");
			}
		}
		return result;
	}
	
	public synchronized static int executeUpdate(String sql,String dataSource) {
		Connection conn = null;
		Statement stmt = null;
		int result = -1;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection(dataSource);
			stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
			result = stmt.executeUpdate(sql);
			log.debug(sql);
			// mysql有自动提交，不需要重复提交
			if(DBUtil.GetDB() != Global.DB_MYSQL) {
				conn.commit();
			}
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL: " + sql + ". 错误信息: " + sqle.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "executeUpdate(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "executeUpdate(String sql)");
			}
		}
		return result;
	}

	/**
	 * 执行批量SQL，<code>sql</code>通过分号分隔不同的SQL语句
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @return 返回操作的记录条数
	 */
	public static int[] doBatch(String sql) {
		String[] arr = sql.split(";");
		return doBatch(arr);
	}
	
	/**
	 * 执行批量SQL，<code>sql</code>获取SQL语句集合
	 * 
	 * @param sql
	 *            SQL语句字符串
	 * @return 返回操作的记录条数
	 */
	public static int[] doBatchList(List sqllist) {
		String[] arr = null;
		if(sqllist != null){
			arr = new String[sqllist.size()];
			for (int i = 0; i < sqllist.size(); i++) {
				arr[i] = (String) sqllist.get(i);
			}
		}
		return doBatch(arr);
	}

	/**
	 * 执行批量SQL.
	 * 
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	public static int[] doBatch(ArrayList oarrsql) {
		String[] arrsql = new String[oarrsql.size()];
		for (int i = 0; i < oarrsql.size(); i++) {
			arrsql[i] = String.valueOf(oarrsql.get(i));
		}
		int[] result = doBatch(arrsql);
		arrsql = null;
		return result;
	}

	/**
	 * 执行批量SQL.
	 * 
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	public static int[] doBatch(Object[] oarrsql) {
		String[] arrsql = new String[oarrsql.length];
		for (int i = 0; i < oarrsql.length; i++) {
			arrsql[i] = String.valueOf(oarrsql[i]);
		}
		int[] result = doBatch(arrsql);
		arrsql = null;
		return result;
	}

	/**
	 * 执行批量SQL.
	 * 
	 * @param arrsql
	 *            SQL语句数组
	 * @return 返回操作的记录条数
	 */
	public static int[] doBatch(String[] arrsql) {
		int[] rowResult = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			// if (!conn.getAutoCommit()) {
			// conn.setAutoCommit(true);
			// }
			// conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
			int j = 0;
			for (int i = 0; i < arrsql.length; i++) {
				if (arrsql[i] == null || arrsql[i].equals("null") || arrsql[i].isEmpty())
					continue;
				stmt.addBatch(arrsql[i]);
				log.debug(arrsql[i]);
				if (((j + 1) % 200) == 0) {
					rowResult = stmt.executeBatch();
					stmt.clearBatch();
				}
				j++;
			}
			if (j % 200 != 0)
				rowResult = stmt.executeBatch();
			// conn.commit();
			// conn.setAutoCommit(true);
			log.warn("所有执行批处理SQL语句{}",Arrays.toString(arrsql));
		} catch (SQLException sqle) {
			log.error(StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", (System
					.currentTimeMillis()))
					+ " SQL语句: " + Arrays.toString(arrsql) + ". 错误信息: " + sqle);
		} finally {
			// try {
			// conn.rollback();
			// conn.setAutoCommit(true);
			// } catch (SQLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "doBatch(String[] arrsql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "doBatch(String[] arrsql)");
			}
		}
		return rowResult;
	}

	/**
	 * 获取某个表具体列的最大值
	 * 
	 * @param tablename
	 *            表名称
	 * @param column
	 *            字段名称
	 * @return 返回一个整形值
	 */
	public synchronized static long getMaxId(String tablename, String column) {
		String sql = "select max(" + column + ") from " + tablename;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		long result = 0;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
//			log.debug(sql);
			if (rs.next()) {
				if (rs.getString(1) != null)
					result = Long.parseLong(rs.getString(1));
			}
		} catch (SQLException sqle) {
			log.error("DataSetBean: Execute getMaxId is error: " + sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getMaxId(String tablename, String column)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getMaxId(String tablename, String column)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getMaxId(String tablename, String column)");
			}
		}
		return result + 1;
	}

	public static HashMap getMap(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		HashMap map = new HashMap();
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			log.debug(sql);
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException sqle) {
			log.error("DataSetBean: Execute getMap is error: " + sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close resultset object error in "
						+ "getMap(String sql)");
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close statement object error in "
						+ "getMap(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getMap(String sql)");
			}
		}
		return map;
	}

	/**
	 * 调度PrepareStatement update方法执行SQL查询
	 * 
	 * @param strSQL
	 * @param params
	 * @return int
	 */
	public static int executeUpdate(String strSQL, String[] params) {
		int exeNum = 0;
		Connection conn = null;
		// Statement stmt = null;
		PreparedStatement pstmt = null;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			pstmt = conn.prepareStatement(strSQL);
			log.debug(strSQL);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			exeNum = pstmt.executeUpdate();
		} catch (SQLException sqle) {
			log.error("DataSetBean:executeUpdate is error: " + strSQL);
			log.error("{}", sqle);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				log
						.error("DataSetBean: close PreparedStatement object error in "
								+ "getMap(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getMap(String sql)");
			}
		}
		return exeNum;
	}

	/**
	 * 调度PrepareStatement query方法执行SQL查询
	 * 
	 * @param strSQL
	 * @param params
	 * @return List
	 */
	public static ArrayList executeQuery(String strSQL, String[] params) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata;
		ArrayList list = new ArrayList();
		HashMap result = null;
		String value;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			pstmt = conn.prepareStatement(strSQL);
			log.debug(strSQL);
			if(null != params){
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				metadata = rs.getMetaData();
				result = new HashMap();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					result.put(metadata.getColumnLabel(i).toLowerCase(), value
							.trim());
				}
				list.add(result);
			}
		} catch (SQLException sqle) {
			log.error("DataSetBean:executeQuery is error: " + strSQL);
			log.error("{}", sqle);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} catch (Exception e) {
				log
						.error("DataSetBean: close PreparedStatement object error in "
								+ "getMap(String sql)");
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				log.error("DataSetBean: close connection object error in "
						+ "getMap(String sql)");
			}
		}
		return list;
	}

	/**
	 * 按照PreparedStatement方式来处理查询数据
	 * <p>
	 * 避免直接注入式SQL攻击
	 * 
	 * @param strSQL
	 *            待查询SQL语句
	 * @param m_elems
	 *            Object（param,type） +
	 *            <p>
	 *            准备写入SQL条件
	 * @return Cursor 释放出游标对象
	 */
	public Cursor getCursor(String strSQL, ArrayList m_elems) {
		String[][] inElements = new String[m_elems.size() / 2][2];
		for (int k = 0; k < m_elems.size() / 2; k++) {
			inElements[k][0] = String.valueOf(m_elems.get(k * 2 + 0));
			inElements[k][1] = String.valueOf(m_elems.get(k * 2 + 1));
		}
		return getCursor(strSQL, inElements);
	}

	/**
	 * 按照PreparedStatement方式来处理查询数据
	 * <p>
	 * 避免直接注入式SQL攻击
	 * 
	 * @param strSQL
	 *            待查询SQL语句
	 * @param inElements
	 *            Object（param,type）
	 *            <p>
	 *            准备写入SQL条件
	 * @return Cursor 释放出游标对象
	 */
	public Cursor getCursor(String strSQL, String[][] inElements) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData metadata = null;
		Hashtable fields = null;
		String value = null;
		Cursor cursor = new Cursor();
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.prepareStatement(strSQL);
			log.debug(strSQL);
			for (int k = 0; k < inElements.length; k++) {
				if (inElements[k][1].equalsIgnoreCase("INT")) {
					stmt.setInt(k + 1, Integer.parseInt(inElements[k][0]));
				}
				if (inElements[k][1].equalsIgnoreCase("STRING")) {
					stmt.setString(k + 1, inElements[k][0]);
				}
				if (inElements[k][1].equalsIgnoreCase("OBJECT")) {
					stmt.setObject(k + 1, inElements[k][0]);
				}
				if (inElements[k][1].equalsIgnoreCase("LONG")) {
					stmt.setLong(k + 1, Long.parseLong(inElements[k][0]));
				}
				if (inElements[k][1].equalsIgnoreCase("DOUBLE")) {
					stmt.setDouble(k + 1, Double.parseDouble(inElements[k][0]));
				}
				if (inElements[k][1].equalsIgnoreCase("FLOAT")) {
					stmt.setFloat(k + 1, Float.parseFloat(inElements[k][0]));
				}
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				metadata = rs.getMetaData();
				fields = new Hashtable();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (value == null)
						value = "";
					fields.put(metadata.getColumnLabel(i).toUpperCase(), value);
				}
				cursor.add(fields);
			}
		} catch (SQLException e) {
			log.error("处理失败：" + e.getMessage());
		} finally {
			try {
				if(stmt!=null){
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cursor;
	}

	/**
	 * 调用能够预防注入式攻击的SQL执行方法（增加、修改、删除记录）
	 * 
	 * @param strSQL
	 *            待执行SQL
	 * @param elements
	 *            待插入或者是更新元素（多组）
	 * @return 执行结果：影响条数
	 */
	public static int executeUpdate(String strSQL, String[][] elements) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			// conn = ConnectionManager.getConnection();
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.prepareStatement(strSQL);
			log.debug(strSQL);
			for (int k = 0; k < elements.length; k++) {
				if (elements[k][0].equalsIgnoreCase("INT")) {
					stmt.setInt(k + 1, Integer.parseInt(elements[k][1]));
				}
				if (elements[k][0].equalsIgnoreCase("STRING")) {
					stmt.setString(k + 1, elements[k][1]);
				}
				if (elements[k][0].equalsIgnoreCase("OBJECT")) {
					stmt.setObject(k + 1, elements[k][1]);
				}
				if (elements[k][0].equalsIgnoreCase("LONG")) {
					stmt.setLong(k + 1, Long.parseLong(elements[k][1]));
				}
				if (elements[k][0].equalsIgnoreCase("DOUBLE")) {
					stmt.setDouble(k + 1, Double.parseDouble(elements[k][1]));
				}
				if (elements[k][0].equalsIgnoreCase("FLOAT")) {
					stmt.setFloat(k + 1, Float.parseFloat(elements[k][1]));
				}
			}
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			result = 0;
			log.error("处理失败：" + e.getMessage());
			return result;
		} finally {
			try {
				stmt.close();
				stmt = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 执行存储过程注入的存储过程，有返回值的
	 * 
	 * @param callable
	 *            存储过程，输入的参数请直接写在callable中
	 * @return 存储过程的返回值
	 * 
	 */
	public static String executeCall(String callable) {
		String result = "";
		Connection conn = null;
		CallableStatement ctmt = null;
		ResultSet res = null;
		try {
			conn = DBAdapter.getJDBCConnection();
			log.debug(callable);
			ctmt = conn.prepareCall(callable);
			ctmt.execute();
			res = ctmt.getResultSet();
			if (res != null) {
				res.next();
				result = res.getString(1);
			}
		} catch (SQLException e) {
			result = "";
			log.error("存储过程处理失败：" + e.getMessage());
			return result;
		} finally {
			try {
				if (null != ctmt) {
					ctmt.close();
				}
				if (null != res) {
					res.close();
				}
				if (null != conn) {
					conn.close();
				}

			} catch (Exception e) {

			} finally {
				ctmt = null;
				res = null;
				conn = null;
			}

		}
		return result;
	}
	

		/**
		 * 
		 * @param selectSql
		 * @return
		 */
		public static long executeProcSelect(String sql) {
			log.debug("executeProcSelect({})", sql);

			long obj = -1;

			Connection conn = null;
			Statement stmt = null;
			ResultSet rst = null;

			try {
				conn = DBAdapter.getJDBCConnection();
				if (conn == null) {
					log.debug("conn == null");

					return obj;
				}

				stmt = conn.createStatement(
						java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
						java.sql.ResultSet.CONCUR_READ_ONLY);
				rst = stmt.executeQuery(sql);
				boolean exist = rst.first();
				if (exist) {
					obj = rst.getLong(1);
				}
			} catch (SQLException ex) {
				log.error("SQLException:{}\n{}", new Object[] { ex.getMessage(),
						sql });
			} catch (Exception ex) {
				log.error("Exception:{}\n{}", new Object[] { ex.getMessage(),
						sql });
			} finally {
				try {
					if (null != rst) {
						rst.close();
						rst = null;
					}
				} catch (SQLException e) {
					log.error("SQLException:{}", e.getMessage());
				}
				
				try {
					if (null != stmt) {
						stmt.close();
						stmt = null;
					}
				} catch (SQLException e) {
					log.error("SQLException:{}", e.getMessage());
				}

				try {
					if(conn!=null){
						conn.close();
						conn = null;
					}
				} catch (Exception e) {
					log.error("Exception:close connection,{}", e.getMessage());
				}
			}

			log.debug("call sybase process get id:{}", obj);

			return obj;
		}

}
