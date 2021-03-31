
package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-8-6
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
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
import com.linkage.module.itms.report.dao.BusOnceDownSucDAONxdx;

public class BusOnceDownSucBIONxdx
{

	private static Logger logger = LoggerFactory
			.getLogger(BusOnceDownSucBIONxdx.class);
	private BusOnceDownSucDAONxdx busOnceDownSucDAO;

	public List<Map<String, Object>> queryDataList(String cityId, String starttime1,
			String endtime1, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		logger.debug("queryDataList({},{},{})", new Object[] { cityId, starttime1,
				endtime1 });
		List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tempValue = busOnceDownSucDAO.getDataList(cityId,
				starttime1, endtime1, gwType, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
		// 按属地统计
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		if (null != cityList && cityList.size() > 0)
		{
			String city_id = "";
			List<String> cCityID = null;
			Map<String, Object> tempMap = null;
			long totalSucNum = 0l; // 注意是0和字母L 总的成功数
			long totalNum = 0l; // 总数
			long broadbandTotalNum = 0l; // 宽带 总数
			long broadbandSucNum = 0l; // 宽带 成功绑定数
			long iptvTotalNum = 0l; // IPTV 总数
			long iptvSucNum = 0l; // IPTV 成功绑定数
			long voipTotalNum = 0l; // VOIP 总数
			long voipSucNum = 0l; // VOIP成功绑定数
			for (int i = 0; i < cityList.size(); i++)
			{
				city_id = cityList.get(i);
				// 所有子属地ID(包括自己)
				cCityID = CityDAO.getAllNextCityIdsByCityPid(city_id);
				tempMap = new HashMap<String, Object>();
				totalSucNum = 0l;
				totalNum = 0l;
				broadbandTotalNum = 0l;
				broadbandSucNum = 0l;
				iptvTotalNum = 0l;
				iptvSucNum = 0l;
				voipTotalNum = 0l;
				voipSucNum = 0l;
				if (null != tempValue && tempValue.size() > 0)
				{
					String cityID = "";
					for (int j = 0; j < tempValue.size(); j++)
					{
						cityID = StringUtil.getStringValue(tempValue.get(j)
								.get("city_id"));
						for (int k = 0; k < cCityID.size(); k++)
						{
							// 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
							if (cityID.equals(cCityID.get(k)))
							{
								totalNum += StringUtil.getLongValue(tempValue.get(j).get(
										"num"));
								// 如果是is_check为1则该版本为规范版本
								if (1 == StringUtil.getLongValue(tempValue.get(j).get(
										"open_status")))
								{
									totalSucNum += StringUtil.getLongValue(tempValue.get(
											j).get("num"));
								}
								// 宽带
								if ("10".equals(StringUtil.getStringValue(tempValue
										.get(j).get("serv_type_id"))))
								{
									broadbandTotalNum += StringUtil
											.getLongValue(tempValue.get(j).get("num"));
									if (1 == StringUtil.getLongValue(tempValue.get(j)
											.get("open_status")))
									{
										broadbandSucNum += StringUtil
												.getLongValue(tempValue.get(j).get("num"));
									}
								}
								// IPTV
								if ("11".equals(StringUtil.getStringValue(tempValue
										.get(j).get("serv_type_id"))))
								{
									iptvTotalNum += StringUtil.getLongValue(tempValue
											.get(j).get("num"));
									if (1 == StringUtil.getLongValue(tempValue.get(j)
											.get("open_status")))
									{
										iptvSucNum += StringUtil.getLongValue(tempValue
												.get(j).get("num"));
									}
								}
								// VOIP
								if ("14".equals(StringUtil.getStringValue(tempValue
										.get(j).get("serv_type_id"))))
								{
									voipTotalNum += StringUtil.getLongValue(tempValue
											.get(j).get("num"));
									if (1 == StringUtil.getLongValue(tempValue.get(j)
											.get("open_status")))
									{
										voipSucNum += StringUtil.getLongValue(tempValue
												.get(j).get("num"));
									}
								}
							}
						}
					}
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("broadbandSucRate",
							percent(broadbandSucNum, broadbandTotalNum));
					tempMap.put("iptvSucRate", percent(iptvSucNum, iptvTotalNum));
					tempMap.put("voipSucRate", percent(voipSucNum, voipTotalNum));
					tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
				}
				else
				{
					// 当查询的结果为空时，所有值赋值0
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("broadbandSucRate",
							percent(broadbandSucNum, broadbandTotalNum));
					tempMap.put("iptvSucRate", percent(iptvSucNum, iptvTotalNum));
					tempMap.put("voipSucRate", percent(voipSucNum, voipTotalNum));
					tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
				}
				returnValue.add(tempMap);
			}
		}
		return returnValue;
	}
	
	public List<Map<String, Object>> queryStbDataList(String cityId, String starttime1,
			String endtime1, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		logger.debug("queryDataList({},{},{})", new Object[] { cityId, starttime1,
				endtime1 });
		List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
		List<HashMap<String, String>> tempValue = busOnceDownSucDAO.getStbDataList(cityId,
				starttime1, endtime1, gwType, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
		// 按属地统计
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		if (null != cityList && cityList.size() > 0)
		{
			String city_id = "";
			List<String> cCityID = null;
			Map<String, Object> tempMap = null;
			long totalSucNum = 0l; // 注意是0和字母L 总的成功数
			long totalNum = 0l; // 总数
			for (int i = 0; i < cityList.size(); i++)
			{
				city_id = cityList.get(i);
				// 所有子属地ID(包括自己)
				cCityID = CityDAO.getAllNextCityIdsByCityPid(city_id);
				tempMap = new HashMap<String, Object>();
				totalSucNum = 0l;
				totalNum = 0l;
				if (null != tempValue && tempValue.size() > 0)
				{
					String cityID = "";
					for (int j = 0; j < tempValue.size(); j++)
					{
						cityID = StringUtil.getStringValue(tempValue.get(j)
								.get("city_id"));
						for (int k = 0; k < cCityID.size(); k++)
						{
							// 如果结果中的cityID和city_id相等或者是它的上一级和city_id相等就将该总数计算出来
							if (cityID.equals(cCityID.get(k)))
							{
								totalNum += StringUtil.getLongValue(tempValue.get(j).get(
										"num"));
								// 如果是is_check为1则该版本为成功版本
								if (1 == StringUtil.getLongValue(tempValue.get(j).get(
										"user_status")))
								{
									totalSucNum += StringUtil.getLongValue(tempValue.get(
											j).get("num"));
								}
							}
						}
					}
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
				}
				else
				{
					// 当查询的结果为空时，所有值赋值0
					tempMap.put("cityId", city_id);
					tempMap.put("cityName", cityMap.get(city_id));
					tempMap.put("totalSucRate", percent(totalSucNum, totalNum));
				}
				returnValue.add(tempMap);
			}
		}
		return returnValue;
	}

	public List<Map> getServInfoDetail(String cityId, String starttime1, String endtime1,
			String servTypeId, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		return busOnceDownSucDAO.getServInfoDetail(cityId, starttime1, endtime1,
				servTypeId, curPage_splitPage, num_splitPage, gwType, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId);
	}
	
	public List<Map> getStbServInfoDetail(String cityId, String starttime1, String endtime1,
			String servTypeId, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		return busOnceDownSucDAO.getStbServInfoDetail(cityId, starttime1, endtime1,
				servTypeId, curPage_splitPage, num_splitPage, gwType, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId);
	}

	public int getServInfoCount(String cityId, String starttime1, String endtime1,
			String servTypeId, int curPage_splitPage, int num_splitPage, String gwType,
			String gwShare_vendorId, String gwShare_deviceModelId,
			String gwShare_devicetypeId)
	{
		return busOnceDownSucDAO.getServInfoCount(cityId, starttime1, endtime1,
				servTypeId, curPage_splitPage, num_splitPage, gwType, gwShare_vendorId,
				gwShare_deviceModelId, gwShare_devicetypeId);
	}

	public List<Map<String, Object>> getServInfoExcel(String cityId, String starttime1,
			String endtime1, String servTypeId, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		return busOnceDownSucDAO.getServInfoExcel(cityId, starttime1, endtime1,
				servTypeId, gwType, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
	}
	
	public List<Map<String, Object>> getStbServInfoExcel(String cityId, String starttime1,
			String endtime1, String servTypeId, String gwType, String gwShare_vendorId,
			String gwShare_deviceModelId, String gwShare_devicetypeId)
	{
		return busOnceDownSucDAO.getStbServInfoExcel(cityId, starttime1, endtime1,
				servTypeId, gwType, gwShare_vendorId, gwShare_deviceModelId,
				gwShare_devicetypeId);
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
			if (LipossGlobals.inArea(Global.NXDX))
			{
				return "0.00%";
			}
			else
			{
				return "N/A";
			}
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

	public BusOnceDownSucDAONxdx getBusOnceDownSucDAO()
	{
		return busOnceDownSucDAO;
	}

	public void setBusOnceDownSucDAO(BusOnceDownSucDAONxdx busOnceDownSucDAO)
	{
		this.busOnceDownSucDAO = busOnceDownSucDAO;
	}
}
