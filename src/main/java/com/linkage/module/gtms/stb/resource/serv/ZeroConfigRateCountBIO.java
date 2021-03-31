
package com.linkage.module.gtms.stb.resource.serv;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.ZeroConfigRateCountDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigRateCountBIO
{

	private static Logger logger = LoggerFactory.getLogger(ZeroConfigRateCountBIO.class);
	private ZeroConfigRateCountDAO dao;

	@SuppressWarnings("rawtypes")
	public List<Map> countAll(String starttime, String endtime, String paramCityId)
	{
		logger.debug("ZeroConfigRateCountBIO====>countAll({},{},{})", new Object[] {
				starttime, endtime });
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		List<String> cityList = CityDAO.getNextCityIdsByCityPid(paramCityId);
		Collections.sort(cityList);
		// 获取所有设备绑定情况
		List<Map> lt = dao.getAllSheetNum(starttime, endtime);
		for (String cityId : cityList)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("cityId", cityId);
			tmap.put("cityName", cityMap.get(cityId));
			Map<String, Object> totalCityMap = new HashMap<String, Object>();
			if (paramCityId.equals(cityId))
			{
				totalCityMap = statisticsCurrDevice(cityId, lt);
			}
			else
			{
				recursionStatistics(totalCityMap, cityId, lt);
			}
			tmap.put("bindNumTal", totalCityMap.get("bindNumTal"));
			tmap.put("zeroBindNumTal", totalCityMap.get("zeroBindNumTal"));
			tmap.put("successBindNumTal", totalCityMap.get("successBindNumTal"));
			tmap.put("failBindNumTal", totalCityMap.get("failBindNumTal"));
			tmap.put("zeroFailOtherTal", totalCityMap.get("zeroFailOtherTal"));
			tmap.put(
					"zeroBindRate",
					percent(StringUtil.getLongValue(totalCityMap.get("zeroBindNumTal")),
							StringUtil.getLongValue(totalCityMap.get("bindNumTal"))));
			tmap.put(
					"successRate",
					percent(StringUtil
							.getLongValue(totalCityMap.get("successBindNumTal")),
							StringUtil.getLongValue(totalCityMap.get("zeroBindNumTal"))));
			tmap.put("imiNumTal", totalCityMap.get("imiNumTal"));
			tmap.put("macSuccTal", totalCityMap.get("macSuccTal"));
			tmap.put(
					"macRate",
					percent(StringUtil.getLongValue(totalCityMap.get("macSuccTal")),
							StringUtil.getLongValue(totalCityMap.get("macNumTal"))));
			tmap.put("ipSuccTal", totalCityMap.get("ipSuccTal"));
			tmap.put(
					"ipRate",
					percent(StringUtil.getLongValue(totalCityMap.get("ipSuccTal")),
							StringUtil.getLongValue(totalCityMap.get("ipNumTal"))));
			tmap.put("itvSuccTal", totalCityMap.get("itvSuccTal"));
			tmap.put(
					"itvRate",
					percent(StringUtil.getLongValue(totalCityMap.get("itvSuccTal")),
							StringUtil.getLongValue(totalCityMap.get("itvNumTal"))));
			list.add(tmap);
		}
		return list;
	}

	/**
	 * 递归的统计city下的零配置数，直到其下面没有城市
	 * 
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private void recursionStatistics(Map<String, Object> totalCityMap, String cityId,
			List<Map> allDevice)
	{
		Map<String, Object> currCityMap = new HashMap<String, Object>();
		// 当前cityId对应的零配置统计情况
		currCityMap = statisticsCurrDevice(cityId, allDevice);
		totalCityMap.put("bindNumTal", StringUtil.getIntValue(totalCityMap, "bindNumTal")
				+ StringUtil.getIntValue(currCityMap, "bindNumTal"));
		totalCityMap.put(
				"zeroBindNumTal",
				StringUtil.getIntValue(totalCityMap, "zeroBindNumTal")
						+ StringUtil.getIntValue(currCityMap, "zeroBindNumTal"));
		totalCityMap.put(
				"successBindNumTal",
				StringUtil.getIntValue(totalCityMap, "successBindNumTal")
						+ StringUtil.getIntValue(currCityMap, "successBindNumTal"));
		totalCityMap.put(
				"failBindNumTal",
				StringUtil.getIntValue(totalCityMap, "failBindNumTal")
						+ StringUtil.getIntValue(currCityMap, "failBindNumTal"));
		totalCityMap.put(
				"zeroFailOtherTal",
				StringUtil.getIntValue(totalCityMap, "zeroFailOtherTal")
						+ StringUtil.getIntValue(currCityMap, "zeroFailOtherTal"));
		totalCityMap.put("imiNumTal", StringUtil.getIntValue(totalCityMap, "imiNumTal")
				+ StringUtil.getIntValue(currCityMap, "imiNumTal"));
		totalCityMap.put("macSuccTal", StringUtil.getIntValue(totalCityMap, "macSuccTal")
				+ StringUtil.getIntValue(currCityMap, "macSuccTal"));
		totalCityMap.put("macNumTal", StringUtil.getIntValue(totalCityMap, "macNumTal")
				+ StringUtil.getIntValue(currCityMap, "macNumTal"));
		totalCityMap.put("ipSuccTal", StringUtil.getIntValue(totalCityMap, "ipSuccTal")
				+ StringUtil.getIntValue(currCityMap, "ipSuccTal"));
		totalCityMap.put("ipNumTal", StringUtil.getIntValue(totalCityMap, "ipNumTal")
				+ StringUtil.getIntValue(currCityMap, "ipNumTal"));
		totalCityMap.put("itvSuccTal", StringUtil.getIntValue(totalCityMap, "itvSuccTal")
				+ StringUtil.getIntValue(currCityMap, "itvSuccTal"));
		totalCityMap.put("itvNumTal", StringUtil.getIntValue(totalCityMap, "itvNumTal")
				+ StringUtil.getIntValue(currCityMap, "itvNumTal"));
		ArrayList<String> subList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		if (subList.size() > 1)
		{
			for (String sunCityId : subList)
			{
				if (!cityId.equals(sunCityId))
				{
					recursionStatistics(totalCityMap, sunCityId, allDevice);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> statisticsCurrDevice(String cityId, List<Map> allDevice)
	{
		Map<String, Object> tmap = new HashMap<String, Object>();
		// 机顶盒安装总数
		long bindNumTal = 0l;
		// 机顶盒零配置总数
		long zeroBindNumTal = 0l;
		// 机顶盒零配置成功总数
		long successBindNumTal = 0l;
		// 机顶盒零配置失败总数
		long failBindNumTal = 0l;
		// 爱运维总数
		long imiNumTal = 0l;
		// MAC总数、成功数
		long macNumTal = 0l;
		long macSuccTal = 0l;
		// IP总数、成功数
		long ipNumTal = 0l;
		long ipSuccTal = 0l;
		// 自助拨号总数、成功数
		long itvNumTal = 0l;
		long itvSuccTal = 0l;
		// 零配置失败的其他绑定数
		long zeroFailOtherTal = 0l;
		for (Map map : allDevice)
		{
			if (cityId.equals(map.get("city_id")))
			{
				long currNum = Long.parseLong(StringUtil.getStringValue(map.get("num")));
				// 绑定总数不区分是否为零配置
				bindNumTal += currNum;
				// 求零配置总数
				if (("0".equals(StringUtil.getStringValue(map.get("bind_way")))
						|| "1".equals(StringUtil.getStringValue(map.get("bind_way")))
						|| "2".equals(StringUtil.getStringValue(map.get("bind_way"))) || "5"
							.equals(StringUtil.getStringValue(map.get("bind_way"))))
						&& !"0".equals(StringUtil.getStringValue(map.get("bind_state"))))
				{
					zeroBindNumTal += currNum;
					// MAC
					if ("0".equals(StringUtil.getStringValue(map.get("bind_way"))))
					{
						macNumTal += currNum;
						if ("1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							successBindNumTal += currNum;
							macSuccTal += currNum;
						}
						if ("-1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							failBindNumTal += currNum;
						}
					}
					// IP
					if ("1".equals(StringUtil.getStringValue(map.get("bind_way"))))
					{
						ipNumTal += currNum;
						if ("1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							successBindNumTal += currNum;
							ipSuccTal += currNum;
						}
						if ("-1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							failBindNumTal += currNum;
						}
					}
					// 自助拨号
					if ("2".equals(StringUtil.getStringValue(map.get("bind_way"))))
					{
						itvNumTal += currNum;
						if ("1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							successBindNumTal += currNum;
							itvSuccTal += currNum;
						}
						if ("-1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							failBindNumTal += currNum;
						}
					}
					// 爱运维总数
					if ("5".equals(StringUtil.getStringValue(map.get("bind_way"))))
					{
						if ("1".equals(StringUtil.getStringValue(map.get("bind_state"))))
						{
							successBindNumTal += currNum;
							imiNumTal += currNum;
						}
					}
				}
				// 零配置失败，其他绑定
				if ("3".equals(StringUtil.getStringValue(map.get("bind_way")))
						&& !"0".equals(StringUtil.getStringValue(map.get("bind_state"))))
				{
					if ("-1".equals(StringUtil.getStringValue(map.get("bind_state"))))
					{
						bindNumTal += currNum;
						zeroFailOtherTal += currNum;
					}
				}
			}
		}
		tmap.put("bindNumTal", bindNumTal);
		tmap.put("zeroBindNumTal", zeroBindNumTal);
		tmap.put("successBindNumTal", successBindNumTal);
		tmap.put("failBindNumTal", failBindNumTal);
		tmap.put("zeroFailOtherTal", zeroFailOtherTal);
		tmap.put("imiNumTal", imiNumTal);
		tmap.put("macSuccTal", macSuccTal);
		tmap.put("macNumTal", macNumTal);
		tmap.put("ipSuccTal", ipSuccTal);
		tmap.put("ipNumTal", ipNumTal);
		tmap.put("itvSuccTal", itvSuccTal);
		tmap.put("itvNumTal", itvNumTal);
		return tmap;
	}

	/**
	 * @author 岩
	 * @date 2016-6-7
	 * @param reasonId
	 * @param cityName
	 * @param starttime
	 * @param endtime
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryZeroConfigDetail(String cityId, String bindState,
			String bindWay, String starttime, String endtime, int curPageSplitPage,
			int numSplitPage)
	{
		logger.debug("ZeroConfigRateCountBIO => queryZeroConfigDetail()");
		List<Map> zeroConfigCountList = dao.queryZeroConfigDetail(cityId, bindState,
				bindWay, starttime, endtime, curPageSplitPage, numSplitPage);
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		if (zeroConfigCountList != null)
		{
			for (Map map : zeroConfigCountList)
			{
				map.put("city_name", cityMap.get(map.get("city_id")));
				DateTimeUtil recent_unbinddate = new DateTimeUtil(
						StringUtil.getLongValue(map.get("bind_time")) * 1000l);
				map.put("bind_time", recent_unbinddate.getLongDate());
			}
		}
		return zeroConfigCountList;
	}

	public int queryZeroConfigDetailCount(String cityId, String bindState,
			String bindWay, String starttime, String endtime)
	{
		return dao.queryZeroConfigDetailCount(cityId, bindState, bindWay, starttime,
				endtime);
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
	private String percent(long p1, long p2)
	{
		logger.debug("percent({},{})", new Object[] { p1, p2 });
		double p3;
		if (p2 == 0)
		{
			return "N/A";
		}
		else
		{
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	public ZeroConfigRateCountDAO getDao()
	{
		return dao;
	}

	public void setDao(ZeroConfigRateCountDAO dao)
	{
		this.dao = dao;
	}
}
