package com.linkage.module.gtms.report.serv;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.report.dao.ConfigFailInfoDaoImpl;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-27
 * @category com.linkage.module.gtms.report.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class ConfigFailInfoSerImpl implements ConfigFailInfoServ
{
	
	private ConfigFailInfoDaoImpl configFailInfoDao = null;
	@Override
	public List<Map> queryfailinfo(String start_time, String end_time, String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		return configFailInfoDao.queryfailinfo(start_time, end_time, city_id, curPage_splitPage, num_splitPage);
	}
	


	@Override
	public int countQueryfailinfo(String start_time, String end_time, String city_id,
			int curPage_splitPage, int num_splitPage)
	{
		// TODO Auto-generated method stub
		return configFailInfoDao.countQueryfailinfo(start_time, end_time, city_id, curPage_splitPage, num_splitPage);
	}

	@Override
	public int countQueryfailinfoExcel(String start_time, String end_time, String city_id)
	{
		// TODO Auto-generated method stub
		return configFailInfoDao.countQueryfailinfoExcel(start_time, end_time, city_id);
	}

	@Override
	public List<Map> getQueryfailinfoExcel(String start_time, String end_time,
			String city_id)
	{
		// TODO Auto-generated method stub
		return configFailInfoDao.getQueryfailinfoExcel(start_time, end_time, city_id);
	}
	
	public ConfigFailInfoDaoImpl getConfigFailInfoDao()
	{
		return configFailInfoDao;
	}
	
	public void setConfigFailInfoDao(ConfigFailInfoDaoImpl configFailInfoDao)
	{
		this.configFailInfoDao = configFailInfoDao;
	}
	
	
}
