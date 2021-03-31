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
import com.linkage.module.itms.resource.dao.UserSpecTerminalsDAO;


/**
 * 
 * @author hanzezheng (Ailk No.)
 * @version 1.0
 * @since 2015-2-12
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class UserSpecTerminalsBIO
{
	private static Logger logger = LoggerFactory.getLogger(UserSpecTerminalsBIO.class);
	private UserSpecTerminalsDAO dao;
	public List<Map> getUserSpecTerminals(String startOpenDate1,String endOpenDate1, String city_id,String spec_id)
	{
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map map=dao.getUserSpecTerminals(startOpenDate1, endOpenDate1, city_id, spec_id);
		Map totalMap=dao.getTerminalSpecAll(startOpenDate1, endOpenDate1, city_id);
		long total = 0;
		long totals = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			totals=0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				total = total + StringUtil.getLongValue(map.get(tlist.get(j)));
				totals = totals + StringUtil.getLongValue(totalMap.get(tlist.get(j)));
			}
			String pert=getDecimal(String.valueOf(total),String.valueOf(totals));
			tmap.put("totals", totals);
			tmap.put("total", total);
			if(pert.equals("0%")){
				tmap.put("pert", "N/A");
			}else{
				tmap.put("pert", pert);
			}
			tmap.put("spec_id", spec_id);
			tmap.put("startOpenDate", startOpenDate1);
			tmap.put("endOpenDate", endOpenDate1);
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public List<Map> getTerminalSpecList(String startOpenDate1,String endOpenDate1, String city_id,String spec_id,int curPage_splitPage, int num_splitPage){
		String cus="";
		String ter="";
		if(spec_id.equals("4221")){
			cus="4+2";
			ter="2+1";
		}else if(spec_id.equals("4220")){
			cus="4+2";
			ter="2+0";
		}else if(spec_id.equals("2142")){
			cus="2+1";
			ter="4+2";
		}else if(spec_id.equals("2042")){
			cus="2+0";
			ter="4+2";
		}
		List<Map> list=dao.getTerminalSpecList(startOpenDate1, endOpenDate1, city_id, spec_id, curPage_splitPage, num_splitPage);
		for (int i = 0; i < list.size(); i++)
		{
			Map map=list.get(i);
			map.put("cusSpec", cus);
			map.put("terSpec", ter);
		}
		return list;
	}
	public List<Map> getCityExcel(String startOpenDate1,String endOpenDate1, String city_id,String spec_id){
		List<Map> list = new ArrayList<Map>();
		// 按本地网统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(不包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPidCore(city_id);
		Collections.sort(cityList);
		cityList.add(0, city_id);
		Map map=dao.getUserSpecTerminals(startOpenDate1, endOpenDate1, city_id, spec_id);
		Map totalMap=dao.getTerminalSpecAll(startOpenDate1, endOpenDate1, city_id);
		long total = 0;
		long totals = 0;
		ArrayList<String> tlist = null;
		for (int i = 0; i < cityList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			total = 0;
			totals=0;
			String cityId = cityList.get(i);
			tlist = CityDAO.getAllNextCityIdsByCityPid(cityId);
			tmap.put("city_id", cityId);
			tmap.put("city_name", cityMap.get(cityId));
			for (int j = 0; j < tlist.size(); j++)
			{
				total = total + StringUtil.getLongValue(map.get(tlist.get(j)));
				totals = totals + StringUtil.getLongValue(totalMap.get(tlist.get(j)));
			}
			String pert=getDecimal(String.valueOf(total),String.valueOf(totals));
			tmap.put("totals", totals);
			tmap.put("total", total);
			if(pert.equals("0%")){
				tmap.put("pert", "N/A");
			}else{
				tmap.put("pert", pert);
			}
			list.add(tmap);
			tlist = null;
		}
		cityMap = null;
		cityList = null;
		tlist = null;
		return list;
	}
	
	public List<Map> getExcle(String startOpenDate1,String endOpenDate1, String city_id,String spec_id){
		String cus="";
		String ter="";
		if(spec_id.equals("4221")){
			cus="4+2";
			ter="2+1";
		}else if(spec_id.equals("4220")){
			cus="4+2";
			ter="2+0";
		}else if(spec_id.equals("2142")){
			cus="2+1";
			ter="4+2";
		}else if(spec_id.equals("2042")){
			cus="2+0";
			ter="4+2";
		}
		List<Map> list=dao.getExcel(startOpenDate1, endOpenDate1, city_id, spec_id);
		for (int i = 0; i < list.size(); i++)
		{
			Map map=list.get(i);
			map.put("cusSpec", cus);
			map.put("terSpec", ter);
		}
		return list;
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
	
	
	public int getTerminalSpecListCount(String startOpenDate1,String endOpenDate1, String city_id,String spec_id,int curPage_splitPage, int num_splitPage){
		return dao.getTerminalSpecListCount(startOpenDate1, endOpenDate1, city_id, spec_id, curPage_splitPage, num_splitPage);
	}
	
	public UserSpecTerminalsDAO getDao()
	{
		return dao;
	}
	
	public void setDao(UserSpecTerminalsDAO dao)
	{
		this.dao = dao;
	}
	
	
}
