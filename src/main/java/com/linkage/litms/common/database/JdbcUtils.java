/*
 * 
 * 创建日期 2006-2-9
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.common.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库SQL操作类（初步完成增删改功能）
 * 
 * @author suzr
 */
public class JdbcUtils {
	/**
	 * 日志记录
	 */
	private static Logger m_logger = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * 返回记录链表
	 */
	private static List lMap = null;

	/**
	 * 操作影响记录数
	 */
	private static int iCols = 0;

	/**
	 * 操作返回数组
	 */
	private static int[] iCodes = null;

	// 数据库联接
	private static Connection connection = null;

	/**
	 * dbUtil类操作对象
	 */
	private static QueryRunner qRunner = new QueryRunner();;

	/**
	 * 构造初始化对象
	 */
	public JdbcUtils() {
	}

	/**
	 * 初始化实例对象
	 * 
	 * @return void
	 */
	private static void initJDBCConnection() {
		connection = DBAdapter.getJDBCConnection();
	}

	/**
	 * 关闭数据库联接
	 * 
	 * @return void
	 */
	private static void closeJDBCConnection() {
		// Use this helper method so we don't have to check for null
		try {
			DbUtils.close(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交数据语句查询（单一SQL操作）
	 * 
	 * @param strSQL
	 * @return List
	 */
	public static List query(String strSQL) {
		initJDBCConnection();

		try {
			lMap = (List) qRunner.query(connection, strSQL,
					new MapListHandler());
		} catch (SQLException e) {
			m_logger.error("query sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return lMap;
	}

	/**
	 * 提交数据语句查询（包含(?)）
	 * 
	 * @param strSQL
	 * @param param
	 * @return List
	 */
	public static List query(String strSQL, Object param) {
		initJDBCConnection();

		try {
			lMap = (List) qRunner.query(connection, strSQL, param,
					new MapListHandler());
		} catch (SQLException e) {
			m_logger.error("query sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return lMap;
	}

	/**
	 * 提交数据语句查询（包含(????.....)）
	 * 
	 * @param strSQL
	 * @param param
	 * @return List
	 */
	public static List query(String strSQL, Object[] param) {
		initJDBCConnection();

		try {
			lMap = (List) qRunner.query(connection, strSQL, param,
					new MapListHandler());
		} catch (SQLException e) {
			m_logger.error("query sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return lMap;
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
	public static List query(String strSQL, String[][] elements) {
		initJDBCConnection();
		PreparedStatement stmt = null;
		ResultSetMetaData metadata = null;
		ResultSet result = null;
		String value = null;
		String name = null;

		try {
			// connection = ConnectionManager.getConnection();
			stmt = connection.prepareStatement(strSQL);

			for (int k = 0; k < elements.length; k++) {
				if (elements[k][1].equalsIgnoreCase(DataType.Int)) {
					stmt.setInt(k + 1, Integer.parseInt(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase(DataType.String)) {
					stmt.setString(k + 1, elements[k][0]);
				}

				if (elements[k][1].equalsIgnoreCase(DataType.Object)) {
					stmt.setObject(k + 1, elements[k][0]);
				}

				if (elements[k][1].equalsIgnoreCase(DataType.Long)) {
					stmt.setLong(k + 1, Long.parseLong(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase(DataType.Double)) {
					stmt.setDouble(k + 1, Double.parseDouble(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase(DataType.Float)) {
					stmt.setFloat(k + 1, Float.parseFloat(elements[k][0]));
				}
			}

			result = stmt.executeQuery();
			metadata = result.getMetaData();
			HashMap fields = null;
			lMap = new ArrayList();

			while (result.next()) {
				fields = new HashMap();
				for (int i = 1; i <= metadata.getColumnCount(); i++) {
					name = metadata.getColumnLabel(i);
					value = result.getString(name);
					if (value == null)
						value = "";
					fields.put(name.toLowerCase(), value.trim());
				}

				lMap.add(fields);
			}
		} catch (SQLException e) {
			m_logger.error("处理失败：" + e.getMessage());
		} finally {
			closeJDBCConnection();
			
			try {
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return lMap;
	}

	/**
	 * 对查询记录集进行排序
	 * 
	 * @param key
	 * @return List
	 */
	public static List sort(String key) {
		return lMap;
	}

	/**
	 * 提交数据语句更新（包含(????.....)）
	 * 
	 * @param strSQL
	 * @param param
	 * @return int
	 */
	public static int update(String strSQL, Object[] param) {
		initJDBCConnection();

		try {
			iCols = qRunner.update(connection, strSQL, param);
		} catch (SQLException e) {
			m_logger.error("update sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCols;
	}

	/**
	 * 提交数据语句更新（包含(?)）
	 * 
	 * @param strSQL
	 * @param param
	 * @return int
	 */
	public static int update(String strSQL, Object param) {
		initJDBCConnection();

		try {
			iCols = qRunner.update(connection, strSQL, param);
		} catch (SQLException e) {
			m_logger.error("update sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCols;
	}

	/**
	 * 提交数据语句更新（单一SQL操作）
	 * 
	 * @param strSQL
	 * @return int
	 */
	public static int update(String strSQL) {
		initJDBCConnection();

		try {
			iCols = qRunner.update(connection, strSQL);
		} catch (SQLException e) {
			m_logger.error("update sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCols;
	}

	/**
	 * 批量更新指定SQL
	 * 
	 * @param strSQL
	 * @return int[]
	 */
	public static int[] batchUpdate(String[] strSQL) {
		initJDBCConnection();

		Statement stmt=null;
		try {
			stmt = connection.createStatement();
			for (int k = 0; k < strSQL.length; k++) {
				stmt.addBatch(strSQL[k]);
			}
			iCodes = stmt.executeBatch();
		} catch (SQLException e) {
			m_logger.error("batch update sql error", e);
		} finally {
			closeJDBCConnection();
			try {
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return iCodes;
	}

	/**
	 * 提交数据语句更新（批量SQL操作）
	 * 
	 * @param strSQL
	 * @return int
	 */
	public static int[] batch(String strSQL, Object[][] params) {
		initJDBCConnection();

		try {
			iCodes = qRunner.batch(connection, strSQL, params);
		} catch (SQLException e) {
			m_logger.error("batch update sql error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCodes;
	}

	/**
	 * 提交数据语句更新（批量SQL操作）
	 * 
	 * @param strSQL
	 * @return int
	 */
	public static int[] batch(String strSQL, String[][][] params) {
		initJDBCConnection();
		 int[] result = null;
		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(strSQL);

			for (int k = 0; k < params.length; k++) {
				m_logger.debug(""+params[k].length);
				for (int s = 0; s < params[k].length; s++) {
					if (params[k][s][1].equalsIgnoreCase("INT")) {
						stmt.setInt(s + 1, Integer.parseInt(params[k][s][0]));
					}

					if (params[k][s][1].equalsIgnoreCase("STRING")) {
						stmt.setString(s + 1, params[k][s][0]);
					}

					if (params[k][s][1].equalsIgnoreCase("OBJECT")) {
						stmt.setObject(s + 1, params[k][s][0]);
					}

					if (params[k][s][1].equalsIgnoreCase("LONG")) {
						stmt.setLong(s + 1, Long.parseLong(params[k][s][0]));
					}

					if (params[k][s][1].equalsIgnoreCase("DOUBLE")) {
						stmt.setDouble(s + 1, Double
								.parseDouble(params[k][s][0]));
					}

					if (params[k][s][1].equalsIgnoreCase("FLOAT")) {
						stmt.setFloat(s + 1, Float.parseFloat(params[k][s][0]));
					}
				}

				stmt.addBatch();
				
			}

			result = stmt.executeBatch();
		} catch (SQLException e) {
			m_logger.error("batch update sql error", e);
		} finally {
			closeJDBCConnection();
			
			try {
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 获取记录的行数
	 * 
	 * @param strSQL
	 * @return int
	 */
	public static int getRecordRows(String strSQL) {
		initJDBCConnection();

		try {
			lMap = query(strSQL);
			iCols = Integer.parseInt(String.valueOf(((Map) (lMap.get(0)))
					.get("num")));
		} catch (Exception e) {
			m_logger.error("get result rows number error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCols;
	}

	/**
	 * 获取记录的行数
	 * 
	 * @param strSQL
	 * @return int
	 */
	public static int getRecordRows(String strSQL, Object[] param) {
		initJDBCConnection();

		try {
			lMap = query(strSQL, param);
			iCols = Integer.parseInt(String.valueOf(((Map) (lMap.get(0)))
					.get("num")));
		} catch (Exception e) {
			m_logger.error("get result rows number error", e);
		} finally {
			closeJDBCConnection();
		}

		return iCols;
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
		initJDBCConnection();
		int result = 0;
		PreparedStatement stmt = null;

		try {
			// connection = ConnectionManager.getConnection();
			stmt = connection.prepareStatement(strSQL);

			for (int k = 0; k < elements.length; k++) {
				if (elements[k][1].equalsIgnoreCase("INT")) {
					stmt.setInt(k + 1, Integer.parseInt(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase("STRING")) {
					stmt.setString(k + 1, elements[k][0]);
				}

				if (elements[k][1].equalsIgnoreCase("OBJECT")) {
					stmt.setObject(k + 1, elements[k][0]);
				}

				if (elements[k][1].equalsIgnoreCase("LONG")) {
					stmt.setLong(k + 1, Long.parseLong(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase("DOUBLE")) {
					stmt.setDouble(k + 1, Double.parseDouble(elements[k][0]));
				}

				if (elements[k][1].equalsIgnoreCase("FLOAT")) {
					stmt.setFloat(k + 1, Float.parseFloat(elements[k][0]));
				}
			}
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			result = 0;
			m_logger.error("处理失败：" + e.getMessage());
		} finally {
			closeJDBCConnection();
			
			try {
				if(stmt!=null){
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	// private static String convertCharacter(String source){
	// String target = null;
	//		
	// try{
	// // target = new String(source.getBytes("gbk"),"iso8859_1");
	// // target = Encoder.toGB(source);
	// target = source;
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//		
	// return target;
	// }

	// private static String convertCharacter(Object source){
	// return convertCharacter(String.valueOf(source));
	// }
	//	
	// private static String[] convertCharacter(String[] source){
	// String[] target = new String[source.length];
	//		
	// for(int k=0;k<source.length;k++){
	// target[k] = convertCharacter(source[k]);
	// }
	//		
	// return target;
	// }
	//	
	// private static String[] convertCharacter(Object[] source){
	// String[] target = new String[source.length];
	// for(int k=0;k<source.length;k++){
	// target[k]=convertCharacter(source[k]);
	// }
	// return convertCharacter(target);
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
