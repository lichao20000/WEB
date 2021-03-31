package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.StbCountReportDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 湖南联通机顶盒按EPG或APK版本统计
 */
@SuppressWarnings("rawtypes")
public class StbCountReportBIO 
{
	private static Logger logger = LoggerFactory.getLogger(StbCountReportBIO.class);
	private StbCountReportBioServ serv;
	private StbCountReportDAO dao;
	
	
	/**
	 * 按属地、epg/apk版本统计数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> countResultByCity(String type,String city_id,
			List<Map<String, String>> cityList) 
	{
		long stime=System.currentTimeMillis()/1000L;
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		List versionList=dao.getVersion(type);
		logger.warn("versionList time="+(System.currentTimeMillis()/1000L - stime));
		if(versionList!=null && !versionList.isEmpty())
		{
			for(int i=0;i<versionList.size();i++)
			{
				Map<String,String> rm=new HashMap<String,String>();
				rm.put("version",StringUtil.getStringValue(
						(Map<String,String>) versionList.get(i),"version",""));
				for(Map<String, String> cm:cityList){
					rm.put(cm.get("city_id"),"0");
				}
				rm.put("total_num","0");
				resultList.add(rm);
			}
		}
		versionList=null;
		
		stime=System.currentTimeMillis()/1000L;
		List list=dao.countResultByCity(type,city_id);
		logger.warn("countResultByCity time="+(System.currentTimeMillis()/1000L - stime));
		if("apk".equals(type))
		{
			//单独统计stb_dev_supplement表里没有的设备
			stime=System.currentTimeMillis()/1000L;
			List listNull=dao.countResultByCityNull(city_id);
			logger.warn("countResultByCityNull time="+(System.currentTimeMillis()/1000L - stime));
			if(listNull!=null && !listNull.isEmpty())
			{
				Map<String,String> rm=new HashMap<String,String>();
				rm.put("version","Unknown");
				for(Map<String, String> cm:cityList){
					rm.put(cm.get("city_id"),"0");
				}
				rm.put("total_num","0");
				resultList.add(rm);
			}
			list.addAll(listNull);
		}
		
		logger.debug("resultList:"+resultList);
		
		for(Map<String,String> map:resultList)
		{
			if(list!=null && !list.isEmpty())
			{
				for(int i=0;i<list.size();i++)
				{
					Map<String, String> countMap = (Map<String, String>) list.get(i);
					String version=StringUtil.IsEmpty(countMap.get("version"))?"":countMap.get("version");
					
					if(version.equals(map.get("version")))
					{
						for(String key:map.keySet()){
							if(!"version".equals(key) && !"total_num".equals(key))
							{
								// key:0013300
								if(city_id.equals(key) && city_id.equals(countMap.get("city_id"))){
									map.put(key,StringUtil.getStringValue(countMap,"c_num"));
								}else{
									ArrayList<String> citys=CityDAO.getAllNextCityIdsByCityPid(key);
									if(!city_id.equals(key) && citys.contains(countMap.get("city_id"))){
										map.put(key,StringUtil.getLongValue(map,key)
												+StringUtil.getLongValue(countMap,"c_num")+"");
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(Map<String, String> mp:resultList){
			long total_num=0;
			for(String key:mp.keySet()){
				if(!"version".equals(key) && !"total_num".equals(key)){
					total_num+=StringUtil.getLongValue(mp.get(key));
				}
			}
			mp.put("total_num",total_num+"");
		}
		
		Map<String,String> rmt=new HashMap<String,String>();
		rmt.put("version","小计");
		long total_num=0;
		long city_num=0;
		for(Map<String, String> cm:cityList){
			city_num=0;
			for(Map<String, String> mp:resultList){
				for(String key:mp.keySet()){
					if(cm.get("city_id").equals(key)){
						city_num+=StringUtil.getLongValue(mp.get(key));
					}
				}
			}
			rmt.put(cm.get("city_id"),city_num+"");
			total_num+=city_num;
		}
		rmt.put("total_num",total_num+"");
		resultList.add(rmt);
		
		return resultList;
	}
	
	/**
	 * 拼装页面table
	 */
	public String toConversion(String type,List<Map<String,String>> data,String city_id,List<HashMap<String, String>> cityListOrder)
	{
		return serv.toConversion(type, data, city_id, cityListOrder);
	}
	
	/**
	 * 将String转成List
	 */
	public List<Map<String, String>> stringToList(String time,String dataType) 
	{
		return serv.stringToList(time, dataType);
	}
	
	/**
	 * 查询详细设备信息
	 */
	public List<Map<String, String>> countResultByCityVersion(String type,String city_id,
			String cityType,String version,int curPage_splitPage,int num_splitPage) 
	{
		return dao.countResultByCityVersion(type,city_id,cityType,version,curPage_splitPage,num_splitPage);
	}
	
	/**
	 * 获取设备总量
	 */
	public int getCountResultByCityVersion(String type,String city_id,String cityType,String version) 
	{
		return dao.getCountResultByCityVersion(type,city_id,cityType,version);
	}
	
	/**
	 * @param city_id
	 * @return
	 * 2019-9-12
	
	 */
	public ArrayList<HashMap<String, String>> getNextCityListOrderByCityPid(String city_id)
	{
		// TODO Auto-generated method stub
		return dao.getNextCityListOrderByCityPid(city_id);
	}
	public StbCountReportDAO getDao() {
		return dao;
	}

	public void setDao(StbCountReportDAO dao) {
		this.dao = dao;
	}

	public StbCountReportBioServ getServ() {
		return serv;
	}

	public void setServ(StbCountReportBioServ serv) {
		this.serv = serv;
	}

}
