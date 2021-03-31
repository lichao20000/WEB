package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.MothTerminalOrderDAO;

public class MothTerminalOrderBIO {

	private static Logger logger = LoggerFactory
			.getLogger(MothTerminalOrderBIO.class);
	private MothTerminalOrderDAO mothTerminalOrderDAO;

	public List<Map> countMothTerminalOrder(String starttime, String endtime,
			String bindDate, String city_id, String isMonth) {
		String bindTime = this.getBindTime(bindDate);
		List<Map> list = new ArrayList<Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO
				.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		// 当月开户总数
		Map<String, String> khMap = mothTerminalOrderDAO.countMothTotalOrder(
				starttime, endtime, city_id);
		// 当月语音业务工单数
		Map<String, String> yyMap = mothTerminalOrderDAO.countYyTotalOrder(
				starttime, endtime, city_id);
		// 当月开户工单绑定终端数
		Map<String, String> khBdMap = mothTerminalOrderDAO
				.countBdTerminalOrder(starttime, endtime, bindTime, city_id,
						isMonth);
		// 当月语音业务工单绑定终端数
		Map<String, String> yyBdMap = mothTerminalOrderDAO
				.countYyBdTerminalOrder(starttime, endtime, bindTime, city_id,
						isMonth);
		// 当月未绑定开户工单绑定终端数
		Map<String, String> khWBdMap = mothTerminalOrderDAO
				.countWYyBdTerminalOrder(starttime, endtime, city_id);
		// 开户总数
		long khNum = 0;
		// 语音业务工单数
		long yyNum = 0;
		// 开户工单绑定终端数
		long khBdNum = 0;
		// 语音业务工单绑定终端数
		long yyBdNum = 0;
		// 未绑定开户工单绑定终端数
		long khWBdNum = 0;
		String percentage;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++) {
			Map<String, Object> tmap = new HashMap<String, Object>();
			khNum = 0;
			yyNum = 0;
			khBdNum = 0;
			yyBdNum = 0;
			khWBdNum = 0;
			percentage = "N/A";
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++) {
				if (null != khMap && !khMap.isEmpty()) {
					khNum += StringUtil.getLongValue(khMap.get(tlist.get(j)));
				}
				if (null != yyMap && !yyMap.isEmpty()) {
					yyNum += StringUtil.getLongValue(yyMap.get(tlist.get(j)));
				}
				if (null != khBdMap && !khBdMap.isEmpty()) {
					khBdNum += StringUtil
							.getLongValue(khBdMap.get(tlist.get(j)));
				}
				if (null != yyBdMap && !yyBdMap.isEmpty()) {
					yyBdNum += StringUtil
							.getLongValue(yyBdMap.get(tlist.get(j)));
				}
				if (null != khWBdMap && !khWBdMap.isEmpty()) {
					khWBdNum += StringUtil.getLongValue(khWBdMap.get(tlist
							.get(j)));
				}
			}
			tmap.put("khNum", khNum);
			tmap.put("yyNum", yyNum);
			tmap.put("khBdNum", khBdNum);
			tmap.put("yyBdNum", yyBdNum);
			tmap.put("khWBdNum", khWBdNum);
			percentage = getDecimal(StringUtil.getStringValue(khBdNum),
					StringUtil.getStringValue(khNum));
			tmap.put("percentage", percentage);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	public List<Map> getMothTerminalOrderExcel(String starttime, String flag,
			String endtime, String city_id) {
		logger.debug("getMothTerminalOrderExcel");
		return mothTerminalOrderDAO.getDeviceListForTerminalExcel(city_id,
				flag, starttime, endtime);
	}

	public List<Map> getDeviceListForWBdTerminal(String city_id,
			String starttime, String endtime, String flag,
			int curPage_splitPage, int num_splitPage) {
		return mothTerminalOrderDAO.getDeviceListForWBdTerminal(city_id,
				starttime, endtime, flag, curPage_splitPage, num_splitPage);
	}

	public int getDeviceListForWBdTerminalCount(String city_id,
			String starttime, String endtime, String flag,
			int curPage_splitPage, int num_splitPage) {
		return mothTerminalOrderDAO.getDeviceListForWBdTerminalCount(city_id,
				starttime, endtime, flag, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDeviceListForTerminalExcel(String city_id, String flag,
			String starttime, String endtime) {
		return mothTerminalOrderDAO.getDeviceListForTerminalExcel(city_id,
				flag, starttime, endtime);
	}

	public MothTerminalOrderDAO getMothTerminalOrderDAO() {
		return mothTerminalOrderDAO;
	}

	public void setMothTerminalOrderDAO(
			MothTerminalOrderDAO mothTerminalOrderDAO) {
		this.mothTerminalOrderDAO = mothTerminalOrderDAO;
	}

	private String getDecimal(String total, String ttotal) {
		if (null == total || "0".equals(total) || null == ttotal
				|| "0".equals(ttotal)) {
			return "N/A";
		}
		float t1 = Float.parseFloat(total);
		float t2 = Float.parseFloat(ttotal);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}

	private String getBindTime(String bindDate) {
		String time;
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (bindDate == null || "".equals(bindDate)) {
			time = null;
		} else {
			dt = new DateTimeUtil(bindDate);
			time = String.valueOf(dt.getLongTime());
		}

		return time;
	}
}
