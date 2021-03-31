package com.linkage.litms.resource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBAdapter;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.module.gwms.Global;

/**
 * @author Jason(3412)
 * @date 2008-11-11
 */
public class DeviceActUtil {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DeviceActUtil.class);
	/**
	 * 这里String的大小比对方式和sybase的order by排序方式一样 设备序列号是否已经存在 (二分查找)
	 * 
	 * @param 设备序列号
	 * @author Jason(3412)
	 * @date 2008-11-11
	 * @return boolean
	 */
	public static boolean isExit(String deviceSn, Cursor sor) {

		if (deviceSn == null)
			return true;
		if (sor == null || sor.getRecordSize() <= 0)
			return false;
		int length = sor.getRecordSize();
		int min = 0;
		int max = length--;
		int midden = (min + max) / 2;
		int result = 0;
		String device_serialnumber = "";
		while (min <= midden && midden <= max) {
			device_serialnumber = (String) sor.getRecord(midden).get(
					"device_serialnumber");
			result = deviceSn.compareToIgnoreCase(device_serialnumber);
			if (result == 0) {
				return true;
			}
			if (midden == min || midden == max) {
				break;
			} else {
				if (result > 0) {
					min = midden;
				} else if (result < 0) {
					max = midden;
				}
			}
			midden = (min + max) / 2;
		}
		return false;
	}

	/**
	 * 获取所有设备序列号
	 * 
	 * @param gw_type：网关类型
	 *            默认为家庭网关
	 * @author Jason(3412)
	 * @date 2008-11-12
	 * @return Cursor
	 */
	public static Cursor getDeviceList(String gw_type) {
		if (gw_type == null || "".equals(gw_type))
			gw_type = "1";
		String strSQL = "select device_serialnumber from tab_gw_device "
				+ " where (device_status=0 or device_status=1) and gw_type="
				+ gw_type + " order by device_serialnumber asc";
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Cursor sor = DataSetBean.getCursor(strSQL);
		logger.debug(strSQL);
		return sor;
	}

	/**
	 * 获取用户表中所有用户
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-11-12
	 * @return Cursor
	 */
	public static HashMap<String, String> getAllUsername(String gwType) {
		String tabName = "tab_hgwcustomer";
		if ("2".equals(gwType))
			tabName = "tab_egwcustomer";
		String querySQL = "select device_serialnumber, username from "
				+ tabName
				+ " where (user_state='1' or user_state='2') and device_serialnumber is not null";
		PrepareSQL psql = new PrepareSQL(querySQL);
    	psql.getSQL();
		return DataSetBean.getMap(querySQL);
	}

	public static String getUsername(String serialnumber, Map serialUserMap) {
		String username = (String) serialUserMap.get(serialnumber);
		if (username == null)
			return "";
		return username;
	}

	/**
	 * 添加数据到表tab_user_dev的SQL
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-11-12
	 * @return String ：sql
	 */
	public static String getAddUserDevSQL(String username, String oui,
			String serialnumber, String servTypeId) {
		String addSQL = "insert into tab_user_dev (username,oui,serialnumber,serv_type_id) "
				+ " values ('"
				+ username
				+ "','"
				+ oui
				+ "','"
				+ serialnumber
				+ "'," + servTypeId + ")";
		PrepareSQL psql = new PrepareSQL(addSQL);
    	psql.getSQL();
		return addSQL;
	}

	/**
	 * 获取设备型号下拉列表
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-11-21
	 * @return String
	 */
