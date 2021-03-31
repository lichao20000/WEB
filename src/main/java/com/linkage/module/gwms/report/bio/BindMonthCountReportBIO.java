/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.BindMonthCountReportDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-11-13
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class BindMonthCountReportBIO {
	Logger logger = LoggerFactory.getLogger(BindMonthCountReportBIO.class);
	
	/**
	 * 持久层
	 */
	BindMonthCountReportDAO bmcDAO = null;
	
	/**
	 * 获取数据
	 * 
	 * @param cityId 不能为空
	 * @param endDataInt 不能为空
	 * @return
	 */
	public List<Map<String,String>> getData(String cityId,long endDataInt){
		logger.debug("BindMonthCountReportBIO=>getData(cityId:{},endDataInt:{})",cityId,endDataInt);
		
		List<Map<String,String>> rsList = new ArrayList<Map<String,String>>();
		
		if(null==cityId || "".equals(cityId)){
			logger.error("BindMonthCountReportBIO=>getData()传入的cityId为空!");
			return rsList;
		}
		
		long userTime = endDataInt;
		long deviceTime = endDataInt;
		
		if(LipossGlobals.isXJDX()){
			deviceTime = endDataInt + 8*24*3600;
		}
		
		int userCount = bmcDAO.getUserCount(cityId, userTime);
		int deviceCount = bmcDAO.getDeviceCount(cityId, userTime,deviceTime);
		double rate = (double) 0.00;
		if(0!=userCount){
			rate = (double)deviceCount/userCount;
		}
		
		Map<String,String> firstMap = new HashMap<String,String>();
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		firstMap.put("city_id", cityId);
		firstMap.put("city_name", cityMap.get(cityId));
		List<String> childCityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		if(childCityList.size()>0){
			firstMap.put("hasChild", "true");
		}else{
			firstMap.put("hasChild", "false");
		}
		firstMap.put("deviceCount", String.valueOf(deviceCount));
		firstMap.put("userCount", String.valueOf(userCount));
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(rate);
		firstMap.put("rate",str);
		rsList.add(firstMap);
		
		for(int i=0;i<childCityList.size();i++){
			String tempCityId = childCityList.get(i);
			userCount = bmcDAO.getUserCount(tempCityId, userTime);
			deviceCount = bmcDAO.getDeviceCount(tempCityId, userTime,deviceTime);
			rate = (double) 0.00;
			if(0!=userCount){
				rate = (double)deviceCount/userCount;
			}
			Map<String,String> childMap = new HashMap<String,String>();
			childMap.put("city_id", tempCityId);
			childMap.put("city_name", cityMap.get(tempCityId));
			List list = CityDAO.getNextCityIdsByCityPidCore(tempCityId);
			if(list.size()>0){
				childMap.put("hasChild", "true");
			}else{
				childMap.put("hasChild", "false");
			}
			list = null;
			childMap.put("deviceCount", String.valueOf(deviceCount));
			childMap.put("userCount", String.valueOf(userCount));
			childMap.put("rate",nf.format(rate));
			rsList.add(childMap);
		}
		cityMap = null;
		childCityList = null;
		return rsList;
	}

	/**
	 * @return the bmcDAO
	 */
	public BindMonthCountReportDAO getBmcDAO() {
		return bmcDAO;
	}

	/**
	 * @param bmcDAO the bmcDAO to set
	 */
	public void setBmcDAO(BindMonthCountReportDAO bmcDAO) {
		this.bmcDAO = bmcDAO;
	}
	
}
