package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import com.linkage.module.itms.resource.dao.QueryDeviceByVoipIpDAO;

/**
 * 
 * @author Reno (Ailk NO.)
 * @version 1.0
 * @since 2015年1月29日
 * @category bio.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class QueryDeviceByVoipIpBIO
{
	private QueryDeviceByVoipIpDAO dao;
	
	public void truncate(){
		dao.truncateTempTable();
	}
	
	public void insertTempTable(List<String> list){
		dao.insertTempTable(list);
	}
	
	public List<Map<String,Object>> dealQueryPaged(int curPage_splitPage, int num_splitPage){
		return dao.dealQueryPaged(curPage_splitPage, num_splitPage);
	}
	public List<Map<String,Object>> dealQuery(){
		return dao.dealQuery();
	}
	
	public Integer queryCount(){
		return dao.queryCount();
	}

	
	public QueryDeviceByVoipIpDAO getDao()
	{
		return dao;
	}

	
	public void setDao(QueryDeviceByVoipIpDAO dao)
	{
		this.dao = dao;
	}
	
	
}
