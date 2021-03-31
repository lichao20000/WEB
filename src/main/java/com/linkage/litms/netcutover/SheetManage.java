package com.linkage.litms.netcutover;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.ExportExcelUtil;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * zhaixf(3412) 2008-04-11 req:XJDX_ITMS-REQ-20080411-CZM-001
 * ---------------------------------------------------------------------------------
 * 
 * @author lizhaojun
 * @version 1.00, 4/16/2007
 * @since HGW 1.0
 *        ---------------------------------------------------------------------------------
 * @author Alex.Yan(yanhj@lianchugang.com)
 * @date 2008-6-13
 * @desc sheet para change.
 */

public class SheetManage 
{
	private static final Logger LOG = LoggerFactory.getLogger(SheetManage.class);
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;

	/**
	 * 查询业务表生成下拉框
	 * 
	 * add by lizhaojun
	 * 
	 * @param null
	 * 
	 * @return String 将操作结果以下拉列表的的形式返回
	 */

	public String getServiceList(String cast) {

		String tmpSql = "select * from tab_service where 1=1 and flag=1 order by service_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select service_id, service_name from tab_service where 1=1 and flag=1 order by service_id";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String serviceList = FormUtil.createListBox(cursor, "service_id",
				"service_name", true, cast, "");
		return serviceList;

	}

	/**
	 * 查询业务表代码，根据业务表的Service_ID过滤 生成下拉框
	 * 
	 * add by lizhaojun
	 * 
	 * @param service_id,cast
	 * 
	 * @return String 将操作结果以下拉列表的的形式返回
	 */

	public String getServiceCode(String service_id, String cast) {

		String tmpSql = "select * from tab_servicecode where service_id="
				+ service_id + " order by service_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select servicecode, service_id from tab_servicecode where service_id="
					+ service_id + " order by service_id";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String serviseList = FormUtil.createListBox(cursor, "servicecode",
				"servicecode", true, cast, "");
		return serviseList;
	}

	/**
	 * 查询业务表代码，根据业务表的Service_ID过滤 生成下拉框
	 * 
	 * add by lizhaojun
	 * 
	 * @param
	 * 
	 * @return String 将操作结果以下拉列表的的形式返回
	 */

	public String getService(HttpServletRequest request) {

		String flag = request.getParameter("flag");

		if (flag == null || flag.equals("")) {

			flag = "1";

		}

		String tmpSql = "select * from tab_service where flag=" + flag
				+ "  order by service_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select service_id, service_name from tab_service where flag=" + flag
					+ "  order by service_id";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String serviseList = FormUtil.createListBox(cursor, "service_id",
				"service_name", true, "", "");
		return serviseList;
	}

	/**
	 * 根据业务表设备型号过滤设备资源表，得到相应的设备 生成下拉框
	 * 
	 * add by lizhaojun
	 * 
	 * @param request
	 * 
	 * @return String 将操作结果以下拉列表的的形式返回
	 * 
	 * @throws UnsupportedEncodingException
	 */

	public String getDeviceList(HttpServletRequest request)
			throws UnsupportedEncodingException {
		String servicecode = new String(request.getParameter("servicecode")
				.getBytes("ISO-8859-1"), "GBK");
		String gather_id = request.getParameter("gather_id");

		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();

		String tmpSql = "select * from tab_gw_device a,tab_servicecode b ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select device_id, oui, device_serialnumber from tab_gw_device a,tab_servicecode b ";
		}
		if (!user.isAdmin()) {
			tmpSql += " , tab_gw_res_area c ";
		}
		tmpSql += " where 1=1 and b.servicecode='" + servicecode
				+ "' and b.devicetype_id = a.devicetype_id and a.gather_id ='"
				+ gather_id + "' and device_status=1";

