/**
 * 
 */
package com.linkage.module.gwms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.bio.interf.I_DeviceStatusReportBIO;
import com.linkage.module.gwms.report.dao.interf.I_DeviceStatusReportDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-8-4
 * @category com.linkage.module.gwms.report.bio
 * 
 */
public class DeviceStatusReportBIO implements I_DeviceStatusReportBIO {
	
	private static Logger log = LoggerFactory.getLogger(DeviceStatusReportBIO.class);
	/**
	 * 注入Dao
	 */
	I_DeviceStatusReportDAO deviceStatusReportDAO;

	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return deviceStatusReportDAO.getChildCity(cityId);
	}

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String[][] getReportData(String cityId, long startDate, long endDate) {

		// 取出子一级属地
		List cityList = deviceStatusReportDAO.getChildCity(cityId);

		// 返回数据
		String[][] dataArr = new String[4][cityList.size()+1];
		
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < cityList.size() + 1; j++) {
				dataArr[i][j] = "0";
			}
		}

		// 初始化属地返回值
		for (int i = 0; i < cityList.size(); i++) {
			Map tempCityMap = (Map) cityList.get(i);
			dataArr[0][i+1] = String.valueOf(tempCityMap.get("city_name"))
					.toString();
		}

		//旮旯
		dataArr[0][0] = "状态\\属地";
		dataArr[1][0] = "已绑定";
		dataArr[2][0] = "未绑定";
		dataArr[3][0] = "设备总数";

		// 查出所有属地，以便求子属地
		Map<String, String> cityAllMap = getCityPC();

		for (int i = 0; i < cityList.size(); i++) {

			Map oneCityGetMap = (Map) cityList.get(i);
			String temp_cityId = String.valueOf(oneCityGetMap.get("city_id"))
					.toString();

			List<String> childCityList = null;
			
			int bindNum = 0;
			int noNindNum = 0;
			
			log.debug("temp_cityId:" + temp_cityId);

			if ("00".equals(temp_cityId)) {
				childCityList = new ArrayList<String>();
				childCityList.add(temp_cityId);
				bindNum = deviceStatusReportDAO.getDevNum(childCityList,
						startDate, endDate,1);
				noNindNum = deviceStatusReportDAO.getDevNum(childCityList,
						startDate, endDate,0);

			} else {

				childCityList = getAllChild(cityAllMap, temp_cityId);
				childCityList.add(temp_cityId);
				bindNum = deviceStatusReportDAO.getDevNum(childCityList,
						startDate, endDate,1);
				noNindNum = deviceStatusReportDAO.getDevNum(childCityList,
						startDate, endDate,0);

			}
			
			dataArr[1][i+1] = String.valueOf(bindNum).toString();
			dataArr[2][i+1] = String.valueOf(noNindNum).toString();
			dataArr[3][i+1] = String.valueOf(bindNum+noNindNum).toString();
			
		}
		
		return dataArr;
	}

	/**
	 * 获取属地父子关系
	 * 
	 * @return
	 */
	public Map<String, String> getCityPC() {

		Map<String, String> cityPCMap = new HashMap<String, String>();
		List cityList = deviceStatusReportDAO.getAllCity();
		for (int i = 0; i < cityList.size(); i++) {
			Map one = (Map) cityList.get(i);
			cityPCMap.put(String.valueOf(one.get("city_id")).toString(), String
					.valueOf(one.get("parent_id")).toString());
		}

		return cityPCMap;

	}

	/**
	 * @category 查询子节点，不包含自己
	 * 
	 * @param cityMap
	 * @param cityId
	 * @return
	 */
	public List<String> getAllChild(Map<String, String> cityMap, String cityId) {

		List<String> childList = new ArrayList<String>();

		Set<String> citySet = cityMap.keySet();
		for (String strCityId : citySet) {
			String strPid = cityMap.get(strCityId);
			if (cityId.equals(strPid)) {
				childList.add(strCityId);
			}
		}

		if (childList.size() > 1) {
			for (int i = 0; i < childList.size(); i++) {
				childList.addAll(getAllChild(cityMap, childList.get(i)));
			}
		}

		return childList;
	}

	/**
	 * @return the deviceStatusReportDAO
	 */
	public I_DeviceStatusReportDAO getDeviceStatusReportDAO() {
		return deviceStatusReportDAO;
	}

	/**
	 * @param deviceStatusReportDAO the deviceStatusReportDAO to set
	 */
	public void setDeviceStatusReportDAO(
			I_DeviceStatusReportDAO deviceStatusReportDAO) {
		this.deviceStatusReportDAO = deviceStatusReportDAO;
	}
	
}
