package com.linkage.module.gtms.stb.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.StbDeviceCountDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class StbDeviceCountBIO 
{
	private static Logger logger = LoggerFactory.getLogger(StbDeviceCountBIO.class);
	
	/**存储统计数据结果文件*/
	private static final String RESULTFILE=LipossGlobals.getLipossHome()+"/WEB-INF/classes/countResultFile/countResult.txt";
	
	private StbDeviceCountDAO dao;
	
	
	/**
	 * 全省iptv用户终端分布及活跃率情况
	 */
	public List<Map<String, String>> stbCount() 
	{
		List<String> queryCityList=dao.queryCityIdList();
		long start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryAllNumList=dao.queryAllNumList();
		logger.warn("stbCount queryAllNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		start=System.currentTimeMillis()/1000;
		List<Map<String,String>> query4K_ProbeNumList=dao.query4K_ProbeNumList();
		logger.warn("stbCount query4K_ProbeNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryMonthActiveNumList=dao.queryMonthActiveNumList(getBefor1Mon());
		logger.warn("stbCount queryMonthActiveNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		Map<String,String> resultMap;
		
		long all_num=0;
		long Y_4K_num=0;
		long is_probe_num=0;
		long month_active_num=0;
		ArrayList<String> cityList;
		start=System.currentTimeMillis()/1000;
		for(String city_id:queryCityList)
		{
			cityList=CityDAO.getNextCityIdsByCityPid(city_id);
			
			for(Map<String,String> m:queryAllNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					all_num+=StringUtil.getLongValue(m,"all_num");
				}
			}
			
			for(Map<String,String> m:query4K_ProbeNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					Y_4K_num+=StringUtil.getLongValue(m,"four_k_num");
					if("1".equals(StringUtil.getStringValue(m,"is_probe"))){
						is_probe_num+=StringUtil.getLongValue(m,"four_k_num");
					}
				}
			}
			
			for(Map<String,String> m:queryMonthActiveNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					month_active_num+=StringUtil.getLongValue(m,"month_active_num");
				}
			}
			
			resultMap=new HashMap<String,String>();
			resultMap.put("city_name",CityDAO.getCityName(city_id));
			resultMap.put("all_num",StringUtil.getStringValue(all_num));
			resultMap.put("Y_4K_num",StringUtil.getStringValue(Y_4K_num));
			resultMap.put("N_4K_num",StringUtil.getStringValue(all_num - Y_4K_num));
			resultMap.put("is_probe_num",StringUtil.getStringValue(is_probe_num));
			resultMap.put("month_active_num",StringUtil.getStringValue(month_active_num));
			resultMap.put("probe_rate",percent(is_probe_num,Y_4K_num)+"%");
			resultMap.put("month_active_rate",percent(month_active_num,all_num)+"%");
			
			resultList.add(resultMap);
			
			all_num=0;
			Y_4K_num=0;
			is_probe_num=0;
			month_active_num=0;
		}
		
		Map<String,String> countMap=new HashMap<String,String>();
		for(Map<String,String> m:resultList){
			for(String key:m.keySet()){
				if("all_num".equals(key)){
					all_num+=StringUtil.getLongValue(m,key);
				}else if("Y_4K_num".equals(key)){
					Y_4K_num+=StringUtil.getLongValue(m,key);
				}else if("is_probe_num".equals(key)){
					is_probe_num+=StringUtil.getLongValue(m,key);
				}else if("month_active_num".equals(key)){
					month_active_num+=StringUtil.getLongValue(m,key);
				}
			}
		}
		countMap.put("city_name","小计");
		countMap.put("all_num",StringUtil.getStringValue(all_num));
		countMap.put("Y_4K_num",StringUtil.getStringValue(Y_4K_num));
		countMap.put("N_4K_num",StringUtil.getStringValue(all_num - Y_4K_num));
		countMap.put("is_probe_num",StringUtil.getStringValue(is_probe_num));
		countMap.put("month_active_num",StringUtil.getStringValue(month_active_num));
		countMap.put("probe_rate",percent(is_probe_num,Y_4K_num)+"%");
		countMap.put("month_active_rate",percent(month_active_num,all_num)+"%");
		
		resultList.add(countMap);
		logger.warn("stbCount deal time:"+(System.currentTimeMillis()/1000 - start));
		return resultList;
	}
	
	

	/**
	 * 全省iptv用户终端分布及活跃率情况 (开始时间，结束时间)
	 */
	public List<Map<String, String>> stbCount(String startTime, String endTime)
	{
		List<String> queryCityList=dao.queryCityIdList();
		long start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryAllNumList=dao.queryAllNumList();
		//List<Map<String,String>> queryAllNumListByTime=dao.queryAllNumList(startTime,endTime);
		logger.warn("stbCount queryAllNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		start=System.currentTimeMillis()/1000;
		//List<Map<String,String>> query4K_ProbeNumList=dao.query4K_ProbeNumList(startTime,endTime);
		List<Map<String,String>> query4K_ProbeNumList=dao.query4K_ProbeNumList();
		logger.warn("stbCount query4K_ProbeNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		
		start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryMonthActiveNumList=dao.queryMonthActiveNumList(startTime,endTime);
		logger.warn("stbCount queryMonthActiveNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		Map<String,String> resultMap;
		
		long all_num=0;
		long Y_4K_num=0;
		long is_probe_num=0;
		long month_active_num=0;
		ArrayList<String> cityList;
		start=System.currentTimeMillis()/1000;
		for(String city_id:queryCityList)
		{
			cityList=CityDAO.getNextCityIdsByCityPid(city_id);
			
			for(Map<String,String> m:queryAllNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					all_num+=StringUtil.getLongValue(m,"all_num");
				}
			}
			
			
			for(Map<String,String> m:query4K_ProbeNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					Y_4K_num+=StringUtil.getLongValue(m,"four_k_num");
					if("1".equals(StringUtil.getStringValue(m,"is_probe"))){
						is_probe_num+=StringUtil.getLongValue(m,"four_k_num");
					}
				}
			}
			
			for(Map<String,String> m:queryMonthActiveNumList){
				if(cityList.contains(StringUtil.getStringValue(m,"city_id"))){
					month_active_num+=StringUtil.getLongValue(m,"month_active_num");
				}
			}
			
			resultMap=new HashMap<String,String>();
			resultMap.put("city_name",CityDAO.getCityName(city_id));
			resultMap.put("all_num",StringUtil.getStringValue(all_num));
			resultMap.put("Y_4K_num",StringUtil.getStringValue(Y_4K_num));
			resultMap.put("N_4K_num",StringUtil.getStringValue(all_num - Y_4K_num));
			resultMap.put("is_probe_num",StringUtil.getStringValue(is_probe_num));
			resultMap.put("month_active_num",StringUtil.getStringValue(month_active_num));
			resultMap.put("probe_rate",percent(is_probe_num,Y_4K_num)+"%");
			resultMap.put("month_active_rate",percent(month_active_num,all_num)+"%");
			
			resultList.add(resultMap);
			
			all_num=0;
			Y_4K_num=0;
			is_probe_num=0;
			month_active_num=0;
		}
		
		Map<String,String> countMap=new HashMap<String,String>();
		for(Map<String,String> m:resultList){
			for(String key:m.keySet()){
				if("all_num".equals(key)){
					all_num+=StringUtil.getLongValue(m,key);
				}else if("Y_4K_num".equals(key)){
					Y_4K_num+=StringUtil.getLongValue(m,key);
				}else if("is_probe_num".equals(key)){
					is_probe_num+=StringUtil.getLongValue(m,key);
				}else if("month_active_num".equals(key)){
					month_active_num+=StringUtil.getLongValue(m,key);
				}
			}
		}
		countMap.put("city_name","小计");
		countMap.put("all_num",StringUtil.getStringValue(all_num));
		countMap.put("Y_4K_num",StringUtil.getStringValue(Y_4K_num));
		countMap.put("N_4K_num",StringUtil.getStringValue(all_num - Y_4K_num));
		countMap.put("is_probe_num",StringUtil.getStringValue(is_probe_num));
		countMap.put("month_active_num",StringUtil.getStringValue(month_active_num));
		countMap.put("probe_rate",percent(is_probe_num,Y_4K_num)+"%");
		countMap.put("month_active_rate",percent(month_active_num,all_num)+"%");
		
		resultList.add(countMap);
		logger.warn("stbCount deal time:"+(System.currentTimeMillis()/1000 - start));
		return resultList;
	}
	
	/**
	 * 全省非4K机顶盒分布报表
	 */
	public List<Map<String, String>> stbN4KCount() 
	{
		List<String> queryCityList=dao.queryCityIdList();
		long start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryN4KNumList=dao.queryN4KNumList();
		logger.warn("stbN4KCount queryN4KNumList time:"+(System.currentTimeMillis()/1000 - start));
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		Map<String,String> resultMap;
		
		long city_all_num=0;
		long hw_num=0;
		long cw_num=0;
		long zx_num=0;
		long ch_num=0;
		long fh_num=0;
		long bst_num=0;
		long js_num=0;
		long other_num=0;
		ArrayList<String> cityList;
		
		start=System.currentTimeMillis()/1000;
		for(String city_id:queryCityList)
		{
			cityList=CityDAO.getNextCityIdsByCityPid(city_id);
			
			for(Map<String,String> m:queryN4KNumList)
			{
				if(cityList.contains(StringUtil.getStringValue(m,"city_id")))
				{
					hw_num+=StringUtil.getLongValue(m,"hw_num");
					cw_num+=StringUtil.getLongValue(m,"cw_num");
					zx_num+=StringUtil.getLongValue(m,"zx_num");
					ch_num+=StringUtil.getLongValue(m,"ch_num");
					fh_num+=StringUtil.getLongValue(m,"fh_num");
					bst_num+=StringUtil.getLongValue(m,"bst_num");
					js_num+=StringUtil.getLongValue(m,"js_num");
					other_num+=StringUtil.getLongValue(m,"other_num");
					
					city_all_num=hw_num+cw_num+zx_num+ch_num+fh_num+bst_num+js_num+other_num;
				}
			}
			
			resultMap=new HashMap<String,String>();
			resultMap.put("city_name",CityDAO.getCityName(city_id));
			resultMap.put("hw_num",StringUtil.getStringValue(hw_num));
			resultMap.put("cw_num",StringUtil.getStringValue(cw_num));
			resultMap.put("zx_num",StringUtil.getStringValue(zx_num));
			resultMap.put("fh_num",StringUtil.getStringValue(fh_num));
			resultMap.put("ch_num",StringUtil.getStringValue(ch_num));
			resultMap.put("bst_num",StringUtil.getStringValue(bst_num));
			resultMap.put("js_num",StringUtil.getStringValue(js_num));
			resultMap.put("other_num",StringUtil.getStringValue(other_num));
			resultMap.put("city_all_num",StringUtil.getStringValue(city_all_num));
			
			resultList.add(resultMap);
			
			city_all_num=0;
			hw_num=0;
			cw_num=0;
			zx_num=0;
			ch_num=0;
			fh_num=0;
			bst_num=0;
			js_num=0;
			other_num=0;
		}
		
		for(Map<String,String> m:resultList){
			for(String key:m.keySet()){
				if("hw_num".equals(key)){
					hw_num+=StringUtil.getLongValue(m,key);
				}else if("cw_num".equals(key)){
					cw_num+=StringUtil.getLongValue(m,key);
				}else if("zx_num".equals(key)){
					zx_num+=StringUtil.getLongValue(m,key);
				}else if("fh_num".equals(key)){
					fh_num+=StringUtil.getLongValue(m,key);
				}else if("ch_num".equals(key)){
					ch_num+=StringUtil.getLongValue(m,key);
				}else if("bst_num".equals(key)){
					bst_num+=StringUtil.getLongValue(m,key);
				}else if("js_num".equals(key)){
					js_num+=StringUtil.getLongValue(m,key);
				}else if("other_num".equals(key)){
					other_num+=StringUtil.getLongValue(m,key);
				}else if("city_all_num".equals(key)){
					city_all_num+=StringUtil.getLongValue(m,key);
				}
			}
		}
		
		Map<String,String> countMap=new HashMap<String,String>();
		countMap.put("city_name","小计");
		countMap.put("hw_num",StringUtil.getStringValue(hw_num));
		countMap.put("cw_num",StringUtil.getStringValue(cw_num));
		countMap.put("zx_num",StringUtil.getStringValue(zx_num));
		countMap.put("fh_num",StringUtil.getStringValue(fh_num));
		countMap.put("ch_num",StringUtil.getStringValue(ch_num));
		countMap.put("bst_num",StringUtil.getStringValue(bst_num));
		countMap.put("js_num",StringUtil.getStringValue(js_num));
		countMap.put("other_num",StringUtil.getStringValue(other_num));
		countMap.put("city_all_num",StringUtil.getStringValue(city_all_num));
		
		resultList.add(countMap);
		logger.warn("stbN4KCount deal time:"+(System.currentTimeMillis()/1000 - start));
		return resultList;
	}
	
	/**
	 * 全省iptv用户终端分布及活跃率情况
	 */
	public List<Map<String, String>> stb4KCount() 
	{
		List<String> queryCityList=dao.queryCityIdList();
		long start=System.currentTimeMillis()/1000;
		List<Map<String,String>> queryN4KNumList  = dao.query4KNumList();
		
		logger.warn("stbN4KCount query4KNumList:"+(System.currentTimeMillis()/1000 - start));
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		Map<String,String> resultMap;
		
		long hw_4k_num=0;
		long hw_probe_num=0;
		long cw_4k_num=0;
		long cw_probe_num=0;
		long zx_4k_num=0;
		long zx_probe_num=0;
		long ch_4k_num=0;
		long ch_probe_num=0;
		long fh_4k_num=0;
		long fh_probe_num=0;
		long bst_4k_num=0;
		long bst_probe_num=0;
		long js_4k_num=0;
		long js_probe_num=0;
		long other_4k_num=0;
		long other_probe_num=0;
		long sum_4k_num=0;
		long sum_probe_num=0;
		ArrayList<String> cityList;
		
		start=System.currentTimeMillis()/1000;
		for(String city_id:queryCityList)
		{
			cityList=CityDAO.getNextCityIdsByCityPid(city_id);
			
			for(Map<String,String> m:queryN4KNumList)
			{
				if(cityList.contains(StringUtil.getStringValue(m,"city_id")))
				{
					String vendor_id=StringUtil.getStringValue(m,"vendor_id");
					long yprobe_num=StringUtil.getLongValue(m,"y_probe_num");
					long y4k_num=StringUtil.getLongValue(m,"four_k_num");
					
					if("10".equals(vendor_id)){//中兴
						zx_4k_num+=y4k_num;
						zx_probe_num+=yprobe_num;
					}else if("15".equals(vendor_id)){//长虹
						ch_4k_num+=y4k_num;
						ch_probe_num+=yprobe_num;
					}else if("2".equals(vendor_id)){//华为
						hw_4k_num+=y4k_num;
						hw_probe_num+=yprobe_num;
					}else if("11".equals(vendor_id)){//创维
						cw_4k_num+=y4k_num;
						cw_probe_num+=yprobe_num;
					}else if("20".equals(vendor_id)){//烽火
						fh_4k_num+=y4k_num;
						fh_probe_num+=yprobe_num;
					}else if("29".equals(vendor_id)){//杰赛
						js_4k_num+=y4k_num;
						js_probe_num+=yprobe_num;
					}else if("17".equals(vendor_id)){//百事通
						bst_4k_num+=y4k_num;
						bst_probe_num+=yprobe_num;
					}else{//其他
						other_4k_num+=y4k_num;
						other_probe_num+=yprobe_num;
					}
				}
			}
			
			sum_4k_num=zx_4k_num+ch_4k_num+cw_4k_num+hw_4k_num
						+fh_4k_num+js_4k_num+bst_4k_num+other_4k_num;
			sum_probe_num=zx_probe_num+ch_probe_num+cw_probe_num+hw_probe_num
						+fh_probe_num+bst_probe_num+js_probe_num+other_probe_num;
			
			resultMap=new HashMap<String,String>();
			resultMap.put("city_name",CityDAO.getCityName(city_id));
			resultMap.put("hw_4k_num",StringUtil.getStringValue(hw_4k_num));
			resultMap.put("hw_probe_num",StringUtil.getStringValue(hw_probe_num));
			resultMap.put("cw_4k_num",StringUtil.getStringValue(cw_4k_num));
			resultMap.put("cw_probe_num",StringUtil.getStringValue(cw_probe_num));
			resultMap.put("zx_4k_num",StringUtil.getStringValue(zx_4k_num));
			resultMap.put("zx_probe_num",StringUtil.getStringValue(zx_probe_num));
			resultMap.put("fh_4k_num",StringUtil.getStringValue(fh_4k_num));
			resultMap.put("fh_probe_num",StringUtil.getStringValue(fh_probe_num));
			resultMap.put("ch_4k_num",StringUtil.getStringValue(ch_4k_num));
			resultMap.put("ch_probe_num",StringUtil.getStringValue(ch_probe_num));
			resultMap.put("bst_4k_num",StringUtil.getStringValue(bst_4k_num));
			resultMap.put("bst_probe_num",StringUtil.getStringValue(bst_probe_num));
			resultMap.put("js_4k_num",StringUtil.getStringValue(js_4k_num));
			resultMap.put("js_probe_num",StringUtil.getStringValue(js_probe_num));
			resultMap.put("other_4k_num",StringUtil.getStringValue(other_4k_num));
			resultMap.put("other_probe_num",StringUtil.getStringValue(other_probe_num));
			resultMap.put("sum_4k_num",StringUtil.getStringValue(sum_4k_num));
			resultMap.put("sum_probe_num",StringUtil.getStringValue(sum_probe_num));
			
			resultList.add(resultMap);
			
			hw_4k_num=0;
			hw_probe_num=0;
			cw_4k_num=0;
			cw_probe_num=0;
			zx_4k_num=0;
			zx_probe_num=0;
			ch_4k_num=0;
			ch_probe_num=0;
			fh_4k_num=0;
			fh_probe_num=0;
			bst_4k_num=0;
			bst_probe_num=0;
			js_4k_num=0;
			js_probe_num=0;
			other_4k_num=0;
			other_probe_num=0;
			sum_4k_num=0;
			sum_probe_num=0;
		}
		
		for(Map<String,String> m:resultList)
		{
			for(String key:m.keySet())
			{
				if("zx_4k_num".equals(key)){
					zx_4k_num+=StringUtil.getLongValue(m,key);
				}else if("zx_probe_num".equals(key)){
					zx_probe_num+=StringUtil.getLongValue(m,key);
				}else if("ch_4k_num".equals(key)){
					ch_4k_num+=StringUtil.getLongValue(m,key);
				}else if("ch_probe_num".equals(key)){
					ch_probe_num+=StringUtil.getLongValue(m,key);
				}else if("cw_4k_num".equals(key)){
					cw_4k_num+=StringUtil.getLongValue(m,key);
				}else if("cw_probe_num".equals(key)){
					cw_probe_num+=StringUtil.getLongValue(m,key);
				}else if("hw_4k_num".equals(key)){
					hw_4k_num+=StringUtil.getLongValue(m,key);
				}else if("hw_probe_num".equals(key)){
					hw_probe_num+=StringUtil.getLongValue(m,key);
				}else if("fh_4k_num".equals(key)){
					fh_4k_num+=StringUtil.getLongValue(m,key);
				}else if("fh_probe_num".equals(key)){
					fh_probe_num+=StringUtil.getLongValue(m,key);
				}else if("js_4k_num".equals(key)){
					js_4k_num+=StringUtil.getLongValue(m,key);
				}else if("js_probe_num".equals(key)){
					js_probe_num+=StringUtil.getLongValue(m,key);
				}else if("bst_4k_num".equals(key)){
					bst_4k_num+=StringUtil.getLongValue(m,key);
				}else if("bst_probe_num".equals(key)){
					bst_probe_num+=StringUtil.getLongValue(m,key);
				}else if("other_4k_num".equals(key)){
					other_4k_num+=StringUtil.getLongValue(m,key);
				}else if("other_probe_num".equals(key)){
					other_probe_num+=StringUtil.getLongValue(m,key);
				}else if("sum_4k_num".equals(key)){
					sum_4k_num+=StringUtil.getLongValue(m,key);
				}else if("sum_probe_num".equals(key)){
					sum_probe_num+=StringUtil.getLongValue(m,key);
				}
			}
		}
		
		Map<String,String> countMap=new HashMap<String,String>();
		countMap.put("city_name","小计");
		countMap.put("hw_4k_num",StringUtil.getStringValue(hw_4k_num));
		countMap.put("hw_probe_num",StringUtil.getStringValue(hw_probe_num));
		countMap.put("cw_4k_num",StringUtil.getStringValue(cw_4k_num));
		countMap.put("cw_probe_num",StringUtil.getStringValue(cw_probe_num));
		countMap.put("zx_4k_num",StringUtil.getStringValue(zx_4k_num));
		countMap.put("zx_probe_num",StringUtil.getStringValue(zx_probe_num));
		countMap.put("fh_4k_num",StringUtil.getStringValue(fh_4k_num));
		countMap.put("fh_probe_num",StringUtil.getStringValue(fh_probe_num));
		countMap.put("ch_4k_num",StringUtil.getStringValue(ch_4k_num));
		countMap.put("ch_probe_num",StringUtil.getStringValue(ch_probe_num));
		countMap.put("bst_4k_num",StringUtil.getStringValue(bst_4k_num));
		countMap.put("bst_probe_num",StringUtil.getStringValue(bst_probe_num));
		countMap.put("js_4k_num",StringUtil.getStringValue(js_4k_num));
		countMap.put("js_probe_num",StringUtil.getStringValue(js_probe_num));
		countMap.put("other_4k_num",StringUtil.getStringValue(other_4k_num));
		countMap.put("other_probe_num",StringUtil.getStringValue(other_probe_num));
		countMap.put("sum_4k_num",StringUtil.getStringValue(sum_4k_num));
		countMap.put("sum_probe_num",StringUtil.getStringValue(sum_probe_num));
		
		resultList.add(countMap);
		logger.warn("stbN4KCount deal time:"+(System.currentTimeMillis()/1000 - start));
		return resultList;
	}
	
	/**
	 * 计算占比,保留两位小数
	 */
	private double percent(long molecular,long denominator)
	{
		BigDecimal bd1 = new BigDecimal(Double.toString(molecular * 100));
		BigDecimal bd2 = new BigDecimal(Double.toString(denominator));
		
		double percent=0d;
		if(molecular!=0 && denominator!=0)
		{
			percent = bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		return percent;
	}
	
	/**
	 * 获取一个月前的秒数
	 */
	private long getBefor1Mon()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        
    	Date str=new Date();
		try{
			str = format.parse(mon);
		}catch (ParseException e){
			logger.error("时间格式不对:"+mon);
		}
		
		return str.getTime()/1000;
	}
	
	/**
	 * 结果数据写入文件，返回时间戳
	 */
	public String listToString(List<Map<String, String>> data,String dataType) 
	{
		if(data==null || data.isEmpty()){
			return null;
		}
	
		StringBuffer sb=new StringBuffer();
		for(Map<String, String> map:data)
		{
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) 
			{
				String key = iterator.next();
				sb.append(key).append("#X#").append(map.get(key)).append("#Y#");
			}
			sb.append("#AB#");
		}
		
		logger.warn("filePath:"+RESULTFILE);
		long time=System.currentTimeMillis();

		FileWriter fw = null;
        try 
        {
            fw = new FileWriter(RESULTFILE,true);
            fw.write(dataType+"-"+time+":"+sb.substring(0,sb.toString().length()-4)+"\n");
            fw.close();
        } catch (IOException e) {
        	logger.error("结果数据存入文件[{}-{}]失败！err:"+e,RESULTFILE,time);
        	return null;
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
            	logger.error("IO流关闭失败,err:"+e);
            }
        }
		
		return StringUtil.getStringValue(time);
	}
	
	
	
	/**
	 * 将String转成List
	 */
	public List<Map<String, String>> stringToList(String time,String dataType) 
	{
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		if(StringUtil.IsEmpty(time)){
			return list;
		}
		String index=dataType+"-"+time+":";
		
		String dataString=null;
		BufferedReader br=null;
		try{
			File file=new File(RESULTFILE);
	         if(!file.exists()||file.isDirectory()){
	        	return null; 
	         }
	            
	         br=new BufferedReader(new FileReader(file));
	         String temp=null;
	         while((temp=br.readLine())!=null)
	         {
	        	 if(temp.indexOf(index)>-1){
	        		 dataString=temp.substring(index.length()).trim();
	        		 break;
	        	 }
	         }
		}catch(IOException e){
			logger.error("读取数据文件[{}-{}]失败，err:"+e,RESULTFILE,time);
			return list;
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(StringUtil.IsEmpty(dataString)){
			logger.warn("数据文件[{}-{}]无数据",RESULTFILE,time);
			return list;
		}
		
		String[] s1=dataString.split("#AB#");
		for(String sm:s1)
		{
			String[] sm1=sm.split("#Y#");
			Map<String,String> map=new HashMap<String,String>();
			for(String m:sm1){
				map.put(m.split("#X#")[0], m.split("#X#")[1]);
			}
			
			list.add(map);
		}
		
		return list;
	}
	
	
	
	public StbDeviceCountDAO getDao() {
		return dao;
	}

	public void setDao(StbDeviceCountDAO dao) {
		this.dao = dao;
	}



}
