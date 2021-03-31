package com.linkage.litms.webtopo;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

public class DeviceResourceInfo {
	
	public DeviceResourceInfo() {
		strResource = "select * from tab_gw_device a left join sgw_security b on a.device_id=b.device_id  where a.device_id = ?";
	}

	public Cursor getDeviceResource(String device_id) {
		PrepareSQL pSQL = new PrepareSQL(strResource);
		pSQL.setString(1, device_id);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());

		return cursor; 
	}

	private String strResource;
}
