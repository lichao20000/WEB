/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.bio.interf.I_ZeroConfigStatisticalBIO;
import com.linkage.module.gwms.report.dao.interf.I_ZeroConfigStatisticalDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-6
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class ZeroConfigStatisticalBIO implements I_ZeroConfigStatisticalBIO{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ZeroConfigStatisticalBIO.class);
	
	I_ZeroConfigStatisticalDAO zeroConfigStatisticalDAO = null;
	
	/**
	 * 查询所有子属地
	 * 
	 * @param city_id
	 * @return
	 */
	public List getChildCityList(String cityId){
		
		logger.debug("getChildCityList(cityId:{})",cityId);
		List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
		
		Map<String,String> cityIdMap = new HashMap<String,String>();
		cityIdMap = CityDAO.getCityIdPidMap();
		Map<String, String> allCityMap = CityDAO.getCityIdCityNameMap();
		//遍历key集合，获取value
		Set entrys = cityIdMap.entrySet();
		
		//自己加上去
		Map<String, String> cityMeMap = new HashMap<String, String>();
		cityMeMap.put("city_id", cityId);
		cityMeMap.put("city_name", allCityMap.get(cityId));
		cityMeMap.put("hasCityId",hasChidCity(cityId)?"false":"true");
		cityList.add(cityMeMap);
		
		Iterator it2 = entrys.iterator();
		while(it2.hasNext()) {
			Map.Entry entry = (Map.Entry)it2.next();
			String key = String.valueOf(entry.getKey()).toString();
			if(!cityId.equals(cityIdMap.get(key))){
				continue;
			}
			Map<String, String> cityMap = new HashMap<String, String>();
			cityMap.put("city_id", key);
			cityMap.put("city_name", allCityMap.get(key));
			cityMap.put("hasCityId", hasChidCity(key)?"true":"false");
			cityList.add(cityMap);
		}
		cityIdMap = null;
		allCityMap = null;
		return cityList;
		
	}
	
	/**
	 * 查询数据
	 * 
	 * @param cityList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[][] getData(List cityList,String startTime,String endTime){
		
		logger.debug("getData(List cityList,String startTime,String endTime)");
		if(null==cityList){
			return null;
		}
		String[][] zeroDataArr = new String[3][cityList.size()+1];
		zeroDataArr[0][0] = "成功数量";
		zeroDataArr[1][0] = "失败数量";
		zeroDataArr[2][0] = "成功百分比(%)";
		
		for(int i=0;i<cityList.size();i++){
			Map one = (Map)cityList.get(i);
			List<String> cityTempList;
			if("00".equals(String.valueOf(one.get("city_id")).toString())){
				cityTempList = new ArrayList<String>();
				cityTempList.add(String.valueOf(one.get("city_id")));
			}else{
				cityTempList = CityDAO.getAllNextCityIdsByCityPid(String.valueOf(one.get("city_id")).toString());
			}
			
			int num1 = zeroConfigStatisticalDAO.getData(cityTempList,startTime,endTime,"1");
			int num2 = zeroConfigStatisticalDAO.getData(cityTempList,startTime,endTime,"0");
			zeroDataArr[0][i+1] = String.valueOf(num1).toString();
			zeroDataArr[1][i+1] = String.valueOf(num2).toString();
			if(0==num1+num2){
				zeroDataArr[2][i+1] = "0";
			}else{
				zeroDataArr[2][i+1] = String.valueOf(num1*100/(num1+num2)).toString();
			}
			cityTempList = null;
		}
		
		return zeroDataArr;
	}
	
	/**
	 * 查询成功数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBindData(String cityId,String startTime,String endTime){
		
		logger.debug("getBindData(String cityId,String startTime,String endTime)");
		List<String> cityList = new ArrayList<String>();
		if("00".equals(cityId)){
			cityList.add(cityId);
		}else{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		List rsget = zeroConfigStatisticalDAO.getBindData(cityList,startTime,endTime);
		List rsset = new ArrayList();
		for(int i=0;i<rsget.size();i++){
			Map one = (Map) rsget.get(i);
			one.put("binddate",sdf.format(Long.parseLong(String.valueOf(one.get("binddate")))*1000));
			rsset.add(one);
		}
		cityList = null;
		return rsset;
	}
	
	/**
	 * 查询失败数据
	 * 
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getNoBindData(String cityId,String startTime,String endTime){
		
		logger.debug("getNoBindData(String cityId,String startTime,String endTime)");
		List<String> cityList = new ArrayList<String>();
		if("00".equals(cityId)){
			cityList.add(cityId);
		}else{
			cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
		
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		List rsget = zeroConfigStatisticalDAO.getNoBindData(cityList,startTime,endTime);
		List rsset = new ArrayList();
		for(int i=0;i<rsget.size();i++){
			Map one = (Map) rsget.get(i);
			one.put("binddate",sdf.format(Long.parseLong(String.valueOf(one.get("binddate")))*1000));
			rsset.add(one);
		}
		cityList = null;
		return rsset;
	}
	
	/**
	 * 查找该属地是否存在子属地
	 * 
	 * @param cityId
	 * @return
	 */
	public boolean hasChidCity(String cityId){
		Map<String,String> cityIdMap = new HashMap<String,String>();
		cityIdMap = CityDAO.getCityIdPidMap();
		
		//遍历key集合，获取value
		Set entrys = cityIdMap.entrySet();
		Iterator it2 = entrys.iterator();
		while(it2.hasNext()) {
			Map.Entry entry = (Map.Entry)it2.next();
			String key = String.valueOf(entry.getKey()).toString();
			if(cityId.equals(cityIdMap.get(key))){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the zeroConfigStatisticalDAO
	 */
	public I_ZeroConfigStatisticalDAO getZeroConfigStatisticalDAO() {
		return zeroConfigStatisticalDAO;
	}

	/**
	 * @param zeroConfigStatisticalDAO the zeroConfigStatisticalDAO to set
	 */
	public void setZeroConfigStatisticalDAO(
			I_ZeroConfigStatisticalDAO zeroConfigStatisticalDAO) {
		this.zeroConfigStatisticalDAO = zeroConfigStatisticalDAO;
	}
	
}
