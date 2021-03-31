
package com.linkage.module.itms.resource.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.resource.dao.PonNetWorkDataDAO;
import com.linkage.module.itms.resource.dao.PonNetWorkDegraInfoDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-8-20
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class PonNetWorkDegraInfoBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(PonNetWorkDegraInfoBIO.class);
	private PonNetWorkDegraInfoDAO dao;
	private PonNetWorkDataDAO pondao;

	public List<Map> getPondata(String city_id, String start_time, String end_time,
			int month)
	{
		List<Map> resultMap = new ArrayList<Map>();
		Map cityMap = Global.G_CityId_CityName_Map;
		Map<String, String> e8cmap = dao.getE8cdata(city_id);
		Map<String, String> ponmap = dao.getDegreadata(start_time, end_time);
		Map<String, String> ponLessMap = dao.getDegreadataLess(start_time, end_time);
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		ArrayList<String> tlist = null;
		long e8cValue_test;
		long ponValue_test;
		long lessValue_test;
		String pert_test;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			e8cValue_test = 0;
			ponValue_test = 0;
			lessValue_test = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("area_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				String city_name = StringUtil.getStringValue(cityMap.get(tlist.get(j)));
				e8cValue_test += StringUtil.getLongValue(e8cmap.get(tlist.get(j)));
				ponValue_test += StringUtil.getLongValue(ponmap.get(city_name));
				lessValue_test += StringUtil.getLongValue(ponLessMap.get(city_name));
			}
			pert_test = getDecimal(StringUtil.getStringValue(ponValue_test),
					StringUtil.getStringValue(e8cValue_test));
			tmap.put("e8cValue", e8cValue_test);
			tmap.put("ponValue", ponValue_test);
			tmap.put("pert", pert_test);
			tmap.put("lessPonValue", lessValue_test);
			resultMap.add(tmap);
		}
		return resultMap;
	}

	public List<Map> getDegraContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.getDegraContext(city_id, start_time, end_time, curPage_splitPage,
				num_splitPage);
	}

	public int countDegraContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.countDegraContext(city_id, start_time, end_time, curPage_splitPage,
				num_splitPage);
	}

	public List<Map> getDegraContextExcel(String city_id, String start_time,
			String end_time)
	{
		return dao.getDegraContextExcel(city_id, start_time, end_time);
	}
	
	public List<Map> getDegraLessContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.getDegraLessContext(city_id, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public int countDegraLessContext(String city_id, String start_time, String end_time,
			int curPage_splitPage, int num_splitPage)
	{
		return dao.countDegraLessContext(city_id, start_time, end_time, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getDegraLessContextExcel(String city_id, String start_time,
			String end_time)
	{
		return dao.getDegraLessContextExcel(city_id, start_time, end_time);
	}
	

	/**
	 * 占用比列
	 * 
	 * @param molecular
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public String getDecimal(String molecular, String denominator)
	{
		if (null == molecular || "0".equals(molecular) || null == denominator
				|| "0".equals(denominator))
		{
			return "0%";
		}
		float t1 = Float.parseFloat(molecular);
		float t2 = Float.parseFloat(denominator);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}

	public PonNetWorkDegraInfoDAO getDao()
	{
		return dao;
	}

	public PonNetWorkDataDAO getPondao()
	{
		return pondao;
	}

	public void setPondao(PonNetWorkDataDAO pondao)
	{
		this.pondao = pondao;
	}

	public void setDao(PonNetWorkDegraInfoDAO dao)
	{
		this.dao = dao;
	}
}