		if (!user.isAdmin()) {
			tmpSql += " and c.res_type=1 and a.device_id = c.res_id and c.area_id="
					+ area_id + " ";
		}

		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null) {
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无符合条件的设备</span></td></tr>";
		} else {
			int iflag = 1;
			while (fields != null) {
				if (iflag % 2 == 0) {
					serviceHtml += "<tr class='green_foot'>";
					serviceHtml += "<td width='2%'>";
					serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\""
							+ (String) fields.get("device_id") + "\">";
					serviceHtml += "</td><td  width='98%'>";
					serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
							+ (String) fields.get("device_serialnumber");
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

		return serviceHtml;

	}


	/**
	 * 桥接改路由后，把用户的相关信息更新（口令，上网类型）gongsj
	 * 
	 * @param username
	 * @param passwd
	 * @return true 更新成功 false 更新失败
	 */
	public boolean updateHgwcustomer(String username, String passwd) {

		String _sql = "update tab_hgwcustomer set passwd='" + passwd
				+ "',wan_type=2 where username='" + username + "'";

		PrepareSQL psql = new PrepareSQL(_sql);
    	psql.getSQL();
		int code = DataSetBean.executeUpdate(_sql);
		if (code > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 桥接改路由后，把用户的相关信息更新（口令，上网类型）gongsj
	 * 
	 * @param username
	 * @param passwd
	 * @return true 更新成功 false 更新失败
	 */
	public boolean updateHgwcustomer(String serv_type_id, String username,
			String passwd, int wan_type, String vpiid, String vciid) {

		String _sql = "update tab_hgwcustomer set passwd='" + passwd
				+ "',vpiid='" + vpiid + "',vciid=" + vciid + ",wan_type="
				+ wan_type + " where username='" + username + "'";

		PrepareSQL psql = new PrepareSQL(_sql);
    	psql.getSQL();
		int code = DataSetBean.executeUpdate(_sql);
		if (code > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回正在处理的工单列表. lizhaojun
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return 返回Cursor类型数据
	 */
	public Cursor excSheetList(String tmpSql) {
		Cursor cursor = null;
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(tmpSql);
		return cursor;

	}

	/**
	 * 返回工单列表. lizhaojun
	 * 
	 * @param request
	 * 
	 * @param
	 * 
	 * @return 返回Cursor类型数据
	 */
	public Cursor getSheetList(HttpServletRequest request) {
		Cursor cursor = null;
		// 获取当前用户信息，以及用户所属的
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		String serviceType = request.getParameter("serviceType");

		long area_id = user.getAreaId();

		String str_lms = request.getParameter("start");
		String str_lms_end = request.getParameter("end");

		long startTime = Long.parseLong(str_lms);
		LOG.debug("startTime :" + startTime);

		Date dt;

		// 初始化页面的时候默认查询1天
		// 将查询细度由原半天放大到一天
		// 时间跨度为24小时
		if (StringUtil.IsEmpty(str_lms_end)) {
			dt = new Date(startTime + 24 * 3600);
		}
		else {
			dt = new Date(Long.parseLong(str_lms_end));
		}
		long endTime = dt.getTime();
		// 过滤条件
		String str_filter = request.getParameter("filter");

		String str_ifcontainChild = request.getParameter("ifcontainChild");
		String sql = "select distinct a.*,d.oui,d.device_serialnumber,d.customer_id,c.service_name,c.serv_type_id,c.oper_type_id from tab_sheet_report a, tab_service c,tab_gw_device d ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select distinct a.city_id, a.exec_status, a.fault_code, a.start_time, a.end_time, a.sheet_id, a.receive_time, " +
					"a.gather_id, a.username, a.fault_desc, a.device_id, a.service_id, " +
					"d.oui,d.device_serialnumber,d.customer_id,c.service_name,c.serv_type_id,c.oper_type_id " +
					"from tab_sheet_report a, tab_service c,tab_gw_device d ";
		}

		String str_city_id = request.getParameter("city_id");
		// if (!user.isAdmin()) {
		// sql += " ,tab_gw_res_area b ";
		// }
		sql += " where a.start_time>=" + startTime + " and a.start_time <="
				+ endTime;

		// if (!user.isAdmin()) {
		// sql += "  and a.device_id = b.res_id and b.area_id=" + area_id
		// + " and b.res_type =1 ";
		// }

		sql += " and a.device_id = d.device_id ";

		if (str_city_id != null && !str_city_id.equals("")) {
			if (str_filter != null && !str_filter.equals("")) {
				String[] filter = str_filter.split(",");
				if (filter.length == 1) {
					if (filter[0].equals("0")) {
						sql += " and a.fault_code = 1 ";
					} else {
						sql += " and a.exec_status = 1 and a.fault_code != 1 ";
					}
				}
			}
			//为-1，则赋值为当前登录用户属地
			if(str_city_id.equals("-1")){
				str_city_id = user.getCityId();
			}
			if (str_ifcontainChild.equals("1")) {
				SelectCityFilter scf = new SelectCityFilter();
				sql += " and a.city_id in ("
						+ scf.getAllSubCityIds(str_city_id, true) + ") ";
			} else {
				sql += " and a.city_id ='" + str_city_id + "' ";
			}
		} else if (!user.isAdmin()) {
			//非admin用户，只能查看自己属地的工单
			SelectCityFilter scf = new SelectCityFilter();
			sql += " and a.city_id in ("
					+ scf.getAllSubCityIds(user.getCityId(), true) + ") ";
		}

		if (serviceType != null && "1".equals(serviceType)) {
			sql += " and a.service_id = c.service_id and c.flag=0 ";
		} else if (serviceType != null && "2".equals(serviceType)) {
			sql += " and a.service_id = c.service_id and c.flag>0 ";
		} else {
			// service_id=1021：桥接改路由
			// service_id=1022：路由改桥接
			sql += "  and a.service_id = c.service_id and c.flag=1 ";
		}

		sql += "  order by a.start_time desc ";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	@SuppressWarnings("unchecked")
	public String toExcel(HttpServletRequest request, HttpServletResponse response) {
		// 处理数据导出
		ExportExcelUtil util = new ExportExcelUtil("", new String[] { "工单编号",
						"属地", "业务类型", "操作类型", "用户帐户", "执行状态", "执行结果", "开始时间", "结束时间", "失败原因描述"});
		try
		{
			Cursor cursor = getSheetList(request);
			Map mp = cursor.getNext();
			List<Map> list = new ArrayList();
			while (mp != null) {
				list.add(mp);
				mp.put("city_name", CityDAO.getCityIdCityNameMap().get(mp.get("city_id")));
				mp.put("serv_type", ServiceAct.getGwServTypeMap().get(mp.get("serv_type_id")));
				mp.put("oper_type", ServiceAct.getGwOperTypeMap().get(mp.get("oper_type_id")));
	
				String execStatus = StringUtil.getStringValue(mp.get("exec_status"));
				if(execStatus.equals("-1")){
					execStatus = "等待执行";
				} else if(execStatus.equals("0")){
					execStatus = "正在执行";
				}else{
					execStatus = "执行完成";
				}
				mp.put("execStatus_name", execStatus);
				
				String fault_code = StringUtil.getStringValue(mp.get("fault_code"));
				int faultCode = StringUtil.getIntegerValue(fault_code);
				String fault_code_name = com.linkage.module.gwms.Global.G_Fault_Map.get(faultCode).getFaultReason();
				mp.put("fault_code_name", fault_code_name);
				
				mp.put("start_time_str", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", 
						Long.parseLong(StringUtil.getStringValue(mp.get("start_time")))));
				
				mp.put("end_time_str", StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", 
						Long.parseLong(StringUtil.getStringValue(mp.get("end_time")))));
				mp = cursor.getNext();
			}
			//util.export(response, rowSet, fileName);
			util.export(response, list, 
					new String[]{"sheet_id", "city_name", "serv_type", "oper_type", "username", "execStatus_name", "fault_code_name", "start_time_str", "end_time_str", "fault_desc" }, "worksheet");
		}
		catch (Exception e)
		{
			LOG.error("export error:", e);
		}
		return null;
	}
	/**
	 * 返回工单列表. lizhaojun
	 * 
	 * @param request
	 * 
	 * @param
	 * 
	 * @return 返回Cursor类型数据
	 */
	public Cursor getSheetListIptv(HttpServletRequest request) {
		Cursor cursor = null;
		// 获取当前用户信息，以及用户所属的
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		String serviceType = request.getParameter("serviceType");

		String str_lms = request.getParameter("start");
		String str_lms_end = request.getParameter("end");

		long startTime = Long.parseLong(str_lms);
		LOG.debug("startTime :" + startTime);

		Date dt;

		// 初始化页面的时候默认查询1天
		// 将查询细度由原半天放大到一天
		// 时间跨度为24小时
		if (StringUtil.IsEmpty(str_lms_end)) {
			dt = new Date(startTime + 24 * 3600);
		}
		else {
			dt = new Date(Long.parseLong(str_lms_end));
		}
		long endTime = dt.getTime();
		// 过滤条件
		String str_filter = request.getParameter("filter");

		String str_ifcontainChild = request.getParameter("ifcontainChild");
		String sql = "select distinct a.*,d.oui,d.device_serialnumber,d.customer_id,c.service_name,c.serv_type_id,c.oper_type_id from tab_sheet_report a, tab_service c,tab_gw_device d ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select distinct a.city_id, a.exec_status, a.fault_code, a.start_time, a.end_time, a.sheet_id, a.receive_time, " +
					"a.gather_id, a.username, a.fault_desc, a.device_id, a.service_id,a.exec_desc, a.exec_count, d.oui," +
					"d.device_serialnumber,d.customer_id,c.service_name,c.serv_type_id,c.oper_type_id " +
					"from tab_sheet_report a, tab_service c,tab_gw_device d ";
		}
		String str_city_id = request.getParameter("city_id");
		// if (!user.isAdmin()) {
		// sql += " ,tab_gw_res_area b ";
		// }
		sql += " where a.start_time>=" + startTime + " and a.start_time <="
				+ endTime;

		// if (!user.isAdmin()) {
		// sql += "  and a.device_id = b.res_id and b.area_id=" + area_id
		// + " and b.res_type =1 ";
		// }

		sql += " and a.device_id = d.device_id ";

		if (str_city_id != null && !str_city_id.equals("")) {
			if (str_filter != null && !str_filter.equals("")) {
				String[] filter = str_filter.split(",");
				if (filter.length == 1) {
					if (filter[0].equals("0")) {
						sql += " and a.fault_code = 1 ";
					} else {
						sql += " and a.exec_status = 1 and a.fault_code != 1 ";
					}
				}
			}
			//为-1，则赋值为当前登录用户属地
			if(str_city_id.equals("-1")){
				str_city_id = user.getCityId();
			}
			if (str_ifcontainChild.equals("1")) {
				SelectCityFilter scf = new SelectCityFilter();
				sql += " and a.city_id in ("
						+ scf.getAllSubCityIds(str_city_id, true) + ") ";
			} else {
				sql += " and a.city_id ='" + str_city_id + "' ";
			}
		} else if (!user.isAdmin()) {
			//非admin用户，只能查看自己属地的工单
			SelectCityFilter scf = new SelectCityFilter();
			sql += " and a.city_id in ("
					+ scf.getAllSubCityIds(user.getCityId(), true) + ") ";
		}

		if (serviceType != null && "1".equals(serviceType)) {
			sql += " and a.service_id = c.service_id and c.flag=0 ";
		} else if (serviceType != null && "2".equals(serviceType)) {
			sql += " and a.service_id = c.service_id and c.flag>0 ";
		} else {
			// service_id=1021：桥接改路由
			// service_id=1022：路由改桥接
			sql += "  and a.service_id = c.service_id and c.flag=1 ";
		}

		sql += "  order by a.start_time desc ";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		return cursor;

	}

	/**
	 * @author lizhaojun 查询详细的工单信息
	 * @param req
	 * @return Cursor
	 */

	public Map getSheetDetail(HttpServletRequest req) {
		String sheet_id = req.getParameter("sheet_id");
		String receive_time = req.getParameter("receive_time");
		String tmpSql = "select * from tab_sheet_report a,tab_sheet b,tab_service c "
				+ " where a.sheet_id = b.sheet_id and b.service_id = c.service_id and a.sheet_id='"
				+ sheet_id + "' and a.receive_time=" + receive_time;
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select a.sheet_id, a.gather_id, a.device_id, a.city_id, a.exec_status, a.exec_count, a.username, " +
					"a.exec_desc, a.fault_code, a.fault_desc, a.receive_time, a.start_time, a.end_time, c.service_name, b.sheet_source " +
					" from tab_sheet_report a,tab_sheet b,tab_service c "
					+ " where a.sheet_id = b.sheet_id and b.service_id = c.service_id and a.sheet_id='"
					+ sheet_id + "' and a.receive_time=" + receive_time;
		}

		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(tmpSql);
		return fields;

	}

	/**
	 * @author lizhaojun 查询家庭网关用户信息表，显示到详细工单页面
	 * 
	 * @param device_id
	 * 
	 * @return Cursor
	 */

	public Map getHgwDetail(String device_id) {

		String tmpSql = "select a.phonenumber,a.office_id,a.zone_id from tab_hgwcustomer a,tab_gw_device b where b.device_id = '"
				+ device_id
				+ "' and a.device_id=b.device_id ";
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(tmpSql);
		return fields;

	}

	/**
	 * @author lizhaojun 错单查询
	 * 
	 * @param request
	 * 
	 * @return Cursor
	 */
	public List getErr_SheetList(HttpServletRequest req) {

		String flag = req.getParameter("flag");

		if (flag == null || flag.equals("")) {

			flag = "1";
		}

		// 获取用户信息
		HttpSession session = req.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String userCityId = user.getCityId();

		String city_id = req.getParameter("city_id");
		if (city_id==null ||"".equals(city_id)) {
			city_id=userCityId;
		}
		String gather_id = req.getParameter("gather_id")==null?"-1":req.getParameter("gather_id").toString();
		String str_ifcontainChild = req.getParameter("ifcontainChild")==null?"1":req.getParameter("ifcontainChild").toString();
		String device_serialnumber = req.getParameter("device_serialnumber")==null?"":req.getParameter("device_serialnumber").toString();
		String username = req.getParameter("username")==null?"":req.getParameter("username").toString();
		String start_Time = req.getParameter("start_Time")==null?"":req.getParameter("start_Time").toString();
		String start_Time1 = "";
		String end_Time = req.getParameter("end_Time")==null?"":req.getParameter("end_Time").toString();
		String end_Time1 = "";
		DateTimeUtil dt = null;
		if (start_Time == null || "".equals(start_Time))
		{
			start_Time1 = "";
		}
		else
		{
			dt = new DateTimeUtil(start_Time);
			start_Time1 = String.valueOf(dt.getLongTime());
		}
		if (end_Time == null || "".equals(end_Time))
		{
			end_Time1 = "";
		}
		else
		{
			dt = new DateTimeUtil(end_Time);
			end_Time1 = String.valueOf(dt.getLongTime());
		}
		
		String tmpSql = "select * from tab_sheet_report a ,tab_service c,tab_gw_device d ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			tmpSql = "select a.sheet_id, a.gather_id, a.device_id, a.city_id, a.exec_status, a.exec_count, a.username, " +
					"a.exec_desc, a.fault_code, a.fault_desc, a.receive_time, a.start_time, a.end_time, d.device_serialnumber " +
					"from tab_sheet_report a ,tab_service c,tab_gw_device d ";
		}

		if (!user.isAdmin()) {
			tmpSql += " ,tab_gw_res_area b ";
		}
		tmpSql += " where a.exec_status = 1 and a.fault_code !=1 and a.service_id = c.service_id and a.device_id=d.device_id and c.flag="
				+ flag + " ";
		if (!user.isAdmin()) {
			tmpSql += "  and b.res_type =1 and b.res_id = a.device_id and b.area_id = "
					+ area_id;
		}
		
		if (device_serialnumber != null && !device_serialnumber.equals("")) {
			if(device_serialnumber.length()>5){
				tmpSql += " and d.dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			tmpSql += " and d.device_serialnumber like '%" + device_serialnumber+ "'";
		}
		if (username != null && !username.equals("")) {
			tmpSql += " and a.username = '" + username + "' ";
		}
		if (start_Time1 != null && !start_Time1.equals("")) {
			tmpSql += " and a.receive_time >= " + start_Time1 + " ";
		}
		if (end_Time1 != null && !end_Time1.equals("")) {
			tmpSql += " and a.receive_time < " + end_Time1 + " ";
		}
		
		if (gather_id != null && !gather_id.equals("")
				&& !gather_id.equals("-1")) {
			tmpSql += " and a.gather_id = '" + gather_id + "' ";
		}
		if (city_id != null && !city_id.equals("")) {
			if (str_ifcontainChild.equals("1")) {
				SelectCityFilter scf = new SelectCityFilter();
				tmpSql += " and a.city_id in ("
						+ scf.getAllSubCityIds(city_id, true) + ") ";
			} else {
				tmpSql += " and a.city_id ='" + city_id + "' ";
			}
		}
		tmpSql += " order by a.start_time desc";

		String stroffset = req.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		QueryPage qryp = new QueryPage();
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		qryp.initPage(tmpSql, offset, pagelen);
		Cursor cursor = DataSetBean.getCursor(tmpSql, offset, pagelen);
		String strBar = qryp.getPageBar("&flag="+flag+"&city_id="+city_id+"&gather_id="+gather_id+"&ifcontainChild="+str_ifcontainChild+"&username="+username+"&device_serialnumber="+device_serialnumber+"&start_Time="+start_Time+"&end_Time="+end_Time);

		List list = new ArrayList();
		list.add(strBar);
		list.add(cursor);

		return list;

	}

	/**
	 * @author lizhaojun 工单查询处理
	 * 
	 * @param request
	 * 
	 * @return Cursor
	 */

	public List getSearchList(HttpServletRequest request) {

		String strSQL = null;

		// ----------------------属地过滤 -------------------------------------
		String cityid = request.getParameter("hid_city_id");

		String ifcontainChild = request.getParameter("ifcontainChild");

		if (ifcontainChild.equals("1")) {
			SelectCityFilter scf = new SelectCityFilter();
			cityid = scf.getAllSubCityIds(cityid, true);
		} else {
			cityid = "'" + cityid + "'";
		}

		// -------------------------------------------------------------------
		String flag = request.getParameter("flag"); // 业务类型

		String start = request.getParameter("start"); // 开始
		start = start.replaceAll("-", "/");
		String startTime = request.getParameter("startTime");
		String end = request.getParameter("end"); // 结束
		end = end.replaceAll("-", "/");
		String endTime = request.getParameter("endTime");

		Date start_data = new Date(start + " " + startTime);
		Date end_data = new Date(end + " " + endTime);

		String sheet_id = request.getParameter("sheet_id"); // 工单编号
		String username = request.getParameter("username"); // 
		String device_model_id = request.getParameter("device_model_id"); 
		String device_sn = request.getParameter("device_serialnumber"); // 设备序列号
		String serv_type_id = request.getParameter("some_service"); // 业务类型
		String oper_type_id = request.getParameter("oper_type"); // 业务类型
		String sheet_status = request.getParameter("sheet_status"); // 工单状态
		//String sheet_source = request.getParameter("sheet_source"); // 来自何处

		strSQL = " select a.*,b.*,c.service_id,c.serv_type_id,c.oper_type_id,c.flag,c.service_desc"
				+ " from tab_sheet_report a,tab_sheet b,tab_service c where"
				+ " a.sheet_id = b.sheet_id and a.service_id = c.service_id and c.flag="
				+ flag + " ";

		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			strSQL = " select a.sheet_id, a.gather_id, a.device_id, a.city_id, a.exec_status, a.exec_count, a.username, " +
					"a.exec_desc, a.fault_code, a.fault_desc, a.receive_time, a.start_time, a.end_time, " +
					"b.sheet_source," +
					" c.service_id, c.serv_type_id, c.oper_type_id, c.flag, c.service_desc"
					+ " from tab_sheet_report a,tab_sheet b,tab_service c where"
					+ " a.sheet_id = b.sheet_id and a.service_id = c.service_id and c.flag="
					+ flag + " ";
		}

		if (sheet_id != null && !sheet_id.equals("")) {
			strSQL += " and a.sheet_id = '" + sheet_id + "'";
		}
		/*if (!sheet_source.equals("-1")) {
			strSQL += " and b.sheet_source = " + sheet_source + "";
		}*/
		if (username != null && username.length() > 0) {
			strSQL += " and a.username='" + username + "'";
		}

		if (!serv_type_id.equals("-1")) {
			strSQL += " and c.serv_type_id = " + serv_type_id + "";
		}
		if (!oper_type_id.equals("-1")) {
			strSQL += " and c.oper_type_id = " + oper_type_id + "";
		}
		
		if (!sheet_status.equals("-1")) {
			if (sheet_status.equals("0")) {
				strSQL += " and a.exec_status = " + sheet_status;
			} else {
				strSQL += " and a.fault_code = " + sheet_status;
			}

		}

		if (cityid != null && !"-1".equals(cityid)) {
			strSQL += " and a.city_id in (" + cityid + ") ";
		}

		strSQL += " and a.start_time > " + start_data.getTime() / 1000
				+ " and a.start_time <" + end_data.getTime() / 1000;
		if ((device_sn != null && device_sn.length() > 0) 
				|| (device_model_id != null && !device_model_id.equals("-1"))) {
			strSQL += " and b.device_id in (select device_id from tab_gw_device where";
			if (device_sn != null && device_sn.length() > 0) {
				strSQL += " device_serialnumber like '%" + device_sn + "%'";
				if (device_model_id != null && !device_model_id.equals("-1")) {
					strSQL += " and device_model_id='" + device_model_id + "'";
				}
			} else {
				if (device_model_id != null && !device_model_id.equals("-1")) {
					strSQL += " device_model_id='" + device_model_id + "'";
				}
			}
			
			strSQL += ")";
		}
		strSQL += " order by a.start_time desc";

		List list = new ArrayList();
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getGoPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		list.add(cursor);
		
		return list;

	}

	/**
	 * 获取测试任务的结果
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getTestAutoUpdateTaskDetail(HttpServletRequest request) {
		ArrayList list = new ArrayList();
		String task_id = request.getParameter("task_id");
		float percent = 0f;
		String success = "0";

		// 计算成功率
		String tab_autoupdate_testSQL = "select count(*) number from tab_autoupdate_test where task_id ="
				+ task_id;
		PrepareSQL psql = new PrepareSQL(tab_autoupdate_testSQL);
    	psql.getSQL();
		HashMap totalCountMap = DataSetBean.getRecord(tab_autoupdate_testSQL);
		String totalCount = "0";
		if (null != totalCountMap) {
			totalCount = (String) totalCountMap.get("number");
		}

		// 策略任务有对应的工单
		if (!"0".equals(totalCount)) {
			String sheet_success_SQL = "select count(*) number from tab_autoupdate_test a,tab_sheet_report b where a.sheet_id=b.sheet_id and b.fault_code =1 and a.task_id ="
					+ task_id;
			PrepareSQL psql2 = new PrepareSQL(sheet_success_SQL);
	    	psql2.getSQL();
			HashMap successCountMap = DataSetBean.getRecord(sheet_success_SQL);
			String successCount = "0";
			if (null != successCountMap) {
				successCount = (String) successCountMap.get("number");
				percent = Float.parseFloat(successCount)
						/ Float.parseFloat(totalCount);
				success = StringUtils.formatNumber(String
						.valueOf(percent * 100), 2);
			} else {
				success = "0";
			}
		} else {
			success = "0";
		}

		list.add(success);

		String sql = "select a.oui,a.device_serialnumber,b.sheet_id,c.exec_status,c.username,c.exec_desc,c.fault_code,c.fault_desc,c.start_time,c.end_time"
				+ "  from tab_gw_device a inner join tab_autoupdate_test b  on a.device_id=b.device_id left join tab_sheet_report c on b.sheet_id = c.sheet_id"
				+ " where b.task_id=" + task_id;
		PrepareSQL psql3 = new PrepareSQL(sql);
    	psql3.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		list.add(cursor);

		return list;
	}

	/**
	 * 获取自动升级任务的详细信息
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getAutoUpdateTaskDetail(HttpServletRequest request) {
		ArrayList list = new ArrayList();
		String task_id = request.getParameter("task_id");
		String success = request.getParameter("successPercent");
		float percent = 0f;
		if (null == success) {
			String tab_autoupdate_sheetSQL = "select count(*) number from tab_autoupdate_sheet where task_id ="
					+ task_id;
			PrepareSQL psql = new PrepareSQL(tab_autoupdate_sheetSQL);
	    	psql.getSQL();
			HashMap totalCountMap = DataSetBean
					.getRecord(tab_autoupdate_sheetSQL);
			String totalCount = "0";
			if (null != totalCountMap) {
				totalCount = (String) totalCountMap.get("number");
			}

			// 策略任务有对应的工单
			if (!"0".equals(totalCount)) {
				String sheet_success_SQL = "select count(*) number from tab_autoupdate_sheet a,tab_sheet_report b where a.sheet_id=b.sheet_id and b.fault_code =1 and a.task_id ="
						+ task_id;
				PrepareSQL psql2 = new PrepareSQL(sheet_success_SQL);
		    	psql2.getSQL();
				HashMap successCountMap = DataSetBean
						.getRecord(sheet_success_SQL);
				String successCount = "0";
				if (null != successCountMap) {
					successCount = (String) successCountMap.get("number");
					percent = Float.parseFloat(successCount)
							/ Float.parseFloat(totalCount);
					success = StringUtils.formatNumber(String
							.valueOf(percent * 100), 2);
				} else {
					success = "0";
				}
			} else {
				success = "0";
			}
		}
		String sql = "select a.oui,a.device_serialnumber,b.sheet_id,c.exec_status,c.username,c.exec_desc,c.fault_code,c.fault_desc,c.start_time,c.end_time"
				+ "  from tab_gw_device a inner join tab_autoupdate_sheet b  on a.device_id=b.device_id left join tab_sheet_report c on b.sheet_id = c.sheet_id"
				+ " where b.task_id=" + task_id;
		LOG.debug("getAutoUpdateTaskDetail_SQL:" + sql);
		// 分页
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		QueryPage qryp = new QueryPage();
		PrepareSQL psql3 = new PrepareSQL(sql);
    	psql3.getSQL();
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar("&task_id=" + task_id
				+ "&successPercent=" + success);
		list.add(strBar);
		Cursor cursor = DataSetBean.getCursor(sql);
		list.add(cursor);
		list.add(success);
		return list;
	}

	/**
	 * @author lizhaojun 将对应的业务id和业务名称放到map里面！
	 * 
	 * @param null
	 * 
	 * @return Map
	 * 
	 */
	public Map getServiceMap() {
		String tmpSql = "select service_id,service_name from tab_service order by service_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();

		Map serviceMap = new HashMap();
		if (fields != null) {
			while (fields != null) {
				serviceMap.put(fields.get("service_id"), fields
						.get("service_name"));
				fields = cursor.getNext();
			}
		}
		return serviceMap;
	}

	/**
	 * @author 手工工单根据用户帐号查询详细信息
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * 
	 */
	public static Map usrMap(HttpServletRequest request) {
		// Map userMap = new HashMap();

		String _userName = request.getParameter("username");
		String serv_type_id = request.getParameter("serv_type_id");
		//
		String user_type = Global.G_Usertype_Servtype_Map.get(serv_type_id);
		String _sql = "select a.wan_type,a.username,a.passwd,a.vpiid,a.vciid,a.vlanid,a.ipaddress,a.ipmask,a.gateway,a.adsl_ser,"
				+ " a.wan_value_1,a.wan_value_2,b.device_id,b.gather_id,b.devicetype_id,b.oui,b.device_serialnumber "
				+ " from tab_hgwcustomer a,tab_gw_device b  where a.username='"
				+ _userName
				+ "' and a.user_state in ('1','2') and b.device_status=1 and  a.device_id=b.device_id and a.serv_type_id="
				+ user_type;
		PrepareSQL psql = new PrepareSQL(_sql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(_sql);
		// Map userMap = _cursor.getNext();
		return fields;
	}

	/**
	 * @author 企业网关单根据用户帐号查询详细信息
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * 
	 */
	public static Map egUsrMap(HttpServletRequest request) {
		// Map userMap = new HashMap();

		String _userName = request.getParameter("username");
		String _sql = "select a.*,b.device_id,b.gather_id ,b.devicetype_id,b.oui,b.device_serialnumber from tab_egwcustomer a,tab_gw_device b  where a.username='"
				+ _userName
				+ "' and a.user_state in ('1','2') and b.device_status=1 and  a.device_id=b.device_id";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			_sql = "select a.username, a.passwd, a.e_id, a.e_username, a.e_passwd," +
					"b.device_id,b.gather_id ,b.devicetype_id,b.oui,b.device_serialnumber " +
					" from tab_egwcustomer a,tab_gw_device b  where a.username='"
					+ _userName
					+ "' and a.user_state in ('1','2') and b.device_status=1 and  a.device_id=b.device_id";
		}
		PrepareSQL psql = new PrepareSQL(_sql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(_sql);
		// Map userMap = _cursor.getNext();
		return fields;
	}

	/**
	 * 
	 * @param parasNum
	 * @param wconnNode
	 * @param wpppConnNode
	 * @param request
	 * @return
	 */
	public String[] getParasArr(int parasNum, String wconnNode,
			String wpppConnNode, HttpServletRequest request) {

		if (0 == parasNum) {
			return null;
		}
		String devicetype_id = request.getParameter("devicetype_id");
		String service_id = request.getParameter("service_id");
		String username = request.getParameter("username");
		String passwd = request.getParameter("passwd");
		String vpiid = request.getParameter("vpiid");
		String vciid = request.getParameter("vciid");
		String oui = request.getParameter("oui");
		String device_serialnumber = request
				.getParameter("device_serialnumber");
		String wanPort1 = request.getParameter("wanPort1");
		String wanPort2 = request.getParameter("wanPort2");
		String wanPort3 = request.getParameter("wanPort3");
		String wanPort4 = request.getParameter("wanPort4");
		String wanPort5 = request.getParameter("wanPort5");
		String lanIP1 = request.getParameter("lanIP1");
		String lanIP2 = request.getParameter("lanIP2");
		String lanIP3 = request.getParameter("lanIP3");
		String lanIP4 = request.getParameter("lanIP4");
		String lanIP5 = request.getParameter("lanIP5");
		String lanPort1 = request.getParameter("lanPort1");
		String lanPort2 = request.getParameter("lanPort2");
		String lanPort3 = request.getParameter("lanPort3");
		String lanPort4 = request.getParameter("lanPort4");
		String lanPort5 = request.getParameter("lanPort5");

		String[] ArrHandSheet = null;
		ArrHandSheet = new String[parasNum];
		// ArrHandSheet = new String[6];
		if (parasNum == 20) {
			// parasNum == 20是中兴的老设备，要删除桥接，再建路由后，配置参数
			ArrHandSheet[0] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode + ".";
			ArrHandSheet[1] = "PVC:" + vpiid + "/" + vciid;
			ArrHandSheet[2] = username;
			ArrHandSheet[3] = passwd;
			ArrHandSheet[4] = "";
			ArrHandSheet[5] = wanPort1;
			ArrHandSheet[6] = lanPort1;
			ArrHandSheet[7] = lanIP1;
			ArrHandSheet[8] = wanPort2;
			ArrHandSheet[9] = lanPort2;
			ArrHandSheet[10] = lanIP2;
			ArrHandSheet[11] = wanPort3;
			ArrHandSheet[12] = lanPort3;
			ArrHandSheet[13] = lanIP3;
			ArrHandSheet[14] = wanPort4;
			ArrHandSheet[15] = lanPort4;
			ArrHandSheet[16] = lanIP4;
			ArrHandSheet[17] = wanPort5;
			ArrHandSheet[18] = lanPort5;
			ArrHandSheet[19] = lanIP5;
		} else {
			// parasNum == 46是华为的设备，完全配置参数，从桥接变成路由，再配置参数
			ArrHandSheet[0] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".ConnectionType";
			ArrHandSheet[1] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".Username";
			ArrHandSheet[2] = username;
			ArrHandSheet[3] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".Password";
			ArrHandSheet[4] = passwd;
			ArrHandSheet[5] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".Enable";

			ArrHandSheet[6] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.";
			ArrHandSheet[7] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0101-InstanceNumber%.ExternalPort";
			ArrHandSheet[8] = wanPort1;
			ArrHandSheet[9] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0101-InstanceNumber%.InternalPort";
			ArrHandSheet[10] = lanPort1;
			ArrHandSheet[11] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0101-InstanceNumber%.InternalClient";
			ArrHandSheet[12] = lanIP1;
			ArrHandSheet[13] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0101-InstanceNumber%.PortMappingEnabled";

			ArrHandSheet[14] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.";
			ArrHandSheet[15] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0103-InstanceNumber%.ExternalPort";
			ArrHandSheet[16] = wanPort2;
			ArrHandSheet[17] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0103-InstanceNumber%.InternalPort";
			ArrHandSheet[18] = lanPort2;
			ArrHandSheet[19] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0103-InstanceNumber%.InternalClient";
			ArrHandSheet[20] = lanIP2;
			ArrHandSheet[21] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0103-InstanceNumber%.PortMappingEnabled";

			ArrHandSheet[22] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.";
			ArrHandSheet[23] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0105-InstanceNumber%.ExternalPort";
			ArrHandSheet[24] = wanPort3;
			ArrHandSheet[25] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0105-InstanceNumber%.InternalPort";
			ArrHandSheet[26] = lanPort3;
			ArrHandSheet[27] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0105-InstanceNumber%.InternalClient";
			ArrHandSheet[28] = lanIP3;
			ArrHandSheet[29] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0105-InstanceNumber%.PortMappingEnabled";

			ArrHandSheet[30] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.";
			ArrHandSheet[31] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0107-InstanceNumber%.ExternalPort";
			ArrHandSheet[32] = wanPort4;
			ArrHandSheet[33] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0107-InstanceNumber%.InternalPort";
			ArrHandSheet[34] = lanPort4;
			ArrHandSheet[35] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0107-InstanceNumber%.InternalClient";
			ArrHandSheet[36] = lanIP4;
			ArrHandSheet[37] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0107-InstanceNumber%.PortMappingEnabled";

			ArrHandSheet[38] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.";
			ArrHandSheet[39] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0109-InstanceNumber%.ExternalPort";
			ArrHandSheet[40] = wanPort5;
			ArrHandSheet[41] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0109-InstanceNumber%.InternalPort";
			ArrHandSheet[42] = lanPort5;
			ArrHandSheet[43] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0109-InstanceNumber%.InternalClient";
			ArrHandSheet[44] = lanIP5;
			ArrHandSheet[45] = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice."
					+ wconnNode
					+ ".WANPPPConnection."
					+ wpppConnNode
					+ ".PortMapping.%"
					+ devicetype_id
					+ "0109-InstanceNumber%.PortMappingEnabled";

		}
		for (int i = 0; i < ArrHandSheet.length; i++) {
			LOG.debug("GSJ--------------ArrHandSheet"
					+ ArrHandSheet[i]);
		}

		return ArrHandSheet;
	}

	/**
	 * 
	 * @param service_id
	 * @param devicetype_id
	 * @return
	 */
	public String countTemplateParas(String devicetype_id, String service_id) {
		
		String sql = "select count(1) total from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
				+ service_id
				+ " and a.devicetype_id = "
				+ devicetype_id
				+ " and b.tc_serial = c.tc_serial and c.have_defvalue=0";

		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select count(*) total from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
					+ service_id
					+ " and a.devicetype_id = "
					+ devicetype_id
					+ " and b.tc_serial = c.tc_serial and c.have_defvalue=0";
		}

		// logger.debug("GSJ----------Countsql:" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map<String, String> fields = DataSetBean.getRecord(sql);

		String total = fields.get("total");
		
		if("0".equals(total)){
			sql = "select count(1) total from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
				+ service_id
				+ " and a.devicetype_id = "
				+ "0"
				+ " and b.tc_serial = c.tc_serial and c.have_defvalue=0";

			// teledb
			if (DBUtil.GetDB() == DB_MYSQL) {
				sql = "select count(*) total from tab_servicecode a,tab_template_cmd b,tab_template_cmd_para c  where a.template_id = b.template_id and  a.service_id = "
						+ service_id
						+ " and a.devicetype_id = "
						+ "0"
						+ " and b.tc_serial = c.tc_serial and c.have_defvalue=0";
			}

			// logger.debug("GSJ----------Countsql:" + sql);
			PrepareSQL psql1 = new PrepareSQL(sql);
	    	psql1.getSQL();
			Map<String, String> fields_2 = DataSetBean.getRecord(sql);
			
			total = fields.get("total");
		}
		
		return total;
	}



	/**
	 * 
	 * 
	 * @param req
	 * @return
	 */
	public static int changeDevice(HttpServletRequest req) {

		// 用户帐户
		String _username = req.getParameter("username");
		String service_id = req.getParameter("type");
		// 原设备信息

		String oui = req.getParameter("old_oui");
		String serialnumber = req.getParameter("old_device_serialnumber");

		// 更换设备信息
		String _oui = req.getParameter("new_oui");
		String _serialnumber = req.getParameter("new_device_serialnumber");

		ArrayList sqlList = new ArrayList();

		// update 设备资源表，把原来的设备做逻辑删除
		String sql = " update tab_gw_device set device_status=0 where oui='"
				+ oui + "' and device_serialnumber='" + serialnumber + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		sqlList.add(sql);
		sql = " update tab_hgwcustomer set user_state='4' where username='"
				+ _username + "' and oui='" + oui
				+ "' and device_serialnumber='" + serialnumber + "'";
		PrepareSQL psql1 = new PrepareSQL(sql);
    	psql1.getSQL();
		sqlList.add(sql);
		
//		long user_id = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
		
		
		
		String str_userid = "";
		
		if(DBUtil.GetDB() == 1 || DBUtil.GetDB() == 2) {
			String callPro = "maxHgwUserIdProc 1";

			Map map = DataSetBean.getRecord(callPro);
			if (null != map && !map.isEmpty()) {
				str_userid = map.values().toArray()[0].toString();
			} else {
				str_userid = StringUtil.getStringValue(DataSetBean.getMaxId("tab_hgwcustomer","user_id"));
				LOG.debug("----get-str_userid-from-sql-");
			}
		}else {
			str_userid = StringUtil.getStringValue(DbUtils.getUnusedID("sql_tab_hgwcustomer", 1));
		}
		
		
		sql = "insert into tab_hgwcustomer select "
				+ str_userid
				+ ","
				+ "gather_id,username,passwd,city_id,cotno,bill_type_id,next_bill_type_id,cust_type_id,user_type_id,bindtype,virtualnum,"
				+ "numcharacter,access_style_id,aut_flag,service_set,realname,sex,cred_type_id,credno,address,office_id,zone_id,access_kind_id,"
				+ "trade_id,licenceregno,occupation_id,education_id,vipcardno,contractno,linkman,linkman_credno,linkphone,linkaddress,mobile,"
				+ "email,agent,agent_credno,agentphone,adsl_res,adsl_card,adsl_dev,adsl_ser,isrepair,bandwidth,ipaddress,overipnum,ipmask,gateway,"
				+ "macaddress,device_id,device_ip,device_shelf,device_frame,device_slot,device_port,basdevice_id,basdevice_ip,"
				+ "basdevice_shelf,basdevice_frame,basdevice_slot,basdevice_port,vlanid,workid,"
				+ "'1',opendate,onlinedate,pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,vpiid,"
				+ "vciid,adsl_hl,userline,dslamserialno,movedate,dealdate,opmode,maxattdnrate,upwidth,"
				+ "'" + _oui + "','" + _serialnumber + "'," + service_id
				+ ",max_user_number,wan_value_1,wan_value_2,0"
				+ " from tab_hgwcustomer where username='" + _username
				+ "' and oui='" + oui + "' and device_serialnumber='"
				+ serialnumber + "'";

		PrepareSQL psql2 = new PrepareSQL(sql);
    	psql2.getSQL();
		sqlList.add(sql);

		int[] code = DataSetBean.doBatch(sqlList);
		if (code != null && code.length > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public int updateHgwcustomer(HttpServletRequest request) {

		// 用户帐号：
		String _username = request.getParameter("account");
		// 设备oui 和 serialnumber
		String _oui = request.getParameter("old_oui");
		String _serialnumber = request.getParameter("old_device_serialnumber");

		// 业务ID
		String service_id = request.getParameter("service_id");
		String serv_type_id = request.getParameter("some_service");
		String oper_type_id = request.getParameter("oper_type");

		
		String userState = "";

		// if (service_id.equals("101") || service_id.equals("111")) {
		// userState = "1";
		// } else if (service_id.equals("102")) {
		// userState = "2";
		// } else if (service_id.equals("103") || service_id.equals("113")) {
		// userState = "3";
		// } else if (service_id.equals("104")) {
		// userState = "1";
		// }

		// modify by lizhaojun 20080229
		if (oper_type_id.equals("1")) {
			userState = "1";
		} else if (oper_type_id.equals("2")) {
			userState = "2";
		} else if (oper_type_id.equals("3")) {
			userState = "3";
		} else if (oper_type_id.equals("4")) {
			userState = "4";
		} else {
			userState = "1";
		}

		String _sql = "update tab_hgwcustomer set user_state='" + userState
				+ "',serv_type_id=" + serv_type_id + " where username='"
				+ _username + "' and oui='" + _oui
				+ "' and device_serialnumber='" + _serialnumber
				+ "' and user_state in('1','2')";

		PrepareSQL psql = new PrepareSQL(_sql);
    	psql.getSQL();
		int code = DataSetBean.executeUpdate(_sql);
		if (code > 0) {
			return 1;
		} else {
			return 0;
		}

	}

	/**
	 * 获取自动升级任务的详细信息
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getUpdateTaskDetail(HttpServletRequest request) {
		ArrayList list = new ArrayList();
		String task_id = request.getParameter("task_id");
		String success = request.getParameter("successPercent");
		String task_name = request.getParameter("task_name");
		float percent = 0f;
		if (null == success) {
			String tab_autoupdate_sheetSQL = "select count(*) number from tab_strategyupdate_sheet where task_id ="
					+ task_id;
			PrepareSQL psql = new PrepareSQL(tab_autoupdate_sheetSQL);
	    	psql.getSQL();
			HashMap totalCountMap = DataSetBean
					.getRecord(tab_autoupdate_sheetSQL);
			String totalCount = "0";
			if (null != totalCountMap) {
				totalCount = (String) totalCountMap.get("number");
			}

			// 策略任务有对应的工单
			if (!"0".equals(totalCount)) {
				String sheet_success_SQL = "select count(*) number from tab_strategyupdate_sheet a,tab_sheet_report b where a.sheet_id=b.sheet_id and b.fault_code =1 and a.task_id ="
						+ task_id;
				PrepareSQL psql1 = new PrepareSQL(sheet_success_SQL);
		    	psql1.getSQL();
				HashMap successCountMap = DataSetBean
						.getRecord(sheet_success_SQL);
				String successCount = "0";
				if (null != successCountMap) {
					successCount = (String) successCountMap.get("number");
					percent = Float.parseFloat(successCount)
							/ Float.parseFloat(totalCount);
					success = StringUtils.formatNumber(String
							.valueOf(percent * 100), 2);
				} else {
					success = "0";
				}
			} else {
				success = "0";
			}
		}
		String sql = "select a.oui,a.device_serialnumber,b.sheet_id,c.exec_status,c.username,c.exec_desc,c.fault_code,c.fault_desc,c.start_time,c.end_time"
				+ "  from tab_gw_device a inner join tab_strategyupdate_sheet b  on a.device_id=b.device_id left join tab_sheet_report c on b.sheet_id = c.sheet_id"
				+ " where b.task_id=" + task_id;
		PrepareSQL psql2 = new PrepareSQL(sql);
    	psql2.getSQL();
		// 分页
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null) {
			offset = 1;
		} else {
			offset = Integer.parseInt(stroffset);
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar("&task_id=" + task_id
				+ "&successPercent=" + success + "&type=3&task_name="
				+ task_name);
		list.add(strBar);
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);
		list.add(success);
		return list;
	}

	/**
	 * 根据device_id获取设备对应的企业网关用户信息
	 * 
	 * @param device_id
	 * @return String[]
	 */
	public String[] getUserInfoBydeviceID(String device_id) {
		String[] userInfo = new String[3];
		String e_id = "";
		String e_username = "";
		String e_passwd = "";

		String sql = "select b.* from tab_gw_device a,tab_egwcustomer b "
				+ "where a.device_id=b.device_id "
				+ "and a.device_status=1 and b.user_state='1' and a.device_id = '"
				+ device_id + "'";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select b.e_id, b.e_username, b.e_passwd from tab_gw_device a,tab_egwcustomer b "
					+ "where a.device_id=b.device_id "
					+ "and a.device_status=1 and b.user_state='1' and a.device_id = '"
					+ device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		if (fields != null) {
			e_id = (String) fields.get("e_id");
			e_username = (String) fields.get("e_username");
			e_passwd = (String) fields.get("e_passwd");
		}

		userInfo[0] = e_id;
		userInfo[1] = e_username;
		userInfo[2] = e_passwd;

		return userInfo;
	}

	/**
	 * 跟据业务类型、操作类型和上网类型查询出service_id
	 * 
	 * @param servTypeId
	 * @param operTypeId
	 * @param wanType
	 * @return
	 */
	public static String getServiceIdBy(String servTypeId, String operTypeId,
			String wanType) {
		String sql = "select service_id from tab_service where serv_type_id="
				+ servTypeId + " and oper_type_id=" + operTypeId
				+ " and wan_type=" + (operTypeId.equals("1") ? wanType : "-1");
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map map = cursor.getNext();
		if (map != null) {
			// logger.debug("map=" + map);
			return (String) map.get("service_id");
			// logger.debug("re=" + re);
		}
		return null;
	}

	/**
	 * 跟据业务类型、操作类型和上网类型查询出service_id
	 * 
	 * @param servTypeId
	 * @param operTypeId
	 * @param wanType
	 * @return
	 */
	public static String getServiceId(String servTypeId, String operTypeId) {
		String sql = "select service_id from tab_service where serv_type_id="
				+ servTypeId + " and oper_type_id=" + operTypeId;

		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map map = cursor.getNext();
		if (map != null) {
			// logger.debug("map=" + map);
			return (String) map.get("service_id");
			// logger.debug("re=" + re);
		}
		return null;
	}

	/**
	 * 根据用户帐号获取用户和设备信息
	 * 
	 * @author lizj （5202）
	 * @param request
	 * @return
	 */
	public static Map getUserInfo(HttpServletRequest request) {
		Map map = null;
		String user_name = request.getParameter("username");
		if (user_name != null) {
			String sql = "select a.username,a.wan_type, a.passwd,a.vpiid,a.vciid,a.ipaddress,a.ipmask,a.gateway,a.adsl_ser,"
					+ " a.wan_value_1,a.wan_value_2,b.device_id,b.gather_id ,b.devicetype_id,"
					+ " b.oui,b.device_serialnumber,a.customer_id"
					+ " from tab_egwcustomer a,tab_gw_device b  where a.username='"
					+ user_name
					+ "' and a.user_state in ('1','2') and  a.device_id=b.device_id and b.gw_type=2";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			map = DataSetBean.getRecord(sql);
		}
		return map;
	}


	public String[] setVpnParam(Map map) {
		String vpn = (String) map.get("vpn");
		String[] param = new String[4];

		if (vpn == null || vpn.equals("")) {
			vpn = "0";
		}

		param[0] = vpn;
		param[1] = vpn;
		param[2] = vpn;
		param[3] = vpn;
		return param;

	}


	/**
	 * 详细设置终端连接数目
	 * 
	 * @param topbox
	 * @param topbox_num
	 * @param photo
	 * @param photo_num
	 * @param computer
	 * @param computer_num
	 * @param telephone
	 * @param telephone_num
	 * @return
	 */
	public String[] setDetailParam(Map map) {
		String topbox = (String) map.get("topbox");
		String topbox_num = (String) map.get("topbox_num");
		String photo = (String) map.get("photo");
		String photo_num = (String) map.get("photo_num");
		String computer = (String) map.get("computer");
		String computer_num = (String) map.get("computer_num");
		String telephone = (String) map.get("telephone");
		String telephone_num = (String) map.get("telephone_num");

		String[] param = new String[8];
		if (topbox == null || topbox.equals("")) {
			topbox = "0";
			topbox_num = "0";
		}

		if (photo == null || photo.equals("")) {
			photo = "0";
			photo_num = "0";
		}

		if (computer == null || computer.equals("")) {
			computer = "0";
			computer_num = "0";
		}
		if (telephone == null || telephone.equals("")) {
			telephone = "0";
			telephone_num = "0";
		}

		param[0] = topbox_num;
		param[1] = topbox;
		param[2] = photo_num;
		param[3] = photo;
		param[4] = computer_num;
		param[5] = computer;
		param[6] = telephone_num;
		param[7] = telephone;

		return param;
	}

	/**
	 * 设置接入安全的参数数组
	 * 
	 * @param firewall
	 * @param wall_level
	 * @param uiface
	 * @param allawip
	 * @param allowport
	 * @param attack
	 * @return
	 */
	public String[] setSecurity(Map map) {
		String firewall = (String) map.get("firewall");
		String wall_level = (String) map.get("firewall");
		String uiface = (String) map.get("firewall");
		String allawip = (String) map.get("firewall");
		String allowport = (String) map.get("firewall");
		String attack = (String) map.get("firewall");

		String[] param = new String[6];

		LOG.debug("firewall :" + firewall);
		LOG.debug("wall_level :" + wall_level);
		LOG.debug("uiface :" + uiface);
		LOG.debug("allawip :" + allawip);
		LOG.debug("allowport :" + allowport);
		LOG.debug("attack :" + attack);

		if (firewall == null || firewall.equals("")) {
			firewall = "0";
			wall_level = "L";
		}

		if (uiface == null || uiface.equals("")) {
			allawip = "";
			allowport = "0";
			uiface = "0";
		}
		if (attack == null || attack.equals("")) {
			attack = "0";
		}

		param[0] = wall_level;
		param[1] = firewall;
		param[2] = allawip;
		param[3] = allowport;
		param[4] = uiface;
		param[5] = attack;

		return param;
	}

	/**
	 * 路由开户需要的工单参数数组
	 * 
	 * @param vpiid
	 * @param vciid
	 * @param connection_type
	 * @param username
	 * @param passwd
	 * @param lanInterface
	 * @param service_list
	 * @param ssid1_username
	 * @param ssid1_passwd
	 * @param ssid2_username
	 * @param ssid2_passwd
	 * @param lanInterface1
	 * @param lanInterface2
	 * @param boss_control
	 * @return
	 */
	public String[] setParamRoute(Map map) {

		String vpiid = (String) map.get("vpiid");
		String vciid = (String) map.get("vciid");
		// String connection_type = (String) map.get("connection_type");
		String username = (String) map.get("username");
		String passwd = (String) map.get("passwd");
		String[] lanInterface = (String[]) map.get("lanInterface");
		// String service_list = (String) map.get("service_list");
		// String ssid1_username = (String) map.get("ssid1_username");
		// String ssid1_passwd = (String) map.get("ssid1_passwd");
		// String ssid2_username = (String) map.get("ssid2_username");
		// String ssid2_passwd = (String) map.get("ssid2_passwd");
		// String[] lanInterface1 = (String[]) map.get("lanInterface1");
		// String boss_control = (String) map.get("boss_control");

		String[] param = new String[4];
		String _LanInterface = "";
		// WANConnection绑定的端口

		if (lanInterface != null && lanInterface.length > 0) {
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		} else {
			_LanInterface = "";
		}
		LOG.debug("_LanInterface :" + _LanInterface);

		// // VLAN2绑定的端口
		// String _LanInterface1 = "";
		// if (lanInterface1 != null) {
		// for (int i = 0; i < lanInterface1.length; i++) {
		// if (i == 0) {
		// _LanInterface1 = lanInterface1[i];
		// } else {
		// _LanInterface1 += "," + lanInterface1[i];
		// }
		// }
		// }
		//
		// logger.debug("_LanInterface2 :" + _LanInterface1);
		//
		// param[0] = ssid1_username;
		// param[1] = ssid1_passwd;
		// param[2] = ssid2_username;
		// param[3] = ssid2_passwd;
		param[0] = "PVC:" + vpiid + "/" + vciid;
		param[1] = username;
		param[2] = passwd;
		// param[7] = service_list;
		param[3] = _LanInterface;
		// param[9] = boss_control;
		return param;

	}

	private Map initParam(HttpServletRequest request) {
		Map map = new HashMap();
		map.put("vpiid", request.getParameter("vpiid"));
		map.put("vciid", request.getParameter("vciid"));
		map.put("connection_type", request.getParameter("connection_type"));
		map.put("lanInterface", request.getParameterValues("LanInterface"));
		map.put("service_list", request.getParameter("service_list"));
		map.put("ssid1_username", request.getParameter("ssid1_username"));
		map.put("ssid1_passwd", request.getParameter("ssid1_passwd"));
		map.put("ssid2_username", request.getParameter("ssid2_username"));
		map.put("ssid2_passwd", request.getParameter("ssid2_passwd"));
		map.put("lanInterface1", request.getParameterValues("LanInterface1"));
		map.put("boss_control", request.getParameter("boss_control"));
		map.put("wan_value_1", request.getParameter("wan_value_1"));

		map.put("serv_type_id", request.getParameter("some_service"));
		map.put("wan_type", request.getParameter("wan_type_"));

		map.put("curUser", (UserRes) (request.getSession()
				.getAttribute("curUser")));

		map.put("device_id", request.getParameter("device_id"));
		map.put("service_id", request.getParameter("service_id"));
		map.put("devicetype_id", request.getParameter("devicetype_id"));
		map.put("gather_id", request.getParameter("gather_id"));
		map.put("oui", request.getParameter("oui"));
		map.put("device_serialnumber", request
				.getParameter("device_serialnumber"));
		map.put("oui_new", request.getParameter("oui_new"));
		map.put("device_serialnumber_new", request
				.getParameter("device_serialnumber_new"));
		map.put("username", request.getParameter("username"));

		map.put("maxnum", request.getParameter("maxnum"));

		map.put("topbox", request.getParameter("topbox"));
		map.put("topbox_num", request.getParameter("topbox_num"));
		map.put("photo", request.getParameter("photo"));
		map.put("photo_num", request.getParameter("photo_num"));
		map.put("computer", request.getParameter("computer"));
		map.put("computer_num", request.getParameter("computer_num"));
		map.put("telephone", request.getParameter("telephone"));
		map.put("telephone_num", request.getParameter("telephone_num"));

		map.put("vpn", request.getParameter("vpn"));

		map.put("ExternalIPAddress", request.getParameter("ipaddress"));
		map.put("SubnetMask", request.getParameter("ipmask"));
		map.put("DefaultGateway", request.getParameter("gateway"));
		map.put("DNSServers", request.getParameter("adsl_ser"));

		return map;
	}

	/**
	 * IPTV开户和ADSL-IPCamera开户 ADSL-IPCamera销户
	 * 
	 * @param map
	 * @return
	 */
	private int iptvOpen(Map map) {
		return 1;
	}


	/**
	 * 根据采集点获取ior信息
	 * 
	 * @param gather_id
	 * @param user
	 * @return
	 */
	public String getIor(String gather_id, User user) {

		return user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
	}

	/**
	 * 组装工单参数数组
	 * 
	 * @author lizj （5202）
	 * @param sheet_id
	 * @return
	 */
	public String[] setParam(Map map) {
		String[] param = new String[2];
		String vpiid = (String) map.get("vpiid");
		String vciid = (String) map.get("vciid");
		// String connection_type = (String) map.get("connection_type");
		String[] lanInterface = (String[]) map.get("lanInterface");
		// String service_list = (String) map.get("service_list");
		// String ssid1_username = (String) map.get("ssid1_username");
		// String ssid1_passwd = (String) map.get("ssid1_passwd");
		// String ssid2_username = (String) map.get("ssid2_username");
		// String ssid2_passwd = (String) map.get("ssid2_passwd");
		// String[] lanInterface1 = (String[]) map.get("lanInterface1");
		// String boss_control = (String) map.get("boss_control");

		String _LanInterface = null;
		// WANConnection绑定的端口
		if (lanInterface != null && lanInterface.length > 0) {
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		} else {
			_LanInterface = "";
		}

		String _LanInterface1 = null;

		// VLAN1绑定的端口
		// if (lanInterface1 != null && lanInterface1.length > 0) {
		// for (int i = 0; i < lanInterface1.length; i++) {
		// if (i == 0) {
		// _LanInterface1 = lanInterface1[i];
		// } else {
		// _LanInterface1 += "," + lanInterface1[i];
		// }
		// }
		// }

		// param[0] = ssid1_username;
		// param[1] = ssid1_passwd;
		// param[2] = ssid2_username;
		// param[3] = ssid2_passwd;
		param[0] = "PVC:" + vpiid + "/" + vciid;
		param[1] = _LanInterface;
		// param[6] = service_list;
		// param[7] = _LanInterface1;
		// param[8] = boss_control;

		for (int i = 0; i < param.length; i++) {
			LOG.debug("param[{}]={}", i, param[i]);
		}
		return param;
	}

	/**
	 * 组装工单参数数组
	 * 
	 * @param map
	 * @param type
	 *            <li>1:STATIC</li>
	 *            <li>2:DHCP</li>
	 * @return
	 */
	public String[] setParam(Map map, int type) {
		String[] param = null;
		String vpiid = (String) map.get("vpiid");
		String vciid = (String) map.get("vciid");
		String[] lanInterface = (String[]) map.get("lanInterface");

		String _LanInterface = null;
		if (lanInterface != null && lanInterface.length > 0) {
			for (int i = 0; i < lanInterface.length; i++) {
				if (i == 0) {
					_LanInterface = lanInterface[i];
				} else {
					_LanInterface += "," + lanInterface[i];
				}
			}
		} else {
			_LanInterface = "";
		}

		if (type == 3) {
			param = new String[6];
			param[0] = "PVC:" + vpiid + "/" + vciid;
			param[1] = (String) map.get("ExternalIPAddress");
			param[2] = (String) map.get("SubnetMask");
			param[3] = (String) map.get("DefaultGateway");
			param[4] = (String) map.get("DNSServers");
			param[5] = _LanInterface;
		} else {
			param = new String[2];
			param[0] = "PVC:" + vpiid + "/" + vciid;
			param[1] = _LanInterface;
		}

		for (int i = 0; i < param.length; i++) {
			LOG.debug("param[{}]={}", i, param[i]);
		}
		return param;
	}

}
