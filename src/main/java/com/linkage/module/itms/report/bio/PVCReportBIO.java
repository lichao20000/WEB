
package com.linkage.module.itms.report.bio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.PVCReportDAO;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class PVCReportBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PVCReportBIO.class);
	private PVCReportDAO pvcReportDao;

	public String[] getTitle()
	{
		logger.debug("getTitle()");
		String[] title = new String[4];
		title[0] = "属地";
		title[1] = "已部署数";
		title[2] = "未部署数";
		title[3] = "部署率";
		return title;
	}

	public String[] getColumn()
	{
		logger.debug("getColumn({})");
		String[] column = new String[4];
		column[0] = "city_name";
		column[1] = "detotal";
		column[2] = "nototal";
		column[3] = "percent";
		return column;
	}

	public String percent(long p1, long p2)
	{
		double p3;
		if (p1 == 0 && p2 == 0)
		{
			return "N/A";
		}
		else
		{
			p3 = (double) p1 / (p1 + p2);
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	/**
	 * 统计iptv考核的用户数
	 * 
	 * @author wangsenbo
	 * @param isNew
	 * @date Feb 25, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> countPVC(String starttime1, String endtime1, String cityId,
			String isNew, String prodSpecId,String isItv)
	{
		logger.debug("countPVC({},{},{})", new Object[] { starttime1, endtime1, cityId });
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(cityId);
		Collections.sort(cityList);
		Map demap = pvcReportDao.countDeployedPVC(starttime1, endtime1, isNew, prodSpecId, isItv);
		Map nomap = pvcReportDao.countNonDeployPVC(starttime1, endtime1, isNew, prodSpecId, isItv);
		// 已部署数
		long detotal = 0;
		// 未部署数
		long nototal = 0;
		ArrayList<String> tlist = null;
		// 选择的属地
		Map<String, Object> amap = new HashMap();
		amap.put("city_id", cityId);
		amap.put("city_name", cityMap.get(cityId));
		detotal = 0;
		nototal = 0;
		tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
		for (int j = 0; j < tlist.size(); j++)
		{
			detotal = detotal + StringUtil.getLongValue(demap.get(tlist.get(j)));
			nototal = nototal + StringUtil.getLongValue(nomap.get(tlist.get(j)));
		}
		amap.put("detotal", detotal);
		amap.put("nototal", nototal);
		amap.put("percent", percent(detotal, nototal));
		amap.put("isAll", "1");
		amap.put("prodSpecId", prodSpecId);
		list.add(amap);
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 已部署数
			detotal = 0;
			// 未部署数
			nototal = 0;
			String city_id = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++)
			{
				detotal = detotal + StringUtil.getLongValue(demap.get(tlist.get(j)));
				nototal = nototal + StringUtil.getLongValue(nomap.get(tlist.get(j)));
			}
			String percent = percent(detotal, nototal);
			tmap.put("detotal", detotal);
			tmap.put("nototal", nototal);
			tmap.put("percent", percent);
			tmap.put("isAll", "0");
			tmap.put("prodSpecId", prodSpecId);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}

	/**
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param curPage_splitPage
	 *            当前页数
	 * @param num_splitPage
	 *            每页记录数
	 * @param isAll
	 * @param isNew
	 * @return List<Map>
	 */
	public List<Map> getHgwList(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll,
			String isNew, String prodSpecId,String isItv)
	{
		return pvcReportDao.getHgwList(starttime1, endtime1, cityId, reform_flag,
				curPage_splitPage, num_splitPage, isAll, isNew, prodSpecId, isItv);
	}

	public int getHgwCount(String starttime1, String endtime1, String cityId,
			String reform_flag, int curPage_splitPage, int num_splitPage, String isAll,
			String isNew, String prodSpecId, String isItv)
	{
		return pvcReportDao.getHgwCount(starttime1, endtime1, cityId, reform_flag,
				curPage_splitPage, num_splitPage, isAll, isNew, prodSpecId, isItv);
	}

	/**
	 * 导出Excel
	 * 
	 * @author wangsenbo
	 * @param isNew 
	 * @date Feb 1, 2010
	 * @param
	 * @return List<Map>
	 */
	public List<Map> getHgwExcel(String starttime1, String endtime1, String cityId,
			String reform_flag, String isNew,String prodSpecId,String isItv)
	{
		return pvcReportDao.getHgwExcel(starttime1, endtime1, cityId, reform_flag,isNew,prodSpecId,isItv);
	}

	/**
	 * @return the pvcReportDao
	 */
	public PVCReportDAO getPvcReportDao()
	{
		return pvcReportDao;
	}

	/**
	 * @param pvcReportDao
	 *            the pvcReportDao to set
	 */
	public void setPvcReportDao(PVCReportDAO pvcReportDao)
	{
		this.pvcReportDao = pvcReportDao;
	}

	public String getCompletedate()
	{
		return pvcReportDao.getCompletedate();
	}
}
