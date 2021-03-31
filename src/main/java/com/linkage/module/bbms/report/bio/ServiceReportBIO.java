
package com.linkage.module.bbms.report.bio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.bbms.report.dao.ServiceReportDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings("unchecked")
public class ServiceReportBIO
{

	private static Logger logger = LoggerFactory.getLogger(ServiceReportBIO.class);
	private ServiceReportDAO serviceReportDao;

	/**
	 * 取的所有的业务类型
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getServiceTypeList()
	{
		return serviceReportDao.getServiceTypeList();
	}

	/**
	 * 根据属地、时间、业务获取业务列表
	 * 
	 * @author wangsenbo
	 * @param reportType
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getServiceList(String cityId, String stat_day1, String serviceId,
			String reportType)
	{
		return serviceReportDao.getServiceList(cityId, stat_day1, serviceId, reportType);
	}

	/**
	 * 统计业务使用情况
	 * 
	 * @author wangsenbo
	 * @date Mar 12, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> countService(String cityId, String stat_day1, String serviceId,
			String reportType)
	{
		logger.debug("countService({},{},{},{})", new Object[] { cityId, stat_day1,
				serviceId, reportType });
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		cityList.add(cityId);
		// 业务使用数
		List<Map<String, String>> cslist = serviceReportDao.countService(cityId,
				stat_day1, serviceId, reportType);
		// 所有业务类型
		List<Map<String, String>> slist = serviceReportDao.getServiceTypeList();
		// 处理得到的统计数据
		Map<String, Map<String, String>> csmap = new HashMap<String, Map<String, String>>();
		for (Map<String, String> smap : slist)
		{
			Map<String, String> map = new HashMap<String, String>();
			for (Map<String, String> cmap : cslist)
			{
				if (StringUtil.getStringValue(cmap.get("service_id")).equals(StringUtil.getStringValue(smap.get("service_id"))))
				{
					map.put(cmap.get("city_id"), StringUtil.getStringValue(cmap.get("succ_num")));
				}
			}
			csmap.put(StringUtil.getStringValue(smap.get("service_id")), map);
		}
		for (int i = 0; i < cityList.size(); i++)
		{
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));

			for (int j = 0; j < slist.size(); j++)
			{
				long total = 0;
				Map<String, String> smap = (Map) slist.get(j);
				for (int k = 0; k < tlist.size(); k++)
				{
					String cityId2 = tlist.get(k);
					total = total
							+ StringUtil.getLongValue(csmap.get(StringUtil.getStringValue(smap.get("service_id"))).get(cityId2));
				}
				tmap.put(smap.get("service_name"), total);
			}
			if (city_id.equals(cityId))
			{
				tmap.put("isAll", "1");
			}
			else
			{
				tmap.put("isAll", "0");
			}
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		return list;
	}

	/**
	 * @return the serviceReportDao
	 */
	public ServiceReportDAO getServiceReportDao()
	{
		return serviceReportDao;
	}

	/**
	 * @param serviceReportDao
	 *            the serviceReportDao to set
	 */
	public void setServiceReportDao(ServiceReportDAO serviceReportDao)
	{
		this.serviceReportDao = serviceReportDao;
	}

	public String[] getTitle(List<Map> titleList)
	{
		String[] title = new String[titleList.size()+1];
		title[0] = "属地";
		for (int i = 0; i < titleList.size(); i++)
		{
			Map<String, String> map = (Map)titleList.get(i);
			title[i+1] = map.get("service_name");				
		}
		return title;
	}

	public String[] getColumn(List<Map> titleList)
	{
		String[] column = new String[titleList.size()+1];
		column[0] = "city_name";
		for (int i = 0; i < titleList.size(); i++)
		{
			Map<String, String> map = (Map)titleList.get(i);
			column[i+1] = map.get("service_name");				
		}
		return column;
	}
}
