/**
 * @(#)CommonMap.java 2006-1-23
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 有关tab_gw_device,及tab_city表相关常用Map.
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class CommonMap {

	/**
	 * 根据资源类型，获取id与device_name对应列表.
	 * 
	 * @param _resource_type_id
	 *            资源ID.以","分开.eg. "0" "1,3,9,11" (String).
	 *            <UL>
	 *            <LI>0: 所有设备
	 *            <LI>1: 核心层-出口路由器
	 *            <LI>2: 核心层-普通路由器
	 *            <LI>3: 业务层-BRAS
	 *            <LI>4: 业务层-SR
	 *            <LI>5: 业务层-三层交换机
	 *            <LI>6: 汇聚层-三层交换
	 *            <LI>7: 汇聚层-二层交换机
	 *            <LI>8: 汇聚层-ATM交换机
	 *            <LI>9: 接入层-二层交换机
	 *            <LI>10: 接入层-IP DSLAM
	 *            <LI>11: 接入层-ATM DSLAM
	 *            <LI>12: 其它-窄带拨号服务器
	 *            <LI>13: 其它-防火墙
	 *            <LI>14: 其它-四层交换机
	 *            <LI>15: 其它-主机
	 *            </UL>
	 * @param gatherID
	 *            采集机编号.(String[])
	 * 
	 * @return Map
	 */
	public static Map getDeviceMap(String _resource_type_id, List gatherID) {

		Map deviceMap = new HashMap();
		deviceMap.clear();
		String strSQL = "select device_id,device_name"
				+ " from tab_gw_device where" + " resource_type_id in (?)"
				+ " and gather_id in (?)";
		PrepareSQL pSQL = new PrepareSQL(strSQL);

		if (_resource_type_id.length() == 0 && _resource_type_id.equals("0")) {
			pSQL.setStringExt(1, "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15", false);
		} else {
			pSQL.setStringExt(1, _resource_type_id, false);
		}

		pSQL.setStringExt(2, StringUtils.weave(gatherID), false);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("device_name"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 查找表（tab_city）,获得属地Map（city_id,city_name）.
	 * 
	 * @return 返回属地Map
	 */
	public static Map getCityMap() {

		Map cityMap = new HashMap();
		cityMap.clear();
		String sqlCity = "select * from tab_city  order by city_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlCity = "select city_id, city_name from tab_city  order by city_id";
		}
		PrepareSQL psql = new PrepareSQL(sqlCity);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlCity);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!cityMap.containsKey(fields.get("city_id"))) {
				cityMap.put(fields.get("city_id"), fields.get("city_name"));
				fields = cursor.getNext();
			}
		}

		return cityMap;
	}

	/**
	 * 获得局向ID与局向名字对应表.
	 * 
	 * @return Map.
	 */
	public static Map getOfficeMap() {

		Map officeMap = new HashMap();
		officeMap.clear();
		String sqlOffice = "select * from tab_office  order by office_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlOffice = "select office_id, office_name from tab_office  order by office_id";
		}
		PrepareSQL psql = new PrepareSQL(sqlOffice);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlOffice);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!officeMap.containsKey(fields.get("office_id"))) {
				officeMap.put(fields.get("office_id"), fields
						.get("office_name"));
				fields = cursor.getNext();
			}
		}
		officeMap.put("未知局向", "未知局向");

		return officeMap;
	}

	/**
	 * 获得小区ID与小区名字对应表
	 * 
	 * @return
	 */
	public static Map getZoneMap() {

		Map map = new HashMap();
		map.clear();
		String sqlZone = "select * from tab_zone order by zone_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlZone = "select zone_id, zone_name from tab_zone order by zone_id";
		}
		PrepareSQL psql = new PrepareSQL(sqlZone);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlZone);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!map.containsKey(fields.get("zone_id"))) {
				map.put(fields.get("zone_id"), fields.get("zone_name"));
				fields = cursor.getNext();
			}
		}
		map.put("未知小区", "未知小区");

		return map;
	}

	/**
	 * 获得资源类型ID与资源类型名字对应表
	 * 
	 * @return
	 */
	public static Map getResourceTypeMap() {
		Map map = new HashMap();
		map.clear();
//		String sqlZone = "select * from tab_resourcetype order by resource_type_id";
//		Cursor cursor = DataSetBean.getCursor(sqlZone);
//		Map fields = cursor.getNext();
//		while (fields != null) {
//			if (!map.containsKey(fields.get("resource_type_id"))) {
//				map.put(fields.get("resource_type_id"), fields
//						.get("resource_type_name"));
//				fields = cursor.getNext();
//			}
//		}
		map.put("未知资源类型", "未知资源类型");

		return map;
	}
	
	/**
	 * 获得设备层次类型ID与层次类型名字对应表
	 * liuli
	 * @return
	 */
	public static Map getnetworkLayerMap() {
		Map map = new HashMap();
		map.clear();
		String sqlZone = "select * from tab_network_layer order by network_layer_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlZone = "select network_layer_id, network_layer_name from tab_network_layer order by network_layer_id";
		}
		PrepareSQL psql = new PrepareSQL(sqlZone);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlZone);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!map.containsKey(fields.get("network_layer_id"))) {
				map.put(fields.get("network_layer_id"), fields
						.get("network_layer_name"));
				fields = cursor.getNext();
			}
		}
		map.put("未知资源类型", "未知资源类型");

		return map;
	}

	/**
	 * 获得厂商ID与厂商名字对应表
	 * 
	 * @return
	 */
	public static Map getVendorMap() {

		Map map = new HashMap();
		map.clear();
		String sqlZone = "select * from tab_vendor order by vendor_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlZone = "select vendor_id, vendor_name from tab_vendor order by vendor_id";
		}
		PrepareSQL psql = new PrepareSQL(sqlZone);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlZone);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!map.containsKey(fields.get("vendor_id"))) {
				map.put(fields.get("vendor_id"), fields.get("vendor_name"));
				fields = cursor.getNext();
			}
		}
		// map.put("未知厂商","未知厂商");

		return map;
	}

	/**
	 * 获取device_id与ip/device_name对应列表
	 * 
	 * @param 采集机编号(List)
	 * @return Map
	 */
	public static Map getDeviceIpdNameMap(List gather_id) {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip,device_name from tab_gw_device  where gather_id in (?)";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
		String s = "";
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				s = (String) fields.get("loopback_ip") + "/"
						+ (String) fields.get("device_name");
				deviceMap.put((String) fields.get("device_id"), s);
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 获取device_id与ip/device_name对应列表
	 * 
	 * @return Map
	 */
	public static Map getDeviceIpdNameMap() {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip,device_name from tab_gw_device";
		String s = "";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				s = (String) fields.get("loopback_ip") + "/"
						+ (String) fields.get("device_name");
				deviceMap.put((String) fields.get("device_id"), s);
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 获取device_id与device_name对应列表
	 * 
	 * @param 采集机编号(List)
	 * @return Map
	 */
	public static Map getDeviceNameMap(List gather_id) {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,device_name from tab_gw_device  where gather_id in (?)";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("device_name"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 获取device_id与device_name对应列表
	 * 
	 * @return Map
	 */
	public static Map getDeviceNameMap() {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,device_name from tab_gw_device";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("device_name"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 获取device_id与LOOPBACK_IP对应列表
	 * 
	 * @param 采集机编号(List)
	 * @return
	 */
	public static Map getDeviceIPMap(List gather_id) {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip from tab_gw_device where gather_id in (?)";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("loopback_ip"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 获取device_id与LOOPBACK_IP对应列表
	 * 
	 * @return Map
	 */
	public static Map getDeviceIPMap() {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip from tab_gw_device";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("loopback_ip"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}
	/**
	 * 获取snmp device_id与LOOPBACK_IP对应列表
	 * 
	 * @return Map
	 */
	public static Map getSnmpDeviceIPMap() {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip from tab_deviceresource";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("device_id"))) {
				deviceMap.put((String) fields.get("device_id"), (String) fields
						.get("loopback_ip"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;
	}

	/**
	 * 根据IP得ID FOR青海无知的要求.
	 * 
	 * @param gather_id
	 * @return
	 */
	public static Map getDeviceIDMap(List gather_id) {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip from tab_gw_device where gather_id in (?)";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("loopback_ip"))) {
				deviceMap.put((String) fields.get("loopback_ip"),
						(String) fields.get("device_id"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;

	}

	/**
	 * 根据IP得ID FOR青海无知的要求.
	 * 
	 * @return Map
	 */
	public static Map getDeviceIDMap() {
		Map deviceMap = new HashMap();
		deviceMap.clear();

		String strSQL = "select device_id,loopback_ip from tab_gw_device";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		while (fields != null) {
			if (!deviceMap.containsKey(fields.get("loopback_ip"))) {
				deviceMap.put((String) fields.get("loopback_ip"),
						(String) fields.get("device_id"));
				fields = cursor.getNext();
			}
		}

		return deviceMap;

	}

	/**
	 * 根据ID得IP
	 * 
	 * @return Map
	 */
	public static String getDeviceIPMapByID(String device_id) {

		String loopback_ip = "";

		String strSQL = "select device_id,loopback_ip from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		Map fields = cursor.getNext();
		if (fields != null) {
			loopback_ip = (String) fields.get("loopback_ip");
		}

		return loopback_ip;

	}

	/**
	 * 根据设备IP得设备ID
	 * 
	 * @param device_ip
	 * @return
	 */
	public static String getDeviceId(String device_ip) {
		String strSQL = "select device_id from tab_gw_device where loopback_ip ='"
				+ device_ip + "'";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		Map map = DataSetBean.getRecord(pSQL.getSQL());

		String device_id = "";
		if (map != null && map.get("device_id") != null)
			device_id = (String) map.get("device_id");
		return device_id;
	}

	/**
	 * 根据设备外部得设备内部ID
	 * 
	 * @param device_id_ex
	 * @param gather_id
	 * @return
	 */

	public static String getDeviceIdByDeviceIdEx(String device_id_ex,String gather_id) {
		String strSQL = "select device_id from tab_gw_device where device_id_ex ='"
				+ device_id_ex + "' and gather_id in("+gather_id+")";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		Map map = DataSetBean.getRecord(pSQL.getSQL());

		String device_id = "";
		if (map != null && map.get("device_id") != null)
			device_id = (String) map.get("device_id");
		return device_id;

	}
	/**
	 * 根据设备外部得设备内部ID
	 * 
	 * @param device_id_ex
	 * @return
	 */

	public static String getDeviceIdByDeviceIdEx(String device_id_ex) {
		String strSQL = "select device_id from tab_gw_device where device_id_ex ='"
				+ device_id_ex + "'";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		Map map = DataSetBean.getRecord(pSQL.getSQL());

		String device_id = "";
		if (map != null && map.get("device_id") != null)
			device_id = (String) map.get("device_id");
		return device_id;

	}

	/**
	 * 根据设备内部得设备外部ID
	 * 
	 * @param device_id
	 * @return
	 */
	public static String getDeviceIdExByDeviceId(String device_id) {
		String strSQL = "select device_id_ex from tab_gw_device where device_id ='"
				+ device_id + "'";
		PrepareSQL pSQL = new PrepareSQL(strSQL);
		Map map = DataSetBean.getRecord(pSQL.getSQL());

		String device_id_ex = "";
		if (map != null && map.get("device_id_ex") != null)
			device_id_ex = (String) map.get("device_id_ex");
		return device_id_ex;

	}

	// 获取每个地市设备ip地址的列表
	public static Map getCityIP() {
		Map cityIP = new HashMap();
		String mysql = "select * from tab_city_hostip";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			mysql = "select city_id, iptype, ip from tab_city_hostip";
		}
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map fields = cursor.getNext();
		while (fields != null) {

			String city_id = (String) fields.get("CITY_ID".toLowerCase());
			String iptype = (String) fields.get("IPTYPE".toLowerCase());
			String ip = (String) fields.get("IP".toLowerCase());
			cityIP.put(city_id + iptype, ip);
			fields = cursor.getNext();
		}
		return cityIP;
	}

	/**
	 * 由ID得IP
	 * 
	 * @param device_id
	 * @return
	 */
	public static String getDeviceIPById(String device_id) {
		String device_ip = "";
		String mysql = "select loopback_ip from tab_gw_device where device_id="
				+ "'" + device_id + "'";
		PrepareSQL pSQL = new PrepareSQL(mysql);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			device_ip = (String) fields.get("loopback_ip");
		}

		return device_ip;
	}

	/**
	 * 由ID得IP/NAME
	 * 
	 * @param device_id
	 * @return
	 */
	public static String getDeviceIPNameById(String device_id) {
		String device_ip = "";
		String mysql = "select loopback_ip,device_name from tab_gw_device where device_id="
				+ "'" + device_id + "'";
		PrepareSQL pSQL = new PrepareSQL(mysql);
		Map fields = DataSetBean.getRecord(pSQL.getSQL());
		if (fields != null) {
			device_ip = (String) fields.get("loopback_ip") + "/"
					+ (String) fields.get("device_name");
		}

		return device_ip;
	}

	/**
	 * 获得设备资源表中所有设备ID与采集机的映射关系
	 * 
	 * @return Map
	 */
	public static Map getGatherIdAndDeviceIdMap() {
		Map m_GatherIdAndDeviceIdMap = new HashMap();

		String mysql = "select device_id,gather_id from tab_gw_device";
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map fields = cursor.getNext();

		while (fields != null) {
			m_GatherIdAndDeviceIdMap.put(fields.get("device_id"), fields
					.get("gather_id"));

			fields = cursor.getNext();
		}
		//
		mysql = null;
		fields = null;
		cursor = null;

		return m_GatherIdAndDeviceIdMap;
	}
	
	/**
	 * 获得采集点表中所有采集点和名称对应关系
	 * 
	 * @return Map
	 */
	public static Map getGatherMap() {
		Map m_GatherIdMap = new HashMap();

		String mysql = "select * from tab_process_desc order by city_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			mysql = "select gather_id, descr from tab_process_desc order by city_id";
		}
		PrepareSQL psql = new PrepareSQL(mysql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(mysql);
		Map fields = cursor.getNext();

		while (fields != null) {
			m_GatherIdMap.put(fields.get("gather_id"), fields
					.get("descr"));

			fields = cursor.getNext();
		}
		//
		mysql = null;
		fields = null;
		cursor = null;

		return m_GatherIdMap ;
	}
	/**
	 * 获取设备id和设备序列号的对应关系
	 * 
	 * 
	 * return Map
	 * 
	 */
	public static Map getDeviceMap(){
		Map devcieMap = new HashMap();
		String tmpSql = "select device_id,device_serialnumber from tab_gw_device where 1=1 order by device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();

		while (fields != null) {
			devcieMap.put(fields.get("device_id"), fields
					.get("device_serialnumber"));

			fields = cursor.getNext();
		}
		//
		tmpSql = null;
		fields = null;
		cursor = null;
		return devcieMap ;		
		
		
	}
	
}
