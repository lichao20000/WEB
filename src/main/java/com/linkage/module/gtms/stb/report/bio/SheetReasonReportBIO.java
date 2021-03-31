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
import com.linkage.module.gtms.stb.report.dao.SheetReasonReportDAO;
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
public class SheetReasonReportBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SheetReasonReportBIO.class);
	
	private SheetReasonReportDAO dao;
	
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
	public List<Map<String, String>> getStatsReport(String cityId, String startOpenDate, String endOpenDate)
	{
		logger.warn("getAllStats({},{},{},{})", new Object[]{ cityId, startOpenDate, endOpenDate});
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		
		Map<String, String> codeMap = new HashMap<String,String>();
		codeMap.put("0","操作成功");
		codeMap.put("03","LOID不能为空");
		codeMap.put("02","work_asgn_id为空");
		codeMap.put("25","无对应的用户信息");
		codeMap.put("32","Order_Remark为空");
		codeMap.put("33","受理编号为空");
		codeMap.put("34","受理日期为空");
		codeMap.put("45","业务账号或业务密码为空");
		codeMap.put("47","设备ID为空");
		codeMap.put("48","STBID为空或非32位");
		codeMap.put("49","设备接入类型为空");
		codeMap.put("50","上门方式为空");
		
		
		list = dao.getStatsGroupbyCode(cityId, startOpenDate, endOpenDate);
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
			
			for (String key : codeMap.keySet()) {
				int num = 0;
				for(Map<String,String> one:list){
					if(city.equals(StringUtil.getStringValue(one.get("city_id"))) && key.equals(StringUtil.getStringValue(one.get("error_code")))){
						num = StringUtil.getIntegerValue(one.get("num"));
						break;
					}
				}
				//页面的key不能使用纯数字
				resOne.put("r"+key,StringUtil.getStringValue(num));
			}
			result.add(resOne);
		}
		
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

	public SheetReasonReportDAO getDao() {
		return dao;
	}

	public void setDao(SheetReasonReportDAO dao) {
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
