package com.linkage.module.gwms.diagnostics.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.interf.IParamTree;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.LanHostDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.gw.WanConnDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.dao.gw.WireInfoDAO;
import com.linkage.module.gwms.dao.tabquery.HgwCustDAO;
import com.linkage.module.gwms.dao.tabquery.HgwServUserDAO;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.LanHostObj;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WanObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class DeviceDiagnosticDAO extends SuperDAO
{
	private static final Logger LOG = LoggerFactory.getLogger(DeviceDiagnosticDAO.class);

	//使用到的DAO
	WireInfoDAO wireInfoDao;
	WanConnDAO wanConnDao;
	WanConnSessDAO wanConnSessDao;
	LanHostDAO lanHostDao;
	HgwCustDAO hgwCustDao;
	HgwServUserDAO hgwServUserDao;
	
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	/**
	 * 获取设备线路信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return DeviceWireInfoObj[]
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId) {
		LOG.debug("queryDevWireInfo (String deviceId):" + deviceId);
		DeviceWireInfoObj[] wireInfoObj = wireInfoDao.queryDevWireInfo(deviceId);
		return wireInfoObj;
	}

	/**
	 * 取出特定(设备)WanConnection下面所有的Sesstion连接属性
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] queryDevWanConnSession(WanConnObj wanConnObj) {
		LOG.debug("queryDevWanConnSession(wanConnObj) :" + wanConnObj);
		WanConnSessObj[] wanConnSessObj = wanConnSessDao.queryDevWanConnSession(wanConnObj);
		return wanConnSessObj;

	}

	/**
	 * 获取设备WanConn特定PVC值结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(String deviceId, String vpiid,
			String vciid) {
		LOG.debug("queryDevWanConn(deviceId, vpi, vci) :" + deviceId + "," + vpiid + "/" + vciid);
		WanConnObj wanConnObj = wanConnDao.queryDevWanConn(deviceId, vpiid, vciid);
		return wanConnObj;
	}

	/**
	 * 获取设备WanConn所有结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnObj[]
	 */
	public WanConnObj[] queryDevWanConn(String deviceId) {
		LOG.debug("queryDevWanConn(deviceId) :" + deviceId);
		WanConnObj[] wanConnObj = wanConnDao.queryDevWanConn(deviceId);
		return wanConnObj;
	}

	/**
	 * 获取lan测连接主机的状态
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return LanDeviceHost[]
	 */
	public LanHostObj[] queryLanHost(String deviceId) {
		LOG.debug("queryLanHost(deviceId) :" + deviceId);
		LanHostObj[] lanHost = lanHostDao.queryLanHost(deviceId);
		return lanHost;
	}
	
	/**
	 * 获取lan测连接主机状态为active=1的host信息
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-7-9
	 * @return LanHostObj[]
	 */
	public LanHostObj[] queryActiveLanHost(String deviceId) {
		LOG.debug("queryActiveLanHost(deviceId) :" + deviceId);
		LanHostObj[] lanHost = lanHostDao.queryLanHost(deviceId,1);
		return lanHost;
	}

	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public String getUserId(String deviceId){
		String userId = hgwCustDao.getUserId(deviceId);
		return userId;
	}

	/**
	 * 获取端口字符串
	 * 			InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WANPPPConnection.1
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-27
	 * @return String
	 */
	public String getPvcInter(String deviceId, String vpi, String vci){
		String deviceInterface = null;
		WanConnObj wanConnObj= queryDevWanConn(deviceId, vpi, vci);
		if(null != wanConnObj){
			WanConnSessObj[] sessObj = queryDevWanConnSession(wanConnObj);
			if(null != sessObj && sessObj.length > 0){
				//取第一个session
				deviceInterface = sessObj[0].getPingInterface();
//				deviceInterface = IParamTree.WANDEVICE + "." + wanConnObj.getWan_id() + "."
//					+ IParamTree.WANCONNDEVICE + "." + wanConnObj.getWan_conn_id() + "."
//					+ IParamTree.WANPPPCONN + "." + sessObj[0].getWanConnSessId();
			}else{
				LOG.error("根据PVC获取ping端口失败, PPPCONN结点默认为1");
				deviceInterface = IParamTree.WANDEVICE + "." + wanConnObj.getWan_id() + "."
				+ IParamTree.WANCONNDEVICE + "." + wanConnObj.getWan_conn_id() + "."
				+ IParamTree.WANPPPCONN + ".1";
			}
		}else{
			LOG.error("设备上不存在该PVC");
//			deviceInterface = IParamTree.WANDEVICE + ".1."
//			+ IParamTree.WANCONNDEVICE + ".1."
//			+ IParamTree.WANPPPCONN + ".1";
		}
		
		return deviceInterface;
	}
	
	
	/**
	 * 获取端口字符串
	 * 			InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WANIPConnection.1
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-27
	 * @return String
	 */
	public String getPvcInter(String deviceId, String vlanid){
		String deviceInterface = null;
		WanConnObj wanConnObj= wanConnDao.queryDevWanConn(deviceId, vlanid);
		if(null != wanConnObj){
			WanConnSessObj[] sessObj = queryDevWanConnSession(wanConnObj);
			if(null != sessObj && sessObj.length > 0){
				//取第一个session
				deviceInterface = sessObj[0].getPingInterface();
			}else{
				LOG.error("根据PVC获取ping端口失败, PPPCONN结点默认为1");
				deviceInterface = IParamTree.WANDEVICE + "." + wanConnObj.getWan_id() + "."
				+ IParamTree.WANCONNDEVICE + "." + wanConnObj.getWan_conn_id() + "."
				+ IParamTree.WANIPCONN + ".1";
			}
		}else{
			LOG.error("设备上不存在该PVC");
		}
	return deviceInterface;
}
	
	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String[]
	 */
	public String[] getServPvc(String servType) {
		LOG.debug("getServPvc({})", servType);
		String strSQL = "select param_name, param_value from local_serv_param where param_filed='servparamcheck'";
		PrepareSQL psql = new PrepareSQL(strSQL);
		if ("11".equals(servType)) {
			psql.append(" and param_name='11'");
		} else if ("12".equals(servType)) {
			psql.append(" and param_name='12'");
		} else {
			psql.append(" and param_name='10'");
		}
		List rList = jt.queryForList(psql.getSQL());
		String[] arryStr = null;
		if (null != rList && rList.size() > 0) {
			int lsize = rList.size();
			arryStr = new String[lsize];
			for (int i = 0; i < lsize; i++) {
				Map rMap = (Map) rList.get(i);
				if (null != rMap && false == rMap.isEmpty()) {
					arryStr[i] = String.valueOf(rMap.get("param_value"));
				}
			}
		}
		return arryStr;
	}

	/**
	 * 获取设备ping信息，包大小,超时时间,ping次数,ping目的地址
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return PingObject
	 */
	public PingObject getPingParam(String pingAddrType) {
		PingObject pingObj = null;
		List tList = queryLocalServParam("pingcheck");
		if (null != tList && tList.isEmpty() == false) {
			// list->map
			int lsize = tList.size();
			Map<String, String> tMap = new HashMap<String, String>();
			for (int i = 0; i < lsize; i++) {
				Map map = (Map) tList.get(i);
				if (null != map && map.isEmpty() == false) {
					tMap.put(String.valueOf(map.get("param_name")), String
							.valueOf(map.get("param_value")));
				}
			}
			// create a PingObject
			pingObj = new PingObject();
			if (null != tMap.get("packgeSize")) {
				pingObj.setPackageSize(Long.valueOf(tMap.get("packgeSize")));
			}
			if (null != tMap.get("timeOut")) {
				pingObj.setTimeOut(Long.valueOf(tMap.get("timeOut")));
			}
			if (null != tMap.get("pingTimes")) {
				pingObj.setNumOfRepetitions(Integer.valueOf(tMap.get("pingTimes")));
			}
			if("dnsAddress".equals(pingAddrType)){
				pingObj.setPingAddress(tMap.get("dnsAddress"));
			}else if("specialAddress".equals(pingAddrType)){
				pingObj.setPingAddress(tMap.get("specialAddress"));
			}else if("specialDomain".equals(pingAddrType)){
				pingObj.setPingAddress(tMap.get("specialDomain"));
			}
		}
		return pingObj;
	}

	
	/**
	 * ping信息，包大小,超时时间,ping次数,ping目的地址
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return PingObject
	 */
	public Map initPingParam() {
		LOG.debug("initPingParam()");
		List tList = queryLocalServParam("pingcheck");
		Map<String, String> tMap = null;
		if (null != tList && tList.isEmpty() == false) {
			LOG.debug("tList is not Empty");
			// list->map
			int lsize = tList.size();
			tMap = new HashMap<String, String>();
			for (int i = 0; i < lsize; i++) {
				Map map = (Map) tList.get(i);
				if (null != map && map.isEmpty() == false) {
					tMap.put(String.valueOf(map.get("param_name")), String
							.valueOf(map.get("param_value")));
				}
			}
		}
		return tMap;
	}
	
	/**
	 * 获取本地配置参数表的参数map
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return List
	 */
	public List queryLocalServParam(String param_filed) {
		String strSQL = "select param_name, param_value from local_serv_param where param_filed=?";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, param_filed);
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取业务用户对象
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-13
	 * @return HgwServUserObj
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId){
		LOG.debug("getUserInfo({},{})", userId, servTypeId);
		if(StringUtil.IsEmpty(userId) || StringUtil.IsEmpty(servTypeId)){
			return null;
		}
		HgwServUserObj servUserObj = hgwServUserDao.getUserInfo(userId, servTypeId);
		return servUserObj;
	}

	/**
	 * 获取WanConnSessObj[], 服务列表包含"INTERNET or BOTH",并且连接状态为"Connected"
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2010-1-26
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] getConntedSess(String deviceId){
		LOG.debug("getConntedSess({})", deviceId);
		return wanConnSessDao.getConntioningSess(deviceId);
	}
	
	
	
	/**
	 * 查询PON设备信息
	 *
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param 
	 * @return PONInfoOBJ[]
	 */
	public PONInfoOBJ[] queryPONInfo(String deviceId){
		
		LOG.debug("queryPONInfo in");
		if (null == deviceId) {
			return null;
		}
		PONInfoOBJ[] ponInfoOBJ = null;
		String strSQL = "";
		PrepareSQL psql = new PrepareSQL(strSQL);
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select status,tx_power,rx_power,transceiver_temperature,supply_vottage,");
			psql.append("bias_current,bytes_sent,bytes_received,packets_sent,packets_received,");
			psql.append("sunicast_packets,runicast_packets,smulticast_packets,rmulticast_packets,");
			psql.append("sbroadcast_packets,rbroadcast_packets,fec_error,hec_error,");
			psql.append("drop_packets,spause_packets,rpause_packets ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_wan_wireinfo_epon where device_id=? ");
		psql.setString(1, deviceId);
		List rList = jt.queryForList(psql.getSQL());
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			ponInfoOBJ = new PONInfoOBJ[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = (Map) rList.get(i);
				ponInfoOBJ[i] = new PONInfoOBJ();
				LOG.debug(rMap.toString());
				if (null != rMap && rMap.isEmpty() == false) {
					ponInfoOBJ[i].setStatus(StringUtil.getStringValue(rMap
							.get("status")));
					
					double	tx_power=StringUtil.getDoubleValue(rMap.get("tx_power"));
					double	rx_power=StringUtil.getDoubleValue(rMap.get("rx_power"));
					if(tx_power>30){
						double temp_tx_power= (Math.log(tx_power/10000) /Math.log(10))*10;
						tx_power=(int) temp_tx_power;
						if(tx_power%10 >=5){
							tx_power=(tx_power/10+1)*10;
						}
						else{
							tx_power=tx_power/10*10;
						}
					}
					if(rx_power>30){
						double temp_rx_power= (Math.log(rx_power/10000) /Math.log(10))*10;
						rx_power=(int) temp_rx_power;
						if(rx_power%10 >=5){
							rx_power=(rx_power/10+1)*10;
						}
						else{
							rx_power=rx_power/10*10;
						}
					}	
					
					LOG.warn("==tx_power==="+tx_power+"=======");
					LOG.warn("==rx_power==="+rx_power+"=======");
					
					ponInfoOBJ[i].setTxpower(tx_power+"");
					ponInfoOBJ[i].setRxpower(rx_power+"");
					ponInfoOBJ[i].setTransceiverTemperature(StringUtil.getStringValue(rMap
							.get("transceiver_temperature")));
					ponInfoOBJ[i].setSupplyVottage(StringUtil.getStringValue(rMap
							.get("supply_vottage")));
					ponInfoOBJ[i].setBiasCurrent(StringUtil.getStringValue(rMap
							.get("bias_current")));
					ponInfoOBJ[i].setBytesSent(StringUtil.getStringValue(rMap
							.get("bytes_sent")));
					ponInfoOBJ[i].setBytesReceived(StringUtil.getStringValue(rMap
							.get("bytes_received")));
					ponInfoOBJ[i].setPacketsSent(StringUtil.getStringValue(rMap
							.get("packets_sent")));
					ponInfoOBJ[i].setPacketsReceived(StringUtil.getStringValue(rMap
							.get("packets_received")));
					
					ponInfoOBJ[i].setSunicastPackets(StringUtil.getStringValue(rMap
							.get("sunicast_packets")));
					ponInfoOBJ[i].setRunicastPackets(StringUtil.getStringValue(rMap
							.get("runicast_packets")));
					ponInfoOBJ[i].setSmulticastPackets(StringUtil.getStringValue(rMap
							.get("smulticast_packets")));
					ponInfoOBJ[i].setRmulticastPackets(StringUtil.getStringValue(rMap
							.get("rmulticast_packets")));
					ponInfoOBJ[i].setSbroadcastPackets(StringUtil.getStringValue(rMap
							.get("sbroadcast_packets")));
					ponInfoOBJ[i].setRbroadcastPackets(StringUtil.getStringValue(rMap
							.get("rbroadcast_packets")));
					ponInfoOBJ[i].setFecError(StringUtil.getStringValue(rMap
							.get("fec_error")));
					ponInfoOBJ[i].setHecError(StringUtil.getStringValue(rMap
							.get("hec_error")));
					ponInfoOBJ[i].setDropPackets(StringUtil.getStringValue(rMap
							.get("drop_packets")));
					ponInfoOBJ[i].setSpausePackets(StringUtil.getStringValue(rMap
							.get("spause_packets")));
					ponInfoOBJ[i].setRpausePackets(StringUtil.getStringValue(rMap
							.get("rpause_packets")));
				}
			}
		}
		return ponInfoOBJ;
	}
	
	
	/**
	 * 获取设备的上行方式，
	 * 
	 * @param 设备ID
	 * @author Jason(3412)
	 * @date 2009-10-29
	 * @return int 1:ADSL 2:Ethernet 3:EPON 4:POTS -1:未知
	 */
	public int getAccessType(String deviceId) {
		LOG.debug("getAccessType({})", deviceId);
		WanObj wanObj = getWan(deviceId, "1");
		if (null != wanObj) {
			if ("DSL".equals(wanObj.getAccessType())) {
				return 1;
			} else if ("Ethernet".equals(wanObj.getAccessType())) {
				return 2;
			} else if ("EPON".equalsIgnoreCase(wanObj.getAccessType()) 
					|| "PON".equalsIgnoreCase(wanObj.getAccessType())
					|| "X_CU_PON".equalsIgnoreCase(wanObj.getAccessType())) {
				return 3;
			} else if ("GPON".equalsIgnoreCase(wanObj.getAccessType())
					||"X_CU_GPON".equalsIgnoreCase(wanObj.getAccessType())) {
				return 4;
			} else {
				return -1;
			}
		} else {
			LOG.warn("从数据库中未获取到设备的WAN结点信息");
			return -1;
		}
	}
	
	
	/**
	 * 根据deviceId和wanId获得一个WanObj
	 * @author gongsj
	 * @date 2009-7-16
	 * @param deviceId
	 * @param wanId
	 * @return
	 */
	public WanObj getWan(String deviceId, String wanId) 
	{
		WanObj wanObj = null;
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_conn_num,access_type,gather_time ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_wan where device_id=? and wan_id=? ");
		psql.setString(1, deviceId);
		psql.setStringExt(2, wanId, false);
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && rMap.isEmpty() == false) {
			wanObj = new WanObj();
			wanObj.setDevId(deviceId);
			wanObj.setWanId(wanId);
			wanObj.setWanConnNum(String.valueOf(rMap.get("wan_conn_num")));
			wanObj.setAccessType(String.valueOf(rMap.get("access_type")));
			wanObj.setGatherTime(String.valueOf(rMap.get("gather_time")));
		}
		
		if (2 == LipossGlobals.accessTypeFrom())
		{
			String accessTypeStr = null;
			Map<String, String> acctype = new HashMap<String, String>();
			acctype.put("ADSL", "DSL");
			acctype.put("LAN", "Ethernet");
			acctype.put("EPON光纤", "EPON");
			acctype.put("GPON光纤", "GPON");
			
			Map result=null;
			PrepareSQL psql1 = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql1.append("select a.access_style_relay_id ");
				psql1.append("from tab_devicetype_info a,tab_gw_device c ");
				psql1.append("where c.devicetype_id=a.devicetype_id and c.device_id=? ");
				result = DataSetBean.getRecord(psql1.getSQL());
				if (null != result && result.isEmpty() == false) {
					psql1=null;
					psql1 = new PrepareSQL();
					psql1.append("select type_name from gw_access_type ");
					psql1.append("where type_id="+StringUtil.getStringValue(result,"access_style_relay_id"));
					result = DataSetBean.getRecord(psql1.getSQL());
				}
			}else{
				psql1.append("select a.devicetype_id,a.access_style_relay_id,a.add_time,b.type_name ");
				psql1.append("from tab_devicetype_info a,gw_access_type b,tab_gw_device c ");
				psql1.append("where c.devicetype_id=a.devicetype_id ");
				psql1.append("and a.access_style_relay_id=b.type_id and c.device_id=? ");
				psql1.setString(1, deviceId);
				
				result = DataSetBean.getRecord(psql1.getSQL());
			}
			
			if (null != result && result.isEmpty() == false) {
				accessTypeStr = acctype.get(String.valueOf(result.get("type_name")));
			}
			if(!StringUtil.IsEmpty(accessTypeStr)) {
				wanObj.setAccessType(accessTypeStr);
			}
		}
		return wanObj;
		
	}
	
	
	public List<Map> checkIsBand(String device_id) {
		String sql = "select a.device_id,a.device_serialnumber,a.cpe_allocatedstatus from tab_gw_device a where a.cpe_allocatedstatus=1 and device_id='"+device_id+"' ";
		List list = null;
		try {
			list = jt.queryForList(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<Map> getCheckList(String account,
			String device_serialnumber,String vendor,String device_model,String hardwareversion,String softwareversion,String startTime,String endTime,
			int curPage_splitPage, int num_splitPage) {

		StringBuffer psql=new StringBuffer();
		psql.append(" select a.acc_loginname,a.loid,a.device_serialnumber,vendor_id,device_model_id,hardwareversion,softwareversion,file_path,is_qualified,test_time,file_time,state from tab_check_project_log a ");
		psql.append(" where 1=1 ");

		if (!StringUtil.IsEmpty(account)) {
			psql.append("   and a.acc_loginname = '");
			psql.append(account);
			psql.append("' ");
		}
		
		if (!StringUtil.IsEmpty(device_serialnumber)) {
			psql.append("   and a.device_serialnumber like '%"+device_serialnumber+"%'");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion ='"+hardwareversion+"' ");
		}
		if (!StringUtil.IsEmpty(softwareversion) && !"-1".equals(softwareversion)) {
			psql.append("   and a.softwareversion ='"+softwareversion+"' ");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append("   and a.test_time >='"+startTime+"' ");
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append("   and a.test_time <'"+endTime+"' ");
		}

		psql.append("   order by  test_time desc ");
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = querySP(psql.toString(),
				(curPage_splitPage - 1) * num_splitPage + 1, num_splitPage,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						Map<String, String> map = new HashMap<String, String>();

						map.put("acc_loginname", StringUtil.getStringValue(rs.getString("acc_loginname")));
						map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
						map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));

						/** 获取设备厂商名称 */
						String vendor_add = StringUtil.getStringValue(vendorMap.get(StringUtil.getStringValue(rs.getString("vendor_id"))));
						if (false == StringUtil.IsEmpty(vendor_add)) {
							map.put("vendorName", vendor_add);
						} else {
							map.put("vendorName", "");
						}

						/** 获取设备型号 */
						String device_model = StringUtil.getStringValue(deviceModelMap.get(StringUtil.getStringValue(rs.getString("device_model_id"))));
						if (false == StringUtil.IsEmpty(device_model)) {
							map.put("device_model", device_model);
						} else {
							map.put("device_model", "");
						}
						/** 获取硬件版本 */
						String hardwareversion = StringUtil.getStringValue(rs.getString("hardwareversion"));
						if (false == StringUtil.IsEmpty(hardwareversion)) {
							map.put("hardwareversion", hardwareversion);
						} else {
							map.put("hardwareversion", "");
						}
						/** 获取软件版本 */
						String softwareversion = StringUtil.getStringValue(rs.getString("softwareversion"));
						if (false == StringUtil.IsEmpty(softwareversion)) {
							map.put("softwareversion", softwareversion);
						} else {
							map.put("softwareversion", "");
						}
						
						map.put("file_path", StringUtil.getStringValue(rs.getString("file_path")));
						map.put("test_time", StringUtil.getStringValue(rs.getString("test_time")));
						map.put("file_time", StringUtil.getStringValue(rs.getString("file_time")));
						
						String is_qualified=StringUtil.getStringValue(rs.getString("is_qualified"));
						if (is_qualified.equals("1")) {
							map.put("is_qualified","是" );
						}else {
							map.put("is_qualified","否" );
						}
						
						String state=StringUtil.getStringValue(rs.getString("state"));
						map.put("state",state);
						if (state.equals("0")) {
							map.put("status","检测中" );
						}else if(state.equals("1")){
							map.put("status","完成" );
						}else {
							map.put("status","失败" );
						}
						
						return map;

					}
				});

		return list;
	}
	
	public int getCheckListCount(String account,
			String device_serialnumber,String vendor,String device_model,String hardwareversion,String softwareversion,String startTime,String endTime,
			int curPage_splitPage, int num_splitPage) {

		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append(" select count(*) from tab_check_project_log a ");
		}else{
			psql.append(" select count(1) from tab_check_project_log a ");
		}
		psql.append(" where 1=1 ");

		if (!StringUtil.IsEmpty(account)) {
			psql.append("   and a.acc_loginname = '");
			psql.append(account);
			psql.append("' ");
		}
		
		if (!StringUtil.IsEmpty(device_serialnumber)) {
			psql.append("   and a.device_serialnumber like '%"+device_serialnumber+"%'");
		}
		if (!StringUtil.IsEmpty(vendor) && !"-1".equals(vendor)) {
			psql.append("   and a.vendor_id = '");
			psql.append(vendor);
			psql.append("' ");
		}

		if (!StringUtil.IsEmpty(device_model) && !"-1".equals(device_model)) {
			psql.append("   and a.device_model_id = '");
			psql.append(device_model);
			psql.append("'");
		}

		if (!StringUtil.IsEmpty(hardwareversion) && !"-1".equals(hardwareversion)) {
			psql.append("   and a.hardwareversion ='"+hardwareversion+"' ");
		}
		if (!StringUtil.IsEmpty(softwareversion) && !"-1".equals(softwareversion)) {
			psql.append("   and a.softwareversion ='"+softwareversion+"' ");
		}
		if (!StringUtil.IsEmpty(startTime)) {
			psql.append("   and a.test_time >='"+startTime+"' ");
		}
		if (!StringUtil.IsEmpty(endTime)) {
			psql.append("   and a.test_time <'"+endTime+"' ");
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
	/**
	 * setDao 注入
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

	public void setWireInfoDao(WireInfoDAO wireInfoDao) {
		this.wireInfoDao = wireInfoDao;
	}

	public void setWanConnDao(WanConnDAO wanConnDao) {
		this.wanConnDao = wanConnDao;
	}

	public void setWanConnSessDao(WanConnSessDAO wanConnSessDao) {
		this.wanConnSessDao = wanConnSessDao;
	}

	public void setLanHostDao(LanHostDAO lanHostDao) {
		this.lanHostDao = lanHostDao;
	}

	public void setHgwCustDao(HgwCustDAO hgwCustDao) {
		this.hgwCustDao = hgwCustDao;
	}

	public void setHgwServUserDao(HgwServUserDAO hgwServUserDao) {
		this.hgwServUserDao = hgwServUserDao;
	}
	
}
