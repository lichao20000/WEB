package com.linkage.module.cuc.diagnostic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.Fault;
import com.linkage.litms.acs.soap.object.ParameterInfoStruct;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.FactoryReset;
import com.linkage.litms.acs.soap.service.GetParameterNames;
import com.linkage.litms.acs.soap.service.GetParameterNamesResponse;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.RPCObject;
import com.linkage.litms.acs.soap.service.Reboot;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGwCheckInterface;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.CommonMap;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.paramConfig.ParamTreeObject;
import com.linkage.litms.paramConfig.WANConnDeviceAct;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author liuli
 * @version 1.00, 4/18/2007
 * @since jtwg_1.1.0 Modify Record: 2007-06-18 Alex.Yan (yanhj@lianchuang.com)
 *        RemoteDB ACS.
 */
public class CucFileSevice {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(CucFileSevice.class);
	private String m_VendorType_SQL = "select distinct  a.devicetype_id, a.device_model,b.devicetype_id  from tab_devicetype_info a,tab_template b where a.devicetype_id=b.devicetype_id ";
	private String m_TemplateType_SQL = "select distinct  template_id,template_name from tab_template";
	private String m_DeviceType_SQL = "select template_id, template_name from tab_template";
	private Cursor cursor = null;

	/**
	 * ?????????????????????????????? add by liuli
	 *
	 * @param request
	 * @return strMsg ??????????????????????????????????????????
	 */
	public String getVendorTypeList(boolean flag, String compare, String rename) {
		PrepareSQL psql = new PrepareSQL(m_VendorType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_VendorType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor,
				"devicetype_id", "device_model", flag, compare, rename);
		return strVendorTypeList;
	}

