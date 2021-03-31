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
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.DataSourceTypeCfgPropertiesManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author banyr (Ailk No.)
 * @version 1.0
 * @since 2018-10-12
 * @category com.linkage.litms.resource
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class DeviceActSpeedUserQuality
{
	private static Logger logger = LoggerFactory.getLogger(DeviceActSpeedUserQuality.class);
	@SuppressWarnings("unused")
	private PrepareSQL pSQL = null;
	private Cursor cursor = null;
	@SuppressWarnings("rawtypes")
	private Map fields = null;
	/**
	 * 厂商列表
	 */
	private String m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
	/**
	 * 机顶盒厂商列表
	 */
	private String stb_m_Vendor_SQL = "select vendor_id,vendor_name,vendor_add from stb_tab_vendor";
	/**
	 * 厂商列表
	 */

	/**
	 * 构造函数
	 */
	public DeviceActSpeedUserQuality()
	{
		pSQL = new PrepareSQL();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getDeviceListByVender(HttpServletRequest request)
	{
		String noCustomer = request.getParameter("noCustomer");
		String gw_type_Str = request.getParameter("gw_type");
		String fileter = "";
		if ((gw_type_Str == null) || (gw_type_Str.equals("")))
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter = fileter + "&gw_type=" + gw_type;
		if ("1".equals(noCustomer))
		{
			fileter = fileter + "&noCustomer=" + noCustomer;
		}
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "";
		if ("1".equals(noCustomer))
		{
			sqlDevice = "select a.* from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber  " +
						"from tab_gw_device a,tab_devicetype_info t where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.customer_id is not null and a.gw_type="
						+ gw_type;
			}
		}
		else
		{	
			sqlDevice = "select a.* ,b.username as loid from tab_gw_device a, tab_hgwcustomer b,tab_netacc_spead c,hgwcust_serv_info d,tab_devicetype_info t " +
					"where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.device_id = b.device_id and b.user_id = d.user_id and d.username = c.username and c.downlink >= 200 and a.customer_id is not null and a.gw_type="
					+ gw_type;
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sqlDevice = "select a.device_id, a.device_id_ex, a.city_id, a.devicetype_id, a.vendor_id, a.device_serialnumber, b.username as loid " +
						"from tab_gw_device a, tab_hgwcustomer b,tab_netacc_spead c,hgwcust_serv_info d,tab_devicetype_info t " +
						"where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.device_id = b.device_id and b.user_id = d.user_id and d.username = c.username and c.downlink >= 200 and a.customer_id is not null and a.gw_type="
						+ gw_type;
			}
		}
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_devicetype = request.getParameter("devicetype");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_device_model_id = request.getParameter("deviceModel_id");
		String str_device_model = request.getParameter("device_model_id");
		String str_city_id = request.getParameter("city_id");
		String mbBroadband = request.getParameter("mbBroadband");
		fileter = fileter + "&" + str_city_id;
		String startTime = request.getParameter("startTime");
		if ((startTime == null) || ("null".equals(startTime)))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if ((endTime == null) || ("null".equals(endTime)))
		{
			endTime = "";
		}
		if ((str_devicetype_id != null) && (!str_devicetype_id.equals("-1"))
				&& (!str_devicetype_id.equals("")))
		{
			sqlDevice = sqlDevice + " and a.devicetype_id =" + str_devicetype_id;
			fileter = fileter + "&devicetype_id=" + str_devicetype_id;
		}
		else if ((str_devicetype != null) && (!str_devicetype.equals("-1"))
				&& (!str_devicetype.equals("")))
		{
			sqlDevice = sqlDevice + " and a.devicetype_id =" + str_devicetype;
			fileter = fileter + "&devicetype_id=" + str_devicetype;
		}
		if ((str_vendor_id != null) && (!str_vendor_id.equals("-1"))
				&& (!str_vendor_id.equals("")))
		{
			sqlDevice = sqlDevice + " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter = fileter + "&vendor_id=" + str_vendor_id;
		}
		if ((str_device_model_id != null) && (!str_device_model_id.equals("-1"))
				&& (!str_device_model_id.equals("")))
		{
			sqlDevice = sqlDevice + " and a.device_model_id ='" + str_device_model_id
					+ "' ";
			fileter = fileter + "&device_model_id=" + str_device_model_id;
		}
		else if ((str_device_model != null) && (!str_device_model.equals("-1"))
				&& (!str_device_model.equals("")))
		{
			sqlDevice = sqlDevice + " and a.device_model_id ='" + str_device_model
					+ "' ";
			fileter = fileter + "&device_model_id=" + str_device_model;
		}
		if ((startTime != null) && (!"".equals(startTime)))
		{
			sqlDevice = sqlDevice + " and a.complete_time >= " + startTime;
			fileter = fileter + "&startTime=" + startTime;
		}
		if ((endTime != null) && (!"".equals(endTime)))
		{
			sqlDevice = sqlDevice + " and a.complete_time <= " + endTime;
			fileter = fileter + "&endTime=" + endTime;
		}
		String is_normal = request.getParameter("is_normal");
		if ((is_normal != null) && (!"".equals(is_normal)) && (!"-1".equals(is_normal)))
		{
			sqlDevice = sqlDevice + " and t.is_normal = " + is_normal;
			fileter = fileter + "&is_normal=" + is_normal;
		}
		if ((mbBroadband != null) && (!"".equals(mbBroadband)) && (!"-1".equals(mbBroadband)))
		{
			sqlDevice = sqlDevice + " and t.mbbroadband = " + mbBroadband;
			fileter = fileter + "&mbBroadband=" + mbBroadband;
		}
		if ("00".equals(str_city_id))
		{
			sqlDevice = sqlDevice + " and a.city_id = '00' ";
			fileter = fileter + "&city_id=" + str_city_id;
		}
		else
		{
			String city_temp = str_city_id;
			if ("-1".equals(str_city_id))
			{
				str_city_id = curUser.getCityId();
				city_temp = "-1";
			}
			else
			{
				List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
				sqlDevice = sqlDevice + " and a.city_id in ("
						+ StringUtils.weave(cityList) + ") ";
			}
			fileter = fileter + "&city_id=" + city_temp;
		}
		fileter = fileter + "&city_id=" + str_city_id;
		logger.warn(" 统计 详细 SQL :[{}]", sqlDevice);
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	 * 读取设备资源表,获取设备信息 导出Excel
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Cursor getDeviceListByVenderExcel(HttpServletRequest request)
	{
		String gw_type_Str = request.getParameter("gw_type");
		String fileter = "";
		if ((gw_type_Str == null) || (gw_type_Str.equals("")))
		{
			gw_type_Str = "1";
		}
		int gw_type = Integer.parseInt(gw_type_Str);
		fileter = fileter + "&gw_type=" + gw_type;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String startTime = request.getParameter("startTime");
		if ((startTime == null) || ("null".equals(startTime)))
		{
			startTime = "";
		}
		String endTime = request.getParameter("endTime");
		if ((endTime == null) || ("null".equals(endTime)))
		{
			endTime = "";
		}
		ArrayList list = new ArrayList();
		list.clear();
		String sqlDevice = "select a.* ,b.username as loid from tab_gw_device a, tab_hgwcustomer b,tab_netacc_spead c,hgwcust_serv_info d,tab_devicetype_info t " +
				" where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.device_id = b.device_id and b.user_id = d.user_id and d.username = c.username and c.downlink >= 200 and a.customer_id is not null and a.gw_type="
				+ gw_type;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sqlDevice = "select a.devicetype_id, a.cpe_mac, a.device_id, a.device_id_ex, a.city_id, a.vendor_id, a.device_serialnumber, b.username as loid " +
					"from tab_gw_device a, tab_hgwcustomer b,tab_netacc_spead c,hgwcust_serv_info d,tab_devicetype_info t " +
					" where a.devicetype_id=t.devicetype_id and a.device_status=1 and a.device_id = b.device_id and b.user_id = d.user_id and d.username = c.username and c.downlink >= 200 and a.customer_id is not null and a.gw_type="
					+ gw_type;
		}
		String str_devicetype_id = request.getParameter("devicetype_id");
		String str_devicetype = request.getParameter("devicetype");
		String str_vendor_id = request.getParameter("vendor_id");
		String str_city_id = request.getParameter("city_id");
		String is_normal = request.getParameter("is_normal");
		String mbBroadband = request.getParameter("mbBroadband");
		if ((is_normal != null) && (!"".equals(is_normal)) && (!"-1".equals(is_normal)))
		{
			sqlDevice = sqlDevice + " and t.is_normal =" + is_normal;
			fileter = fileter + "&is_normal=" + is_normal;
		}
		if ((mbBroadband != null) && (!"".equals(mbBroadband)) && (!"-1".equals(mbBroadband)))
		{
			sqlDevice = sqlDevice + " and t.mbbroadband =" + mbBroadband;
			fileter = fileter + "&mbBroadband=" + mbBroadband;
		}
		if ((str_devicetype_id != null) && (!str_devicetype_id.equals("-1"))
				&& (!str_devicetype_id.equals("")))
		{
			sqlDevice = sqlDevice + " and a.devicetype_id =" + str_devicetype_id;
			fileter = fileter + "&devicetype_id=" + str_devicetype_id;
		}
		else if ((str_devicetype != null) && (!str_devicetype.equals("-1"))
				&& (!str_devicetype.equals("")))
		{
			sqlDevice = sqlDevice + " and a.devicetype_id =" + str_devicetype;
			fileter = fileter + "&devicetype_id=" + str_devicetype;
		}
		if ((str_vendor_id != null) && (!str_vendor_id.equals("-1"))
				&& (!str_vendor_id.equals("")))
		{
			sqlDevice = sqlDevice + " and a.vendor_id ='" + str_vendor_id + "' ";
			fileter = fileter + "&vendor_id=" + str_vendor_id;
		}
		if ((startTime != null) && (!"".equals(startTime)))
		{
			sqlDevice = sqlDevice + " and a.complete_time >= " + startTime;
			fileter = fileter + "&startTime=" + startTime;
		}
		if ((endTime != null) && (!"".equals(endTime)))
		{
			sqlDevice = sqlDevice + " and a.complete_time <= " + endTime;
			fileter = fileter + "&endTime=" + endTime;
		}
		if ((str_city_id == null) || ("".equals(str_city_id)))
		{
			str_city_id = curUser.getCityId();
		}
		if (str_city_id.equals(curUser.getCityId()))
		{
			sqlDevice = sqlDevice + " and a.city_id = '" + str_city_id + "' ";
		}
		else if (!"00".equals(str_city_id))
		{
			if ("-1".equals(str_city_id))
			{
				str_city_id = curUser.getCityId();
			}
			else
			{
				List cityList = CityDAO.getAllNextCityIdsByCityPid(str_city_id);
				sqlDevice = sqlDevice + " and a.city_id in ("
						+ StringUtils.weave(cityList) + ") ";
			}
			fileter = fileter + "&city_id=" + str_city_id;
		}
		fileter = fileter + "&city_id=" + str_city_id;
		PrepareSQL psql = new PrepareSQL(sqlDevice);
		psql.getSQL();
		this.cursor = DataSetBean.getCursor(sqlDevice, DataSourceTypeCfgPropertiesManager
				.getInstance().getConfigItem(getClass().getName()));
		return this.cursor;
	}

}
