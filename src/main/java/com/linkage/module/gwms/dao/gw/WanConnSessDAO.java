package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.interf.dao.I_StrategyDAO;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class WanConnSessDAO extends SuperDAO implements I_StrategyDAO 
{
	private static Logger logger = LoggerFactory.getLogger(WanConnSessDAO.class);
	
	public WanConnSessObj[] queryDevWanConnSession(Map map) 
	{
		String device_id = StringUtil.getStringValue(map.get("device_id"));
		String wan_id =  StringUtil.getStringValue(map.get("wan_id"));
		String wan_conn_id =  StringUtil.getStringValue(map.get("wan_conn_id"));
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select dns_ipv6,ip_ipv6,ip_mode,vlan_id,gather_time,conn_trigger,");
			psql.append("backup_itfs,load_percent,work_mode,dial_num,ppp_auth_protocol,");
			psql.append("vlan_id,nat_enab,last_conn_error,conn_status,dns,dns_enab,");
			psql.append("gateway,mask,ip,ip_type,password,username,bind_port,");
			psql.append("serv_list,conn_type,name,enable,sess_type,wan_conn_sess_id,wan_conn_id,wan_id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(device_id));
		psql.append(" where device_id=? and wan_id=? and wan_conn_id=? ");
		psql.setString(1, device_id);
		psql.setStringExt(2, wan_id, false);
		psql.setStringExt(3, wan_conn_id, false);
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnSessObj[] wanConnSessObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnSessObj = new WanConnSessObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = null;
				if (null != rList.get(i)
						&& (rMap = (Map) rList.get(i)).isEmpty() == false) {
					wanConnSessObj[i] = new WanConnSessObj();
					wanConnSessObj[i].setWanId(String.valueOf(rMap.get("wan_id")));
					wanConnSessObj[i].setWanConnId(String.valueOf(rMap.get("wan_conn_id")));
					wanConnSessObj[i].setWanConnSessId(String.valueOf(rMap.get("wan_conn_sess_id")));
					// session type:PPP OR IP
					wanConnSessObj[i].setSessType(String.valueOf(rMap.get("sess_type")));
					wanConnSessObj[i].setEnable(String.valueOf(rMap.get("enable")));
					// 连接名称
					wanConnSessObj[i].setName(String.valueOf(rMap.get("name")));
					// 连接类型 ConnectionType IP_Routed or IP_Bridged
					wanConnSessObj[i].setConnType(String.valueOf(rMap.get("conn_type")));
					// X_CT-COM_ServiceList TR069 or INTERNET or TR069&INTERNET
					// or OTHER
					wanConnSessObj[i].setServList(String.valueOf(rMap.get("serv_list")));
					// 绑定端口
					wanConnSessObj[i].setBindPort(String.valueOf(rMap.get("bind_port")));
					// 用户账号
					wanConnSessObj[i].setUsername(String.valueOf(rMap.get("username")));
					// 账号密码
					wanConnSessObj[i].setPassword(String.valueOf(rMap.get("password")));
					// AddressingType: DHCP or Static
					wanConnSessObj[i].setIpType(String.valueOf(rMap.get("ip_type")));
					// ip
					wanConnSessObj[i].setIp(String.valueOf(rMap.get("ip")));
					// IP mask
					wanConnSessObj[i].setMask(String.valueOf(rMap.get("mask")));
					// gateway
					wanConnSessObj[i].setGateway(String.valueOf(rMap.get("gateway")));
					// dns_enab: 1:开启 0:未
					wanConnSessObj[i].setDnsEnab(String.valueOf(rMap.get("dns_enab")));
					// dns
					wanConnSessObj[i].setDns(String.valueOf(rMap.get("dns")));
					// 连接状态
					wanConnSessObj[i].setStatus(String.valueOf(rMap.get("conn_status")));
					// 拨号错误码
					wanConnSessObj[i].setConnError(String.valueOf(rMap.get("last_conn_error")));
					// NAT开关
					wanConnSessObj[i].setNatEnable(String.valueOf(rMap.get("nat_enab")));
					// "vpi/vci"
					/**
					if(wanConnObj.getVpi_id() != null && wanConnObj.getVci_id() != null)
					{
						wanConnSessObj[i].setPvc(wanConnObj.getVpi_id() + "/"
								+ wanConnObj.getVci_id());
					}
					else
					{
						wanConnSessObj[i].setPvc(wanConnObj.getVpi_id());
					}
					**/
					// "vlanid"
					wanConnSessObj[i].setVlanid(String.valueOf(rMap.get("vlan_id")));
					
					//PPPAuthenticationProtocol
					wanConnSessObj[i].setPppAuthProtocol(String.valueOf(rMap.get("ppp_auth_protocol")));
					
					//X_CT-COM_DialNumber
					wanConnSessObj[i].setDialNum(String.valueOf(rMap.get("dial_num")));
					
					//X_CT-COM_WorkMode
					wanConnSessObj[i].setWorkMode(String.valueOf(rMap.get("work_mode")));
					
					//X_CT-COM_LoadPercent
					wanConnSessObj[i].setLoadPercent(String.valueOf(rMap.get("load_percent")));
					
					//X_CT-COM_BackupInterface
					wanConnSessObj[i].setBackupItfs(String.valueOf(rMap.get("backup_itfs")));
					
					//ConnectionTrigger
					wanConnSessObj[i].setConnTrigger(String.valueOf(rMap.get("conn_trigger")));
					
					wanConnSessObj[i].setDeviceId(device_id);
					wanConnSessObj[i].setGatherTime(String.valueOf(rMap.get("gather_time")));
					
					// add by chenjie 2013-10-25 运营商信息
					if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
					{
						// CUC的vlanid在wanConnSess中
						wanConnSessObj[i].setVlanid(String.valueOf(rMap.get("vlan_id")));
					}
					wanConnSessObj[i].setIpMode(String.valueOf(rMap.get("ip_mode")));
					wanConnSessObj[i].setDns_ipv6(String.valueOf(rMap.get("ip_ipv6")));
					wanConnSessObj[i].setIp_ipv6(String.valueOf(rMap.get("dns_ipv6")));
				}
			}
		}
		return wanConnSessObj;

	
	}

	/**
	 * 取出特定(设备)WanConnection下面所有的Sesstion连接属性
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] queryDevWanConnSession(WanConnObj wanConnObj) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,wan_conn_id,wan_conn_sess_id,sess_type,enable,");
			psql.append("name,conn_type,serv_list,bind_port,username,password,ip_type,");
			psql.append("ip,mask,gateway,dns_enab,dns,conn_status,last_conn_error,");
			psql.append("nat_enab,ppp_auth_protocol,dial_num,work_mode,load_percent,backup_itfs,");
			psql.append("conn_trigger,gather_time,ip_mode,dns_ipv6,ip_ipv6,multicast_vlan,dhcp_enable ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+getTabName(wanConnObj.getDevice_id()));
		psql.append(" where device_id=? and wan_id=? and wan_conn_id=? ");
		psql.setString(1, wanConnObj.getDevice_id());
		psql.setStringExt(2, wanConnObj.getWan_id(), false);
		psql.setStringExt(3, wanConnObj.getWan_conn_id(), false);
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnSessObj[] wanConnSessObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnSessObj = new WanConnSessObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = null;
				if (null != rList.get(i)
						&& (rMap = (Map) rList.get(i)).isEmpty() == false) 
				{
					wanConnSessObj[i] = new WanConnSessObj();
					wanConnSessObj[i].setWanId(String.valueOf(rMap.get("wan_id")));
					wanConnSessObj[i].setWanConnId(String.valueOf(rMap.get("wan_conn_id")));
					wanConnSessObj[i].setWanConnSessId(String.valueOf(rMap.get("wan_conn_sess_id")));
					// session type:PPP OR IP
					wanConnSessObj[i].setSessType(String.valueOf(rMap.get("sess_type")));
					wanConnSessObj[i].setEnable(String.valueOf(rMap.get("enable")));
					// 连接名称
					wanConnSessObj[i].setName(String.valueOf(rMap.get("name")));
					// 连接类型 ConnectionType IP_Routed or IP_Bridged
					wanConnSessObj[i].setConnType(String.valueOf(rMap.get("conn_type")));
					// X_CT-COM_ServiceList TR069 or INTERNET or TR069&INTERNET
					// or OTHER
					wanConnSessObj[i].setServList(String.valueOf(rMap.get("serv_list")));
					// 绑定端口
					wanConnSessObj[i].setBindPort(String.valueOf(rMap.get("bind_port")));
					// 用户账号
					wanConnSessObj[i].setUsername(String.valueOf(rMap.get("username")));
					// 账号密码
					wanConnSessObj[i].setPassword(String.valueOf(rMap.get("password")));
					// AddressingType: DHCP or Static
					wanConnSessObj[i].setIpType(String.valueOf(rMap.get("ip_type")));
					// ip
					wanConnSessObj[i].setIp(String.valueOf(rMap.get("ip")));
					// IP mask
					wanConnSessObj[i].setMask(String.valueOf(rMap.get("mask")));
					// gateway
					wanConnSessObj[i].setGateway(String.valueOf(rMap.get("gateway")));
					// dns_enab: 1:开启 0:未
					wanConnSessObj[i].setDnsEnab(String.valueOf(rMap.get("dns_enab")));
					// dns
					wanConnSessObj[i].setDns(String.valueOf(rMap.get("dns")));
					// 连接状态
					wanConnSessObj[i].setStatus(String.valueOf(rMap.get("conn_status")));
					// 拨号错误码
					wanConnSessObj[i].setConnError(String.valueOf(rMap.get("last_conn_error")));
					// NAT开关
					wanConnSessObj[i].setNatEnable(String.valueOf(rMap.get("nat_enab")));
					// "vpi/vci"
					if(wanConnObj.getVpi_id() != null && wanConnObj.getVci_id() != null)
					{
						wanConnSessObj[i].setPvc(wanConnObj.getVpi_id() + "/"
								+ wanConnObj.getVci_id());
					}
					else
					{
						wanConnSessObj[i].setPvc(wanConnObj.getVpi_id());
					}
					// "vlanid"
					wanConnSessObj[i].setVlanid(wanConnObj.getVlan_id());
					
					//PPPAuthenticationProtocol
					wanConnSessObj[i].setPppAuthProtocol(String.valueOf(rMap.get("ppp_auth_protocol")));
					
					//X_CT-COM_DialNumber
					wanConnSessObj[i].setDialNum(String.valueOf(rMap.get("dial_num")));
					
					//X_CT-COM_WorkMode
					wanConnSessObj[i].setWorkMode(String.valueOf(rMap.get("work_mode")));
					
					//X_CT-COM_LoadPercent
					wanConnSessObj[i].setLoadPercent(String.valueOf(rMap.get("load_percent")));
					
					//X_CT-COM_BackupInterface
					wanConnSessObj[i].setBackupItfs(String.valueOf(rMap.get("backup_itfs")));
					
					//ConnectionTrigger
					wanConnSessObj[i].setConnTrigger(String.valueOf(rMap.get("conn_trigger")));
					
					wanConnSessObj[i].setDeviceId(wanConnObj.getDevice_id());
					wanConnSessObj[i].setGatherTime(String.valueOf(rMap.get("gather_time")));
					
					// add by chenjie 2013-10-25 运营商信息
//					if(LipossGlobals.getLipossProperty("telecom").equals(Global.TELECOM_CUC))
//					{
//						wanConnSessObj[i].setVlanid(String.valueOf(rMap.get("vlan_id")));
//					}
					
					wanConnSessObj[i].setIpMode(String.valueOf(rMap.get("ip_mode")));
					wanConnSessObj[i].setDns_ipv6(String.valueOf(rMap.get("dns_ipv6")));
					wanConnSessObj[i].setIp_ipv6(String.valueOf(rMap.get("ip_ipv6")));
					if(LipossGlobals.inArea(Global.HBLT))
					{
						wanConnSessObj[i].setMulticast_vlan(StringUtil.getStringValue(rMap, "multicast_vlan"));
					}
					if(LipossGlobals.inArea(Global.JSDX)||LipossGlobals.inArea(Global.CQDX))
					{
						wanConnSessObj[i].setMulticast_vlan(StringUtil.getStringValue(rMap, "multicast_vlan"));
						
						PrepareSQL sql2 = new PrepareSQL("select snooping_enable from gw_iptv where device_id=? ");
						sql2.setString(1, wanConnObj.getDevice_id());
						// 执行查询
						List list = jt.queryForList(sql2.getSQL());
						String snooping_enable = "";
						if(null!=list && list.size()!=0){
							snooping_enable = StringUtil.getStringValue((Map)list.get(0), "snooping_enable", "");
							logger.warn("{}的snooping_enable={}",
									new Object[]{wanConnObj.getDevice_id(), snooping_enable});
						}
						
						if("".equals(snooping_enable)){
							snooping_enable = "";
						}
						else if("1".equals(snooping_enable)){
							snooping_enable = "true";
						}
						else{
							snooping_enable = "false";
						}
						
						wanConnSessObj[i].setSnooping_enable(snooping_enable);
						String dhcp_status = StringUtil.getStringValue(rMap.get("dhcp_enable"));
						logger.warn("=======dhcp_status======{}",dhcp_status);
						if(dhcp_status.equals("1")){
							wanConnSessObj[i].setDhcp_status("开启");
						}else if(dhcp_status.equals("0")){
							wanConnSessObj[i].setDhcp_status("关闭");
						}else{
							wanConnSessObj[i].setDhcp_status("-");
						}
					}
				}
			}
		}
		return wanConnSessObj;
	}
	
	/**
	 * 获取WanConnSessObj[], 服务列表包含"INTERNET or BOTH",并且连接状态为"Connecting"
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-7-16
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] getConntioningSess(String deviceId)
	{
		logger.debug("getConntioningSess({deviceId})", deviceId);
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select wan_id,wan_conn_id,wan_conn_sess_id,sess_type,enable,");
			psql.append("password,ip_type,name,conn_type,serv_list,bind_port,username,");
			psql.append("ip,mask,gateway,dns_enab,dns,conn_status,last_conn_error,nat_enab,");
			psql.append("ppp_auth_protocol,dial_num,work_mode,load_percent,backup_itfs,");
			psql.append("conn_trigger,gather_time,ip_mode,ip_ipv6,dns_ipv6,vpi_id,vci_id,vlan_id ");
		}else{
			psql.append("select a.*,b.vpi_id,b.vci_id,b.vlan_id ");
		}
		psql.append("from gw_wan_conn_session"+ Global.getSuffixName(gw_type));
		psql.append(" a,gw_wan_conn" + Global.getSuffixName(gw_type)+" b ");
		psql.append("where a.device_id=? and a.conn_status='Connected' ");
		psql.append("and (a.serv_list like '%INTERNET%' or a.serv_list='BOTH') ");
		psql.append("and a.device_id=b.device_id and a.wan_id=b.wan_id and a.wan_conn_id=b.wan_conn_id ");
		
		psql.setString(1, deviceId);
//		psql.setString(2, "%INTERNET%");
//		psql.setString(3, "Connected");
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnSessObj[] wanConnSessObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnSessObj = new WanConnSessObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = null;
				if (null != rList.get(i)
						&& (rMap = (Map) rList.get(i)).isEmpty() == false) 
				{
					wanConnSessObj[i] = new WanConnSessObj();
					wanConnSessObj[i].setWanId(String.valueOf(rMap.get("wan_id")));
					wanConnSessObj[i].setWanConnId(String.valueOf(rMap.get("wan_conn_id")));
					wanConnSessObj[i].setWanConnSessId(String.valueOf(rMap.get("wan_conn_sess_id")));
					// session type:PPP OR IP
					wanConnSessObj[i].setConnType(String.valueOf(rMap.get("sess_type")));
					wanConnSessObj[i].setEnable(String.valueOf(rMap.get("enable")));
					// 连接名称
					wanConnSessObj[i].setName(String.valueOf(rMap.get("name")));
					// 连接类型 ConnectionType IP_Routed or IP_Bridged
					wanConnSessObj[i].setConnType(String.valueOf(rMap.get("conn_type")));
					// X_CT-COM_ServiceList TR069 or INTERNET or TR069&INTERNET
					// or OTHER
					wanConnSessObj[i].setServList(String.valueOf(rMap.get("serv_list")));
					// 绑定端口
					wanConnSessObj[i].setBindPort(String.valueOf(rMap.get("bind_port")));
					// 用户账号
					wanConnSessObj[i].setUsername(String.valueOf(rMap.get("username")));
					// 账号密码
					wanConnSessObj[i].setPassword(String.valueOf(rMap.get("password")));
					// AddressingType: DHCP or Static
					wanConnSessObj[i].setIpType(String.valueOf(rMap.get("ip_type")));
					// ip
					wanConnSessObj[i].setIp(String.valueOf(rMap.get("ip")));
					// IP mask
					wanConnSessObj[i].setMask(String.valueOf(rMap.get("mask")));
					// gateway
					wanConnSessObj[i].setGateway(String.valueOf(rMap.get("gateway")));
					// dns_enab: 1:开启 0:未
					wanConnSessObj[i].setDnsEnab(String.valueOf(rMap.get("dns_enab")));
					// dns
					wanConnSessObj[i].setDns(String.valueOf(rMap.get("dns")));
					// 连接状态
					wanConnSessObj[i].setStatus(String.valueOf(rMap.get("conn_status")));
					// 拨号错误码
					wanConnSessObj[i].setConnError(String.valueOf(rMap.get("last_conn_error")));
					// NAT开关
					wanConnSessObj[i].setNatEnable(String.valueOf(rMap.get("nat_enab")));
					
					//PPPAuthenticationProtocol
					wanConnSessObj[i].setPppAuthProtocol(String.valueOf(rMap.get("ppp_auth_protocol")));
					
					//X_CT-COM_DialNumber
					wanConnSessObj[i].setDialNum(String.valueOf(rMap.get("dial_num")));
					
					//X_CT-COM_WorkMode
					wanConnSessObj[i].setWorkMode(String.valueOf(rMap.get("work_mode")));
					
					//X_CT-COM_LoadPercent
					wanConnSessObj[i].setLoadPercent(String.valueOf(rMap.get("load_percent")));
					
					//X_CT-COM_BackupInterface
					wanConnSessObj[i].setBackupItfs(String.valueOf(rMap.get("backup_itfs")));
					
					//ConnectionTrigger
					wanConnSessObj[i].setConnTrigger(String.valueOf(rMap.get("conn_trigger")));
					
					wanConnSessObj[i].setDeviceId(deviceId);
					wanConnSessObj[i].setGatherTime(String.valueOf(rMap.get("gather_time")));
					wanConnSessObj[i].setIpMode(String.valueOf(rMap.get("ip_mode")));
					wanConnSessObj[i].setDns_ipv6(String.valueOf(rMap.get("ip_ipv6")));
					wanConnSessObj[i].setIp_ipv6(String.valueOf(rMap.get("dns_ipv6")));
					//PVC和VLAN
					wanConnSessObj[i].setPvc(StringUtil.getStringValue(rMap.get("vpi_id") + "/"
							+ StringUtil.getStringValue(rMap.get("vci_id"))));
					wanConnSessObj[i].setVlanid(StringUtil.getStringValue(rMap.get("vlan_id")));
				}
			}
		}
		return wanConnSessObj;
	}
	
	
	/**
	 * 取出设备所有的Session连接属性
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return WanConnSessObj[] 返回设备上所有Session的对象数组，没有则返回null 
	 */
	public WanConnSessObj[] queryAllConnSession(String deviceId) 
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("wan_id,wan_conn_id,wan_conn_sess_id,sess_type,enable,");
			psql.append("name,conn_type,serv_list,bind_port,username,password,ip_type,");
			psql.append("ip,mask,gateway,dns_enab,dns,conn_status,last_conn_error,");
			psql.append("nat_enab,ppp_auth_protocol,dial_num,work_mode,load_percent,");
			psql.append("backup_itfs,conn_trigger,gather_time,ip_mode,ip_ipv6,dns_ipv6 ");
		}else{
			psql.append("select * ");
		}
		psql.append("from " + getTabName(deviceId) + " where device_id=? ");
		psql.setString(1, deviceId);
		// 执行查询
		List rList = jt.queryForList(psql.getSQL());
		// WanConnObj数组，存储结果
		WanConnSessObj[] wanConnSessObj = null;
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wanConnSessObj = new WanConnSessObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = null;
				if (null != rList.get(i)
						&& (rMap = (Map) rList.get(i)).isEmpty() == false) {
					wanConnSessObj[i] = new WanConnSessObj();
					wanConnSessObj[i].setWanId(String.valueOf(rMap.get("wan_id")));
					wanConnSessObj[i].setWanConnId(String.valueOf(rMap.get("wan_conn_id")));
					wanConnSessObj[i].setWanConnSessId(String.valueOf(rMap.get("wan_conn_sess_id")));
					// session type:PPP OR IP
					wanConnSessObj[i].setSessType(String.valueOf(rMap.get("sess_type")));
					wanConnSessObj[i].setEnable(String.valueOf(rMap.get("enable")));
					// 连接名称
					wanConnSessObj[i].setName(String.valueOf(rMap.get("name")));
					// 连接类型 ConnectionType IP_Routed or IP_Bridged
					wanConnSessObj[i].setConnType(String.valueOf(rMap.get("conn_type")));
					// X_CT-COM_ServiceList TR069 or INTERNET or TR069&INTERNET
					// or OTHER
					wanConnSessObj[i].setServList(String.valueOf(rMap.get("serv_list")));
					// 绑定端口
					wanConnSessObj[i].setBindPort(String.valueOf(rMap.get("bind_port")));
					// 用户账号
					wanConnSessObj[i].setUsername(String.valueOf(rMap.get("username")));
					// 账号密码
					wanConnSessObj[i].setPassword(String.valueOf(rMap.get("password")));
					// AddressingType: DHCP or Static
					wanConnSessObj[i].setIpType(String.valueOf(rMap.get("ip_type")));
					// ip
					wanConnSessObj[i].setIp(String.valueOf(rMap.get("ip")));
					// IP mask
					wanConnSessObj[i].setMask(String.valueOf(rMap.get("mask")));
					// gateway
					wanConnSessObj[i].setGateway(String.valueOf(rMap.get("gateway")));
					// dns_enab: 1:开启 0:未
					wanConnSessObj[i].setDnsEnab(String.valueOf(rMap.get("dns_enab")));
					// dns
					wanConnSessObj[i].setDns(String.valueOf(rMap.get("dns")));
					// 连接状态
					wanConnSessObj[i].setStatus(String.valueOf(rMap.get("conn_status")));
					// 拨号错误码
					wanConnSessObj[i].setConnError(String.valueOf(rMap.get("last_conn_error")));
					// NAT开关
					wanConnSessObj[i].setNatEnable(String.valueOf(rMap.get("nat_enab")));

					//PPPAuthenticationProtocol
					wanConnSessObj[i].setPppAuthProtocol(String.valueOf(rMap.get("ppp_auth_protocol")));
					
					//X_CT-COM_DialNumber
					wanConnSessObj[i].setDialNum(String.valueOf(rMap.get("dial_num")));
					
					//X_CT-COM_WorkMode
					wanConnSessObj[i].setWorkMode(String.valueOf(rMap.get("work_mode")));
					
					//X_CT-COM_LoadPercent
					wanConnSessObj[i].setLoadPercent(String.valueOf(rMap.get("load_percent")));
					
					//X_CT-COM_BackupInterface
					wanConnSessObj[i].setBackupItfs(String.valueOf(rMap.get("backup_itfs")));
					
					//ConnectionTrigger
					wanConnSessObj[i].setConnTrigger(String.valueOf(rMap.get("conn_trigger")));
					
					wanConnSessObj[i].setDeviceId(deviceId);
					wanConnSessObj[i].setGatherTime(String.valueOf(rMap.get("gather_time")));
					wanConnSessObj[i].setIpMode(String.valueOf(rMap.get("ip_mode")));
					wanConnSessObj[i].setDns_ipv6(String.valueOf(rMap.get("ip_ipv6")));
					wanConnSessObj[i].setIp_ipv6(String.valueOf(rMap.get("dns_ipv6")));
				}
			}
		}
		return wanConnSessObj;

	}
	
	/**
	 * add by chenjie 2012-10-26 
	 * 采集表gw_wan_conn_session区分ITMS/BBMS
	 * @param deviceId
	 * @return
	 */
	public String getTabName(String deviceId)
	{
		String tabName = "gw_wan_conn_session";
		return Global.getTabName(deviceId, tabName);
	}
}
