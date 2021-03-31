package com.linkage.module.itms.report.dao;

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
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
/**
 * 新开用户绑定终端情况 Dao
 * @author zhangyu
 *
 */
@SuppressWarnings("all")
public class UserOpenBindDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(UserOpenBindDAO.class);
	
	private Map<String, String> cityMap = null;
	private Map<String, String> userTypeMap = null;
	// 设备厂商
	private Map<String, String> venderMap = null;       
	 // 设备型号
	private Map<String, String> deviceModelMap = null; 
	
	/**
	 * 新开用户绑定终端情况 列表
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> userOpenBindList(String cityId, String starttime, String endtime, 
			int curPage_splitPage, int num_splitPage) 
	{
		logger.debug("getUserList({},{},{},{},{},{})",
				new Object[] { cityId, starttime, endtime, curPage_splitPage, num_splitPage });
		
		PrepareSQL psql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		
		psql.append("select a.username loid, a.city_id, a.opendate, a.binddate, e.vendor_id, "
				+ "e.device_model_id, e.last_time, f.username broadband_account ");
		psql.append(" from tab_hgwcustomer a ");
		psql.append(" left join ( ");
		psql.append(" 	select d.device_id, d.vendor_id, d.device_model_id, s.last_time ");
		psql.append(" 	from tab_gw_device d, gw_devicestatus s ");
		psql.append(" 	where d.device_id = s.device_id ");
		psql.append(" ) e on a.device_id = e.device_id ");
		psql.append(" left join hgwcust_serv_info f on a.user_id = f.user_id and f.serv_type_id = '10' ");
		psql.append(" where 1=1 ");
		
		if (false == StringUtil.IsEmpty(starttime)){
			psql.append(" and a.opendate >= "+starttime);
		}
		
		if (false == StringUtil.IsEmpty(endtime)){
			psql.append(" and a.opendate <= "+endtime);
		}
		
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and a.city_id in ("+StringUtils.weave(cityIdList)+")");
		}
		psql.append(" order by a.opendate desc ");
		
		cityMap = CityDAO.getCityIdCityNameMap();
		venderMap = getVenderMap();                        // 设备厂商
		deviceModelMap = getDeviceModelMap();              // 设备型号
		
		List<Map> list = null;
		if(curPage_splitPage == -1) {
			list = jt.queryForList(psql.getSQL());
		}else {
			list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1, num_splitPage);
		}
		
		return list;
	}
		
	/**
	 * 新开用户绑定终端情况 总数
	 * @param cityId
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public int userOpenBindCount(String cityId, String starttime, String endtime) 
	{
		logger.debug("userOpenBindCount({},{},{})", new Object[] { cityId, starttime, endtime });
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) from tab_hgwcustomer where 1=1");
		}else{
			psql.append("select count(1) from tab_hgwcustomer where 1=1");
		}
		
		if (false == StringUtil.IsEmpty(starttime)){
			psql.append(" and opendate >= "+starttime);
		}
		
		if (false == StringUtil.IsEmpty(endtime)){
			psql.append(" and opendate <= "+endtime);
		}
		
		if (false == StringUtil.IsEmpty(cityId) && !"00".equals(cityId)) {
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			psql.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
		}
		
		return jt.queryForInt(psql.getSQL());
	}
	
	/**
	 * 获取厂商Map<vendor_id, vendor_name>
	 * @return
	 */
	public Map<String, String> getVenderMap() 
	{
		logger.debug("getVenderMap()");
		PrepareSQL sql = new PrepareSQL("select vendor_id,vendor_name,vendor_add from tab_vendor");
		List<Map> list = jt.queryForList(sql.getSQL());
		
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
	 * 获取型号Map<device_model_id, device_model>
	 * @return
	 */
	public Map<String, String> getDeviceModelMap() 
	{
		logger.debug("getDeviceModelMap()");
		
		String devicemodel = "";
		String device_model_id = "";
		Map<String, String> deviceModelMap = new HashMap<String,String>();

		PrepareSQL sql = new PrepareSQL("select device_model_id, device_model from gw_device_model");
		List<Map> list = jt.queryForList(sql.getSQL());
    	
    	for (Map<String, String> map : list) {
    		devicemodel = StringUtil.getStringValue(map.get("device_model"));
    		device_model_id = StringUtil.getStringValue(map.get("device_model_id"));
    		deviceModelMap.put(device_model_id, devicemodel);
		}
    	return deviceModelMap;
	}
	
	
	public HashMap<String, String> getDeviceSoftVersionMap() 
	{
		logger.debug("getDeviceModelMap()");
		
		String softwareversion = "";
		String devicetype_id = "";
		HashMap<String, String> deviceTypeMap = new HashMap<String,String>();

		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.softwareversion,b.devicetype_id");
		psql.append("  from tab_devicetype_info b ");
		
    	List<Map> list = jt.queryForList(psql.getSQL());
    	
    	for (Map<String, String> map : list) {
    		softwareversion = StringUtil.getStringValue(map.get("softwareversion"));
			devicetype_id = StringUtil.getStringValue(map.get("devicetype_id"));
			deviceTypeMap.put(devicetype_id, softwareversion);
		}
    	return deviceTypeMap;
	}

	/**
	 * 根据area_id查询city_id
	 * @param area_id
	 * @return
	 */
	public List<Map> queryCityIdByAreaId(String area_id)
	{
		PrepareSQL sql = new PrepareSQL();
		sql.setSQL("select a.city_id from tab_city_area a ");
		sql.append("inner join tab_city b on a.city_id=b.city_id ");
		
		if (!StringUtil.IsEmpty(area_id) && !"1".equals(area_id)) {
			sql.append("where a.area_id="+area_id+" and b.parent_id='00' ");
		}else {
			sql.append("where a.area_id="+area_id+" ");
		}
		return jt.queryForList(sql.getSQL());
	}

	

	
}
