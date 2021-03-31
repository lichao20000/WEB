
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.dao.BindLogCountDAO;

public class BindLogCountBIO {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(BindLogCountBIO.class);
	
	BindLogCountDAO bindLogCountDAO;
	
	public List<Map<String, String>> getBindCountMap(long bindStartTime, long bindEndTime,String cityId) {
		
		logger.debug("BindLogCountBIO=>getBindCountMap({},{},{})",
				new Object[]{bindStartTime,bindEndTime,cityId});
		
		List<Map<String, String>> _rsList = new ArrayList<Map<String, String>>();
		
		Map<String,Integer> data = new HashMap<String,Integer>();
		Map<String,String> _rsdata = new HashMap<String,String>();
		_rsdata.put("city_id", cityId);
		_rsdata.put("city_name", CityDAO.getCityIdCityNameMap().get(cityId));
		List list = bindLogCountDAO.getBindCountList(bindStartTime, bindEndTime, cityId);
		
		int all_bind = 0;
		int all_bindless = 0;
		int all_modify = 0;
		for(int i=0;i<list.size();i++){
			Map _map = (Map)list.get(i);
			String _cityId = String.valueOf(_map.get("city_id"));
			String _operType = String.valueOf(_map.get("oper_type"));
			int num = Integer.parseInt(String.valueOf(_map.get("num")));
			if("1".equals(_operType)){
				all_bind = all_bind+num;
			}else if("2".equals(_operType)){
				all_bindless = all_bindless + num;
			}else {
				all_modify = all_modify + num;
			}
			data.put(_cityId+":"+_operType,num);
		}
		
		_rsdata.put("bind", String.valueOf(all_bind));
		_rsdata.put("bindless", String.valueOf(all_bindless));
		_rsdata.put("modify", String.valueOf(all_modify));
		_rsList.add(_rsdata);
		
		List<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		
		for(int i=0;i<cityList.size();i++){
			List<String> childCityList = CityDAO.getAllNextCityIdsByCityPid(cityList.get(i));
			int _temp_bind = 0;
			int _temp_bindless = 0;
			int _temp_modify = 0;
			for(int j=0;j<childCityList.size();j++){
				if(null!=data.get(childCityList.get(j)+":1")){
					_temp_bind += data.get(childCityList.get(j)+":1");
				}
				if(null!=data.get(childCityList.get(j)+":2")){
					_temp_bindless += data.get(childCityList.get(j)+":2");
				}
				if(null!=data.get(childCityList.get(j)+":3")){
					_temp_modify += data.get(childCityList.get(j)+":3");
				}
			}
			Map<String,String> rsdata_ = new HashMap<String,String>();
			rsdata_.put("city_id", cityList.get(i));
			rsdata_.put("city_name", CityDAO.getCityIdCityNameMap().get(cityList.get(i)));
			rsdata_.put("bind", String.valueOf(_temp_bind));
			rsdata_.put("bindless", String.valueOf(_temp_bindless));
			rsdata_.put("modify", String.valueOf(_temp_modify));
			_rsList.add(rsdata_);
		}
		
		return _rsList;
	}

	/**
	 * @return the bindLogCountDAO
	 */
	public BindLogCountDAO getBindLogCountDAO() {
		return bindLogCountDAO;
	}

	/**
	 * @param bindLogCountDAO the bindLogCountDAO to set
	 */
	public void setBindLogCountDAO(BindLogCountDAO bindLogCountDAO) {
		this.bindLogCountDAO = bindLogCountDAO;
	}
	
}
