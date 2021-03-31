package com.linkage.module.gtms.resource.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.resource.dao.CountDeviceByServTypeIdDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings("rawtypes")
public class CountDeviceByServTypeIdServImp implements CountDeviceByServTypeIdServ 
{
	private static Logger logger = LoggerFactory.getLogger(CountDeviceByServTypeIdServImp.class);
	
	private CountDeviceByServTypeIdDAO dao ;
	
	
	/**
	 * 统计已开通业务量
	 */
	public List<Map> countHaveOpenningService(String gw_type, String cityId)
	{
		logger.debug("countHaveOpenningService()");
		List<Map> list = new ArrayList<Map>();
		
		List<Map> haveResultList = dao.countHaveOpenningService(gw_type, cityId);
		
		// 按属地统计
		Map cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		
		for (int i = 0; i < cityList.size(); i++) 
		{
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			long internetValue = 0L;
			long iptvValue = 0L;
			long voipValue = 0L;
			
			String servTypeIdComp = null;
			String cityIdComp = null;
			long numComp = 0L;
			
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			for (int j = 0; j < tlist.size(); j++)
			{
				String cityId2 = tlist.get(j);
				for (Map<String, String> infoMap : haveResultList) 
				{
					servTypeIdComp = String.valueOf(infoMap.get("serv_type_id"));
					cityIdComp = String.valueOf(infoMap.get("city_id"));
					numComp = StringUtil.getLongValue(infoMap.get("num"));
					
					if (cityId2.equals(cityIdComp)) 
					{
						if ("10".equals(servTypeIdComp)) {
							internetValue = internetValue + numComp;
						}else if ("11".equals(servTypeIdComp)) {
							iptvValue = iptvValue + numComp;
						}else if ("14".equals(servTypeIdComp)) {
							voipValue = voipValue + numComp;
						}
					}
				}
			}
			tmap.put("internetValue", internetValue);
			tmap.put("iptvValue", iptvValue);
			tmap.put("voipValue", voipValue);
			tmap.put("gw_type", gw_type);
			
			list.add(tmap);
			tlist = null;
		}
		
		cityMap = null;
		cityList = null;
		return list;
	}
	
