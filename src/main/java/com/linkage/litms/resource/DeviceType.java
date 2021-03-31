/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.resource;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

;

/**
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Nov 27, 2007
 * @see DeviceAct
 * @since 1.0
 */
public class DeviceType {

	/**
	 * 获取设备型号列表
	 * 
	 * @param type
	 * 		<li>1:TR069</li>
	 * 		<li>2:SNMP</li>
	 * @return
	 */
	public static Cursor getDeviceTypeList(int type) {
		String sql = "select * from "
			+ (1 == type ? "tab_devicetype_info" : "tab_devicetype_info");
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return DataSetBean.getCursor(sql);
	}
}
