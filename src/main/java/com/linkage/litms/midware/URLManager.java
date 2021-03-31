package com.linkage.litms.midware;

import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class URLManager {
	
	public Map getURLInfoById (String id) {
		String sql = "select * from mid_ware_url where id='" + id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select mid_path from mid_ware_url where id='" + id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return DataSetBean.getRecord(sql);
	}
	
	public String getURLPathById (String id) {
		Map map = getURLInfoById(id);
		Object obj = map.get("mid_path");
		if (obj != null) {
			return (String)obj;
		}
		return null;
	}
}
