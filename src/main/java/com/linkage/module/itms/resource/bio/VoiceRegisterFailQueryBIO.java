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
import com.linkage.module.itms.resource.dao.VoiceRegisterFailQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-31
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceRegisterFailQueryBIO
{
	
	private static Logger logger = LoggerFactory.getLogger(VoiceRegisterFailQueryBIO.class);
	
	private VoiceRegisterFailQueryDAO dao;
	
	public List<Map> voiceOrderQueryInfo(String city_id,String start_time,String end_time){
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		// 语音端口1注册失败统计信息
		Map<String, String> lineOne =  dao.voiceRegisterFailOne(city_id, start_time, end_time);
		// 语音端口2注册失败统计信息
		Map<String, String> linetwo = dao.voiceRegisterFailTwo(city_id, start_time, end_time);
		// 语音端口1,2同时统计信息
		Map<String, String> lineOneAndTwo =  dao.voiceRegisterFailOneAndTwo(city_id, start_time, end_time);
		//语音端口1，统计信息
		long lineOneNum = 0;
		//语音端口1，统计信息
		long lineTwoNum = 0;
		//语音端口1,2，同时为未启用信息
		long lineOneTwoNoNum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			//语音端口1，统计信息
			 lineOneNum = 0;
			//语音端口2，统计信息
			 lineTwoNum = 0;
			//语音端口1，2未启用统计信息
			 lineOneTwoNoNum = 0;
			 
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != lineOne && !lineOne.isEmpty())
				{
					lineOneNum += StringUtil.getLongValue(lineOne.get(tlist.get(j)));
				}
				if (null != linetwo && !linetwo.isEmpty())
				{
					lineTwoNum += StringUtil.getLongValue(linetwo.get(tlist.get(j)));
				}
				if (null != lineOneAndTwo && !lineOneAndTwo.isEmpty())
				{
					lineOneTwoNoNum += StringUtil.getLongValue(lineOneAndTwo.get(tlist.get(j)));
				}
			}
			tmap.put("lineOneNum", lineOneNum);
			tmap.put("lineTwoNum", lineTwoNum);
			tmap.put("lineOneTwoNoNum", lineOneTwoNoNum);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	public List<Map> voiceRegisterDeviceQueryInfo(String city_id,String line_id, String start_time,
			String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("voiceRegisterDeviceQueryInfo()");
		return dao.voiceRegisterDeviceQueryInfo(city_id, line_id, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public int countVoiceRegisterDeviceQueryInfo(String city_id, String line_id,String start_time,
			String end_time, int curPage_splitPage, int num_splitPage){
		logger.debug("countVoiceRegisterDeviceQueryInfo()");
		return dao.countVoiceRegisterDeviceQueryInfo(city_id, line_id, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> voiceRegisterDeviceQueryExcel(String city_id,String line_id, String start_time,String end_time){
		logger.debug("voiceRegisterDeviceQueryExcel()");
		return dao.voiceRegisterDeviceQueryExcel(city_id, line_id, start_time, end_time);
	}
	
	public VoiceRegisterFailQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(VoiceRegisterFailQueryDAO dao)
	{
		this.dao = dao;
	}
}
