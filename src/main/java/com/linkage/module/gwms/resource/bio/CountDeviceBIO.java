
package com.linkage.module.gwms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.report.obj.DevicetypeChildOBJ;
import com.linkage.module.gwms.report.obj.DevicetypeNewestFindReportOBJ;
import com.linkage.module.gwms.resource.dao.CountDeviceDAO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2012-3-6 上午09:15:02
 * @category com.linkage.module.gwms.resource.bio
 * @copyright 南京联创科技 网管科技部
 */
public class CountDeviceBIO
{

	private CountDeviceDAO dao;

	public List<Map> queryCityName(String cityId)
	{
		return dao.queryCityName(cityId);
	}

	public List<String> filterDevice(String cityId, String vendor, String devicemodel,
			String devicetype, String protocol2, String protocol1, String protocol0)
	{
		return dao.filterDevice(cityId, vendor, devicemodel, devicetype, protocol2,
				protocol1, protocol0);
	}

	public List<Map> getDevicetype(List<String> list, String is_check,
			String rela_dev_type, String startTime, String endTime)
	{
		return dao.getDevicetype(list, is_check, rela_dev_type, startTime, endTime);
	}

	public List<Map> getData(List<Map> devicetypeList, List<Map> cityList,
			String userCityId)
	{
		return dao.getData(devicetypeList, cityList, userCityId);
	}

	public List<Map> queryDetail(String userCityId, String devicetype, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.queryDetail(userCityId, devicetype, cityId, curPage_splitPage,
				num_splitPage);
	}

	public int getCountDevice(String userCityId, String devicetype, String cityId,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.getCountDevice(userCityId, devicetype, cityId, curPage_splitPage,
				num_splitPage);
	}

	public List<Map> queryDetailForExcel(String userCityId, String devicetype,
			String cityId)
	{
		return dao.queryDetailForExcel(userCityId, devicetype, cityId);
	}

	public List<String> getEndAll(List dataList, List<Map> cityList,
			List<Map> devicetypeList)
	{
		Map<String, String> totalMap = new HashMap<String, String>();
		String filterDevicetype = "";
		if(null ==dataList ||  dataList.size()==0){
			return  null;
		}
		for (int i = 0; i < devicetypeList.size(); i++)
		{
			filterDevicetype = filterDevicetype
					+ StringUtil.getStringValue(devicetypeList.get(i)
							.get("devicetype_id")) + ",";
		}
		
		filterDevicetype = filterDevicetype.substring(0, filterDevicetype.length() - 1);
		for (int i = 0; i < cityList.size(); i++)
		{
			totalMap.put(StringUtil.getStringValue(cityList.get(i).get("city_id")), "0");
		}
		totalMap.put("-1", "0");
		// dataList.size()
		for (int i = 0; i < dataList.size(); i++)
		{
			DevicetypeNewestFindReportOBJ vendor = new DevicetypeNewestFindReportOBJ();
			vendor = (DevicetypeNewestFindReportOBJ) dataList.get(i);
			List<DevicetypeChildOBJ> devicemodelList = vendor.getChildList();
			// devicemodelList.size()
			for (int j = 0; j < devicemodelList.size(); j++)
			{
				List<List> num = devicemodelList.get(j).getNum();
				// logger.warn("num的大小："+num.size());
				for (int m = 0; m < num.size(); m++)
				{
					List devicetyList = num.get(m);
					// logger.warn("devicetyList的大小："+devicetyList.size());
					for (int n = 0; n < cityList.size(); n++)
					{
						// logger.warn("当前的cityid："+StringUtil.getStringValue(cityList.get(n).get("city_id")));
						int temptotal = StringUtil.getIntegerValue(totalMap
								.get(StringUtil.getStringValue(cityList.get(n).get(
										"city_id"))));
						if (!"0".equals((String) devicetyList.get(n + 4)))
						{
							Pattern p1 = Pattern.compile("(?<=>).+?(?=</a>)");
							Matcher m1 = p1.matcher((String) devicetyList.get(n + 4)
									.toString());
							while (m1.find())
							{
								temptotal = temptotal
										+ StringUtil.getIntegerValue(m1.group().trim());
							}
						}
						totalMap.put(StringUtil.getStringValue(cityList.get(n).get(
								"city_id")), String.valueOf(temptotal));
					}
					int temptotalAll = StringUtil.getIntegerValue(totalMap.get("-1"));
					if (!"0".equals((String) devicetyList.get(cityList.size() + 4)))
					{
						Pattern p2 = Pattern.compile("(?<=>).+?(?=</a>)");
						Matcher m2 = p2.matcher((String) devicetyList.get(
								cityList.size() + 4).toString());
						while (m2.find())
						{
							temptotalAll = temptotalAll
									+ StringUtil.getIntegerValue(m2.group().trim());
						}
						totalMap.put("-1", String.valueOf(temptotalAll));
					}
				}
			}
		}
		List<String> endTotal = new ArrayList<String>();
		for (int i = 0; i < cityList.size(); i++)
		{
			int endNum = StringUtil.getIntegerValue(totalMap.get(StringUtil
					.getStringValue(cityList.get(i).get("city_id"))));
			if (endNum > 0)
			{
				endTotal.add("<a href=\"javascript:detailAll('"
						+ StringUtil.getStringValue(cityList.get(i).get("city_id"))
						+ "','" + filterDevicetype + "')\">" + endNum + "</a>");
			}
			else
			{
				endTotal.add("0");
			}
		}
		int endTotalAll = StringUtil.getIntegerValue(totalMap.get("-1"));
		// logger.warn("最后的小计："+endTotalAll);
		if (endTotalAll > 0)
		{
			endTotal.add("<a href=\"javascript:detailAll('-1','" + filterDevicetype
					+ "')\">" + endTotalAll + "</a>");
		}
		else
		{
			endTotal.add("0");
		}
		return endTotal;
	}

	public List<Map> queryDetailAll(String userCityId, String filterDevicetype,
			String cityId, int curPage_splitPage, int num_splitPage)
	{
		return dao.queryDetailAll(userCityId, filterDevicetype, cityId,
				curPage_splitPage, num_splitPage);
	}

	public int queryDetailAllCount(String userCityId, String filterDevicetype,
			String cityId, int curPage_splitPage, int num_splitPage)
	{
		return dao.queryDetailAllCount(userCityId, filterDevicetype, cityId,
				curPage_splitPage, num_splitPage);
	}

	public List<Map> queryDetailForExcelAll(String userCityId, String filterDevicetype,
			String cityId)
	{
		return dao.queryDetailForExcelAll(userCityId, filterDevicetype, cityId);
	}

	public CountDeviceDAO getDao()
	{
		return dao;
	}

	public void setDao(CountDeviceDAO dao)
	{
		this.dao = dao;
	}
}
