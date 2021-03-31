
package com.linkage.module.itms.resource.bio;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.DevRepairTestInfoDAO;
import com.linkage.module.itms.resource.obj.RepairDevObj;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年10月21日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept
 */
public class DevRepairTestInfoBIO
{

	private static Logger logger = LoggerFactory.getLogger(DevRepairTestInfoBIO.class);
	/** dao */
	private DevRepairTestInfoDAO dao;

	/**
	 * 获取返修厂商
	 * 
	 * @return list
	 */
	public List<Map<String, String>> getVendor()
	{
		return dao.getVendor();
	}

	/**
	 * 获取批次
	 * 
	 * @return list
	 */
	public List<Map<String, String>> getBatchNum()
	{
		return dao.getBatchNum();
	}

	/**
	 * 根据厂商生成各地市使用率柱状图
	 * 
	 * @param vendor_name
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @param cityId
	 * @return String
	 * @throws Exception
	 */
	public String getUseRateDataByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getUseRateDataByVendor()");
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer istestCount = new StringBuffer("[");
		StringBuffer bindCount = new StringBuffer("[");
		StringBuffer useCount = new StringBuffer("[");
		StringBuffer noUseCount = new StringBuffer("[");
		List<String> cityIds = new ArrayList<String>();
		// 各属地终端总数集合
		List<Map<String, String>> totalList = dao.getUseRateBarTotalCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 各属地到货测试数量集合
		List<Map<String, String>> isTestList = dao.getUseRateBarIsTestCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 各属地绑定时间为空数量集合
		List<Map<String, String>> nullBindList = dao.getUseRateBarNullBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 各属地使用数集合
		List<Map<String, String>> noUseCountList = dao.getNoUseCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 省中心返修终端数量
		long total_countSum = 0;
		// 省中心到货测试数量
		long istest_countSum = 0;
		// 省中心绑定时间为空数量
		long bind_countSum = 0;
		// 省中心
		String city_nameSum = "";
		// 省中心未使用数
		long noUse_countSum = 0;
		if ("00".equals(cityId))
		{
			city_nameSum = "省中心";
		}
		else
		{
			city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
		}
		if (totalList != null && totalList.size() > 0)
		{
			String city_id = "";
			// 返修终端数量
			long total_count = 0;
			// 到货测试数量
			long istest_count = 0;
			// 绑定时间为空数量
			long bind_count = 0;
			// 未使用数
			long noUse_count = 0;
			for (int i = 0; i < totalList.size(); i++)
			{
				logger.debug("getUseRateDataByVendor() ===> 获取到数据");
				city_id = StringUtil.getStringValue(totalList.get(i), "city_id");
				if (null != city_id && !"-1".equals(city_id))
				{
					total_count = StringUtil
							.getLongValue(totalList.get(i), "total_count");
					istest_count = StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					bind_count = StringUtil.getLongValue(nullBindList.get(i),
							"nullbindtime_count");
					noUse_count = StringUtil.getLongValue(noUseCountList.get(i),
							"nouse_count");
				}
				total_countSum += total_count;
				istest_countSum += istest_count;
				bind_countSum += bind_count;
				noUse_countSum += noUse_count;
			}
			cityName = stringAppend(city_nameSum, cityName).append(",");
			totalCount = stringAppend(total_countSum + "", totalCount).append(",");
			istestCount = stringAppend(istest_countSum + "", istestCount).append(",");
			bindCount = stringAppend(bind_countSum + "", bindCount).append(",");
			useCount = stringAppend(total_countSum - noUse_countSum + "", useCount)
					.append(",");
			noUseCount = stringAppend(noUse_countSum + "", noUseCount).append(",");
			city_nameSum = "";
		}
		if (totalList != null && totalList.size() > 0)
		{
			for (int i = 0; i < totalList.size(); i++)
			{
				logger.debug("getUseRateDataByCity() ===> 获取到数据");
				String city_id = "";
				String city_name = "";
				// 返修终端数量
				long total_count = 0;
				// 到货测试数量
				long istest_count = 0;
				// 绑定时间为空数量
				long bind_count = 0;
				// 使用数
				long use_count = 0;
				// 未使用数
				long noUse_count = 0;
				city_id = StringUtil.getStringValue(totalList.get(i), "city_id");
				if (null != city_id && !"-1".equals(city_id))
				{
					total_count += StringUtil.getLongValue(totalList.get(i),
							"total_count");
					istest_count += StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					bind_count += StringUtil.getLongValue(nullBindList.get(i),
							"nullbindtime_count");
					noUse_count += StringUtil.getLongValue(noUseCountList.get(i),
							"nouse_count");
					use_count += StringUtil.getLongValue(total_count - noUse_count);
					city_name = Global.G_CityId_CityName_Map.get(city_id);
					cityName = stringAppend(city_name, cityName).append(",");
					totalCount = stringAppend(total_count + "", totalCount).append(",");
					istestCount = stringAppend(istest_count + "", istestCount)
							.append(",");
					bindCount = stringAppend(bind_count + "", bindCount).append(",");
					useCount = stringAppend(use_count + "", useCount).append(",");
					noUseCount = stringAppend(noUse_count + "", noUseCount).append(",");
					city_name = "";
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
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (useCount.toString().endsWith(","))
			{
				useCount = useCount.deleteCharAt(useCount.length() - 1);
			}
			if (noUseCount.toString().endsWith(","))
			{
				noUseCount = noUseCount.deleteCharAt(noUseCount.length() - 1);
			}
		}
		else
		{
			logger.debug("getUseRateDataByCity() ===> 未获取到数据");
			Collections.sort(cityIds);
			String total_countStr = "0";
			String bind_countStr = "0";
			String istest_countStr = "0";
			String use_countStr = "0";
			String noUse_countStr = "0";
			String city_name = "";
			for (String city_id : cityIds)
			{
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name, cityName).append(",");
				totalCount = stringAppend(total_countStr, totalCount).append(",");
				istestCount = stringAppend(istest_countStr, istestCount).append(",");
				bindCount = stringAppend(bind_countStr, bindCount).append(",");
				useCount = stringAppend(use_countStr, useCount).append(",");
				noUseCount = stringAppend(noUse_countStr, noUseCount).append(",");
				city_name = "";
			}
			if (cityName.toString().endsWith(","))
			{
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (useCount.toString().endsWith(","))
			{
				useCount = useCount.deleteCharAt(useCount.length() - 1);
			}
			if (noUseCount.toString().endsWith(","))
			{
				noUseCount = noUseCount.deleteCharAt(noUseCount.length() - 1);
			}
		}
		cityName.append("]");
		totalCount.append("]");
		istestCount.append("]");
		bindCount.append("]");
		useCount.append("]");
		noUseCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"istest\":");
		json.append(istestCount);
		json.append(",");
		json.append("\"bind\":");
		json.append(bindCount);
		json.append(",");
		json.append("\"use\":");
		json.append(useCount);
		json.append(",");
		json.append("\"noUse\":");
		json.append(noUseCount);
		json.append("}");
		return json.toString();
	}

