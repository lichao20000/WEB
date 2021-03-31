/**
 * 设备资源类，操作设备相关数据
 */

package com.linkage.litms.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import RemoteDB.DeviceStatusStruct;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.common.ServerCode;
import com.linkage.litms.common.corba.ACSManager;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.corba.MidWareCorba;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Encoder;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.common.util.ZooCuratorLockUtil;
import com.linkage.litms.system.AreaManager;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.litms.system.dbimpl.LogItem;
import com.linkage.litms.webtopo.MCControlManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * <P>
 * Linkage Communication Technology Co., Ltd
 * <P>
 * <P>
 * Copyright 2005-2007. All right reserved.
 * <P>
 * 
 * @version 1.0.0 2007-6-16
 * @author Linkage. 2007-6-16 modified by Alex.Yan (yanhj@lianchuang.com) RemoteDB. ACS.
 */
public class DeviceActHblt
{

	private static Logger logger = LoggerFactory.getLogger(DeviceAct.class);
	private PrepareSQL pSQL = null;
	private Cursor cursor = null;
	private Map fields = null;
	private Map countNumMap = null;
	private String m_OfficeList_SQL = "select office_id,office_name from tab_office order by office_id";
	private String m_ZoneList_SQL = "select zone_id,zone_name from tab_zone order by zone_id";
	/**
	 * 设备资源添加sql
	 */
	private String m_DeviceAdd_SQL = "insert into tab_gw_device (device_id,device_id_ex,loopback_ip,device_serialnumber,devicetype_id,manage_staff,city_id,office_id,zone_id,device_addr,complete_time,buy_time,service_year,staff_id,remark,gather_id,device_status,cpe_mac,res_pro_id,device_name,maxenvelopes,retrycount,cr_port,cr_path,cpe_currentupdatetime,acs_username,acs_passwd,cpe_username,cpe_passwd,gw_type,device_model_id,snmp_udp,device_type,customer_id,device_url) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	/**
	 * 修改设备信息
	 */
	// private String m_DeviceInfoUpdate_SQL =
	// "update tab_gw_device set device_id_ex=?,loopback_ip=?,device_serialnumber=?,devicetype_id=?,manage_staff=?,city_id=?,office_id=?,zone_id=?,device_addr=?,complete_time=?,buy_time=?,service_year=?,staff_id=?,remark=?,gather_id=?,device_status=?,cpe_mac=?,res_pro_id=?,device_name=?,maxenvelopes=?,retrycount=?,cr_port=?,cr_path=?,cpe_currentupdatetime=?,acs_username=?,acs_passwd=?,cpe_username=?,cpe_passwd=?,device_model_id=?,snmp_udp=?,device_type=?,customer_id=?,device_url=? where device_id=?";
	/**
	 * 用户组
	 */
	private String m_GroupList_SQL = "select group_oid,group_name from tab_group";
	/**
	 * 用户组 用户
	 */
	private String m_UserOfGroupList_SQL = "select a.per_acc_oid,b.acc_loginname from tab_persons a,tab_accounts b where a.per_acc_oid=b.acc_oid and a.per_acc_oid in  (select acc_oid from tab_acc_group) order by b.acc_loginname";
	/**
	 * 厂商列表
	 */
	// private String m_Vendor_SQL =
	// "select vendor_id,vendor_name + '(' + vendor_id + ')' as vendor_name,vendor_add from tab_vendor";
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
	/**
	 * 机顶盒厂商列表
	 */
	// private String m_Vendor_SQL =
	// "select vendor_id,vendor_name + '(' + vendor_id + ')' as vendor_name,vendor_add from tab_vendor";
	private String stb_m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from stb_tab_vendor";
	/**
	 * 厂商列表
	 */
	private String m_VendorList = "select vendor_id,vendor_name, vendor_add from tab_vendor";
	/**
	 * 厂商列表，不需要OUI
	 */
	private String m_selectDeviceVendorSql = "select vendor_id,vendor_name from tab_vendor";
	/**
	 * 获取设备名称
	 */
	// private String m_getVendor_SQL =
	// "select vendor_name from tab_vendor where vendor_id=?";
	// private String m_getVendor_SQL =
	// "select a.vendor_name from tab_vendor a,tab_vendor_oui b where " +
	// "  a.vendor_id=b.vendor_id and b.oui=?";
	/**
	 * 获取某设备的用户信息
	 */
	// private String sqlCurs =
	// "select a.username,b.service_name from ? a,tab_service b where a.serv_type_id=b.service_id and oui=? and device_serialnumber=?";
	/**
	 * 设备资源表sql
	 */
	private String m_DeviceInfo_SQL = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id and b.res_type=1 where b.area_id=? and a.device_id=?";
	/**
	 * 采集点sql
	 */
	private String m_GatherList_SQL = "select gather_id,descr from tab_process_desc order by area_id";
	/**
	 * 查询设备所支持的业务代码
	 */
	private String m_DeviceSupport_SQL = "select a.servicecode,b.service_name from tab_servicecode a,tab_service b,tab_service c"
			+ " where a.service_id=b.service_id and a.service_id = c.service_id and c.flag > 0 and a.devicetype_id=?";
	/**
	 * 查询设备当前所支持的业务代码
	 */
	// private String m_NowDeviceSupport_SQL =
	// "select b.service_name from tab_gw_user_serv a, tab_service b where a.serv_type_id = b.serv_type_id and a.device_id = ? and b.flag > 0";
	/**
	 * 查询设备当前开通的业务代码及状态
	 */
	// private String m_NowDeviceSupportStatus_SQL =
	// "select b.serv_type_id, a.is_active, b.service_name from tab_gw_user_serv a, tab_service b where a.serv_type_id = b.serv_type_id and a.device_id = ? and b.flag > 0";
	/**
	 * 查询绑定的用户及状态
	 */
	private String m_BindCustomerStatus_SQL = "select user_state from tab_egwcustomer where oui = ? and device_serialnumber = ?";
	/**
	 * 获取ITMS操作历史信息
	 */
	private String m_getHistoryOperation_SQL = "select * from tab_sheet_report where device_id = ? and exec_status = 1";
	/*
     * 
     */
	private String m_GetDeviceBasInfoByCities_SQL = "select * from tab_deviceresource where resource_type_id=3 and city_id in (?) order by loopback_ip";
	/**
	 * 获取某厂商某设备型号的其他信息（软硬件版本信息）
	 */
	// private String m_DeviceVersion_SQL =
	// "select devicetype_id,manufacturer,specversion,hardwareversion,softwareversion,device_model from tab_devicetype_info where oui=? and devicetype_id=?";
	/**
	 * 获取某厂商某设备型号的模板
	 */
	private String m_DevModelTemp_SQL = "select template_id,template_name,template_desc from tab_template where devicetype_id=? order by template_id desc";
	/**
	 * 设置设备的确认状态
	 */
	// private String m_SetDeviceStatus_SQL = "update tab_gw_device set
	// device_status=? where device_id in(?)";
	/**
	 * 增加确认设备
	 */
	private String m_ConfirmDevAdd_SQL = "insert into tab_confirmdevice values(?,?,?)";
	/**
	 * 确认设备列表
	 */
	private String m_ConfirmDev_List_SQL = "select * from tab_confirmdevice";
	/**
	 * 根据多个device_id获取查询设备
	 */
	private String m_DeviceByIds = "select a.* from tab_gw_device a where a.device_id in (?)";
	/**
	 * 获取所有的设备型号信息
	 */
	private String m_DeviceTypeList_SQL = "select * FROM tab_devicetype_info";
	/**
	 * 增加设备型号
	 */
	// private String m_DeviceTypeAdd_SQL =
	// "insert into tab_devicetype_info(devicetype_id,manufacturer,oui,device_model,specversion,hardwareversion,softwareversion,area_id) values(?,?,?,?,?,?,?,?)";
	// private String m_DeviceTypeAdd_SQL =
	// "insert into tab_devicetype_info(devicetype_id,manufacturer,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,area_id) values(?,?,?,?,?,?,?,?)";
	/**
	 * 更新设备型号
	 */
	// private String m_DeviceTypeUpdate_SQL =
	// "update tab_devicetype_info set manufacturer=?,oui=?,device_model=?,specversion=?,hardwareversion=?,softwareversion=?,area_id=? where devicetype_id=?";
	// private String m_DeviceTypeUpdate_SQL =
	// "update tab_devicetype_info set manufacturer=?,vendor_id=?,device_model_id=?,specversion=?,hardwareversion=?,softwareversion=?,area_id=? where devicetype_id=?";
	/**
	 * 删除设备型号
	 */
	private String m_DeviceTypeDelete_SQL = "delete from tab_devicetype_info where devicetype_id=?";
	/**
	 * 根据设备型号的ID查询设备型号的详细信息
	 */
	private String m_DeviceTypeByID_SQL = "select * from tab_devicetype_info where devicetype_id=?";
	/**
	 * 根据区域编号获得采集机信息
	 */
	private String m_Gathers_ByAreaId = "select * from tab_gw_res_area where res_type=2 and area_id=?";
	/**
	 * 删除区域编号下的采集机信息
	 */
	private String m_DelGathers_ByAreaId = "delete from tab_gw_res_area where res_type=2 and area_id=?";
	/**
	 * 保存区域与采集机关联入库
	 */
	private String m_Gathers_Save = "insert into tab_gw_res_area (res_type,res_id,area_id) values (?,?,?)";
	/**
	 * 根据hgw用户名称获取对应的设备
	 */
	private String m_selectDeviceByHGWUserName = "select a.device_id,a.gather_id,a.device_name,a.loopback_ip,a.device_serialnumber,a.oui,b.user_state "
			+ " from tab_gw_device a, tab_hgwcustomer b"
			+ " where a.device_status=1 and b.username=? and a.device_id=b.device_id";
	/**
	 * 查询OUI对应的设备序列号
	 */
	private String m_selectDeviceSerialByOUI = "select distinct device_serialnumber  from tab_gw_device  where oui=?";
	/**
	 * 根据OUI查找对应的设备型号
	 */
	// private String m_selectDeviceModelByOUI =
	// "select distinct(device_model) device_model from tab_devicetype_info where oui=?";
	private String m_selectDeviceModelByOUI = "select device_model from gw_device_model where vendor_id=?";
	/**
	 * 统计地市的设备数量
	 */
	private String m_DeviceNumOfCity = "select b.city_id,count(a.device_id) as num from tab_gw_device a left join tab_city b"
			+ " on a.city_id=b.city_id "
			+ " where b.city_id is not null"
			+ " and b.parent_id=?";
	/**
	 * 根据OUI＋设备型号，查询对应的软件版本
	 */
	// private String m_selectDeviceVersionByOUIAndDeviceModel =
	// "select distinct(softwareversion) softwareversion from tab_devicetype_info where oui=? and devicetype_id=?";
	private String m_selectDeviceVersionByOUIAndDeviceModel = "select distinct(softwareversion) softwareversion from tab_devicetype_info where devicetype_id=?";
	// private String getDeviceSoftVersionByOUIAndDeviceModel =
	// "select devicetype_id,softwareversion from tab_devicetype_info where oui=? and devicetype_id=?";
	// private String getDeviceSoftVersionByOUIAndDeviceTypeID =
	// "select devicetype_id,softwareversion from tab_devicetype_info where oui=? and devicetype_id=?";
	/**
	 * 根据OUI＋设备序列号来查看设备已开通的业务
	 */
	// private String m_selectDeviceUnit =
	// "select b.service_name,b.service_desc from tab_hgwcustomer a,tab_service b"
	// +
	// " where a.device_serialnumber=? and a.oui=? and a.user_state in('1','2') and a.serv_type_id=b.service_id";
	/**
	 * 查询设备的采集点
	 */
	private String m_selectDeviceInfo = "select devicetype_id,gather_id from tab_gw_device where device_id=?";
	private String m_selectStbDeviceInfo = "select devicetype_id,gather_id from stb_tab_gw_device where device_id=?";

	/**
	 * 构造函数
	 */
	public DeviceActHblt()
	{
		pSQL = new PrepareSQL();
	}

	/**
	 * 取当前时间(秒数)
	 */
	DateTimeUtil dtu = new DateTimeUtil();
	long nowTime = dtu.getLongTime();

	/**
	 * 根据device_id来查询采集点
	 * 
	 * @param device_id
	 * @return
	 */
	public HashMap getDeviceInfo(String device_id)
	{
		pSQL.setSQL(m_selectDeviceInfo);
		pSQL.setString(1, device_id);
		HashMap record = DataSetBean.getRecord(pSQL.getSQL());
		return record;
	}

	/**
	 * 根据device_id来查询采集点
	 * 
	 * @param device_id
	 * @return
	 */
	public HashMap getStbDeviceInfo(String device_id)
	{
		pSQL.setSQL(m_selectStbDeviceInfo);
		pSQL.setString(1, device_id);
		HashMap record = DataSetBean.getRecord(pSQL.getSQL());
		return record;
	}

	/**
	 * 根据OUI＋设备序列号查找设备列表
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceUnit(HttpServletRequest request)
	{
		String oui = request.getParameter("device_oui");
		String serialnumber = request.getParameter("device_serialnumber");
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpSql = "select a.* from tab_gw_device a ";
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		tmpSql += " where a.device_status=1";
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		if (oui != null && !oui.equals(""))
		{
			tmpSql += " and a.oui like '%" + oui + "%'";
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			if (serialnumber.length() > 5)
			{
				tmpSql += " and a.dev_sub_sn ='"
						+ serialnumber.substring(serialnumber.length() - 6,
								serialnumber.length()) + "'";
			}
			tmpSql += " and a.device_serialnumber like '%" + serialnumber + "'";
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(tmpSql);
		return cursor;
	}

	/**
	 * 根据用户名或设备序列号查找设备
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceInformStatList(HttpServletRequest request)
	{
		String username = request.getParameter("username");
		String serialnumber = request.getParameter("device_serialnumber");
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String startdateStr = start_day + " " + start_time;
		String enddateStr = end_day + " " + end_time;
		String gw_type = request.getParameter("gw_type");
		long startTime = new DateTimeUtil(startdateStr).getLongTime();
		long endTime = new DateTimeUtil(enddateStr).getLongTime();
		String usernameDevice_serialnumber = null;
		if (null != username && !"".equals(username))
		{
			StringBuffer tmpSqlusername = new StringBuffer();
			if (gw_type.equals(Global.GW_TYPE_ITMS))
			{
				tmpSqlusername
						.append("select device_serialnumber from tab_hgwcustomer where user_state='1' and username = '");
			}
			else
			{
				tmpSqlusername
						.append("select device_serialnumber from tab_egwcustomer where user_state='1' and username = '");
			}
			tmpSqlusername.append(username);
			tmpSqlusername.append("'");
			PrepareSQL psql = new PrepareSQL(tmpSqlusername.toString());
			psql.getSQL();
			cursor = DataSetBean.getCursor(tmpSqlusername.toString());
			Map fields = cursor.getNext();
			if (null != fields)
			{
				usernameDevice_serialnumber = (String) fields.get("device_serialnumber");
			}
			// 该用户匹配的设备不存在
			if (null == usernameDevice_serialnumber
					|| "".equals(usernameDevice_serialnumber))
			{
				return null;
			}
		}
		// 获取用户信息
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		// User user = curUser.getUser();
		// long area_id = user.getAreaId();
		StringBuffer tmpSql = new StringBuffer();
		tmpSql.append("select distinct device_id,oui,device_serialnumber,city_id,gw_type, count(1) total from tab_inform_log where 1=1 ");
		if (null != usernameDevice_serialnumber
				&& !"".equals(usernameDevice_serialnumber))
		{
			tmpSql.append(" and device_serialnumber = '" + usernameDevice_serialnumber
					+ "'");
		}
		if (null != serialnumber && !"".equals(serialnumber))
		{
			tmpSql.append(" and device_serialnumber like '%" + serialnumber + "%'");
		}
		if (startTime != 0 && !"".equals(startTime))
		{
			tmpSql.append(" and time >= " + startTime);
		}
		if (endTime != 0 && !"".equals(endTime))
		{
			tmpSql.append(" and time <=" + endTime);
		}
		tmpSql.append(" group by device_id, oui, device_serialnumber, city_id,username, usertype, gw_type");
		PrepareSQL psql1 = new PrepareSQL(tmpSql.toString());
		psql1.getSQL();
		cursor = DataSetBean.getCursor(tmpSql.toString());
		return cursor;
	}

	/**
	 * 根据用户名或设备序列号查找设备
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListUseingTime(HttpServletRequest request)
	{
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String city_id = request.getParameter("city_id");
		String device_sn = request.getParameter("device_sn");
		String startTime_int = request.getParameter("startTime_int");
		String endTime_int = request.getParameter("endTime_int");
		// String cpe_currentstatus = request.getParameter("cpe_currentstatus");
		String linkparam = "";
		long startTime = 0;
		long endTime = 0;
		if (null != start_day && null != start_time && null != end_day
				&& null != end_time)
		{
			String startdateStr = start_day + " " + start_time;
			String enddateStr = end_day + " " + end_time;
			startTime = new DateTimeUtil(startdateStr).getLongTime();
			endTime = new DateTimeUtil(enddateStr).getLongTime();
		}
		if (null != startTime_int && null != endTime_int)
		{
			startTime = Long.parseLong(startTime_int);
			endTime = Long.parseLong(endTime_int);
		}
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		// String tmpSql =
		// "select device_id, oui, device_serialnumber, city_id, gw_type, complete_time, cpe_currentstatus from tab_gw_device where 1=1";
		String tmpSql = "select device_id, oui, device_serialnumber, city_id, gw_type, complete_time from tab_gw_device where 1=1";
		// if (city_id != null && !city_id.equals("")) {
		if (city_id == null || city_id.equals("") || "-1".equals(city_id))
		{
			city_id = user.getCityId();
		}
		if ("00".equals(city_id))
		{
			// SelectCityFilter scf = new SelectCityFilter();
			// tmpSql += " and city_id in (" + scf.getAllSubCityIds(city_id,
			// true) + ")";
		}
		else
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			tmpSql += " and city_id in (" + StringUtils.weave(list) + ")";
			list = null;
		}
		linkparam += "&city_id=" + city_id;
		if (startTime != 0 && !"".equals(startTime))
		{
			tmpSql += " and complete_time >= " + startTime;
			linkparam += "&startTime_int=" + startTime;
		}
		if (endTime != 0 && !"".equals(endTime))
		{
			tmpSql += " and complete_time <=" + endTime;
			linkparam += "&endTime_int=" + endTime;
		}
		if (device_sn != null && !"".equals(device_sn))
		{
			if (device_sn.length() > 5)
			{
				tmpSql += " and dev_sub_sn ='"
						+ device_sn.substring(device_sn.length() - 6, device_sn.length())
						+ "'";
			}
			tmpSql += " and device_serialnumber like '%" + device_sn + "'";
			linkparam += "&device_sn=" + device_sn;
		}
		// if (cpe_currentstatus != null && !cpe_currentstatus.isEmpty()) {
		// tmpSql += " and cpe_currentstatus=" + cpe_currentstatus;
		// linkparam += "&cpe_currentstatus=" + cpe_currentstatus;
		// }
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(tmpSql, offset, pagelen);
		cursor = DataSetBean.getCursor(tmpSql, offset, pagelen);
		String strBar = qryp.getPageBar(linkparam);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 根据用户名或设备序列号查找设备
	 * 
	 * @param request
	 * @return
	 */
	// public ArrayList getSheetList_bss(HttpServletRequest request) {
	//
	// String start_day = request.getParameter("start_day");
	// String start_time = request.getParameter("start_time");
	// String end_day = request.getParameter("end_day");
	// String end_time = request.getParameter("end_time");
	// String city_id = request.getParameter("city_id");
	// String username = request.getParameter("username");
	// String startTime_int = request.getParameter("startTime_int");
	// String endTime_int = request.getParameter("endTime_int");
	// String linkparam = "";
	//
	// long startTime = 0;
	// long endTime = 0;
	// if (null != start_day && null != start_time && null != end_day
	// && null != end_time) {
	// String startdateStr = start_day + " " + start_time;
	// String enddateStr = end_day + " " + end_time;
	// startTime = new DateTimeUtil(startdateStr).getLongTime();
	// endTime = new DateTimeUtil(enddateStr).getLongTime();
	// }
	//
	// if (null != startTime_int && null != endTime_int) {
	// startTime = Long.parseLong(startTime_int);
	// endTime = Long.parseLong(endTime_int);
	// }
	//
	// // 获取用户信息
	// HttpSession session = request.getSession();
	// UserRes curUser = (UserRes) session.getAttribute("curUser");
	// User user = curUser.getUser();
	// long area_id = user.getAreaId();
	//
	// String tmpSql =
	// "select serv_type_id, oper_type_id, username, oui, device_serialnumber, city_id, e_id, e_username, result,receive_date from tab_bbms_bsn_open_original where 1=1";
	//
	// if (city_id != null && !city_id.equals("")) {
	// if (!user.isAdmin()) {
	// SelectCityFilter scf = new SelectCityFilter();
	// tmpSql += " and city_id in ("
	// + scf.getAllSubCityIds(city_id, true) + ")";
	// }
	// linkparam += "&city_id=" + city_id;
	// }
	//
	// if (startTime != 0 && !"".equals(startTime)) {
	// tmpSql += " and receive_date >= " + startTime;
	// linkparam += "&startTime_int=" + startTime;
	// }
	// if (endTime != 0 && !"".equals(endTime)) {
	// tmpSql += " and receive_date <=" + endTime;
	// linkparam += "&endTime_int=" + endTime;
	// }
	// if (username != null && !"".equals(username)) {
	// tmpSql += " and username like '%" + username + "%'";
	// linkparam += "&username=" + username;
	// }
	//
	// // tmpSql += " group by device_id, oui, device_serialnumber,
	// // city_id,username, usertype, gw_type";
	//
	// logger.debug("-----------------------SQL:" + tmpSql);
	//
	// // cursor = DataSetBean.getCursor(tmpSql);
	//
	// ArrayList list = new ArrayList();
	// list.clear();
	// String stroffset = request.getParameter("offset");
	// int pagelen = 50;
	// int offset;
	// if (stroffset == null)
	// offset = 1;
	// else
	// offset = Integer.parseInt(stroffset);
	// QueryPage qryp = new QueryPage();
	// qryp.initPage(tmpSql, offset, pagelen);
	// cursor = DataSetBean.getCursor(tmpSql, offset, pagelen);
	// String strBar = qryp.getPageBar(linkparam);
	// list.add(strBar);
	// list.add(cursor);
	// return list;
	// }
	/**
     * 
     */
	public String getTotal()
	{
		if (null != countNumMap)
		{
			return (String) countNumMap.get("total");
		}
		else
		{
			return "0";
		}
	}

	/**
	 * 根据用户名或设备序列号查找设备交互信息
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceInformStat(HttpServletRequest request)
	{
		String username = request.getParameter("username");
		String serialnumber = request.getParameter("device_serialnumber");
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String startdateStr = start_day + " " + start_time;
		String enddateStr = end_day + " " + end_time;
		long startTime = new DateTimeUtil(startdateStr).getLongTime();
		long endTime = new DateTimeUtil(enddateStr).getLongTime();
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpSql = "select * from tab_inform_log where 1=1";
		if (username != null && !username.equals(""))
		{
			tmpSql += " and oui like '%" + username + "%'";
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			tmpSql += " and device_serialnumber like '%" + serialnumber + "%'";
		}
		if (startTime != 0 && !"".equals(startTime))
		{
			tmpSql += " and time >= " + startTime;
		}
		if (endTime != 0 && !"".equals(endTime))
		{
			tmpSql += " and time <=" + endTime;
		}
		tmpSql += " order by time desc ";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(tmpSql);
		return cursor;
	}

	/**
	 * @param request
	 * @param pos
	 * @param rename
	 * @return
	 */
	public String getDeviceSoftVersionByOUIAndDeviceModel(HttpServletRequest request,
			String pos, String rename)
	{
		// String vendor_id = request.getParameter("vendor_id");
		// String devicetype_id = request.getParameter("devicetype_id");
		// pSQL.setSQL(getDeviceSoftVersionByOUIAndDeviceTypeId);
		// pSQL.setString(1, vendor_id);
		// pSQL.setInt(2, Integer.parseInt(devicetype_id));
		// logger.debug("getDeviceSoftVersionByOUIAndDeviceModel_SQL:" +
		// pSQL.getSQL());
		// cursor = DataSetBean.getCursor(pSQL.getSQL());
		// String versionStr = FormUtil.createListBox(cursor, "devicetype_id",
		// "softwareversion",
		// true, pos, rename);
		String versionStr = getDeviceSoftVersionByOUIAndDeviceTypeID(request, pos, rename);
		return versionStr;
	}

	/**
	 * @param request
	 * @param pos
	 * @param rename
	 * @return
	 */
	public String getDeviceSoftVersionByOUIAndDeviceTypeID(HttpServletRequest request,
			String pos, String rename)
	{
		String strSQL = "select devicetype_id,softwareversion from tab_devicetype_info where device_model_id=?";
		// String vendor_id = request.getParameter("vendor_id");
		String deviceModelId = request.getParameter("device_model_id");
		pSQL.setSQL(strSQL);
		// pSQL.setString(1, vendor_id);
		// pSQL.setInt(2, Integer.parseInt(deviceModel));
		pSQL.setString(1, deviceModelId);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		String versionStr = FormUtil.createListBox(cursor, "devicetype_id",
				"softwareversion", true, pos, rename);
		return versionStr;
	}

	/**
	 * @param request
	 * @param pos
	 * @param rename
	 * @return
	 */
	public String getDeviceVersionByVendorIDAndDeviceModel(HttpServletRequest request,
			String pos, String rename)
	{
		// String vendor_id = request.getParameter("vendor_id");
		String devicetype_id = request.getParameter("devicetype_id");
		pSQL.setSQL(m_selectDeviceVersionByOUIAndDeviceModel);
		// pSQL.setString(1, vendor_id);
		// pSQL.setStringExt(2, devicetype_id, false);
		pSQL.setStringExt(1, devicetype_id, false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		String versionStr = FormUtil.createListBox(cursor, "softwareversion",
				"softwareversion", true, pos, rename);
		return versionStr;
	}

	/**
	 * 根据OUI查找设备型号
	 * 
	 * @param oui
	 * @return
	 */
	public Cursor getDeviceModelByOUI(String oui)
	{
		pSQL.setSQL(m_selectDeviceModelByOUI);
		pSQL.setString(1, oui);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		return cursor;
	}

	/**
	 * 根据hgwUserName来查询用户权限范围内的设备
	 * 
	 * @param hgwUserName
	 * @return
	 */
	public Cursor getDeviceInfoByHgwUserName(HttpServletRequest request,
			String hgwUserName)
	{
		pSQL.setSQL(m_selectDeviceByHGWUserName);
		pSQL.setStringExt(1, hgwUserName, true);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// 增加判断 如果是管理员，则不需要权限过滤
		if (curUser.getUser().isAdmin())
		{
			return cursor;
		}
		Cursor resultCursor = new Cursor();
		Map fields = cursor.getNext();
		if (null != fields)
		{
			List deviceidList = curUser.getUserDevRes();
			String device_id = "";
			while (null != fields)
			{
				device_id = (String) fields.get("device_id");
				if (deviceidList.contains(device_id))
				{
					resultCursor.add(fields);
				}
				fields = cursor.getNext();
			}
			cursor = null;
			fields = null;
		}
		return resultCursor;
	}

	/**
	 * 根据hgwUserName来查询用户权限范围内的并且为开户状态的设备
	 * 
	 * @param request
	 * @param hgwUserName
	 * @param user_state
	 *            1:开户 2:暂停 3:销户
	 * @return
	 */
	public Map getDeviceInfoByHgwUserState(HttpServletRequest request,
			String hgwUserName, String user_state)
	{
		Cursor cursor = getDeviceInfoByHgwUserName(request, hgwUserName);
		Map fields = cursor.getNext();
		if (fields == null)
			return null;
		while (fields != null)
		{
			if (user_state.equals(fields.get("user_state")))
			{
				return fields;
			}
			fields = cursor.getNext();
		}
		return null;
	}

	/**
	 * 获取特定OUI相关联的设备序列号
	 * 
	 * @param OUI
	 * @return
	 */
	public Cursor getDeviceSerailByOUI(String OUI)
	{
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(m_selectDeviceSerialByOUI);
		pSQL.setStringExt(1, OUI, true);
		return DataSetBean.getCursor(pSQL.getSQL());
	}

	/**
	 * 根据区域编号获得采集机信息
	 * 
	 * @param m_AreaId
	 * @return ArrayList
	 */
	public ArrayList getGathersWithAreaId(String m_AreaId)
	{
		ArrayList list = new ArrayList();
		pSQL.setSQL(m_Gathers_ByAreaId);
		pSQL.setInt(1, Integer.parseInt(m_AreaId));
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map field = cursor.getNext();
		while (field != null)
		{
			list.add(field.get("res_id"));
			field = cursor.getNext();
		}
		field = null;
		cursor = null;
		return list;
	}

	/**
	 * 根据设备编码ip/gather_id 检测ip是否存在
	 * 
	 * @param m_DeviceId
	 * @return String
	 */
	public String checkIp(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		List gatherList = curUser.getUserProcesses();
		String gather_ids = StringUtils.weave(gatherList);
		String ip = request.getParameter("ip");
		logger.debug(ip);
		String sql = " select device_name,device_id_ex from tab_deviceresource where loopback_ip='"
				+ ip + "' and gather_id in(" + gather_ids + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap name = DataSetBean.getRecord(sql);
		if (name != null)
		{
			if (name.get("device_name") != null)
				return name.get("device_name").toString();
			else if (name.get("device_id_ex") != null)
				return name.get("device_id_ex").toString();
			else
				return null;
		}
		else
			return null;
	}

	/**
	 * 获取厂商列表
	 * 
	 * @param flag
	 *            事件关联
	 * @return
	 */
	public String getVendorList(boolean flag, String compare, String rename)
	{
		// if(StringUtils.getDB() == 1){//sybase
		// //select vendor_id,vendor_name + '(' + vendor_id + ')' from
		// tab_vendor
		// }else{
		//
		// }
		PrepareSQL psql = new PrepareSQL(m_VendorList);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_VendorList);
		// String strVendorList = FormUtil.createListBox(cursor, "vendor_id",
		// "vendor_name", flag, compare, rename);
		// 先暂时改过来,等讨论以后再定论
		String strVendorList = FormUtil.createListBox(cursor, "vendor_id", "vendor_add",
				flag, compare, rename, true);
		cursor = null;
		return strVendorList;
	}

