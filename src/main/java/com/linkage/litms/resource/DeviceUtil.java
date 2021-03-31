package com.linkage.litms.resource;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class DeviceUtil {
	public DeviceUtil() {
	}

	/**
	 * 获取所有设备型号对应信息
	 * 
	 * @param
	 * @return HashMap key：devicetype_id value：device_model，softwareversion
	 */
	public Map getDeviceTypeMap() {
//		Map<String, String> deviceTypeMap = new HashMap<String, String>();
//		Cursor cursorTmp = DataSetBean
//				.getCursor("select * from tab_devicetype_info");
//		Map fieldTmp = cursorTmp.getNext();
//		while (fieldTmp != null) {
//			deviceTypeMap.put((String) fieldTmp.get("devicetype_id"), fieldTmp
//					.get("device_model")
//					+ "," + fieldTmp.get("softwareversion"));
//			fieldTmp = cursorTmp.getNext();
//		}
//		return deviceTypeMap;
		return new DeviceAct().getDeviceTypeMap();
	}

	/**
	 * 获取设备帐号之间的对应关系
	 * 
	 * @param gw_type
	 *            设备类型 1：家庭网关 2：企业网关 3：SNMP设备
	 * @return HashMap key：device_id value：对应的帐号信息map
	 */
	public Map<String, String> getUserDevMap(String gw_type) {
		String tab_name = "";
		// 家庭网关
		if (gw_type == null || gw_type.equals("") || gw_type.equals("1")) {
			tab_name = "tab_hgwcustomer";
			gw_type = "1";
		}
		// 企业网关
		else {
			if ("2".equals(gw_type)) {
				tab_name = "tab_egwcustomer";
				gw_type = "2";
			}
			// snmp设备
			else {
				tab_name = "tab_egwcustomer";
				gw_type = "3";
			}
		}
		// 查询设备帐号对应关系的sql
		String sql = "";
		if (!"3".equals(gw_type)) {
			sql = "select b.username,a.oui,a.device_serialnumber,a.device_id,a.city_id,a.gather_id,b.serv_type_id"
					+ " from tab_gw_device a, "
					+ tab_name
					+ "  b"
					+ " where a.cpe_allocatedstatus=1 and a.device_id = b.device_id";
		} else {
			sql = "select b.username,a.device_serialnumber,a.device_id,a.city_id,a.gather_id"
					+ " from tab_deviceresource a, cus_radiuscustomer b"
					+ " where a.device_id = b.device_id"
					+ " and a.device_status != -1 and b.user_state = '1'";
		}
		// 查询用户设备对应信息
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		Map fields = cursor.getNext();
		// 将设备和帐号对应关系放入map中
		Map userDevice = new HashMap();
		while (fields != null) {
			if ("3".equals(gw_type)) {
				userDevice.put(fields.get("device_id"), fields);
			} else {
				userDevice.put(fields.get("device_id") + "#"
						+ fields.get("serv_type_id"), fields);
			}
			fields = cursor.getNext();
		}
		return userDevice;
	}

	public String initGWSql(String device_serial, String loopback_ip,
			String city_id, String status, String vendor_id,
			String device_model_id, String devicetype_id, UserRes curUser,
			String gw_type) {
		String sql = "";
		// 非管理员用户根据域来查询
		if (curUser.getUser().isAdmin()) {
			//修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sql = "select a.*," + curUser.getAreaId() + " as area_id" 
			+ " from tab_gw_device a where a.device_status > 0 and a.gw_type="
			+ gw_type + " and a.cpe_allocatedstatus=1";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select a.device_id, a.city_id, a.vendor_id, a.devicetype_id," + curUser.getAreaId() + " as area_id"
						+ " from tab_gw_device a where a.device_status > 0 and a.gw_type="
						+ gw_type + " and a.cpe_allocatedstatus=1";
			}
			
//			sql = "select a.*,area_id=" + curUser.getAreaId()
//					+ " from tab_gw_device a where a.device_status > 0 and a.gw_type="
//					+ gw_type + " and a.cpe_allocatedstatus=1";
		} else {
			sql = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id "
					+ "where b.res_type=1 and a.device_status > 0 and b.res_id in (select res_id from tab_gw_res_area where area_id="
					+ curUser.getAreaId()
					+ " and res_type=1) and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select a.device_id, a.city_id, a.vendor_id, a.devicetype_id, b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id "
						+ "where b.res_type=1 and a.device_status > 0 and b.res_id in (select res_id from tab_gw_res_area where area_id="
						+ curUser.getAreaId()
						+ " and res_type=1) and a.gw_type="
						+ gw_type;
			}
		}
		// 设备序列号
		if (device_serial != null && !"".equals(device_serial)) {
			if(device_serial.length()>5){
				sql += " and a.dev_sub_sn ='" + device_serial.substring(device_serial.length()-6, device_serial.length()) + "'";
			}
			sql += " and a.device_serialnumber like '%" + device_serial + "'";
		}
		// 设备ip
		if (loopback_ip != null && !"".equals(loopback_ip)) {
			sql += " and a.loopback_ip = '" + loopback_ip + "'";
		}
		// 属地
		if (city_id != null && !"".equals(city_id) && !"-1".equals(city_id)) {
			sql += " and (a.city_id = '"
					+ city_id
					+ "' or a.city_id in (select city_id from tab_city where parent_id='"
					+ city_id + "'))";
		}
		// 状态
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			if ("2".equals(status)) {
				sql += "and a.cpe_currentstatus = null";
			} else {
				sql += "and a.cpe_currentstatus = " + status + "";
			}
		}
		// 厂商
