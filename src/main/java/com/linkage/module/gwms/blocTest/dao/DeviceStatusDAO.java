package com.linkage.module.gwms.blocTest.dao;

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
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class DeviceStatusDAO extends SuperDAO {

	private static Logger logger = LoggerFactory.getLogger(DeviceStatusDAO.class);
	
	/**
	 * Map<city_id,city_name>
	 */
	private Map<String, String> cityMap = null;
	/**
	 * HashMap<vendor_id,vendor_add>
	 */
	private HashMap<String, String> vendorMap = null;
	/**
	 * HashMap<device_model_id,device_model>
	 */
	private HashMap<String, String> deviceModelMap = null;
	
	
	
	/**
	 * 
	 * @param gw_type  1 表示家庭网关  2 表示企业网关
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param curUser
	 * @param vendor 设备厂商
	 * @param device_model  设备型号
	 * @param specversion  设备版本
	 * @param device_serialnumber  设备序列号
	 * @param city_id  设备属地
	 * @param loopback_ip  设备IP
	 * @return
	 */


	public List<Map> getDeviceList(String gw_type, String vendor, String device_model,  String specversion, 
			                       String device_serialnumber, String city_id, String loopback_ip){
		logger.debug("getDeviceList({},{},{},{},{},{},{},{},{},{},{})");

		StringBuffer sql = new StringBuffer();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		}*/
	    sql.append(" select a.device_id, a.oui, a.device_serialnumber, a.loopback_ip, a.cpe_currentstatus, a.gw_type, ")
	       .append(" b.vendor_id, c.device_model_id, d.city_id, e.devicetype_id, f.online_status from tab_gw_device a  ")
	       .append("   left join tab_vendor b on a.vendor_id = b.vendor_id ")
	       .append("   left join gw_device_model c on a. device_model_id = c.device_model_id ")
	       .append("   left join tab_city d on a.city_id = d.city_id ")
	       .append("   left join tab_devicetype_info e on a.devicetype_id = e.devicetype_id ")
	       .append("   left join gw_devicestatus f on a.device_id = f.device_id ")
	       .append("  where 1 = 1");
		
		// 1 表示 查询家庭网关设备  2 表示查询企业网关设备
		if ("1".equals(gw_type) || "2".equals(gw_type)){
			sql.append("    and a.gw_type = " + gw_type);
		}
		if ((!"".equals(vendor)) && (vendor != null) && (!"-1".equals(vendor))) {
			sql.append("    and b.vendor_id = '" + vendor + "' "); // 设备厂商
		}
		if ((!"".equals(device_model)) && (device_model != null) && (!"-1".equals(device_model))) { // 设备型号
			sql.append("    and c.device_model_id = '" + device_model + "'");
		}
		if ((!"".equals(specversion)) && (specversion != null) && (!"-1".equals(specversion)) && (!"-2".equals(specversion))) { // 设备特别版本
			sql.append("    and e.devicetype_id = " + specversion + " ");
		}
		if ((!"".equals(device_serialnumber)) && (device_serialnumber != null)) { // 设备序列号
			sql.append("    and a.device_serialnumber like '%" + device_serialnumber + "' ");
		}
		if ((!"".equals(city_id)) && (city_id != null) && (!"-1".equals(city_id))) { // 设备属地ID
			sql.append("    and d.city_id = '" + city_id + "' ");
		}
		if ((!"".equals(loopback_ip)) && (loopback_ip != null)) { // 设备IP
			sql.append("    and a.loopback_ip = '" + loopback_ip + "' ");
		}
		
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
    	
		cityMap = CityDAO.getCityIdCityNameMap();
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
		List<Map> list = jt.query(sql.toString(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("device_id", rs.getString("device_id"));   // 设备ID
				map.put("oui", rs.getString("oui"));  // 设备OUI
				map.put("device_serialnumber", rs.getString("device_serialnumber"));  // 设备序列号
				map.put("loopback_ip", rs.getString("loopback_ip"));  // 设备IP
				map.put("devicetype_id", rs.getString("devicetype_id")); // 设备类型ID
				
				String city_id = rs.getString("city_id");  // 设备属地ID 
				map.put("city_id", city_id);
				String city_name = StringUtil.getStringValue(cityMap.get(city_id));
				if (false == StringUtil.IsEmpty(city_name)) {
					map.put("city_name", city_name);
				} else {
					map.put("city_name", "");
				}
				
				String status = rs.getString("online_status");
				map.put("online_status", status);   // 设备在线状态
				if ("1".equals(status)) {
					map.put("onlineStatusValue", "设备在线");
				} else {
					map.put("onlineStatusValue", "设备下线");
				}
				
				String vendor_id = rs.getString("vendor_id");  // 设备厂商
				map.put("vendor_id", vendor_id);
//				String vendor_add = StringUtil.getStringValue(vendorMap.get(vendor_id));
//				if (false == StringUtil.IsEmpty(vendor_add)) {
//					map.put("vendor_add", vendor_add);
//				} else {
//					map.put("vendor_add", "");
//				}
				
				String device_model_id = rs.getString("device_model_id");
				map.put("device_model_id", device_model_id);
				
				map.put("gw_type", rs.getString("gw_type"));
			
				return map;
			}
				});
		cityMap = null;
		vendorMap = null;
		deviceModelMap = null;
		return list;
	}
}
