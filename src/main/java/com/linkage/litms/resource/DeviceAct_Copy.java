/**
 * 设备资源类，操作设备相关数据
 */
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
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * <P>
 * Linkage Communication Technology Co., Ltd
 * <P>
 * <P>
 * Copyright 2005-2007. All right reserved.
 * <P>
 * 
 * @version 1.0.0 2007-6-16
 * @author Linkage. 2007-6-16 modified by Alex.Yan (yanhj@lianchuang.com)
 *         RemoteDB. ACS.
 */
public class DeviceAct_Copy {
	private static Logger m_logger = LoggerFactory.getLogger(DeviceAct_Copy.class);
	
	private Cursor cursor = null;
	
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
	
	/**
	 * 构造函数
	 * 
	 */
	public DeviceAct_Copy() {

	}

	/**
	 * 读取设备资源表,获取设备信息
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceInfoList(HttpServletRequest request) {
		// 从request中取出gw_type 1:家庭网关设备 2:企业网关设备
		String gw_type_Str = request.getParameter("gw_type");

		String filterStr = "";

		if (null == gw_type_Str) {
			gw_type_Str = "2";
		}
		int gw_type = Integer.parseInt(gw_type_Str);

		String tab_name = "";
		if (gw_type == 1) {

			tab_name = "tab_hgwcustomer";

		} else {

			tab_name = "tab_egwcustomer";

		}

		filterStr += "&gw_type=" + gw_type;

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
		if (null != start_time && null != start_day && null != end_day && null != end_time) {
			String startdateStr = start_day + " " + start_time;
			String enddateStr = end_day + " " + end_time;

			startTime = new DateTimeUtil(startdateStr).getLongTime();
			endTime = new DateTimeUtil(enddateStr).getLongTime();
		}
		
		String sqlDevice = "select a.*,b.area_id,c.online_status,c.last_time,c.oper_time from tab_gw_device a left join gw_devicestatus c on a.device_id=c.device_id left join tab_gw_res_area b on a.device_id=b.res_id and b.res_type=1 and b.area_id="
			+ areaid + " where a.gw_type= " + gw_type + " ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = "select a.device_name,a.oui,a.device_serialnumber,a.retrycount,b.area_id,c.online_status,c.last_time,c.oper_time from tab_gw_device a left join gw_devicestatus c on a.device_id=c.device_id left join tab_gw_res_area b on a.device_id=b.res_id and b.res_type=1 and b.area_id="
					+ areaid + " where a.gw_type= " + gw_type + " ";
		}
		
		/**
		 * modify by qixueqi 2009-04-16 09:29
		 */
		if (curUser.getUser().isAdmin()) {
			sqlDevice = "select a.*,c.online_status,c.last_time,c.oper_time from tab_gw_device a ,gw_devicestatus c where a.device_id=c.device_id and  a.gw_type= " + gw_type + " ";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select a.device_name,a.oui,a.device_serialnumber,a.retrycount,c.online_status,c.last_time,c.oper_time from tab_gw_device a ,gw_devicestatus c where a.device_id=c.device_id and  a.gw_type= " + gw_type + " ";
			}
		}

		if (username != null && !"".equals(username)) {
			sqlDevice += " and a.oui || a.device_serialnumber in (select oui || device_serialnumber from "
					+ tab_name + " where username = '" + username + "' and user_state='1' )";

			filterStr += "&username=" + username;
		}

		if (serialnumber != null && !"".equals(serialnumber)) {
			if(serialnumber.length()>5){
				sqlDevice += " and a.dev_sub_sn ='" + serialnumber.substring(serialnumber.length()-6, serialnumber.length()) + "'";
			}
			sqlDevice += " and a.device_serialnumber like'%" + serialnumber + "'";

			filterStr += "&serialnumber=" + serialnumber;
		}

		if (null != str_softwareversion && !"".equals(str_softwareversion)
				&& !"-1".equals(str_softwareversion)) {
			m_logger.debug("str_softwareversion:" + str_softwareversion);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_devicetype_info where softwareversion='"
					+ str_softwareversion + "')";

			filterStr += "&softwareversion=" + str_softwareversion;
		}

		// 选择了在线状态
		if (null != str_status && !"".equals(str_status) && !"-1".equals(str_status)
				&& !"null".equals(str_status)) {
			m_logger.debug("str_status:" + str_status);
			if ("2".equals(str_status)) {
				sqlDevice += " and c.online_status is null";
			} else {
				sqlDevice += " and c.online_status =" + str_status;
			}

			filterStr += "&status=" + str_status;
		}

		if (null != str_area && !"".equals(str_area)) {
			m_logger.debug("str_area:" + str_area);
			if (!"admin.com".equals(str_area)) {
				sqlDevice += " and a.device_id in(select res_id from tab_gw_res_area f,tab_area g where f.area_id =g.area_id and g.area_name='"
						+ str_area + "' and f.res_type=1)";
			}

			filterStr += "&area=" + str_area;
		}

		if (null != str_deviceserial && !"".equals(str_deviceserial)) {
			m_logger.debug("str_deviceserial:" + str_deviceserial);
			sqlDevice += " and a.device_serialnumber ='" + str_deviceserial + "'";

			filterStr += "&device_serial=" + str_deviceserial;
		}

		if (null != str_servicecode && !"".equals(str_servicecode)) {
			m_logger.debug("str_servicecode:" + str_servicecode);
			sqlDevice += " and a.devicetype_id in(select distinct(devicetype_id) from tab_servicecode where servicecode='"
					+ str_servicecode + "')";

			filterStr += "&service_code=" + str_servicecode;
		}

		if (str_device_id_ex != null && !str_device_id_ex.equals("")) {
			sqlDevice += " and a.device_id_ex ='" + str_device_id_ex + "' ";
			filterStr += "&device_id_ex=" + str_device_id_ex;
		}
		if (str_loopback_ip != null && !str_loopback_ip.trim().equals("")) {
			sqlDevice += " and a.loopback_ip like '%" + str_loopback_ip.trim() + "%'";

			filterStr += "&loopback_ip=" + str_loopback_ip;
		}
		if (str_cpe_mc != null && !str_cpe_mc.trim().equals("")) {
			sqlDevice += " and a.cpe_mac ='" + str_cpe_mc.trim() + "'";

			filterStr += "&cpe_mc=" + str_cpe_mc;

		}
		if (str_devicetype_id != null && !str_devicetype_id.equals("-1")
				&& !str_devicetype_id.equals("")) {
			sqlDevice += " and a.devicetype_id =" + str_devicetype_id;

			filterStr += "&devicetype_id=" + str_devicetype_id;
		}
		if (str_vendor_id != null && !str_vendor_id.equals("-1") && !str_vendor_id.equals("")) {
			sqlDevice += " and a.oui ='" + str_vendor_id + "' ";

			filterStr += "&vendor_id=" + str_vendor_id;
		}
		// 设备确认状态
		if (str_device_status != null && !str_device_status.equals("")) {
			sqlDevice += " and a.device_status =" + str_device_status;

			filterStr += "&device_status=" + str_device_status;
		} else {
			sqlDevice += " and a.device_status > 0";
		}
		if (cpe_currentstatus != null && !cpe_currentstatus.equals("")
				&& !cpe_currentstatus.equals("-1")) {
			m_logger.debug("cpe_currentstatus:" + cpe_currentstatus);
			if (("_null_").equals(cpe_currentstatus)) {
				sqlDevice += " and a.online_status is null";// 查询出设备未知状态
			} else
				sqlDevice += " and a.online_status=" + cpe_currentstatus;

			filterStr += "&cpe_currentstatus=" + cpe_currentstatus;
		}
		if (str_city_id != null && !str_city_id.equals("") && !str_city_id.equals("-1")) {
			sqlDevice += " and a.city_id ='" + str_city_id + "' ";

			filterStr += "&city_id=" + str_city_id;
		}

		if (str_gather_id != null && !str_gather_id.equals("") && !str_gather_id.equals("-1")) {
			sqlDevice += " and a.gather_id ='" + str_gather_id + "' ";

			filterStr += "&gather_id=" + str_gather_id;
		}

		if (startTime != 0 && !"".equals(start_time)) {
			sqlDevice += " and c.last_time >= " + startTime;

			filterStr += " &cpe_currentupdatetime >= " + startTime;
		}
		if (endTime != 0 && !"".equals(endTime)) {
			sqlDevice += " and c.last_time <=" + endTime;

			filterStr += " &cpe_currentupdatetime<=" + endTime;
		}

		// 设备列表呈现时，先按属地排序，再按厂商，设备类型排
		sqlDevice += " order by a.city_id, a.oui, a.device_model_id";

		m_logger.debug("获取设备列表信息-------DeviceRecord:" + sqlDevice);
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
	 * 获取设备信息（根据设备绑定时间查询设备)
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByBindTime(HttpServletRequest request) {

		String fileter = "";

		ArrayList list = new ArrayList();

		StringBuffer sqlDevice = new StringBuffer();
			
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice.append("select a.device_id, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber from tab_gw_device a");
		}
		else {
			sqlDevice.append("select a.* from tab_gw_device a");
		}

		if(2==LipossGlobals.SystemType()){
			sqlDevice.append(" ,tab_egwcustomer b where a.device_id=b.device_id and b.user_state in ('1','2') ");
		}else{
			sqlDevice.append(" ,tab_hgwcustomer b where a.device_id=b.device_id and b.user_state in ('1','2') ");
		}
		
		// 查找设备条件
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str_city_id = request.getParameter("city_id");

		if (startTime != null && !startTime.equals("")) {
			sqlDevice.append(" and b.opendate >=" + startTime);
			fileter += "&startTime=" + startTime;
		}
		if (endTime != null && !endTime.equals("")) {
			long longEndTime = Long.parseLong(endTime);
			sqlDevice.append(" and b.opendate <=" + endTime);
			long longTemp = longEndTime + 8*24*3600;
			sqlDevice.append(" and b.binddate <=" + longTemp);
			fileter += "&endTime=" + endTime;
		}

		if (str_city_id == null || str_city_id.equals("") || str_city_id.equals("-1")) {
			str_city_id = "00";
		}
		if(!"00".equals(str_city_id)){
			sqlDevice.append(" and a.city_id in (");
			List list1 = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
			sqlDevice.append(StringUtils.weave(list1));
			sqlDevice.append(") ");
			list1 = null;
		}

		fileter += "&city_id=" + str_city_id;
		fileter += "&binddate=binddate";

		m_logger.debug("统计获取设备列表SQL :" + sqlDevice);
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
	 * 获取设备信息(根据设备上报时间查询设备)
	 * 
	 * @param request
	 * @return
	 */
	public List getDeviceListByCity(HttpServletRequest request) {

		String fileter = "";

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String str_city_id = request.getParameter("city_id");
		
		long longStartTime = Long.parseLong(startTime);
		long longEndTime = Long.parseLong(endTime);
		
		ArrayList list = new ArrayList();
		PrepareSQL ppSQL = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			ppSQL.setSQL("select a.device_id, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber from tab_gw_device a ");
		}
		else {
			ppSQL.setSQL("select a.* from tab_gw_device a ");
		}
		if(2==LipossGlobals.SystemType()){
			ppSQL.append(" ,tab_egwcustomer b ");
		}else{
			ppSQL.append(" ,tab_hgwcustomer b ");
		}
		ppSQL.append(" where cpe_allocatedstatus=1 and a.device_id=b.device_id and b.user_state in ('1','2') ");
		if (longStartTime != 0) {
			ppSQL.append(" and b.opendate >= "+longStartTime);
			fileter += "&startTime=" + startTime;
		}
		if (longEndTime != 0) {
			ppSQL.append(" and b.opendate <= "+longEndTime);
			fileter += "&endTime=" + endTime;
		}
		
		if(!"00".equals(str_city_id)){
			List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
			ppSQL.append(PrepareSQL.AND,"a.city_id",cityList);
			cityList = null;
			fileter += "&city_id=" + str_city_id;
		}
		
		String stroffset = request.getParameter("offset");

		int pagelen = 30;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(ppSQL.toString(), offset, pagelen);
		cursor = DataSetBean.getCursor(ppSQL.toString(), offset, pagelen);
		String strBar = qryp.getPageBar(fileter);
		list.add(strBar);
		list.add(cursor);
		return list;
	}
	
	/**
	 * 获取设备型号版本map
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public Map getDeviceTypeMap() {

		String devicemodel = "";
		String softwareversion = "";
		String devicetype_id = "";
		Map deviceTypeMap = new HashMap();

//		Cursor cursorTmp = DataSetBean.getCursor("select * from tab_devicetype_info");
		String sql ="select a.device_model_id,a.device_model,b.softwareversion,b.devicetype_id" +
		" from gw_device_model a, tab_devicetype_info b where a.device_model_id=b.device_model_id";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursorTmp = DataSetBean.getCursor(sql);
		
		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null) {
			devicemodel = (String) fieldTmp.get("device_model");
			softwareversion = (String) fieldTmp.get("softwareversion");
			//型号，软件版本数组，modelsoft[0]为型号，modelsoft[1]为软件版本
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
	 * 取得所有vendorId和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap() {
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		PrepareSQL psql = new PrepareSQL(m_Vendor_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(m_Vendor_SQL);
		
		Map fields = cursor.getNext();
		if (fields == null) {

		} else {
			while (fields != null) {
				String ouiName = (String) fields.get("vendor_add");

				if (ouiName != null && !"".equals(ouiName)) {
					ouiMap.put((String) fields.get("vendor_id"), ouiName);
				} else {
					ouiMap
							.put((String) fields.get("vendor_id"), (String) fields
									.get("vendor_name"));
				}

				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}
}
