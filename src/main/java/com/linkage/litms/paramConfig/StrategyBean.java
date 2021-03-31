package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;

public class StrategyBean {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(StrategyBean.class);
	private PrepareSQL pSQL;
	
	public StrategyBean() {
		pSQL = new PrepareSQL();
	}
	/**
	 * 根据用户宽带账号过滤设备
	 * 
	 * @param request
	 * @param needFilter
	 * @return
	 */
	public String getDev_user_HTML(HttpServletRequest request, boolean needFilter) {

		String hguser = request.getParameter("hguser");
		// String serialnumber = request.getParameter("serialnumber");
		String isOpened_userValue = request.getParameter("isOpened_userValue");
		String gw_type = request.getParameter("gw_type");
		int num = 1;
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		long area_id = user.getAreaId();
		String city_id = user.getCityId();
		
		StringBuffer sqlBuff = new StringBuffer();
		
		sqlBuff.append("select a.user_id,a.username,a.oui,a.device_serialnumber from tab_hgwcustomer a ")
		   .append(" left join hgwcust_serv_info b on a.user_id=b.user_id where a.username = '").append(hguser)
		   .append("' and a.user_state in ('1','2') ");
		
		if (LipossGlobals.isOracle()) {
			sqlBuff.append("and a.device_serialnumber is not null ");
		}else {
			sqlBuff.append("and a.device_serialnumber is not null and a.device_serialnumber != ''");
		}
	
		if (!user.isAdmin()) {
			sqlBuff.append(" and a.city_id in (" ).append(getAllSubCityIds(city_id, true)).append(")") ;
		}
		

//		if (null != gw_type && !"".equals(gw_type)) {
//			tmpSql += " and gw_type = " + gw_type;
//		}

		

//		if (!user.isAdmin()) {
//			// tmpSql += " and b.res_type=1 and b.area_id=" + area_id + " ";
//		}
//		// 按用户查询方式
//		if (null != hguser && !"".equals(hguser)) {
//			tmpSql += " and username like '%" + hguser + "%' ";
//		}
		
		if ("0".equals(isOpened_userValue)) {
			// 显示全部的ADSL已开户的用户
			//tmpSql += " group by username having count(1) = 1";
		} else if ("1".equals(isOpened_userValue)) {
			// 只显示未开户的用户和设备信息(IPTV业务)
			sqlBuff.append(" and b.open_status is null");
		} else if ("2".equals(isOpened_userValue)) {
			// 只显示已开户但失败的用户和设备信息
			sqlBuff.append(" and (b.open_status=-1 or b.open_status=0)");
		} else if ("3".equals(isOpened_userValue)) {
			// 只显示已开户并成功的用户和设备信息
			sqlBuff.append(" and b.open_status=1");
		}

		// if (serialnumber != null && !"".equals(serialnumber)) {
		// tmpSql += " and a.device_serialnumber like '%" + serialnumber
		// + "%'";
		// }
		// tmpSql += " order by a.oui,a.device_serialnumber";

		PrepareSQL psql = new PrepareSQL(sqlBuff.toString());
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sqlBuff.toString());
		Map fields = cursor.getNext();
		String serviceHtml = "";
		serviceHtml += "<TABLE class='data_table'>";
		serviceHtml += "<TR>";
		serviceHtml += "<TH>&nbsp;选择&nbsp;</TH>";
		serviceHtml += "<TH>&nbsp;序号&nbsp;</TH>";
		serviceHtml += "<TH>&nbsp;宽带账号&nbsp;</TH>";
		serviceHtml += "<TH>&nbsp;厂商号&nbsp;</TH>";
		serviceHtml += "<TH>&nbsp;设备序列号&nbsp;</TH>";
		serviceHtml += "</TR>";

