/**
 * 
 */
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.report.dao.EVDOCountReportDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-11
 * @category bio.report
 * 
 */
public class EVDOCountReportBIO {

	private static Logger logger = LoggerFactory.getLogger(EVDOCountReportBIO.class);
	
	/**
	 * 注入Dao
	 */
	EVDOCountReportDAO evdoDAO;

	/**
	 * @查询报表数据
	 * 
	 * @param city_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List getReportData(String cityId, long startDate, long endDate) {
		
		List cityList = CityDAO.getNextCityListByCityPid(cityId);
		
		List<Map> dataReturn = new ArrayList<Map>();
		
		for (int i = 0; i < cityList.size(); i++) {

			Map oneCityGetMap = (Map) cityList.get(i);
			
			String temp_cityId = String.valueOf(oneCityGetMap.get("city_id")).toString();
			String temp_cityName = String.valueOf(oneCityGetMap.get("city_name")).toString();
			
			Map<String, String> oneDataReMap = new HashMap<String, String>();
			oneDataReMap.put("city_name", temp_cityName);
			oneDataReMap.put("city_id", temp_cityId);
			
			logger.debug("temp_cityId:" + temp_cityId);
			
			List dataFrom = evdoDAO.getEVDOCount(temp_cityId, startDate, endDate);
			int device_count = evdoDAO.getDeviceCount(temp_cityId, startDate, endDate);
			List list = CityDAO.getAllNextCityIdsByCityPid(temp_cityId);
			if(list.size()>1){
				oneDataReMap.put("haschild", "true");
			}
			list = null;
			int standby_count = 0;
			int main_count = 0;
			int evdo_count = 0;
			double standby_evdo = 0.0;
			double main_evdo = 0.0;
			double evdo_device = 0.0;

			for (int j = 0; j < dataFrom.size(); j++) {

				Map oneDataFrom = (Map) dataFrom.get(j);
				String card_bind_stat = String.valueOf(oneDataFrom.get("card_bind_stat")).toString();
				String work_mode = String.valueOf(oneDataFrom.get("work_mode")).toString();
				
				if ("1".equals(card_bind_stat)) {
					evdo_count = evdo_count + 1;
				}
				if ("Main".equals(work_mode)) {
					main_count = main_count + 1;
				}
				if ("Standby".equals(work_mode)) {
					standby_count = standby_count + 1;
				}
			}
			
			if(0==evdo_count){
				standby_evdo = 0.0;
				main_evdo = 0.0;
				evdo_device = 0.0;
			}else{
				standby_evdo = standby_count*100/evdo_count;
				main_evdo = main_count*100/evdo_count;
				evdo_device = evdo_count*100/device_count;
			}
				
			oneDataReMap.put("device_count", String.valueOf(device_count).toString());
			oneDataReMap.put("evdo_count", String.valueOf(evdo_count).toString());
			oneDataReMap.put("standby_count", String.valueOf(standby_count).toString());
			oneDataReMap.put("main_count", String.valueOf(main_count).toString());
			oneDataReMap.put("standby_evdo", String.valueOf(standby_evdo).toString());
			oneDataReMap.put("main_evdo", String.valueOf(main_evdo).toString());
			oneDataReMap.put("evdo_device", String.valueOf(evdo_device).toString());

			dataReturn.add(oneDataReMap);
		}

		cityList = null;
		return dataReturn;
	}
	
	/**
	 * 取得表头
	 */
	public String[] getTbTitle(){
		
		String[] temp = {"属地","设备总数","EVDO总数","备用链路总数","主链路总数","备用链路总数/EVDO总数","主链路总数/EVDO总数","EVDO总数/设备总数"};
		return temp;
	}
	
	/**
	 * 取得列名
	 */
	public String[] getTbName(){
		String[] temp = {"city_name","device_count","evdo_count","standby_count","main_count","standby_evdo","main_evdo","evdo_device"};
		return temp;
	}

	/**
	 * @return the evdoDAO
	 */
	public EVDOCountReportDAO getEvdoDAO() {
		return evdoDAO;
	}

	/**
	 * @param evdoDAO the evdoDAO to set
	 */
	public void setEvdoDAO(EVDOCountReportDAO evdoDAO) {
		this.evdoDAO = evdoDAO;
	}
	
}