	/**
	 * 根据属地生成各厂商使用率柱状图
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return String
	 */
	public String getUseRateDataByCity(String cityId, String starttime, String endtime,
			String batchNum)
	{
		logger.debug("DevRepairTestInfoBIO ====> getUseRateDataByCity()");
		StringBuffer json = new StringBuffer("{\"vendors\":");
		StringBuffer vendorName = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer istestCount = new StringBuffer("[");
		StringBuffer bindCount = new StringBuffer("[");
		StringBuffer useCount = new StringBuffer("[");
		StringBuffer noUseCount = new StringBuffer("[");
		List<String> vendorIds = new ArrayList<String>();
		// 总终端数
		List<Map<String, String>> totalList = dao.getUseRateBarTotalCountByCity(cityId,
				starttime, endtime, batchNum);
		// 到货测试数
		List<Map<String, String>> isTestList = dao.getUseRateBarIsTestCountByCity(cityId,
				starttime, endtime, batchNum);
		// 绑定时间为空数
		List<Map<String, String>> nullBindList = dao.getUseRateBarNullBindCountByCity(
				cityId, starttime, endtime, batchNum);
		// 使用数
		List<Map<String, String>> noUseCountList = dao.getNoUseCountByCity(cityId,
				starttime, endtime, batchNum);
		Map<String, String> vendorMap = getRepairDevMap();
		if (totalList != null && totalList.size() > 0)
		{
			logger.debug("getUseRateDataByVendor() ===> 获取到数据");
			for (int i = 0; i < totalList.size(); i++)
			{
				String vendor_id = "";
				String vendor_name = "";
				// 返修终端数量
				long total_count = 0;
				// 到货测试数量
				long istest_count = 0;
				// 绑定时间为空数量
				long bind_count = 0;
				// 未使用数
				long noUse_count = 0;
				vendor_id = StringUtil.getStringValue(totalList.get(i), "vendor_id");
				if (null != vendor_id && !"-1".equals(vendor_id))
				{
					total_count += StringUtil.getLongValue(totalList.get(i),
							"total_count");
					istest_count += StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					bind_count += StringUtil.getLongValue(nullBindList.get(i),
							"nullbindtime_count");
					noUse_count += StringUtil.getLongValue(noUseCountList.get(i),
							"nouse_count");
					vendor_name = vendorMap.get(vendor_id);
					vendorName = stringAppend(vendor_name, vendorName).append(",");
					totalCount = stringAppend(total_count + "", totalCount).append(",");
					istestCount = stringAppend(istest_count + "", istestCount)
							.append(",");
					bindCount = stringAppend(bind_count + "", bindCount).append(",");
					useCount = stringAppend(total_count - noUse_count + "", useCount)
							.append(",");
					noUseCount = stringAppend(noUse_count + "", noUseCount).append(",");
					vendor_name = "";
				}
			}
			if (vendorName.toString().endsWith(","))
			{
				vendorName = vendorName.deleteCharAt(vendorName.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (useCount.toString().endsWith(","))
			{
				useCount = useCount.deleteCharAt(useCount.length() - 1);
			}
			if (noUseCount.toString().endsWith(","))
			{
				noUseCount = noUseCount.deleteCharAt(noUseCount.length() - 1);
			}
		}
		else
		{
			logger.debug("getUseRateDataByVendor() ===> 未获取到数据");
			Collections.sort(vendorIds);
			String total_countStr = "0";
			String bind_countStr = "0";
			String istest_countStr = "0";
			String use_countStr = "0";
			String noUse_countStr = "0";
			String vendor_name = "";
			for (String vendor_id : vendorIds)
			{
				vendor_name = vendorMap.get(vendor_id);
				vendorName = stringAppend(vendor_name, vendorName).append(",");
				totalCount = stringAppend(total_countStr, totalCount).append(",");
				istestCount = stringAppend(istest_countStr, istestCount).append(",");
				bindCount = stringAppend(bind_countStr, bindCount).append(",");
				useCount = stringAppend(use_countStr, useCount).append(",");
				noUseCount = stringAppend(noUse_countStr, noUseCount).append(",");
				vendor_name = "";
			}
			if (vendorName.toString().endsWith(","))
			{
				vendorName = vendorName.deleteCharAt(vendorName.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (useCount.toString().endsWith(","))
			{
				useCount = useCount.deleteCharAt(useCount.length() - 1);
			}
			if (noUseCount.toString().endsWith(","))
			{
				noUseCount = noUseCount.deleteCharAt(noUseCount.length() - 1);
			}
		}
		vendorName.append("]");
		totalCount.append("]");
		istestCount.append("]");
		bindCount.append("]");
		useCount.append("]");
		noUseCount.append("]");
		json.append(vendorName);
		json.append(",");
		json.append("\"total\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"istest\":");
		json.append(istestCount);
		json.append(",");
		json.append("\"bind\":");
		json.append(bindCount);
		json.append(",");
		json.append("\"use\":");
		json.append(useCount);
		json.append(",");
		json.append("\"noUse\":");
		json.append(noUseCount);
		json.append("}");
		return json.toString();
	}

	/**
	 * 根据厂商展示使用率饼图
	 * 
	 * @param vendor_name
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return String
	 * @throws Exception
	 */
	public String getUseRatePieCountByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getUseRatePieCountByVendor({})");
		StringBuffer json = new StringBuffer();
		// 总终端数
		List<Map<String, String>> toalList = dao.getUseRatePieTotalCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 到货测试数
		List<Map<String, String>> isTestList = dao.getUseRatePieIsTestCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 绑定时间为空数
		List<Map<String, String>> nullBindList = dao.getUseRatePieNullBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		if (toalList != null && toalList.size() > 0)
		{
			logger.debug("DevRepairTestInfoBIO ====> getUseRatePieCountByVendor({}) ==获取到数据");
			// 返修终端数量
			String total_count = "";
			// 到货测试数量
			String istest_count = "";
			// 绑定时间为空数量
			String nullbindtime_count = "";
			for (int i = 0; i < toalList.size(); i++)
			{
				total_count = StringUtil.getStringValue(toalList.get(i), "total_count");
				istest_count = StringUtil.getStringValue(isTestList.get(i),
						"istest_count");
				nullbindtime_count = StringUtil.getStringValue(nullBindList.get(i),
						"nullbindtime_count");
				String tmps = getUseRatePieJson(total_count, istest_count,
						nullbindtime_count);
				json.append(tmps);
				json.append(",");
				total_count = "";
				istest_count = "";
				nullbindtime_count = "";
			}
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		else
		{
			logger.debug("DevRepairTestInfoBIO ====> getUseRatePieCountByVendor({}) ==未获取到数据");
			String total_count = "0";
			String istest_count = "0";
			String nullbindtime_count = "0";
			String tmps = getUseRatePieJson(total_count, istest_count, nullbindtime_count);
			json.append(tmps);
			json.append(",");
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		return json.toString();
	}

	/**
	 * 根据属地展示各厂商使用率饼图
	 * 
	 * @param vendor_name
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return String
	 * @throws Exception
	 */
	public String getUseRatePieCountByCity(String CityId, String starttime,
			String endtime, String batchNum) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getUseRatePieDataByCity({})");
		StringBuffer json = new StringBuffer();
		// 总终端数
		List<Map<String, String>> totalList = dao.getUseRatePieTotalCountByCity(CityId,
				starttime, endtime, batchNum);
		// 到货测试数
		List<Map<String, String>> isTestList = dao.getUseRatePieIsTestCountByCity(CityId,
				starttime, endtime, batchNum);
		// 绑定时间为空数
		List<Map<String, String>> nullBindList = dao.getUseRatePieNullBindCountByCity(
				CityId, starttime, endtime, batchNum);
		if (totalList != null && totalList.size() > 0)
		{
			logger.debug("DevRepairTestInfoBIO ====> getUseRatePieDataByCity({}) ==获取到数据");
			// 返修终端数量
			String total_count = "";
			// 到货测试数量
			String istest_count = "";
			// 绑定时间为空数量
			String nullbindtime_count = "";
			for (int i = 0; i < totalList.size(); i++)
			{
				total_count = StringUtil.getStringValue(totalList.get(i), "total_count");
				istest_count = StringUtil.getStringValue(isTestList.get(i),
						"istest_count");
				nullbindtime_count = StringUtil.getStringValue(nullBindList.get(i),
						"nullbindtime_count");
				String tmps = getUseRatePieJson(total_count, istest_count,
						nullbindtime_count);
				json.append(tmps);
				json.append(",");
				total_count = "";
				istest_count = "";
				nullbindtime_count = "";
			}
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		else
		{
			logger.debug("DevRepairTestInfoBIO ====> getUseRatePieDataByCity({}) ==未获取到数据");
			String total_count = "0";
			String istest_count = "0";
			String nullbindtime_count = "0";
			String tmps = getUseRatePieJson(total_count, istest_count, nullbindtime_count);
			json.append(tmps);
			json.append(",");
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		return json.toString();
	}

	/**
	 * 根据厂商生成各地市合格率柱状图
	 * 
	 * @param vendor_name
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @param cityId
	 * @return String
	 * @throws Exception
	 */
	public String getQualifiedRateByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getQualifiedRate()");
		StringBuffer json = new StringBuffer("{\"cities\":");
		StringBuffer cityName = new StringBuffer("[");
		StringBuffer qualifyCount = new StringBuffer("[");
		StringBuffer nobindCount = new StringBuffer("[");
		StringBuffer bindCount = new StringBuffer("[");
		StringBuffer addBussCount = new StringBuffer("[");
		StringBuffer istestCount = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer qualifiedCount = new StringBuffer("[");
		StringBuffer noQualifiedCount = new StringBuffer("[");
		List<String> cityIds = new ArrayList<String>();
		// 获取江苏省各地市
		cityIds = CityDAO.getNextCityIdsByCityPid(cityId);
		// 三月合格数量
		long qualify_countSum = 0;
		List<Map<String, String>> qualifyList = dao.getQualifiedCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 终端状态解绑数量
		long nobind_countSum = 0;
		List<Map<String, String>> noBindList = dao.getQualifiedNoBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 终端状态绑定数量
		long bind_countSum = 0;
		List<Map<String, String>> bindList = dao.getQualifiedBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 增加业务数量
		long addBuss_countSum = 0;
		List<Map<String, String>> addBussList = dao.getQualifiedAddBussCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 到货测试为是数量
		long istest_countSum = 0;
		List<Map<String, String>> isTestList = dao.getQualifiedIsTestCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 不合格数
		long noQualified_countSum = 0;
		List<Map<String, String>> noQualifiedCountList = dao.getNoQualifiedByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 使用总数
		long total_countSum = 0;
		String city_nameSum = "";
		if ("00".equals(cityId))
		{
			city_nameSum = "省中心";
		}
		else
		{
			city_nameSum = "全" + Global.G_CityId_CityName_Map.get(cityId);
		}
		if (qualifyList != null && qualifyList.size() > 0)
		{
			logger.debug("getQualifiedRate() ===> 获取到数据");
			for (int i = 0; i < qualifyList.size(); i++)
			{
				String city_id = "";
				// 三月合格数量
				long qualify_count = 0;
				// 终端状态解绑数量
				long nobind_count = 0;
				// 终端状态绑定数量
				long bind_count = 0;
				// 增加业务数量
				long addBuss_count = 0;
				// 到货测试为是数量
				long istest_count = 0;
				// 未合格数
				long noQualified_count = 0;
				// 总数
				long total_count = 0;
				city_id = StringUtil.getStringValue(qualifyList.get(i), "city_id");
				if (null != city_id && !"-1".equals(city_id) && !"00".equals(city_id))
				{
					qualify_count = StringUtil.getLongValue(qualifyList.get(i),
							"qualify_count");
					nobind_count = StringUtil.getLongValue(noBindList.get(i),
							"nobind_count");
					bind_count = StringUtil.getLongValue(bindList.get(i), "bind_count");
					addBuss_count = StringUtil.getLongValue(addBussList.get(i),
							"addBuss_count");
					istest_count = StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					noQualified_count = StringUtil.getLongValue(
							noQualifiedCountList.get(i), "noqualified_count");
					total_count = nobind_count + bind_count - istest_count;
				}
				qualify_countSum += qualify_count;
				nobind_countSum += nobind_count;
				bind_countSum += bind_count;
				addBuss_countSum += addBuss_count;
				istest_countSum += istest_count;
				noQualified_countSum += noQualified_count;
				total_countSum += total_count;
			}
			cityName = stringAppend(city_nameSum, cityName).append(",");
			qualifyCount = stringAppend(qualify_countSum + "", qualifyCount).append(",");
			nobindCount = stringAppend(nobind_countSum + "", nobindCount).append(",");
			bindCount = stringAppend(bind_countSum + "", bindCount).append(",");
			addBussCount = stringAppend(addBuss_countSum + "", addBussCount).append(",");
			istestCount = stringAppend(istest_countSum + "", istestCount).append(",");
			totalCount = stringAppend(total_countSum + "", totalCount).append(",");
			qualifiedCount = stringAppend(total_countSum - noQualified_countSum + "",
					qualifiedCount).append(",");
			noQualifiedCount = stringAppend(noQualified_countSum + "", noQualifiedCount)
					.append(",");
			city_nameSum = "";
		}
		if (qualifyList != null && qualifyList.size() > 0)
		{
			logger.debug("getQualifiedRate() ===> 获取到数据");
			for (int i = 0; i < qualifyList.size(); i++)
			{
				String city_id = "";
				// 三月合格数量
				long qualify_count = 0;
				// 终端状态解绑数量
				long nobind_count = 0;
				// 终端状态绑定数量
				long bind_count = 0;
				// 增加业务数量
				long addBuss_count = 0;
				// 到货测试为是数量
				long istest_count = 0;
				// 总数
				long total_count = 0;
				// 合格数
				long qualified_count = 0;
				// 未合格数
				long noQualified_count = 0;
				String city_name = "";
				city_id = StringUtil.getStringValue(qualifyList.get(i), "city_id");
				if (null != city_id && !"-1".equals(city_id) && !"00".equals(city_id))
				{
					qualify_count += StringUtil.getLongValue(qualifyList.get(i),
							"qualify_count");
					nobind_count += StringUtil.getLongValue(noBindList.get(i),
							"nobind_count");
					bind_count += StringUtil.getLongValue(bindList.get(i), "bind_count");
					addBuss_count += StringUtil.getLongValue(addBussList.get(i),
							"addBuss_count");
					istest_count += StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					noQualified_count += StringUtil.getLongValue(
							noQualifiedCountList.get(i), "noqualified_count");
					total_count += StringUtil.getLongValue(StringUtil.getLongValue(
							noBindList.get(i), "nobind_count")
							+ StringUtil.getLongValue(bindList.get(i), "bind_count")
							- StringUtil.getLongValue(isTestList.get(i), "istest_count"));
					qualified_count += StringUtil.getLongValue(StringUtil.getLongValue(
							noBindList.get(i), "nobind_count")
							+ StringUtil.getLongValue(bindList.get(i), "bind_count")
							- StringUtil.getLongValue(isTestList.get(i), "istest_count")
							- StringUtil.getLongValue(noQualifiedCountList.get(i),
									"noqualified_count"));
					city_name = Global.G_CityId_CityName_Map.get(city_id);
					cityName = stringAppend(city_name, cityName).append(",");
					qualifyCount = stringAppend(qualify_count + "", qualifyCount).append(
							",");
					nobindCount = stringAppend(nobind_count + "", nobindCount)
							.append(",");
					bindCount = stringAppend(bind_count + "", bindCount).append(",");
					addBussCount = stringAppend(addBuss_count + "", addBussCount).append(
							",");
					istestCount = stringAppend(istest_count + "", istestCount)
							.append(",");
					totalCount = stringAppend(total_count + "", totalCount).append(",");
					qualifiedCount = stringAppend(qualified_count + "", qualifiedCount)
							.append(",");
					noQualifiedCount = stringAppend(noQualified_count + "",
							noQualifiedCount).append(",");
					city_name = "";
				}
			}
			if (cityName.toString().endsWith(","))
			{
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if (qualifyCount.toString().endsWith(","))
			{
				qualifyCount = qualifyCount.deleteCharAt(qualifyCount.length() - 1);
			}
			if (nobindCount.toString().endsWith(","))
			{
				nobindCount = nobindCount.deleteCharAt(nobindCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (addBussCount.toString().endsWith(","))
			{
				addBussCount = addBussCount.deleteCharAt(addBussCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (qualifiedCount.toString().endsWith(","))
			{
				qualifiedCount = qualifiedCount.deleteCharAt(qualifiedCount.length() - 1);
			}
			if (noQualifiedCount.toString().endsWith(","))
			{
				noQualifiedCount = noQualifiedCount.deleteCharAt(noQualifiedCount
						.length() - 1);
			}
		}
		else
		{
			logger.debug("getQualifiedRate() ===> 未获取到数据");
			Collections.sort(cityIds);
			String qualify_count = "0";
			String noBind_count = "0";
			String bind_count = "0";
			String addBuss_count = "0";
			String istest_count = "0";
			String total_count = "0";
			String qualified_count = "0";
			String noQualified_count = "0";
			String city_name = "";
			for (String city_id : cityIds)
			{
				city_name = Global.G_CityId_CityName_Map.get(city_id);
				cityName = stringAppend(city_name, cityName).append(",");
				qualifyCount = stringAppend(qualify_count, qualifyCount).append(",");
				nobindCount = stringAppend(noBind_count, nobindCount).append(",");
				bindCount = stringAppend(bind_count, bindCount).append(",");
				addBussCount = stringAppend(addBuss_count, addBussCount).append(",");
				istestCount = stringAppend(istest_count, istestCount).append(",");
				totalCount = stringAppend(total_count, totalCount).append(",");
				qualifiedCount = stringAppend(qualified_count, qualifiedCount)
						.append(",");
				noQualifiedCount = stringAppend(noQualified_count, noQualifiedCount)
						.append(",");
				city_name = "";
			}
			if (cityName.toString().endsWith(","))
			{
				cityName = cityName.deleteCharAt(cityName.length() - 1);
			}
			if (qualifyCount.toString().endsWith(","))
			{
				qualifyCount = qualifyCount.deleteCharAt(qualifyCount.length() - 1);
			}
			if (nobindCount.toString().endsWith(","))
			{
				nobindCount = nobindCount.deleteCharAt(nobindCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (addBussCount.toString().endsWith(","))
			{
				addBussCount = addBussCount.deleteCharAt(addBussCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (qualifiedCount.toString().endsWith(","))
			{
				qualifiedCount = qualifiedCount.deleteCharAt(qualifiedCount.length() - 1);
			}
			if (noQualifiedCount.toString().endsWith(","))
			{
				noQualifiedCount = noQualifiedCount.deleteCharAt(noQualifiedCount
						.length() - 1);
			}
		}
		cityName.append("]");
		qualifyCount.append("]");
		nobindCount.append("]");
		bindCount.append("]");
		addBussCount.append("]");
		istestCount.append("]");
		totalCount.append("]");
		qualifiedCount.append("]");
		noQualifiedCount.append("]");
		json.append(cityName);
		json.append(",");
		json.append("\"qualifyCount\":");
		json.append(qualifyCount);
		json.append(",");
		json.append("\"nobindCount\":");
		json.append(nobindCount);
		json.append(",");
		json.append("\"bindCount\":");
		json.append(bindCount);
		json.append(",");
		json.append("\"addBussCount\":");
		json.append(addBussCount);
		json.append(",");
		json.append("\"istestCount\":");
		json.append(istestCount);
		json.append(",");
		json.append("\"totalCount\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"qualifiedCount\":");
		json.append(qualifiedCount);
		json.append(",");
		json.append("\"noQualifiedCount\":");
		json.append(noQualifiedCount);
		json.append("}");
		return json.toString();
	}

	/**
	 * 根据属地生成各地市合格率柱状图
	 * 
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return
	 * @throws Exception
	 */
	public String getQualifiedRateByCity(String cityId, String starttime, String endtime,
			String batchNum) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getQualifiedRate()");
		StringBuffer json = new StringBuffer("{\"vendors\":");
		StringBuffer vendorName = new StringBuffer("[");
		StringBuffer qualifyCount = new StringBuffer("[");
		StringBuffer nobindCount = new StringBuffer("[");
		StringBuffer bindCount = new StringBuffer("[");
		StringBuffer addBussCount = new StringBuffer("[");
		StringBuffer istestCount = new StringBuffer("[");
		StringBuffer totalCount = new StringBuffer("[");
		StringBuffer qualifiedCount = new StringBuffer("[");
		StringBuffer noQualifiedCount = new StringBuffer("[");
		List<String> vendorIds = new ArrayList<String>();
		List<Map<String, String>> vendorList = dao.getVendor();
		// 三月合格数量
		List<Map<String, String>> qualifyList = dao.getQualifyCountByCity(cityId,
				starttime, endtime, batchNum);
		// 终端状态解绑数量
		List<Map<String, String>> noBindList = dao.getQualifiedNoBindCountByCity(cityId,
				starttime, endtime, batchNum);
		// 终端状态绑定数量
		List<Map<String, String>> bindList = dao.getQualifiedBindCountByCity(cityId,
				starttime, endtime, batchNum);
		// 增加业务数量
		List<Map<String, String>> addBussList = dao.getQualifiedAddBussCountByCity(
				cityId, starttime, endtime, batchNum);
		// 到货测试为是数量
		List<Map<String, String>> isTestList = dao.getQualifiedIsTestCountByCity(cityId,
				starttime, endtime, batchNum);
		// 合格数
		List<Map<String, String>> noQualifiedCountList = dao.getNoQualifiedCountByCity(
				cityId, starttime, endtime, batchNum);
		Map<String, String> vendorMap = getRepairDevMap();
		if (qualifyList != null && qualifyList.size() > 0)
		{
			logger.debug("getQualifiedRate() ===> 获取到数据");
			for (int i = 0; i < qualifyList.size(); i++)
			{
				String vendor_id = "";
				// 三月合格数量
				long qualify_count = 0;
				// 终端状态解绑数量
				long nobind_count = 0;
				// 终端状态绑定数量
				long bind_count = 0;
				// 增加业务数量
				long addBuss_count = 0;
				// 到货测试为是数量
				long istest_count = 0;
				// 总数
				long total_count = 0;
				// 合格数
				long qualified_count = 0;
				// 未合格数
				long noQualified_count = 0;
				String vendor_name = "";
				vendor_id = StringUtil.getStringValue(qualifyList.get(i), "vendor_id");
				if (null != vendor_id && !"-1".equals(vendor_id))
				{
					qualify_count += StringUtil.getLongValue(qualifyList.get(i),
							"qualify_count");
					nobind_count += StringUtil.getLongValue(noBindList.get(i),
							"nobind_count");
					bind_count += StringUtil.getLongValue(bindList.get(i), "bind_count");
					addBuss_count += StringUtil.getLongValue(addBussList.get(i),
							"addBuss_count");
					istest_count += StringUtil.getLongValue(isTestList.get(i),
							"istest_count");
					total_count += StringUtil.getLongValue(StringUtil.getLongValue(
							noBindList.get(i), "nobind_count")
							+ StringUtil.getLongValue(bindList.get(i), "bind_count")
							- StringUtil.getLongValue(isTestList.get(i), "istest_count"));
					qualified_count += StringUtil.getLongValue(StringUtil.getLongValue(
							noBindList.get(i), "nobind_count")
							+ StringUtil.getLongValue(bindList.get(i), "bind_count")
							- StringUtil.getLongValue(isTestList.get(i), "istest_count")
							- StringUtil.getLongValue(noQualifiedCountList.get(i),
									"noqualified_count"));
					noQualified_count += StringUtil.getLongValue(
							noQualifiedCountList.get(i), "noqualified_count");
					vendor_name = vendorMap.get(vendor_id);
					vendorName = stringAppend(vendor_name, vendorName).append(",");
					qualifyCount = stringAppend(qualify_count + "", qualifyCount).append(
							",");
					nobindCount = stringAppend(nobind_count + "", nobindCount)
							.append(",");
					bindCount = stringAppend(bind_count + "", bindCount).append(",");
					addBussCount = stringAppend(addBuss_count + "", addBussCount).append(
							",");
					istestCount = stringAppend(istest_count + "", istestCount)
							.append(",");
					totalCount = stringAppend(total_count + "", totalCount).append(",");
					qualifiedCount = stringAppend(qualified_count + "", qualifiedCount)
							.append(",");
					noQualifiedCount = stringAppend(noQualified_count + "",
							noQualifiedCount).append(",");
					vendor_name = "";
				}
			}
			if (vendorName.toString().endsWith(","))
			{
				vendorName = vendorName.deleteCharAt(vendorName.length() - 1);
			}
			if (qualifyCount.toString().endsWith(","))
			{
				qualifyCount = qualifyCount.deleteCharAt(qualifyCount.length() - 1);
			}
			if (nobindCount.toString().endsWith(","))
			{
				nobindCount = nobindCount.deleteCharAt(nobindCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (addBussCount.toString().endsWith(","))
			{
				addBussCount = addBussCount.deleteCharAt(addBussCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (qualifiedCount.toString().endsWith(","))
			{
				qualifiedCount = qualifiedCount.deleteCharAt(qualifiedCount.length() - 1);
			}
			if (noQualifiedCount.toString().endsWith(","))
			{
				noQualifiedCount = noQualifiedCount.deleteCharAt(noQualifiedCount
						.length() - 1);
			}
		}
		else
		{
			logger.debug("getQualifiedRate() ===> 未获取到数据");
			Collections.sort(vendorIds);
			String qualify_count = "0";
			String bind_count = "0";
			String noBind_count = "0";
			String addBuss_count = "0";
			String istest_count = "0";
			String total_count = "0";
			String qualified_count = "0";
			String noQualified_count = "0";
			String vendor_name = "";
			for (String vendor_id : vendorIds)
			{
				vendor_name = vendorList.get(0).get(vendor_id);
				vendorName = stringAppend(vendor_name, vendorName).append(",");
				qualifyCount = stringAppend(qualify_count, qualifyCount).append(",");
				bindCount = stringAppend(bind_count, bindCount).append(",");
				nobindCount = stringAppend(noBind_count, nobindCount).append(",");
				addBussCount = stringAppend(addBuss_count, addBussCount).append(",");
				istestCount = stringAppend(istest_count, istestCount).append(",");
				totalCount = stringAppend(total_count, totalCount).append(",");
				qualifiedCount = stringAppend(qualified_count, qualifiedCount)
						.append(",");
				noQualifiedCount = stringAppend(noQualified_count, noQualifiedCount)
						.append(",");
				vendor_name = "";
			}
			if (vendorName.toString().endsWith(","))
			{
				vendorName = vendorName.deleteCharAt(vendorName.length() - 1);
			}
			if (qualifyCount.toString().endsWith(","))
			{
				qualifyCount = qualifyCount.deleteCharAt(qualifyCount.length() - 1);
			}
			if (nobindCount.toString().endsWith(","))
			{
				nobindCount = nobindCount.deleteCharAt(nobindCount.length() - 1);
			}
			if (bindCount.toString().endsWith(","))
			{
				bindCount = bindCount.deleteCharAt(bindCount.length() - 1);
			}
			if (addBussCount.toString().endsWith(","))
			{
				addBussCount = addBussCount.deleteCharAt(addBussCount.length() - 1);
			}
			if (istestCount.toString().endsWith(","))
			{
				istestCount = istestCount.deleteCharAt(istestCount.length() - 1);
			}
			if (totalCount.toString().endsWith(","))
			{
				totalCount = totalCount.deleteCharAt(totalCount.length() - 1);
			}
			if (qualifiedCount.toString().endsWith(","))
			{
				qualifiedCount = qualifiedCount.deleteCharAt(qualifiedCount.length() - 1);
			}
			if (noQualifiedCount.toString().endsWith(","))
			{
				noQualifiedCount = noQualifiedCount.deleteCharAt(noQualifiedCount
						.length() - 1);
			}
		}
		vendorName.append("]");
		qualifyCount.append("]");
		nobindCount.append("]");
		bindCount.append("]");
		addBussCount.append("]");
		istestCount.append("]");
		totalCount.append("]");
		qualifiedCount.append("]");
		noQualifiedCount.append("]");
		json.append(vendorName);
		json.append(",");
		json.append("\"qualifyCount\":");
		json.append(qualifyCount);
		json.append(",");
		json.append("\"nobindCount\":");
		json.append(nobindCount);
		json.append(",");
		json.append("\"bindCount\":");
		json.append(bindCount);
		json.append(",");
		json.append("\"addBussCount\":");
		json.append(addBussCount);
		json.append(",");
		json.append("\"istestCount\":");
		json.append(istestCount);
		json.append(",");
		json.append("\"totalCount\":");
		json.append(totalCount);
		json.append(",");
		json.append("\"qualifiedCount\":");
		json.append(qualifiedCount);
		json.append(",");
		json.append("\"noQualifiedCount\":");
		json.append(noQualifiedCount);
		json.append("}");
		return json.toString();
	}

	/**
	 * 根据厂商展示各属地合格率饼图
	 * 
	 * @param vendor_name
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return String
	 * @throws Exception
	 */
	public String getQualifiedRatePieDataByVendor(long repair_vendor_id,
			String starttime, String endtime, String batchNum, String cityId)
			throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByVendor({})");
		StringBuffer json = new StringBuffer();
		// 三月合格数量
		List<Map<String, String>> qualifiedList = dao.getQualifiedPieCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 终端状态解绑数量
		List<Map<String, String>> noBindList = dao.getQualifiedPieNoBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 终端状态绑定数量
		List<Map<String, String>> bindList = dao.getQualifiedPieBindCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 增加业务数量
		List<Map<String, String>> addBussList = dao.getQualifiedPieAddBussCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		// 到货测试为是数量
		List<Map<String, String>> isTestList = dao.getQualifiedPieIsTestCountByVendor(
				repair_vendor_id, starttime, endtime, batchNum, cityId);
		if (qualifiedList != null && qualifiedList.size() > 0)
		{
			logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByVendor({}) ==获取到数据");
			// 三月合格数量
			String qualify_count = "";
			// 终端状态解绑数量
			String nobind_count = "";
			// 终端状态绑定数量
			String bind_count = "";
			// 增加业务数量
			String addBuss_count = "";
			// 到货测试为是数量
			String istest_count = "";
			for (int i = 0; i < qualifiedList.size(); i++)
			{
				qualify_count = StringUtil.getStringValue(qualifiedList.get(i),
						"qualify_count");
				nobind_count = StringUtil.getStringValue(noBindList.get(i),
						"nobind_count");
				bind_count = StringUtil.getStringValue(bindList.get(i), "bind_count");
				addBuss_count = StringUtil.getStringValue(addBussList.get(i),
						"addBuss_count");
				istest_count = StringUtil.getStringValue(isTestList.get(i),
						"istest_count");
				String tmps = getQualifiedRatePieJson(qualify_count, nobind_count,
						bind_count, addBuss_count, istest_count);
				json.append(tmps);
				json.append(",");
				qualify_count = "";
				nobind_count = "";
				bind_count = "";
				addBuss_count = "";
				istest_count = "";
			}
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		else
		{
			logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByVendor({}) ==未获取到数据");
			String qualify_count = "0";
			String nobind_count = "0";
			String bind_count = "0";
			String addBuss_count = "0";
			String istest_count = "0";
			String tmps = getQualifiedRatePieJson(qualify_count, nobind_count,
					bind_count, addBuss_count, istest_count);
			json.append(tmps);
			json.append(",");
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		return json.toString();
	}

	/**
	 * 根据属地展示各厂商饼图
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return String
	 * @throws Exception
	 */
	public String getQualifiedRatePieDataByCity(String cityId, String starttime,
			String endtime, String batchNum) throws Exception
	{
		logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByCity({})");
		StringBuffer json = new StringBuffer();
		// 三月合格数量
		List<Map<String, String>> qualifiedList = dao.getQualifiedRatePieCountByCity(
				cityId, starttime, endtime, batchNum);
		// 终端状态解绑数量
		List<Map<String, String>> noBindList = dao.getQualifiedPieNoBindCountByCity(
				cityId, starttime, endtime, batchNum);
		// 终端状态绑定数量
		List<Map<String, String>> bindList = dao.getQualifiedPieBindCountByCity(cityId,
				starttime, endtime, batchNum);
		// 增加业务数量
		List<Map<String, String>> addBussList = dao.getQualifiedPieAddBussCountByCity(
				cityId, starttime, endtime, batchNum);
		// 到货测试为是数量
		List<Map<String, String>> isTestList = dao.getQualifiedPieIsTestCountByCity(
				cityId, starttime, endtime, batchNum);
		if (qualifiedList != null && qualifiedList.size() > 0)
		{
			logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByCity({}) ==获取到数据");
			// 三月合格数量
			String qualify_count = "";
			// 终端状态解绑数量
			String nobind_count = "";
			// 终端状态绑定数量
			String bind_count = "";
			// 增加业务数量
			String addBuss_count = "";
			// 到货测试为是数量
			String istest_count = "";
			for (int i = 0; i < qualifiedList.size(); i++)
			{
				qualify_count = StringUtil.getStringValue(qualifiedList.get(i),
						"qualify_count");
				nobind_count = StringUtil.getStringValue(noBindList.get(i),
						"nobind_count");
				bind_count = StringUtil.getStringValue(bindList.get(i), "bind_count");
				addBuss_count = StringUtil.getStringValue(addBussList.get(i),
						"addBuss_count");
				istest_count = StringUtil.getStringValue(isTestList.get(i),
						"istest_count");
				String tmps = getQualifiedRatePieJson(qualify_count, nobind_count,
						bind_count, addBuss_count, istest_count);
				json.append(tmps);
				json.append(",");
				qualify_count = "";
				nobind_count = "";
				bind_count = "";
				addBuss_count = "";
				istest_count = "";
			}
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		else
		{
			logger.debug("DevRepairTestInfoBIO ====> getQualifiedRatePieDataByCity({}) ==未获取到数据");
			String qualify_count = "0";
			String nobind_count = "0";
			String bind_count = "0";
			String addBuss_count = "0";
			String istest_count = "0";
			String tmps = getQualifiedRatePieJson(qualify_count, nobind_count,
					bind_count, addBuss_count, istest_count);
			json.append(tmps);
			json.append(",");
			if (json.toString().endsWith(","))
			{
				json = json.deleteCharAt(json.length() - 1);
			}
		}
		return json.toString();
	}

	public static StringBuffer stringAppend(String str, StringBuffer tmp)
	{
		tmp.append("\"");
		tmp.append(str);
		tmp.append("\"");
		return tmp;
	}

	public String getUseRatePieJson(String total_count, String istest_count,
			String bind_count)
	{
		String unit = "";
		String total = total_count;
		String istestCount = istest_count;
		String bindCount = bind_count;
		double useRate = 0;
		if ("".equals(total_count) || null == StringUtil.getStringValue(total_count)
				|| "".equals(total_count))
		{
			useRate = 0;
		}
		else if (StringUtil.getDoubleValue(total) == 0)
		{
			useRate = 0;
		}
		else
		{
			useRate = 1
					- (StringUtil.getDoubleValue(istestCount) + StringUtil
							.getDoubleValue(bindCount))
					/ StringUtil.getDoubleValue(total);
		}
		StringBuffer tmp = new StringBuffer("{\"total\":\"");
		tmp.append(total);
		tmp.append("\",");
		tmp.append("\"unit\":\"");
		tmp.append(unit);
		tmp.append("\",");
		tmp.append("\"use\":[{\"value\":\"");
		tmp.append(useRate);
		tmp.append("\",\"name\":\"使用率\"},{\"value\":\"");
		tmp.append(1 - useRate);
		tmp.append("\",\"name\":\"未使用率\"}]}");
		return tmp.toString();
	}

	public String getQualifiedRatePieJson(String qualify_count, String nobind_count,
			String bind_count, String addBuss_count, String istest_count)
	{
		String qualifyCount = qualify_count;
		String nobindCount = nobind_count;
		String bindCount = bind_count;
		String addBussCount = addBuss_count;
		String istestCount = istest_count;
		double qualiedRate = 0;
		double tmpQual = (StringUtil.getDoubleValue(nobindCount)
				+ StringUtil.getDoubleValue(bindCount)
				- StringUtil.getDoubleValue(addBussCount) - StringUtil
				.getDoubleValue(istestCount));
		if (tmpQual == 0)
		{
			qualiedRate = 0;
		}
		else
		{
			qualiedRate = StringUtil.getDoubleValue(qualifyCount) / tmpQual;
		}
		StringBuffer tmp = new StringBuffer();
		tmp.append("{\"qualify\":[{\"value\":\"");
		tmp.append(qualiedRate);
		tmp.append("\",\"name\":\"合格率\"},{\"value\":\"");
		tmp.append(1 - qualiedRate);
		tmp.append("\",\"name\":\"不合格率\"}]}");
		return tmp.toString();
	}

	public String divide(long p1, long p2)
	{
		DecimalFormat df = new DecimalFormat("#0.0000");
		String avg = "0";
		if (p2 != 0 && p1 != 0)
		{
			double tmp = (double) p1 / p2;
			avg = df.format(tmp);
		}
		return avg;
	}

	/**
	 * 获取属地的下一级
	 * 
	 * @param cityId
	 * @return
	 */
	public List<Map<String, String>> getCityList(String cityId)
	{
		return CityDAO.getNextCityListByCityPid(cityId);
	}

	/**
	 * 导出使用率统计数据
	 * 
	 * @param repair_vendor_id
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused", "null" })
	public List<Map<String, String>> getUseRateTotalList(
			List<Map<String, String>> cityTempList, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		List<Map<String, String>> totalList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> noUseList1 = new ArrayList<Map<String, String>>();
		List rsList = new ArrayList();
		List<Map<String, String>> allRepairVerndorList = new ArrayList<Map<String, String>>();
		NumberFormat nt = NumberFormat.getPercentInstance();
		// 厂商
		allRepairVerndorList = dao.getVendorList(repair_vendor_id, cityId, starttime,
				endtime, batchNum);
		Map vendorMap = dao.getRepairDevMap();
		for (Map<String, String> repairVerndor : allRepairVerndorList)
		{
			// 合计总数
			int sumTotal = 0;
			// 总使用数
			int sumUseCount = 0;
			// 总使用率
			double sumUseRate = 0;
			RepairDevObj obj = new RepairDevObj();
			List<List> childList = new ArrayList<List>();
			List<Map<String, String>> totalList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> noUseList = new ArrayList<Map<String, String>>();
			List<String> stringList = new ArrayList<String>();
			List<String> stringList1 = new ArrayList<String>();
			List<String> stringList2 = new ArrayList<String>();
			stringList.add("使用数");
			stringList1.add("返修终端数");
			stringList2.add("使用率");
			long repairVerndorId = StringUtil.getLongValue(repairVerndor,
					"repair_vendor_id");
			for (int i = 0; i < cityTempList.size(); i++)
			{
				String city_id = StringUtil
						.getStringValue(cityTempList.get(i), "city_id");
				if (!"00".equals(city_id))
				{
					totalList = new ArrayList();
					noUseList = new ArrayList();
					totalList = dao.getTotalList(city_id, repairVerndorId, cityId,
							starttime, endtime, batchNum);
					noUseList = dao.getNoUseList(city_id, repairVerndorId, cityId,
							starttime, endtime, batchNum);
					// 总数
					String total = "";
					// 未使用数
					String noUse = "";
					// 使用数
					int useCount = 0;
					// 使用率
					double useRate = 0;
					if (null != totalList || totalList.size() > 0)
					{
						total = StringUtil
								.getStringValue(totalList.get(0), "total_count");
						if (null != noUseList || noUseList.size() > 0)
						{
							noUse = StringUtil.getStringValue(noUseList.get(0),
									"nouse_count");
							useCount = StringUtil.getIntegerValue(total)
									- StringUtil.getIntegerValue(noUse);
						}
						else
						{
							useCount = StringUtil.getIntegerValue(total);
						}
						useRate = StringUtil.getDoubleValue(useCount)
								/ StringUtil.getDoubleValue(total);
					}
					if ("0".equals(total))
					{
						total = "0";
						noUse = "0";
						useRate = 0;
					}
					stringList.add(StringUtil.getStringValue(useCount));
					stringList1.add(total);
					if (0 != useRate || 100 != useRate)
					{
						nt.setMinimumFractionDigits(2);
						stringList2.add(nt.format(useRate));
					}
					else
					{
						stringList2.add(nt.format(useRate));
					}
					sumTotal += StringUtil.getIntegerValue(total);
					sumUseCount += useCount;
					if (0 == sumTotal)
					{
						sumUseRate = 0;
					}
					else
					{
						sumUseRate = StringUtil.getDoubleValue(sumUseCount)
								/ StringUtil.getDoubleValue(sumTotal);
					}
				}
			}
			stringList.add(StringUtil.getStringValue(sumUseCount));
			stringList1.add(StringUtil.getStringValue(sumTotal));
			if (0 != sumUseRate || 100 != sumUseRate)
			{
				nt.setMinimumFractionDigits(2);
				stringList2.add(nt.format(sumUseRate));
			}
			else
			{
				stringList2.add(nt.format(sumUseRate));
			}
			childList.add(stringList);
			childList.add(stringList1);
			childList.add(stringList2);
			obj.setChildList(childList);
			obj.setVendor_name(StringUtil.getStringValue(vendorMap.get(StringUtil
					.getStringValue(repairVerndorId))));
			rsList.add(obj);
		}
		if (repair_vendor_id == -1 || (StringUtil.IsEmpty(cityId) && "-1".equals(cityId)))
		{
			List<String> stringList3 = new ArrayList<String>();
			List<String> stringList4 = new ArrayList<String>();
			List<String> stringList5 = new ArrayList<String>();
			List<List> childList1 = new ArrayList<List>();
			stringList3.add("总使用数");
			stringList4.add("总返修终端数");
			stringList5.add("总使用率");
			// 所有厂家总数
			String total1 = "";
			// 所有厂家未使用数
			String noUse1 = "";
			// 所有厂家使用数
			int useCount1 = 0;
			// 所有厂家使用率
			double useRate1 = 0;
			// 所有厂家总数
			int sumTotal1 = 0;
			// 所有厂家总使用数
			int sumUseCount1 = 0;
			// 所有厂家总使用率
			double sumUseRate1 = 0;
			RepairDevObj obj = new RepairDevObj();
			RepairDevObj obj1 = new RepairDevObj();
			for (int i = 0; i < cityTempList.size(); i++)
			{
				String city_id1 = StringUtil.getStringValue(cityTempList.get(i),
						"city_id");
				if (!"00".equals(city_id1))
				{
					totalList1 = new ArrayList();
					noUseList1 = new ArrayList();
					totalList1 = dao.getTotalList(city_id1, -1, cityId, starttime,
							endtime, batchNum);
					noUseList1 = dao.getNoUseList(city_id1, -1, cityId, starttime,
							endtime, batchNum);
					if (null != totalList1 || totalList1.size() > 0)
					{
						total1 = StringUtil.getStringValue(totalList1.get(0),
								"total_count");
						if (null != noUseList1 || noUseList1.size() > 0)
						{
							noUse1 = StringUtil.getStringValue(noUseList1.get(0),
									"nouse_count");
							useCount1 = StringUtil.getIntegerValue(total1)
									- StringUtil.getIntegerValue(noUse1);
						}
						else
						{
							useCount1 = StringUtil.getIntegerValue(total1);
						}
						useRate1 = StringUtil.getDoubleValue(useCount1)
								/ StringUtil.getDoubleValue(total1);
					}
					if ("0".equals(total1))
					{
						total1 = "0";
						noUse1 = "0";
						useRate1 = 0;
					}
					stringList3.add(StringUtil.getStringValue(useCount1));
					stringList4.add(total1);
					if (0 != useRate1 || 100 != useRate1)
					{
						nt.setMinimumFractionDigits(2);
						stringList5.add(nt.format(useRate1));
					}
					else
					{
						stringList5.add(nt.format(useRate1));
					}
					sumTotal1 += StringUtil.getIntegerValue(total1);
					sumUseCount1 += useCount1;
					sumUseRate1 = StringUtil.getDoubleValue(sumUseCount1)
							/ StringUtil.getDoubleValue(sumTotal1);
				}
			}
			stringList3.add(StringUtil.getStringValue(sumUseCount1));
			stringList4.add(StringUtil.getStringValue(sumTotal1));
			if (0 != sumUseRate1 || 100 != sumUseRate1)
			{
				nt.setMinimumFractionDigits(2);
				stringList5.add(nt.format(sumUseRate1));
			}
			else
			{
				stringList5.add(nt.format(sumUseRate1));
			}
			childList1.add(stringList3);
			childList1.add(stringList4);
			childList1.add(stringList5);
			obj1.setChildList(childList1);
			obj1.setVendor_name("所有厂家");
			rsList.add(obj1);
		}
		return rsList;
	}

	/**
	 * 导出合格率统计数据
	 * 
	 * @param repair_vendor_id
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<Map<String, String>> getQualifiedRateTotalList(
			List<Map<String, String>> cityTempList, long repair_vendor_id, String cityId,
			String starttime, String endtime, String batchNum)
	{
		List<Map<String, String>> qualifiedList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> noBindList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> bindList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> isTestList1 = new ArrayList<Map<String, String>>();
		List rsList = new ArrayList();
		List<Map<String, String>> allRepairVerndorList = new ArrayList<Map<String, String>>();
		NumberFormat nt = NumberFormat.getPercentInstance();
		// 厂商
		allRepairVerndorList = dao.getVendorList(repair_vendor_id, cityId, starttime,
				endtime, batchNum);
		Map vendorMap = dao.getRepairDevMap();
		for (Map<String, String> repairVerndor : allRepairVerndorList)
		{
			// 合计总数
			int sumTotal = 0;
			// 总合格数
			int sumQualifiedCount = 0;
			// 总合格率
			double sumQualifiedRate = 0;
			RepairDevObj obj = new RepairDevObj();
			List<List> childList = new ArrayList<List>();
			List<Map<String, String>> qualifiedList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> noBindList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> bindList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> isTestList = new ArrayList<Map<String, String>>();
			List<String> stringList = new ArrayList<String>();
			List<String> stringList1 = new ArrayList<String>();
			List<String> stringList2 = new ArrayList<String>();
			stringList.add("合格数");
			stringList1.add("总数");
			stringList2.add("合格率");
			long repairVerndorId = StringUtil.getLongValue(repairVerndor,
					"repair_vendor_id");
			for (int i = 0; i < cityTempList.size(); i++)
			{
				String city_id = StringUtil
						.getStringValue(cityTempList.get(i), "city_id");
				if (!"00".equals(city_id))
				{
					qualifiedList = new ArrayList();
					noBindList = new ArrayList();
					bindList = new ArrayList();
					isTestList = new ArrayList();
					qualifiedList = dao.getQualifiedCountList(city_id, repairVerndorId,
							cityId, starttime, endtime, batchNum);
					noBindList = dao.getNoBindCountList(city_id, repairVerndorId, cityId,
							starttime, endtime, batchNum);
					bindList = dao.getBindCountList(city_id, repairVerndorId, cityId,
							starttime, endtime, batchNum);
					isTestList = dao.getIsTestCountList(city_id, repairVerndorId, cityId,
							starttime, endtime, batchNum);
					// 总数
					int total = 0;
					// 合格数
					String qualifiedCount = "";
					// 合格率
					double qualifiedRate = 0;
					if ((null != qualifiedList && qualifiedList.size() > 0)
							&& (null != noBindList && noBindList.size() > 0)
							&& (null != bindList && bindList.size() > 0)
							&& (null != isTestList && isTestList.size() > 0))
					{
						qualifiedCount = StringUtil.getStringValue(qualifiedList.get(0),
								"qualify_count");
						total = StringUtil.getIntegerValue(StringUtil.getStringValue(
								noBindList.get(0), "nobind_count"))
								+ StringUtil.getIntegerValue(StringUtil.getStringValue(
										bindList.get(0), "bind_count"))
								- StringUtil.getIntegerValue(StringUtil.getStringValue(
										isTestList.get(0), "istest_count"));
						qualifiedRate = StringUtil.getDoubleValue(qualifiedCount)
								/ StringUtil.getDoubleValue(total);
					}
					if (0 == total)
					{
						qualifiedRate = 0;
					}
					stringList.add(StringUtil.getStringValue(qualifiedCount));
					stringList1.add(StringUtil.getStringValue(total));
					if (0 != qualifiedRate || 100 != qualifiedRate)
					{
						nt.setMinimumFractionDigits(2);
						stringList2.add(nt.format(qualifiedRate));
					}
					else
					{
						stringList2.add(nt.format(qualifiedRate));
					}
					sumTotal += StringUtil.getIntegerValue(total);
					sumQualifiedCount += StringUtil.getIntegerValue(qualifiedCount);
					if (0 == sumTotal)
					{
						sumQualifiedRate = 0;
					}
					else
					{
						sumQualifiedRate = StringUtil.getDoubleValue(sumQualifiedCount)
								/ StringUtil.getDoubleValue(sumTotal);
					}
				}
			}
			stringList.add(StringUtil.getStringValue(sumQualifiedCount));
			stringList1.add(StringUtil.getStringValue(sumTotal));
			if (0 != sumQualifiedRate || 100 != sumQualifiedRate)
			{
				nt.setMinimumFractionDigits(2);
				stringList2.add(nt.format(sumQualifiedRate));
			}
			else
			{
				stringList2.add(nt.format(sumQualifiedRate));
			}
			childList.add(stringList);
			childList.add(stringList1);
			childList.add(stringList2);
			obj.setChildList(childList);
			obj.setVendor_name(StringUtil.getStringValue(vendorMap.get(StringUtil
					.getStringValue(repairVerndorId))));
			rsList.add(obj);
		}
		if (repair_vendor_id == -1 || (StringUtil.IsEmpty(cityId) && "-1".equals(cityId)))
		{
			List<String> stringList3 = new ArrayList<String>();
			List<String> stringList4 = new ArrayList<String>();
			List<String> stringList5 = new ArrayList<String>();
			List<List> childList1 = new ArrayList<List>();
			stringList3.add("总合格数");
			stringList4.add("总数");
			stringList5.add("总合格率");
			// 所有厂家总数
			int total1 = 0;
			// 所有厂家合格数
			int qualifiedCount1 = 0;
			// 所有厂家合格率
			double qualifiedRate1 = 0;
			// 所有厂家总数
			int sumTotal1 = 0;
			// 所有厂家总合格数
			int sumQualifiedCount1 = 0;
			// 所有厂家总合格率
			double sumQualifiedRate1 = 0;
			RepairDevObj obj = new RepairDevObj();
			RepairDevObj obj1 = new RepairDevObj();
			for (int i = 0; i < cityTempList.size(); i++)
			{
				String city_id1 = StringUtil.getStringValue(cityTempList.get(i),
						"city_id");
				if (!"00".equals(city_id1))
				{
					qualifiedList1 = new ArrayList();
					qualifiedList1 = new ArrayList();
					qualifiedList1 = new ArrayList();
					qualifiedList1 = new ArrayList();
					qualifiedList1 = new ArrayList();
					qualifiedList1 = dao.getQualifiedCountList(city_id1, -1, cityId,
							starttime, endtime, batchNum);
					noBindList1 = dao.getNoBindCountList(city_id1, -1, cityId, starttime,
							endtime, batchNum);
					bindList1 = dao.getBindCountList(city_id1, -1, cityId, starttime,
							endtime, batchNum);
					isTestList1 = dao.getIsTestCountList(city_id1, -1, cityId, starttime,
							endtime, batchNum);
					if ((null != qualifiedList1 && qualifiedList1.size() > 0)
							&& (null != noBindList1 && noBindList1.size() > 0)
							&& (null != bindList1 && bindList1.size() > 0)
							&& (null != isTestList1 && isTestList1.size() > 0))
					{
						qualifiedCount1 = StringUtil.getIntegerValue(StringUtil
								.getStringValue(qualifiedList1.get(0), "qualify_count"));
						total1 = StringUtil.getIntegerValue(StringUtil.getStringValue(
								noBindList1.get(0), "nobind_count"))
								+ StringUtil.getIntegerValue(StringUtil.getStringValue(
										bindList1.get(0), "bind_count"))
								- StringUtil.getIntegerValue(StringUtil.getStringValue(
										isTestList1.get(0), "istest_count"));
						qualifiedRate1 = StringUtil.getDoubleValue(qualifiedCount1)
								/ StringUtil.getDoubleValue(total1);
					}
					if (0 == total1)
					{
						qualifiedRate1 = 0;
					}
					stringList3.add(StringUtil.getStringValue(qualifiedCount1));
					stringList4.add(StringUtil.getStringValue(total1));
					if (0 != qualifiedRate1 || 100 != qualifiedRate1)
					{
						nt.setMinimumFractionDigits(2);
						stringList5.add(nt.format(qualifiedRate1));
					}
					else
					{
						stringList5.add(nt.format(qualifiedRate1));
					}
					sumTotal1 += StringUtil.getIntegerValue(total1);
					sumQualifiedCount1 += qualifiedCount1;
					sumQualifiedRate1 = StringUtil.getDoubleValue(sumQualifiedCount1)
							/ StringUtil.getDoubleValue(sumTotal1);
				}
			}
			stringList3.add(StringUtil.getStringValue(sumQualifiedCount1));
			stringList4.add(StringUtil.getStringValue(sumTotal1));
			if (0 != sumQualifiedRate1 || 100 != sumQualifiedRate1)
			{
				nt.setMinimumFractionDigits(2);
				stringList5.add(nt.format(sumQualifiedRate1));
			}
			else
			{
				stringList5.add(nt.format(sumQualifiedRate1));
			}
			childList1.add(stringList3);
			childList1.add(stringList4);
			childList1.add(stringList5);
			obj1.setChildList(childList1);
			obj1.setVendor_name("所有厂家");
			rsList.add(obj1);
		}
		return rsList;
	}

	/**
	 * 根据厂商导出返修厂商未使用清单
	 * 
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> noUseListToExcelByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId)
	{
		return dao.getNoUseListByVendor(repair_vendor_id, starttime, endtime, batchNum,
				cityId);
	}

	/**
	 * 根据属地导出返修厂商未使用清单
	 * 
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> noUseListToExcelByCity(String cityId, String starttime,
			String endtime, String batchNum)
	{
		return dao.getNoUseListByCity(cityId, starttime, endtime, batchNum);
	}

	/**
	 * 根据厂商导出返修厂商未合格清单
	 * 
	 * @param repair_vendor_id
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> noQualifiedToExcelByVendor(long repair_vendor_id, String starttime,
			String endtime, String batchNum, String cityId)
	{
		logger.debug("bio --getQualifiedListByVendor()");
		return dao.getQualifiedListByVendor(repair_vendor_id, starttime, endtime,
				batchNum, cityId);
	}

	/**
	 * 根据属地导出返修厂商未合格清单
	 * 
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param batchNum
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> noQualifiedToExcelByCity(String cityId, String starttime,
			String endtime, String batchNum)
	{
		return dao.getQualifiedListByCity(cityId, starttime, endtime, batchNum);
	}

	/**
	 * 查询终端维修检测情况
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 终端维修检测情况列表
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryRepairDev(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.queryRepairDev(device_serialnumber, repair_vendor, device_vendor,
				device_model, attribution_city, send_city, manufacture_date_start,
				manufacture_date_end, curPage_splitPage, num_splitPage);
	}

	/**
	 * 导出终端维修检测情况
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @return 终端维修检测情况excel
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> parseExcel(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end)
	{
		return dao.parseExcel(device_serialnumber, repair_vendor, device_vendor,
				device_model, attribution_city, send_city, manufacture_date_start,
				manufacture_date_end);
	}

	/**
	 * 查询终端维修检测情况总数
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @param repair_vendor
	 *            返修厂家
	 * @param device_vendor
	 *            终端厂家
	 * @param device_model
	 *            终端型号
	 * @param attribution_city
	 *            归属城市
	 * @param send_city
	 *            发往城市
	 * @param manufacture_date_start
	 *            出厂时间起
	 * @param manufacture_date_end
	 *            出厂时间止
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return 终端维修检测情况总数
	 */
	public int countQueryRepairDev(String device_serialnumber, String repair_vendor,
			String device_vendor, String device_model, String attribution_city,
			String send_city, String manufacture_date_start, String manufacture_date_end,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.countQueryRepairDev(device_serialnumber, repair_vendor, device_vendor,
				device_model, attribution_city, send_city, manufacture_date_start,
				manufacture_date_end, curPage_splitPage, num_splitPage);
	}

	/**
	 * 查询终端独立档案
	 * 
	 * @param device_serialnumber
	 *            设备序列号
	 * @return 终端独立档案
	 */
	@SuppressWarnings("rawtypes")
	public Map queryRepairDevDetail(String device_serialnumber, String attribution_city)
	{
		return dao.queryRepairDevDetail(device_serialnumber, attribution_city);
	}

	/**
	 * 获取发送城市列表
	 * 
	 * @param attribution_city
	 *            归属城市ID
	 * @return 型号
	 */
	public String getSendCity(String attribution_city)
	{
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> treemap = new TreeMap<String, String>(); // 为了下拉框排序
		if ("00".contains(attribution_city))
		{
			map = Global.G_CityId_CityName_Map;
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				treemap.put(entry.getKey(), entry.getValue());
			}
		}
		else
		{
			treemap = CityDAO.getNextCityMapByCityPid(attribution_city);
		}
		StringBuffer bf = new StringBuffer();
		for (Map.Entry<String, String> entry : treemap.entrySet())
		{
			bf.append(entry.getKey());
			bf.append("$");
			bf.append(entry.getValue());
			bf.append("#");
		}
		bf.deleteCharAt(bf.length() - 1);
		return bf.toString();
	}

	/**
	 * 获取维修厂家列表
	 * 
	 * @return 维修厂家列表
	 */
	public List<Map<String, String>> getRepairVendor()
	{
		return dao.getRepairVendor();
	}

	/**
	 * 终端厂家列表
	 * 
	 * @return 终端厂家列表
	 */
	public List<Map<String, String>> getDevVendor()
	{
		return dao.getDevVendor();
	}

	/**
	 * 设备型号列表
	 * 
	 * @return 设备型号列表
	 */
	public List<Map<String, String>> getDevModel()
	{
		return dao.getDevModel();
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
	 * 获取所有终端厂商map 维修厂商Map<vendor_id,vendor_add>
	 */
	public Map<String, String> getDevVendorMap()
	{
		return dao.getDevVendorMap();
	}

	/**
	 * 获取所有维修厂商map 维修厂商Map<vendor_id,city_name>
	 */
	public Map<String, String> getRepairDevMap()
	{
		return dao.getRepairDevMap();
	}

	public void setDao(DevRepairTestInfoDAO dao)
	{
		this.dao = dao;
	}

	public int getQueryCount()
	{
		return dao.getQueryCount();
	}
}
