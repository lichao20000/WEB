/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.common.database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;

/**
 * db connection pool.
 * 
 * @author alex(yanhj@)
 * @version 1.6
 */
public class DBAdapter {
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DBAdapter.class);
	private final static String alias = "proxool.xml-test";

	/** db connection */
	private static Connection connection = null;

	/**
	 * Retrieve the JDBC connection to PHFW database.
	 * 
	 * @return Connection
	 */
	public static synchronized Connection getJDBCConnection() {
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			connection = DriverManager.getConnection(alias);
		} catch (Exception e) {
			logger.error("Exception:get jdbc-connection error,{}", e.getMessage());
			connection = null;
		}

		return connection;
	}
	
	/**
	 * ��ȡ��ͬ���Դ����ݿ�����
	 * @param dbName
	 * @return
	 */
	public static synchronized Connection getJDBCConnection(String dbName)
	{
		try {
			if(StringUtil.IsEmpty(dbName))
				return getJDBCConnection();
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			connection = DriverManager.getConnection(dbName);
		} catch (Exception e) {
			logger.error("Exception:get jdbc-connection error,{}", e.getMessage());
			connection = null;
		}

		return connection;
	}

	/**
	 * get the url of database.
	 * 
	 */
	public static String getDriverURL() {

		String alias = ProxoolFacade.getAliases()[0];
		try {
			ConnectionPoolDefinitionIF cpd = ProxoolFacade
					.getConnectionPoolDefinition(alias);

			return cpd.getUrl();
		} catch (ProxoolException e) {
			logger.error("ProxoolException:{}", e.getMessage());
		}

		return null;
	}
	
	/**
	 * get the url of database.
	 * 
	 */
	public static String getDriverURL(String aliases) {
		try {
			ConnectionPoolDefinitionIF cpd = ProxoolFacade
					.getConnectionPoolDefinition(aliases);
			return cpd.getUrl();
		} catch (ProxoolException e) {
			logger.error("ProxoolException:{}", e.getMessage());
		}
		return null;
	}
}
