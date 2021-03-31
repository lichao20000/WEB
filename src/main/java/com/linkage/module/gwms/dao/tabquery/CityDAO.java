/*
 *
 * 创建日期 2006-2-13 Administrator suzr 
 * 
 * 修改日期 2009-09-09 onelinesky
 * 
 * 代码样式 － 代码模板
 */
package com.linkage.module.gwms.dao.tabquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class CityDAO {
	
	//日志记录
	private static Logger m_logger = LoggerFactory.getLogger(CityDAO.class);
	/**
	 * 判断是不是省中心及其更高的属地
	 */
	public static boolean isAdmin(String cityId){
		m_logger.debug("isAdmin({})",cityId);
		boolean flag = false;
		if(null==cityId || "-1".equals(cityId) || "00".equals(cityId) || "".equals(cityId)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 获取所有的属地ID(city_id)
	 * 该方法对外开放
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return ArrayList 所有的属地city_id
	 */
	public static ArrayList<String> getAllCityIdList(){
		
		m_logger.debug("getAllCityIdList()");
		
		return Global.G_CityIds;
	}
	
	/**
	 * 获取所有属地的G_CityId_CityName_Map
	 * 属地ID、属地名Map<city_id,city_name>
	 * 
	 * 该方法对外开放
	 * 
	 * @param 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * @return Map 返回全部的city_id与city_name的映射Map
	 */
	public static Map<String,String> getCityIdCityNameMap(){
		
		m_logger.debug("getCityIdCityNameMap()");
		
		return Global.G_CityId_CityName_Map;
	}
	
	/**
	 * 根据城市ID查询名称
	 * @param cityId 城市id
	 * @return 该城市ID对应的城市名称，如果城市ID不存在，则返回null
	 */
	public static String getCityName(String cityId)
	{
		if (cityId == null || cityId.trim().length() == 0)
		{
			return null;
		}
		return Global.G_CityId_CityName_Map.get(cityId);
	}
	
	/**
	 * 根据城市名称查询对应的城市ID
	 * @param cityName 城市名称
	 * @return 该城市名称对应的城市ID，如果不存在，则返回null
	 */
	public static String getCityId(String cityName)
	{
		if (cityName == null || cityName.trim().length() == 0)
		{
			return null;
		}
		Set<String> cityIdSet = Global.G_CityId_CityName_Map.keySet();
		for (String cityId : cityIdSet)
		{
			if (cityName.equals(Global.G_CityId_CityName_Map.get(cityId)))
			{
				return cityId;
			}
		}
		return null;
	}
	
	/**
	 * 获取所有子属地与父属地的对应
	 * 获取所有属地的ConcurrentHashMap<ciry_id,parent_id>
	 * 
	 * 该方法对外开放
	 * 
	 * @param 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * @return Map 返回所有的的city_id,parent_id的映射Map
	 */
	public static Map<String,String> getCityIdPidMap(){
		
		m_logger.debug("getCityIdPidMap()");
		return Global.G_City_Pcity_Map;
	}
	
	/**
	 * 根据当前属地查询该属地的所有上级属地，直至省中心（包括自己）
	 * 
	 * 此方法对外提供
	 * 
	 * @param cityId
	 * @return
	 */
	public static List<String> getAllPcityIdByCityId(String cityId){
		
		m_logger.debug("getAllPCityIdByCityId(cityId:{})",cityId);
		
		if(StringUtil.IsEmpty(cityId)){
			return null;
		}
		
		List<String> list = new ArrayList<String>();
		
		String[] cityIds = cityId.split(",");
		for(String id : cityIds)
		{
			list.add(id);
			if("-1".equals(id) || "00".equals(id)){
				continue;
			}
			
			String tempCityId = Global.G_City_Pcity_Map.get(id);
			if(!list.contains(tempCityId))
			{
				list.add(tempCityId);
			}
			while(!"00".equals(tempCityId) && null!=tempCityId){
				tempCityId = Global.G_City_Pcity_Map.get(tempCityId);
				if(!list.contains(tempCityId))
				{
					list.add(tempCityId);
				}
				
			}
		}
		return list;
	}
	
	/**
	 * 查询该属地的本地网属地（如果是省中心则返回省中心）
	 * 
	 * 此方法对外提供
	 * 
	 * @param cityId
	 * @return
	 */
	public static String getLocationCityIdByCityId(String cityId){
		
		m_logger.debug("getLocationCityIdByCityId(cityId:{})",cityId);
		
		if(null==cityId){
			return null;
		}
		
		String rsCityId = cityId;
		
		if("-1".equals(cityId) || "00".equals(cityId)){
			rsCityId = "00";
		}else{
			while(!"00".equals(Global.G_City_Pcity_Map.get(rsCityId)) && null!=(Global.G_City_Pcity_Map.get(rsCityId)) && !"-1".equals(Global.G_City_Pcity_Map.get(rsCityId))){
				rsCityId = Global.G_City_Pcity_Map.get(rsCityId);
			}
		}
		
		return rsCityId;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID(包含自己)
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return ArrayList 待查询属地的下一级子属地(包含自己),
	 */
	public static ArrayList<String> getNextCityIdsByCityPid(String m_CityPid) {
		
		m_logger.debug("getNextCityIdsByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		ArrayList<String> list = getNextCityIdsByCityPidCore(m_CityPid);
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				list.add(cityId);
			}
		}
		
		return list;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID(不包含自己)
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return ArrayList 待查询属地的下一级子属地(不包含自己),
	 */
	public static ArrayList<String> getNextCityIdsByCityPidCore(String m_CityPid) {
		
		m_logger.debug("getNextCityIdsByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				if(null != Global.G_City_Child_List_Map.get(cityId))
				{
					list.addAll(Global.G_City_Child_List_Map.get(cityId));
				}
				
			}
		}
		
//		Iterator it = Global.G_City_Pcity_Map.keySet().iterator();
//		
//		while(it.hasNext()) {
//			
//			String key = (String) it.next();
//			if(Global.G_City_Pcity_Map.get(key).equals(m_CityPid)){
//				list.add(key);
//			}
//		}
		
		return list;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID与NAME的对应关系(包含自己)
	 * 返回格式为Map<String,String>
	 * 例如：<00,省中心>
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return Map<String,String> 待查询属地ID获取下一层属地ID与NAME的对应关系(包含自己)
	 */
	public static Map<String,String> getNextCityMapByCityPid(String m_CityPid) {
		
		m_logger.debug("getNextCityIdsByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		Map<String,String> map =  getNextCityMapByCityPidCore(m_CityPid);
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				map.put(cityId, Global.G_CityId_CityName_Map.get(cityId));
			}
		}
		
		
		return map;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID与NAME的对应关系(不包含自己)
	 * 返回格式为Map<String,String>
	 * 例如：<00,省中心>
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return Map<String,String> 待查询属地ID获取下一层属地ID与NAME的对应关系(不包含自己)
	 */
	public static Map<String,String> getNextCityMapByCityPidCore(String m_CityPid) {
		
		m_logger.debug("getNextCityIdsByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		Map<String,String> map =  new ConcurrentHashMap<String,String>();
		
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		
		Iterator<String> it = Global.G_City_Pcity_Map.keySet().iterator();
		
		while(it.hasNext()) {
			
			String key = (String) it.next();
			
			if(null != cityIds)
			{
				for(String cityId : cityIds)
				{
					if(Global.G_City_Pcity_Map.get(key).equals(cityId)){
						map.put(key, Global.G_CityId_CityName_Map.get(key));
					}
				}
			}
			
		}
		
		return map;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID与NAME的对应关系(包含自己)
	 * 返回格式为List<Map<String,String>>
	 * 例如：<<city_id,00>,<city_name,省中心>>
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return List<Map<String,String>> 待查询属地ID获取下一层属地ID与NAME的对应关系(包含自己)
	 */
	public static List<Map<String,String>> getNextCityListByCityPid(String m_CityPid) {
		
		m_logger.debug("getNextCityListByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		//modify by chensq5 20151104：修改list中cityId的存放顺序，第一个应该为登录用户自身的cityid,后面才是其cityid的子id
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("city_id", cityId);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityId));
				list.add(map);
			}
		}
		
		List<Map<String,String>> childIdList =  getNextCityListByCityPidCore(m_CityPid);
		
		list.addAll(childIdList);
		
		return list;
	}
	
	/**
	 * 根据父属地ID获取下一层属地ID与NAME的对应关系(不包含自己)
	 * 返回格式为List<Map<String,String>>
	 * 例如：<<city_id,00>,<city_name,省中心>>
	 * 
	 * 此方法对外提供
	 *
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return List<Map<String,String>> 待查询属地ID获取下一层属地ID与NAME的对应关系(不包含自己)
	 */
	public static List<Map<String,String>> getNextCityListByCityPidCore(String m_CityPid) {
		
		m_logger.debug("getNextCityListByCityPidCore(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		List<Map<String,String>> list =  new ArrayList<Map<String,String>>();
		
		Iterator<String> it = Global.G_City_Pcity_Map.keySet().iterator();
		
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		
		
		while(it.hasNext()) {
			
			String key = (String) it.next();
			
			if(null != cityIds)
			{
				for(String cityId : cityIds)
				{
					if(Global.G_City_Pcity_Map.get(key).equals(cityId)){
						Map<String, String> map = new HashMap<String, String>();
						map.put("city_id", key);
						map.put("city_name", Global.G_CityId_CityName_Map.get(key));
						list.add(map);
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 查询该属地的所有子属地(包括自己)
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return ArrayList 返回待查询属地的所有子属地的city_id(包括自己)
	 */
	public static ArrayList<String> getAllNextCityIdsByCityPid(String m_CityPid){
		
		m_logger.debug("getAllNextCityIdsByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid)		
		{
			return null;
		}
		
		ArrayList<String> list = getAllNextCityIdsByCityPidCore(m_CityPid);
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				list.add(cityId);
			}
		}
		
		return list;
	}
	
	/**
	 * 查询该属地的所有子属地(不包括自己)
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return ArrayList 返回待查询属地的所有子属地的city_id(不包括自己)
	 */
	public static ArrayList<String> getAllNextCityIdsByCityPidCore(String m_CityPid){
		m_logger.debug("getAllNextCityIdsByCityPidCore(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				if(null==Global.G_City_Child_List_Map.get(cityId)){
					m_logger.warn("getAllNextCityIdsByCityPidCore({})无法获取子属地",cityId);
				}else{
					list.addAll(Global.G_City_Child_List_Map.get(cityId));
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询该属地的所有子属地的city_id与city_name的所有映射(包括自己)
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return Map 返回待属地的所有子属地的city_id与city_name的所有映射(包括自己)
	 */
	public static Map<String,String> getAllNextCityMapByCityPid(String m_CityPid){
		m_logger.debug("getAllNextCityMapByCityPid(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		
		Map<String,String> map = getAllNextCityMapByCityPidCore(m_CityPid);
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				map.put(cityId, Global.G_CityId_CityName_Map.get(cityId));
			}
		}
	
		
		return map;
	}
	
	/**
	 * 查询该属地的所有子属地的city_id与city_name的所有映射(不包括自己)
	 * 返回格式<"00","省中心">
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return Map 返回待属地的所有子属地的city_id与city_name的所有映射(不包括自己)
	 */
	public static Map<String, String> getAllNextCityMapByCityPidCore(String m_CityPid){
		m_logger.debug("getAllNextCityMapByCityPidCore(m_CityPid:{})",m_CityPid);
		
		if(null==m_CityPid){
			return null;
		}
		
		Map<String,String> map = new ConcurrentHashMap<String,String>();
		
		ArrayList<String> list = new ArrayList<String>();
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				if(null != Global.G_City_Child_List_Map.get(cityId))
				{
					list.addAll(Global.G_City_Child_List_Map.get(cityId));
				}
				
			}
		}
		
		for(String cityId:list){
			map.put(cityId, Global.G_CityId_CityName_Map.get(cityId));
		}
		
		return map;
	}
	
	/**
	 * 查询该属地的所有子属地(包括自己)
	 * 返回格式为List<Map<String,String>>
	 * 其中Map:<"city_id","00"><"city_name","省中心">
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return List 其中Map:<"city_id","00"><"city_name","省中心">(包括自己)
	 */
	public static List<Map<String,String>> getAllNextCityListByCityPid(String m_CityPid){
		m_logger.debug("getAllNextCityListByCityPid(m_CityPid:{})",m_CityPid);
		
		List<Map<String,String>> rsList = new ArrayList<Map<String,String>>();
		
		
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				Map<String,String> map = new HashMap<String,String>();
				map.put("city_id", cityId);
				map.put("city_name", Global.G_CityId_CityName_Map.get(cityId));
				rsList.add(map);
			}
		}
		
		rsList.addAll(getAllNextCityListByCityPidCore(m_CityPid));
		
		return rsList;
	}
	
	/**
	 * 查询该属地的所有子属地(不包括自己)
	 * 返回格式为List<Map<String,String>>
	 * 其中Map:<"city_id","00"><"city_name","省中心">
	 * 
	 * 此方法对外开发
	 * 
	 * @param m_CityPid 属地
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return List 其中Map:<"city_id","00"><"city_name","省中心">(不包括自己)
	 */
	public static List<Map<String,String>> getAllNextCityListByCityPidCore(String m_CityPid){
		m_logger.debug("getAllNextCityListByCityPidCore(m_CityPid:{})",m_CityPid);
		
		List<Map<String,String>> rsList = new ArrayList<Map<String,String>>();
		ArrayList<String> list = new ArrayList<String>();
		String[] cityIds = null;
		if(!StringUtil.IsEmpty(m_CityPid))
		{
			cityIds = m_CityPid.split(",");
		}
		if(null != cityIds)
		{
			for(String cityId : cityIds)
			{
				if(null != Global.G_City_Child_List_Map.get(cityId))
				{
					list.addAll(Global.G_City_Child_List_Map.get(cityId));
				}
				
			}
		}
//		ArrayList<String> list = Global.G_City_Child_List_Map.get(m_CityPid);
		
		for(String cityId:list){
			Map<String,String> map = new HashMap<String,String>();
			map.put("city_id", cityId);
			map.put("city_name", Global.G_CityId_CityName_Map.get(cityId));
			rsList.add(map);
		}
		return rsList;
	}
	
	
////////////////////////////////////////////////////////////////////////////////
/////////////////////以下方法为只供系统初始化调用或者该类内部调用///////////////////////
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取所有的属地ID(city_id)
	 * 该方法不对外开放
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @param 
	 * @author qixueqi
	 * @date 2009-5-4
	 * @return ArrayList 经查询数据库的全部属地的city_id
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getAllCityIdListCore(){
		
		m_logger.debug("getAllCityIdListCore()");
		
		String strSQL = " select city_id from tab_city order by city_id ";
		ArrayList<String> resultList = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List<Map> list = DataSetBean.executeQuery(strSQL, new String[0]);
		for(Map map : list){
			resultList.add(String.valueOf(map.get("city_id")));
		}
		
		return resultList;
	}
	
	/**
	 * 获取所有属地的G_CityId_CityName_Map
	 * 属地ID、属地名Map<city_id,city_name>
	 * 
	 * 该方法不对外开放
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @param 
	 * @author qixueqi
	 * @date 2009-5-4
	 * @return Map 返回经查询数据库的全部的city_id与city_name的映射Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCityIdCityNameMapCore(){
		
		m_logger.debug("getCityIdCityNameMapCore()");
		
		String strSQL = "select city_id, city_name from tab_city order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map); 
		return resultMap;
	}
	
	/**
	 * 获取所有子属地与父属地的对应
	 * 获取所有属地的ConcurrentHashMap<ciry_id,parent_id>
	 * 
	 * 该方法不对外开放
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @param 
	 * @author qixueqi
	 * @date 2009-5-4
	 * @return Map 返回经查询数据库的city_id,parent_id的映射Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCityIdPidMapCore(){
		
		m_logger.debug("getCityIdPidMapCore()");
		
		String strSQL = "select city_id, parent_id from tab_city order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map);
		return resultMap;
	}
	
	/**
	 * @category 查询子节点，不包含自己
	 * 
	 * 此方法不对外开发
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @param cityMap
	 * @param cityId
	 * 
	 * @author qixueqi
	 * @date 2009-9-14
	 * @return ArrayList 
	 */
	private ArrayList<String> getAllChild(Map<String,String> cityMap,String cityId){
		
		m_logger.debug("getAllChild(cityMap({}),cityId({}))");
		
		ArrayList<String> childList = new ArrayList<String>();
		Set<String> citySet = cityMap.keySet();
		for (String strCityId : citySet) {
			String strPid = cityMap.get(strCityId);
			if (cityId.equals(strPid)) {
				childList.add(strCityId);
			}
		}
		if(childList.size()>1){
			for(int i=0;i<childList.size();i++){
				childList.addAll(getAllChild(cityMap, childList.get(i)));
			}
		}
		return childList;
	}
	
	/**
	 * 查询出所有的属地的子属地List集合，并将city_id,List形成Map返回
	 * 
	 * 此方法不对外开发
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @author onelinesky(4174)
	 * @date 2009-9-14
	 * 
	 * @return Map 返回所有的属地与该属地的所有的子属地对应关系
	 */
	public Map<String, ArrayList<String>> getAllCityIdChildListMap(){
		
		m_logger.debug("getAllCityIdChildListMap()");
		
		Map<String, ArrayList<String>> resultMap = new ConcurrentHashMap<String, ArrayList<String>>();
		
		if(null==Global.G_CityIds || Global.G_CityIds.size()<1){
			m_logger.error("Global.G_CityIds没有初始化！");
		}
		if(null==Global.G_City_Pcity_Map || Global.G_City_Pcity_Map.size()<1){
			m_logger.error("Global.G_City_Pcity_Map没有初始化！");
		}
		
		for(int i=0;i<Global.G_CityIds.size();i++){
			String cityId = Global.G_CityIds.get(i);
			resultMap.put(cityId, getAllChild(Global.G_City_Pcity_Map,cityId));
		}
		
		return resultMap;
	}
	
	private String m_CityInfoDel_SQL = "delete from tab_city where city_id=?";

	/**
	 * 根据属地ID和属地名称判断新增属地是否重复
	 */
	private String m_CityDupli_SQL = "select * from tab_city where city_id=? or city_name=?";

	/**
	 * 根据属地新旧ID和属地名称判断新增属地是否重复
	 */
	private String m_CityDulliMore_SQL = "select * from tab_city where (city_id=? or city_name=?) and city_id<>?";
	
	private String m_ParentID_SQL = "select * from tab_city where (city_id=? or city_name=?) and parent_id<>?";
	
	/**
	 * 新增属地信息
	 */
	private String m_CityInfoAdd_SQL = "insert into tab_city (city_id,city_name,staff_id,remark,parent_id) values (?,?,?,?,?)";

	/**
	 * 根据指定属地编码更新属地信息
	 */
	private String m_CityInfoUpdate_SQL = "update tab_city set city_id=?,city_name=?,staff_id=?,remark=?,parent_id=? where city_id=?";

	/**
	 * 根据指定属地编码获得属地信息
	 */
	private String m_CityInfo_ByCityId_SQL = "select * from tab_city where city_id=?";
    
	/**
	 * 分页查询，获得所有属地 modify by YYS 2006-10-12 根据登陆用户权限呈现属地
	 *
	 * @param request
	 * @return ArrayList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getCityList(HttpServletRequest request) {
		
		ArrayList list = new ArrayList();
		list.clear();

		String stroffset = request.getParameter("offset");
		String city_name = request.getParameter("city_name");
		int pagelen = 15;
		int offset;
		if (null == stroffset)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		
        QueryPage qryp = new QueryPage();
        PrepareSQL pSQL = new PrepareSQL();
       /** if(DBUtil.GetDB()==Global.DB_MYSQL){
        	//TODO wait
        	//无引用
        } */
        pSQL.append("select * FROM tab_city where city_id in (?) ");
        
        // 山西联通属地资源增加按属地名查询
        if(Global.SXLT.equals(Global.instAreaShortName)) 
        {
        	if(!StringUtil.IsEmpty(city_name)) 
        	{
        		pSQL.append(" and city_name like '%" + city_name + "%' ");
        	}
        	pSQL.append(" order by city_id");
        }
        
        
        HttpSession session = request.getSession();
        UserRes curUser = (UserRes)session.getAttribute("curUser");
        String city = curUser.getCityId();
       
        pSQL.setStringExt(1, StringUtils.weave(getAllNextCityIdsByCityPid(city)),false);
		
        qryp.initPage(pSQL.getSQL(), offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL(), offset, pagelen);
		list.add(cursor);

		return list;
	}

	/**
	 * 对属地信息进行操作（增、删、改）
	 *
	 * @param request
	 * @return String
	 */
	public synchronized String cityActDo(HttpServletRequest request) {
		
		int iCode = 0;
		
		PrepareSQL pSQL = new PrepareSQL();
		
		String strSQL = null;
		String strMsg = null;
		String strAction = request.getParameter("action");
		if ("delete".equals(strAction)) { // 删除操作
			String str_city_id = request.getParameter("city_id");
			if(getAllNextCityIdsByCityPidCore(str_city_id).size()>0){
				strMsg = "属地编号\"" + str_city_id.trim() + "\"或属地名称\""
				+ getCityIdCityNameMap().get(str_city_id.trim()) + "\"存在子属地，请先删除子属地。";
			}else{
				pSQL.setSQL(m_CityInfoDel_SQL);
				pSQL.setString(1, str_city_id);
				strSQL = pSQL.getSQL();
				
				iCode = DataSetBean.executeUpdate(strSQL);
				if (iCode > 0) {
					strMsg = "属地资源操作成功！";
					Global.G_CityIds.remove(str_city_id);
					Global.G_CityId_CityName_Map.remove(str_city_id);
					Global.G_City_Pcity_Map.remove(str_city_id);
					Global.G_City_Child_List_Map.remove(str_city_id);
				}else{
					strMsg = "属地资源操作失败，请返回重试或稍后再试！";
				}
			}
			
			str_city_id = null;
		} else {
			String city_id_old = request.getParameter("city_id_old");
			String str_city_id = request.getParameter("city_id");
			String str_city_name = request.getParameter("city_name");
			String str_parent_id = request.getParameter("parent_id");
			String str_staff_id = request.getParameter("staff_id");
			String str_remark = request.getParameter("remark");

			if ("add".equals(strAction)) { // 增加操作
				// 判断是否重复
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					pSQL.append("select city_id,city_name ");
					pSQL.append("from tab_city where city_id=? or city_name=? ");
				}else{
					pSQL.setSQL(m_CityDupli_SQL);
				}
				
				pSQL.setString(1, str_city_id.trim());
				pSQL.setString(2, str_city_name.trim());
				strSQL = pSQL.getSQL();
				if (null  != DataSetBean.getRecord(strSQL)){
					strMsg = "属地编号\"" + str_city_id.trim() + "\"或属地名称\""
							+ str_city_name.trim() + "\"已经存在，请换一个属地编号或属地名称。";
				}else {
					pSQL.setSQL(m_CityInfoAdd_SQL);
					pSQL.setString(1, str_city_id);
					pSQL.setString(2, str_city_name);
					pSQL.setString(3, str_staff_id);
					pSQL.setString(4, str_remark);
					pSQL.setString(5, str_parent_id);
					strSQL = pSQL.getSQL();
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",)", ",null)");
					
					iCode = DataSetBean.executeUpdate(strSQL);
					if (iCode > 0) {
						strMsg = "属地资源操作成功！";
						Global.G_CityIds.add(str_city_id);
						Global.G_CityId_CityName_Map.put(str_city_id,str_city_name);
						Global.G_City_Pcity_Map.put(str_city_id,str_parent_id);
						Global.G_City_Child_List_Map.put(str_city_id,getAllChild(Global.G_City_Pcity_Map,str_city_id));
						Global.G_City_Child_List_Map.put(str_parent_id,getAllChild(Global.G_City_Pcity_Map,str_parent_id));
						// 20200511 解决添加完成后 省中心子属地没有新增属地的问题
						Global.G_City_Child_List_Map.put("00",getAllChild(Global.G_City_Pcity_Map,"00"));
					}else{
						strMsg = "属地资源操作失败，请返回重试或稍后再试！";
					}
				}
			} else { // 修改操作
				// 判断是否重复
				if(DBUtil.GetDB()==Global.DB_MYSQL){
					pSQL.append("select city_id,city_name from tab_city ");
					pSQL.append("where (city_id=? or city_name=?) and city_id<>? ");
				}else{
					pSQL.setSQL(m_CityDulliMore_SQL);
				}
				
				pSQL.setString(1, str_city_id);
				pSQL.setString(2, str_city_name);
				pSQL.setString(3, city_id_old);

				if (null  != DataSetBean.getRecord(pSQL.getSQL())){
					strMsg = "属地编号\"" + str_city_id + "\"或属地名称\""
							+ str_city_name + "\"已经存在，请换一个属地编号或属地名称。";
				}
				else {
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						pSQL.append("select city_id,city_name from tab_city ");
						pSQL.append("where (city_id=? or city_name=?) and parent_id<>? ");
					}else{
						pSQL.setSQL(m_ParentID_SQL);
					}
					
					pSQL.setString(1, str_city_id);
					pSQL.setString(2, str_city_name);
					pSQL.setString(3, str_parent_id);
					if (null  != DataSetBean.getRecord(pSQL.getSQL())) {
						strMsg = "属地编号\"" + str_city_id + "\",属地名称\""
								+ str_city_name + "\"的父属地不正确，请重新选择。";
					} else {
						pSQL.setSQL(m_CityInfoUpdate_SQL);
						pSQL.setString(1, str_city_id);
						pSQL.setString(2, str_city_name);
						pSQL.setString(3, str_staff_id);
						pSQL.setString(4, str_remark);
						pSQL.setString(5, str_parent_id);
						pSQL.setString(6, city_id_old);
						strSQL = pSQL.getSQL();
						strSQL = StringUtils.replace(strSQL, "=,", "=null,");
						strSQL = StringUtils.replace(strSQL, "= where",
								"=null where");
						iCode = DataSetBean.executeUpdate(strSQL);
						if (iCode > 0) {
							strMsg = "属地资源操作成功！";
							
							Global.G_CityIds.remove(city_id_old);
							Global.G_CityIds.add(str_city_id);
							
							Global.G_CityId_CityName_Map.remove(city_id_old);
							Global.G_CityId_CityName_Map.put(str_city_id,str_city_name);
							
							Global.G_City_Pcity_Map.remove(city_id_old);
							Global.G_City_Pcity_Map.put(str_city_id,str_parent_id);
							
							Global.G_City_Child_List_Map.remove(city_id_old);
							Global.G_City_Child_List_Map.put(str_city_id,getAllChild(Global.G_City_Pcity_Map,str_city_id));
							
						}else{
							strMsg = "属地资源操作失败，请返回重试或稍后再试！";
						}
					}
				}
			}
		}
		if (null == strMsg && strSQL.equals("")) {
			strMsg = "属地资源操作失败，请返回重试或稍后再试！";
		}
		
		return strMsg;
	}
	
	/**
	 * 根据指定属地编码获得属地信息
	 *
	 * @param m_CityId
	 * @return Map
	 */
	public Map getCityInfoById(String m_CityId) {
		PrepareSQL pSQL = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			//无引用
		} */
		pSQL.setSQL(m_CityInfo_ByCityId_SQL);
		pSQL.setString(1, m_CityId);
		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 获取所有属地的二级disCityId_CityName_Map
	 * 属地ID、属地名Map<city_id,city_name>
	 * 
	 * 该方法不对外开放
	 * 只供系统初始化调用或者该类内部调用
	 * 
	 * @param 
	 * @author qixueqi
	 * @date 2009-5-4
	 * @return Map 返回经查询数据库的全部的city_id与city_name的映射Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getSencondCityIdCityNameMap(){		
		String strSQL = "select city_id, city_name from tab_city where parent_id = '00' order by city_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Map map = DataSetBean.getMap(strSQL);
		Map<String, String> resultMap = new ConcurrentHashMap<String, String>();
		resultMap.putAll(map); 
		return resultMap;
	}
}