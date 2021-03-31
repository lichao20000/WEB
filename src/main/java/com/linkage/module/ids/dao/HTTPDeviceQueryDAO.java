package com.linkage.module.ids.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-18
 * @category com.linkage.module.ids.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class HTTPDeviceQueryDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(HTTPDeviceQueryDAO.class);
	
	public Map<String,String> getDefaultdiag()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select column1,column2 from tab_diag_default where default_type_id=3 ");
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String,String> map = new HashMap<String, String>();
		if(null!=list && list.size()>0){
			map.put("column1", StringUtil.getStringValue(list.get(0).get("column1")));
			map.put("column2", StringUtil.getStringValue(list.get(0).get("column2")));
		}
		return map;
	}
	/**
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @param status
	 * @param test_time
	 * @param download_url
	 * @param eth_priority
	 * @return
	 */
	public int addHTTPDiagResult(String oui,String device_serialnumber,String status,
			String test_time,String download_url,String eth_priority,
			String RequestsReceivedTime,String TransportStartTime,String TransportEndTime,
			String ReceiveByteContainHead,String ReceiveByte,String TCPRequestTime,
			String TCPResponseTime, String cityId,String netaccount,
			String loid,String maxspeed,String avgspeed)
	{
		PrepareSQL psql = new PrepareSQL();
		if(LipossGlobals.inArea(Global.JSDX)){
			psql = new PrepareSQL("insert into tab_http_diag_result(oui,device_serialnumber," +
					"status,test_time,download_url,eth_priority,rom_time,bom_time,eom_time," +
					"test_bytes_rece,total_bytes_rece,tcp_req_time,tcp_resp_time,cityname," +
					"netaccount,loid,maxspeed,avgspeed) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		} else {
			psql = new PrepareSQL("insert into tab_http_diag_result(oui,device_serialnumber," +
					"status,test_time,download_url,eth_priority,rom_time,bom_time,eom_time," +
					"test_bytes_rece,total_bytes_rece,tcp_req_time,tcp_resp_time) " +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		}
		
		psql.setString(1, StringUtil.getStringValue(oui));
		psql.setString(2, StringUtil.getStringValue(device_serialnumber));
		psql.setInt(3, StringUtil.getIntegerValue(status));
		psql.setLong(4, StringUtil.getLongValue(test_time));
		psql.setString(5, StringUtil.getStringValue(download_url));
		psql.setInt(6, StringUtil.getIntegerValue(eth_priority));
		psql.setString(7, StringUtil.getStringValue(RequestsReceivedTime));
		psql.setString(8, StringUtil.getStringValue(TransportStartTime));
		psql.setString(9, StringUtil.getStringValue(TransportEndTime));
		psql.setLong(10, StringUtil.getLongValue(ReceiveByteContainHead));
		psql.setLong(11, StringUtil.getLongValue(ReceiveByte));
		psql.setString(12, StringUtil.getStringValue(TCPRequestTime));
		psql.setString(13, StringUtil.getStringValue(TCPResponseTime));
		if(LipossGlobals.inArea(Global.JSDX)){
			psql.setString(14, Global.G_CityId_CityName_Map.get(cityId));
			psql.setString(15, netaccount);
			psql.setString(16, loid);
			psql.setString(17, maxspeed);
			psql.setString(18, avgspeed);
		}
		
		return jt.update(psql.getSQL());
	}
	
	/**
	 * 安徽电信下载拨测专属测试用户查询
	 */
	public List<HashMap<String,String>> getTestUserList()
	{
		PrepareSQL psql=new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select testname,username,password,testrate from tab_http_test_user");
		}else{
			psql.append("select * from tab_http_test_user");
		}
		return DBOperation.getRecords(psql.getSQL());
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfo(String device_serialnumber) 
	{
		PrepareSQL psql = new PrepareSQL(
				"select a.city_id, a.username loid,b.username netaccount " +
				"from tab_hgwcustomer a left join hgwcust_serv_info b ");
		psql.append("on a.user_id = b.user_id where b.serv_type_id = 10 " +
				"and a.device_serialnumber = '"
				+ device_serialnumber + "' order by a.opendate desc");
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());

		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return new HashMap<String, String>();
	}
	
	/**
	 * 河北联通查询速率表
	 */
	public String getRate(String pppoeUserName, String device_id)
	{
		PrepareSQL psql=new PrepareSQL();
		psql.append("select a.rate from tab_speed_dev_rate a,tab_gw_device b ");
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("where concat(a.pppoe_name,ifnull(a.account_suffix,''))=? ");
		}else{
			psql.append("where a.pppoe_name||nvl(a.account_suffix,'')=? ");
		}
		psql.append(" and b.device_serialnumber=? and a.device_serialnumber=b.device_serialnumber ");
		psql.setString(1, pppoeUserName);
		psql.setString(2, device_id);
		return StringUtil.getStringValue(DBOperation.getRecord(psql.getSQL()), "rate", "");
	}
	
	public int addHTTPResult(String devId,String oui,String device_serialnumber,
			String status,String test_time,String download_url,
			String RequestsReceivedTime,String TransportStartTime,
			String TransportEndTime,String ReceiveByte,String TCPRequestTime,
			String TCPResponseTime, String testUsername,String loid,String avgtTotalspeed,
			String avgspeed,String cmdId,String rstMsg,String downlink,String ip,
			String netUser,String acc_loginame)
	{
		String wanType = "";
		String user = "";
		if(!StringUtil.IsEmpty(netUser)){
			if(netUser.split("#").length < 2){
				user = netUser.split("#")[0];
			}else{
				user = netUser.split("#")[0];
				wanType = netUser.split("#")[1];
			}
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql = new PrepareSQL("insert into tab_http_simplex_rate ");
		psql.append("(device_id,oui,device_serialnumber,cmdId,rstCode,rstMsg," +
				"username,downlink,avgSampledTotalValues,maxSampledTotalValues,"+
				"transportStartTime,transportEndTime,ip,receiveByte,tcpRequestTime," +
				"tcpResponseTime,downUrl,updateTime,test_time,wan_type,loid,status," +
				"testusername,acc_loginame) ");
		psql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		psql.setString(1, StringUtil.getStringValue(devId));
		psql.setString(2, StringUtil.getStringValue(oui));
		psql.setString(3, StringUtil.getStringValue(device_serialnumber));
		psql.setString(4, StringUtil.getStringValue(cmdId));
		psql.setLong(5, StringUtil.getLongValue(test_time));
		psql.setString(6, StringUtil.getStringValue(rstMsg));
		psql.setString(7, StringUtil.getStringValue(user));
		psql.setString(8, StringUtil.getStringValue(downlink));
		psql.setString(9, StringUtil.getStringValue(avgtTotalspeed));
		psql.setString(10, StringUtil.getStringValue(avgspeed));
		psql.setString(11, StringUtil.getStringValue(TransportStartTime));
		psql.setString(12, StringUtil.getStringValue(TransportEndTime));
		psql.setString(13, StringUtil.getStringValue(ip));
		psql.setString(14, StringUtil.getStringValue(ReceiveByte));
		psql.setString(15, StringUtil.getStringValue(TCPRequestTime));
		psql.setString(16, StringUtil.getStringValue(TCPResponseTime));
		psql.setString(17, StringUtil.getStringValue(download_url));
		psql.setLong(18, System.currentTimeMillis()/1000);
		psql.setLong(19, StringUtil.getLongValue(cmdId));
		psql.setString(20, getWanType(wanType));
		psql.setString(21, StringUtil.getStringValue(loid));
		psql.setInt(22, StringUtil.getIntegerValue(status));
		psql.setString(23, StringUtil.getStringValue(testUsername));
		psql.setString(24, StringUtil.getStringValue(acc_loginame));
		
		return jt.update(psql.getSQL());
	}
	
	public String getWanType(String type)
	{
		String ret = "IP_Routed";
		if("1".equals(type)){
			ret = "PPPoE_Bridged";
		}
		return ret;
	}
	
	public Map<String, String> getInfoForSDDX(String type, String username) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			psql.append("select a.vendor_id,a.device_model_id,a.devicetype_id,");
			psql.append("t.city_id,t.loid,t.device_id,t.username,t.passwd ");
			psql.append("from tab_gw_device a,");
			psql.append("(select e.device_id,e.city_id,e.username as loid,e.device_id,g.username,g.passwd ");
			psql.append("from tab_hgwcustomer e,hgwcust_serv_info g ");
			psql.append("where e.user_id=g.user_id and g.serv_type_id=10 ");
			if ("1".equals(type)) {
				psql.append("and g.username='" + username + "'"); // 宽带账号
			}else if ("2".equals(type)){
				psql.append("and e.username='" + username + "'"); // loid
			}
			psql.append(") t ");
			psql.append("where a.device_id=t.device_id ");
		}else{
			psql.append("select f.city_name, b.vendor_name,c.device_model,d.softwareversion,");
			psql.append("e.username as loid, a.device_id, g.username, g.passwd ");
			psql.append("from tab_gw_device a,tab_vendor b,gw_device_model c," +
					"tab_devicetype_info d,tab_hgwcustomer e,");
			psql.append("tab_city f,hgwcust_serv_info g where e.device_id=a.device_id ");
			psql.append("and a.vendor_id=b.vendor_id and a.device_model_id=c.device_model_id " +
					"and a.devicetype_id=d.devicetype_id ");
			psql.append("and f.city_id=e.city_id and e.user_id=g.user_id and g.serv_type_id=10 ");
			if ("1".equals(type)) {
				psql.append("and g.username='" + username + "'"); // 宽带账号
			}else if ("2".equals(type)){
				psql.append("and e.username='" + username + "'"); // loid
			}
		}
		
		List<Map<String, String>> list = jt.queryForList(psql.getSQL());
		if(list==null || list.isEmpty()){
			return new HashMap<String, String>();
		}
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			Map<String,String> vn=getVendorName();
			Map<String,String> dm=getDeviceModel();
			Map<String,String> dti=getDeviceTypeInfo();
			for(Map<String, String> m:list)
			{
				Map<String, String> map=new HashMap<String,String>();
				String city_name=CityDAO.getCityName(StringUtil.getStringValue(m,"city_id"));
				String vendor_name=vn.get(StringUtil.getStringValue(m,"vendor_id"));
				String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
				String softwareversion=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
				if(StringUtil.IsEmpty(city_name) 
						|| StringUtil.IsEmpty(vendor_name) 
						|| StringUtil.IsEmpty(device_model) 
						|| StringUtil.IsEmpty(softwareversion)){
					continue;
				}
				map.put("city_name",city_name);
				map.put("vendor_name",vendor_name);
				map.put("device_model",device_model);
				map.put("softwareversion",softwareversion);
				map.put("loid",StringUtil.getStringValue(m,"loid"));
				map.put("device_id",StringUtil.getStringValue(m,"device_id"));
				map.put("username",StringUtil.getStringValue(m,"username"));
				map.put("passwd",StringUtil.getStringValue(m,"passwd"));
				return map;
			}
			
			vn=null;
			dm=null;
			dti=null;
			return new HashMap<String, String>();
		}else{
			return list.get(0);
		}
		
	}
	
	/**
	 * 获取所有厂商
	 */
	public Map<String,String> getVendorName()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_name from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"vendor_id"),
						StringUtil.getStringValue(m,"vendor_name"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有型号
	 */
	public Map<String,String> getDeviceModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from gw_device_model order by device_model_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有版本
	 */
	public Map<String,String> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,softwareversion from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"devicetype_id"),
						StringUtil.getStringValue(m,"softwareversion"));
			}
		}
		
		return map;
	}
}