	public String getDeviceModelByVendorID() {
		// String strSQL =
		// "select devicetype_id,device_model from tab_devicetype_info ";
		String strSQL = "select a.device_model,b.devicetype_id"
				+ " from gw_device_model a, tab_devicetype_info b where a.device_model_id=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strVendorTypeList = FormUtil.createListBox(cursor,
				"devicetype_id", "device_model", true, "", "", true);
		return strVendorTypeList;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????????????? ??????????????? add by liuli
	 *
	 * @param request
	 * @return String ????????????????????????????????????????????????
	 */
	public String getDeviceList(HttpServletRequest request) {
		String devicetype_id = request.getParameter("devicetype_id");
		String gather_id = request.getParameter("gather_id");
		String tmpSql = "select device_id, device_serialnumber, oui from tab_gw_device ";
		tmpSql += " where devicetype_id =" + devicetype_id
				+ " and gather_id ='" + gather_id + "' and device_status=1";
		tmpSql += " order by device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null) {
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>????????????????????????</span></td></tr>";
		} else {
			int iflag = 1;
			while (fields != null) {
				if (iflag % 2 == 0) {
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\">";
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("device_id")
							+ "/" + (String) fields.get("device_serialnumber");
					serviceHtml += "</td></tr>";
				} else {
					serviceHtml += "<tr>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\">";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber");
					serviceHtml += "</td></tr>";
				}
				iflag++;
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		logger.debug("serviceHtml==>" + serviceHtml);
		return serviceHtml;
	}

	public String getAllBoundPasswd(String oui, String device_serialnumber,
			String table_name, String device_type) {
		String passwdStr = "";
		String sql = "";
		// ?????????tr069??????
		if (device_type.equals("tr069")) {
			sql = "select passwd from " + table_name + " where oui='" + oui
					+ "' and device_serialnumber='" + device_serialnumber
					+ "' and user_state= '1' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null) {
				while (fields != null) {
					if (i == 0) {
						passwdStr = (String) fields.get("passwd");
					} else {
						passwdStr += "," + (String) fields.get("passwd");
					}
					i++;
					fields = cursor.getNext();
				}
			}
		}
		if (null == passwdStr) {
			passwdStr = "";
		}
		return passwdStr;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 *
	 * @param oui
	 * @param device_serialnumber
	 * @param table_name
	 * @param device_type
	 * @return
	 */
	public String getAllBoundUser(String oui, String device_serialnumber,
			String table_name, String device_type) {
		String userStr = "";
		String sql = "";
		// ?????????tr069??????
		if (device_type.equals("tr069")) {
			sql = "select username from " + table_name + " where oui='" + oui
					+ "' and device_serialnumber='" + device_serialnumber
					+ "' and user_state= '1' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null) {
				while (fields != null) {
					if (i == 0) {
						userStr = (String) fields.get("username");
					} else {
						userStr += "," + (String) fields.get("username");
					}
					i++;
					fields = cursor.getNext();
				}
			}
		} else {
			// ?????????SNMP?????? oui????????????device_id
			sql = "select username from " + table_name + " where device_id='"
					+ oui + "'";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null) {
				while (fields != null) {
					if (i == 0) {
						userStr = (String) fields.get("username");
					} else {
						userStr += "," + (String) fields.get("username");
					}
					i++;
					fields = cursor.getNext();
				}
			}
		}
		return userStr;
	}

	/**
	 * *************************************************************************
	 * ************************* TR069 ??? SNMP ?????? ???????????? ???????????????????????? 2007???12???17
	 * lizj???5202???
	 * ***************************************************************
	 * ************************************
	 *
	 * @param request
	 *            ????????????????????????????????????????????????????????????????????????????????????
	 * @param device_type
	 *            ??????????????? 0????????????????????????1????????????????????????2????????????????????????3???Snmp????????????
	 * @return
	 */
	public String getDeviceHtml(HttpServletRequest request, int device_type,
			String type, boolean needFilter) {
		// ???????????????????????????
		String serviceHtml = "";
		// ?????? ??????????????????
		if (device_type == 1) {
			serviceHtml += getTr069Device(request, type, needFilter,
					"tab_hgwcustomer", Global.GW_TYPE_ITMS);
		}
		// ?????? ??????????????????
		if (device_type == 2) {
			serviceHtml += getTr069Device(request, type, needFilter,
					"tab_egwcustomer", Global.GW_TYPE_BBMS);
		}
		// ?????? SNMP????????????
		if (device_type == 3) {
			serviceHtml += getSnmpDevice(request, type, needFilter);
		}
		if (device_type == 0) {
			serviceHtml += getTr069Device(request, type, needFilter,
					"tab_hgwcustomer", Global.GW_TYPE_ITMS);
			serviceHtml += getTr069Device(request, type, needFilter,
					"tab_egwcustomer", Global.GW_TYPE_BBMS);
			serviceHtml += getSnmpDevice(request, type, needFilter);
		}
		if (serviceHtml == null || serviceHtml.equals("")) {
			serviceHtml = "???????????????????????????";
		}
		return serviceHtml;
	}

	/**
	 * ??????SNMP???????????????
	 *
	 * @param request
	 * @param type
	 *            ????????????radio ??????checkbox
	 * @param needFilter
	 *            ?????????radio??????checkbox?????????????????????
	 * @return
	 */
	public String getSnmpDevice(HttpServletRequest request, String type,
			boolean needFilter) {
		// type ??????????????????????????? radio or checkbox
		// ??????
		String city_id = request.getParameter("city_id");
		// ??????
		// String vendor_id = request.getParameter("vendor_id");
		// ??????????????? device_model
		// String devicetype_id = request.getParameter("devicetype_id");
		// ???????????? ?????? devicetype_id
		String softwareversion = request.getParameter("softwareversion");
		// ????????????
		String hguser = request.getParameter("hguser");
		// ????????????
		String telephone = request.getParameter("telephone");
		// ???????????????
		String device_serialnumber = request.getParameter("serialnumber");
		// ????????????
		String loopback_ip = request.getParameter("loopback_ip");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		// DeviceAct dac = new DeviceAct();
		// Map deviceType = dac.getDeviceTypeMap();
		// ??????devicetype_id ?????????strSoftwareversion, ?????????SNMP??????????????????????????????
		// strSoftwareversion
		// ????????????
		// String[] tmp = (String[]) deviceType.get(softwareversion);
		// String strSoftwareversion = "";
		// if (tmp != null)
		// {
		// strSoftwareversion = tmp[1];
		// }
		// TODO wait (more table related)
		String tmpSql = "select a.device_id, a.oui, a.device_serialnumber, a.gather_id, a.loopback_ip "
				+ " from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		if (!user.isAdmin()) {
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone))) {
			tmpSql += " inner join tab_egwcustomer d on a.device_id =d.device_id ";
		}
		if (!user.isAdmin()) {
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		// ?????????????????????
		if (null != hguser && !"".equals(hguser)) {
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone)) {
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if (device_serialnumber.length() > 5) {
				tmpSql += " and a.dev_sub_sn = '"
						+ device_serialnumber.substring(
								device_serialnumber.length() - 6,
								device_serialnumber.length()) + "' ";
			}
			tmpSql += " and a.device_serialnumber like '%"
					+ device_serialnumber + "' ";
		}
		if (loopback_ip != null && !loopback_ip.equals("")) {
			tmpSql += " and a.loopback_ip like '%" + loopback_ip + "%' ";
		}
		if (city_id != null && !city_id.equals("") && !city_id.equals("-1")) {
			// ????????????????????????
			SelectCityFilter scf = new SelectCityFilter();
			String citys = scf.getAllSubCityIds(city_id, true);
			tmpSql += " and a.city_id in (" + citys + ") ";
		}
		// // devicetype_id ?????? device_model
		// if (devicetype_id != null && !devicetype_id.equals("-1")) {
		//
		// tmpSql += " and a.device_model ='" + devicetype_id + "' ";
		// }
		// //??????
		// if (null != vendor_id && !"".equals(vendor_id)) {
		// tmpSql += " and a.oui='" + vendor_id + "'";
		// }
		// ????????????
		if (null != softwareversion && !"".equals(softwareversion)) {
			tmpSql += " and a.devicetype_id=" + softwareversion;
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		String serviceHtml = "";
		Cursor _cursor = DataSetBean.getCursor(tmpSql);
		Map _fields = _cursor.getNext();
		if (_fields != null) {
			while (_fields != null) {
				String username = getAllBoundUser((String) _fields.get("oui"),
						(String) _fields.get("device_serialnumber"),
						"tab_egwcustomer", "tr069");
				serviceHtml += "<input type=\"" + type + "\" dev_serial=\""
						+ _fields.get("device_serialnumber")
						+ "\" id=\"device_id\" name=\"device_id\" gather_id=\""
						+ (String) _fields.get("gather_id") + "\" username=\""
						+ username + "\"  devicetype=\"snmp\" value=\""
						+ (String) _fields.get("device_id") + "\"";
				if (needFilter) {
					serviceHtml += " onclick=\"filterByDevIDAndDevTypeID(this)\">";
				} else {
					serviceHtml += "\">";
				}
				serviceHtml += "&nbsp;"
						+ (String) _fields.get("device_serialnumber") + "|"
						+ (String) _fields.get("loopback_ip") + " || "
						+ username + "<br>";
				_fields = _cursor.getNext();
			}
		}
		_cursor = null;
		_fields = null;
		return serviceHtml;
	}

	/**
	 * TR069???????????????????????????
	 *
	 * @param request
	 * @param type
	 * @param needFilter
	 * @param tab_name
	 * @return
	 */
	public String getTr069Device(HttpServletRequest request, String type,
			boolean needFilter, String tab_name, String gw_type) {
		// type ??????????????????????????? radio or checkbox
		// ??????
		String city_id = request.getParameter("city_id");
		// ??????
		String vendor_id = request.getParameter("vendor_id");
		// ??????????????? device_model
		// String devicetype_id = request.getParameter("devicetype_id");
		// ???????????? ?????? devicetype_id
		String softwareversion = request.getParameter("softwareversion");
		// ????????????
		String hguser = request.getParameter("hguser");
		// ????????????
		String telephone = request.getParameter("telephone");
		// ???????????????
		String device_serialnumber = request.getParameter("serialnumber");
		// ????????????
		String loopback_ip = request.getParameter("loopback_ip");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		// TODO wait (more table related)
		String tmpSql = "select a.oui, a.device_serialnumber, a.gather_id, a.device_id, a.loopback_ip  "
				+ "from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id";
		// ???Admin ??????
		if (!user.isAdmin()) {
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone))) {
			tmpSql += " inner join " + tab_name
					+ " d on a.device_id = d.device_id and d.user_state='1' ";
		}
		tmpSql += " where a.device_status=1";
		tmpSql += " and  a.gw_type=" + gw_type;
		if (!user.isAdmin()) {
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		// ?????????????????????
		if (null != hguser && !"".equals(hguser)) {
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone)) {
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (city_id != null && !city_id.equals("") && !city_id.equals("-1")) {
			// ????????????????????????
			SelectCityFilter scf = new SelectCityFilter();
			String citys = scf.getAllSubCityIds(city_id, true);
			tmpSql += " and a.city_id in (" + citys + ") ";
		}
		if (null != vendor_id && !"".equals(vendor_id)) {
			// tmpSql += " and c.oui='" + vendor_id + "'";
			tmpSql += " and c.vendor_id='" + vendor_id + "'";
		}
		if (null != softwareversion && !"".equals(softwareversion)) {
			tmpSql += " and a.devicetype_id=" + softwareversion + " ";
		}
		if (null != device_serialnumber && !"".equals(device_serialnumber)) {
			if (device_serialnumber.length() > 5) {
				tmpSql += " and a.dev_sub_sn ='"
						+ device_serialnumber.substring(
								device_serialnumber.length() - 6,
								device_serialnumber.length()) + "' ";
			}
			tmpSql += " and a.device_serialnumber like '%"
					+ device_serialnumber + "' ";
		}
		if (null != loopback_ip && !"".equals(loopback_ip)) {
			tmpSql += " and a.loopback_ip like '%" + loopback_ip + "%' ";
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "";
		if (fields != null) {
			while (fields != null) {
				String username = getAllBoundUser((String) fields.get("oui"),
						(String) fields.get("device_serialnumber"), tab_name,
						"tr069");
				String passwd = getAllBoundPasswd((String) fields.get("oui"),
						(String) fields.get("device_serialnumber"), tab_name,
						"tr069");
				serviceHtml += "<input type="
						+ type
						+ " oui='"
						+ fields.get("oui")
						+ "' dev_serial=\""
						+ fields.get("device_serialnumber")
						+ "\" id=\"device_id\" name=\"device_id\"  gather_id=\""
						+ (String) fields.get("gather_id") + "\"  username=\""
						+ username + "\"  passwd=\"" + passwd
						+ "\" devicetype=\"tr069\" value=\""
						+ (String) fields.get("device_id") + "\"";
				if (needFilter) {
					serviceHtml += " onclick=\"filterByDevIDAndDevTypeID(this)\">";
				} else {
					serviceHtml += "\">";
				}
				serviceHtml += (String) fields.get("oui") + "-"
						+ (String) fields.get("device_serialnumber") + " | "
						+ (String) fields.get("loopback_ip") + " | " + username
						+ "<br>";
				fields = cursor.getNext();
			}
		}
		cursor = null;
		fields = null;
		return serviceHtml;
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????? ??????????????? add by liuli liuli@lianchuang.com
	 *
	 * @param request
	 * @return String ????????????????????????????????????????????????
	 */
	public String getDeviceListByVersion(HttpServletRequest request,
			boolean needFilter) {
		String gather_id = request.getParameter("gather_id");// ?????????
		// String oui = request.getParameter("vendor_id");//????????????
		String vendor_id = request.getParameter("vendor_id");// ????????????
		String softwareversion = request.getParameter("softwareversion");
		String devicetype_id = request.getParameter("devicetype_id");
		String hguser = request.getParameter("hguser");
		String telephone = request.getParameter("telephone");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();

		// ?????????????????????
		boolean isJoinHgwcustomer = false;
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone))) {
			isJoinHgwcustomer = true;
		}

		// TODO wait (more table related)
		// String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select ";
		if(isJoinHgwcustomer){
			tmpSql += " d.username, ";
		}
		tmpSql += " a.device_id, a.oui, a.device_serialnumber, a.device_id "
				+ " from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// ???admin??????
		if (!user.isAdmin()) {
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		if (isJoinHgwcustomer) {
			tmpSql += " inner join tab_hgwcustomer d on a.device_id = d.device_id ";
		}
		tmpSql += " where a.device_status=1";
		// ???admin??????
		if (!user.isAdmin()) {
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		// ?????????????????????
		if (null != hguser && !"".equals(hguser)) {
			tmpSql += " and d.username ='" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone)) {
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (gather_id != null && !gather_id.equals("")) {
			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (vendor_id != null && !vendor_id.equals("")) {
			tmpSql += " and c.vendor_id ='" + vendor_id + "'";
		}
		if (softwareversion != null && !softwareversion.equals("")) {
			tmpSql += " and c.softwareversion ='" + softwareversion + "'";
		}
		if (devicetype_id != null && !devicetype_id.equals("")) {
			tmpSql += " and a.devicetype_id =" + devicetype_id + " ";
		}
		tmpSql += " order by a.device_id";
		// logger.debug("deviceresource Sql---------------------:" +
		// tmpSql);
		logger.debug("========software/VersionManage.java======BEN===>>SQL:");
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null) {
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>????????????????????????</span></td></tr>";
		} else {
			int iflag = 1;
			while (fields != null) {
				tmpUser = (String) fields.get("username");
				logger.debug("	tmpUser:" + tmpUser);
				if (tmpUser == null || tmpUser.equalsIgnoreCase("null")) {
					tmpUser = "|";
					logger.debug("	username null");
				}
				if (iflag % 2 == 0) {
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\"";
					if (needFilter) {
						serviceHtml += " onclick=\"filterByDevID()\">";
					} else {
						serviceHtml += ">";
					}
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber")
							+ " | " + tmpUser;
					serviceHtml += "</td></tr>";
				} else {
					serviceHtml += "<tr>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\"";
					if (needFilter) {
						serviceHtml += " onclick=\"filterByDevID()\">";
					} else {
						serviceHtml += ">";
					}
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber")
							+ " | " + tmpUser;
					serviceHtml += "</td></tr>";
				}
				// iflag++;
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	public String gettemplateTypeList(boolean flag, String compare,
			String rename) {
		PrepareSQL psql = new PrepareSQL(m_TemplateType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_TemplateType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor,
				"template_id", "template_name", flag, compare, rename);
		return strVendorTypeList;
	}

	public String getdeviceTypeList() {
		PrepareSQL psql = new PrepareSQL(m_DeviceType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_DeviceType_SQL);
		String strVendorTypeList = FormUtil.createListBox1(cursor,
				"template_id", "template_name", false, "", "", true);
		return strVendorTypeList;
	}

	public Map getTemplateMap() {
		Map templateMap = new HashMap();
		String sql = "select template_id, template_name from tab_template ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				String template_id = (String) fields.get("template_id");
				String template_name = (String) fields.get("template_name");
				templateMap.put(template_id, template_name);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public Map getServiceMap() {
		Map templateMap = new HashMap();
		String sql = "select service_id, service_name from tab_service ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				String template_id = (String) fields.get("service_id");
				String template_name = (String) fields.get("service_name");
				templateMap.put(template_id, template_name);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public Map getDevicelistMap() {
		Map templateMap = new HashMap();
		// String sql = "select distinct a.devicetype_id,
		// a.device_model,b.devicetype_id
		// from tab_devicetype_info a,tab_template b where
		// a.devicetype_id=b.devicetype_id";
		String sql = "select distinct  a.devicetype_id, c.device_model "
				+ " from tab_devicetype_info a,tab_template b,gw_device_model c "
				+ " where a.devicetype_id=b.devicetype_id and a.device_model_id=c.device_model_id ";
		PrepareSQL psql = new PrepareSQL(sql);// TODO wait (more table related)
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			while (fields != null) {
				String template_id = (String) fields.get("devicetype_id");
				String template_name = (String) fields.get("device_model");
				templateMap.put(template_id, template_name);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public String getInternetPVCNode(String device_id, String pvc) {
		logger.debug("pvc----------pvc :" + pvc);
		// ??????????????????
		// int gw_type = LipossGlobals.getGw_Type(device_id);

		String wconnNode = "";
		String wpppConnNode = "-1";
		// String Pvc_Internet =
		// LipossGlobals.getLipossProperty("InstArea.PVC_835");
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_1 = collection_1.iterator();
		if (collection_1.size() == 0) {
			wconnNode = "-1";
		}
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		String pathWANConn = null;
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		String paraValue = "";
		Map paraValueMap;
		String path_ppp = "";
		while (iterator_1.hasNext()) {
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWANConn = (String) iterator_1.next();
			// logger.debug("GSJ----------pathWANConn:" + pathWANConn);
			paraValueMap = paramTreeObject.getParaValueMap(pathWANConn
					+ "WANDSLLinkConfig.DestinationAddress", device_id);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			// logger.debug("GSJ----------paraValue :" + paraValue);
			if (paraValue.indexOf(pvc) != -1) {
				wconnNode = getLastString(rootPara, pathWANConn);
				logger.debug("A----------wconnNode :" + wconnNode);
				collection_2 = WANConnDeviceAct.getParameterNameList(device_id,
						pathWANConn + "WANPPPConnection.");
				iterator_2 = collection_2.iterator();
				while (iterator_2.hasNext()) {
					path_ppp = (String) iterator_2.next();
					// logger.debug("GSJ----------path_ppp :" + path_ppp);
					wpppConnNode = getLastString(pathWANConn
							+ "WANPPPConnection.", path_ppp);
					logger.debug("B---------wpppConnNode :" + wpppConnNode);
				}
				break;
			}
		}
		return wconnNode + "," + wpppConnNode;
	}

	/**
	 * ??????????????????
	 *
	 * @param request
	 * @return
	 */
	public String chgBusiness(String device_id, HttpServletRequest request) {
		// String Pvc_Internet =
		// LipossGlobals.getLipossProperty("InstArea.PVC_835");
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_1 = collection_1.iterator();
		logger.debug("GSJ----------length:" + collection_1.size());
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		String pathWANConn = null;
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		// ??????????????????
		// int gw_type = LipossGlobals.getGw_Type(device_id);
		String paraValue = "";
		Map paraValueMap;
		int flag_connType = 0;
		int flag_username = 0;
		int flag_passwd = 0;
		String path_ppp = "";
		String connType = request.getParameter("connType");
		// String username = request.getParameter("username");
		// String passwd = request.getParameter("passwd");
		String resultStr = "-1";
		while (iterator_1.hasNext()) {
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWANConn = (String) iterator_1.next();
			logger.debug("GSJ----------pathWANConn:" + pathWANConn);
			paraValueMap = paramTreeObject.getParaValueMap(pathWANConn
					+ "WANDSLLinkConfig.DestinationAddress", device_id);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			logger.debug("GSJ----------paraValue :" + paraValue);
			if (paraValue.indexOf("8/85") != -1) {
				logger.debug("GSJ----------in :");
				collection_2 = WANConnDeviceAct.getParameterNameList(device_id,
						pathWANConn + "WANPPPConnection.");
				iterator_2 = collection_2.iterator();
				while (iterator_2.hasNext()) {
					path_ppp = (String) iterator_2.next();
					logger.debug("GSJ----------path_ppp :" + path_ppp);
					flag_connType = paramTreeObject.setParaValueFlag(path_ppp
							+ "ConnectionType", device_id, connType);
					logger.debug("GSJ----------flag_connType :" + flag_connType);
					if (flag_connType == 1 && "IP_Routed".equals(connType)) {
						logger.debug("GSJ----------????????????????????????!");
						flag_username = 1;// paramTreeObject.setParaValueFlag(path_ppp+"Username",ior,device_id,gather_id,username);
						if (flag_username == 1) {
							logger.debug("GSJ----------?????????????????????!");
							flag_passwd = 1;// paramTreeObject.setParaValueFlag(path_ppp+"Password",ior,device_id,gather_id,passwd);
							if (flag_passwd == 1) {
								logger.debug("GSJ----------??????????????????!");
								resultStr = "1";
								// ChongqiList(request);
							} else {
								// ??????????????????
								resultStr = "-3";
							}
						} else {
							// ?????????????????????
							resultStr = "-2";
						}
					} else if (flag_connType == 1
							&& "PPPoE_Bridged".equals(connType)) {
						// ????????????????????????
						resultStr = "1";
					} else {
					}
				}
				break;
			}
		}
		logger.debug("?????????????????????" + resultStr);
		return resultStr;
	}

	ArrayList pppoeList = new ArrayList();
	ArrayList ipList = new ArrayList();

	public void getAllNamePw(String device_id) {
		// String wconnNode = "";
		// String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		logger.debug("device_id---------:" + device_id);
		// logger.debug("gather_id---------:" + gather_id);
		// logger.debug("ior---------:" + ior);
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_1 = collection_1.iterator();
		if (collection_1.size() == 0) {
			return;
		}
		logger.debug("collection_1---------:" + collection_1);
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		Collection collection_3 = null;
		Iterator iterator_3 = null;
		String pathWANConn = null;
		// String paraValue = "";
		// Map paraValueMap;
		String path_ppp = "";
		String path_ip = "";
		String[] paramArrPPPoE = new String[6];
		String[] paramArrIP = new String[9];
		while (iterator_1.hasNext()) {
			pathWANConn = (String) iterator_1.next();
			logger.debug("A----------pathWANConn :" + pathWANConn);
			if (rootPara.equals(pathWANConn) || "".equals(pathWANConn)) {
				continue;
			}
			collection_2 = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANConn + "WANPPPConnection.");
			logger.debug("AA----------collection_2 :" + collection_2);
			iterator_2 = collection_2.iterator();
			while (iterator_2.hasNext()) {
				path_ppp = (String) iterator_2.next();
				if ((pathWANConn + "WANPPPConnection.").equals(path_ppp)
						|| "".equals(path_ppp)) {
					continue;
				}
				logger.debug("B---------path_ppp :" + path_ppp);
				paramArrPPPoE[0] = path_ppp + "ConnectionType";
				paramArrPPPoE[1] = path_ppp + "X_CT-COM_LanInterface";
				paramArrPPPoE[2] = path_ppp + "X_CT-COM_ServiceList";
				paramArrPPPoE[3] = path_ppp + "Enable";
				paramArrPPPoE[4] = path_ppp + "Username";
				paramArrPPPoE[5] = path_ppp + "Password";
				Map pppoeMap = getDevInfo_tr069(path_ppp, device_id,
						paramArrPPPoE);
				logger.debug("BB------------:" + pppoeMap);
				pppoeList.add(pppoeMap);
			}
			collection_3 = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANConn + "WANIPConnection.");
			iterator_3 = collection_3.iterator();
			while (iterator_3.hasNext()) {
				logger.debug("C----------collection_2 ");
				path_ip = (String) iterator_3.next();
				if ((pathWANConn + "WANIPConnection.").equals(path_ip)
						|| "".equals(path_ip)) {
					continue;
				}
				logger.debug("CC---------path_ip :" + path_ip);
				paramArrIP[0] = path_ip + "ConnectionType";
				paramArrIP[1] = path_ip + "X_CT-COM_LanInterface";
				paramArrIP[2] = path_ip + "X_CT-COM_ServiceList";
				paramArrIP[3] = path_ip + "Enable";
				paramArrIP[4] = path_ip + "AddressingType";
				paramArrIP[5] = path_ip + "ExternalIPAddress";
				paramArrIP[6] = path_ip + "SubnetMask";
				paramArrIP[7] = path_ip + "DefaultGateway";
				// paramArrIP[8] = path_ip + "DNSServers";
				paramArrIP[8] = "InternetGatewayDevice.WANDevice.1.WANCommonInterfaceConfig.WANAccessType";
				Map ipMap = getDevInfo_tr069(path_ip, device_id, paramArrIP);
				ipList.add(ipMap);
			}
		}
	}

	public ArrayList getpppoeList() {
		return pppoeList;
	}

	public ArrayList getipList() {
		return ipList;
	}

	// TODO
	/**
	 * ????????????PPPoE????????????
	 */
	ArrayList<String> allPPPoENames = new ArrayList<String>();

	public void getAllPPPoENames() {
		String paramName = null;
		String param1 = "InternetGatewayDevice.WANDevice.";
		String param2 = ".WANConnectionDevice.";
		String param3 = ".WANPPPConnection.";
		// String param4 = ".Username";
		// logger.debug("allParamsNamesMap----------:" + allParamsNamesMap);
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0) {
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++) {
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, param3, ".Enable")
					|| parse(paramName, param1, param2, param3, ".Username")
					|| parse(paramName, param1, param2, param3,
							".ConnectionType")) {
				allPPPoENames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnPPPoENames() {
		return allPPPoENames;
	}

	/**
	 * ????????????IP????????????
	 */
	ArrayList<String> allIPNames = new ArrayList<String>();

	public void getAllIPNames() {
		String paramName = null;
		String param1 = "InternetGatewayDevice.WANDevice.";
		String param2 = ".WANConnectionDevice.";
		String param3 = ".WANIPConnection.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0) {
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++) {
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, param3, ".Enable")
					|| parse(paramName, param1, param2, param3,
							".ExternalIPAddress")
					|| parse(paramName, param1, param2, param3, ".SubnetMask")
					|| parse(paramName, param1, param2, param3,
							".DefaultGateway")
					|| parse(paramName, param1, param2, param3, ".DNSServers")) {
				allIPNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnIPNames() {
		return allIPNames;
	}

	/**
	 * ????????????WLAN????????????
	 */
	ArrayList<String> allWLANNames = new ArrayList<String>();

	public void getAllWLANNames() {
		String paramName = null;
		String param1 = "InternetGatewayDevice.LANDevice.";
		String param2 = ".WLANConfiguration.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0) {
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++) {
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, ".Status")
					|| parse(paramName, param1, param2, ".SSID")) {
				allWLANNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnWLANNames() {
		return allWLANNames;
	}

	/**
	 * ????????????DHCP????????????
	 */
	ArrayList<String> allDHCPNames = new ArrayList<String>();

	public void getAllDHCPNames() {
		String paramName = null;
		String param1 = "InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0) {
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++) {
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, ".DHCPConfig.DHCPServerEnable")
					|| parse(paramName, param1, ".DHCPConfig.MinAddress")
					|| parse(paramName, param1, ".DHCPConfig.MaxAddress")
					|| parse(paramName, param1, ".DHCPConfig.ReservedAddresses")
					|| parse(paramName, param1, ".DHCPConfig.SubnetMask")
					|| parse(paramName, param1, ".DHCPConfig.DNSServers")
					|| parse(paramName, param1, ".DHCPConfig.DomainName")
					|| parse(paramName, param1, ".DHCPConfig.DefaultGateway")) {
				allDHCPNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnDHCPNames() {
		return allDHCPNames;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	public ArrayList<String> returnallUserfulParamsNames() {
		return allUserfulParamsNamesList;
	}

	/** ??????????????????????????? */
	ArrayList<String> allUserfulParamsNamesList = new ArrayList<String>();
	HashMap<String, String> allParamsNamesMap = new HashMap<String, String>();

	/**
	 * ???????????????????????????????????????????????????i???
	 *
	 * @param line
	 * @return true false
	 */
	private boolean parse(String line, String param1, String param2,
			String param3, String param4) {
		// logger.debug("line-T1:" + line);
		Pattern a = Pattern.compile(param1 + "\\d{1,2}" + param2 + "\\d{1,2}"
				+ param3 + "\\d{1,2}" + param4);
		Matcher m = a.matcher(line.trim());
		// logger.debug(m.matches());
		return m.matches();
	}

	/**
	 * ???????????????????????????????????????????????????i???
	 *
	 * @param line
	 * @return true false
	 */
	private boolean parse(String line, String param1, String param2,
			String param3) {
		// logger.debug("line-T2:" + line);
		Pattern a = Pattern.compile(param1 + "\\d{1,2}" + param2 + "\\d{1,2}"
				+ param3);
		Matcher m = a.matcher(line.trim());
		// logger.debug(m.matches());
		return m.matches();
	}

	/**
	 * ???????????????????????????????????????????????????i???
	 *
	 * @param line
	 * @return true false
	 */
	private boolean parse(String line, String param1, String param2) {
		// logger.debug("line-T3:" + line);
		Pattern a = Pattern.compile(param1 + "\\d{1,2}" + param2);
		Matcher m = a.matcher(line.trim());
		// logger.debug(m.matches());
		return m.matches();
	}

	/**
	 * ????????????InternetGatewayDevice.????????????
	 *
	 * @return
	 */
	public void getAllParamNames(String device_id) {
		logger.debug("GSJ--------------getAllParamNames");

		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);

		// ???????????????????????????
		String paraV = "InternetGatewayDevice.";
		// ?????????????????????MAP
		// HashMap paraMap = new HashMap();
		allParamsNamesMap.clear();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(0);
		DevRpc[] devRPCArr = this.getDevRPCArr(device_id, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type, gw_type);
		Element element = dealDevRPCResponse("GetParameterNamesResponse",
				devRpcCmdOBJList, device_id);
		if (null == element) {
			return;
		}
		// ???????????????????????????
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// ????????????XML??????,??????????????????
		if (null != getParameterNamesResponse) {
			ParameterInfoStruct[] pisArr = getParameterNamesResponse
					.getParameterList();
			if (null != pisArr) {
				String name = null;
				for (int i = 0; i < pisArr.length; i++) {
					name = pisArr[i].getName();
					// String writable = pisArr[i].getWritable();
					allParamsNamesMap.put(i + "", name);
					// logger.debug("GSJ--------------allParamsNamesMap:"+allParamsNamesMap);
				}
			}
		}
		// return paraMap;
		getAllPPPoENames();
		getAllIPNames();
		getAllWLANNames();
		getAllDHCPNames();
		allParamsNamesMap.clear();
		allParamsNamesMap = null;
	}

	/**
	 * ?????????????????????????????????RPC????????????
	 *
	 * @author wangsenbo
	 * @date Mar 22, 2011
	 * @param
	 * @return RPCObject
	 */
	private Element dealDevRPCResponse(String stringRpcName,
			List<DevRpcCmdOBJ> devRpcCmdOBJList, String deviceId) {
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0) {
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(0);
		if (devRpcCmdOBJ == null) {
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdOBJ
				.getRpcList();
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0) {
			logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(0);
		if (acsRpcCmdObj == null) {
			logger.warn("[{}]ACSRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		if (stringRpcName == null) {
			logger.warn("[{}]stringRpcName?????????", deviceId);
			return null;
		}
		if (stringRpcName.equals(acsRpcCmdObj.getRpcName())) {
			String resp = acsRpcCmdObj.getValue();
			logger.warn("[{}]???????????????{}", deviceId, resp);
			Fault fault = null;
			if (resp == null || "".equals(resp)) {
				logger.debug("[{}]DevRpcCmdOBJ.value == null", deviceId);
			} else {
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapOBJ != null) {
					fault = XmlToRpc.Fault(soapOBJ.getRpcElement());
					if (fault != null) {
						logger.warn("setValue({})={}", deviceId, fault
								.getDetail().getFaultString());
					} else {
						return soapOBJ.getRpcElement();
					}
				}
			}
		}
		return null;
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param device_id
	 * @param gather_id
	 * @param params
	 * @return
	 */
	Map<String, String> AllNeededNameValueMap = new HashMap();
	String[] params1 = new String[10];
	ArrayList AllNeededList = new ArrayList();

	public void getAllNames_ValueMap(String device_id, String gather_id,
			String[] params, String ior) {
		try {
			logger.debug("???????????????????????????????????????????????????5???...");
			Thread.sleep(5000);
			logger.debug("??????????????????????????????ACS...");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		AllNeededNameValueMap = getDevInfo_webtopo(device_id, params);
		// return pppoeMap;
	}

	public Map<String, String> getAllNeededNameValueMap() {
		if (null == AllNeededNameValueMap || AllNeededNameValueMap.size() == 0) {
			return null;
		} else {
			return AllNeededNameValueMap;
		}
	}

	ArrayList AllNamesList = new ArrayList();
	ArrayList AllStaticIPList = new ArrayList();
	ArrayList AllwlanList = new ArrayList();
	ArrayList AlldhcpList = new ArrayList();
	StringBuffer allNamesBuff = new StringBuffer();
	StringBuffer allIPNamesBuff = new StringBuffer();
	StringBuffer allWLANNamesBuff = new StringBuffer();
	StringBuffer allDHCPNamesBuff = new StringBuffer();

	public void getAllNamePw_webtopo(String device_id, String gather_id,
			String ior) {
		String rootPara = "InternetGatewayDevice.WANDevice.";
		logger.debug("device_id|gather_id|ior:" + device_id + "|" + gather_id
				+ "|" + ior);
		Collection collection_0 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_0 = collection_0.iterator();
		if (collection_0.size() == 0) {
			return;
		}
		logger.debug("collection_0=:" + collection_0);
		Collection collection_1 = null;
		Iterator iterator_1 = null;
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		String pathWANDevice = null;
		String pathWANConn = null;
		String pathPPPConn = "";
		String[] paramArrPPPoE = new String[3];
		while (iterator_0.hasNext()) {
			// ??????InternetGatewayDevice.WANDevice.{i}.
			pathWANDevice = (String) iterator_0.next();
			logger.debug("pathWANDevice=:" + pathWANDevice);
			if (rootPara.equals(pathWANDevice) || "".equals(pathWANDevice)) {
				continue;
			}
			collection_1 = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANDevice + "WANConnectionDevice.");
			iterator_1 = collection_1.iterator();
			if (collection_1.size() == 0) {
				return;
			}
			while (iterator_1.hasNext()) {
				// ??????InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.
				pathWANConn = (String) iterator_1.next();
				logger.debug("pathWANConn=:" + pathWANConn);
				if (pathWANConn.equals(pathWANConn + "WANConnectionDevice.")
						|| "".equals(pathWANConn)) {
					continue;
				}
				collection_2 = WANConnDeviceAct.getParameterNameList(device_id,
						pathWANConn + "WANPPPConnection.");
				iterator_2 = collection_2.iterator();
				while (iterator_2.hasNext()) {
					pathPPPConn = (String) iterator_2.next();
					logger.debug("pathPPPConn=:" + pathPPPConn);
					if ((pathWANConn + "WANPPPConnection.").equals(pathPPPConn)
							|| "".equals(pathPPPConn)) {
						continue;
					}
					paramArrPPPoE[0] = pathPPPConn + "Enable";
					paramArrPPPoE[1] = pathPPPConn + "Username";
					paramArrPPPoE[2] = pathPPPConn + "ConnectionType";
					// TODO
					allNamesBuff.append(paramArrPPPoE[0] + "|"
							+ paramArrPPPoE[1] + "|" + paramArrPPPoE[2]);
					allNamesBuff.append("#");
					Map pppoeMap = getDevInfo_webtopo(device_id, paramArrPPPoE);
					// pppoeMap.put("AllNames", );
					AllNamesList.add(pppoeMap);
					// AllNamesList.add(allNamesBuff.toString());
				}
			}
		}
		// ????????????
		collection_0 = null;
		iterator_0 = null;
		collection_1 = null;
		iterator_1 = null;
		collection_2 = null;
		iterator_2 = null;
	}

	public void getAllIP_webtopo(String device_id, String gather_id, String ior) {
		String rootPara = "InternetGatewayDevice.WANDevice.";
		logger.debug("device_id|gather_id|ior:" + device_id + "|" + gather_id
				+ "|" + ior);
		Collection collection_0 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_0 = collection_0.iterator();
		if (collection_0.size() == 0) {
			return;
		}
		logger.debug("collection_0=:" + collection_0);
		Collection collection_1 = null;
		Iterator iterator_1 = null;
		Collection collection_2 = null;
		Iterator iterator_2 = null;
		String pathWANDevice = null;
		String pathWANConn = null;
		String pathIPConn = null;
		String[] paramArrIP = new String[5];
		while (iterator_0.hasNext()) {
			// ??????InternetGatewayDevice.WANDevice.{i}.
			pathWANDevice = (String) iterator_0.next();
			logger.debug("pathWANDevice=:" + pathWANDevice);
			if (rootPara.equals(pathWANDevice) || "".equals(pathWANDevice)) {
				continue;
			}
			collection_1 = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANDevice + "WANConnectionDevice.");
			iterator_1 = collection_1.iterator();
			if (collection_1.size() == 0) {
				return;
			}
			while (iterator_1.hasNext()) {
				// ??????InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.
				pathWANConn = (String) iterator_1.next();
				logger.debug("pathWANConn=:" + pathWANConn);
				if (pathWANConn.equals(pathWANConn + "WANConnectionDevice.")
						|| "".equals(pathWANConn)) {
					continue;
				}
				collection_2 = WANConnDeviceAct.getParameterNameList(device_id,
						pathWANConn + "WANIPConnection.");
				iterator_2 = collection_2.iterator();
				while (iterator_2.hasNext()) {
					pathIPConn = (String) iterator_2.next();
					logger.debug("pathIPConn=:" + pathIPConn);
					if ((pathWANConn + "WANIPConnection.").equals(pathIPConn)
							|| "".equals(pathIPConn)) {
						continue;
					}
					paramArrIP[0] = pathIPConn + "Enable";
					paramArrIP[1] = pathIPConn + "ExternalIPAddress";
					paramArrIP[2] = pathIPConn + "SubnetMask";
					paramArrIP[3] = pathIPConn + "DefaultGateway";
					paramArrIP[4] = pathIPConn + "DNSServers";
					allIPNamesBuff.append(paramArrIP[0] + "|" + paramArrIP[1]
							+ "|" + paramArrIP[2] + "|" + paramArrIP[3] + "|"
							+ paramArrIP[4]);
					allIPNamesBuff.append("#");
					Map ipMap = getDevInfo_webtopo(device_id, paramArrIP);
					// ipMap.put("paramNames", paramArrIP);
					AllStaticIPList.add(ipMap);
				}
			}
		}
		// ????????????
		collection_0 = null;
		iterator_0 = null;
		collection_1 = null;
		iterator_1 = null;
		collection_2 = null;
		iterator_2 = null;
	}

	public String getAllpppoeNames() {
		return allNamesBuff.toString();
	}

	public String getAllipNames() {
		return allIPNamesBuff.toString();
	}

	public void getAllwlan_webtopo(String device_id, String gather_id,
			String ior) {
		// String wconnNode = "";
		// String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.LANDevice.";
		// ??????WANConnectionDevice.?????????????????????
		logger.debug("device_id|gather_id|ior:" + device_id + "|" + gather_id
				+ "|" + ior);
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_1 = collection_1.iterator();
		if (collection_1.size() == 0) {
			return;
		}
		logger.debug("collection_1=:" + collection_1);
		Collection collection_3 = null;
		Iterator iterator_3 = null;
		String pathWANConn = null;
		String path_ip = "";
		String[] paramArrIP = new String[2];
		while (iterator_1.hasNext()) {
			pathWANConn = (String) iterator_1.next();
			logger.debug("pathWANConn=:" + pathWANConn);
			if (rootPara.equals(pathWANConn) || "".equals(pathWANConn)) {
				continue;
			}
			collection_3 = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANConn + "WLANConfiguration.");
			iterator_3 = collection_3.iterator();
			while (iterator_3.hasNext()) {
				path_ip = (String) iterator_3.next();
				logger.debug("path_SSID=:" + path_ip);
				if ((pathWANConn + "WLANConfiguration.").equals(path_ip)
						|| "".equals(path_ip)) {
					continue;
				}
				paramArrIP[0] = path_ip + "Status";
				paramArrIP[1] = path_ip + "SSID";
				allWLANNamesBuff.append(paramArrIP[0] + "|" + paramArrIP[1]);
				allWLANNamesBuff.append("#");
				Map ipMap = getDevInfo_webtopo(device_id, paramArrIP);
				AllwlanList.add(ipMap);
			}
		}
	}

	public String getAllwlanNames() {
		return allWLANNamesBuff.toString();
	}

	public void getAlldhcp_webtopo(String device_id, String gather_id,
			String ior) {
		// String wconnNode = "";
		// String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.";
		// ??????WANConnectionDevice.?????????????????????
		logger.debug("device_id|gather_id|ior:" + device_id + "|" + gather_id
				+ "|" + ior);
		Collection collection_1 = WANConnDeviceAct.getParameterNameList(
				device_id, rootPara);
		Iterator iterator_1 = collection_1.iterator();
		if (collection_1.size() == 0) {
			return;
		}
		logger.debug("collection_1=:" + collection_1);
		// Collection collection_3 = null;
		// Iterator iterator_3 = null;
		String pathWANConn = null;
		String[] paramArrIP = new String[8];
		while (iterator_1.hasNext()) {
			pathWANConn = (String) iterator_1.next();
			logger.debug("pathWANConn=:" + pathWANConn);
			if (rootPara.equals(pathWANConn) || "".equals(pathWANConn)) {
				continue;
			}
			paramArrIP[0] = pathWANConn + "DHCPConfig.DHCPServerEnable";
			paramArrIP[1] = pathWANConn + "DHCPConfig.MinAddress";
			paramArrIP[2] = pathWANConn + "DHCPConfig.MaxAddress";
			paramArrIP[3] = pathWANConn + "DHCPConfig.ReservedAddresses";
			paramArrIP[4] = pathWANConn + "DHCPConfig.SubnetMask";
			paramArrIP[5] = pathWANConn + "DHCPConfig.DNSServers";
			paramArrIP[6] = pathWANConn + "DHCPConfig.DomainName";
			paramArrIP[7] = pathWANConn + "DHCPConfig.DefaultGateway";
			allDHCPNamesBuff.append(paramArrIP[0] + "|" + paramArrIP[1] + "|"
					+ paramArrIP[2] + "|" + paramArrIP[3] + "|" + paramArrIP[4]
					+ "|" + paramArrIP[5] + "|" + paramArrIP[6] + "|"
					+ paramArrIP[7]);
			allDHCPNamesBuff.append("#");
			Map ipMap = getDevInfo_webtopo(device_id, paramArrIP);
			AlldhcpList.add(ipMap);
		}
	}

	public String getAlldhcpNames() {
		return allDHCPNamesBuff.toString();
	}

	public ArrayList getAllDHCPList() {
		return AlldhcpList;
	}

	public ArrayList getAllwlanList() {
		return AllwlanList;
	}

	public ArrayList getAllNamesList() {
		return AllNamesList;
	}

	public ArrayList getAllStaticIPList() {
		return AllStaticIPList;
	}

	// /**
	// * ?????????????????????????????????
	// *
	// * @param params_name
	// * @param params_value
	// * @return -1:??????????????????????????????????????? 0:?????????????????? 1:????????????
	// */
	// public int setParamValueIP(String[] params_name, String[] params_value,
	// String device_id, String gather_id, String ior)
	// {
	// this.ior = ior;
	// int flag = 0;
	// // ???????????????:SetParameterValues
	// SetParameterValues setParameterValues = new SetParameterValues();
	// setParameterValues.setParameterKey("ss");
	// logger.debug("Z------------------------ :");
	// String[] para_type_id = { "1", "1", "1", "4", "1", "1", "1", "1" };//
	// this.getParaType(params_name,
	// // device_id,
	// // gather_id);
	// for (int i = 0; i < para_type_id.length; i++)
	// {
	// logger.debug("ZZ------------------------ :" + para_type_id[i]);
	// }
	// if (null == para_type_id)
	// {
	// return -2;
	// }
	// if ("-9".equals(para_type_id[0]))
	// {
	// // ????????????????????????????????????????????????
	// return -1;
	// }
	// if ("-10".equals(para_type_id[0]))
	// {
	// // ??????????????????
	// return -10;
	// }
	// logger.debug("para_type_id------------------------ :" + para_type_id[0]);
	// ParameterValueStruct[] parameterValueStructArr = new
	// ParameterValueStruct[params_name.length];
	// for (int i = 0; i < params_name.length; i++)
	// {
	// AnyObject anyObject = new AnyObject();
	// anyObject.para_value = params_value[i];
	// anyObject.para_type_id = para_type_id[i];
	// parameterValueStructArr[i] = new ParameterValueStruct();
	// parameterValueStructArr[i].setName(params_name[i]);
	// parameterValueStructArr[i].setValue(anyObject);
	// }
	// logger.debug("UUU------------------------ ");
	// setParameterValues.setParameterList(parameterValueStructArr);
	// DevRPC[] devRPCArr = this.getDevRPCArr(setParameterValues, device_id);
	// logger.debug("UUUa------------------------devRPCArr: " + devRPCArr);
	// try
	// {
	// DevRPCRep[] devRPCRep = this.getDevRPCResponse(devRPCArr, device_id);
	// // ???????????????????????????
	// logger.debug("UUUB------------------------devRPCRep: " + devRPCRep);
	// String setRes = devRPCRep[0].rpcArr[0];
	// // ?????????SetParameterValuesResponse??????????????????????????????????????????????????????????????????????????????????????????????????????
	// SetParameterValuesResponse setParameterValuesResponse = new
	// SetParameterValuesResponse();
	// // ???SOAP?????????????????????????????????XML,????????????
	// SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
	// if (soapOBJ == null)
	// {
	// return flag;
	// }
	// setParameterValuesResponse = XmlToRpc.SetParameterValuesResponse(soapOBJ
	// .getRpcElement());
	// logger.debug("setParameterValuesResponse---:" +
	// setParameterValuesResponse);
	// if (null != setParameterValuesResponse)
	// {
	// flag = 1;
	// }
	// else
	// {
	// flag = 0;
	// }
	// }
	// catch (Exception e)
	// {
	// flag = 0;
	// e.printStackTrace();
	// }
	// return flag;
	// }
	// /**
	// * ?????????????????????????????????
	// *
	// * @param params_name
	// * @param params_value
	// * @return -1:??????????????????????????????????????? 0:?????????????????? 1:????????????
	// */
	// public int setParamValuePPP(String[] params_name, String[] params_value,
	// String device_id, String gather_id, String ior)
	// {
	// this.ior = ior;
	// int flag = 0;
	// // ???????????????:SetParameterValues
	// SetParameterValues setParameterValues = new SetParameterValues();
	// setParameterValues.setParameterKey("ss");
	// logger.debug("Z------------------------ :");
	// String[] para_type_id = { "1", "1", "1", "4", "1", "1" };//
	// this.getParaType(params_name,
	// // device_id,
	// // gather_id);
	// for (int i = 0; i < para_type_id.length; i++)
	// {
	// logger.debug("ZZ------------------------ :" + para_type_id[i]);
	// }
	// if (null == para_type_id)
	// {
	// return -2;
	// }
	// if ("-9".equals(para_type_id[0]))
	// {
	// // ????????????????????????????????????????????????
	// return -1;
	// }
	// if ("-10".equals(para_type_id[0]))
	// {
	// // ??????????????????
	// return -10;
	// }
	// logger.debug("para_type_id------------------------ :" + para_type_id[0]);
	// ParameterValueStruct[] parameterValueStructArr = new
	// ParameterValueStruct[params_name.length];
	// for (int i = 0; i < params_name.length; i++)
	// {
	// AnyObject anyObject = new AnyObject();
	// anyObject.para_value = params_value[i];
	// anyObject.para_type_id = para_type_id[i];
	// parameterValueStructArr[i] = new ParameterValueStruct();
	// parameterValueStructArr[i].setName(params_name[i]);
	// parameterValueStructArr[i].setValue(anyObject);
	// }
	// logger.debug("UUU------------------------ ");
	// setParameterValues.setParameterList(parameterValueStructArr);
	// DevRPC[] devRPCArr = this.getDevRPCArr(setParameterValues, device_id);
	// logger.debug("UUUa------------------------devRPCArr: " + devRPCArr);
	// try
	// {
	// DevRPCRep[] devRPCRep = this.getDevRPCResponse(devRPCArr, device_id);
	// // ???????????????????????????
	// logger.debug("UUUB------------------------devRPCRep: " + devRPCRep);
	// String setRes = devRPCRep[0].rpcArr[0];
	// // ?????????SetParameterValuesResponse??????????????????????????????????????????????????????????????????????????????????????????????????????
	// SetParameterValuesResponse setParameterValuesResponse = new
	// SetParameterValuesResponse();
	// // ???SOAP?????????????????????????????????XML,????????????
	// SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
	// if (soapOBJ == null)
	// {
	// return flag;
	// }
	// setParameterValuesResponse = XmlToRpc.SetParameterValuesResponse(soapOBJ
	// .getRpcElement());
	// logger.debug("setParameterValuesResponse---:" +
	// setParameterValuesResponse);
	// if (null != setParameterValuesResponse)
	// {
	// flag = 1;
	// }
	// else
	// {
	// flag = 0;
	// }
	// }
	// catch (Exception e)
	// {
	// flag = 0;
	// e.printStackTrace();
	// }
	// return flag;
	// }
	/**
	 * ???????????????????????????????????????1:String 4:boolean ??????tab_para
	 *
	 * @param paras_name
	 * @return
	 */
	// public String[] getParaType(String[] paras_name, String device_id, String
	// gather_id)
	// {
	// GetParameterValues getParameterValues = new GetParameterValues();
	// String[] paramValues = paras_name;
	// getParameterValues.setParameterNames(paramValues);
	// for (int i = 0; i < paras_name.length; i++)
	// {
	// logger.debug("paras_name---------:" + paras_name[i]);
	// }
	// String[] paraType_id = null;
	// DevRPC[] devRPCArr = this.getDevRPCArr(getParameterValues, device_id);
	// // ????????????????????????
	// if (devRPCArr == null)
	// return null;
	// try
	// {
	// DevRPCRep[] devRPCRep = this.getDevRPCResponse(devRPCArr, gather_id);
	// if (null == devRPCRep)
	// {
	// // ??????????????????
	// return new String[] { "-10" };
	// }
	// // ???????????????????????????
	// String setRes = devRPCRep[0].rpcArr[0];
	// GetParameterValuesResponse getParameterValuesResponse = new
	// GetParameterValuesResponse();
	// // ???SOAP?????????????????????????????????XML,????????????
	// SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(setRes));
	// if (soapOBJ == null)
	// {
	// return paraType_id;
	// }
	// getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ
	// .getRpcElement());
	// if (null != getParameterValuesResponse)
	// {
	// ParameterValueStruct[] pisArr = getParameterValuesResponse
	// .getParameterList();
	// for (int i = 0; i < pisArr.length; i++)
	// {
	// logger.debug("pisArr---------:" + pisArr[i].getName());
	// }
	// paraType_id = new String[pisArr.length];
	// if (pisArr != null)
	// {
	// String name = null;
	// String value = null;
	// for (int i = 0; i < pisArr.length; i++)
	// {
	// name = pisArr[i].getName();
	// value = pisArr[i].getValue().para_value;
	// paraType_id[i] = pisArr[i].getValue().para_type_id;
	// logger.debug("GSJ---paraType_id[i] :" + paraType_id[i]);
	// logger.debug("GSJ---name :" + name);
	// logger.debug("GSJ---value :" + value);
	// }
	// }
	// }
	// else
	// {
	// // ????????????????????????????????????????????????
	// return new String[] { "-9" };
	// }
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// return paraType_id;
	// }
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRPCArr,
			int rpcType, String gw_type) {
		logger.debug("getDevRPCResponse(devRPCArr)");
		if (devRPCArr == null) {
			logger.error("devRPCArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, rpcType);
		return devRPCRep;
	}

	/**
	 * ??????device_id???????????????1???DevRPC????????????
	 *
	 * @param device_id
	 *            ??????id
	 * @param rpcObject
	 *            ----GetParameterValues/GetParameterNames/
	 * @return
	 */
	public DevRpc[] getDevRPCArr(String device_id, RPCObject rpcObject) {
		DevRpc[] devRPCArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null) {
			stringRpcValue = "";
			stringRpcName = "";
		} else {
			stringRpcValue = rpcObject.toRPC();
			stringRpcName = rpcObject.getClass().getSimpleName();
		}
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		devRPCArr[0].rpcArr = new Rpc[1];
		devRPCArr[0].rpcArr[0] = new Rpc();
		devRPCArr[0].rpcArr[0].rpcId = "1";
		devRPCArr[0].rpcArr[0].rpcName = stringRpcName;
		devRPCArr[0].rpcArr[0].rpcValue = stringRpcValue;
		return devRPCArr;
	}

	/**
	 * ??????tr069?????????????????????????????????????????????????????????????????????
	 *
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return ??????????????????key???HashMap?????????????????????????????????????????????
	 */
	public Map<String, String> getDevInfo_tr069(String path, String device_id,
			String[] arr) {
		// ???ACS????????????????????????
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		Map paraValueMap = null;
		Map<String, String> InfoMap = new HashMap<String, String>();
		// ??????????????????????????????
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				if (!"".equals(arr[i])) {
					paraValueMap = paramTreeObject.getParaValueMap(arr[i],
							device_id);
					if (paraValueMap != null) {
						InfoMap.put("" + i, (String) paraValueMap.get(arr[i])
								+ "|" + path);
					}
					// ??????1???
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return InfoMap;
	}

	/**
	 * ??????tr069?????????????????????????????????????????????????????????????????????
	 *
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return ??????????????????key???HashMap?????????????????????????????????????????????
	 */
	public Map<String, String> getDevInfo_webtopo(String device_id, String[] arr) {
		// ???ACS????????????????????????
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		Map paraValueMap = paramTreeObject.getParaValue_multi(arr, device_id);
		return paraValueMap;
	}

	/**
	 * ??????IOR
	 *
	 * @param gather_id
	 * @return ior
	 */
	private String getIOR(String gather_id) {
		String ior = "";
		String getIorSQL = "select ior from tab_ior where object_name='ACS_"
				+ gather_id + "' and object_poa='ACS_Poa_" + gather_id + "'";
		PrepareSQL psql = new PrepareSQL(getIorSQL);
		psql.getSQL();
		Map map = DataSetBean.getRecord(getIorSQL);
		if (null != map) {
			ior = (String) map.get("ior");
		}
		return ior;
	}

	/**
	 * @return
	 */
	// public String chgParaValues() {
	// String rootPara =
	// "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
	//
	// GetParameterNames getParameterNames = new GetParameterNames();
	//
	//
	// return null;
	// }
	/**
	 * tr069 ??? snmp ??????PING ??????
	 *
	 * @author lizj ???5202???
	 * @param request
	 * @return
	 */
	public String allPingResult(HttpServletRequest request) {
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (devicetype.equals("tr069")) {
			strResult = PingList(request);
		} else {
			// ??????SnmpGwCheck ??????
			strResult = snmpPing(request);
		}
		return strResult;
	}

	/**
	 * SNMP??????PING???????????????corba??????
	 *
	 * @author lizj ???5202???
	 * @param request
	 * @return
	 */
	public String snmpPing(HttpServletRequest request) {
		// ??????ID
		String device_id = request.getParameter("device_id");
		// ??????IP
		String test_ip = request.getParameter("test_ip");
		// ????????????ms
		String time_out = request.getParameter("time_out");
		logger.debug("device_id :" + device_id);
		logger.debug("test_ip :" + test_ip);
		logger.debug("time_out :" + time_out);
		// ??????????????????oid ???????????????????????? ??????ping???????????????OID
		String StrSQL = "select oid from tab_gw_model_oper_oid where device_model = "
				+ "(select device_model_id from tab_gw_device where device_id= '"
				+ device_id + "') and oid_type =3";
		PrepareSQL psql = new PrepareSQL(StrSQL);
		psql.getSQL();
		Map oidMap = DataSetBean.getRecord(StrSQL);
		// ??????????????????
		StrSQL = "select device_id, snmp_w_passwd from sgw_security where device_id='" + device_id
				+ "'";
		String deviceSQL = "select loopback_ip,snmp_udp from tab_gw_device where device_id='"
				+ device_id + "'";
		psql = new PrepareSQL(StrSQL);
		psql.getSQL();
		Map deviceMap = DataSetBean.getRecord(StrSQL);
		psql = new PrepareSQL(deviceSQL);
		psql.getSQL();
		Map gwMap = DataSetBean.getRecord(deviceSQL);
		// ???????????????????????????
		SnmpGwCheck.PingDev setDev = new SnmpGwCheck.PingDev();
		setDev.device_id = (String) deviceMap.get("device_id");
		// ??????
		setDev.loopback_ip = (String) gwMap.get("loopback_ip");
		// oid
		setDev.oid = (String) oidMap.get("oid");
		// ??????????????????????????????snmp??????
		setDev.port = Integer.parseInt((String) gwMap.get("snmp_udp"));
		// ?????????
		setDev.set_comm = (String) deviceMap.get("snmp_w_passwd");
		setDev.timeout = 60;
		setDev.value = "ADDR=" + test_ip + ";TIMES=" + time_out;
		setDev.version = "2c";
		logger.debug("-----------------++++++++-----------------------");
		logger.debug("device_id=" + setDev.device_id + "-loopback_ip="
				+ setDev.loopback_ip + "-oid=" + setDev.oid);
		logger.debug("port=" + setDev.port + "-set_comm=" + setDev.set_comm
				+ "-timeout=" + setDev.timeout);
		logger.debug("value=" + setDev.value + "-version=" + setDev.version);
		SnmpGwCheck.PingDev[] pingDev_arr = new SnmpGwCheck.PingDev[1];
		pingDev_arr[0] = setDev;
		// ????????????corba??????
		SnmpGwCheck.PingData[] resultData = SnmpGwCheckInterface.GetInstance()
				.SnmpPing(pingDev_arr, device_id);
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
		serviceHtml += "????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
		serviceHtml += "??????????????????(ms)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
		serviceHtml += "???????????????(%)";
		serviceHtml += "</td></tr>";
		String time = "";
		String miss = "";
		if (resultData == null || resultData.length == 0) {
			serviceHtml += "<tr class=''>";
			serviceHtml += "<td bgcolor='#FFFFFF' colspan='3' nowrap>??????corba?????????";
			serviceHtml += "</td></tr>";
		} else {
			for (int i = 0; i < resultData.length; i++) {
				time = (String) resultData[i].times;
				miss = (String) resultData[i].miss;
				logger.debug("time :" + time);
				logger.debug("miss :" + miss);
				if (time == null || (time.equals("") && miss.equals(""))) {
					serviceHtml += "<tr class=''><td bgcolor='#FFFFFF' width='30%' nowrap colspan='3'> ?????????????????????????????????!";
					serviceHtml += "</td></tr>";
				} else {
					serviceHtml += "<tr class=''>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
					Map fields = DataSetBean
							.getRecord("select loopback_ip from tab_gw_device where device_id='"
									+ resultData[i].device_id + "'");
					serviceHtml += fields.get("loopback_ip");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
					serviceHtml += StringUtils.formatString(time, 2);
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
					serviceHtml += StringUtils.formatString(miss, 2);
					serviceHtml += "</td></tr>";
					fields = null;
				}
			}
		}
		return serviceHtml;
	}

	/**
	 * ??????Ping?????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @return
	 */
	public String PingList(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		// ??????????????????
		String gw_type = request.getParameter("gw_type");
		// String gather_id = "";
		String Interface = request.getParameter("Interface");
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan???ping?????????????????????
		// begin
		if (Interface
				.startsWith("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig")) {
			Interface = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
			logger.debug("??????ping?????????LAN????????????InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1");
		}
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan???ping?????????????????????
		// end
		String Host = request.getParameter("Host");
		String NumberOfRepetitions = request
				.getParameter("NumberOfRepetitions");
		String Timeout = request.getParameter("Timeout");
		String DataBlockSize = request.getParameter("DataBlockSize");
		// String DSCP = request.getParameter("DSCP");
		// String strAction = request.getParameter("action");
		DevRpc[] devRPCArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 ??????????????????????????????????????????????????????
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 ??????????????????????????????????????????????????????
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(device_id, out + "-" + SerialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[7];
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;// + ".";????????????????????????????????????
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = Host;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = DataBlockSize;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		ParameterValueStruct[6] = new ParameterValueStruct();
		ParameterValueStruct[6]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = "0";
		// unsignedInt
		anyObject.para_type_id = "3";
		ParameterValueStruct[6].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "IP???????????????";
		serviceHtml += "</td></tr>";
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0) {
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
			errMessage = "??????????????????";
		} else if (devRPCRep.get(0) == null) {
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
			errMessage = "??????????????????";
		} else {
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1) {
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			} else {
				errMessage = "??????????????????";
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0) {
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", device_id);
				} else {
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep
							.get(0).getRpcList();
					if (rpcList != null && !rpcList.isEmpty()) {
						for (int k = 0; k < rpcList.size(); k++) {
							if ("GetParameterValuesResponse".equals(rpcList
									.get(k).getRpcName())) {
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", device_id, resp);
								Fault fault = null;
								if (resp == null || "".equals(resp)) {
									logger.debug(
											"[{}]DevRpcCmdOBJ.value == null",
											device_id);
								} else {
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML
											.CreateXML(resp));
									if (soapOBJ != null) {
										fault = XmlToRpc.Fault(soapOBJ
												.getRpcElement());
										Element element = soapOBJ
												.getRpcElement();
										if (element != null) {
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null) {
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												PingMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++) {
													PingMap.put(
															parameterValueStructArr[j]
																	.getName(),
															parameterValueStructArr[j]
																	.getValue().para_value);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::"
					+ devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			if (PingMap == null) {
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='7'><span><font color=red>"
						+ errMessage + "</font></span></td></tr>";
			} else {
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += (StringUtil
						.getIntegerValue(PingMap
								.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")) / StringUtil
						.getIntegerValue(NumberOfRepetitions))
						* 100 + "" + "%";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += Host;
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

		StringBuffer sb = new StringBuffer();
		/** ????????? **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(NumberOfRepetitions).append("</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap = getSuggested(4);
		// ?????????
		String biasValue = (String) pingSuggestMap.get("ex_bias");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		// ip??????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append("").append("</td></tr>");

		if (PingMap != null) {

			/** ???????????? **/

			String suggestRegular = (String) pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

			// ?????????
			int success_count = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount")));
			int numberOfRepetitions = Integer.parseInt(NumberOfRepetitions);
			sb.append(judgeIntValue(numberOfRepetitions, success_count, "=", 0));

			// ?????????
			int fail_count = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, fail_count, "=", 0));

			// ????????????
			int average_time = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), average_time,
					suggestRegular, 0));

			int min_time = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), min_time,
					suggestRegular, 0));

			int max_time = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), max_time,
					suggestRegular, 0));

			// ?????????
			int failure_count = (StringUtil
					.getIntegerValue(PingMap
							.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")) / StringUtil
					.getIntegerValue(NumberOfRepetitions)) * 100;
			sb.append(judgeIntValue(0, failure_count, "=", 0));

			// host
			sb.append("<td bgcolor='#FFFFFF' nowrap>");
			sb.append("").append("</td>");

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		} else {
			sb.append("<tr>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");
			sb.append("<td bgcolor='#FFFFFF' nowrap colspan=7><font color=red>");
			sb.append("??????").append("</font></td></tr>");
			serviceHtml += sb.toString() + "</table>";
		}

		return serviceHtml;
	}

	public Map getSuggested(int id) {
		PrepareSQL psql = new PrepareSQL(
				"select ex_bias, ex_regular from gw_egw_expert where id=?");
		psql.setInt(1, id);
		Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
		Map fields1 = cursor1.getNext();
		return fields1;
	}

	/**
	 * ?????????
	 *
	 * @param biasValue
	 *            ?????????
	 * @param factValue
	 *            ?????????
	 * @param regular
	 *            ????????????
	 * @param orValue
	 *            ???????????????
	 */
	public String judgeIntValue(double biasValue, double factValue,
			String regular, double orValue) {
		StringBuffer sb = new StringBuffer();
		// ??????
		if ("<".equals(regular)) {
			if (factValue < biasValue) {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			} else {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}
		// ??????
		else if (">".equals(regular)) {
			if (factValue > biasValue) {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			} else {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}
		// ??????
		else if ("=".equals(regular)) {
			if (factValue == biasValue) {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			} else {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}

		// ?????????
		else if ("<>".equals(regular)) {
			if (biasValue - orValue < factValue
					&& factValue < biasValue + orValue) {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			} else {
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		} else {
			// ???????????????????????????
			sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
			sb.append("").append("</font></td>");
		}
		return sb.toString();
	}

	/**
	 * tr069 ??? snmp ??????HttpGet ??????
	 *
	 * @author zhangcong ???67706???
	 * @param request
	 * @return
	 */
	public String allHttpGetResult(HttpServletRequest request) {
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (devicetype.equals("tr069")) {
			strResult = HttpGetList(request);
		}
		// else
		// {
		// // ??????SnmpGwCheck ??????
		// strResult = snmpHttpGet(request);
		// }
		return strResult;
	}

	/**
	 * tr069 ??? snmp ??????DNSQuery ??????
	 *
	 * @author zhangcong ???67706???
	 * @param request
	 * @return
	 */
	public String allDNSQueryResult(HttpServletRequest request) {
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (devicetype.equals("tr069")) {
			strResult = DNSQueryList(request);
		}
		// else
		// {
		// // ??????SnmpGwCheck ??????
		// strResult = snmpHttpGet(request);
		// }
		return strResult;
	}

	// /**
	// * SNMP??????HttpGet???????????????corba??????
	// *
	// * @author zhangcong ???67706???
	// * @param request
	// * @return
	// */
	// public String snmpHttpGet(HttpServletRequest request)
	// {
	// // ??????ID
	// String device_id = request.getParameter("device_id");
	// // ??????IP
	// String test_ip = request.getParameter("test_ip");
	// // ????????????ms
	// String time_out = request.getParameter("time_out");
	// logger.debug("device_id :" + device_id);
	// logger.debug("test_ip :" + test_ip);
	// logger.debug("time_out :" + time_out);
	// // ??????????????????oid ???????????????????????? ??????ping???????????????OID
	// String StrSQL =
	// "select * from tab_gw_model_oper_oid where device_model = "
	// + "(select device_model_id from tab_gw_device where device_id= '"
	// + device_id + "') and oid_type =3";
	// PrepareSQL psql = new PrepareSQL(StrSQL);
	// psql.getSQL();
	// Map oidMap = DataSetBean.getRecord(StrSQL);
	// // ??????????????????
	// StrSQL = "select *  from  sgw_security where device_id='" + device_id +
	// "'";
	// String deviceSQL =
	// "select loopback_ip,snmp_udp from tab_gw_device where device_id='"
	// + device_id + "'";
	// psql = new PrepareSQL(StrSQL);
	// psql.getSQL();
	// Map deviceMap = DataSetBean.getRecord(StrSQL);
	// psql = new PrepareSQL(deviceSQL);
	// psql.getSQL();
	// Map gwMap = DataSetBean.getRecord(deviceSQL);
	// // ???????????????????????????
	// SnmpGwCheck.PingDev setDev = new SnmpGwCheck.PingDev();
	// setDev.device_id = (String) deviceMap.get("device_id");
	// // ??????
	// setDev.loopback_ip = (String) gwMap.get("loopback_ip");
	// // oid
	// setDev.oid = (String) oidMap.get("oid");
	// // ??????????????????????????????snmp??????
	// setDev.port = Integer.parseInt((String) gwMap.get("snmp_udp"));
	// // ?????????
	// setDev.set_comm = (String) deviceMap.get("snmp_w_passwd");
	// setDev.timeout = 60;
	// setDev.value = "ADDR=" + test_ip + ";TIMES=" + time_out;
	// setDev.version = "2c";
	// logger.debug("-----------------++++++++-----------------------");
	// logger.debug("device_id=" + setDev.device_id + "-loopback_ip="
	// + setDev.loopback_ip + "-oid=" + setDev.oid);
	// logger.debug("port=" + setDev.port + "-set_comm=" + setDev.set_comm +
	// "-timeout="
	// + setDev.timeout);
	// logger.debug("value=" + setDev.value + "-version=" + setDev.version);
	// SnmpGwCheck.PingDev[] pingDev_arr = new SnmpGwCheck.PingDev[1];
	// pingDev_arr[0] = setDev;
	// // ????????????corba??????
	// SnmpGwCheck.PingData[] resultData =
	// SnmpGwCheckInterface.GetInstance().SnmpPing(
	// pingDev_arr, device_id);
	// String serviceHtml =
	// "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
	// serviceHtml += "<tr class='blue_foot'>";
	// serviceHtml +=
	// "<td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
	// serviceHtml += "????????????";
	// serviceHtml +=
	// "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
	// serviceHtml += "??????????????????(ms)";
	// serviceHtml +=
	// "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
	// serviceHtml += "???????????????(%)";
	// serviceHtml += "</td></tr>";
	// String time = "";
	// String miss = "";
	// if (resultData == null || resultData.length == 0)
	// {
	// serviceHtml += "<tr class=''>";
	// serviceHtml += "<td bgcolor='#FFFFFF' colspan='3' nowrap>??????corba?????????";
	// serviceHtml += "</td></tr>";
	// }
	// else
	// {
	// for (int i = 0; i < resultData.length; i++)
	// {
	// time = (String) resultData[i].times;
	// miss = (String) resultData[i].miss;
	// logger.debug("time :" + time);
	// logger.debug("miss :" + miss);
	// if (time == null || (time.equals("") && miss.equals("")))
	// {
	// serviceHtml +=
	// "<tr class=''><td bgcolor='#FFFFFF' width='30%' nowrap colspan='3'> ?????????????????????????????????!";
	// serviceHtml += "</td></tr>";
	// }
	// else
	// {
	// serviceHtml += "<tr class=''>";
	// serviceHtml += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
	// Map fields = DataSetBean
	// .getRecord("select loopback_ip from tab_gw_device where device_id='"
	// + resultData[i].device_id + "'");
	// serviceHtml += fields.get("loopback_ip");
	// serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
	// serviceHtml += StringUtils.formatString(time, 2);
	// serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
	// serviceHtml += StringUtils.formatString(miss, 2);
	// serviceHtml += "</td></tr>";
	// fields = null;
	// }
	// }
	// }
	// return serviceHtml;
	// }

	/**
	 * ??????Ping?????????????????? zhangcong@
	 *
	 * @param request
	 * @return
	 */
	public String HttpGetList(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		// ??????????????????
		String gw_type = request.getParameter("gw_type");
		String gather_id = "";
		String Interface = request.getParameter("Interface");

		String URL = request.getParameter("URL");
		String NumberOfRepetitions = request
				.getParameter("NumberOfRepetitions");
		String Timeout = request.getParameter("Timeout");
		String httpVersion = request.getParameter("httpVersion");
		DevRpc[] devRPCArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		osMap.put(device_id, out + "-" + SerialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[6];
		// Requested
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		// ??????????????????WAN
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		// ??????URL
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.URL");
		anyObject = new AnyObject();
		anyObject.para_value = URL;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		// ????????????
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		// ????????????
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		// HTTP??????
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.HttpVersion");
		anyObject = new AnyObject();
		anyObject.para_value = httpVersion;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		// ????????????
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0) {
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
			errMessage = "??????????????????";
		} else if (devRPCRep.get(0) == null) {
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
			errMessage = "??????????????????";
		} else {
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1) {
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			} else {
				errMessage = "??????????????????";
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0) {
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", device_id);
				} else {
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep
							.get(0).getRpcList();
					if (rpcList != null && !rpcList.isEmpty()) {
						for (int k = 0; k < rpcList.size(); k++) {
							if ("GetParameterValuesResponse".equals(rpcList
									.get(k).getRpcName())) {
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", device_id, resp);
								Fault fault = null;
								if (resp == null || "".equals(resp)) {
									logger.debug(
											"[{}]DevRpcCmdOBJ.value == null",
											device_id);
								} else {
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML
											.CreateXML(resp));
									if (soapOBJ != null) {
										fault = XmlToRpc.Fault(soapOBJ
												.getRpcElement());
										Element element = soapOBJ
												.getRpcElement();
										if (element != null) {
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null) {
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												PingMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++) {
													PingMap.put(
															parameterValueStructArr[j]
																	.getName(),
															parameterValueStructArr[j]
																	.getValue().para_value);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::"
					+ devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			// ????????????????????????????????????????????????????????????
			if (PingMap == null) {
				serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'><tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='5'><span><font color=red>"
						+ errMessage + "</font></span></td></tr></table>";

				// ????????????????????????
				return serviceHtml;
			} else {
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "????????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "?????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "?????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "??????????????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "??????????????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "??????????????????";
				serviceHtml += "</td></tr>";
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
			Map<String, String> expertMap = getSuggested(7);
			int ex_bias = -99;
			String ex_regular = "";
			if (null != expertMap) {
				ex_bias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
				ex_regular = expertMap.get("ex_regular");
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "?????????";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += NumberOfRepetitions;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += 0;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += ex_bias;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += ex_bias;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += ex_bias;
				serviceHtml += "</td></tr>";

				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "????????????";
				serviceHtml += "</td>";
				serviceHtml += judgeIntValue(
						StringUtil.getIntegerValue(NumberOfRepetitions),
						StringUtil.getIntegerValue(PingMap
								.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount")),
						"=", 0);
				serviceHtml += judgeIntValue(
						0,
						StringUtil.getIntegerValue(PingMap
								.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount")),
						"=", 0);
				serviceHtml += judgeIntValue(
						ex_bias,
						StringUtil.getIntegerValue(PingMap
								.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime")),
						ex_regular, 0);
				serviceHtml += judgeIntValue(
						ex_bias,
						StringUtil.getIntegerValue(PingMap
								.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime")),
						ex_regular, 0);
				serviceHtml += judgeIntValue(
						ex_bias,
						StringUtil.getIntegerValue(PingMap
								.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime")),
						ex_regular, 0);
				serviceHtml += "</tr>";
			} else {
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "?????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap colspan='5'>";
				serviceHtml += "<font color='red'>";
				serviceHtml += "????????????????????????";
				serviceHtml += "</font>";
				serviceHtml += "</td></tr>";

				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "????????????";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap colspan='5'>";
				serviceHtml += "<font color='red'>";
				serviceHtml += "???????????????";
				serviceHtml += "</font>";
				serviceHtml += "</td></tr>";
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * ??????Ping?????????????????? zhangcong@
	 *
	 * @param request
	 * @return
	 */
	public String DNSQueryList(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);
		String gather_id = "";
		String Interface = request.getParameter("Interface");

		String DNSServerIP = request.getParameter("DNSServerIP");
		String NumberOfRepetitions = request
				.getParameter("NumberOfRepetitions");
		String Timeout = request.getParameter("Timeout");
		String DomainName = request.getParameter("DomainName");
		DevRpc[] devRPCArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>();
		String serviceHtml = "";
		String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 ??????????????????????????????????????????????????????
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 ??????????????????????????????????????????????????????
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(device_id, out + "-" + SerialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[6];
		// Requested
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		// ??????????????????WAN
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = Interface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		// ??????URL
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DNSServerIP");
		anyObject = new AnyObject();
		anyObject.para_value = DNSServerIP;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		// ????????????
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = NumberOfRepetitions;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		// ????????????
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = Timeout;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		// HTTP??????
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DomainName");
		anyObject = new AnyObject();
		anyObject.para_value = DomainName;
		anyObject.para_type_id = "1";
		ParameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		// ????????????
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type + "");
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		serviceHtml += "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td></tr>";
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0) {
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
			errMessage = "??????????????????";
		} else if (devRPCRep.get(0) == null) {
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
			errMessage = "??????????????????";
		} else {
			int stat = devRPCRep.get(0).getStat();
			if (stat != 1) {
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			} else {
				errMessage = "??????????????????";
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0) {
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", device_id);
				} else {
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep
							.get(0).getRpcList();
					if (rpcList != null && !rpcList.isEmpty()) {
						for (int k = 0; k < rpcList.size(); k++) {
							if ("GetParameterValuesResponse".equals(rpcList
									.get(k).getRpcName())) {
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", device_id, resp);
								Fault fault = null;
								if (resp == null || "".equals(resp)) {
									logger.debug(
											"[{}]DevRpcCmdOBJ.value == null",
											device_id);
								} else {
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML
											.CreateXML(resp));
									if (soapOBJ != null) {
										fault = XmlToRpc.Fault(soapOBJ
												.getRpcElement());
										Element element = soapOBJ
												.getRpcElement();
										if (element != null) {
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null) {
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												PingMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++) {
													PingMap.put(
															parameterValueStructArr[j]
																	.getName(),
															parameterValueStructArr[j]
																	.getValue().para_value);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			logger.debug("GSJ------------------------:::" + devRPCRep.size());
			logger.debug("GSJ------------------------:::"
					+ devRPCRep.get(0).getStat());
			String device_name = osMap.get(device_id);
			if (PingMap == null) {
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "</td><td colspan='5'><span><font color=red>"
						+ errMessage + "</font></span></td></tr>";
			} else {
				logger.debug("F-----------:" + PingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += device_name;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += PingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

		StringBuffer sb = new StringBuffer();
		/** ????????? **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(NumberOfRepetitions).append("</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap = getSuggested(6);
		// ?????????
		String biasValue = (String) pingSuggestMap.get("ex_bias");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");

		// ??????????????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(biasValue).append("</td>");
		sb.append("</tr>");

		if (PingMap != null) {

			/** ???????????? **/
			String suggestRegular = (String) pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

			// ?????????
			int success_count = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount")));
			int numberOfRepetitions = Integer.parseInt(NumberOfRepetitions);
			sb.append(judgeIntValue(numberOfRepetitions, success_count, "=", 0));

			// ?????????
			int fail_count = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, fail_count, "=", 0));

			// ????????????
			int average_time = Integer
					.valueOf(String.valueOf(PingMap
							.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), average_time,
					suggestRegular, 0));

			String _tempMinTime = String
					.valueOf(PingMap
							.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime"));
			int min_time = 0;
			if (!(null == _tempMinTime || "null".equals(_tempMinTime))) {
				min_time = Integer.valueOf(_tempMinTime);
			}
			sb.append(judgeIntValue(Integer.parseInt(biasValue), min_time,
					suggestRegular, 0));

			String _tempMaxTime = String
					.valueOf(PingMap
							.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime"));
			int max_time = 0;
			if (!(null == _tempMaxTime || "null".equals(_tempMaxTime))) {
				max_time = Integer.valueOf(_tempMaxTime);
			}
			sb.append(judgeIntValue(Integer.parseInt(biasValue), max_time,
					suggestRegular, 0));

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		} else {
			sb.append("<tr>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");
			sb.append("<td bgcolor='#FFFFFF' nowrap colspan=5><font color=red>");
			sb.append("??????").append("</font></td></tr>");
			serviceHtml += sb.toString() + "</table>";
		}
		return serviceHtml;
	}

	/**
	 * ??????ATMF5LOOP?????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @return
	 */
	public String ATMF5LOOPList(HttpServletRequest request) {
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		String device_id = request.getParameter("device_id");

		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);

		String[] arrDevice_id = device_id.split(",");
		String gather_id = "";
		String _param = request.getParameter("ATMF5");
		String NumberOfRepetions = request.getParameter("NumberOfRepetions");
		String Timeout = request.getParameter("Timeout");
		DevRpc[] devRPCArr = new DevRpc[arrDevice_id.length];
		for (int i = 0; i < arrDevice_id.length; i++) {
			String strSQL1 = "select oui, device_serialnumber, gather_id, loopback_ip, cr_port, cr_path, acs_username, acs_passwd "
					+ " from tab_gw_device where device_id='"
					+ arrDevice_id[i] + "'";
			PrepareSQL psql = new PrepareSQL(strSQL1);
			Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
			Map fields1 = cursor1.getNext();
			// begin add by w5221 ??????????????????????????????????????????????????????
			gather_id = (String) fields1.get("gather_id");
			// end add by w5221 ??????????????????????????????????????????????????????
			String out = (String) fields1.get("oui");
			String SerialNumber = (String) fields1.get("device_serialnumber");
			String ip = (String) fields1.get("loopback_ip");
			String Port = (String) fields1.get("cr_port");
			String path = (String) fields1.get("cr_path");
			String username = (String) fields1.get("acs_username");
			String passwd = (String) fields1.get("acs_passwd");
			AnyObject anyObject = new AnyObject();
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[3];
			ParameterValueStruct[0] = new ParameterValueStruct();
			ParameterValueStruct[0].setName(_param
					+ "WANATMF5LoopbackDiagnostics.DiagnosticsState");
			anyObject.para_value = "Requested";
			anyObject.para_type_id = "1";
			ParameterValueStruct[0].setValue(anyObject);
			ParameterValueStruct[1] = new ParameterValueStruct();
			ParameterValueStruct[1].setName(_param
					+ "WANATMF5LoopbackDiagnostics.NumberOfRepetitions");
			anyObject = new AnyObject();
			anyObject.para_value = NumberOfRepetions;
			anyObject.para_type_id = "3";
			ParameterValueStruct[1].setValue(anyObject);
			ParameterValueStruct[2] = new ParameterValueStruct();
			ParameterValueStruct[2].setName(_param
					+ "WANATMF5LoopbackDiagnostics.Timeout");
			anyObject = new AnyObject();
			anyObject.para_value = Timeout;
			anyObject.para_type_id = "3";
			ParameterValueStruct[2].setValue(anyObject);
			setParameterValues.setParameterList(ParameterValueStruct);
			setParameterValues.setParameterKey("ATMF5LOOP");
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[5];
			parameterNamesArr[0] = _param
					+ "WANATMF5LoopbackDiagnostics.SuccessCount";
			parameterNamesArr[1] = _param
					+ "WANATMF5LoopbackDiagnostics.FailureCount";
			parameterNamesArr[2] = _param
					+ "WANATMF5LoopbackDiagnostics.AverageResponseTime";
			parameterNamesArr[3] = _param
					+ "WANATMF5LoopbackDiagnostics.MinimumResponseTime";
			parameterNamesArr[4] = _param
					+ "WANATMF5LoopbackDiagnostics.MaximumResponseTime";
			getParameterValues.setParameterNames(parameterNamesArr);
			devRPCArr[i] = new DevRpc();
			devRPCArr[i].devId = device_id;
			Rpc[] rpcArr = new Rpc[2];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "SetParameterValues";
			rpcArr[0].rpcValue = setParameterValues.toRPC();
			rpcArr[1] = new Rpc();
			rpcArr[1].rpcId = "2";
			rpcArr[1].rpcName = "GetParameterValues";
			rpcArr[1].rpcValue = getParameterValues.toRPC();
			devRPCArr[i].rpcArr = rpcArr;
		}
		// corba
		List<DevRpcCmdOBJ> devRpcCmdOBJList = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRpcCmdOBJList = devRPCManager
				.execRPC(devRPCArr, Global.DiagCmd_Type);
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "?????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "??????????????????";
		serviceHtml += "</td></tr>";
		for (int j = 0; devRpcCmdOBJList != null && j < devRpcCmdOBJList.size(); j++) {
			Map PingMap = null;
			ParameterValueStruct[] pingStruct = null;
			DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(j);
			if (devRpcCmdOBJ != null) {
				String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
						+ devRpcCmdOBJ.getDevId() + "'";
				PrepareSQL psql = new PrepareSQL(strSQL1);
				psql.getSQL();
				Cursor cursor1 = DataSetBean.getCursor(strSQL1);
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String SerialNumber = (String) fields1
						.get("device_serialnumber");
				String device_name = out + "-" + SerialNumber;
				String errMessage = "";
				int stat = devRpcCmdOBJ.getStat();
				if (stat != 1) {
					errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				} else {
					errMessage = "??????????????????";
					if (devRpcCmdOBJ.getRpcList() == null
							|| devRpcCmdOBJ.getRpcList().size() == 0) {
						logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????",
								devRpcCmdOBJ.getDevId());
					} else {
						List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcCmdOBJ
								.getRpcList();
						if (rpcList != null && !rpcList.isEmpty()) {
							for (int k = 0; k < rpcList.size(); k++) {
								if ("GetParameterValuesResponse".equals(rpcList
										.get(k).getRpcName())) {
									String resp = rpcList.get(k).getValue();
									logger.warn("[{}]???????????????{}",
											devRpcCmdOBJ.getDevId(), resp);
									Fault fault = null;
									if (resp == null || "".equals(resp)) {
										logger.debug(
												"[{}]DevRpcCmdOBJ.value == null",
												devRpcCmdOBJ.getDevId());
									} else {
										SoapOBJ soapOBJ = XML.getSoabOBJ(XML
												.CreateXML(resp));
										if (soapOBJ != null) {
											Element element = soapOBJ
													.getRpcElement();
											if (element != null) {
												GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
														.GetParameterValuesResponse(element);
												if (getParameterValuesResponse != null) {
													pingStruct = new ParameterValueStruct[5];
													pingStruct = getParameterValuesResponse
															.getParameterList();
													for (int l = 0; l < pingStruct.length; l++) {
														PingMap.put(
																pingStruct[l]
																		.getName(),
																pingStruct[l]
																		.getValue().para_value);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (null == pingStruct || PingMap == null) {
					serviceHtml += "<tr bgcolor='#FFFFFF'>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "</td><td colspan='5'><span>" + errMessage
							+ "</span></td></tr>";
				} else {
					serviceHtml += "<tr class='blue_foot'>";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += PingMap.get(_param
							+ "WANATMF5LoopbackDiagnostics.SuccessCount");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += PingMap.get(_param
							+ "WANATMF5LoopbackDiagnostics.FailureCount");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += PingMap
							.get(_param
									+ "WANATMF5LoopbackDiagnostics.AverageResponseTime");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += PingMap
							.get(_param
									+ "WANATMF5LoopbackDiagnostics.MinimumResponseTime");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += PingMap
							.get(_param
									+ "WANATMF5LoopbackDiagnostics.MaximumResponseTime");
					serviceHtml += "</td></tr>";
				}
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * ?????????????????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 *            request
	 * @param gw_type
	 *            gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public Map ChongqiList(HttpServletRequest request, String gw_type) {
		logger.debug("FileSevice=>ChongqiList({}) ", gw_type);
		HashMap resultMap = new HashMap();
		String deviceIds = request.getParameter("device_id");
		String[] arrDevice_id = deviceIds.split(",");
		DevRpc[] devRPCArr = new DevRpc[arrDevice_id.length];
		for (int i = 0; i < devRPCArr.length; i++) {
			Reboot reboot = new Reboot();
			reboot.setCommandKey("Reboot");
			devRPCArr[i] = new DevRpc();
			devRPCArr[i].devId = arrDevice_id[i];
			Rpc[] rpcArr = new Rpc[1];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "Reboot";
			rpcArr[0].rpcValue = reboot.toRPC();
			devRPCArr[i].rpcArr = rpcArr;
		}
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = devRPCManager.execRPC(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0) {
			logger.warn("List<DevRpcCmdOBJ>???????????????");
		}
		for (int j = 0; j < devRpcCmdOBJList.size(); j++) {
			DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(j);
			if (devRpcCmdOBJ != null) {
				String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
						+ devRpcCmdOBJ.getDevId() + "'";
				PrepareSQL psql = new PrepareSQL(strSQL1);
				Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String SerialNumber = (String) fields1
						.get("device_serialnumber");
				int flag = devRpcCmdOBJ.getStat();
				if (flag == 1) {
					resultMap.put(out + "-" + SerialNumber, "????????????");
				} else {
					resultMap.put(out + "-" + SerialNumber, "????????????");
				}
			}
		}
		return resultMap;
	}

	/**
	 * ?????????????????????????????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @param gw_type
	 *            gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public Map HuifuList(HttpServletRequest request, String gw_type) {
		HashMap resultMap = new HashMap();
		String device_id = request.getParameter("device_id");
		String[] arrDevice_id = device_id.split(",");
		DevRpc[] devRPCArr = new DevRpc[arrDevice_id.length];
		for (int i = 0; i < devRPCArr.length; i++) {
			FactoryReset factoryReset = new FactoryReset();
			devRPCArr[i] = new DevRpc();
			devRPCArr[i].devId = arrDevice_id[i];
			Rpc[] rpcArr = new Rpc[1];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "FactoryReset";
			rpcArr[0].rpcValue = factoryReset.toRPC();
			devRPCArr[i].rpcArr = rpcArr;
		}
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = devRPCManager.execRPC(devRPCArr,
				Global.RpcCmd_Type);
		if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0) {
			logger.warn("List<DevRpcCmdOBJ>???????????????");
		}
		for (int j = 0; j < devRpcCmdOBJList.size(); j++) {
			DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(j);
			if (devRpcCmdOBJ != null) {
				String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
						+ devRpcCmdOBJ.getDevId() + "'";
				PrepareSQL psql = new PrepareSQL(strSQL1);
				Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String SerialNumber = (String) fields1
						.get("device_serialnumber");
				int flag = devRpcCmdOBJ.getStat();
				if (flag == 1) {
					resultMap.put(out + "-" + SerialNumber, "????????????????????????");
				} else {
					resultMap.put(out + "-" + SerialNumber, "????????????????????????");
				}
			}
		}
		return resultMap;
	}

	/**
	 * ??????DSL?????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @param gw_type
	 *            gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public String DSLList(HttpServletRequest request, String gw_type) {
		String device_id = request.getParameter("device_id");
		String dslWan = request.getParameter("dslWan");
		String[] arrDevice_id = device_id.split(",");
		DevRpc[] devRPCArr = new DevRpc[arrDevice_id.length];
		logger.debug("LZJ>>>>> arrDevice_id.length :" + arrDevice_id.length);
		for (int i = 0; i < arrDevice_id.length; i++) {
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[1];
			ParameterValueStruct[0] = new ParameterValueStruct();
			ParameterValueStruct[0].setName(dslWan
					+ "WANDSLDiagnostics.LoopDiagnosticsState");
			AnyObject anyObject = new AnyObject();
			anyObject.para_value = "Requested";
			anyObject.para_type_id = "1";
			ParameterValueStruct[0].setValue(anyObject);
			setParameterValues.setParameterList(ParameterValueStruct);
			setParameterValues.setParameterKey("DSL");
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[10];
			parameterNamesArr[0] = dslWan + "WANDSLDiagnostics.ACTPSDds";
			parameterNamesArr[1] = dslWan + "WANDSLDiagnostics.ACTPSDus";
			parameterNamesArr[2] = dslWan + "WANDSLDiagnostics.ACTATPds";
			parameterNamesArr[3] = dslWan + "WANDSLDiagnostics.ACTATPus";
			parameterNamesArr[4] = dslWan + "WANDSLDiagnostics.HLINSCds";
			parameterNamesArr[5] = dslWan + "WANDSLDiagnostics.HLINpsds";
			parameterNamesArr[6] = dslWan + "WANDSLDiagnostics.QLNpsds";
			parameterNamesArr[7] = dslWan + "WANDSLDiagnostics.SNRpsds";
			parameterNamesArr[8] = dslWan + "WANDSLDiagnostics.BITSpsds";
			parameterNamesArr[9] = dslWan + "WANDSLDiagnostics.GAINSpsds";
			getParameterValues.setParameterNames(parameterNamesArr);
			devRPCArr[i] = new DevRpc();
			devRPCArr[i].devId = device_id;
			Rpc[] rpcArr = new Rpc[2];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "SetParameterValues";
			rpcArr[0].rpcValue = setParameterValues.toRPC();
			rpcArr[1] = new Rpc();
			rpcArr[1].rpcId = "2";
			rpcArr[1].rpcName = "GetParameterValues";
			rpcArr[1].rpcValue = getParameterValues.toRPC();
			devRPCArr[i].rpcArr = rpcArr;
		}
		// corba
		List<DevRpcCmdOBJ> devRpcCmdOBJList = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRpcCmdOBJList = devRPCManager
				.execRPC(devRPCArr, Global.DiagCmd_Type);
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		serviceHtml += "<tr class='blue_foot'>";
		serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "????????????";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTPSDds(??????????????????)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTPSDus(??????????????????)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTATPds(DSL????????????)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "ACTATPus(DSL????????????)";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "HLINSCds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "HLINpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "QLNpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "SNRpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "BITSpsds";
		serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
		serviceHtml += "GAINSpsds";
		serviceHtml += "</td></tr>";
		logger.debug("devRpcCmdOBJList.size--------------2222222222"
				+ devRpcCmdOBJList.size());

		StringBuffer sb = new StringBuffer();
		for (int j = 0; devRpcCmdOBJList != null && j < devRpcCmdOBJList.size(); j++) {
			Map DSLMap = null;
			ParameterValueStruct[] DSLStruct = null;
			DevRpcCmdOBJ devRpcCmdOBJ = devRpcCmdOBJList.get(j);
			if (devRpcCmdOBJ != null) {
				String strSQL1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
						+ devRpcCmdOBJ.getDevId() + "'";
				PrepareSQL psql = new PrepareSQL(strSQL1);
				psql.getSQL();
				Cursor cursor1 = DataSetBean.getCursor(strSQL1);
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String SerialNumber = (String) fields1
						.get("device_serialnumber");
				String device_name = out + "-" + SerialNumber;
				String errMessage = "";
				int stat = devRpcCmdOBJ.getStat();
				if (stat != 1) {
					errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				} else {
					errMessage = "??????????????????";
					if (devRpcCmdOBJ.getRpcList() == null
							|| devRpcCmdOBJ.getRpcList().size() == 0) {
						logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????",
								devRpcCmdOBJ.getDevId());
					} else {
						List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcCmdOBJ
								.getRpcList();
						if (rpcList != null && !rpcList.isEmpty()) {
							for (int k = 0; k < rpcList.size(); k++) {
								if ("GetParameterValuesResponse".equals(rpcList
										.get(k).getRpcName())) {
									String resp = rpcList.get(k).getValue();
									logger.warn("[{}]???????????????{}",
											devRpcCmdOBJ.getDevId(), resp);
									Fault fault = null;
									if (resp == null || "".equals(resp)) {
										logger.debug(
												"[{}]DevRpcCmdOBJ.value == null",
												devRpcCmdOBJ.getDevId());
									} else {
										SoapOBJ soapOBJ = XML.getSoabOBJ(XML
												.CreateXML(resp));
										if (soapOBJ != null) {
											Element element = soapOBJ
													.getRpcElement();
											if (element != null) {
												GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
														.GetParameterValuesResponse(element);
												if (getParameterValuesResponse != null) {
													DSLStruct = getParameterValuesResponse
															.getParameterList();
													DSLMap = new HashMap();
													for (int l = 0; l < DSLStruct.length; l++) {
														DSLMap.put(
																DSLStruct[l]
																		.getName(),
																DSLStruct[l]
																		.getValue().para_value
																		.replaceAll(
																				",",
																				",<br>"));
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (null == DSLStruct || DSLMap == null) {
					serviceHtml += "<tr bgcolor='#FFFFFF'>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "</td><td colspan='10'><span><font color=red>"
							+ errMessage + "</font></span></td></tr>";
				} else {
					serviceHtml += "<tr class='blue_foot'>";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += device_name;
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINSCds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.QLNpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.SNRpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.BITSpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += DSLMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.GAINSpsds");
					serviceHtml += "</td></tr>";
				}

				// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

				/** ????????? **/
				sb.append("<tr class='blue_foot'>");
				sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");

				Map ACTPSDds_map = getSuggested(8);
				Map ACTPSDus_map = getSuggested(9);
				Map ACTATPds_map = getSuggested(10);
				Map ACTATPus_map = getSuggested(11);

				double ACTPSDds = Double.valueOf(StringUtil
						.getStringValue(ACTPSDds_map.get("ex_bias")));
				double ACTPSDds_orValue = Double.valueOf(StringUtil
						.getStringValue(ACTPSDds_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(
						ACTPSDds - ACTPSDds_orValue + " - "
								+ Double.valueOf(ACTPSDds + ACTPSDds_orValue))
						.append("</td>");

				double ACTPSDus = Double.valueOf(StringUtil
						.getStringValue(ACTPSDus_map.get("ex_bias")));
				double ACTPSDus_orValue = Double.valueOf(StringUtil
						.getStringValue(ACTPSDus_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(
						ACTPSDus - ACTPSDus_orValue + " - "
								+ Double.valueOf(ACTPSDds + ACTPSDds_orValue))
						.append("</td>");

				double ACTATPds = Double.valueOf(StringUtil
						.getStringValue(ACTATPds_map.get("ex_bias")));
				double ACTATPds_orValue = Double.valueOf(StringUtil
						.getStringValue(ACTATPds_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(
						ACTATPds - ACTATPds_orValue + " - "
								+ Double.valueOf(ACTATPds + ACTATPds_orValue))
						.append("</td>");

				double ACTATPus = Double.valueOf(StringUtil
						.getStringValue(ACTATPus_map.get("ex_bias")));
				double ACTATPus_orValue = Double.valueOf(StringUtil
						.getStringValue(ACTATPus_map.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(
						ACTATPus - ACTATPus_orValue + " - "
								+ Double.valueOf(ACTATPus + ACTATPus_orValue))
						.append("</td>");

				// ?????????????????????null
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("</tr>");

				if (DSLMap != null) {
					// ????????????
					sb.append("<tr class='blue_foot'>");
					sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

					double ACTPSDds_fact = Double
							.valueOf(StringUtil.getStringValue(DSLMap
									.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds")));
					String ACTPSDds_regular = StringUtil
							.getStringValue(ACTPSDds_map.get("ex_regular"));
					sb.append(judgeIntValue(ACTPSDds, ACTPSDds_fact,
							ACTPSDds_regular, ACTPSDds_orValue));

					double ACTPSDus_fact = Double
							.valueOf(StringUtil.getStringValue(DSLMap
									.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus")));
					String ACTPSDus_regular = StringUtil
							.getStringValue(ACTPSDus_map.get("ex_regular"));
					sb.append(judgeIntValue(ACTPSDus, ACTPSDus_fact,
							ACTPSDus_regular, ACTPSDus_orValue));

					double ACTATPds_fact = Double
							.valueOf(StringUtil.getStringValue(DSLMap
									.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds")));
					String ACTATPds_regular = StringUtil
							.getStringValue(ACTATPds_map.get("ex_regular"));
					sb.append(judgeIntValue(ACTATPds, ACTATPds_fact,
							ACTATPds_regular, ACTATPds_orValue));

					double ACTATPus_fact = Double
							.valueOf(StringUtil.getStringValue(DSLMap
									.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus")));
					String ACTATPus_regular = StringUtil
							.getStringValue(ACTATPus_map.get("ex_regular"));
					sb.append(judgeIntValue(ACTATPus, ACTATPus_fact,
							ACTATPus_regular, ACTATPus_orValue));

					// ?????????????????????null
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
					sb.append("</tr>");
				}

				else {
					sb.append("<tr>");
					sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");
					sb.append("<td bgcolor='#FFFFFF' nowrap colspan=10><font color=red>");
					sb.append("??????").append("</font></td></tr>");
				}
				// end add
			}
		}
		serviceHtml += sb.toString() + "</table>";
		return serviceHtml;
	}

	/**
	 * ???????????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @return
	 */
	public String fileAct(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";
		ArrayList Str_sql = new ArrayList();
		String service_id = request.getParameter("lg_id");
		String strAction = request.getParameter("action");
		if (strAction.equals("delete")) {
			strSQL = "delete from tab_service  where service_id=" + service_id
					+ "";
			strSQL += " delete from tab_servicecode  where service_id="
					+ service_id + "";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			Str_sql.add(strSQL);
		} else {
			String str_filename = request.getParameter("lg_name");
			String str_lg_desc = request.getParameter("lg_desc");
			if (strAction.equals("add")) {
				// ??????????????????
				String tmpSql = "select count(*) num from tab_service where service_name='"
						+ str_filename + "'";
				PrepareSQL psql = new PrepareSQL(tmpSql);
				psql.getSQL();
				Map map = DataSetBean.getRecord(tmpSql);
				if (map != null && StringUtil.getIntValue(map, "num") > 0) {
					strMsg = "????????????\"" + str_filename + "\"??????????????????????????????????????????";
				} else {
					long service_id1;
					if (DataSetBean.getMaxId("tab_service", "service_id") < 1000) {
						service_id1 = 1001;
					} else {
						service_id1 = DataSetBean.getMaxId("tab_service",
								"service_id");
					}
					strSQL = "insert into tab_service(service_id,service_name,service_desc) values("
							+ service_id1
							+ ",'"
							+ str_filename
							+ "','"
							+ str_lg_desc + "')";
					Str_sql.add(strSQL);
					psql = new PrepareSQL(strSQL);
					psql.getSQL();
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",)", ",null)");
				}
			} else {
				// ????????????
				// ??????????????????
				String Sql = "select count(*) num from tab_service where service_name='"
						+ str_filename + "' and service_id=" + service_id + "";
				PrepareSQL psql = new PrepareSQL(Sql);
				psql.getSQL();
				Map map1 = DataSetBean.getRecord(Sql);
				if (map1 != null && StringUtil.getIntValue(map1, "num") > 0) {
					strSQL = "update tab_service set service_name='"
							+ str_filename + "',service_desc='" + str_lg_desc
							+ "' where service_id=" + service_id + "";
					strSQL = StringUtils.replace(strSQL, "=,", "=null,");
					strSQL = StringUtils.replace(strSQL, "= where",
							"=null where");
					psql = new PrepareSQL(strSQL);
					psql.getSQL();
					Str_sql.add(strSQL);
				} else {
					String tmpSql = "select count(*) num from tab_service where service_name='"
							+ str_filename + "'";
					psql = new PrepareSQL(tmpSql);
					psql.getSQL();
					Map map = DataSetBean.getRecord(tmpSql);
					if (map != null && StringUtil.getIntValue(map, "num") > 0) {
						strMsg = "????????????\"" + str_filename + "\"??????????????????????????????????????????";
					} else {
						strSQL = "update tab_service set service_name='"
								+ str_filename + "',service_desc='"
								+ str_lg_desc + "' where service_id="
								+ service_id + "";
						strSQL = StringUtils.replace(strSQL, "=,", "=null,");
						strSQL = StringUtils.replace(strSQL, "= where",
								"=null where");
						psql = new PrepareSQL(strSQL);
						psql.getSQL();
						Str_sql.add(strSQL);
					}
				}
			}
		}
		if (strMsg == null || !strSQL.equals("")) {
			// logger.debug("ssssssssssssssss>");
			int iCode[] = DataSetBean.doBatch(Str_sql);
			if (iCode != null && iCode.length > 0) {
				strMsg = "?????????????????????";
			} else {
				strMsg = "??????????????????????????????????????????????????????";
			}
		}
		return strMsg;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param name
	 * @return
	 */
	public boolean checkname1(String name) {
		String tmpSql = "select count(*) num from tab_servicecode where servicecode='"
				+ name + "'";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(tmpSql);
		if (map != null && StringUtil.getIntValue(map, "num") > 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param service_id
	 * @param str_drt_direction
	 * @param str_template_name
	 * @return
	 */
	public String checkname2(String service_id, String str_drt_direction,
			String str_template_name) {
		String str = "";
		String tmpSql = "select a.template_id,a.devicetype_id,b.template_name,e.device_model "
				+ " from tab_servicecode a,tab_template b,tab_devicetype_info c,tab_service d,gw_device_model e"
				+ " where a.service_id="
				+ service_id
				+ " and a.devicetype_id=c.devicetype_id and a.devicetype_id="
				+ str_drt_direction
				+ " and a.template_id="
				+ str_template_name
				+ " and a.template_id=b.template_id and c.devicetype_id=b.devicetype_id "
				+ " and c.device_model_id=e.device_model_id ";// TODO wait (more table related)
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(tmpSql);
		if (map != null) {
			str = map.get("device_model") + " / " + map.get("template_name");
		}
		return str;
	}

	public String YewuCode(HttpServletRequest request)
			throws UnsupportedEncodingException {
		String strSQL = "";
		String strMsg = "";
		String service_id = request.getParameter("lg_id");
		String strAction = request.getParameter("action");
		if (strAction.equals("delete")) {
			String drt_name1 = new String(request.getParameter("drt_name1")
					.getBytes());
			drt_name1 = java.net.URLDecoder.decode(drt_name1, "GBK");
			strSQL = "delete from tab_servicecode  where servicecode='"
					+ drt_name1 + "'";
		} else {
			String str_drt_name = request.getParameter("drt_name");
			String str_drt_direction = request.getParameter("devicetype_id");
			String str_template_name = request.getParameter("template_id");
			String qwe = request.getParameter("qwe");
			String cityList = request.getParameter("cityList");
			if (strAction.equals("add")) {
				// ??????????????????
				boolean name = this.checkname1(str_drt_name);
				String name1 = this.checkname2(service_id, str_drt_direction,
						str_template_name);
				if (name == false) {
					strMsg = "??????????????????\"" + str_drt_name + "\"????????????????????????????????????????????????";
				} else if (!name1.equals("") && name1 != null) {
					strMsg = "???????????????????????????" + name1 + "?????????????????????????????????????????????????????????";
				} else {
					strSQL = "insert into tab_servicecode(servicecode,service_id,template_id,devicetype_id,citylist) values('"
							+ str_drt_name
							+ "',"
							+ service_id
							+ ","
							+ str_template_name
							+ ","
							+ str_drt_direction
							+ ",'" + cityList + "')";
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",,", ",null,");
					strSQL = StringUtils.replace(strSQL, ",)", ",null)");
				}
			} else {
				// ????????????
				strSQL = "update tab_servicecode set template_id="
						+ str_template_name + ", devicetype_id="
						+ str_drt_direction + ", citylist='" + cityList
						+ "' where servicecode='" + str_drt_name + "'";
				strSQL = StringUtils.replace(strSQL, "=,", "=null,");
				strSQL = StringUtils.replace(strSQL, "= where", "=null where");
				// logger.debug("strSQLliiiiiii"+strSQL);
			}
		}
		if (strMsg == null || !strSQL.equals("")) {
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			int iCode = DataSetBean.executeUpdate(strSQL);
			if (iCode > 0) {
				strMsg = "???????????????????????????";
			} else {
				strMsg = "????????????????????????????????????????????????????????????";
			}
		}
		return strMsg;
	}

	/**
	 * ?????????????????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getCodeHtml(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Map map = this.getTemplateMap();
		Map Servicelist = this.getServiceMap();
		Map Devicelist = this.getDevicelistMap();
		String str_service_id = request.getParameter("lg_id");
		// logger.debug("str_service_id===========liuli"+str_service_id);
		String strSQL = "select servicecode,service_id,template_id,devicetype_id,citylist from tab_servicecode where  service_id="
				+ str_service_id + "";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getPageBar("&lg_id=" + str_service_id);
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null) {
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=6 HEIGHT=30 class=column>??????????????????????????????</TD></TR>";
		} else {
			while (fields != null) {
				// String Service_name =(String)( Servicelist.get((String)
				// fields.get("service_id")));
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='center'>"
						+ Servicelist.get((String) fields.get("service_id"))
						+ "</TD>";
				strData += "<TD align='center'>"
						+ (String) fields.get("servicecode") + "</TD>";
				String DEvice = "";
				String DEtemplate = "";
				if (Devicelist.get((String) fields.get("devicetype_id")) == null) {
					DEvice = "";
				} else {
					DEvice = (String) (Devicelist.get((String) fields
							.get("devicetype_id")));
				}
				strData += "<TD align='center'>" + DEvice + "</TD>";
				if (map.get((String) fields.get("template_id")) == null) {
					DEtemplate = "";
				} else {
					DEtemplate = (String) (map.get((String) fields
							.get("template_id")));
				}
				strData += "<TD align='center'>" + DEtemplate + "</TD>";
				// ????????????
				String cityList = (String) fields.get("citylist");
				String cityListDesc = "";
				String[] cityArr = cityList.split(";");
				Map cityMap = CommonMap.getCityMap();
				for (int i = 0; i < cityArr.length; i++) {
					if (cityArr[i] != null && !"".equals(cityArr[i])) {
						if ("".equals(cityListDesc)) {
							cityListDesc = (String) cityMap.get(cityArr[i]);
						} else {
							cityListDesc = cityListDesc + "???"
									+ (String) cityMap.get(cityArr[i]);
						}
					}
				}
				strData += "<TD align='center'>" + cityListDesc + "</TD>";
				String tmpCode = (String) fields.get("servicecode");
				// String tmpCode = new
				// String(((String)fields.get("servicecode")).getBytes());
				strData += "<TD width=250 align='center'><A HREF='#' onclick=editCode('"
						+ tmpCode
						+ "','"
						+ (String) fields.get("template_id")
						+ "','"
						+ (String) fields.get("devicetype_id")
						+ "','"
						+ cityList
						+ "')>??????</A> | <A HREF='#'"
						+ "  onclick=delWarn('" + tmpCode + "')>??????</A></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=6 align=right>"
					+ strBar + "</TD></TR>";
		}
		fields = null;
		return strData;
	}

	/**
	 * ?????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 *            -HttpServletRequest
	 */
	public String getHtml(HttpServletRequest request) {
		String strSQL = "select service_id,service_name,service_desc from tab_service where flag=-1";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null) {
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=4 HEIGHT=30 class=column>????????????????????????</TD></TR>";
		} else {
			while (fields != null) {
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_id") + "</TD>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_name") + "</TD>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_desc") + "</TD>";
				strData += "<TD width=250 align='center'><A HREF=\"javascript:Edit('"
						+ (String) fields.get("service_id")
						+ "','"
						+ (String) fields.get("service_name")
						+ "','"
						+ (String) fields.get("service_desc")
						+ "');\">??????</A> | <A HREF=jt_sevice_from_save.jsp?action=delete&lg_id="
						+ (String) fields.get("service_id")
						+ " onclick='return delWarn();'>??????</A>|<a href='jt_yewu_code.jsp?lg_id="
						+ (String) fields.get("service_id")
						+ "'>??????????????????</a></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right>"
					+ strBar + "</TD></TR>";
		}
		fields = null;
		return strData;
	}

	/**
	 * ??????????????????. liuli@lianchuang.com
	 *
	 * @param request
	 * @return
	 */
	public String getHtml1(HttpServletRequest request) {
		String strSQL = "select service_id,service_name,service_desc from tab_service where flag>=0";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null) {
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=4 HEIGHT=30 class=column>??????????????????????????????</TD></TR>";
		} else {
			while (fields != null) {
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_id") + "</TD>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_name") + "</TD>";
				strData += "<TD align='left'>"
						+ (String) fields.get("service_desc") + "</TD>";
				strData += "<TD width=250 align='center'><a href='jt_yewu_code.jsp?lg_id="
						+ (String) fields.get("service_id")
						+ "'>??????????????????</a></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right>"
					+ strBar + "</TD></TR>";
		}
		fields = null;
		return strData;
	}

	/**
	 * @author wangsenbo
	 * @date Apr 21, 2011
	 * @param
	 * @return int
	 */
	public int testConnection(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");

		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);

		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0) {
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", device_id);
		} else if (devRPCRep.get(0) == null) {
			logger.warn("[{}]DevRpcCmdOBJ???????????????", device_id);
		} else {
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}

	/**
	 * ping??????????????????
	 *
	 * @param device_id
	 * @return
	 */
	public String getPingInterface(String device_id, String gw_type) {
		logger.warn("getCUCPingInterface({},{},{})");
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type);
		// String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String _interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
		name = "Lan(IPInterface):1";
		value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		logger.debug("LAN???(IPInterface)???" + name + "=" + value);
		_interList += "<option selected value='" + value + "'>" + name
				+ "</option>";
		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(device_id, ConstantClass.GATHER_WAN, 1);

		logger.warn("??????????????????=====" + irt);
		String errorMsg = "";
		if (irt != 1) {
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
			// _interList += "<option value='" + value + "'>" + name +
			// "</option>";
		} else {
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if (Global.GW_TYPE_ITMS.equals(gw_type)) {
				wanConnIds = getWanConnIds(device_id);
			} else {
				wanConnIds = getWanConnIds_bbms(device_id);
			}
			if (wanConnIds == null || wanConnIds.isEmpty()) {
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
				// _interList += "<option value='" + value + "'>" + name +
				// "</option>";
			} else {
				flag = true;
				for (Map map : wanConnIds) {
					String wan_conn_id = StringUtil.getStringValue(map
							.get("wan_conn_id"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map
							.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map
							.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map
							.get("serv_list"));
					String tmp = "";
					if (vlanid == null || vlanid.equals("")
							|| vlanid.equals("NULL")) {
						tmp = "pvc:" + pvc;
					} else {
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1")) {
						name = "Wan(" + "WANPPPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANPPPConnection." + wan_conn_sess_id + ".";
					} else if (sessType.equals("2")) {
						name = "Wan(" + "WANIPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANIPConnection." + wan_conn_sess_id + ".";
					} else {
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					_interList += "<option value='" + value + "'>" + name
							+ "</option>";
				}
			}
		}
		// 3?????????listBox
		_interList += "</select>";
		if (!flag) {
			_interList = errorMsg;
		}
		logger.warn("_interList-----" + _interList);
		return _interList;
	}

	/**
	 * HttpGet??????????????????
	 *
	 * @param device_id
	 * @return
	 */
	public String getHttpGetInterface(String device_id, String gw_type) {
		logger.debug("getHttpGetInterface({})", device_id);
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type);
		// String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		// ?????????WAN???
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String _interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
		name = "Lan(IPInterface):1";
		value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		logger.debug("LAN???(IPInterface)???" + name + "=" + value);
		_interList += "<option selected value='" + value + "'>" + name
				+ "</option>";
		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(device_id, ConstantClass.GATHER_WAN, 1);
		String errorMsg = "";
		logger.warn("??????????????????=====" + irt);
		if (irt != 1) {
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
		} else {
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if (Global.GW_TYPE_ITMS.equals(gw_type)) {
				wanConnIds = getWanConnIds(device_id);
			} else {
				wanConnIds = getWanConnIds_bbms(device_id);
			}
			if (wanConnIds == null || wanConnIds.isEmpty()) {
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
			} else {
				flag = true;
				for (Map map : wanConnIds) {
					String wan_conn_id = StringUtil.getStringValue(map
							.get("wan_conn_id"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map
							.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map
							.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map
							.get("serv_list"));
					String tmp = "";
					if (vlanid == null || vlanid.equals("")
							|| vlanid.equals("NULL")) {
						tmp = "pvc:" + pvc;
					} else {
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1")) {
						name = "Wan(" + "WANPPPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANPPPConnection." + wan_conn_sess_id + ".";
					} else if (sessType.equals("2")) {
						name = "Wan(" + "WANIPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANIPConnection." + wan_conn_sess_id + ".";
					} else {
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					_interList += "<option value='" + value + "'>" + name
							+ "</option>";
				}
			}
		}
		// 3?????????listBox
		_interList += "</select>";
		if (!flag) {
			_interList = errorMsg;
		}
		logger.warn("_interList-----" + _interList);
		return _interList;
	}

	/**
	 * DNSQuery????????????interface
	 *
	 * @param device_id
	 * @return
	 */
	public String getDNSQueryInterface(String device_id) {
		logger.debug("getDNSQueryInterface({})", device_id);
		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type + "");
		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		// ?????????WAN???
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String _interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
		name = "Lan(IPInterface):1";
		value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		logger.debug("LAN???(IPInterface)???" + name + "=" + value);
		_interList += "<option selected value='" + value + "'>" + name
				+ "</option>";
		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(device_id, ConstantClass.GATHER_WAN, 1);
		String errorMsg = "";
		logger.warn("??????????????????=====" + irt);
		if (irt != 1) {
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
		} else {
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if (Global.GW_TYPE_ITMS.equals(gw_type)) {
				wanConnIds = getWanConnIds(device_id);
			} else {
				wanConnIds = getWanConnIds_bbms(device_id);
			}
			if (wanConnIds == null || wanConnIds.isEmpty()) {
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
			} else {
				flag = true;
				for (Map map : wanConnIds) {
					String wan_conn_id = StringUtil.getStringValue(map
							.get("wan_conn_id"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = "";
					String vlanid = StringUtil.getStringValue(map
							.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map
							.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map
							.get("serv_list"));
					String tmp = "";
					if (vlanid == null || vlanid.equals("")
							|| vlanid.equals("NULL")) {
						tmp = "pvc:" + pvc;
					} else {
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1")) {
						name = "Wan(" + "WANPPPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANPPPConnection." + wan_conn_sess_id + ".";
					} else if (sessType.equals("2")) {
						name = "Wan(" + "WANIPConnection/" + tmp + "):"
								+ wan_conn_id + "-" + wan_conn_sess_id;
						value = wanConnDevice + wan_conn_id
								+ ".WANIPConnection." + wan_conn_sess_id + ".";
					} else {
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					_interList += "<option value='" + value + "'>" + name
							+ "</option>";
				}
			}
		}
		// 3?????????listBox
		_interList += "</select>";
		if (!flag) {
			_interList = errorMsg;
		}
		logger.warn("_interList-----" + _interList);
		return _interList;
	}

	/**
	 * ??????DSL wan??????
	 *
	 * @param device_id
	 * @return
	 */
	public String getDslWAN(String device_id) {
		logger.debug("getDNSQueryInterface({})", device_id);

		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);

		// ??????WAN??????????????????
		String path = "InternetGatewayDevice.WANDevice.";
		Map<String, String> paramMap = new HashMap<String, String>();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(path);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRPCArr = getDevRPCArr(device_id, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdOBJList = this.getDevRPCResponse(devRPCArr,
				Global.RpcCmd_Type, gw_type);
		String errorMsg = "";
		if (null == devRpcCmdOBJList || 0 == devRpcCmdOBJList.size()) {
			errorMsg = "??????????????????";
			return errorMsg;
		}
		// ???????????????????????????
		Element element = dealDevRPCResponse("GetParameterNamesResponse",
				devRpcCmdOBJList, device_id);
		if (element == null) {
			errorMsg = "???????????????Wan??????";
			return errorMsg;
		}
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		// ???SOAP?????????????????????????????????XML,????????????
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// ????????????XML??????,??????????????????
		StringBuffer sb = new StringBuffer();
		if (null != getParameterNamesResponse) {
			ParameterInfoStruct[] pisArr = getParameterNamesResponse
					.getParameterList();
			if (null != pisArr) {
				String name = null;
				for (int i = 0; i < pisArr.length; i++) {
					name = pisArr[i].getName();
					String writable = pisArr[i].getWritable();
					sb.append(name).append("\6");
				}
			}
		}
		if (sb.length() == 0 || StringUtil.IsEmpty(sb.toString())) {
			return "";
		}
		return sb.substring(0, sb.length() - 1);
	}

	private List getWanConnIds(String device_id) {
		List<Map> list = new ArrayList<Map>();
		PrepareSQL psql = new PrepareSQL(
				"select b.sess_type,b.serv_list,a.vlan_id,b.wan_conn_id,b.wan_conn_sess_id "
						+ "from cuc_gw_wan_conn_session b,cuc_gw_wan_conn a "
						+ "where a.device_id = b.device_id and a.wan_id = b.wan_id and a.wan_conn_id = b.wan_conn_id and upper(b.serv_list) like '%INTERNET%' and b.device_id = ?");
		psql.setString(1, device_id);
		Cursor cursor = DataSetBean.getCursor(psql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++) {
			list.add(cursor.getRecord(i));
		}
		return list;
	}

	private List getWanConnIds_bbms(String device_id) {
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();
		sql.append("select b.sess_type,b.serv_list,a.vlan_id, a.vpi_id, a.vci_id, b.wan_conn_id,b.wan_conn_sess_id " +
				"from gw_wan_conn_bbms a,gw_wan_conn_session_bbms b where a.device_id=b.device_id " +
				"and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and upper(b.serv_list) like '%INTERNET%' and a.device_id='");
		sql.append(device_id).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++) {
			Map map = cursor.getRecord(i);
			String vpi_id = StringUtil.getStringValue(map, "vpi_id");
			String vci_id = StringUtil.getStringValue(map, "vci_id");
			map.put("pvc", vpi_id + "/" + vci_id);
			list.add(map);
		}
		return list;
	}

	/**
	 * ??????PING???????????? liuli@lianchuang.com
	 *
	 * @return
	 */
	public String getPingInterfaceListBox(String device_id) {
		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);
		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		// ??????Lan
		// ???InternetGatewayDevice.LANDevice.1.LAN-HostConfigManagement.IPInterface.???
		String _interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		String param = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
		// int length = param.length();
		// ??????IPInterface?????????
		Collection collection = WANConnDeviceAct.getParameterNameList(
				device_id, param);
		Iterator iterator = collection.iterator();
		String name = null;
		String value = null;
		// ????????????????????? IPInterface
		if (iterator.hasNext())
			flag = true;
		while (iterator.hasNext()) {
			value = ((String) iterator.next());
			if (param.equals(value)) {
				logger.debug("param???value?????????...");
				continue;
			}
			// InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1.
			// ????????????????????????????????????"."
			name = getLastString(param, value);
			name = "Lan(IPInterface):" + name;
			value = trimDot(value);
			logger.debug("LAN???(IPInterface)???" + name + "=" + value);
			_interList += "<option selected value='" + value + "'>" + name
					+ "</option>";
		}
		// -----------------------------------------------modify by lizhaojun
		// ?????????????????????????????????-------------------------
		// ??????WAN
		param = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		collection = WANConnDeviceAct.getParameterNameList(device_id, param);
		Collection collectionSub = null;
		Iterator iteratorSub = null;
		iterator = collection.iterator();
		String pathWANConn = null;
		// length = param.length();
		String paraValue = "";
		Map paraValueMap;
		String name2 = "";
		while (iterator.hasNext()) {
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWANConn = (String) iterator.next();
			if (param.equals(pathWANConn)) {
				logger.debug("param???pathWANConn?????????...");
				continue;
			}
			paraValueMap = paramTreeObject.getParaValueMap(pathWANConn
					+ "WANDSLLinkConfig.DestinationAddress", device_id);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			logger.debug("paraValue :" + paraValue);
			if (paraValue.indexOf(_Pvc) != -1) {
				name = "??????WAN???(WANPPPConnection)???"
						+ getLastString(param, pathWANConn);
			} else {
				name = "??????WAN???(WANPPPConnection)???"
						+ getLastString(param, pathWANConn);
			}
			// name = "WAN :" + getLastString(param, pathWANConn);
			collectionSub = WANConnDeviceAct.getParameterNameList(device_id,
					pathWANConn + "WANPPPConnection.");
			iteratorSub = collectionSub.iterator();
			// ?????????????????????WanPPPconnection
			if (iteratorSub.hasNext())
				flag = true;
			while (iteratorSub.hasNext()) {
				value = (String) iteratorSub.next();
				if ((pathWANConn + "WANPPPConnection.").equals(value)) {
					logger.debug("A--???????????????...");
					continue;
				}
				value = trimDot(value);
				logger.debug("wanppp ping_value :" + value);
				_interList += "<option value='" + value + "'>" + name + "-"
						+ getLastString(value) + "</option>";
			}
			// --------------------
			Collection collectionSub2 = WANConnDeviceAct.getParameterNameList(
					device_id, pathWANConn + "WANIPConnection.");
			iteratorSub = collectionSub2.iterator();
			if (iteratorSub.hasNext()) {
				flag = true;
				name2 = "WAN???(WANIPConnection)???"
						+ getLastString(param, pathWANConn);
			}
			while (iteratorSub.hasNext()) {
				value = (String) iteratorSub.next();
				if ((pathWANConn + "WANIPConnection.").equals(value)) {
					logger.debug("B--???????????????...");
					continue;
				}
				value = trimDot(value);
				logger.debug("wanIP ping_value :" + value);
				_interList += "<option value='" + value + "'>" + name2 + "-"
						+ getLastString(value) + "</option>";
			}
			paraValueMap = null;
			paraValue = null;
		}
		_interList += "</select>";
		if (flag == false)
			_interList = "";
		return _interList;
	}

	/**
	 * ??????ATMF5LOOP???????????? liuli@lianchuang.com
	 *
	 * @return
	 */
	public String getATMF5LOOPInterfaceListBox(String device_id) {
		// ??????????????????
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// -----------------------------------------------modify by lizhaojun
		// 2007-8-21
		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		boolean flag = false;
		StringBuffer sb = new StringBuffer();
		String param = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		Collection collection = WANConnDeviceAct.getParameterNameList(
				device_id, param);
		Iterator iterator = collection.iterator();
		String name = null;
		String value = null;
		if (iterator.hasNext())
			flag = true;
		sb.append("<select name=ATMF5 id=Interface class='bk'>");
		while (iterator.hasNext()) {
			value = ((String) iterator.next());
			if (param.equals(value)) {
				logger.debug("param???value?????????...");
				continue;
			}
			paramTreeObject.setGwType(gw_type);
			Map paraValueMap = paramTreeObject.getParaValueMap(value
					+ "WANDSLLinkConfig.DestinationAddress", device_id);
			String paraValue = paramTreeObject.getParaVlue(paraValueMap);
			if (paraValue.indexOf(_Pvc) != -1) {
				name = "??????WAN(WANConnectionDevice)???"
						+ getLastString(param, value);
			} else {
				name = "??????WAN(WANConnectionDevice)???"
						+ getLastString(param, value);
			}
			logger.debug("value :" + value);
			sb.append("<option  value='" + value + "'>" + name + "</option>");
		}
		sb.append("</select>");
		// ??????????????????Interface
		if (flag == false) {
			return "";
		}
		return sb.toString();
	}

	/**
	 * ?????????????????????????????? liuli@lianchuang.com
	 *
	 * @param value
	 * @return
	 */
	public String trimDot(String value) {
		logger.debug("value:" + value);
		int dotIndex = value.lastIndexOf(".");
		if (dotIndex == value.length() - 1) {
			value = value.substring(0, dotIndex);
		}
		// logger.debug("trimDot==>value:" + value);
		return value;
	}

	/**
	 * ??????InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.(
	 * source)??????
	 * InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1.
	 * (value)
	 * InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1
	 * (value) ?????? 1
	 *
	 * @return
	 */
	public String getLastString(String source, String value) {
		logger.debug("source-----:" + source);
		logger.debug("value-----:" + value);
		value = trimDot(value = value.substring(source.length()));
		return value;
	}

	/**
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.12.WANPPPConnection
	 * .8/***8.--->8
	 *
	 * @param value
	 * @return
	 */
	public String getLastString(String value) {
		value = trimDot(value);
		int dotIndex = value.lastIndexOf(".");
		if (dotIndex != -1)
			value = value.substring(dotIndex + 1);
		return value;
	}
}