//	public String getDeviceModelList(boolean flag, String compare, String rename, String oui){
//		
//		String strSQL = "select device_model,oui from gw_device_model";
//		if(oui != null && !"".equals(oui))
//			strSQL += " where oui='" + oui + "'";
//		logger.debug("getdevicemodellist:" + strSQL);
//		Cursor cursor = DataSetBean.getCursor(strSQL);
//		String strDeviceModelList = FormUtil.createListBox_replace(cursor, "device_model", "device_model",
//				"device_model", "oui", flag, compare, rename, true);
//		cursor = null;
//		return strDeviceModelList;
//	}
	public String getDeviceModelList(boolean flag, String compare, String rename, String vendor_id){
		
		String strSQL = "select device_model_id,device_model,vendor_id from gw_device_model";
		if(vendor_id != null && !"".equals(vendor_id))
			strSQL += " where vendor_id='" + vendor_id + "'";
		logger.debug("getdevicemodellist:" + strSQL);
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(strSQL);
		String strDeviceModelList = FormUtil.createListBox_replace(cursor, "device_model_id", "device_model",
				"device_model", "vendor_id", flag, compare, rename, true);
		cursor = null;
		return strDeviceModelList;
	}
	
	/**
	 * 获取设备类型的清单表
	 * 
	 * @return
	 */
	public ArrayList getDeviceTypeList(HttpServletRequest request) {
		ArrayList list = new ArrayList();
		list.clear();
		String getDeviceTypeListSQL = "select a.*,b.vendor_add,c.device_model "
				+ " FROM tab_devicetype_info a, tab_vendor b, gw_device_model c"
				+ " where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getDeviceTypeListSQL = "select a.area_id,a.specversion,a.hardwareversion,a.softwareversion,a.devicetype_id,b.vendor_add,c.device_model "
					+ " FROM tab_devicetype_info a, tab_vendor b, gw_device_model c"
					+ " where a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id";
		}
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(getDeviceTypeListSQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(getDeviceTypeListSQL);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(getDeviceTypeListSQL, offset, pagelen);
		list.add(cursor);
		return list;
	}
	
	/**
	 * get device_id
	 * 
	 * @param count
	 * @return
	 */
	synchronized static public int getProMaxDeviceId(int count) {
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return getProMaxDeviceIdOld(count);
		}
		
		// TELEDB
		return StringUtil.getIntegerValue(DbUtils.getUnusedID("sql_tab_gw_device", count));
	}
	
    /**
     * 获取最大id，根据数据库类型调用不同方法。
     * @param count
     * @return 返回获取的最大id
     */
	
	public static int getProMaxDeviceIdOld(int count) {
		if (count <= 0)
			return -2;
		logger.debug("getMaxDeviceId4Oracle({})", count);
		long serial = -1;
		if (count <= 0)
		{
			serial = -2;
			return (int)serial;
		}
		CallableStatement cstmt = null;
		Connection conn = null;
		String sql = "{call maxTR069DeviceIdProc(?,?)}";
		try
		{
			conn = DBAdapter.getJDBCConnection();
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, count);
			cstmt.registerOutParameter(2, Types.INTEGER);
			cstmt.execute();
			serial = cstmt.getLong(2);
		}
		catch (Exception e)
		{
			logger.error("GetUnusedDeviceSerial Exception:{}", e.getMessage());
		}
		finally
		{
			sql = null;
			if (cstmt != null)
			{
				try
				{
					cstmt.close();
				}
				catch (SQLException e)
				{
					logger.error("cstmt.close SQLException:{}", e.getMessage());
				}
				cstmt = null;
			}
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (Exception e)
				{
					logger.error("conn.close error:{}", e.getMessage());
				}
				conn = null;
			}
		}
		return (int)serial;
	}
	
	/**
	 * 根据设备ID获取设备信息
	 * 
	 * @param device_id
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return Map
	 */
	public static Map getDeviceInfo(String deviceId){
		String strSQL = "select * from tab_gw_device where device_id='"
				+ deviceId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select gather_id, oui, device_serialnumber from tab_gw_device where device_id='"
					+ deviceId + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		return DataSetBean.getRecord(strSQL);
	}
	
	/**
	 * 逐位比较字符串的大小
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-11-11
	 * @return int
	 * 
	 * 0:相等; 1:s1>s2; -1:s1<s2; -2:s1 is null or s2 is null or ""
	 */
	public static int bigger(String s1, String s2) {
		if (s1 == null || s2 == null)
			return -2;
		if (s1.equals(s2))
			return 0;
		int s1Len = s1.length();
		int s2Len = s2.length();
		int i = 0;
		int len = (s1Len > s2Len ? s2Len : s1Len);
		for (i = 0; i < len; i++) {
			if (s1.charAt(i) > s2.charAt(i)) {
				return 1;
			} else if (s1.charAt(i) < s2.charAt(i)) {
				return -1;
			} else {
				i++;
				if (i == len) {
					if (s1Len == s2Len)
						return 0;
					else if (s1Len > s2Len)
						return 1;
					else
						return -1;
				}
			}
		}
		return -2;
	}
}
