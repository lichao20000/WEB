/**
 * @(#)CommonForm.java 2006-1-12
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

/**
 * <p>
 * Title: 常用表单数据
 * </p>
 * <p>
 * Description: 如采集机选择列表，
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Linkage Corporation.
 * </p>
 * 
 * @author Yanhj, Network Management Product Department, ID Card No.5126
 * @version 2.0
 */

public class CommonForm {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(CommonForm.class);
	public CommonForm() {
	}

	/**
	 * 返回采集机列表数据.
	 * <UL>
	 * <LI><SELECT NAME='gather_id_form' CLASS=bk></LI>
	 * <LI><OPTION VALUE='-1'>==请选择==</OPTION></LI>
	 * <LI><OPTION VALUE='gather_id'>...</OPTION></LI>
	 * <LI></SELECT></LI>
	 * 
	 * @return
	 */
	public static String getGather_idBox(HttpServletRequest request) {

		String sql = "select * from tab_process_desc where gather_id in (?) ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select gather_id, descr from tab_process_desc where gather_id in (?) ";
		}
		HttpSession session = request.getSession();
		UserRes dbUserRes = (UserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();
		PrepareSQL pSQL = new PrepareSQL(sql);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);

		Cursor curosor = DataSetBean.getCursor(pSQL.getSQL());
		Map fields = new HashMap();
		fields = curosor.getNext();
		String sHtml = "<SELECT NAME='gather_id_form'  CLASS=bk>";
		sHtml += "<OPTION VALUE='-1'>==请选择==</OPTION>";

		while (fields != null) {
			sHtml += "<OPTION VALUE='" + (String) fields.get("gather_id")
					+ "'>==" + (String) fields.get("descr") + "==</OPTION>";
			fields = curosor.getNext();
		}

		sHtml += "</SELECT>";

