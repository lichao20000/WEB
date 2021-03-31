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
import com.linkage.module.itms.resource.dao.VoiceOrderQueryDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-28
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class VoiceOrderQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(VoiceOrderQueryBIO.class);
	
	private VoiceOrderQueryDAO dao;
	
	
	public List<Map> voiceOrderQueryInfo(String city_id,String start_time,String end_time){
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		// 语音端口1，统计信息
		Map<String, String> lineOne =  dao.voiceOrderTotalOneInfo(city_id, start_time, end_time);
		// 语音端口1，未其用户信息
		Map<String, String> lineNoOne = dao.voiceOrderLineOneInfo(city_id, start_time, end_time);
		// 语音端口1，统计信息
		Map<String, String> linetwo =  dao.voiceOrderTotalTwoInfo(city_id, start_time, end_time);
		// 语音端口1，未其用户信息
		Map<String, String> lineNotwo = dao.voiceOrderLineTwoInfo(city_id, start_time, end_time);
		//语音端口1,2，同时未启用信息
		Map<String, String> lineNoonetwo = dao.voiceOrderLineOneTwoInfo(city_id, start_time, end_time);
		//语音端口1，统计信息
		long lineOneNum = 0;
		//语音端口1，未启用统计信息
		long lineOneNoNum = 0;
		//语音端口1，统计信息
		long lineTwoNum = 0;
		//语音端口1，未启用统计信息
		long lineTwoNoNum = 0;
		//语音端口1,2，同时为未启用信息
		long lineOneTwoNoNum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			//语音端口1，统计信息
			 lineOneNum = 0;
			//语音端口1，未启用统计信息
			 lineOneNoNum = 0;
			//语音端口1，统计信息
			 lineTwoNum = 0;
			//语音端口1，未启用统计信息
			 lineTwoNoNum = 0;
			//语音端口1,2，同时为未启用信息
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
				if (null != lineNoOne && !lineNoOne.isEmpty())
				{
					lineOneNoNum += StringUtil.getLongValue(lineNoOne.get(tlist.get(j)));
				}
				if (null != linetwo && !linetwo.isEmpty())
				{
					lineTwoNum += StringUtil.getLongValue(linetwo.get(tlist.get(j)));
				}
				if (null != lineNotwo && !lineNotwo.isEmpty())
				{
					lineTwoNoNum += StringUtil.getLongValue(lineNotwo.get(tlist.get(j)));
				}
				if (null != lineNoonetwo && !lineNoonetwo.isEmpty())
				{
					lineOneTwoNoNum += StringUtil.getLongValue(lineNoonetwo.get(tlist.get(j)));
				}
			}
			tmap.put("lineOneNum", lineOneNum);
			tmap.put("lineOneNoNum", lineOneNoNum);
			tmap.put("lineTwoNum", lineTwoNum);
			tmap.put("lineTwoNoNum", lineTwoNoNum);
			tmap.put("lineOneTwoNoNum", lineOneTwoNoNum);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}
	
	public List<Map> voiceDeviceQueryInfo(String city_id, String start_time,
			String end_time,String numInfo, int curPage_splitPage, int num_splitPage){
		logger.debug("voiceDeviceQueryInfo()");
		return dao.voiceDeviceQueryInfo(city_id, start_time, end_time,numInfo, curPage_splitPage, num_splitPage);
	}
	
	public int countVoiceDeviceQueryInfo(String city_id, String start_time,
			String end_time,String numInfo, int curPage_splitPage, int num_splitPage){
		logger.debug("countVoiceDeviceQueryInfo()");
		return dao.countVoiceDeviceQueryInfo(city_id, start_time, end_time, numInfo,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> voiceDeviceQueryExcel(String city_id, String start_time,String end_time, String numInfo){
		logger.debug("voiceDeviceQueryExcel()");
		return dao.voiceDeviceQueryExcel(city_id, start_time, end_time, numInfo);
	}
	
	public VoiceOrderQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(VoiceOrderQueryDAO dao)
	{
		this.dao = dao;
	}
}
