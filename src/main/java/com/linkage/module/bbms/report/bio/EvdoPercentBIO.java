
package com.linkage.module.bbms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.bbms.report.dao.EvdoPercentDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.CustTypeDAO;
import com.linkage.module.gwms.util.StringUtil;

public class EvdoPercentBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(EvdoPercentBIO.class);
	private EvdoPercentDAO evdoPercentDao;

	/**
	 * @param evdoPercentDao
	 *            the evdoPercentDao to set
	 */
	public void setEvdoPercentDao(EvdoPercentDAO evdoPercentDao)
	{
		this.evdoPercentDao = evdoPercentDao;
	}

	public String percent(long p1, long p2)
	{
		double p3;
		if (p1 == 0)
		{
			p3 = 0;
		}
		else
		{
			p3 = (double) p2 / p1;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	/**
	 * EVDO网关占比
	 * 
	 * @author wangsenbo
	 * @date Nov 19, 2009
	 * @return List<Map>
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getEvdoPercent(String type, String starttime1, String endtime1,
			UserRes curUser)
	{
		logger.debug("getEvdoPercent({},{},{})", new Object[] { type, starttime1,
				endtime1 });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		if ("1".equals(type))
		{
			Map cityMap = CityDAO.getCityIdCityNameMap();
			//获取登陆用户属地的下一层属地ID(不包含自己)
			ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(curUser
					.getCityId());
			Map cmap = evdoPercentDao.getCityTotal(starttime1, endtime1);
			Map emap = evdoPercentDao.getCityEvdoTotal(starttime1, endtime1);			
			for (int i = 0; i < cityList.size(); i++)
			{
				Map<String, Object> tmap = new HashMap<String, Object>();
				String city_id = cityList.get(i);
				ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
				tmap.put("city_id", city_id);
				tmap.put("city_name", cityMap.get(city_id));
				long total = 0;
				long evdototal = 0;
				for(int j = 0;j<tlist.size();j++){
					total = total+StringUtil.getLongValue(cmap.get(tlist.get(j)));
					evdototal = evdototal + StringUtil.getLongValue(emap.get(tlist.get(j)));
				}
				String percent = percent(total, evdototal);
				tmap.put("total", total);
				tmap.put("evdototal", evdototal);
				tmap.put("percent", percent);
				list.add(tmap);
				tlist = null;
			}
			Map<String, Object> amap = new HashMap();
			amap.put("city_id", curUser.getCityId());
			amap.put("city_name", cityMap.get(curUser.getCityId()));
			long atotal = StringUtil.getLongValue(cmap.get(curUser.getCityId()));
			long aevdototal = StringUtil.getLongValue(emap.get(curUser.getCityId()));
			String apercent = percent(atotal, aevdototal);
			amap.put("total", atotal);
			amap.put("evdototal", aevdototal);
			amap.put("percent", apercent);
			list.add(amap);
			cityMap = null;
			cityList = null;
		}
		// 按行业统计
		if ("2".equals(type))
		{
			List custTypeList = CustTypeDAO.getAllCustType();
			ArrayList<String> cityList = null;
			if (false == curUser.getUser().isAdmin())
			{
				cityList = CityDAO.getAllNextCityIdsByCityPid(curUser.getCityId());
			}
			Map wmap = evdoPercentDao.getWayTotal(starttime1, endtime1, cityList);
			Map emap = evdoPercentDao.getWayEvdoTotal(starttime1, endtime1, cityList);
			cityList = null;
			Map<String, Object> amap = new HashMap();
			amap.put("cust_type_name", "总计");
			long atotal = 0;
			long aevdototal = 0;
			for (int i = 0; i < custTypeList.size(); i++)
			{
				Map tmap = (Map) custTypeList.get(i);
				long total = StringUtil.getLongValue(wmap.get(tmap.get("cust_type_id")));
				long evdototal = StringUtil.getLongValue(emap.get(tmap
						.get("cust_type_id")));
				atotal = atotal + total;
				aevdototal = aevdototal + evdototal;
				String percent = percent(total, evdototal);
				tmap.put("total", total);
				tmap.put("evdototal", evdototal);
				tmap.put("percent", percent);
				list.add(tmap);
			}
			String apercent = percent(atotal, aevdototal);
			amap.put("total", atotal);
			amap.put("evdototal", aevdototal);
			amap.put("percent", apercent);
			list.add(amap);
		}
		return list;
	}

	public String[] getTitle(String type)
	{
		logger.debug("getTitle({})", type);
		String[] title = new String[4];
		if ("1".equals(type))
		{
			title[0] = "本地网";
			title[1] = "总网关";
			title[2] = "EVDO网关";
			title[3] = "占比";
		}
		if ("2".equals(type))
		{
			title[0] = "行业";
			title[1] = "总网关";
			title[2] = "EVDO网关";
			title[3] = "占比";
		}
		return title;
	}

	public String[] getColumn(String type)
	{
		logger.debug("getColumn({})", type);
		String[] column = new String[4];
		if ("1".equals(type))
		{
			column[0] = "city_name";
			column[1] = "total";
			column[2] = "evdototal";
			column[3] = "percent";
		}
		if ("2".equals(type))
		{
			column[0] = "cust_type_name";
			column[1] = "total";
			column[2] = "evdototal";
			column[3] = "percent";
		}
		return column;
	}
}
