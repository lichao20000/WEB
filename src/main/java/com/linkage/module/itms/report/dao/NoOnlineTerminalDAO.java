package com.linkage.module.itms.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class NoOnlineTerminalDAO extends SuperDAO 
{
	private Logger logger = LoggerFactory.getLogger(NoOnlineTerminalDAO.class);
	
	private Map<String, String> cityMap = null;
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String, String> devicetypeMap = null;
	
	
	public Map getNoOnlineTerminalInfo(String city_id, String starttime)
	{
		logger.debug("getNoOnlineTerminalExcel({},{})", new Object[] {city_id, starttime});
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_gw_device a,gw_devicestatus b ");
		sql.append("where a.device_id=b.device_id and a.device_status!=-1 and a.gw_type=1 ");
		
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and b.last_time<").append(starttime);
		}
		sql.append(" group by a.city_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if(false == list.isEmpty()){
			for(int i=0; i<list.size(); i++ ){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), StringUtil
						.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	public Map getNoOnlineTerminalByBdInfo(String city_id, String starttime)
	{
		logger.debug("getNoOnlineTerminalExcel({},{})", new Object[] {city_id, starttime});
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select a.city_id,count(*) as total ");
		}else{
			sql.append("select a.city_id,count(1) as total ");
		}
		sql.append("from tab_gw_device a,gw_devicestatus b ");
		sql.append("where a.device_id=b.device_id and a.device_status!=-1 and a.gw_type=1 ");
		sql.append("and a.device_status=1 and a.cpe_allocatedstatus=1  ");
		if (!StringUtil.IsEmpty(city_id) && !"00".equals(city_id)
				&& !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and b.last_time<").append(starttime);
		}
		sql.append(" group by a.city_id");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = new ArrayList<Map>();
		list = jt.queryForList(psql.getSQL());
		Map<String, String> map = new HashMap<String, String>();
		if(false == list.isEmpty()){
			for(int i=0; i<list.size(); i++ ){
				Map rmap = (Map)list.get(i);
				map.put(StringUtil.getStringValue(rmap.get("city_id")), 
						StringUtil.getStringValue(rmap.get("total")));
			}
		}
		return map;
	}
	
	
	public List<Map> getDeviceList(String city_id, String starttime, int curPage_splitPage, int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		psql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username ");
		psql.append("from tab_gw_device a ");
		psql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in('1','2')),");
		psql.append("gw_devicestatus c ");
		psql.append("where a.device_id=c.device_id and a.city_id in(?) and a.device_status!=-1 ");
		psql.append("and c.last_time<? and a.gw_type=1");
		
		List cityList = CityDAO.getAllNextCityIdsByCityPid(city_id);
		psql.setStringExt(1, StringUtils.weave(cityList), false);
		psql.setLong(2, StringUtil.getLongValue(starttime));
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id"));
				String city_id = rs.getString("city_id");
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap,city_id,"");
				map.put("city_name", city_name);
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap,vendor_id,"");
				map.put("vendor_add", vendor_add);
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap,device_model_id,"");
				map.put("device_model", device_model);
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap,devicetype_id,"");
				map.put("softwareversion", softwareversion);
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("username", rs.getString("username"));
				return map;
			}
		});
		cityList = null;
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
	
	public int getDevCount(String city_id, String starttime, int curPage_splitPage,
			int num_splitPage)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		psql.append("from tab_gw_device a,gw_devicestatus b ");
		psql.append("where a.device_id=b.device_id and a.city_id in (?) ");
		psql.append("and a.device_status!=-1 and b.last_time<? and a.gw_type=1");
		
		List cityList = CityDAO.getAllNextCityIdsByCityPid(city_id);
		psql.setStringExt(1, StringUtils.weave(cityList), false);
		psql.setLong(2, StringUtil.getLongValue(starttime));
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		cityList = null;
		return maxPage;
	}
	
	
	public List<Map> getDevExcel(String city_id, String starttime)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.device_id,a.city_id,a.oui,a.device_model_id,a.devicetype_id,");
		psql.append("a.vendor_id,a.device_serialnumber,a.loopback_ip,b.username ");
		psql.append("from tab_gw_device a ");
		psql.append("left join tab_hgwcustomer b on (a.device_id=b.device_id and b.user_state in ('1','2')),");
		psql.append("gw_devicestatus c ");
		psql.append("where a.device_id=c.device_id and a.city_id in (?) ");
		psql.append("and a.device_status!=-1 and c.last_time<? and a.gw_type=1");
		
		List cityList = CityDAO.getAllNextCityIdsByCityPid(city_id);
		psql.setStringExt(1, StringUtils.weave(cityList), false);
		psql.setLong(2, StringUtil.getLongValue(starttime));
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		devicetypeMap = VendorModelVersionDAO.getDevicetypeMap();
		List<Map> list = jt.query(psql.getSQL(), new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				map.put("device_id", rs.getString("device_id")); 
				String city_id = rs.getString("city_id");
				String city_name = StringUtil.getStringValue(cityMap,city_id,"");
				map.put("city_name", city_name);
				
				String vendor_id = rs.getString("vendor_id");
				String vendor_add = StringUtil.getStringValue(vendorMap,vendor_id,"");
				map.put("vendor_add", vendor_add);
				
				String device_model_id = rs.getString("device_model_id");
				String device_model = StringUtil.getStringValue(deviceModelMap,device_model_id,"");
				map.put("device_model", device_model);
				
				String devicetype_id = rs.getString("devicetype_id");
				String softwareversion = StringUtil.getStringValue(devicetypeMap,devicetype_id,"");
				map.put("softwareversion", softwareversion);
				
				map.put("device_serialnumber", rs.getString("device_serialnumber"));
				map.put("loopback_ip", rs.getString("loopback_ip"));
				map.put("username", rs.getString("username"));
				return map;
			}
		});
		cityList = null;
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		devicetypeMap = null;
		return list;
	}
	
	
}
