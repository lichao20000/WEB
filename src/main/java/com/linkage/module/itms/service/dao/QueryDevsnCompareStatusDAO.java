package com.linkage.module.itms.service.dao;

import java.util.Map;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-11-2
 * @category com.linkage.module.itms.service.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDevsnCompareStatusDAO
{
	public Map<String,String> getDevsnCompareStatus(String loid)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select loid,devsnfrominform,devsnfromeserver,comparetime,comeparestatus ");
		psql.append("from devsn_compare_log where loid = '" + loid);
		psql.append("' order by comparetime desc");
		return DBOperation.getRecord(psql.getSQL());
	}
}
