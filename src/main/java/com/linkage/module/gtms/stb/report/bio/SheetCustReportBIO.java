/**
 * 
 */
package com.linkage.module.gtms.stb.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.report.dao.SheetCustReportDAO;
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
public class SheetCustReportBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetCustReportBIO.class);
	
	private SheetCustReportDAO dao;
	
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
	 * @return [<cityId=00,cityName=nanjing, succNum=0, failNum=0, notNum=0> , ...]
	 */
	public List<Map<String, String>> getStatsReport(String cityId, String servTypeId, String startOpenDate, String endOpenDate)
	{
		logger.warn("getAllStats({},{},{},{})", new Object[]{ cityId, servTypeId, startOpenDate, endOpenDate});
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if("1".equals(servTypeId)){
			list = dao.getStats(cityId,startOpenDate, endOpenDate);
			int succ = 0;
			int falut = 0;
			int undo = 0;
			for(Map<String,String> one:list){
				logger.warn("user_status:"+StringUtil.getIntegerValue(StringUtil.getIntegerValue(one.get("user_status"))));
				logger.warn("num:"+StringUtil.getIntegerValue(StringUtil.getIntegerValue(one.get("num"))));
				if(0==StringUtil.getIntegerValue(one.get("user_status"))){
					undo = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(-1==StringUtil.getIntegerValue(one.get("user_status"))){
					falut = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(1==StringUtil.getIntegerValue(one.get("user_status"))){
					succ = StringUtil.getIntegerValue(one.get("num"));
				}
			}
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("name","机顶盒");
			resOne.put("succ",StringUtil.getStringValue(succ));
			resOne.put("fault",StringUtil.getStringValue(falut));
			resOne.put("undo",StringUtil.getStringValue(undo));
			result.add(resOne);
		}
		else if("2".equals(servTypeId)){
			list = dao.getStatsGroupByCity(cityId, startOpenDate, endOpenDate);
			// 根据当前cityId获取下一级的cityId
			List<String> cityList = CityDAO.getNextCityIdsByCityPidCore("00");
			// 所有地市的map，根据cityID获取cityName
			Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
			// 自地市id排序
			Collections.sort(cityList);
			// 保证当前地市在首位展示
			cityList.add(0, cityId);
			
			for(String city:cityList){
				int succ = 0;
				int falut = 0;
				int undo = 0;
				for(Map<String,String> one:list){
					if(city.equals(one.get("city_id")) && 0==StringUtil.getIntegerValue(one.get("user_status"))){
						undo = StringUtil.getIntegerValue(one.get("num"));
					}
					else if(city.equals(one.get("city_id")) && -1==StringUtil.getIntegerValue(one.get("user_status"))){
						falut = StringUtil.getIntegerValue(one.get("num"));
					}
					else if(city.equals(one.get("city_id")) && 1==StringUtil.getIntegerValue(one.get("user_status"))){
						succ = StringUtil.getIntegerValue(one.get("num"));
					}
				}
				Map<String,String> resOne = new HashMap<String,String>();
				resOne.put("name",cityMap.get(city));
				resOne.put("succ",StringUtil.getStringValue(succ));
				resOne.put("fault",StringUtil.getStringValue(falut));
				resOne.put("undo",StringUtil.getStringValue(undo));
				result.add(resOne);
			}
		}
		else if("3".equals(servTypeId)){
			list = dao.getStats(cityId,startOpenDate, endOpenDate);
			int succ = 0;
			int falut = 0;
			int undo = 0;
			for(Map<String,String> one:list){
				if(1==StringUtil.getIntegerValue(one.get("user_status"))){
					succ = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(-1==StringUtil.getIntegerValue(one.get("user_status"))){
					falut = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(0==StringUtil.getIntegerValue(one.get("user_status"))){
					undo = StringUtil.getIntegerValue(one.get("num"));
				}
			}
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("name","其他");
			resOne.put("succ",StringUtil.getStringValue(succ));
			resOne.put("fault",StringUtil.getStringValue(falut));
			resOne.put("undo",StringUtil.getStringValue(undo));
			
			Map<String,String> resOne1 = new HashMap<String,String>();
			resOne1.put("name","桥接");
			resOne1.put("succ","0");
			resOne1.put("fault","0");
			resOne1.put("undo","0");
			
			Map<String,String> resOne2 = new HashMap<String,String>();
			resOne2.put("name","路由");
			resOne2.put("succ","0");
			resOne2.put("fault","0");
			resOne2.put("undo","0");
			
			result.add(resOne1);
			result.add(resOne2);
			result.add(resOne);
		}
		else if("4".equals(servTypeId)){
			list = dao.getStats(cityId,startOpenDate, endOpenDate);
			int succ = 0;
			int falut = 0;
			int undo = 0;
			for(Map<String,String> one:list){
				if(0==StringUtil.getIntegerValue(one.get("user_status"))){
					undo = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(-1==StringUtil.getIntegerValue(one.get("user_status"))){
					falut = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(1==StringUtil.getIntegerValue(one.get("user_status"))){
					succ = StringUtil.getIntegerValue(one.get("num"));
				}
			}
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("name","iptv");
			resOne.put("succ",StringUtil.getStringValue(succ));
			resOne.put("fault",StringUtil.getStringValue(falut));
			resOne.put("undo",StringUtil.getStringValue(undo));
			result.add(resOne);
		}
		else if("5".equals(servTypeId)){
			list = dao.getStats(cityId,startOpenDate, endOpenDate);
			int succ = 0;
			int falut = 0;
			int undo = 0;
			for(Map<String,String> one:list){
				if(0==StringUtil.getIntegerValue(one.get("user_status"))){
					undo = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(-1==StringUtil.getIntegerValue(one.get("user_status"))){
					falut = StringUtil.getIntegerValue(one.get("num"));
				}
				else if(1==StringUtil.getIntegerValue(one.get("user_status"))){
					succ = StringUtil.getIntegerValue(one.get("num"));
				}
			}
			Map<String,String> resOne = new HashMap<String,String>();
			resOne.put("name","IPTV用户");
			resOne.put("succ",StringUtil.getStringValue(succ));
			resOne.put("fault",StringUtil.getStringValue(falut));
			resOne.put("undo",StringUtil.getStringValue(undo));
			result.add(resOne);
		}
		
		logger.warn("before user_status...........=");
		print(result);
		
		for(Map<String,String> resOne : result){
			int succ = StringUtil.getIntegerValue(resOne.get("succ"));
			int fault = StringUtil.getIntegerValue(resOne.get("fault"));
			int undo = StringUtil.getIntegerValue(resOne.get("undo"));
			resOne.put("percent", countPercent(succ,fault));
			resOne.put("all", StringUtil.getStringValue(succ+fault+undo));
		}
		
		logger.warn("after user_status............=");
		print(result);
		return result;
	}
	
	private void print(List<Map<String,String>> s){
		try{
			for(Map<String,String> one:s){
		    	for (String ss : one.keySet()) {
		    		logger.warn("key:" + ss);
		    		logger.warn("values:" + one.get(ss).toString());
		    	}
		    }
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 计算百分比
	 * @param succNum
	 * @param failNum
	 * @param notNum
	 * @return
	 */
	public String countPercent(int succNum, int failNum)
	{
		logger.debug("countPercent({},{})", new Object[]{succNum, failNum});
		if(succNum == 0)
		{
			return "0.0%";
		}
		double s = Double.parseDouble(String.valueOf(succNum));
		double f = Double.parseDouble(String.valueOf(failNum));
		
		double percent = s / (s + f);
		DecimalFormat   fmt   =   new   DecimalFormat( "#%");
		fmt.setMinimumFractionDigits(1);
		fmt.setMaximumFractionDigits(1);
		return fmt.format(percent);
	}

	/**
	 * 计算百分比
	 * @param succNum
	 * @param failNum
	 * @param notNum
	 * @return
	 */
	public String countPercentCust(int succNum, int failNum, int notNum)
	{
		logger.debug("countPercent({},{},{})", new Object[]{succNum, failNum, notNum});
		if(succNum == 0)
		{
			return "0.0%";
		}
		double s = Double.parseDouble(String.valueOf(succNum));
		double f = Double.parseDouble(String.valueOf(failNum));
		double n = Double.parseDouble(String.valueOf(notNum));
		
		double percent = s / (s + f + n);
		DecimalFormat   fmt   =   new   DecimalFormat( "#%");
		fmt.setMinimumFractionDigits(1);
		fmt.setMaximumFractionDigits(1);
		return fmt.format(percent);
	}

	/**
	 * 查看详细
	 * @param cityId
	 * @param openStatus
	 * @param deviceType
	 * @param servTypeId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @param  curPage_splitPage
	 * @param  num_splitPage
	 * @return
	 */
	public List<Map> getDetailReport(String cityId, String openStatus, String deviceType, String servTypeId, String startOpenDate, String endOpenDate,
			int curPage_splitPage,
			int num_splitPage) {
		
		logger.debug("getDetailReport({},{},{},{},{},{})", new Object[]{cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate});
		maxPage_splitPage = dao.countDetailReport(cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate, curPage_splitPage, num_splitPage);
		return dao.getDetailReport(cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate, curPage_splitPage, num_splitPage);
	}
	
	// 不分页
	public List<Map> getDetailReport(String cityId, String openStatus, String deviceType, String servTypeId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getDetailReport({},{},{},{},{},{})", new Object[]{cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate});
		return dao.getDetailReport(cityId, openStatus, deviceType, servTypeId, startOpenDate, endOpenDate);
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

	public SheetCustReportDAO getDao() {
		return dao;
	}

	public void setDao(SheetCustReportDAO dao) {
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
