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
import com.linkage.module.itms.resource.dao.TerminalE8CDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-17
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class TerminalE8CBIO
{
	private static Logger logger = LoggerFactory.getLogger(TerminalE8CBIO.class);
	private TerminalE8CDAO dao;
	
	public List<Map> getTerminalE8CInfo(String city_id, String start_time, String end_time){
		logger.debug("TerminalE8CBIO=>getTerminalE8CInfo()");
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map<String,String> terminalAll = dao.getTerminalAll(city_id);
		Map<String,String> terminalNew = dao.getNewTerminal(city_id, start_time, end_time);
		Map<String,String> terminalTwoAndOne = dao.getTwoAndOneTerminal(city_id, start_time, end_time);
		Map<String,String> terminalFourAndTwo = dao.getFourAndTwoTerminal(city_id, start_time, end_time);
		Map<String,String> terminalEgw = dao.getEgwTerminal(city_id, start_time, end_time);
		long allNum;
		long newNum;
		long twoNum;
		long fourNum;
		long egwNum;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			allNum = 0;
			newNum = 0;
			twoNum = 0;
			fourNum = 0;
			egwNum = 0;
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
				if (null != terminalNew && !terminalNew.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalNew.get(tlist.get(j)));
					newNum += StringUtil.getLongValue(str);
				}
				if (null != terminalTwoAndOne && !terminalTwoAndOne.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalTwoAndOne.get(tlist.get(j)));
					twoNum += StringUtil.getLongValue(str);
				}
				if (null != terminalFourAndTwo && !terminalFourAndTwo.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalFourAndTwo.get(tlist.get(j)));
					fourNum += StringUtil.getLongValue(str);
				}
				if (null != terminalEgw && !terminalEgw.isEmpty())
				{
					String str = StringUtil.getStringValue(terminalEgw.get(tlist.get(j)));
					egwNum += StringUtil.getLongValue(str);
				}
			}
			tmap.put("allNum", allNum);
			tmap.put("newNum", newNum);
			tmap.put("twoNum", twoNum);
			tmap.put("fourNum", fourNum);
			tmap.put("egwNum", egwNum);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}


	
	public TerminalE8CDAO getDao()
	{
		return dao;
	}


	
	public void setDao(TerminalE8CDAO dao)
	{
		this.dao = dao;
	}
}
