package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.TianyiStoreBusinessDAO;

public class TianyiStoreBusinessBIO {

	private TianyiStoreBusinessDAO dao;

	public Map<String, String> getUserType() {
		return dao.getUserType();
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> countTianyiStoreBusiness(String starttime,
			String endtime, String city_id, String userType) {
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map<String, String> niMap = dao.countTianyiStoreBusiness(
				starttime, endtime, city_id, userType);
		long ninum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			ninum = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++) {
				if (null != niMap && !niMap.isEmpty()) {
					ninum += StringUtil.getLongValue(niMap.get(tlist.get(j)));
				}
			}
			tmap.put("ninum", ninum);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime,String userType, int curPage_splitPage,
			int num_splitPage) {
		return dao.getDeviceListForWBdTerminal(city_id, starttime, endtime,userType,
				curPage_splitPage, num_splitPage);
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime,String userType, int curPage_splitPage,
			int num_splitPage) {
		return dao.getDeviceListForWBdTerminalCount(city_id, starttime,
				endtime,userType, curPage_splitPage, num_splitPage);
	}

	public TianyiStoreBusinessDAO getDao() {
		return dao;
	}

	public void setDao(TianyiStoreBusinessDAO dao) {
		this.dao = dao;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDevExcel(String city_id, String starttime,
			String endtime,String userType) {
		return dao.getDevExcel(city_id, starttime, endtime,userType);
	}

}
