package com.linkage.module.gtms.report.serv;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.report.dao.BusinessOpenCountDao;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;


public class BusinessOpenCountServImpl implements BusinessOpenCountServ {
	private static Logger logger = LoggerFactory.getLogger(BusinessOpenCountServImpl.class);
	
	private BusinessOpenCountDao dao ; 
	
	@SuppressWarnings("unchecked")
	public List<Map> countAll(String cityId, String starttime, String endtime, String selectTypeId) {
		logger.warn("BusinessOpenCountServImpl====>countAll({},{},{},{})",new Object[]{cityId,starttime,endtime,selectTypeId});
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		List<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		
		if (Global.NMGDX.equals(Global.instAreaShortName) && "00".equals(cityId))
		{
			cityList.clear();
			String str = "17199;17299;17099;18299;17599;17699;17999;17499;17799;17899;17399;18399;00";
//			String str = "551;552;00";
			cityList =  Arrays.asList(str.split(";"));
		}
		else
		{
			Collections.sort(cityList);
		}
		//获取所有用户开户情况
		List lt = dao.getAllSheetNum(cityId, starttime,endtime,selectTypeId);
		Map<String,String> tempMap1 = new HashMap<String,String>();
		Map<String,String> tempMap2 = new HashMap<String,String>();
		Map<String,String> tempMap3 = new HashMap<String,String>();
		if(!lt.isEmpty()){
			for (int i = 0; i < lt.size(); i++) {
				Map rmap = (Map)lt.get(i);
				String openStatus = StringUtil.getStringValue(rmap.get("open_status"));
				//下发成功
				if(openStatus.equals("1")){
					tempMap1.put(StringUtil.getStringValue(rmap.get("city_id")),StringUtil
							.getStringValue(rmap.get("num")));
				}
				//下发失败
				if(openStatus.equals("-1")){
					tempMap2.put(StringUtil.getStringValue(rmap.get("city_id")),StringUtil
							.getStringValue(rmap.get("num")));
				}
				//未下发
				if(openStatus.equals("0")){
					tempMap3.put(StringUtil.getStringValue(rmap.get("city_id")),StringUtil
							.getStringValue(rmap.get("num")));
				}
			}
		}
		long notOpenNumTal = 0l;
		long successOpenNumTal = 0l;
		long failOpenNumTal = 0l;
		for (int i = 0; i < cityList.size(); i++) {
			String city_id = cityList.get(i);
			ArrayList<String> subList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			Map<String, Object> tmap = new HashMap<String, Object>();
			
			tmap.put("cityId", city_id);
			tmap.put("cityName", cityMap.get(city_id));
			
			//未下发 
			long notOpenNum = 0l;
			// 下发成功 
			long successOpenNum = 0l;
			// 下发失败 
			long failOpenNum = 0l;
			if(city_id.equals(cityId))
			{
				successOpenNum =  StringUtil.getLongValue(tempMap1.get(city_id));
				failOpenNum    =  StringUtil.getLongValue(tempMap2.get(city_id));
				notOpenNum     =  StringUtil.getLongValue(tempMap3.get(city_id));
			}
			else
			{
				for (int j = 0; j < subList.size(); j++) {
					String subCityId = subList.get(j);
					successOpenNum = successOpenNum + StringUtil.getLongValue(tempMap1.get(subCityId));
					failOpenNum    = failOpenNum    + StringUtil.getLongValue(tempMap2.get(subCityId));
					notOpenNum     = notOpenNum     + StringUtil.getLongValue(tempMap3.get(subCityId));
				}
			}
			notOpenNumTal += notOpenNum;
			successOpenNumTal += successOpenNum;
			failOpenNumTal += failOpenNum;
			
			tmap.put("notOpenNum", notOpenNum);
			tmap.put("successOpenNum", successOpenNum);
			tmap.put("failOpenNum", failOpenNum);
			tmap.put("openNum", failOpenNum+successOpenNum);
			tmap.put("totalNum", failOpenNum+successOpenNum+notOpenNum);
			tmap.put("successRate", percent(successOpenNum,(failOpenNum+successOpenNum)));
			
			
			list.add(tmap);
			subList = null;
		}
		if (Global.NMGDX.equals(Global.instAreaShortName) && "00".equals(cityId))
		{
			Map<String, Object> tal = new HashMap<String, Object>();
			tal.put("cityId", "00");
			tal.put("cityName", "汇总");
			tal.put("notOpenNum", notOpenNumTal);
			tal.put("successOpenNum", successOpenNumTal);
			tal.put("failOpenNum", failOpenNumTal);
			tal.put("openNum", failOpenNumTal + successOpenNumTal);
			tal.put("totalNum", failOpenNumTal + successOpenNumTal + notOpenNumTal);
			tal.put("successRate",
					percent(successOpenNumTal, (failOpenNumTal + successOpenNumTal)));
			list.add(tal);
		}
		cityMap = null;
		cityList = null;
		
		return list;
	}
	@Override
	public List<Map> getUserList(String openStatus,String cityId,String parentCityId, String starttime,
			String endtime, int curPageSplitPage, int numSplitPage, String selectTypeId) {
		
		return dao.getUserList(openStatus,cityId,parentCityId,starttime,endtime,curPageSplitPage,numSplitPage,selectTypeId);
	}
	public int getUserCount(String openStatus,String cityId,String parentCityId, String starttime, String endtime,
			int curPageSplitPage, int numSplitPage, String selectTypeId) {
		
		return dao.getUserCount(openStatus,cityId, parentCityId,starttime,endtime,curPageSplitPage,numSplitPage,selectTypeId);
	}

	public List<Map> getUserListExcel(String openStatus, String cityId,String parentCityId,
			String starttime, String endtime, String selectTypeId) {
		// TODO Auto-generated method stub
		return dao.getUserListExcel(openStatus,cityId, parentCityId, starttime,endtime, selectTypeId);
	}

	/**
	 * 计算百分比 
	 * @param p1  分子
	 * @param p2  分母
	 * @return
	 */
	public String percent(long p1, long p2){
		
		logger.debug("percent({},{})", new Object[]{p1, p2});
		
		double p3;
		if (p2 == 0){
			return "N/A";
		}else{
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public void setDao(BusinessOpenCountDao dao) {
		this.dao = dao;
	}
	
}
