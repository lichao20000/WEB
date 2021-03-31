
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class commonAct
{

	private Cursor cursor = null;
	private Map fields = null;
	/**
	 * 厂商列表
	 */
	// private String m_Vendor_SQL = "select vendor_id,vendor_name + '(' + vendor_id + ')'
	// as vendor_name,vendor_add from tab_vendor";
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";

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
		String loopback_ip = request.getParameter("loopback_ip");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String starttime1;
		String endtime1;
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
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String user_city_id = curUser.getCityId();
		if (null == device_serialnumber)
		{
			device_serialnumber = "";
		}
		String linkParam = "";
		String sqlDevice = " select * from tab_gw_device where device_status=0 and gw_type=2 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = "select device_id, device_id_ex, devicetype_id, city_id, vendor_id, device_serialnumber," +
					" loopback_ip, complete_time from tab_gw_device where device_status=0 and gw_type=2 ";
		}
		if (false == StringUtil.IsEmpty(starttime1))
		{
			sqlDevice += " and complete_time >=" + starttime1 + " ";
			linkParam += "&starttime=" + starttime;
		}
		if (false == StringUtil.IsEmpty(endtime1))
		{
			sqlDevice += " and complete_time <" + endtime1 + " ";
			linkParam += "&endtime=" + endtime;
		}
		if (device_serialnumber != null && !"".equals(device_serialnumber))
		{
			if (device_serialnumber.length() > 5)
			{
				sqlDevice += " and dev_sub_sn ='"
						+ device_serialnumber.substring(device_serialnumber.length() - 6,
								device_serialnumber.length()) + "'";
			}
			sqlDevice += " and device_serialnumber like'%" + device_serialnumber + "%'";
			linkParam += "&device_serialnumber=" + device_serialnumber;
		}
		if (loopback_ip != null && !"".equals(loopback_ip))
		{
			sqlDevice += " and loopback_ip like'%" + loopback_ip + "%'";
			linkParam += "&loopback_ip=" + loopback_ip;
		}
		if (!user.isAdmin())
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(user_city_id);
			sqlDevice += " and city_id in (" + StringUtils.weave(list) + ",'00')";
			list = null;
		}
		// sqlDevice += " order by device_id";
		PrepareSQL psql = new PrepareSQL(sqlDevice);
    	psql.getSQL();
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
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar(linkParam);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 获得未绑定设备列表
	 * 
	 * @author gongsj
	 * @date 2009-8-27
	 * @param request
	 * @return
	 */
	public List getUnBindedDeviceInfoList(HttpServletRequest request)
	{
		String device_serialnumber = request.getParameter("device_serialnumber");
		String loopback_ip = request.getParameter("loopback_ip");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String user_city_id = curUser.getCityId();
		if (null == device_serialnumber)
		{
			device_serialnumber = "";
		}
		String sqlDevice = " select * from tab_gw_device where cpe_allocatedstatus=0  ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = " select device_id, device_id_ex, devicetype_id, city_id, vendor_id, device_serialnumber," +
					" loopback_ip from tab_gw_device where cpe_allocatedstatus=0  ";
		}
		if (device_serialnumber != null && !"".equals(device_serialnumber))
		{
			if (device_serialnumber.length() > 5)
			{
				sqlDevice += " and dev_sub_sn ='"
						+ device_serialnumber.substring(device_serialnumber.length() - 6,
								device_serialnumber.length()) + "'";
			}
			sqlDevice += " and device_serialnumber like'%" + device_serialnumber + "%'";
		}
		if (loopback_ip != null && !"".equals(loopback_ip))
		{
			sqlDevice += " and loopback_ip like'%" + loopback_ip + "%'";
		}
		if (!user.isAdmin())
		{
			List list = CityDAO.getAllNextCityIdsByCityPid(user_city_id);
			sqlDevice += " and city_id in (" + StringUtils.weave(list) + ",'00')";
			list = null;
		}
		// sqlDevice += " order by device_id";
		PrepareSQL psql = new PrepareSQL(sqlDevice);
    	psql.getSQL();
		String stroffset = request.getParameter("offset");
		ArrayList list = new ArrayList();
		list.clear();
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
		qryp.initPage(sqlDevice, offset, pagelen);
		cursor = DataSetBean.getCursor(sqlDevice, offset, pagelen);
		String strBar = qryp.getPageBar("&device_serialnumber=" + device_serialnumber
				+ "&loopback_ip" + loopback_ip);
		list.add(strBar);
		list.add(cursor);
		return list;
	}

	/**
	 * 获取域id与名称映射关系
	 * 
	 * @return
	 */
	public Map getAreaIdMapName()
	{
		String sql = "select area_id,area_name from tab_area";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map result = new HashMap();
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
	 * 取得所有OUI和厂商名对应的MAP
	 * 
	 * @param request
	 * @return ArrayList
	 */
	public HashMap getOUIDevMap()
	{
		HashMap ouiMap = new HashMap();
		ouiMap.clear();
		PrepareSQL psql = new PrepareSQL(m_Vendor_SQL);
    	psql.getSQL();
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
					ouiMap.put((String) fields.get("vendor_id"), (String) fields
							.get("vendor_name"));
				}
				fields = cursor.getNext();
			}
		}
		return ouiMap;
	}
}
