
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
import com.linkage.module.itms.resource.dao.DevWriteOffRateDAO;

/**
 * @author Reno (Ailk No.)
 * @version 1.0
 * @since 2016年4月11日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevWriteOffRateBIO
{

	/** dao */
	private DevWriteOffRateDAO dao;

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
	 * 核销率撞撞图和饼图
	 * 
	 * @param cityId
	 *            属地ID
	 * @param starttime
	 *            核销起始时间
	 * @param endtime
	 *            核销结束时间
	 * @return json字符串
	 */
	public String writeOffRate(String cityId, String starttime, String endtime)
	{
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		// 核销总数用Buffer
		StringBuffer totalCount = new StringBuffer("[");
		// 自动核销数用Buffer
		StringBuffer autoCount = new StringBuffer("[");
		// 人工核销数用Buffer
		StringBuffer manCount = new StringBuffer("[");
		// 计算全地市 数量用(非"00"时使用)
		int autoCount_all = 0;
		int manCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int auto_count = 0;
		int total_count = 0;
		int man_count = 0;
		List<String> cityIds = new ArrayList<String>();
		boolean replaceFlg = false;
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> autoCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				autoCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的自动核销数
			Map<String, String> autoCountMap = dao.getWriteOffCountFor00("1", starttime,
					endtime);
			// 全省时 求出各个cityId对应的核销总数
			Map<String, String> totalCountMap = dao
					.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (autoCountMap != null && !autoCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : autoCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						autoCountMap00.put(parent_id, autoCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						autoCountMap00.put(
								"00",
								autoCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						autoCountMap00.put(
								entry.getKey(),
								autoCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							autoCountMap00.put("00", autoCountMap00.get("00")
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
				auto_count = autoCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				man_count = total_count - auto_count;
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				autoCount = stringAppend(StringUtil.getStringValue(auto_count), autoCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				manCount = stringAppend(StringUtil.getStringValue(man_count), manCount)
						.append(",");
			}
		}
		else
		{
			totalCount = new StringBuffer("[\"totalCount\",");
			autoCount = new StringBuffer("[\"autoCount\",");
			manCount = new StringBuffer("[\"manCount\",");
			replaceFlg = true;
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			cityName = stringAppend(city_nameSum, cityName).append(",");
			// 根据city_id 获取其与其子属地的自动核销数
			Map<String, String> autoCountMap = dao.getWriteOffCount("1", cityId,
					starttime, endtime);
			// 根据city_id 获取其与其子属地的核销总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime,
					endtime);
			// 获取属地是地市那一级的数量(自动 人工 和 总核销)
			autoCount_all = StringUtil.getIntValue(autoCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			manCount_all = totalCount_all - autoCount_all;
			for (String city : cityIds)
			{
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				// 计算区县的数量(自动 人工 和 总核销)
				auto_count = StringUtil.getIntValue(autoCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				man_count = total_count - auto_count;
				autoCount = stringAppend(StringUtil.getStringValue(auto_count), autoCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				manCount = stringAppend(StringUtil.getStringValue(man_count), manCount)
						.append(",");
				// 计算全地市的数量(自动 人工 和 总核销)
				autoCount_all = autoCount_all + auto_count;
				manCount_all = manCount_all + man_count;
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
		if (autoCount.toString().endsWith(","))
		{
			autoCount = autoCount.deleteCharAt(autoCount.length() - 1);
		}
		if (manCount.toString().endsWith(","))
		{
			manCount = manCount.deleteCharAt(manCount.length() - 1);
		}
		cityName.append("]");
		totalCount.append("]");
		autoCount.append("]");
		manCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"auto\":");
		json.append(autoCount);
		json.append(",");
		json.append("\"man\":");
		json.append(manCount);
		json.append("}");
		if (replaceFlg)
		{
			return json.toString()
					.replace("totalCount", StringUtil.getStringValue(totalCount_all))
					.replace("autoCount", StringUtil.getStringValue(autoCount_all))
					.replace("manCount", StringUtil.getStringValue(manCount_all));
		}
		else
		{
			return json.toString();
		}
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
	 * 核销率导出报表
	 * 
	 * @param cityId
	 *            属地Id
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return 报表List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String cityId, String starttime, String endtime)
	{
		// 计算全地市 数量用(非"00"时使用)
		int autoCount_all = 0;
		int manCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int auto_count = 0;
		int total_count = 0;
		int man_count = 0;
		List<Map> list = new ArrayList<Map>();
		List<String> cityIds = new ArrayList<String>();
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> autoCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				autoCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的自动核销数
			Map<String, String> autoCountMap = dao.getWriteOffCountFor00("1", starttime,
					endtime);
			// 全省时 求出各个cityId对应的核销总数
			Map<String, String> totalCountMap = dao
					.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (autoCountMap != null && !autoCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : autoCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						autoCountMap00.put(parent_id, autoCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						autoCountMap00.put(
								"00",
								autoCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						autoCountMap00.put(
								entry.getKey(),
								autoCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							autoCountMap00.put("00", autoCountMap00.get("00")
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
				Map<String, String> map = new HashMap<String, String>();
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				auto_count = autoCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				man_count = total_count - auto_count;
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("autocount", StringUtil.getStringValue(auto_count));
				map.put("mancount", StringUtil.getStringValue(man_count));
				map.put("autorate", getPercent(auto_count, total_count));
				map.put("manrate", getPercent(man_count, total_count));
				list.add(map);
			}
		}
		else
		{
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			// 根据city_id 获取其与其子属地的自动核销数
			Map<String, String> autoCountMap = dao.getWriteOffCount("1", cityId,
					starttime, endtime);
			// 根据city_id 获取其与其子属地的核销总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime,
					endtime);
			// 获取属地是地市那一级的数量(自动 人工 和 总核销)
			autoCount_all = StringUtil.getIntValue(autoCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			manCount_all = totalCount_all - autoCount_all;
			for (String city : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				// 计算区县的数量(自动 人工 和 总核销)
				auto_count = StringUtil.getIntValue(autoCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				man_count = total_count - auto_count;
				// 计算全地市的数量(自动 人工 和 总核销)
				autoCount_all = autoCount_all + auto_count;
				manCount_all = manCount_all + man_count;
				totalCount_all = totalCount_all + total_count;
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("autocount", StringUtil.getStringValue(auto_count));
				map.put("mancount", StringUtil.getStringValue(man_count));
				map.put("autorate", getPercent(auto_count, total_count));
				map.put("manrate", getPercent(man_count, total_count));
				list.add(map);
			}
			// 地市 如全苏州 单独计算
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("city", city_nameSum);
			map1.put("totalcount", StringUtil.getStringValue(totalCount_all));
			map1.put("autocount", StringUtil.getStringValue(autoCount_all));
			map1.put("mancount", StringUtil.getStringValue(manCount_all));
			map1.put("autorate", getPercent(autoCount_all, totalCount_all));
			map1.put("manrate", getPercent(manCount_all, totalCount_all));
			list.add(map1);
		}
		return list;
	}

	/**
	 * 核销率导出清单
	 * 
	 * @param cityId
	 *            属地Id
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @param writeOffType
	 *            核销方式
	 * @return 清单List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseDetail(String cityId, String starttime, String endtime,
			String writeOffType)
	{
		return dao.parseDetail(cityId, starttime, endtime, writeOffType);
	}

	/**
	 * 更换率统计柱状图和饼图
	 * 
	 * @param cityId
	 *            属地Id
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return json字符串
	 */
	public String devChange(String cityId, String starttime, String endtime)
	{
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		// 设备更换总数用Buffer
		StringBuffer totalCount = new StringBuffer("[");
		// 规范更换数Buffer
		StringBuffer normalCount = new StringBuffer("[");
		// 不规范更换数Buffer
		StringBuffer noNormalCount = new StringBuffer("[");
		// 计算全地市 数量用(非"00"时使用)
		int normalCount_all = 0;
		int noNormalCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int normal_count = 0;
		int total_count = 0;
		int noNomor_count = 0;
		List<String> cityIds = new ArrayList<String>();
		boolean replaceFlg = false;
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> normalCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				normalCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的规范更换数
			Map<String, String> normalCountMap = dao
					.getDevChangeFor00(starttime, endtime);
			// 全省时 求出各个cityId对应的更换总数
			Map<String, String> totalCountMap = dao.getTotalChangeCountFor00(starttime,
					endtime);
			// 父属地
			String parent_id = "";
			if (normalCountMap != null && !normalCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : normalCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						normalCountMap00.put(parent_id, normalCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						normalCountMap00.put("00", normalCountMap00.get("00")
								+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						normalCountMap00.put(
								entry.getKey(),
								normalCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							normalCountMap00.put("00", normalCountMap00.get("00")
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
				normal_count = normalCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				noNomor_count = total_count - normal_count;
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				normalCount = stringAppend(StringUtil.getStringValue(normal_count),
						normalCount).append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				noNormalCount = stringAppend(StringUtil.getStringValue(noNomor_count),
						noNormalCount).append(",");
			}
		}
		else
		{
			totalCount = new StringBuffer("[\"totalCount\",");
			normalCount = new StringBuffer("[\"normalCount\",");
			noNormalCount = new StringBuffer("[\"noNormalCount\",");
			replaceFlg = true;
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			cityName = stringAppend(city_nameSum, cityName).append(",");
			// 根据city_id 获取其与其子属地的规范更换数
			Map<String, String> normalCountMap = dao.getDevChange(cityId, starttime,
					endtime);
			// 根据city_id 获取其与其子属地的更换总数
			Map<String, String> totalCountMap = dao.getDevChangeTotalCount(cityId,
					starttime, endtime);
			// 获取属地是地市那一级的数量(规范 不规范 和 总数)
			normalCount_all = StringUtil.getIntValue(normalCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			noNormalCount_all = totalCount_all - normalCount_all;
			for (String city : cityIds)
			{
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				// 计算区县的数量(自动 人工 和 总核销)
				normal_count = StringUtil.getIntValue(normalCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				noNomor_count = total_count - normal_count;
				normalCount = stringAppend(StringUtil.getStringValue(normal_count),
						normalCount).append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				noNormalCount = stringAppend(StringUtil.getStringValue(noNomor_count),
						noNormalCount).append(",");
				// 计算全地市的数量(自动 人工 和 总核销)
				normalCount_all = normalCount_all + normal_count;
				noNormalCount_all = noNormalCount_all + noNomor_count;
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
		if (normalCount.toString().endsWith(","))
		{
			normalCount = normalCount.deleteCharAt(normalCount.length() - 1);
		}
		if (noNormalCount.toString().endsWith(","))
		{
			noNormalCount = noNormalCount.deleteCharAt(noNormalCount.length() - 1);
		}
		cityName.append("]");
		totalCount.append("]");
		normalCount.append("]");
		noNormalCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"normal\":");
		json.append(normalCount);
		json.append(",");
		json.append("\"nonormal\":");
		json.append(noNormalCount);
		json.append("}");
		if (replaceFlg)
		{
			return json
					.toString()
					.replace("totalCount", StringUtil.getStringValue(totalCount_all))
					.replace("normalCount", StringUtil.getStringValue(normalCount_all))
					.replace("noNormalCount",
							StringUtil.getStringValue(noNormalCount_all));
		}
		else
		{
			return json.toString();
		}
	}

	/**
	 * 更换率不规范清单
	 * 
	 * @param cityId
	 *            属地Id
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return 清单List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseChangeDetail(String cityId, String starttime, String endtime)
	{
		return dao.parseChangeDetail(cityId, starttime, endtime);
	}

	
	/**
	 * 更换率全量清单
	 * @author 岩 
	 * @date 2016-8-17
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseChangeAllDetail(String cityId, String starttime, String endtime)
	{
		return dao.parseChangeAllDetail(cityId, starttime, endtime);
	}
	/**
	 * 更换率报表
	 * 
	 * @param cityId
	 *            属地Id
	 * @param starttime
	 *            开始时间
	 * @param endtime
	 *            结束时间
	 * @return 报表List
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseChangeExcel(String cityId, String starttime, String endtime)
	{
		// 计算全地市 数量用(非"00"时使用)
		int normalCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int normarcount = 0;
		int total_count = 0;
		List<Map> list = new ArrayList<Map>();
		List<String> cityIds = new ArrayList<String>();
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> normalCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				normalCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的规范更换数
			Map<String, String> normalCountMap = dao
					.getDevChangeFor00(starttime, endtime);
			// 全省时 求出各个cityId对应的总数
			Map<String, String> totalCountMap = dao.getTotalChangeCountFor00(starttime,
					endtime);
			// 父属地
			String parent_id = "";
			if (normalCountMap != null && !normalCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : normalCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						normalCountMap00.put(parent_id, normalCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						normalCountMap00.put("00", normalCountMap00.get("00")
								+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						normalCountMap00.put(
								entry.getKey(),
								normalCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							normalCountMap00.put("00", normalCountMap00.get("00")
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
				Map<String, String> map = new HashMap<String, String>();
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				normarcount = normalCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("normalcount", StringUtil.getStringValue(normarcount));
				map.put("normalrate", getPercent(normarcount, total_count));
				list.add(map);
			}
		}
		else
		{
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			// 根据city_id 获取其与其子属地的规范数
			Map<String, String> normalCountMap = dao.getDevChange(cityId, starttime,
					endtime);
			// 根据city_id 获取其与其子属地的总数
			Map<String, String> totalCountMap = dao.getDevChangeTotalCount(cityId,
					starttime, endtime);
			// 获取属地是地市那一级的数量(规范 和 总核)
			normalCount_all = StringUtil.getIntValue(normalCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			for (String city : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				// 计算区县的数量(自动 人工 和 总核销)
				normarcount = StringUtil.getIntValue(normalCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				// 计算全地市的数量(自动 人工 和 总核销)
				normalCount_all = normalCount_all + normarcount;
				totalCount_all = totalCount_all + total_count;
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("normalcount", StringUtil.getStringValue(normarcount));
				map.put("normalrate", getPercent(normarcount, total_count));
				list.add(map);
			}
			// 地市 如全苏州 单独计算
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("city", city_nameSum);
			map1.put("totalcount", StringUtil.getStringValue(totalCount_all));
			map1.put("normalcount", StringUtil.getStringValue(normalCount_all));
			map1.put("normalrate", getPercent(normalCount_all, totalCount_all));
			list.add(map1);
		}
		return list;
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

	public void setDao(DevWriteOffRateDAO dao)
	{
		this.dao = dao;
	}
}
