package com.linkage.litms.uss;

import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

public class DBUtil {

	/**
	 * 根据业务用户账号取得device_id
	 * @param username
	 * @return
	 */
	public static String getDeviceID(String username) {
		String device_id;
		String sql = "select device_id from tab_gw_device where device_serialnumber"
				+ " =(select device_serialnumber from tab_egwcustomer where username='" + username
				+ "' and user_state='1')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map<String, String> map = DataSetBean.getRecord(sql);
		if (null == map) {
			device_id = null;
		} else {
			device_id = map.get("device_id");
		}
		
		return device_id;
	}
	
	/**
	 * 根据业务用户账号取得device_name,gather_id
	 * @param username
	 * @return
	 */
	public static String getDeviceInfo(String username) {
		String device_name;
		String gather_id;
		StringBuilder strBuilder = new StringBuilder();
		String sql = "select device_name, gather_id from tab_gw_device where device_serialnumber"
				+ " =(select device_serialnumber from tab_egwcustomer where username='" + username
				+ "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map<String, String> map = DataSetBean.getRecord(sql);
		if (null == map) {
			return null;
		} else {
			device_name = map.get("device_name");
			gather_id = map.get("gather_id");
		}
		
		return strBuilder.append(device_name).append(",").append(gather_id).toString();
	}
}