		if (fields == null) {
			serviceHtml += "<TR><TD colspan='10'></TD></TR><TR><TD colspan='10'>没有符合条件的设备！</TD></TR>";
		} else {
			while (fields != null) {
				serviceHtml += "<TR>";
				//logger.debug("GSJ----------1:" + isOpened_userValue);
				//logger.debug("GSJ----------2:" + checkIsRunning((String) fields.get("username")));
				if ("2".equals(isOpened_userValue) && checkIsRunning((String) fields.get("username"))) {
					serviceHtml += "<TD align='center'><input type='checkbox' id='device_id' name='device_id' value='" 
						+ fields.get("oui")+"|"+fields.get("device_serialnumber")+"|"
						+ fields.get("username")+"|"+ fields.get("user_id") + "' disabled></TD>";
				} else {
					serviceHtml += "<TD align='center'><input type='checkbox' id='device_id' name='device_id' value='" 
						+ fields.get("oui")+"|"+fields.get("device_serialnumber")+"|"
						+ fields.get("username")+"|"+ fields.get("user_id") + "'></TD>";
				}
				serviceHtml += "<TD align='center'>" + num++ + "</TD>";
				serviceHtml += "<TD align='center'>" + (String) fields.get("username") + "</TD>";
				serviceHtml += "<TD align='center'>" + (String) fields.get("oui") + "</TD>" + "<TD>"
						+ (String) fields.get("device_serialnumber") + "</TD>";
				serviceHtml += "</TR>";
				fields = cursor.getNext();
			}
		}
		serviceHtml += "</TABLE>";
		return serviceHtml;
	}

	
	/**
	 * 把页面上配置的信息入库
	 * 
	 * @param request
	 * @return
	 */
	public String configiTV_SAVE(HttpServletRequest request) {
		String strSQL = "";
		ArrayList<String> allSQLList = new ArrayList<String>();
		String strMsg = "";
		String str_id = "";
		String sheet_para = "";
		String user_id="";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		StringBuffer sbIds = new StringBuffer();
		
		String insert_gw_serv_strategy = "insert into gw_serv_strategy(id,status,acc_oid," +
				"time,type,gather_id,device_id,oui,device_serialnumber,username,sheet_para,service_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String update_gw_serv_strategy = "update gw_serv_strategy set status=?, time=?, sheet_para=?,type=?,result_id=0,result_desc='',start_time=null,end_time=null where username=?";
		
		String insert_hgwcustomer_serv_info = "insert into hgwcust_serv_info(user_id," +
				"serv_type_id,username,serv_status,passwd,wan_type,vpiid,vciid,bind_port,open_status,dealdate,updatetime)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String update_hgwcustomer_serv_info = "update hgwcust_serv_info set open_status=?,updatetime=? where user_id=?";
		
		String acc_oid = "";//request.getParameter("acc_oid");
		String acc_loginname = user.getAccount();
		String type = request.getParameter("strategy_type");
		String gather_id = ""; //request.getParameter("gather_id");
		String device_id = ""; //request.getParameter("device_id");
		String dev_gat_id = "";
		String oui = ""; //request.getParameter("oui");
		String device_serialnumber = ""; //request.getParameter("device_serialnumber");
		String username = ""; //request.getParameter("username");
		String ssid2 = "";//request.getParameter("ssid2");
		String serv_type_id = request.getParameter("serv_type_id");
		String service_id = request.getParameter("service_id");
		String bind_port = request.getParameter("bind_port");
		String vpi = request.getParameter("vpiid");
		String vci = request.getParameter("vciid");
		String pvc = "PVC:" + vpi + "/" + vci;
		
		//认证方式
		String Au_way = request.getParameter("Au_way");
		//基本认证模式(WEP和WPA是一样的)
		String Au_basic = "";
		if ("None".equals(Au_way)) {
			Au_basic = "OpenSystem";
		} else {
			Au_basic = "Both";
		}
		//WEP模式密钥
		String WEP_pw = request.getParameter("WEP_pw");
		//WPA模式密钥
		String WPA_pw = request.getParameter("WPA_pw");
		
		Map<String, String> accMap = getAccMap();
		acc_oid = accMap.get(acc_loginname);
		logger.debug("Loginname:" + acc_loginname);
		logger.debug("acc_oid:" + acc_oid);
		
		Date currentDate = new Date();
	    // 设备最近更新时间 获取当前时间
	    long l_curupdatetime = currentDate.getTime() / 1000;
	    currentDate = null;
	    String[] deviceInfo = request.getParameterValues("device_id");
	    //String[] device_id_multi = new String[deviceInfo.length];
	    
	    long id = StrategyOBJ.createStrategyId();
	    
	    for (int i=0; i<deviceInfo.length; i++) {
	    	oui = deviceInfo[i].split("\\|")[0];
	    	device_serialnumber = deviceInfo[i].split("\\|")[1];
	    	dev_gat_id = LipossGlobals.ALL_DEV_MAP.get(device_serialnumber);
	    	
	    	//logger.debug("dev_gat_id-----------:" + dev_gat_id);
	    	
	    	if (dev_gat_id != null) {
	    		device_id = dev_gat_id.split(",")[0];
	    		//device_id_multi[i] = device_id;
	    		
	    		//logger.debug("device_id_multi-----------:" + device_id);
	    		
	    		gather_id = dev_gat_id.split(",")[1];
	    	}
	    	username = deviceInfo[i].split("\\|")[2];
	    	user_id = deviceInfo[i].split("\\|")[3];
	    	
	    	ssid2 = "iTV"+device_serialnumber.substring(device_serialnumber.length() - 5, device_serialnumber.length());
	    	//sheet_para = vpi + "|||" + vci + "|||" + ssid2 + "|||" + ssid2_pw + "|||" + ssid2_encrypt;
	    	sheet_para = Au_way + "|||" + Au_basic + "|||" + WEP_pw + "|||" + WPA_pw + "|||" + ssid2 + "|||" + pvc + "|||" + bind_port;
	    	
	    	//logger.debug("GSJ-----------:" + sheet_para);
	    	// 取得新的userid
	    	String idTemp = checkUsername(username);
			if (idTemp != null) {
				//如果用户已在策略表里存在，则只需更新即可
				pSQL.setSQL(update_gw_serv_strategy);
				pSQL.setInt(1, 0);
				pSQL.setLong(2, l_curupdatetime);
				pSQL.setString(3, sheet_para);
				pSQL.setInt(4, Integer.parseInt(type));
				pSQL.setString(5, username);
				strSQL = pSQL.getSQL();
				allSQLList.add(strSQL);
				sbIds.append(idTemp + ",");
				
				pSQL.setSQL(update_hgwcustomer_serv_info);
				pSQL.setInt(1, 0);
				pSQL.setLong(2, l_curupdatetime);
				pSQL.setInt(3, Integer.parseInt(user_id));
				strSQL = pSQL.getSQL();
				allSQLList.add(strSQL);
				
			} else {
				//如果用户在策略表里不存在
				pSQL.setSQL(insert_gw_serv_strategy);
				pSQL.setLong(1, id);
				pSQL.setInt(2, 0);
				pSQL.setInt(3, Integer.parseInt(acc_oid));
				pSQL.setLong(4, l_curupdatetime);
				pSQL.setInt(5, Integer.parseInt(type));
				pSQL.setString(6, gather_id);
				pSQL.setString(7, device_id);
				pSQL.setString(8, oui);
				pSQL.setString(9, device_serialnumber);
				pSQL.setString(10, username);
				pSQL.setString(11, sheet_para);
				pSQL.setInt(12, Integer.parseInt(service_id));
				strSQL = pSQL.getSQL();
				strSQL = strSQL.replaceAll("',,'", "null");
				strSQL = strSQL.replaceAll(",\\)", ",null)");
				allSQLList.add(strSQL);
				sbIds.append(id + ",");
				
				//插入家庭网关客户业务信息表hgwcust_serv_info
				pSQL.setSQL(insert_hgwcustomer_serv_info);
				pSQL.setInt(1, Integer.parseInt(user_id));
				pSQL.setInt(2, Integer.parseInt(serv_type_id));
				pSQL.setString(3, username);
				pSQL.setInt(4, 1);
				//TODO 密码暂时入为空
				pSQL.setString(5, "");
				//1:PPPoE(桥接) 2:PPPoE(路由) 3:STATIC 4:DHCP
				pSQL.setInt(6, 1);
				pSQL.setString(7, vpi);
				pSQL.setInt(8, Integer.parseInt(vci));
				pSQL.setString(9, bind_port);
				//开通状态 0：未做	1：成功 -1:失败
				pSQL.setInt(10,0);
				pSQL.setLong(11, l_curupdatetime);
				pSQL.setLong(12, l_curupdatetime);
				strSQL = pSQL.getSQL();
				allSQLList.add(strSQL);
			}
			id = id + 1;
	    }
		
		if (allSQLList.size() > 0 && "0".equals(type)) {
			int[] rCode = DataSetBean.doBatch(allSQLList);
			if (rCode != null && rCode.length > 0) {
				//调用预读模块，实时调用后台
				invokePreProcess(sbIds.toString().split(","));
				strMsg = "已调用后台进行处理，并通知后台入库，请查询是否订制成功！";
			} else {
				strMsg = "入库失败！";
			}
			//LipossGlobals.ALL_SQL_IPTV = allSQLList;
			
		} else if (allSQLList.size() > 0 && !"0".equals(type)) {
			LipossGlobals.ALL_SQL_IPTV = allSQLList;
			strMsg = "已通知后台入库，请查询是否订制成功！";
		} else {
			strMsg = "入库失败！";
		}
		return strMsg;
	}
	
	
	/**
	 * 查询
	 * @param request
	 * @return
	 */
	public List searchStrategyInfo(HttpServletRequest request) {

		String start_day = request.getParameter("start_day");
		String start_time = request.getParameter("start_time");
		String end_day = request.getParameter("end_day");
		String end_time = request.getParameter("end_time");
		String acc_loginname = request.getParameter("acc_loginname");
		String username = request.getParameter("username");
		String startTime_int = request.getParameter("startTime_int");
		String endTime_int = request.getParameter("endTime_int");
		String id = request.getParameter("id");
		
		String linkparam = "";

		long startTime = 0;
		long endTime = 0;
		if (null != start_day && null != start_time && null != end_day
			&& null != end_time) {
		    String startdateStr = start_day + " " + start_time;
		    String enddateStr = end_day + " " + end_time;
		    startTime = new DateTimeUtil(startdateStr).getLongTime();
		    endTime = new DateTimeUtil(enddateStr).getLongTime();
		}

		if (null != startTime_int && null != endTime_int) {
		    startTime = Long.parseLong(startTime_int);
		    endTime = Long.parseLong(endTime_int);
		}

		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		String tmpSql = "select * from gw_serv_strategy where 1=1";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select device_serialnumber, status, time, start_time, end_time, result_id, username, oui " +
					"from gw_serv_strategy where 1=1";
		}

		if (id != null && !"".equals(id)) {
		    tmpSql += " and id = " + id;
		    linkparam += "&id=" + id;
		}
		
		if (startTime != 0 && !"".equals(startTime)) {
		    tmpSql += " and time >= " + startTime;
		    linkparam += "&startTime_int=" + startTime;
		}
		if (endTime != 0 && !"".equals(endTime)) {
		    tmpSql += " and time <=" + endTime;
		    linkparam += "&endTime_int=" + endTime;
		}
		if (acc_loginname != null && !"".equals(acc_loginname)) {
		    tmpSql += " and acc_oid = (select acc_oid from tab_accounts where acc_loginname='"+acc_loginname+"') ";
		    linkparam += "&acc_oid=(select acc_oid from tab_accounts where acc_loginname='"+acc_loginname+"') ";
		}
		
		if(username != null && !username.isEmpty()){
		    tmpSql += " and username=" + username;
		    linkparam += "&username=" + username;
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();

		ArrayList list = new ArrayList();
		list.clear();
		String stroffset = request.getParameter("offset");
		int pagelen = 50;
		int offset;
		if (stroffset == null)
		    offset = 1;
		else
		    offset = Integer.parseInt(stroffset);
		QueryPage qryp = new QueryPage();
		qryp.initPage(tmpSql, offset, pagelen);
		Cursor cursor = DataSetBean.getCursor(tmpSql, offset, pagelen);
		String strBar = qryp.getPageBar(linkparam);
		list.add(strBar);
		list.add(cursor);
		return list;
	    }
	
	/**
	 * 策略表详细信息
	 * @param request
	 * @return
	 */
	public Map detailStrategyInfo(HttpServletRequest request) {

		String username = request.getParameter("username");

		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		String tmpSql = "select * from gw_serv_strategy where username='" + username+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select device_serialnumber, status, time, start_time, end_time, result_id, username, oui, result_desc, type " +
					"from gw_serv_strategy where username='" + username+"'";
		}
		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();

		Map map = DataSetBean.getRecord(tmpSql);
		return map;
	    }
	
	/**
	 * 客户业务详细信息
	 * @param request
	 * @return
	 */
	public Map detailCustomerInfo(HttpServletRequest request) {

		String username = request.getParameter("username");

		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();

		String tmpSql = "select * from hgwcust_serv_info where username='" + username+"'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			tmpSql = "select vpiid, vciid, open_status from hgwcust_serv_info where username='" + username+"'";
		}

		PrepareSQL psql = new PrepareSQL(tmpSql);
    	psql.getSQL();

		Map map = DataSetBean.getRecord(tmpSql);
		return map;
	    }
	
	/**
	 * 每次载入页面时，判断数据库中的数据是否和LipossGlobals中ALL_DEV_MAP的数据是否一致
	 * 如不一致，则载入，否则什么都不做
	 */
	public void dealDevMap() {
		String countDevSQL = "select count(1) num from tab_gw_device";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			countDevSQL = "select count(*) num from tab_gw_device";
		}
		String devInfoSQL = "select device_serialnumber, device_id, gather_id from tab_gw_device";
		String device_serialnumber = "";
		String device_id = "";
		String gather_id = "";
		PrepareSQL psql = new PrepareSQL(countDevSQL);
    	psql.getSQL();
		Map<String, String> countMap = DataSetBean.getRecord(countDevSQL);
		String number = countMap.get("num");
		logger.debug("NUMBER-----:" + number);
		logger.debug("LipossGlobals.ALL_DEV_MAP.size()-----:" + LipossGlobals.ALL_DEV_MAP.size());
		
		if (number == null || LipossGlobals.ALL_DEV_MAP.size() != Integer.parseInt(number)) {
			LipossGlobals.ALL_DEV_MAP.clear();
			PrepareSQL psql1 = new PrepareSQL(devInfoSQL);
	    	psql1.getSQL();
			Cursor cursor = DataSetBean.getCursor(devInfoSQL);
			Map fields = cursor.getNext();
			while(fields != null) {
				device_serialnumber = (String)fields.get("device_serialnumber");
				device_id = (String)fields.get("device_id");
				gather_id = (String)fields.get("gather_id");
				
				LipossGlobals.ALL_DEV_MAP.put(device_serialnumber, device_id + "," + gather_id);
				fields = cursor.getNext();
			}
			logger.debug("LipossGlobals.ALL_DEV_MAP.size()-ALL----:" + LipossGlobals.ALL_DEV_MAP.size());
		}
	}
	
	/**
	 * 开始调用预读模块
	 * @param invokeStruct
	 */
	private void invokePreProcess(String[] idArr) {
		if(null != idArr){
			CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS).processOOBatch(idArr);
		}
	}
	
	/**
	 * 通过device_id数组和service_id数组，得到一个PreProcess.OneToOne[]
	 * @param device_id
	 * @param service_id
	 * @return
	 */
