package com.linkage.module.ids.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.ids.dao.PPPoEFailReasonCountDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-13
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class PPPoEFailReasonCountBIO
{
	private static Logger logger = LoggerFactory.getLogger(PPPoEFailReasonCountBIO.class);
	
	private PPPoEFailReasonCountDAO dao;
	
	public List<Map> getPppoeFailReasonCountList(String start_time, String end_time,String city_id){
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		
		Map<String,Long> pppoeList = dao.getPppoeFailReasonCountList(start_time, end_time, city_id);
		long reason1 = 0;
		long reason2 = 0;
		long reason3 = 0;
		
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			reason1 = 0;
			reason2 = 0;
			reason3 = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != pppoeList && !pppoeList.isEmpty())
				{
					String code = tlist.get(j);
					reason1 = reason1+StringUtil.getLongValue(pppoeList.get(code+"ERROR_ISP_TIME_OUT"));
					reason2 = reason2+StringUtil.getLongValue(pppoeList.get(code+"ERROR_AUTHENTICATION_FAILURE"));
					reason3 = reason3+StringUtil.getLongValue(pppoeList.get(code+"ERROR_ISP_DISCONNECT"));
				}
			}
			tmap.put("reason1", reason1);
			tmap.put("reason2", reason2);
			tmap.put("reason3", reason3);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public List<Map> getPppoeFailReasonInfo(String start_time, String end_time,String city_id,String failCode, int curPage_splitPage, int num_splitPage){
		return dao.getPppoeFailReasonInfo(start_time, end_time, city_id, failCode,curPage_splitPage, num_splitPage);
	}
	
	public int countGetPppoeFailReasonInfo(String start_time, String end_time,String city_id, String failCode,int curPage_splitPage, int num_splitPage){
		return dao.countGetPppoeFailReasonInfo(start_time, end_time, city_id, failCode, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getPppoeFailReasonInfoExcel(String start_time, String end_time,String city_id, String failCode){
		return dao.getPppoeFailReasonInfoExcel(start_time, end_time, city_id, failCode);
	}
	
	public PPPoEFailReasonCountDAO getDao()
	{
		return dao;
	}

	
	public void setDao(PPPoEFailReasonCountDAO dao)
	{
		this.dao = dao;
	}
			
}
