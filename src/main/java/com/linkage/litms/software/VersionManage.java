
package com.linkage.litms.software;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.setData;
import Performance.setDataReturn;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author lizhaojun
 * @version 1.00, 5/10/2007
 * @since HGW 1.0
 * @Modify
 *        <p>
 *        2007-06-18 Alex.Yan (yanhj@lianchuang.com) <br>
 *        RemoteDB ACS.
 *        <p>
 *        2007-08-08 Alex.Yan (yanhj@lianchuang.com) <br>
 *        file_path_3 + "/" + filename_3.
 *        <p>
 *        2007-08-30 Alex.Yan (yanhj@lianchuang.com) <br>
 *        file_path_3 + "/" + filename_3 + "&type=1".
 */
public class VersionManage
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(VersionManage.class);
	/**
	 * 设备自动升级策略
	 */
	private String autoUpdate_InsertSQL = "insert into tab_device_autoupdate(task_id,task_name,task_type,username,task_time,type,is_check,is_over) values(?,?,?,?,?,?,?,?)";
	/**
	 * 策略工单对应表
	 */
	private String autoupdate_sheet_InsertSQL = "insert into tab_autoupdate_sheet(task_id,device_id,sheet_id,is_over) values(?,?,?,?)";
	/**
	 * 策略任务测试表
	 */
	private String autoupdate_test_InsertSQL = "insert into tab_autoupdate_test(task_id,device_id,sheet_id) values(?,?,?)";
	/**
	 * 展示策略任务
	 */
	private String autoupdate_task_SelectSQL = "select * from tab_device_autoupdate where task_type=? order by task_time desc";
	/**
	 * 任务下各工单详细信息
	 */
	private String autoupdate_taskdetail_SQL = "select * from tab_autoupdate_sheet where task_id=?";
	/**
	 * 根据工单id，来查询对应的工单信息
	 */
	private String sheetInfo_SQL = "select * from tab_sheet where sheet_id in(?)";
	/**
	 * 策略任务测试表
	 */
	private String autoupdate_tasktest_InsertSQL = "insert into tab_autoupdate_test(task_id，device_id,sheet_id) values(?,?,?)";
	/**
	 * 更新工单对应表的部分工单完成情况
	 */
	private String autoupdate_task_markSQL = "update tab_autoupdate_sheet set is_over=1 where task_id=? and device_id=? and sheet_id=?";
	/**
	 * 更新工单对应表的部分工单完成情况
	 */
	private String autoupdate_task_markSQL_jiangsu = "update tab_strategyupdate_sheet set sheet_status=1 where task_id=? and device_id=? and sheet_id=?";
	/**
	 * 搜索这些设备中没有完成的某个类型的工单数目
	 */
	private String isAllowConfig_SQL = "select count(*) number from tab_device_autoupdate a,tab_autoupdate_sheet b where a.task_id = b.task_id and a.is_check!=2 and a.task_type=? and b.device_id in(?) and b.is_over=0";

	/**
	 * @author lizhaojun
	 * @param request
	 * @return Stirng htkl
	 */
	public String getDeviceHtml(HttpServletRequest request, boolean needFilter)
	{
		String gather_id = request.getParameter("gather_id");
		String devicetype_id = request.getParameter("devicetype_id");
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select * from tab_gw_device a ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select oui, device_serialnumber, device_id from tab_gw_device a ";
		}
		if (!user.isAdmin())
		{
			tmpSql += " , tab_gw_res_area b ";
		}
		tmpSql += " where a.device_status=1 ";
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and a.device_id = b.res_id and b.area_id="
					+ area_id + " ";
		}
		if (gather_id != null && !gather_id.equals(""))
		{
			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id =" + devicetype_id + " ";
		}
		tmpSql += " order by a.device_id";
		// logger.debug("deviceresource Sql---------------------:" +
		// tmpSql);
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无符合条件的设备</span></td></tr>";
		}
		else
		{
			int iflag = 1;
			while (fields != null)
			{
				// 获取用户名字,显示用
				tmpUserSql = "select username from tab_hgwcustomer where oui='"
						+ (String) fields.get("oui") + "' and device_serialnumber='"
						+ (String) fields.get("device_serialnumber") + "'";
				psql = new PrepareSQL(tmpUserSql);
				psql.getSQL();
				Map fields2 = DataSetBean.getRecord(tmpUserSql);
				// logger.debug("GSJ============"+tmpUserSql);
				if (fields2 == null)
				{
					tmpUser = "|";
				}
				else
				{
					tmpUser = (String) fields2.get("username");
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
				iflag++;
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * 获取任务下的设备
	 * 
	 * @param task_id
	 * @return
	 */
	public Cursor getDeviceByTaskID(String task_id)
	{
		String sql = "select b.oui,b.device_serialnumber,a.sheet_id,a.device_id from tab_autoupdate_sheet a, tab_gw_device b"
				+ " where a.device_id = b.device_id and a.task_id=" + task_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	/**
	 * 获取任务下的设备
	 * 
	 * @param task_id
	 * @return
	 */
	public Cursor getDeviceByTaskID_jiangsu(String task_id)
	{
		String sql = "select b.oui,b.device_serialnumber,a.sheet_id,a.device_id from tab_strategyupdate_sheet a, tab_gw_device b"
				+ " where a.device_id = b.device_id and sheet_serial=1 and a.task_id="
				+ task_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		return cursor;
	}

	/**
	 * 返回任务是否已测试
	 * 
	 * @param request
	 * @return
	 */
	public boolean isTest(String task_id)
	{
		boolean result = false;
		String sql = "select count(*) number from tab_autoupdate_test where task_id="
				+ task_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap record = DataSetBean.getRecord(sql);
		if (Long.parseLong((String) record.get("number")) > 0)
		{
			result = true;
		}
		return result;
	}

	/**
	 * 审核策略
	 * 
	 * @param request
	 */
	public void checkAutoUpdateTask(HttpServletRequest request)
	{
		String task_id = request.getParameter("task_id");
		String checkResult = request.getParameter("checkResult");
		if ("pass".equals(checkResult))
		{
			checkResult = "1";
		}
		else
		{
			checkResult = "2";
		}
		String sql = "update tab_device_autoupdate set is_check=" + checkResult
				+ " where task_id=" + task_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		// 执行更改
		DataSetBean.executeUpdate(sql);
	}

	/**
	 * 如果action为check，则返回为审核的任务，否则返回所有的任务 type为1，返回版本升级的策略，为2，则返回配置升级的策略
	 * 
	 * @param request
	 * @return
	 */
	public ArrayList getAutoUpdateTaskInfo(HttpServletRequest request)
	{
		ArrayList list = new ArrayList();
		String param = "";
		int type = 1;
		if (null != request.getParameter("type"))
		{
			type = Integer.parseInt((String) request.getParameter("type"));
		}
		param = "&type=" + type;
		Cursor cursor = null;
		PrepareSQL pSQL = new PrepareSQL();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			autoupdate_task_SelectSQL = "select task_id, task_name, username, type, is_check, is_over from tab_device_autoupdate where task_type=? order by task_time desc";
		}

		pSQL.setSQL(autoupdate_task_SelectSQL);
		if ("check".equals(request.getParameter("action")))
		{
			param += "&action=check";
			pSQL.setStringExt(1, type + " and is_check=0 ", false);
		}
		else
		{
			pSQL.setInt(1, type);
		}
		logger.debug("getAutoUpdateTaskInfo_SQL:" + pSQL.getSQL());
		// 分页
		String stroffset = request.getParameter("offset");
		int pagelen = 30;
		int offset;
		if (stroffset == null)
		{
			offset = 1;
		}
		else
		{
			offset = Integer.parseInt(stroffset);
		}
		QueryPage qryp = new QueryPage();
		qryp.initPage(pSQL.getSQL(), offset, pagelen);
		String strBar = qryp.getPageBar(param);
		list.add(strBar);
		cursor = DataSetBean.getCursor(pSQL.getSQL());
		list.add(cursor);
		return list;
	}

	/**
	 * 查询任务 是否完成
	 * 
	 * @param task_id
	 * @return
	 */
	public boolean isTaskFinish(String task_id)
	{
		boolean isFinish = false;
		String sql = "select count(*) number from tab_autoupdate_sheet where is_over=0 and task_id="
				+ task_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		HashMap record = DataSetBean.getRecord(sql);
		if (Long.parseLong((String) record.get("number")) == 0)
		{
			isFinish = true;
		}
		return isFinish;
	}

	/**
	 * 显示自动升级设备信息
	 * 
	 * @param request
	 * @return
	 */
	public Cursor getAutoUpdateDeviceInfo(HttpServletRequest request)
	{
		Cursor cursor = new Cursor();
		String selectSQL = "select b.oui,b.device_serialnumber,b.loopback_ip,a.sheet_id,a.type from tab_device_autoupdate a,tab_gw_device b ";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		// 不是admin
		if (!user.isAdmin())
		{
			selectSQL += ",tab_gw_res_area c ";
		}
		selectSQL += " where a.device_id = b.device_id ";
		// 不是admin的情况,加上设备的权限控制
		if (!user.isAdmin())
		{
			selectSQL += " and a.device_id=c.res_id and c.res_type=1 and c.area_id="
					+ user.getAreaId();
		}
		PrepareSQL psql = new PrepareSQL(selectSQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(selectSQL);
		return cursor;
	}

	/**
	 * 这个策略是否允许配置
	 * 
	 * @param request
	 * @return true:允许配置 false:不允许配置
	 */
	public boolean isAllowConfig(HttpServletRequest request)
	{
		String[] device_list = request.getParameterValues("device_id");
		String auto_type = request.getParameter("auto_type");
		String device_idStr = null;
		if (null == device_list || 0 == device_list.length || null == auto_type
				|| "".equals(auto_type))
		{
			return false;
		}
		// 为sql语句的device_id作准备
		for (int i = 0; i < device_list.length; i++)
		{
			if (null == device_idStr)
			{
				device_idStr = "'" + device_list[i] + "'";
			}
			else
			{
				device_idStr += ",'" + device_list[i] + "'";
			}
		}
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(isAllowConfig_SQL);
		pSQL.setStringExt(1, auto_type, false);
		pSQL.setStringExt(2, device_idStr, false);
		logger.debug("isAllowConfig_SQL:" + pSQL.getSQL());
		HashMap record = DataSetBean.getRecord(pSQL.getSQL());
		if (null == record)
		{
			return false;
		}
		// 如果没有查到记录,则允许配置
		if (Integer.parseInt((String) record.get("number")) == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * @author benyp 根据设备版本结合tab_devicetype_info查询
	 * @param request
	 * @return Stirng htkl
	 */
	public String getDeviceHtmlUseDeviceVersion(HttpServletRequest request,
			boolean needFilter)
	{
		String gather_id = request.getParameter("gather_id");// 采集地
		String vendor_id = request.getParameter("vendor_id");// 设备厂商
		String softwareversion = request.getParameter("softwareversion");
		String devicetype_id = request.getParameter("devicetype_id");
		String hguser = request.getParameter("hguser");
		String telephone = request.getParameter("telephone");
		String serialnumber = request.getParameter("serialnumber");
		String gw_type = request.getParameter("gw_type");
		//
		String type = "";
		if (needFilter)
			type = "radio";
		else
			type = "checkbox";
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select * from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			if ((null != hguser && !"".equals(hguser))
					|| (null != telephone && !"".equals(telephone)))
			{
				tmpSql = "select d.username, a.device_id, oui, device_serialnumber from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
			}else{
				tmpSql = "select a.device_id, oui, device_serialnumber from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
			}
			
		}
		// // 非admin用户
		// if (!user.isAdmin()) {
		// tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		// }
		// 按用户查询方式
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone)))
		{
			if (LipossGlobals.IsITMS())
			{
				tmpSql += " inner join tab_hgwcustomer d on a.device_id = d.device_id ";
			}
			else
			{
				tmpSql += " inner join tab_egwcustomer d on a.device_id = d.device_id ";
			}
		}
		tmpSql += " where a.device_status=1 ";
		// 非省中心用户
		if (null != user.getCityId() && !"00".equals(user.getCityId())
				&& !"".equals(user.getCityId()))
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(user.getCityId());
			tmpSql += " and a.city_id in (" + StringUtils.weave(list) + ")";
			list.clear();
		}
		if (null != gw_type && !"".equals(gw_type))
		{
			tmpSql += " and a.gw_type = " + gw_type;
		}
		// // 非admin用户
		// if (!user.isAdmin()) {
		// tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		// }
		// 按用户查询方式
		if (null != hguser && !"".equals(hguser))
		{
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone))
		{
			tmpSql += " and d.phonenumber ='" + telephone + "'";
		}
		if (gather_id != null && !gather_id.equals(""))
		{
			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (vendor_id != null && !vendor_id.equals(""))
		{
			tmpSql += " and a.vendor_id ='" + vendor_id + "'";
		}
		if (softwareversion != null && !softwareversion.equals(""))
		{
			tmpSql += " and c.softwareversion ='" + softwareversion + "'";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id =" + devicetype_id + " ";
		}
		if (serialnumber != null && !"".equals(serialnumber))
		{
			if (serialnumber.length() > 5)
			{
				tmpSql += " and a.dev_sub_sn ='"
						+ serialnumber.substring(serialnumber.length() - 6, serialnumber
								.length()) + "'";
			}
			tmpSql += " and a.device_serialnumber like '%" + serialnumber + "'";
		}
		tmpSql += " order by a.device_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "";// <table border='0' align='center' width='100%'
		// cellpadding='0' cellspacing='1'
		// bgcolor='#999999'>";
		if (fields == null)
		{
			serviceHtml += "无符合条件的设备";
		}
		else
		{
			while (fields != null)
			{
				tmpUser = (String) fields.get("username");
				// logger.debug(" tmpUser:" + tmpUser);
				if (tmpUser == null || tmpUser.equalsIgnoreCase("null"))
				{
					tmpUser = "";
				}
				else
				{
					tmpUser = "|" + tmpUser;
				}
				serviceHtml += "<input type=\"" + type
						+ "\" id=\"device_id\" name=\"device_id\" value=\""
						+ (String) fields.get("device_id") + "\"";
				if (needFilter)
				{
					serviceHtml += " onclick=\"filterByDevID(" + devicetype_id + ")\">";
				}
				else
				{
					serviceHtml += ">";
				}
				serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
						+ (String) fields.get("device_serialnumber") + tmpUser + "<br>";
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		// logger.debug("serviceHtml:"+serviceHtml);
		return serviceHtml;
	}

	/**
	 * @author benyp 根据设备版本结合tab_devicetype_info查询
	 * @param request
	 * @return Stirng htkl
	 */
	public String getDeviceHtmlUseDeviceVersionforSearch(HttpServletRequest request,
			boolean needFilter)
	{
		String gather_id = request.getParameter("gather_id");// 采集地
		// String oui = request.getParameter("vendor_id");// 设备厂商
		String vendor_id = request.getParameter("vendor_id");// 设备厂商
		String softwareversion = request.getParameter("softwareversion");
		String devicetype_id = request.getParameter("devicetype_id");
		String hguser = request.getParameter("hguser");
		String telephone = request.getParameter("telephone");
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select * from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select oui, device_serialnumber, cpe_username, cpe_passwd, acs_username, acs_passwd, x_com_username, x_com_passwd " +
					"from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		}
		// 非admin用户
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		// 按用户查询方式
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone)))
		{
			tmpSql += " inner join tab_egwcustomer d on a.device_id = d.device_id ";
		}
		tmpSql += " where a.device_status=1";
		// 非admin用户
		if (!user.isAdmin())
		{
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}
		// 按用户查询方式
		if (null != hguser && !"".equals(hguser))
		{
			tmpSql += " and d.username like '%" + hguser + "%'";
		}
		if (null != telephone && !"".equals(telephone))
		{
			tmpSql += " and d.phonenumber like '%" + telephone + "%'";
		}
		if (gather_id != null && !gather_id.equals(""))
		{
			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (vendor_id != null && !vendor_id.equals(""))
		{
			tmpSql += " and c.vendor_id ='" + vendor_id + "'";
		}
		if (softwareversion != null && !softwareversion.equals(""))
		{
			tmpSql += " and c.softwareversion ='" + softwareversion + "'";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id =" + devicetype_id + " ";
		}
		tmpSql += " order by a.device_id";
		// logger.debug("deviceresource Sql---------------------:" +
		// tmpSql);
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		/*
		 * if (fields == null) { serviceHtml += "<tr bgcolor='#FFFFFF'>"; serviceHtml += "<td colspan='2'><span>无符合条件的设备</span></td></tr>"; }
		 * else { int iflag = 1; while (fields != null) { // 获取用户名字,显示用 // tmpUserSql =
		 * "select username from tab_hgwcustomer where oui='" // + (String)
		 * fields.get("oui") // + "' and device_serialnumber='" // + (String)
		 * fields.get("device_serialnumber") // + "'"; // Map fields2 =
		 * DataSetBean.getRecord(tmpUserSql); // //
		 * logger.debug("GSJ============"+tmpUserSql);
		 * 
		 * tmpUser = (String) fields.get("username"); logger.debug(" tmpUser:"+tmpUser);
		 * if (tmpUser == null||tmpUser.equalsIgnoreCase("null")) { tmpUser = "|";
		 * logger.debug(" username null"); }
		 * 
		 * if (iflag % 2 == 0) { serviceHtml += "<tr class='green_foot'>"; serviceHtml += "<td width='2%'>";
		 * serviceHtml += "<input type=\"checkbox\" id=\"device_id\" name=\"device_id\"
		 * value=\"" + (String) fields.get("device_id") + "\""; if (needFilter) {
		 * serviceHtml += " onclick=\"filterByDevID()\">"; } else { serviceHtml += ">"; }
		 * serviceHtml += "</td><td  width='98%'>"; serviceHtml += "&nbsp;" + (String)
		 * fields.get("oui") + "-" + (String) fields .get("device_serialnumber") + " | " +
		 * tmpUser; serviceHtml += "</td></tr>"; } else { serviceHtml += "<tr>";
		 * serviceHtml += "<td bgcolor='#FFFFFF' width='2%'>"; serviceHtml += "<input
		 * type=\"checkbox\" id=\"device_id\" name=\"device_id\" value=\"" + (String)
		 * fields.get("device_id") + "\""; if (needFilter) { serviceHtml += "
		 * onclick=\"filterByDevID()\">"; } else { serviceHtml += ">"; }
		 * 
		 * serviceHtml += "</td><td bgcolor='#FFFFFF' width='98%'>"; serviceHtml +=
		 * "&nbsp;" + (String) fields.get("oui") + "-" + (String) fields
		 * .get("device_serialnumber") + " | " + tmpUser; serviceHtml += "</td></tr>"; }
		 * 
		 * //iflag++;
		 * 
		 * 
		 * 
		 * 
		 * fields = cursor.getNext(); } }
		 */
		// edit by benyp
		if (fields == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无符合条件的设备</span></td></tr>";
		}
		else
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<th>设备OUI</th><th>设备序列号</th><th>CPE用户名</th><th>CPE密码</th><th>ACS用户名</th><th>ACS密码</th><th>电信维护账号</th><th>电信维护密码</th></tr>";
			while (fields != null)
			{
				serviceHtml += "<tr>";
				serviceHtml += "<td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("oui");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("device_serialnumber");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("cpe_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("cpe_passwd");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("acs_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("acs_passwd");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("x_com_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("x_com_passwd");
				serviceHtml += "</td></tr>";
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * @author benyp 根据OUI查询
	 * @param request
	 * @return Stirng htkl
	 */
	public String getDeviceHtmlUseOUI(HttpServletRequest request, boolean needFilter)
	{
		String oui = request.getParameter("oui");// 采集地
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String tmpUserSql = "";
		String tmpUser = "";
		String tmpSql = "select * from tab_gw_device a , tab_devicetype_info c   ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select oui, device_serialnumber, cpe_username, cpe_passwd, acs_username, acs_passwd, x_com_username, x_com_passwd " +
					"from tab_gw_device a , tab_devicetype_info c   ";
		}
		// 非admin用户
		if (!user.isAdmin())
		{
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}
		tmpSql += " where a.devicetype_id=c.devicetype_id and a.device_status !=-1";
		if (oui != null && !oui.equals(""))
		{
			// tmpSql += " and c.oui like'%" + oui + "%'";
			tmpSql += " and a.oui like'%" + oui + "%'";
		}
		tmpSql += " order by a.device_id";
		// logger.debug("deviceresource Sql---------------------:" +
		// tmpSql);
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>";
		if (fields == null)
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<td colspan='2'><span>无符合条件的设备</span></td></tr>";
		}
		else
		{
			serviceHtml += "<tr bgcolor='#FFFFFF'>";
			serviceHtml += "<th>设备OUI</th><th>设备序列号</th><th>CPE用户名</th><th>CPE密码</th><th>ACS用户名</th><th>ACS密码</th><th>电信维护账号</th><th>电信维护密码</th></tr>";
			while (fields != null)
			{
				serviceHtml += "<tr>";
				serviceHtml += "<td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("oui");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("device_serialnumber");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("cpe_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("cpe_passwd");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("acs_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("acs_passwd");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("x_com_username");
				serviceHtml += "</td><td bgcolor='#FFFFFF'>";
				serviceHtml += (String) fields.get("x_com_passwd");
				serviceHtml += "</td></tr>";
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</table>";
		return serviceHtml;
	}

	/**
	 * @author lizhaojun 查询文件服务器和配置文件表，生成下拉框返回
	 * @param String
	 *            selectName
	 * @return String
	 */
	public String getFilePath_1(String selectName)
	{
		String tmpSql = "select * from tab_file_server where file_type =2";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select outter_url, dir_id, server_dir from tab_file_server where file_type =2";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='" + selectName
				+ "' class='bk' onchange=showChild('" + selectName + "')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				/*
				 * checkList += "<option value='" + fields.get("outter_url") + "/" +
				 * fields.get("server_dir") + "/" + fields.get("verconfile_name") + "'>" +
				 * fields.get("outter_url") + "/" + fields.get("server_dir") + "/" +
				 * fields.get("verconfile_name") + "</option>";
				 */
				checkList += "<option value='" + fields.get("outter_url") + "|"
						+ fields.get("dir_id") + "'>" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}

	/**
	 * @author lizhaojun 查询文件服务器和配置文件表，生成下拉框返回
	 * @param String
	 *            selectName
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getFilePath_2(String selectName, String devicetype_id,
			String device_id, boolean isFromLocal)
	{
		String tmpSql = " select * from tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = " select outter_url, server_dir, verconfile_name, verconfile_size " +
					"from tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id = " + devicetype_id;
		}
		if (device_id != null && !device_id.equals(""))
		{
			if (devicetype_id != null && !devicetype_id.equals(""))
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
			else
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
		}
		else
		{
			if (isFromLocal)
			{
				tmpSql += " and a.device_id is null ";
			}
			else
			{
				tmpSql += " and a.device_id is not null ";
			}
		}
		tmpSql += "  order by a.verconfile_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='" + selectName
				+ "' class='bk' onchange=showChild('" + selectName + "')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/" + fields.get("verconfile_name")
						+ "|" + fields.get("verconfile_name") + "|"
						+ fields.get("verconfile_size") + "'>" + fields.get("outter_url")
						+ "/" + fields.get("server_dir") + "/"
						+ fields.get("verconfile_name") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}
	
	
	
	public String getFilePath_stb(String selectName, String devicetype_id,
			String device_id, boolean isFromLocal)
	{
		String tmpSql = " select * from stb_tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = " select outter_url, server_dir, verconfile_name, verconfile_size " +
					"from stb_tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id = " + devicetype_id;
		}
		if (device_id != null && !device_id.equals(""))
		{
			if (devicetype_id != null && !devicetype_id.equals(""))
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
			else
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
		}
		else
		{
			if (isFromLocal)
			{
				tmpSql += " and a.device_id is null ";
			}
			else
			{
				tmpSql += " and a.device_id is not null ";
			}
		}
		tmpSql += "  order by a.verconfile_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='" + selectName
				+ "' class='bk' onchange=showChild('" + selectName + "')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/" + fields.get("verconfile_name")
						+ "|" + fields.get("verconfile_name") + "|"
						+ fields.get("verconfile_size") + "'>" + fields.get("outter_url")
						+ "/" + fields.get("server_dir") + "/"
						+ fields.get("verconfile_name") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}
	
	/**
	 * @author lizhaojun 查询文件服务器和配置文件表，生成下拉框返回
	 * @param String
	 *            selectName
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getFilePath_3(String selectName, String devicetype_id,
			String device_id, boolean isFromLocal,String gw_type)
	{
		String tmpSql = " select * from tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = " select outter_url, server_dir, verconfile_name, verconfile_size " +
					"from tab_vercon_file a,tab_file_server b where a.dir_id = b.dir_id and a.verconfile_isexist =1 ";
		}
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id = " + devicetype_id;
		}
		if (device_id != null && !device_id.equals(""))
		{
			if (devicetype_id != null && !devicetype_id.equals(""))
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
			else
			{
				tmpSql += " and a.device_id = '" + device_id + "'";
			}
		}
		else
		{
			if (isFromLocal)
			{
				tmpSql += " and a.device_id is null ";
			}
			else
			{
				tmpSql += " and a.device_id is not null ";
			}
		}
		
		tmpSql += " and a.verconfile_isexist = 1";
		if(!gw_type.equals("") && gw_type != null)
		{
			tmpSql += " and a.gw_type = '"+gw_type+"'";
		}
		tmpSql += "  order by a.verconfile_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='" + selectName
				+ "' class='bk' onchange=showChild('" + selectName + "')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/" + fields.get("verconfile_name")
						+ "|" + fields.get("verconfile_name") + "|"
						+ fields.get("verconfile_size") + "'>" + fields.get("outter_url")
						+ "/" + fields.get("server_dir") + "/"
						+ fields.get("verconfile_name") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}

	
	/**
	 * 查询文件服务器和版本文件表，生成下拉框返回
	 * 
	 * @author lizhaojun
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getVersionFilePath(String devicetype_id)
	{
		String tmpSql = " select distinct outter_url,server_dir,softwarefile_name,softwarefile_size "
				+ " from tab_software_file a,tab_file_server b "
				+ " where a.dir_id = b.dir_id and a.softwarefile_isexist =1 ";
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id = " + devicetype_id;
		}
		// tmpSql += " order by a.softwarefile_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='file_path_2' class='bk' onchange=showChild('file_path_2')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/"
						+ fields.get("softwarefile_name") + "|"
						+ fields.get("softwarefile_name") + "|"
						+ fields.get("softwarefile_size") + "'>"
						+ fields.get("outter_url") + "/" + fields.get("server_dir") + "/"
						+ fields.get("softwarefile_name") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}

	/**
	 * 根据版本文件ID查询文件服务器和版本文件表，生成文本框
	 * 
	 * @author qixueqi
	 * @param String
	 *            goal_softwareversion
	 * @return String
	 */
	public String getGoalVersionFilePath(String goal_softwareversion)
	{
		String tmpSql = " select * from tab_software_file a,tab_file_server b where a.dir_id = b.dir_id and a.softwarefile_isexist =1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = " select outter_url, server_dir, softwarefile_name, softwarefile_size " +
					"from tab_software_file a,tab_file_server b where a.dir_id = b.dir_id and a.softwarefile_isexist =1 ";
		}
		String checkList = "";
		if (goal_softwareversion != null && !goal_softwareversion.equals(""))
		{
			tmpSql += " and a.softwarefile_id = " + goal_softwareversion;
		}
		else
		{
			checkList += "<input type='hidden' name='file_path_2' value=''/>";
			checkList += "<input type=text name='file_path_2_display' size='60' value=''/>";
			return checkList;
		}
		// tmpSql += " order by a.softwarefile_id desc";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		if (null != fields)
		{
			checkList += "<input type='hidden' name='file_path_2' value='"
					+ fields.get("outter_url") + "/" + fields.get("server_dir") + "/"
					+ fields.get("softwarefile_name") + "|"
					+ fields.get("softwarefile_name") + "|"
					+ fields.get("softwarefile_size") + "'/>";
			checkList += "<input type=text name='file_path_2_display' size='60' value='"
					+ fields.get("outter_url") + "/" + fields.get("server_dir") + "/"
					+ fields.get("softwarefile_name") + "' readOnly/>";
		}
		else
		{
			checkList += "<input type='hidden' name='file_path_2' value=''/>";
			checkList += "<input type=text name='file_path_2_display' size='60' value=''/>";
		}
		// logger.debug("[getGoalVersionFilePath] html" + checkList);
		return checkList;
	}

	/**
	 * 查询软件版本型号表和版本文件表，生成下拉框返回
	 * 
	 * @author qixueqi
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getSoftwareversion(String devicetype_id, String deviceModelId)
	{
		String tmpSql = " select a.softwarefile_id,a.devicetype_id,a.softwarefile_name,b.softwareversion from tab_software_file a,tab_devicetype_info b where a.devicetype_id=b.devicetype_id and a.softwarefile_isexist = 1 ";
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id=" + devicetype_id;
		}
		if (deviceModelId != null && !deviceModelId.equals(""))
		{
			tmpSql += " and a.device_model_id='" + deviceModelId + "'";
		}
		// tmpSql += " order by a.softwarefile_id desc";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='goal_softwareversion' class='bk' onchange=selectGoalSoftFile()>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			HashSet<String> set = new HashSet<String>();
			while (null != fields)
			{
				String version = fields.get("softwareversion").toString();
				String softName = fields.get("softwarefile_name").toString();
				if (false == set.contains(softName))
				{
					checkList += "<option value='" + fields.get("softwarefile_id") + "'>"
							+ softName + "(" + version + ")</option>";
					set.add(softName);
				}
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}
	/**
	 * 查询软件版本型号表和版本文件表，生成下拉框返回
	 * 
	 * @author qixueqi
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getSoftwareversion()
	{
		String tmpSql = " select a.softwarefile_id,a.devicetype_id,a.softwarefile_name," 
			+ "b.softwareversion from tab_software_file a,tab_devicetype_info b " 
			+ "where a.devicetype_id=b.devicetype_id and a.softwarefile_isexist = 1 ";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='goal_devicetype_id' class='bk' >";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			HashSet<String> set = new HashSet<String>();
			while (null != fields)
			{
				String version = fields.get("softwareversion").toString();
				String softName = fields.get("softwarefile_name").toString();
				if (false == set.contains(softName))
				{
					checkList += "<option value='" + fields.get("devicetype_id") + "'>"
							+ softName + "(" + version + ")</option>";
					set.add(softName);
				}
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}

	/**
	 * 根据设备ID获取设备型号对应的设备版本文件list
	 * 
	 * @param device_id
	 * @author Jason(3412)
	 * @date 2009-3-31
	 * @return String
	 */
	public String getSoftwareversion(String device_id)
	{
		HashMap map = getDeviceTypeInfo(device_id);
		if (map != null)
		{
			String strDevModelId = String.valueOf(map.get("device_model_id"));
			if (!"null".equals(strDevModelId) && !"".equals(strDevModelId))
			{
				return getSoftwareversion(null, strDevModelId);
			}
		}
		return null;
	}

	/**
	 * 获取设备类型信息：device_model_id,devicetype_id
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-3-31
	 * @return HashMap
	 */
	public static HashMap getDeviceTypeInfo(String deviceId)
	{
		HashMap map = null;
		String strSQL = "select device_model_id,devicetype_id from tab_gw_device"
				+ " where device_id='" + deviceId + "'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		map = DataSetBean.getRecord(strSQL);
		if (map != null && !map.isEmpty())
		{
			return map;
		}
		return null;
	}

	/**
	 * 查询文件服务器和版本文件表，生成下拉框返回
	 * 
	 * @author lizhaojun
	 * @param String
	 *            devicetype_id
	 * @return String
	 */
	public String getVersionFilePathToConfigStock(String devicetype_id)
	{
		String tmpSql = " select distinct outter_url,server_dir,softwarefile_name,softwarefile_size,access_user,access_passwd "
				+ " from tab_software_file a,tab_file_server b where a.dir_id = b.dir_id and a.softwarefile_isexist =1 ";
		if (devicetype_id != null && !devicetype_id.equals(""))
		{
			tmpSql += " and a.devicetype_id = " + devicetype_id;
		}
		// tmpSql += " order by a.softwarefile_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='file_path_2' class='bk' onchange=showChild('file_path_2')>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/"
						+ fields.get("softwarefile_name") + "|"
						+ fields.get("softwarefile_name") + "|"
						+ fields.get("softwarefile_size") + "|"
						+ fields.get("access_user") + "|" + fields.get("access_passwd")
						+ "'>" + fields.get("outter_url") + "/"
						+ fields.get("server_dir") + "/"
						+ fields.get("softwarefile_name") + "</option>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		return checkList;
	}

	/**
	 * 获取设备型号下拉列表框 add by lizhaojun
	 * 
	 * @param null
	 * @return String 将操作结果以String的形式返回
	 */
	public String getDeviceTypeList(String cast, boolean hasChild)
	{
		String tmpSql = "";
		if (LipossGlobals.isOracle())
		{
			tmpSql =  "select a.devicetype_id, b.device_model || a.softwareversion as device_model from tab_devicetype_info a, gw_device_model b  where a.device_model_id = b.device_model_id and a.vendor_id = b.vendor_id";
		}else
		{
			tmpSql = "select a.devicetype_id, b.device_model+'('+a.softwareversion+')' as device_model from tab_devicetype_info a, gw_device_model b  where a.device_model_id = b.device_model_id and a.vendor_id = b.vendor_id";
		}
		// String tmpSql = "select device_model_id, device_model from gw_device_model";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String DeviceTypeList = FormUtil.createListBox(cursor, "devicetype_id",
				"device_model", hasChild, cast, "");
		return DeviceTypeList;
	}

	/**
	 * 软件版本下拉列表框,文件上传修改页面使用 add by qixueqi
	 * 
	 * @param null
	 * @return String 将操作结果以String的形式返回
	 */
	public String getDeviceTypeList(String cast, boolean hasChild, String device_model_id)
	{
		// String tmpSql = "select a.devicetype_id,a.softwareversion +'(' +
		// a.hardwareversion + ')' as softwareversion from tab_devicetype_info
		// a,gw_device_model b where a.device_model = b.device_model and a.oui=b.oui and
		// b.device_model_id ='" + device_model_id + "' order by a.devicetype_id";
		//1为Oracle;2为Sysbase;0为其他
		String contactStr = DBUtil.GetDB() == Global.DB_ORACLE ?"||":"+";
		String tmpSql = "select a.devicetype_id,a.softwareversion "+contactStr+"'(' "+contactStr+" a.hardwareversion "+contactStr+" ')' as softwareversion  "
				+ " from tab_devicetype_info a,gw_device_model b where a.device_model_id = b.device_model_id";
		if (device_model_id != null && !"".equals(device_model_id))
		{
			tmpSql += " and b.device_model_id ='" + device_model_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String DeviceTypeList = FormUtil.createListBox(cursor, "devicetype_id",
				"softwareversion", hasChild, cast, "");
		return DeviceTypeList;
	}

	/**
	 * 厂商下拉列表框 add by qixueqi
	 * 
	 * @param null
	 * @return String 将操作结果以String的形式返回
	 */
	public String getStrVendorList(boolean flag, String compare, String rename)
	{
		String tmpSql = "select vendor_id,vendor_name, vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String strVendorList = FormUtil.createListBox_replace(cursor, "vendor_id",
				"vendor_add", "vendor_name", "vendor_id", flag, compare, rename, true);
		cursor = null;
		return strVendorList;
	}

	/**
	 * 获取设备型号下拉列表框 add by qixueqi
	 * 
	 * @param null
	 * @return String 将操作结果以String的形式返回
	 */
	public String getDevicemodelidlist(String cast, boolean hasChild, String vendor_id)
	{
		String tmpSql = "select device_model_id,device_model from gw_device_model where 1 =1 ";
		if (null != vendor_id && !("".equals(vendor_id)))
		{
			tmpSql += "and vendor_id = '" + vendor_id + "' ";
		}
		tmpSql += " order by device_model_id";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		String DeviceTypeList = FormUtil.createListBox(cursor, "device_model_id",
				"device_model", hasChild, cast, "");
		return DeviceTypeList;
	}

	/**
	 * 获取配置文件/日志文件upload的路径,以下拉框的形式返回!
	 * 
	 * @param type
	 *            1-版本文件；2-配置文件；3-日志文件
	 * @param isUpload
	 *            1-上传；0-下载
	 * @return String
	 */
	public String getURLPath(int type)
	{
		String tmpSql = " select * from tab_file_server where file_type = " + type;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = " select outter_url, dir_id, access_user, access_passwd, server_dir " +
					"from tab_file_server where file_type = " + type;
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String checkList = "<select name='url_path' class='bk' onchange='selectChg()'>";
		if (null != fields)
		{
			checkList += "<option value='-1'>===============请选择===============</option>";
			while (null != fields)
			{
				checkList += "<option value='" + fields.get("outter_url") + "|"
						+ fields.get("dir_id") + "|" + fields.get("access_user") + "|"
						+ fields.get("access_passwd") + "'>" + fields.get("outter_url")
						+ "/" + fields.get("server_dir") + "" + "</option>";
				// "<input type='hidden' name='dir_id'
				// value='"+fields.get("dir_id")+"'>";
				// +"<input type='hidden' name='access_user'
				// value='"+fields.get("access_user")+"'>"
				// +"<input type='hidden' name='access_passwd'
				// value='"+fields.get("access_passwd")+"'>";
				fields = cursor.getNext();
			}
		}
		else
		{
			checkList += "<option value='-1'>==此项没有记录==</option>";
		}
		checkList += "</select>";
		logger.debug("checkList=======>" + checkList);
		return checkList;
	}

	/**
	 * 获取所有已有的配置文件名列表
	 * 
	 * @return
	 */
	public String getAllConfFileNames()
	{
		String allNames = "";
		String tmpSql = "select verconfile_name from tab_vercon_file";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		while (fields != null)
		{
			allNames += (String) fields.get("verconfile_name");
			allNames += ",";
			fields = cursor.getNext();
		}
		return allNames;
	}

	/**
	 * 取得当前时间,一直到秒为止
	 * 
	 * @return String
	 */
	public String getCurrentTime()
	{
		Calendar cal = Calendar.getInstance();
		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH) + 1;
		int iDay = cal.get(Calendar.DATE);
		int iHour = cal.get(Calendar.HOUR_OF_DAY);
		int iMinute = cal.get(Calendar.MINUTE);
		int iSecond = cal.get(Calendar.SECOND);
		return (iYear + "" + iMonth + "" + iDay + "" + iHour + "" + iMinute + ""
				+ iSecond + "");
	}

	/**
	 * 根据sheetId获取sheet表信息
	 * 
	 * @param sheetId
	 * @return Map
	 */
	public Map getSheetInfoById(String sheetId)
	{
		String sql = "select * from tab_sheet where sheet_id='"
				+ sheetId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_id from tab_sheet where sheet_id='"
					+ sheetId + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	/**
	 * 根据设备类型和oid类型获取网关操作特殊OID表信息
	 * 
	 * @param deviceModel
	 * @param oidType
	 * @return Map
	 */
	public Map getGWModelOperOid(String deviceModel, int oidType)
	{
		String sql = "select * from tab_gw_model_oper_oid where device_model='" + deviceModel
				+ "' and oid_type=" + oidType;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select oid from tab_gw_model_oper_oid where device_model='" + deviceModel
					+ "' and oid_type=" + oidType;
		}

		PrepareSQL psql = new PrepareSQL();
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	/**
	 * 根据返回结果update sheet_report表信息
	 * 
	 * @param status
	 * @param faultCode
	 * @param endTime
	 * @return
	 */
	public int updateSheetReport(String sheetId, int status, int faultCode, long endTime)
	{
		PrepareSQL psql = new PrepareSQL("update tab_sheet_report set exec_status="
				+ status + ",fault_code=" + faultCode + ",end_time=" + endTime
				+ " where sheet_id='" + sheetId + "'");
		psql.getSQL();
		return DataSetBean.executeUpdate("update tab_sheet_report set exec_status="
				+ status + ",fault_code=" + faultCode + ",end_time=" + endTime
				+ " where sheet_id='" + sheetId + "'");
	}

	/**
	 * 根据sheetId获取sheet para表信息
	 * 
	 * @param sheetId
	 * @return
	 */
	public Map getSheetParaInfoById(String sheetId)
	{
		String sql = "select * from tab_sheet_para where sheet_id='"
				+ sheetId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select def_value from tab_sheet_para where sheet_id='"
					+ sheetId + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getRecord(sql);
	}

	/**
	 * 调用corba接口执行软件升级
	 * 
	 * @param String[]
	 *            sheetID
	 * @return List<setDataReturn>
	 */
	public List<setDataReturn> transferCorbaForVersionUpdate(String[] sheetID)
	{
		List<setDataReturn> list = new ArrayList();
		DeviceAct act = new DeviceAct();
		SnmpGatherInterface snmpGather = SnmpGatherInterface.GetInstance();
		int oidType = 4;
		int len = 1;
		for (String sheetId : sheetID)
		{
			String deviceId = (String) ((getSheetInfoById(sheetId)).get("device_id"));
			// Map map__ = act.getDeviceInfoMap(deviceId);
			// String deviceModel = (String) map__.get("device_model");
			Map map = new DeviceAct().getDeviceInfoMap(deviceId);
			String loopBackIp = (String) map.get("loopback_ip");
			// String readComm = (String) map.get("snmp_ro_community");
			String readComm = "s";
			String writeComm = (String) map.get("snmp_rw_community");
			String port = (String) map.get("snmp_udp");
			String deviceModel = (String) map.get("device_model");
			Map map_ = getGWModelOperOid(deviceModel, oidType);
			String oid = (String) map_.get("oid");
			Map _map = getSheetParaInfoById(sheetId);
			String value = (String) _map.get("def_value");
			logger.debug("[简单软件升级] loopBackIp=" + loopBackIp + " $readComm=" + readComm
					+ " $writeComm=" + writeComm + " $port=" + port + " $value=" + value
					+ " $oid=" + oid);
			setData data = new setData(loopBackIp, port, oid, value, readComm, writeComm);
			setDataReturn[] setDataReturn_ = snmpGather.SetNetSnmpWayV2(
					new setData[] { data }, len, deviceId);
			long endTime = (new Date().getTime()) / 1000;
			String result = "0";
			if (setDataReturn_ != null)
			{
				result = setDataReturn_[0].result;
				logger
						.debug("[简单软件升级] setDataReturn_[0]="
								+ setDataReturn_[0].loopback_ip + " $"
								+ setDataReturn_[0].result);
			}
			updateSheetReport(sheetId, result, endTime);
			if (setDataReturn_ != null)
			{
				for (int i = 0; i < setDataReturn_.length; i++)
				{
					list.add(setDataReturn_[i]);
				}
			}
		}
		return list;
	}

	private int updateSheetReport(String sheetId, String result, long endTime)
	{
		int status = -1;
		int result_ = -1;
		if (result.equals("1"))
		{
			result_ = 1;
		}
		else
		{
			status = 0;
		}
		return updateSheetReport(sheetId, status, result_, endTime);
	}

	private String softUpXml(String file_ur, String username,String password,String softwarefile_size,
			String softwarefile_name,String delay_time,String sucess_url,String fail_url)
	{
		String strXml = null;
		// new doc
		Document doc = DocumentHelper.createDocument();
		// root node: NET
		Element root = doc.addElement("SoftUpdate");
		root.addAttribute("flag", "1");
		root.addElement("CommandKey").addText("SoftUpdate");
		root.addElement("FileType").addText("1 Firmware Upgrade Image");
		root.addElement("URL").addText(file_ur);
		root.addElement("Username").addText(username);
		root.addElement("Password").addText(password);
		root.addElement("FileSize").addText(softwarefile_size);
		root.addElement("TargetFileName").addText(softwarefile_name);
		root.addElement("DelaySeconds").addText(delay_time);
		root.addElement("SuccessURL").addText(sucess_url);
		root.addElement("FailureURL").addText(fail_url);
		strXml = doc.asXML();
		return strXml;
	}

	/**
	 * 软件简单升级，形成策略
	 * 
	 * @author wangsenbo
	 * @date Apr 23, 2011
	 * @param request request
	 * @param devicetype_id devicetype_id
	 * @param gw_type gw_type(必须)
	 * @return boolean
	 */
	public boolean softup(HttpServletRequest request, String devicetype_id,
			long userId,String gw_type)
	{
		String strategyXmlParam = "";
		String[] device_list = request.getParameterValues("device_id");
		
		// ----------------------------软件升级-----------------------------------------------
		String keyword_2 = request.getParameter("keyword_2"); // 关键字
		String filetype_2 = request.getParameter("filetype_2"); // 文件类型
		String[] file_path2 = request.getParameter("file_path_2").split("\\|");
		String file_path_2 = file_path2[0]; // 文件路径
		String username_2 = request.getParameter("username_2"); // 用户名
		String passwd_2 = request.getParameter("passwd_2"); // 密码
		String file_size_2 = request.getParameter("file_size_2"); // 文件大小
		String filename_2 = request.getParameter("filename_2"); // 文件名称
		String delay_time_2 = request.getParameter("delay_time_2"); // 延时
		String sucess_url_2 = request.getParameter("sucess_url_2"); // 成功URL
		String fail_url_2 = request.getParameter("fail_url_2"); // 失败URL
		
		
		strategyXmlParam = softUpXml(file_path_2, username_2, passwd_2, file_size_2, filename_2, delay_time_2, sucess_url_2, fail_url_2);
		logger.debug("XML: " + strategyXmlParam);
		/** 入策略表，调预读 */
		ArrayList<String> sqllist = new ArrayList<String>();
		SuperDAO dao = new SuperDAO();
		String[] stragetyIds = new String[device_list.length];

		//得到设备类型
		int gw_type_int = -1;
		gw_type_int = Integer.parseInt(gw_type);
		
		// 配置的service_id
		int serviceId = 5;
		for (int i = 0; i < device_list.length; i++)
		{
			StrategyOBJ strategyObj = new StrategyOBJ();
			// 策略ID
			strategyObj.createId();
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(userId);
			// 立即执行
			strategyObj.setType(0);
			// 设备ID
			strategyObj.setDeviceId(device_list[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			stragetyIds[i] = String.valueOf(strategyObj.getId());
			// 入策略表
			sqllist.addAll(dao.strategySQL(strategyObj));
		}
		boolean flag = false;
		// 立即执行
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.debug("批量执行策略入库：  成功");
			flag = true;
		}
		else
		{
			logger.debug("批量执行策略入库：  失败");
			flag = false;
		}
		logger.warn("立即执行，开始调用预处理...");
		// 调用预读
		if (true == CreateObjectFactory.createPreProcess(String.valueOf(gw_type_int)).processOOBatch(stragetyIds))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 软件简单升级，形成策略
	 * 为了不影响上面原始的softup方法，我在这里将softup方法重写了一下
	 * 
	 * add by zhangchy 2011-11-21
	 * 
	 * @param request
	 * @param devicetype_id
	 * @param userId
	 * @param tempStr
	 * @return
	 */
	public String softup(HttpServletRequest request, String devicetype_id, long userId)
	{
		String strategyXmlParam = "";
		String[] device_list = request.getParameterValues("device_id");
		
		// ----------------------------软件升级-----------------------------------------------
		String[] file_path2 = request.getParameter("file_path_2").split("\\|");
		String file_path_2 = file_path2[0]; // 文件路径
		String username_2 = request.getParameter("username_2"); // 用户名
		String passwd_2 = request.getParameter("passwd_2"); // 密码
		String file_size_2 = request.getParameter("file_size_2"); // 文件大小
		String filename_2 = request.getParameter("filename_2"); // 文件名称
		String delay_time_2 = request.getParameter("delay_time_2"); // 延时
		String sucess_url_2 = request.getParameter("sucess_url_2"); // 成功URL
		String fail_url_2 = request.getParameter("fail_url_2"); // 失败URL
		String gw_type = request.getParameter("gw_type");
		
		
		strategyXmlParam = softUpXml(file_path_2, username_2, passwd_2, file_size_2, filename_2, delay_time_2, sucess_url_2, fail_url_2);
		logger.debug("XML: " + strategyXmlParam);
		/** 入策略表，调预读 */
		ArrayList<String> sqllist = new ArrayList<String>();
		SuperDAO dao = new SuperDAO();
		String[] stragetyIds = new String[device_list.length];
		// 配置的service_id
		int serviceId = 5;
		String strategyId = "";
		for (int i = 0; i < device_list.length; i++)
		{
			StrategyOBJ strategyObj = new StrategyOBJ();
			
			// 策略ID
			strategyObj.createId();
			
			if("".equals(strategyId)){
				strategyId = strategyObj.getId()+"";
			}else{
				strategyId = strategyId +";"+ strategyObj.getId();
			}
			
			// 策略配置时间
			strategyObj.setTime(TimeUtil.getCurrentTime());
			// 用户id
			strategyObj.setAccOid(userId);
			// 立即执行
			strategyObj.setType(0);
			// 设备ID
			strategyObj.setDeviceId(device_list[i]);
			// QOS serviceId
			strategyObj.setServiceId(serviceId);
			// 顺序,默认1
			strategyObj.setOrderId(1);
			// 工单类型: 新工单,工单参数为xml串的工单
			strategyObj.setSheetType(2);
			//分地市取IP http://10.128.0.43:8000/FileServer/FILE/SOFT/wwww
			if("1".equals(LipossGlobals.getLipossProperty("isDistributeCity"))){
				String oldIP = file_path_2.substring(file_path_2.indexOf("://")+3, file_path_2.indexOf("/FileServer"));
				oldIP = oldIP.substring(0, oldIP.indexOf(":"));
				String newIP = getIPbyId(device_list[i]);
				strategyXmlParam = strategyXmlParam.replace(oldIP, newIP);
			}
			// 参数
			strategyObj.setSheetPara(strategyXmlParam);
			strategyObj.setTempId(serviceId);
			strategyObj.setIsLastOne(1);
			stragetyIds[i] = String.valueOf(strategyObj.getId());
			// 入策略表
			
			if(Global.JLDX.equals(Global.instAreaShortName)){
				sqllist.addAll(dao.strategySQL(strategyObj, LipossGlobals.getLipossProperty("strategy_tabname.strategy.tabname")));
			}
			else{
				sqllist.addAll(dao.strategySQL(strategyObj));
			}
			
		}
		boolean flag = false;
		// 立即执行
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.debug("批量执行策略入库：  成功");
			flag = true;
		}
		else
		{
			logger.debug("批量执行策略入库：  失败");
			flag = false;
		}
		logger.warn("立即执行，开始调用预处理...");
		// 调用预读
		if (true == CreateObjectFactory.createPreProcess(gw_type).processOOBatch(stragetyIds))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return strategyId;
	}
	
	
	public String getIPbyId(String deviceId)
	{
		String tmpSql = "select ip from tab_stb_file_path_city_ip ci,tab_gw_device d where ci.city_id=d.city_id and d.device_id='"+deviceId+"'";
		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		return StringUtil.getStringValue(fields, "ip");
	}

	public static void main(String[] args)
	{
		String aa = "<SoftUpdate flag=\"1\"><CommandKey>SoftUpdate</CommandKey><FileType>1 Firmware Upgrade Image</FileType><URL>http://10.128.0.43:8000/FileServer/FILE/SOFT/wwww</URL><Username></Username><Password></Password><FileSize>7468</FileSize><TargetFileName>wwww</TargetFileName><DelaySeconds>0</DelaySeconds><SuccessURL></SuccessURL><FailureURL></FailureURL></SoftUpdate>";
		aa = aa.replace("10.128.0.43", "0.0.0.0");
		System.out.println(aa);
	}
	
	
}