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
import com.linkage.module.ids.dao.DeviceTVBDAO;

public class DeviceTVBBIO {
	
	private Logger logger = LoggerFactory.getLogger(DeviceTVBBIO.class);
	
	private DeviceTVBDAO dao;
	
	@SuppressWarnings("unchecked")
	public List<Map> queryByTVB(int curPage_splitPage,int num_splitPage,String indexName,String indexType,String starttime,String endtime,String cityId,String temperature,String bais_current,String vottage){
		
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		Map map = dao.queryByTVB(curPage_splitPage,num_splitPage,indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage);
		
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId) && !"-1".equals(cityId)){
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("cityId", cityId);
			tmap.put("cityName", cityMap.get(cityId));
			tmap.put("total", String.valueOf(map.get(cityId)));
			tmap.put("starttime", starttime);
			tmap.put("endtime", endtime);
			tmap.put("temperature", temperature);
			tmap.put("bais_current", bais_current);
			tmap.put("vottage", vottage);
			list.add(tmap);
		}else{
			List cityList=CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityList);
			for (int i = 0; i <cityList.size(); i++) {
				Map<String, Object> tmap = new HashMap<String, Object>();
				tmap.put("cityId", cityList.get(i));
				tmap.put("cityName", cityMap.get(cityList.get(i)));
				tmap.put("total",String.valueOf(map.get(cityList.get(i))));
				tmap.put("starttime", starttime);
				tmap.put("endtime", endtime);
				tmap.put("temperature", temperature);
				tmap.put("bais_current", bais_current);
				tmap.put("vottage", vottage);
				list.add(tmap);
			}
		}
			
		return list;
	}
	public List<Map<String, Object>> queryByTVBList(String indexName,String indexType,String starttime,String endtime,String cityId,String temperature,String bais_current,String vottage,int curPage_splitPage, int num_splitPage){
		return dao.queryByTVBList(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
	}
	
	public int queryByTVBCount(String indexName,String indexType,String starttime,String endtime,String cityId,String temperature,String bais_current,String vottage,int curPage_splitPage, int num_splitPage){
		return dao.queryByTVBCount(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> queryByTVBExcel(String indexName,String indexType,String starttime,String endtime,String cityId,String temperature,String bais_current,String vottage,int curPage_splitPage, int num_splitPage,int total){
		return dao.queryByTVBExcel(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage, total);
	}
	
	public int queryByTVBTotal(String indexName,String indexType,String starttime,String endtime,String cityId,String temperature,String bais_current,String vottage,int curPage_splitPage, int num_splitPage){
		return dao.queryByTVBTotal(indexName, indexType, starttime, endtime, cityId, temperature, bais_current, vottage, curPage_splitPage, num_splitPage);
	}
	public DeviceTVBDAO getDao() {
		return dao;
	}

	public void setDao(DeviceTVBDAO dao) {
		this.dao = dao;
	}

}
