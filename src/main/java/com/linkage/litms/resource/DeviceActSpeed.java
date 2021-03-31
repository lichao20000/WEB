
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;

/**
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-10-8
 * @category com.linkage.litms.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceActSpeed
{

	private Cursor cursor = null;
	@SuppressWarnings("rawtypes")
	private Map fields = null;
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
	private static Logger logger = LoggerFactory.getLogger(DeviceActSpeed.class);
	
	@SuppressWarnings("rawtypes")
	public List getDeviceInfoListBySpeed(HttpServletRequest request, int gw_type)
	{
		if(Global.JXDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedJX(request,gw_type);
		}
		else if(Global.XJDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedXJ(request,gw_type);
		}
		else if(Global.NXDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedNX(request,gw_type);
		}
		else if(Global.HBDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedHB(request,gw_type);
		}
		else
		{
			return getDeviceInfoListBySpeedOther(request,gw_type);
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	public List getDeviceInfoListBySpeedExcel(HttpServletRequest request, int gw_type)
	{
		if(Global.JXDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedExcelJX(request,gw_type);
		}
		else if(Global.XJDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedExcelXJ(request,gw_type);
		}
		else if(Global.NXDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedExcelNX(request,gw_type);
		}
		else if(Global.HBDX.equals(Global.instAreaShortName))
		{
			return getDeviceInfoListBySpeedExcelHB(request,gw_type);
		}
		else
		{
			return getDeviceInfoListBySpeedExcelOther(request,gw_type);
		}
	}
	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedJX(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			sqlDevice = "select distinct a.*, d.username as loid, "
					+ areaid
					+ " as area_id "
					+ " from tab_gw_device a,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
		}
		logger.warn("gbbroadband is " + gbbroadband);
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.gbbroadband = '" + gbbroadband + "'";
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
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
		cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}
	
	/**
	 * 读取设备资源表,获取设备信息(宁夏)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedNX(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_devicetype_info t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_devicetype_info t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}

			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		logger.warn("mbbroadband is " + gbbroadband);
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.mbbroadband = " + gbbroadband;
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
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
		cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}
	
	/**
	 * 读取设备资源表,获取设备信息(湖北)
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedHB(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,REPORT_GW_DEVICE_2018 a,REPORT_HGW_SERV_INFO_2018 c ,REPORT_HGWCUSTOMER_2018 d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,REPORT_GW_DEVICE_2018 a,REPORT_HGW_SERV_INFO_2018 c ,REPORT_HGWCUSTOMER_2018 d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		logger.warn("gbbroadband is " + gbbroadband);
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.gbbroadband = " + gbbroadband;
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
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
		cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedXJ(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.gbbroadband = 1 and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.gbbroadband = 1 and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			sqlDevice = "select distinct a.*,d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where b.username = c.username "
					+ " and a.devicetype_id = t.devicetype_id and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 and t.gbbroadband = 1"
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber,d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where b.username = c.username "
						+ " and a.devicetype_id = t.devicetype_id and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 and t.gbbroadband = 1"
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
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
		cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedOther(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			if(Global.XJDX.equals(Global.instAreaShortName))
			{
				sqlDevice = "select distinct a.*,d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d where b.username = c.username "
						+ " and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 ";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select distinct a.*,d.username as loid,"
							+ areaid
							+ " as area_id "
							+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d where b.username = c.username "
							+ " and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 ";
				}
			}
			else
			{
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
						+ " where c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
							+ areaid
							+ " as area_id "
							+ " from tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
							+ " where c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
				}
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
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
		cursor = DataSetBean.getCursor(
				sqlDevice,
				offset,
				pagelen,
				DataSourceTypeCfgPropertiesManager.getInstance().getConfigItem(
						getClass().getName()));
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedExcelOther(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid, "
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
			}
			sqlDevice += " and b.downlink =" + speed + " ";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			if(Global.XJDX.equals(Global.instAreaShortName))
			{
				sqlDevice = "select distinct a.*,d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d where b.username = c.username "
						+ " and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 ";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber,d.username as loid,"
							+ areaid
							+ " as area_id "
							+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d where b.username = c.username "
							+ " and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 ";
				}
			}
			else
			{
				sqlDevice = "select distinct a.*, d.username as loid, "
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
						+ " where c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
							+ areaid
							+ " as area_id "
							+ " from tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d "
							+ " where c.user_id = d.user_id and d.device_id = a.device_id and a.device_status = 1 ";
				}
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
		}
		logger.warn(" 导出开始 : ...");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		list.add(cursor);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedExcelXJ(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.gbbroadband = 1 and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.gbbroadband = 1 and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			sqlDevice = "select distinct a.*,d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where b.username = c.username "
					+ " and a.devicetype_id = t.devicetype_id and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 and t.gbbroadband = 1"
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber,d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a, tab_netacc_spead b, hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t where b.username = c.username "
						+ " and a.devicetype_id = t.devicetype_id and c.user_id = d.user_id and d.device_id = a.device_id and a.device_status =1 and a.gw_type=1 and t.gbbroadband = 1"
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
		}
		logger.warn(" 导出开始 : ...");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		list.add(cursor);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedExcelJX(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		else
		{
			sqlDevice = "select distinct a.*, d.username as loid, "
					+ areaid
					+ " as area_id "
					+ " from tab_gw_device a,tab_hgwcustomer d ,tab_device_version_attribute t"
					+ " where d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid, "
						+ areaid
						+ " as area_id "
						+ " from tab_gw_device a,tab_hgwcustomer d ,tab_device_version_attribute t"
						+ " where d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
		}
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.gbbroadband = '" + gbbroadband + "'";
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
		}
		logger.warn(" 导出开始 : ...");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		list.add(cursor);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedExcelNX(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_devicetype_info t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,tab_gw_device a,hgwcust_serv_info c ,tab_hgwcustomer d ,tab_devicetype_info t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.mbbroadband = " + gbbroadband;
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
		}
		logger.warn(" 导出开始 : ...");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		list.add(cursor);
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDeviceInfoListBySpeedExcelHB(HttpServletRequest request, int gw_type)
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		long areaid = curUser.getAreaId();
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		String fileter = "";
		fileter = fileter + "&gw_type=" + gw_type;
		String speed = request.getParameter("speed");
		String gbbroadband = request.getParameter("gbbroadband");
		if (speed != null && !speed.equals("") && !speed.equals("-1"))
		{
			
			sqlDevice = "select distinct a.*, d.username as loid,"
					+ areaid
					+ " as area_id "
					+ " from tab_netacc_spead b,REPORT_GW_DEVICE_2018 a,REPORT_HGW_SERV_INFO_2018 c ,REPORT_HGWCUSTOMER_2018 d ,tab_device_version_attribute t"
					+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
					+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select distinct a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, d.username as loid,"
						+ areaid
						+ " as area_id "
						+ " from tab_netacc_spead b,REPORT_GW_DEVICE_2018 a,REPORT_HGW_SERV_INFO_2018 c ,REPORT_HGWCUSTOMER_2018 d ,tab_device_version_attribute t"
						+ " where b.username = c.username and c.user_id = d.user_id and d.device_id = a.device_id and a.devicetype_id = t.devicetype_id and a.device_status = 1 "
						+ " and t.devicetype_id in (select distinct devicetype_id from tab_quality_temporary )";
			}
			sqlDevice += " and b.downlink = '" + speed + "'";
			fileter = fileter + "&speed=" + speed;
		}
		if (gbbroadband != null && !gbbroadband.equals("") && !gbbroadband.equals("-1"))
		{
			if("1".equals(gbbroadband) || "2".equals(gbbroadband))
			{
				sqlDevice += " and t.gbbroadband = " + gbbroadband;
				fileter = fileter + "&gbbroadband=" + gbbroadband;
			}
		}
		// 查找设备条件
		String city_id = request.getParameter("city_id");
		String[] cityid = city_id.split(",");
		List listshow = new ArrayList();
		for (int i = 0; i < cityid.length; i++)
		{
			listshow.add(cityid[i]);
		}
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
			fileter = fileter + "&city_id=" + city_id;
		}
		logger.warn(" 导出开始 : ...");
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		list.add(cursor);
		return list;
	}

	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
}
