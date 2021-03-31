package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.UserQueryDAO;

public class UserQueryBIO {
	private static Logger logger = LoggerFactory
			.getLogger(UserQueryBIO.class);

	private UserQueryDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> queryUserQuery(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, String querytype) {
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map<String, String> map = dao.queryUserQuery(city_id, start_time,
				end_time, deviceType, isActive, specName, moreinter, moreitv,
				morevoip, moretianyi, querytype);

		if ("4".equals(querytype)) {
			long num = 0;
			ArrayList<String> tlist = null;
			for (int i = 0; i < cityList.size(); i++) {
				Map<String, Object> tmap = new HashMap<String, Object>();
				num = 0;
				String cityId = cityList.get(i);
				tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				tmap.put("city_id", cityId);
				tmap.put("city_name", cityMap.get(cityId));
				for (int j = 0; j < tlist.size(); j++) {
					if (null != map && !map.isEmpty()) {
						String str = StringUtil.getStringValue(map.get(tlist
								.get(j)));
						if (null != str && !"".equals(str.trim())) {
							num = num + StringUtil.getLongValue(str);
						}
					}
				}
				tmap.put("deploy_total", StringUtil.getStringValue(num));
				list.add(tmap);
				tlist = null;
			}
			cityMap = null;
			cityList = null;
			tlist = null;
		} else {
			if (!StringUtil.IsEmpty(city_id) && !"-1".equals(city_id)
					&& !"00".equals(city_id)) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("city_id", city_id);
				tempMap.put("city_name", cityMap.get(city_id));
				tempMap.put("deploy_total",
						StringUtil.getStringValue(map.get(city_id)));
				list.add(tempMap);
			} else {
				long num = 0;
				ArrayList<String> tlist = null;
				for (int i = 0; i < cityList.size(); i++) {
					Map<String, Object> tmap = new HashMap<String, Object>();
					num = 0;
					String cityId = cityList.get(i);
					tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
					tmap.put("city_id", cityId);
					tmap.put("city_name", cityMap.get(cityId));
					for (int j = 0; j < tlist.size(); j++) {
						if (null != map && !map.isEmpty()) {
							String str = StringUtil.getStringValue(map
									.get(tlist.get(j)));
							if (null != str && !"".equals(str.trim())) {
								num = num + StringUtil.getLongValue(str);
							}
						}
					}
					tmap.put("deploy_total", StringUtil.getStringValue(num));
					list.add(tmap);
					tlist = null;
				}
				cityMap = null;
				cityList = null;
				tlist = null;
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> queryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO=>queryUserQueryList");
		return dao.queryUserQueryList(city_id, start_time, end_time,
				deviceType, isActive, specName, moreinter, moreitv, morevoip,
				moretianyi, curPage_splitPage, num_splitPage);
	}

	public int countQueryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi, int curPage_splitPage, int num_splitPage) {
		logger.debug("UserQueryDAO=>countQueryUserQueryList");
		return dao.countQueryUserQueryList(city_id, start_time, end_time,
				deviceType, isActive, specName, moreinter, moreitv, morevoip,
				moretianyi, curPage_splitPage, num_splitPage);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> excelQueryUserQueryList(String city_id, String start_time,
			String end_time, String deviceType, String isActive,
			String specName, String moreinter, String moreitv, String morevoip,
			String moretianyi) {
		logger.debug("UserQueryDAO=>excelQueryUserQueryList");
		return dao.excelQueryUserQueryList(city_id, start_time, end_time,
				deviceType, isActive, specName, moreinter, moreitv, morevoip,
				moretianyi);
	}

	public UserQueryDAO getDao() {
		return dao;
	}

	public void setDao(UserQueryDAO dao) {
		this.dao = dao;
	}
}
