
package com.linkage.module.itms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;

/**
 * @author chenzhangjian (Ailk No.)
 * @version 1.0
 * @since 2015-8-13
 * @category com.linkage.module.itms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MemcachedManageDAO
{

	/**
	 * 查询缓存库信息
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryMemcachedInfos()
	{
		PrepareSQL psql = new PrepareSQL(
				"select memid,mname,ipaddress,port,memdesc from tab_info_memorycaches where status = 1 order by mname");
		return DBOperation.getRecords(psql.getSQL());
	}
}
