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

import com.linkage.module.gwms.report.bio.interf.I_ServPackageDeviceBIO;
import com.linkage.module.gwms.report.dao.interf.I_ServPackageDeviceDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category bio.report
 * 
 */
@SuppressWarnings("unchecked")
public class ServPackageDeviceBIO implements I_ServPackageDeviceBIO {

	private Logger log = LoggerFactory.getLogger(ServPackageDeviceBIO.class);
	
	/**
	 * 注入Dao
	 */
	I_ServPackageDeviceDAO servPackageDeviceDAO;

	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return servPackageDeviceDAO.getChildCity(cityId);
	}

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String[][] getReportData(String cityId, long startDate, long endDate,String bookparam) {

		log.debug("getReportData()");
		
		// 取出子一级属地
		List cityList = servPackageDeviceDAO.getChildCity(cityId);
		
		//取出所有套餐
		List servPackageList = new ArrayList();
		List list = servPackageDeviceDAO.getGwServPackage();
		Map codeMap = new HashMap();
		for(int i=0;i<list.size();i++){
			Map one = (Map)list.get(i);
			codeMap.put(String.valueOf(one.get("serv_package_id")), one.get("serv_package_name"));
		}
		if(null!=bookparam && !"".equals(bookparam)){
			String tempParam[] = bookparam.split("\\$"); 
			servPackageList = new ArrayList();
			for(int i=0;i<tempParam.length;i++){
				Map one = new HashMap();
				one.put("serv_package_id", tempParam[i]);
				one.put("serv_package_name", codeMap.get(tempParam[i]));
				servPackageList.add(one);
			}
		}else{
			servPackageList = list;
		}

		// 返回数据
		String[][] dataArr = new String[servPackageList.size() + 2][cityList
				.size()+1];
		// 求合计数组
		int sum[] = new int[cityList.size()];
		
		// 初始化值
		for (int i = 0; i < cityList.size(); i++) {
			sum[i] = 0;
		}
		for (int i = 1; i < servPackageList.size() + 2; i++) {
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

		// 初始化套餐返回值
		for (int i = 0; i < servPackageList.size(); i++) {
			Map tempServMap = (Map) servPackageList.get(i);
			dataArr[i+1][0] = String
					.valueOf(tempServMap.get("serv_package_name")).toString();
		}
		//旮旯
		dataArr[0][0] = "套餐\\属地";
		// 最后一项【合计】
		dataArr[servPackageList.size()+1][0] = "合计";

		// 查出所有属地，以便求子属地
		Map<String, String> cityAllMap = getCityPC();

		for (int i = 0; i < cityList.size(); i++) {

			Map oneCityGetMap = (Map) cityList.get(i);
			String temp_cityId = String.valueOf(oneCityGetMap.get("city_id"))
					.toString();

			List<String> childCityList = null;
			List dataFrom = null;

			if ("00".equals(temp_cityId)) {
				childCityList = new ArrayList<String>();
				childCityList.add(temp_cityId);
				dataFrom = servPackageDeviceDAO.getDevNum(childCityList,
						startDate, endDate);

			} else {

				childCityList = getAllChild(cityAllMap, temp_cityId);
				childCityList.add(temp_cityId);
				dataFrom = servPackageDeviceDAO.getDevNum(childCityList,
						startDate, endDate);

			}

			for (int j = 0; j < dataFrom.size(); j++) {

				Map oneDataFrom = (Map) dataFrom.get(j);

				String temp_serv_package_name = String.valueOf(
						oneDataFrom.get("serv_package_name")).toString();
				String temp_num = String.valueOf(oneDataFrom.get("num"))
						.toString();

				for (int k = 0; k < servPackageList.size(); k++) {
					if(temp_serv_package_name.equals(dataArr[k][0])){
						dataArr[k][i+1] = temp_num;
						sum[i] = sum[i] + Integer.parseInt(temp_num);
					}
				}
			}
		}
		
		/**
		 * 将合计数据合并
		 */
		for (int i = 0; i < cityList.size(); i++) {
			dataArr[servPackageList.size()+1][i+1] = String.valueOf(sum[i]).toString() ;
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
		List cityList = servPackageDeviceDAO.getAllCity();
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
	 * 获取套餐
	 * @return
	 */
	public List getGwServPackage(){
		return servPackageDeviceDAO.getGwServPackage();
	}
	
	/**
	 * @return the servPackageDeviceDAO
	 */
	public I_ServPackageDeviceDAO getServPackageDeviceDAO() {
		return servPackageDeviceDAO;
	}

	/**
	 * @param servPackageDeviceDAO
	 *            the servPackageDeviceDAO to set
	 */
	public void setServPackageDeviceDAO(
			I_ServPackageDeviceDAO servPackageDeviceDAO) {
		this.servPackageDeviceDAO = servPackageDeviceDAO;
	}

}
