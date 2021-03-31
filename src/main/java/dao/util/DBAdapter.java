/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package dao.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * db connection pool.
 * 
 * @author gaoyi
 * @version 1.6
 */
public class DBAdapter {
	/** log */
	private static final Logger LOG = LoggerFactory
			.getLogger(DBAdapter.class);

	/** db connection */
	private static Connection connection = null;

	/**
	 * Retrieve the JDBC connection to PHFW database.
	 * @param alias :the alias of proxool
	 * @return Connection
	 */
	public static synchronized Connection getJDBCConnection(String alias) {
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			connection = DriverManager.getConnection(alias);
		} catch (Exception e) {
			LOG.error("Exception:get jdbc-connection error,{}", e.getMessage());
			connection = null;
		}

		return connection;
	}

	/**
	 * get the url of database.
	 * @param alias :the alias of proxool
	 * 
	 */
	public static String getDriverURL(String alias) {

		try {
			ConnectionPoolDefinitionIF cpd = ProxoolFacade
					.getConnectionPoolDefinition(alias);

			return cpd.getUrl();
		} catch (ProxoolException e) {
			LOG.error("ProxoolException:{}", e.getMessage());
		}

		return null;
	}
	 public static int GetDB(String alias)
	    {
		 	LOG.debug("GetDB()");
	        String db = getDriverURL(alias);
	        LOG.debug("db url:{}", db);
	        if(db == null)
	            return 0;
	        if(db.indexOf("oracle") >= 0)
	            return 1;
	        return db.indexOf("sybase") < 0 ? 0 : 2;
	    }
}
