package com.linkage.litms.uss;

import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

public class GetCustomerInfo extends AllTrTds {

	public GetCustomerInfo() {
		UssLog.log("-------------------------GetCustomerInfo-------------------------------------");
	}
	
	/**
	 * 获取所有客户相关信息
	 * 
	 * @param customerID
	 * @return
	 */
	public String getCustomerRelatedInfo(String customerID) {
		String html = "";
		//获得头信息
		html += CommonMtd.getTitleTable("客户相关信息");
		// 获取客户信息
		html += getCustomerInfo(customerID);
		// 获取业务用户信息
		html += getBusinessInfo(customerID);
		// 获取设备信息
		html += getDeviceInfo(customerID);
		// 获得返回按钮
		html += getReturnBtn(customerID);
		
		html = html(html);
		return html;
	}

	/**
	 * 获取客户信息
	 * 
	 * @param customerID
	 * @return
	 */
	public String getCustomerInfo(String customerID) {
		String html = "";
		String customerSQL = "select * from tab_customerinfo where customer_id='" + customerID
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			customerSQL = "select customer_name, customer_pwd, customer_type, customer_size, customer_address, linkman, " +
					"linkphone, email, mobile, customer_state, update_time, opendate, pausedate, closedate, city_id, office_id, zone_id" +
					" from tab_customerinfo where customer_id='" + customerID
					+ "'";
		}
		UssLog.log("[GetCustomerInfo-getCustomerInfo]------customerSQL------:" + customerSQL);
		PrepareSQL psql = new PrepareSQL(customerSQL);
		psql.getSQL();
		Map<String, String> aRecMap = DataSetBean.getRecord(customerSQL);
		if (null == aRecMap) {
			html += tr(td_B("没有查询到该客户数据！"));
			return BuildHTML.getComplete("客户信息", 10, html);
		} else {
			String customer_name = aRecMap.get("customer_name");
			String customer_pwd = aRecMap.get("customer_pwd");
			String customer_type = aRecMap.get("customer_type");
			String customer_size = aRecMap.get("customer_size");
			String customer_address = aRecMap.get("customer_address");
			String linkman = aRecMap.get("linkman");
			String linkphone = aRecMap.get("linkphone");
			String email = aRecMap.get("email");
			String mobile = aRecMap.get("mobile");
			String customer_state = aRecMap.get("customer_state");
			String update_time = aRecMap.get("update_time");
			String opendate = aRecMap.get("opendate");
			String pausedate = aRecMap.get("pausedate");
			String closedate = aRecMap.get("closedate");
			String city_id = aRecMap.get("city_id");
			String office_id = aRecMap.get("office_id");
			String zone_id = aRecMap.get("zone_id");

			//html += tr(td_B("客户名称") + td_B(customer_name), td_B("客户密码") + td_B(customer_pwd));
			html += tr(td_B("客户名称") + td_B(customer_name), td_B("客户密码") + td_B("******"));
			html += tr(td_B("客户状态") + td_B(getCustomerStatus(customer_state)), td_B("客户地址")
					+ td_B(customer_address));
			html += tr(td_B("联系人") + td_B(linkman), td_B("联系电话") + td_B(linkphone));
			html += tr(td_B("电子邮件") + td_B(email), td_B("手机") + td_B(mobile));
			html += tr(td_B("更新时间") + td_B(getTimeFormat(update_time)), td_B("开户时间")
					+ td_B(getTimeFormat(opendate)));
			html += tr(td_B("暂停时间") + td_B(getTimeFormat(pausedate)), td_B("销户时间")
					+ td_B(getTimeFormat(closedate)));
			html += tr(td_B("属地") + td_B(getCityName(city_id)), td_B("局向") + td_B(getOfficeName(office_id)));
			//html += tr(td_B("小区") + td_B(getZoneName(zone_id), "", "3", ""));

		}
		return BuildHTML.getComplete("客户信息", 4, html);
	}

	/**
	 * 获取业务用户数据
	 * 
	 * @param customerID
	 * @return
	 */
	public String getBusinessInfo(String customerID) {
		String html = "";
		String htmlOne = "";
		String username = "";
		String passwd = "";
		String adsl_ser = "";
		String bandwidth = "";
		String ipaddress = "";
		String ipmask = "";
		String gateway = "";
		String user_state = "";
		String opendate = "";
		String onlinedate = "";
		String pausedate = "";
		String closedate = "";
		String phonenumber = "";
		String remark = "";
		String opmode = "";
		String serv_type_id = "";
		String open_status = "";
		String wan_type = "";
		String access_style_id = "";
		
		String busSQL = "select * from tab_egwcustomer where customer_id='" + customerID + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			busSQL = "select username, passwd, adsl_ser, bandwidth, ipaddress, ipmask, gateway, user_state, opendate," +
					"onlinedate, pausedate, closedate, phonenumber, remark, opmode, serv_type_id, open_status, wan_type, access_style_id " +
					"from tab_egwcustomer where customer_id='" + customerID + "'";
		}
		
		UssLog.log("[GetCustomerInfo-getBusinessInfo]------busSQL------:" + busSQL);
		PrepareSQL psql = new PrepareSQL(busSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(busSQL);
		Map<String, String> busInfoMap = cursor.getNext();
		if (null == busInfoMap) {
			html += tr(td_B("没有查询到该客户的业务用户数据！"));
			return BuildHTML.getComplete("业务用户信息", 10, html);
		}
		while (null != busInfoMap) {
			html ="";
			username = busInfoMap.get("username");
			passwd = busInfoMap.get("passwd");
			adsl_ser = busInfoMap.get("adsl_ser");
			bandwidth = busInfoMap.get("bandwidth");
			if ("0".equals(bandwidth) || null == bandwidth || "null".equals(bandwidth)) {
				bandwidth = "";
			}
			ipaddress = busInfoMap.get("ipaddress");
			ipmask = busInfoMap.get("ipmask");
			gateway = busInfoMap.get("gateway");
			user_state = busInfoMap.get("user_state");
			opendate = busInfoMap.get("opendate");
			onlinedate = busInfoMap.get("onlinedate");
			pausedate = busInfoMap.get("pausedate");
			closedate = busInfoMap.get("closedate");
			phonenumber = busInfoMap.get("phonenumber");
			remark = busInfoMap.get("remark");
			opmode = busInfoMap.get("opmode");
			serv_type_id = busInfoMap.get("serv_type_id");
			open_status = busInfoMap.get("open_status");
			wan_type = busInfoMap.get("wan_type");
			access_style_id = busInfoMap.get("access_style_id");

			if (!"".equals(html)) {
				html += tr(td("<hr>", "", "4", ""));
			}
			//html += tr(td_B("用户帐号") + td_B(username), td_B("用户密码") + td_B(passwd));
			html += tr(td_B("用户帐号") + td_B(username), td_B("用户密码") + td_B("******"));
			html += tr(td_B("DNS地址") + td_B(adsl_ser), td_B("IP地址") + td_B(ipaddress));
			html += tr(td_B("网关") + td_B(gateway), td_B("掩码") + td_B(ipmask));
			html += tr(td_B("用户状态") + td_B(getUserState(user_state)), td_B("开户时间")
					+ td_B(getTimeFormat(opendate)));
			html += tr(td_B("暂停时间") + td_B(getTimeFormat(pausedate)), td_B("销户时间")
					+ td_B(getTimeFormat(closedate)));
			html += tr(td_B("用户类型") + td_B(getServTypeId(serv_type_id)), td_B("开通状态")
					//+ td_B(getOpenStatus(open_status)));
					+ td_B("开通"));
			html += tr(td_B("上网类型") + td_B(getWanType(wan_type)), td_B("接入方式")
					+ td_B(getAccessStyleId(access_style_id)));
			html += tr(td_B("带宽") + td_B(bandwidth),td_B("备注") + td_B((remark == null ? "" : remark)));

			busInfoMap = cursor.getNext();
			
			htmlOne += BuildHTML.getComplete("业务用户（"+username+"）", 4, html);
		}

		return htmlOne;
	}

	/**
	 * 获取设备信息
	 * 
	 * @param customerID
	 * @return
	 */
	public String getDeviceInfo(String customerID) {
		String html = "";
		String htmlOne = "";
		String devSQL = "select a.*, b.username from tab_gw_device a, tab_egwcustomer b "
				+ "where a.device_id=b.device_id and b.customer_id='"
				+ customerID + "'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			devSQL = "select a.device_serialnumber, a.cpe_mac, a.loopback_ip, a.cpe_currentupdatetime, a.complete_time, " +
					"a.cpe_currentstatus, a.device_name, a.devicetype_id, a.oui, a.area_id, a.device_status, a.maxenvelopes, " +
					"a.city_id, a.device_type, b.username " +
					" from tab_gw_device a, tab_egwcustomer b "
					+ "where a.device_id=b.device_id and b.customer_id='"
					+ customerID + "'";
		}
		
		UssLog.log("[GetCustomerInfo-getDeviceInfo]------devSQL------:" + devSQL);
		PrepareSQL psql = new PrepareSQL(devSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(devSQL);
		Map<String, String> fields = cursor.getNext();
		
		String username = ""; //用户账号
		String device_serialnumber = "";// 设备序列号
		String device_name = "";// 设备名称
		String maxenvelopes = "";// 最大包数Envelopes
		String device_model = "";// 设备型号
		String devicetype_id = "";
		String cpe_mac = "";// 设备MAC地址
		String loopback_ip = "";
		String cpe_currentupdatetime = "";// 设备最近更新时间
		String software_version = ""; // 软件版本
		String handware_version = ""; // 硬件版本
		String spec_version = ""; // 硬件版本
		String cpe_currentstatus = "";// 设备当前注册状态
		String cpe_operationinfo = "";// 设备操作的历史信息
		// String vendor_id = "";// 厂商oui
		String oui = "";// 厂商oui
		String manufacturer = "";
		// String oui_name = "";
		String device_area_id = "";
		String device_status = "";
		String city_id = "";
		String complete_time = "";
		String device_type = "";

//		String gw_type = "";
//		String gw_type_name = "";
		
		String pppoe = "";
		Map area_Map = getAreaIdMapName();
		String device_area_name = (String) area_Map.get(device_area_id);
		if (null == device_area_name)
			device_area_name = "江苏";

		if (null == fields) {
			html += tr(td_B("没有查询到该客户的设备信息！"));
			return BuildHTML.getComplete("设备信息", 10, html);
		}

		while (null != fields) {
			html ="";
			username = fields.get("username");
			device_serialnumber = fields.get("device_serialnumber");
			cpe_mac = fields.get("cpe_mac");
			loopback_ip = fields.get("loopback_ip");
			cpe_currentupdatetime = fields.get("cpe_currentupdatetime");
			complete_time = fields.get("complete_time");
			cpe_currentstatus = fields.get("cpe_currentstatus");
			device_name = fields.get("device_name");
			devicetype_id = fields.get("devicetype_id");
			oui = fields.get("oui");
			device_area_id = fields.get("area_id");
			device_status = fields.get("device_status");
			maxenvelopes = fields.get("maxenvelopes");
//			gw_type = fields.get("gw_type");
//			if (gw_type.equals("0")) {
//				gw_type_name = "普通网络设备";
//			} else if (gw_type.equals("1")) {
//				gw_type_name = "家庭网关设备";
//			} else if (gw_type.equals("4")) {
//				gw_type_name = "安全网关设备";
//			} else if (gw_type.equals("5")) {
//				gw_type_name = "混合型网关";
//			} else {
//				gw_type_name = "企业网关设备";
//			}
			city_id = fields.get("city_id");
			device_type = fields.get("device_type");

			Map<String, String> map = getDeviceModelVersion(oui, devicetype_id);
			if (map != null) {
				software_version = map.get("softwareversion");
				handware_version =  map.get("hardwareversion");
				spec_version = map.get("specversion");
				manufacturer = map.get("manufacturer");
				device_model = map.get("device_model");
			}
			// 将cpe_currentupdatetime转换成时间
			if (cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")) {
				DateTimeUtil dateTimeUtil = new DateTimeUtil(
						Long.parseLong(cpe_currentupdatetime) * 1000);
				cpe_currentupdatetime = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}

			// 将complete_time转换成时间
			if (complete_time != null && !complete_time.equals("")) {
				DateTimeUtil dateTimeUtil = new DateTimeUtil(Long.parseLong(complete_time) * 1000);
				complete_time = dateTimeUtil.getLongDate();
				dateTimeUtil = null;
			}

			if (!"".equals(html)) {
				html += tr(td("<hr>", "", "4", ""));
			}
			html += tr(td_B("设备序列号") + td_B(device_serialnumber), td_B("设备名称") + td_B(device_name));
			html += tr(td_B("设备IP") + td_B(loopback_ip), td_B("厂商oui") + td_B(oui));
			html += tr(td_B("设备型号") + td_B(device_model), td_B("设备类型") + td_B(device_type));
			html += tr(td_B("软件版本") + td_B(software_version), td_B("") + td_B(""));

//			// html += tr(td_B("设备ID") + td_B(device_id));
//			html += tr(td_B("设备序列号") + td_B(device_serialnumber));
//			html += tr(td_B("设备名称") + td_B(device_name));
//			html += tr(td_B("设备IP") + td_B(loopback_ip));
//			html += tr(td_B("厂商oui") + td_B(oui));
//			// html += tr(td_B("设备MAC地址") + td_B(cpe_mac));
//			html += tr(td_B("设备型号") + td_B(device_model));
//			html += tr(td_B("设备类型") + td_B(device_type));
//			html += tr(td_B("软件版本") + td_B(software_version));

			fields = cursor.getNext();
			
			htmlOne += BuildHTML.getComplete("设备信息(用户账号“"+username+"”)", 4, html);
		}

		return htmlOne;
	}

	
	/**
	 * 获得返回按钮
	 * @param customerID
	 * @return
	 */
	private String getReturnBtn(String customerID) {
		String html = "";
		html += tr(td("<input class='bottom1' type='button' value=' 返 回 ' onclick='doReturn("+customerID+")'>","center", "10", "","tdd_A"));
		html = BuildHTML.getComplete("", 4, html);
		return html;
	}
	
	private String getTimeFormat(String time) {
		if (null == time || "".equals(time.trim()) || "0".equals(time.trim())) {
			return "";
		}
		return DateUtil.format(Long.parseLong(time) * 1000, "yyyy-MM-dd hh:mm:ss");
	}

	private String getAccessStyleId(String access_style_id) {
		return "1".equals(access_style_id) ? "ADSL" : ("2".equals(access_style_id) ? "普通LAN" : ("3"
				.equals(access_style_id) ? "普通光纤" : ("4".equals(access_style_id) ? "专线LAN" : ("5"
				.equals(access_style_id) ? "专线光纤" : "(未知)"))));
	}

	private String getWanType(String wan_type) {
		return "1".equals(wan_type) ? "桥接" : ("2".equals(wan_type) ? "路由"
				: ("3".equals(wan_type) ? "静态IP" : ("4".equals(wan_type) ? "DHCP" : "(未知)")));
	}

	private String getOpenStatus(String open_status) {
		return "0".equals(open_status) ? "未做" : ("1".equals(open_status) ? "成功" : ("-1"
				.equals(open_status) ? "失败" : "(未知)"));
	}

	private String getOpmode(String opmode) {
		return "0".equals(opmode) ? "未竣工" : ("1".equals(opmode) ? "竣工" : "(未知)");
	}

	private String getUserState(String user_state) {
		return "0".equals(user_state) ? "删除用户" : ("1".equals(user_state) ? "开户" : ("2"
				.equals(user_state) ? "暂停" : ("3".equals(user_state) ? "销户" : ("4"
				.equals(user_state) ? "更换设备" : "(未知)"))));
	}

	private String getCustomerType(String customer_type) {
		return "1".equals(customer_type) ? "企业单位" : ("2".equals(customer_type) ? "事业单位" : "(未知)");
	}

	private String getCustomerStatus(String customer_state) {
		return "1".equals(customer_state) ? "开通" : ("2".equals(customer_state) ? "暂停" : ("3"
				.equals(customer_state) ? "销户" : ("4".equals(customer_state) ? "更换设备" : "(未知)")));
	}

	private String getCustomerSize(String customer_size) {
		return "0".equals(customer_size) ? "小" : ("1".equals(customer_size) ? "中" : ("2"
				.equals(customer_size) ? "大" : "(未知)"));
	}

	private String getServTypeId(String serv_type_id) {
		if (null == serv_type_id) {
			return "";
		}
		String sql = "select serv_type_name from tab_gw_serv_type where serv_type_id="
				+ serv_type_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(sql);
		if (null != map) {
			return map.get("serv_type_name") == null ? "" : (String) map.get("serv_type_name");
		}
		return "";
	}

	private String getCityName(String city_id) {
		if ("-1".equals(city_id)) {
			return "";
		}
		String sql = "select city_name from tab_city where city_id='" + city_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(sql);
		if (null != map) {
			return map.get("city_name") == null ? "" : (String) map.get("city_name");
		}
		return "";
	}

	private String getZoneName(String zone_id) {
		if ("-1".equals(zone_id)) {
			return "";
		}
		String sql = "select zone_name from tab_zone where zone_id='" + zone_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(sql);
		if (null != map) {
			return map.get("zone_name") == null ? "" : (String) map.get("zone_name");
		}
		return "";
	}

	private String getOfficeName(String office_id) {
		if ("-1".equals(office_id)) {
			return "";
		}
		String sql = "select office_name from tab_office where office_id='" + office_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(sql);
		if (null != map) {
			return map.get("office_name") == null ? "" : (String) map.get("office_name");
		}
		return "";
	}

	private Map<String, String> getDeviceModelVersion(String oui, String devicetype_id) {
		String sql = "select devicetype_id,manufacturer,specversion,hardwareversion,softwareversion,device_model " +
				" from tab_devicetype_info a,gw_device_model b where a.device_model_id=b.device_model_id " +
				" and devicetype_id=" + devicetype_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	public Map<String, String> getAreaIdMapName() {
		String sql = "select area_id,area_name from tab_area";
		Map<String, String> result = new HashMap<String, String>();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map<String, String> map = cursor.getNext();

		while (null != map) {

			result.put(map.get("area_id"), map.get("area_name"));
			map = cursor.getNext();
		}
		return result;
	}

	/**
	 * 获取设备当前开通的业务代码
	 * 
	 * @param oui
	 * @param device_model
	 * @return
	 */
	public String getSupportServiceCodeByCustomer(String device_id) {
		String sql = "select b.service_name from tab_gw_user_serv a, tab_service b where a.serv_type_id = b.serv_type_id and a.device_id='"
				+ device_id + "' and b.flag > 0";

		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		String s = "";
		// try {
		// while (rs.next()) {
		// s += busInfoMap.get("service_name") + "\n";
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		return s;
	}

	
}