		return sHtml;
	}

	public static ArrayList getCity(HttpServletRequest request) {

		String sqlTxt = "select distinct city_id from tab_deviceresource"
				+ " where gather_id in (?) and device_id in "
				+ "(select res_id from tab_gw_res_area where res_type=1 and area_id =?) and"
				+ " resource_type_id in (10,11) order by city_id";
		HttpSession session = request.getSession();
		UserRes dbUserRes = (UserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();
		PrepareSQL pSQL = new PrepareSQL(sqlTxt);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
		pSQL.setLong(2, dbUserRes.getAreaId());

		Cursor curosor = DataSetBean.getCursor(pSQL.getSQL());
		logger.warn("属地Sql: " + pSQL.getSQL());
		// 去掉deviceFilter.doFilter 方法：肖学逢
		// // 设备权限过滤
		// DeviceResDataFilter deviceFilter = new DeviceResDataFilter(curosor,
		// "device_id");
		// curosor = (Cursor) deviceFilter.doFilter(dbUserRes);

		Map map = curosor.getNext();
		String city_id = "";
		ArrayList city_idList = new ArrayList();
		while (map != null) {
			city_id = (String) map.get("city_id");
			if (!city_idList.contains(city_id) && !city_id.equals("0")) {
				city_idList.add(city_id);
			}
			map = curosor.getNext();
		}

		// Cursor citycursor = null;
		// Map cityMap = new HashMap();
		// Map tmpMap = CommonMap.getCityMap();
		logger.debug("属地数目: " + city_idList.size());
		// for (int i=0; i < city_idList.size(); i++) {
		// cityMap.clear();
		// logger.debug("city_id=" + city_idList.get(i));
		// logger.debug("city_name=" + tmpMap.get(city_idList.get(i)));
		// cityMap.put(city_idList.get(i), tmpMap.get(city_idList.get(i)));
		// if (cityMap != null)
		// citycursor.add(cityMap);
		// }
		//		    
		return city_idList;
	}

	/**
	 * @author lizj （5202）
	 * @param request
	 * @return
	 */
	public static ArrayList getDeviceCity(HttpServletRequest request) {

		String sqlTxt = "select distinct city_id from tab_deviceresource"
				+ " where gather_id in (?) and device_id in "
				+ "(select res_id from tab_gw_res_area where res_type=1 and area_id =?) and"
				+ " resource_type_id=101 order by city_id";
		HttpSession session = request.getSession();
		UserRes dbUserRes = (UserRes) session.getAttribute("curUser");
		List gather_id = dbUserRes.getUserProcesses();
		PrepareSQL pSQL = new PrepareSQL(sqlTxt);
		pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
		pSQL.setLong(2, dbUserRes.getAreaId());

		Cursor curosor = DataSetBean.getCursor(pSQL.getSQL());

		Map map = curosor.getNext();
		String city_id = "";
		ArrayList city_idList = new ArrayList();
		while (map != null) {
			city_id = (String) map.get("city_id");
			if (!city_idList.contains(city_id) && !city_id.equals("0")) {
				city_idList.add(city_id);
			}
			map = curosor.getNext();
		}

		return city_idList;
	}

	// public static void main(String[] args) {
	// CommonForm commonForm1 = new CommonForm();
	// }

	// 统一配置中获取各个地市的checkbox选框
	public static String getCityCheckBox(String list) {
		String sqlTxt = "select * from tab_city where (parent_id='00' or parent_id = '-1')";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlTxt = "select city_id, city_name from tab_city where (parent_id='00' or parent_id = '-1')";
		}
		// HttpSession session = request.getSession();
		// UserRes curuser = (UserRes) session.getAttribute("curUser");
		StringBuffer CityCB = new StringBuffer();
		PrepareSQL pSQL = new PrepareSQL(sqlTxt);

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		// int size=cursor.getRecordSize();

		int i = 0;
		Map fields = cursor.getNext();
		while (fields != null) {
			String city_id = (String) fields.get("CITY_ID".toLowerCase());
			String city_name = (String) fields.get("CITY_NAME".toLowerCase());

			if (list.indexOf("," + city_id + ",") > -1) {
				CityCB.append(
						"<input type='checkbox' checked name='city' value='")
						.append(city_id).append("'>").append(city_name);
			} else {
				CityCB.append("<input type='checkbox' name='city' value='")
						.append(city_id).append("'>").append(city_name);
			}
			if ((i + 1) % 5 == 0) {
				CityCB.append("<br>");
			}
			i++;
			fields = cursor.getNext();
		}

		return CityCB.toString();
	}

	/**
	 * 根据登录的用户所在的地市,获取各个地市中某个地市的Check Box框
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String 各个地市中某个地市的Check Box框
	 */
	public static String getCityCheckBox_(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserRes curuser = (UserRes) session.getAttribute("curUser");
		String city_id = curuser.getCityId();
		String[] cityid=city_id.split(",");
		List list=new ArrayList();
		for(int i=0;i<cityid.length;i++)
		{
			list.add(cityid[i]);
		}
		String sqlTxt = "select * from tab_city where city_id in(" + StringUtils.weave(list)
				+ ") or  parent_id in(" + StringUtils.weave(list) + ")";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlTxt = "select city_id, city_name from tab_city where city_id in(" + StringUtils.weave(list)
					+ ") or  parent_id in(" + StringUtils.weave(list) + ")";
		}
		StringBuffer CityCB = new StringBuffer();
		PrepareSQL pSQL = new PrepareSQL(sqlTxt);

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());

		int i = 0;
		Map fields = cursor.getNext();
		while (fields != null) {
			// 生成复选框
			CityCB.append("<input type='checkbox' name='city' value='").append(
					(String) fields.get("city_id")).append("'>").append(
					(String) fields.get("city_name"));
			if ((i + 1) % 3 == 0) {
				CityCB.append("<br>");
			}
			i++;
			fields = cursor.getNext();
		}
		return CityCB.toString();
	}
}
