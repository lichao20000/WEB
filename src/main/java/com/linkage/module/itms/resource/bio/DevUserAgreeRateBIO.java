
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
import com.linkage.module.itms.resource.dao.DevUserAgreeRateDAO;

/**
 * 回收终端使用率
 * 
 * @author 岩 (Ailk No.)
 * @version 1.0
 * @since 2016-4-26
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DevUserAgreeRateBIO
{

	/** dao */
	private DevUserAgreeRateDAO dao;

	/**
	 * 获取回收终端使用率，调用dao内的方法获取回收终端数以及回收终端使用数 根据city_id是否为00做相应的处理
	 * 
	 * @author 岩
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public String DevUserAgreeRate(String cityId, String starttime, String endtime,
			String userSpecId, String deviceSpecId)
	{
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		// 回收终端总数 用Buffer
		StringBuffer totalCount = new StringBuffer("[");
		// 回收终端使用数 用Buffer
		StringBuffer disAgreeCount = new StringBuffer("[");
		// 回收终端未使用数 用Buffer
		StringBuffer agreeCount = new StringBuffer("[");
		// 计算全地市 数量用(非"00"时使用)
		int disAgreeCount_all = 0;
		int agreeCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int disAgree_count = 0;
		int total_count = 0;
		int agree_count = 0;
		List<String> cityIds = new ArrayList<String>();
		boolean replaceFlg = false;
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> disAgreeCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				disAgreeCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的回收终端使用数
			Map<String, String> disAgreeCountMap = dao.getDevAgreeCountFor00(starttime,
					endtime, userSpecId, deviceSpecId);
			// 全省时 求出各个cityId对应的回收终端总数
			Map<String, String> totalCountMap = dao
					.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (disAgreeCountMap != null && !disAgreeCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : disAgreeCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						disAgreeCountMap00.put(parent_id, disAgreeCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						disAgreeCountMap00.put(
								"00",
								disAgreeCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						disAgreeCountMap00.put(
								entry.getKey(),
								disAgreeCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							disAgreeCountMap00
									.put("00",
											disAgreeCountMap00.get("00")
													+ StringUtil.getIntegerValue(entry
															.getValue()));
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
				disAgree_count = disAgreeCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				agree_count = total_count - disAgree_count;
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				disAgreeCount = stringAppend(StringUtil.getStringValue(disAgree_count), disAgreeCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				agreeCount = stringAppend(StringUtil.getStringValue(agree_count),
						agreeCount).append(",");
			}
		}
		else
		{
			totalCount = new StringBuffer("[\"totalCount\",");
			disAgreeCount = new StringBuffer("[\"disAgreeCount\",");
			agreeCount = new StringBuffer("[\"agreeCount\",");
			replaceFlg = true;
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			cityName = stringAppend(city_nameSum, cityName).append(",");
			// 根据city_id 获取其与其子属地的回收终端使用数
			Map<String, String> disAgreeCountMap = dao.getDevAgreeCount(cityId, starttime,
					endtime, userSpecId, deviceSpecId);
			// 根据city_id 获取其与其子属地的回收终端总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime,
					endtime);
			// 获取属地是地市那一级的数量(使用 未使用 和 总数)
			disAgreeCount_all = StringUtil.getIntValue(disAgreeCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			agreeCount_all = totalCount_all - disAgreeCount_all;
			for (String city : cityIds)
			{
				cityName = stringAppend(Global.G_CityId_CityName_Map.get(city), cityName)
						.append(",");
				// 计算区县的数量(使用 未使用 和 总数)
				disAgree_count = StringUtil.getIntValue(disAgreeCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				agree_count = total_count - disAgree_count;
				disAgreeCount = stringAppend(StringUtil.getStringValue(disAgree_count), disAgreeCount)
						.append(",");
				totalCount = stringAppend(StringUtil.getStringValue(total_count),
						totalCount).append(",");
				agreeCount = stringAppend(StringUtil.getStringValue(agree_count),
						agreeCount).append(",");
				// 计算全地市的数量(使用 未使用 和 总数)
				disAgreeCount_all = disAgreeCount_all + disAgree_count;
				agreeCount_all = agreeCount_all + agree_count;
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
		if (disAgreeCount.toString().endsWith(","))
		{
			disAgreeCount = disAgreeCount.deleteCharAt(disAgreeCount.length() - 1);
		}
		if (agreeCount.toString().endsWith(","))
		{
			agreeCount = agreeCount.deleteCharAt(agreeCount.length() - 1);
		}
		cityName.append("]");
		totalCount.append("]");
		disAgreeCount.append("]");
		agreeCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"disAgree\":");
		json.append(disAgreeCount);
		json.append(",");
		json.append("\"agree\":");
		json.append(agreeCount);
		json.append("}");
		if (replaceFlg)
		{
			return json.toString()
					.replace("totalCount", StringUtil.getStringValue(totalCount_all))
					.replace("disAgreeCount", StringUtil.getStringValue(disAgreeCount_all))
					.replace("agreeCount", StringUtil.getStringValue(agreeCount_all));
		}
		else
		{
			return json.toString();
		}
	}

	/**
	 * 导出清单
	 * 
	 * @author 岩
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseDetail(String cityId, String starttime, String endtime,
			String userSpecId, String deviceSpecId)
	{
		return dao.parseDetail(cityId, starttime, endtime, userSpecId, deviceSpecId);
	}

	/**
	 * @author 岩
	 * @date 2016-4-26
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String cityId, String starttime, String endtime,
			String userSpecId, String deviceSpecId)
	{
		// 计算全地市 数量用(非"00"时使用)
		int disAgreeCount_all = 0;
		int agreeCount_all = 0;
		int totalCount_all = 0;
		// 单独地市 或 区县用
		int disAgree_count = 0;
		int total_count = 0;
		int agree_count = 0;
		List<Map> list = new ArrayList<Map>();
		List<String> cityIds = new ArrayList<String>();
		// 当前选择为省中心时
		if ("00".equals(cityId))
		{
			// 查询属地的下一级子属地(包含自己)
			cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
			Collections.sort(cityIds);
			Map<String, Integer> disAgreeCountMap00 = new TreeMap<String, Integer>();
			Map<String, Integer> totalCountMap00 = new TreeMap<String, Integer>();
			for (String city : cityIds)
			{
				disAgreeCountMap00.put(city, 0);
				totalCountMap00.put(city, 0);
			}
			// 全省时 求出各个cityId对应的规格一致数数
			Map<String, String> disAgreeCountMap = dao.getDevAgreeCountFor00(starttime,
					endtime, userSpecId, deviceSpecId);
			// 全省时 求出各个cityId对应的核销总数
			Map<String, String> totalCountMap = dao
					.getTotalCountFor00(starttime, endtime);
			// 父属地
			String parent_id = "";
			if (disAgreeCountMap != null && !disAgreeCountMap.isEmpty())
			{
				// 此种方法遍历map 必须判空 否则会报空指针异常
				for (Map.Entry<String, String> entry : disAgreeCountMap.entrySet())
				{
					// 若city_id是三级属地
					if (!cityIds.contains(entry.getKey()))
					{
						// 获取其父属地
						parent_id = CityDAO.getLocationCityIdByCityId(entry.getKey());
						// 父属地 数量叠加
						disAgreeCountMap00.put(parent_id, disAgreeCountMap00.get(parent_id)
								+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						disAgreeCountMap00.put(
								"00",
								disAgreeCountMap00.get("00")
										+ StringUtil.getIntegerValue(entry.getValue()));
					}
					// 若city_id是二级属地或者 省中心
					else
					{
						disAgreeCountMap00.put(
								entry.getKey(),
								disAgreeCountMap00.get(entry.getKey())
										+ StringUtil.getIntegerValue(entry.getValue()));
						// 全省的 数量叠加
						if (!"00".equals(entry.getKey()))
						{
							disAgreeCountMap00.put("00", disAgreeCountMap00.get("00")
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
				disAgree_count = disAgreeCountMap00.get(city);
				total_count = totalCountMap00.get(city);
				agree_count = total_count - disAgree_count;
				map.put("totalcount", StringUtil.getStringValue(total_count));
				map.put("disAgreecount", StringUtil.getStringValue(disAgree_count));
				map.put("agreecount", StringUtil.getStringValue(agree_count));
				map.put("disAgreerate", getPercent(disAgree_count, total_count));
				map.put("agreerate", getPercent(agree_count, total_count));
				list.add(map);
			}
		}
		// 当前选择不为省中心时
		else
		{
			cityIds = CityDAO.getNextCityIdsByCityPidCore(cityId);
			String city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
			// 根据city_id 获取其与其子属地的规格一致数数
			Map<String, String> disAgreeCountMap = dao.getDevAgreeCount(cityId, starttime,
					endtime, userSpecId, deviceSpecId);
			// 根据city_id 获取其与其子属地的核销总数
			Map<String, String> totalCountMap = dao.getTotalCount(cityId, starttime,
					endtime);
			// 获取属地是地市那一级的数量(自动 人工 和 总核销)
			disAgreeCount_all = StringUtil.getIntValue(disAgreeCountMap, cityId);
			totalCount_all = StringUtil.getIntValue(totalCountMap, cityId);
			agreeCount_all = totalCount_all - disAgreeCount_all;
			for (String city : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				// 计算区县的数量(自动 人工 和 总核销)
				disAgree_count = StringUtil.getIntValue(disAgreeCountMap, city);
				total_count = StringUtil.getIntValue(totalCountMap, city);
				agree_count = total_count - disAgree_count;
				// 计算全地市的数量(自动 人工 和 总核销)
				disAgreeCount_all = disAgreeCount_all + disAgree_count;
				agreeCount_all = agreeCount_all + agree_count;
				totalCount_all = totalCount_all + total_count;
				map.put("city", Global.G_CityId_CityName_Map.get(city));
				map.put("totalcount", StringUtil.getStringValue(total_count ));
				map.put("disAgreecount", StringUtil.getStringValue(disAgree_count));
				map.put("agreecount", StringUtil.getStringValue(agree_count));
				map.put("disAgreerate", getPercent(disAgree_count, total_count));
				map.put("agreerate", getPercent(agree_count, total_count));
				list.add(map);
			}
			// 地市 如全disAgree 单独计算
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("city", city_nameSum);
			map1.put("totalcount", StringUtil.getStringValue(totalCount_all));
			map1.put("disAgreecount", StringUtil.getStringValue(disAgreeCount_all));
			map1.put("agreecount", StringUtil.getStringValue(agreeCount_all));
			map1.put("disAgreerate", getPercent(disAgreeCount_all, totalCount_all));
			map1.put("agreerate", getPercent(agreeCount_all, totalCount_all));
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
	 * 获取规格表字段
	 * 页面下拉框显示部分
	 * @author 岩 
	 * @date 2016-6-20
	 * @return
	 */
	public List<HashMap<String, String>> getSpecName()
	{
		// 取所有属地的ID
		return dao.getSpecName();
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

	public void setDao(DevUserAgreeRateDAO dao)
	{
		this.dao = dao;
	}

	public int getQueryCount()
	{
		return dao.getQueryCount();
	}
}