	/**
	 * 获取厂商列表
	 * 
	 * @param flag
	 *            事件关联
	 * @return
	 */
	public String getVendorList2(boolean flag, String compare, String rename)
	{
		// if(StringUtils.getDB() == 1){//sybase
		// //select vendor_id,vendor_name + '(' + vendor_id + ')' from
		// tab_vendor
		// }else{
		//
		// }
		PrepareSQL psql = new PrepareSQL(m_VendorList);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_VendorList);
		String strVendorList = FormUtil.createListBox(cursor, "vendor_id", "vendor_name",
				flag, compare, rename, true, true);
		// String strVendorList = FormUtil.createListBox(cursor, "vendor_id",
		// "vendor_add", flag, compare, rename, true, true);
		cursor = null;
		return strVendorList;
	}

	/**
	 * 获取厂商列表，不需要oui
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @return
	 */
	public String getVendorWithoutOUIList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_selectDeviceVendorSql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_selectDeviceVendorSql);
		String strVendorList = FormUtil.createListBox(cursor, "vendor_id", "vendor_name",
				flag, compare, rename);
		cursor = null;
		return strVendorList;
	}

	/**
	 * 根据厂商获取设备型号
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getDeviceModelByVendorID1(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		String temOper = "+";
		// String strSQL = "select devicetype_id,device_model" + temOper + "'('"
		// + temOper
		// + "softwareversion" + temOper + "')'"
		// + " as device_model from tab_devicetype_info where 1=1 ";
		// if (strv_id != null)
		// strSQL += " and oui='" + strv_id + "'";
		String strSQL = "select device_model_id,device_model from gw_device_model where 1=1";
		if (strv_id != null)
		{
			strSQL += " and vendor_id='" + strv_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strDeviceModel = FormUtil.createListBox(cursor, "device_model_id",
				"device_model", true, pos, rename);
		// String strDeviceModel = FormUtil.createListBox(cursor,
		// "devicetype_id",
		// "device_model", true, pos, rename, true, true);
		return strDeviceModel;
	}

	/**
	 * 根据厂商获取设备型号
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getDeviceModelByVendorID2(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		String strSQL = "select a.devicetype_id,b.device_model from tab_devicetype_info a, gw_device_model b where a.device_model_id=b.device_model_id ";
		if (strv_id != null)
		{
			strSQL += " and vendor_id='" + strv_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strDeviceModel = FormUtil.createListBox(cursor, "devicetype_id",
				"device_model", true, pos, rename, true, true);
		return strDeviceModel;
	}

	/**
	 * 根据厂商获取设备型号(不要软件版本)
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getDeviceModelByVendorID(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		// if(StringUtils.getDB() == 1){//sybase
		// //temOper = "+";
		// }else{
		// //temOper = "||";
		// }
		String device_type = request.getParameter("device_type");
		// 如果是SNMP企业网关设备
		String strSQL = "";
		String strDeviceModel = "";
		logger.debug("device_type :" + device_type);
		if (device_type != null && device_type.equals("snmp"))
		{
			strSQL = "select serial,device_name from tab_devicetype_info where 1=1 ";
			if (strv_id != null)
				strSQL += " and vendor_id='" + strv_id + "' ";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			cursor = DataSetBean.getCursor(strSQL);
			strDeviceModel = FormUtil.createListBox(cursor, "device_name", "device_name",
					false, pos, "device_model");
		}
		else
		{
			// strSQL =
			// "select devicetype_id,device_model + '(' + softwareversion + ')' as device_model from tab_devicetype_info where 1=1 ";
			// if (strv_id != null)
			// strSQL += " and oui='" + strv_id + "'";
			//
			// cursor = DataSetBean.getCursor(strSQL);
			// strDeviceModel = FormUtil.createListBox(cursor, "devicetype_id",
			// "device_model", true,
			// pos, rename);
			strDeviceModel = getDeviceModelByVendorID1(request, pos, rename);
		}
		// String strSQL = "select devicetype_id,device_model + '(' +
		// softwareversion + ')' as device_model from tab_devicetype_info where
		// 1=1 ";
		return strDeviceModel;
	}

	/**
	 * 根据厂商获取软件版本
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getDeviceVersionByVendorID(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		// if(StringUtils.getDB() == 1){//sybase
		// //temOper = "+";
		// }else{
		// //temOper = "||";
		// }
		String device_type = request.getParameter("device_type");
		// 如果是SNMP企业网关设备
		String strSQL = "";
		String strDeviceVersion = "";
		logger.debug("device_type :" + device_type);
		if (device_type != null && device_type.equals("snmp"))
		{
			strSQL = "select serial,device_name from tab_devicetype_info where 1=1 ";
			if (strv_id != null)
				strSQL += " and vendor_id='" + strv_id + "' ";
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			cursor = DataSetBean.getCursor(strSQL);
			strDeviceVersion = FormUtil.createListBox(cursor, "device_name",
					"device_name", false, pos, "device_model");
		}
		else
		{
			strSQL = "select distinct devicetype_id, softwareversion from tab_devicetype_info where 1=1 ";
			if (strv_id != null)
			{
				// strSQL += " and oui='" + strv_id + "'";
				strSQL += " and vendor_id='" + strv_id + "'";
			}
			PrepareSQL psql = new PrepareSQL(strSQL);
			psql.getSQL();
			cursor = DataSetBean.getCursor(strSQL);
			strDeviceVersion = FormUtil.createListBox(cursor, "devicetype_id",
					"softwareversion", false, pos, rename);
		}
		return strDeviceVersion;
	}

	/**
	 * 根据厂商获取设备型号(不要设备类型)
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getOnlyDeviceModelByOUI(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		String strSQL = "select distinct device_model_id, device_model from gw_device_model where 1=1 ";
		if (strv_id != null)
			strSQL += " and vendor_id='" + strv_id + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strDeviceModel = FormUtil.createListBox(cursor, "device_model_id",
				"device_model", true, pos, rename);
		// String strDeviceModel =
		// getDeviceModelByVendorID1(request,pos,rename);
		return strDeviceModel;
	}

	/**
	 * 根据厂商获取设备型号(不要设备类型)
	 * 
	 * @param vendor_id
	 * @return
	 */
	public String getDeviceModelByOUI(HttpServletRequest request, String pos,
			String rename)
	{
		String strv_id = request.getParameter("vendor_id");
		// if(StringUtils.getDB() == 1){//sybase
		// //temOper = "+";
		// }else{
		// //temOper = "||";
		// }
		// String strSQL =
		// "select devicetype_id, device_model from tab_devicetype_info where 1=1 ";
		// if (strv_id != null)
		// strSQL += " and oui='" + strv_id + "'";
		// logger.debug("getDeviceModelByVendorID_sql:" + strSQL);
		// cursor = DataSetBean.getCursor(strSQL);
		// String strDeviceModel = FormUtil.createListBox(cursor,
		// "devicetype_id",
		// "device_model",
		// true, pos, rename);
		String strDeviceModel = getDeviceModelByVendorID1(request, pos, rename);
		return strDeviceModel;
	}

	/**
	 * 选择当前用户区域下的所有子域及当前用户所在域
	 * 
	 * @author byp
	 */
	public String QueryArea(HttpServletRequest request)
	{
		// 根据用户过滤管理域
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		String strUserDomains = "";
		String domain_SQL = "";
		boolean flg;
		if (curUser.getUser().isAdmin())
		{
			domain_SQL = "select area_name area from tab_area where 1=1";
			flg = false;
		}
		else
		{
			domain_SQL = "select a.area_name parea ,b.area_name area from tab_area a,tab_area b where a.area_id = b.area_pid and a.area_id="
					+ areaid;
			flg = true;
		}
		PrepareSQL psql = new PrepareSQL(domain_SQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(domain_SQL);
		Map fields = cursor.getNext();
		if (fields == null)
		{
			strUserDomains += "<select name='area' class='bk'>";
			strUserDomains += "<option value='-2'>==此项没有记录==</option>";
			strUserDomains += "</select>";
		}
		else
		{
			strUserDomains += "<select name='area' class='bk'>";
			strUserDomains += "<option value='-1'>==请选择==</option>";
			if (flg)
			{
				strUserDomains += "<option value='" + (String) fields.get("parea") + "'>"
						+ (String) fields.get("parea") + "</option>";
				fields = cursor.getNext();
				// logger.debug("当前域" + (String) fields.get("parea"));
			}
			while (fields != null)
			{
				strUserDomains += "<option value='" + (String) fields.get("area") + "'>"
						+ (String) fields.get("area") + "</option>";
				fields = cursor.getNext();
			}
			strUserDomains += "</select>";
		}
		return strUserDomains;
	}

	/**
	 * 供查询设备使用
	 * 
	 * @param request
	 * @return
	 */
	public List QueryDeviceList(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		// String gw_type_Str = null;
		// String he_type_str = request.getParameter("he_type");
		// if (null == he_type_str || "0".equals(he_type_str))
		// {
		// gw_type_Str = request.getParameter("gw_type");
		// if (null == gw_type_Str)
		// {
		// gw_type_Str = "1";
		// }
		// }
		// else
		// {
		// gw_type_Str = request.getParameter("he_type");
		// }
		String gw_type_Str = request.getParameter("gw_type");
		String he_type_str = request.getParameter("he_type");
		if (null != gw_type_Str && "3".equals(gw_type_Str))
		{
			gw_type_Str = he_type_str;
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		// modify by lizhaojun 2007-12-03 NJLC_HG-BUG-ZHOUMF-20071031-002
		String linkParam = "&gw_type=" + gw_type + "&he_type=" + he_type_str;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		/**
		 * modify by qixueqi modify time 2010-6-22 15:08 because delete area
		 */
		/**
		 * String sqlDevice =
		 * "select a.*,b.area_id from tab_gw_device a,gw_devicestatus c ,tab_gw_res_area b where  a.device_id=b.res_id and a.device_id=c.device_id and b.res_type=1 and a.gw_type="
		 * + gw_type + " and b.area_id=" + areaid; if (curUser.getUser().isAdmin()) {
		 * sqlDevice = "select a.*,area_id=" + areaid +
		 * " from tab_gw_device a,gw_devicestatus c where a.device_id=c.device_id and a.gw_type="
		 * + gw_type + " "; }
		 */
		String sqlDevice = "";
		if (0 == gw_type)
		{
			sqlDevice = "select a.* from tab_gw_device a,gw_devicestatus c "
					+ " where a.device_id=c.device_id ";
		}
		else
		{
			if (Global.HBLT.equals(Global.instAreaShortName))
			{
				sqlDevice = "select a.*,b.gigabit_port from tab_gw_device a left join tab_device_version_attribute b on (a.devicetype_id = b.devicetype_id),gw_devicestatus c "
						+ " where a.device_id=c.device_id and a.gw_type=" + gw_type;
			}
			else
			{
				sqlDevice = "select a.*  from tab_gw_device a,gw_devicestatus c "
						+ " where a.device_id=c.device_id  and a.gw_type=" + gw_type;
			}
		}
		// 查找设备条件
		String str_device_id_ex = request.getParameter("device_id_ex");
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_loopback_ip = request.getParameter("loopback_ip");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String str_cpe_mc = request.getParameter("cpe_mc");
		String str_gather_id = request.getParameter("gather_id");
		String str_softwareversion = request.getParameter("softwareversion"); // 软件版本
		String str_status = request.getParameter("status"); // 在线状态
		String str_area = request.getParameter("area");
		String str_deviceserial = request.getParameter("device_serial");
		String str_servicecode = request.getParameter("service_code");
		String str_deviceModelId = request.getParameter("device_model_id");
		String str_devType = request.getParameter("type_name");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String device_status = request.getParameter("device_status");
		String starttime1;
		String endtime1;
		if (null == str_city_id || "".equals(str_city_id) || "-1".equals(str_city_id))
		{
			str_city_id = curUser.getCityId();
		}
		// 时间转换
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
		logger.debug("---------------1:" + str_device_id_ex + "#" + str_devicetype_id
				+ "#" + str_loopback_ip + "#" + str_vendor_id + "#" + str_city_id + "#"
				+ str_cpe_mc + "#" + str_gather_id + "#" + str_softwareversion + "#"
				+ str_status + "#" + str_area + "#" + str_deviceserial + "#"
				+ str_servicecode + "#" + str_devType + "#" + starttime + "#" + endtime
				+ "#" + gw_type);
		if (null != str_softwareversion && !"".equals(str_softwareversion)
				&& !"-1".equals(str_softwareversion))
		{
			logger.debug("str_softwareversion:" + str_softwareversion);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_devicetype_info where softwareversion='"
					+ str_softwareversion + "')";
			linkParam += "&softwareversion=" + str_softwareversion;
		}
		// 选择了在线状态
		if (null != str_status && !"".equals(str_status) && !"-1".equals(str_status))
		{
			logger.debug("str_status:" + str_status);
			if ("2".equals(str_status))
			{
				sqlDevice += " and c.online_status is null";
			}
			else
			{
				sqlDevice += " and c.online_status =" + str_status;
			}
			linkParam += "&status=" + str_status;
		}
		/**
		 * if (null != str_area && !"".equals(str_area) && !"-1".equals(str_area)) {
		 * logger.debug("str_area:" + str_area); if (!"admin.com".equals(str_area)) {
		 * sqlDevice +=
		 * " and a.device_id in(select res_id from tab_gw_res_area f,tab_area g where f.area_id =g.area_id and g.area_name='"
		 * + str_area + "' and f.res_type=1)"; } linkParam += "&area=" + str_area; }
		 */
		if (null != str_deviceserial && !"".equals(str_deviceserial))
		{
			str_deviceserial = str_deviceserial.trim();
			logger.debug("str_deviceserial:" + str_deviceserial);
			if (str_deviceserial.length() > 5)
			{
				sqlDevice += " and a.dev_sub_sn ='"
						+ str_deviceserial.substring(str_deviceserial.length() - 6,
								str_deviceserial.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%" + str_deviceserial + "'";
			linkParam += "&device_serial=" + str_deviceserial;
		}
		if (null != str_servicecode && !"".equals(str_servicecode))
		{
			logger.debug("str_servicecode:" + str_servicecode);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_servicecode where servicecode='"
					+ str_servicecode + "')";
			linkParam += "&service_code=" + str_servicecode;
		}
		if (str_device_id_ex != null && !str_device_id_ex.equals(""))
		{
			sqlDevice += " and a.device_id_ex like'%" + str_device_id_ex + "%' ";
			linkParam += "&device_id_ex=" + str_device_id_ex;
		}
		if (str_loopback_ip != null && !str_loopback_ip.trim().equals(""))
		{
			sqlDevice += " and a.loopback_ip ='" + str_loopback_ip.trim() + "'";
			linkParam += "&loopback_ip=" + str_loopback_ip;
		}
		if (str_cpe_mc != null && !str_cpe_mc.trim().equals(""))
		{
			sqlDevice += " and a.cpe_mac ='" + str_cpe_mc.trim() + "'";
			linkParam += "&cpe_mc=" + str_cpe_mc;
		}
		if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			linkParam += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals(""))
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			linkParam += "&vendor_id=" + str_vendor_id;
		}
		if (str_deviceModelId != null && !str_deviceModelId.equals("-1")
				&& !str_deviceModelId.equals(""))
		{
			sqlDevice += " and a.device_model_id ='" + str_deviceModelId + "' ";
			linkParam += "&device_model_id=" + str_deviceModelId;
		}
		if (!"00".equals(str_city_id))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
			sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
			linkParam += "&city_id=" + str_city_id;
		}
		if (str_gather_id != null && !str_gather_id.equals("")
				&& !str_gather_id.equals("-1"))
		{
			sqlDevice += " and a.gather_id ='" + str_gather_id + "' ";
			linkParam += "&gather_id=" + str_gather_id;
		}
		if (str_devType != null && !str_devType.trim().equals("")
				&& !str_devType.equals("-1"))
		{
			sqlDevice += " and a.device_type ='" + str_devType + "' ";
			linkParam += "&type_name=" + str_devType;
		}
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sqlDevice += " and c.last_time >=" + starttime1 + " ";
			linkParam += "&starttime=" + starttime;
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sqlDevice += " and c.last_time <=" + endtime1 + " ";
			linkParam += "&endtime=" + endtime;
		}
		// 确认/未确认 add by chenjie 2011-6-23
		if (StringUtil.IsEmpty(device_status) == false)
		{
			if (!device_status.equals("-1"))
			{
				sqlDevice += " and a.device_status=" + device_status;
			}
			linkParam += "&device_status=" + device_status;
		}
		logger.debug("获取设备列表信息--------5:" + sqlDevice);
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		// NJLC_HG-BUG-ZHOUMF-20071031-002 modify by lizhaojun 2007-12-03
		logger.debug("linkParam :" + linkParam);
		String strBar = qryp.getPageBar(linkParam);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 读取SNMP设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getSnmpDeviceInfoList(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "select a.*,b.area_id from tab_deviceresource a left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and b.area_id="
				+ areaid + " and a.resource_type_id =101 ";
		if (curUser.getUser().isAdmin())
		{
			// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sqlDevice = "select a.*,"
					+ areaid
					+ " as area_id"
					+ " from tab_deviceresource a where 1=1  and a.resource_type_id =101 ";
			// sqlDevice = "select a.*,area_id=" + areaid
			// +
			// " from tab_deviceresource a where 1=1  and a.resource_type_id =101 ";
		}
		// 查找设备条件
		// String str_device_id_ex = request.getParameter("device_id_ex");
		// String str_devicetype_id = request.getParameter("devicetype_id");
		// String str_loopback_ip = request.getParameter("loopback_ip");
		// String str_vendor_id = request.getParameter("vendor_id");
		// String str_device_status = request.getParameter("device_status");//
		// 是否注册设备
		// String str_city_id = request.getParameter("city_id");
		// String cpe_currentstatus = request.getParameter("cpe_currentstatus");
		// String str_cpe_mc = request.getParameter("cpe_mc");
		// String str_gather_id = request.getParameter("gather_id");
		// String str_softwareversion = request.getParameter("softwareversion");
		// //软件版本
		// String str_status = request.getParameter("status"); //在线状态
		// String str_area = request.getParameter("area");
		// String str_deviceserial = request.getParameter("device_serial");
		// String str_servicecode = request.getParameter("service_code");
		// 模糊查询的序列号
		String serialnumber = request.getParameter("serialnumber");
		// 用户帐号
		String username = request.getParameter("username");
		if (serialnumber != null && !"".equals(serialnumber))
		{
			sqlDevice += " and a.device_serialnumber like'%" + serialnumber + "%'";
		}
		if (username != null && !"".equals(username))
		{
			sqlDevice += " and a.device_id in (select device_id from cus_radiuscustomer where username = '"
					+ username + "')";
		}
		// if (null != str_softwareversion && !"".equals(str_softwareversion)
		// && !"-1".equals(str_softwareversion)) {
		// logger.debug("str_softwareversion:" + str_softwareversion);
		// sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id)
		// from tab_devicetype_info where softwareversion='"
		// + str_softwareversion + "')";
		// }
		//
		// //选择了在线状态
		// if (null != str_status && !"".equals(str_status)
		// && !"-1".equals(str_status) && !"null".equals(str_status)) {
		// logger.debug("str_status:" + str_status);
		// if ("2".equals(str_status)) {
		// sqlDevice += " and a.cpe_currentstatus is null";
		// } else {
		// sqlDevice += " and a.cpe_currentstatus =" + str_status;
		// }
		// }
		//
		// if (null != str_area && !"".equals(str_area)) {
		// logger.debug("str_area:" + str_area);
		// if (!"admin.com".equals(str_area)) {
		// sqlDevice += " and a.device_id in(select res_id from tab_gw_res_area
		// f,tab_area g where f.area_id =g.area_id and g.area_name='"
		// + str_area + "' and f.res_type=1)";
		// }
		// }
		//
		// if (null != str_deviceserial && !"".equals(str_deviceserial)) {
		// logger.debug("str_deviceserial:" + str_deviceserial);
		// sqlDevice += " and a.device_serialnumber ='" + str_deviceserial
		// + "'";
		// }
		//
		// if (null != str_servicecode && !"".equals(str_servicecode)) {
		// logger.debug("str_servicecode:" + str_servicecode);
		// sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id)
		// from tab_servicecode where servicecode='"
		// + str_servicecode + "')";
		// }
		//
		// if (str_device_id_ex != null && !str_device_id_ex.equals("")) {
		// sqlDevice += " and a.device_id_ex ='" + str_device_id_ex + "' ";
		// }
		// if (str_loopback_ip != null && !str_loopback_ip.trim().equals("")) {
		// sqlDevice += " and a.loopback_ip ='" + str_loopback_ip.trim() + "'";
		// }
		// if (str_cpe_mc != null && !str_cpe_mc.trim().equals("")) {
		// sqlDevice += " and a.cpe_mac ='" + str_cpe_mc.trim() + "'";
		// }
		// if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
		// && !str_devicetype_id.equals("")) {
		// sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
		// }
		// if (str_vendor_id != null && !str_vendor_id.equals("-1")
		// && !str_vendor_id.equals("")) {
		// sqlDevice += " and a.oui ='" + str_vendor_id + "' ";
		// }
		// // 设备确认状态
		// if (str_device_status != null && !str_device_status.equals("")) {
		// sqlDevice += " and a.device_status =" + str_device_status;
		// } else {
		// sqlDevice += " and a.device_status=1";
		// }
		// if (cpe_currentstatus != null && !cpe_currentstatus.equals("")
		// && !cpe_currentstatus.equals("-1")) {
		// logger.debug("cpe_currentstatus:" + cpe_currentstatus);
		// if (("_null_").equals(cpe_currentstatus)) {
		// sqlDevice += " and a.cpe_currentstatus is null";//查询出设备未知状态
		// } else
		// sqlDevice += " and a.cpe_currentstatus=" + cpe_currentstatus;
		// }
		// if (str_city_id != null && !str_city_id.equals("")
		// && !str_city_id.equals("-1")) {
		// sqlDevice += " and a.city_id ='" + str_city_id + "' ";
		// }
		//
		// if (str_gather_id != null && !str_gather_id.equals("")
		// && !str_gather_id.equals("-1")) {
		// sqlDevice += " and a.gather_id ='" + str_gather_id + "' ";
		// }
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoList(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String he_type_str = request.getParameter("he_type");
		String filterStr = "";
		if (null == gw_type_Str)
		{
			gw_type_Str = "2";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		String tab_name = "";
		if (gw_type == 1)
		{
			tab_name = "tab_hgwcustomer";
		}
		else if (gw_type == 2)
		{
			tab_name = "tab_egwcustomer";
		}
		else if (gw_type == 3)
		{
			tab_name = "tab_hgwcustomer";
			gw_type = Integer.parseInt(he_type_str);
		}
		filterStr += "&gw_type=" + gw_type + "&he_type=" + he_type_str;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		// 查找设备条件
		String str_device_id_ex = request.getParameter("device_id_ex");
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_loopback_ip = request.getParameter("loopback_ip");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_device_status = request.getParameter("device_status");// 是否注册设备
		String str_city_id = request.getParameter("city_id");
		String cpe_currentstatus = request.getParameter("cpe_currentstatus");
		String str_cpe_mc = request.getParameter("cpe_mc");
		String str_gather_id = request.getParameter("gather_id");
		String str_softwareversion = request.getParameter("softwareversion"); // 软件版本
		String str_status = request.getParameter("status"); // 在线状态
		String str_area = request.getParameter("area");
		String str_deviceserial = request.getParameter("device_serial");
		String str_servicecode = request.getParameter("service_code");
		// 模糊查询的序列号
		String serialnumber = request.getParameter("serialnumber");
		// 用户帐号
		String username = request.getParameter("username");
		long startTime = 0;
		long endTime = 0;
		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		if (null != start_time && null != start_day && null != end_day
				&& null != end_time)
		{
			String startdateStr = start_day + " " + start_time;
			String enddateStr = end_day + " " + end_time;
			startTime = new DateTimeUtil(startdateStr).getLongTime();
			endTime = new DateTimeUtil(enddateStr).getLongTime();
		}
		String sqlDevice = "";
		logger.debug("gw_type参数：" + gw_type);
		if (0 == gw_type)
		{
			sqlDevice = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and b.area_id="
					+ areaid;
		}
		else
		{
			sqlDevice = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and b.area_id="
					+ areaid + " and a.gw_type= " + gw_type + " ";
		}
		if (curUser.getUser().isAdmin())
		{
			// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sqlDevice = "select a.*," + areaid + " as area_id"
					+ " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type + " ";
			// sqlDevice = "select a.*,area_id=" + areaid
			// + " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type +
			// " ";
		}
		if (username != null && !"".equals(username))
		{
			sqlDevice += " and a.oui || a.device_serialnumber in (select oui || device_serialnumber from "
					+ tab_name
					+ " where username = '"
					+ username
					+ "' and user_state='1' )";
			filterStr += "&username=" + username;
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			serialnumber = serialnumber.trim();
			if (serialnumber.length() > 5)
			{
				sqlDevice += " and a.dev_sub_sn ='"
						+ serialnumber.substring(serialnumber.length() - 6,
								serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like'%" + serialnumber + "'";
			filterStr += "&serialnumber=" + serialnumber;
		}
		if (null != str_softwareversion && !"".equals(str_softwareversion)
				&& !"-1".equals(str_softwareversion))
		{
			logger.debug("str_softwareversion:" + str_softwareversion);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_devicetype_info where softwareversion='"
					+ str_softwareversion + "')";
			filterStr += "&softwareversion=" + str_softwareversion;
		}
		// 选择了在线状态
		if (null != str_status && !"".equals(str_status) && !"-1".equals(str_status)
				&& !"null".equals(str_status))
		{
			logger.debug("str_status:" + str_status);
			if ("2".equals(str_status))
			{
				sqlDevice += " and a.cpe_currentstatus is null";
			}
			else
			{
				sqlDevice += " and a.cpe_currentstatus =" + str_status;
			}
			filterStr += "&status=" + str_status;
		}
		if (null != str_area && !"".equals(str_area))
		{
			logger.debug("str_area:" + str_area);
			if (!"admin.com".equals(str_area))
			{
				sqlDevice += " and a.device_id in(select res_id from tab_gw_res_area f,tab_area g where f.area_id =g.area_id and g.area_name='"
						+ str_area + "' and f.res_type=1)";
			}
			filterStr += "&area=" + str_area;
		}
		if (null != str_deviceserial && !"".equals(str_deviceserial))
		{
			logger.debug("str_deviceserial:" + str_deviceserial);
			str_deviceserial = str_deviceserial.trim();
			sqlDevice += " and a.device_serialnumber ='" + str_deviceserial + "'";
			filterStr += "&device_serial=" + str_deviceserial;
		}
		if (null != str_servicecode && !"".equals(str_servicecode))
		{
			logger.debug("str_servicecode:" + str_servicecode);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_servicecode where servicecode='"
					+ str_servicecode + "')";
			filterStr += "&service_code=" + str_servicecode;
		}
		if (str_device_id_ex != null && !str_device_id_ex.equals(""))
		{
			sqlDevice += " and a.device_id_ex ='" + str_device_id_ex + "' ";
			filterStr += "&device_id_ex=" + str_device_id_ex;
		}
		if (str_loopback_ip != null && !str_loopback_ip.trim().equals(""))
		{
			sqlDevice += " and a.loopback_ip = '" + str_loopback_ip.trim() + "'";
			filterStr += "&loopback_ip=" + str_loopback_ip;
		}
		if (str_cpe_mc != null && !str_cpe_mc.trim().equals(""))
		{
			sqlDevice += " and a.cpe_mac ='" + str_cpe_mc.trim() + "'";
			filterStr += "&cpe_mc=" + str_cpe_mc;
		}
		if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			filterStr += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals(""))
		{
			// sqlDevice += " and a.oui ='" + str_vendor_id + "' ";
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			filterStr += "&vendor_id=" + str_vendor_id;
		}
		// 设备确认状态
		if (str_device_status != null && !str_device_status.equals(""))
		{
			sqlDevice += " and a.device_status =" + str_device_status;
			filterStr += "&device_status=" + str_device_status;
		}
		else
		{
			sqlDevice += " and a.device_status > 0";
		}
		if (cpe_currentstatus != null && !cpe_currentstatus.equals("")
				&& !cpe_currentstatus.equals("-1"))
		{
			logger.debug("cpe_currentstatus:" + cpe_currentstatus);
			if (("_null_").equals(cpe_currentstatus))
			{
				sqlDevice += " and a.cpe_currentstatus is null";// 查询出设备未知状态
			}
			else
				sqlDevice += " and a.cpe_currentstatus=" + cpe_currentstatus;
			filterStr += "&cpe_currentstatus=" + cpe_currentstatus;
		}
		if (str_city_id != null && !str_city_id.equals("") && !str_city_id.equals("-1"))
		{
			sqlDevice += " and a.city_id ='" + str_city_id + "' ";
			filterStr += "&city_id=" + str_city_id;
		}
		if (str_gather_id != null && !str_gather_id.equals("")
				&& !str_gather_id.equals("-1"))
		{
			sqlDevice += " and a.gather_id ='" + str_gather_id + "' ";
			filterStr += "&gather_id=" + str_gather_id;
		}
		if (startTime != 0 && !"".equals(start_time))
		{
			sqlDevice += " and a.cpe_currentupdatetime >= " + startTime;
			filterStr += " &cpe_currentupdatetime >= " + startTime;
		}
		if (endTime != 0 && !"".equals(endTime))
		{
			sqlDevice += " and a.cpe_currentupdatetime <=" + endTime;
			filterStr += " &cpe_currentupdatetime <=" + endTime;
		}
		// 设备列表呈现时，先按属地排序，再按厂商，设备类型排
		sqlDevice += " order by a.city_id, a.oui, a.device_model_id";
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(filterStr);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 未确认，未分配域的设备列表
	 * 
	 * @author lizj （5202）
	 * @param request
	 * @return
	 */
	public List getUnconfirmDeviceInfoList(HttpServletRequest request)
	{
		String device_serialnumber = request.getParameter("device_serialnumber");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String city_id = curUser.getCityId();
		if (null == device_serialnumber)
		{
			device_serialnumber = "";
		}
		String sqlDevice = " select * from tab_gw_device where device_status=0  ";
		if (device_serialnumber != null && !"".equals(device_serialnumber))
		{
			device_serialnumber = device_serialnumber.trim();
			if (device_serialnumber.length() > 5)
			{
				sqlDevice += " and dev_sub_sn ='"
						+ device_serialnumber.substring(device_serialnumber.length() - 6,
								device_serialnumber.length()) + "'";
			}
			sqlDevice += " and device_serialnumber like'%" + device_serialnumber + "'";
		}
		if (!user.isAdmin())
		{
			SelectCityFilter scf = new SelectCityFilter();
			sqlDevice += " and city_id in (" + scf.getAllSubCityIds(city_id, true) + ")";
		}
		sqlDevice += " order by device_id";
		String stroffset = request.getParameter("offset");
		ArrayList list = new ArrayList();
		list.clear();
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar("&device_serialnumber=" + device_serialnumber);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 根据设备状态设备统计结果
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getStatDeviceInfoList(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		if (gw_type == 3)
		{
			String he_type_str = request.getParameter("he_type");
			if (null != he_type_str)
			{
				gw_type = Integer.parseInt(he_type_str);
			}
		}
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		ArrayList list = new ArrayList();
		list.clear();
		// String sqlDevice =
		// "select a.*,b.area_id from tab_gw_device a,gw_devicestatus c left join tab_gw_res_area b on a.device_id=b.res_id where a.device_id=c.device_id and b.res_type=1 and a.device_status != -1 and a.gw_type="
		PrepareSQL pSQL = new PrepareSQL();
		// 查找设备条件
		String stroffset = request.getParameter("offset");
		String deviceStatus = request.getParameter("device_status");
		String cpeCurrentstatus = request.getParameter("cpe_currentstatus");
		String cpeAllocatedstatus = "";
		if (LipossGlobals.inArea(Global.NXDX) || LipossGlobals.inArea(Global.JXDX))
		{
			cpeAllocatedstatus = request.getParameter("cpe_allocatedstatus");
		}
		String last_time = String.valueOf(nowTime - 12 * 3600);
		if (gw_type == 0)
		{
			pSQL.setSQL(" select a.* from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id ");
		}
		else
		{
			pSQL.setSQL(" select a.* from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id and a.gw_type= "
					+ gw_type);
		}
		if (!curUser.getUser().isAdmin())
		{
			pSQL.append(PrepareSQL.AND, "a.city_id",
					CityDAO.getAllNextCityIdsByCityPid(curUser.getCityId()));
		}
		if (null != cpeCurrentstatus)
		{
			if ("1".equals(cpeCurrentstatus))
			{
				pSQL.append(" and b.online_status=1 and b.last_time>");
				pSQL.append(last_time);
			}
			else if ("0".equals(cpeCurrentstatus))
			{
				pSQL.append(" and (b.online_status=0 or (b.online_status=1 and b.last_time<");
				pSQL.append(last_time);
				pSQL.append(")) ");
			}
			else
			{
				pSQL.append(" and b.online_status=null ");
			}
		}
		//
		//
		// if(cpeCurrentstatus.equals("1")){
		// sqlDevice = "select a.*,area_id=" + areaid
		// +
		// " from tab_gw_device a,gw_devicestatus c where c.device_id in(select device_id from gw_devicestatus where ("+nowTime+"-last_time)<=43200) and a.device_id=c.device_id and a.device_status != -1 and a.gw_type="
		// + gw_type + " and c.online_status= "+ cpeCurrentstatus+" ";
		// }
		// else if(cpeCurrentstatus.equals("0")){
		// sqlDevice = "select a.*,area_id=" + areaid
		// +
		// " from tab_gw_device a,gw_devicestatus c where a.device_id=c.device_id and a.device_status != -1 and a.gw_type="
		// + gw_type +
		// " and (c.device_id in(select device_id from gw_devicestatus where ("+nowTime+"-last_time)>43200) or c.online_status= "
		// + cpeCurrentstatus+") ";
		// }else{
		// sqlDevice = "select a.*,area_id=" + areaid
		// +
		// " from tab_gw_device a,gw_devicestatus c where a.device_id=c.device_id and a.device_status != -1 and a.gw_type="
		// + gw_type + " and c.online_status= "+ cpeCurrentstatus+" ";
		// }
		// if(cpeCurrentstatus.equals("1")){
		// sqlDevice = "select a.*,b.area_id "
		// + " from tab_gw_device a left join gw_devicestatus c "
		// +
		// " on (a.device_id=c.device_id and a.device_status != -1 and c.device_id in(select device_id from gw_devicestatus where ("+nowTime+"-last_time)<=43200))"
		// +
		// " left join tab_gw_res_area b on (a.device_id=b.res_id and b.res_type=1) "
		// + " and a.gw_type="
		// + gw_type + " and c.online_status= "+
		// cpeCurrentstatus+" and b.area_id=" +
		// areaid;
		// }
		// else if(cpeCurrentstatus.equals("0")){
		// sqlDevice = "select a.*,b.area_id "
		// + " from tab_gw_device a left join gw_devicestatus c "
		// +
		// " on (a.device_id=c.device_id and a.device_status != -1 and c.device_id in(select device_id from gw_devicestatus where ("+nowTime+"-last_time)<=43200))"
		// +
		// " left join tab_gw_res_area b on (a.device_id=b.res_id and b.res_type=1) "
		// + " and a.gw_type="
		// + gw_type + " and c.online_status= "+
		// cpeCurrentstatus+" and b.area_id=" +
		// areaid;
		// }else{
		// sqlDevice = "select a.*,b.area_id "
		// + " from tab_gw_device a left join gw_devicestatus c "
		// + " on (a.device_id=c.device_id and a.device_status != -1)"
		// +
		// " left join tab_gw_res_area b on (a.device_id=b.res_id and b.res_type=1) "
		// + " and a.gw_type="
		// + gw_type + " and c.online_status= "+
		// cpeCurrentstatus+" and b.area_id=" +
		// areaid;
		// }
		if (deviceStatus != null)
		{
			pSQL.append(" and a.device_status=" + deviceStatus);
		}
		if (null != cpeAllocatedstatus)
		{
			if ("1".equals(cpeAllocatedstatus))
			{
				pSQL.append(" and a.customer_id is not null");
			}
			else
			{
				pSQL.append(" and a.customer_id is null ");
			}
		}
		String sqlStr = pSQL.getSQL();
		String strBar = "";
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlStr, offset, pagelen);
		cursor = DataSetBean.getCursor(
				sqlStr,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
		if (deviceStatus != null)
		{
			strBar = qryp.getPageBar("&device_status=" + deviceStatus + "&gw_type="
					+ gw_type + "&he_type");
		}
		else if (cpeCurrentstatus != null)
		{
			strBar = qryp.getPageBar("&cpe_currentstatus=" + cpeCurrentstatus
					+ "&gw_type=" + gw_type);
		}
		else
		{
			strBar = qryp.getPageBar("&cpe_allocatedstatus=" + cpeAllocatedstatus
					+ "&gw_type=" + gw_type);
		}
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoListByTime(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String filterStr = "";
		if (null == gw_type_Str)
		{
			gw_type_Str = "2";
		}
		String tab_name = "";
		if ("1".equals(gw_type_Str))
		{
			tab_name = "tab_hgwcustomer";
		}
		else
		{
			tab_name = "tab_egwcustomer";
		}
		filterStr += "&gw_type=" + gw_type_Str;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		long startTime = 0;
		long endTime = 0;
		// reportType:0 表示用complete_time 来查询；reportType:1
		// 表示用cpe_currentupdatetime 来查询；
		String reportType = request.getParameter("reportType");
		if (null == reportType)
			reportType = "0";
		filterStr += "&reportType=" + reportType;
		String start_day = request.getParameter("start_day");
		String end_day = request.getParameter("end_day");
		if (null == start_day || null == end_day)
		{
			DateTimeUtil dtu = new DateTimeUtil();
			dtu.getNextDate(-1);
			start_day = dtu.getDate();
			end_day = dtu.getDate();
		}
		filterStr += "&start_day=" + start_day;
		filterStr += "&end_day=" + end_day;
		startTime = new DateTimeUtil(start_day + " 00:00:00").getLongTime();
		endTime = new DateTimeUtil(end_day + " 23:59:59").getLongTime();
		String sqlDevice = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and b.area_id="
				+ areaid + " and a.gw_type= " + gw_type_Str + " ";
		if (curUser.getUser().isAdmin())
		{
			// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sqlDevice = "select a.*," + areaid + " as area_id"
					+ " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type_Str
					+ " ";
			// sqlDevice = "select a.*,area_id=" + areaid
			// + " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type_Str
			// + " ";
		}
		if ("1".equals(reportType))
		{
			if (startTime != 0 && !"".equals(startTime))
			{
				sqlDevice += " and a.cpe_currentupdatetime >= " + startTime;
			}
			if (endTime != 0 && !"".equals(endTime))
			{
				sqlDevice += " and a.cpe_currentupdatetime <=" + endTime;
			}
		}
		else
		{
			if (startTime != 0 && !"".equals(startTime))
			{
				sqlDevice += " and a.complete_time >= " + startTime;
			}
			if (endTime != 0 && !"".equals(endTime))
			{
				sqlDevice += " and a.complete_time <=" + endTime;
			}
		}
		// 设备列表呈现时，先按属地排序，再按厂商，设备类型排
		sqlDevice += " order by a.city_id, a.oui, a.device_model_id";
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(filterStr);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceInfoPrtByTime(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "2";
		}
		String tab_name = "";
		if ("1".equals(gw_type_Str))
		{
			tab_name = "tab_hgwcustomer";
		}
		else
		{
			tab_name = "tab_egwcustomer";
		}
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		long startTime = 0;
		long endTime = 0;
		// reportType:0 表示用complete_time 来查询；reportType:1
		// 表示用cpe_currentupdatetime 来查询；
		String reportType = request.getParameter("reportType");
		if (null == reportType)
			reportType = "0";
		String start_day = request.getParameter("start_day");
		String end_day = request.getParameter("end_day");
		if (null == start_day || null == end_day)
		{
			DateTimeUtil dtu = new DateTimeUtil();
			dtu.getNextDate(-1);
			start_day = dtu.getDate();
			end_day = dtu.getDate();
		}
		startTime = new DateTimeUtil(start_day + " 00:00:00").getLongTime();
		endTime = new DateTimeUtil(end_day + " 23:59:59").getLongTime();
		String sqlDevice = "select a.*,b.area_id from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and b.area_id="
				+ areaid + " and a.gw_type= " + gw_type_Str + " ";
		if (curUser.getUser().isAdmin())
		{
			// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangcong@ 2011-06-22
			sqlDevice = "select a.*," + areaid + " as area_id"
					+ " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type_Str
					+ " ";
			// sqlDevice = "select a.*,area_id=" + areaid
			// + " from tab_gw_device a where 1=1 and a.gw_type= " + gw_type_Str
			// + " ";
		}
		if ("1".equals(reportType))
		{
			if (startTime != 0 && !"".equals(startTime))
			{
				sqlDevice += " and a.cpe_currentupdatetime >= " + startTime;
			}
			if (endTime != 0 && !"".equals(endTime))
			{
				sqlDevice += " and a.cpe_currentupdatetime <=" + endTime;
			}
		}
		else
		{
			if (startTime != 0 && !"".equals(startTime))
			{
				sqlDevice += " and a.complete_time >= " + startTime;
			}
			if (endTime != 0 && !"".equals(endTime))
			{
				sqlDevice += " and a.complete_time <=" + endTime;
			}
		}
		// 设备列表呈现时，先按属地排序，再按厂商，设备类型排
		sqlDevice += " order by a.city_id, a.oui, a.device_model_id";
		String stroffset = request.getParameter("offset");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice);
		return cursor;
	}

	/**
	 * @param request
	 * @return
	 */
	public Cursor getStatDeviceInfoListPRT(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "0";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		// modify by lizhaojun 2007-12-03 NJLC_HG-BUG-ZHOUMF-20071031-002
		String linkParam = "&gw_type = " + gw_type;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		/**
		 * modify by qixueqi modify time 2010-6-22 15:08 because delete area
		 */
		/**
		 * String sqlDevice =
		 * "select a.*,b.area_id from tab_gw_device a left join gw_devicestatus c on a.device_id=c.device_id left join tab_gw_res_area b on a.device_id=b.res_id where b.res_type=1 and a.device_status != -1 and a.gw_type="
		 * + gw_type + " and b.area_id=" + areaid; if (curUser.getUser().isAdmin()) {
		 * sqlDevice = "select a.*,area_id=" + areaid +
		 * " from tab_gw_device a,gw_devicestatus c where a.device_id=c.device_id and a.device_status != -1 and a.gw_type="
		 * + gw_type + " "; }
		 */
		String sqlDevice = "";
		if (0 == gw_type)
		{
			sqlDevice = "select a.* from tab_gw_device a,gw_devicestatus c "
					+ " where a.device_id=c.device_id ";
		}
		else
		{
			if (Global.HBLT.equals(Global.instAreaShortName))
			{
				sqlDevice = "select a.*,b.gigabit_port from tab_gw_device a left join tab_device_version_attribute b on (a.devicetype_id = b.devicetype_id),gw_devicestatus c  "
						+ " where a.device_id=c.device_id and a.gw_type=" + gw_type;
			}
			else
			{
				sqlDevice = "select a.* from tab_gw_device a,gw_devicestatus c "
						+ " where a.device_id=c.device_id and a.gw_type=" + gw_type;
			}
		}
		// 查找设备条件g
		String str_device_id_ex = request.getParameter("device_id_ex");
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_loopback_ip = request.getParameter("loopback_ip");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String str_cpe_mc = request.getParameter("cpe_mc");
		String str_gather_id = request.getParameter("gather_id");
		String str_softwareversion = request.getParameter("softwareversion"); // 软件版本
		String str_status = request.getParameter("str_status"); // 在线状态
		/*
		 * add by wangsenbo 因为页面传的在线状态字段str_status，cpe_currentstatus都有，要统一，就要修改大量页面，容易漏掉
		 */
		String cpe_currentstatus = request.getParameter("cpe_currentstatus"); // 在线状态
		/**
		 * 宁夏电信需要新增用户是否绑定的统计
		 */
		String cpe_allocatedstatus = request.getParameter("cpe_allocatedstatus"); // 用户关联状态
		String str_area = request.getParameter("area");
		String str_deviceserial = request.getParameter("device_serial");
		String str_servicecode = request.getParameter("service_code");
		String str_device_status = request.getParameter("device_status");// 设备状态（删除、确认、未确认）
		String str_device_model_id = request.getParameter("device_model_id");
		String str_devType = request.getParameter("type_name");
		// String cpe_allocatedstatus =
		// request.getParameter("cpe_allocatedstatus");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String starttime1;
		String endtime1;
		if (null == str_city_id || "".equals(str_city_id) || "-1".equals(str_city_id))
		{
			str_city_id = curUser.getCityId();
		}
		// 时间转换
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
		logger.debug("2:" + str_device_id_ex + "#" + str_devicetype_id + "#"
				+ str_loopback_ip + "#" + str_vendor_id + "#" + str_city_id + "#"
				+ str_cpe_mc + "#" + str_gather_id + "#" + str_softwareversion + "#"
				+ str_status + "#" + str_area + "#" + str_deviceserial + "#"
				+ str_servicecode + "#" + str_devType + "#" + starttime + "#" + endtime);
		if (null != str_softwareversion && !"".equals(str_softwareversion)
				&& !"-1".equals(str_softwareversion))
		{
			logger.debug("str_softwareversion:" + str_softwareversion);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_devicetype_info where softwareversion='"
					+ str_softwareversion + "')";
			linkParam += "&softwareversion=" + str_softwareversion;
		}
		// 选择了在线状态
		if (null != cpe_allocatedstatus && !"".equals(cpe_allocatedstatus)
				&& !"-1".equals(cpe_allocatedstatus))
		{
			logger.debug("cpe_allocatedstatus:" + cpe_allocatedstatus);
			if ("1".equals(cpe_allocatedstatus))
			{
				sqlDevice += " and a.customer_id is not null";
			}
			else
			{
				sqlDevice += " and a.customer_id is null";
			}
			linkParam += "&cpe_allocatedstatus=" + cpe_allocatedstatus;
		}
		// 选择了是否绑定
		if (null != str_status && !"".equals(str_status) && !"-1".equals(str_status))
		{
			logger.debug("str_status:" + str_status);
			if ("2".equals(str_status))
			{
				sqlDevice += " and c.online_status is null";
			}
			else
			{
				sqlDevice += " and c.online_status =" + str_status;
			}
			linkParam += "&str_status=" + str_status;
		}
		if (null != cpe_currentstatus && !"".equals(cpe_currentstatus)
				&& !"-1".equals(cpe_currentstatus))
		{
			logger.debug("cpe_currentstatus:" + cpe_currentstatus);
			if ("2".equals(str_status))
			{
				sqlDevice += " and c.online_status is null";
			}
			else
			{
				String last_time = String.valueOf(nowTime - 12 * 3600);
				if ("1".equals(cpe_currentstatus))
				{
					sqlDevice += " and c.online_status=1 and c.last_time>";
					sqlDevice += last_time;
				}
				else if ("0".equals(cpe_currentstatus))
				{
					sqlDevice += " and (c.online_status=0 or (c.online_status=1 and c.last_time<";
					sqlDevice += last_time;
					sqlDevice += ")) ";
				}
				else
				{
					sqlDevice += " and c.online_status=null ";
				}
			}
			linkParam += "&cpe_currentstatus=" + cpe_currentstatus;
		}
		if (null != str_area && !"".equals(str_area) && !"-1".equals(str_area))
		{
			logger.debug("str_area:" + str_area);
			if (!"admin.com".equals(str_area))
			{
				sqlDevice += " and a.device_id in(select res_id from tab_gw_res_area f,tab_area g where f.area_id =g.area_id and g.area_name='"
						+ str_area + "' and f.res_type=1)";
			}
			linkParam += "&area=" + str_area;
		}
		if (null != str_deviceserial && !"".equals(str_deviceserial))
		{
			logger.debug("str_deviceserial:" + str_deviceserial);
			str_deviceserial = str_deviceserial.trim();
			if (str_deviceserial.length() > 5)
			{
				sqlDevice += " and a.dev_sub_sn ='"
						+ str_deviceserial.substring(str_deviceserial.length() - 6,
								str_deviceserial.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like '%" + str_deviceserial + "'";
			linkParam += "&device_serial=" + str_deviceserial;
		}
		if (null != str_servicecode && !"".equals(str_servicecode))
		{
			logger.debug("str_servicecode:" + str_servicecode);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_servicecode where servicecode='"
					+ str_servicecode + "')";
			linkParam += "&service_code=" + str_servicecode;
		}
		if (str_device_id_ex != null && !str_device_id_ex.equals(""))
		{
			sqlDevice += " and a.device_id_ex like '%" + str_device_id_ex + "%' ";
			linkParam += "&device_id_ex=" + str_device_id_ex;
		}
		if (str_loopback_ip != null && !str_loopback_ip.trim().equals(""))
		{
			sqlDevice += " and a.loopback_ip ='" + str_loopback_ip.trim() + "'";
			linkParam += "&loopback_ip=" + str_loopback_ip;
		}
		if (str_cpe_mc != null && !str_cpe_mc.trim().equals(""))
		{
			sqlDevice += " and a.cpe_mac ='" + str_cpe_mc.trim() + "'";
			linkParam += "&cpe_mc=" + str_cpe_mc;
		}
		if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals(""))
		{
			logger.debug("str_devicetype_id=" + str_devicetype_id);
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			linkParam += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals(""))
		{
			// sqlDevice += " and a.oui ='" + str_vendor_id + "' ";
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			linkParam += "&vendor_id=" + str_vendor_id;
		}
		if (str_device_model_id != null && !str_device_model_id.equals("-1")
				&& !str_device_model_id.equals(""))
		{
			sqlDevice += " and a.device_model_id ='" + str_device_model_id + "' ";
			linkParam += "&device_model_id=" + str_device_model_id;
		}
		if (!"00".equals(str_city_id))
		{
			List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
			sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
			linkParam += "&city_id=" + str_city_id;
		}
		if (str_gather_id != null && !str_gather_id.equals("")
				&& !str_gather_id.equals("-1"))
		{
			logger.debug("str_gather_id=" + str_gather_id);
			sqlDevice += " and a.gather_id ='" + str_gather_id + "' ";
			linkParam += "&gather_id=" + str_gather_id;
		}
		// 确认/未确认 add by chenjie 2011-6-23
		if (StringUtil.IsEmpty(str_device_status) == false)
		{
			if (!str_device_status.equals("-1"))
			{
				sqlDevice += " and a.device_status=" + str_device_status;
			}
			linkParam += "&device_status=" + str_device_status;
		}
		if (str_devType != null && !str_devType.trim().equals("")
				&& !str_devType.equals("-1"))
		{
			sqlDevice += " and a.device_type ='" + str_devType + "' ";
			linkParam += "&type_name=" + str_devType;
		}
		// if (cpe_allocatedstatus != null &&
		// !cpe_allocatedstatus.trim().equals("")) {
		// sqlDevice += " and a.cpe_allocatedstatus =" + cpe_allocatedstatus +
		// " ";
		// linkParam += "&cpe_allocatedstatus=" + cpe_allocatedstatus;
		// }
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sqlDevice += " and c.last_time >=" + starttime1 + " ";
			linkParam += "&starttime=" + starttime;
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sqlDevice += " and c.last_time <=" + endtime1 + " ";
			linkParam += "&endtime=" + endtime;
		}
		sqlDevice += " order by a.city_id,a.device_status";
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		/*
		 * String stroffset = request.getParameter("offset");
		 * 
		 * int pagelen = 50; int offset; if (stroffset == null) offset = 1; else offset =
		 * Integer.parseInt(stroffset); QueryPage qryp = new QueryPage();
		 * qryp.initPage(sqlDevice, offset, pagelen);
		 */
		// cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice);
		// NJLC_HG-BUG-ZHOUMF-20071031-002 modify by lizhaojun 2007-12-03
		/*
		 * logger.debug("linkParam :" + linkParam);
		 * 
		 * String strBar = qryp.getPageBar(linkParam);
		 * 
		 * list.add(strBar); list.add(cursor); return list;
		 */
		return cursor;
		// /////////////////////////////////////////////
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		/*
		 * String gw_type_Str = request.getParameter("gw_type"); if (null == gw_type_Str)
		 * { gw_type_Str = "1"; }
		 */
		/*
		 * int gw_type = Integer.parseInt(gw_type_Str);
		 * 
		 * HttpSession session = request.getSession(); UserRes curUser = (UserRes)
		 * session.getAttribute("curUser"); long areaid = curUser.getAreaId(); ArrayList
		 * list = new ArrayList(); list.clear(); String sqlDevice = "select a.,b.area_id
		 * from tab_gw_device a left join tab_gw_res_area b on a.device_id=b.res_id where
		 * b.res_type=1 and a.device_status != -1 and a.gw_typ=" + gw_type + " and
		 * b.area_id=" + areaid; if (curUser.getUser().isAdmin()) { sqlDevice = " select
		 * a.,area_id=" + areaid + " from tab_gw_device a where 1=1 and a.device_status !=
		 * -1 and gw_type=" + gw_type + " "; }
		 */
		// 查找设备条件
		/*
		 * String deviceStatus = request.getParameter("device_status"); String
		 * cpeCurrentstatus = request.getParameter("cpe_currentstatus");
		 * 
		 * if (deviceStatus != null && !deviceStatus.equals("")) { sqlDevice +=
		 * " and a.device_status=" + deviceStatus; } if (cpeCurrentstatus != null &&
		 * !cpeCurrentstatus.equals("")) { sqlDevice += " and a.cpe_currentstatus=" +
		 * cpeCurrentstatus; }
		 * 
		 * logger.debug("获取设备列表信息---1:" + sqlDevice);
		 * 
		 * return DataSetBean.getCursor(sqlDevice);
		 */
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoListByService(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangchy 2012-12-10
		// String sqlDevice = "select distinct a.*,area_id="
		// + areaid
		String sqlDevice = "select distinct a.*, "
				+ areaid
				+ " as area_id "
				+ " from tab_hgwcustomer b,tab_gw_device a"
				+ " where device_status = 1 and b.username is not null and a.device_id = b.device_id";
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
		String service_id = request.getParameter("service_id");
		if (city_id != null && !city_id.equals("") && !city_id.equals("-1"))
		{
			if ("00".equals(city_id))
			{
				sqlDevice += " and a.city_id ='" + city_id + "' and gw_type=" + gw_type
						+ " ";
			}
			else
			{
				sqlDevice += " and a.city_id in (select city_id from tab_city where city_id in("
						+ StringUtils.weave(listshow)
						+ ") or parent_id in("
						+ StringUtils.weave(listshow) + ")) and gw_type=" + gw_type + " ";
			}
		}
		if (service_id != null && !service_id.equals("") && !service_id.equals("-1"))
		{
			sqlDevice += " and b.serv_type_id =" + service_id + " ";
		}
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar("&gw_type=" + gw_type);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoListOutService(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		// 修改SQL，Oracle、Sysbase均支持字段默认值。 modify by zhangchy 2012-12-10
		// String sqlDevice = "select distinct a.*,area_id="
		// + areaid
		String sqlDevice = "select distinct a.*, "
				+ areaid
				+ " as area_id "
				+ " from tab_gw_device a"
				+ " where device_status = 1 and not exists (select b.device_serialnumber from tab_hgwcustomer b"
				+ " where a.device_id = b.device_id";
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
		String service_id = request.getParameter("service_id");
		if (service_id != null && !service_id.equals("") && !service_id.equals("-1"))
		{
			sqlDevice += " and b.serv_type_id =" + service_id + " ";
		}
		sqlDevice += ")";
		if (city_id != null && !city_id.equals("") && !city_id.equals("-1"))
		{
			if ("00".equals(city_id))
			{
				sqlDevice += " and a.city_id ='" + city_id + "' and gw_type=" + gw_type
						+ " ";
			}
			else
			{
				sqlDevice += " and a.city_id in (select city_id from tab_city where city_id in("
						+ StringUtils.weave(listshow)
						+ ") or parent_id in ("
						+ StringUtils.weave(listshow) + ")) and gw_type=" + gw_type + " ";
			}
		}
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar("&gw_type=" + gw_type);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 软件版本实时查询
	 * 
	 * @param request
	 * @return 返回MAP-->key:os value:version值
	 */
	public Map queryDeviceVersion(HttpServletRequest request)
	{
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		// 多个device_id，以","分割
		String device_ids = request.getParameter("device_id");
		String gather_id = "";
		String[] arrDevice_Id = null;
		if (device_ids != null)
			arrDevice_Id = device_ids.split(",");
		else
			arrDevice_Id = new String[0];
		if (arrDevice_Id.length > 0)
		{
			Map map = getDeviceInfo(arrDevice_Id[0]);
			// 得到采集点
		}
		String device_id = null;
		String oui = null;
		// String device_name = null;
		String device_serialnumber = null;
		String loopback_ip = null;
		String port = null;
		String path = null;
		String username = null;
		String passwd = null;
		int index = 0;
		cursor = getDevCursorByIDs(arrDevice_Id);
		DevRpc[] devRPCArr = new DevRpc[cursor.getRecordSize()];
		fields = cursor.getNext();
		HashMap<String, String> deviceMap = new HashMap<String, String>();
		while (fields != null)
		{
			device_id = (String) fields.get("device_id");
			oui = (String) fields.get("oui");
			// device_name = (String)fields.get("device_name");
			device_serialnumber = (String) fields.get("device_serialnumber");
			deviceMap.put(device_id, oui + "-" + device_serialnumber);
			GetParameterValues getParameterValues = new GetParameterValues();
			String[] parameterNamesArr = new String[1];
			parameterNamesArr[0] = "InternetGatewayDevice.DeviceInfo.SoftwareVersion";
			getParameterValues.setParameterNames(parameterNamesArr);
			devRPCArr[index] = new DevRpc();
			devRPCArr[index].devId = device_id;
			devRPCArr[index].rpcArr = new Rpc[1];
			devRPCArr[index].rpcArr[0] = new Rpc();
			devRPCArr[index].rpcArr[0].rpcId = "1";
			devRPCArr[index].rpcArr[0].rpcName = "GetParameterValues";
			devRPCArr[index].rpcArr[0].rpcValue = getParameterValues.toRPC();
			fields = cursor.getNext();
			index++;
		}
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type_Str);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcCmd_Type);
		// 存放device_name--->version 版本
		Map mapDevVersion = null;
		if (devRPCRep != null)
		{
			String rpcResp = null;
			String value = null;
			String os = null;// oui+serial_numer
			mapDevVersion = new HashMap();
			ParameterValueStruct[] pvsArr = null;
			GetParameterValuesResponse getParameterValuesResponse = null;
			// 遍历获取到的devRPCRep
			for (int i = 0, length = devRPCRep.size(); i < length; i++)
			{
				rpcResp = devRPCRep.get(i).getRpcList().get(0).getValue();
				getParameterValuesResponse = new GetParameterValuesResponse();
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(rpcResp));
				if (soapOBJ == null)
				{
					return mapDevVersion;
				}
				getParameterValuesResponse = XmlToRpc.GetParameterValuesResponse(soapOBJ
						.getRpcElement());
				if (getParameterValuesResponse != null)
				{
					pvsArr = getParameterValuesResponse.getParameterList();
					// 获取的版本数据
					value = String.valueOf(pvsArr[0].getValue().para_value);
				}
				else
				{
					value = "获取失败,请检查设备是否在线";
				}
				os = deviceMap.get(devRPCRep.get(i).getDevId());
				mapDevVersion.put(os, value);
			}
			getParameterValuesResponse = null;
			rpcResp = null;
			value = null;
			pvsArr = null;
			devRPCRep = null;
		}
		// clear
		devRPCArr = null;
		return mapDevVersion;
	}

	/**
	 * 根据device_id数组获取cursor结果集
	 * 
	 * @param arrDevId
	 *            设备数组id
	 * @return 返回查询结果集
	 */
	public Cursor getDevCursorByIDs(String[] arrDevId)
	{
		// 如果数组id为null或者长度为零，则返回空数据集
		if (arrDevId == null || arrDevId.length == 0)
			return new Cursor();
		pSQL.setSQL(m_DeviceByIds);
		List list = Arrays.asList(arrDevId);
		pSQL.setStringExt(1, StringUtils.weave(list), false);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		list = null;
		return cursor;
	}

	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	public Map getAreaIdMapName()
	{
		String sql = "select area_id,area_name from tab_area";
		Map result = new HashMap();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		fields = cursor.getNext();
		while (fields != null)
		{
			result.put(fields.get("area_id"), fields.get("area_name"));
			fields = cursor.getNext();
		}
		return result;
	}

	/**
	 * 实现设备资料的增、删、改操作
	 * 
	 * @param request
	 * @return 返回结果javascript代码,便于jsp页面弹出相应信息给用户.
	 */
	public String getDeviceActMsg(HttpServletRequest request)
	{
		String gw_type = request.getParameter("gw_type");
		String actMeg = "";
		String action = request.getParameter("_action");
		ArrayList listSQL = new ArrayList();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long m_AreaId = user.getAreaId();
		// List devList = curUser.getUserDevRes(user);
		Date currentDate = null;
		String[] arr_Device_id = request.getParameterValues("device_id");
		// 修改设备状态
		if ("status".equals(action))
		{
			String[] webtopoDev = new String[arr_Device_id.length];
			String batchSQL = "";
			// 家庭网关只更新设备状态,返回true
			if (Global.GW_TYPE_ITMS.equals(gw_type))
			{
				for (int i = 0; i < arr_Device_id.length; i++)
				{
					batchSQL += "update tab_gw_device set device_status=1 ,city_id='"
							+ user.getCityId() + "' where device_id='" + arr_Device_id[i]
							+ "';";
					batchSQL += "delete from tab_gw_res_area where res_id='"
							+ arr_Device_id[i] + "'" + " and res_type=1;";
					// 调用EGWUserInfoAct中insertAreaTable方法默认前面会有分号（;）
					batchSQL += EGWUserInfoAct.insertAreaTable(arr_Device_id[i],
							String.valueOf(m_AreaId));
				}
				PrepareSQL psql = new PrepareSQL(batchSQL);
				psql.getSQL();
				int iCode[] = DataSetBean.doBatch(batchSQL);
				if (iCode == null || iCode.length <= 0)
				{
					return "设备确认更新(数据库)失败！";
				}
				return "设备确认更新成功！";
			}
			else
			{ // 企业网关:更新设备状态,添加域表,通知MC
				for (int i = 0; i < arr_Device_id.length; i++)
				{
					batchSQL += "update tab_gw_device set device_status=1,city_id='"
							+ user.getCityId() + "' where device_id='" + arr_Device_id[i]
							+ "';";
					batchSQL += "delete from tab_gw_res_area where res_id='"
							+ arr_Device_id[i] + "'" + " and res_type=1";
					batchSQL += EGWUserInfoAct.insertAreaTable(arr_Device_id[i],
							String.valueOf(m_AreaId));
					webtopoDev[i] = "1/gw/" + arr_Device_id[i];
				}
				PrepareSQL psql = new PrepareSQL(batchSQL);
				psql.getSQL();
				int iCode[] = DataSetBean.doBatch(batchSQL);
				if (iCode == null || iCode.length <= 0)
				{
					return "设备确认更新(数据库)失败！";
				}
				return "设备确认更新成功 ！";
				// return setDeviceStatus(webtopoDev, user.getAccount(),
				// user.getPasswd());
			}
		}
		// 删除设备 批量删除
		else if ("delete".equals(action))
		{
			logger.debug("Begin to Delete.");
			return delDevice(request, arr_Device_id);
		}
		else
		{// 添加和修改设备
			currentDate = new Date();
			// 设备最近更新时间 获取当前时间
			long l_curupdatetime = currentDate.getTime() / 1000;
			currentDate = null;
			// 获取Form表单数据
			String str_device_id = request.getParameter("device_id");
			String str_device_id_ex = request.getParameter("device_id_ex");
			String str_loopback_ip = request.getParameter("loopback_ip");
			// String str_oui = request.getParameter("vendor_id");
			String str_device_serialnumber = request.getParameter("device_serialnumber");
			String str_devicetype_id = request.getParameter("devicetype_id");
			String str_manage_staff = request.getParameter("per_acc_oid");
			String str_city_id = request.getParameter("city_id");
			String str_office_id = request.getParameter("office_id");
			str_office_id = str_office_id == null ? "" : str_office_id;
			String str_zone_id = request.getParameter("zone_id");
			str_zone_id = str_zone_id == null ? "" : str_zone_id;
			String str_device_addr = request.getParameter("device_addr");
			String str_complete_time = request.getParameter("hidden_complete_time");
			String str_buy_time = request.getParameter("hidden_buy_time");
			String str_service_year = request.getParameter("service_year");
			String str_staff_id = request.getParameter("staff_id");
			String str_remark = request.getParameter("remark");
			String str_gather_id = request.getParameter("gather_id");
			String str_cpe_mac = request.getParameter("cpe_mac");
			String str_res_pro_id = request.getParameter("res_pro_id");
			String device_type = request.getParameter("device_type");
			String str_device_name = request.getParameter("device_name");
			// // 设备名称为空，则device_name=Oui+ device_serialnumber.
			// if (str_device_name == null || str_device_name.trim().equals(""))
			// {
			// str_device_name = str_oui + "-" + str_device_serialnumber;
			// }
			// 最大包数Envelopes
			String str_maxenvelopes = request.getParameter("maxenvelopes");
			String str_retrycount = request.getParameter("retrycount");
			String str_port = request.getParameter("port");
			String str_path = request.getParameter("path");
			String str_acs_username = request.getParameter("acs_username");
			String str_acs_passwd = request.getParameter("acs_passwd");
			String str_cpe_username = request.getParameter("cpe_username");
			String str_cpe_passwd = request.getParameter("cpe_passwd");
			String content = "";
			String result = "";
			String e_id = request.getParameter("e_id");
			String device_url = request.getParameter("device_url");
			/**
			 * SNMP认证设备表新增的几个字段 安全级别1:都不必填 安全级别2:加解密协议,私钥 必填 安全级别3:都必填
			 */
			// 协议版本(必填)
			String snmp_version = request.getParameter("snmp_version");
			// 鉴权用户
			String security_username = request.getParameter("security_username");
			// SNMP引擎
			String engine_id = request.getParameter("engine_id");
			// ContextName
			String context_name = request.getParameter("context_name");
			// 安全级别
			String security_level = request.getParameter("security_level");
			// 鉴权协议
			String auth_protocol = request.getParameter("auth_protocol");
			// 鉴权密钥
			String auth_passwd = request.getParameter("auth_passwd");
			// 加解密协议
			String privacy_protocol = request.getParameter("privacy_protocol");
			// 私钥
			String privacy_passwd = request.getParameter("privacy_passwd");
			// Snmp端口(必填)
			String snmp_udp = request.getParameter("snmp_udp");
			if (null == snmp_udp || "".equals(snmp_udp) || "null".equals(snmp_udp))
			{
				snmp_udp = "0";
			}
			// SNMP读口令
			String snmp_r_passwd = request.getParameter("snmp_r_passwd");
			// SNMP写口令
			String snmp_w_passwd = request.getParameter("snmp_w_passwd");
			/**
			 * 写死
			 */
			// 是否启用
			String is_enable = "1";
			// 安全模型
			String security_model = "3";
			/**
			 * SNMP认证设备表新增字段到此结束
			 */
			// String gw_type = request.getParameter("gw_type");
			String midwareInterId = "";
			// String str_devicetype = "";
			// 设备型号id
			String device_model_id = request.getParameter("device_model_id");
			// String device_model_id = getModelIdByType(str_devicetype_id);
			if ("" != str_device_serialnumber && null != str_device_serialnumber)
			{
				str_device_serialnumber = str_device_serialnumber.trim();
			}
			try
			{
				// HashMap devtypemap = getDeviceTypeInfoByID(request);
				// str_devicetype = (String) devtypemap.get("device_model");
			}
			catch (Exception e)
			{
			}
			// 根据所增加用户的属地，取得相应的area_id，而不是根据当前登录的用户 add by gongsj
			String getAreaIdsql = "select area_id from tab_city_area where city_id='"
					+ str_city_id + "'";
			PrepareSQL psql1 = new PrepareSQL(getAreaIdsql);
			psql1.getSQL();
			Map areaIdMap = DataSetBean.getRecord(getAreaIdsql);
			if (null != areaIdMap)
			{
				m_AreaId = Long.parseLong((String) areaIdMap.get("area_id"));
			}
			if ("add".equals(action))
			{
				logger.debug("Begin ADD Device.");
				midwareInterId = "D1";
				try
				{
					content = Encoder.toISO("新增设备" + str_device_name + "，设备ip为"
							+ str_loopback_ip);
				}
				catch (Exception e)
				{
					content = "";
				}
				WebCorbaInst corba = new WebCorbaInst("db");
				String m_DeviceSerial = corba.getDeviceSerial(user);
				if (null == m_DeviceSerial || m_DeviceSerial.toLowerCase().equals("null"))
				{
					m_DeviceSerial = "1";
					return actMeg = "<script language=\"javascript\">alert('获取设备id失败,请重试!');</script>";
				}
				str_device_id = m_DeviceSerial;
				pSQL.setSQL(m_DeviceAdd_SQL);
				pSQL.setString(1, str_device_id);// 暂时使用设备外部id作为device_id
				pSQL.setString(2, str_device_id_ex);
				pSQL.setString(3, str_loopback_ip);
				pSQL.setString(4, str_device_serialnumber);
				pSQL.setStringExt(5, str_devicetype_id, false);
				pSQL.setString(6, str_manage_staff);
				pSQL.setString(7, str_city_id);
				pSQL.setString(8, str_office_id);
				pSQL.setString(9, str_zone_id);
				pSQL.setString(10, str_device_addr);
				pSQL.setStringExt(11, str_complete_time, false);
				pSQL.setStringExt(12, str_buy_time, false);
				pSQL.setInt(13, Integer.parseInt(str_service_year));
				pSQL.setString(14, str_staff_id);
				pSQL.setString(15, str_remark);
				pSQL.setString(16, str_gather_id);
				pSQL.setInt(17, 1);
				pSQL.setString(18, str_cpe_mac);
				pSQL.setString(19, str_res_pro_id);
				pSQL.setString(20, str_device_name);
				pSQL.setStringExt(21, str_maxenvelopes, false);
				pSQL.setStringExt(22, str_retrycount, false);
				pSQL.setStringExt(23, str_port, false);
				pSQL.setString(24, str_path);
				pSQL.setLong(25, l_curupdatetime);
				pSQL.setString(26, str_acs_username);
				pSQL.setString(27, str_acs_passwd);
				pSQL.setString(28, str_cpe_username);
				pSQL.setString(29, str_cpe_passwd);
				pSQL.setInt(30, Integer.parseInt(gw_type));
				pSQL.setString(31, device_model_id);
				// SNMP 端口
				pSQL.setInt(32, Integer.parseInt(snmp_udp));
				pSQL.setString(33, device_type);
				pSQL.setString(34, e_id);
				pSQL.setString(35, device_url);
				String strSQL = pSQL.getSQL();
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				logger.debug(strSQL);
				listSQL.add(strSQL);
				/** **********新增SNMP设备认证表相关************ */
				strSQL = "insert into sgw_security(device_id,snmp_version,is_enable,security_username,security_model,engine_id,"
						+ "context_name,security_level,auth_protocol,auth_passwd,privacy_protocol,"
						+ "privacy_passwd,snmp_r_passwd,snmp_w_passwd) values('"
						+ str_device_id
						+ "','"
						+ snmp_version
						+ "',"
						+ is_enable
						+ ",'"
						+ security_username
						+ "',"
						+ security_model
						+ ",'"
						+ engine_id
						+ "','"
						+ context_name
						+ "',"
						+ security_level
						+ ",'"
						+ auth_protocol
						+ "','"
						+ auth_passwd
						+ "','"
						+ privacy_protocol
						+ "','"
						+ privacy_passwd
						+ "','"
						+ snmp_r_passwd
						+ "','"
						+ snmp_w_passwd + "')";
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				PrepareSQL psql2 = new PrepareSQL(strSQL);
				psql2.getSQL();
				listSQL.add(strSQL);
				/****************************/
				// 需要为此用户区域注入此设备权限，也需要给此用户所属区域上层属地注入此设备权限
				AreaManager Manager = new AreaManageSyb();
				// 获取到用户自身所属区域
				ArrayList m_AreaList = Manager.getUpperToTopAreaIds((Integer
						.parseInt(String.valueOf(m_AreaId))));
				// ArrayList m_AreaList = new ArrayList();
				// m_AreaList.add(String.valueOf(m_AreaId));
				// 获得tab_gw_res_area最大ID下一个ID
				// long m_MaxId = DataSetBean.getMaxId("tab_gw_res_area", "id");
				for (int k = 0; k < m_AreaList.size(); k++)
				{
					strSQL = "insert into tab_gw_res_area(res_type,res_id,area_id) values("
							// + m_MaxId
							+ "1,'" + str_device_id + "'," + m_AreaList.get(k) + ")";
					PrepareSQL psql3 = new PrepareSQL(strSQL);
					psql3.getSQL();
					// m_MaxId++;
					listSQL.add(strSQL);
				}
				m_AreaList.clear();
				m_AreaList = null;
			}
			else if ("update".equals(action))
			{
				logger.debug("Update Begin...");
				midwareInterId = "D2";
				try
				{
					content = Encoder.toISO(getDevLogInfo(request));
				}
				catch (Exception e)
				{
					content = "";
				}
				String m_DeviceInfoUpdate_SQL = "update tab_gw_device set device_id_ex=?,loopback_ip=?,"
						+ " manage_staff=?,city_id=?,office_id=?,zone_id=?,device_addr=?,"
						+ " buy_time=?,service_year=?,staff_id=?,remark=?,"
						+ " device_status=?,cpe_mac=?,res_pro_id=?,device_name=?,maxenvelopes=?,retrycount=?,";
				if (device_type != null && !"".equals(device_type))
				{
					m_DeviceInfoUpdate_SQL += "device_type=?,";
				}
				m_DeviceInfoUpdate_SQL += "device_url=? where device_id=?";
				// 修改操作
				pSQL.setSQL(m_DeviceInfoUpdate_SQL);
				pSQL.setString(1, str_device_id_ex);
				pSQL.setString(2, str_loopback_ip);
				// pSQL.setString(3, str_device_serialnumber);
				// pSQL.setStringExt(4, str_devicetype_id, false);
				pSQL.setString(3, str_manage_staff);
				pSQL.setString(4, str_city_id);
				pSQL.setString(5, str_office_id);
				pSQL.setString(6, str_zone_id);
				pSQL.setString(7, str_device_addr);
				// pSQL.setStringExt(10, str_complete_time, false);
				pSQL.setStringExt(8, str_buy_time, false);
				pSQL.setInt(9, Integer.parseInt(str_service_year));
				pSQL.setString(10, str_staff_id);
				pSQL.setString(11, str_remark);
				// pSQL.setString(15, str_gather_id);
				pSQL.setInt(12, 1);
				pSQL.setString(13, str_cpe_mac);
				pSQL.setString(14, str_res_pro_id);
				pSQL.setString(15, str_device_name);
				pSQL.setStringExt(16, str_maxenvelopes, false);
				pSQL.setStringExt(17, str_retrycount, false);
				// pSQL.setStringExt(18, str_port, false);
				// pSQL.setString(19, str_path);
				// pSQL.setLong(24, l_curupdatetime);
				// pSQL.setString(25, str_acs_username);
				// pSQL.setString(26, str_acs_passwd);
				// pSQL.setString(27, str_cpe_username);
				// pSQL.setString(28, str_cpe_passwd);
				// pSQL.setString(29, device_model_id);
				// SNMP 端口
				// pSQL.setInt(20, Integer.parseInt(snmp_udp));
				// 如果if(device_type != null &&
				// !"".equals(device_type))这个条件不满足的话，
				// 下面的pSQL.setString(19, device_url);pSQL.setString(20,
				// str_device_id);与update语句中的"?"不匹配
				// -- 注释 by zhangchy
				// 2011-09-01-------beging----具体逻辑在下面的add部分重新改写------
				// if(device_type != null && !"".equals(device_type))
				// {
				// pSQL.setString(18, device_type);
				// }
				// //pSQL.setString(22, e_id);
				// pSQL.setString(19, device_url);
				// pSQL.setString(20, str_device_id);
				// -- 注释 by zhangchy 2011-09-01-------beging----------
				// -----add by zhangchy 2011-09-01 -----begin -----------
				if (device_type != null && !"".equals(device_type))
				{
					pSQL.setString(18, device_type);
					pSQL.setString(19, device_url);
					pSQL.setString(20, str_device_id);
				}
				else
				{
					pSQL.setString(18, device_url);
					pSQL.setString(19, str_device_id);
				}
				// -----add by zhangchy 2011-09-01 -----end-----------
				listSQL.add(pSQL.getSQL());
				/** **********新增SNMP设备认证表相关************ */
				String strSQL = "";
				String isExistSQL = "select device_id from sgw_security where device_id='"
						+ str_device_id + "'";
				PrepareSQL psql4 = new PrepareSQL(isExistSQL);
				psql4.getSQL();
				Map isExistMap = DataSetBean.getRecord(isExistSQL);
				if (null == isExistMap)
				{
					// 记录不存在,需要insert
					strSQL = "insert into sgw_security(device_id,snmp_version,is_enable,security_username,security_model,engine_id,"
							+ "context_name,security_level,auth_protocol,auth_passwd,privacy_protocol,"
							+ "privacy_passwd,snmp_r_passwd,snmp_w_passwd) values('"
							+ str_device_id
							+ "','"
							+ snmp_version
							+ "',"
							+ is_enable
							+ ",'"
							+ security_username
							+ "',"
							+ security_model
							+ ",'"
							+ engine_id
							+ "','"
							+ context_name
							+ "',"
							+ security_level
							+ ",'"
							+ auth_protocol
							+ "','"
							+ auth_passwd
							+ "','"
							+ privacy_protocol
							+ "','"
							+ privacy_passwd
							+ "','"
							+ snmp_r_passwd + "','" + snmp_w_passwd + "')";
				}
				else
				{
					// 记录存在,直接update
					strSQL = "update sgw_security set snmp_version='" + snmp_version
							+ "',is_enable=" + is_enable + ",security_username='"
							+ security_username + "',security_model=" + security_model
							+ ",engine_id='" + engine_id + "',context_name='"
							+ context_name + "',security_level=" + security_level
							+ ",auth_protocol='" + auth_protocol + "',auth_passwd='"
							+ auth_passwd + "',privacy_protocol='" + privacy_protocol
							+ "',privacy_passwd='" + privacy_passwd + "',snmp_r_passwd='"
							+ snmp_r_passwd + "',snmp_w_passwd='" + snmp_w_passwd
							+ "' where device_id='" + str_device_id + "'";
				}
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				PrepareSQL psql5 = new PrepareSQL(strSQL);
				psql5.getSQL();
				listSQL.add(strSQL);
				/****************************/
			}
			// 将上面sql执行
			logger.debug("List SQL.size():" + listSQL.size());
			int iCode[] = DataSetBean.doBatch(listSQL);
			logger.debug("iCode[]:" + iCode[0]);
			if (iCode != null && iCode.length > 0)
			{
				// if (devList.size() > 0)
				// {
				// if ("add".equals(action))
				// {
				// devList.add(str_device_id);
				// }
				// }
				try
				{
					result = Encoder.toISO("成功");
				}
				catch (Exception e)
				{
					result = "";
				}
				if ("update".equals(action))
				{
					actMeg = "<script language=\"javascript\">alert('修改成功!');parent.goList();</script>";
				}
				else if (action.equals("add"))
				{
					actMeg = "<script language=\"javascript\">alert('添加成功!');parent.resetForm();</script>";
				}
				// 通知中间件
				try
				{
					if (LipossGlobals.getMidWare())
					{
						if ("D1".equals(midwareInterId))
						{
							// 设备添加时暂不把设备添加到中间件平台，以后修改
						}
						else
						{
							// 当已经把设备添加到中间件平台时，修改设备才需要更新中间件平台
							String getMidwaresql = " select device_id,city_id,oui,device_serialnumber,device_model,device_ad from gw_device_midware where device_id='"
									+ str_device_id + "'";
							PrepareSQL psql6 = new PrepareSQL(getMidwaresql);
							psql6.getSQL();
							Map MidwareMap = DataSetBean.getRecord(getMidwaresql);
							if (null != MidwareMap)
							{
								String midwareCity = (String) MidwareMap.get("city_id");
								String midwareOui = (String) MidwareMap.get("oui");
								String midwareDeviceSn = (String) MidwareMap
										.get("device_serialnumber");
								String midwaremodel = (String) MidwareMap
										.get("device_model");
								String StringDeviceAd = (String) MidwareMap
										.get("device_ad");
								// 更新中间件设备信息
								int temp_ = updateMidwareDevice(str_device_id,
										midwareOui, midwareDeviceSn, midwaremodel,
										StringDeviceAd, "1", "", "", "");
								if (0 == temp_)
								{
									// 删除中间件老域
									temp_ = deleteMidwareDeviceCity(str_device_id,
											midwareOui, midwareDeviceSn, midwareCity);
									if (0 == temp_)
									{
										// 添加中间件新域
										temp_ = addMidwareDeviceCity(str_device_id,
												midwareOui, midwareDeviceSn, str_city_id);
										if (0 == temp_)
										{
											// 更新中间件表——gw_device_midware
											String updateMidwaresql = "update gw_device_midware set city_id='"
													+ str_city_id
													+ "' where device_id='"
													+ str_device_id + "'";
											PrepareSQL psql7 = new PrepareSQL(
													updateMidwaresql);
											psql7.getSQL();
											DataSetBean.executeUpdate(updateMidwaresql);
										}
									}
								}
							}
						}
					}
				}
				catch (Exception exx)
				{
					exx.printStackTrace();
					try
					{
						result = Encoder.toISO("通知中间件失败");
					}
					catch (Exception e)
					{
						result = "Inform MidWare Failure";
					}
				}
			}
			else
			{
				try
				{
					result = Encoder.toISO("失败");
				}
				catch (Exception e)
				{
					result = "";
				}
				actMeg = "<script language=\"javascript\">alert('操作失败,请重试!');</script>";
			}
			// 记日志
			LogItem.getInstance()
					.writeItemLog(request, 1, str_device_id, content, result);
		}
		action = null;
		listSQL.clear();
		listSQL = null;
		return actMeg;
	}

	/**
	 * 中间件增加设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public int addMidwareDevice(String deviceId, String oui, String deviceSn,
			String deviceModel, String adNumber, String status, String area,
			String group, String phone)
	{
		int result = 0;
		// 1. 0 设备添加成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10002 设备已经存在
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject devObject = new MidWare.DeviceObject();
				devObject.device_id = deviceId;
				devObject.oui = oui;
				devObject.device_serialnumber = deviceSn;
				// 其他参数
				MidWare.Param[] paramArr = new MidWare.Param[6];
				paramArr[0] = new MidWare.Param();
				paramArr[0].param_name = "TYPE";
				paramArr[0].param_type = "String";
				paramArr[0].param_value = deviceModel;
				paramArr[1] = new MidWare.Param();
				paramArr[1].param_name = "AREA";
				paramArr[1].param_type = "String";
				paramArr[1].param_value = area == null ? "" : area;
				paramArr[2] = new MidWare.Param();
				paramArr[2].param_name = "GROUP";
				paramArr[2].param_type = "String";
				paramArr[2].param_value = group == null ? "" : group;
				paramArr[3] = new MidWare.Param();
				paramArr[3].param_name = "PHONE";
				paramArr[3].param_type = "String";
				paramArr[3].param_value = phone == null ? "" : phone;
				paramArr[4] = new MidWare.Param();
				paramArr[4].param_name = "STATUS";
				paramArr[4].param_type = "String";
				paramArr[4].param_value = status;
				paramArr[5] = new MidWare.Param();
				paramArr[5].param_name = "AD";
				paramArr[5].param_type = "String";
				paramArr[5].param_value = adNumber;
				MidWare.DeviceListObject[] devListObjectArr = new MidWare.DeviceListObject[1];
				devListObjectArr[0] = new MidWare.DeviceListObject();
				devListObjectArr[0].deviceObject = new MidWare.DeviceObject();
				devListObjectArr[0].deviceObject = devObject;
				devListObjectArr[0].paramArr = new MidWare.Param[6];
				devListObjectArr[0].paramArr = paramArr;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjectRepArr = null;
				logger.debug("－－－－－－－－调用中间件接口！－－－－－－－－－－");
				devObjectRepArr = midCorba.addDevice(devListObjectArr);
				if (devObjectRepArr != null && devObjectRepArr.length > 0)
				{
					logger.debug("－－－－－－－－中间件接口调用成功！－－－－－－－－－－");
					result = devObjectRepArr[0].result_code;
				}
				else
				{
					logger.debug("－－－－－－－－中间件接口调用失败！－－－－－－－－－－");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * 中间件更新设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public int updateMidwareDevice(String deviceId, String oui, String deviceSn,
			String deviceModel, String adNumber, String status, String area,
			String group, String phone)
	{
		int result = 0;
		// 1. 0 设备修改成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject devObject = new MidWare.DeviceObject();
				devObject.device_id = deviceId;
				devObject.oui = oui;
				devObject.device_serialnumber = deviceSn;
				// 其他参数
				MidWare.Param[] paramArr = new MidWare.Param[6];
				paramArr[0] = new MidWare.Param();
				paramArr[0].param_name = "TYPE";
				paramArr[0].param_type = "String";
				paramArr[0].param_value = deviceModel;
				paramArr[1] = new MidWare.Param();
				paramArr[1].param_name = "AREA";
				paramArr[1].param_type = "String";
				paramArr[1].param_value = area == null ? "" : area;
				paramArr[2] = new MidWare.Param();
				paramArr[2].param_name = "GROUP";
				paramArr[2].param_type = "String";
				paramArr[2].param_value = group == null ? "" : group;
				paramArr[3] = new MidWare.Param();
				paramArr[3].param_name = "PHONE";
				paramArr[3].param_type = "String";
				paramArr[3].param_value = phone == null ? "" : phone;
				paramArr[4] = new MidWare.Param();
				paramArr[4].param_name = "STATUS";
				paramArr[4].param_type = "String";
				paramArr[4].param_value = status;
				paramArr[5] = new MidWare.Param();
				paramArr[5].param_name = "AD";
				paramArr[5].param_type = "String";
				paramArr[5].param_value = adNumber;
				MidWare.DeviceListObject[] devListObjectArr = new MidWare.DeviceListObject[1];
				devListObjectArr[0] = new MidWare.DeviceListObject();
				devListObjectArr[0].deviceObject = new MidWare.DeviceObject();
				devListObjectArr[0].deviceObject = devObject;
				devListObjectArr[0].paramArr = new MidWare.Param[6];
				devListObjectArr[0].paramArr = paramArr;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjectRepArr = null;
				devObjectRepArr = midCorba.updateDevice(devListObjectArr);
				if (devObjectRepArr != null && devObjectRepArr.length > 0)
				{
					logger.debug("－－－－－－－－中间件接口调用成功！－－－－－－－－－－");
					result = devObjectRepArr[0].result_code;
				}
				else
				{
					logger.debug("－－－－－－－－中间件接口调用失败！－－－－－－－－－－");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * 中间件删除设备
	 * 
	 * @author wangsenbo
	 * @date Apr 7, 2010
	 * @param
	 * @return String
	 */
	public int deleteMidwareDevice(String deviceId, String oui, String deviceSn)
	{
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DeviceObject[] devObjectArr = new MidWare.DeviceObject[1];
				MidWare.DeviceObject devObj = new MidWare.DeviceObject();
				devObj.device_id = deviceId;
				devObj.oui = oui;
				devObj.device_serialnumber = deviceSn;
				devObjectArr[0] = new MidWare.DeviceObject();
				devObjectArr[0] = devObj;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DeviceObjectRep[] devObjArr = midCorba.delDevice(devObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * 中间件设备添加到域
	 * 
	 * @author qixueqi
	 * @date 5 5, 2010
	 * @param
	 * @return String
	 */
	public int addMidwareDeviceCity(String deviceId, String oui, String deviceSn,
			String cityId)
	{
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DevAreaObject[] devAreaObjectArr = new MidWare.DevAreaObject[1];
				devAreaObjectArr[0] = new MidWare.DevAreaObject();
				devAreaObjectArr[0].area_id = cityId;
				devAreaObjectArr[0].deviceObjectArr = new MidWare.DeviceObject[1];
				devAreaObjectArr[0].deviceObjectArr[0] = new MidWare.DeviceObject();
				devAreaObjectArr[0].deviceObjectArr[0].device_id = deviceId;
				devAreaObjectArr[0].deviceObjectArr[0].oui = oui;
				devAreaObjectArr[0].deviceObjectArr[0].device_serialnumber = deviceSn;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DevAreaObjectRep[] devObjArr = midCorba
						.addAreaDev(devAreaObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * 中间件设备添加到域
	 * 
	 * @author qixueqi
	 * @date 5 5, 2010
	 * @param
	 * @return String
	 */
	public int deleteMidwareDeviceCity(String deviceId, String oui, String deviceSn,
			String cityId)
	{
		int result = 0;
		// 1. 0 设备删除成功
		// 2. 1 服务器连接失败
		// 3. 10001 数据格式错误
		// 4. 10003 设备不存在
		// 通知中间件:
		try
		{
			if (LipossGlobals.getMidWare())
			{
				MidWare.DevAreaObject[] devAreaObjectArr = new MidWare.DevAreaObject[1];
				devAreaObjectArr[0] = new MidWare.DevAreaObject();
				devAreaObjectArr[0].area_id = cityId;
				devAreaObjectArr[0].deviceObjectArr = new MidWare.DeviceObject[1];
				devAreaObjectArr[0].deviceObjectArr[0] = new MidWare.DeviceObject();
				devAreaObjectArr[0].deviceObjectArr[0].device_id = deviceId;
				devAreaObjectArr[0].deviceObjectArr[0].oui = oui;
				devAreaObjectArr[0].deviceObjectArr[0].device_serialnumber = deviceSn;
				MidWareCorba midCorba = new MidWareCorba();
				MidWare.DevAreaObjectRep[] devObjArr = midCorba
						.delAreaDev(devAreaObjectArr);
				if (devObjArr != null && devObjArr.length > 0)
				{
					logger.debug("删除设备调用中间件接口成功！");
					result = devObjArr[0].result_code;
				}
				else
				{
					logger.debug("删除设备调用中间件接口失败！");
					result = 1;
				}
			}
		}
		catch (Exception exx)
		{
			exx.printStackTrace();
			logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
			result = 1;
		}
		return result;
	}

	/**
	 * 实现SNMP设备资料的增、删、改操作
	 * 
	 * @param request
	 * @return 返回结果javascript代码,便于jsp页面弹出相应信息给用户.
	 */
	public String getSnmpDeviceActMsg(HttpServletRequest request)
	{
		String actMeg = "";
		String strSQL = "";
		String action = request.getParameter("action");
		ArrayList listSQL = new ArrayList();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long m_AreaId = user.getAreaId();
		List devList = curUser.getUserDevRes(user);
		Date currentDate = null;
		String[] arr_Device_id = request.getParameterValues("device_id");
		// 修改设备状态
		if ("status".equals(action))
		{
			// String[] webtopoDev = new String[arr_Device_id.length];
			// for (int i = 0; i < arr_Device_id.length; i++) {
			// webtopoDev[i] = "1/dev/" + arr_Device_id[i];
			// }
			//
			// return setDeviceStatus(webtopoDev, user.getAccount(), user
			// .getPasswd());
		}
		// 删除设备 批量删除
		else if ("delete".equals(action))
		{
			logger.debug("Begin to Delete.");
			return delSnmpDevice(request, arr_Device_id);
		}
		else
		{// 添加和修改设备
			currentDate = new Date();
			// 设备最近更新时间 获取当前时间
			long l_curupdatetime = currentDate.getTime() / 1000;
			currentDate = null;
			// 获取Form表单数据
			String str_device_id = request.getParameter("device_id");
			String str_device_id_ex = request.getParameter("device_id_ex");
			String str_loopback_ip = request.getParameter("loopback_ip");
			String str_vendor_id = request.getParameter("vendor_id");
			String str_device_name = request.getParameter("device_name");
			String str_device_model = request.getParameter("device_model");
			String str_device_useto = request.getParameter("device_useto");
			String device_serialnumber = request.getParameter("device_serialnumber");
			String str_manage_staff = request.getParameter("per_acc_oid");
			String str_city_id = request.getParameter("city_id");
			String snmp_ro_community = request.getParameter("snmp_ro_community");
			String snmp_rw_community = request.getParameter("snmp_rw_community");
			String str_device_addr = request.getParameter("device_addr");
			String str_complete_time = request.getParameter("complete_time");
			String str_buy_time = request.getParameter("buy_time");
			String str_service_year = request.getParameter("service_year");
			String str_staff_id = request.getParameter("staff_id");
			String str_remark = request.getParameter("remark");
			String str_gather_id = request.getParameter("gather_id");
			String content = "";
			String result = "";
			if ("add".equals(action))
			{
				logger.debug("Begin ADD Device.");
				try
				{
					content = Encoder.toISO("新增设备" + str_device_name + "，设备ip为"
							+ str_loopback_ip);
				}
				catch (Exception e)
				{
					content = "";
				}
				WebCorbaInst corba = new WebCorbaInst("db");
				String m_DeviceSerial = corba.getDeviceSerial(user);
				if (null == m_DeviceSerial || m_DeviceSerial.toLowerCase().equals("null"))
				{
					m_DeviceSerial = "1";
					return actMeg = "<script language=\"javascript\">alert('获取设备id失败,请重试!');</script>";
				}
				str_device_id = m_DeviceSerial;
				strSQL = "insert into tab_deviceresource(device_id,device_name,snmp_ro_community,resource_type_id,vendor_id,device_serialnumber,device_model,manage_staff,city_id,device_useto,complete_time,buy_time,service_year,"
						+ "staff_id,loopback_ip,snmp_rw_community,device_id_ex,gather_id,is_snmp_egw) values("
						+ "'"
						+ str_device_id
						+ "'"
						+ ",'"
						+ str_device_name
						+ "'"
						+ ",'"
						+ snmp_ro_community
						+ "'"
						+ ",101"
						+ ",'"
						+ str_vendor_id
						+ "'"
						+ ",'"
						+ device_serialnumber
						+ "'"
						+ ",'"
						+ str_device_model
						+ "'"
						+ ",'"
						+ str_manage_staff
						+ "'"
						+ ",'"
						+ str_city_id
						+ "'"
						+ ",'"
						+ str_device_useto
						+ "'"
						+ ",'"
						+ str_complete_time
						+ "'"
						+ ",'"
						+ str_buy_time
						+ "'"
						+ ","
						+ str_service_year
						+ ""
						+ ",'"
						+ str_staff_id
						+ "'"
						+ ",'"
						+ str_loopback_ip
						+ "'"
						+ ",'"
						+ snmp_rw_community
						+ "'"
						+ ",'"
						+ str_device_id_ex
						+ "'"
						+ ",'"
						+ str_gather_id + "'" + ",1)";
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				PrepareSQL psql = new PrepareSQL(strSQL);
				psql.getSQL();
				listSQL.add(strSQL);
				// 需要为此用户区域注入此设备权限，也需要给此用户所属区域上层属地注入此设备权限
				AreaManager Manager = new AreaManageSyb();
				// 获取到用户自身所属区域
				ArrayList m_AreaList = Manager.getUpperToTopAreaIds((Integer
						.parseInt(String.valueOf(m_AreaId))));
				// ArrayList m_AreaList = new ArrayList();
				// m_AreaList.add(String.valueOf(m_AreaId));
				// 获得tab_gw_res_area最大ID下一个ID
				// long m_MaxId = DataSetBean.getMaxId("tab_gw_res_area", "id");
				for (int k = 0; k < m_AreaList.size(); k++)
				{
					strSQL = "insert into tab_gw_res_area(res_type,res_id,area_id) values("
							+ "1,'" + str_device_id + "'," + m_AreaList.get(k) + ")";
					psql = new PrepareSQL(strSQL);
					psql.getSQL();
					listSQL.add(strSQL);
				}
				m_AreaList.clear();
				m_AreaList = null;
			}
			else if ("update".equals(action))
			{
				logger.debug("Update Begin...");
				String old_ip = request.getParameter("old_loopback_ip");
				try
				{
					if (old_ip.equals(str_loopback_ip))
					{
						content = Encoder.toISO("编辑设备" + str_device_name + "，设备ip为"
								+ str_loopback_ip);
					}
					else
					{
						content = Encoder.toISO("编辑设备" + str_device_name + "，原设备ip为"
								+ old_ip + "，新设备ip为" + str_loopback_ip);
					}
				}
				catch (Exception e)
				{
					content = "";
				}
				strSQL = "update tab_deviceresource set device_id_ex='"
						+ str_device_id_ex + "',device_name='" + str_device_name
						+ "',loopback_ip='" + str_loopback_ip + "',gather_id='"
						+ str_gather_id + "',vendor_id='" + str_vendor_id
						+ "',device_model='" + str_device_model + "',device_useto='"
						+ str_device_useto + "',device_serialnumber='"
						+ device_serialnumber + "',manage_staff='" + str_manage_staff
						+ "',snmp_ro_community='" + snmp_ro_community
						+ "',snmp_rw_community='" + snmp_rw_community + "',city_id='"
						+ str_city_id + "',buy_time='" + str_buy_time
						+ "',complete_time='" + str_complete_time + "',service_year="
						+ str_service_year + ",staff_id='" + str_staff_id + "',remark='"
						+ str_remark + "' where device_id='" + str_device_id + "'";
				strSQL = strSQL.replaceAll(",,", ",null,");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				PrepareSQL psql = new PrepareSQL(strSQL);
				psql.getSQL();
				listSQL.add(strSQL);
			}
			// 将上面sql执行
			logger.debug("listSQL.size():" + listSQL.size());
			int iCode[] = DataSetBean.doBatch(listSQL);
			if (iCode != null && iCode.length > 0)
			{
				if (devList.size() > 0)
				{
					if ("add".equals(action))
					{
						devList.add(str_device_id);
					}
				}
				try
				{
					result = Encoder.toISO("成功");
				}
				catch (Exception e)
				{
					e.printStackTrace();
					result = "";
				}
				if ("update".equals(action))
				{
					actMeg = "<script language=\"javascript\">alert('修改成功!');parent.goList();</script>";
				}
				else if (action.equals("add"))
				{
					actMeg = "<script language=\"javascript\">alert('添加成功!'); parent.resetForm();</script>";
				}
			}
			else
			{
				try
				{
					result = Encoder.toISO("失败");
				}
				catch (Exception e)
				{
					result = "";
				}
			}
			// 记日志
			LogItem.getInstance()
					.writeItemLog(request, 1, str_device_id, content, result);
		}
		action = null;
		listSQL.clear();
		listSQL = null;
		return actMeg;
	}

	/**
	 * 删除SNMP企业网关设备
	 * 
	 * @param request
	 * @param device_ids
	 * @return
	 */
	public String delSnmpDevice(HttpServletRequest request, String[] device_ids)
	{
		String actMeg = null;
		String content = "删除设备";
		String result = "删除成功";
		// 数组不为空
		if (device_ids != null && device_ids.length > 0)
		{
			String[] webtopoDev = new String[device_ids.length];
			for (int i = 0; i < device_ids.length; i++)
			{
				webtopoDev[i] = "1/gw/" + device_ids[i];
			}
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			if (delDevInfoMC(curUser, webtopoDev))
			{
				actMeg = "<script language=\"javascript\">parent.alert('删除成功!') ; parent.refresh();</script>";
				result += "并通知后台删除成功";
				// 更新设备状态，防止后台没有处理成功
				String deviceList = StringUtils.weave(device_ids, "','");
				String sql = " delete from tab_deviceresource where device_id in ('"
						+ deviceList + "');";
				sql += " delete from tab_gw_res_area where res_id in ('" + deviceList
						+ "');";
				// 删除设备对应的用户帐号
				sql += " delete from cus_radiuscustomer where device_id in ('"
						+ deviceList + "')";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				DataSetBean.doBatch(sql);
			}
			else
			{
				result = "删除失败";
				actMeg = "<script language=\"javascript\">parent.alert('删除失败!');</script>";
			}
			// 转换下字符集，记日志
			try
			{
				content = Encoder.toISO(content);
			}
			catch (Exception e)
			{
				content = "delete device";
			}
			try
			{
				result = Encoder.toISO(result);
			}
			catch (Exception e)
			{
				result = "success";
			}
			for (int i = 0; i < device_ids.length; i++)
			{
				logger.debug("delete device!");
				LogItem.getInstance().writeItemLog(request, 1, device_ids[i], content,
						result);
			}
		}
		else
		{
			actMeg = "<script language=\"javascript\"> alert('删除失败,请重试!');</script>";
		}
		return actMeg;
	}

	/**
	 * 删除设备
	 * 
	 * @param device_ids
	 *            设备ID数组
	 * @return String
	 */
	public String delDevice(HttpServletRequest request, String[] device_ids)
	{
		String actMeg = null;
		String content = "删除设备";
		String result = "删除成功";
		String gw_type = request.getParameter("gw_type");
		// 数组不为空
		if (device_ids != null && device_ids.length > 0)
		{
			String[] webtopoDev = new String[device_ids.length];
			for (int i = 0; i < device_ids.length; i++)
			{
				webtopoDev[i] = "1/gw/" + device_ids[i];
			}
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			if (delDevInfoMC(curUser, webtopoDev))
			{
				actMeg = "alert('并通知后台删除成功!');refresh();";
				result += "并通知后台删除成功";
				// 从数据库中取待删除设备的vendor_type和device_serial:
				String deviceList = StringUtils.weave(device_ids, "','");
				String[] params = null;
				String sqlQry = "select a.device_id,a.oui,a.device_serialnumber from tab_gw_device a "
						+ "where a.device_id in ('" + deviceList + "')";
				PrepareSQL psql = new PrepareSQL(sqlQry);
				psql.getSQL();
				List rslist = DataSetBean.executeQuery(sqlQry, params);
				logger.debug("rslist:" + rslist);
				// 更新设备状态，防止后台没有处理成功
				String sql = "update tab_gw_device set device_status = 0 where device_id in ('"
						+ deviceList + "');";
				sql += "delete from tab_gw_res_area where  res_type=1  and res_id in ('"
						+ deviceList + "');";
				// 删除设备对应的用户帐号
				String deluser = request.getParameter("deluser");
				if (Global.GW_TYPE_BBMS.equals(gw_type))
				{
					if (null != deluser && "true".equals(deluser))
					{
						sql += "delete from tab_egwcustomer where oui || device_serialnumber in (select a.oui || a.device_serialnumber from tab_gw_device a  where a.device_id in ('"
								+ deviceList + "'));";
					}
				}
				else
				{
					if (deluser != null && "true".equals(deluser))
					{
						sql += "delete from tab_hgwcustomer where oui || device_serialnumber in (select a.oui || a.device_serialnumber from tab_gw_device a  where a.device_id in ('"
								+ deviceList + "'));";
					}
				}
				psql = new PrepareSQL(sql);
				psql.getSQL();
				DataSetBean.doBatch(sql);
				// 通知中间件:
				try
				{
					if (LipossGlobals.getMidWare())
					{
						MidWare.DeviceObject[] devObjectArr = new MidWare.DeviceObject[rslist
								.size()];
						for (int i = 0; i < rslist.size(); i++)
						{
							HashMap rowmap = (HashMap) rslist.get(i);
							String stoui = (String) rowmap.get("oui");
							stoui = stoui == null ? "" : stoui;
							String deviceId_ = (String) rowmap.get("device_id");
							String devSn = "device_serialnumber";
							devSn = (String) rowmap.get(devSn);
							devSn = devSn == null ? "" : devSn;
							// 当已经把设备添加到中间件平台时，修改设备才需要更新中间件平台
							String getMidwaresql = " select device_id,city_id,oui,device_serialnumber,device_model "
									+ "from gw_device_midware where device_id='"
									+ deviceId_ + "'";
							psql = new PrepareSQL(getMidwaresql);
							psql.getSQL();
							Map MidwareMap = DataSetBean.getRecord(getMidwaresql);
							if (null != MidwareMap)
							{
								String midwareCity = (String) MidwareMap.get("city_id");
								String midwareOui = (String) MidwareMap.get("oui");
								String midwareDeviceSn = (String) MidwareMap
										.get("device_serialnumber");
								String midwaremodel = (String) MidwareMap
										.get("device_model");
								// 删除中间件域信息
								int temp_ = deleteMidwareDeviceCity(deviceId_,
										midwareOui, midwareDeviceSn, midwareCity);
								if (0 == temp_)
								{
									// 删除中间件设备
									temp_ = deleteMidwareDevice(deviceId_, midwareOui,
											midwareDeviceSn);
									if (0 == temp_)
									{
										logger.debug("删除设备调用中间件接口成功！");
										// 更新中间件表——gw_device_midware
										String updateMidwaresql = "delete gw_device_midware  where device_id in'"
												+ deviceList + "'";
										psql = new PrepareSQL(updateMidwaresql);
										psql.getSQL();
										DataSetBean.executeUpdate(updateMidwaresql);
									}
									else
									{
										logger.debug("删除设备调用中间件接口失败！");
									}
								}
							}
						}
					}
				}
				catch (Exception exx)
				{
					// exx.printStackTrace();
					try
					{
						result = Encoder.toISO(exx.getLocalizedMessage());
					}
					catch (Exception e)
					{
						result = "Inform MidWare Failure";
					}
				}
			}
			else
			{
				result = "通知后台删除失败";
				actMeg = "alert('通知后台删除失败!');refresh();";
			}
			// 转换下字符集，记日志
			try
			{
				content = Encoder.toISO(content);
			}
			catch (Exception e)
			{
				content = "delete device";
			}
			try
			{
				result = Encoder.toISO(result);
			}
			catch (Exception e)
			{
				result = "success";
			}
		}
		else
		{
			actMeg = "alert('删除失败,请重试!');";
		}
		return actMeg;
	}

	/**
	 * 删除设备,通知MC
	 * 
	 * @param curUser
	 * @param listDevID
	 * @return 删除是否成功
	 */
	public boolean delDevInfoMC(UserRes curUser, String[] devids)
	{
		if (curUser == null)
			return false;
		User user = curUser.getUser();
		try
		{
			MCControlManager mc = new MCControlManager(user.getAccount(),
					user.getPasswd());
			if (mc != null)
			{
				// 删除成功
				if (mc.DelObjs(devids) == 0)
				{
					mc = null;
					return true;
				}
			}
			mc = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 获取设备的用户信息(用户名，帐号，电话，地址，Email) Cursor
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @return
	 */
	public Cursor getCustomerOfDev(String device_id, String gw_type)
	{
		String tab_name = "tab_hgwcustomer";
		if (gw_type == null || gw_type.equals(""))
		{
			tab_name = "tab_hgwcustomer";
		}
		else
		{
			if (gw_type.equals("1") || gw_type.equals("3"))
			{
				tab_name = "tab_hgwcustomer";
			}
			else
			{
				tab_name = "tab_egwcustomer";
			}
		}
		String sql = "select user_id,username,serv_type_name as service_name from "
				+ tab_name
				+ "  a,tab_gw_serv_type b where a.serv_type_id=b.serv_type_id and device_id='"
				+ device_id + "' and user_state in('1','2')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getCursor(sql);
	}

	/**
	 * 获取设备的用户信息(用户名，帐号，电话，地址，Email) Cursor
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @return
	 */
	public Cursor getCustomerOfDevBBMS(String device_id)
	{
		/*
		 * String tab_name = ""; if (gw_type == null || gw_type.equals("")) { tab_name =
		 * "tab_customerinfo"; } else { if (gw_type.equals("1")) { tab_name =
		 * "tab_hgwcustomer"; } else { tab_name = "tab_customerinfo"; } }
		 */
		String sql = "select customer_name ,customer_pwd ,customer_type from tab_customerinfo"
				+ "  where customer_id in (select customer_id from tab_gw_device where device_id='"
				+ device_id + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getCursor(sql);
	}

	/**
	 * 获取某台设备信息
	 * 
	 * @param device_id
	 * @return
	 */
	public Map getSingleDeviceInfo(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long m_AreaId = curUser.getUser().getAreaId();
		// 如果是管理员
		// if (curUser.getUser().isAdmin()) {
		// select a.*, b.* from tab_gw_device a,tab_devres_desc b where
		// a.device_id = b.device_id
		// 江苏电信itms需要查询是否开通了awifi
		String is_awifi_sql = "";
		String sql = "";
		if (Global.JSDX.equals(Global.instAreaShortName))
		{
			is_awifi_sql = ",b.is_awifi";
		}
		if (Global.HBLT.equals(Global.instAreaShortName))
		{
			sql = "select a.*,? as area_id,b.spec_id,c.gigabit_port "
					+ is_awifi_sql
					+ " from tab_gw_device a  "
					+ "left join  tab_devicetype_info b on (a.devicetype_id=b.devicetype_id) left join tab_device_version_attribute c on(b.devicetype_id=c.devicetype_id) "
					+ "where a.device_id=?";
		}
		else
		{
			sql = "select a.*,? as area_id,b.spec_id"
					+ is_awifi_sql
					+ " from tab_gw_device a  "
					+ "left join  tab_devicetype_info b on (a.devicetype_id=b.devicetype_id) "
					+ "where a.device_id=?";
		}
		pSQL.setSQL(sql);
		// } else {
		// pSQL.setSQL(this.m_DeviceInfo_SQL);
		// }
		pSQL.setLong(1, m_AreaId);
		pSQL.setString(2, device_id);
		Map map = DataSetBean.getRecord(pSQL.getSQL());
		Map<String, String> bssDevMap = Global.G_BssDev_PortName_Map;
		if (map == null)
		{
			return null;
		}
		else
		{
			String spec_id = (String) map.get("spec_id");
			map.put("spec_name", bssDevMap.get(spec_id));
		}
		/**
		 * 修改人：姚重亮 修改时间：2010.9.6 修改内容：注释掉对表tab_devres_desc所做的操作（该表已删除）
		 */
		// pSQL.setSQL("select * from tab_devres_desc where device_id=?");
		// pSQL.setString(1, device_id);
		// Map descMap = DataSetBean.getRecord(pSQL.getSQL());
		// if (descMap != null) {
		// map.putAll(descMap);
		// }
		pSQL.setSQL("select online_status,last_time,oper_time from gw_devicestatus where device_id=?");
		pSQL.setString(1, device_id);
		Map tatusMap = DataSetBean.getRecord(pSQL.getSQL());
		if (null != tatusMap)
		{
			map.putAll(tatusMap);
		}
		return map;
	}

	/**
	 * 获取设备的相应的硬件版本/软件版本/SpecVersion/设备型号ID(devicetype_id)/设备供应商Manufacturer
	 * 
	 * @param oui
	 * @param devicetype_id
	 * @return
	 */
	// public Map getDeviceModelVersion(String oui, String devicetype_id) {
	// pSQL.setSQL(this.m_DeviceVersion_SQL);
	// pSQL.setString(1, oui);
	// pSQL.setStringExt(2, devicetype_id, false);
	// return DataSetBean.getRecord(pSQL.getSQL());
	// }
	public Map getDeviceModelVersion(String vendor_id, String devicetype_id)
	{
		String m_DeviceVersion_SQL = "select devicetype_id,vendor_name,specversion,hardwareversion,softwareversion,is_normal,is_check, "
				+ " device_model from tab_devicetype_info a, tab_vendor b, gw_device_model c "
				+ " where devicetype_id=? and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id";
		pSQL.setSQL(m_DeviceVersion_SQL);
		pSQL.setStringExt(1, devicetype_id, false);
		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 获取某厂商某设备型号的模板
	 * 
	 * @param oui
	 * @param device_model
	 * @return
	 */
	public Cursor getDeviceModelTemplate(String oui, String devicetype_id)
	{
		pSQL.setSQL(m_DevModelTemp_SQL);
		// pSQL.setString(1, oui);
		pSQL.setStringExt(1, devicetype_id, false);
		String sql = pSQL.getSQL();
		logger.debug("Get Template For Model:" + sql);
		return DataSetBean.getCursor(sql);
	}

	/**
	 * 列表显示用户分组信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getGroupList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_GroupList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_GroupList_SQL);
		String strGroupList = FormUtil.createListBox(cursor, "manage_staff",
				"group_name", flag, compare, rename);
		return strGroupList;
	}

	/**
	 * 根据设备类型显示版本信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getMibVersionList(boolean flag, String compare, String rename,
			HttpServletRequest request)
	{
		String model = request.getParameter("model");
		String SQL = "select distinct os_version from tab_devicetype_info where device_name='"
				+ model + "'";
		PrepareSQL psql = new PrepareSQL(SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(SQL);
		String strMibVersionList = FormUtil.createListBox(cursor, "os_version",
				"os_version", flag, compare, rename);
		return strMibVersionList;
	}

	/**
	 * 列表显示分组下用户信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getUserOfGroupList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_UserOfGroupList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_UserOfGroupList_SQL);
		String strUserList = FormUtil.createListBox(cursor, "per_acc_oid",
				"acc_loginname", flag, compare, rename);
		return strUserList;
	}

	/**
	 * 列表显示局向信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getOfficeList1(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_OfficeList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_OfficeList_SQL);
		String strOfficeList = FormUtil.createListBox(cursor, "office_id", "office_name",
				flag, compare, rename);
		return strOfficeList;
	}

	/**
	 * 列表显示小区信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getZoneList1(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_ZoneList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_ZoneList_SQL);
		String strZoneList = FormUtil.createListBox(cursor, "zone_id", "zone_name", flag,
				compare, rename);
		return strZoneList;
	}

	/**
	 * 列表显示小区信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getZoneList(boolean flag, String compare, String rename,
			String gather_ids)
	{
		String sql = "select zone_id,zone_name from tab_zone where gather_id in("
				+ gather_ids + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strZoneList = FormUtil.createListBox(cursor, "zone_id", "zone_name", flag,
				compare, rename);
		return strZoneList;
	}

	/**
	 * 列表显示局向信息
	 * 
	 * @author Xiaoxf
	 * @date 2007-01-19
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @param control
	 *            :city_id_list 范围控制
	 * @return String
	 */
	public String getOfficeList(boolean flag, String compare, String m_CityIdQuery,
			String rename)
	{
		String sql = "select distinct office_id,office_name from tab_office order by office_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strOfficeList = FormUtil.createListBox(cursor, "office_id", "office_name",
				flag, compare, rename);
		return strOfficeList;
	}

	/**
	 * 根据登陆用户的city_id获得其自身及其所有下属属地并以此过滤设备 modify by YYS 2006-09-24
	 * 
	 * @param request
	 * @return Cursor
	 */
	public Cursor getDeviceBasInfo(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		pSQL.setSQL(m_GetDeviceBasInfoByCities_SQL);
		SelectCityFilter scf = new SelectCityFilter();
		pSQL.setStringExt(1, scf.getAllSubCityIds(city_id, true), false);
		logger.debug(pSQL.getSQL());
		return DataSetBean.getCursor(pSQL.getSQL());
		//
		// CityAct act = new CityAct();
		// String citys =
		// StringUtils.weave(act.getNextCityIdsByCityPid(city_id));
		// sql = "select * from tab_deviceresource where resource_type_id=3 and
		// city_id in ("
		// + citys + ") order by loopback_ip";
		// logger.debug("feng:" + sql);
		// return DataSetBean.getCursor(sql);
	}

	public Cursor getDslamInfoCursor(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		ArrayList list = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String citys = StringUtils.weave(list);
		list = null;
		String sql = "select device_id,loopback_ip,device_name  from tab_deviceresource where (resource_type_id=10 or resource_type_id=11) and city_id in ("
				+ citys + ") order by loopback_ip";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getCursor(sql);
	}

	/**
	 * 根据设备编码Device_id获得设备信息
	 * 
	 * @param m_DeviceId
	 * @return Map
	 */
	public Map getDeviceInfoMap(String m_DeviceId)
	{
		PrepareSQL psql = new PrepareSQL(
				"select * from tab_deviceresource where device_id='" + m_DeviceId + "'");
		psql.getSQL();
		return DataSetBean.getRecord("select * from tab_deviceresource where device_id='"
				+ m_DeviceId + "'");
	}

	/**
	 * 根据设备编码Device_id获得ADSL接入用户表信息
	 * 
	 * @param deviceId
	 * @return Map
	 */
	public Map getRadiuscustomerByDeviceId(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL(
				"select * from cus_radiuscustomer where device_id='" + deviceId + "'");
		psql.getSQL();
		return DataSetBean.getRecord("select * from cus_radiuscustomer where device_id='"
				+ deviceId + "'");
	}

	/**
	 * 列表显示小区信息
	 * 
	 * @param flag
	 *            判断是否关联
	 * @param compare
	 *            比较值
	 * @param rename
	 *            重命名
	 * @return String
	 */
	public String getZoneList(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_ZoneList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_ZoneList_SQL);
		String strZoneList = FormUtil.createListBox(cursor, "zone_id", "zone_name", flag,
				compare, rename);
		return strZoneList;
	}

	/**
	 * 取得当前用户的属地及下属属地
	 * 
	 * @author Yanhj
	 * @date 2006-8-22
	 * @param flag
	 * @param pos
	 * @param rename
	 * @param request
	 * @return
	 */
	public String getCityListSelf(boolean flag, String pos, String rename,
			HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String[] cityid = city_id.split(",");
		List list = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			list.add(cityid[i]);
		}
		String sql = "";
		if (Global.CQDX.equals(Global.instAreaShortName))
		{
			sql = "select city_id,city_name from tab_city where parent_id in("
					+ StringUtils.weave(list) + ") or city_id in("
					+ StringUtils.weave(list) + ")";
		}
		else
		{
			sql = "select city_id,city_name from tab_city where parent_id='" + city_id
					+ "' or city_id='" + city_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strCityList = FormUtil.createListBox(cursor, "city_id", "city_name", flag,
				pos, rename);
		return strCityList;
	}

	/**
	 * 获取用户权限范围内的采集机,并生成下列框
	 */
	public String getGatherList(HttpSession session, String pos, String rename,
			boolean showChild)
	{
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		List m_ProcessesList = curUser.getUserProcesses();
		logger.debug("ABC--------------------:" + m_ProcessesList);
		return FormUtil.createListBox(getGatherIdNameMap(m_ProcessesList), "gather_id",
				"descr", showChild, pos, rename);
	}

	/**
	 * 获取用户权限范围内的采集机(私有)
	 * 
	 * @param m_ProcessesList
	 *            用户权限内的采集机列表
	 * @return
	 */
	private Cursor getGatherIdNameMap(List m_ProcessesList)
	{
		PrepareSQL psql = new PrepareSQL(m_GatherList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_GatherList_SQL);
		Cursor cursor_tmp = new Cursor();
		fields = cursor.getNext();
		while (fields != null)
		{
			if (m_ProcessesList.contains(fields.get("gather_id")))
			{
				cursor_tmp.add(fields);
			}
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		return cursor_tmp;
	}

	/**
	 * 获取系统内的所有采集机信息
	 * 
	 * @return
	 */
	public Map getGatherIdNameMap()
	{
		Map m_GatherIdNameMap = new HashMap();
		m_GatherIdNameMap.clear();
		cursor = getGatherList();
		fields = cursor.getNext();
		while (fields != null)
		{
			m_GatherIdNameMap.put(fields.get("gather_id"), fields.get("descr"));
			fields = cursor.getNext();
		}
		// clear
		fields = null;
		cursor = null;
		return m_GatherIdNameMap;
	}

	/**
	 * 根据区域编号删除对应区域采集机信息
	 * 
	 * @param m_AreaId
	 * @return boolean
	 */
	public boolean delGathersWithAreaId(String m_AreaId)
	{
		pSQL.setSQL(m_DelGathers_ByAreaId);
		pSQL.setInt(1, Integer.parseInt(m_AreaId));
		int iCode = DataSetBean.executeUpdate(pSQL.getSQL());
		return (iCode > 0) ? true : false;
	}

	/**
	 * 把区域与采集机关联关系入库
	 * 
	 * @param m_ProcessList
	 * @param m_AreaId
	 * @return boolean
	 */
	public boolean saveGathers(String[] m_ProcessList, String m_AreaId)
	{
		// modify by lizhaojun /tab_gw_res_area表结构发生变换
		// long m_MaxId = DataSetBean.getMaxId("tab_gw_res_area", "id");
		ArrayList list = new ArrayList();
		for (int k = 0; k < m_ProcessList.length; k++)
		{
			pSQL.setSQL(m_Gathers_Save);
			// pSQL.setInt(1, Integer.parseInt(String.valueOf(m_MaxId)) + k);
			pSQL.setInt(1, 2);
			pSQL.setString(2, m_ProcessList[k]);
			pSQL.setInt(3, Integer.parseInt(m_AreaId));
			list.add(pSQL.getSQL());
		}
		int[] iCode = DataSetBean.doBatch(list);
		return (iCode != null && iCode[0] > 0) ? true : false;
	}

	/**
	 * 获得所有采集机列表
	 * 
	 * @return Cursor
	 */
	public Cursor getGatherList()
	{
		PrepareSQL psql = new PrepareSQL(m_GatherList_SQL);
		psql.getSQL();
		return DataSetBean.getCursor(m_GatherList_SQL);
	}

	/**
	 * 获取设备所支持的业务代码
	 * 
	 * @param oui
	 * @param device_model
	 * @return
	 */
	public String getSupportServiceCode(String oui, String devicetype_id)
	{
		pSQL.setSQL(m_DeviceSupport_SQL);
		// pSQL.setString(1, oui);
		pSQL.setStringExt(1, devicetype_id, false);
		logger.debug("getSupportServiceCode:" + pSQL.getSQL());
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		StringBuffer sb = new StringBuffer();
		// 下拉框形式
		// sb.append("<select><option>==支持的业务代码==</option>");
		// while (fields != null) {
		// sb.append("<option>" + fields.get("servicecode") + "</option>");
		// fields = cursor.getNext();
		// }
		// sb.append("</select>");
		// 字符串换行形式
		sb.append("");
		while (fields != null)
		{
			sb.append("" + fields.get("service_name") + "\n");
			fields = cursor.getNext();
		}
		return sb.toString();
	}

	/**
	 * 获取一种设备的业务支持信息 johnson 2008-7-14 String
	 */
	public String getSupportService(String devicetype_id)
	{
		String strSQL = "select b.serv_type_name  from tab_servicecode a,tab_gw_serv_type b,tab_service c "
				+ "where a.service_id=c.service_id and c.serv_type_id=b.serv_type_id  and c.flag > 0 "
				+ "and a.devicetype_id=? group by b.serv_type_id,b.serv_type_name";
		pSQL.setSQL(strSQL);
		pSQL.setStringExt(1, devicetype_id, false);
		logger.debug("getSupportService:" + pSQL.getSQL());
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		StringBuffer sb = new StringBuffer();
		// 字符串换行形式
		sb.append("");
		while (fields != null)
		{
			sb.append(fields.get("serv_type_name"));
			sb.append("\n");
			fields = cursor.getNext();
		}
		return sb.toString();
	}

	/**
	 * 修改人：姚重亮 修改日期：2010.9.6 修改内容：注释掉方法getServiceStatusByCustomer（表tab_gw_user_serv已删除）
	 */
	// /**
	// * 获取设备开通的业务及是否激活
	// *
	// * @param device_id
	// * @return
	// */
	// public List getServiceStatusByCustomer(String device_id) {
	// List list = new ArrayList();
	// pSQL.setSQL(m_NowDeviceSupportStatus_SQL);
	// pSQL.setString(1, device_id);
	// cursor = DataSetBean.getCursor(pSQL.getSQL());
	// fields = cursor.getNext();
	//
	// while (fields != null) {
	// list.add(new String[] { (String) fields.get("serv_type_id"),
	// (String) fields.get("is_active"), (String) fields.get("service_name") });
	// fields = cursor.getNext();
	// }
	// return list;
	// }
	/**
	 * 获取设备开通的业务信息 johnson 2008-7-14 List
	 */
	public List getServiceStatByDevice(String device_id)
	{
		List list = new ArrayList();
		// 根据设备ID获取业务信息
		String strServSQL = "select b.serv_type_id, a.serv_state,b.serv_type_name from gw_dev_serv a, tab_gw_serv_type b "
				+ "where a.device_id = ? and a.serv_type_id = b.serv_type_id";
		pSQL.setSQL(strServSQL);
		pSQL.setString(1, device_id);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		while (fields != null)
		{
			list.add(new String[] { (String) fields.get("serv_type_id"),
					(String) fields.get("serv_state"),
					(String) fields.get("serv_type_name") });
			fields = cursor.getNext();
		}
		return list;
	}

	/**
	 * 获取绑定了该设备的用户及状态
	 * 
	 * @param device_id
	 * @return
	 */
	public List getBindCustomerStatusByDeviceId(String oui, String device_serialnumber)
	{
		List list = new ArrayList();
		pSQL.setSQL(m_BindCustomerStatus_SQL);
		pSQL.setString(1, oui);
		pSQL.setString(2, device_serialnumber);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		while (fields != null)
		{
			list.add((String) fields.get("user_state"));
			fields = cursor.getNext();
		}
		return list;
	}

	/**
	 * 修改人：姚重亮 修改时间：2010.9.6
	 * 修改内容：注释掉方法getSupportServiceCodeByCustomer（表tab_gw_user_serv已删除）
	 */
	// /**
	// * 获取设备当前开通的业务代码 add by benyp
	// *
	// * @param oui
	// * @param device_model
	// * @return
	// */
	// public String getSupportServiceCodeByCustomer(String device_id) {
	// pSQL.setSQL(m_NowDeviceSupport_SQL);
	//
	// pSQL.setString(1, device_id);
	// cursor = DataSetBean.getCursor(pSQL.getSQL());
	// fields = cursor.getNext();
	// StringBuffer sb = new StringBuffer();
	//
	// // 字符串换行形式
	// sb.append("");
	// while (fields != null) {
	// sb.append("" + fields.get("service_name") + "\n");
	// fields = cursor.getNext();
	// }
	//
	// return sb.toString();
	// }
	/**
	 * 修改人：姚重亮 修改日期：2010.9.8 修改内容：注释掉该方法（表tab_gw_user_serv已删除）
	 */
	/**
	 * 获取用户设备当前开通的业务 johnson 2008-7-14 String
	 */
	// public String getSupportServiceByDevice(String device_id) {
	//
	// String strSQL = "select serv_type_name from tab_gw_serv_type  "
	// + "where device_id = ?";
	// pSQL.setSQL(strSQL);
	// pSQL.setString(1, device_id);
	// cursor = DataSetBean.getCursor(pSQL.getSQL());
	// fields = cursor.getNext();
	// StringBuffer sb = new StringBuffer();
	//
	// // 字符串换行形式
	// sb.append("");
	// while (fields != null) {
	// sb.append("" + fields.get("serv_type_name") + "\n");
	// fields = cursor.getNext();
	// }
	//
	// return sb.toString();
	// }
	/**
	 * 获取ITMS操作历史信息
	 */
	public String getHistoryOperation(String device_id)
	{
		String result = "";
		pSQL.setSQL(m_getHistoryOperation_SQL);
		pSQL.setStringExt(1, device_id, true);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		fields = cursor.getNext();
		// StringBuffer sb = new StringBuffer();
		String serviceHtml = "<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		// 字符串换行形式
		if (fields != null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF' height='20'>";
			serviceHtml += "<td class=column align='center'>工单编号</td><td class=column align='center'>用户帐户</td><td class=column align='center'>执行结果</td><td class=column align='center'>开始时间</td><td class=column align='center'>结束时间</td></tr>";
			// sb.append(""+" 工单编号 | 用户帐户 | 执行结果 | 开始时间 | 结束时间 "+"\n");
		}
		else
		{
			serviceHtml += "<tr bgcolor='#FFFFFF' height='20'>";
			serviceHtml += "<td colspan='2'><span>没有操作信息！</span></td></tr>";
			// sb.append("没有操作信息！");
		}
		while (fields != null)
		{
			String tmp = "";
			long Start_time = 0;
			tmp = (String) fields.get("start_time");
			if (tmp != null && !"".equals(tmp))
			{
				Start_time = Long.parseLong(tmp);
			}
			else
			{
				Start_time = (new Date()).getTime();
			}
			long End_time = 0;
			tmp = (String) fields.get("end_time");
			if (tmp != null && !"".equals(tmp))
			{
				End_time = Long.parseLong(tmp);
			}
			else
			{
				End_time = (new Date()).getTime();
			}
			String start_time = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", Start_time);
			String end_time = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", End_time);
			String res = (String) fields.get("fault_code");
			String name = (String) fields.get("username");
			String username = name.length() < 1 ? "           " : name;
			if ("-1".equals(res))
			{
				result = " 连接不上        ";
			}
			else if ("0".equals(res))
			{
				result = " 未知错误        ";
			}
			else if ("-2".equals(res))
			{
				result = " 连接超时        ";
			}
			else if ("-3".equals(res))
			{
				result = " 未找到相关工单  ";
			}
			else if ("-4".equals(res))
			{
				result = " 未找到相关设备  ";
			}
			else if ("-5".equals(res))
			{
				result = "未找到相关RPC命令";
			}
			else if ("-6".equals(res))
			{
				result = " 设备正被操作    ";
			}
			else
			{
				result = " 成功            ";
			}
			/*
			 * sb.append("" + fields.get("sheet_id") +" | "+username +" | "+result
			 * +" | "+start_time +" | "+end_time +"\n");
			 */
			serviceHtml += "<tr height='20'>";
			serviceHtml += "<td bgcolor='#FFFFFF'>";
			serviceHtml += (String) fields.get("sheet_id");
			serviceHtml += "</td><td bgcolor='#FFFFFF'>";
			serviceHtml += username;
			serviceHtml += "</td><td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += result;
			serviceHtml += "</td><td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += start_time;
			serviceHtml += "</td><td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += end_time;
			serviceHtml += "</td></tr>";
			fields = cursor.getNext();
		}
		serviceHtml += "</table>";
		// return sb.toString();
		return serviceHtml;
	}

	/**
	 * 统计设备
	 * 
	 * @return
	 */
	public String getDeviceState(HttpServletRequest request)
	{
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		if (null == gw_type_Str)
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		String strData = "";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// String sql =
		// "select a.device_status,count(a.device_id) as num  from tab_gw_device a where a.device_status != -1 and gw_type="
		// + gw_type
		// +
		// " and device_id in(select res_id from tab_gw_res_area where area_id="
		// + curUser.getAreaId() + " and res_type=1) group by a.device_status";
		// String sql1 = "";
		// // 如果是管理员，则不需要权限过滤
		// if (curUser.getUser().isAdmin()) {
		// sql =
		// "select device_status,count(device_id) as num from tab_gw_device where device_status != -1 and gw_type="
		// + gw_type + " group by device_status";
		// }
		PrepareSQL pSQL = new PrepareSQL();
		if (curUser.getUser().isAdmin())
		{
			pSQL.setSQL("select device_status,count(device_id) as num from tab_gw_device where device_status != -1 and gw_type="
					+ gw_type + " group by device_status");
		}
		else
		{
			pSQL.setSQL("select a.device_status,count(a.device_id) as num  from tab_gw_device a where a.device_status != -1 and gw_type="
					+ gw_type);
			pSQL.append(PrepareSQL.AND, "a.city_id",
					CityDAO.getAllNextCityIdsByCityPid(curUser.getCityId()));
			pSQL.append(" group by a.device_status ");
		}
		cursor = DataSetBean.getCursor(
				pSQL.toString(),
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
		fields = cursor.getNext();
		String state = null;
		String num = null;
		Map mapResult = new HashMap();
		while (fields != null)
		{
			state = (String) fields.get("device_status");
			num = (String) fields.get("num");
			fields = cursor.getNext();
			mapResult.put(state, num);
		}
		logger.debug("mapResult:" + mapResult);
		strData += "<TR><td class=column rowspan=2 align=right>设备是否确认</td>";
		strData += "<td class=column >已确认</td>";
		num = (String) mapResult.get("1");
		if (num != null)
		{
			strData += "<td class=column><a href='DeviceList.jsp?device_status=1&gw_type="
					+ gw_type + "' target='_blank'>" + num + "</a></td></TR>";
		}
		else
		{
			strData += "<td class=column><a href='#'>0</a></td></TR>";
		}
		num = (String) mapResult.get("0");
		strData += "<TR><td class=column>未确认</td>";
		if (num != null)
		{
			strData += "<td class=column><a href='DeviceList.jsp?device_status=0&gw_type="
					+ gw_type + "' target='_blank'>" + num + "</a></td></TR>";
		}
		else
			strData += "<td class=column><a href='#'>0</a></td></TR>";
		/*
		 * num = (String)mapResult.get("0"); strData +=
		 * "<TR><td class=column>已删除的设备</td>"; if(num != null){ strData += "<td
		 * class=column><a href='DeviceList.jsp?device_status=-1'>" + num +
		 * "</a></td></TR>"; }else strData +=
		 * "<td class=column><a href='#'>0</a></td></TR>";
		 */
		strData += "<TR><td class=column colspan=3>&nbsp;</td></TR>";
		PrepareSQL pSQL1 = new PrepareSQL();
		PrepareSQL pSQL2 = new PrepareSQL();
		if (curUser.getUser().isAdmin())
		{
			// sql =
			// "select cpe_currentstatus,count(device_id) as num from tab_gw_device where device_status != -1 and gw_type="
			// + gw_type + " group by cpe_currentstatus";
			// sql:根据在线标志位查询在线设备数； sql1:查询十二小时内有上报连接的设备数
			pSQL1.setSQL("select b.online_status,count(a.device_id) as num from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id and gw_type="
					+ gw_type + " group by b.online_status");
			pSQL2.setSQL("select b.online_status,count(a.device_id) as Rnum from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id and gw_type="
					+ gw_type
					+ " and  ("
					+ nowTime
					+ "-last_time)<=43200 group by b.online_status");
			// sql = "select cpe_currentstatus,count(device_id) as num from
			// tab_gw_device where device_id in(select res_id from
			// tab_gw_res_area where area_id="+ + " and res_type=1) group by
			// cpe_currentstatus";
		}
		else
		{
			// sql = "select a.cpe_currentstatus,count(a.device_id) from
			// tab_gw_device a left join tab_gw_res_area b on
			// a.device_id=b.res_id and b.res_type=1 where b.area_id=? and
			// device_status != -1 group by a.cpe_currentstatus";
			pSQL1.setSQL("select b.online_status,count(a.device_id) as num from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id and gw_type="
					+ gw_type + " ");
			pSQL1.append(PrepareSQL.AND, "a.city_id",
					CityDAO.getAllNextCityIdsByCityPid(curUser.getCityId()));
			pSQL1.append(" group by b.online_status ");
			pSQL2.setSQL("select b.online_status,count(a.device_id) as rnum from tab_gw_device a,gw_devicestatus b where a.device_id=b.device_id and gw_type="
					+ gw_type + " and  (" + nowTime + "-last_time)<=43200 ");
			pSQL2.append(PrepareSQL.AND, "a.city_id",
					CityDAO.getAllNextCityIdsByCityPid(curUser.getCityId()));
			pSQL2.append(" group by b.online_status ");
		}
		// sql = "select cpe_currentstatus,count(device_id) as num from
		// tab_gw_device where device_id in(select res_id from tab_gw_res_area
		// where area_id="+ curUser.getUser().getAreaId() + " and res_type=1)
		// group by cpe_currentstatus";
		mapResult.clear();
		cursor = DataSetBean.getCursor(pSQL1.getSQL(), DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		fields = cursor.getNext();
		while (null != fields)
		{
			state = (String) fields.get("online_status");
			num = (String) fields.get("num");
			if (state.equals("null") || state.equals(""))
				mapResult.put("_null_", num);// 设备未知状态
			else
				mapResult.put(state, num);
			fields = cursor.getNext();
		}
		Cursor Rnums = DataSetBean.getCursor(
				pSQL2.getSQL(),
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
		Map map = Rnums.getNext();
		Map result = new HashMap();
		String Rnum = null;
		while (null != map)
		{
			state = (String) map.get("online_status");
			Rnum = (String) map.get("rnum");
			if (state.equals("null") || state.equals(""))
				result.put("_null_", Rnum);
			else
				result.put(state, Rnum);
			map = Rnums.getNext();
		}
		Rnum = (String) result.get("1");
		int snum = 0;
		if (null != (String) mapResult.get("1"))
		{
			snum = Integer.parseInt((String) mapResult.get("1"));
		}
		strData += "<TR><td class=column rowspan=3 align=right>设备是否在线</td>";
		strData += "<td class=column>设备在线数" + "</td>";
		if (Rnum != null)
		{
			strData += "<td class=column><a href='DeviceList.jsp?cpe_currentstatus=1&gw_type="
					+ gw_type + "' target='_blank'>" + Rnum + "</a></td></tr>";
		}
		else
			strData += "<td class=column><a href='#'>0</a></td></tr>";
		num = (String) mapResult.get("0");
		int Tnum = 0;
		if (null != num)
		{
			Tnum = Integer.parseInt(num);
		}
		int iRnum = 0;
		if (null != Rnum)
		{
			iRnum = Integer.parseInt(Rnum);
		}
		iRnum = snum - iRnum;
		strData += "<TR><td class=column>设备下线数" + "</td>";
		if (Tnum != 0 || iRnum != 0)
		{
			strData += "<td class=column><a href='DeviceList.jsp?cpe_currentstatus=0&gw_type="
					+ gw_type + "' target='_blank'>" + (Tnum + iRnum) + "</a></td></tr>";
		}
		else
			strData += "<td class=column><a href='#'>0</a></td></tr>";
		num = (String) mapResult.get("_null_");
		strData += "<TR><td class=column>设备未知状态" + "</td>";
		if (null != num)
		{
			strData += "<td class=column><a href='DeviceList.jsp?cpe_currentstatus=_null_&gw_type="
					+ gw_type + "' target='_blank'>" + num + "</a></td></tr>";
		}
		else
			strData += "<td class=column><a href='#'>0</a></td></tr>";
		cursor = null;
		mapResult = null;
		if (fields != null)
		{
			fields.clear();
		}
		fields = null;
		if (strData.equals(""))
		{
			strData += "<TR><td class=column colspan=2>无统计数据.</td></TR>";
		}
		if (LipossGlobals.inArea(Global.NXDX) || LipossGlobals.inArea(Global.JXDX))
		{
			strData += "<TR><td class=column colspan=3>&nbsp;</td></TR>";
			PrepareSQL pSQL3 = new PrepareSQL();
			PrepareSQL pSQL4 = new PrepareSQL();
			if (curUser.getUser().isAdmin())
			{
				// sql3: 查询绑定设备数； sql4:查询未绑定数
				pSQL3.setSQL("select 1 as bindstatus, count(a.device_id) as bindnum from tab_gw_device a where a.customer_id is not null and gw_type="
						+ gw_type);
				pSQL4.setSQL("select 0 as bindstatus, count(a.device_id) as unbindnum from tab_gw_device a where a.customer_id is null and gw_type="
						+ gw_type);
			}
			Cursor bindMap = DataSetBean.getCursor(
					pSQL3.getSQL(),
					DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
							this.getClass().getName()));
			Map map3 = bindMap.getNext();
			String bindnum = null;
			while (null != map3)
			{
				logger.warn("map3 " + map3);
				bindnum = (String) map3.get("bindnum");
				map3 = bindMap.getNext();
			}
			Cursor unBindMap = DataSetBean.getCursor(
					pSQL4.getSQL(),
					DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
							this.getClass().getName()));
			Map map4 = unBindMap.getNext();
			String unbindnum = null;
			while (null != map4)
			{
				unbindnum = (String) map4.get("unbindnum");
				map4 = unBindMap.getNext();
			}
			strData += "<TR><td class=column rowspan=2 align=right>设备是否绑定</td>";
			strData += "<td class=column >已绑定</td>";
			if (bindnum != null)
			{
				strData += "<td class=column><a href='DeviceList.jsp?cpe_allocatedstatus=1&gw_type="
						+ gw_type + "' target='_blank'>" + bindnum + "</a></td></TR>";
			}
			else
			{
				strData += "<td class=column><a href='#'>0</a></td></TR>";
			}
			strData += "<TR><td class=column>未绑定</td>";
			if (unbindnum != null)
			{
				strData += "<td class=column><a href='DeviceList.jsp?cpe_allocatedstatus=0&gw_type="
						+ gw_type + "' target='_blank'>" + unbindnum + "</a></td></TR>";
			}
			else
			{
				strData += "<td class=column><a href='#'>0</a></td></TR>";
			}
		}
		return strData;
	}

	/**
	 * 构造OUI下拉框
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @return
	 */
	public String getOUIStr(boolean flag, String compare, String rename)
	{
		cursor = DataSetBean.getCursor(m_Vendor_SQL);
		String strVendorList = FormUtil.createListBox(cursor, "vendor_name",
				"vendor_name", flag, compare, rename);
		cursor = null;
		return strVendorList;
	}

	/**
	 * 获取设备类型的清单表
	 * 
	 * @return
	 */
	public ArrayList getDeviceTypeList(HttpServletRequest request)
	{
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(m_DeviceTypeList_SQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(m_DeviceTypeList_SQL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(m_DeviceTypeList_SQL, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 设备型号的下拉框
	 * 
	 * @param flag
	 * @param compare
	 * @param rename
	 * @return
	 */
	public String getDeviceTypeSelectStr(boolean flag, String compare, String rename)
	{
		PrepareSQL psql = new PrepareSQL(m_DeviceTypeList_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_DeviceTypeList_SQL);
		String deviceTypeStr = FormUtil.createListBox(cursor, "devicetype_id",
				"device_model", flag, compare, rename);
		return deviceTypeStr;
	}

	/**
	 * 保存设备类型
	 * 
	 * @param request
	 * @return
	 */
	public boolean updateDeviceType(HttpServletRequest request)
	{
		boolean result = true;
		// String oui = request.getParameter("vendor_id");
		String vendorId = request.getParameter("vendor_id");
		// String manufacturer = request.getParameter("manufacturer");
		// <select name=device_model的下拉框 value=device_model_id
		String deviceModelId = request.getParameter("device_model");
		String SpecVersion = request.getParameter("SpecVersion");
		String HardwareVersion = request.getParameter("HardwareVersion");
		String SoftwareVersion = request.getParameter("SoftwareVersion");
		String area_id = request.getParameter("area_id");
		String action = request.getParameter("action");
		PrepareSQL prepareSql = new PrepareSQL();
		/**
		 * String manufacturer = ""; pSQL.setSQL(this.m_getVendor_SQL); pSQL.setString(1,
		 * vendorId); logger.debug(pSQL.getSQL()); Cursor cursor =
		 * DataSetBean.getCursor(pSQL.getSQL()); Map fields = cursor.getNext(); if (fields
		 * != null) { manufacturer = (String) fields.get("vendor_name"); }
		 **/
		// 设备类型增加
		if ("add".equals(action))
		{
			String addDevTypeSQL = "insert into tab_devicetype_info("
					+ " devicetype_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,area_id)"
					+ " values(?,?,?,?,?,?,?)";
			prepareSql.setSQL(addDevTypeSQL);
			long max_id = DataSetBean.getMaxId("tab_devicetype_info", "devicetype_id");
			prepareSql.setStringExt(1, String.valueOf(max_id), false);
			prepareSql.setStringExt(2, vendorId, true);
			prepareSql.setStringExt(3, deviceModelId, true);
			prepareSql.setStringExt(4, SpecVersion, true);
			prepareSql.setStringExt(5, HardwareVersion, true);
			prepareSql.setStringExt(6, SoftwareVersion, true);
			prepareSql.setStringExt(7, area_id, false);
			logger.debug("insertDeviceTypeSql:" + prepareSql.getSQL());
			int row = DataSetBean.executeUpdate(prepareSql.getSQL());
			if (row <= 0)
			{
				result = false;
			}
		}
		else if ("delete".equals(action))
		{
			String id = request.getParameter("devicetype_id");
			if (null == id)
			{
				result = false;
			}
			prepareSql.setSQL(m_DeviceTypeDelete_SQL);
			prepareSql.setStringExt(1, id, false);
			logger.debug("deleteDeviceTypeSql:" + prepareSql.getSQL());
			DataSetBean.executeUpdate(prepareSql.getSQL());
		}
		else
		{
			String id = request.getParameter("devicetype_id");
			if (null == id)
			{
				result = false;
			}
			else
			{
				String modifyDevTypeSQL = "update tab_devicetype_info set "
						+ " vendor_id=?,device_model_id=?,specversion=?,hardwareversion=?,softwareversion=?,area_id=? "
						+ " where devicetype_id=?";
				prepareSql.setSQL(modifyDevTypeSQL);
				prepareSql.setStringExt(1, vendorId, true);
				prepareSql.setStringExt(2, deviceModelId, true);
				prepareSql.setStringExt(3, SpecVersion, true);
				prepareSql.setStringExt(4, HardwareVersion, true);
				prepareSql.setStringExt(5, SoftwareVersion, true);
				prepareSql.setStringExt(6, area_id, false);
				prepareSql.setStringExt(7, id, false);
				logger.debug("updateDeviceTypeSql:" + prepareSql.getSQL());
				int row = DataSetBean.executeUpdate(prepareSql.getSQL());
				if (row < 0)
				{
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * 查询设备的详细信息
	 * 
	 * @param request
	 * @return
	 */
	public HashMap getDeviceTypeInfoByID(HttpServletRequest request)
	{
		HashMap deviceTypeInfo = null;
		PrepareSQL prepareSql = new PrepareSQL();
		String id = request.getParameter("devicetype_id");
		if (null != id)
		{
			prepareSql.setSQL(m_DeviceTypeByID_SQL);
			prepareSql.setStringExt(1, id, false);
			logger.debug("getDeviceTypeInfoByID_SQL:" + prepareSql.getSQL());
			deviceTypeInfo = DataSetBean.getRecord(prepareSql.getSQL());
		}
		return deviceTypeInfo;
	}

	/**
	 * topo设置未确认
	 */
	public String setDeviceStatus_enter(User user, String[] device_ids)
	{
		String batchSQL = "";
		long m_AreaId = user.getAreaId();
		for (int i = 0; i < device_ids.length; i++)
		{
			logger.debug("device_ids[" + i + "]:" + device_ids[i]);
			String[] device_id_temp = device_ids[i].split("1/gw/");
			String device_id = "";
			if (device_id_temp.length < 2)
			{
				continue;
			}
			else
			{
				device_id = device_id_temp[1];
			}
			logger.debug("device_id:" + device_id);
			batchSQL += "update tab_gw_device set device_status=1,city_id='"
					+ user.getCityId() + "' where device_id='" + device_ids[i] + "';";
			batchSQL += "delete from tab_gw_res_area where res_id='" + device_ids[i]
					+ "'" + " and res_type=1";
			batchSQL += EGWUserInfoAct.insertAreaTable(device_ids[i],
					String.valueOf(m_AreaId));
		}
		PrepareSQL psql = new PrepareSQL(batchSQL);
		psql.getSQL();
		int iCode[] = DataSetBean.doBatch(batchSQL);
		if (iCode == null || iCode.length <= 0)
		{
			return "alert('设备确认更新(数据库)失败！');";
		}
		return setDeviceStatus(device_ids, user.getAccount(), user.getPasswd());
	}

	/**
	 * 设置设备的确认状态
	 * 
	 * @param dev_idList
	 * @return 返回javascript字符串，web端使用eval函数执行代码。length表示成功确认设备数。
	 */
	public String setDeviceStatus(String[] device_ids, String account, String passwd)
	{
		String msg = "";
		// begin delete by w5221 设备的状态更新由MasterControl来做了
		/*
		 * pSQL.setSQL(m_SetDeviceStatus_SQL); pSQL.setInt(1, status);
		 * pSQL.setStringExt(2, StringUtils.weave(dev_idList), false); String sql =
		 * pSQL.getSQL(); logger.debug("确认设备：" + sql); int length
		 * =DataSetBean.executeUpdate(sql); sql = null;
		 */
		// end delete by w5221 设备的状态更新由MasterControl来做了
		// 通知MC
		boolean result = true;
		if (device_ids.length <= 0)
		{
			result = false;
			msg = "设备确认更新失败！";
		}
		else
		{
			result = ACSManager.getInstance(account, passwd).confirmDevice(device_ids);
			if (!result)
			{
				msg = "设备确认更新失败！";
			}
			else
			{
				msg = "设备确认更新成功！";
			}
		}
		return msg;
	}

	/**
	 * 设置设备的确认状态
	 * 
	 * @param dev_idList
	 * @return 返回javascript字符串，web端使用eval函数执行代码。length表示成功确认设备数。
	 */
	public static int setChangeDeviceStatus(DeviceStatusStruct[] deviceStatusStructArr,
			String account, String passwd)
	{
		int msg = -1;
		// begin delete by w5221 设备的状态更新由MasterControl来做了
		/*
		 * pSQL.setSQL(m_SetDeviceStatus_SQL); pSQL.setInt(1, status);
		 * pSQL.setStringExt(2, StringUtils.weave(dev_idList), false); String sql =
		 * pSQL.getSQL(); logger.debug("确认设备：" + sql); int length
		 * =DataSetBean.executeUpdate(sql); sql = null;
		 */
		// end delete by w5221 设备的状态更新由MasterControl来做了
		// 通知MC
		boolean result = true;
		if (deviceStatusStructArr.length <= 0)
		{
			result = false;
			msg = 0;
		}
		else
		{
			result = ACSManager.getInstance(account, passwd).ChangeDeviceStatus(
					deviceStatusStructArr);
			if (!result)
			{
				msg = 0;
			}
			else
			{
				msg = 1;
			}
		}
		return msg;
	}

	/**
	 * 获取确认设备信息
	 * 
	 * @param request
	 * @return Map
	 */
	public Map getConfirmDev(HttpServletRequest request)
	{
		String device_id = request.getParameter("device_id");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long m_AreaId = curUser.getUser().getAreaId();
		pSQL.setSQL(this.m_DeviceInfo_SQL);
		pSQL.setString(1, device_id);
		pSQL.setLong(2, m_AreaId);
		return DataSetBean.getRecord(pSQL.getSQL());
	}

	/**
	 * 增加,删除,编辑确认设备信息
	 * 
	 * @param request
	 * @return String
	 */
	public String ConfirmDevActExe(HttpServletRequest request)
	{
		String strSQL = "";
		String strMsg = "";
		String strAction = request.getParameter("action");
		if (strAction.equals("delete"))
		{ // 删除操作
			String str_oui = request.getParameter("oui");
			String str_device_serialnumber = request.getParameter("device_serialnumber");
			strSQL += "delete from tab_confirmdevice where oui='" + str_oui
					+ "' and device_serialnumber='" + str_device_serialnumber + "'";
		}
		else
		{
			String oui = request.getParameter("oui");
			String device_serialnumber = request.getParameter("device_serialnumber");
			String city_id = request.getParameter("city_id");
			if (strAction.equals("add"))
			{
				pSQL.setSQL(m_ConfirmDevAdd_SQL);
				pSQL.setString(1, oui);
				pSQL.setString(2, device_serialnumber);
				pSQL.setString(3, city_id);
				strSQL = pSQL.getSQL();
				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
			}
			else
			{
				String str_oui = request.getParameter("oui");
				String str_device_serialnumber = request
						.getParameter("device_serialnumber");
				strSQL = "update tab_confirmdevice set oui='" + oui
						+ "' ,device_serialnumber= " + device_serialnumber
						+ " , city_id='" + city_id + "' where oui='" + str_oui
						+ "' and device_serialnumber='" + str_device_serialnumber + "'";
				strSQL = strSQL.replaceAll("=,", "=null,");
				strSQL = strSQL.replaceAll("= where", "=null where");
			}
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		if (!strSQL.equals(""))
		{
			int iCode = DataSetBean.executeUpdate(strSQL);
			if (iCode > 0)
			{
				strMsg = "确认设备操作成功!";
			}
			else
			{
				strMsg = "确认设备操作失败!";
			}
		}
		return strMsg;
	}

	/**
	 * 取得所有确认设备的信息
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public ArrayList getConfirmDevCursor(HttpServletRequest request)
	{
		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();
		String oui = request.getParameter("oui");
		String device_serialnumber = request.getParameter("device_serialnumber");
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");
		m_ConfirmDev_List_SQL += " where 1 = 1";
		if (request.getParameter("submit") != null
				&& (oui != null || device_serialnumber != null))
		{
			oui = oui.trim();
			device_serialnumber = device_serialnumber.trim();
			if (oui.length() != 0)
			{
				m_ConfirmDev_List_SQL += " and oui = '" + oui + "' ";
			}
			if (device_serialnumber.length() != 0)
			{
				m_ConfirmDev_List_SQL += " and device_serialnumber like '%"
						+ device_serialnumber + "%' ";
			}
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(m_ConfirmDev_List_SQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(m_ConfirmDev_List_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_ConfirmDev_List_SQL, offset, pagelen);
		list.add(cursor);
		return list;
	}

	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap(String gw_type)
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		if ("4".equals(gw_type))
		{
			cursor = DataSetBean.getCursor(stb_m_Vendor_SQL);
		}
		else
		{
			cursor = DataSetBean.getCursor(m_Vendor_SQL);
		}
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				String ouiName = (String) fields.get("vendor_add");
				if (ouiName != null && !"".equals(ouiName))
				{
					ouiMap.put((String) fields.get("vendor_id"), ouiName);
				}
				else
				{
					ouiMap.put((String) fields.get("vendor_id"),
							(String) fields.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}

	/**
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap()
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		cursor = DataSetBean.getCursor(m_Vendor_SQL);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				String ouiName = (String) fields.get("vendor_add");
				if (ouiName != null && !"".equals(ouiName))
				{
					ouiMap.put((String) fields.get("vendor_id"), ouiName);
				}
				else
				{
					ouiMap.put((String) fields.get("vendor_id"),
							(String) fields.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}

	/**
	 * 取得所有oui和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIVendorMap()
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		String sql = "select b.oui,a.vendor_id,a.vendor_name,a.vendor_add "
				+ " from tab_vendor a, tab_vendor_oui b where a.vendor_id=b.vendor_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				String ouiName = (String) fields.get("vendor_add");
				if (ouiName != null && !"".equals(ouiName))
				{
					ouiMap.put((String) fields.get("oui"), ouiName);
				}
				else
				{
					ouiMap.put((String) fields.get("oui"),
							(String) fields.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}

	/**
	 * 获取设备型号版本map
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public Map getDeviceTypeMap()
	{
		String devicemodel = "";
		String softwareversion = "";
		String devicetype_id = "";
		Map deviceTypeMap = new HashMap();
		// Cursor cursorTmp =
		// DataSetBean.getCursor("select * from tab_devicetype_info");
		String sql = "select a.device_model_id,a.device_model,b.softwareversion,b.devicetype_id"
				+ " from gw_device_model a, tab_devicetype_info b where a.device_model_id=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursorTmp = DataSetBean
				.getCursor("select a.device_model_id,a.device_model,b.softwareversion,b.devicetype_id"
						+ " from gw_device_model a, tab_devicetype_info b where a.device_model_id=b.device_model_id");
		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null)
		{
			devicemodel = (String) fieldTmp.get("device_model");
			softwareversion = (String) fieldTmp.get("softwareversion");
			// 型号，软件版本数组，modelsoft[0]为型号，modelsoft[1]为软件版本
			String[] modelsoft = new String[2];
			modelsoft[0] = devicemodel;
			modelsoft[1] = softwareversion;
			devicetype_id = (String) fieldTmp.get("devicetype_id");
			deviceTypeMap.put(devicetype_id, modelsoft);
			fieldTmp = cursorTmp.getNext();
		}
		return deviceTypeMap;
	}

	/**
	 * 取得所有city_id和city_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getCityMap(HttpServletRequest request)
	{
		HashMap cityMap = new HashMap();
		cityMap.clear();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String[] cityid = city_id.split(",");
		List list = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			list.add(cityid[i]);
		}
		String sql = "select city_id,city_name from tab_city where parent_id in("
				+ StringUtils.weave(list) + ") or city_id in(" + StringUtils.weave(list)
				+ ") order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				cityMap.put((String) fields.get("city_id"),
						(String) fields.get("city_name"));
				fields = cursor.getNext();
			}
		}
		return cityMap;
	}

	/**
	 * 取得所有city_id和city_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getCityMap_All()
	{
		HashMap cityMap = new HashMap();
		cityMap.clear();
		String sql = "select city_id,city_name from tab_city order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				cityMap.put((String) fields.get("city_id"),
						(String) fields.get("city_name"));
				fields = cursor.getNext();
			}
		}
		return cityMap;
	}

	/**
	 * 取得所有user_id和usertype_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getUsertypeMap_All()
	{
		HashMap userypeMap = new HashMap();
		userypeMap.clear();
		userypeMap.put("1", "个人用户");
		userypeMap.put("2", "企业用户");
		userypeMap.put("3", "网吧用户");
		userypeMap.put("-1", "其它");
		return userypeMap;
	}

	/**
	 * 取得所有user_id和usertype_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getDeviceTypeMap_All()
	{
		HashMap deviceTypeMap = new HashMap();
		deviceTypeMap.clear();
		deviceTypeMap.put("1", "家庭网关设备");
		deviceTypeMap.put("2", "企业网关设备");
		deviceTypeMap.put("3", "");
		deviceTypeMap.put("-1", "其它");
		return deviceTypeMap;
	}

	/**
	 * 取得所有user_id和usertype_name名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public String getCounts_All()
	{
		String sql = "select city_id,city_name from tab_city order by city_id";
		return null;
	}

	/**
	 * Read File
	 * 
	 * @param fname
	 *            filename
	 * @return return List
	 */
	public List readFileRes(String fname)
	{
		String tempf = LipossGlobals.getLipossHome() + File.separator + "temp";
		String path = tempf + File.separator + fname;
		List list = new ArrayList();
		BufferedReader in=null;
		try
		{
			in = new BufferedReader(new FileReader(path));
			String line;
			while ((line = in.readLine()) != null)
			{
				list.add(line);
			}
			
			File f = new File(path);
			f.delete();
			f = null;
		}
		catch (IOException e)
		{
			logger.debug("处理文件出错" + e.getMessage());
		}finally{
			try {
				if(in!=null){
					in.close();
					in = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}

	/**
	 * 读cvs文件,生成批量SQL并入库
	 * 
	 * @param name
	 * @return -1:cvs文件中记录为0 -2:获取最大id异常 其他:入库成功数.
	 */
	public int doBatchFromCvs(HttpServletRequest request)
	{
		String name = request.getParameter("filename");
		String gw_type = request.getParameter("gw_type");
		logger.debug("gw_type-------------:" + gw_type);
		if (gw_type == null || "".equals(gw_type.trim()) || "null".equals(gw_type.trim()))
			gw_type = "1";
		List list = readFileRes(name);
		if (list.size() < 1)
			return -1;
		// 抛弃第一行
		list = list.subList(1, list.size());
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// WebCorbaInst corba = new WebCorbaInst("db");
		// 现在不调用MC了
		// String m_DeviceSerial = corba.getDeviceSerial(curUser.getUser(),
		// list.size());
		// corba = null;
		// int int_DeviceSerial = Integer.parseInt(m_DeviceSerial);
		int int_DeviceSerial = DeviceActUtil.getProMaxDeviceId(list.size());
		ArrayList listSQL = new ArrayList(list.size());
		DeviceResObject device = new DeviceResObject(curUser.getUser());
		device.setUpdate_Buy_Completetime(new Date().getTime() / 1000);
		Iterator itertor = list.iterator();
		String item = null;
		// add by zhaixf
		Cursor sor = DeviceActUtil.getDeviceList(gw_type);
		logger.debug("___cursor size:" + sor.getRecordSize());
		Map serialUserMap = DeviceActUtil.getAllUsername(gw_type);
		String username = "";
		String serialnumber = "";
		String oui = "";
		// 默认为家庭网关上网业务
		String servTypeId = ServerCode.HgwNetOpenServCode;
		if ("2".equals(gw_type))
		{
			servTypeId = ServerCode.EgwNetOpenServCode;
		}
		String tmpSQL = "";
		while (itertor.hasNext())
		{
			int_DeviceSerial++;
			item = (String) itertor.next();
			if (!device.setDataSource(item))
			{
				continue;
			}
			serialnumber = device.getDevice_serialnumber();
			oui = device.getOui();
			if (DeviceActUtil.isExit(serialnumber, sor))
			{
				continue;
			}
			device.setDeviceId(int_DeviceSerial);
			tmpSQL = device.getInsertSQL(gw_type);
			tmpSQL = tmpSQL.replace(",'',", ",,");
			tmpSQL = tmpSQL.replace(",'null',", ",,");
			tmpSQL = tmpSQL.replace(",'NULL',", ",,");
			listSQL.add(tmpSQL);
			username = DeviceActUtil.getUsername(device.getDevice_serialnumber(),
					serialUserMap);
			tmpSQL = DeviceActUtil.getAddUserDevSQL(username, oui, serialnumber,
					servTypeId);
			tmpSQL = tmpSQL.replace(",'',", ",,");
			tmpSQL = tmpSQL.replace(",'null',", ",,");
			tmpSQL = tmpSQL.replace(",'NULL',", ",,");
			listSQL.add(tmpSQL);
		}
		// Clear Resource
		device = null;
		itertor = null;
		list = null;
		serialUserMap = null;
		sor = null;
		int[] iCode = DataSetBean.doBatch(listSQL);
		if (iCode != null && iCode.length > 0)
		{
			return listSQL.size() / 2;
		}
		return 0;
	}

	/**
	 * 查询已开通特定业务的设备
	 * 
	 * @return
	 */
	public String getDeviceOfService()
	{
		String sql = "select a.service_name,b.oui,b.device_serialnumber "
				+ " from tab_hgwcustomer b,tab_service a"
				+ " on a.service_id = b.serv_type_id"
				+ " where a.service_name is not null";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strData = "";
		fields = cursor.getNext();
		if (fields != null)
		{
			while (fields != null)
			{
				strData += "<tr><td class=column>" + fields.get("service_name") + "</td>";
				strData += "<td class=column>" + fields.get("oui") + "</td>";
				strData += "<td class=column>" + fields.get("device_serialnumber")
						+ "</td></tr>";
				fields = cursor.getNext();
			}
		}
		else
		{
			strData += "<tr><td colspan=3 class=column>查询无数据!</td></tr>";
		}
		return strData;
	}

	/**
	 * 按属地统计设备数量
	 * 
	 * @return
	 */
	public Map getDeviceCounOfCity(HttpServletRequest request)
	{
		PrepareSQL psql = new PrepareSQL(m_DeviceNumOfCity);
		psql.getSQL();
		return DataSetBean.getMap(m_DeviceNumOfCity);
	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByVender(HttpServletRequest request)
	{
		// 1. 家庭网关设备 2.企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String sqlid = request.getParameter("sqlid");
		String fileter = "";
		if (gw_type_Str == null || gw_type_Str.equals(""))
		{
			gw_type_Str = "1";
		}
		// 修改
		String devicetype = request.getParameter("devicetype");
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter += "&gw_type=" + gw_type;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
		String use_state = request.getParameter("use_state");
		if (!StringUtil.IsEmpty(use_state))
		{
			session.setAttribute("use_state", use_state);
		}
		else
		{
			use_state = (String) session.getAttribute("use_state");
		}
		// long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;
		if (4 == gw_type)
		{
			if(Global.HBLT.equals(Global.instAreaShortName))
			{
				sqlDevice = "select * from stb_tab_gw_device a,stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status=1";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
							"from stb_tab_gw_device a,stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status=1";
				}
			}
			else
			{
				sqlDevice = "select * from stb_tab_gw_device a,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 ";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
							" from stb_tab_gw_device a,stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 ";
				}
			}
		}	
		else
		{
			sqlDevice = "select * from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
						" from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null and a.gw_type="
						+ gw_type;
			}
		}
		// 查找设备条件
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_device_model_id = request.getParameter("deviceModel_id");
		String str_city_id = request.getParameter("city_id");
		String cityIdSelect = request.getParameter("cityIdSelect");
		String deviceTypeId = request.getParameter("devicetype");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		fileter += "&" + str_city_id;
		String startTime = request.getParameter("startTime");
		if (null == startTime || "null".equals(startTime))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if (null == endTime || "null".equals(endTime))
		{
			endTime = "";
		}
		String recentStartTime = request.getParameter("recentStartTime");
		if (null == recentStartTime || "null".equals(recentStartTime))
		{
			recentStartTime = "";
		}
		String recentEndTime = request.getParameter("recentEndTime");
		if (null == recentEndTime || "null".equals(recentEndTime))
		{
			recentEndTime = "";
		}
		if (null != isBind && !"null".equals(isBind) && !"undefined".equals(isBind)
				&& !"0".equals(isBind) && !"".equals(isBind) && !sqlid.equals("1"))
		{
			sqlDevice += " and a.customer_id is not null ";
			fileter += "&isBind=" + isBind;
		}
		if(deviceTypeId != null && !deviceTypeId.equals("-1")
				&& !deviceTypeId.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + deviceTypeId;
			fileter += "&devicetype_id=" + deviceTypeId;
		}
		else if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			fileter += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals(""))
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter += "&vendor_id=" + str_vendor_id;
		}
		if (str_device_model_id != null && !str_device_model_id.equals("-1")
				&& !str_device_model_id.equals(""))
		{
			sqlDevice += " and a.device_model_id ='" + str_device_model_id + "' ";
			fileter += "&device_model_id=" + str_device_model_id;
		}
		if (null != startTime && !"".equals(startTime))
		{
			sqlDevice += " and a.complete_time >= " + startTime;
			fileter += "&startTime=" + startTime;
		}
		if (null != endTime && !"".equals(endTime))
		{
			sqlDevice += " and a.complete_time <= " + endTime;
			fileter += "&endTime=" + endTime;
		}
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			logger.warn("recentStartTime : " + recentStartTime + " " + "recentEndTime : " + recentEndTime);
			if (null != recentStartTime && !"".equals(recentStartTime))
			{
				sqlDevice += " and b.last_time >= " + recentStartTime;
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (null != recentEndTime && !"".equals(recentEndTime))
			{
				sqlDevice += " and b.last_time <= " + recentEndTime;
				fileter += "&recentEndTime=" + recentEndTime;
			}
		}
		String is_normal = request.getParameter("is_normal");
		if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal))
		{
			sqlDevice += " and t.is_normal = " + is_normal;
			fileter += "&is_normal=" + is_normal;
		}
		// 在统计页面，省中心只是统计省中心，没有统计子区域，故该详情页面，也只统计省中心，不统计子区域
		String type = curUser.getCityId();
		if ("00".equals(str_city_id))
		{
			sqlDevice += " and a.city_id = '00' ";
			fileter += "&city_id=" + str_city_id;
		}
		else
		{
			String city_temp = str_city_id;
			if (Global.HBLT.equals(Global.instAreaShortName)
					&& gw_type == 4)
			{
				String citynext = request.getParameter("citynext");
				if ("-1".equals(str_city_id))
				{
					if(!"-1".equals(citynext) && !"00".equals(citynext))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(citynext);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else if(!"-1".equals(cityIdSelect) && !"00".equals(cityIdSelect))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityIdSelect);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid("00");
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
				}
				else
				{
					// hb_lt除了省中心外不选二级属地时其余均展示所有信息，选了二级属地时展示具体信息
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)) && ("-1".equals(citynext) || "00".equals(citynext)))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(city_temp);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(city_temp)
								+ "') ";
					}
				}
				fileter += "&cityIdSelect=" + cityIdSelect;
				fileter += "&citynext=" + citynext;
			}
			else
			{
				if ("-1".equals(str_city_id))
				{
					// 如果为-1，则展示当前用户地市下所有设备信息
					str_city_id = curUser.getCityId();
					List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
					sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
				}
				else if (str_city_id.length() == 3)
				{
					if ("00".equals(type))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList)
								+ ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + str_city_id + "') ";
					}
				}
				else
				{
					// str_city_id = curUser.getCityId();
					sqlDevice += " and a.city_id in (" + str_city_id + ") ";
					city_temp = "-1";
				}
			}
			fileter += "&city_id=" + city_temp;
		}
		fileter += "&city_id=" + str_city_id;
		logger.warn(" 统计 详细 SQL :[{}]", sqlDevice);
	    String stroffset = request.getParameter("offset");
	    int pagelen = 50;
	    int offset;
	    if (stroffset == null) {
	      offset = 1;
	    } else {
	      offset = Integer.parseInt(stroffset);
	    }
	    QueryPage qryp = new QueryPage();
	    qryp.initPage(sqlDevice, offset, pagelen);
	    PrepareSQL psql = new PrepareSQL(sqlDevice);
	    psql.getSQL();
	    this.cursor = DataSetBean.getCursor(
	      sqlDevice, 
	      offset, 
	      pagelen, 
	      DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
	      getClass().getName()));
	    String strBar = qryp.getPageBar(fileter);
	    list.add(strBar);
	    list.add(this.cursor);
	    return list;
	}
	
	
	
	/**
	 * 读取设备资源表,获取开通IPV6的设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByV6Status(HttpServletRequest request)
	{
		// 1. 家庭网关设备 2.企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String sqlid = request.getParameter("sqlid");
		String fileter = "";
		if (gw_type_Str == null || gw_type_Str.equals(""))
		{
			gw_type_Str = "1";
		}
		// 修改
		String devicetype = request.getParameter("devicetype");
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter += "&gw_type=" + gw_type;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		// long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;
		
		sqlDevice = "select * from tab_gw_device a,tab_devicetype_info t ,tab_net_serv_param d,tab_hgwcustomer e ,hgwcust_serv_info h "
				+"where e.device_id = a.device_id and e.user_id = d.user_id and d.ip_type = 3 and a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null "
				+" and h.serv_type_id = 10 and h.open_status = 1 and h.user_id =e.user_id and a.gw_type="
					+ gw_type;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
					" from tab_gw_device a,tab_devicetype_info t ,tab_net_serv_param d,tab_hgwcustomer e ,hgwcust_serv_info h "
					+"where e.device_id = a.device_id and e.user_id = d.user_id and d.ip_type = 3 and a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null "
					+" and h.serv_type_id = 10 and h.open_status = 1 and h.user_id =e.user_id and a.gw_type="
					+ gw_type;
		}

		// 查找设备条件
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_device_model_id = request.getParameter("deviceModel_id");
		String str_city_id = request.getParameter("city_id");
		String cityIdSelect = request.getParameter("cityIdSelect");
		String deviceTypeId = request.getParameter("devicetype");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		fileter += "&" + str_city_id;
		String startTime = request.getParameter("startTime");
		if (null == startTime || "null".equals(startTime))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if (null == endTime || "null".equals(endTime))
		{
			endTime = "";
		}
		String recentStartTime = request.getParameter("recentStartTime");
		if (null == recentStartTime || "null".equals(recentStartTime))
		{
			recentStartTime = "";
		}
		String recentEndTime = request.getParameter("recentEndTime");
		if (null == recentEndTime || "null".equals(recentEndTime))
		{
			recentEndTime = "";
		}
		if (null != isBind && !"null".equals(isBind) && !"undefined".equals(isBind)
				&& !"0".equals(isBind) && !"".equals(isBind) && !sqlid.equals("1"))
		{
			sqlDevice += " and a.customer_id is not null ";
			fileter += "&isBind=" + isBind;
		}
		if(deviceTypeId != null && !deviceTypeId.equals("-1")
				&& !deviceTypeId.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + deviceTypeId;
			fileter += "&devicetype_id=" + deviceTypeId;
		}
		else if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			fileter += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals(""))
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter += "&vendor_id=" + str_vendor_id;
		}
		if (str_device_model_id != null && !str_device_model_id.equals("-1")
				&& !str_device_model_id.equals(""))
		{
			sqlDevice += " and a.device_model_id ='" + str_device_model_id + "' ";
			fileter += "&device_model_id=" + str_device_model_id;
		}
		if (null != startTime && !"".equals(startTime))
		{
			sqlDevice += " and a.complete_time >= " + startTime;
			fileter += "&startTime=" + startTime;
		}
		if (null != endTime && !"".equals(endTime))
		{
			sqlDevice += " and a.complete_time <= " + endTime;
			fileter += "&endTime=" + endTime;
		}
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			logger.warn("recentStartTime : " + recentStartTime + " " + "recentEndTime : " + recentEndTime);
			if (null != recentStartTime && !"".equals(recentStartTime))
			{
				sqlDevice += " and b.last_time >= " + recentStartTime;
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (null != recentEndTime && !"".equals(recentEndTime))
			{
				sqlDevice += " and b.last_time <= " + recentEndTime;
				fileter += "&recentEndTime=" + recentEndTime;
			}
		}
		String is_normal = request.getParameter("is_normal");
		if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal))
		{
			sqlDevice += " and t.is_normal = " + is_normal;
			fileter += "&is_normal=" + is_normal;
		}
		// 在统计页面，省中心只是统计省中心，没有统计子区域，故该详情页面，也只统计省中心，不统计子区域
		String type = curUser.getCityId();
		if ("00".equals(str_city_id))
		{
			sqlDevice += " and a.city_id = '00' ";
			fileter += "&city_id=" + str_city_id;
		}
		else
		{
			String city_temp = str_city_id;
			if (Global.HBLT.equals(Global.instAreaShortName)
					&& gw_type == 1)
			{
				String citynext = request.getParameter("citynext");
				if ("-1".equals(str_city_id))
				{
					if(!"-1".equals(citynext) && !"00".equals(citynext))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(citynext);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else if(!"-1".equals(cityIdSelect) && !"00".equals(cityIdSelect))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityIdSelect);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid("00");
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
				}
				else
				{
					// hb_lt除了省中心外不选二级属地时其余均展示所有信息，选了二级属地时展示具体信息
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)) && ("-1".equals(citynext) || "00".equals(citynext)))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(city_temp);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(city_temp)
								+ "') ";
					}
				}
				fileter += "&cityIdSelect=" + cityIdSelect;
				fileter += "&citynext=" + citynext;
			}
			else
			{
				if ("-1".equals(str_city_id))
				{
					// 如果为-1，则展示当前用户地市下所有设备信息
					str_city_id = curUser.getCityId();
					List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
					sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
				}
				else if (str_city_id.length() == 3)
				{
					if ("00".equals(type))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList)
								+ ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + str_city_id + "') ";
					}
				}
				else
				{
					// str_city_id = curUser.getCityId();
					sqlDevice += " and a.city_id in (" + str_city_id + ") ";
					city_temp = "-1";
				}
			}
			fileter += "&city_id=" + city_temp;
		}
		fileter += "&city_id=" + str_city_id;
		logger.warn(" 统计 详细 SQL :[{}]", sqlDevice);
	    String stroffset = request.getParameter("offset");
	    int pagelen = 50;
	    int offset;
	    if (stroffset == null) {
	      offset = 1;
	    } else {
	      offset = Integer.parseInt(stroffset);
	    }
	    QueryPage qryp = new QueryPage();
	    qryp.initPage(sqlDevice, offset, pagelen);
	    PrepareSQL psql = new PrepareSQL(sqlDevice);
	    psql.getSQL();
	    this.cursor = DataSetBean.getCursor(
	      sqlDevice, 
	      offset, 
	      pagelen, 
	      DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
	      getClass().getName()));
	    String strBar = qryp.getPageBar(fileter);
	    list.add(strBar);
	    list.add(this.cursor);
	    return list;
	}

	/**
	 * 读取设备资源表,获取设备信息 导出Excel
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceListByVenderExcel(HttpServletRequest request)
	{
		// 1. 家庭网关设备 2.企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String rela_dev_type_id = request.getParameter("rela_dev_type_id");
		String fileter = "";
		if (gw_type_Str == null || gw_type_Str.equals(""))
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter += "&gw_type=" + gw_type;
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
		String use_state = request.getParameter("use_state");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (StringUtil.IsEmpty(use_state))
		{
			use_state = (String) session.getAttribute("use_state");
		}
		// long areaid = curUser.getAreaId();
		String startTime = request.getParameter("startTime");
		if (null == startTime || "null".equals(startTime))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if (null == endTime || "null".equals(endTime))
		{
			endTime = "";
		}
		String recentStartTime = request.getParameter("recentStartTime");
		if (null == recentStartTime || "null".equals(recentStartTime))
		{
			recentStartTime = "";
		}
		String recentEndTime = request.getParameter("recentEndTime");
		if (null == recentEndTime || "null".equals(recentEndTime))
		{
			recentEndTime = "";
		}
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;
		if (gw_type == 4)
		{
			sqlDevice = "select * from stb_tab_gw_device a,stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status=1 and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
						" from stb_tab_gw_device a,stb_gw_devicestatus b, stb_tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_id=b.device_id and a.device_status=1 and a.gw_type="
						+ gw_type;
			}
		}
		else
		{
			sqlDevice = "select * from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.gw_type="
						+ gw_type;
			}
		}
		// 查找设备条件
		String str_devicetype_id = request.getParameter("devicetype_id");
		// 修改
		String devicetype = request.getParameter("devicetype");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String cityId = request.getParameter("city_id");// 修改处
		String cityIdSelect = request.getParameter("cityIdSelect");
		String citynext = request.getParameter("citynext");
		String str_deviceModelId = request.getParameter("deviceModelId");
		String is_normal = request.getParameter("is_normal");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal)
				&& !"null".equals(is_normal))
		{
			sqlDevice += " and t.is_normal =" + is_normal;
			fileter += "&is_normal=" + is_normal;
		}
		if ((rela_dev_type_id != null) && (!rela_dev_type_id.equals("-1"))
				&& (!rela_dev_type_id.equals("")) && (!rela_dev_type_id.equals("null")))
		{
			sqlDevice = sqlDevice + " and t.rela_dev_type_id =" + rela_dev_type_id;
			fileter = fileter + "&rela_dev_type_id=" + rela_dev_type_id;
		}
		if(devicetype != null && !devicetype.equals("-1")
				&& !devicetype.equals(""))
		{
			sqlDevice += " and a.devicetype_id =" + devicetype;
			fileter += "&devicetype_id=" + devicetype;
		}
		else if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals("") && !str_devicetype_id.equals("null"))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			fileter += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals("") && !str_vendor_id.equals("null"))// 修
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter += "&vendor_id=" + str_vendor_id;
		}
		if (null != startTime && !"".equals(startTime))
		{
			sqlDevice += " and a.complete_time >= " + startTime;
			fileter += "&startTime=" + startTime;
		}
		if (null != endTime && !"".equals(endTime))
		{
			sqlDevice += " and a.complete_time <= " + endTime;
			fileter += "&endTime=" + endTime;
		}
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			logger.warn("recentStartTime : " + recentStartTime + " " + "recentEndTime : " + recentEndTime);
			if (null != recentStartTime && !"".equals(recentStartTime))
			{
				sqlDevice += " and b.last_time >= " + recentStartTime;
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (null != recentEndTime && !"".equals(recentEndTime))
			{
				sqlDevice += " and b.last_time <= " + recentEndTime;
				fileter += "&recentEndTime=" + recentEndTime;
			}
		}
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& gw_type == 4)
		{
			// hb_lt除了省中心外其余均展示所有信息
			if ("00".equals(str_city_id))
			{
				sqlDevice += " and a.city_id = '00' ";
			}
			else{
				if ("-1".equals(str_city_id))
				{
					if(!"-1".equals(citynext) && !"00".equals(citynext))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(citynext);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else if(!"-1".equals(cityIdSelect) && !"00".equals(cityIdSelect))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityIdSelect);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid("00");
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
				}
				else
				{
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)) && ("-1".equals(citynext) || "00".equals(citynext)))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						logger.warn("cityId is ...cityId : " + cityId);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(cityId)
								+ "') ";
					}
				}
			}
		}
		else
		{
			if (null == str_city_id || "".equals(str_city_id))
			{
				str_city_id = curUser.getCityId();
			}
			if (str_city_id.equals(curUser.getCityId()))
			{
				sqlDevice += " and a.city_id = '" + str_city_id + "' ";
			}
			else
			{
				String type = curUser.getCityId();
				if ("00".equals(str_city_id))
				{
					sqlDevice += " and a.city_id = '00' ";
					fileter += "&city_id=" + str_city_id;
				}
				else
				{
					String city_temp = str_city_id;
					if ("-1".equals(str_city_id))
					{
						// 如果为-1，则展示当前用户地市下所有设备信息
						str_city_id = curUser.getCityId();
						List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList)
								+ ") ";
					}
					else if (str_city_id.length() == 3)
					{
						if ("00".equals(type))
						{
							List cityList = CityDAO
									.getAllNextCityIdsByCityPid(str_city_id);
							sqlDevice += " and a.city_id in ("
									+ StringUtils.weave(cityList) + ") ";
						}
						else
						{
							sqlDevice += " and a.city_id in ('" + str_city_id + "') ";
						}
					}
					else
					{
						// str_city_id = curUser.getCityId();
						sqlDevice += " and a.city_id in (" + str_city_id + ") ";
					}
				}
			}
		}
		fileter += "&city_id=" + str_city_id;
		// sqlDevice += " and device_status=1";}
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		return cursor;
	}

	/**
	 * 读取设备资源表,根据开通V6状态，获取设备信息 导出Excel
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getDeviceListByV6StatusExcel(HttpServletRequest request)
	{
		// 1. 家庭网关设备 2.企业网关设备
		String gw_type_Str = request.getParameter("gw_type");
		String rela_dev_type_id = request.getParameter("rela_dev_type_id");
		String fileter = "";
		if (gw_type_Str == null || gw_type_Str.equals(""))
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter += "&gw_type=" + gw_type;
		/* JLDX-REQ-20180606-JIANGHAO6-002 增加在用情况 */
		String use_state = request.getParameter("use_state");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		if (StringUtil.IsEmpty(use_state))
		{
			use_state = (String) session.getAttribute("use_state");
		}
		// long areaid = curUser.getAreaId();
		String startTime = request.getParameter("startTime");
		if (null == startTime || "null".equals(startTime))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if (null == endTime || "null".equals(endTime))
		{
			endTime = "";
		}
		String recentStartTime = request.getParameter("recentStartTime");
		if (null == recentStartTime || "null".equals(recentStartTime))
		{
			recentStartTime = "";
		}
		String recentEndTime = request.getParameter("recentEndTime");
		if (null == recentEndTime || "null".equals(recentEndTime))
		{
			recentEndTime = "";
		}
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice;
		
		sqlDevice = "select * from tab_gw_device a,tab_devicetype_info t ,tab_net_serv_param d,tab_hgwcustomer e ,hgwcust_serv_info h "
					+" where e.device_id = a.device_id and d.ip_type = 3  and e.user_id = d.user_id and  a.devicetype_id=t.devicetype_id "
					+" and h.user_id =e.user_id and h.serv_type_id = 10 and h.open_status = 1 and a.device_status=1 and a.gw_type="
					+ gw_type;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = "select devicetype_id, serv_account, cpe_mac, device_id, device_id_ex, city_id, vendor_id, device_serialnumber, area_id " +
					" from tab_gw_device a,tab_devicetype_info t ,tab_net_serv_param d,tab_hgwcustomer e ,hgwcust_serv_info h "
					+" where e.device_id = a.device_id and d.ip_type = 3  and e.user_id = d.user_id and  a.devicetype_id=t.devicetype_id "
					+" and h.user_id =e.user_id and h.serv_type_id = 10 and h.open_status = 1 and a.device_status=1 and a.gw_type="
					+ gw_type;
		}
		// 查找设备条件
		String str_devicetype_id = request.getParameter("devicetype_id");
		// 修改
		String devicetype = request.getParameter("devicetype");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String cityId = request.getParameter("city_id");// 修改处
		String cityIdSelect = request.getParameter("cityIdSelect");
		String citynext = request.getParameter("citynext");
		String str_deviceModelId = request.getParameter("deviceModelId");
		String is_normal = request.getParameter("is_normal");
		String isBind = StringUtil.getStringValue(request.getParameter("isBind"));
		if (null != is_normal && !"".equals(is_normal) && !"-1".equals(is_normal)
				&& !"null".equals(is_normal))
		{
			sqlDevice += " and t.is_normal =" + is_normal;
			fileter += "&is_normal=" + is_normal;
		}
		if ((rela_dev_type_id != null) && (!rela_dev_type_id.equals("-1"))
				&& (!rela_dev_type_id.equals("")) && (!rela_dev_type_id.equals("null")))
		{
			sqlDevice = sqlDevice + " and t.rela_dev_type_id =" + rela_dev_type_id;
			fileter = fileter + "&rela_dev_type_id=" + rela_dev_type_id;
		}
		if(devicetype != null && !devicetype.equals("-1")
				&& !devicetype.equals("") && !devicetype.equals("null"))
		{
			sqlDevice += " and a.devicetype_id =" + devicetype;
			fileter += "&devicetype_id=" + devicetype;
		}
		else if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals("") && !str_devicetype_id.equals("null"))
		{
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;
			fileter += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1")
				&& !str_vendor_id.equals("") && !str_vendor_id.equals("null"))// 修
		{
			sqlDevice += " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter += "&vendor_id=" + str_vendor_id;
		}
		if (null != startTime && !"".equals(startTime))
		{
			sqlDevice += " and a.complete_time >= " + startTime;
			fileter += "&startTime=" + startTime;
		}
		if (null != endTime && !"".equals(endTime))
		{
			sqlDevice += " and a.complete_time <= " + endTime;
			fileter += "&endTime=" + endTime;
		}
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			logger.warn("recentStartTime : " + recentStartTime + " " + "recentEndTime : " + recentEndTime);
			if (null != recentStartTime && !"".equals(recentStartTime))
			{
				sqlDevice += " and b.last_time >= " + recentStartTime;
				fileter += "&recentStartTime=" + recentStartTime;
			}
			if (null != recentEndTime && !"".equals(recentEndTime))
			{
				sqlDevice += " and b.last_time <= " + recentEndTime;
				fileter += "&recentEndTime=" + recentEndTime;
			}
		}
		if (Global.HBLT.equals(Global.instAreaShortName)
				&& gw_type == 1)
		{
			// hb_lt除了省中心外其余均展示所有信息
			if ("00".equals(str_city_id))
			{
				sqlDevice += " and a.city_id = '00' ";
			}
			else{
				if ("-1".equals(str_city_id))
				{
					if(!"-1".equals(citynext) && !"00".equals(citynext))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(citynext);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else if(!"-1".equals(cityIdSelect) && !"00".equals(cityIdSelect))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityIdSelect);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid("00");
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
				}
				else
				{
					if(("-1".equals(cityIdSelect) || "00".equals(cityIdSelect)) && ("-1".equals(citynext) || "00".equals(citynext)))
					{
						List cityList = CityDAO.getAllNextCityIdsByCityPid(cityId);
						logger.warn("cityId is ...cityId : " + cityId);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList) + ") ";
					}
					else
					{
						sqlDevice += " and a.city_id in ('" + StringUtil.getStringValue(cityId)
								+ "') ";
					}
				}
			}
		}
		else
		{
			if (null == str_city_id || "".equals(str_city_id))
			{
				str_city_id = curUser.getCityId();
			}
			if (str_city_id.equals(curUser.getCityId()))
			{
				sqlDevice += " and a.city_id = '" + str_city_id + "' ";
			}
			else
			{
				String type = curUser.getCityId();
				if ("00".equals(str_city_id))
				{
					sqlDevice += " and a.city_id = '00' ";
					fileter += "&city_id=" + str_city_id;
				}
				else
				{
					String city_temp = str_city_id;
					if ("-1".equals(str_city_id))
					{
						// 如果为-1，则展示当前用户地市下所有设备信息
						str_city_id = curUser.getCityId();
						List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
						sqlDevice += " and a.city_id in (" + StringUtils.weave(cityList)
								+ ") ";
					}
					else if (str_city_id.length() == 3)
					{
						if ("00".equals(type))
						{
							List cityList = CityDAO
									.getAllNextCityIdsByCityPid(str_city_id);
							sqlDevice += " and a.city_id in ("
									+ StringUtils.weave(cityList) + ") ";
						}
						else
						{
							sqlDevice += " and a.city_id in ('" + str_city_id + "') ";
						}
					}
					else
					{
						// str_city_id = curUser.getCityId();
						sqlDevice += " and a.city_id in (" + str_city_id + ") ";
					}
				}
			}
		}
		fileter += "&city_id=" + str_city_id;
		// sqlDevice += " and device_status=1";}
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		return cursor;
	}
	
	/**
	 * 设备状态监控
	 * 
	 * @param request
	 * @return
	 */
	public static List getDevicestatus(HttpServletRequest request)
	{
		List list = new ArrayList();
		// 设备域名
		String loopback_ip = request.getParameter("loopback_ip");
		// 设备序列号
		String device_ser = request.getParameter("device_ser");
		// 属地
		String city_id = request.getParameter("city_id");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String para = "";
		String sql = "select * from tab_gw_device";
		// 如果是管理员，则不需要权限过滤
		if (!curUser.getUser().isAdmin())
		{
			sql += " and device_id in (select res_id from tab_gw_res_area where area_id="
					+ curUser.getAreaId() + ")";
		}
		if (loopback_ip != null && !loopback_ip.equals(""))
		{
			sql += " and a.loopback_ip like '%" + loopback_ip + "%' ";
			para = "&loopback_ip=" + loopback_ip;
		}
		if (device_ser != null && !device_ser.equals(""))
		{
			if (device_ser.length() > 5)
			{
				sql += " and a.dev_sub_sn ='"
						+ device_ser.substring(device_ser.length() - 6,
								device_ser.length()) + "'";
			}
			sql += " and a.device_serialnumber like'%" + device_ser + "' ";
			para += "&device_ser=" + device_ser;
		}
		if (city_id != null && !city_id.equals("-1") && !city_id.equals(""))
		{
			sql += " and a.city_id='" + city_id + "' ";
			para += "&city_id=" + city_id;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		String strBar = qryp.getPageBar(para);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 设备信息数据的初始同步
	 * 
	 * @param gw_type
	 * @return
	 */
	public Cursor getBatchDeviceInfo(int gw_type)
	{
		// gw_type 1:家庭网关设备 2:企业网关设备
		ArrayList list = new ArrayList();
		list.clear();
		String sql = "select d.device_id, d.device_type, d.oui, d.device_serialnumber, d.device_status, h.phonenumber, a.area_id "
				+ " from tab_gw_device d, tab_hgwcustomer h, tab_gw_res_area a "
				+ " where gw_type="
				+ gw_type
				+ " and h.device_serialnumber=d.device_serialnumber "
				+ " and a.res_id=d.device_id ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	/**
	 * 域信息数据的初始同步
	 * 
	 * @param gw_type
	 * @return
	 */
	public Cursor getBatchAreaInfo(int res_type)
	{
		// res_type 1：TR069 GW
		ArrayList list = new ArrayList();
		list.clear();
		String sql = "select a.area_id, d.device_serialnumber "
				+ " from tab_gw_res_area a, tab_gw_device d " + " where a.res_type="
				+ res_type + " and a.res_id=d.device_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	/**
	 * 取得当前用户的属地及下属属地
	 * 
	 * @author zhaixf
	 * @date 2008-1-26
	 * @param flag
	 * @param pos
	 * @param rename
	 * @param request
	 * @return
	 */
	public String getCityListAll(boolean flag, String pos, String rename,
			HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String city_id = curUser.getCityId();
		String[] cityid = city_id.split(",");
		List list = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			list.add(cityid[i]);
		}
		String sql = "select city_id,parent_id from tab_city where city_id in ("
				+ StringUtils.weave(list) + ")";
		Map map = DataSetBean.getMap(sql);
		String city = getcity(map, city_id);
		if (city.equals("-1"))
			sql = "select city_id,city_name from tab_city";
		else
			sql = "select city_id,city_name from tab_city where parent_id in ("
					+ StringUtils.weave(list) + ") or city_id in("
					+ StringUtils.weave(list) + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		String strCityList = FormUtil.createListBox(cursor, "city_id", "city_name", flag,
				pos, rename);
		return strCityList;
	}

	public String getcity(Map map, String city_id)
	{
		String[] cityid = city_id.split(",");
		String city = "";
		for (int i = 0; i < cityid.length; i++)
		{
			if (cityid[i].equals("00"))
			{
				city = "-1";
				break;
			}
			else
			{
				city = "1";
			}
		}
		return city;
	}

	/**
	 * @param oui
	 * @param device_serialnumber
	 * @return
	 */
	public String getIPTVaccounts(String serv_type_id, String oui,
			String device_serialnumber)
	{
		String iptv_userAccount = "";
		String sql = "select b.username from tab_gw_device a, tab_hgwcustomer b  where a.oui='"
				+ oui
				+ "' and a.device_serialnumber ='"
				+ device_serialnumber
				+ "' and a.device_id = b.device_id and b.serv_type_id !=" + serv_type_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map fileds1 = DataSetBean.getRecord(sql);
		if (null != fileds1)
		{
			iptv_userAccount = (String) fileds1.get("username");
		}
		return iptv_userAccount;
	}

	/**
	 * 根据oui、device_model获取设备的device_model_id（调用存储过程，若不存在则添加一条新纪录）
	 * 
	 * @param oui
	 * @param device_model
	 * @return 有则返回id、无则返回-1
	 */
	public static String getDeviceModelID(String oui, String device_model)
	{
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			return ZooCuratorLockUtil.getInstance().getTgwModelId(oui, device_model, "1");
		}else {
			String callable = "getTgwModelIdProc '" + oui + "','" + device_model + "'";
			String model_id = DataSetBean.executeCall(callable);
			logger.debug("getTgwModelIdProc==" + callable);
			logger.debug("getTgwModelIdProc==" + model_id);
			if (model_id != null && !"".equals(model_id))
			{
				return model_id;
			}
			else
			{
				return "-1";
			}
		}
	}

	/**
	 * 根据oui、device_model、softWareVersion获取设备的devicetype_id（调用存储过程，若不存在则添加一条新纪录）
	 * 
	 * @param oui
	 * @param device_model
	 * @param softWareVersion
	 * @return 有则返回id、无则返回-1
	 */
	public static String getDeviceTypeID(String oui, String manufacturer,
			String device_model, String specversion, String hardwareversion,
			String softwareversion)
	{
		
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			return ZooCuratorLockUtil.getInstance().getTgwModelTypeId(manufacturer, oui, 
					device_model, softwareversion, hardwareversion, specversion);
		}else {	
			String callable = "getTgwModelTypeIdProc '" + oui + "','" + manufacturer + "','"
					+ device_model + "','" + specversion + "','" + hardwareversion + "','"
					+ softwareversion + "'";
			String devicetype_id = DataSetBean.executeCall(callable);
			logger.debug("getTgwModelTypeIdProc==" + callable);
			logger.debug("getTgwModelTypeIdProc==" + devicetype_id);
			if (devicetype_id != null && !"".equals(devicetype_id))
			{
				return devicetype_id;
			}
			else
			{
				return "-1";
			}
		}
	}

	/**
	 * 根据devicetype_id获取设备的device_model_id
	 * 
	 * @param devicetype_id
	 * @return
	 */
	public static String getModelIdByType(String devicetype_id)
	{
		String sql = "select a.device_model_id from gw_device_model a, tab_devicetype_info b "
				// + " where a.oui=b.oui and a.device_model=b.device_model " +
				// " and b.devicetype_id="
				+ " where a.device_model_id=b.device_model_id "
				+ " and b.devicetype_id="
				+ devicetype_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map fields = DataSetBean.getRecord(sql);
		if (fields != null)
		{
			return fields.get("device_model_id").toString();
		}
		else
		{
			return "-1";
		}
	}

	/**
	 * 获取设备的编辑日志信息
	 * 
	 * @param request
	 * @return
	 */
	private String getDevLogInfo(HttpServletRequest request)
	{
		String msg = "编辑设备：";
		OfficeAct office = new OfficeAct();
		ZoneAct zone = new ZoneAct();
		// 设备序列号
		String device_serialnumber = request.getParameter("device_serialnumber");
		String old_device_serialnumber = request.getParameter("old_device_serialnumber");
		if (!old_device_serialnumber.equals(device_serialnumber))
		{
			msg += "修改设备序列号为" + device_serialnumber + "，原序列号为" + old_device_serialnumber
					+ "；";
		}
		// 设备名称
		String device_name = request.getParameter("device_name");
		String old_device_name = request.getParameter("old_device_name");
		if (!old_device_name.equals(device_name))
		{
			msg += "修改设备名称为" + device_name + "，原设备名称为" + old_device_name + "；";
		}
		// 属地
		String city_id = request.getParameter("city_id");
		String old_city_id = request.getParameter("old_city_id");
		if (!old_city_id.equals(city_id))
		{
			Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
			msg += "修改设备属地为" + cityMap.get(city_id) + "，原属地为" + cityMap.get(old_city_id)
					+ "；";
			cityMap = null;
		}
		// 局向
		String office_id = request.getParameter("office_id");
		String old_office_id = request.getParameter("old_office_id");
		if (!old_office_id.equals(office_id))
		{
			msg += "修改设备局向为" + office.getOfficeInfo(office_id).get("office_name")
					+ "，原局向为" + office.getOfficeInfo(old_office_id).get("office_name")
					+ "；";
		}
		// 小区
		String zone_id = request.getParameter("zone_id");
		String old_zone_id = request.getParameter("old_zone_id");
		if (!old_zone_id.equals(zone_id))
		{
			msg += "修改设备小区为" + zone.getZoneInfo(zone_id).get("zone_name") + "，原小区为"
					+ zone.getZoneInfo(old_zone_id).get("zone_name") + "；";
		}
		// 厂商
		String vendor_id = request.getParameter("vendor_id");
		String old_vendor_id = request.getParameter("old_vendor_id");
		if (!old_vendor_id.equals(vendor_id))
		{
			msg += "修改设备厂商为" + getVendorname(vendor_id) + "，原厂商为"
					+ getVendorname(old_vendor_id) + "；";
		}
		// 设备型号
		String devicetype_id = request.getParameter("devicetype_id");
		String old_devicetype_id = request.getParameter("old_devicetype_id");
		if (!old_devicetype_id.equals(devicetype_id))
		{
			msg += "修改设备型号为" + getTypename(devicetype_id) + "，原型号为"
					+ getTypename(old_devicetype_id) + "；";
		}
		// 协议版本
		String snmp_version = request.getParameter("snmp_version");
		String old_snmp_version = request.getParameter("old_snmp_version");
		if (!old_snmp_version.equals(snmp_version))
		{
			msg += "修改设备协议版本为" + snmp_version + "，原协议版本为" + old_snmp_version + "；";
		}
		// 设备类型
		String device_type = request.getParameter("device_type");
		String old_device_type = request.getParameter("old_device_type");
		if (!old_device_type.equals(device_type))
		{
			msg += "修改设备类型为" + device_type + "，原设备类型为" + old_device_type + "；";
		}
		// SNMP读口令
		String snmp_r_passwd = request.getParameter("snmp_r_passwd");
		String old_snmp_r_passwd = request.getParameter("old_snmp_r_passwd");
		if (!old_snmp_r_passwd.equals(snmp_r_passwd))
		{
			msg += "修改设备SNMP读口令为" + snmp_r_passwd + "，原SNMP读口令为" + old_snmp_r_passwd
					+ "；";
		}
		// SNMP写口令
		String snmp_w_passwd = request.getParameter("snmp_w_passwd");
		String old_snmp_w_passwd = request.getParameter("old_snmp_w_passwd");
		if (!old_snmp_w_passwd.equals(snmp_w_passwd))
		{
			msg += "修改设备SNMP写口令为" + snmp_w_passwd + "，原SNMP写口令为" + old_snmp_w_passwd
					+ "；";
		}
		// SNMP端口
		String snmp_udp = request.getParameter("snmp_udp");
		String old_snmp_udp = request.getParameter("old_snmp_udp");
		if (!old_snmp_udp.equals(snmp_udp))
		{
			msg += "修改设备SNMP端口为" + snmp_udp + "，原SNMP端口为" + old_snmp_udp + "；";
		}
		// 端口
		String port = request.getParameter("cr_port");
		String old_port = request.getParameter("old_port");
		if (!old_port.equals(port))
		{
			msg += "修改设备端口为" + port + "，原端口为" + old_port + "；";
		}
		// 设备ip
		String old_loopback_ip = request.getParameter("old_loopback_ip");
		String loopback_ip = request.getParameter("loopback_ip");
		if (!old_loopback_ip.equals(loopback_ip))
		{
			msg += "修改设备ip为" + loopback_ip + "，原ip为" + old_loopback_ip + "；";
		}
		// 设备MAC码
		String cpe_mac = request.getParameter("cpe_mac");
		String old_cpe_mac = request.getParameter("old_cpe_mac");
		if (!old_cpe_mac.equals(cpe_mac))
		{
			msg += "修改设备MAC码为" + cpe_mac + "，原设备MAC码为" + old_cpe_mac + "；";
		}
		// 连接请求对应的路径
		String path = request.getParameter("cr_path");
		String old_path = request.getParameter("old_path");
		if (!old_path.equals(path))
		{
			msg += "修改设备连接请求对应的路径为" + path + "，原路径为" + old_path + "；";
		}
		// 设备详细地址
		String device_addr = request.getParameter("device_addr");
		String old_device_addr = request.getParameter("old_device_addr");
		if (!old_device_addr.equals(device_addr))
		{
			msg += "修改设备详细地址为" + device_addr + "，原地址为" + old_device_addr + "；";
		}
		// 维保年限
		String service_year = request.getParameter("service_year");
		String old_service_year = request.getParameter("old_service_year");
		if (!old_service_year.equals(service_year))
		{
			msg += "修改设备维保年限为" + service_year + "年，原维保年限为" + old_service_year + "年" + "；";
		}
		logger.debug("==============" + msg);
		return msg;
	}

	/**
	 * 根据厂商类型查询厂商名称
	 * 
	 * @param vendor_id
	 * @return
	 */
	private String getVendorname(String vendor_id)
	{
		PrepareSQL psql = new PrepareSQL(
				"select vendor_name from tab_vendor where vendor_id='" + vendor_id + "'");
		psql.getSQL();
		Cursor cursor = DataSetBean
				.getCursor("select vendor_name from tab_vendor where vendor_id='"
						+ vendor_id + "'");
		// Cursor cursor = DataSetBean
		// .getCursor("select a.vendor_name from tab_vendor a,tab_vendor_oui b where a.vendor_id=b.vendor_id and b.oui='"
		// + vendor_id + "'");
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return (String) fields.get("vendor_name");
		}
		else
		{
			return "";
		}
	}

	/**
	 * 根据设备类型id查询设备型号名称
	 * 
	 * @param device_type_id
	 * @return
	 */
	private String getTypename(String device_type_id)
	{
		// Cursor cursor = DataSetBean
		// .getCursor("select device_model from tab_devicetype_info where devicetype_id='"
		// + device_type_id + "'");
		if (null == device_type_id)
		{
			return "";
		}
		PrepareSQL psql = new PrepareSQL(
				"select device_model from tab_devicetype_info a,gw_device_model b where devicetype_id="
						+ device_type_id + " and a.device_model_id=b.device_model_id");
		psql.getSQL();
		Cursor cursor = DataSetBean
				.getCursor("select device_model from tab_devicetype_info a,gw_device_model b where devicetype_id="
						+ device_type_id + " and a.device_model_id=b.device_model_id");
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return (String) fields.get("device_model");
		}
		else
		{
			return "";
		}
	}

	/**
	 * 获取设备型号ID，NAME的map
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-14
	 * @return Map
	 */
	public static Map getDevice_Model()
	{
		String strSQL = "select device_model_id, device_model from gw_device_model";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return DataSetBean.getMap(strSQL);
	}

	/**
	 * 根据厂商获取设备型号,
	 * 
	 * @param
	 * @author zhaixf
	 * @date 2008-8-14
	 * @return String
	 */
	// public String getDeviceModel(String oui) {
	//
	// String strSQL = "select distinct device_model from tab_devicetype_info";
	// if (oui != null && !"".equals(oui.trim()))
	// strSQL += " where oui='" + oui + "'";
	// logger.debug("getDeviceModelAndSoftV_SQL::" + strSQL);
	// cursor = DataSetBean.getCursor(strSQL);
	// String strDeviceModel = FormUtil.createListBox(cursor, "device_model",
	// "device_model",
	// true, "", "devicetype_id");
	//
	// return strDeviceModel;
	// }
	public String getDeviceModel(String vendorId)
	{
		String strSQL = "select device_model_id,device_model from gw_device_model";
		if (vendorId != null && !"".equals(vendorId.trim()))
			strSQL += " where vendor_id='" + vendorId + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strDeviceModel = FormUtil.createListBox(cursor, "device_model_id",
				"device_model", true, "", "device_model_id");
		return strDeviceModel;
	}

	// public String getDeviceSoftware(String oui, String device_model) {
	//
	// String strSQL =
	// "select devicetype_id, softwareversion from tab_devicetype_info where 1=1";
	//
	// if (oui != null && !"".equals(oui.trim()))
	// strSQL += " and oui='" + oui + "'";
	//
	// if (device_model != null && !"".equals(device_model.trim()))
	// strSQL += " and device_model='" + device_model + "'";
	//
	// logger.debug("getDeviceSoftware_SQL:" + strSQL);
	// cursor = DataSetBean.getCursor(strSQL);
	// String strDeviceSoftware = FormUtil.createListBox(cursor,
	// "devicetype_id",
	// "softwareversion", true, "", "softwareversion");
	// return strDeviceSoftware;
	// }
	public String getDeviceSoftware(String vendorId, String deviceModelId)
	{
		String strSQL = "select devicetype_id, softwareversion+'('+hardwareversion+')' as softwareversion from tab_devicetype_info where 1=1";
		if (vendorId != null && !"".equals(vendorId.trim()))
			strSQL += " and vendor_id='" + vendorId + "'";
		if (deviceModelId != null && !"".equals(deviceModelId.trim()))
			strSQL += " and device_model_id='" + deviceModelId + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL);
		String strDeviceSoftware = FormUtil.createListBox(cursor, "devicetype_id",
				"softwareversion", true, "", "softwareversion");
		return strDeviceSoftware;
	}

	/**
	 * 获取设备信息(根据设备上报时间查询设备)
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByCity(HttpServletRequest request)
	{
		String fileter = "";
		ArrayList list = new ArrayList();
		String sqlDevice = "select * from tab_gw_device where cpe_allocatedstatus=1 ";
		// 查找设备条件
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str_city_id = request.getParameter("city_id");
		if (endTime != null && !endTime.equals(""))
		{
			long longEndTime = new DateTimeUtil(endTime).getLongTime();
			sqlDevice += " and complete_time <=" + longEndTime;
			fileter += "&endTime=" + endTime;
		}
		if (startTime != null && !startTime.equals(""))
		{
			long longStartTime = new DateTimeUtil(startTime).getLongTime();
			sqlDevice += " and complete_time >=" + longStartTime + " ";
			fileter += "&startTime=" + startTime;
		}
		if (str_city_id == null || str_city_id.equals("") || str_city_id.equals("-1"))
		{
			str_city_id = "00";
		}
		List list1 = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
		String allCityIds = StringUtils.weave(list1);
		list1 = null;
		sqlDevice += " and city_id in (" + allCityIds + ") ";
		fileter += "&city_id=" + str_city_id;
		logger.debug("统计获取设备列表SQL :" + sqlDevice);
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice, offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 获取设备信息（根据设备绑定时间查询设备)
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByBindTime(HttpServletRequest request)
	{
		String fileter = "";
		ArrayList list = new ArrayList();
		StringBuffer sqlDevice = new StringBuffer();
		sqlDevice.append("select a.* from tab_gw_device a");
		if (2 == LipossGlobals.SystemType())
		{
			sqlDevice
					.append(" ,tab_egwcustomer b where a.device_id=b.device_id and b.user_state in ('1','2') ");
		}
		else
		{
			sqlDevice
					.append(" ,tab_hgwcustomer b where a.device_id=b.device_id and b.user_state in ('1','2') ");
		}
		// 查找设备条件
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str_city_id = request.getParameter("city_id");
		if (startTime != null && !startTime.equals(""))
		{
			sqlDevice.append(" and b.opendate >=" + startTime);
			fileter += "&startTime=" + startTime;
		}
		if (endTime != null && !endTime.equals(""))
		{
			long longEndTime = Long.parseLong(endTime);
			sqlDevice.append(" and b.opendate <=" + endTime);
			long longTemp = longEndTime + 8 * 24 * 3600;
			sqlDevice.append(" and b.binddate <=" + longTemp);
			fileter += "&endTime=" + endTime;
		}
		if (str_city_id == null || str_city_id.equals("") || str_city_id.equals("-1"))
		{
			str_city_id = "00";
		}
		if (!"00".equals(str_city_id))
		{
			sqlDevice.append(" and a.city_id in (");
			List list1 = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
			sqlDevice.append(StringUtils.weave(list1));
			sqlDevice.append(") ");
			list1 = null;
		}
		fileter += "&city_id=" + str_city_id;
		fileter += "&binddate=binddate";
		logger.debug("统计获取设备列表SQL :" + sqlDevice);
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(sqlDevice.toString(), offset, pagelen);
		PrepareSQL psql = new PrepareSQL(sqlDevice.toString());
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice.toString(), offset, pagelen);
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 从策略表中获取新旧版本信息XML
	 * 
	 * @param request
	 * @return
	 */
	public Map getCurrentSoftUpStrategyXML(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Map tmpMap = null;
		String device_id = StringUtil.getStringValue(request.getParameter("device_id"));
		PrepareSQL sb = new PrepareSQL("");
		if (null != device_id)
		{
			sb.append(" select * from gw_serv_strategy where device_id=? and service_id=5 and type=4");
			sb.setString(1, device_id);
		}
		tmpMap = DataSetBean.getRecord(sb.getSQL());
		String sheet_para = "";
		String encoding = "UTF-8";
		Document doc;
		if (null != tmpMap)
		{
			sheet_para = StringUtil.getStringValue(tmpMap.get("sheet_para"));
			try
			{
				doc = DocumentHelper.parseText(sheet_para);
				StringWriter writer = new StringWriter();
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent(true);
				format.setEncoding(encoding);
				XMLWriter xmlwriter = new XMLWriter(writer, format);
				xmlwriter.write(doc);
				sheet_para = writer.toString();
				sheet_para = sheet_para.replace("&", "&amp;");
				sheet_para = sheet_para.replace("<", "&lt;");
				sheet_para = sheet_para.replace(">", "&gt;");
				sheet_para = sheet_para.replace("\r\n", "\n");
				sheet_para = sheet_para.replace("\n", "<br>\n");
				sheet_para = sheet_para.replace("\t", "    ");
				sheet_para = sheet_para.replace("  ", " &nbsp;");
				map.put("sheet_para", sheet_para);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				map.put("sheet_para", "");
			}
		}
		return map;
	}

	/**
	 * 从策略表中获取策略
	 * 
	 * @param request
	 * @return
	 */
	public Map getCurrentSoftUpStrategy(HttpServletRequest request)
	{
		Map<String, String> map = new HashMap<String, String>();
		Map fields = null;
		String device_id = StringUtil.getStringValue(request.getParameter("device_id"));
		PrepareSQL sb = new PrepareSQL("");
		if (null != device_id)
		{
			sb.append(" select * from gw_serv_strategy where device_id=? and service_id=5 and type=4");
			sb.setString(1, device_id);
		}
		fields = DataSetBean.getRecord(sb.getSQL());
		String sheet_para = "";
		String encoding = "UTF-8";
		Document doc;
		if (null != fields)
		{
			map.put("status", StringUtil.getStringValue(fields.get("status")));
			map.put("resultId", StringUtil.getStringValue(fields.get("result_id")));
			sheet_para = StringUtil.getStringValue(fields.get("sheet_para"));
			try
			{
				doc = DocumentHelper.parseText(sheet_para);
				Element rootEle = doc.getRootElement();
				map.put("oldDeviceTypeId", rootEle.element("OldDeviceTypeId")
						.getTextTrim());
				map.put("newDeviceTypeId", rootEle.element("NewDeviceTypeId")
						.getTextTrim());
			}
			catch (Exception e)
			{
				logger.warn("在解析策略升级--由字符串到DOM对象时出错");
			}
		}
		logger.warn("解析软件升级策略" + map.toString());
		return map;
	}

	/**
	 * 获取软件升级版本历史记录
	 * 
	 * @param device_id
	 * @return 返回包装好数据的字符串
	 */
	public String getSoftUpHistory(String device_id)
	{
		Map<String, String> map = getSoftwareKV();
		Cursor cursor = null;
		PrepareSQL sb = new PrepareSQL("");
		if (null != device_id)
		{
			sb.append(" select * from gw_device_update_record where device_id=? ");
			sb.setString(1, device_id);
		}
		cursor = DataSetBean.getCursor(sb.getSQL());
		fields = cursor.getNext();
		// StringBuffer sb = new StringBuffer();
		String serviceHtml = "<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		// 字符串换行形式
		if (fields != null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF' height='20'>";
			serviceHtml += "<td class=column align='center'>设备老版本</td><td class=column align='center'>设备新版本</td><td class=column align='center'>改变时间</td></tr>";
		}
		else
		{
			serviceHtml += "<tr bgcolor='#FFFFFF' height='20'>";
			serviceHtml += "<td colspan='2'><span>没有设备升级历史信息！</span></td></tr>";
		}
		while (fields != null)
		{
			String tmp = "";
			long update_time = 0;
			tmp = (String) fields.get("update_time");
			if (null != tmp && !"".equals(tmp))
			{
				update_time = Long.parseLong(tmp);
			}
			else
			{
				update_time = (new Date()).getTime();
			}
			String updateTime = StringUtils
					.formatDate("yyyy-MM-dd HH:mm:ss", update_time);
			String old_type = map.get((String) fields.get("old_type_id"));
			String new_type = map.get((String) fields.get("new_type_id"));
			if (null == old_type)
			{
				old_type = "";
			}
			if (null == new_type)
			{
				new_type = "";
			}
			serviceHtml += "<tr height='20'>";
			serviceHtml += "<td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += old_type;
			serviceHtml += "</td><td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += new_type;
			serviceHtml += "</td><td bgcolor='#FFFFFF' align='center'>";
			serviceHtml += updateTime;
			serviceHtml += "</td></tr>";
			fields = cursor.getNext();
		}
		serviceHtml += "</table>";
		// return sb.toString();
		return serviceHtml;
	}

	public Map<String, String> getSoftwareKV()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		String sql = " select devicetype_id,softwareversion from tab_devicetype_info ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null)
		{
		}
		else
		{
			while (fields != null)
			{
				map.put((String) fields.get("devicetype_id"),
						(String) fields.get("softwareversion"));
				fields = cursor.getNext();
			}
		}
		return map;
	}

	public Cursor getUserDev(String sql, int offset, int pagelen)
	{
		return DataSetBean.getCursor(
				sql,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						this.getClass().getName()));
	}

	public Cursor getSth(String sql)
	{
		return DataSetBean.getCursor(sql, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
	}

	public int getNum(String sql)
	{
		return DataSetBean.executeUpdate(sql, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
	}

	public String getQuery(String sql, int offset, int pagelen, String gw_type)
	{
		QueryPage qryp = new QueryPage();
		qryp.initPage1(sql, offset, pagelen, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(this.getClass().getName()));
		return qryp.getPageBar("&gw_type=" + gw_type);
	}
}
