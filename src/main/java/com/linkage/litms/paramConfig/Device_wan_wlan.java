package com.linkage.litms.paramConfig;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

/**
 * 
 * 
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

public class Device_wan_wlan {
	/**
	 * @author benyp 根据设备版本结合tab_devicetype_info查询,但是选择事件设置的为device_id
	 * @param request
	 * 
	 * @return Stirng htkl
	 */
	public String getDevice_id(HttpServletRequest request,
			boolean needFilter) {

		String gather_id = request.getParameter("gather_id");// 采集地
		String oui = request.getParameter("vendor_id");// 设备厂商
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

		String tmpUser = "";

		String tmpSql = "select * from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select username, a.device_id, a.oui, a.device_serialnumber " +
					" from tab_gw_device a inner join tab_devicetype_info c on a.devicetype_id=c.devicetype_id ";
		}

		// 非admin用户
		if (!user.isAdmin()) {
			tmpSql += " inner join tab_gw_res_area b on a.device_id = b.res_id";
		}

		// 按用户查询方式
		if ((null != hguser && !"".equals(hguser))
				|| (null != telephone && !"".equals(telephone))) {
			tmpSql += " inner join tab_egwcustomer d on a.device_id = d.device_id ";
		}

		
		
		tmpSql += " where a.device_status=1 ";

		if (null != gw_type && !"".equals(gw_type)) {
			tmpSql += " and a.gw_type = "+ gw_type;
		}
		
		// 非admin用户
		if (!user.isAdmin()) {
			tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
		}

		// 按用户查询方式
		if (null != hguser && !"".equals(hguser)) {
			tmpSql += " and d.username = '" + hguser + "'";
		}
		if (null != telephone && !"".equals(telephone)) {
			tmpSql += " and d.phonenumber like '%" + telephone + "%'";
		}

		if (gather_id != null && !gather_id.equals("")) {

			tmpSql += " and a.gather_id ='" + gather_id + "'";
		}
		if (oui != null && !oui.equals("")) {

			tmpSql += " and c.oui ='" + oui + "'";
		}
		if (softwareversion != null && !softwareversion.equals("")) {

			tmpSql += " and c.softwareversion ='" + softwareversion + "'";
		}
		if (devicetype_id != null && !devicetype_id.equals("")) {

			tmpSql += " and a.devicetype_id =" + devicetype_id + " ";
		}
		if (serialnumber != null && !"".equals(serialnumber)) {
			if(serialnumber.length()>5){
				tmpSql += " and a.dev_sub_sn ='" + serialnumber.substring(serialnumber.length()-6, serialnumber.length()) + "'";
			}
			tmpSql += " and a.device_serialnumber like '%" + serialnumber
					+ "'";
		}

		PrepareSQL psql = new PrepareSQL(tmpSql);
		psql.getSQL();
		
		Cursor cursor = DataSetBean.getCursor(tmpSql);
		Map fields = cursor.getNext();
		String serviceHtml = "";// <table border='0' align='center' width='100%'
		// cellpadding='0' cellspacing='1'
		// bgcolor='#999999'>";

		if (fields == null) {
			serviceHtml += "无符合条件的设备";
		} else {

			while (fields != null) {

				tmpUser = (String) fields.get("username");
				// logger.debug(" tmpUser:" + tmpUser);
				if (tmpUser == null || tmpUser.equalsIgnoreCase("null")) {
					tmpUser = "";
				} else {
					tmpUser = "|" + tmpUser;
				}

				String device_id = (String) fields.get("device_id");
				
				serviceHtml += "<input type=\"" + type
						+ "\" id=\"device_id\" name=\"device_id\" value=\""
						+ device_id + "\"";
				if (needFilter) {
					serviceHtml += " onclick=\"setDevice_id(" + device_id
							+ ")\">";
				} else {
					serviceHtml += ">";
				}
				serviceHtml += "&nbsp;" + (String) fields.get("oui") + "-"
						+ (String) fields.get("device_serialnumber") + tmpUser
						+ "<br>";

				fields = cursor.getNext();
			}

		}

		serviceHtml += "</table>";
		// logger.debug("serviceHtml:"+serviceHtml);
		return serviceHtml;

	}
}