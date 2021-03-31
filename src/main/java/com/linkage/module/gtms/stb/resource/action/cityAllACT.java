package com.linkage.module.gtms.stb.resource.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.cityAllBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-3-1
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class cityAllACT
{
	private List<Map<String, String>> firstCityList = null;
	private List<Map<String, String>> secondCityList = null;
	private List<Map<String, String>> thirdCityList = null;
	private String firstCitys;
	private String secondCitys;
	private Map map=new HashMap();
	private String ajax = "";
	private cityAllBIO bio;
	private String city_id;
	public String cityAll()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		firstCityList = CityDAO.getNextCityListByCityPid(curUser.getUser().getCityId());
		return "cityall";
	}
	public String getfirstCity()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		if(curUser.getUser().getCityId().equals("00"))
		{
			ajax=bio.getfirstCity(curUser.getUser().getCityId());
		}else
		{
			ajax=bio.getfirstCity1(curUser.getUser().getCityId());
		}
		return "ajax";
	}
	public String getcity()
	{
		ajax=bio.getcity(firstCitys, secondCitys);
		return "ajax";
	}
	/**
	 * 根据父类id动态获取子属地
	 */
	public String getsecondCity()
	{
		ajax=bio.getsecondCity(firstCitys);
		return "ajax";
	}
	public String getFirstCityList()
	{
		ajax=bio.getnextcity(city_id);
		return "ajax";
	}
	public String init()
	{
		UserRes curUser = WebUtil.getCurrentUser();
		//map=bio.test(curUser.getUser().getCityId());
		/*List list=CityDAO.getNextCityIdsByCityPid("13000000,12000001");
		logger.warn("getNextCityIdsByCityPid====="+list);
		List list1=CityDAO.getAllPcityIdByCityId("13000000,12000001");
		logger.warn("getAllPcityIdByCityId====="+list1);
		List list2=CityDAO.getNextCityIdsByCityPidCore("13000000,12000001");
		logger.warn("getNextCityIdsByCityPidCore====="+list2);
		
		Map map=CityDAO.getNextCityMapByCityPid("13000000,12000001");
		logger.warn("getNextCityMapByCityPid====="+map);
		
		Map map1=CityDAO.getNextCityMapByCityPidCore("13000000,12000001");
		logger.warn("getNextCityMapByCityPidCore====="+map1);
		List list5=CityDAO.getNextCityListByCityPid("13000000,12000001");
		logger.warn("getNextCityListByCityPid====="+list5);
		List list6=CityDAO.getNextCityListByCityPidCore("13000000,12000001");
		logger.warn("getNextCityListByCityPidCore====="+list6);
		
		List list7=CityDAO.getAllNextCityIdsByCityPid("13000000,12000001");
		logger.warn("getAllNextCityIdsByCityPid====="+list7);
		
		List list8=CityDAO.getAllNextCityIdsByCityPidCore("13000000,12000001");
		logger.warn("getAllNextCityIdsByCityPidCore====="+list8);
		
		Map map3=CityDAO.getAllNextCityMapByCityPid("13000000,12000001");
		logger.warn("getAllNextCityMapByCityPid====="+map3);
		
		Map map4=CityDAO.getAllNextCityMapByCityPidCore("13000000,12000001");
		logger.warn("getAllNextCityMapByCityPidCore====="+map4);
		
		List list10=CityDAO.getAllNextCityListByCityPid("13000000,12000001");
		logger.warn("getAllNextCityListByCityPid====="+list10);
		List list11=CityDAO.getAllNextCityListByCityPidCore("13000000,12000001");
		logger.warn("getAllNextCityListByCityPidCore====="+list11);*/
		/*String aa="13000000,12000001";
		List ss=CityDAO.getNextCityListByCityPid(aa);
		logger.warn("ss==="+ss);*/
		/*List list1=CityDAO.getAllPcityIdByCityId("13000003,12000106");
		logger.warn("getAllPcityIdByCityId=====list1===="+list1);
		List list2=CityDAO.getAllPcityIdByCityId("13000000,00");
		logger.warn("getAllPcityIdByCityId=====list2===="+list2);
		List list3=CityDAO.getAllPcityIdByCityId("00,-1");
		logger.warn("getAllPcityIdByCityId=====list3===="+list3);
		List list4=CityDAO.getAllPcityIdByCityId("00,12000001");
		logger.warn("getAllPcityIdByCityId=====list4===="+list4);
		List list5=CityDAO.getAllPcityIdByCityId("00");
		logger.warn("getAllPcityIdByCityId=====list5===="+list5);
		List list6=CityDAO.getAllPcityIdByCityId("-1");
		logger.warn("getAllPcityIdByCityId=====list6===="+list6);
		List list7=CityDAO.getAllPcityIdByCityId("12000101");
		logger.warn("getAllPcityIdByCityId=====list7===="+list7);*/
		ajax=bio.getfirstCity(curUser.getUser().getCityId());
		return "init";
	}
	public void setFirstCityList(List<Map<String, String>> firstCityList)
	{
		this.firstCityList = firstCityList;
	}
	
	public List<Map<String, String>> getSecondCityList()
	{
		return secondCityList;
	}
	
	public void setSecondCityList(List<Map<String, String>> secondCityList)
	{
		this.secondCityList = secondCityList;
	}
	
	public List<Map<String, String>> getThirdCityList()
	{
		return thirdCityList;
	}
	
	public void setThirdCityList(List<Map<String, String>> thirdCityList)
	{
		this.thirdCityList = thirdCityList;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
	
	public cityAllBIO getBio()
	{
		return bio;
	}
	
	public void setBio(cityAllBIO bio)
	{
		this.bio = bio;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public String getFirstCitys()
	{
		return firstCitys;
	}
	
	public void setFirstCitys(String firstCitys)
	{
		this.firstCitys = firstCitys;
	}
	
	public String getSecondCitys()
	{
		return secondCitys;
	}
	
	public void setSecondCitys(String secondCitys)
	{
		this.secondCitys = secondCitys;
	}
	
	public String getCity_id()
	{
		return city_id;
	}
	
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
	
}
