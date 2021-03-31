package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.NetWorkQualityAnalyseDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-27
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class NetWorkQualityAnalyseBIO
{
	
	private static Logger logger = LoggerFactory
			.getLogger(NetWorkQualityAnalyseBIO.class);
	
	private NetWorkQualityAnalyseDAO dao;

	public List<Map> netWorkQualityAnalyseInfo(String start_time, String end_time,String device_serialnumber, String loid, int curPage_splitPage,
			int num_splitPage){
		return dao.netWorkQualityAnalyseInfo(start_time, end_time, device_serialnumber, loid, curPage_splitPage, num_splitPage);
	}
	
	public int countNetWorkQualityAnalyseInfo(String start_time, String end_time,String device_serialnumber, String loid, int curPage_splitPage,
			int num_splitPage){
		return dao.countNetWorkQualityAnalyseInfo(start_time, end_time, device_serialnumber, loid, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> netWorkQualityAnalyseInfoExcel(String start_time, String end_time,String device_serialnumber, String loid){
		return dao.netWorkQualityAnalyseInfoExcel(start_time, end_time, device_serialnumber, loid);
	}
	
	
	public NetWorkQualityAnalyseDAO getDao()
	{
		return dao;
	}

	
	public void setDao(NetWorkQualityAnalyseDAO dao)
	{
		this.dao = dao;
	}
}
