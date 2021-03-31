
package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.StbDeviceCountDAO;

public class StbDeviceCountBIO
{

	private static Logger logger = LoggerFactory.getLogger(StbDeviceCountBIO.class);
	private StbDeviceCountDAO dao;

	/**
	 * 根据厂商属地统计设备数量
	 * 
	 * @param userCityId
	 * @param gw_type
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String, String>> getCountData(String userCityId)
	{
		logger.debug("StbDeviceCountBIO=>getCountData({})", new Object[] { userCityId });
		List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
		// 厂商
		Map<String, String> vendorMap = dao.getVendorMap();
		// 查询结果
		Map<String, Map<String, Integer>> map2 = dao.getCountMap(userCityId);
		Map<String, String> resMap = null;
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(userCityId);
		Collections.sort(cityList);
		Iterator it = vendorMap.keySet().iterator();
		while (it.hasNext())
		{
			String vendorId = StringUtil.getStringValue(it.next());
			resMap = new HashMap<String, String>();
			resMap.put("vendorName", vendorMap.get(vendorId) + "(" + vendorId + ")");
			Map<String, Integer> tempMap = map2.get(vendorId);
			int addNum = 0;
			if (null != tempMap)
			{
				for (int j = 0; j < cityList.size(); j++)
				{
					String city_ID = cityList.get(j);
					ArrayList<String> subList = CityDAO
							.getAllNextCityIdsByCityPid(city_ID);
					int num = 0;
					if (city_ID.equals(userCityId))
					{
						num = StringUtil.getIntegerValue(tempMap.get(city_ID));
					}
					else
					{
						for (int k = 0; k < subList.size(); k++)
						{
							String subCityId = subList.get(k);
							num += StringUtil.getIntegerValue(tempMap.get(subCityId));
						}
					}
					addNum += num;
					resMap.put("vendorId", vendorId);
					resMap.put(city_ID, num + "");
				}
				resMap.put("total", addNum + "");
			}
			else
			{
				for (int j = 0; j < cityList.size(); j++)
				{
					String city_ID = cityList.get(j);
					resMap.put(city_ID, "0");
					resMap.put("total", "0");
				}
			}
			resList.add(resMap);
		}
		// 以上只是和查询出来的结果对应，下面是接着处理没有值的数据和小计
		// 处理小计
		HashMap<String, String> totalNumMap = new HashMap<String, String>();
		totalNumMap.put("vendorName", "小计");
		int sumTotalNum = 0;
		for (int j = 0; j < cityList.size(); j++)
		{
			String city_ID = cityList.get(j);
			int totalNum = 0;
			for (int k = 0; k < resList.size(); k++)
			{
				Map tempMap = resList.get(k);
				tempMap.get(city_ID);
				totalNum += StringUtil.getIntegerValue(tempMap.get(city_ID));
			}
			totalNumMap.put(city_ID, totalNum + "");
			sumTotalNum += totalNum;
		}
		totalNumMap.put("total", sumTotalNum + "");
		resList.add(totalNumMap);
		return resList;
	}

	/**
	 * 获取设备详细信息
	 * 
	 * @param vendorId
	 * @param cityId
	 * @param gw_type
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getDetailInfo(String vendorId, String cityId, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("StbDeviceCountBIO=>getDetailInfo({},{},{},{})", new Object[] {
				vendorId, cityId, curPage_splitPage, num_splitPage });
		return dao.getDetailInfo(vendorId, cityId, curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取详细页面设备总数
	 * 
	 * @param vendorId
	 * @param cityId
	 * @param gw_type
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return int
	 */
	public int countQueryDetail(String vendorId, String cityId, int curPage_splitPage,
			int num_splitPage)
	{
		logger.debug("StbDeviceCountBIO=>countQueryDetail({},{},{},{})", new Object[] {
				vendorId, cityId, curPage_splitPage, num_splitPage });
		return dao.countQueryDetail(vendorId, cityId, curPage_splitPage, num_splitPage);
	}

	public void setDao(StbDeviceCountDAO dao)
	{
		this.dao = dao;
	}
}
