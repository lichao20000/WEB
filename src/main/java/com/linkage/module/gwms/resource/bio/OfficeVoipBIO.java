package com.linkage.module.gwms.resource.bio;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.dao.tabquery.CityOfficeZoneDAO;
import com.linkage.module.gwms.dao.tabquery.OfficeDAO;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-9-25
 */
public class OfficeVoipBIO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(OfficeVoipBIO.class);
	
	CityOfficeZoneDAO cityOfficeZoneDao;
	
	
	/**
	 * 获取属地下面所有的局向对应服务器地址的对象List
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-25
	 * @return List
	 */
	public List getOfficeVoipList(String cityId){
		logger.debug("getOfficeVoipList({})", cityId);
		List offVoipList = null;
		List<String> officeList = null;
		List<String> cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		if(null != cityList && false == cityList.isEmpty()){
			officeList = new ArrayList<String>();
			Map<String, List<String>> cityOfficeMap = cityOfficeZoneDao.getCityIdOfficeIds();
			for(String city : cityList){
				List<String> tmpList = null;
				if(null != (tmpList = cityOfficeMap.get(city))){
					officeList.addAll(tmpList);
				}
			}
			if(null != officeList && false == officeList.isEmpty()){
				Map offvoipObjMap = OfficeDAO.getInstance().getOfficeIdVoip();
				if(null != offvoipObjMap && false == offvoipObjMap.isEmpty()){
					offVoipList = new ArrayList();
					Map offIdNameMap = OfficeDAO.getInstance().getOfficeIdNameMap();
					Map officeCityMap = cityOfficeZoneDao.getOfficeIdCityId();
					if(null != offIdNameMap && null != officeCityMap){
						Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
						for(String officeId : officeList){
							VoiceServiceProfileObj obj = (VoiceServiceProfileObj)offvoipObjMap.get(officeId);
							Map tmap = new HashMap();
							if(null == obj){
								obj = new VoiceServiceProfileObj();
							}
							tmap.put("proxServ", obj.getProxServ());
							tmap.put("proxPort", obj.getProxPort());
							tmap.put("proxServ2", obj.getProxServ2());
							tmap.put("proxPort2", obj.getProxPort2());
							tmap.put("regiServ", obj.getRegiServ());
							tmap.put("regiPort", obj.getRegiPort());
							tmap.put("standRegiServ", obj.getStandRegiServ());
							tmap.put("standRegiPort", obj.getStandRegiPort());
							tmap.put("outBoundProxy", obj.getOutBoundProxy());
							tmap.put("outBoundPort", obj.getOutBoundPort());
							tmap.put("standOutBoundProxy", obj.getStandOutBoundProxy());
							tmap.put("standOutBoundPort", obj.getStandOutBoundPort());
							obj.setOfficeId(officeId);
							tmap.put("officeName", offIdNameMap.get(officeId));
							tmap.put("cityId", officeCityMap.get(officeId));
							tmap.put("cityName", cityMap.get(officeCityMap.get(officeId)));
							offVoipList.add(tmap);
						}
						cityMap = null;
					}
				}else{
					logger.warn("officeVoipListMap is null");
				}
			}else{
				logger.warn("officeList is null");
			}
		}else{
			logger.warn("cityList is null");
		}
		cityList = null;
		return offVoipList;
	}

	/**
	 * Object To Map 对象按属相翻译为map
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-25
	 * @return Map
	 */
	public Map obj2Map(Object obj){
		logger.debug("obj2Map({})", obj);
		Map rMap = null;
		if(null != obj){
			Field[] fieldArr = obj.getClass().getFields();
			if(null != fieldArr){
				rMap = new HashMap();
				for(Field field : fieldArr){
					String tmpStr = null;
					try {
						tmpStr = StringUtil.getStringValue(field.get(tmpStr));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					logger.debug(field.getName() + "--:--" + tmpStr);
					rMap.put(field.getName(), tmpStr);
				}
			}else{
				logger.debug("obj has no fields ");
			}
		}else{
			logger.debug("obj is null ");
		}
		logger.debug("rMap:" + rMap);
		return rMap;
	}
	
	
	public void setCityOfficeZoneDao(CityOfficeZoneDAO cityOfficeZoneDao) {
		this.cityOfficeZoneDao = cityOfficeZoneDao;
	}

	/**
	 * 获取属地下面所有的局向对应服务器地址的对象List
	 *
	 * @author wangsenbo
	 * @date Jan 20, 2010
	 * @param 
	 * @return List
	 */
	public List getOfficeVoipList(String cityId, int curPage_splitPage, int num_splitPage)
	{
		return cityOfficeZoneDao.getOfficeVoipList(cityId, curPage_splitPage, num_splitPage);
	}

	public int getOfficeVoipCount(String cityId, int curPage_splitPage, int num_splitPage)
	{
		return cityOfficeZoneDao.getOfficeVoipCount(cityId, curPage_splitPage, num_splitPage);
	}

}
