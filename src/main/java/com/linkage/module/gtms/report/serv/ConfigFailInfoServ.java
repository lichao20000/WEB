package com.linkage.module.gtms.report.serv;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-27
 * @category com.linkage.module.gtms.report.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface ConfigFailInfoServ
{	
	
	public List<Map> queryfailinfo(String start_time,String end_time,String city_id,int curPage_splitPage, int num_splitPage);
	
	public int countQueryfailinfo(String start_time, String end_time, String city_id, int curPage_splitPage, int num_splitPage);

	public int countQueryfailinfoExcel(String start_time, String end_time, String city_id);

	public List<Map> getQueryfailinfoExcel(String start_time, String end_time, String city_id);
}
