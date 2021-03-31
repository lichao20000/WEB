package com.linkage.module.gwms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.TyGateAgreeCountReportDAO;

/**
 * 江西电信天翼网关版本一致率
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2019-4-15
 */
public class TyGateAgreeCountReportBIO {
	Logger logger = LoggerFactory.getLogger(TyGateAgreeCountReportBIO.class);
	// 持久层
	TyGateAgreeCountReportDAO dao;
	
	
	/**
	 * 获取数据
	 * @param
	 * @param cityId
	 * @param startTime1
	 * @param endTime1
	 * @return
	 */
	public List<Map<String,String>> getData(String cityId,String startTime1, String endTime1,String gwType,String isExcludeUpgrade,
			String recent_start_Time1,String recent_end_Time1){
		
		List<Map<String,String>> rsList = new ArrayList<Map<String,String>>();
		
		if(null==cityId || "".equals(cityId)){
			logger.error("TyGateAgreeCountReportBIO=>getData()传入的cityId为空!");
			return rsList;
		}
		
		int allTyCount = dao.getTyCount(cityId, startTime1, endTime1,null,gwType,isExcludeUpgrade,recent_start_Time1, recent_end_Time1);
		int notTyCount = dao.getTyCount(cityId, startTime1, endTime1,"0",gwType,isExcludeUpgrade,recent_start_Time1, recent_end_Time1);
		double rate = (double) 0.00;
		if(0!=allTyCount){
			rate = (double)(allTyCount-notTyCount)/allTyCount;
		}
		
		Map<String,String> firstMap = new HashMap<String,String>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		firstMap.put("city_id", cityId);
		firstMap.put("city_name", "合计");
		List<String> childCityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		if(childCityList.size()>0){
			firstMap.put("hasChild", "true");
		}else{
			firstMap.put("hasChild", "false");
		}
		firstMap.put("notTyCount", String.valueOf(notTyCount));
		firstMap.put("allTyCount", String.valueOf(allTyCount));
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(rate);
		firstMap.put("rate",str);
		
		for(int i=0;i<childCityList.size();i++){
			String tempCityId = childCityList.get(i);
			allTyCount = dao.getTyCount(tempCityId, startTime1, endTime1,null,gwType,isExcludeUpgrade,recent_start_Time1, recent_end_Time1);
			notTyCount = dao.getTyCount(tempCityId, startTime1, endTime1,"0",gwType,isExcludeUpgrade,recent_start_Time1, recent_end_Time1);
			rate = (double) 0.00;
			if(0!=allTyCount){
				rate = (double)(allTyCount-notTyCount)/allTyCount;
			}
			Map<String,String> childMap = new HashMap<String,String>();
			childMap.put("city_id", tempCityId);
			childMap.put("city_name", cityMap.get(tempCityId));
			List<String> list = CityDAO.getNextCityIdsByCityPidCore(tempCityId);
			if(list.size()>0){
				childMap.put("hasChild", "true");
			}else{
				childMap.put("hasChild", "false");
			}
			list = null;
			childMap.put("notTyCount", String.valueOf(notTyCount));
			childMap.put("allTyCount", String.valueOf(allTyCount));
			childMap.put("rate",nf.format(rate));
			rsList.add(childMap);
		}
		rsList.add(firstMap);
		cityMap = null;
		childCityList = null;
		return rsList;
	}
	
	/**
	 * 获取指定地市，根据厂商型号版本进行划分
	 * @param
	 * @param cityId
	 * @param startTime1
	 * @param endTime1
	 * @param is_highversion
	 * @return
	 */
	public List<Map<String,String>> getTyDetailCountList(String cityId,String startTime1,String endTime1, String is_highversion
			,String gwType,String isExcludeUpgrade,String recent_start_Time1, String recent_end_Time1) {
		logger.debug("getTyDetailCountList=>getData(cityId:{},startTime:{},startTime:{})",cityId,startTime1,endTime1);
		List<Map<String,String>> rsList = new ArrayList<Map<String,String>>();
		
		if(null==cityId || "".equals(cityId)){
			logger.error("TyGateAgreeCountReportBIO=>getData()传入的cityId为空!");
			return rsList;
		}
		rsList = dao.getTyDetailCountList(cityId,startTime1,endTime1,is_highversion,gwType,isExcludeUpgrade,recent_start_Time1, recent_end_Time1);
		return rsList;
	}

	
	
	
	public List<Map<String,String>>  queryDataListJXDX(String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1){
		List<Map<String,String>>  list = dao.queryDataListJXDX(gw_type, vendorId, deviceModelId, isExcludeUpgrade, 
				startTime1, endTime1, recent_start_Time1, recent_end_Time1);
		return list;
	}
	
	public List<Map<String,String>>  querydetailListJXDX(String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1,String is_highversion,String hardwareversion){
		List<Map<String,String>>  list = dao.querydetailListJXDX(gw_type, vendorId, deviceModelId, isExcludeUpgrade, 
				startTime1, endTime1, recent_start_Time1, recent_end_Time1,is_highversion,hardwareversion);
		return list;
	}
	
	public List<Map<String,String>>  queryDataListSDDX(String querydata,String gw_type,String vendorId,String deviceModelId,String isExcludeUpgrade,
			String startTime1,String endTime1,String recent_start_Time1,String recent_end_Time1){
		List<Map<String,String>>  list = dao.queryDataListSDDX(querydata,gw_type, vendorId, deviceModelId, isExcludeUpgrade, 
				startTime1, endTime1, recent_start_Time1, recent_end_Time1);
		return list;
	}
	
	public TyGateAgreeCountReportDAO getDao() {
		return dao;
	}

	public void setDao(TyGateAgreeCountReportDAO dao) {
		this.dao = dao;
	}
	
}
