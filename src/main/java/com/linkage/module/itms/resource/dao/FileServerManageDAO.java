
package com.linkage.module.itms.resource.dao;

import java.util.List;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

public class FileServerManageDAO extends SuperDAO {

	public List queryServer() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from TAB_ITMS_SERVER order by id asc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	public List queryServer(String id) {
		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql.append("select server_name, host, port, username, password from TAB_ITMS_SERVER where id="+id+"");
		}
		else {
			sql.append("select * from TAB_ITMS_SERVER where id="+id+"");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
		return list;
	}
}
