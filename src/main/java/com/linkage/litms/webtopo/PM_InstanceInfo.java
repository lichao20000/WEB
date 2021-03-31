package com.linkage.litms.webtopo;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class PM_InstanceInfo {
	private String strSQL = "select * from pm_map_instance where device_id=? and expressionid=? and indexid=?";
	private PrepareSQL pSQL = new PrepareSQL();
	private HashMap map_Instanceinfo = null;
	
	public PM_InstanceInfo () {
		
	}
	
	public HashMap setBaseInstance(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		String expressionid = request.getParameter("expressionid");
		String indexid = request.getParameter("instanceIndex");
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select intodb, collect from pm_map_instance where device_id=? and expressionid=? and indexid=?";
		}
		pSQL.setSQL(strSQL);
		pSQL.setStringExt(1,device_id,true);
		pSQL.setStringExt(2,expressionid,false);
		pSQL.setStringExt(3,indexid,true);
		
		map_Instanceinfo = DataSetBean.getRecord(pSQL.getSQL());
		
		return map_Instanceinfo;
	}
	
}
