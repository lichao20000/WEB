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
import com.linkage.module.itms.resource.dao.FunctionDeploymentByAreaDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-9
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class FunctionDeploymentByAreaBIO
{
	private static Logger logger = LoggerFactory.getLogger(FunctionDeploymentByAreaBIO.class);
	
	private FunctionDeploymentByAreaDAO dao;
	
	public List<Map> quertFunctionDeployByArea(String city_id, String gn, String end_time){
		logger.debug("FunctionDeploymentByAreaBIO=>quertFunctionDeployByArea");
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		
		Map<String,String> map = dao.quertFunctionDeployByArea(city_id, gn, end_time);
		
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
	
	public List<Map> quertFunctionDeployByAreaList(String city_id, String gn, String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("FunctionDeploymentByAreaBIO=>quertFunctionDeployByAreaList");
		return dao.quertFunctionDeployByAreaList(city_id, gn, end_time, curPage_splitPage, num_splitPage);
	}
	
	public int countQuertFunctionDeployByAreaList(String city_id, String gn, String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("FunctionDeploymentByAreaBIO=>countQuertFunctionDeployByAreaList");
		return dao.countQuertFunctionDeployByAreaList(city_id, gn, end_time, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> excelQuertFunctionDeployByAreaList(String city_id, String gn, String end_time){
		logger.debug("FunctionDeploymentByAreaBIO=>excelQuertFunctionDeployByAreaList");
		return dao.excelQuertFunctionDeployByAreaList(city_id, gn, end_time);
	}
	
	
	public FunctionDeploymentByAreaDAO getDao()
	{
		return dao;
	}

	
	public void setDao(FunctionDeploymentByAreaDAO dao)
	{
		this.dao = dao;
	}
}