//		if (vendor_id != null && !"".equals(vendor_id)
//				&& !"-1".equals(vendor_id)) {
//			sql += " and a.oui = '" + vendor_id + "'";
//		}
		if (vendor_id != null && !"".equals(vendor_id)
				&& !"-1".equals(vendor_id)) {
			sql += " and a.vendor_id = '" + vendor_id + "'";
		}
		// 设备类型
//		if (device_model != null && !"".equals(device_model)
//				&& !"-1".equals(device_model)) {
//			sql += " and a.devicetype_id in (select devicetype_id from tab_devicetype_info where device_model='"
//					+ device_model + "')";
//		}
		if (device_model_id != null && !"".equals(device_model_id)
				&& !"-1".equals(device_model_id)) {
			sql += " and a.device_model_id='" + device_model_id + "'";
		}
		// 设备软件版本
		if (devicetype_id != null && !"".equals(devicetype_id)
				&& !"-1".equals(devicetype_id)) {
			sql += " and a.devicetype_id = " + devicetype_id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return sql;
	}

	public String initSNMPSql(String device_serial, String loopback_ip,
			String city_id, String status, String vendor_id,
			String device_model, String devicetype_id, UserRes curUser) {
		String sql = "";
		// 非管理员用户根据域来查询
		if (curUser.getUser().isAdmin()) {
			//修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sql = "select a.*," + curUser.getAreaId() + " as area_id" 
			+ " from tab_deviceresource a where a.device_status = 1";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select a.device_id, a.city_id, a.vendor_id, a.devicetype_id," + curUser.getAreaId() + " as area_id"
						+ " from tab_deviceresource a where a.device_status = 1";
			}
//			sql = "select a.*,area_id=" + curUser.getAreaId()
//					+ " from tab_deviceresource a where a.device_status = 1";
		} else {
			sql = "select a.*,b.area_id from tab_deviceresource a left join tab_gw_res_area b on a.device_id=b.res_id "
					+ "where b.res_type=1 and a.device_status = 1 and b.device_id in (select res_id from tab_gw_res_area where area_id="
					+ curUser.getAreaId() + " and res_type=1)";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "select a.device_id, a.city_id, a.vendor_id, a.devicetype_id, b.area_id from tab_deviceresource a left join tab_gw_res_area b on a.device_id=b.res_id "
						+ "where b.res_type=1 and a.device_status = 1 and b.device_id in (select res_id from tab_gw_res_area where area_id="
						+ curUser.getAreaId() + " and res_type=1)";
			}
		}
		// 设备序列号
		if (device_serial != null && !"".equals(device_serial)) {
			sql += " and a.device_serialnumber like '%" + device_serial + "%'";
		}
		// 设备ip
		if (loopback_ip != null && !"".equals(loopback_ip)) {
			sql += " and a.loopback_ip = '" + loopback_ip + "'";
		}
		// 属地
		if (city_id != null && !"".equals(city_id) && !"-1".equals(city_id)) {
			sql += " and (a.city_id = '"
					+ city_id
					+ "' or a.city_id in (select city_id from tab_city where parent_id='"
					+ city_id + "'))";
		}
		// 状态
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			if ("2".equals(status)) {
				sql += "and a.cpe_currentstatus = null";
			} else {
				sql += "and a.cpe_currentstatus = " + status + "";
			}
		}
		// 厂商
		if (vendor_id != null && !"".equals(vendor_id)
				&& !"-1".equals(vendor_id)) {
			sql += " and a.vendor_id = '" + vendor_id + "'";
		}
		// 设备类型
		if (device_model != null && !"".equals(device_model)
				&& !"-1".equals(device_model)) {
			sql += " and a.device_model = '" + device_model + "'";
		}
		// 设备软件版本
		if (devicetype_id != null && !"".equals(devicetype_id)
				&& !"-1".equals(devicetype_id)) {
			sql += " and a.devicetype_id = " + devicetype_id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return sql;
	}

	/**
	 * get device_id by oui and device_sn
	 * 
	 * @param oui
	 * @param device_sn
	 * @return
	 */
	public static String GetDeviceIdBySN(String oui, String device_sn) {
		String sql = "select device_id from tab_gw_device where oui='" + oui
				+ "' and device_serialnumber='" + device_sn + "'";
		Map map = DataSetBean.getRecord(sql);
		if (map != null) {
			sql = (String) map.get("device_id");
		}
		if (sql == null) {
			sql = "";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return sql;
	}

	/**
	 * get device_id by oui and device_sn
	 * 
	 * @param oui
	 * @param device_sn
	 * @return
	 */
	public static boolean updateUserStateByCustomer(String customer_id,
			int customer_state) {
		if (customer_state > 1 && customer_state < 4) {
			String sql = "update tab_egwcustomer set user_state='"
					+ customer_state
					+ "' where user_state in ('1','2') and e_id='"
					+ customer_id + "'";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			if (DataSetBean.executeUpdate(sql) >= 0) {
				return true;
			} else {
				return false;
			}

		}

		return true;
	}
}
