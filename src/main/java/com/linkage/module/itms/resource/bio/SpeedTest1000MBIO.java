package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.resource.dao.DevBatchRestartQueryDAO;
import com.linkage.module.itms.resource.dao.SpeedTest1000MDAO;


@SuppressWarnings("rawtypes")
public class SpeedTest1000MBIO 
{
	private static Logger logger = LoggerFactory.getLogger(SpeedTest1000MBIO.class);
	
	private SpeedTest1000MDAO dao;
	public List<Map> queryCityId(String area_id){
		return dao.queryCityId(area_id);
	}
	
	public List<Map> query(String speedRet,String bandwidth,String startTime,String endTime,String cityId,int curPage_splitPage, int num_splitPage){
		return dao.query(speedRet, bandwidth, startTime, endTime, cityId, curPage_splitPage, num_splitPage);
	}
	
	public int queryCount(String speedRet,String bandwidth,String startTime,String endTime,String cityId,int curPage_splitPage, int num_splitPage){
		return dao.queryCount(speedRet, bandwidth, startTime, endTime, cityId, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> toExcel(String speedRet,String bandwidth,String startTime,String endTime,String cityId,int curPage_splitPage, int num_splitPage){
		return dao.toExcel(speedRet, bandwidth, startTime, endTime, cityId);
	} 
	
	public SpeedTest1000MDAO getDao() {
		return dao;
	}

	public void setDao(SpeedTest1000MDAO dao) {
		this.dao = dao;
	}

	
}
