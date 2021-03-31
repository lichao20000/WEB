
package com.linkage.module.itms.resource.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.StbSheetSuccDAO;

public class StbSheetSuccBIO
{

	private static Logger logger = LoggerFactory.getLogger(StbSheetSuccBIO.class);
	private StbSheetSuccDAO dao;

	public List<Map<String, Object>> getCountData(String cityId, String starttime,
			String endtime)
	{
		logger.debug("StbSheetSuccBIO=>getCountData({},{},{})", new Object[] { cityId,
				starttime, endtime });
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 根据属地cityId获取下一层属地ID(包含自己)
		List<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		// 按属地统计
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		Collections.sort(cityList);
		// 获取所有接入方式成功情况
		List<Map<String, String>> successList = dao.getCountNum(cityId, starttime,
				endtime, "1");
		// 获取所有接入方式总工单数情况
		List<Map<String, String>> TotalList = dao.getCountNum(cityId, starttime, endtime,
				"2");
		Map<String, String> FTTHSuccMap = new HashMap<String, String>();
		Map<String, String> FTTBSuccMap = new HashMap<String, String>();
		Map<String, String> LANSuccMap = new HashMap<String, String>();
		Map<String, String> HGWSuccMap = new HashMap<String, String>();
		Map<String, String> FTTHTotalMap = new HashMap<String, String>();
		Map<String, String> FTTBTotalMap = new HashMap<String, String>();
		Map<String, String> LANTotalMap = new HashMap<String, String>();
		Map<String, String> HGWTotalMap = new HashMap<String, String>();
		if (!successList.isEmpty())
		{
			for (int i = 0; i < successList.size(); i++)
			{
				String stbUpyle = StringUtil.getStringValue(successList.get(i),
						"stbuptyle");
				// FTTH
				if ("1".equals(stbUpyle))
				{
					FTTHSuccMap.put(
							StringUtil.getStringValue(successList.get(i), "city_id"),
							StringUtil.getStringValue(successList.get(i), "num"));
				}
				else if ("2".equals(stbUpyle))// FTTB
				{
					FTTBSuccMap.put(
							StringUtil.getStringValue(successList.get(i), "city_id"),
							StringUtil.getStringValue(successList.get(i), "num"));
				}
				else if ("3".equals(stbUpyle))// LAN
				{
					LANSuccMap.put(
							StringUtil.getStringValue(successList.get(i), "city_id"),
							StringUtil.getStringValue(successList.get(i), "num"));
				}
				else
				// HGW
				{
					HGWSuccMap.put(
							StringUtil.getStringValue(successList.get(i), "city_id"),
							StringUtil.getStringValue(successList.get(i), "num"));
				}
			}
		}
		if (!TotalList.isEmpty())
		{
			for (int i = 0; i < TotalList.size(); i++)
			{
				String stbUpyle = StringUtil
						.getStringValue(TotalList.get(i), "stbuptyle");
				// FTTH
				if ("1".equals(stbUpyle))
				{
					FTTHTotalMap.put(
							StringUtil.getStringValue(TotalList.get(i), "city_id"),
							StringUtil.getStringValue(TotalList.get(i), "num"));
				}
				else if ("2".equals(stbUpyle)) // FTTB
				{
					FTTBTotalMap.put(
							StringUtil.getStringValue(TotalList.get(i), "city_id"),
							StringUtil.getStringValue(TotalList.get(i), "num"));
				}
				else if ("3".equals(stbUpyle))// LAN
				{
					LANTotalMap.put(
							StringUtil.getStringValue(TotalList.get(i), "city_id"),
							StringUtil.getStringValue(TotalList.get(i), "num"));
				}
				else
				// HGW
				{
					HGWTotalMap.put(
							StringUtil.getStringValue(TotalList.get(i), "city_id"),
							StringUtil.getStringValue(TotalList.get(i), "num"));
				}
			}
		}
		// FTTH成功数合计
		long FTTHSuccNumTal = 0l;
		// FTTB成功数合计
		long FTTBSuccNumTal = 0l;
		// LAN成功数合计
		long LANSuccNumTal = 0l;
		// LAN成功数合计
		long HGWSuccNumTal = 0l;
		// FTTH总数合计
		long FTTHTotalNumTal = 0l;
		// FTTB总数合计
		long FTTBTotalNumTal = 0l;
		// LAN总数合计
		long LANTotalNumTal = 0l;
		// LAN总数合计
		long HGWTotalNumTal = 0l;
		Map<String, Object> tmap = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			String city_id = cityList.get(i);
			ArrayList<String> subList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap = new HashMap<String, Object>();
			tmap.put("cityId", city_id);
			tmap.put("cityName", cityMap.get(city_id));
			// FTTH成功数
			long FTTHSuccNum = 0l;
			// FTTB成功数
			long FTTBSuccNum = 0l;
			// LAN成功数
			long LANSuccNum = 0l;
			// HGW成功数
			long HGWSuccNum = 0l;
			if (city_id.equals(cityId))
			{
				FTTHSuccNum = StringUtil.getLongValue(FTTHSuccMap.get(city_id));
				FTTBSuccNum = StringUtil.getLongValue(FTTBSuccMap.get(city_id));
				LANSuccNum = StringUtil.getLongValue(LANSuccMap.get(city_id));
				HGWSuccNum = StringUtil.getLongValue(LANSuccMap.get(city_id));
			}
			else
			{
				for (int j = 0; j < subList.size(); j++)
				{
					String subCityId = subList.get(j);
					FTTHSuccNum += +StringUtil.getLongValue(FTTHSuccMap.get(subCityId));
					FTTBSuccNum += StringUtil.getLongValue(FTTBSuccMap.get(subCityId));
					LANSuccNum += StringUtil.getLongValue(LANSuccMap.get(subCityId));
					HGWSuccNum += StringUtil.getLongValue(HGWSuccMap.get(subCityId));
				}
			}
			FTTHSuccNumTal += FTTHSuccNum;
			FTTBSuccNumTal += FTTBSuccNum;
			LANSuccNumTal += LANSuccNum;
			HGWSuccNumTal += HGWSuccNum;
			// FTTH总数
			long FTTHTotalNum = 0l;
			// FTTB总数
			long FTTBTotalNum = 0l;
			// LAN总数
			long LANTotalNum = 0l;
			// HGW总数
			long HGWTotalNum = 0l;
			if (city_id.equals(cityId))
			{
				FTTHTotalNum = StringUtil.getLongValue(FTTHTotalMap.get(city_id));
				FTTBTotalNum = StringUtil.getLongValue(FTTBTotalMap.get(city_id));
				LANTotalNum = StringUtil.getLongValue(LANTotalMap.get(city_id));
				HGWTotalNum = StringUtil.getLongValue(HGWTotalMap.get(city_id));
			}
			else
			{
				for (int j = 0; j < subList.size(); j++)
				{
					String subCityId = subList.get(j);
					FTTHTotalNum += +StringUtil.getLongValue(FTTHTotalMap.get(subCityId));
					FTTBTotalNum += StringUtil.getLongValue(FTTBTotalMap.get(subCityId));
					LANTotalNum += StringUtil.getLongValue(LANTotalMap.get(subCityId));
					HGWTotalNum += StringUtil.getLongValue(HGWTotalMap.get(subCityId));
				}
			}
			FTTHTotalNumTal += FTTHTotalNum;
			FTTBTotalNumTal += FTTBTotalNum;
			LANTotalNumTal += LANTotalNum;
			HGWTotalNumTal += HGWTotalNum;
			tmap.put("FTTHSuccNum", FTTHSuccNum);
			tmap.put("FTTHTotalNum", FTTHTotalNum);
			tmap.put("FTTHSuccRate", percent(FTTHSuccNum, FTTHTotalNum));
			tmap.put("FTTBSuccNum", FTTBSuccNum);
			tmap.put("FTTBTotalNum", FTTBTotalNum);
			tmap.put("FTTBSuccRate", percent(FTTBSuccNum, FTTBTotalNum));
			tmap.put("LANSuccNum", LANSuccNum);
			tmap.put("LANTotalNum", LANTotalNum);
			tmap.put("LANSuccRate", percent(LANSuccNum, LANTotalNum));
			tmap.put("HGWSuccNum", HGWSuccNum);
			tmap.put("HGWTotalNum", HGWTotalNum);
			tmap.put("HGWSuccRate", percent(HGWSuccNum, HGWTotalNum));
			long toalSuccNum = FTTHSuccNum + FTTBSuccNum + LANSuccNum + HGWSuccNum;
			tmap.put("totalSuccNum", toalSuccNum);
			long totalNum = FTTHTotalNum + FTTBTotalNum + LANTotalNum + HGWTotalNum;
			tmap.put("totalNum", totalNum);
			tmap.put("totalSuccRate", percent(toalSuccNum, totalNum));
			list.add(tmap);
			subList = null;
		}
		// 下面合计一行
		tmap = new HashMap<String, Object>();
		tmap.put("cityId", "00");
		tmap.put("cityName", "总数");
		tmap.put("FTTHSuccNum", FTTHSuccNumTal);
		tmap.put("FTTHTotalNum", FTTHTotalNumTal);
		tmap.put("FTTHSuccRate", percent(FTTHSuccNumTal, FTTHTotalNumTal));
		tmap.put("FTTBSuccNum", FTTBSuccNumTal);
		tmap.put("FTTBTotalNum", FTTBTotalNumTal);
		tmap.put("FTTBSuccRate", percent(FTTBSuccNumTal, FTTBTotalNumTal));
		tmap.put("LANSuccNum", LANSuccNumTal);
		tmap.put("LANTotalNum", LANTotalNumTal);
		tmap.put("LANSuccRate", percent(LANSuccNumTal, LANTotalNumTal));
		tmap.put("HGWSuccNum", HGWSuccNumTal);
		tmap.put("HGWTotalNum", HGWTotalNumTal);
		tmap.put("HGWSuccRate", percent(HGWSuccNumTal, HGWTotalNumTal));
		long sumToalSuccNum = FTTHSuccNumTal + FTTBSuccNumTal + LANSuccNumTal
				+ HGWSuccNumTal;
		tmap.put("totalSuccNum", sumToalSuccNum);
		long sumTotalNum = FTTHTotalNumTal + FTTBTotalNumTal + LANTotalNumTal
				+ HGWTotalNumTal;
		tmap.put("totalNum", sumTotalNum);
		tmap.put("totalSuccRate", percent(sumToalSuccNum, sumTotalNum));
		list.add(tmap);
		cityMap = null;
		cityList = null;
		return list;
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
	public String percent(long p1, long p2)
	{
		logger.debug("percent({},{})", new Object[] { p1, p2 });
		double p3;
		if (p2 == 0)
		{
			p3 = 0;
		}
		else
		{
			p3 = StringUtil.getDoubleValue(p1) / StringUtil.getDoubleValue(p2);
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public void setDao(StbSheetSuccDAO dao)
	{
		this.dao = dao;
	}
}
