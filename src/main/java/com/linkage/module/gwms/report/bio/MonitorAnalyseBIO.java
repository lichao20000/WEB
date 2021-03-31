package com.linkage.module.gwms.report.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SysManager.longArrHelper;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.report.dao.MonitorAnalyseDAO;

import flex.messaging.io.ArrayList;


/**
 * 
 * @author zzd (Ailk No.)
 * @version 1.0
 * @since 2016-12-26
 * @category com.linkage.module.gwms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorAnalyseBIO
{
	private MonitorAnalyseDAO dao;
	public List<Map> queryMonitorAnalyse(int curPage_splitPage,int num_splitPage,String startTime,String endTime,String indexType,String instance, String ipSelect ){
		startTime = startTime+" 00:00:00";
		endTime = endTime+" 23:59:59";
		long start_time = new DateTimeUtil(startTime).getLongTime();
		long end_time = new DateTimeUtil(endTime).getLongTime();
		List<Map> list = dao.queryMonitorAnalyse(curPage_splitPage, num_splitPage, start_time, end_time, indexType,instance,ipSelect);
		return list;
	}
	public List<Map> queryMonitorAnalyseForExcel(String startTime,String endTime,String indexType ,String instance,String ipSelect){
		startTime = startTime+" 00:00:00";
		endTime = endTime+" 23:59:59";
		long start_time = new DateTimeUtil(startTime).getLongTime();
		long end_time = new DateTimeUtil(endTime).getLongTime();
		List<Map> list = dao.queryMonitorAnalyseForExcel( start_time, end_time, indexType, instance,ipSelect);
		if("4".equals(indexType)){
			for(Map map:list){
				DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(
						StringUtil.getStringValue(map, "gettime")) * 1000L);
				map.put("gettime", dt.getYYYY_MM_DD_HH_mm_ss());

			}
		}
		return list;
	}
	public int queryMonitorAnalyseCount(int curPage_splitPage,int num_splitPage,String startTime,String endTime,String indexType ,String instance, String ipSelect){
		startTime = startTime+" 00:00:00";
		endTime = endTime+" 23:59:59";
		long start_time = new DateTimeUtil(startTime).getLongTime();
		long end_time = new DateTimeUtil(endTime).getLongTime();
		return dao.queryMonitorAnalyseCount(curPage_splitPage, num_splitPage, start_time, end_time, indexType, instance,ipSelect);
	}
	public List<Map> queryIpMsg(){
		List<Map> list = dao.queryIpMsg();
		List<Map> returnList = new ArrayList();
		Map returnMap = new HashMap();
		for(Map map:list){
			String hostip = (String) map.get("hostip");
			String hostname = (String) map.get("hostname");
			String hostValue = hostip+"("+hostname+")";
			returnMap.put("hostip", hostip);
			returnMap.put("hostValue",hostValue );
			returnList.add(returnMap);
		}
		
		return returnList;
	}
	
	
	public MonitorAnalyseDAO getDao()
	{
		return dao;
	}

	public void setDao(MonitorAnalyseDAO dao)
	{
		this.dao = dao;
	}

}
