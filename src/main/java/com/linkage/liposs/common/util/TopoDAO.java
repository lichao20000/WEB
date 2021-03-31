/**
 * Asiainfo-Linkage,Inc.<BR>
 * Copyright 2005-2010. All right reserved.
 */
package com.linkage.liposs.common.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBAdapter;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.module.gwms.Global;

/**
 * DAO OF device:tab_gw_device,gw_devicestatus,gw_exception,sgw_security
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Mar 5, 2009
 * @see
 * @since 1.0
 */
public class TopoDAO {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(TopoDAO.class);

	public static long MAX_UNUSED_DEVICEID = -1L;
	
	public static long MIN_UNUSED_DEVICEID = -1L;
	
	public static int SUM_UNUSED_DEVICEID = 50;
	
	/**
	 * get device_id
	 * 
	 * @param count
	 * @return
	 */
	synchronized static public long GetUnusedDeviceSerial(int count) {
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return GetUnusedDeviceSerialOld(count);
		}
		
		// TELEDB
		return DbUtils.getUnusedID("sql_tab_gw_device", count);
	}
	
	/**
	 * get device_id
	 * 
	 * @param count
	 * @return
	 */
	static public long GetUnusedDeviceSerialOld(int count) {
		logger.debug("GetUnusedDeviceSerial({})", count);

		long serial = -1;

		if (count <= 0) {
			serial = -2;

			return serial;
		}

		if( MIN_UNUSED_DEVICEID < 0 ){
						
			if (DBUtil.GetDB() == Global.DB_ORACLE) {
				// oracle
				MIN_UNUSED_DEVICEID = getMaxDeviceId4Oracle(SUM_UNUSED_DEVICEID) - 1;
			} else if (DBUtil.GetDB() == Global.DB_SYBASE) {
				// sybase
				MIN_UNUSED_DEVICEID = getMaxDeviceId4Sybase(SUM_UNUSED_DEVICEID) - 1;
			}
			MAX_UNUSED_DEVICEID = MIN_UNUSED_DEVICEID + SUM_UNUSED_DEVICEID;
		}
		
		if( MAX_UNUSED_DEVICEID < (MIN_UNUSED_DEVICEID + count)){
			
			if(SUM_UNUSED_DEVICEID < count){

				if (DBUtil.GetDB() == Global.DB_ORACLE) {
					// oracle
					MIN_UNUSED_DEVICEID = getMaxDeviceId4Oracle(count) - 1;
				} else if (DBUtil.GetDB() == Global.DB_SYBASE) {
					// sybase
					MIN_UNUSED_DEVICEID = getMaxDeviceId4Sybase(count) - 1;
				}
				MAX_UNUSED_DEVICEID = MIN_UNUSED_DEVICEID + count;
			} else {
				
				if (DBUtil.GetDB() == Global.DB_ORACLE) {
					// oracle
					MIN_UNUSED_DEVICEID = getMaxDeviceId4Oracle(SUM_UNUSED_DEVICEID) - 1;
				} else if (DBUtil.GetDB() == Global.DB_SYBASE) {
					// sybase
					MIN_UNUSED_DEVICEID = getMaxDeviceId4Sybase(SUM_UNUSED_DEVICEID) - 1;
				}
				MAX_UNUSED_DEVICEID = MIN_UNUSED_DEVICEID + SUM_UNUSED_DEVICEID;
			}

		}
		
		serial = MIN_UNUSED_DEVICEID + 1;
		MIN_UNUSED_DEVICEID = MIN_UNUSED_DEVICEID + count;

		logger.debug("ID={}", serial);

		return serial;
	}

	/**
	 * get device_id
	 * 
	 * @param count
	 * @return
	 */
	public static long getMaxDeviceId4Sybase(int count) {
		logger.debug("getMaxDeviceId4Sybase({})", count);

		long serial = -1;

		if (count <= 0) {
			serial = -2;

			return serial;
		}

		String sql = "maxTR069DeviceIdProc ?";
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setInt(1, count);

		return DBOperation.executeProcSelect(pSQL.getSQL());
	}

	/**
	 * get device_id
	 * 
	 * @param count
	 * @return
	 */
	public static long getMaxDeviceId4Oracle(int count) {
		logger.debug("getMaxDeviceId4Oracle({})", count);

		long serial = -1;

		if (count <= 0) {
			serial = -2;

			return serial;
		}

		CallableStatement cstmt = null;
		Connection conn = null;
		String sql = "{call maxTR069DeviceIdProc(?,?)}";

		try {
			conn = DBAdapter.getJDBCConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, count);
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();
			serial = cstmt.getLong(2);
		} catch (Exception e) {
			logger.error("GetUnusedDeviceSerial Exception:{}", e.getMessage());
		} finally {
			sql = null;

			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) {
					logger.error("cstmt.close SQLException:{}", e.getMessage());
				}
				cstmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.error("conn.close error:{}", e.getMessage());
				}

				conn = null;
			}
		}

		return serial;
	}
}
