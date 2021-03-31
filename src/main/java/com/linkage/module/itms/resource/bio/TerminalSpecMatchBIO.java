package com.linkage.module.itms.resource.bio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.TerminalSpecMatchDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-18
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalSpecMatchBIO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalSpecMatchBIO.class);
	private TerminalSpecMatchDAO dao;
	
	public List<Map> getTerminalSpecMatchInfo(String city_id, String start_time, String end_time){
		logger.debug("TerminalSpecMatchBIO=>getTerminalSpecMatchInfo()");
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map<String,String> terminalAll = dao.getTerminalSpecAll(city_id, start_time, end_time);
		Map<String,String> terminalOne = dao.getTerminalSpectNoMactchOne(city_id, start_time, end_time);
		Map<String,String> terminalTwo = dao.getTerminalSpectNoMactchTwo(city_id, start_time, end_time);
		Map<String,String> terminalThree = dao.getTerminalSpectNoMactchThree(city_id, start_time, end_time);
		Map<String,String> terminalFour = dao.getTerminalSpectNoMactchFour(city_id, start_time, end_time);
		long allNum;
		long allNoNum;
		long oneNum;
		long twoNum;
		long threeNum;
		long fourNum;
		String percentage = "N/A";
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			allNum = 0;
			allNoNum=0;	
			oneNum = 0;
			twoNum = 0;
			threeNum = 0;
			fourNum = 0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				if (null != terminalAll && !terminalAll.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalAll.get(tlist.get(j)));
					allNum += StringUtil.getLongValue(str);
				}
				if (null != terminalOne && !terminalOne.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalOne.get(tlist.get(j)));
					oneNum += StringUtil.getLongValue(str);
				}
				if (null != terminalTwo && !terminalTwo.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalTwo.get(tlist.get(j)));
					twoNum += StringUtil.getLongValue(str);
				}
				if (null != terminalThree && !terminalThree.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalThree.get(tlist.get(j)));
					threeNum += StringUtil.getLongValue(str);
				}
				if (null != terminalFour && !terminalFour.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalFour.get(tlist.get(j)));
					fourNum += StringUtil.getLongValue(str);
				}
			}
			allNoNum = oneNum+twoNum+threeNum+fourNum;
			tmap.put("allNum", allNum);
			tmap.put("allNoNum", allNoNum);
			tmap.put("oneNum", oneNum);
			tmap.put("twoNum", twoNum);
			tmap.put("threeNum", threeNum);
			tmap.put("fourNum", fourNum);
			percentage = getDecimal(StringUtil.getStringValue(allNoNum),
					StringUtil.getStringValue(allNum));
			tmap.put("percentage", percentage);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		logger.warn("list:"+list.toString());
		return list;
	}
	
	public TerminalSpecMatchDAO getDao()
	{
		return dao;
	}
	
	public void setDao(TerminalSpecMatchDAO dao)
	{
		this.dao = dao;
	}
	
	private String getDecimal(String total, String ttotal)
	{
		if (null == total || "0".equals(total) || null == ttotal || "0".equals(ttotal))
		{
			return "N/A";
		}
		float t1 = Float.parseFloat(total);
		float t2 = Float.parseFloat(ttotal);
		float f = t1 / t2;
		DecimalFormat df = new DecimalFormat();
		String style = "0.00%";
		df.applyPattern(style);
		return df.format(f);
	}
	
	
	
}
