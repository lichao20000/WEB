package com.linkage.module.gtms.stb.resource.serv;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.cityAllDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-1
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class cityAllBIO
{
	private static Logger logger = LoggerFactory.getLogger(cityAllBIO.class);
	private cityAllDAO dao;
	public String getfirstCity(String cityId) {
		List<Map<String,String>> list = CityDAO.getNextCityListByCityPid(cityId);
		if(!Global.CQDX.equals(Global.instAreaShortName)){
		 Collections.sort(list, new Comparator<Map<String, String>>() {
				public int compare(Map<String, String> o1, Map<String, String> o2) {
					//name1是从你list里面拿出来的一个 
	                Integer name1 = Integer.valueOf(o1.get("city_id").toString()) ; 
	              //name1是从你list里面拿出来的第二个name
	                Integer name2 = Integer.valueOf(o2.get("city_id").toString()) ; 
	                return name1.compareTo(name2);
	            }

	        });}
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();
		StringBuffer bf2 = new StringBuffer();
		for(int i=0;i<list.size();i++)
		{
			Map map = list.get(i);
			if (i > 0){
				bf.append("#");}
			bf.append(map.get("city_id"));
			bf.append("$");
			bf.append(map.get("city_name"));
			if(!String.valueOf(list.get(i).get("city_id")).equals("00"))
			{
			Map<String,String> listshow=CityDAO.getNextCityMapByCityPidCore(String.valueOf(list.get(i).get("city_id")));
			if(listshow!=null&&listshow.size()>0){
			bf1.append("|");
			for(Map.Entry<String, String> entry:listshow.entrySet())
			{
				bf1.append("#");
				bf1.append(entry.getKey());
				bf1.append("$");
				bf1.append(entry.getValue());
			}}
			}
		}
		logger.warn("return ======="+bf.toString()+","+bf1.toString());
		return bf.toString()+","+bf1.toString();
	}
	
	public String getfirstCity1(String cityId) {
		String[] city=cityId.split(",");
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();
		for(int k=0;k<city.length;k++)
		{
			Map map=dao.querycity(city[k]);
			logger.warn("map==="+map);
			if(map!=null&&map.size()>0)
			{
			List<Map<String,String>> list=dao.querycityid(city[k]);
			for(int i=0;i<list.size();i++)
			{
				Map map1 = list.get(i);
				
					bf.append("#");
				bf.append(map1.get("city_id"));
				bf.append("$");
				bf.append(map1.get("city_name"));
			}
			Map<String,String> listshow=CityDAO.getNextCityMapByCityPidCore(String.valueOf(city[k]));
			bf1.append("|");
			for(Map.Entry<String, String> entry:listshow.entrySet())
			{
				bf1.append("#");
				bf1.append(entry.getKey());
				bf1.append("$");
				bf1.append(entry.getValue());
			}
			}else{
				List<Map<String,String>> list=dao.querycityid(city[k]);
				for(int i=0;i<list.size();i++)
				{
					Map map1 = list.get(i);
					bf1.append("|");
					bf1.append("#");
					bf1.append(map1.get("city_id"));
					bf1.append("$");
					bf1.append(map1.get("city_name"));
				}
			}
		}
		return bf.toString()+","+bf1.toString();
	}
	/**
	 * 查询所有子属地
	 * @param firstCitys
	 * @param secondCitys
	 * @return
	 */
	public String getnextcity(String cityid)
	{
		String[] cityids=cityid.split(",");
		String city_id="";
		for(int i=0;i<cityids.length;i++)
		{
			List<Map<String,String>> list = CityDAO.getNextCityListByCityPid(cityids[i]);
			if(list!=null&&list.size()>0)
			{
				for(int j=0;j<list.size();j++)
				{
				city_id +=String.valueOf(list.get(j).get("city_id"))+",";
				}
			}else
			{
				city_id +=cityids[i];
			}
		}
		return city_id.substring(0,city_id.length()-1);
	}
	public String getcity(String firstCitys,String secondCitys)
	{
		String cityid="";
		String[] firstCity=firstCitys.split(",");
		Map<String,String> mapshow=new HashMap<String, String>();
		Map<String,String> map1=new HashMap<String, String>();
		Set<String> set=new HashSet<String>();
		for(int j=0;j<firstCity.length;j++)
		{
			String aa="";
			mapshow.put(firstCity[j], firstCity[j]);
			aa=firstCity[j];
			if(aa.equals("00"))
			{
				cityid="00";
			}
		}
		logger.warn("mapshow===="+mapshow);
		if(StringUtil.IsEmpty(cityid))
		{
	if(!StringUtil.IsEmpty(secondCitys)){
		String[] secondCity=secondCitys.split(",");
		for(int i=0;i<secondCity.length;i++)
		{
			Map map=dao.queryData(secondCity[i]);
			if(map.size()>0&&map!=null)
			{
				if(!StringUtil.IsEmpty(mapshow.get(map.get("parent_id"))))
				{
					set.add(mapshow.get(map.get("parent_id")));
				}else
				{
					set.add( secondCity[i]);
				}
			}else
			{
				set.add(mapshow.get(map.get("parent_id")));
			}
		}
		for(String str:set)
		{
			cityid +=str+",";
		}
		logger.warn("cityid.substring(0,cityid.length()-1)====="+cityid.substring(0,cityid.length()-1));
		return cityid.substring(0,cityid.length()-1);
		}else
		{
			return firstCitys;
		}
			
		}
		return cityid;
	}
	public String getsecondCity(String firstCitys)
	{
		String[] firstCity=firstCitys.split(",");
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<firstCity.length;i++)
		{
			Map<String,String> map=CityDAO.getNextCityMapByCityPidCore(firstCity[i]);
			for(Map.Entry<String, String> entry:map.entrySet())
			{
				bf.append("#");
				bf.append(entry.getKey());
			}
		}
		return bf.toString();
	}
	public Map test(String cityId)
	{
		Map map=CityDAO.getAllNextCityMapByCityPid(cityId);
		return map;
	}
	
	public cityAllDAO getDao()
	{
		return dao;
	}
	
	public void setDao(cityAllDAO dao)
	{
		this.dao = dao;
	}
	
}
