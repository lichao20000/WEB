package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.TerminalEnvironmentStatementDAO;

public class TerminalEnvironmentStatementBIO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalEnvironmentStatementBIO.class);
	
	private TerminalEnvironmentStatementDAO dao;
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryTerminalEnvironmentStatement(String city_id, String start_time, String end_time,String temperature,String bais_current, String voltage){
		logger.debug("TerminalEnvironmentStatementDAO=>queryTerminalEnvironmentStatement");
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		
		Map<String,String> map = dao.queryTerminalEnvironmentStatement(city_id, start_time, end_time,temperature, bais_current,  voltage);
		
		long num = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			num = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != map && !map.isEmpty())
				{
					String str = StringUtil.getStringValue(map.get(tlist.get(j)));
					if (null != str && !"".equals(str.trim()))
					{
						num = num + StringUtil.getLongValue(str);
					}
				}
			}
			tmap.put("deploy_total", StringUtil.getStringValue(num));
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> queryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage, int curPage_splitPage, int num_splitPage){
		logger.debug("TerminalEnvironmentStatementDAO=>queryTerminalEnvironmentStatementList");
		return dao.queryTerminalEnvironmentStatementList(city_id, start_time, end_time,temperature,bais_current,voltage, curPage_splitPage, num_splitPage);
	}
	
	public int countQueryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage, int curPage_splitPage, int num_splitPage){
		logger.debug("TerminalEnvironmentStatementDAO=>countQueryTerminalEnvironmentStatementList");
		return dao.countQueryTerminalEnvironmentStatementList(city_id, start_time, end_time,temperature,bais_current,voltage, curPage_splitPage, num_splitPage);
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> excelQueryTerminalEnvironmentStatementList(String city_id, String start_time, String end_time,String temperature,String bais_current,String voltage){
		logger.debug("TerminalEnvironmentStatementDAO=>excelQueryTerminalEnvironmentStatementList");
		return dao.excelQueryTerminalEnvironmentStatementList(city_id, start_time, end_time,temperature,bais_current,voltage);
	}

	public TerminalEnvironmentStatementDAO getDao() {
		return dao;
	}

	public void setDao(TerminalEnvironmentStatementDAO dao) {
		this.dao = dao;
	}
}
