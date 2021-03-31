package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.dao.VoiceFailResonQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-30
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceFailResonQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(VoiceFailResonQueryBIO.class);
	
	private VoiceFailResonQueryDAO dao;
	
	
	public List<Map> voiceFailResonQueryInfo(String start_time, String end_time, String city_id){
		
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		
		Map<String,String> oneMap = dao.voiceFailOneInfo(start_time, end_time, city_id);
		Map<String,String> twoMap = dao.voiceFailTwoInfo(start_time, end_time, city_id);
		Map<String,String> threeMap = dao.voiceFailThreeInfo(start_time, end_time, city_id);
		Map<String,String> fourMap = dao.voiceFailFourInfo(start_time, end_time, city_id);
		Map<String,String> fiveMap = dao.voiceFailFiveInfo(start_time, end_time, city_id);
		
		long oneNum = 0;
		long twoNum = 0;
		long threeNum = 0;
		long fourNum = 0;
		long fiveNum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			//语音端口1，统计信息
			oneNum = 0;
			//语音端口1，未启用统计信息
			twoNum = 0;
			//语音端口1，统计信息
			threeNum = 0;
			//语音端口1，未启用统计信息
			fourNum = 0;
			//语音端口1,2，同时为未启用信息
			fiveNum = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != oneMap && !oneMap.isEmpty())
				{
					oneNum += StringUtil.getLongValue(oneMap.get(tlist.get(j)));
				}
				if (null != twoMap && !twoMap.isEmpty())
				{
					twoNum += StringUtil.getLongValue(twoMap.get(tlist.get(j)));
				}
				if (null != threeMap && !threeMap.isEmpty())
				{
					threeNum += StringUtil.getLongValue(threeMap.get(tlist.get(j)));
				}
				if (null != fourMap && !fourMap.isEmpty())
				{
					fourNum += StringUtil.getLongValue(fourMap.get(tlist.get(j)));
				}
				if (null != fiveMap && !fiveMap.isEmpty())
				{
					fiveNum += StringUtil.getLongValue(fiveMap.get(tlist.get(j)));
				}
			}
			tmap.put("oneNum", oneNum);
			tmap.put("twoNum", twoNum);
			tmap.put("threeNum", threeNum);
			tmap.put("fourNum", fourNum);
			tmap.put("fiveNum", fiveNum);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	public List<Map> voiceFailDeviceQueryInfo(String city_id,String reason, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("voiceDeviceQueryInfo()");
		return dao.voiceFailDeviceQueryInfo(city_id, reason, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public int countVoiceFailDeviceQueryInfo(String city_id, String reason, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("countVoiceDeviceQueryInfo()");
		return dao.countVoiceFailDeviceQueryInfo(city_id, reason, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> voiceFailDeviceQueryExcel(String city_id,String reason, String start_time,String end_time){
		logger.debug("voiceFailDeviceQueryExcel()");
		return dao.voiceFailDeviceQueryExcel(city_id, reason, start_time, end_time);
	}
	
	public VoiceFailResonQueryDAO getDao()
	{
		return dao;
	}


	
	public void setDao(VoiceFailResonQueryDAO dao)
	{
		this.dao = dao;
	}
}
