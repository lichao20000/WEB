
package com.linkage.module.gwms.dao.tabquery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.util.DeviceTypeUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class HgwCustDAO extends SuperDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(HgwCustDAO.class);

	private String gw_type;

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	/**
	 * 获取开户状态的用户信息,从用户表 存在返回对象，不存在返回NULL
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public HgwCustObj getUserInfo(String userId,String gw_type)
	{
		logger.debug("getUserInfo({})", userId);
		String tableName = "tab_hgwcustomer";  
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " tab_egwcustomer ";
		} 
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select a.user_id,a.username,a.city_id,a.office_id,a.device_id,");
			psql.append("a.oui,a.device_serialnumber,a.realname,a.linkaddress,a.credno,");
			psql.append("a.dealdate,a.adsl_ser,a.maxattdnrate,a.email,a.gateway,");
			psql.append("a.ipaddress,a.linkman,a.linkphone,a.ipmask,a.max_user_number,");
			psql.append("a.mobile,a.passwd,a.realname,a.phonenumber,a.upwidth,");
			psql.append("a.wan_type,a.zone_id,a.vciid,a.vpiid,a.vlanid,b.type_id from ");
		}else{
			psql.append("select a.*,b.type_id from ");
		}
		psql.append(tableName+" a,gw_cust_user_dev_type b ");
		psql.append("where a.user_id=? and a.user_state='1' and b.user_id=? ");
		psql.setStringExt(1, userId, false);
		psql.setStringExt(2, userId, false);
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		HgwCustObj hgwCustObj = null;
		if (null != rMap && false == rMap.isEmpty())
		{
			hgwCustObj = new HgwCustObj();
			map2Obj(hgwCustObj, rMap);
		}
		return hgwCustObj;
	}

	/**
	 * 获取所有的终端
	 * 
	 * @param
	 * @author
	 * @date
	 * @return list
	 */
	public List getTypeNameList()
	{
		logger.debug("getTypeNameList({})");
		PrepareSQL psql = new PrepareSQL("select type_name,type_id from gw_dev_type");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public HgwCustObj getCustObjByDevId(String deviceId)
	{
		logger.debug("getCustObjByDevId({})", deviceId);
		HgwCustObj hgwCustObj = null;
		
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		String tableName = "tab_egwcustomer";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " tab_hgwcustomer ";
		} 
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select user_id,username,city_id,office_id,device_id,");
			psql.append("oui,device_serialnumber,realname,linkaddress,credno,");
			psql.append("dealdate,adsl_ser,maxattdnrate,email,gateway,");
			psql.append("ipaddress,linkman,linkphone,ipmask,max_user_number,");
			psql.append("mobile,passwd,realname,phonenumber,upwidth,");
			psql.append("wan_type,zone_id,vciid,vpiid,vlanid from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(tableName+" where device_id=? and (user_state='1' or user_state='2')");
		psql.setString(1, deviceId);
		Map rMap = queryForMap(psql.getSQL());
		if (null != rMap && false == rMap.isEmpty())
		{
			hgwCustObj = new HgwCustObj();
			map2Obj(hgwCustObj, rMap);
		}
		return hgwCustObj;
	}
	public void recordServiceDone(Map map)
	{
		long user_oid = (Long) map.get("user_oid");
		String acc_login_ip = (String)map.get("acc_login_ip");
		int operlog_type = (Integer) map.get("operlog_type");
		long oper_time = (Long) map.get("oper_time");
		String oper_object = (String) map.get("oper_object");
		String oper_result = (String) map.get("oper_result");
		String RecordItemLogSql = "insert into tab_oper_log("
				+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_object"
				+ ",operation_result) values(?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(RecordItemLogSql);
		psql.setLong(1, user_oid);
		psql.setString(2, acc_login_ip);
		psql.setInt(3, operlog_type);
		psql.setLong(4, oper_time);
		psql.setString(5, oper_object);
		psql.setString(6, oper_result);
		jt.update(psql.getSQL());
	}

	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public String getUserId(String deviceId)
	{
		logger.debug("getUserId({})", deviceId);
		String userId = null;
		// String strSQL = "select a.user_id from tab_hgwcustomer a, tab_gw_device b "
		// //+ " where a.oui=b.oui and a.device_serialnumber=b.device_serialnumber"
		// + " where a.device_id = b.device_id"
		// + " and b.device_id=?";
		// PrepareSQL psql = new PrepareSQL(strSQL);
		// psql.setString(1, deviceId);
		// List rList = jt.queryForList(psql.getSQL(), String.class);
		// if (null != rList && false == rList.isEmpty()) {
		// userId = String.valueOf(rList.get(0));
		// }
		HgwCustObj hgwCustObj = getCustObjByDevId(deviceId);
		if (null != hgwCustObj)
		{
			userId = hgwCustObj.getUserId();
		}
		return userId;
	}

	/**
	 * 根据用户账号,设备序列号从用户表中获取用户信息,用户存在则返回用户对象(多于一条返回第一条),没有返回null
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public HgwCustObj[] getCustObjArrInfo(String username, String devSn)
	{
		logger.debug("getUserInfo({}, {})", username, devSn);
		HgwCustObj[] hgwCustObjArr = null;
		
		String tableName = "tab_hgwcustomer";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " tab_egwcustomer ";
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select user_id,username,city_id,office_id,device_id,");
			psql.append("oui,device_serialnumber,realname,linkaddress,credno,");
			psql.append("dealdate,adsl_ser,maxattdnrate,email,gateway,");
			psql.append("ipaddress,linkman,linkphone,ipmask,max_user_number,");
			psql.append("mobile,passwd,realname,phonenumber,upwidth,");
			psql.append("wan_type,zone_id,vciid,vpiid,vlanid,type_id from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(tableName+" where (user_state='1' or user_state='2')");
		
		if (false == StringUtil.IsEmpty(username))
		{
			psql.append(" and username='" + username + "'");
		}
		if (false == StringUtil.IsEmpty(devSn))
		{
			psql.append(" and device_serialnumber like '%" + devSn + "'");
		}
		List list = jt.queryForList(psql.getSQL());
		if (null != list && false == list.isEmpty())
		{
			int size = list.size();
			hgwCustObjArr = new HgwCustObj[size];
			for (int i = 0; i < size; i++)
			{
				Map rMap = (Map) list.get(i);
				HgwCustObj hgwCustObj = new HgwCustObj();
				map2Obj(hgwCustObj, rMap);
				hgwCustObjArr[i] = hgwCustObj;
			}
		}
		return hgwCustObjArr;
	}

	/**
	 * 增加用户
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-11
	 * @return String
	 */
	public int saveCust(HgwCustObj hgwObj)
	{
		logger.debug("saveCust({})", hgwObj);
		long nowTime = new Date().getTime() / 1000;
		
		String tableName = "tab_hgwcustomer";
		if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " tab_egwcustomer ";
		}
		
		// 后面的开户工单可以更新前面的开户工单信息,DSLAM的相关信息没有更新
		String strSQL = "insert into "+tableName+" (passwd,phonenumber,bandwidth,maxattdnrate,upwidth,max_user_number,"
				+ " vlanid, vpiid, vciid, city_id, office_id, zone_id, linkman, linkphone, email,"
				+ " linkaddress, updatetime, credno, mobile, cred_type_id, user_state, user_id,"
				+ " dealdate, opendate, wan_type, username, realname, ipaddress, ipmask, gateway, adsl_ser,user_type_id)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		PrepareSQL psqlAdd = new PrepareSQL();
		String strAdd = "insert into gw_cust_user_dev_type(customer_id,user_id,type_id,time) values (?,?,?,?)";
		psqlAdd.setSQL(strAdd);
		psqlAdd.setString(1, hgwObj.getUserId());
		psqlAdd.setInt(2, StringUtil.getIntegerValue(hgwObj.getUserId()));
		psqlAdd.setString(3, hgwObj.getTypeName());
		psqlAdd.setLong(4, System.currentTimeMillis() / 1000);
		jt.update(psqlAdd.getSQL());
		
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(strSQL);
		psql.setString(1, hgwObj.getPasswd());
		psql.setString(2, hgwObj.getTelepone());
		psql.setLong(3, hgwObj.getUpSpeed());
		psql.setLong(4, hgwObj.getDownSpeed());
		psql.setLong(5, hgwObj.getUpSpeed());
		psql.setInt(6, hgwObj.getMaxUserNum());
		psql.setString(7, hgwObj.getVlanid());
		psql.setString(8, hgwObj.getVpiid());
		psql.setInt(9, StringUtil.getIntegerValue(hgwObj.getVciid()));
		psql.setString(10, hgwObj.getCityId());
		psql.setString(11, hgwObj.getOfficeId());
		psql.setString(12, hgwObj.getZoneId());
		psql.setString(13, hgwObj.getLinkman());
		psql.setString(14, hgwObj.getLinkphone());
		psql.setString(15, hgwObj.getEmail());
		psql.setString(16, hgwObj.getAddress());
		psql.setLong(17, nowTime);
		psql.setString(18, hgwObj.getCredno());
		psql.setString(19, hgwObj.getMobile());
		// 身份证
		psql.setInt(20, 1);
		// 设置为开户状态
		psql.setString(21, "1");
		psql.setLong(22, StringUtil.getLongValue(hgwObj.getUserId()));
		psql.setLong(23, hgwObj.getDealdate());
		psql.setLong(24, nowTime);
		psql.setLong(25, hgwObj.getWanType());
		psql.setString(26, hgwObj.getUsername());
		psql.setString(27, hgwObj.getRealname());
		psql.setString(28, hgwObj.getIp());
		psql.setString(29, hgwObj.getMask());
		psql.setString(30, hgwObj.getGateway());
		psql.setString(31, hgwObj.getDns());
		psql.setString(32, "3");
		return jt.update(psql.getSQL());
	}

	/**
	 * 更新用户表数据
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-10
	 * @return String
	 */
	public int updateCust(HgwCustObj hgwObj)
	{
		logger.debug("updateCust({})", hgwObj);
		long nowTime = new Date().getTime() / 1000;
		
		String tableName = "";
		if (Global.GW_TYPE_ITMS.equals(gw_type)) {
			tableName = " tab_hgwcustomer ";
		} else if (Global.GW_TYPE_BBMS.equals(gw_type)) {
			tableName = " tab_egwcustomer ";
		} else {
			tableName = " tab_hgwcustomer ";
		}
		
		// 后面的开户工单可以更新前面的开户工单信息,DSLAM的相关信息没有更新
		String strSQL = "update "+tableName+" set passwd=?, phonenumber=?, bandwidth=?, maxattdnrate=?, upwidth=?, max_user_number=?, "
				+ " vlanid=?, vpiid=?, vciid=?, city_id=?, office_id=?, zone_id=?, linkman=?, linkphone=?, email=?,"
				+ " linkaddress=?, updatetime=?, credno=?, mobile=?, wan_type=?, cred_type_id=1, user_state='1',"
				+ " dealdate=?, opendate=?, realname=?, ipaddress=?, ipmask=?, gateway=?, adsl_ser=? where user_id=?";
		// String str="select type_id from gw_dev_type where type_name=? ";
		// PrepareSQL pSql = new PrepareSQL();
		// pSql.setSQL(str);
		// pSql.setString(1, hgwObj.getTypeName());
		// String type_id=(String) jt.queryForList(pSql.getSQL()).get(0);
		PrepareSQL psqlUpdate = new PrepareSQL();
		String strUpdate = "update  gw_cust_user_dev_type set type_id=?,time=? where user_id = ?";
		psqlUpdate.setSQL(strUpdate);
		psqlUpdate.setString(1, hgwObj.getTypeName());
		psqlUpdate.setLong(2, System.currentTimeMillis() / 1000);
		psqlUpdate.setInt(3, StringUtil.getIntegerValue(hgwObj.getUserId()));
		jt.update(psqlUpdate.getSQL());
		
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(strSQL);
		psql.setString(1, hgwObj.getPasswd());
		psql.setString(2, hgwObj.getTelepone());
		psql.setLong(3, hgwObj.getDownSpeed());
		psql.setLong(4, hgwObj.getDownSpeed());
		psql.setLong(5, hgwObj.getUpSpeed());
		psql.setInt(6, hgwObj.getMaxUserNum());
		psql.setString(7, hgwObj.getVlanid());
		psql.setString(8, hgwObj.getVpiid());
		psql.setInt(9, StringUtil.getIntegerValue(hgwObj.getVciid()));
		psql.setString(10, hgwObj.getCityId());
		psql.setString(11, hgwObj.getOfficeId());
		psql.setString(12, hgwObj.getZoneId());
		psql.setString(13, hgwObj.getLinkman());
		psql.setString(14, hgwObj.getLinkphone());
		psql.setString(15, hgwObj.getEmail());
		psql.setString(16, hgwObj.getAddress());
		psql.setLong(17, nowTime);
		psql.setString(18, hgwObj.getCredno());
		psql.setString(19, hgwObj.getMobile());
		psql.setInt(20, hgwObj.getWanType());
		psql.setLong(21, hgwObj.getDealdate());
		psql.setLong(22, nowTime);
		psql.setString(23, hgwObj.getRealname());
		psql.setString(24, hgwObj.getIp());
		psql.setString(25, hgwObj.getMask());
		psql.setString(26, hgwObj.getGateway());
		psql.setString(27, hgwObj.getDns());
		psql.setLong(28, StringUtil.getLongValue(hgwObj.getUserId()));
		return jt.update(psql.getSQL());
	}

	/**
	 * 检查用户表中是否已经存在该用户，存在返回1，否则返回-1
	 * 
	 * @param 用户账号
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return int 存在返回1，否则返回-1
	 */
	public int checkUser(String username)
	{
		logger.debug("checkUser({})", username);
		int iret = -1;
		HgwCustObj[] objArr = getCustObjArrInfo(username, null);
		if (null != objArr)
		{
			iret = 1;
		}
		return iret;
	}

	public static long generateUserId() 
	{
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return generateUserIdOld();
		}
		return DbUtils.getUnusedID("sql_tab_hgwcustomer", 1);
	}
	
	/**
	 * 获取tab_hgwcustomer表新的user_id
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return long
	 */
	public static long generateUserIdOld()
	{
		logger.debug("generateUserId()");
		long userid = -1L;
		String callPro = "maxHgwUserIdProc 1";
		Map map = DataSetBean.getRecord(callPro);
		if (null != map && !map.isEmpty())
		{
			String strUserId = map.values().toArray()[0].toString();
			logger.debug("get the new userId:" + strUserId);
			userid = StringUtil.getLongValue(strUserId);
		}
		return userid;
	}

	/**
	 * 表(字段)转为对象(属相)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-29
	 * @return void
	 */
	private void map2Obj(HgwCustObj hgwCustObj, Map rMap)
	{
		logger.debug("map2Obj({}, {})", hgwCustObj, rMap);
		Map<String, String> cityMap = CityDAO.getCityIdCityNameMap();
		hgwCustObj.setUserId(StringUtil.getStringValue(rMap.get("user_id")));
		hgwCustObj.setUsername(StringUtil.getStringValue(rMap.get("username")));
		hgwCustObj.setCityId(StringUtil.getStringValue(rMap.get("city_id")));
		hgwCustObj.setCityName(cityMap.get(hgwCustObj.getCityId()));
		hgwCustObj.setOfficeId(StringUtil.getStringValue(rMap.get("office_id")));
		hgwCustObj.setOfficeName(OfficeDAO.getInstance().getOfficeIdNameMap().get(
				hgwCustObj.getOfficeId()));
		hgwCustObj.setDeviceId(StringUtil.getStringValue(rMap.get("device_id")));
		hgwCustObj.setOui(StringUtil.getStringValue(rMap.get("oui")));
		hgwCustObj.setDeviceSerial(StringUtil.getStringValue(rMap
				.get("device_serialnumber")));
		hgwCustObj.setRealname(StringUtil.getStringValue(rMap.get("realname")));
		hgwCustObj.setAddress(StringUtil.getStringValue(rMap.get("linkaddress")));
		hgwCustObj.setCredno(StringUtil.getStringValue(rMap.get("credno")));
		hgwCustObj.setDealdate(StringUtil.getLongValue(rMap.get("dealdate")));
		hgwCustObj.setDns(StringUtil.getStringValue(rMap.get("adsl_ser")));
		hgwCustObj.setDownSpeed(StringUtil.getLongValue(rMap.get("maxattdnrate")));
		hgwCustObj.setEmail(StringUtil.getStringValue(rMap.get("email")));
		hgwCustObj.setGateway(StringUtil.getStringValue(rMap.get("gateway")));
		hgwCustObj.setIp(StringUtil.getStringValue(rMap.get("ipaddress")));
		hgwCustObj.setLinkman(StringUtil.getStringValue(rMap.get("linkman")));
		hgwCustObj.setLinkphone(StringUtil.getStringValue(rMap.get("linkphone")));
		hgwCustObj.setMask(StringUtil.getStringValue(rMap.get("ipmask")));
		hgwCustObj.setMaxUserNum(StringUtil.getIntegerValue(rMap.get("max_user_number")));
		hgwCustObj.setMobile(StringUtil.getStringValue(rMap.get("mobile")));
		hgwCustObj.setPasswd(StringUtil.getStringValue(rMap.get("passwd")));
		hgwCustObj.setRealname(StringUtil.getStringValue(rMap.get("realname")));
		hgwCustObj.setTelepone(StringUtil.getStringValue(rMap.get("phonenumber")));
		hgwCustObj.setUpSpeed(StringUtil.getLongValue(rMap.get("upwidth")));
		hgwCustObj.setWanType(StringUtil.getIntegerValue(rMap.get("wan_type")));
		hgwCustObj.setZoneId(StringUtil.getStringValue(rMap.get("zone_id")));
		hgwCustObj.setVciid(StringUtil.getStringValue(rMap.get("vciid")));
		hgwCustObj.setVpiid(StringUtil.getStringValue(rMap.get("vpiid")));
		hgwCustObj.setVlanid(StringUtil.getStringValue(rMap.get("vlanid")));
		hgwCustObj.setTypeName(StringUtil.getStringValue(rMap.get("type_id")));
		cityMap = null;
	}
	
	
	
	/**
	 * 业务下发内容预先记录
	 * @param taskId 主键
	 * @param accOid 用户Id
	 * @param operType  //1:业务下发;2:批量无线业务开通（通用）;3:批量无线开通;4:批量无线关闭
	 * @param servType  //0:全业务;10:宽带业务;11:IPTV业务;14:语音业务;2001:无线开通;2003:无线关闭
	 * @param operStatus  //0:未处理;1:已处理;-1:处理失败
	 * @param gwType  //1:家庭网关;2:企业网关
	 * @param deviceId
	 * @param addTime
	 * @return
	 * @author jianglp(75508)
	 * @date 2016/7/25
	 * @return the number of rows affected
	 */
	public int addBatOptTask(long taskId, long accOid, int operType, int servType,
			int operStatus, int gwType, String deviceId, long addTime){
		logger.debug("addBatOptTask:"+taskId);
		String strSQL = "insert into BATCH_OPERATION_TASK ("
				+"task_id,acc_oid,oper_type,serv_type,oper_status,gw_type,device_id," +
				"add_time) values (?,?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setLong(1, taskId);
		psql.setLong(2,accOid);
		psql.setInt(3, operType);
		psql.setInt(4, servType);
		psql.setInt(5, operStatus);
		psql.setInt(6, gwType);
		psql.setString(7,deviceId);
		psql.setLong(8, addTime);
		int res=jt.update(psql.getSQL());
		logger.debug("addBatOptTask:"+res);
		return res;
	}
	
	/**
	 * 
	 * @param bodList 
	 * @return
	 * @author jianglp(75508)
	 * @date 2016/7/25
	 * @return an array of the number of rows affected by each statement
	 */
	public int[] addBatOptDevByBatch(List<Map> bodList){
		logger.debug("WirelessBusinessCtrlDao.addBatOptDevByBatch()---start:");
		String strSQL = "insert into batch_operation_dev("
				+"task_id,device_id,service_id,strategy_type,ssid,vlan_id,wireless_port," +
				"buss_level,channel,wireless_type,add_time,do_status" +
				") values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] sqlArr=new String[bodList.size()];
		PrepareSQL psql = new PrepareSQL(strSQL);
		for(int i=0;i<bodList.size();i++){
			Map bod=bodList.get(i);
			psql.setLong(1, StringUtil.getLongValue(bod, "taskId"));
			psql.setString(2,StringUtil.getStringValue(bod, "deviceId"));
			psql.setInt(3, StringUtil.getIntValue(bod,"serviceId"));
			psql.setString(4, StringUtil.getStringValue(bod,"stategyType"));
			psql.setString(5, StringUtil.getStringValue(bod,"ssid"));
			psql.setInt(6, StringUtil.getIntValue(bod,"vlanId"));
			psql.setInt(7, StringUtil.getIntValue(bod,"wirelessPort"));
			psql.setInt(8, StringUtil.getIntValue(bod,"bussLevel"));
			psql.setString(9, StringUtil.getStringValue(bod,"channel"));
			psql.setInt(10, StringUtil.getIntValue(bod,"wirelessType"));
			psql.setLong(11, StringUtil.getLongValue(bod,"addTime"));
			psql.setInt(12, StringUtil.getIntValue(bod,"doStatus"));
			sqlArr[i]=psql.getSQL();
		}
		int res[]=jt.batchUpdate(sqlArr);
		return res;
	}
	
	
	/**
	 * 入组播下移明细表批量
	 * @param bodList 
	 * @return
	 * @author fanjm(35572)
	 * @param loidlist 
	 * @param snlist 
	 * @param useridlist 
	 * @param user_oid 操作人
	 * @throws Exception 
	 * @date 2018/1/23
	 */
	public void addMulticastBatch(List<String> list, List<String> snlist, List<String> loidlist, List<String> useridlist, long user_oid) throws Exception{
		Map bod=null;
		ArrayList<String> batchSql = new ArrayList<String>();
		String[] sqls;
		
		for (int i = 0; i < list.size(); i++) {
			String device_id = list.get(i);
			String sn = snlist.get(i);
			String loid = loidlist.get(i);
			String userid = useridlist.get(i);
			String sql = addMulticast(device_id,user_oid,sn,loid,userid);
			
			batchSql.add(sql);
			if(batchSql.size()>=500){
				sqls = (String[]) batchSql.toArray(new String[batchSql.size()]);
				jt.batchUpdate(sqls);
				batchSql = new ArrayList<String>();
			}
		}
		if(batchSql.size()>0){
			sqls = (String[]) batchSql.toArray(new String[batchSql.size()]);
			jt.batchUpdate(sqls);
		}
	}
	
	
	/*public List<Map> queryInfo(String device_id) {

		logger.debug("queryMulticastNum()");
		
		PrepareSQL psql = new PrepareSQL("select d.device_serialnumber,a.username 
			from tab_hgwcustomer a,tab_gw_device d where a.device_id=d.device_id and d.device_id="+device_id);

		List<Map> num = jt.queryForList(psql.getSQL());

		return num;
	}*/
	
	/**
	 * 入组播下移明细表
	 * @param bodList 
	 * @return
	 * @author fanjm(35572)
	 * @param user_oid 操作人
	 * @param userid 
	 * @date 2018/1/23
	 * @return an array of the number of rows affected by each statement
	 */
	public String addMulticast(String deviceId, long user_oid,String sn,
			String loid, String userid) throws Exception
	{
		logger.debug("HgwCustDao.addMulticast()---start:");
		String strSQL = "insert into tab_setmulticast_dev(device_id,status,settime," +
				"acc_oid,device_serialnumber,iptv_account,user_id) values (?,?,?,?,?,?,?)";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		psql.setInt(2,0);
		psql.setLong(3, System.currentTimeMillis()/1000);
		psql.setLong(4, user_oid);
		psql.setString(5, sn);
		psql.setString(6, loid);
		psql.setString(7, userid);
		return psql.getSQL();
	}


	public List<String> getListBySql(String matchSQL) {
		return jt.queryForList(matchSQL);
	}

	public List<Map> queryTaskList(String servType_query, int status_query,
		long expire_time_start, long expire_time_end,int curPage_splitPage,
		int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select serv_type,task_id,oper_status,add_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from batch_operation_task where 1=1 ");
		if (!StringUtil.IsEmpty(servType_query.trim())) {
			psql.append(" and serv_type = "+servType_query);
		}
		if (-1 != status_query) {
			if(status_query==-3){
				psql.append(" and oper_status not in(0,1,2) ");
			}else {
				psql.append(" and oper_status = " + status_query);
			}
		}
		if (-1 != expire_time_start) {
			psql.append(" and add_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and add_time <= "+expire_time_end);
		}
		psql.append(" order by add_time desc");

		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				long succNum = 0;
				long failNum = 0;
				long totalNum = 0;
				long unDoneNum = 0;
				Map<String, String> map = new HashMap<String, String>();
				map.put("servTypeId", rs.getString("serv_type"));
				String taskId = rs.getString("task_id");

				if(!StringUtil.IsEmpty(taskId)){
					String strategyTab = "batch_operation_dev";
					PrepareSQL psql = new PrepareSQL();
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						psql.append("select count(*) from ");
					}else{
						psql.append("select count(1) from ");
					}
					psql.append(strategyTab + " where do_status=0 and task_id='"+taskId+"'");

					unDoneNum = jt.queryForLong(psql.getSQL());

					PrepareSQL psqlb = new PrepareSQL();
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						psqlb.append("select count(*) from ");
					}else{
						psqlb.append("select count(1) from ");
					}
					psqlb.append(strategyTab + " where do_status=1 and task_id='"+taskId+"'");

					succNum = jt.queryForLong(psqlb.getSQL());

					PrepareSQL psqla = new PrepareSQL();
					if(DBUtil.GetDB()==Global.DB_MYSQL){
						psqla.append("select count(*) from ");
					}else{
						psqla.append("select count(1) from ");
					}
					psqla.append(strategyTab + " where task_id='"+taskId+"'");

					totalNum = jt.queryForLong(psqla.getSQL());

					failNum = totalNum-unDoneNum-succNum;
				}
				
				map.put("task_id", taskId);
				map.put("status", rs.getString("oper_status"));
				map.put("totalNum", totalNum+"");
				map.put("succNum", succNum+"");
				map.put("failNum", failNum+"");
				map.put("unDoneNum", unDoneNum+"");
				map.put("servTypeNme", StringUtil.getStringValue(getServTypeList(),rs.getString("serv_type")));
				String add_time = rs.getString("add_time");
				String date = new DateTimeUtil(StringUtil.getLongValue(add_time)*1000).getLongDate();
				map.put("add_time", date);
				return map;
			}
		});
		return list;

	}

	public int queryTaskCount(String servType_query, int status_query,
		long expire_time_start, long expire_time_end, int curPage_splitPage,
		int num_splitPage) 
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from batch_operation_task ");
		psql.append(" where 1=1 ");
		if (!StringUtil.IsEmpty(servType_query.trim())) {
			psql.append(" and serv_type = "+servType_query);
		}
		if (-1 != status_query) {
			if(status_query==-3){
				psql.append(" and oper_status not in(0,1,2) ");
			}else {
				psql.append(" and oper_status = " + status_query);
			}
		}
		if (-1 != expire_time_start) {
			psql.append(" and add_time >= "+expire_time_start);
		}
		if (-1 != expire_time_end) {
			psql.append(" and add_time <= "+expire_time_end);
		}
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public int delTask(String task_id) {

		int res = 1;
		String[] sqls = new String[2];
		sqls[0] = "delete from BATCH_OPERATION_TASK where task_id='"+task_id+"'";
		sqls[1] = "delete from batch_operation_dev where task_id='"+task_id+"'";//删除未作的策略

		try{
			jt.batchUpdate(sqls);
		}
		catch (Exception e) {
			e.printStackTrace();
			res = 0;
		}
		return res;

	}

	public List<Map> queryTaskGyCityList(String task_id, String servTypeId, 
			int curPage_splitPage, int num_splitPage) 
	{
		List<Map> tList = new ArrayList<Map>();
		List<Map<String,String>> sList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> uList = new ArrayList<Map<String,String>>();
		long succNum = 0;
		long failNum = 0;
		long totalNum = 0;
		long unDoneNum = 0;

		String strategyTab = "batch_operation_dev";

		if(null != task_id || "".equals(task_id))
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select count(*) num,d.city_id from ");
			}else{
				psql.append("select count(1) num,d.city_id from ");
			}
			psql.append(strategyTab+" s,tab_gw_device d ");
			psql.append("where d.device_id=s.device_id and s.do_status=0 ");
			psql.append("and s.task_id='"+task_id + "' group by d.city_id");
			uList = jt.queryForList(psql.getSQL());

			PrepareSQL psqlb = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psqlb.append("select count(*) num,d.city_id from ");
			}else{
				psqlb.append("select count(1) num,d.city_id from ");
			}
			psqlb.append(strategyTab+" s,tab_gw_device d ");
			psqlb.append("where d.device_id=s.device_id and s.do_status=1 ");
			psqlb.append("and s.task_id='"+task_id + "' group by d.city_id");
			sList = jt.queryForList(psqlb.getSQL());

			PrepareSQL psqla = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psqla.append("select count(*) num,d.city_id from ");
			}else{
				psqla.append("select count(1) num,d.city_id from ");
			}
			psqla.append(strategyTab+" s,tab_gw_device d ");
			psqla.append("where d.device_id=s.device_id ");
			psqla.append("and s.task_id='"+task_id + "' group by d.city_id order by d.city_id");

			
			tList = querySP(psqla.getSQL(), (curPage_splitPage - 1) * num_splitPage
					+ 1, num_splitPage);

			for(int i=0;i<tList.size();i++) {
				Map<String, String> map = tList.get(i);
				succNum = 0;
				failNum = 0;
				unDoneNum = 0;
				String cityid = map.get("city_id");
				if (!StringUtil.IsEmpty(cityid)) {
					totalNum = StringUtil.getLongValue(map.get("num"));
					for (Map<String, String> maps : sList) {
						if (cityid.equals(maps.get("city_id"))) {
							succNum = StringUtil.getLongValue(maps.get("num"));
							break;
						}
					}
					for (Map<String, String> mapu : uList) {
						if (cityid.equals(mapu.get("city_id"))) {
							unDoneNum = StringUtil.getLongValue(mapu.get("num"));
							break;
						}
					}
					failNum = totalNum - unDoneNum - succNum;
					map.put("totalNum", totalNum + "");
					map.put("succNum", succNum + "");
					map.put("failNum", failNum + "");
					map.put("unDoneNum", unDoneNum + "");
					map.put("servTypeId", servTypeId);
					map.put("servTypeNme", StringUtil.getStringValue(getServTypeList(),servTypeId));
					map.put("city_id", cityid);
					map.put("city_name", Global.G_CityId_CityName_Map.get(cityid));
				}
			}
		}

		return tList;
	}

	public int queryTaskGyCityCount(String task_id) 
	{
		String strategyTab = "batch_operation_dev";
		int total = 0;
		if(null != task_id || "".equals(task_id))
		{
			PrepareSQL psqla = new PrepareSQL();
			psqla.append("select count(x.num) from ");
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psqla.append("(select count(*) num,d.city_id from ");
			}else{
				psqla.append("(select count(1) num,d.city_id from ");
			}
			psqla.append(strategyTab+" s,tab_gw_device d ");
			psqla.append("where d.device_id=s.device_id ");
			psqla.append("and s.task_id='"+task_id + "' group by d.city_id) x ");
			total = jt.queryForInt(psqla.getSQL());
		}

		return total;
	}


	public Map<String,String> getServTypeList() {
		String sql = "select t.serv_type_id,t.serv_type_name from tab_gw_serv_type t ";
		List<Map<String,String>> list = jt.queryForList(sql);
		Map map = new HashMap();
		if(list.size()>0){
			for (int i = 0; i <list.size() ; i++) {
				map.put(StringUtil.getStringValue(list.get(i).get("serv_type_id")),
						StringUtil.getStringValue(list.get(i).get("serv_type_name")));
			}
		}
		return map;
	}

	public List<Map> queryDevList(String task_id, String city_id, String type, 
			int curPage_splitPage, int num_splitPage) 
	{
		List<Map> list = new ArrayList<Map>();

		String strategyTab = "batch_operation_dev" ;

		if(null != task_id || "".equals(task_id)){
			PrepareSQL psql = new PrepareSQL();
			psql.append("select d.vendor_id,d.city_id,d.device_model_id,");
			psql.append("d.device_serialnumber,d.devicetype_id,s.do_status,s.add_time ");
			psql.append("from "+strategyTab+" s,tab_gw_device d ");
			psql.append("where d.device_id=s.device_id ");
			psql.append("and s.task_id='"+task_id+"'");
			if(!com.linkage.commons.util.StringUtil.IsEmpty(city_id)){
				psql.append(" and d.city_id='"+city_id+"'");
			}
			if("unDone".equals(type)){
				psql.append(" and s.do_status = 0  ");
			}
			else if("succ".equals(type)){
				psql.append(" and s.do_status = 1 ");
			}
			else if("fail".equals(type)){
				psql.append(" and s.do_status not in (0,1) ");
			}

			//不传分页信息查询全部
			if(curPage_splitPage==0 && num_splitPage==0){
				list = jt.queryForList(psql.getSQL());
			}
			else{
				list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
						+ 1, num_splitPage);
			}

			for(int i=0;i<list.size();i++){
				Map<String,String> map  = list.get(i);
				String vendor_id = map.get("vendor_id");
				if(null!= DeviceTypeUtil.vendorMap && !com.linkage.commons.util.StringUtil.IsEmpty(vendor_id)){
					String vendor = DeviceTypeUtil.vendorMap.get(vendor_id);
					map.put("vendor_id", vendor);
				}

				String device_model_id = map.get("device_model_id");
				if(null!=DeviceTypeUtil.deviceModelMap && !com.linkage.commons.util.StringUtil.IsEmpty(device_model_id)){
					String model = DeviceTypeUtil.deviceModelMap.get(device_model_id);
					map.put("device_model_id", model);
				}

				String devicetype_id = com.linkage.commons.util.StringUtil.getStringValue(map.get("devicetype_id"));
				if(null!=DeviceTypeUtil.softVersionMap && !com.linkage.commons.util.StringUtil.IsEmpty(devicetype_id)){
					String version = DeviceTypeUtil.softVersionMap.get(devicetype_id);
					//修复问题：新增的软件信息在软件升级管理界面不展示问题。未从softVersionMap中获取到对应的version时，更新下内存
					if(com.linkage.commons.util.StringUtil.IsEmpty(version))
					{
						DeviceTypeUtil.init();  //更新下内存
						version = DeviceTypeUtil.softVersionMap.get(devicetype_id); //再取一次
					}
					map.put("version", version);
				}


				long add_time = StringUtil.getLongValue(map.get("add_time"));
				if(add_time!=0){
					map.put("add_time", new DateTimeUtil(add_time*1000).getLongDate());
				}
				else{
					map.put("add_time", "");
				}


				if(null!=Global.G_CityId_CityName_Map){
					map.put("city_name", Global.G_CityId_CityName_Map.get(city_id));
				}

				int status = StringUtil.getIntegerValue(map.get("do_status"));

				if(status ==0){
					map.put("status", "未做");
				}
				else if(status ==1){
					map.put("status", "成功");
				}
				else{
					map.put("status", "失败");
				}
			}
		}
		return list;
	}
}
