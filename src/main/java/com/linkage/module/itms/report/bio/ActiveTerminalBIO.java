
package com.linkage.module.itms.report.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.act.ActiveTerminalACT;
import com.linkage.module.itms.report.dao.ActiveTerminalDAO;

public class ActiveTerminalBIO
{

	private static Logger logger = LoggerFactory.getLogger(ActiveTerminalBIO.class);
	private ActiveTerminalDAO activeTerminalDAO = null;

	/**
	 * public List<Map> getActiveTerminalInfo(String city_id, String starttime, String
	 * endtime, String device_type,int curPage_splitPage, int num_splitPage){
	 * logger.debug("getActiveTerminalInfo()"); return
	 * activeTerminalDAO.getActiveTerminalInfo(city_id, starttime, endtime, device_type,
	 * curPage_splitPage, num_splitPage); }
	 */
	public List<Map> getActiveTerminalInfo(String city_id, String starttime,
			String endtime, String device_type)
	{
		logger.debug("getActiveTerminalInfo()");
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map activemap = activeTerminalDAO.getActiveTerminalMap(city_id, starttime,
				endtime, device_type);
		// 活跃设备数
		long total;
		// 设备总数
		long ttotal;
		String percentage = "N/A";
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			ttotal = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != activemap && !activemap.isEmpty())
				{
					String str = StringUtil.getStringValue(activemap.get(tlist.get(j)));
					if (null != str && !"".equals(str.trim()))
					{
						String[] arrStr = str.split(":");
						total = total + StringUtil.getLongValue(arrStr[0]);
						ttotal = ttotal + StringUtil.getLongValue(arrStr[1]);
					}
				}
			}
			tmap.put("terminal_total", ttotal);
			tmap.put("terminal_activeTotal", total);
			percentage = getDecimal(StringUtil.getStringValue(total),
					StringUtil.getStringValue(ttotal));
			tmap.put("percentage", percentage);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	public List<Map> getDeviceListForAll(String city_id, String device_type,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceListForAll");
		return activeTerminalDAO.getDeviceListForAll(city_id, device_type,
				curPage_splitPage, num_splitPage);
	}

	public int countDeviceListForAll(String city_id, String device_type,
			int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countDeviceListForAll");
		return activeTerminalDAO.getDevForAllCount(city_id, device_type,
				curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevForAllExcel(String city_id, String device_type)
	{
		logger.debug("getDevForAllExcel");
		return activeTerminalDAO.getDevForAllExcel(city_id, device_type);
	}

	public List<Map> getDeviceListForTime(String city_id, String starttime,
			String endtime, String device_type, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceListForTime");
		return activeTerminalDAO.getDeviceListForTime(city_id, starttime, endtime,
				device_type, curPage_splitPage, num_splitPage);
	}

	public int getDeviceListForTimeCount(String city_id, String starttime,
			String endtime, String device_type, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("getDeviceListForTimeCount");
		return activeTerminalDAO.getDeviceListForTimeCount(city_id, starttime, endtime,
				device_type, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDeviceListForTimeExcel(String city_id, String starttime,
			String endtime, String device_type)
	{
		logger.debug("getDeviceListForTimeExcel");
		return activeTerminalDAO.getDeviceListForTimeExcel(city_id, starttime, endtime,
				device_type);
	}

	public ActiveTerminalDAO getActiveTerminalDAO()
	{
		return activeTerminalDAO;
	}

	public void setActiveTerminalDAO(ActiveTerminalDAO activeTerminalDAO)
	{
		this.activeTerminalDAO = activeTerminalDAO;
	}

	private String getDecimal(String total, String ttotal)
	{
		if (null == total || "0".equals(total) || null == ttotal || "0".equals(ttotal))
		{
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
}
