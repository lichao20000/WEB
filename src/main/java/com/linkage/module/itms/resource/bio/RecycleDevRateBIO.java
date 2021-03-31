
package com.linkage.module.itms.resource.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.RecycleDevRateDAO;

/**
 * 回收终端使用率
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-26
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class RecycleDevRateBIO
{

	/** dao */
	private RecycleDevRateDAO dao;

	/**
	 * 获取回收终端使用率，调用dao内的方法获取回收终端数以及回收终端使用数
	 * 根据city_id是否为00做相应的处理
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public String recycleDevRate(String cityId, String starttime, String endtime)
	{
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		// 回收终端总数  		用Buffer
		StringBuffer totalCount = new StringBuffer("[");
		// 回收终端使用数	用Buffer
		StringBuffer useCount = new StringBuffer("[");
		// 回收终端未使用数	用Buffer
		StringBuffer unUseCount = new StringBuffer("[");
		// 计算全地市 数量用(非"00"时使用)
		int useCount_all = 0;
		int unUseCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int use_count = 0;
		int total_count = 0;
		int unUse_count = 0;
		List<String> cityIds = new ArrayList<String>();
		boolean replaceFlg = false;
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> useCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				useCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的回收终端使用数
			Map<String, String> useCountMap = dao.getRecycleDevCountFor00(starttime, endtime);
			// 全省时 求出各个cityId对应的回收终端总数
			Map<String, String> totalCountMap = dao.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (useCountMap != null && !useCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : useCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						useCountMap00.put(parent_id, useCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						useCountMap00.put( "00", useCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						useCountMap00.put(
								entry.getKey(),
								useCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							useCountMap00.put("00", useCountMap00.get("00")
									+ StringUtil.getIntegerValue(entry.getValue()));
						}
					}
				}
			}
			if (totalCountMap != null && !totalCountMap.isEmpty())
			{
				for (Map.Entry<String, String> entry : totalCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						totalCountMap00.put(parent_id, totalCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						totalCountMap00.put(
								"00",
								totalCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						totalCountMap00.put(
								entry.getKey(),
								totalCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							totalCountMap00.put("00", totalCountMap00.get("00")
									+ StringUtil.getIntegerValue(entry.getValue()));
						}
					}
				}
			}
			for (String city : cityIds)
			{
				use_count = useCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				unUse_count = total_count - use_count;
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				useCount = stringAppend(StringUtil.getStringValue(use_count), useCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				unUseCount = stringAppend(StringUtil.getStringValue(unUse_count), unUseCount)
						.append(",");
			}
		}
		else
		{
			totalCount = new StringBuffer("[\"totalCount\",");
			useCount = new StringBuffer("[\"useCount\",");
			unUseCount = new StringBuffer("[\"unUseCount\",");
			replaceFlg = true;
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			cityName = stringAppend(city_nameSum, cityName).append(",");
			// 根据city_id 获取其与其子属地的回收终端使用数
			Map<String, String> useCountMap = dao.getRecycleDevCount(cityId, starttime, endtime);
			// 根据city_id 获取其与其子属地的回收终端总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime, endtime);
			// 获取属地是地市那一级的数量(使用 未使用 和 总数)
			useCount_all = StringUtil.getIntValue(useCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			unUseCount_all = totalCount_all - useCount_all;
			for (String city : cityIds)
			{
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				// 计算区县的数量(使用 未使用 和 总数)
				use_count = StringUtil.getIntValue(useCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				unUse_count = total_count - use_count;
				useCount = stringAppend(StringUtil.getStringValue(use_count), useCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				unUseCount = stringAppend(StringUtil.getStringValue(unUse_count), unUseCount)
						.append(",");
				// 计算全地市的数量(使用 未使用 和 总数)
				useCount_all = useCount_all + use_count;
				unUseCount_all = unUseCount_all + unUse_count;
				totalCount_all = totalCount_all + total_count;
			}
		}
		if (cityName.toString().endsWith(","))
		{
			cityName = cityName.deleteCharAt(cityName.length() - 1);
		}
		if (totalCount.toString().endsWith(","))
		{
			totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
		}
		if (useCount.toString().endsWith(","))
		{
			useCount = useCount.deleteCharAt(useCount.length() - 1);
		}
		if (unUseCount.toString().endsWith(","))
		{
			unUseCount = unUseCount.deleteCharAt(unUseCount.length() - 1);
		}
		cityName.append("]");
		totalCount.append("]");
		useCount.append("]");
		unUseCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"use\":");
		json.append(useCount);
		json.append(",");
		json.append("\"unUse\":");
		json.append(unUseCount);
		json.append("}");
		if (replaceFlg)
		{
			return json.toString()
					.replace("totalCount", StringUtil.getStringValue(totalCount_all))
					.replace("useCount", StringUtil.getStringValue(useCount_all))
					.replace("unUseCount", StringUtil.getStringValue(unUseCount_all));
		}
		else
		{
			return json.toString();
		}
	
	}
	
	/**
	 * 导出清单
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseDetail(String cityId, String starttime, String endtime )
	{
		return dao.parseDetail(cityId, starttime, endtime);
	}	
	
	/**
	 * 
	 * @author 岩 
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String cityId, String starttime, String endtime)
	{
		// 计算全地市 数量用(非"00"时使用)
		int useCount_all = 0;
		int unUseCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int use_count = 0;
		int total_count = 0;
		int unUse_count = 0;
		List<Map> list = new ArrayList<Map>();
		List<String> cityIds = new ArrayList<String>();
		
		//当前选择为省中心时
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> useCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				useCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的自动核销数
			Map<String, String> useCountMap = dao.getRecycleDevCountFor00( starttime, endtime);
			// 全省时 求出各个cityId对应的核销总数
			Map<String, String> totalCountMap = dao.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (useCountMap != null && !useCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : useCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						useCountMap00.put(parent_id, useCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						useCountMap00.put( "00", useCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						useCountMap00.put( entry.getKey(), useCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							useCountMap00.put("00", useCountMap00.get("00")
									+ StringUtil.getIntegerValue(entry.getValue()));
						}
					}
				}
			}
			if (totalCountMap != null && !totalCountMap.isEmpty())
			{
				for (Map.Entry<String, String> entry : totalCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						totalCountMap00.put(parent_id, totalCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						totalCountMap00.put("00", totalCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						totalCountMap00.put( entry.getKey(), totalCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							totalCountMap00.put("00", totalCountMap00.get("00")
									+ StringUtil.getIntegerValue(entry.getValue()));
						}
					}
				}
			}
			for (String city : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				use_count = useCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				unUse_count = total_count - use_count;
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("usecount", StringUtil.getStringValue(use_count));
				map.put("unUsecount", StringUtil.getStringValue(unUse_count));
				map.put("userate", getPercent(use_count, total_count));
				map.put("unUserate", getPercent(unUse_count, total_count));
				list.add(map);
			}
		}
		//当前选择不为省中心时
		else
		{
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			// 根据city_id 获取其与其子属地的自动核销数
			Map<String, String> useCountMap = dao.getRecycleDevCount(cityId,
					starttime, endtime);
			// 根据city_id 获取其与其子属地的核销总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime,
					endtime);
			// 获取属地是地市那一级的数量(自动 人工 和 总核销)
			useCount_all = StringUtil.getIntValue(useCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			unUseCount_all = totalCount_all - useCount_all;
			for (String city : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				// 计算区县的数量(自动 人工 和 总核销)
				use_count = StringUtil.getIntValue(useCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				unUse_count = total_count - use_count;
				// 计算全地市的数量(自动 人工 和 总核销)
				useCount_all = useCount_all + use_count;
				unUseCount_all = unUseCount_all + unUse_count;
				totalCount_all = totalCount_all + total_count;
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("usecount", StringUtil.getStringValue(use_count));
				map.put("unUsecount", StringUtil.getStringValue(unUse_count));
				map.put("userate", getPercent(use_count, total_count));
				map.put("unUserate", getPercent(unUse_count, total_count));
				list.add(map);
			}
			// 地市 如全苏州 单独计算
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("city", city_nameSum);
			map1.put("totalcount", StringUtil.getStringValue(totalCount_all));
			map1.put("usecount", StringUtil.getStringValue(useCount_all));
			map1.put("unUsecount", StringUtil.getStringValue(unUseCount_all));
			map1.put("userate", getPercent(useCount_all, totalCount_all));
			map1.put("unUserate", getPercent(unUseCount_all, totalCount_all));
			list.add(map1);
		}
		return list;
	}


	/**
	 * 获取二级地市对应map
	 */
	public static Map<String, String> getSencondCityIdCityNameMap()
	{
		CityDAO cityAct = new CityDAO();
		// 取所有属地的ID
		return cityAct.getSencondCityIdCityNameMap();
	}

	/**
	 * 字符串拼接工具方法
	 */
	private StringBuffer stringAppend(String str, StringBuffer tmp)
	{
		tmp.append("\"");
		tmp.append(str);
		tmp.append("\"");
		return tmp;
	}
	
	/**
	 * 统计百分比工具方法
	 * 
	 * @return
	 */
	private String getPercent(int x, int total)
	{
		if (total == 0)
		{
			return "0%";
		}
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		return numberFormat.format((float) x / (float) total * 100) + "%";
	}
	
	public void setDao(RecycleDevRateDAO dao)
	{
		this.dao = dao;
	}

	public int getQueryCount()
	{
		return dao.getQueryCount();
	}
}
