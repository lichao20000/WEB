package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

public class SimulateSheetSxNewDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(SimulateSheetSxNewDAO.class);

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getUserInfo(String username, String gw_type) {
		logger.debug("getUserInfo({})", username);
		StringBuffer sql = new StringBuffer();
		if (gw_type.equals("1")) {
			sql.append("select access_style_id from tab_hgwcustomer " +
					"where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		} else if (gw_type.equals("2")) {
			sql.append("select access_style_id from tab_egwcustomer " +
					"where (user_state='1' or user_state='2') and username='")
					.append(username).append("'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map<String, String>> list = this.jt.query(psql.getSQL(),
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderType", rs.getString("access_style_id"));
						return map;
					}
				});
		return list;
	}
}
