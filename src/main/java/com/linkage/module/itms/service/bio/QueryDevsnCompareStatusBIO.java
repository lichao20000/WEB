package com.linkage.module.itms.service.bio;

import java.util.Map;

import com.linkage.module.itms.service.dao.QueryDevsnCompareStatusDAO;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-11-2
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDevsnCompareStatusBIO
{
	private QueryDevsnCompareStatusDAO dao;
	
	public Map<String,String> getDevsnCompareStatus(String loid)
	{
		return dao.getDevsnCompareStatus(loid);
	}
	
	public QueryDevsnCompareStatusDAO getDao()
	{
		return dao;
	}

	public void setDao(QueryDevsnCompareStatusDAO dao)
	{
		this.dao = dao;
	}
}
