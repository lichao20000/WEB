/**
 * @(#)CommonCursor.java 2006-2-12
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class CommonCursor {
	
	/**
	 * 属地
	 * @return
	 */
	public static Cursor getCityCursor() {
		
		String strSQL = "select city_id,city_name from tab_city order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}
	
	/**
	 * 局向
	 * @return
	 */
	public static Cursor getOfficeCursor() {
		
		String strSQL = "select office_id,office_name from tab_office order by office_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}


	/**
	 * 小区
	 * @return
	 */
	public static Cursor getZoneCursor() {
		
		String strSQL = "select zone_id,zone_name from tab_zone order by zone_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}
	

	/**
	 * 资源类型
	 * @return
	 */
	public static Cursor getResourceTypeCursor() {
		
		String strSQL = "select resource_type_id,resource_name from tab_resourcetype order by resource_type_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}
	
	
	/**
	 * 厂商
	 * @return
	 */
	public static Cursor getVendorCursor() {
		
		String strSQL = "select vendor_id,vendor_name,vendor_add from tab_vendor order by vendor_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}
	
	/**
	 * 设备
	 * @return
	 */
	public static Cursor getDeviceNameCursor() {
		
		String strSQL = "select distinct device_name from tab_devicetype_info";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}


	/**
	 * os_version
	 * @return
	 */
	public static Cursor getOsVersionCursor() {
		
		String strSQL = "select distinct os_version from tab_devicetype_info";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getCursor(strSQL);
	}

}
