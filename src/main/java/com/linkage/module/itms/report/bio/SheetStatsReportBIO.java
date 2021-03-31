/**
 * 
 */
package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.SheetStatsReportDAO;

/**
 * @author chenjie
 * @date 2011-8-26
 * 工单统计报表
 */
public class SheetStatsReportBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetStatsReportBIO.class);
	
	private SheetStatsReportDAO dao;
	
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
	public List<Map<String, String>> getStatsReport(String deviceType, String cityId, String servTypeId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getAllStats({},{},{},{},{})", new Object[]{deviceType, cityId, servTypeId, startOpenDate, endOpenDate});
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		// 所有地市的map，根据cityID获取cityName
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 根据当前cityId获取下一级的cityId
		List<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		// 自地市id排序
		Collections.sort(cityList);
		// 保证当前地市在首位展示
		cityList.add(0, cityId);
		
		// 所有地市数据map
		allStatsMap = getStatsByCityId(deviceType, cityId, servTypeId, startOpenDate, endOpenDate);
		
		// 组装最后表格数据
		for(String city : cityList)
		{
			// 原始数据
			int succNum = 0;
			int failNum = 0;
			int notNum = 0;
			
			// 页面展示的一条
			Map<String, String> oneData = new HashMap<String, String>();
			oneData.put("cityId", city);
			oneData.put("cityName", (String)cityMap.get(city));
			
			// 每个地市需要累积计算其子地市的数据，作为最后这个地市的最终数据
			List<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city);
			for(int i=0; i<tlist.size(); i++)
			{
				// 之前已经查询好的数据map中取出数据
				Map<String,Integer> dataMap =  allStatsMap.get(tlist.get(i));
				succNum += dataMap.get("succNum");
				failNum += dataMap.get("failNum");
				notNum += dataMap.get("notNum");
			}
			
			/*
			Map<String,Integer> dataMap =  allStatsMap.get(city);
			
			succNum = dataMap.get("succNum");
			failNum = dataMap.get("failNum");
			notNum = dataMap.get("notNum");
			*/
			
			oneData.put("succNum", String.valueOf(succNum));
			oneData.put("failNum", String.valueOf(failNum));
			oneData.put("notNum", String.valueOf(notNum));
			oneData.put("percent", countPercent(succNum, failNum, notNum));
			
			result.add(oneData);
		}
		
		return result;
	}
	
	/**
	 * 根据CITY和其他条件统计出相对地市的所有数据(已做/未作/失败)
	 * @param deviceType
	 * @param cityId
	 * @param servTypeId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return Map<String,Map<String,Integer>> 
	 */
	public Map<String,Map<String,Integer>>  getStatsByCityId(String deviceType, String cityId, String servTypeId, String startOpenDate, String endOpenDate)
	{
		logger.debug("getStatsByCityId({},{},{},{},{})", new Object[]{deviceType, cityId, servTypeId, startOpenDate, endOpenDate});
		return dao.getStatsMapByCityId(deviceType, cityId, servTypeId, startOpenDate, endOpenDate);
	}
	
	/**
	 * 计算百分比
	 * @param succNum
	 * @param failNum
	 * @param notNum
	 * @return
	 */
	public String countPercent(int succNum, int failNum, int notNum)
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

	public SheetStatsReportDAO getDao() {
		return dao;
	}

	public void setDao(SheetStatsReportDAO dao) {
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
