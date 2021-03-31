
package com.linkage.module.gwms.report.bio;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.report.dao.BridgeToRouteCountDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 桥接、路由统计
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class BridgeToRouteCountBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(BridgeToRouteCountBIO.class);
	private BridgeToRouteCountDAO dao;

	/**
	 * 桥接、路由方式统计
	 * 
	 * @author wangsenbo
	 * @date Jul 19, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> count(String starttime1, String endtime1, String cityId)
	{
		logger.debug("count({},{},{}", new Object[] { starttime1, endtime1, cityId });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		List typeList = dao.count(starttime1, endtime1, cityId);
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 桥接用户数
			long bridgetotal = 0;
			// 路由用户数
			long routetotal = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				for (int k = 0; k < typeList.size(); k++)
				{
					Map rmap = (Map) typeList.get(k);
					if (cityId2.equals(rmap.get("city_id")))
					{
						// // 桥接用户数
						if ("1".equals(StringUtil
								.getStringValue(rmap.get("parm_type_id"))))
						{
							bridgetotal = bridgetotal
									+ StringUtil.getLongValue(rmap.get("total"));
						}
						// 路由用户数
						if ("2".equals(StringUtil
								.getStringValue(rmap.get("parm_type_id"))))
						{
							routetotal = routetotal
									+ StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			tmap.put("bridgetotal", bridgetotal);
			tmap.put("routetotal", routetotal);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	/**
	 * 桥接、路由方式统计2
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @return
	 */
	public List<Map> count2(String starttime1, String endtime1, String cityId)
	{
		logger.debug("count({},{},{}", new Object[] { starttime1, endtime1, cityId });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(0, cityId);
		List typeList = dao.count2(starttime1, endtime1, cityId);
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 桥接用户数
			long bridgetotal = 0;
			// 路由用户数
			long routetotal = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				for (int k = 0; k < typeList.size(); k++)
				{
					Map rmap = (Map) typeList.get(k);
					if (cityId2.equals(rmap.get("city_id")))
					{
						// // 桥接用户数
						if ("1".equals(StringUtil.getStringValue(rmap.get("wan_type"))))
						{
							bridgetotal = bridgetotal
									+ StringUtil.getLongValue(rmap.get("total"));
						}
						// 路由用户数
						if ("2".equals(StringUtil.getStringValue(rmap.get("wan_type"))))
						{
							routetotal = routetotal
									+ StringUtil.getLongValue(rmap.get("total"));
						}
					}
				}
			}
			tmap.put("bridgetotal", bridgetotal);
			tmap.put("routetotal", routetotal);
			
			long total = bridgetotal + routetotal;
			if(0L != total){
				
				DecimalFormat df = new DecimalFormat("0.00");
				float bridgePercent = new BigDecimal(100 *  ((float)bridgetotal/total)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				
				tmap.put("bridgePercent", df.format(bridgePercent));
				tmap.put("routePercent", df.format(100 - bridgePercent));
			}else{
				tmap.put("bridgePercent", "0.00");
				tmap.put("routePercent", "0.00");
			}
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public static void main(String[] args) {
		float a = 1.19f;
		System.out.println(100 -a);
	}
	
	/**
	 * 导出Excel
	 * 
	 * @param starttime1
	 * @param endtime1
	 * @param cityId
	 * @return
	 */
	public List<Map> exportExcel2(String starttime1, String endtime1, String cityId, String sessionType)
	{
		logger.debug("count({},{},{}", new Object[] { starttime1, endtime1, cityId });
		return dao.exportExcel2(starttime1, endtime1, cityId, sessionType);
	}

	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String sessionType, int curPage_splitPage, int num_splitPage)
	{
		return dao.getHgwList(starttime1, endtime1, cityId, sessionType,
				curPage_splitPage, num_splitPage);
	}

	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String sessionType, int curPage_splitPage, int num_splitPage)
	{
		return dao.getHgwCount(starttime1, endtime1, cityId, sessionType,
				curPage_splitPage, num_splitPage);
	}

	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String sessionType)
	{
		return dao.getHgwExcel(starttime1, endtime1, cityId, sessionType);
	}

	/**
	 * @return the dao
	 */
	public BridgeToRouteCountDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BridgeToRouteCountDAO dao)
	{
		this.dao = dao;
	}
}
