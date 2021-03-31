package com.linkage.litms.resource;

/**
 * zhaixf(3412) 2008-05-08
 * req:XJDX_ITMS-BUG-20080506-XXF-001
 * 
 * zhaixf(3412) 2008-05-08
 * req:NJLC_HG-BUG-ZHOUMF-20080508-001
 *
 */
/**
 zhaixf(3412) 2008-04-22
 JSDX_ITMS-BUG-YHJ-20080421-001
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.queries.function.valuesource.MultiFunction.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.Global;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.litms.webtopo.MCControlManager;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Aug 26, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings( { "unused", "unchecked" })
public class EGWUserInfoAct {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(EGWUserInfoAct.class);
	private static final int DB_MYSQL=com.linkage.module.gwms.Global.DB_MYSQL;
	private PrepareSQL pSQL;

	private String str_user_id = null;

	/**
	 * 新增企业网关用户信息
	 */
	private String m_EGWUserInfoAdd_SQL = "insert into tab_egwcustomer (user_id,username,passwd,"
			+ "serv_type_id, wan_type, vpiid, vciid, customer_id, user_state, "
			+ " access_style_id, opmode, onlinedate, dealdate, "
			+ " opendate, pausedate, closedate,ipaddress, ipmask, gateway, adsl_ser, oui, device_serialnumber, "
			+ "maxattdnrate, upwidth , lan_num, ssid_num, work_model, vlanid, flag_pvc) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	private String m_serv_users_List_SQL = "select a.user_id, a.gather_id, a.username, a.cotno,a.cust_type_id, a.user_type_id,a.user_state from tab_egwcustomer a"
			+ " where a.service_id = ? and a.user_state = ?";

	private List<String> stringKeyList = null;
	private List<String> intKeyList = null;
	
	public EGWUserInfoAct() {
		pSQL = new PrepareSQL();
	}

	/**
	 * 增加、删除、更新
	 * 
	 * @param request
	 * @return String modify by zhaixf
	 */
	public String EGWUserInfoActExe(HttpServletRequest request) {
		String strSQL = "";
		String strMsg = "";
		String strAction = request.getParameter("action");

		UserRes curUser = (UserRes) request.getSession()
				.getAttribute("curUser");
		User user = curUser.getUser();

		String oprateUserId = ((UserRes) request.getSession().getAttribute(
				"curUser")).getUser().getAccount();
		long time = new Date().getTime();
		time = time / 1000;
		String devOui = null;
		String devSerial = null;
		if (strAction.equals("delete")) { // 删除操作

			String str_user_id = request.getParameter("user_id");
			String str_gather_id = request.getParameter("gather_id");

			int int_user_id = Integer.parseInt(str_user_id);

			/**
			 * add 2009/03/19 by qixueqi if user having device,then return;else
			 * countiune
			 */
			String hasdevicesql = "select device_id from tab_egwcustomer where user_id="
					+ int_user_id;
			PrepareSQL psql = new PrepareSQL(hasdevicesql);
	    	psql.getSQL();
			HashMap hasMap = DataSetBean.getRecord(hasdevicesql);

			if (null != hasMap && !hasMap.isEmpty()) {

				String deviceId = String.valueOf(
						hasMap.get("device_id")).toString();
				if (null != deviceId && !"".equals(deviceId)) {
					return "该用户已绑定设备，删除设备前请先解绑！";
				}
			}

			// 删除用户，把用户移到_bak表中
			if(DBUtil.GetDB() == DB_MYSQL) 
			{// mysql
				backEgwcustomer(int_user_id);	
			}
			else {
				strSQL += "insert into tab_egwcustomer_bak select * from tab_egwcustomer where user_id="
						+ int_user_id + ";";
			}
			
			strSQL += "update tab_egwcustomer_bak set updatetime=" + time
					+ ",staff_id='" + oprateUserId + "' where user_id="
					+ int_user_id + ";";
			strSQL += "delete from tab_egwcustomer where user_id="
					+ int_user_id + ";";

		} else if (strAction.equals("deleteBatch")) { // 删除操作
			List<String> devIdList = new ArrayList<String>();
			ArrayList<String> delSqlList = new ArrayList<String>();

			String[] arr_isCheckedToDel = request
					.getParameterValues("isCheckedToDel");
			for (int i = 0; i < arr_isCheckedToDel.length; i++) {
				String str_user_id = arr_isCheckedToDel[i].split("\\|")[0];
				String str_gather_id = arr_isCheckedToDel[i].split("\\|")[1];
				int int_user_id = Integer.parseInt(str_user_id);
				strSQL += "delete from tab_egwcustomer where user_id="
						+ int_user_id + ";";
				// 删除用户，把用户移到_bak表中
				if(DBUtil.GetDB() == DB_MYSQL) 
				{// mysql
					backEgwcustomer(int_user_id);	
				}
				else {
					strSQL += "insert into tab_egwcustomer_bak select * from tab_egwcustomer where user_id="
							+ int_user_id + ";";
				}
				
				strSQL += "update tab_egwcustomer_bak set updatetime=" + time
						+ ",staff_id='" + oprateUserId + "' where user_id="
						+ int_user_id + ";";
				strSQL += "delete from tab_egwcustomer where user_id="
						+ int_user_id + ";";
				// by zhaixf
				// 查看该用户有没有设备信息
				String deviceSQL = "select device_serialnumber,oui from tab_egwcustomer where user_id="
						+ int_user_id;
				PrepareSQL psql = new PrepareSQL(deviceSQL);
		    	psql.getSQL();
				Map recordMap = DataSetBean.getRecord(deviceSQL);
				if (null != recordMap && !recordMap.isEmpty()) {
					String serialnumber = (String) recordMap
							.get("device_serialnumber");
					String oui = (String) recordMap.get("oui");

					// 删除 网关域权限表/tab_gw_res_area 中数据，并通知MC
					String getDeviceIdSql = "select device_id from tab_gw_device where oui='"
							+ oui
							+ "' and device_serialnumber='"
							+ serialnumber + "'";
					psql = new PrepareSQL(getDeviceIdSql);
			    	psql.getSQL();
					HashMap hm = DataSetBean.getRecord(getDeviceIdSql);
					if (null != hm) {
						String deviceId = (String) hm.get("device_id");
						if (null != deviceId) {
							String delSql = "delete from tab_gw_res_area where res_id='"
									+ deviceId
									+ "' and res_type=1 and area_id=(select area_id from tab_city_area where city_id=(select city_id from tab_customerinfo where customer_id=(select customer_id from tab_egwcustomer where user_id="
									+ str_user_id + ")))";
							psql = new PrepareSQL(delSql);
					    	psql.getSQL();
							delSqlList.add(delSql);
							devIdList.add(deviceId);
						}
					}

					// 如果有则查看该设备有没有其他用户
					if (null != serialnumber && !"".equals(serialnumber)) {
						String userSQL = "select user_id from tab_egwcustomer where device_serialnumber='"
								+ serialnumber
								+ "' and oui='"
								+ oui
								+ "' and user_state != '0' and user_state != '3'";
						psql = new PrepareSQL(userSQL);
				    	psql.getSQL();
						recordMap = DataSetBean.getRecord(userSQL);
						// 如果没有则把设备表中设备状态置为未绑定，同时把客户ID置为空
						if (null == recordMap || recordMap.isEmpty()) {
							strSQL += "update tab_gw_device set cpe_allocatedstatus=0, customer_id = null "
									+ "where oui='"
									+ oui
									+ "' and device_serialnumber='"
									+ serialnumber + "';";
						}
					}
				}
				if (i != arr_isCheckedToDel.length - 1) {
					strSQL += ";";
				}
			}
			// 批量执行 (删除 网关域权限表/tab_gw_res_area 中数据，并通知MC)
			DataSetBean.doBatch(delSqlList);
			MCControlManager mc = new MCControlManager(user.getAccount(), user
					.getPasswd());
			String[] device_array = (String[]) devIdList.toArray();
			int retMsg = mc.reloadDeviceAraeInfo(device_array);
			if (retMsg != 0) {
				logger.warn("通知后台CORBA失败(调用MC时)");
			} else {
				logger.warn("通知后台CORBA成功(调用MC时)");
			}
		} else {
			String username = request.getParameter("username");
			String passwd = request.getParameter("passwd");
			String city_id = request.getParameter("city_id");
			String access_style_id = request.getParameter("access_style_id");
			String adsl_ser = request.getParameter("adsl_ser");
			String ipaddress = request.getParameter("ipaddress");
			String ipmask = request.getParameter("ipmask");
			String gateway = request.getParameter("gateway");
			String vlanid = request.getParameter("vlanid");
			String user_state = request.getParameter("user_state");
			String opendate = request.getParameter("hidOpenDate");
			String onlinedate = request.getParameter("hidOnlineDate");
			String pausedate = request.getParameter("hidPauseDate");
			String closedate = request.getParameter("hidCloseDate");
			String vpiid = request.getParameter("vpiid");
			String vciid = request.getParameter("vciid");
			String userline = request.getParameter("userline");
			// by zhaixf itms安装则置为1，用于和iposs（0）区分
			if (null == userline || "".equals(userline))
				userline = String.valueOf(UserInstAct.ITMSUSERLINE);

			String dealdate = request.getParameter("hidDealDate");
			String opmode = request.getParameter("opmode");
			String maxattdnrate = request.getParameter("maxattdnrate");
			String upwidth = request.getParameter("upwidth");

			// 设备信息
			String oui = request.getParameter("oui");
			devOui = oui;
			String device_serialnumber = request
					.getParameter("device_serialnumber");
			devSerial = device_serialnumber;
			String e_id = request.getParameter("e_id");
			logger.debug("E_ID-----------------:" + e_id);
			String service_id = request.getParameter("some_service");

			String flag_pvc = "0";
			if ("12".equals(service_id)) {
				flag_pvc = "1";
			}

			String wan_type = request.getParameter("wan_type");
			String lan_num = request.getParameter("lan_num");
			String ssid_num = request.getParameter("ssid_num");
			String work_model = request.getParameter("work_model");

			if (wan_type == null || wan_type.equals("")) {
				wan_type = "1";
			}

			if (strAction.equals("add")) {

				// 判断设备是否已经开通了该业务
				if (isOpenServ(device_serialnumber, oui, service_id)) {
					return "操作失败：该设备已经开通了该项业务，请重新添加！";
				}

				// 取得新的userid
				String callPro = "maxEgwUserIdProc 1";
				Map map = DataSetBean.getRecord(callPro);
				if (null != map && !map.isEmpty()) {
					str_user_id = map.values().toArray()[0].toString();
				} else {
					long userid = DataSetBean.getMaxId("tab_egwcustomer",
							"user_id");
					str_user_id = String.valueOf(userid);
					logger.debug("----get-str_userid-from-sql-");
				}

				logger.debug("----str_userid--" + str_user_id);
				pSQL.setSQL(m_EGWUserInfoAdd_SQL);
				// Integer.parseInt(str_userid)
				pSQL.setInt(1, Integer.parseInt(str_user_id));
				pSQL.setString(2, username);
				pSQL.setString(3, passwd);
				if (null != service_id && !"".equals(service_id)) {
					pSQL.setInt(4, Integer.parseInt(service_id));
				}
				if (null != wan_type && !"".equals(wan_type)) {
					pSQL.setInt(5, Integer.parseInt(wan_type));
				}
				pSQL.setString(6, vpiid);
				if (null != vciid && !"".equals(vciid)) {
					pSQL.setInt(7, Integer.parseInt(vciid));
				}
				pSQL.setString(8, e_id);
				pSQL.setString(9, user_state);
				if (null != access_style_id && !"".equals(access_style_id)) {
					pSQL.setInt(10, Integer.parseInt(access_style_id));
				}
				pSQL.setString(11, opmode);
				if (onlinedate != null && onlinedate.length() != 0) {
					pSQL.setInt(12, Integer.parseInt(onlinedate));
				} else {
					pSQL.setString(12, ",,");
				}
				if (dealdate != null && dealdate.length() != 0) {
					pSQL.setInt(13, Integer.parseInt(dealdate));
				} else {
					pSQL.setString(13, ",,");
				}
				if (opendate != null && opendate.length() != 0) {
					pSQL.setInt(14, Integer.parseInt(opendate));
				} else {
					pSQL.setString(14, ",,");
				}
				if (pausedate != null && pausedate.length() != 0) {
					pSQL.setInt(15, Integer.parseInt(pausedate));
				} else {
					pSQL.setString(15, ",,");
				}
				if (closedate != null && closedate.length() != 0) {
					pSQL.setInt(16, Integer.parseInt(closedate));
				} else {
					pSQL.setString(16, ",,");
				}
				pSQL.setString(17, ipaddress);
				pSQL.setString(18, ipmask);
				pSQL.setString(19, gateway);
				pSQL.setString(20, adsl_ser);
				pSQL.setString(21, oui);
				pSQL.setString(22, device_serialnumber);
				pSQL.setInt(23, Integer.parseInt(maxattdnrate));
				pSQL.setInt(24, Integer.parseInt(upwidth));
				pSQL.setInt(25, Integer.parseInt(lan_num));
				pSQL.setInt(26, Integer.parseInt(ssid_num));
				pSQL.setInt(27, Integer.parseInt(work_model));
				pSQL.setString(28, vlanid);
				pSQL.setStringExt(29, flag_pvc, false);
				// 添加用户信息
				strSQL = pSQL.getSQL();
				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				strSQL = strSQL.replaceAll(",'null',", ",null,");

				String citySQL = "select city_id from tab_customerinfo where customer_id='"
						+ e_id + "'";
				PrepareSQL psql = new PrepareSQL(citySQL);
		    	psql.getSQL();
				Map cityMap = DataSetBean.getRecord(citySQL);
				if (cityMap != null) {
					city_id = (String) cityMap.get("city_id");
				}

				// 如果有设备，则更新设备的绑定状态和客户ID
				if (null != device_serialnumber
						&& !"".equals(device_serialnumber)) {
					// devNotNull = true;
					strSQL += "update tab_gw_device set cpe_allocatedstatus=1, customer_id='"
							+ e_id
							+ "',city_id='"
							+ city_id
							+ "'"
							+ " where device_serialnumber='"
							+ device_serialnumber + "' and oui='" + oui + "';";
					// 添加用户时，当设备信息不为空的时候，向网关域权限表添加纪录
					String deviceIdSQL = "select device_id from tab_gw_device "
							+ " where device_serialnumber='"
							+ device_serialnumber + "' and oui='" + oui + "'";
					psql = new PrepareSQL(deviceIdSQL);
			    	psql.getSQL();
					Map deviceIdMap = DataSetBean.getRecord(deviceIdSQL);
					String newDeviceId = (String) deviceIdMap.get("device_id");
					// 当该设备存在于表中时，先删除该设备信息。
					strSQL += "; delete from tab_gw_res_area where res_id='"
							+ newDeviceId + "'" + " and res_type=1";
					// 获得当前用户域ID
					String curAreaId = getAreaId(city_id);
					logger.debug("--curAreaId--" + curAreaId);
					if (null == curAreaId) {
						curAreaId = String.valueOf(((UserRes) request
								.getSession().getAttribute("curUser"))
								.getAreaId());
					}
					strSQL += insertAreaTable(newDeviceId, curAreaId);

				}
			} else {

				str_user_id = request.getParameter("user_id");
				// 查看用户数据库中设备状态
				String deviceSQL = "select device_serialnumber,oui from tab_egwcustomer where user_id="
						+ str_user_id;
				PrepareSQL psql = new PrepareSQL(deviceSQL);
		    	psql.getSQL();
				HashMap map = DataSetBean.getRecord(deviceSQL);
				String oldSerialnumber = "";
				String oldOUI = "";

				if (null != map && !map.isEmpty()) {
					oldSerialnumber = (String) map.get("device_serialnumber");
					oldOUI = (String) map.get("oui");

					// 更新 网关域权限表/tab_gw_res_area 中数据，并通知MC
					// String getDeviceIdSql = "select device_id from
					// tab_gw_device where oui='" + oui
					// + "' and device_serialnumber='" + device_serialnumber +
					// "'";
					// HashMap hm = DataSetBean.getRecord(getDeviceIdSql);
					// if (null != hm) {
					// String deviceId = (String) hm.get("device_id");
					// if (null != deviceId) {
					// String getAreaIdSql = "select area_id from tab_city_area
					// where city_id=(select city_id from tab_customerinfo where
					// customer_id=(select customer_id from tab_egwcustomer
					// where user_id="
					// + str_user_id + "))";
					// HashMap hm2 = DataSetBean.getRecord(getAreaIdSql);
					// if (null != hm2) {
					// String areaId = (String) hm2.get("area_id");
					// if (null != areaId) {
					// String sql = null;
					// String selSql = "select count(*) num from tab_gw_res_area
					// where res_id='"
					// + deviceId
					// + "' and res_type=1 and area_id=(select area_id from
					// tab_city_area where city_id=(select city_id from
					// tab_customerinfo where customer_id=(select customer_id
					// from tab_egwcustomer where user_id="
					// + str_user_id + ")))";
					// HashMap hm3 = DataSetBean.getRecord(selSql);
					// if (null != hm3 && "0".equals((String) hm3.get("num"))) {
					// sql = "insert into tab_gw_res_area values (1, '" +
					// deviceId + "', " + areaId + ")";
					// } else {
					// sql = "update tab_gw_res_area set area_id="
					// + areaId
					// + " where res_type=1 and res_id='"
					// + deviceId
					// + "' and area_id=(select area_id from tab_city_area where
					// city_id=(select city_id from tab_customerinfo where
					// customer_id=(select customer_id from tab_egwcustomer
					// where user_id="
					// + str_user_id + ")))";
					// }
					// DataSetBean.executeUpdate(sql);
					//									
					// MCControlManager mc = new
					// MCControlManager(user.getAccount(),
					// user.getPasswd());
					// String[] device_array = { deviceId };
					// int retMsg = mc.reloadDeviceAraeInfo(device_array);
					// if (retMsg != 0) {
					// UserinstLog.log(request, "通知后台CORBA失败(调用MC时)。");
					// logger.debug("通知后台CORBA失败(调用MC时)。");
					// } else {
					// UserinstLog.log(request, "通知后台CORBA成功(调用MC时)。");
					// logger.debug("通知后台CORBA成功(调用MC时)。");
					// }
					// }
					// }
					// }
					// }
				}
				String citySQL = "select city_id from tab_customerinfo where customer_id='"
						+ e_id + "'";
				psql = new PrepareSQL(citySQL);
		    	psql.getSQL();
				Map cityMap = DataSetBean.getRecord(citySQL);
				if (cityMap != null) {
					city_id = (String) cityMap.get("city_id");
				}
				// 更新旧设备的sql语句
				String updateOldDevice = "update tab_gw_device set cpe_allocatedstatus=0, customer_id = null where device_serialnumber='"
						+ oldSerialnumber + "' and oui='" + oldOUI + "';";
				// 更新新设备的sql语句
				String updateNewDevice = "update tab_gw_device set cpe_allocatedstatus=1, customer_id='"
						+ e_id
						+ "', city_id='"
						+ city_id
						+ "'"
						+ " where device_serialnumber='"
						+ device_serialnumber
						+ "' and oui='" + oui + "';";

				// 如果页面设备不为空
				if (null != device_serialnumber
						&& !"".equals(device_serialnumber)) {
					// 查询新设备的device_id用于域表
					String deviceIdSQL = "select device_id from tab_gw_device "
							+ " where device_serialnumber='"
							+ device_serialnumber + "' and oui='" + oui + "'";
					psql = new PrepareSQL(deviceIdSQL);
			    	psql.getSQL();
					Map deviceIdMap = DataSetBean.getRecord(deviceIdSQL);
					String newDeviceId = "";
					if (deviceIdMap != null && !deviceIdMap.isEmpty()) {
						newDeviceId = (String) deviceIdMap.get("device_id");
					}

					if (null != oldSerialnumber && !"".equals(oldSerialnumber)) {
						// 更新旧设备
						strSQL += updateOldDevice;

					}
					// 更新新设备
					strSQL += updateNewDevice;

					// 当该设备存在于表中时，先删除该设备信息。
					strSQL += " delete from tab_gw_res_area where res_id='"
							+ newDeviceId + "'" + " and res_type=1";

					// 获得当前用户域ID
					String curAreaId = HGWUserInfoAct.getAreaId(city_id);
					logger.debug("--curAreaId--" + curAreaId);
					if (null == curAreaId) {
						curAreaId = String.valueOf(((UserRes) request
								.getSession().getAttribute("curUser"))
								.getAreaId());
					}
					strSQL += insertAreaTable(newDeviceId, curAreaId);
				} else {
					if (null != oldSerialnumber && !"".equals(oldSerialnumber)) {
						// 更新旧设备
						strSQL += updateOldDevice;
					}
				}

				strSQL += ";update tab_egwcustomer set username='" + username
						+ "',passwd='" + passwd + "',user_state='" + user_state
						+ "',opendate=" + opendate + ",onlinedate="
						+ onlinedate + ",pausedate=" + pausedate
						+ ",closedate=" + closedate + ",vpiid='" + vpiid
						+ "',vciid=" + vciid + ",dealdate=" + dealdate
						+ ",opmode='" + opmode + "',serv_type_id=" + service_id
						+ ",customer_id='" + e_id + "',access_style_id="
						+ access_style_id + ",ipaddress='" + ipaddress
						+ "',ipmask='" + ipmask + "',gateway='" + gateway
						+ "',adsl_ser='" + adsl_ser + "',wan_type=" + wan_type
						+ ",oui='" + oui + "',device_serialnumber='"
						+ device_serialnumber + "',maxattdnrate="
						+ maxattdnrate + ",upwidth=" + upwidth + ",lan_num="
						+ lan_num + ",ssid_num=" + ssid_num + ",work_model="
						+ work_model + ",vlanid='" + vlanid + "'"
						+ " where user_id=" + str_user_id;
				strSQL = strSQL.replaceAll("=,", "=null,");
				strSQL = strSQL.replaceAll("= where", "=null where");
				strSQL = strSQL.replaceAll("='null',", "=null,");
			}
		}

		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();

		if (!strSQL.equals("")) {
			int iCode[] = DataSetBean.doBatch(strSQL);
			if (iCode != null && iCode.length > 0) {
				strMsg = "企业网关用户操作成功！";
			} else {
				strMsg = "企业网关用户操作失败，请返回重试或稍后再试！";
			}
			logger.debug("==dobatch======>>" + strSQL);

		}

		//informMC(request, strAction, str_user_id, devOui, devSerial, user);

		return strMsg;
	}

	/**
	 * 备份用户表
	 * @param int_user_id
	 */
	private void backEgwcustomer(int int_user_id)
	{
		// 获取属性列表
		String stringKeys = "gather_id,username,passwd,city_id,cotno,numcharacter,aut_flag,service_set,realname,sex,credno,address,office_id,zone_id,licenceregno,vipcardno,contractno,linkman,,linkphone,linkaddress,,email,agent,agent_credno,agentphone,adsl_card,adsl_dev,adsl_ser,isrepair,ipaddress,,gateway,macaddress,device_id,device_ip,basdevice_id,basdevice_ip,vlanid,workid,user_state,staff_id,,phonenumber,cableid,vpiid,dslamserialno,opmode,oui,device_serialnumber,wan_value_1,wan_value_2,customer_id,bind_port,network_spec";
		String intKeys = "user_id,bill_type_id,next_bill_type_id,cust_type_id,user_type_id,bindtype,virtualnum,,cred_type_id,access_kind_id,trade_id,occupation_id,education_id,adsl_res,bandwidth,,device_shelf,device_frame,device_slot,device_port,basdevice_shelf,basdevice_frame,basdevice_slot,basdevice_port,opendate,onlinedate,pausedate,closedate,updatetime,bwlevel,vciid,adsl_hl,userline,movedate,dealdate,maxattdnrate,upwidth,serv_type_id,max_user_number,open_status,wan_type,lan_num,ssid_num,work_model,flag_pvc,binddate,stat_bind_enab,bind_flag,is_chk_bind,spec_id,sip_id,protocol";
		stringKeyList = Arrays.asList(stringKeys.split(","));
		intKeyList = Arrays.asList(intKeys.split(","));
		
		String keys = stringKeys + "," + intKeys;
		
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(stringKeyList);
		keyList.addAll(intKeyList);
		
		// 查询数据
		String querySql = "select " + keys + " from tab_hgwcustomer where user_id=" + int_user_id;
		ArrayList list = DataSetBean.executeQuery(querySql, null);
		if(list == null || list.isEmpty()) {
			return;
		}
		
		// 插入备份表
		StringBuilder insertSqls = new StringBuilder();
		String insertSql = "";
		
		for (int i = 0; i < list.size(); i++)
		{
			Map map = null;
			try
			{
				map = (Map)list.get(i);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			
			if(map == null) {
				continue;
			}
			
			insertSql = "insert into tab_hgwcustomer_bak ("+ keys +") values (" + values(map, keyList) + ");";
			insertSqls.append(insertSql);
		}
		
		DataSetBean.doBatch(insertSqls.toString());
	}

	/**
	 * 获取value列表
	 * @param map
	 * @param keyList
	 * @return
	 */
	private String values(Map map, List<String> keyList)
	{
		if(map == null || map.isEmpty() || keyList == null || keyList.isEmpty()) {
			return "";
		}
		
		List<String> stringValues = new ArrayList<String>();
		List<Integer> intValues = new ArrayList<Integer>();
		
		String stringValue = "";
		int intValue = 0;
		
		for (int i = 0; i < keyList.size(); i++)
		{
			if(stringKeyList.contains(keyList.get(i))) {
				stringValue = StringUtil.getStringValue(map, keyList.get(i));
				if(stringValue == null) {
					stringValue = "";
				}
				stringValues.add(stringValue);
			}
			else if(intKeyList.contains(keyList.get(i))) {
				intValue = StringUtil.getIntValue(map, keyList.get(i));
				intValues.add(intValue);
			}
		}

		return StringUtils.weave(stringValues) + ","  + weave(intValues);
	}
	
	
	/**
	 * list转成str
	 * @param list
	 * @return
	 */
	private String weave(List list)
	{
		StringBuffer sb = new StringBuffer(100);
		if (list.size() != 0) {
			sb.append(list.get(0));

			for (int i = 1; i < list.size(); i++) {
				sb.append(",").append(list.get(i));
			}
		}
		
		return sb.toString();
	}

	/**
	 * 通知MC
	 * 
	 * @date 2009-5-14
	 * @param request
	 * @param strAction
	 * @param str_user_id
	 * @param devOui
	 * @param devSerial
	 * @param user
	 */
	public void informMC(HttpServletRequest request, String strAction,
			String str_user_id, String devOui, String devSerial, User user) {
		// 添加 网关域权限表/tab_gw_res_area 中数据，并通知MC
		String getDeviceIdSql = "select device_id from tab_gw_device where oui='"
				+ devOui + "' and device_serialnumber='" + devSerial + "'";
		PrepareSQL psql = new PrepareSQL(getDeviceIdSql);
    	psql.getSQL();
		HashMap hm = DataSetBean.getRecord(getDeviceIdSql);
		if (null != hm) {
			String deviceId = (String) hm.get("device_id");
			logger.debug("当前通知MC的设备：" + deviceId);
			if (null != deviceId) {
				MCControlManager mc = new MCControlManager(user.getAccount(),
						user.getPasswd());
				String[] device_array = { deviceId };
				int retMsg = mc.reloadDeviceAraeInfo(device_array);
				if (retMsg != 0) {
					logger.warn("通知后台CORBA失败(调用MC时)。");
				} else {
					logger.warn("通知后台CORBA成功(调用MC时)。");
				}
			}
		}

		// if (strAction.equals("add")) {
		//
		//			
		// } else if (strAction.equals("update")) {
		//
		// }
	}

	/**
	 * 根据属地获取域信息
	 * 
	 * @param
	 * @author johnson
	 * @date 2008-7-29
	 * @return String
	 */
	public static String getAreaId(String cityId) {
		PrepareSQL psql = new PrepareSQL("select area_id from tab_city_area where city_id='"
				+ cityId + "'");
    	psql.getSQL();
		Map fields = DataSetBean
				.getRecord("select area_id from tab_city_area where city_id='"
						+ cityId + "'");
		if (fields == null) {
			return null;
		} else {
			logger.debug("--area_id--" + (String) fields.get("area_id"));
			return (String) fields.get("area_id");
		}
	}

	public static String insertAreaTable(String device_id, String curAreaId) {

		String strSql = "";

		if (null == curAreaId || "0".equals(curAreaId) || "1".equals(curAreaId)) {
			return strSql;
		}

		List list = AreaManageSyb.getAllareaPid(curAreaId);
		int listSize = list.size() - 1;

		while (listSize >= 0) {
			strSql += " insert into tab_gw_res_area(res_type,res_id,area_id) values(1,'"
					+ device_id + "'," + list.get(listSize) + ")";
			--listSize;
		}
		PrepareSQL psql = new PrepareSQL(strSql);
    	psql.getSQL();
		return strSql;
	}

	/**
	 * 所有EGW用户列表,分页
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getEGWUsersCursor(HttpServletRequest request) {

		StringBuffer sql = new StringBuffer();
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql.append("select a.cust_type_id, a.device_id, a.dealdate, a.user_state, a.username, b.customer_name, " +
					"a.city_id, a.phonenumber, a.device_serialnumber, a.user_id, a.gather_id, a.realname " +
					"from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id");
		}
		else {
			sql.append("select a.*, b.* from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id");
		}
		String filterStr = "";
		filterStr += ("&opt=" + request.getParameter("opt"));
		// ---------modify by zhaixf 加入属地判断
		UserRes userRes = (UserRes) request.getSession()
				.getAttribute("curUser");
		String cityId = userRes.getCityId();
		//String sql = "select city_id, parent_id from tab_city where city_id = '"
		//		+ cityId + "'";
		//HashMap map = DataSetBean.getMap(sql);
		// ---------

		Cursor cursor = null;

		String searchAction = request.getParameter("searchForUsers");
		// 用户统计的情况
		if ("statUsers".equals(searchAction)) {
			ArrayList list = getEGWUsersServCursor(request);

			return list;
		} else {
			// 用户检索的情况
			ArrayList list = new ArrayList();
			list.clear();
			String username = request.getParameter("username");
			String phonenumber = request.getParameter("phonenumber");
			String stroffset = request.getParameter("offset");
			String flg = request.getParameter("flg");
			int pagelen = 15;
			int offset;
			if (stroffset == null)
				offset = 1;
			else
				offset = Integer.parseInt(stroffset);

			if (request.getParameter("submit") != null
					&& (username != null || phonenumber != null)) {

				username = username.trim();
				phonenumber = phonenumber.trim();
				if (username.length() != 0) {
					sql.append(" and a.username = '");
					sql.append(username);
					sql.append("' ");
					filterStr += "&username=" + username;
				}

				if (phonenumber.length() != 0) {
					sql.append(" and a.phonenumber like '%");
					sql.append(phonenumber);
					sql.append("%' ");
					filterStr += "&phonenumber=" + phonenumber;
				}
			}
			if (flg == null) {
				filterStr += "&flg=" + flg;
			}
			sql.append(" and a.user_state in ('1','2') ");

			// -----------zhaixf----- 20090925之前
			// -----------漆学启------ 20090925
			Map<String, String> cityMap = CityDAO.getCityIdPidMap();
			if (!"-1".equals(cityMap.get(cityId))) {
				List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and a.city_id in (");
				sql.append(StringUtils.weave(list1));
				sql.append(")");
				list1 = null;
				
			}
			cityMap = null;

			// -----------------
			logger.debug("-----------m_EGWUsers_List_SQL----" + sql.toString());
			
			QueryPage qryp = new QueryPage();
			qryp.initPage(sql.toString(), offset, pagelen);
			String strBar = qryp.getPageBar(filterStr);
			list.add(strBar);
			PrepareSQL psql = new PrepareSQL(sql.toString());
	    	psql.getSQL();
			cursor = DataSetBean
					.getCursor(sql.toString(), offset, pagelen);
			list.add(cursor);

			return list;
		}

	}

	/**
	 * 所有EGW用户列表，excel
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getEGWUsersCursorExcel(HttpServletRequest request) {

		// ---------modify by zhaixf 加入属地判断
		UserRes userRes = (UserRes) request.getSession()
				.getAttribute("curUser");
		String cityId = userRes.getCityId();
		//String sql = "select city_id, parent_id from tab_city where city_id = '"
		//		+ cityId + "'";
		//HashMap map = DataSetBean.getMap(sql);
		// ---------

		Cursor cursor = null;

		String searchAction = request.getParameter("searchForUsers");
		// 用户统计的情况
		if ("statUsers".equals(searchAction)) {
			ArrayList list = getEGWUsersServCursor(request);

			return list;
		} else {
			// 用户检索的情况
			ArrayList list = new ArrayList();
			list.clear();
			String username = request.getParameter("username");
			String phonenumber = request.getParameter("phonenumber");
			String flg = request.getParameter("flg");

			StringBuffer sql = new StringBuffer();

			// teledb
			if (DBUtil.GetDB() == DB_MYSQL) {
				sql.append("select a.cust_type_id, a.device_id, a.dealdate, a.user_state, a.username, b.customer_name, " +
						"a.city_id, a.phonenumber, a.device_serialnumber, a.user_id, a.gather_id, a.realname,a.serv_type_id " +
						"from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id");
			}
			else {
				sql.append("select a.*, b.* from tab_egwcustomer a, tab_customerinfo b where a.customer_id=b.customer_id");
			}
			//sql.append(" where 1 = 1 ");
			// m_EGWUsers_List_SQL += " where 1 = 1";

			if (request.getParameter("submit") != null
					&& (username != null || phonenumber != null)) {

				username = username.trim();
				phonenumber = phonenumber.trim();
				if (username.length() != 0) {
					sql.append(" and username = '");
					sql.append(username);
					sql.append("'");
				}

				if (phonenumber.length() != 0) {
					sql.append(" and phonenumber like '%");
					sql.append(phonenumber);
					sql.append("%' ");
				}
			}
			if (flg == null) {
				sql.append(" and user_state in ('1','2') ");
			}

			// -----------zhaixf-----
			Map<String, String> cityMap = CityDAO.getCityIdPidMap();
			if (!"-1".equals(cityMap.get(cityId))) {
				List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
				sql.append(" and city_id in (");
				sql.append(StringUtils.weave(list1));
				sql.append(")");
				list1 = null;
				
			}
			cityMap = null;
			
			logger.debug("-----------m_EGWUsers_List_SQL----" + sql.toString());
			
			// -----------------
			PrepareSQL psql = new PrepareSQL(sql.toString());
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql.toString());
			list.add(cursor);

			return list;
		}

	}

	/**
	 * 统计是否开通某业务的EGW用户列表
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getEGWUsersServCursor(HttpServletRequest request) {
		String strSQL = "";
		String rtnMessage = "";
		String optinalMessage = "";
		Cursor cursor = null;

		String service_id = request.getParameter("some_service");
		String service_status = request.getParameter("radioBtn");
		String dev_id = request.getParameter("dev_id");
		String cpeCurrentStatus = request.getParameter("CPE_CurrentStatus");
		String user_domain = request.getParameter("user_domain");
		String dev_ip = request.getParameter("dev_ip");
		String vendorName = request.getParameter("vendorName");
		String devModelName = request.getParameter("devModelName");
		String osVersionName = request.getParameter("osVersionName");
		ArrayList list = new ArrayList();
		list.clear();

		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		// HttpSession session = request.getSession();
		// UserRes curUser = (UserRes) session.getAttribute("curUser");

		if (dev_id != null) {
			m_serv_users_List_SQL += " and a.device_id = '" + dev_id + "'";
			optinalMessage += ",设备ID为\"" + dev_id + "\"";
		}
		if (vendorName != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where oui='"
					+ vendorName + "'" +" and gw_type=2 "+")";
			optinalMessage += ",设备厂商为\"" + vendorName + "\"";
		}
		if (devModelName != null) {
			// m_serv_users_List_SQL +=
			// " and a.device_serialnumber in (select device_serialnumber from
			// tab_gw_device where devicetype_id in (select devicetype_id from
			// tab_devicetype_info where device_model='"
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where gw_type=2 and  device_model_id in (select device_model_id from gw_device_model where device_model='"
					+ devModelName + "'))";
			optinalMessage += ",设备类型为\"" + devModelName + "\"";
		}
		if (osVersionName != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where gw_type=2 and devicetype_id in (select devicetype_id from tab_devicetype_info where softwareversion='"
					+ osVersionName + "'))";
			optinalMessage += ",软件版本为\"" + osVersionName + "\"";
		}
		if (cpeCurrentStatus != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where gw_type=2 and  cpe_currentstatus="
					+ cpeCurrentStatus + ")";
			if ("1".equals(cpeCurrentStatus)) {
				optinalMessage += ",在线的";
			} else {
				optinalMessage += ",不在线的";
			}

		}
		if (user_domain != null) {
			m_serv_users_List_SQL += " and a.device_id in (select res_id from tab_gw_res_area where res_type=1 and area_id="
					+ user_domain.split("\\|")[0] + ")";
			optinalMessage += ",管理域为\"" + user_domain.split("\\|")[1] + "\"";
		}
		if (dev_ip != null) {
			m_serv_users_List_SQL += " and a.device_serialnumber in (select device_serialnumber from tab_gw_device where gw_type=2 and loopback_ip='"
					+ dev_ip + "')";
			optinalMessage += ",设备IP为\"" + dev_ip + "\"";
		}

		pSQL.setSQL(m_serv_users_List_SQL);
		pSQL.setInt(1, Integer.parseInt(service_id.split("\\|")[0]));
		pSQL.setString(2, service_status);
		if (Integer.parseInt(service_status) == 0) {
			rtnMessage = "未开通";
		} else {
			rtnMessage = "已开通";
		}
		rtnMessage += "\"" + service_id.split("\\|")[1] + "\"业务";
		rtnMessage += optinalMessage;
		strSQL = pSQL.getSQL();
		
		QueryPage qryp = new QueryPage();
		qryp.initPage(strSQL, offset, pagelen);
		String strBar = qryp.getPageBar();
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(strSQL, offset, pagelen);
		list.add(cursor);
		list.add(rtnMessage);
		return list;
	}

	/**
	 * 得到所选用户关联的基本信息
	 * 
	 * 
	 * @return
	 */
	public Cursor getUserRelatedBaseInfo(HttpServletRequest request) {
		Cursor cursor = null;
		String user_id = request.getParameter("user_id");
		String relatedBaseInfo_SQL = "select * "
				+ "from tab_egwcustomer where user_id=" + user_id;
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			relatedBaseInfo_SQL = "select ipaddress, ipmask, gateway, adsl_ser, workid, bandwidth, opendate, wan_type, " +
					"work_model, access_style_id, vpiid, vciid, vlanid, customer_id, username, user_state, lan_num, " +
					"ssid_num, upwidth, maxattdnrate, oui, device_serialnumber, device_id, binddate, userline, is_chk_bind "
					+ "from tab_egwcustomer where user_id=" + user_id;
		}
		PrepareSQL psql = new PrepareSQL(relatedBaseInfo_SQL);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(relatedBaseInfo_SQL);

		return cursor;
	}

	/**
	 * 查询该用户关联的终端信息
	 * 
	 */
	public Cursor getCustDeviceType(String user_id) {

		Cursor cursor = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select a.type_name ");
		sql.append("from gw_bss_dev_type a,gw_cust_user_dev_type b");
		sql.append(" where b.user_id=");
		sql.append(user_id);
		sql.append(" and a.type_id=b.type_id ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql.toString());

		return cursor;
	}
	/**
	 * 查询该用户关联的套餐信息
	 * 
	 */
	public Cursor getCustPackag(String user_id) {

		Cursor cursor = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select c.serv_package_name ");
		sql.append("from gw_serv_package c,gw_cust_user_package d where d.user_id=");
		sql.append(user_id);
		sql.append(" and c.serv_package_id=d.serv_package_id");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql.toString());

		return cursor;
	}

	/**
	 * 获得当前用户已开通业务
	 * 
	 * 
	 * @return
	 */
	public List getUserCurRelatedSerInfo(String userId) {

		Cursor cor = null;
		List retnList = new ArrayList();
		String servSql = "select username from tab_egwcustomer where user_id = "
				+ userId;
		PrepareSQL psql = new PrepareSQL(servSql);
    	psql.getSQL();
		Map map = DataSetBean.getRecord(servSql);
		if (map != null) {
			servSql = "select serv_type_id from tab_gw_user_serv where username = '"
					+ map.get("username") + "'";
			psql = new PrepareSQL(servSql);
	    	psql.getSQL();
			cor = DataSetBean.getCursor(servSql);
			if ((map = cor.getNext()) != null) {
				retnList.add(Global.Serv_type_Map.get(map.get("serv_type_id")));
			}
		}
		return retnList;
	}

	/**
	 * 得到所选用户可以使用的业务
	 * 
	 * 
	 * @return
	 */
	public List getServiceInfo(String user_id) {
		Cursor cursor = null;
		List retnList = new ArrayList();
		// get the user's serv_type_id
		String serInfoSQL = "select serv_type_id from tab_egwcustomer "
				+ "where user_id = " + user_id;
		PrepareSQL psql = new PrepareSQL(serInfoSQL);
    	psql.getSQL();
		Map map = DataSetBean.getRecord(serInfoSQL);
		if (map != null) {
			String servType = (String) map.get("serv_type_id");
			serInfoSQL = "select serv_type_id from gw_usertype_servtype where user_type = "
					+ servType;
			psql = new PrepareSQL(serInfoSQL);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(serInfoSQL);
			while ((map = cursor.getNext()) != null) {
				retnList.add(Global.Serv_type_Map.get(map.get("serv_type_id")));
			}
		}
		return retnList;
	}

	/**
	 * 获取当前用户所开通的所有业务
	 * 
	 * @return
	 */
	public Cursor getEgwServInfo(String user_id) {
		Cursor cursor = null;
		
		StringBuffer serInfoSQL = new StringBuffer();
		
		serInfoSQL.append(" select a.serv_type_id,a.serv_type_name,a.type,b.username, ");
		serInfoSQL.append(" b.serv_status,b.passwd,b.wan_type,b.vpiid,b.vciid,b.vlanid, ");
		serInfoSQL.append(" b.ipaddress,b.ipmask,b.gateway,b.adsl_ser,b.bind_port, ");
		serInfoSQL.append(" b.wan_value_1,b.wan_value_2,b.open_status,b.dealdate,b.opendate, ");
		serInfoSQL.append(" b.pausedate,b.closedate,b.updatetime from tab_gw_serv_type a, ");
		serInfoSQL.append(" egwcust_serv_info b where a.serv_type_id=b.serv_type_id ");
		serInfoSQL.append(" and b.serv_status in (1,2) and b.user_id= " );
		serInfoSQL.append(user_id );
		
		PrepareSQL psql = new PrepareSQL(serInfoSQL.toString());
    	psql.getSQL();
		cursor = DataSetBean.getCursor(serInfoSQL.toString());
		
		return cursor;
	}
	
	/**
	 * 查询是否存在该设备的业务
	 * 
	 * @param oui
	 * @param deviceSerialnumber
	 * @param service_id
	 * @return
	 */
	private int checkDevice(String oui, String device_serialnumber,
			String service_id) {
		int count = 0;

		String sql = "select count(*) as total from tab_egwcustomer where oui='"
				+ oui
				+ "' and device_serialnumber='"
				+ device_serialnumber
				+ "' and service_id=" + service_id;
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			String tmp = (String) fields.get("total");
			if (tmp != null && !"".equals(tmp)) {
				count = Integer.parseInt(tmp);
			}
		}

		return count;
	}

	/**
	 * 查询对应客户的业务用户
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getEGWUsersList(HttpServletRequest request) {

		// 初始化
		String sql = "select * from tab_egwcustomer where user_state != '0' ";
		// teledb
		if (DBUtil.GetDB() == DB_MYSQL) {
			sql = "select cust_type_id, device_serialnumber, user_type_id, dealdate, user_state, username, phonenumber, " +
					"serv_type_id, user_id, gather_id, realname " +
					"from tab_egwcustomer where user_state != '0' ";
		}
		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		String customer_id = request.getParameter("customer_id");

		// 过滤条件
		String filterStr = "";
		if (customer_id != null && !"".equals(customer_id.trim())) {
			sql += " and customer_id='" + customer_id + "'";
			filterStr += "&customer_id=" + customer_id;
		}

		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();

		// 分页查询
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		QueryPage qryp = new QueryPage();
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar(filterStr);
		list.add(strBar);
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		list.add(cursor);

		return list;

	}

	/**
	 * 根据客户id查询设备信息
	 * 
	 * @param e_id
	 * @return
	 */
	private String[] getDevByCustomer(String e_id) {
		// 初始化
		String[] dev = new String[2];
		dev[0] = "";
		dev[1] = "";

		String sql = "select oui,device_serialnumber from tab_customerinfo where customer_id = '"
				+ e_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			String oui = (String) fields.get("oui");
			String device_serialnumber = (String) fields
					.get("device_serialnumber");
			if (oui != null && !"".equals(oui)) {
				dev[0] = oui;
			}
			if (device_serialnumber != null && !"".equals(device_serialnumber)) {
				dev[1] = device_serialnumber;
			}
		}

		return dev;
	}

	/**
	 * 根据设备信息查询对应的客户id
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @return
	 */
	private String getCustomerID(String oui, String device_serialnumber) {
		String sql = "select customer_id from tab_gw_device where oui='" + oui
				+ "' and device_serialnumber = '" + device_serialnumber + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			return (String) fields.get("customer_id");
		} else {
			return "";
		}
	}

	/**
	 * 查询客户名称
	 * 
	 * @param e_id
	 * @return
	 */
	public String getCustomerName(String e_id) {
		String sql = "select customer_name from tab_customerinfo where customer_id = '"
				+ e_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null) {
			return (String) fields.get("customer_name");
		} else {
			return "";
		}
	}

	/**
	 * 判断该设备是否已经开通该业务
	 * 
	 * @author johnson
	 * @date 2008-7-11
	 * 
	 * @return boolean
	 */
	public static boolean isOpenServ(String device_serialnumber, String oui,
			String servType) {
		if (null == servType) {
			logger.warn("---isOpenServ--exception--业务为空");
			return false;
		}
		if (null != device_serialnumber && !"".equals(device_serialnumber)) {
			String strSql = "select serv_type_id from tab_egwcustomer where device_serialnumber='"
					+ device_serialnumber
					+ "' and oui='"
					+ oui
					+ "' and (user_state='1' or user_state='2')";
			PrepareSQL psql = new PrepareSQL(strSql);
	    	psql.getSQL();
			Cursor cursor = DataSetBean.getCursor(strSql);
			if (null != cursor && cursor.getRecordSize() > 0) {
				Map map = new HashMap();
				while ((map = cursor.getNext()) != null) {
					if (!map.isEmpty()) {
						if (servType.equals(map.get("serv_type_id"))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 查询对应客户的业务用户
	 * 
	 * @return ArrayList（strBar,cursor）
	 */
	public ArrayList getStateUsers(HttpServletRequest request) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.user_id,a.username,a.passwd,a.adsl_ser,a.bandwidth,a.ipaddress,");
		sql.append("a.ipmask,a.gateway,a.vlanid,a.workid,a.user_state,a.opendate,a.onlinedate,");
		sql.append("a.pausedate,a.closedate,a.updatetime,a.binddate,a.staff_id,a.remark,");
		sql.append("a.phonenumber,a.vpiid,a.vciid,a.adsl_hl,a.userline,a.dealdate,");
		sql.append("a.opmode,a.maxattdnrate,a.upwidth,a.oui,a.device_serialnumber,a.device_id,");
		sql.append("a.serv_type_id,a.max_user_number,a.wan_value_1,a.wan_value_2,a.open_status,");
		sql.append("a.customer_id,a.wan_type,a.lan_num,a.ssid_num,a.work_model,a.access_style_id,");
		sql.append("a.device_ip,a.device_shelf,a.device_frame,a.device_slot,a.device_port,");
		sql.append("a.bind_port,a.flag_pvc,a.stat_bind_enab from tab_egwcustomer a,");
		sql.append("tab_customerinfo b where a.customer_id=b.customer_id ");
		
		String filterStr = "";
		String cityId = request.getParameter("city_id");
		// 用户检索的情况
		ArrayList list = new ArrayList();
		list.clear();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String stroffset = request.getParameter("offset");
		String gw_type = request.getParameter("gw_type");
		String access_type = request.getParameter("accessType");
		filterStr += "&gw_type=" + gw_type;
		
		int pagelen = 30;
		int offset;
		if (null  == stroffset || "null".equals(stroffset))
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);

		sql.append(" and a.user_state in ('1','2')");
		
		//判断是否为查询提交，如果是则 加入查询字段条件
		if (startTime != null && !startTime.equals("")) {

			sql.append(" and a.opendate >= ");
			sql.append(startTime);
			filterStr += "&startTime=" + startTime;
		}
		
		if (endTime != null && !endTime.equals("")) {

			sql.append(" and a.opendate <= ");
			sql.append(endTime);
			filterStr += "&endTime=" + endTime;
		}
		
		if("ADSL".equals(access_type)){
			sql.append(" and a.access_style_id in (1,6)");
			filterStr += "&accessType=ADSL";
		}else if ("LAN".equals(access_type)){
			sql.append(" and a.access_style_id in (2,4)");
			filterStr += "&accessType=LAN";
		}else if ("EPON".equals(access_type)){
			sql.append(" and a.access_style_id in (3,5)");
			filterStr += "&accessType=EPON";
		}
		
		if (cityId == null || "".equals(cityId)) {
			cityId = "00";
		}
		
		if(!"00".equals(cityId)){
			List list1 = CityDAO.getAllNextCityIdsByCityPid(cityId);
			String allCityIds = StringUtils.weave(list1);
			sql.append(" and city_id in (");
			sql.append(allCityIds);
			sql.append(")");
			list1 = null;
		}
		filterStr += "&city_id=" + cityId;
		
		
		logger.debug("统计设备/用户，用户列表:" + sql.toString());
		QueryPage qryp = new QueryPage();
		qryp.initPage(sql.toString(), offset, pagelen);
		String strBar = qryp.getPageBar(filterStr);
		list.add(strBar);
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql.toString(), offset, pagelen);
		list.add(cursor);
		
		return list;

	}
}
