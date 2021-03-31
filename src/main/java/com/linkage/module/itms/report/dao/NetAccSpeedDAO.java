package com.linkage.module.itms.report.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class NetAccSpeedDAO extends SuperDAO{

	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(NetAccSpeedDAO.class);

	private Map<String, String> venderMap = null;       // 设备厂商
	private Map<String, String> deviceModelMap = null;  // 设备型号
	private Map<String, String> deviceSoftVersionMap = null;  // 设备版本
	private Map<String, String> devicehardwareversionMap = null;  // 设备硬件版本
	private Map<String, String> cityMap = null;
	
	@SuppressWarnings("rawtypes")
	public Map<String, String> getAllrealSpeedList(String cityId,
			String starttime1, String endtime1) 
	{
		StringBuffer psql = new StringBuffer();
		psql.append("select c.city_id,count(*) num from tab_netacc_spead g,tab_gw_device c,");
		psql.append("(select h.loid,h.username,h.device_id,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed ");
		}else{
			psql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed ");
		}
		
		psql.append(" from tab_http_special_speed_intf h where h.rstcode = 0 ");
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psql.append(" and h.test_time >  ?  " );
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psql.append(" and h.test_time <  ? " );
		}
		psql.append(" group by h.loid,h.username,h.device_id) t ");
		psql.append(" where t.username = g.username and c.device_id = t.device_id ");
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and c.city_id in(");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" group by c.city_id ");
		PrepareSQL sql = new PrepareSQL(psql.toString());
		int i= 1;
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			sql.setLong(i, StringUtil.getLongValue(starttime1));
			i = i +1;
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			sql.setLong(i, StringUtil.getLongValue(endtime1));
			i = i +1;
		}
		@SuppressWarnings("unchecked")
		List<Map<String,String>> queryForList = jt.queryForList(sql.getSQL());
		Map<String,String> result = new HashMap<String,String>();
		for (Map<String,String> object : queryForList) {
			result.put(object.get("city_id"), 
					StringUtil.getStringValue(StringUtil.getLongValue(object.get("num"))));
		}
		return result;
	}
	
	

	public Map<String, String> succuserspeedMap(String cityId,
			String starttime1, String endtime1) 
	{
		StringBuffer psql = new StringBuffer();
		psql.append("select c.city_id,count(*) num from tab_netacc_spead g,tab_gw_device c, ");
		psql.append("(select h.loid,h.username,h.device_id,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed ");
		}else{
			psql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed ");
		}
		
		psql.append(" from tab_http_special_speed_intf h where h.rstcode = 0 ");
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psql.append(" and h.test_time >  ? " );
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psql.append(" and h.test_time <  ?" );
		}
		psql.append(" group by h.loid,h.username,h.device_id) t ");
		psql.append(" where t.realspeed >= g.downlink and t.username = g.username and c.device_id = t.device_id ");
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and c.city_id in(");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" group by c.city_id ");
		
		PrepareSQL psqssl = new PrepareSQL(psql.toString());
		int i= 1;
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psqssl.setLong(i, StringUtil.getLongValue(starttime1));
			i = i +1;
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psqssl.setLong(i, StringUtil.getLongValue(endtime1));
			i = i +1;
		}
		
		@SuppressWarnings("unchecked")
		List<Map<String,String>> queryForList = jt.queryForList(psqssl.getSQL());
		Map<String,String> result = new HashMap<String,String>();
		for (Map<String,String> object : queryForList) {
			result.put(object.get("city_id"),  
					StringUtil.getStringValue(StringUtil.getLongValue(object.get("num"))));
		}
		return result;
	}

	public Map<String, String> falueuserspeedMap(String cityId,
			String starttime1, String endtime1) 
	{
		StringBuffer psql = new StringBuffer();
		
		psql.append("select c.city_id,count(*) num from tab_netacc_spead g,tab_gw_device c, ");
		psql.append("(select h.loid,h.username,h.device_id,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed ");
		}else{
			psql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed ");
		}
		
		psql.append(" from tab_http_special_speed_intf h where h.rstcode = 0 ");
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psql.append(" and h.test_time >  ? " );
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psql.append(" and h.test_time <  ? " );
		}
		psql.append(" group by h.loid,h.username,h.device_id) t ");
		psql.append(" where t.realspeed < g.downlink and t.username = g.username and c.device_id = t.device_id ");
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			@SuppressWarnings("rawtypes")
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and c.city_id in(");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		psql.append(" group by c.city_id ");
		PrepareSQL psqlstr = new PrepareSQL(psql.toString());
		int i= 1;
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psqlstr.setLong(i, StringUtil.getLongValue(starttime1));
			i = i +1;
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psqlstr.setLong(i, StringUtil.getLongValue(endtime1));
			i = i +1;
		}
		
		@SuppressWarnings("unchecked")
		List<Map<String,String>> queryForList = jt.queryForList(psqlstr.getSQL());
		Map<String,String> result = new HashMap<String,String>();
		for (Map<String,String> object : queryForList) {
			result.put(object.get("city_id"), 
					StringUtil.getStringValue(StringUtil.getLongValue(object.get("num"))));
		}
		return result;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getNextCityIdsByCityPid(String cityId)
	{
		String getNextCityIdsByCityPid = "select city_id from tab_city where (parent_id ='" +
				cityId + "' or city_id = ? ) order by city_id";
		
		PrepareSQL psql = new PrepareSQL(getNextCityIdsByCityPid);
		psql.setString(1, cityId);
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1)
					throws java.sql.SQLException
			{
				Map m = new HashMap();
				m.put("city_id", rs.getString("city_id"));
				return m;
			}
		});
		return list;
	}



	@SuppressWarnings("rawtypes")
	public List<Map> getList(String flag, String cityId, String starttime1,
			String endtime1, int curPage_splitPage, int num_splitPage) {
		StringBuffer psql = new StringBuffer();
		
		psql.append("select c.city_id,g.downlink,t.realspeed,c.device_id,t.loid as username,t.username as netuser  from tab_netacc_spead g,tab_gw_device c,  ");
		psql.append("(select h.loid,h.username,h.device_id,");
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed  ");
		}else{
			psql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed  ");
		}
		
		psql.append(" from tab_http_special_speed_intf h where h.rstcode = 0 ");
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psql.append(" and h.test_time >  ?" );
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psql.append(" and h.test_time <  ?" );
		}
		psql.append(" group by h.loid,h.username,h.device_id) t ");
		psql.append(" where t.username = g.username and c.device_id = t.device_id ");
		if("1".equals(flag)){
			psql.append(" and t.realspeed >= g.downlink ");
		}
		if("2".equals(flag)){
			psql.append(" and t.realspeed < g.downlink ");
		}
		
		if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and c.city_id in(");
			psql.append(StringUtils.weave(cityIdList));
			psql.append(")");
			cityIdList = null;
		}
		
		PrepareSQL psqlstr = new PrepareSQL(psql.toString());
		int i= 1;
		if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
			psqlstr.setLong(i, StringUtil.getLongValue(starttime1));
			i = i +1;
		}
		if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
			psqlstr.setLong(i, StringUtil.getLongValue(endtime1));
			i = i +1;
		}
		
		venderMap = getVenderMap();                        // 设备厂商
		deviceModelMap = getDeviceModelMap();              // 设备型号
		deviceSoftVersionMap = getDeviceSoftVersionMap();  // 设备版本
		cityMap = CityDAO.getCityIdCityNameMap();
		
		List<Map> list = querySP(psqlstr.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException{
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("username", StringUtil.getStringValue(rs.getString("username")));
				map.put("downlink", StringUtil.getStringValue(rs.getString("downlink")));
				String realspeedtest = rs.getString("realspeed");
					//查询测速时间
				PrepareSQL pSql = new PrepareSQL();
				pSql.append("select test_time from tab_http_special_speed_intf a,");
				pSql.append("(select h.username,");
				if(DBUtil.GetDB()==3){
					//TODO wait
					pSql.append("max(to_number(h.maxsampledtotalvalues)) as realspeed ");
				}else{
					pSql.append("max(to_number(h.maxsampledtotalvalues)) as realspeed ");
				}
				pSql.append("from tab_http_special_speed_intf h ");
				pSql.append("where h.username='"+rs.getString("netuser")+"' ");
				pSql.append("and h.rstcode=0 and h.loid='"+rs.getString("username")+ "' ");
				pSql.append("and h.device_id="+rs.getString("device_id")+" ");
				pSql.append("group by h.username) b ");
				pSql.append("where a.username=b.username and a.maxsampledtotalvalues=b.realspeed");
				
				List listtime = jt.queryForList(pSql.getSQL());
				if (!listtime.isEmpty()) {
					Map maptime = (Map) listtime.get(0);
					String complete_time = StringUtil.getStringValue(maptime.get("test_time")); 
					long opertime = StringUtil.getLongValue(complete_time) * 1000L;
					DateTimeUtil dt = new DateTimeUtil(opertime);
					map.put("test_time",dt.getLongDate() );
				}
				double result = new BigDecimal(realspeedtest).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				map.put("realspeed",StringUtil.getStringValue(result));
			
				String city_id = StringUtil.getStringValue(rs.getString("city_id"));
				if (false == StringUtil.IsEmpty(city_id)){
					String city_name = StringUtil.getStringValue(cityMap.get(city_id));
					map.put("city_name", city_name);
				}else{
					map.put("city_name", "");
				}
				
				String device_id = StringUtil.getStringValue(rs.getString("device_id"));
				if(!StringUtil.IsEmpty(device_id))
				{
					PrepareSQL pSqlvendor = new PrepareSQL();
					pSqlvendor.append("select vendor_id,device_model_id,devicetype_id ");
					pSqlvendor.append("from tab_gw_device where device_id='"+device_id+"'");
					
					List list = jt.queryForList(pSqlvendor.getSQL());
					if (!list.isEmpty()) {
						Map rmap = (Map) list.get(0);
						
						String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));  // 厂商ID
						String device_model_id = StringUtil.getStringValue(rmap.get("device_model_id"));  // 型号ID
						String devicetype_id = StringUtil.getStringValue(rmap.get("devicetype_id"));   // 版本ID
						
						if (!StringUtil.IsEmpty(vendor_id)) {
							map.put("vendor_add", venderMap.get(vendor_id));  // 设备厂商
						}else {
							map.put("vendor_add", "-");  // 设备厂商
						}
						
						if (!StringUtil.IsEmpty(device_model_id)) {
							map.put("device_model", deviceModelMap.get(device_model_id));  // 设备型号
						} else {
							map.put("device_model", "-");  // 设备型号
						}
						
						if(!StringUtil.IsEmpty(devicetype_id)){
							map.put("softversion", deviceSoftVersionMap.get(devicetype_id));  // 设备版本
							map.put("hardwareversion", devicehardwareversionMap.get(devicetype_id));  // 设备硬件版本
						} else {
							map.put("softversion", "-");   // 设备版本
							map.put("hardwareversion", "-");   // 设备硬件版本
						}
					}
				
				}
				return map;
			}
		});
		
		return list;
	}


		/**
		 * 取得所有vendorId和厂商名对应的MAP
		 * 
		 * @param request
		 * @return ArrayList
		 */
		public HashMap<String, String> getVenderMap() 
		{
			logger.debug("getVenderMap()");
			PrepareSQL psql = new PrepareSQL("select vendor_id,vendor_name,vendor_add from tab_vendor");
			
			List<Map> list = jt.queryForList(psql.getSQL());
			HashMap<String, String> vendorMap = new HashMap<String, String>();
			for(Map<String, String> map : list){
				String vendorAdd = StringUtil.getStringValue(map.get("vendor_add"));
				if(false == StringUtil.IsEmpty(vendorAdd)){
					vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), vendorAdd);
				}else {
					vendorMap.put(StringUtil.getStringValue(map.get("vendor_id")), 
									StringUtil.getStringValue(map.get("vendor_name")));
				}
			}
			return vendorMap;
		}
		
		
		/**
		 * 获取设备型号map
		 * 
		 * @param request
		 * @return ArrayList
		 */
		public HashMap<String, String> getDeviceModelMap() 
		{
			logger.debug("getDeviceModelMap()");
			String devicemodel = "";
			String device_model_id = "";
			HashMap<String, String> deviceTypeMap = new HashMap<String,String>();

			PrepareSQL psql = new PrepareSQL();
			psql.append("select device_model_id,device_model from gw_device_model");
	    	
	    	List<Map> list = jt.queryForList(psql.getSQL());
	    	
	    	for (Map<String, String> map : list) {
	    		devicemodel = StringUtil.getStringValue(map.get("device_model"));
	    		device_model_id = StringUtil.getStringValue(map.get("device_model_id"));
				deviceTypeMap.put(device_model_id, devicemodel);
			}
	    	return deviceTypeMap;
		}
		
		/**
		 * 获取设备版本map
		 * 
		 * @param request
		 * @return ArrayList
		 */
		public HashMap<String, String> getDeviceSoftVersionMap()
		{
			logger.debug("getDeviceModelMap()");
			
			String softwareversion = "";
			String hardwareversion = "";
			String devicetype_id = "";
			HashMap<String, String> deviceTypeMap = new HashMap<String,String>();
			devicehardwareversionMap = new HashMap<String,String>();

			PrepareSQL psql = new PrepareSQL();
			psql.append("select b.softwareversion,b.hardwareversion,b.devicetype_id ");
			psql.append("from tab_devicetype_info b ");
	    	
	    	List<Map> list = jt.queryForList(psql.getSQL());
	    	
	    	for (Map<String, String> map : list) {
	    		softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
	    		hardwareversion = StringUtil.getStringValue(map.get("hardwareversion"));
				devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
				deviceTypeMap.put(devicetype_id, softwareversion);
				devicehardwareversionMap.put(devicetype_id, hardwareversion);
			}
	    	return deviceTypeMap;
		}



		public int getListCount(String flag, String cityId,
				String starttime1, String endtime1, int curPage_splitPage,
				int num_splitPage) 
		{
			PrepareSQL sql = new PrepareSQL();
			sql.append("select count(*) from tab_netacc_spead g,tab_gw_device c,");
			sql.append("(select h.loid,h.username,h.device_id,");
			if(DBUtil.GetDB()==3){
				//TODO wait
				sql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed ");
			}else{
				sql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed ");
			}
			
			sql.append("from tab_http_special_speed_intf h where h.rstcode = 0 ");
			if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
				sql.append(" and h.test_time >  " + StringUtil.getLongValue(starttime1) );
			}
			if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
				sql.append(" and h.test_time <  " + StringUtil.getLongValue(endtime1));
			}
			sql.append(" group by h.loid,h.username,h.device_id) t ");
			sql.append(" where t.username = g.username and c.device_id = t.device_id ");
			if("1".equals(flag)){
				sql.append(" and t.realspeed >= g.downlink ");
			}
			if("2".equals(flag)){
				sql.append(" and t.realspeed < g.downlink ");
			}
			
			if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and c.city_id in(");
				sql.append(StringUtils.weave(cityIdList));
				sql.append(")");
				cityIdList = null;
			}
			int total = jt.queryForInt(sql.getSQL());
			int maxPage = 1;
			if (total % num_splitPage == 0){
				maxPage = total / num_splitPage;
			}else{
				maxPage = total / num_splitPage + 1;
			}
			
			return maxPage;
		}



		@SuppressWarnings("unchecked")
		public List<Map> getdetailExcel(String flag, String cityId,
				String starttime1, String endtime1) 
		{
			PrepareSQL psql = new PrepareSQL();
			psql.append("select c.city_id,g.downlink,t.realspeed,c.device_id,t.loid as username,t.username as netuser ");
			psql.append("from tab_netacc_spead g,tab_gw_device c,");
			psql.append("(select h.loid,h.username,h.device_id,");
			if(DBUtil.GetDB()==3){
				//TODO wait
				psql.append("max(cast(h.maxsampledtotalvalues as signed))*8/1024 as realspeed ");
			}else{
				psql.append("max(to_number(h.maxsampledtotalvalues))*8/1024 as realspeed ");
			}
			psql.append("from tab_http_special_speed_intf h where h.rstcode = 0 ");
			if(!StringUtil.IsEmpty(starttime1) && !"0".equals(starttime1)){
				psql.append(" and h.test_time >  "+StringUtil.getLongValue(starttime1) );
			}
			if(!StringUtil.IsEmpty(endtime1) && !"0".equals(endtime1)){
				psql.append(" and h.test_time <  "+ StringUtil.getLongValue(endtime1));
			}
			psql.append(" group by h.loid,h.username,h.device_id) t ");
			psql.append(" where t.username = g.username and c.device_id = t.device_id ");
			if("1".equals(flag)){
				psql.append(" and t.realspeed >= g.downlink ");
			}
			if("2".equals(flag)){
				psql.append(" and t.realspeed < g.downlink ");
			}
			
			if(!StringUtil.IsEmpty(cityId) && !"00".equals(cityId)){
				List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
				psql.append(" and c.city_id in(");
				psql.append(StringUtils.weave(cityIdList));
				psql.append(")");
				cityIdList = null;
			}
			
			PrepareSQL psqlstr = new PrepareSQL(psql.toString());
			
				venderMap = getVenderMap();                        // 设备厂商
				deviceModelMap = getDeviceModelMap();              // 设备型号
				deviceSoftVersionMap = getDeviceSoftVersionMap();  // 设备版本
				cityMap = CityDAO.getCityIdCityNameMap();
				
				List<Map> list = jt.query(psqlstr.getSQL(), new RowMapper(){

					public Object mapRow(ResultSet rs, int arg1) throws SQLException{
						
						Map<String, String> map = new HashMap<String, String>();
						
						map.put("username", StringUtil.getStringValue(rs.getString("username")));
						map.put("downlink", StringUtil.getStringValue(rs.getString("downlink")));
						String realspeedtest = rs.getString("realspeed");
							//查询测速时间
						PrepareSQL pSql = new PrepareSQL();
						pSql.append("select test_time from tab_http_special_speed_intf a,");
						pSql.append("(select h.username,");
						if(DBUtil.GetDB()==3){
							//TODO wait
							pSql.append("max(cast(h.maxsampledtotalvalues as signed)) as realspeed ");
						}else{
							pSql.append("max(to_number(h.maxsampledtotalvalues)) as realspeed ");
						}
						pSql.append("from tab_http_special_speed_intf h ");
						pSql.append("where h.username='"+rs.getString("netuser")+"' ");
						pSql.append("and h.rstcode=0 and h.loid='"+ rs.getString("username")+ "' ");
						pSql.append("and h.device_id="+rs.getString("device_id")+" ");
						pSql.append("group by h.username) b ");
						pSql.append("where a.username = b.username and a.maxsampledtotalvalues = b.realspeed");
						List listtime = jt.queryForList(pSql.getSQL());
						if (!listtime.isEmpty()) {
							Map maptime = (Map) listtime.get(0);
							String complete_time = StringUtil.getStringValue(maptime.get("test_time")); 
							long opertime = StringUtil.getLongValue(complete_time) * 1000L;
							DateTimeUtil dt = new DateTimeUtil(opertime);
							map.put("test_time",dt.getLongDate() );
						}
						double result = new BigDecimal(realspeedtest).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
						map.put("realspeed",StringUtil.getStringValue(result));
					
						String city_id = StringUtil.getStringValue(rs.getString("city_id"));
						if (false == StringUtil.IsEmpty(city_id)){
							String city_name = StringUtil.getStringValue(cityMap.get(city_id));
							map.put("city_name", city_name);
						}else{
							map.put("city_name", "");
						}
						
						String device_id = StringUtil.getStringValue(rs.getString("device_id"));
						if(!StringUtil.IsEmpty(device_id)){
							PrepareSQL pSqlvendor = new PrepareSQL();
							pSqlvendor.append("select vendor_id,device_model_id,devicetype_id ");
							pSqlvendor.append("from tab_gw_device where device_id='"+device_id+"'");
							List list = jt.queryForList(pSqlvendor.getSQL());
							if (!list.isEmpty()) {
								Map rmap = (Map) list.get(0);
								
								String vendor_id = StringUtil.getStringValue(rmap.get("vendor_id"));  // 厂商ID
								String device_model_id = StringUtil.getStringValue(rmap.get("device_model_id"));  // 型号ID
								String devicetype_id = StringUtil.getStringValue(rmap.get("devicetype_id"));   // 版本ID
								
								if (!StringUtil.IsEmpty(vendor_id)) {
									map.put("vendor_add", venderMap.get(vendor_id));  // 设备厂商
								}else {
									map.put("vendor_add", "-");  // 设备厂商
								}
								
								if (!StringUtil.IsEmpty(device_model_id)) {
									map.put("device_model", deviceModelMap.get(device_model_id));  // 设备型号
								} else {
									map.put("device_model", "-");  // 设备型号
								}
								
								if(!StringUtil.IsEmpty(devicetype_id)){
									map.put("softversion", deviceSoftVersionMap.get(devicetype_id));  // 设备版本
									map.put("hardwareversion", devicehardwareversionMap.get(devicetype_id));  // 设备硬件版本
								} else {
									map.put("softversion", "-");   // 设备版本
									map.put("hardwareversion", "-");   // 设备硬件版本
								}
							}
						
						}
						return map;
					}
				});
				
				return list;
		}
		
}
