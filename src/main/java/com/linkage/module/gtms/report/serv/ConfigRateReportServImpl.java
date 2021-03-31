package com.linkage.module.gtms.report.serv;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.report.dao.ConfigRateReportDao;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ConfigRateReportServImpl implements ConfigRateReportServ {
	private ConfigRateReportDao configRateReportDao;

	public ConfigRateReportDao getConfigRateReportDao() {
		return configRateReportDao;
	}

	public void setConfigRateReportDao(ConfigRateReportDao configRateReportDao) {
		this.configRateReportDao = configRateReportDao;
	}

	public List<Map> countSuccessRate(String endtime1,
			String cityId) {
		List<Map> countlist = configRateReportDao.countSuccessRate(
				endtime1, cityId);
		
		List<Map> list = new ArrayList<Map>();
		Map<String, Map> newMap = new HashMap<String, Map>();
		for (Map map : countlist) {
			newMap.put(String.valueOf(map.get("cityId")), map);
		}

		// 首先列出要查询cityId的下一级地市
		List<String> cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		int count = 1;

		for (String inId : cityIds) {
			int sum = 0;
			int succ = 0;
			String rate = "";
			String total = "";
			String succNum = "";
			List<String> nextList = CityDAO.getAllNextCityIdsByCityPid(inId);

			Map<String, Object> tmp = new HashMap<String, Object>();

			if (count == cityIds.size()) {
				tmp.put("isAll", "1");
			} else {
				tmp.put("isAll", "0");
			}
			tmp.put("city_id", inId);
			tmp.put("city_name", cityMap.get(inId));

			for (String nextCity : nextList) {
				if (null != newMap.get(nextCity)) {
					total = String.valueOf(newMap.get(nextCity).get("total"));
					succNum = String.valueOf(newMap.get(nextCity).get("succ"));
					sum = sum + Integer.valueOf(total);
					succ = succ + Integer.valueOf(succNum);
				}
			}
			rate = percent(succ, sum);
			tmp.put("total", sum);
			tmp.put("succ", succ);
			tmp.put("rate", rate);
			list.add(tmp);
			count++;
		}
		return list;
	}

	public String percent(long p1, long p2) {
		double p3;
		if (p1 == 0 && p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

}
