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

import com.linkage.module.gwms.report.bio.interf.I_BssSheetReportBIO;
import com.linkage.module.gwms.report.dao.interf.I_BssSheetReportDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category bio.report
 * 
 */
@SuppressWarnings("unchecked")
public class BssSheetReportBIO implements I_BssSheetReportBIO {

	private static Logger log = LoggerFactory.getLogger(BssSheetReportBIO.class);
	
	/**
	 * 注入Dao
	 */
	I_BssSheetReportDAO bssSheetReportDao;

	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) {
		return bssSheetReportDao.getChildCity(cityId);
	}

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(String cityId, long startDate, long endDate) {

		List cityList = bssSheetReportDao.getChildCity(cityId);

		List<Map> dataReturn = new ArrayList<Map>();
		Map<String,String> cityAllMap = getCityPC();
		
		Map<String, String> oneMap = new HashMap<String, String>();
		int sum1_all = 0;
		int sum2_all = 0;
		int sum3_all = 0;
		int sum4_all = 0;
		int sum5_all = 0;
		int sum6_all = 0;
		int sum7_all = 0;
		int sum8_all = 0;
		int sum9_all = 0;
		int sum10_all = 0;
		double sum11_all = 0.0;

		for (int i = 0; i < cityList.size(); i++) {

			Map oneCityGetMap = (Map) cityList.get(i);
			String temp_cityId = String.valueOf(oneCityGetMap.get("city_id"))
					.toString();
			String temp_cityName = String.valueOf(
					oneCityGetMap.get("city_name")).toString();
			Map<String, String> oneDataReMap = new HashMap<String, String>();
			oneDataReMap.put("city_name", temp_cityName);
			oneDataReMap.put("city_id", temp_cityId);

			List<String> childCityList = null;
			List dataFrom = null;

			log.debug("temp_cityId:" + temp_cityId);

			if ("00".equals(temp_cityId)) {
				childCityList = new ArrayList<String>();
				childCityList.add(temp_cityId);
				dataFrom = bssSheetReportDao.getBssSheet(childCityList,
						startDate, endDate);
				
			} else {
				
				childCityList = getAllChild(cityAllMap,temp_cityId);
				childCityList.add(temp_cityId);
				dataFrom = bssSheetReportDao.getBssSheet(childCityList,
						startDate, endDate);
				
			}
			
			if (childCityList.size() > 1) {
				oneDataReMap.put("haschild", "true");
				childCityList.clear();
			}

			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			int sum4 = 0;
			int sum5 = 0;
			int sum6 = 0;
			int sum7 = 0;
			int sum8 = 0;
			int sum9 = 0;
			int sum10 = 0;
			double sum11 = 0.0;

			for (int j = 0; j < dataFrom.size(); j++) {

				Map oneDataFrom = (Map) dataFrom.get(j);
				String temp_datatype = String.valueOf(oneDataFrom.get("type"))
						.toString();
				String temp_datastate = String.valueOf(
						oneDataFrom.get("result_spec_state")).toString();

				if ("1".equals(temp_datatype)) {
					sum1 = sum1 + 1;
				}
				if ("2".equals(temp_datatype)) {
					sum2 = sum2 + 1;
				}
				if ("3".equals(temp_datatype)) {
					sum3 = sum3 + 1;
				}
				if ("4".equals(temp_datatype)) {
					sum4 = sum4 + 1;
				}
				if ("5".equals(temp_datatype)) {
					sum5 = sum5 + 1;
				}
				if ("6".equals(temp_datatype)) {
					sum6 = sum6 + 1;
				}
				if ("7".equals(temp_datatype)) {
					sum7 = sum7 + 1;
				}
				if ("8".equals(temp_datatype)) {
					sum8 = sum8 + 1;
				}
				if ("1".equals(temp_datastate)) {
					sum10 = sum10 + 1;
				}
			}

			sum9 = sum1 + sum2 + sum3 + sum4 + sum5 + sum6 + sum7 + sum8;

			/**
			 * 如果是省公司的话，统计小计
			 */
			if ("00".equals(cityId)) {

				sum1_all = sum1_all + sum1;
				sum2_all = sum2_all + sum2;
				sum3_all = sum3_all + sum3;
				sum4_all = sum4_all + sum4;
				sum5_all = sum5_all + sum5;
				sum6_all = sum6_all + sum6;
				sum7_all = sum7_all + sum7;
				sum8_all = sum8_all + sum8;
				sum9_all = sum9_all + sum9;
				sum10_all = sum10_all + sum10;

			}

			if (0 == sum9) {
				sum11 = 100.0;
			} else {
				sum11 = sum10 * 100 / sum9;
			}

			oneDataReMap.put("sum1", String.valueOf(sum1).toString());
			oneDataReMap.put("sum2", String.valueOf(sum2).toString());
			oneDataReMap.put("sum3", String.valueOf(sum3).toString());
			oneDataReMap.put("sum4", String.valueOf(sum4).toString());
			oneDataReMap.put("sum5", String.valueOf(sum5).toString());
			oneDataReMap.put("sum6", String.valueOf(sum6).toString());
			oneDataReMap.put("sum7", String.valueOf(sum7).toString());
			oneDataReMap.put("sum8", String.valueOf(sum8).toString());
			oneDataReMap.put("sum9", String.valueOf(sum9).toString());
			oneDataReMap.put("sum10", String.valueOf(sum10).toString());
			oneDataReMap.put("sum11", String.valueOf(sum11).toString());

			dataReturn.add(oneDataReMap);
		}

		/**
		 * 如果是省公司的话，统计小计
		 */
		if ("00".equals(cityId)) {

			if (0 == sum9_all) {
				sum11_all = 100.0;
			} else {
				sum11_all = sum10_all * 100 / sum9_all;
			}

			oneMap.put("city_name", "合计");
			oneMap.put("sum1", String.valueOf(sum1_all).toString());
			oneMap.put("sum2", String.valueOf(sum2_all).toString());
			oneMap.put("sum3", String.valueOf(sum3_all).toString());
			oneMap.put("sum4", String.valueOf(sum4_all).toString());
			oneMap.put("sum5", String.valueOf(sum5_all).toString());
			oneMap.put("sum6", String.valueOf(sum6_all).toString());
			oneMap.put("sum7", String.valueOf(sum7_all).toString());
			oneMap.put("sum8", String.valueOf(sum8_all).toString());
			oneMap.put("sum9", String.valueOf(sum9_all).toString());
			oneMap.put("sum10", String.valueOf(sum10_all).toString());
			oneMap.put("sum11", String.valueOf(sum11_all).toString());

			dataReturn.add(oneMap);
		}

		return dataReturn;
	}
	
	/**
	 * 取得表头
	 */
	public String[] getTbTitle(){
		
		String[] temp = {"属地","开户","暂停","销户","复机","更改速率","更改账号","更换设备","更改IP","统计工单总数","成功工单数","成功率(%)"};
		return temp;
	}
	
	/**
	 * 取得列名
	 */
	public String[] getTbName(){
		String[] temp = {"city_name","sum1","sum2","sum3","sum4","sum5","sum6","sum7","sum8","sum9","sum10","sum11"};
		return temp;
	}
	
	/**
	 * 获取属地父子关系
	 * 
	 * @return
	 */
	public Map<String,String> getCityPC() {

		Map<String, String> cityPCMap = new HashMap<String, String>();
		List cityList = bssSheetReportDao.getAllCity();
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
	public List<String> getAllChild(Map<String,String> cityMap,String cityId){
		
		List<String> childList = new ArrayList<String>();
		
		Set<String> citySet = cityMap.keySet();
		for (String strCityId : citySet) {
			String strPid = cityMap.get(strCityId);
			if (cityId.equals(strPid)) {
				childList.add(strCityId);
			}
		}

		if(childList.size()>1){
			for(int i=0;i<childList.size();i++){
				childList.addAll(getAllChild(cityMap,childList.get(i)));
			}
		}
		
		return childList;
	}

	/**
	 * @return the bssSheetReportDao
	 */
	public I_BssSheetReportDAO getBssSheetReportDao() {
		return bssSheetReportDao;
	}

	/**
	 * @param bssSheetReportDao
	 *            the bssSheetReportDao to set
	 */
	public void setBssSheetReportDao(I_BssSheetReportDAO bssSheetReportDao) {
		this.bssSheetReportDao = bssSheetReportDao;
	}

}
