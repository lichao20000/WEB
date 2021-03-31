/**
 * 
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.GwDeviceVenCityQueryDAO;
import com.linkage.module.gwms.Global;
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
public class GwDeviceVenCityQueryBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(GwDeviceVenCityQueryBIO.class);
	
	private GwDeviceVenCityQueryDAO dao;
	
	/**
	 * 地市-数据map
	 * <00(city_id), <succNum=1, failNum=2, notNum=3>>
	 */
	private Map<String,Map<String,Integer>> allStatsMap = null;
	
	private int maxPage_splitPage;

	/**
	 * 根据设备统计
	 * @param titleList 
	 * @param devicetypeId
	 * @param deviceModelId
	 * @param vendorId
	 * @param cityId
	 * @param startOpenDate
	 * @param endOpenDate
	 * @return
	 */
	public List<Map<String, String>> getStatsReport(List<String> cityShow,List<String> titleList, String devicetypeId,String deviceModelId,String vendorId,String cityId, String startOpenDate, String endOpenDate)
	{
		logger.warn("bio.getStatsReport({}{}{}{}{}{}{})", new Object[]{cityId, devicetypeId, deviceModelId,vendorId,cityId,startOpenDate, endOpenDate});
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		//总数、在线设备数、开通业务数
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		list = dao.getAllStatsGroupByCity(devicetypeId,deviceModelId,vendorId,cityId, startOpenDate, endOpenDate);
		ArrayList<String> cityArray = new ArrayList<String>();
		if("00".equals(cityId)){
			List<Map<String, String>> cityMap = CityDAO.getNextCityListByCityPidCore(cityId);
			for(Map<String,String> map:cityMap){
				cityArray.add(map.get("city_id"));
			}
			cityArray.add("00");
		}
		else{
			cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
		
		
		for(int j=0;j<cityArray.size();j++){
			String curCity = cityArray.get(j);
			titleList.add(Global.G_CityId_CityName_Map.get(curCity));
			cityShow.add(curCity);
		}
		
		String temp_model_id = "";
		String temp_version = "";
		
		for(int i=0;i<list.size();i++){
			
			Map<String,String> oneList = list.get(i);
			String vendor_id = StringUtil.getStringValue(oneList, "vendor_name", "");
			String device_model_id = StringUtil.getStringValue(oneList, "device_model", "");
			String softwareversion = StringUtil.getStringValue(oneList, "softwareversion", "");
			
			if(temp_model_id.equals(device_model_id)&&temp_version.equals(softwareversion)){
				continue;
			}
			else{
				Map<String,String> oneRes = new HashMap<String,String>();
				oneRes.put("vendor", vendor_id);
				oneRes.put("model", device_model_id);
				oneRes.put("version", softwareversion);
				temp_model_id = device_model_id;
				temp_version = device_model_id;
				for(int j=0;j<cityArray.size();j++){
					String curCity = cityArray.get(j);
					oneRes.put(curCity, "0");
				}
				result.add(oneRes);
			}
		}
		
		
		for(int i=0;i<list.size();i++){
			Map<String,String> oneList = list.get(i);
			String vendor_id = StringUtil.getStringValue(oneList, "vendor_name", "");
			String device_model_id = StringUtil.getStringValue(oneList, "device_model", "");
			String softwareversion = StringUtil.getStringValue(oneList, "softwareversion", "");
			String city_id = StringUtil.getStringValue(oneList, "city_id", "");
			
			for(int j=0;j<result.size();j++){
				Map<String,String> oneRes = result.get(j);
				String vendor = StringUtil.getStringValue(oneRes, "vendor", "");
				String model = StringUtil.getStringValue(oneRes, "model", "");
				String version = StringUtil.getStringValue(oneRes, "version", "");
				if(vendor_id.equals(vendor)&&device_model_id.equals(model)&&softwareversion.equals(version)){
					oneRes.put(city_id, StringUtil.getStringValue(oneList, "num", ""));
					break;
				}
			}
		}
		
		
		for(int i=0;i<result.size();i++){
			int sum = 0;
			Map<String,String> oneRes = result.get(i);
			
			for(int j=0;j<cityArray.size();j++){
				String curCity = cityArray.get(j);
				sum += StringUtil.getIntValue(oneRes, curCity, 0);
			}
			oneRes.put("sum", StringUtil.getStringValue(sum));
		}
		Map<String,String> oneRes = new HashMap<String,String>();
		int sum = 0;
		for(int j=0;j<cityArray.size();j++)
		{
			int num = 0;
			String curCity = cityArray.get(j);
			
			for(int i=0;i<result.size();i++){
				Map<String,String> oneRes1 = new HashMap<String,String>();
				oneRes1 = result.get(i);
				num += StringUtil.getIntValue(oneRes1, curCity, 0);
			}
			sum = sum+num;
			oneRes.put(cityArray.get(j), String.valueOf(num));
		}
		oneRes.put("sum", String.valueOf(sum));
		result.add(oneRes);
		//NXDX-NX-BUG-20191011-LiuX-001（机顶盒数量查询统计-同一厂家型号版本统计出多行数据）
		if (Global.NXDX.equals(Global.instAreaShortName))
		{
			List<Map<String, String>> filterResult = new ArrayList<Map<String, String>>();
			for (Map<String, String> dataMap : result)
			{
				String vendor = StringUtil.getStringValue(dataMap, "vendor");
				String model = StringUtil.getStringValue(dataMap, "model");
				String version = StringUtil.getStringValue(dataMap, "version");
				String sum1 = StringUtil.getStringValue(dataMap, "sum");
				if (StringUtil.IsEmpty(vendor) && StringUtil.IsEmpty(model)
						&& StringUtil.IsEmpty(version))
				{
					// 如果vendor（厂商）为空，则为小计数据不进行过滤
					filterResult.add(dataMap);
				}
				else
				{
					if (!"0".equals(sum1))
					{
						filterResult.add(dataMap);
					}
				}
			}
			return filterResult;
		}
		return result;
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
	/*public List<Map> getDetailReport(String cityId, String openStatus, String deviceType, String servTypeId, String startOpenDate, String endOpenDate,
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
	}*/
	
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

	public GwDeviceVenCityQueryDAO getDao() {
		return dao;
	}

	public void setDao(GwDeviceVenCityQueryDAO dao) {
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
