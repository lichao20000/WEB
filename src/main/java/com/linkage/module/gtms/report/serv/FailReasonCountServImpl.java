package com.linkage.module.gtms.report.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.report.dao.FailReasonCountDao;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class FailReasonCountServImpl implements FailReasonCountServ {
	private static Logger logger = LoggerFactory.getLogger(FailReasonCountServImpl.class);
	
	private FailReasonCountDao dao;
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> failReasonCount(String cityId, String starttime,
			String endtime) {
		List<Map<String,String>> resList = new ArrayList<Map<String,String>>();
		//错误码对应的名称
		Map<String,String> failCodeMap = dao.getFailCode();
		//查询结果
		Map<String,Map<String,Integer>> map2 = dao.getFailReasonCount(cityId,starttime,endtime);
		
		Map<String,String> resMap = null;
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		Iterator  it =  failCodeMap.keySet().iterator();
		while(it.hasNext()){
			String failCode = (String)it.next();
			resMap = new HashMap<String,String>();
			resMap.put("failReason", failCodeMap.get(failCode));
			Map<String,Integer> tempMap = map2.get(failCode);
			if(null != tempMap){
            			for(int j=0;j<cityList.size();j++){
            				String city_ID = cityList.get(j);
            				ArrayList<String> subList = CityDAO.getAllNextCityIdsByCityPid(city_ID);
            				int addNum = 0;
            				for (int k = 0; k < subList.size(); k++) {
            					String subCityId = subList.get(k);
            					addNum += StringUtil.getIntegerValue(tempMap.get(subCityId));
            				}
            				resMap.put(city_ID, addNum+"");
    		    		}
			}
			else{
				for(int j=0;j<cityList.size();j++){
    				String city_ID = cityList.get(j);
    				resMap.put(city_ID, "0");
	    		}
			}
			resList.add(resMap);
		}
		//以上只是和查询出来的结果对应，下面是接着处理没有值的数据和小记
		//处理小计	
		HashMap<String,String> totalNumMap= new HashMap<String,String>();
		totalNumMap.put("failReason", "小计");
		for(int j=0;j<cityList.size();j++){
			String city_ID = cityList.get(j);
			int totalNum = 0;
			for (int k = 0; k < resList.size(); k++) {
				Map tempMap = resList.get(k);
				tempMap.get(city_ID);
				totalNum += StringUtil.getIntegerValue(tempMap.get(city_ID));
			}
			totalNumMap.put(city_ID, totalNum+"");
		}
		resList.add(totalNumMap);
		return resList;
	}
	
	public void setDao(FailReasonCountDao dao) {
		this.dao = dao;
	}
}