//	private PreProcess.OneToOne[] getOTOStruct(String[] device_id, String service_id) {
//		PreProcess.OneToOne[] oto = new PreProcess.OneToOne[device_id.length];
//		for (int i = 0; i < oto.length; i++) {
//			oto[i] = new PreProcess.OneToOne();
//			oto[i].deviceId = device_id[i];
//			oto[i].serviceId = service_id;
//			
//			logger.debug("oto[i].deviceId:" + oto[i].deviceId);
//			logger.debug("oto[i].serviceId:" + oto[i].serviceId);
//		}
//		return oto;
//	}
	/**
	 * 获得预读模块的IOR
	 * @return
	 */
	private String getPreProcessIOR() {
		String ior = null;
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		PrepareSQL psql = new PrepareSQL(iorSQL);
    	psql.getSQL();
		Map<String, String> iorMap = DataSetBean.getRecord(iorSQL);
		ior = iorMap.get("ior");
		return ior;
	}
	/**
	 * 检查该用户是否已执行过IPTV策略配置了
	 * @return
	 */
	public String checkUsername(String username) {
		String checkUserSQL = "select id from gw_serv_strategy where username='" + username + "'";
		PrepareSQL psql = new PrepareSQL(checkUserSQL);
    	psql.getSQL();
		Map<String, String> checkUserMap = DataSetBean.getRecord(checkUserSQL);
		if (null == checkUserMap || null == checkUserMap.get("id") || "".equals(checkUserMap.get("id"))) {
			return null;
		} else {
			return checkUserMap.get("id");
		}
	}
	
	/**
	 * 检查后台是否正在操作设备
	 * @param username
	 * @return true 正在操作
	 * 		   false 空闲
	 */
	private boolean checkIsRunning(String username) {
		String chkSQL = "select start_time, end_time from gw_serv_strategy where username='" + username + "'";
		PrepareSQL psql = new PrepareSQL(chkSQL);
    	psql.getSQL();
		Map record = DataSetBean.getRecord(chkSQL);
		logger.debug("record.get1:" + record.get("start_time"));
		logger.debug("record.get2:" + record.get("end_time"));
		
		if (record != null && record.get("start_time") != null && !"".equals(record.get("start_time")) && (record.get("end_time") == null || "".equals(record.get("end_time")))) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 获取所有的登录用户信息
	 * @return
	 */
	public Map<String, String> getAccMap() {
		String accSQL = "select acc_loginname, acc_oid from tab_accounts";
		PrepareSQL psql = new PrepareSQL(accSQL);
    	psql.getSQL();
		Map<String, String> accMap = DataSetBean.getMap(accSQL);
		return accMap;
	}
	public String getAllSubCityIds(String m_CityPid, boolean self) {
		return StringUtils.weave(getAllSubCityIdsWithArray(m_CityPid, self));
	}

	/**
	 * 根据属地ID自身获得下属所有属地ID
	 * 
	 * @param m_CityPid
	 * @param self
	 * @return
	 */
	public ArrayList<String> getAllSubCityIdsWithArray(String m_CityPid, boolean self) {
		// 定义保存属地以及下属所有属地对象的链表
		ArrayList<String> m_CityPidList = new ArrayList<String>();
		ArrayList<String> m_CityIdList = new ArrayList<String>();
		m_CityPidList.clear();
		m_CityIdList.clear();
		m_CityPidList.add(m_CityPid);
		// 得到所有下属属地对象链表
		m_CityIdList = getCityIdsByCityPid(m_CityPidList, m_CityIdList);
		if (self) {
			m_CityIdList.add(m_CityPid);
		}
		return m_CityIdList;
	}

	/**
	 * 根据父属地ID获得所有下属子属地ID
	 * 
	 * @param Pids
	 * @param TotalPids
	 * @return
	 */
	public ArrayList<String> getCityIdsByCityPid(ArrayList<String> Pids, ArrayList<String> TotalPids) {
		String GetIDByPid_CitySQL = "select city_id,city_name from tab_city where parent_id=?";
		Cursor cursor = null;
		Map field = null;
		ArrayList<String> tempCityIdList = new ArrayList<String>();
		tempCityIdList.clear();

		for (int i = 0; i < Pids.size(); i++) {
			PrepareSQL pSQL = new PrepareSQL(GetIDByPid_CitySQL);
			pSQL.setString(1, (String) Pids.get(i));

			cursor = DataSetBean.getCursor(pSQL.getSQL());
			field = cursor.getNext();

			// 存在子节点 继续迭代
			if (field != null) {
				while (field != null) {
					tempCityIdList.add((String) field.get("city_id"));
					field = cursor.getNext();
				}
				TotalPids = getCityIdsByCityPid(tempCityIdList, TotalPids);
				tempCityIdList.clear();

			} else {// 迭代终止 加入返回的ArrayList
				if (!TotalPids.contains(Pids.get(i))) {
					TotalPids.add(Pids.get(i));
				}
			}
		}
		return TotalPids;
	}

}
