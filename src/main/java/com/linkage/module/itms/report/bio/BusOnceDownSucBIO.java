package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.BusOnceDownSucDAO;

public class BusOnceDownSucBIO {
	private static Logger logger = LoggerFactory
			.getLogger(BusOnceDownSucBIO.class);
	private BusOnceDownSucDAO busOnceDownSucDAO;
	private static final String OPEN_STATUS = "open_status";
	private static final String SERV_TYPE_ID = "serv_type_id";

	public List<Map<String, Object>> queryDataList(String cityId,
			String starttime1, String endtime1, String gwType) {
		logger.debug("queryDataList({},{},{})", cityId, starttime1, endtime1);

		List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tempValue = busOnceDownSucDAO.getDataList(
				cityId, starttime1, endtime1,  gwType);

		// 按属地统计
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		if(cityList == null || cityList.isEmpty()){
			return returnValue;
		}
		Collections.sort(cityList);
		List<String> cCityId = null;
		Map<String, Object> tempMap = null;
		long totalSucNum; // 注意是0和字母L 总的成功数
		long totalNum; // 总数
		long broadbandTotalNum; // 宽带 总数
		long broadbandSucNum; // 宽带 成功绑定数
		long iptvTotalNum; // IPTV 总数
		long iptvSucNum; // IPTV 成功绑定数
		long voipTotalNum; // VOIP 总数
		long voipSucNum; // VOIP成功绑定数
		for (String cityIdN : cityList) {
			// 所有子属地ID(包括自己)
			cCityId = CityDAO.getAllNextCityIdsByCityPid(cityIdN);
			tempMap = new HashMap<String, Object>();
			totalSucNum = 0L;
			totalNum = 0L;
			broadbandTotalNum = 0L;
			broadbandSucNum = 0L;
			iptvTotalNum = 0L;
			iptvSucNum = 0L;
			voipTotalNum = 0L;
			voipSucNum = 0L;
			if (null != tempValue && !tempValue.isEmpty()) {
				String cityIdT = "";
				for (Map<String, Object> stringObjectMap : tempValue) {
					cityIdT = StringUtil.getStringValue(stringObjectMap
							.get("city_id"));
					if(cCityId == null || cCityId.isEmpty()){
						break;
					}
					for (String value : cCityId) {
						// 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
						if (cityIdT.equals(value)) {

							totalNum += StringUtil.getLongValue(stringObjectMap.get("num"));
							// 如果是is_check为1则该版本为规范版本
							if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

								totalSucNum += StringUtil
										.getLongValue(stringObjectMap.get(
												"num"));
							}
							// 宽带
							if ("10".equals(StringUtil
									.getStringValue(stringObjectMap.get(
											SERV_TYPE_ID)))) {
								broadbandTotalNum += StringUtil
										.getLongValue(stringObjectMap.get(
												"num"));
								if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

									broadbandSucNum += StringUtil
											.getLongValue(stringObjectMap
													.get("num"));
								}
							}
							// IPTV
							if ("11".equals(StringUtil
									.getStringValue(stringObjectMap.get(
											SERV_TYPE_ID)))) {
								iptvTotalNum += StringUtil
										.getLongValue(stringObjectMap.get(
												"num"));
								if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

									iptvSucNum += StringUtil
											.getLongValue(stringObjectMap
													.get("num"));
								}
							}
							// VOIP
							if ("14".equals(StringUtil
									.getStringValue(stringObjectMap.get(
											SERV_TYPE_ID)))) {
								voipTotalNum += StringUtil
										.getLongValue(stringObjectMap.get(
												"num"));
								if (1 == StringUtil.getLongValue(stringObjectMap.get(OPEN_STATUS))) {

									voipSucNum += StringUtil
											.getLongValue(stringObjectMap
													.get("num"));
								}
							}
						}
					}
				}
				tempMap.put("cityId", cityIdN);
				tempMap.put("cityName", cityMap.get(cityIdN));
				tempMap.put("broadbandSucRate", percent(broadbandSucNum,
						broadbandTotalNum));
				tempMap.put("iptvSucRate",
						percent(iptvSucNum, iptvTotalNum));
				tempMap.put("voipSucRate",
						percent(voipSucNum, voipTotalNum));
				tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
			} else {
				// 当查询的结果为空时，所有值赋值0
				tempMap.put("cityId", cityIdN);
				tempMap.put("cityName", cityMap.get(cityIdN));
				tempMap.put("broadbandSucRate", percent(broadbandSucNum,
						broadbandTotalNum));
				tempMap.put("iptvSucRate",
						percent(iptvSucNum, iptvTotalNum));
				tempMap.put("voipSucRate",
						percent(voipSucNum, voipTotalNum));
				tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
			}
			returnValue.add(tempMap);
		}

		return returnValue;
	}

	public List<Map> getServInfoDetail(String cityId, String starttime1,
			String endtime1, String servTypeId, int curPageSplitPage,
			int numSplitPage,String gwType) {

		return busOnceDownSucDAO.getServInfoDetail(cityId, starttime1,
				endtime1, servTypeId, curPageSplitPage, numSplitPage, gwType);
	}

	public int getServInfoCount(String cityId, String starttime1,
			String endtime1, String servTypeId, String gwType) {

		return busOnceDownSucDAO.getServInfoCount(cityId, starttime1, endtime1,
				servTypeId, gwType);
	}

	public List<Map<String, Object>> getServInfoExcel(String cityId,
			String starttime1, String endtime1, String servTypeId, String gwType) {

		return busOnceDownSucDAO.getServInfoExcel(cityId, starttime1, endtime1,
				servTypeId, gwType);
	}

	/**
	 * 计算百分比
	 * 
	 * @param p1
	 *            分子
	 * @param p2
	 *            分母
	 * @return
	 */
	public String percent(long p1, long p2) {

		logger.debug("percent({},{})", new Object[] { p1, p2 });

		double p3;
		if (p2 == 0) {
			if(LipossGlobals.inArea(Global.NXDX)){
				return "0.00%";
			}else {
				return "N/A";
			}
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public BusOnceDownSucDAO getBusOnceDownSucDAO() {
		return busOnceDownSucDAO;
	}

	public void setBusOnceDownSucDAO(BusOnceDownSucDAO busOnceDownSucDAO) {
		this.busOnceDownSucDAO = busOnceDownSucDAO;
	}

}
