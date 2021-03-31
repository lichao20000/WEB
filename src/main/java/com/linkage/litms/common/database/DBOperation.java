/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.common.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;

/**
 * connection request for HGW.
 * 
 * @author alex(yanhj@)
 * @version 1.6
 */
public class DBOperation {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DBOperation.class);
	
	/**
	 * getDBConn by dbName
	 * @param dbName
	 * @return
	 */
	public static Connection getDBConn(String dbName)
	{
		Connection conn = null;
		// for multi DataSource, add by chenjie 2013-12-26
		if(!StringUtil.IsEmpty(dbName))
			conn = DBAdapter.getJDBCConnection(dbName);
		else
			conn = DBAdapter.getJDBCConnection();
		
		return conn;
	}
	
	/**
	 * default getMaxId
	 * @param id
	 * @param tableName
	 * @return
	 */
	public static long getMaxId(String id, String tableName){
		return getMaxId(id, tableName, null);
	}

	/**
	 * get max id from tableName
	 * 
	 * @param id
	 * @param tableName
	 * @return
	 */
	public static long getMaxId(String id, String tableName, String dbName) {
		logger.debug("getMaxId({})", new Object[] {id, tableName, dbName});

		String sql = "select " + DBUtil.NVL() + "(max(" + id
				+ "),0) as id from " + tableName;

		return StringUtil.getLongValue(DBOperation.getRecord(sql, dbName), "id", 0);
	}
	
	/**
	 * default executeCall 
	 * @param sql
	 * @return
	 */
	public static CallableStatement executeCall(String sql) {
		return executeCall(sql, null);
	}

	/**
	 * call procedure.
	 * 
	 * @param callSql
	 * @return
	 */
	public static CallableStatement executeCall(String sql, String dbName) {
		logger.debug("executeCall({},{})", sql, dbName);

		CallableStatement cstmt = null;
		Connection conn = null;

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return cstmt;
			}

			cstmt = conn.prepareCall(sql);
		} catch (SQLException ex) {
			try {
				cstmt = conn.prepareCall(sql);
			} catch (SQLException e) {
				logger.error("SQLException:{}\n{}",
						new Object[] { ex.getMessage(), sql });
			}
		} catch (Exception ex) {
			logger.error("Exception:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return cstmt;
	}
	
	
	/**
	 * default getRecord
	 * @param sql
	 * @return
	 */
	public static Map<String, String> getRecord(String sql) {
		return getRecord(sql, null);
	}

	/**
	 * if only one record, get it else get the first record.
	 * 
	 * @param sql
	 * @return
	 */
	public static Map<String, String> getRecord(String sql, String dbName) {
		logger.debug("getRecord({},{})", sql, dbName);

		Map<String, String> fields = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData metadata = null;

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return fields;
			}

			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				fields = new HashMap<String, String>();
				metadata = rs.getMetaData();
				String value = "";
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (null != value) {
						fields.put(metadata.getColumnLabel(i).toLowerCase(),
								value.trim());
					}
				}
			} else {
				fields = null;
			}
		} catch (SQLException e1) {
			logger.error("SQLException:{}\n{}", new Object[] { e1.getMessage(),
					sql });
		} catch (Exception ex) {
			logger.error("Exception:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			metadata = null;
			try {
				if (null != rs) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return fields;
	}
	
	/**
	 * default getRecords
	 * @param sql
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> getRecords(String sql) {
		return getRecords(sql, null);
	}

	/**
	 * get list of records.
	 * 
	 * @param sql
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> getRecords(String sql, String dbName) {
		logger.debug("getRecords({},{})", sql, dbName);

		ArrayList<HashMap<String, String>> list = null;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData metadata = null;
		String value = "";

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return list;
			}

			list = new ArrayList<HashMap<String, String>>(100);
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				metadata = rs.getMetaData();
				HashMap<String, String> fields = new HashMap<String, String>();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					value = rs.getString(metadata.getColumnLabel(i));
					if (null != value) {
						fields.put(metadata.getColumnLabel(i).toLowerCase(),
								value.trim());
					}
				}
				list.add(fields);
			}
		} catch (SQLException e1) {
			logger.error("SQLException:{}\n{}", new Object[] { e1.getMessage(),
					sql });
		} catch (Exception ex) {
			logger.error("SQLException:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			metadata = null;
			try {
				if (null != rs) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return list;
	}
	
	/**
	 * default executeUpdate
	 * @param sql
	 * @param dbName
	 * @return
	 */
	public static int executeUpdate(String sql) { 
		return executeUpdate(sql, null);
	}

	/**
	 * update db.
	 * 
	 * @param sql
	 *            -String
	 * @return -int <h4>ִ�н��</h4> <li>>0: Success!</li> <li>-1:Get dbConnection
	 *         error!</li> <li>-2:update failure!</li>
	 */
	public static int executeUpdate(String sql, String dbName) {
		logger.debug("executeUpdate({},{})", sql, dbName);

		int iCode = -1;
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return iCode;
			}

			pst = conn.prepareStatement(sql);
			iCode = pst.executeUpdate();
		} catch (SQLException e1) {
			logger.error("SQLException:{}\n{}", new Object[] { e1.getMessage(),
					sql });
		} catch (Exception ex) {
			logger.error("Exception:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return iCode;
	}
	
	/**
	 * default executeUpdate
	 * @param list
	 * @return
	 */
	public static int executeUpdate(ArrayList<String> list){
		return executeUpdate(list, null);
	}

	/**
	 * update db.
	 * 
	 * @param list
	 *            -ArrayList
	 * @return -int <h4>ִ�н��</h4> <li>1: Success!</li> <li>-1:Get dbConnection
	 *         error!</li> <li>-2:update failure!</li> <li>-3:Sql array is null!
	 *         </li>
	 */
	public static int executeUpdate(ArrayList<String> list, String dbName) {
		logger.debug("executeUpdate(list,{})", dbName);

		int iCode = -1;

		if (list == null || list.size() == 0) {
			logger.warn("sql array is null");
			return -3;
		}

		Connection conn = null;
		Statement pst = null;

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return iCode;
			}

			pst = conn.createStatement();

			String sql = "";
			for (int i = 0; i < list.size(); i++) {
				sql = (String) list.get(i);
				if (sql != null && sql.length() > 0) {
					pst.addBatch(sql);
				}
				if (i % 100 == 0) {
					pst.executeBatch();
				}
			}

			pst.executeBatch();

			logger.info("sql batch size:{}", list.size());

			iCode = 1;
		} catch (SQLException e1) {
			logger.error("SQLException:{}", new Object[] { e1.getMessage() });
			for (int i = 0; i < list.size(); i++) {
				logger.error(list.get(i));
			}
			iCode = -2;

		} catch (Exception ex) {
			logger.error("Exception:{}", new Object[] { ex.getMessage() });
			for (int i = 0; i < list.size(); i++) {
				logger.error(list.get(i));
			}
			iCode = -2;
		} finally {
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return iCode;
	}
	
	/**
	 * default executeProcSelect
	 * @param sql
	 * @return
	 */
	public static int executeProcSelect(String sql) {
		return executeProcSelect(sql, null);
	}

	/**
	 * 
	 * @param selectSql
	 * @return
	 */
	public static int executeProcSelect(String sql, String dbName) {
		logger.debug("executeProcSelect({},{})", sql, dbName);

		int obj = -1;

		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;

		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return obj;
			}

			stmt = conn.createStatement(
					java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
					java.sql.ResultSet.CONCUR_READ_ONLY);
			rst = stmt.executeQuery(sql);
			boolean exist = rst.first();
			if (exist) {
				obj = rst.getInt(1);
			}
		} catch (SQLException ex) {
			logger.error("SQLException:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} catch (Exception ex) {
			logger.error("Exception:{}\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			try {
				if (null != rst) {
					rst.close();
					rst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != stmt) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		logger.debug("call sybase process get id:{}", obj);

		return obj;
	}
	
	/**
	 * default getMap
	 * @param sql
	 * @return
	 */
	public static HashMap<String, String> getMap(String sql) {
		return getMap(sql, null);
	}

	/**
	 * ִ��select��ѯ��䣬����HashMap<column1, column2>��ʽ�Ľ��
	 * 
	 * @param ��ѯ���
	 * @author Jason(3412)
	 * @date 2010-6-22
	 * @return HashMap ����HashMap
	 */
	public static HashMap<String, String> getMap(String sql, String dbName) {
		logger.debug("getMap({},{})", sql, dbName);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		HashMap<String, String> map = null;
		try {
			// for multi DataSource, modify by chenjie 2013-12-26
			conn = getDBConn(dbName);
			
			if (conn == null) {
				logger.debug("conn == null");

				return map;
			}

			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql);

			map = new HashMap<String, String>();
			while (rst.next()) {
				map.put(rst.getString(1), rst.getString(2));
			}
		} catch (SQLException ex) {
			logger.error("SQLException:{}.\n{}", new Object[] {
					ex.getMessage(), sql });
		} catch (Exception ex) {

			logger.error("Exception:{}.\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			try {
				if (null != rst) {
					rst.close();
					rst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != stmt) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}
		return map;
	}
	
	/**
	 * default executeUpdate
	 * @param mysql
	 * @return
	 */
	public static int executeUpdate(String[] mysql) {
		return executeUpdate(mysql, null);
	}

	/**
	 * update db.
	 * 
	 * @param mysql
	 *            -String[]
	 * @return -int <h4>ִ�н��</h4> <li>1: Success!</li> <li>-1:Get dbConnection
	 *         error!</li> <li>-2:update failure!</li> <li>-3:Sql array is null!
	 *         </li>
	 */
	public static int executeUpdate(String[] mysql, String dbName) {
		logger.debug("executeUpdate(mysql,{})", dbName);

		int iCode = -1;

		if (mysql == null || mysql.length == 0) {
			logger.error("sql array is null");
			return -3;
		}

		// for multi DataSource, modify by chenjie 2013-12-26
		Connection conn = getDBConn(dbName);
		
		if (conn == null) {
			logger.debug("conn == null");

			return iCode;
		}

		Statement pst = null;

		try {
			pst = conn.createStatement();
			String sql = "";
			for (int i = 0; i < mysql.length; i++) {
				sql = mysql[i];

				if (sql != null && sql.length() > 0) {
					pst.addBatch(sql);
				}
				if (i % 100 == 99) {
					pst.executeBatch();
				}
			}

			pst.executeBatch();

			logger.info("sql batch size:{}", mysql.length);

			iCode = 1;
		} catch (SQLException e1) {
			logger.error("SQLException:{}", new Object[] { e1.getMessage() });
			for (int i = 0; i < mysql.length; i++) {
				logger.error(mysql[i]);
			}
			iCode = -2;

		} catch (Exception ex) {
			logger.error("Exception:{}", new Object[] { ex.getMessage() });
			for (int i = 0; i < mysql.length; i++) {
				logger.error(mysql[i]);
			}
			iCode = -2;
		} finally {
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return iCode;
	}
	
	/**
	 * default getStrRecords(String sql)
	 * @param sql
	 * @return
	 */
	public static Set<String> getStrRecords(String sql){
		return getStrRecords(sql, null);
	}

	/**
	 * get string list of records (first colum).
	 * 
	 * @param sql
	 * @return
	 */
	public static Set<String> getStrRecords(String sql, String dbName) {
		logger.debug("getStrRecords({},{})", sql, dbName);

		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;

		Set<String> set = null;
		
		// for multi DataSource, modify by chenjie 2013-12-26
		conn = getDBConn(dbName);
		
		if (conn == null) {
			logger.debug("conn == null");

			return set;
		}

		try {
			pst = conn.prepareStatement(sql);

			set = new HashSet<String>(30);
			rs = pst.executeQuery();
			while (rs.next()) {
				set.add(rs.getString(1));
			}
		} catch (SQLException e1) {
			logger.error("SQLException:{}.\n{}", new Object[] {
					e1.getMessage(), sql });
		} catch (Exception ex) {
			logger.error("Exception:{}.\n{}", new Object[] { ex.getMessage(),
					sql });
		} finally {
			try {
				if (null != rs) {
					rs.close();
					rs = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}
			
			try {
				if (null != pst) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				logger.error("SQLException:{}", e.getMessage());
			}

			try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error("Exception:close connection,{}", e.getMessage());
			}
		}

		return set;
	}
}
