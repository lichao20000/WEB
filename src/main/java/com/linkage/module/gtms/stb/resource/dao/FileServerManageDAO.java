
package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

public class FileServerManageDAO extends SuperDAO {


	public List queryServer() {
		StringBuffer sql = new StringBuffer();
		sql.append("select id, server_name, host, port, username, password, fileserverport " +
				" from stb_tab_file_server order by id asc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		return list;
	}

	public List queryServer(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select server_name, host, port, username, password from stb_tab_file_server where id="+id+"");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		return list;
	}
}
