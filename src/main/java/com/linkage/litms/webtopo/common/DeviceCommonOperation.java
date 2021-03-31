package com.linkage.litms.webtopo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.other.AdslAct;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.DbUserRes;

public class DeviceCommonOperation {
	private static Logger m_logger = LoggerFactory.getLogger(DeviceCommonOperation.class);
	HttpSession session = null;

	private static HashMap TypeMap = null;

	private static HashMap O_TypeMap = null;

	private PrepareSQL pSQL = null;

//	private String mysql = "select c.device_model_id  serial from tab_gw_device a,tab_devicetype_info b,gw_device_model c where a.devicetype_id=b.devicetype_id "
//		+" and b.device_model=c.device_model and a.oui=c.oui and a.device_id=?";
	private String mysql = "select c.device_model_id  serial from tab_gw_device a,gw_device_model c where "
		+" and a.device_model_id=c.device_model_id and a.device_id=?";

	public DeviceCommonOperation(HttpSession session) {
		this.session = session;
	}

	public DeviceCommonOperation() {
		if (pSQL == null) {
			pSQL = new PrepareSQL();
		}
	}

	public String getSerialByDeviceid(String device_id) {
		String serial = null;
		m_logger.debug("sql:" + mysql);
		pSQL.setSQL(mysql);
		pSQL.setString(1, device_id);
		m_logger.debug("getSerialByDeviceid:" + pSQL.getSQL());
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		if (map != null) {
			serial = (String) map.get("serial");
		}

		return serial;
	}

	public Object[] getUserDevModal() {
		Object[] ret = new Object[2];
		ArrayList list = new ArrayList();
		HashMap devMap = new HashMap();

		DbUserRes dbuser = (DbUserRes) session.getAttribute("curUser");

		List devList = dbuser.getUserDevRes(dbuser.getUser());
		// 如果小于5000的话通过表的内联解决，否则通过遍历设备资源表进行解决
		String device_id = null;
		String device_model = null;
		String os_version = null;
		String gather_id = null;
		String serial = null;

		String dev_gather = null;
		// 获取设备型号和序列号的对应关系
		getTypeNameMap();
		if (devList.size() < 5000) {
			String mysql = "select device_id,device_model,os_version,gather_id from tab_gw_device"
					+ " where device_id in (select res_id from tab_gw_res_area where res_type=1 "
					+ "and area_id=?)";
			pSQL = new PrepareSQL(mysql);
			pSQL.setSQL(mysql);
			pSQL.setLong(1, dbuser.getAreaId());
			Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
			Map map = cursor.getNext();
			ArrayList list1 = null;
			while (map != null) {
				device_id = (String) map.get("device_id");
				device_model = (String) map.get("device_model");
				os_version = (String) map.get("os_version");
				gather_id = (String) map.get("gather_id");
				dev_gather = device_id + "/" + gather_id;
				serial = (String) O_TypeMap
						.get(device_model + "/" + os_version);
				serial = serial == null ? "-1" : serial;
				if (list.indexOf(serial) == -1) {
					list.add(serial);
					list1 = new ArrayList();
					list1.add(dev_gather);
					devMap.put(serial, list1);
				} else {
					list1 = (ArrayList) devMap.get(serial);
					list1.add(dev_gather);
				}
				map = cursor.getNext();
			}
		} else {
			String mysql = "select device_id,device_model,os_version,gather_id from tab_gw_device";
			PrepareSQL psql = new PrepareSQL(mysql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(mysql);
			Map map = cursor.getNext();
			ArrayList list1 = null;
			while (map != null) {
				device_id = (String) map.get("device_id");
				device_model = (String) map.get("device_model");
				os_version = (String) map.get("os_version");
				gather_id = (String) map.get("gather_id");
				dev_gather = device_id + "/" + gather_id;
				serial = (String) O_TypeMap
						.get(device_model + "/" + os_version);
				serial = serial == null ? "-1" : serial;
				if (devList.indexOf(device_id) >= 0) {
					if (list.indexOf(serial) == -1) {
						list.add(serial);
						list1 = new ArrayList();
						list1.add(dev_gather);
						devMap.put(serial, list1);
					} else {
						list1 = (ArrayList) devMap.get(serial);
						list1.add(dev_gather);
					}
				}
				map = cursor.getNext();
			}

		}
		ret[0] = list;
		ret[1] = devMap;

		return ret;
	}

	public HashMap getTypeNameMap() {
		if (TypeMap == null) {
			TypeMap = new HashMap();
			O_TypeMap = new HashMap();
			String mysql = "select vendor_id,device_name,serial,sys_id,os_version "
					+ " from tab_devicetype_info ";
			PrepareSQL psql = new PrepareSQL(mysql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(mysql);
			Map map = cursor.getNext();
			String serial = null;
			String device_name = null;
			String os_version = null;
			TypeMap.put("-1", "未知");
			while (map != null) {
				serial = (String) map.get("serial");
				device_name = (String) map.get("device_name");
				os_version = (String) map.get("os_version");
				O_TypeMap.put(device_name + "/" + os_version, serial);
				TypeMap.put(serial, device_name);
				map = cursor.getNext();
			}
		}

		return TypeMap;

	}

	/**
	 * 获取选择框
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @return String
	 */
	public String getGatherInfoForm(boolean flag, String compare, String rename) {
		AdslAct aa = new AdslAct();
		Cursor tmp_cursor = aa.getGatherInfo();

		UserRes curUser = (UserRes) session.getAttribute("curUser");
		List list = curUser.getUserProcesses();
		// logger.debug(list.get(0));
		Cursor cursor = new Cursor();
		Map map = tmp_cursor.getNext();
		while (map != null) {
			if (list.contains((String) map.get("gather_id"))) {
				cursor.add(map);
			}
			map = tmp_cursor.getNext();
		}

		tmp_cursor = null;

		String strResourceList = FormUtil.createListBox(cursor, "gather_id",
				"descr", flag, compare, rename);
		return strResourceList;
	}

	/**
	 * 根据设备型号从表tab_devicetype_info里得到设备厂商
	 */
	public String getVendor_idBySerial(String device_id) {
		String vendor_id = null;
		String sql = "select oui from tab_gw_device where device_id= '"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map cur = DataSetBean.getRecord(sql);
		
		if (cur != null) {
			vendor_id = (String) cur.get("oui");
		}
		
		//clear
		cur = null;
		
		return vendor_id;
	}

}