package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.ZeroConfigFailReasonDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigFailReasonBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigFailReasonBIO.class);
	
	private ZeroConfigFailReasonDAO dao;
	
	/**
	 * 零配置失败原因统计
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	public Map<String,Map> queryZeroConfigFailReason(String starttime,String endtime,String cityId){
		
		logger.debug("ZeroConfigFailReasonBIO => queryZeroConfigFailReason({},{},{})",new Object[]{starttime,endtime,cityId});
		Map<String,Map> cityFailMap = new LinkedHashMap<String, Map>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		
		// 查询当前所有的失败情况
		List<Map> failDescList  = dao.queryZeroConfiglFailDesc();
		
		// 查询所有的失败原因
		List<Map> failReasonList = dao.queryZeroConfigFailReason(starttime,endtime);
		
		// 根据属地cityId获取下一层属地ID(包含自己)
		List<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		
		for(Map failDescMap : failDescList){
			Map tempMap = new HashMap();
			String failDesc = StringUtil.getStringValue(failDescMap.get("reason_desc"));
			String reasonId = StringUtil.getStringValue(failDescMap.get("reason_id"));
			
			int resonCount = 0;
			for(String childCityId : cityList){
				int cityCount = 0;
				String childCityName = StringUtil.getStringValue(cityMap.get(childCityId));
								
				// 查询城市的子城市不包括自己
				ArrayList<String> subList = CityDAO.getNextCityIdsByCityPidCore(childCityId);
				for(Map failReasonMap : failReasonList){
					String failCityId = StringUtil.getStringValue(failReasonMap.get("city_id"));
					String failReasonId = StringUtil.getStringValue(failReasonMap.get("fail_reason_id"));
					if(reasonId.equals(failReasonId)){
						if("00".equals(childCityId)){
							if(childCityId.equals(failCityId)){
								cityCount += StringUtil.getIntegerValue(failReasonMap.get("num"));
							}
						}
						else{
							if(childCityId.equals(failCityId) || subList.contains(failCityId)){
								cityCount += StringUtil.getIntegerValue(failReasonMap.get("num"));
							}
						}	
					}					
				}
				resonCount += cityCount;
				tempMap.put(childCityName, cityCount);
			}
			tempMap.put("小计", resonCount);					
			cityFailMap.put(reasonId + "##" +failDesc, tempMap);				
		}
		return cityFailMap;
	}
	
	/**
	 * 获取cityid的所有下一级子属地的name，包括自己
	 * @param cityId
	 * @return
	 */
	public List<String> getAllCityName(String cityId){
		logger.debug("ZeroConfigFailReasonBIO => getAllCityName()",cityId);
		List<String> cityNameList = new ArrayList<String>();
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
				
		List<String> cityIdList = CityDAO.getNextCityIdsByCityPid(cityId);
		for(String childCityId : cityIdList){
			cityNameList.add(StringUtil.getStringValue(cityMap.get(childCityId)));
		}
		cityNameList.add("小计");
		return cityNameList;
	}
	
	/**
	 * 组合零配置失败原因
	 * @param cityNameList
	 * @param cityFailMap
	 * @return
	 */
	public List<List<String>> getZeroFailData(List<String> cityNameList,Map<String,Map> cityFailMap,String cityId){
		logger.debug("ZeroConfigFailReasonBIO => getZeroFailData()");
		List<List<String>> faileCountList = new ArrayList<List<String>>();
		
		// 城市名称
		List<String> titleList = new ArrayList<String>();	
		titleList.add("绑定方式");
		titleList.add("");
		titleList.add("失败原因");
		for(String cityName : cityNameList){
			titleList.add(cityName);
		}
		faileCountList.add(titleList);
		
		// 城市id
		List<String> cityIdList = CityDAO.getNextCityIdsByCityPid(cityId);
		List<String> cityIdHiddenList = new ArrayList<String>();	
		cityIdHiddenList.add("");
		cityIdHiddenList.add("");
		cityIdHiddenList.add("");
		for(String childCityId : cityIdList){
			cityIdHiddenList.add(childCityId);
		}
		faileCountList.add(cityIdHiddenList);
			
		// 计算城市小计
		List<String> cityCountList = new ArrayList<String>();
		cityCountList.add("");
		cityCountList.add("failId");
		cityCountList.add("小计");
		
		// 用于区分绑定方式
		int index = 0;
		for(Map.Entry<String, Map> entry : cityFailMap.entrySet()) {		
			List<String> dataList = new ArrayList<String>();
			
			if(index == 0){
				dataList.add("");
			}else if(index < 3){
				dataList.add("ITMS MAC绑定");
			}else if(index < 5){
				dataList.add("AAA IP绑定");
			}else if(index == 5){
				dataList.add("宽带自助安装");
			}else if(index == 6){
				dataList.add("用户账号已绑定");
			}else if(index == 7){
				dataList.add("其他绑定");
			}else if(index == 8){
				dataList.add("更换机顶盒");
			}
			
			index++;
			dataList.add(entry.getKey().split("##")[0]);
			dataList.add(entry.getKey().split("##")[1]);
			int cityIndex = 3;
			for(String cityName : cityNameList){
				dataList.add(StringUtil.getStringValue(entry.getValue().get(cityName)));
				if(cityCountList.size() == cityIndex){
					cityCountList.add(StringUtil.getStringValue(entry.getValue().get(cityName)));
				}
				else{
					cityCountList.set(cityIndex, StringUtil.getStringValue(StringUtil.getIntegerValue(cityCountList.get(cityIndex)) 
							+StringUtil.getIntegerValue(entry.getValue().get(cityName))));
				}
				cityIndex++;
			}			
			faileCountList.add(dataList);
		}
		faileCountList.add(cityCountList);
		
		return faileCountList;
	}
	
	/**
	 * 根据reasonDesc和cityId查询设备信息
	 * 
	 * @param reasonDesc
	 * @param cityName
	 * @return
	 */
	public List<Map> queryZeroConfigFailReasonDetail(String reasonId,String cityName,String starttime,String endtime,int curPageSplitPage,int numSplitPage){
		logger.debug("ZeroConfigFailReasonBIO => queryZeroConfigFailReasonDetail()");
		String allCityId = getCurrAllCityId(cityName);
		List<Map> zeroConfigFailDeviceList = dao.queryZeroConfigFailDevice(allCityId,reasonId,starttime,endtime,curPageSplitPage,numSplitPage);
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		if(zeroConfigFailDeviceList != null){
			for(Map map : zeroConfigFailDeviceList){
				map.put("city_name", cityMap.get(map.get("city_id")));
			}
		}
		return zeroConfigFailDeviceList;
	}
	
	public int queryZeroConfigFailReasonDetailCount(String reasonId,String cityId,String starttime,String endtime){
		String allCityId = getCurrAllCityId(cityId);
		return dao.queryZeroConfigFailDeviceCount(allCityId, reasonId, starttime, endtime);
	}
	
	public List<Map> zeroConfigFaileDeviceExcel(String reasonId, String cityId,String starttime,String endtime){
		logger.debug("ZeroConfigFailReasonBIO => zeroConfigFaileDeviceExcel()");
		String allCityId = getCurrAllCityId(cityId);
		List<Map> zeroConfigFailDeviceList = dao.zeroConfigFaileDeviceExcel(allCityId,reasonId,starttime,endtime);
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		if(zeroConfigFailDeviceList != null){
			for(Map map : zeroConfigFailDeviceList){
				map.put("city_name", cityMap.get(map.get("city_id")));
			}
		}
		return zeroConfigFailDeviceList;
	}

	private String getCurrAllCityId(String cityId){		
		List<String> cityList = new ArrayList<String>();
		if("00".equals(cityId)){
			cityList.add(cityId);
		}
		else{
			cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		}
		
		String allCityId = "";
		if(cityList != null){
			for(int index=0;index<cityList.size();index++){
				if(index == 0){
					allCityId += "(" + "'" + cityList.get(index) + "'";
				}
				
				if(index == cityList.size()-1 && index!=0){
					allCityId += "," +  "'" + cityList.get(index) + "'" +  ")";
				}
				
				if(index == cityList.size()-1 && index==0){
					allCityId += ")";
				}
				
				if(index !=0 && index != cityList.size()-1){
					allCityId += "," +  "'" + cityList.get(index) + "'";
				}
			}
		}
		else{
			allCityId = "('')";
		}
		return allCityId;
	}
	
	public ZeroConfigFailReasonDAO getDao() {
		return dao;
	}

	public void setDao(ZeroConfigFailReasonDAO dao) {
		this.dao = dao;
	}
}

