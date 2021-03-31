
package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.StbCategoryCountDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 湖南联通机顶盒按类别统计
 */
@SuppressWarnings("rawtypes")
public class StbCategoryCountBIO
{

	private static Logger logger = LoggerFactory.getLogger(StbCategoryCountBIO.class);
	private StbCountReportBioServ serv;
	private StbCategoryCountDAO dao;
	
	/**
	 * 按属地、设备类型统计数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> countResultByCity(String city_id,List<Map<String, String>> cityList)
	{
		List<Map<String, String>> resultList  = new ArrayList<Map<String, String>>();;    //返回结果
		Map<String, String> categoryMap = new HashMap<String, String>();    //串货设备
		Map<String, String> uncategoryMap = new HashMap<String, String>();   //行货设备
		Map<String, String> unKnownMap = new HashMap<String, String>();   //行货设备
		// 获取串货的设备
		List categoryList = dao.countCategoryDevByCity(city_id,1);
		// 获取串货的设备
		List unCategoryList = dao.countCategoryDevByCity(city_id,0);
		// 获取所有设备的集合 按city_id的下级地市分组
		List allList = dao.countResultByCity(city_id);
		categoryMap.put("category", "串");
		uncategoryMap.put("category", "行");
		unKnownMap.put("category", "未知");
		// 将需要展示的地市id放入内存
		for (Map<String, String> citylist : cityList)
		{
			categoryMap.put(StringUtil.getStringValue(citylist, "city_id", ""), "0");
			uncategoryMap.put(StringUtil.getStringValue(citylist, "city_id", ""), "0");
			unKnownMap.put(StringUtil.getStringValue(citylist, "city_id", ""), "0");
		}
		/**
		 * 分地市处理数据
		 */
		int cateTotal_sum = 0;
		int uncateTotal_sum = 0;
		int unKnownTOtal_sum = 0;
		if (allList != null && !allList.isEmpty())
		{
			for (int i = 0; i < allList.size(); i++)
			{
				Map<String, String> sumMap = (Map<String, String>) allList.get(i);
				String cityId = StringUtil.getStringValue(sumMap, "city_id", "");
				int sum = StringUtil.getIntValue(sumMap, "d_cum", 0); // 该地市设备总量
				int catecount = 0; // 该地市及下级地市串货设备量
				for (int j = 0; j < categoryList.size(); j++)
				{
					Map<String, String> cateMap = (Map<String, String>) categoryList
							.get(j);
					String ch_city = StringUtil.getStringValue(cateMap, "city_id", "");
					if("00".equals(city_id))
					{
						ArrayList<String> citys = CityDAO.getAllNextCityIdsByCityPid(cityId);
						if (!"00".equals(cityId) && citys.contains(ch_city))
						{
							catecount += StringUtil.getIntValue(cateMap, "num", 0);
						}else if(cityId.equals(ch_city)) 
						{
							catecount += StringUtil.getIntValue(cateMap, "num", 0);
						}
					}
					else if (cityId.equals(ch_city))
					{
						catecount += StringUtil.getIntValue(cateMap, "num", 0);
					}
				}
				int unCatecount = 0; // 该地市及下级地市行货设备量
				for (int j = 0; j < unCategoryList.size(); j++)
				{
					Map<String, String> unCateMap = (Map<String, String>) unCategoryList
							.get(j);
					String ch_city = StringUtil.getStringValue(unCateMap, "city_id", "");
					if("00".equals(city_id))
					{
						ArrayList<String> citys = CityDAO.getAllNextCityIdsByCityPid(cityId);
						if (!"00".equals(cityId) && citys.contains(ch_city))
						{
							unCatecount += StringUtil.getIntValue(unCateMap, "num", 0);
						}else if(cityId.equals(ch_city)) 
						{
							unCatecount += StringUtil.getIntValue(unCateMap, "num", 0);
						}
					}
					else if (cityId.equals(ch_city))
					{
						unCatecount += StringUtil.getIntValue(unCateMap, "num", 0);
					}
				}
				
				cateTotal_sum += catecount;
				uncateTotal_sum += unCatecount;
				unKnownTOtal_sum += sum-catecount-unCatecount;
				categoryMap.put(cityId, catecount + "");
				uncategoryMap.put(cityId, unCatecount + "");
				unKnownMap.put(cityId, (sum-catecount-unCatecount) + "");
			}
		}
		categoryMap.put("total_num", cateTotal_sum + "");
		uncategoryMap.put("total_num", uncateTotal_sum + "");
		unKnownMap.put("total_num", unKnownTOtal_sum + "");
		resultList.add(categoryMap);
		resultList.add(uncategoryMap);
		resultList.add(unKnownMap);
		/**
		 * 统计数据
		 */
		Map<String, String> rmt = new HashMap<String, String>();
		rmt.put("category", "小计");
		long total_num = 0;
		long city_num = 0;
		for (Map<String, String> cm : cityList)
		{
			city_num = 0;
			for (Map<String, String> mp : resultList)
			{
				for (String key : mp.keySet())
				{
					if (cm.get("city_id").equals(key))
					{
						city_num += StringUtil.getLongValue(mp.get(key));
					}
				}
			}
			rmt.put(cm.get("city_id"), city_num + "");
			total_num += city_num;
		}
		rmt.put("total_num", total_num + "");
		resultList.add(rmt);
		logger.debug("resultList:[{}]",resultList.toString());
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List cateDateHandle(List list)
	{
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++)
		{
			Map returnMap = new HashMap();
			Map<String, String> map = (Map<String, String>) list.get(i);
			String city_id = map.get("city_id");
			Object obj = map.get("c_num");
			String c_num = Integer.parseInt(String.valueOf(obj)) + "";
			returnMap.put(city_id, c_num);
			resultList.add(returnMap);
		}
		return resultList;
	}

	/**
	 * 按厂商、设备类型统计数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> countResultByVendor(String vendor_id,
			List<HashMap<String, String>> vendorList)
	{
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		// 返回结果
		Map<String, String> categoryMap = new HashMap<String, String>(); // 串货设备
		Map<String, String> uncategoryMap = new HashMap<String, String>(); // 行货设备
		Map<String, String> unKnownMap = new HashMap<String, String>(); // 未知设备
		categoryMap.put("category", "串");
		uncategoryMap.put("category", "行");
		unKnownMap.put("category", "未知");
		// 将需要展示的地市id放入内存
		for (Map<String, String> vendorlist : vendorList)
		{
			categoryMap.put(StringUtil.getStringValue(vendorlist, "vendor_id", ""), "0");
			uncategoryMap.put(StringUtil.getStringValue(vendorlist, "vendor_id", ""), "0");
			unKnownMap.put(StringUtil.getStringValue(vendorlist, "vendor_id", ""), "0");
		}
		List allList = dao.countResultByVendor(vendor_id);// 获取所有设备量
		List categoryList = dao.countCategoryDevByVendor(vendor_id,1);// 获取串货的设备
		List unCategoryList = dao.countCategoryDevByVendor(vendor_id,0);// 获取串货的设备
		/**
		 * 分厂商处理数据
		 */

		int cateTotal_sum = 0;
		int uncateTotal_sum = 0;
		int unKnownTotal_sum = 0;
		if (allList != null && !allList.isEmpty())
		{
			for (Object object : allList)
			{
				Map<String, String> sumMap = (Map<String, String>) object;
				String vendorId = sumMap.get("vendor_id");
				int sum = StringUtil.getIntValue(sumMap, "d_cum", 0); // 该厂商设备总量
				int catecount = 0; // 该厂商串货设备量
				for (int j = 0; j < categoryList.size(); j++)
				{
					Map<String, String> cateMap = (Map<String, String>) categoryList
							.get(j);
					String ch_vendor = StringUtil.getStringValue(cateMap, "vendor_id", "");
					if (vendorId.equals(ch_vendor))
					{
						catecount = StringUtil.getIntValue(cateMap, "c_num", 0);
					}
				}
				int  unCatecount = 0; // 该厂商行货设备量
				for (int j = 0; j < unCategoryList.size(); j++)
				{
					Map<String, String> unCateMap = (Map<String, String>) unCategoryList
							.get(j);
					String ch_vendor = StringUtil.getStringValue(unCateMap, "vendor_id", "");
					if (vendorId.equals(ch_vendor))
					{
						unCatecount = StringUtil.getIntValue(unCateMap, "c_num", 0);
					}
				}
				cateTotal_sum += catecount;
				uncateTotal_sum += unCatecount;
				unKnownTotal_sum += sum - catecount - unCatecount;
				
				categoryMap.put(vendorId, catecount + "");
				uncategoryMap.put(vendorId, unCatecount + "");
				unKnownMap.put(vendorId, ( sum - catecount - unCatecount)+"");
			}
		}
		categoryMap.put("total_num", cateTotal_sum + "");
		uncategoryMap.put("total_num",uncateTotal_sum + "");
		unKnownMap.put("total_num",unKnownTotal_sum + "");
		resultList.add(categoryMap);
		resultList.add(uncategoryMap);
		resultList.add(unKnownMap);
		/**
		 * 统计数据
		 */
		Map<String, String> rmt = new HashMap<String, String>();
		rmt.put("category", "小计");
		long total_num = 0;
		long vendor_num = 0;
		for (Map<String, String> cm : vendorList)
		{
			vendor_num = 0;
			for (Map<String, String> mp : resultList)
			{
				for (String key : mp.keySet())
				{
					if (cm.get("vendor_id").equals(key))
					{
						vendor_num += StringUtil.getLongValue(mp.get(key));
					}
				}
			}
			rmt.put(cm.get("vendor_id"), vendor_num + "");
			total_num += vendor_num;
		}
		rmt.put("total_num", total_num + "");
		resultList.add(rmt);
		logger.debug("resultList:[{}]",resultList.toString());;
		return resultList;
	}
	/**
	 * 拼装页面table
	 */
	public String toConversion(String type, List<Map<String, String>> data,
			String city_id, List<HashMap<String, String>> cityListOrder)
	{
		return serv.toConversion(type, data, city_id, cityListOrder);
	}
	
	/**
	 * 拼装页面table  厂商用
	 */
	public String toConversion4Vendor(String type, List<Map<String, String>> data,
			String vendor_id, List<HashMap<String, String>> vendorList)
	{
		return serv.toConversion4Vendor(type, data, vendor_id, vendorList);
	}

	/**
	 * 将String转成List
	 */
	public List<Map<String, String>> stringToList(String time, String dataType)
	{
		return serv.stringToList(time, dataType);
	}

	/**
	 * 查询详细设备信息
	 */
	public List<Map<String, String>> countResultByCity(String type,
			String city_id, String cityType, String version, int curPage_splitPage,
			int num_splitPage)
	{
		return dao.countResultByCityCategory(type, city_id, cityType, version,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取设备总量
	 */
	public int getCountResultByCity(String type, String city_id, String cityType,
			String version)
	{
		return dao.getCountResultByCityVersion(type, city_id, cityType, version);
	}

	/**
	 * 获取厂商
	 */
	public ArrayList<HashMap<String, String>> getVendorList(String vendor_id)
	{
		return dao.getVendorList(vendor_id);
	}
	
	/**
	 * @param city_id
	 * @return 2019-9-12
	 */
	public ArrayList<HashMap<String, String>> getNextCityListOrderByCityPid(String city_id)
	{
		// TODO Auto-generated method stub
		return dao.getNextCityListOrderByCityPid(city_id);
	}

	public StbCategoryCountDAO getDao()
	{
		return dao;
	}

	public void setDao(StbCategoryCountDAO dao)
	{
		this.dao = dao;
	}

	public StbCountReportBioServ getServ()
	{
		return serv;
	}

	public void setServ(StbCountReportBioServ serv)
	{
		this.serv = serv;
	}
}
