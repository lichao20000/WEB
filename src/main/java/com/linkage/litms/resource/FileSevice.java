
package com.linkage.litms.resource;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

import com.linkage.commons.db.DBUtil;
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
 * @since jtwg_1.1.0 Modify Record: 2007-06-18 Alex.Yan (yanhj@lianchuang.com) RemoteDB
 *        ACS.
 */
public class FileSevice
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(FileSevice.class);
	private String mVendorTypeSql = "select distinct  a.devicetype_id, a.device_model,b.devicetype_id  from tab_devicetype_info a,tab_template b where a.devicetype_id=b.devicetype_id ";
	private String mTemplateTypeSql = "select distinct  template_id,template_name from tab_template";
	private String mDeviceTypeSql = "select * from tab_template";
	private Cursor cursor = null;

	private static final String TR069 = "tr069";
	
	/** 0??????????????? ???1?????????ITMS??? 2?????????BBMS???3?????????SNMP???4:???????????????  */
	private static final int GW_TYPE_ALL = 0;
	private static final int GW_TYPE_ITMS = 1;
	private static final int GW_TYPE_BBMS = 2;
	private static final int GW_TYPE_SNMP = 3;
	private static final int GW_TYPE_STB = 4;
	
	/** ???????????????????????????????????? */
	private static final int DEVICE_SN_LEN = 5;
	/** ??????????????????id */
	private static final String ILLEGAL_CICY_ID = "-1";
	/** ?????????  */
	private static final String GRATER = ">";
	private static final String LESS = "<";
	private static final String EQUAL = "=";
	private static final String LESS_GRATER = "<>";
	
	private static final String NULL = "null";
	private static final String DELETE = "delete";
	private static final String ADD = "add";
	
	private static final String CUC = "CUC";
	
	/**
	 * ?????????????????????????????? add by liuli
	 *
	 * @param request
	 * @return strMsg ??????????????????????????????????????????
	 */
	public String getVendorTypeList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(mVendorTypeSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(mVendorTypeSql);
		String strVendorTypeList = FormUtil.createListBox(cursor, "devicetype_id",
				"device_model", flag, compare, rename);
		return strVendorTypeList;
	}

	public String getDeviceModelByVendorID()
	{
		// String strSQL = "select devicetype_id,device_model from tab_devicetype_info ";
		String strSql = "select a.device_model,b.devicetype_id"
				+ " from gw_device_model a, tab_devicetype_info b where a.device_model_id=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(strSql);
		cursor = DataSetBean.getCursor(psql.getSQL());
		String strVendorTypeList = FormUtil.createListBox(cursor, "devicetype_id",
				"device_model", true, "", "", true);
		return strVendorTypeList;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????????????? ??????????????? add by liuli
	 *
	 * @param request
	 * @return String ????????????????????????????????????????????????
	 */
	public String getDeviceList(HttpServletRequest request)
	{
		String deviceTypeId = request.getParameter("devicetype_id");
		String gatherId = request.getParameter("gather_id");
		String tmpSql = "select * from tab_gw_device ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select device_id, device_serialnumber, oui from tab_gw_device ";
		}
		tmpSql += " where devicetype_id =" + deviceTypeId + " and gather_id ='"
				+ gatherId + "' and device_status=1";
		tmpSql += " order by device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>????????????????????????</span></td></tr>";
		}
		else
		{
			int iflag = 1;
			while (fields != null)
			{
				if (iflag % 2 == 0)
				{
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\">";
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("device_id") + "/"
							+ (String) fields.get("device_serialnumber");
					serviceHtml += "</td></tr>";
				}
				else
				{
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

	public String getAllBoundPasswd(String oui, String deviceSerialnumber,
			String tableName, String deviceType)
	{
		String passwdStr = "";
		String sql = "";
		// ?????????tr069??????
		if (TR069.equals(deviceType))
		{
			sql = "select passwd from " + tableName + " where oui='" + oui
					+ "' and device_serialnumber='" + deviceSerialnumber
					+ "' and user_state= '1' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null)
			{
				while (fields != null)
				{
					if (i == 0)
					{
						passwdStr = (String) fields.get("passwd");
					}
					else
					{
						passwdStr += "," + (String) fields.get("passwd");
					}
					i++;
					fields = cursor.getNext();
				}
			}
		}
		if (null == passwdStr)
		{
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
	public String getAllBoundUser(String oui, String deviceSerialnumber,
			String tableName, String deviceType)
	{
		String userStr = "";
		String sql = "";
		// ?????????tr069??????
		if (TR069.equals(deviceType))
		{
			sql = "select username from " + tableName + " where oui='" + oui
					+ "' and device_serialnumber='" + deviceSerialnumber
					+ "' and user_state= '1' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null)
			{
				while (fields != null)
				{
					if (i == 0)
					{
						userStr = (String) fields.get("username");
					}
					else
					{
						userStr += "," + (String) fields.get("username");
					}
					i++;
					fields = cursor.getNext();
				}
			}
		}
		else
		{
			// ?????????SNMP?????? oui????????????device_id
			sql = "select username from " + tableName + " where device_id='" + oui + "'";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			int i = 0;
			if (fields != null)
			{
				while (fields != null)
				{
					if (i == 0)
					{
						userStr = (String) fields.get("username");
					}
					else
					{
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
	 * **************************************************************************************************
	 * TR069 ??? SNMP ?????? ???????????? ???????????????????????? 2007???12???17 lizj???5202???
	 * ***************************************************************************************************
	 *
	 * @param request
	 *            ????????????????????????????????????????????????????????????????????????????????????
	 * @param device_type
	 *            ??????????????? 0????????????????????????1????????????????????????2????????????????????????3???Snmp????????????
	 * @return
	 */
	public String getDeviceHtml(HttpServletRequest request, int deviceType, String type,
			boolean needFilter)
	{
		// ???????????????????????????
		String serviceHtml = "";
		// ?????? ??????????????????
		if (deviceType == GW_TYPE_ITMS)
		{
			serviceHtml += getTr069Device(request, type, needFilter, "tab_hgwcustomer", Global.GW_TYPE_ITMS);
		}
		// ?????? ??????????????????
		if (deviceType == GW_TYPE_BBMS)
		{
			serviceHtml += getTr069Device(request, type, needFilter, "tab_egwcustomer", Global.GW_TYPE_BBMS);
		}
		// ?????? SNMP????????????
		if (deviceType == GW_TYPE_SNMP)
		{
			serviceHtml += getSnmpDevice(request, type, needFilter);
		}
		if (deviceType == GW_TYPE_ALL)
		{
			serviceHtml += getTr069Device(request, type, needFilter, "tab_hgwcustomer", Global.GW_TYPE_ITMS);
			serviceHtml += getTr069Device(request, type, needFilter, "tab_egwcustomer", Global.GW_TYPE_BBMS);
			serviceHtml += getSnmpDevice(request, type, needFilter);
		}
		if (StringUtil.IsEmpty(serviceHtml))
		{
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
			boolean needFilter)
	{
		// type ??????????????????????????? radio or checkbox
		// ??????
		String cityId = request.getParameter("city_id");
		// ??????
//		String vendor_id = request.getParameter("vendor_id");
		// ??????????????? device_model
//		String devicetype_id = request.getParameter("devicetype_id");
		// ???????????? ?????? devicetype_id
		String softwareversion = request.getParameter("softwareversion");
		// ????????????
		String hguser = request.getParameter("hguser");
		// ????????????
		String telephone = request.getParameter("telephone");
		// ???????????????
		String deviceSerialnumber = request.getParameter("serialnumber");
		// ????????????
		String loopbackIp = request.getParameter("loopback_ip");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long areaId = user.getAreaId();
//		DeviceAct dac = new DeviceAct();
//		Map deviceType = dac.getDeviceTypeMap();
		// ??????devicetype_id ?????????strSoftwareversion, ?????????SNMP?????????????????????????????? strSoftwareversion
		// ????????????
//		String[] tmp = (String[]) deviceType.get(softwareversion);
//		String strSoftwareversion = "";
//		if (tmp != null)
//		{
//			strSoftwareversion = tmp[1];
//		}
		String tmpSql = "select a.*, c.* from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select a.oui, a.device_serialnumber, a.gather_id, a.device_id, a.loopback_ip " +
					"from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		}
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		boolean existed = !StringUtil.IsEmpty(hguser) || !StringUtil.IsEmpty(telephone);
		if (existed)
		{
			tmpSql += " inner join tab_egwcustomer d on a.device_id =d.device_id ";
		}
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + areaId + " ";
		}
		// ?????????????????????
		if (!StringUtil.IsEmpty(hguser))
		{
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (!StringUtil.IsEmpty(telephone))
		{
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > DEVICE_SN_LEN)
			{
				tmpSql += " and a.dev_sub_sn '"
						+ deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
								deviceSerialnumber.length()) + "' ";
			}
			tmpSql += " and a.device_serialnumber like '%" + deviceSerialnumber + "' ";
		}
		if (!StringUtil.IsEmpty(loopbackIp))
		{
			tmpSql += " and a.loopback_ip like '%" + loopbackIp + "%' ";
		}
		if (!StringUtil.IsEmpty(cityId) && !cityId.equals(ILLEGAL_CICY_ID))
		{
			// ????????????????????????
			SelectCityFilter scf = new SelectCityFilter();
			String citys = scf.getAllSubCityIds(cityId, true);
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
		if (!StringUtil.IsEmpty(softwareversion))
		{
			tmpSql += " and a.devicetype_id=" + softwareversion;
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		String serviceHtml = "";
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String username = getAllBoundUser((String) fields.get("oui"),
						(String) fields.get("device_serialnumber"), "tab_egwcustomer",
						"tr069");
				serviceHtml += "<input type=\"" + type + "\" dev_serial=\""
						+ fields.get("device_serialnumber")
						+ "\" id=\"device_id\" name=\"device_id\" gather_id=\""
						+ (String) fields.get("gather_id") + "\" username=\"" + username
						+ "\"  devicetype=\"snmp\" value=\""
						+ (String) fields.get("device_id") + "\"";
				if (needFilter)
				{
					serviceHtml += " onclick=\"filterByDevIDAndDevTypeID(this)\">";
				}
				else
				{
					serviceHtml += "\">";
				}
				serviceHtml += "&nbsp;" + (String) fields.get("device_serialnumber")
						+ "|" + (String) fields.get("loopback_ip") + " || " + username
						+ "<br>";
				fields = cursor.getNext();
			}
		}
		cursor = null;
		fields = null;
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
			boolean needFilter, String tabName, String gwType)
	{
		// type ??????????????????????????? radio or checkbox
		// ??????
		String cityId = request.getParameter("city_id");
		// ??????
		String vendorId = request.getParameter("vendor_id");
		// ??????????????? device_model
//		String devicetype_id = request.getParameter("devicetype_id");
		// ???????????? ?????? devicetype_id
		String softwareversion = request.getParameter("softwareversion");
		// ????????????
		String hguser = request.getParameter("hguser");
		// ????????????
		String telephone = request.getParameter("telephone");
		// ???????????????
		String deviceSerialnumber = request.getParameter("serialnumber");
		// ????????????
		String loopbackIp = request.getParameter("loopback_ip");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long areaId = user.getAreaId();
		String tmpSql = "select * from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select a.device_id, a.oui, a.device_serialnumber, a.gather_id, a.loopback_ip from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id";
		}
		// ???Admin ??????
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		boolean existed = !StringUtil.IsEmpty(hguser) || !StringUtil.IsEmpty(telephone);
		if (existed)
		{
			tmpSql += " inner join " + tabName
					+ " d on a.device_id = d.device_id and d.user_state='1' ";
		}
		tmpSql += " where a.device_status=1";
		tmpSql += " and  a.gw_type=" + gwType;
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + areaId + " ";
		}
		// ?????????????????????
		if (!StringUtil.IsEmpty(hguser))
		{
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (!StringUtil.IsEmpty(telephone))
		{
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (!StringUtil.IsEmpty(cityId) && !cityId.equals(ILLEGAL_CICY_ID))
		{
			// ????????????????????????
			SelectCityFilter scf = new SelectCityFilter();
			String citys = scf.getAllSubCityIds(cityId, true);
			tmpSql += " and a.city_id in (" + citys + ") ";
		}
		if (!StringUtil.IsEmpty(vendorId))
		{
			// tmpSql += " and c.oui='" + vendor_id + "'";
			tmpSql += " and c.vendor_id='" + vendorId + "'";
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			tmpSql += " and a.devicetype_id=" + softwareversion + " ";
		}
		if (!StringUtil.IsEmpty(deviceSerialnumber))
		{
			if (deviceSerialnumber.length() > DEVICE_SN_LEN)
			{
				tmpSql += " and a.dev_sub_sn ='"
						+ deviceSerialnumber.substring(deviceSerialnumber.length() - 6,
								deviceSerialnumber.length()) + "' ";
			}
			tmpSql += " and a.device_serialnumber like '%" + deviceSerialnumber + "' ";
		}
		if (!StringUtil.IsEmpty(loopbackIp))
		{
			tmpSql += " and a.loopback_ip like '%" + loopbackIp + "%' ";
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "";
		if (fields != null)
		{
			while (fields != null)
			{
				String username = getAllBoundUser((String) fields.get("oui"),
						(String) fields.get("device_serialnumber"), tabName, "tr069");
				String passwd = getAllBoundPasswd((String) fields.get("oui"),
						(String) fields.get("device_serialnumber"), tabName, "tr069");
				serviceHtml += "<input type=" + type + " oui='" + fields.get("oui")
						+ "' dev_serial=\"" + fields.get("device_serialnumber")
						+ "\" id=\"device_id\" name=\"device_id\"  gather_id=\""
						+ (String) fields.get("gather_id") + "\"  username=\"" + username
						+ "\"  passwd=\"" + passwd + "\" devicetype=\"tr069\" value=\""
						+ (String) fields.get("device_id") + "\"";
				if (needFilter)
				{
					serviceHtml += " onclick=\"filterByDevIDAndDevTypeID(this)\">";
				}
				else
				{
					serviceHtml += "\">";
				}
				serviceHtml += (String) fields.get("oui") + "-"
						+ (String) fields.get("device_serialnumber") + " | "
						+ (String) fields.get("loopback_ip") + " | " + username + "<br>";
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
	public String getDeviceListByVersion(HttpServletRequest request, boolean needFilter)
	{
		// ?????????
		String gatherId = request.getParameter("gather_id");
		//????????????
		// String oui = request.getParameter("vendor_id");
		// ????????????
		String vendorId = request.getParameter("vendor_id");
		String softwareversion = request.getParameter("softwareversion");
		String devicetypeId = request.getParameter("devicetype_id");
		String hguser = request.getParameter("hguser");
		String telephone = request.getParameter("telephone");
		// ??????????????????
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long areaId = user.getAreaId();
//		String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select * from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// ???admin??????
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// ?????????????????????
		boolean existed = !StringUtil.IsEmpty(hguser) || !StringUtil.IsEmpty(telephone);
		if (existed)
		{
			tmpSql += " inner join tab_hgwcustomer d on a.device_id = d.device_id ";
		}
		tmpSql += " where a.device_status=1";
		// ???admin??????
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + areaId + " ";
		}
		// ?????????????????????
		if (!StringUtil.IsEmpty(hguser))
		{
			tmpSql += " and d.username ='" + hguser + "'";
		}
		if (!StringUtil.IsEmpty(telephone))
		{
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (!StringUtil.IsEmpty(gatherId))
		{
			tmpSql += " and a.gather_id ='" + gatherId + "'";
		}
		if (!StringUtil.IsEmpty(vendorId))
		{
			tmpSql += " and c.vendor_id ='" + vendorId + "'";
		}
		if (!StringUtil.IsEmpty(softwareversion))
		{
			tmpSql += " and c.softwareversion ='" + softwareversion + "'";
		}
		if (!StringUtil.IsEmpty(devicetypeId))
		{
			tmpSql += " and a.devicetype_id =" + devicetypeId + " ";
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
		if (fields == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>????????????????????????</span></td></tr>";
		}
		else
		{
			int iflag = 1;
			while (fields != null)
			{
				tmpUser = (String) fields.get("username");
				logger.debug("	tmpUser:" + tmpUser);
				if (tmpUser == null || tmpUser.equalsIgnoreCase("null"))
				{
					tmpUser = "|";
					logger.debug("	username null");
				}
				if (iflag % 2 == 0)
				{
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\"";
					if (needFilter)
					{
						serviceHtml += " onclick=\"filterByDevID()\">";
					}
					else
					{
						serviceHtml += ">";
					}
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber") + " | "
							+ tmpUser;
					serviceHtml += "</td></tr>";
				}
				else
				{
					serviceHtml += "<tr>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\"";
					if (needFilter)
					{
						serviceHtml += " onclick=\"filterByDevID()\">";
					}
					else
					{
						serviceHtml += ">";
					}
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber") + " | "
							+ tmpUser;
					serviceHtml += "</td></tr>";
				}
				// iflag++;
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	public String gettemplateTypeList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(mTemplateTypeSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(mTemplateTypeSql);
		String strVendorTypeList = FormUtil.createListBox(cursor, "template_id",
				"template_name", flag, compare, rename);
		return strVendorTypeList;
	}

	public String getdeviceTypeList()
	{
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			mDeviceTypeSql = "select template_id, template_name from tab_template";
		}
		PrepareSQL psql = new PrepareSQL(mDeviceTypeSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(mDeviceTypeSql);
		String strVendorTypeList = FormUtil.createListBox1(cursor, "template_id",
				"template_name", false, "", "", true);
		return strVendorTypeList;
	}

	public Map getTemplateMap()
	{
		String sql = "select * from tab_template ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select template_id, template_name from tab_template ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		
		Map<String, String> templateMap = new HashMap<String, String>(cursor.getRecordSize());
		
		Map fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String templateId = (String) fields.get("template_id");
				String templateName = (String) fields.get("template_name");
				templateMap.put(templateId, templateName);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public Map getServiceMap()
	{
		String sql = "select * from tab_service ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select service_id, service_name from tab_service ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		
		Map templateMap = new HashMap(cursor.getRecordSize());
		
		Map fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String templateId = (String) fields.get("service_id");
				String templateName = (String) fields.get("service_name");
				templateMap.put(templateId, templateName);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public Map getDevicelistMap()
	{
		// String sql = "select distinct a.devicetype_id, a.device_model,b.devicetype_id
		// from tab_devicetype_info a,tab_template b where
		// a.devicetype_id=b.devicetype_id";
		String sql = "select distinct  a.devicetype_id, c.device_model "
				+ " from tab_devicetype_info a,tab_template b,gw_device_model c "
				+ " where a.devicetype_id=b.devicetype_id and a.device_model_id=c.device_model_id ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		
		Map<String, String> templateMap = new HashMap<String, String>(cursor.getRecordSize());
		
		Map fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				String templateId = (String) fields.get("devicetype_id");
				String templateName = (String) fields.get("device_model");
				templateMap.put(templateId, templateName);
				fields = cursor.getNext();
			}
		}
		return templateMap;
	}

	public String getInternetPVCNode(String deviceId,String pvc)
	{
		logger.debug("pvc----------pvc :" + pvc);
		//??????????????????
//		int gw_type = LipossGlobals.getGw_Type(device_id);

		String wconnNode = "";
		String wpppConnNode = "-1";
//		String Pvc_Internet = LipossGlobals.getLipossProperty("InstArea.PVC_835");
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		Collection collection1 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator1 = collection1.iterator();
		if (collection1.size() == 0)
		{
			wconnNode = "-1";
		}
		Collection collection2 = null;
		Iterator iterator2 = null;
		String pathWanConn = null;
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		String paraValue = "";
		Map paraValueMap;
		String pathPpp = "";
		while (iterator1.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWanConn = (String) iterator1.next();
			// logger.debug("GSJ----------pathWANConn:" + pathWANConn);
			paraValueMap = paramTreeObject.getParaValueMap(pathWanConn
					+ "WANDSLLinkConfig.DestinationAddress", deviceId);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			// logger.debug("GSJ----------paraValue :" + paraValue);
			if (paraValue.indexOf(pvc) != -1)
			{
				wconnNode = getLastString(rootPara, pathWanConn);
				logger.debug("A----------wconnNode :" + wconnNode);
				collection2 = WANConnDeviceAct.getParameterNameList(deviceId,
						pathWanConn + "WANPPPConnection.");
				iterator2 = collection2.iterator();
				while (iterator2.hasNext())
				{
					pathPpp = (String) iterator2.next();
					// logger.debug("GSJ----------path_ppp :" + path_ppp);
					wpppConnNode = getLastString(pathWanConn + "WANPPPConnection.",
							pathPpp);
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
	public String chgBusiness(String deviceId,
			HttpServletRequest request)
	{
//		String Pvc_Internet = LipossGlobals.getLipossProperty("InstArea.PVC_835");
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		Collection collection1 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator1 = collection1.iterator();
		logger.debug("GSJ----------length:" + collection1.size());
		Collection collection2 = null;
		Iterator iterator2 = null;
		String pathWanConn = null;
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		//??????????????????
//		int gw_type = LipossGlobals.getGw_Type(device_id);
		String paraValue = "";
		Map paraValueMap;
		int flagConnType = 0;
		int flagUsername = 0;
		int flagPasswd = 0;
		String pathPpp = "";
		String connType = request.getParameter("connType");
//		String username = request.getParameter("username");
//		String passwd = request.getParameter("passwd");
		String resultStr = "-1";
		while (iterator1.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWanConn = (String) iterator1.next();
			logger.debug("GSJ----------pathWANConn:" + pathWanConn);
			paraValueMap = paramTreeObject.getParaValueMap(pathWanConn
					+ "WANDSLLinkConfig.DestinationAddress", deviceId);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			logger.debug("GSJ----------paraValue :" + paraValue);
			if (paraValue.indexOf("8/85") != -1)
			{
				logger.debug("GSJ----------in :");
				collection2 = WANConnDeviceAct.getParameterNameList(deviceId,
						pathWanConn + "WANPPPConnection.");
				iterator2 = collection2.iterator();
				while (iterator2.hasNext())
				{
					pathPpp = (String) iterator2.next();
					logger.debug("GSJ----------path_ppp :" + pathPpp);
					flagConnType = paramTreeObject.setParaValueFlag(pathPpp
							+ "ConnectionType", deviceId, connType);
					logger.debug("GSJ----------flag_connType :" + flagConnType);
					if (flagConnType == 1 && "IP_Routed".equals(connType))
					{
						logger.debug("GSJ----------????????????????????????!");
						// paramTreeObject.setParaValueFlag(path_ppp+"Username",ior,device_id,gather_id,username);
						flagUsername = 1;
						if (flagUsername == 1)
						{
							logger.debug("GSJ----------?????????????????????!");
							// paramTreeObject.setParaValueFlag(path_ppp+"Password",ior,device_id,gather_id,passwd);
							flagPasswd = 1;
							if (flagPasswd == 1)
							{
								logger.debug("GSJ----------??????????????????!");
								resultStr = "1";
								// ChongqiList(request);
							}
							else
							{
								// ??????????????????
								resultStr = "-3";
							}
						}
						else
						{
							// ?????????????????????
							resultStr = "-2";
						}
					}
					else if (flagConnType == 1 && "PPPoE_Bridged".equals(connType))
					{
						// ????????????????????????
						resultStr = "1";
					}
					else
					{
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

	public void getAllNamePw(String deviceId)
	{
//		String wconnNode = "";
//		String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		// ??????WANConnectionDevice.?????????????????????
		logger.debug("device_id---------:" + deviceId);
//		logger.debug("gather_id---------:" + gather_id);
//		logger.debug("ior---------:" + ior);
		Collection collection1 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator1 = collection1.iterator();
		if (collection1.size() == 0)
		{
			return;
		}
		logger.debug("collection_1---------:" + collection1);
		Collection collection2 = null;
		Iterator iterator2 = null;
		Collection collection3 = null;
		Iterator iterator3 = null;
		String pathWanConn = null;
//		String paraValue = "";
//		Map paraValueMap;
		String pathPpp = "";
		String pathIp = "";
		String[] paramArrPppoe = new String[6];
		String[] paramArrIp = new String[9];
		while (iterator1.hasNext())
		{
			pathWanConn = (String) iterator1.next();
			logger.debug("A----------pathWANConn :" + pathWanConn);
			if (rootPara.equals(pathWanConn) || "".equals(pathWanConn))
			{
				continue;
			}
			collection2 = WANConnDeviceAct.getParameterNameList(deviceId, pathWanConn
					+ "WANPPPConnection.");
			logger.debug("AA----------collection_2 :" + collection2);
			iterator2 = collection2.iterator();
			while (iterator2.hasNext())
			{
				pathPpp = (String) iterator2.next();
				if ((pathWanConn + "WANPPPConnection.").equals(pathPpp)
						|| "".equals(pathPpp))
				{
					continue;
				}
				logger.debug("B---------path_ppp :" + pathPpp);
				paramArrPppoe[0] = pathPpp + "ConnectionType";
				paramArrPppoe[1] = pathPpp + "X_CT-COM_LanInterface";
				paramArrPppoe[2] = pathPpp + "X_CT-COM_ServiceList";
				paramArrPppoe[3] = pathPpp + "Enable";
				paramArrPppoe[4] = pathPpp + "Username";
				paramArrPppoe[5] = pathPpp + "Password";
				Map pppoeMap = getDevInfo_tr069(pathPpp, deviceId,
						paramArrPppoe);
				logger.debug("BB------------:" + pppoeMap);
				pppoeList.add(pppoeMap);
			}
			collection3 = WANConnDeviceAct.getParameterNameList(deviceId, pathWanConn
					+ "WANIPConnection.");
			iterator3 = collection3.iterator();
			while (iterator3.hasNext())
			{
				logger.debug("C----------collection_2 ");
				pathIp = (String) iterator3.next();
				if ((pathWanConn + "WANIPConnection.").equals(pathIp)
						|| "".equals(pathIp))
				{
					continue;
				}
				logger.debug("CC---------path_ip :" + pathIp);
				paramArrIp[0] = pathIp + "ConnectionType";
				paramArrIp[1] = pathIp + "X_CT-COM_LanInterface";
				paramArrIp[2] = pathIp + "X_CT-COM_ServiceList";
				paramArrIp[3] = pathIp + "Enable";
				paramArrIp[4] = pathIp + "AddressingType";
				paramArrIp[5] = pathIp + "ExternalIPAddress";
				paramArrIp[6] = pathIp + "SubnetMask";
				paramArrIp[7] = pathIp + "DefaultGateway";
				// paramArrIP[8] = path_ip + "DNSServers";
				paramArrIp[8] = "InternetGatewayDevice.WANDevice.1.WANCommonInterfaceConfig.WANAccessType";
				Map ipMap = getDevInfo_tr069(pathIp, deviceId, paramArrIp);
				ipList.add(ipMap);
			}
		}
	}

	public ArrayList getpppoeList()
	{
		return pppoeList;
	}

	public ArrayList getipList()
	{
		return ipList;
	}

	// TODO
	/**
	 * ????????????PPPoE????????????
	 */
	ArrayList<String> allPppoeNames = new ArrayList<String>();

	public void getAllPPPoENames()
	{
		String paramName = null;
		String param1 = "InternetGatewayDevice.WANDevice.";
		String param2 = ".WANConnectionDevice.";
		String param3 = ".WANPPPConnection.";
		// String param4 = ".Username";
		// logger.debug("allParamsNamesMap----------:" + allParamsNamesMap);
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0)
		{
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++)
		{
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, param3, ".Enable")
					|| parse(paramName, param1, param2, param3, ".Username")
					|| parse(paramName, param1, param2, param3, ".ConnectionType"))
			{
				allPppoeNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnPPPoENames()
	{
		return allPppoeNames;
	}

	/**
	 * ????????????IP????????????
	 */
	ArrayList<String> allIpNames = new ArrayList<String>();

	public void getAllIPNames()
	{
		String paramName = null;
		String param1 = "InternetGatewayDevice.WANDevice.";
		String param2 = ".WANConnectionDevice.";
		String param3 = ".WANIPConnection.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0)
		{
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++)
		{
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, param3, ".Enable")
					|| parse(paramName, param1, param2, param3, ".ExternalIPAddress")
					|| parse(paramName, param1, param2, param3, ".SubnetMask")
					|| parse(paramName, param1, param2, param3, ".DefaultGateway")
					|| parse(paramName, param1, param2, param3, ".DNSServers"))
			{
				allIpNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnIPNames()
	{
		return allIpNames;
	}

	/**
	 * ????????????WLAN????????????
	 */
	ArrayList<String> allWlanNames = new ArrayList<String>();

	public void getAllWLANNames()
	{
		String paramName = null;
		String param1 = "InternetGatewayDevice.LANDevice.";
		String param2 = ".WLANConfiguration.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0)
		{
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++)
		{
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, param2, ".Status")
					|| parse(paramName, param1, param2, ".SSID"))
			{
				allWlanNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnWLANNames()
	{
		return allWlanNames;
	}

	/**
	 * ????????????DHCP????????????
	 */
	ArrayList<String> allDhcpNames = new ArrayList<String>();

	public void getAllDHCPNames()
	{
		String paramName = null;
		String param1 = "InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.";
		if (null == allParamsNamesMap || allParamsNamesMap.size() == 0)
		{
			return;
		}
		for (int i = 0; i < allParamsNamesMap.size(); i++)
		{
			paramName = allParamsNamesMap.get(i + "");
			if (parse(paramName, param1, ".DHCPConfig.DHCPServerEnable")
					|| parse(paramName, param1, ".DHCPConfig.MinAddress")
					|| parse(paramName, param1, ".DHCPConfig.MaxAddress")
					|| parse(paramName, param1, ".DHCPConfig.ReservedAddresses")
					|| parse(paramName, param1, ".DHCPConfig.SubnetMask")
					|| parse(paramName, param1, ".DHCPConfig.DNSServers")
					|| parse(paramName, param1, ".DHCPConfig.DomainName")
					|| parse(paramName, param1, ".DHCPConfig.DefaultGateway"))
			{
				allDhcpNames.add(paramName);
				allUserfulParamsNamesList.add(paramName);
			}
		}
	}

	public ArrayList<String> returnDHCPNames()
	{
		return allDhcpNames;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	public ArrayList<String> returnallUserfulParamsNames()
	{
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
	private boolean parse(String line, String param1, String param2, String param3,
			String param4)
	{
		// logger.debug("line-T1:" + line);
		Pattern a = Pattern.compile(param1 + "\\d{1,2}" + param2 + "\\d{1,2}" + param3
				+ "\\d{1,2}" + param4);
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
	private boolean parse(String line, String param1, String param2, String param3)
	{
		// logger.debug("line-T2:" + line);
		Pattern a = Pattern.compile(param1 + "\\d{1,2}" + param2 + "\\d{1,2}" + param3);
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
	private boolean parse(String line, String param1, String param2)
	{
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
	public void getAllParamNames(String deviceId)
	{
		logger.debug("GSJ--------------getAllParamNames");

		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);

		// ???????????????????????????
		String paraV = "InternetGatewayDevice.";
		// ?????????????????????MAP
		// HashMap paraMap = new HashMap();
		allParamsNamesMap.clear();
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(paraV);
		getParameterNames.setNextLevel(0);
		DevRpc[] devRpcArr = getDevRPCArr(deviceId, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdObjList = getDevRPCResponse(devRpcArr,
				Global.RpcCmd_Type,gwType);
		Element element = dealDevRPCResponse("GetParameterNamesResponse",
				devRpcCmdObjList, deviceId);
		if (null == element)
		{
			return;
		}
		// ???????????????????????????
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// ????????????XML??????,??????????????????
		if (null != getParameterNamesResponse)
		{
			ParameterInfoStruct[] pisArr = getParameterNamesResponse.getParameterList();
			if (null != pisArr)
			{
				String name = null;
				for (int i = 0; i < pisArr.length; i++)
				{
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
			List<DevRpcCmdOBJ> devRpcCmdObjList, String deviceId)
	{
		if (devRpcCmdObjList == null || devRpcCmdObjList.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		DevRpcCmdOBJ devRpcCmdObj = devRpcCmdObjList.get(0);
		if (devRpcCmdObj == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcCmdObjList = devRpcCmdObj.getRpcList();
		if (rpcCmdObjList == null || rpcCmdObjList.size() == 0)
		{
			logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
			return null;
		}
		com.ailk.tr069.devrpc.obj.mq.Rpc acsRpcCmdObj = rpcCmdObjList.get(0);
		if (acsRpcCmdObj == null)
		{
			logger.warn("[{}]ACSRpcCmdOBJ???????????????", deviceId);
			return null;
		}
		if (stringRpcName == null)
		{
			logger.warn("[{}]stringRpcName?????????", deviceId);
			return null;
		}
		if (stringRpcName.equals(acsRpcCmdObj.getRpcName()))
		{
			String resp = acsRpcCmdObj.getValue();
			logger.warn("[{}]???????????????{}", deviceId, resp);
			Fault fault = null;
			if (StringUtil.IsEmpty(resp))
			{
				logger.debug("[{}]DevRpcCmdOBJ.value == null", deviceId);
			}
			else
			{
				SoapOBJ soapObj = XML.getSoabOBJ(XML.CreateXML(resp));
				if (soapObj != null)
				{
					fault = XmlToRpc.Fault(soapObj.getRpcElement());
					if (fault != null)
					{
						logger.warn("setValue({})={}", deviceId, fault.getDetail()
								.getFaultString());
					}
					else
					{
						return soapObj.getRpcElement();
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
	Map<String, String> allNeededNameValueMap = new HashMap();
	String[] params1 = new String[10];
	ArrayList allNeededList = new ArrayList();

	public void getAllNames_ValueMap(String deviceId, String gatherId, String[] params,
			String ior)
	{
		try
		{
			logger.debug("???????????????????????????????????????????????????5???...");
			Thread.sleep(5000);
			logger.debug("??????????????????????????????ACS...");
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		allNeededNameValueMap = getDevInfo_webtopo(deviceId, params);
		// return pppoeMap;
	}

	public Map<String, String> getAllNeededNameValueMap()
	{
		if (null == allNeededNameValueMap || allNeededNameValueMap.size() == 0)
		{
			return null;
		}
		else
		{
			return allNeededNameValueMap;
		}
	}

	ArrayList allNamesList = new ArrayList();
	ArrayList allStaticIpList = new ArrayList();
	ArrayList allWlanList = new ArrayList();
	ArrayList allDhcpList = new ArrayList();
	StringBuffer allNamesBuff = new StringBuffer();
	StringBuffer allIpNamesBuff = new StringBuffer();
	StringBuffer allWlanNamesBuff = new StringBuffer();
	StringBuffer allDhcpNamesBuff = new StringBuffer();

	public void getAllNamePw_webtopo(String deviceId, String gatherId, String ior)
	{
		String rootPara = "InternetGatewayDevice.WANDevice.";
		logger
				.debug("device_id|gather_id|ior:" + deviceId + "|" + gatherId + "|"
						+ ior);
		Collection collection0 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator0 = collection0.iterator();
		if (collection0.size() == 0)
		{
			return;
		}
		logger.debug("collection0=:" + collection0);
		Collection collection1 = null;
		Iterator iterator1 = null;
		Collection collection2 = null;
		Iterator iterator2 = null;
		String pathWanDevice = null;
		String pathWanConn = null;
		String pathPppConn = "";
		String[] paramArrPppoe = new String[3];
		while (iterator0.hasNext())
		{
			// ??????InternetGatewayDevice.WANDevice.{i}.
			pathWanDevice = (String) iterator0.next();
			logger.debug("pathWANDevice=:" + pathWanDevice);
			if (rootPara.equals(pathWanDevice) || "".equals(pathWanDevice))
			{
				continue;
			}
			collection1 = WANConnDeviceAct.getParameterNameList(deviceId, pathWanDevice
					+ "WANConnectionDevice.");
			iterator1 = collection1.iterator();
			if (collection1.size() == 0)
			{
				return;
			}
			while (iterator1.hasNext())
			{
				// ??????InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.
				pathWanConn = (String) iterator1.next();
				logger.debug("pathWANConn=:" + pathWanConn);
				if (pathWanConn.equals(pathWanConn + "WANConnectionDevice.")
						|| "".equals(pathWanConn))
				{
					continue;
				}
				collection2 = WANConnDeviceAct.getParameterNameList(deviceId,
						pathWanConn + "WANPPPConnection.");
				iterator2 = collection2.iterator();
				while (iterator2.hasNext())
				{
					pathPppConn = (String) iterator2.next();
					logger.debug("pathPPPConn=:" + pathPppConn);
					if ((pathWanConn + "WANPPPConnection.").equals(pathPppConn)
							|| "".equals(pathPppConn))
					{
						continue;
					}
					paramArrPppoe[0] = pathPppConn + "Enable";
					paramArrPppoe[1] = pathPppConn + "Username";
					paramArrPppoe[2] = pathPppConn + "ConnectionType";
					// TODO
					allNamesBuff.append(paramArrPppoe[0] + "|" + paramArrPppoe[1] + "|"
							+ paramArrPppoe[2]);
					allNamesBuff.append("#");
					Map pppoeMap = getDevInfo_webtopo(deviceId, paramArrPppoe);
					// pppoeMap.put("AllNames", );
					allNamesList.add(pppoeMap);
					// AllNamesList.add(allNamesBuff.toString());
				}
			}
		}
		// ????????????
		collection0 = null;
		iterator0 = null;
		collection1 = null;
		iterator1 = null;
		collection2 = null;
		iterator2 = null;
	}

	public void getAllIP_webtopo(String deviceId, String gatherId, String ior)
	{
		String rootPara = "InternetGatewayDevice.WANDevice.";
		logger
				.debug("device_id|gather_id|ior:" + deviceId + "|" + gatherId + "|"
						+ ior);
		Collection collection0 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator0 = collection0.iterator();
		if (collection0.size() == 0)
		{
			return;
		}
		logger.debug("collection_0=:" + collection0);
		Collection collection1 = null;
		Iterator iterator1 = null;
		Collection collection2 = null;
		Iterator iterator2 = null;
		String pathWanDevice = null;
		String pathWanConn = null;
		String pathIpConn = null;
		String[] paramArrIp = new String[5];
		while (iterator0.hasNext())
		{
			// ??????InternetGatewayDevice.WANDevice.{i}.
			pathWanDevice = (String) iterator0.next();
			logger.debug("pathWANDevice=:" + pathWanDevice);
			if (rootPara.equals(pathWanDevice) || "".equals(pathWanDevice))
			{
				continue;
			}
			collection1 = WANConnDeviceAct.getParameterNameList(deviceId, pathWanDevice
					+ "WANConnectionDevice.");
			iterator1 = collection1.iterator();
			if (collection1.size() == 0)
			{
				return;
			}
			while (iterator1.hasNext())
			{
				// ??????InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.
				pathWanConn = (String) iterator1.next();
				logger.debug("pathWANConn=:" + pathWanConn);
				if (pathWanConn.equals(pathWanConn + "WANConnectionDevice.")
						|| "".equals(pathWanConn))
				{
					continue;
				}
				collection2 = WANConnDeviceAct.getParameterNameList(deviceId,
						pathWanConn + "WANIPConnection.");
				iterator2 = collection2.iterator();
				while (iterator2.hasNext())
				{
					pathIpConn = (String) iterator2.next();
					logger.debug("pathIPConn=:" + pathIpConn);
					if ((pathWanConn + "WANIPConnection.").equals(pathIpConn)
							|| "".equals(pathIpConn))
					{
						continue;
					}
					paramArrIp[0] = pathIpConn + "Enable";
					paramArrIp[1] = pathIpConn + "ExternalIPAddress";
					paramArrIp[2] = pathIpConn + "SubnetMask";
					paramArrIp[3] = pathIpConn + "DefaultGateway";
					paramArrIp[4] = pathIpConn + "DNSServers";
					allIpNamesBuff.append(paramArrIp[0] + "|" + paramArrIp[1] + "|"
							+ paramArrIp[2] + "|" + paramArrIp[3] + "|" + paramArrIp[4]);
					allIpNamesBuff.append("#");
					Map ipMap = getDevInfo_webtopo(deviceId, paramArrIp);
					// ipMap.put("paramNames", paramArrIP);
					allStaticIpList.add(ipMap);
				}
			}
		}
		// ????????????
		collection0 = null;
		iterator0 = null;
		collection1 = null;
		iterator1 = null;
		collection2 = null;
		iterator2 = null;
	}

	public String getAllpppoeNames()
	{
		return allNamesBuff.toString();
	}

	public String getAllipNames()
	{
		return allIpNamesBuff.toString();
	}

	public void getAllwlan_webtopo(String deviceId, String gatherId, String ior)
	{
//		String wconnNode = "";
//		String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.LANDevice.";
		// ??????WANConnectionDevice.?????????????????????
		logger
				.debug("device_id|gather_id|ior:" + deviceId + "|" + gatherId + "|"
						+ ior);
		Collection collection1 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator1 = collection1.iterator();
		if (collection1.size() == 0)
		{
			return;
		}
		logger.debug("collection_1=:" + collection1);
		Collection collection3 = null;
		Iterator iterator3 = null;
		String pathWanConn = null;
		String pathIp = "";
		String[] paramArrIp = new String[2];
		while (iterator1.hasNext())
		{
			pathWanConn = (String) iterator1.next();
			logger.debug("pathWANConn=:" + pathWanConn);
			if (rootPara.equals(pathWanConn) || "".equals(pathWanConn))
			{
				continue;
			}
			collection3 = WANConnDeviceAct.getParameterNameList(deviceId, pathWanConn
					+ "WLANConfiguration.");
			iterator3 = collection3.iterator();
			while (iterator3.hasNext())
			{
				pathIp = (String) iterator3.next();
				logger.debug("path_SSID=:" + pathIp);
				if ((pathWanConn + "WLANConfiguration.").equals(pathIp)
						|| "".equals(pathIp))
				{
					continue;
				}
				paramArrIp[0] = pathIp + "Status";
				paramArrIp[1] = pathIp + "SSID";
				allWlanNamesBuff.append(paramArrIp[0] + "|" + paramArrIp[1]);
				allWlanNamesBuff.append("#");
				Map ipMap = getDevInfo_webtopo(deviceId, paramArrIp);
				allWlanList.add(ipMap);
			}
		}
	}

	public String getAllwlanNames()
	{
		return allWlanNamesBuff.toString();
	}

	public void getAlldhcp_webtopo(String deviceId, String gatherId, String ior)
	{
//		String wconnNode = "";
//		String wpppConnNode = "-1";
		String rootPara = "InternetGatewayDevice.X_CT-COM_VLAN.VLANConfig.";
		// ??????WANConnectionDevice.?????????????????????
		logger
				.debug("device_id|gather_id|ior:" + deviceId + "|" + gatherId + "|"
						+ ior);
		Collection collection1 = WANConnDeviceAct.getParameterNameList(deviceId,
				rootPara);
		Iterator iterator1 = collection1.iterator();
		if (collection1.size() == 0)
		{
			return;
		}
		logger.debug("collection_1=:" + collection1);
//		Collection collection_3 = null;
//		Iterator iterator_3 = null;
		String pathWanConn = null;
		String[] paramArrIp = new String[8];
		while (iterator1.hasNext())
		{
			pathWanConn = (String) iterator1.next();
			logger.debug("pathWANConn=:" + pathWanConn);
			if (rootPara.equals(pathWanConn) || "".equals(pathWanConn))
			{
				continue;
			}
			paramArrIp[0] = pathWanConn + "DHCPConfig.DHCPServerEnable";
			paramArrIp[1] = pathWanConn + "DHCPConfig.MinAddress";
			paramArrIp[2] = pathWanConn + "DHCPConfig.MaxAddress";
			paramArrIp[3] = pathWanConn + "DHCPConfig.ReservedAddresses";
			paramArrIp[4] = pathWanConn + "DHCPConfig.SubnetMask";
			paramArrIp[5] = pathWanConn + "DHCPConfig.DNSServers";
			paramArrIp[6] = pathWanConn + "DHCPConfig.DomainName";
			paramArrIp[7] = pathWanConn + "DHCPConfig.DefaultGateway";
			allDhcpNamesBuff.append(paramArrIp[0] + "|" + paramArrIp[1] + "|"
					+ paramArrIp[2] + "|" + paramArrIp[3] + "|" + paramArrIp[4] + "|"
					+ paramArrIp[5] + "|" + paramArrIp[6] + "|" + paramArrIp[7]);
			allDhcpNamesBuff.append("#");
			Map ipMap = getDevInfo_webtopo(deviceId, paramArrIp);
			allDhcpList.add(ipMap);
		}
	}

	public String getAlldhcpNames()
	{
		return allDhcpNamesBuff.toString();
	}

	public ArrayList getAllDHCPList()
	{
		return allDhcpList;
	}

	public ArrayList getAllwlanList()
	{
		return allWlanList;
	}

	public ArrayList getAllNamesList()
	{
		return allNamesList;
	}

	public ArrayList getAllStaticIPList()
	{
		return allStaticIpList;
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
	// parameterValueStruct[params_name.length];
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
	// logger.debug("setParameterValuesResponse---:" + setParameterValuesResponse);
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
	// parameterValueStruct[params_name.length];
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
	// logger.debug("setParameterValuesResponse---:" + setParameterValuesResponse);
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
	
	/**
	 * 
	 * @param devRpcArr
	 * @param rpcType
	 * @param gwType
	 * @return
	 */
	public List<DevRpcCmdOBJ> getDevRPCResponse(DevRpc[] devRpcArr, int rpcType,String gwType)
	{
		logger.debug("getDevRPCResponse(devRpcArr)");
		if (devRpcArr == null)
		{
			logger.error("devRpcArr == null");
			return null;
		}
		List<DevRpcCmdOBJ> devRpcRep = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcRep = devRpcManager.execRPC(devRpcArr, rpcType);
		return devRpcRep;
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
	public DevRpc[] getDevRPCArr(String deviceId, RPCObject rpcObject)
	{
		DevRpc[] devRpcArr = new DevRpc[1];
		String stringRpcValue = "";
		String stringRpcName = "";
		if (rpcObject == null)
		{
			stringRpcValue = "";
			stringRpcName = "";
		}
		else
		{
			stringRpcValue = rpcObject.toRPC();
			stringRpcName = rpcObject.getClass().getSimpleName();
		}
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		devRpcArr[0].rpcArr = new Rpc[1];
		devRpcArr[0].rpcArr[0] = new Rpc();
		devRpcArr[0].rpcArr[0].rpcId = "1";
		devRpcArr[0].rpcArr[0].rpcName = stringRpcName;
		devRpcArr[0].rpcArr[0].rpcValue = stringRpcValue;
		return devRpcArr;
	}

	/**
	 * ??????tr069?????????????????????????????????????????????????????????????????????
	 *
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return ??????????????????key???HashMap?????????????????????????????????????????????
	 */
	public Map<String, String> getDevInfo_tr069(String path, String deviceId, String[] arr)
	{
		// ???ACS????????????????????????
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		Map paraValueMap = null;
		Map<String, String> infoMap = new HashMap<String, String>(arr.length);
		// ??????????????????????????????
		for (int i = 0; i < arr.length; i++)
		{
			if (!"".equals(arr[i]))
			{
				paraValueMap = paramTreeObject.getParaValueMap(arr[i], deviceId);
				if (paraValueMap != null)
				{
					infoMap.put("" + i, (String) paraValueMap.get(arr[i]) + "|"
							+ path);
				}
				// ??????1???
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		return infoMap;
	}

	/**
	 * ??????tr069?????????????????????????????????????????????????????????????????????
	 *
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return ??????????????????key???HashMap?????????????????????????????????????????????
	 */
	public Map<String, String> getDevInfo_webtopo(String deviceId,String[] arr)
	{
		// ???ACS????????????????????????
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		Map paraValueMap = paramTreeObject.getParaValue_multi(arr, deviceId);
		return paraValueMap;
	}

	/**
	 * ??????IOR
	 *
	 * @param gather_id
	 * @return ior
	 */
	private String getIOR(String gatherId)
	{
		String ior = "";
		String getIorSql = "select ior from tab_ior where object_name='ACS_" + gatherId
				+ "' and object_poa='ACS_Poa_" + gatherId + "'";
		PrepareSQL psql = new PrepareSQL(getIorSql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(getIorSql);
		if (null != map)
		{
			ior = (String) map.get("ior");
		}
		return ior;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String allPingResult(HttpServletRequest request)
	{
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (TR069.equals(devicetype))
		{
			strResult = PingList(request);
		}
		else
		{
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
	public String snmpPing(HttpServletRequest request)
	{
		// ??????ID
		String deviceId = request.getParameter("device_id");
		// ??????IP
		String testIp = request.getParameter("test_ip");
		// ????????????ms
		String timeOut = request.getParameter("time_out");
		logger.debug("device_id :" + deviceId);
		logger.debug("test_ip :" + testIp);
		logger.debug("time_out :" + timeOut);
		// ??????????????????oid ???????????????????????? ??????ping???????????????OID
		String strSql = "select * from tab_gw_model_oper_oid where device_model = "
				+ "(select device_model_id from tab_gw_device where device_id= '"
				+ deviceId + "') and oid_type =3";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql = "select oid from tab_gw_model_oper_oid where device_model = "
					+ "(select device_model_id from tab_gw_device where device_id= '"
					+ deviceId + "') and oid_type =3";
		}
		PrepareSQL psql = new PrepareSQL(strSql);
		psql.getSQL();
		Map oidMap = DataSetBean.getRecord(strSql);
		// ??????????????????
		strSql = "select *  from  sgw_security where device_id='" + deviceId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql = "select device_id, snmp_w_passwd from  sgw_security where device_id='" + deviceId + "'";
		}
		String deviceSql = "select loopback_ip,snmp_udp from tab_gw_device where device_id='"
				+ deviceId + "'";
		psql = new PrepareSQL(strSql);
		psql.getSQL();
		Map deviceMap = DataSetBean.getRecord(strSql);
		psql = new PrepareSQL(deviceSql);
		psql.getSQL();
		Map gwMap = DataSetBean.getRecord(deviceSql);
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
		setDev.value = "ADDR=" + testIp + ";TIMES=" + timeOut;
		setDev.version = "2c";
		logger.debug("-----------------++++++++-----------------------");
		logger.debug("device_id=" + setDev.device_id + "-loopback_ip="
				+ setDev.loopback_ip + "-oid=" + setDev.oid);
		logger.debug("port=" + setDev.port + "-set_comm=" + setDev.set_comm + "-timeout="
				+ setDev.timeout);
		logger.debug("value=" + setDev.value + "-version=" + setDev.version);
		SnmpGwCheck.PingDev[] pingDevArr = new SnmpGwCheck.PingDev[1];
		pingDevArr[0] = setDev;
		// ????????????corba??????
		SnmpGwCheck.PingData[] resultData = SnmpGwCheckInterface.GetInstance().SnmpPing(
				pingDevArr, deviceId);
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
		if (resultData == null || resultData.length == 0)
		{
			serviceHtml += "<tr class=''>";
			serviceHtml += "<td bgcolor='#FFFFFF' colspan='3' nowrap>??????corba?????????";
			serviceHtml += "</td></tr>";
		}
		else
		{
			for (int i = 0; i < resultData.length; i++)
			{
				time = (String) resultData[i].times;
				miss = (String) resultData[i].miss;
				logger.debug("time :" + time);
				logger.debug("miss :" + miss);
				boolean existed = time == null || (time.equals("") && miss.equals(""));
				if (existed)
				{
					serviceHtml += "<tr class=''><td bgcolor='#FFFFFF' width='30%' nowrap colspan='3'> ?????????????????????????????????!";
					serviceHtml += "</td></tr>";
				}
				else
				{
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
	public String PingList(HttpServletRequest request)
	{
		String deviceId = request.getParameter("device_id");
		//??????????????????
		String gwType = request.getParameter("gw_type");
//		String gather_id = "";
		String interFace = request.getParameter("Interface");
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan???ping????????????????????? begin
		if (interFace
				.startsWith("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig"))
		{
			interFace = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
			logger
					.debug("??????ping?????????LAN????????????InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1");
		}
		// add by panyin for bug XJDX-ITMS-BUG-20101117-XXF-001 lan???ping????????????????????? end
		String host = request.getParameter("Host");
		String numberOfRepetitions = request.getParameter("NumberOfRepetitions");
		String timeOut = request.getParameter("Timeout");
		String dataBlockSize = request.getParameter("DataBlockSize");
		// String DSCP = request.getParameter("DSCP");
		// String strAction = request.getParameter("action");
		DevRpc[] devRpcArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>(1);
		String serviceHtml = "";
		String strSql1 = "select *  from tab_gw_device where device_id='" + deviceId
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + deviceId
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSql1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSql1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 ??????????????????????????????????????????????????????
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 ??????????????????????????????????????????????????????
		String out = (String) fields1.get("oui");
		String serialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(deviceId, out + "-" + serialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[7];
		parameterValueStruct[0] = new ParameterValueStruct();
		parameterValueStruct[0]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		parameterValueStruct[0].setValue(anyObject);
		parameterValueStruct[1] = new ParameterValueStruct();
		parameterValueStruct[1]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Interface");
		anyObject = new AnyObject();
		// + ".";????????????????????????????????????
		anyObject.para_value = interFace;
		anyObject.para_type_id = "1";
		parameterValueStruct[1].setValue(anyObject);
		parameterValueStruct[2] = new ParameterValueStruct();
		parameterValueStruct[2].setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = host;
		anyObject.para_type_id = "1";
		parameterValueStruct[2].setValue(anyObject);
		parameterValueStruct[3] = new ParameterValueStruct();
		parameterValueStruct[3]
				.setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = numberOfRepetitions;
		anyObject.para_type_id = "3";
		parameterValueStruct[3].setValue(anyObject);
		parameterValueStruct[4] = new ParameterValueStruct();
		parameterValueStruct[4]
				.setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = timeOut;
		anyObject.para_type_id = "3";
		parameterValueStruct[4].setValue(anyObject);
		parameterValueStruct[5] = new ParameterValueStruct();
		parameterValueStruct[5]
				.setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = dataBlockSize;
		anyObject.para_type_id = "3";
		parameterValueStruct[5].setValue(anyObject);
		parameterValueStruct[6] = new ParameterValueStruct();
		parameterValueStruct[6].setName("InternetGatewayDevice.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = "0";
		// unsignedInt
		anyObject.para_type_id = "3";
		parameterValueStruct[6].setValue(anyObject);
		setParameterValues.setParameterList(parameterValueStruct);
		setParameterValues.setParameterKey("Ping");
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		// corba
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRpcRep = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcRep = devRpcManager.execRPC(devRpcArr, Global.DiagCmd_Type);
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
		Map pingMap = null;
		if (devRpcRep == null || devRpcRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else if (devRpcRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else
		{
			int stat = devRpcRep.get(0).getStat();
			if (stat != 1)
			{
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			}
			else
			{
				errMessage = "??????????????????";
				if (devRpcRep.get(0).getRpcList() == null
						|| devRpcRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcRep.get(0)
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", deviceId, resp);
								Fault fault = null;
								if (StringUtil.IsEmpty(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											deviceId);
								}
								else
								{
									SoapOBJ soapObj = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapObj != null)
									{
										fault = XmlToRpc.Fault(soapObj.getRpcElement());
										Element element = soapObj.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												pingMap = new HashMap<String, String>(parameterValueStructArr.length);
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													pingMap
															.put(
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
			logger.debug("GSJ------------------------:::" + devRpcRep.size());
			logger.debug("GSJ------------------------:::" + devRpcRep.get(0).getStat());
			String deviceName = osMap.get(deviceId);
			if (pingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += deviceName;
				serviceHtml += "</td><td colspan='7'><span><font color=red>" + errMessage
						+ "</font></span></td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + pingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += deviceName;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += (StringUtil.getIntegerValue(pingMap
				.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount"))/StringUtil.getIntegerValue(numberOfRepetitions))*100+""+"%";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += host;
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

		StringBuffer sb = new StringBuffer();
		/**  ????????? **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(numberOfRepetitions).append("</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap =  getSuggested(4);
		// ?????????
		String biasValue = (String)pingSuggestMap.get("ex_bias");

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

		if(pingMap != null){

			/**  ???????????? **/

			String suggestRegular = (String)pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

			// ?????????
			int successCount = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount")));
			int numberOfRepetitionsInt = Integer.parseInt(numberOfRepetitions);
			sb.append(judgeIntValue(numberOfRepetitionsInt, successCount, "=", 0));

			// ?????????
			int failCount = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, failCount, "=", 0));

			// ????????????
			int averageTime = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), averageTime, suggestRegular, 0));

			int minTime = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), minTime, suggestRegular, 0));

			int maxTime = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), maxTime, suggestRegular, 0));

			// ?????????
			int failureCount = (StringUtil.getIntegerValue(pingMap
					.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount"))/StringUtil.getIntegerValue(numberOfRepetitions))*100;
			sb.append(judgeIntValue(0, failureCount, "=", 0));

			// host
			sb.append("<td bgcolor='#FFFFFF' nowrap>");
			sb.append("").append("</td>");

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		}
		else
		{
			sb.append("<tr>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");
			sb.append("<td bgcolor='#FFFFFF' nowrap colspan=7><font color=red>");
			sb.append("??????").append("</font></td></tr>");
			serviceHtml += sb.toString() + "</table>";
		}

		return serviceHtml;
	}

	public Map getSuggested(int id)
	{
		String sql = "select * from gw_egw_expert where id=?";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select id, ex_name, ex_regular, ex_bias, ex_succ_desc, ex_fault_desc, ex_suggest, ex_desc " +
					"from gw_egw_expert where id=?";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setInt(1, id);
		Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
		Map fields1 = cursor1.getNext();
		return fields1;
	}

	/**
	 * ?????????
	 * @param biasValue  ?????????
	 * @param factValue  ?????????
	 * @param regular    ????????????
	 * @param orValue    ???????????????
	 */
	public String judgeIntValue(double biasValue, double factValue, String regular, double orValue)
	{
		StringBuffer sb = new StringBuffer();
		// ??????
		if(LESS.equals(regular))
		{
			if(factValue < biasValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}
		// ??????
		else if(GRATER.equals(regular))
		{
			if(factValue > biasValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}
		// ??????
		else if(EQUAL.equals(regular))
		{
			// ?????? factValue ??? biasValue ???????????? 
			double diff = 1e-6;
			if(Math.abs(factValue - biasValue) < diff)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}

		// ?????????
		else if(LESS_GRATER.equals(regular))
		{
			if(biasValue - orValue < factValue && factValue < biasValue + orValue)
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=green>");
				sb.append("??????").append("</font></td>");
			}
			else
			{
				sb.append("<td bgcolor='#FFFFFF' nowrap><font color=red>");
				sb.append("??????").append("</font></td>");
			}
		}else
		{
			//???????????????????????????
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
	public String allHttpGetResult(HttpServletRequest request)
	{
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (devicetype.equals(TR069))
		{
			strResult = HttpGetList(request);
		}
//		else
//		{
//			// ??????SnmpGwCheck ??????
//			strResult = snmpHttpGet(request);
//		}
		return strResult;
	}

	/**
	 * tr069 ??? snmp ??????DNSQuery ??????
	 *
	 * @author zhangcong ???67706???
	 * @param request
	 * @return
	 */
	public String allDNSQueryResult(HttpServletRequest request)
	{
		// ????????????
		String strResult = "";
		// ???????????????tr069 ?????? snmp
		String devicetype = request.getParameter("devicetype");
		logger.debug("devicetype :" + devicetype);
		// ?????????tr069???????????????ACS ??????
		if (devicetype.equals(TR069))
		{
			strResult = DNSQueryList(request);
		}
//		else
//		{
//			// ??????SnmpGwCheck ??????
//			strResult = snmpHttpGet(request);
//		}
		return strResult;
	}

//	/**
//	 * SNMP??????HttpGet???????????????corba??????
//	 *
//	 * @author zhangcong ???67706???
//	 * @param request
//	 * @return
//	 */
//	public String snmpHttpGet(HttpServletRequest request)
//	{
//		// ??????ID
//		String device_id = request.getParameter("device_id");
//		// ??????IP
//		String test_ip = request.getParameter("test_ip");
//		// ????????????ms
//		String time_out = request.getParameter("time_out");
//		logger.debug("device_id :" + device_id);
//		logger.debug("test_ip :" + test_ip);
//		logger.debug("time_out :" + time_out);
//		// ??????????????????oid ???????????????????????? ??????ping???????????????OID
//		String StrSQL = "select * from tab_gw_model_oper_oid where device_model = "
//				+ "(select device_model_id from tab_gw_device where device_id= '"
//				+ device_id + "') and oid_type =3";
//		PrepareSQL psql = new PrepareSQL(StrSQL);
//		psql.getSQL();
//		Map oidMap = DataSetBean.getRecord(StrSQL);
//		// ??????????????????
//		StrSQL = "select *  from  sgw_security where device_id='" + device_id + "'";
//		String deviceSQL = "select loopback_ip,snmp_udp from tab_gw_device where device_id='"
//				+ device_id + "'";
//		psql = new PrepareSQL(StrSQL);
//		psql.getSQL();
//		Map deviceMap = DataSetBean.getRecord(StrSQL);
//		psql = new PrepareSQL(deviceSQL);
//		psql.getSQL();
//		Map gwMap = DataSetBean.getRecord(deviceSQL);
//		// ???????????????????????????
//		SnmpGwCheck.PingDev setDev = new SnmpGwCheck.PingDev();
//		setDev.device_id = (String) deviceMap.get("device_id");
//		// ??????
//		setDev.loopback_ip = (String) gwMap.get("loopback_ip");
//		// oid
//		setDev.oid = (String) oidMap.get("oid");
//		// ??????????????????????????????snmp??????
//		setDev.port = Integer.parseInt((String) gwMap.get("snmp_udp"));
//		// ?????????
//		setDev.set_comm = (String) deviceMap.get("snmp_w_passwd");
//		setDev.timeout = 60;
//		setDev.value = "ADDR=" + test_ip + ";TIMES=" + time_out;
//		setDev.version = "2c";
//		logger.debug("-----------------++++++++-----------------------");
//		logger.debug("device_id=" + setDev.device_id + "-loopback_ip="
//				+ setDev.loopback_ip + "-oid=" + setDev.oid);
//		logger.debug("port=" + setDev.port + "-set_comm=" + setDev.set_comm + "-timeout="
//				+ setDev.timeout);
//		logger.debug("value=" + setDev.value + "-version=" + setDev.version);
//		SnmpGwCheck.PingDev[] pingDev_arr = new SnmpGwCheck.PingDev[1];
//		pingDev_arr[0] = setDev;
//		// ????????????corba??????
//		SnmpGwCheck.PingData[] resultData = SnmpGwCheckInterface.GetInstance().SnmpPing(
//				pingDev_arr, device_id);
//		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
//		serviceHtml += "<tr class='blue_foot'>";
//		serviceHtml += "<td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
//		serviceHtml += "????????????";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
//		serviceHtml += "??????????????????(ms)";
//		serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' align='center' nowrap>";
//		serviceHtml += "???????????????(%)";
//		serviceHtml += "</td></tr>";
//		String time = "";
//		String miss = "";
//		if (resultData == null || resultData.length == 0)
//		{
//			serviceHtml += "<tr class=''>";
//			serviceHtml += "<td bgcolor='#FFFFFF' colspan='3' nowrap>??????corba?????????";
//			serviceHtml += "</td></tr>";
//		}
//		else
//		{
//			for (int i = 0; i < resultData.length; i++)
//			{
//				time = (String) resultData[i].times;
//				miss = (String) resultData[i].miss;
//				logger.debug("time :" + time);
//				logger.debug("miss :" + miss);
//				if (time == null || (time.equals("") && miss.equals("")))
//				{
//					serviceHtml += "<tr class=''><td bgcolor='#FFFFFF' width='30%' nowrap colspan='3'> ?????????????????????????????????!";
//					serviceHtml += "</td></tr>";
//				}
//				else
//				{
//					serviceHtml += "<tr class=''>";
//					serviceHtml += "<td bgcolor='#FFFFFF' width='30%' nowrap>";
//					Map fields = DataSetBean
//							.getRecord("select loopback_ip from tab_gw_device where device_id='"
//									+ resultData[i].device_id + "'");
//					serviceHtml += fields.get("loopback_ip");
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
//					serviceHtml += StringUtils.formatString(time, 2);
//					serviceHtml += "</td><td bgcolor='#FFFFFF' width='30%' nowrap>";
//					serviceHtml += StringUtils.formatString(miss, 2);
//					serviceHtml += "</td></tr>";
//					fields = null;
//				}
//			}
//		}
//		return serviceHtml;
//	}

	/**
	 * ??????Ping??????????????????
	 * zhangcong@
	 *
	 * @param request
	 * @return
	 */
	public String HttpGetList(HttpServletRequest request)
	{
		String deviceId = request.getParameter("device_id");
		//??????????????????
		String gwType = request.getParameter("gw_type");
		String gatherId = "";
		String interFace = request.getParameter("Interface");

		String url = request.getParameter("URL");
		String numberOfRepetitions = request.getParameter("NumberOfRepetitions");
		String timeOut = request.getParameter("Timeout");
		String httpVersion = request.getParameter("httpVersion");
		DevRpc[] devRpcArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>(1);
		String serviceHtml = "";
		String strSql1 = "select *  from tab_gw_device where device_id='" + deviceId
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + deviceId
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSql1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSql1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String serialNumber = (String) fields1.get("device_serialnumber");
		osMap.put(deviceId, out + "-" + serialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[6];
		//Requested
		parameterValueStruct[0] = new ParameterValueStruct();
		parameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		parameterValueStruct[0].setValue(anyObject);
		//??????????????????WAN
		parameterValueStruct[1] = new ParameterValueStruct();
		parameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = interFace;
		anyObject.para_type_id = "1";
		parameterValueStruct[1].setValue(anyObject);
		//??????URL
		parameterValueStruct[2] = new ParameterValueStruct();
		parameterValueStruct[2].setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.URL");
		anyObject = new AnyObject();
		anyObject.para_value = url;
		anyObject.para_type_id = "1";
		parameterValueStruct[2].setValue(anyObject);
		//????????????
		parameterValueStruct[3] = new ParameterValueStruct();
		parameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = numberOfRepetitions;
		anyObject.para_type_id = "3";
		parameterValueStruct[3].setValue(anyObject);
		//????????????
		parameterValueStruct[4] = new ParameterValueStruct();
		parameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = timeOut;
		anyObject.para_type_id = "3";
		parameterValueStruct[4].setValue(anyObject);
		//HTTP??????
		parameterValueStruct[5] = new ParameterValueStruct();
		parameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.HttpVersion");
		anyObject = new AnyObject();
		anyObject.para_value = httpVersion;
		anyObject.para_type_id = "3";
		parameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(parameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		//????????????
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		List<DevRpcCmdOBJ> devRpcRep = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcRep = devRpcManager.execRPC(devRpcArr, Global.DiagCmd_Type);
		String errMessage = "";
		Map pingMap = null;
		if (devRpcRep == null || devRpcRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else if (devRpcRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else
		{
			int stat = devRpcRep.get(0).getStat();
			if (stat != 1)
			{
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			}
			else
			{
				errMessage = "??????????????????";
				if (devRpcRep.get(0).getRpcList() == null
						|| devRpcRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcRep.get(0)
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", deviceId, resp);
								Fault fault = null;
								if (StringUtil.IsEmpty(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											deviceId);
								}
								else
								{
									SoapOBJ soapObj = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapObj != null)
									{
										fault = XmlToRpc.Fault(soapObj.getRpcElement());
										Element element = soapObj.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												pingMap = new HashMap<String, String>(parameterValueStructArr.length);
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													pingMap
															.put(
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
			logger.debug("GSJ------------------------:::" + devRpcRep.size());
			logger.debug("GSJ------------------------:::" + devRpcRep.get(0).getStat());
			String deviceName = osMap.get(deviceId);
			//????????????????????????????????????????????????????????????
			if (pingMap == null)
			{
				serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'><tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += deviceName;
				serviceHtml += "</td><td colspan='5'><span><font color=red>" + errMessage
						+ "</font></span></td></tr></table>";

				//????????????????????????
				return serviceHtml;
			}
			else
			{
				logger.debug("F-----------:" + pingMap);
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
				serviceHtml += deviceName;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
			Map<String,String> expertMap = getSuggested(7);
			int exBias = -99;
			String exRegular = "";
			if(null != expertMap)
			{
				exBias = StringUtil.getIntegerValue(expertMap.get("ex_bias"));
				exRegular = expertMap.get("ex_regular");
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "?????????";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += numberOfRepetitions;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += 0;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += exBias;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += exBias;
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += exBias;
				serviceHtml += "</td></tr>";

				logger.debug("F-----------:" + pingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += "????????????";
				serviceHtml += "</td>";
				serviceHtml += judgeIntValue(StringUtil.getIntegerValue(numberOfRepetitions), StringUtil.getIntegerValue(pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.SuccessCount")), "=", 0);
				serviceHtml += judgeIntValue(0, StringUtil.getIntegerValue(pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.FailureCount")), "=", 0);
				serviceHtml += judgeIntValue(exBias, StringUtil.getIntegerValue(pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.AverageResponseTime")), exRegular, 0);
				serviceHtml += judgeIntValue(exBias, StringUtil.getIntegerValue(pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MinimumResponseTime")), exRegular, 0);
				serviceHtml += judgeIntValue(exBias, StringUtil.getIntegerValue(pingMap
						.get("InternetGatewayDevice.X_CT-COM_HttpGetDiagnostics.MaximumResponseTime")), exRegular, 0);
				serviceHtml += "</tr>";
			}else
			{
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
	 * ??????Ping??????????????????
	 * zhangcong@
	 *
	 * @param request
	 * @return
	 */
	public String DNSQueryList(HttpServletRequest request)
	{
		String deviceId = request.getParameter("device_id");
		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);
		String gatherId = "";
		String interFace = request.getParameter("Interface");

		String dnsServerIp = request.getParameter("DNSServerIP");
		String numberOfRepetitions = request.getParameter("NumberOfRepetitions");
		String timeOut = request.getParameter("Timeout");
		String domainName = request.getParameter("DomainName");
		DevRpc[] devRpcArr = new DevRpc[1];

		Map<String, String> osMap = new HashMap<String, String>(1);
		String serviceHtml = "";
		String strSql1 = "select *  from tab_gw_device where device_id='" + deviceId
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='" + deviceId
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSql1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSql1);
		Map fields1 = cursor1.getNext();
		// begin add by w5221 ??????????????????????????????????????????????????????
		// gather_id = (String) fields1.get("gather_id");
		// end add by w5221 ??????????????????????????????????????????????????????
		String out = (String) fields1.get("oui");
		String serialNumber = (String) fields1.get("device_serialnumber");
		// String ip = (String) fields1.get("loopback_ip");
		// String Port = (String) fields1.get("cr_port");
		// String path = (String) fields1.get("cr_path");
		// String username = (String) fields1.get("acs_username");
		// String passwd = (String) fields1.get("acs_passwd");
		// add by lizhaojun ----2007-06-21
		osMap.put(deviceId, out + "-" + serialNumber);
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[6];
		//Requested
		parameterValueStruct[0] = new ParameterValueStruct();
		parameterValueStruct[0]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		parameterValueStruct[0].setValue(anyObject);
		//??????????????????WAN
		parameterValueStruct[1] = new ParameterValueStruct();
		parameterValueStruct[1]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = interFace;
		anyObject.para_type_id = "1";
		parameterValueStruct[1].setValue(anyObject);
		//??????URL
		parameterValueStruct[2] = new ParameterValueStruct();
		parameterValueStruct[2].setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DNSServerIP");
		anyObject = new AnyObject();
		anyObject.para_value = dnsServerIp;
		anyObject.para_type_id = "1";
		parameterValueStruct[2].setValue(anyObject);
		//????????????
		parameterValueStruct[3] = new ParameterValueStruct();
		parameterValueStruct[3]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = numberOfRepetitions;
		anyObject.para_type_id = "3";
		parameterValueStruct[3].setValue(anyObject);
		//????????????
		parameterValueStruct[4] = new ParameterValueStruct();
		parameterValueStruct[4]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = timeOut;
		anyObject.para_type_id = "3";
		parameterValueStruct[4].setValue(anyObject);
		//HTTP??????
		parameterValueStruct[5] = new ParameterValueStruct();
		parameterValueStruct[5]
				.setName("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.DomainName");
		anyObject = new AnyObject();
		anyObject.para_value = domainName;
		anyObject.para_type_id = "1";
		parameterValueStruct[5].setValue(anyObject);
		setParameterValues.setParameterList(parameterValueStruct);
		setParameterValues.setParameterKey("Ping");

		//????????????
		GetParameterValues getParameterValues = new GetParameterValues();
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRpcArr[0].rpcArr = rpcArr;
		// corba
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		List<DevRpcCmdOBJ> devRpcRep = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType+"");
		devRpcRep = devRpcManager.execRPC(devRpcArr, Global.DiagCmd_Type);
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
		Map pingMap = null;
		if (devRpcRep == null || devRpcRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else if (devRpcRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
			errMessage = "??????????????????";
		}
		else
		{
			int stat = devRpcRep.get(0).getStat();
			if (stat != 1)
			{
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
			}
			else
			{
				errMessage = "??????????????????";
				if (devRpcRep.get(0).getRpcList() == null
						|| devRpcRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", deviceId);
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcRep.get(0)
							.getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k)
									.getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]???????????????{}", deviceId, resp);
								Fault fault = null;
								if (StringUtil.IsEmpty(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null",
											deviceId);
								}
								else
								{
									SoapOBJ soapObj = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapObj != null)
									{
										fault = XmlToRpc.Fault(soapObj.getRpcElement());
										Element element = soapObj.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												pingMap = new HashMap<String, String>(parameterValueStructArr.length);
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													pingMap
															.put(
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
			logger.debug("GSJ------------------------:::" + devRpcRep.size());
			logger.debug("GSJ------------------------:::" + devRpcRep.get(0).getStat());
			String deviceName = osMap.get(deviceId);
			if (pingMap == null)
			{
				serviceHtml += "<tr bgcolor='#FFFFFF'>";
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += deviceName;
				serviceHtml += "</td><td colspan='5'><span><font color=red>" + errMessage
						+ "</font></span></td></tr>";
			}
			else
			{
				logger.debug("F-----------:" + pingMap);
				serviceHtml += "<tr class='blue_foot'>";
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += deviceName;
				serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime");
				serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
				serviceHtml += pingMap
						.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime");
				serviceHtml += "</td></tr>";
			}
		}

		// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

		StringBuffer sb = new StringBuffer();
		/**  ????????? **/
		sb.append("<tr class='blue_foot'>");
		sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(numberOfRepetitions).append("</td>");
		// ?????????
		sb.append("<td bgcolor='#FFFFFF' nowrap>");
		sb.append(0).append("</td>");

		Map pingSuggestMap =  getSuggested(6);
		// ?????????
		String biasValue = (String)pingSuggestMap.get("ex_bias");

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

		if(pingMap != null){

			/**  ???????????? **/
			String suggestRegular = (String)pingSuggestMap.get("ex_regular");

			sb.append("<tr class='blue_foot'>");
			sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

			// ?????????
			int successCount = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.SuccessCount")));
			int numberOfRepetitionsInt = Integer.parseInt(numberOfRepetitions);
			sb.append(judgeIntValue(numberOfRepetitionsInt, successCount, "=", 0));

			// ?????????
			int failCount = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.FailureCount")));
			sb.append(judgeIntValue(0, failCount, "=", 0));

			// ????????????
			int averageTime = Integer.valueOf(String.valueOf(pingMap.get("InternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.AverageResponseTime")));
			sb.append(judgeIntValue(Integer.parseInt(biasValue), averageTime, suggestRegular, 0));

			String tempMinTime = String.valueOf(pingMap.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MinimumResponseTime"));
			int minTime = 0;
			if(!(null==tempMinTime || NULL.equals(tempMinTime))){
				minTime = Integer.valueOf(tempMinTime);
			}
			sb.append(judgeIntValue(Integer.parseInt(biasValue), minTime, suggestRegular, 0));

			String tempMaxTime = String.valueOf(pingMap.get("IInternetGatewayDevice.X_CT-COM_DNSQueryDiagnostics.MaximumResponseTime"));
			int maxTime = 0;
			if(!(null==tempMaxTime || NULL.equals(tempMaxTime))){
				maxTime = Integer.valueOf(tempMaxTime);
			}
			sb.append(judgeIntValue(Integer.parseInt(biasValue), maxTime, suggestRegular, 0));

			//
			sb.append("</tr>");

			// end add

			serviceHtml += sb.toString() + "</table>";
		}
		else
		{
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
	public String ATMF5LOOPList(HttpServletRequest request)
	{
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		String deviceId = request.getParameter("device_id");

		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);

		String[] arrDeviceId = deviceId.split(",");
		String gatherId = "";
		String param = request.getParameter("ATMF5");
		String numberOfRepetions = request.getParameter("NumberOfRepetions");
		String timeOut = request.getParameter("Timeout");
		DevRpc[] devRpcArr = new DevRpc[arrDeviceId.length];
		for (int i = 0; i < arrDeviceId.length; i++)
		{
			String strSql1 = "select *  from tab_gw_device where device_id='"
					+ arrDeviceId[i] + "'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSql1 = "select gather_id, oui, device_serialnumber, loopback_ip, cr_port, cr_path, acs_username, acs_passwd " +
						"from tab_gw_device where device_id='"
						+ arrDeviceId[i] + "'";
			}
			com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSql1);
			psql.getSQL();
			Cursor cursor1 = DataSetBean.getCursor(strSql1);
			Map fields1 = cursor1.getNext();
			// begin add by w5221 ??????????????????????????????????????????????????????
			gatherId = (String) fields1.get("gather_id");
			// end add by w5221 ??????????????????????????????????????????????????????
			String out = (String) fields1.get("oui");
			String serialNumber = (String) fields1.get("device_serialnumber");
			String ip = (String) fields1.get("loopback_ip");
			String port = (String) fields1.get("cr_port");
			String path = (String) fields1.get("cr_path");
			String username = (String) fields1.get("acs_username");
			String passwd = (String) fields1.get("acs_passwd");
			AnyObject anyObject = new AnyObject();
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[3];
			parameterValueStruct[0] = new ParameterValueStruct();
			parameterValueStruct[0].setName(param
					+ "WANATMF5LoopbackDiagnostics.DiagnosticsState");
			anyObject.para_value = "Requested";
			anyObject.para_type_id = "1";
			parameterValueStruct[0].setValue(anyObject);
			parameterValueStruct[1] = new ParameterValueStruct();
			parameterValueStruct[1].setName(param
					+ "WANATMF5LoopbackDiagnostics.NumberOfRepetitions");
			anyObject = new AnyObject();
			anyObject.para_value = numberOfRepetions;
			anyObject.para_type_id = "3";
			parameterValueStruct[1].setValue(anyObject);
			parameterValueStruct[2] = new ParameterValueStruct();
			parameterValueStruct[2].setName(param
					+ "WANATMF5LoopbackDiagnostics.Timeout");
			anyObject = new AnyObject();
			anyObject.para_value = timeOut;
			anyObject.para_type_id = "3";
			parameterValueStruct[2].setValue(anyObject);
			setParameterValues.setParameterList(parameterValueStruct);
			setParameterValues.setParameterKey("ATMF5LOOP");
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[5];
			parameterNamesArr[0] = param + "WANATMF5LoopbackDiagnostics.SuccessCount";
			parameterNamesArr[1] = param + "WANATMF5LoopbackDiagnostics.FailureCount";
			parameterNamesArr[2] = param
					+ "WANATMF5LoopbackDiagnostics.AverageResponseTime";
			parameterNamesArr[3] = param
					+ "WANATMF5LoopbackDiagnostics.MinimumResponseTime";
			parameterNamesArr[4] = param
					+ "WANATMF5LoopbackDiagnostics.MaximumResponseTime";
			getParameterValues.setParameterNames(parameterNamesArr);
			devRpcArr[i] = new DevRpc();
			devRpcArr[i].devId = deviceId;
			Rpc[] rpcArr = new Rpc[2];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "SetParameterValues";
			rpcArr[0].rpcValue = setParameterValues.toRPC();
			rpcArr[1] = new Rpc();
			rpcArr[1].rpcId = "2";
			rpcArr[1].rpcName = "GetParameterValues";
			rpcArr[1].rpcValue = getParameterValues.toRPC();
			devRpcArr[i].rpcArr = rpcArr;
		}
		// corba
		List<DevRpcCmdOBJ> devRpcCmdObjList = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcCmdObjList = devRpcManager.execRPC(devRpcArr, Global.DiagCmd_Type);
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
		for (int j = 0; devRpcCmdObjList != null && j < devRpcCmdObjList.size(); j++)
		{
			Map pingMap = null;
			ParameterValueStruct[] pingStruct = null;
			DevRpcCmdOBJ devRpcCmdObj = devRpcCmdObjList.get(j);
			if (devRpcCmdObj!= null)
			{
				String strSql1 = "select *  from tab_gw_device where device_id='"
						+ devRpcCmdObj.getDevId() + "'";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
							+ devRpcCmdObj.getDevId() + "'";
				}
				PrepareSQL psql = new PrepareSQL(strSql1);
				psql.getSQL();
				Cursor cursor1 = DataSetBean.getCursor(strSql1);
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String serialNumber = (String) fields1.get("device_serialnumber");
				String deviceName = out + "-" + serialNumber;
				String errMessage = "";
				int stat = devRpcCmdObj.getStat();
				if (stat != 1)
				{
					errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				}
				else
				{
					errMessage = "??????????????????";
					if (devRpcCmdObj.getRpcList() == null
							|| devRpcCmdObj.getRpcList().size() == 0)
					{
						logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", devRpcCmdObj
								.getDevId());
					}
					else
					{
						List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcCmdObj
								.getRpcList();
						if (rpcList != null && !rpcList.isEmpty())
						{
							for (int k = 0; k < rpcList.size(); k++)
							{
								if ("GetParameterValuesResponse".equals(rpcList.get(k)
										.getRpcName()))
								{
									String resp = rpcList.get(k).getValue();
									logger.warn("[{}]???????????????{}", devRpcCmdObj.getDevId(),
											resp);
									Fault fault = null;
									if (StringUtil.IsEmpty(resp))
									{
										logger.debug("[{}]DevRpcCmdOBJ.value == null",
												devRpcCmdObj.getDevId());
									}
									else
									{
										SoapOBJ soapObj = XML.getSoabOBJ(XML
												.CreateXML(resp));
										if (soapObj != null)
										{
											Element element = soapObj.getRpcElement();
											if (element != null)
											{
												GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
														.GetParameterValuesResponse(element);
												if (getParameterValuesResponse != null)
												{
													pingStruct = new ParameterValueStruct[5];
													pingStruct = getParameterValuesResponse
															.getParameterList();
													for (int l = 0; l < pingStruct.length; l++)
													{
														if(pingMap!=null)
														{
															pingMap.put(pingStruct[l].getName(), 
																		pingStruct[l].getValue().para_value);
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
				}
				if (null == pingStruct || pingMap == null)
				{
					serviceHtml += "<tr bgcolor='#FFFFFF'>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += deviceName;
					serviceHtml += "</td><td colspan='5'><span>" + errMessage
							+ "</span></td></tr>";
				}
				else
				{
					serviceHtml += "<tr class='blue_foot'>";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += deviceName;
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += pingMap.get(param
							+ "WANATMF5LoopbackDiagnostics.SuccessCount");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += pingMap.get(param
							+ "WANATMF5LoopbackDiagnostics.FailureCount");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += pingMap.get(param
							+ "WANATMF5LoopbackDiagnostics.AverageResponseTime");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += pingMap.get(param
							+ "WANATMF5LoopbackDiagnostics.MinimumResponseTime");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += pingMap.get(param
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
	 * @param request request
	 * @param gw_type gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public Map ChongqiList(HttpServletRequest request,String gwType)
	{
		logger.debug("FileSevice=>ChongqiList({}) ",gwType);
		
		String deviceIds = request.getParameter("device_id");
		String[] arrDeviceId = deviceIds.split(",");
		DevRpc[] devRpcArr = new DevRpc[arrDeviceId.length];
		for (int i = 0; i < devRpcArr.length; i++)
		{
			Reboot reboot = new Reboot();
			reboot.setCommandKey("Reboot");
			devRpcArr[i] = new DevRpc();
			devRpcArr[i].devId = arrDeviceId[i];
			Rpc[] rpcArr = new Rpc[1];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "Reboot";
			rpcArr[0].rpcValue = reboot.toRPC();
			devRpcArr[i].rpcArr = rpcArr;
		}
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		List<DevRpcCmdOBJ> devRpcCmdObjList = devRpcManager.execRPC(devRpcArr,
				Global.RpcCmd_Type);
		if (devRpcCmdObjList == null || devRpcCmdObjList.size() == 0)
		{
			logger.warn("List<DevRpcCmdOBJ>???????????????");
			return new HashMap<String, String>();
		}
		
		HashMap<String, String> resultMap = new HashMap<String, String>(devRpcCmdObjList.size());
		
		for (int j = 0; j < devRpcCmdObjList.size(); j++)
		{
			DevRpcCmdOBJ devRpcCmdObj = devRpcCmdObjList.get(j);
			if (devRpcCmdObj!= null)
			{
				String strSql1 = "select *  from tab_gw_device where device_id='"
						+ devRpcCmdObj.getDevId() + "'";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
							+ devRpcCmdObj.getDevId() + "'";
				}
				PrepareSQL psql = new PrepareSQL(strSql1);
				Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String serialNumber = (String) fields1.get("device_serialnumber");
				int flag = devRpcCmdObj.getStat();
				if (flag == 1)
				{
					resultMap.put(out + "-" + serialNumber, "????????????");
				}
				else
				{
					resultMap.put(out + "-" + serialNumber, "????????????");
				}
			}
		}
		return resultMap;
	}

	/**
	 * ?????????????????????????????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @param gw_type gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public Map HuifuList(HttpServletRequest request,String gwType)
	{
		String deviceId = request.getParameter("device_id");
		String[] arrDeviceId = deviceId.split(",");
		DevRpc[] devRpcArr = new DevRpc[arrDeviceId.length];
		for (int i = 0; i < devRpcArr.length; i++)
		{
			FactoryReset factoryReset = new FactoryReset();
			devRpcArr[i] = new DevRpc();
			devRpcArr[i].devId = arrDeviceId[i];
			Rpc[] rpcArr = new Rpc[1];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "FactoryReset";
			rpcArr[0].rpcValue = factoryReset.toRPC();
			devRpcArr[i].rpcArr = rpcArr;
		}
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		List<DevRpcCmdOBJ> devRpcCmdObjList = devRpcManager.execRPC(devRpcArr,
				Global.RpcCmd_Type);
		if (devRpcCmdObjList == null || devRpcCmdObjList.size() == 0)
		{
			logger.warn("List<DevRpcCmdOBJ>???????????????");
			return new HashMap<String, String>();
		}
		
		HashMap<String, String> resultMap = new HashMap<String, String>(devRpcCmdObjList.size());
		
		for (int j = 0; j < devRpcCmdObjList.size(); j++)
		{
			DevRpcCmdOBJ devRpcCmdObj = devRpcCmdObjList.get(j);
			if (devRpcCmdObj!= null)
			{
				String strSql1 = "select *  from tab_gw_device where device_id='"
						+ devRpcCmdObj.getDevId() + "'";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
							+ devRpcCmdObj.getDevId() + "'";
				}
				PrepareSQL psql = new PrepareSQL(strSql1);
				Cursor cursor1 = DataSetBean.getCursor(psql.getSQL());
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String serialNumber = (String) fields1.get("device_serialnumber");
				int flag = devRpcCmdObj.getStat();
				if (flag == 1)
				{
					resultMap.put(out + "-" + serialNumber, "????????????????????????");
				}
				else
				{
					resultMap.put(out + "-" + serialNumber, "????????????????????????");
				}
			}
		}
		return resultMap;
	}

	/**
	 * ??????DSL?????????????????? liuli@lianchuang.com
	 *
	 * @param request
	 * @param gw_type gw_type
	 * @return
	 * @see GWTypeENUM
	 */
	public String DSLList(HttpServletRequest request,String gwType)
	{
		String deviceId = request.getParameter("device_id");
		String dslWan = request.getParameter("dslWan");
		String[] arrDeviceId = deviceId.split(",");
		DevRpc[] devRpcArr = new DevRpc[arrDeviceId.length];
		logger.debug("LZJ>>>>> arrDevice_id.length :" + arrDeviceId.length);
		for (int i = 0; i < arrDeviceId.length; i++)
		{
			SetParameterValues setParameterValues = new SetParameterValues();
			ParameterValueStruct[] parameterValueStruct = new ParameterValueStruct[1];
			parameterValueStruct[0] = new ParameterValueStruct();
			parameterValueStruct[0]
					.setName(dslWan + "WANDSLDiagnostics.LoopDiagnosticsState");
			AnyObject anyObject = new AnyObject();
			anyObject.para_value = "Requested";
			anyObject.para_type_id = "1";
			parameterValueStruct[0].setValue(anyObject);
			setParameterValues.setParameterList(parameterValueStruct);
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
			devRpcArr[i] = new DevRpc();
			devRpcArr[i].devId = deviceId;
			Rpc[] rpcArr = new Rpc[2];
			rpcArr[0] = new Rpc();
			rpcArr[0].rpcId = "1";
			rpcArr[0].rpcName = "SetParameterValues";
			rpcArr[0].rpcValue = setParameterValues.toRPC();
			rpcArr[1] = new Rpc();
			rpcArr[1].rpcId = "2";
			rpcArr[1].rpcName = "GetParameterValues";
			rpcArr[1].rpcValue = getParameterValues.toRPC();
			devRpcArr[i].rpcArr = rpcArr;
		}
		// corba
		List<DevRpcCmdOBJ> devRpcCmdObjList = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcCmdObjList = devRpcManager.execRPC(devRpcArr, Global.DiagCmd_Type);
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
				+ devRpcCmdObjList.size());

		StringBuffer sb = new StringBuffer();
		for (int j = 0; devRpcCmdObjList != null && j < devRpcCmdObjList.size(); j++)
		{
			Map dslMap = null;
			ParameterValueStruct[] dslStruct = null;
			DevRpcCmdOBJ devRpcCmdObj = devRpcCmdObjList.get(j);
			if (devRpcCmdObj != null)
			{
				String strSql1 = "select *  from tab_gw_device where device_id='"
						+ devRpcCmdObj.getDevId() + "'";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					strSql1 = "select oui, device_serialnumber from tab_gw_device where device_id='"
							+ devRpcCmdObj.getDevId() + "'";
				}
				PrepareSQL psql = new PrepareSQL(strSql1);
				psql.getSQL();
				Cursor cursor1 = DataSetBean.getCursor(strSql1);
				Map fields1 = cursor1.getNext();
				String out = (String) fields1.get("oui");
				String serialNumber = (String) fields1.get("device_serialnumber");
				String deviceName = out + "-" + serialNumber;
				String errMessage = "";
				int stat = devRpcCmdObj.getStat();
				if (stat != 1)
				{
					errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				}
				else
				{
					errMessage = "??????????????????";
					if (devRpcCmdObj.getRpcList() == null
							|| devRpcCmdObj.getRpcList().size() == 0)
					{
						logger.warn("[{}]List<ACSRpcCmdOBJ>???????????????", devRpcCmdObj
								.getDevId());
					}
					else
					{
						List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRpcCmdObj
								.getRpcList();
						if (rpcList != null && !rpcList.isEmpty())
						{
							for (int k = 0; k < rpcList.size(); k++)
							{
								if ("GetParameterValuesResponse".equals(rpcList.get(k)
										.getRpcName()))
								{
									String resp = rpcList.get(k).getValue();
									logger.warn("[{}]???????????????{}", devRpcCmdObj.getDevId(),
											resp);
									Fault fault = null;
									if (StringUtil.IsEmpty(resp))
									{
										logger.debug("[{}]DevRpcCmdOBJ.value == null",
												devRpcCmdObj.getDevId());
									}
									else
									{
										SoapOBJ soapObj = XML.getSoabOBJ(XML
												.CreateXML(resp));
										if (soapObj != null)
										{
											Element element = soapObj.getRpcElement();
											if (element != null)
											{
												GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
														.GetParameterValuesResponse(element);
												if (getParameterValuesResponse != null)
												{
													dslStruct = getParameterValuesResponse
															.getParameterList();
													dslMap = new HashMap(dslStruct.length);
													for (int l = 0; l < dslStruct.length; l++)
													{
														dslMap
																.put(
																		dslStruct[l]
																				.getName(),
																		dslStruct[l]
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
				if (null == dslStruct || dslMap == null)
				{
					serviceHtml += "<tr bgcolor='#FFFFFF'>";
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += deviceName;
					serviceHtml += "</td><td colspan='10'><span><font color=red>" + errMessage
							+ "</font></span></td></tr>";
				}
				else
				{
					serviceHtml += "<tr class='blue_foot'>";
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += deviceName;
					serviceHtml += "<td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINSCds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.HLINpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.QLNpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.SNRpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.BITSpsds");
					serviceHtml += "</td><td bgcolor='#FFFFFF' width='2%' nowrap>";
					serviceHtml += dslMap
							.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.GAINSpsds");
					serviceHtml += "</td></tr>";
				}

				// begin add by chenjie(67371) 2011-8-9 ??????????????????????????????

				/**  ????????? **/
				sb.append("<tr class='blue_foot'>");
				sb.append("<td bgcolor='#FFFFFF' nowrap>?????????</td>");

				Map actpsddsMap = getSuggested(8);
				Map actpsdusMap = getSuggested(9);
				Map actatpdsMap = getSuggested(10);
				Map actatpusMap = getSuggested(11);

				double actpsdds = Double.valueOf(StringUtil.getStringValue(actpsddsMap.get("ex_bias")));
				double actpsddsOrValue = Double.valueOf(StringUtil.getStringValue(actpsddsMap.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(actpsdds - actpsddsOrValue + " - " + Double.valueOf(actpsdds + actpsddsOrValue)).append("</td>");

				double actpsdus = Double.valueOf(StringUtil.getStringValue(actpsdusMap.get("ex_bias")));
				double actpsdusOrValue = Double.valueOf(StringUtil.getStringValue(actpsdusMap.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(actpsdus - actpsdusOrValue + " - " + Double.valueOf(actpsdds + actpsddsOrValue)).append("</td>");

				double actatpds = Double.valueOf(StringUtil.getStringValue(actatpdsMap.get("ex_bias")));
				double actatpdsOrValue = Double.valueOf(StringUtil.getStringValue(actatpdsMap.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(actatpds - actatpdsOrValue + " - " + Double.valueOf(actatpds + actatpdsOrValue)).append("</td>");

				double actatpus = Double.valueOf(StringUtil.getStringValue(actatpusMap.get("ex_bias")));
				double actatpusOrValue = Double.valueOf(StringUtil.getStringValue(actatpusMap.get("ex_suggest")));
				sb.append("<td bgcolor='#FFFFFF' nowrap>");
				sb.append(actatpus - actatpusOrValue + " - " + Double.valueOf(actatpus + actatpusOrValue)).append("</td>");

				// ?????????????????????null
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("</tr>");

				if(dslMap != null)
				{
				// ????????????
				sb.append("<tr class='blue_foot'>");
				sb.append("<td bgcolor='#FFFFFF' nowrap>????????????</td>");

				double actpsddsFact = Double.valueOf(StringUtil.getStringValue(dslMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDds")));
				String actpsddsRegular = StringUtil.getStringValue(actpsddsMap.get("ex_regular"));
				sb.append(judgeIntValue(actpsdds, actpsddsFact, actpsddsRegular, actpsddsOrValue));

				double actpsdusFact = Double.valueOf(StringUtil.getStringValue(dslMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTPSDus")));
				String actpsdusRegular = StringUtil.getStringValue(actpsdusMap.get("ex_regular"));
				sb.append(judgeIntValue(actpsdus, actpsdusFact, actpsdusRegular, actpsdusOrValue));

				double actatpdsFact = Double.valueOf(StringUtil.getStringValue(dslMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPds")));
				String actatpdsRegular = StringUtil.getStringValue(actatpdsMap.get("ex_regular"));
				sb.append(judgeIntValue(actatpds, actatpdsFact, actatpdsRegular, actatpdsOrValue));

				double actatpusFact = Double.valueOf(StringUtil.getStringValue(dslMap.get("InternetGatewayDevice.WANDevice.1.WANDSLDiagnostics.ACTATPus")));
				String actatpusRegular = StringUtil.getStringValue(actatpusMap.get("ex_regular"));
				sb.append(judgeIntValue(actatpus, actatpusFact, actatpusRegular, actatpusOrValue));

				// ?????????????????????null
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("<td bgcolor='#FFFFFF' nowrap></td>");
				sb.append("</tr>");
				}

				else
				{
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
	public String fileAct(HttpServletRequest request)
	{
		String tableName = "tab_service";
		String column = "service_id";
		int columnNum = 1000;
		
		String strSql = "";
		String strMsg = "";
		ArrayList strSqlList = new ArrayList();
		String serviceId = request.getParameter("lg_id");
		String strAction = request.getParameter("action");
		if (DELETE.equals(strAction))
		{
			strSql = "delete from tab_service where service_id=" + serviceId;
			strSqlList.add(strSql);

			strSql = "delete from tab_servicecode where service_id=" + serviceId;
			strSqlList.add(strSql);
		}
		else
		{
			String strFilename = request.getParameter("lg_name");
			String strLgDesc = request.getParameter("lg_desc");
			if (ADD.equals(strAction))
			{
				// ??????????????????
				String tmpSql = "select * from tab_service where service_name='"
						+ strFilename + "'";
				PrepareSQL psql = new PrepareSQL(tmpSql);
				psql.getSQL();
				Map map = DataSetBean.getRecord(tmpSql);
				if (map != null)
				{
					strMsg = "????????????\"" + strFilename + "\"??????????????????????????????????????????";
				}
				else
				{
					long serviceId1;
					if (DataSetBean.getMaxId(tableName, column) < columnNum)
					{
						serviceId1 = 1001;
					}
					else
					{
						serviceId1 = DataSetBean.getMaxId(tableName, column);
					}
					strSql = "insert into tab_service(service_id,service_name,service_desc) values("
							+ serviceId1
							+ ",'"
							+ strFilename
							+ "','"
							+ strLgDesc
							+ "')";
					strSqlList.add(strSql);
					psql = new PrepareSQL(strSql);
					psql.getSQL();
					strSql = StringUtils.replace(strSql, ",,", ",null,");
					strSql = StringUtils.replace(strSql, ",,", ",null,");
					strSql = StringUtils.replace(strSql, ",)", ",null)");
				}
			}
			else
			{
				// ????????????
				// ??????????????????
				String sql = "select * from tab_service where service_name='"
						+ strFilename + "' and service_id=" + serviceId + "";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				Map map1 = DataSetBean.getRecord(sql);
				if (map1 != null)
				{
					strSql = "update tab_service set service_name='" + strFilename
							+ "',service_desc='" + strLgDesc + "' where service_id="
							+ serviceId + "";
					strSql = StringUtils.replace(strSql, "=,", "=null,");
					strSql = StringUtils.replace(strSql, "= where", "=null where");
					psql = new PrepareSQL(strSql);
					psql.getSQL();
					strSqlList.add(strSql);
				}
				else
				{
					String tmpSql = "select * from tab_service where service_name='"
							+ strFilename + "'";
					psql = new PrepareSQL(tmpSql);
					psql.getSQL();
					Map map = DataSetBean.getRecord(tmpSql);
					if (map != null)
					{
						strMsg = "????????????\"" + strFilename + "\"??????????????????????????????????????????";
					}
					else
					{
						strSql = "update tab_service set service_name='" + strFilename
								+ "',service_desc='" + strLgDesc
								+ "' where service_id=" + serviceId + "";
						strSql = StringUtils.replace(strSql, "=,", "=null,");
						strSql = StringUtils.replace(strSql, "= where", "=null where");
						psql = new PrepareSQL(strSql);
						psql.getSQL();
						strSqlList.add(strSql);
					}
				}
			}
		}
		if (strMsg == null || !strSql.equals(""))
		{
			// logger.debug("ssssssssssssssss>");
			int iCode[] = DataSetBean.doBatch(strSqlList);
			if (iCode != null && iCode.length > 0)
			{
				strMsg = "?????????????????????";
			}
			else
			{
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
	public boolean checkname1(String name)
	{
		String tmpSql = "select * from tab_servicecode where servicecode='" + name + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select servicecode from tab_servicecode where servicecode='" + name + "'";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(tmpSql);
		if (map != null)
		{
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
	public String checkname2(String serviceId, String strDrtDirection,
			String strTemplateName)
	{
		String str = "";
		String tmpSql = "select a.template_id,a.devicetype_id,b.template_name,e.device_model "
				+ " from tab_servicecode a,tab_template b,tab_devicetype_info c,tab_service d,gw_device_model e"
				+ " where a.service_id="
				+ serviceId
				+ " and a.devicetype_id=c.devicetype_id and a.devicetype_id="
				+ strDrtDirection
				+ " and a.template_id="
				+ strTemplateName
				+ " and a.template_id=b.template_id and c.devicetype_id=b.devicetype_id "
				+ " and c.device_model_id=e.device_model_id ";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(tmpSql);
		if (map != null)
		{
			str = map.get("device_model") + " / " + map.get("template_name");
		}
		return str;
	}

	public String YewuCode(HttpServletRequest request)
			throws UnsupportedEncodingException
	{
		String strSql = "";
		String strMsg = "";
		String serviceId = request.getParameter("lg_id");
		String strAction = request.getParameter("action");
		if (DELETE.equals(strAction))
		{
			String drtName1 = new String(request.getParameter("drt_name1").getBytes());
			drtName1 = java.net.URLDecoder.decode(drtName1, "GBK");
			strSql = "delete from tab_servicecode  where servicecode='" + drtName1 + "'";
		}
		else
		{
			String strDrtName = request.getParameter("drt_name");
			String strDrtDirection = request.getParameter("devicetype_id");
			String strTemplateName = request.getParameter("template_id");
			String qwe = request.getParameter("qwe");
			String cityList = request.getParameter("cityList");
			if (ADD.equals(strAction))
			{
				// ??????????????????
				boolean name = checkname1(strDrtName);
				String name1 = checkname2(serviceId, strDrtDirection,
						strTemplateName);
				if (name == false)
				{
					strMsg = "??????????????????\"" + strDrtName + "\"????????????????????????????????????????????????";
				}
				else if (!StringUtil.IsEmpty(name1))
				{
					strMsg = "???????????????????????????" + name1 + "?????????????????????????????????????????????????????????";
				}
				else
				{
					strSql = "insert into tab_servicecode(servicecode,service_id,template_id,devicetype_id,citylist) values('"
							+ strDrtName
							+ "',"
							+ serviceId
							+ ","
							+ strTemplateName
							+ "," + strDrtDirection + ",'" + cityList + "')";
					strSql = StringUtils.replace(strSql, ",,", ",null,");
					strSql = StringUtils.replace(strSql, ",,", ",null,");
					strSql = StringUtils.replace(strSql, ",)", ",null)");
				}
			}
			else
			{
				// ????????????
				strSql = "update tab_servicecode set template_id=" + strTemplateName
						+ ", devicetype_id=" + strDrtDirection + ", citylist='"
						+ cityList + "' where servicecode='" + strDrtName + "'";
				strSql = StringUtils.replace(strSql, "=,", "=null,");
				strSql = StringUtils.replace(strSql, "= where", "=null where");
				// logger.debug("strSQLliiiiiii"+strSQL);
			}
		}
		if (strMsg == null || !strSql.equals(""))
		{
			PrepareSQL psql = new PrepareSQL(strSql);
			psql.getSQL();
			int iCode = DataSetBean.executeUpdate(strSql);
			if (iCode > 0)
			{
				strMsg = "???????????????????????????";
			}
			else
			{
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
			throws UnsupportedEncodingException
	{
		Map map = getTemplateMap();
		Map serviceList = getServiceMap();
		Map deviceList = getDevicelistMap();
		String strServiceId = request.getParameter("lg_id");
		// logger.debug("str_service_id===========liuli"+str_service_id);
		String strSql = "select servicecode,service_id,template_id,devicetype_id,citylist from tab_servicecode where  service_id="
				+ strServiceId + "";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		qryp.initPage(strSql, offset, pagelen);
		String strBar = qryp.getPageBar("&lg_id=" + strServiceId);
		PrepareSQL psql = new PrepareSQL(strSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSql, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null)
		{
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=6 HEIGHT=30 class=column>??????????????????????????????</TD></TR>";
		}
		else
		{
			while (fields != null)
			{
				// String Service_name =(String)( Servicelist.get((String)
				// fields.get("service_id")));
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='center'>"
						+ serviceList.get((String) fields.get("service_id")) + "</TD>";
				strData += "<TD align='center'>" + (String) fields.get("servicecode")
						+ "</TD>";
				String device = "";
				String deTemplate = "";
				if (deviceList.get((String) fields.get("devicetype_id")) == null)
				{
					device = "";
				}
				else
				{
					device = (String) (deviceList.get((String) fields
							.get("devicetype_id")));
				}
				strData += "<TD align='center'>" + device + "</TD>";
				if (map.get((String) fields.get("template_id")) == null)
				{
					deTemplate = "";
				}
				else
				{
					deTemplate = (String) (map.get((String) fields.get("template_id")));
				}
				strData += "<TD align='center'>" + deTemplate + "</TD>";
				// ????????????
				String cityList = (String) fields.get("citylist");
				StringBuilder cityListDesc = new StringBuilder("");
				String[] cityArr = cityList.split(";");
				Map cityMap = CommonMap.getCityMap();
				for (int i = 0; i < cityArr.length; i++)
				{
					if (!StringUtil.IsEmpty(cityArr[i]))
					{
						if ("".equals(cityListDesc.toString()))
						{
							cityListDesc.append((String) cityMap.get(cityArr[i]));
						}
						else
						{
							cityListDesc.append("???");
							cityListDesc.append((String) cityMap.get(cityArr[i]));
						}
					}
				}
				strData += "<TD align='center'>" + cityListDesc.toString() + "</TD>";
				String tmpCode = (String) fields.get("servicecode");
				// String tmpCode = new
				// String(((String)fields.get("servicecode")).getBytes());
				strData += "<TD width=250 align='center'><A HREF='#' onclick=editCode('"
						+ tmpCode + "','" + (String) fields.get("template_id") + "','"
						+ (String) fields.get("devicetype_id") + "','" + cityList
						+ "')>??????</A> | <A HREF='#'" + "  onclick=delWarn('" + tmpCode
						+ "')>??????</A></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=6 align=right>" + strBar
					+ "</TD></TR>";
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
	public String getHtml(HttpServletRequest request)
	{
		String strSql = "select service_id,service_name,service_desc from tab_service where flag=-1";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		qryp.initPage(strSql, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(strSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSql, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null)
		{
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=4 HEIGHT=30 class=column>????????????????????????</TD></TR>";
		}
		else
		{
			while (fields != null)
			{
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='left'>" + (String) fields.get("service_id")
						+ "</TD>";
				strData += "<TD align='left'>" + (String) fields.get("service_name")
						+ "</TD>";
				strData += "<TD align='left'>" + (String) fields.get("service_desc")
						+ "</TD>";
				strData += "<TD width=250 align='center'><A HREF=\"javascript:Edit('"
						+ (String) fields.get("service_id")
						+ "','"
						+ (String) fields.get("service_name")
						+ "','"
						+ (String) fields.get("service_desc")
						+ "');\">??????</A> | <A HREF=jt_sevice_from_save.jsp?action=delete&lg_id="
						+ (String) fields.get("service_id")
						+ " onclick='return delWarn();'>??????</A>|<a href='jt_yewu_code.jsp?lg_id="
						+ (String) fields.get("service_id") + "'>??????????????????</a></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right>" + strBar
					+ "</TD></TR>";
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
	public String getHtml1(HttpServletRequest request)
	{
		String strSql = "select service_id,service_name,service_desc from tab_service where flag>=0";
		String strData = "";
		String stroffset = request.getParameter("offset");
		QueryPage qryp = new QueryPage();
		int pagelen = 8;
		int offset;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		qryp.initPage(strSql, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(strSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSql, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null)
		{
			strData = "<TR  bgcolor=#FFFFFF><TD COLSPAN=4 HEIGHT=30 class=column>??????????????????????????????</TD></TR>";
		}
		else
		{
			while (fields != null)
			{
				strData += "<TR bgcolor=#FFFFFF>";
				strData += "<TD align='left'>" + (String) fields.get("service_id")
						+ "</TD>";
				strData += "<TD align='left'>" + (String) fields.get("service_name")
						+ "</TD>";
				strData += "<TD align='left'>" + (String) fields.get("service_desc")
						+ "</TD>";
				strData += "<TD width=250 align='center'><a href='jt_yewu_code.jsp?lg_id="
						+ (String) fields.get("service_id") + "'>??????????????????</a></TD>";
				strData += "</TR>";
				fields = cursor.getNext();
			}
			strData += "<TR BGCOLOR=#FFFFFF><TD COLSPAN=4 align=right>" + strBar
					+ "</TD></TR>";
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
	public int testConnection(HttpServletRequest request)
	{
		String deviceId = request.getParameter("device_id");

		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);

		DevRpc[] devRpcArr = new DevRpc[1];
		devRpcArr[0] = new DevRpc();
		devRpcArr[0].devId = deviceId;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRpcArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRpcRep = null;
		DevRPCManager devRpcManager = new DevRPCManager(gwType);
		devRpcRep = devRpcManager.execRPC(devRpcArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRpcRep == null || devRpcRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>???????????????", deviceId);
		}
		else if (devRpcRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ???????????????", deviceId);
		}
		else
		{
			flag = devRpcRep.get(0).getStat();
		}
		return flag;
	}

	/**
	 * ping??????????????????
	 *
	 * @param device_id
	 * @return
	 */
	public String getPingInterface(String deviceId, String gwType)
	{
		logger.debug("getPingInterface({},{},{})");
		SuperGatherCorba sgCorba = new SuperGatherCorba(gwType);
//		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		if (!LipossGlobals.isJlLt())
		{
			// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
			name = "Lan(IPInterface):1";
			value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
			logger.debug("LAN???(IPInterface)???" + name + "=" + value);
			interList += "<option selected value='" + value + "'>" + name + "</option>";
		}

		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(deviceId, ConstantClass.GATHER_WAN, 1);

		logger.warn("??????????????????=====" + irt);
		String errorMsg = "";
		if (irt != 1)
		{
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
			//_interList += "<option value='" + value + "'>" + name + "</option>";
		}
		else
		{
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if(Global.GW_TYPE_ITMS.equals(gwType)){
				wanConnIds = getWanConnIds(deviceId);
			} else {
				wanConnIds = getWanConnIds_bbms(deviceId);
			}
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
				//_interList += "<option value='" + value + "'>" + name + "</option>";
			}
			else
			{
				flag = true;
				for (Map map : wanConnIds)
				{
					String wanConnId = StringUtil
							.getStringValue(map.get("wan_conn_id"));
					String wanConnSessId = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String servList = StringUtil.getStringValue(map.get("serv_list"));
					String tmp = "";
					if (StringUtil.IsEmpty(vlanid) || vlanid.equals("NULL"))
					{
						tmp = "pvc:" + pvc;
					}
					else
					{
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1"))
					{
						name = "Wan(" + "WANPPPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANPPPConnection."
								+ wanConnSessId + ".";
					}
					else if (sessType.equals("2"))
					{
						name = "Wan(" + "WANIPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANIPConnection."
								+ wanConnSessId + ".";
					}
					else
					{
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					interList += "<option value='" + value + "'>" + name + "</option>";
				}
			}
		}
		// 3?????????listBox
		interList += "</select>";
		if (!flag)
		{
			interList = errorMsg;
		}
		logger.warn("interList-----" + interList);
		return interList;
	}
	/**
	 * HttpGet??????????????????
	 *
	 * @param device_id
	 * @return
	 */
	public String getHttpGetInterface(String deviceId, String gwType)
	{
		logger.debug("getHttpGetInterface({})",deviceId);
		SuperGatherCorba sgCorba = new SuperGatherCorba(gwType);
//		String _Pvc = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		//?????????WAN???
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
		name = "Lan(IPInterface):1";
		value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		logger.debug("LAN???(IPInterface)???" + name + "=" + value);
		interList += "<option selected value='" + value + "'>" + name + "</option>";
		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(deviceId, ConstantClass.GATHER_WAN, 1);
		String errorMsg = "";
		logger.warn("??????????????????=====" + irt);
		if (irt != 1)
		{
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
		}
		else
		{
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if(Global.GW_TYPE_ITMS.equals(gwType)){
				wanConnIds = getWanConnIds(deviceId);
			} else {
				wanConnIds = getWanConnIds_bbms(deviceId);
			}
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
			}
			else
			{
				flag = true;
				for (Map map : wanConnIds)
				{
					String wanConnId = StringUtil
							.getStringValue(map.get("wan_conn_id"));
					String wanConnSessId = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String servList = StringUtil.getStringValue(map.get("serv_list"));
					String tmp = "";
					if (StringUtil.IsEmpty(vlanid) || vlanid.equals("NULL"))
					{
						tmp = "pvc:" + pvc;
					}
					else
					{
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1"))
					{
						name = "Wan(" + "WANPPPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANPPPConnection."
								+ wanConnSessId + ".";
					}
					else if (sessType.equals("2"))
					{
						name = "Wan(" + "WANIPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANIPConnection."
								+ wanConnSessId + ".";
					}
					else
					{
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					interList += "<option value='" + value + "'>" + name + "</option>";
				}
			}
		}
		// 3?????????listBox
		interList += "</select>";
		if (!flag)
		{
			interList = errorMsg;
		}
		logger.warn("interList-----" + interList);
		return interList;
	}

	/**
	 * DNSQuery????????????interface
	 * @param device_id
	 * @return
	 */
	public String getDNSQueryInterface(String deviceId)
	{
		logger.debug("getDNSQueryInterface({})",deviceId);
		// ??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);
		SuperGatherCorba sgCorba = new SuperGatherCorba(gwType+"");
		String pvc1 = LipossGlobals.getLipossProperty("InstArea.PVC");
		String name = "";
		String value = "";
		//?????????WAN???
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		// ??????Lan???InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1???
		name = "Lan(IPInterface):1";
		value = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		logger.debug("LAN???(IPInterface)???" + name + "=" + value);
		interList += "<option selected value='" + value + "'>" + name + "</option>";
		// ??????Wan
		// 1???????????????,??????InternetGatewayDevice.WANDevice?????????
		int irt = sgCorba.getCpeParams(deviceId, ConstantClass.GATHER_WAN, 1);
		String errorMsg = "";
		logger.warn("??????????????????=====" + irt);
		if (irt != 1)
		{
			errorMsg = "??????????????????";
			logger.warn(errorMsg);
		}
		else
		{
			// 2?????????????????????wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = null;
			if(Global.GW_TYPE_ITMS.equals(gwType)){
				wanConnIds = getWanConnIds(deviceId);
			} else {
				wanConnIds = getWanConnIds_bbms(deviceId);
			}
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				errorMsg = "???????????????Wan??????";
				logger.warn(errorMsg);
			}
			else
			{
				flag = true;
				for (Map map : wanConnIds)
				{
					String wanConnId = StringUtil
							.getStringValue(map.get("wan_conn_id"));
					String wanConnSessId = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String pvc = StringUtil.getStringValue(map.get("pvc"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String servList = StringUtil.getStringValue(map.get("serv_list"));
					String tmp = "";
					if (StringUtil.IsEmpty(vlanid) || vlanid.equals("NULL"))
					{
						tmp = "pvc:" + pvc;
					}
					else
					{
						tmp = "vlanid:" + vlanid;
					}
					if (sessType.equals("1"))
					{
						name = "Wan(" + "WANPPPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANPPPConnection."
								+ wanConnSessId + ".";
					}
					else if (sessType.equals("2"))
					{
						name = "Wan(" + "WANIPConnection/" + tmp + "):" + wanConnId
								+ "-" + wanConnSessId;
						value = wanConnDevice + wanConnId + ".WANIPConnection."
								+ wanConnSessId + ".";
					}
					else
					{
						logger.warn("sessType????????????" + sessType);
						continue;
					}
					logger.debug("wanppp ping_value :" + value);
					interList += "<option value='" + value + "'>" + name + "</option>";
				}
			}
		}
		// 3?????????listBox
		interList += "</select>";
		if (!flag)
		{
			interList = errorMsg;
		}
		logger.warn("interList-----" + interList);
		return interList;
	}

	/**
	 * ??????DSL wan??????
	 * @param device_id
	 * @return
	 */
	public String getDslWAN(String deviceId)
	{
		logger.debug("getDNSQueryInterface({})",deviceId);

		// ??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);

		// ??????WAN??????????????????
		String path = "InternetGatewayDevice.WANDevice.";
		//Map<String, String> paramMap = new HashMap<String, String>(1);
		GetParameterNames getParameterNames = new GetParameterNames();
		getParameterNames.setParameterPath(path);
		getParameterNames.setNextLevel(1);
		DevRpc[] devRpcArr = getDevRPCArr(deviceId, getParameterNames);
		List<DevRpcCmdOBJ> devRpcCmdObjList = getDevRPCResponse(devRpcArr,Global.RpcCmd_Type,gwType);
		String errorMsg = "";
		if (null == devRpcCmdObjList || 0 == devRpcCmdObjList.size())
		{
			errorMsg = "??????????????????";
			return errorMsg;
		}
		// ???????????????????????????
		Element element = dealDevRPCResponse("GetParameterNamesResponse",devRpcCmdObjList, deviceId);
		if (element == null)
		{
			errorMsg = "???????????????Wan??????";
			return errorMsg;
		}
		GetParameterNamesResponse getParameterNamesResponse = new GetParameterNamesResponse();
		// ???SOAP?????????????????????????????????XML,????????????
		getParameterNamesResponse = XmlToRpc.GetParameterNamesResponse(element);
		// ????????????XML??????,??????????????????
		StringBuffer sb = new StringBuffer();
		if (null != getParameterNamesResponse)
		{
			ParameterInfoStruct[] pisArr = getParameterNamesResponse.getParameterList();
			if (null != pisArr)
			{
				String name = null;
				for (int i = 0; i < pisArr.length; i++)
				{
					name = pisArr[i].getName();
					String writable = pisArr[i].getWritable();
					sb.append(name).append("\6");
				}
			}
		}
		if(sb.length() == 0 || StringUtil.IsEmpty(sb.toString()))
		{
			return "";
		}
		return sb.substring(0, sb.length()-1);
	}

	private List getWanConnIds(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();
		String telecom = LipossGlobals.getLipossProperty("telecom");
		String tabnameA = "gw_wan_conn";
		String tabnameB = "gw_wan_conn_session";

		if (CUC.equalsIgnoreCase(telecom)) {
			tabnameA = "CUC_gw_wan_conn";
			tabnameB = "CUC_gw_wan_conn_session";
		}

		// oracle
		if(DBUtil.GetDB() == Global.DB_ORACLE){
			sql.append("select b.sess_type,b.serv_list,a.vlan_id,to_char(a.vpi_id) || '/' || to_char(a.vci_id) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}
		// sysbase
		else if (DBUtil.GetDB() == Global.DB_SYBASE) {
			sql.append("select b.sess_type,b.serv_list,a.vlan_id,convert(varchar,a.vpi_id)+'/'+convert(varchar,a.vci_id) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select b.sess_type,b.serv_list,a.vlan_id, concat(CAST(a.vpi_id AS CHAR), '/', CAST(a.vci_id AS CHAR)) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}

		sql.append("from " + tabnameA + " a," + tabnameB +" b where a.device_id=b.device_id and a.wan_conn_id=b.wan_conn_id  and a.device_id='");
		sql.append(deviceId).append("'");
		if (LipossGlobals.isJlLt())
		{
			sql.append(" and a.vlan_id in ('41','43','44') ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++)
		{
			list.add(cursor.getRecord(i));
		}
		return list;
	}

	private List getWanConnIds_bbms(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		List<Map> list = new ArrayList<Map>();

		// oracle
		if(1 == DBUtil.GetDB()){
			sql.append("select b.sess_type,b.serv_list,a.vlan_id,to_char(a.vpi_id) || '/' || to_char(a.vci_id) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}
		// sysbase
		else if (DBUtil.GetDB() == Global.DB_SYBASE) {
			sql.append("select b.sess_type,b.serv_list,a.vlan_id,convert(varchar,a.vpi_id)+'/'+convert(varchar,a.vci_id) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select b.sess_type,b.serv_list,a.vlan_id, concat(CAST(a.vpi_id AS CHAR), '/', CAST(a.vci_id AS CHAR)) pvc,b.wan_conn_id,b.wan_conn_sess_id ");
		}

		sql.append("from gw_wan_conn_bbms a,gw_wan_conn_session_bbms b where a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id and upper(b.serv_list) like '%INTERNET%' and a.device_id='");
		sql.append(deviceId).append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString());
		for (int i = 0; i < cursor.getRecordSize(); i++)
		{
			list.add(cursor.getRecord(i));
		}
		return list;
	}

	/**
	 * ??????PING???????????? liuli@lianchuang.com
	 *
	 * @return
	 */
	public String getPingInterfaceListBox(String deviceId)
	{
		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);
		String pvc1 = LipossGlobals.getLipossProperty("InstArea.PVC");
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		// ??????Lan ???InternetGatewayDevice.LANDevice.1.LAN-HostConfigManagement.IPInterface.???
		String interList = "<select name=Interface id=Interface class='bk'>";
		boolean flag = false;
		String param = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
		// int length = param.length();
		// ??????IPInterface?????????
		Collection collection = WANConnDeviceAct.getParameterNameList(deviceId, param);
		Iterator iterator = collection.iterator();
		
		String name = "";
		String value = null;
		// ????????????????????? IPInterface
		if (iterator.hasNext()) {
			flag = true;
		}
		while (iterator.hasNext())
		{
			value = ((String) iterator.next());
			if (param.equals(value))
			{
				logger.debug("param???value?????????...");
				continue;
			}
			// InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1.
			// ????????????????????????????????????"."
			name = "Lan(IPInterface):" + getLastString(param, value);
			
			value = trimDot(value);
			logger.debug("LAN???(IPInterface)???" + name + "=" + value);
			interList += "<option selected value='" + value + "'>" + name + "</option>";
		}
		// -----------------------------------------------modify by lizhaojun
		// ?????????????????????????????????-------------------------
		// ??????WAN
		param = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		collection = WANConnDeviceAct.getParameterNameList(deviceId, param);
		Collection collectionSub = null;
		Iterator iteratorSub = null;
		iterator = collection.iterator();
		String pathWanConn = null;
		// length = param.length();
		String paraValue = "";
		Map paraValueMap;
		String name2 = "";
		while (iterator.hasNext())
		{
			// InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.
			pathWanConn = (String) iterator.next();
			if (param.equals(pathWanConn))
			{
				logger.debug("param???pathWANConn?????????...");
				continue;
			}
			paraValueMap = paramTreeObject.getParaValueMap(pathWanConn
					+ "WANDSLLinkConfig.DestinationAddress", deviceId);
			paraValue = paramTreeObject.getParaVlue(paraValueMap);
			logger.debug("paraValue :" + paraValue);
			if (paraValue.indexOf(pvc1) != -1)
			{
				name = "??????WAN???(WANPPPConnection)???" + getLastString(param, pathWanConn);
			}
			else
			{
				name = "??????WAN???(WANPPPConnection)???" + getLastString(param, pathWanConn);
			}
			// name = "WAN :" + getLastString(param, pathWANConn);
			collectionSub = WANConnDeviceAct.getParameterNameList(deviceId, pathWanConn
					+ "WANPPPConnection.");
			iteratorSub = collectionSub.iterator();
			// ?????????????????????WanPPPconnection
			if (iteratorSub.hasNext()) {
				flag = true;
			}
			while (iteratorSub.hasNext())
			{
				value = (String) iteratorSub.next();
				if ((pathWanConn + "WANPPPConnection.").equals(value))
				{
					logger.debug("A--???????????????...");
					continue;
				}
				value = trimDot(value);
				logger.debug("wanppp ping_value :" + value);
				interList += "<option value='" + value + "'>" + name + "-"
						+ getLastString(value) + "</option>";
			}
			// --------------------
			Collection collectionSub2 = WANConnDeviceAct.getParameterNameList(deviceId,
					pathWanConn + "WANIPConnection.");
			iteratorSub = collectionSub2.iterator();
			if (iteratorSub.hasNext())
			{
				flag = true;
				name2 = "WAN???(WANIPConnection)???" + getLastString(param, pathWanConn);
			}
			while (iteratorSub.hasNext())
			{
				value = (String) iteratorSub.next();
				if ((pathWanConn + "WANIPConnection.").equals(value))
				{
					logger.debug("B--???????????????...");
					continue;
				}
				value = trimDot(value);
				logger.debug("wanIP ping_value :" + value);
				interList += "<option value='" + value + "'>" + name2 + "-"
						+ getLastString(value) + "</option>";
			}
			paraValueMap = null;
			paraValue = null;
		}
		interList += "</select>";
		if (flag == false) {
			interList = "";
		}
		return interList;
	}

	/**
	 * ??????ATMF5LOOP???????????? liuli@lianchuang.com
	 *
	 * @return
	 */
	public String getATMF5LOOPInterfaceListBox(String deviceId)
	{
		//??????????????????
		String gwType = LipossGlobals.getGw_Type(deviceId);
		// -----------------------------------------------modify by lizhaojun 2007-8-21
		String pvc1 = LipossGlobals.getLipossProperty("InstArea.PVC");
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		boolean flag = false;
		StringBuffer sb = new StringBuffer();
		String param = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		Collection collection = WANConnDeviceAct.getParameterNameList(deviceId, param);
		Iterator iterator = collection.iterator();
		String name = null;
		String value = null;
		if (iterator.hasNext()) {
			flag = true;
		}
		sb.append("<select name=ATMF5 id=Interface class='bk'>");
		while (iterator.hasNext())
		{
			value = ((String) iterator.next());
			if (param.equals(value))
			{
				logger.debug("param???value?????????...");
				continue;
			}
			paramTreeObject.setGwType(gwType);
			Map paraValueMap = paramTreeObject.getParaValueMap(value
					+ "WANDSLLinkConfig.DestinationAddress", deviceId);
			String paraValue = paramTreeObject.getParaVlue(paraValueMap);
			if (paraValue.indexOf(pvc1) != -1)
			{
				name = "??????WAN(WANConnectionDevice)???" + getLastString(param, value);
			}
			else
			{
				name = "??????WAN(WANConnectionDevice)???" + getLastString(param, value);
			}
			logger.debug("value :" + value);
			sb.append("<option  value='" + value + "'>" + name + "</option>");
		}
		sb.append("</select>");
		// ??????????????????Interface
		if (flag == false)
		{
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
	public String trimDot(String value)
	{
		logger.debug("value:" + value);
		int dotIndex = value.lastIndexOf(".");
		if (dotIndex == value.length() - 1)
		{
			value = value.substring(0, dotIndex);
		}
		// logger.debug("trimDot==>value:" + value);
		return value;
	}

	/**
	 * ??????InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.(source)??????
	 * InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1. (value)
	 * InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1 (value) ??????
	 * 1
	 *
	 * @return
	 */
	public String getLastString(String source, String value)
	{
		logger.debug("source-----:" + source);
		logger.debug("value-----:" + value);
		value = trimDot(value = value.substring(source.length()));
		return value;
	}

	/**
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.12.WANPPPConnection.8/***8.--->8
	 *
	 * @param value
	 * @return
	 */
	public String getLastString(String value)
	{
		value = trimDot(value);
		int dotIndex = value.lastIndexOf(".");
		if (dotIndex != -1) {
			value = value.substring(dotIndex + 1);
		}
		return value;
	}
}
