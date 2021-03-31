
package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.config.dao.StackRefreshCountDAO;
import com.linkage.module.gtms.config.dao.VoipChangeCountDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年2月10日
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class VoipChangeCountBIO
{

	private static Logger logger = LoggerFactory.getLogger(VoipChangeCountBIO.class);
	private VoipChangeCountDAO dao;

	public List<Map> queryVoipChangeInfo(String type, String cityId)
	{
		logger.warn("VoipChangeCountBIO--queryVoipChangeInfo(),type:{},cityID:{}", type, cityId);
		List date = this.dao.queryVoipChangeInfo(type);
		if ((date == null) || (date.isEmpty()))
		{
			return new ArrayList();
		}
		logger.warn("date:[]",date);

		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		List list = new ArrayList();
		for (int i = 0; i < cityList.size(); i++)
		{
			long countAll = 0L;
			long failnum = 0L;
			long successnum = 0L;
			long noupnum = 0L;
			String city_id = (String) cityList.get(i);
			ArrayList tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			Map tmap = new HashMap();
			tmap.put("", cityList);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = (String) tlist.get(j);
				for (int k = 0; k < date.size(); k++)
				{
					Map rmap = (Map) date.get(k);
					if (!cityId2.equals(rmap.get("city_id")))
						continue;
					String status = StringUtil.getStringValue(rmap.get("set_result"));
					if (("1".equals(status)))
					{
						successnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (!"".equals(status) && "-1".equals(status))
					{
						failnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else if (!"".equals(status) && "0".equals(status))
					{
						noupnum += StringUtil.getLongValue(rmap.get("total"));
					}
					else
					{
						countAll += StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			countAll = countAll + successnum + failnum+noupnum;
			tmap.put("allup", Long.valueOf(countAll));
			tmap.put("successnum", Long.valueOf(successnum));
			tmap.put("failnum", Long.valueOf(failnum));
			tmap.put("noupnum", Long.valueOf(noupnum));
			list.add(tmap);
			tlist = null;
		}
		return list;
	}

	public List<Map> getDev(String type, String status, String cityId, int curPage_splitPage, int num_splitPage)
	{
		return this.dao.getDevList(type, status, cityId, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String type, String status, String cityId, int curPage_splitPage, int num_splitPage)
	{
		return this.dao.getDevCount(type, status, cityId, curPage_splitPage, num_splitPage);
	}

	public List<Map> getDevExcel(String type, String cityId, String status)
	{
		return this.dao.getDevExcel(type, status, cityId);
	}

	public VoipChangeCountDAO getDao()
	{
		return dao;
	}

	public void setDao(VoipChangeCountDAO dao)
	{
		this.dao = dao;
	}
}
