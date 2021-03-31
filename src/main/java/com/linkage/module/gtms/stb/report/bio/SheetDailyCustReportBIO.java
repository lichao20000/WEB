/**
 * 
 */
package com.linkage.module.gtms.stb.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.report.dao.SheetDailyCustReportDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 工单报表
 * fan'j'm
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2018-4-20
 * @category com.linkage.module.gtms.stb.report.bio
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class SheetDailyCustReportBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetDailyCustReportBIO.class);
	
	private SheetDailyCustReportDAO dao;
	
	/**
	 * 地市-数据map
	 * <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	private Map<String,Map<String,Integer>> allStatsMap = null;
	
	private int maxPage_splitPage;

	/**
	 * 工单统计报表(结果给页面使用)
	 * @param deviceType     终端类型
	 * @param cityId         地市
	 * @param servTypeId     业务类型
	 * @param startOpenDate  开始时间
	 * @param endOpenDate    结束时间
	 * @param dateList 
	 * @return [<cityId=00,cityName=nanjing, succNum=0, failNum=0, notNum=0> , ...]
	 */
	public List<Map<String, String>> getStatsReport(String cityId,  String servTypeId, String startOpenDate, String endOpenDate, List<String> dateList)
	{
		logger.warn("getAllStats({},{},{})", new Object[]{ cityId, startOpenDate, endOpenDate});
		List<String> beginlist = new ArrayList<String>();
		List<String> endlist = new ArrayList<String>();
		getTime(startOpenDate,endOpenDate,beginlist,endlist,dateList);
		
		//全部时间区间的结果list
		List<List<Map<String, String>>> alllist = new ArrayList<List<Map<String, String>>>();
		List<String> alllist1 = new ArrayList<String>();
		if("2".equals(servTypeId)){
			for(int k=0;k<beginlist.size();k++){
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				list = dao.getStatsGroupbyCity(cityId, beginlist.get(k), endlist.get(k));
				alllist.add(list);
			}
		}
		else{
			for(int k=0;k<beginlist.size();k++){
				int num = 0;
				num = dao.getStats(cityId, beginlist.get(k), endlist.get(k));
				alllist1.add(StringUtil.getStringValue(num));
			}
		}
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		if("1".equals(servTypeId)){
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("city_name","STB");
			
			for(int i=0;i<alllist1.size();i++){
				resOne.put("date"+i,alllist1.get(i));
			}
			result.add(resOne);
		}
		else if("2".equals(servTypeId)){
			// 根据当前cityId获取下一级的cityId
			List<String> cityList = CityDAO.getNextCityIdsByCityPidCore("00");
			// 所有地市的map，根据cityID获取cityName
			Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
			// 自地市id排序
			Collections.sort(cityList);
			// 保证当前地市在首位展示
			cityList.add(0, cityId);
	
			for(String city:cityList){
				Map<String,String> resOne = new HashMap<String,String>();
				resOne.put("city_name",cityMap.get(city));
				
				for(int i=0;i<alllist.size();i++){
					int num = 0;
					for (Map<String,String> one:alllist.get(i)) {
						if(city.equals(StringUtil.getStringValue(one.get("city_id")))){
							num = StringUtil.getIntegerValue(one.get("num"));
							break;
						}
					}
					resOne.put("date"+i,StringUtil.getStringValue(num));
				}
				result.add(resOne);
			}
		}
		else if("3".equals(servTypeId)){
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("city_name","其他");
			
			for(int i=0;i<alllist1.size();i++){
				resOne.put("date"+i,alllist1.get(i));
			}
			result.add(resOne);
			
			Map<String,String> resOne1 = new HashMap<String,String>();
			resOne1.put("city_name","桥接");
			
			for(int i=0;i<alllist1.size();i++){
				resOne1.put("date"+i,"0");
			}
			result.add(resOne1);
			
			Map<String,String> resOne2 = new HashMap<String,String>();
			resOne2.put("city_name","路由");
			
			for(int i=0;i<alllist1.size();i++){
				resOne2.put("date"+i,"0");
			}
			result.add(resOne2);
		}
		else if("4".equals(servTypeId)){
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("city_name","iptv");
			
			for(int i=0;i<alllist1.size();i++){
				resOne.put("date"+i,alllist1.get(i));
			}
			result.add(resOne);
		}
		else if("5".equals(servTypeId)){
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("city_name","IPTV用户");
			
			for(int i=0;i<alllist1.size();i++){
				resOne.put("date"+i,alllist1.get(i));
			}
			result.add(resOne);
		}
		
		print(result);
		return result;
	}
	
	private void getTime(String startOpenDate, String endOpenDate,
			List<String> beginlist, List<String> endlist, List<String> dateList)
	{
		//开始日
		String startDateStr = new DateTimeUtil(StringUtil.getLongValue(startOpenDate)*1000).getDate();
		//开始日得秒数
		long startDatel = new DateTimeUtil(startDateStr).getLongTime();
		//结束日
		String endDateStr = new DateTimeUtil(StringUtil.getLongValue(endOpenDate)*1000).getDate();
		//结束日得秒数
		long endDatel = new DateTimeUtil(endDateStr).getLongTime() + 86399;

		long tmpstart = startDatel;
		long tmpend = tmpstart+86399;
		beginlist.add(StringUtil.getStringValue(tmpstart));
		endlist.add(StringUtil.getStringValue(tmpend));
		dateList.add(startDateStr);
		
		while(tmpstart+86400<=endDatel){
			tmpstart = tmpstart+86400;
			tmpend = tmpend+86400;
			dateList.add(new DateTimeUtil(tmpstart*1000).getDate());
			beginlist.add(StringUtil.getStringValue(tmpstart));
			endlist.add(StringUtil.getStringValue(tmpend));
		}
		for(int i=0;i<beginlist.size();i++){
			System.out.println(beginlist.get(i)+","+endlist.get(i)+","+dateList.get(i));
		}
	}
	
	public static void main(String[] args)
	{
		List<String> beginlist = new ArrayList<String>();
		List<String> endlist = new ArrayList<String>();
		List<String> dateList = new ArrayList<String>();
		SheetDailyCustReportBIO a = new SheetDailyCustReportBIO();
		a.getTime("1522478235","1524551835",beginlist,endlist,dateList);
	}
	private void print(List<Map<String,String>> s){
		try{
			for(Map<String,String> one:s){
		    	for (String ss : one.keySet()) {
		    		logger.warn(ss+"," + one.get(ss).toString());
		    	}
		    }
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	

	public Map<String, Map<String, Integer>> getAllStatsMap() {
		return allStatsMap;
	}

	public void setAllStatsMap(Map<String, Map<String, Integer>> allStatsMap) {
		this.allStatsMap = allStatsMap;
	}
	
	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}

	public SheetDailyCustReportDAO getDao() {
		return dao;
	}

	public void setDao(SheetDailyCustReportDAO dao) {
		this.dao = dao;
	}

	public void transData(List<Map> detailReportList) {
		Map map = null;
		for(int i=0; i<detailReportList.size(); i++)
		{
			map = detailReportList.get(i);
			if(StringUtil.IsEmpty(StringUtil.getStringValue(map.get("type_id"))))
			{
				map.put("type_id", "");
			}
			else if(Integer.parseInt(StringUtil.getStringValue(map.get("type_id"))) == 2)
			{
				map.put("type_id", "E8-C");
			}
			else if(Integer.parseInt(StringUtil.getStringValue(map.get("type_id"))) == 1)
			{
				map.put("type_id", "E8-B");
			}
			else
			{
				map.put("type_id", "");
			}
			
			if(StringUtil.IsEmpty(StringUtil.getStringValue(map.get("open_status"))))
			{
				map.put("open_status", "");
			}
			else if(Integer.parseInt(StringUtil.getStringValue(map.get("open_status"))) == 1)
			{
				map.put("open_status", "成功");
			}
			else if(Integer.parseInt(StringUtil.getStringValue(map.get("open_status"))) == -1)
			{
				map.put("open_status", "失败");
			}
			else if(Integer.parseInt(StringUtil.getStringValue(map.get("open_status"))) == 0)
			{
				map.put("open_status", "未做");
			}
		}
		
	}
}
