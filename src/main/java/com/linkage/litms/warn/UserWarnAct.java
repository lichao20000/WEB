/**
 * 
 */
package com.linkage.litms.warn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
/**
 * @author zhaixf
 * @date 2008-7-24
 */
public class UserWarnAct {
	
	/**
	 * 
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-7-24
	 * @return Cursor
	 */
	public static Cursor getUserWarn(HttpServletRequest request){
		//从request中获取查询字段
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String city_id = request.getParameter("city_id");
		String warnType = request.getParameter("warnType");
		//如果属地为空，则以当前用户属地为
		if (null == city_id || "".equals(city_id)){
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			User user = curUser.getUser();
			city_id = user.getCityId();
		}
		String strSQL = "";
		if(null != username && !"".equals(username)){
			strSQL = "where username = '" + username + "'";
		}
		
		DataSetBean.getCursor(strSQL);
		
		return null;
	}
	
	/**
	 * 获取线路连接状态信息
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-7-24
	 * @return Cursor
	 */
	public static Cursor getUserLinkStat(HttpServletRequest request){

		String device_serialnumber = request.getParameter("device_serialnumber");
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String city_id = user.getCityId();
		List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
		String sqlCitys = StringUtils.weave(list);
		list = null;
		StringBuilder strSQL = new StringBuilder();
		strSQL.append("select a.*, b.* from gw_devicestatus a, tab_gw_device b " +
				"where a.device_id = b.device_id");
		strSQL.append(" and b.city_id in (");
		strSQL.append(sqlCitys);
		strSQL.append(")");
		if(null != device_serialnumber && !"".equals(device_serialnumber)){
			if(device_serialnumber.length()>5){
				strSQL.append(" and b.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
			}
			strSQL.append(" and device_serialnumber like '%" + device_serialnumber + "'");
		}
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return DataSetBean.getCursor(strSQL.toString());
	}
	
	/**
	 * 获取线路连接状态信息
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-7-25
	 * @return Map
	 */
	public static Map getDetailLinkStat(String device_id){
		
		if(device_id == null || "".equals(device_id)){
			return null;
		}	
		String strSQL = "select * from gw_devicestatus where device_id='" + device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select reboot_remark1, reboot_remark2, reboot_remark3, reboot_remark4, stop_remark1, stop_remark2," +
					" stop_remark3, stop_remark4, statusuptime, restart_time, last_auth_time, lastoutlinetime " +
					" from gw_devicestatus where device_id='" + device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Map map = DataSetBean.getRecord(strSQL);
		if(map != null && !map.isEmpty()){
			/*************************模块数据格式转化*******************/
			StringBuilder reboot_remark = new StringBuilder();
			int tmp = stringToInt((String)map.get("reboot_remark1"));
			reboot_remark.append(Integer.toBinaryString(tmp));
			tmp = stringToInt((String)map.get("reboot_remark2"));
			reboot_remark.append(Integer.toBinaryString(tmp));
			tmp = stringToInt((String)map.get("reboot_remark3"));
			reboot_remark.append(Integer.toBinaryString(tmp));
			tmp = stringToInt((String)map.get("reboot_remark4"));
			reboot_remark.append(Integer.toBinaryString(tmp));
			
			StringBuilder stop_remark = new StringBuilder();
			tmp = stringToInt((String)map.get("stop_remark1"));
			stop_remark.append(tmp);
			tmp = stringToInt((String)map.get("stop_remark2"));
			stop_remark.append(tmp);
			tmp = stringToInt((String)map.get("stop_remark3"));
			stop_remark.append(tmp);
			tmp = stringToInt((String)map.get("stop_remark4"));
			stop_remark.append(tmp);
			/*********************************************************/
			//修改时间格式
			String statusuptime = dateFormat(stringToLong((String)map.get("statusuptime"))*1000);
			String restart_time = dateFormat(stringToLong((String)map.get("restart_time"))*1000);
			String last_auth_time = dateFormat(stringToLong((String)map.get("last_auth_time"))*1000);
			String lastoutlinetime = dateFormat(stringToLong((String)map.get("lastoutlinetime"))*1000);
			
			map.put("reboot_remark", reboot_remark.toString());
			map.put("stop_remark", stop_remark.toString());
			
			map.put("statusuptime", statusuptime);
			map.put("last_auth_time", last_auth_time);
			map.put("restart_time", restart_time);
			map.put("lastoutlinetime", lastoutlinetime);
		}
		return map;
	}
	
	/**
	 * 获取客户相关信息
	 * 
	 * @param 
	 * @author johnson
	 * @date 2008-7-25
	 * @return Map
	 */
	public static Map getCustomerinfo(String device_id){
		
		String customer_id = "";
		if(device_id == null || "".equals(device_id)){
			return null;
		}
		String strSQL = "select customer_id from tab_gw_device where device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		Map map = DataSetBean.getRecord(strSQL);
		if(map != null){
			customer_id = (String)map.get("customer_id");
			strSQL = "select * from tab_customerinfo where customer_id='"+customer_id+"'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				strSQL = "select city_id, city_name from tab_customerinfo where customer_id='"+customer_id+"'";
			}
			psql = new PrepareSQL(strSQL);
			psql.getSQL();
			map = DataSetBean.getRecord(strSQL);
			if(map != null && !map.isEmpty()){
				Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
				map.put("city_name", cityMap.get((String)map.get("city_id")));
				cityMap = null;
			}

		}
		return map;
	}
	
	
	
	public static String dateFormat(long time){
		return dateFormat("yyyy-MM-dd HH:mm:ss", time);
	}
	/**
	 * 用long型时间返回格式字符串
	 * 
	 * @param 
	 * @author johnson
	 * @date 2008-7-25
	 * @return String
	 */
	public static String dateFormat(String format, long time){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time));
	}
	/**
	 * String转换为int，如果param为null或者""则返回0
	 * 
	 * @param 
	 * @author johnson
	 * @date 2008-7-25
	 * @return int
	 */
	public static int stringToInt(String tmp){
		
		if(tmp == null || "".equals(tmp)){
			return 0;
		}
		return Integer.parseInt(tmp);
	}
	
	/**
	 * String转换为long，如果param为null或者""则返回0
	 * 
	 * @param 
	 * @author johnson
	 * @date 2008-8-13
	 * @return long
	 */
	public static long stringToLong(String tmp){
		
		if(tmp == null || "".equals(tmp)){
			return 0;
		}
		return Long.parseLong(tmp);
	}
	
}
