package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.StatisticalLordMGCAddressDAO;

public class StatisticalLordMGCAddressBIO {

	private StatisticalLordMGCAddressDAO dao;

	@SuppressWarnings({ "rawtypes" })
	public String getCityName(String city_id) {
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		StringBuffer head = new StringBuffer();
		head.append("<tr><th >序号</th>");
		head.append("<th >MGC地址</th>");
		for (int i = 0; i < cityList.size(); i++) {
			head.append("<th>").append(cityMap.get(cityList.get(i)))
					.append("</th>");
		}
		head.append("</tr>");
		return head.toString();
	}

	public String countMoreBroadbandBusiness(String city_id) {
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		StringBuffer sb = new StringBuffer();
		Map<String, Map<String, String>> resultMap = dao
				.countMoreBroadbandBusiness(city_id);
		if (resultMap.isEmpty()) {
			int row = cityList.size() + 2;
			sb.append("<tr><td colspan=" + row + ">该属地不存在任何MGC</td></tr>");
			return sb.toString();
		}
		int count = 1;
		Iterator<Map.Entry<String, Map<String, String>>> iterator = resultMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Map<String, String>> entry = iterator.next();
			String key = entry.getKey();
			// if (!StringUtil.IsEmpty(key)) {
			sb.append("<tr><td>" + (count++) + "").append("</td>");
			sb.append("<td>" + (key) + "").append("</td>");
			Map<String, String> myCityMap = entry.getValue();
			ArrayList<String> tlist = null;
			for (int j = 0; j < cityList.size(); j++) {
				String cityId = cityList.get(j);
				tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
				int num = 0;
				for (int k = 0; k < tlist.size(); k++) {
					if (null != myCityMap.get(tlist.get(k))) {
						num += Long.valueOf(myCityMap.get(tlist.get(k)));
					}
				}
				if (0 != num) {
					sb.append(
							"<td><a href=\"javascript:openDevForWbdTerminal('"
									+ cityId + "','" + key + "');\">")
							.append(num).append("</a></td>");
				} else {
					sb.append(
							"<td><a href=\"javascript:openDevForWbdTerminal('"
									+ cityId + "','" + key + "');\">")
							.append("0").append("</a></td>");
				}
			}
			// } else {
			// continue;
			// }
		}
		sb.append("<tr><td colspan='2'>").append("小计").append("</td>");
		Map<String, String> niMap = dao.xiaoji(city_id);
		long ninum = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++) {
			ninum = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			for (int j = 0; j < tlist.size(); j++) {
				if (null != niMap && !niMap.isEmpty()) {
					ninum += StringUtil.getLongValue(niMap.get(tlist.get(j)));
				}
			}
			if (0 != ninum) {
				sb.append("<td>").append(ninum).append("</td>");
			} else {
				sb.append("<td>").append("0</td>");
			}
			tlist = null;
		}
		sb.append("</tr>");
		cityList = null;
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String prox_serv, int curPage_splitPage, int num_splitPage) {
		return dao.getDeviceListForWBdTerminal(city_id, prox_serv,
				curPage_splitPage, num_splitPage);
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String prox_serv, int curPage_splitPage, int num_splitPage) {
		return dao.getDeviceListForWBdTerminalCount(city_id, prox_serv,
				curPage_splitPage, num_splitPage);
	}

	// @SuppressWarnings("rawtypes")
	// public List<Map> getDevExcel(String city_id, String prox_serv) {
	// return dao.getDevExcel(city_id, prox_serv);
	// }

	public StatisticalLordMGCAddressDAO getDao() {
		return dao;
	}

	public void setDao(StatisticalLordMGCAddressDAO dao) {
		this.dao = dao;
	}

}
