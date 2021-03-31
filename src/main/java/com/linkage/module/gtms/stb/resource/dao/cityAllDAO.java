package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-1
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class cityAllDAO extends SuperDAO
{
	public Map<String, String> queryData(String city_id)
	{
		StringBuffer sql = new StringBuffer("select parent_id from tab_city  where 1=1");
		if (!StringUtil.IsEmpty(city_id)) {
			sql.append(" and city_id = '" + city_id + "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return DBOperation.getRecord(psql.getSQL());
	}

	public Map<String, String> querycity(String city_id)
	{
		StringBuffer sql = new StringBuffer("select parent_id  from tab_city  where 1=1");
		if (!StringUtil.IsEmpty(city_id)) {
			sql.append(" and city_id  = '" + city_id + "' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return DBOperation.getRecord(psql.getSQL());
	}
	public List<Map<String, String>> querycityid(String city_id)
	{
		StringBuffer sql = new StringBuffer("select city_id,city_name  from tab_city  where 1=1");
		if (!StringUtil.IsEmpty(city_id)) {
			sql.append(" and city_id  = '" + city_id + "' ");
		}
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
}
