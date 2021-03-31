
package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.dao.UserDeviceReportDAO;

/**
 * prototype类型
 * 
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-7-19
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UserDeviceReportBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(UserDeviceReportBIO.class);
	private static final long DEFAULT_COUNT = 0L;
	private UserDeviceReportDAO userDeviceReportDAO;
	// 屏蔽内存加载方式，不然需要从数据库从新加载数据，需要重启服务
	// private static List e8bUserList = null;
	// private static List e8cUserList = null;
	// private static List bindE8bUserList = null;
	// private static List bindE8cUserList = null;
	private List e8bUserList = null;
	private List e8cUserList = null;
	private List bindE8bUserList = null;
	private List bindE8cUserList = null;

	public List<Map> queryUserDevice(String cityId, String startTimeInSecond,
			String endTimeInSecond)
	{
		logger.info("queryUserDevice in city[{}]", cityId);
		// 因为数据库数据量较大，为提高页面响应速度，暂将数据一次加载到内存，不再实时从数据库查询。
		// if (e8bUserList == null)
		// {
		e8bUserList = userDeviceReportDAO
				.countE8BUser(startTimeInSecond, endTimeInSecond);
		// }
		// if (e8cUserList == null)
		// {
		e8cUserList = userDeviceReportDAO
				.countE8CUser(startTimeInSecond, endTimeInSecond);
		// }
		// if (bindE8bUserList == null)
		// {
		bindE8bUserList = userDeviceReportDAO.countBindE8BUser(startTimeInSecond,
				endTimeInSecond);
		// }
		// if (bindE8cUserList == null)
		// {
		bindE8cUserList = userDeviceReportDAO.countBindE8CUser(startTimeInSecond,
				endTimeInSecond);
		// }
		Map cityIdNameMap = CityDAO.getCityIdCityNameMap();
		List<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		if (!"00".equals(cityId))
		{
			cityList.add(cityId);
		}
		Collections.sort(cityList);
		List<Map> resultList = new ArrayList<Map>();
		for (String tmpCityId : cityList)
		{
			resultList.add(countByPid(tmpCityId, cityIdNameMap));
		}
		if ("00".equals(cityId))
		{
			// 如果是省中心，则统计的数据是所有数据的总和
			resultList.add(countCenter("00", cityIdNameMap, resultList));
		}
		return resultList;
	}

	/**
	 * 如果是统计省中心数据，则省中心数据是子地市的所有数据的总和。该方法调用在统计子地市的总和之后调用
	 * 
	 * @param centerCityId
	 * @param cityIdNameMap
	 * @param childCountList
	 * @return
	 */
	private Map countCenter(String centerCityId, Map cityIdNameMap,
			List<Map> childCountList)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("city_id", centerCityId);
		resultMap.put("city_name",
				StringUtil.getStringValue(cityIdNameMap.get(centerCityId)));
		long e8bCount = 0L;
		long e8cCount = 0L;
		long allE8cCount = 0L;
		long bindE8bCount = 0L;
		long bindE8cCount = 0L;
		long bindAllE8cCount = 0L;
		for (Map childMap : childCountList)
		{
			e8bCount += StringUtil.getLongValue(childMap, "e8b_count", DEFAULT_COUNT);
			e8cCount += StringUtil.getLongValue(childMap, "e8c_count", DEFAULT_COUNT);
			allE8cCount += StringUtil.getLongValue(childMap, "all_e8c_count",
					DEFAULT_COUNT);
			bindE8bCount += StringUtil.getLongValue(childMap, "bind_e8b_count",
					DEFAULT_COUNT);
			bindE8cCount += StringUtil.getLongValue(childMap, "bind_e8c_count",
					DEFAULT_COUNT);
			bindAllE8cCount += StringUtil.getLongValue(childMap, "bind_all_e8c_count",
					DEFAULT_COUNT);
		}
		resultMap.put("e8b_count", String.valueOf(e8bCount));
		resultMap.put("e8c_count", String.valueOf(e8cCount));
		resultMap.put("all_e8c_count", String.valueOf(allE8cCount));
		resultMap.put("bind_e8b_count", String.valueOf(bindE8bCount));
		resultMap.put("bind_e8c_count", String.valueOf(bindE8cCount));
		resultMap.put("bind_all_e8c_count", String.valueOf(bindAllE8cCount));
		logger.info("userDeiveReport by city[{}], result is {}", centerCityId, resultMap);
		return resultMap;
	}

	/**
	 * 根据父城市ID查询子城市的所有用户数
	 * 
	 * @param parentCityId
	 * @return
	 */
	private Map countByPid(String parentCityId, Map cityIdNameMap)
	{
		Map<String, String> resultMap = new HashMap<String, String>();
		List<String> cityList = CityDAO.getNextCityIdsByCityPid(parentCityId);
		resultMap.put("city_id", parentCityId);
		resultMap.put("city_name",
				StringUtil.getStringValue(cityIdNameMap.get(parentCityId)));
		long e8bCount = 0L;
		long e8cCount = 0L;
		long allE8cCount = 0L;
		long bindE8bCount = 0L;
		long bindE8cCount = 0L;
		long bindAllE8cCount = 0L;
		for (String cityId : cityList)
		{
			e8bCount += getLongValue(e8bUserList, cityId, "e8b_count");
			Map e8cUserMap = getMap(e8cUserList, cityId);
			e8cCount += StringUtil.getLongValue(e8cUserMap, "e8c_count", DEFAULT_COUNT);
			allE8cCount += StringUtil.getLongValue(e8cUserMap, "all_e8c_count",
					DEFAULT_COUNT);
			bindE8bCount += getLongValue(bindE8bUserList, cityId, "bind_e8b_count");
			Map bindE8cUserMap = getMap(bindE8cUserList, cityId);
			bindE8cCount += StringUtil.getLongValue(bindE8cUserMap, "bind_e8c_count",
					DEFAULT_COUNT);
			bindAllE8cCount += StringUtil.getLongValue(bindE8cUserMap,
					"bind_all_e8c_count", DEFAULT_COUNT);
		}
		resultMap.put("e8b_count", String.valueOf(e8bCount));
		resultMap.put("e8c_count", String.valueOf(e8cCount));
		resultMap.put("all_e8c_count", String.valueOf(allE8cCount));
		resultMap.put("bind_e8b_count", String.valueOf(bindE8bCount));
		resultMap.put("bind_e8c_count", String.valueOf(bindE8cCount));
		resultMap.put("bind_all_e8c_count", String.valueOf(bindAllE8cCount));
		logger.info("userDeiveReport by city[{}], result is {}", parentCityId, resultMap);
		return resultMap;
	}

	private long getLongValue(List<Map> list, String cityId, String key)
	{
		Map map = getMap(list, cityId);
		return StringUtil.getLongValue(map, key, DEFAULT_COUNT);
	}

	private Map getMap(List<Map> list, String cityId)
	{
		for (Map map : list)
		{
			if (cityId.equals(StringUtil.getStringValue(map.get("city_id"))))
			{
				return map;
			}
		}
		return new HashMap();
	}

	public UserDeviceReportDAO getUserDeviceReportDAO()
	{
		return userDeviceReportDAO;
	}

	public void setUserDeviceReportDAO(UserDeviceReportDAO userDeviceReportDAO)
	{
		this.userDeviceReportDAO = userDeviceReportDAO;
	}
}