	/**
	 * 统计家庭网关宽带业务或VOIP业务已开通业务量
	 * 宁夏特制
	 */
	@Override
	public List<Map> countHaveOpenService(String gw_type, String cityId,
			String servTypeId, long startTime, long endTime,String type) 
	{
		logger.warn("countHaveOpenService({},{},{},{},{})",
				cityId,servTypeId,gw_type,startTime,endTime);
		
		long stime=System.currentTimeMillis()/1000;
		List<Map> haveResultList = dao.countHaveOpenService(gw_type,cityId,
											servTypeId,startTime,endTime,type);
		logger.warn("countHaveOpenService() 统计耗时：{}s",System.currentTimeMillis()/1000 - stime);
		
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		
		List<Map> list = new ArrayList<Map>();
		Map<String,String> tolmap = new HashMap<String,String>();
		for (String city_id:cityList)
		{
			long internetValue = 0L;
			long voipValue = 0L;
			
			if(city_id.equals(cityId))
			{
				for (Map<String, String> infoMap : haveResultList)
				{
					if(city_id.equals(StringUtil.getStringValue(infoMap,"city_id")))
					{
						if (10==StringUtil.getIntValue(infoMap, "serv_type_id")) {
							internetValue = StringUtil.getLongValue(infoMap,"num");
						}else if (14==StringUtil.getIntValue(infoMap, "serv_type_id") 
								|| 11==StringUtil.getIntValue(infoMap, "serv_type_id")) {
							voipValue = StringUtil.getLongValue(infoMap,"num");
						}
					}
				}
				
				Map<String,String> local_map = new HashMap<String,String>();
				local_map.put("city_id", city_id+"_local");
				local_map.put("city_name", CityDAO.getCityName(city_id));
				local_map.put("internetValue", internetValue+"");
				local_map.put("voipValue", voipValue+"");
				
				list.add(local_map);
				local_map=null;
			}
			
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			for (String cityidnext:tlist)
			{
				if(cityidnext.equals(cityId)){
					continue;
				}
				
				for (Map<String, String> infoMap : haveResultList) 
				{
					if (!cityidnext.equals(StringUtil.getStringValue(infoMap, "city_id"))) {
						continue;
					}
					
				//	if (cityidnext.equals(StringUtil.getStringValue(infoMap, "city_id"))) 
				//	{
					if (10==StringUtil.getIntValue(infoMap, "serv_type_id")) {
						internetValue += StringUtil.getLongValue(infoMap,"num");
					}else if (14==StringUtil.getIntValue(infoMap, "serv_type_id") 
								|| 11==StringUtil.getIntValue(infoMap, "serv_type_id")) {
						voipValue += StringUtil.getLongValue(infoMap,"num");
					}
				//	}
				}
			}
			
			if(city_id.equals(cityId))
			{
				tolmap.put("city_id",cityId+"_city_all");
				tolmap.put("city_name","合计");
				tolmap.put("internetValue", internetValue+"");
				tolmap.put("voipValue", voipValue+"");
			}
			else
			{
				Map<String,String> tmap = new HashMap<String,String>();
				tmap.put("city_id", city_id);
				tmap.put("city_name", CityDAO.getCityName(city_id));
				tmap.put("internetValue", internetValue+"");
				tmap.put("voipValue", voipValue+"");
				
				list.add(tmap);
				tmap=null;
			}
			
			tlist = null;
		}
		list.add(tolmap);
		
		cityList = null;
		haveResultList=null;
		
		return list;
	}
	
	
	/**
	 * 统计未开通业务量
	 */
	public List<Map> countHaveNotOpenningService(String gw_type, String cityId)
	{
		logger.debug("countHaveNotOpenningService()");
		List<Map> haveResultList = dao.countHaveNotOpenningService(gw_type, cityId);
		
		Map cityMap = CityDAO.getCityIdCityNameMap();
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		
		List<Map> list = new ArrayList<Map>();
		for (int i = 0; i < cityList.size(); i++) 
		{
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			
			long internetValue = 0L;
			long iptvValue = 0L;
			long voipValue = 0L;
			
			String servTypeIdComp = null;
			String cityIdComp = null;
			long numComp = 0L;
			
			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			
			for (int j = 0; j < tlist.size(); j++)
			{
				for (Map<String, String> infoMap : haveResultList) 
				{
					servTypeIdComp = String.valueOf(infoMap.get("serv_type_id"));
					cityIdComp = String.valueOf(infoMap.get("city_id"));
					numComp = StringUtil.getLongValue(infoMap.get("num"));
					
					if (tlist.get(j).equals(cityIdComp)) 
					{
						if ("10".equals(servTypeIdComp)) {
							internetValue = internetValue + numComp;
						}else if ("11".equals(servTypeIdComp)) {
							iptvValue = iptvValue + numComp;
						}else if ("14".equals(servTypeIdComp)) {
							voipValue = voipValue + numComp;
						}
					}
				}
			}
			tmap.put("internetValue", internetValue);
			tmap.put("iptvValue", iptvValue);
			tmap.put("voipValue", voipValue);
			tmap.put("gw_type", gw_type);
			
			list.add(tmap);
			tlist = null;
		}
		
		cityMap = null;
		cityList = null;
		return list;
	}
	
	/**
	 * 详细信息展示
	 */
	public List<Map> getDetail(String cityId, String gw_type, String servTypeId,
			String isOpen, int curPage_splitPage, int num_splitPage,long stime,long etime) 
	{
		logger.debug("bio==>getDetail({},{},{})",cityId,servTypeId,gw_type);
		
		return dao.getDetail(cityId,gw_type,servTypeId,isOpen,curPage_splitPage,
				num_splitPage,stime,etime);
	}
	
	/**
	 * 查看详细信息 分页
	 */
	public int getCount(String cityId, String gw_type, String servTypeId, String isOpen,
			int curPage_splitPage, int num_splitPage,long stime,long etime) 
	{
		logger.debug("bio==>getCount({},{},{})",cityId,servTypeId,gw_type);
		
		return dao.getCount(cityId,gw_type,servTypeId,isOpen,curPage_splitPage,
				num_splitPage,stime,etime);
	}
	
	/**
	 * 详细信息导出
	 */
	public List<Map> getDetailExcel(String cityId,String gw_type,String servTypeId,
			String isOpen,long stime,long etime)
	{
		logger.debug("bio==>getDetailExcel({},{},{})",cityId,servTypeId,gw_type);
		return dao.getDetailExcel(cityId, gw_type, servTypeId, isOpen,stime,etime);
	}

	
	
	public CountDeviceByServTypeIdDAO getDao() {
		return dao;
	}
	
	public void setDao(CountDeviceByServTypeIdDAO dao) {
		this.dao = dao;
	}


}
