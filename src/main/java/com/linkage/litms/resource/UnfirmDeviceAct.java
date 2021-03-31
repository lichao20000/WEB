/**
 * 
 */
package com.linkage.litms.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author zhaixf(3412)
 * @date 2008-8-5
 */
public class UnfirmDeviceAct {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(UnfirmDeviceAct.class);
	/**
	 * 按条件获取未确认设备信息
	 * 
	 * @param 
	 * @author zhaixf(3412)
	 * @date 2008-8-5
	 * @return Cursor
	 */
	public static Cursor unConfirmDeviceList(HttpServletRequest request){
		String gw_type = request.getParameter("gw_type");
		String device_serialnumber = request.getParameter("device_serialnumber");
		String loopback_ip = request.getParameter("loopback_ip");
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		
		String user_city_id = curUser.getCityId();
		
		String unConfirmDeviceSQL = "select city_id,gw_type,devicetype_id,oui,device_serialnumber from tab_gw_device where device_status=0 ";
		if(isNotNullAndEmpty(gw_type)){
			unConfirmDeviceSQL += " and gw_type =" + gw_type;
		}
		if(isNotNullAndEmpty(device_serialnumber)){
			if(device_serialnumber.length()>5){
				unConfirmDeviceSQL += " and dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
			}
			unConfirmDeviceSQL += " and device_serialnumber like '%" + device_serialnumber + "'";
		}
		if(isNotNullAndEmpty(loopback_ip)){
			unConfirmDeviceSQL += " and loopback_ip like '%" + loopback_ip + "%'";
		}
		
		if (!user.isAdmin()) {
			List list = CityDAO.getAllNextCityIdsByCityPid(user_city_id);
			unConfirmDeviceSQL += " and city_id in (" + StringUtils.weave(list) + ",'00')";
			list = null;
		}
		
		PrepareSQL psql = new PrepareSQL(unConfirmDeviceSQL);
		psql.getSQL();
		Cursor sor = DataSetBean.getCursor(unConfirmDeviceSQL);
		logger.debug("unConfirmDeviceList: execute over");
		return sor;
	}
	
	/**
	 * 获取设备型号版本map
	 * 
	 * @param 
	 * @author zhaixf(3412)
	 * @date 2008-8-5
	 * @return Map
	 */
	public static Map getDeviceTypeMap(){
		String devicemodel = "";
		String softwareversion = "";
		String devicetype_id = "";
		Map deviceTypeMap = new HashMap();
//		Cursor cursorTmp = DataSetBean.getCursor("select * from tab_devicetype_info");
		PrepareSQL psql = new PrepareSQL("select a.softwareversion,a.devicetype_id,b.device_model " +
				" from tab_devicetype_info a, gw_device_model b" +
				" where a.device_model_id=b.device_model_id");
		psql.getSQL();
		Cursor cursorTmp = DataSetBean.getCursor("select a.softwareversion,a.devicetype_id,b.device_model " +
				" from tab_devicetype_info a, gw_device_model b" +
				" where a.device_model_id=b.device_model_id");
		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null){
			devicemodel = (String)fieldTmp.get("device_model");
			softwareversion = (String)fieldTmp.get("softwareversion");
			//型号，软件版本数组，modelsoft[0]为型号，modelsoft[1]为软件版本
			String[] modelsoft = new String[2];
			modelsoft[0] = devicemodel;
			modelsoft[1] = softwareversion;
			devicetype_id = (String)fieldTmp.get("devicetype_id");
			deviceTypeMap.put(devicetype_id,modelsoft);
			fieldTmp = cursorTmp.getNext();
		}
		return deviceTypeMap;
	}
	/**
	 * 参数不等于null，并且不为""
	 * 
	 * @param 
	 * @author zhaixf(3412)
	 * @date 2008-8-5
	 * @return boolean
	 */
	public static boolean isNotNullAndEmpty(String param){
		if(null != param && !"".equals(param)){
			return true;
		}
		return false;
	}
}
