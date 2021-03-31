package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.E8CActiveReportDAO;

public class E8CActiveReportBIO {

	private static Logger logger = LoggerFactory.getLogger(E8CActiveReportBIO.class);
	
	private E8CActiveReportDAO dao;
	
	public List<Map> getE8CNum(String startDealdate,String endDealdate,String cityId,String isActive){
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		Map map=dao.getE8CNum(startDealdate, endDealdate, cityId, isActive);
		
		
		long total = 0;
		long totals = 0;
		ArrayList<String> tlist = null;
		
		if(!StringUtil.IsEmpty(isActive)&&"0".equals(isActive)){
			Map map1=dao.getE8cNoDevice(startDealdate, endDealdate, cityId, isActive);
			Map map2=dao.getE8cNotIsActive(startDealdate, endDealdate, cityId, isActive);
			for (int i = 0; i < cityList.size(); i++)
			{
				Map<String, Object> tmap = new HashMap<String, Object>();
				total = 0;
				totals = 0;
				String city_id = cityList.get(i);
				tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
				tmap.put("cityId", city_id);
				tmap.put("city_name", cityMap.get(city_id));
				long temp =0;
				for (int j = 0; j < tlist.size(); j++)
				{
					totals = totals + StringUtil.getLongValue(map.get(tlist.get(j)));
					temp =  StringUtil.getLongValue(map1.get(tlist.get(j)))+StringUtil.getLongValue(map2.get(tlist.get(j)));
					total = total + temp;
				}
				String percent=getDecimal(String.valueOf(total),String.valueOf(totals));
				tmap.put("totals", totals);
				tmap.put("total", total);
				if(percent.equals("0%")){
					tmap.put("percent", "N/A");
				}else{
					tmap.put("percent", percent);
				}
				tmap.put("startDealdate", startDealdate);
				tmap.put("endDealdate", endDealdate);
				tmap.put("isActive", isActive);
				list.add(tmap);
				tlist=null;
			}
		}else{
		
		Map rmap=dao.getBindNum(startDealdate, endDealdate, cityId, isActive);
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			totals = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("cityId", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				totals = totals + StringUtil.getLongValue(map.get(tlist.get(j)));
				total =  total+ StringUtil.getLongValue(rmap.get(tlist.get(j)));
			}
			String percent=getDecimal(String.valueOf(total),String.valueOf(totals));
			tmap.put("totals", totals);
			tmap.put("total", total);
			if(percent.equals("0%")){
				tmap.put("percent", "N/A");
			}else{
				tmap.put("percent", percent);
			}
			tmap.put("startDealdate", startDealdate);
			tmap.put("endDealdate", endDealdate);
			tmap.put("isActive", isActive);
			list.add(tmap);
			tlist=null;
		}
		
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public List<Map> getCustomerLists(String startDealdate,String endDealdate,String cityId,String isActive,String type,int curPage_splitPage, int num_splitPage){
		return dao.getCustomerLists(startDealdate, endDealdate, cityId, isActive, type, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getCityExcel(String startDealdate,String endDealdate,String cityId,String isActive){
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		Map map=dao.getE8CNum(startDealdate, endDealdate, cityId, isActive);
		Map rmap=dao.getBindNum(startDealdate, endDealdate, cityId, isActive);
		long total = 0;
		long totals = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			totals = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				totals = totals + StringUtil.getLongValue(map.get(tlist.get(j)));
				total =  total+ StringUtil.getLongValue(rmap.get(tlist.get(j)));
			}
			String percent=getDecimal(String.valueOf(total),String.valueOf(totals));
			tmap.put("totals", totals);
			tmap.put("total", total);
			if(percent.equals("0%")){
				tmap.put("percent", "N/A");
			}else{
				tmap.put("percent", percent);
			}
			list.add(tmap);
			tlist=null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public int queryCusCount(String startDealdate,String endDealdate,String cityId,String isActive,String type,int curPage_splitPage, int num_splitPage){
		return dao.queryCusCount(startDealdate, endDealdate, cityId, isActive, type, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getCustomerExcel(String startDealdate,String endDealdate,String cityId,String isActive,String type){
		return dao.getCustomerExcel(startDealdate, endDealdate, cityId, isActive, type);
	}
	/**
	 * 占用比列
	 * 
	 * @param molecular
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public String getDecimal(String molecular, String denominator)
	{
		if (null == molecular || "0".equals(molecular) || null == denominator
				|| "0".equals(denominator))
		{
			return "0%";
		}
		float t1 = Float.parseFloat(molecular);
		float t2 = Float.parseFloat(denominator);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}

	public E8CActiveReportDAO getDao() {
		return dao;
	}

	public void setDao(E8CActiveReportDAO dao) {
		this.dao = dao;
	}
	
}
