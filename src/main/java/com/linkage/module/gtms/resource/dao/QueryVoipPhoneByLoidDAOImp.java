package com.linkage.module.gtms.resource.dao;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;


public class QueryVoipPhoneByLoidDAOImp extends SuperDAO implements QueryVoipPhoneByLoidDAO {

	private static Logger logger = LoggerFactory
			.getLogger(QueryVoipPhoneByLoidDAOImp.class);


	public List<HashMap<String, String>> queryVoipPhoneByLoid(String loidStr) {

		logger.debug("QueryVoipPhoneByLoidDAOImp==>queryVoipPhoneByLoid()");

		PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
		psql.append("select a.city_id, a.username, c.line_id, c.voip_phone");
		psql.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_voip_serv_param c");
		psql.append(" where 1=1");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and a.user_id = c.user_id ");
		psql.append("   and b.serv_type_id = 14 ");  // 业务类型为VOIP
		psql.append("   and a.username in(");
		psql.append(loidStr);
		psql.append(")");
		psql.append(" order by a.username, c.line_id");  // 此处的排序不要随意更改

		return DBOperation.getRecords(psql.getSQL());
	}



}
