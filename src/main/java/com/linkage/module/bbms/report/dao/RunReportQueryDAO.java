package com.linkage.module.bbms.report.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class RunReportQueryDAO extends SuperDAO{

	private static Logger logger = LoggerFactory.getLogger(RunReportQueryDAO.class);
	/**
	 *
	 * @param userName 用户帐号
	 * @param deviceSerialnumber  设备序列号
	 * @param deviceIP   设备IP
	 * @return Map<String,String> 网关基本信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryGatewayBaseInfo(String userName,
			String deviceSerialnumber, String deviceIP) {

		logger.debug("queryGatewayBaseInfo({},{},{})", new Object[]{userName, deviceSerialnumber, deviceIP});

		PrepareSQL sb = new PrepareSQL();// TODO wait (more table related)
		logger.warn("DAO:deviceSerialnumber:="+deviceSerialnumber);
		sb.append(" select  a.device_serialnumber,a.oui, a.cpe_mac,a.loopback_ip,a.city_id, ");
		sb.append("  c.vendor_name,d.device_model,a.device_type,b.softwareversion  \n");
		sb.append(" from 	tab_gw_device a, tab_devicetype_info b,tab_vendor c, gw_device_model d ");
		sb.append(" where a.vendor_id=c.vendor_id  \n  and a.devicetype_id=b.devicetype_id ");
		sb.append(" and a.device_model_id=d.device_model_id \n");
		if(!StringUtil.IsEmpty(deviceSerialnumber)){
			sb.append(" and a.device_serialnumber = '"+deviceSerialnumber+"'");
		}else{
			if(!StringUtil.IsEmpty(deviceIP)){
				sb.append(" and a.loopback_ip = '" );
				sb.append(deviceIP);
				sb.append("'");
			}
		}

		logger.warn("获取网关基本信息sql:"+sb.getSQL());
		return jt.queryForList(sb.getSQL());
	}
	/**
	 *
	 * @param deviceId 设备ID
	 * @return  网关业务信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryGatewayService(String deviceId) {

		List<Map> returnNetServLt = new ArrayList<Map>();
		logger.warn("查询网关业务信息RunReportQueryDAO.queryGatewayService(),deviceId="+deviceId);
		PrepareSQL sb = new PrepareSQL();
		sb.append("  select  a.user_id,a.username,a.city_id,b.serv_type_id,b.dealdate,b.opendate,b.open_status ");
		sb.append("   from tab_egwcustomer a left join egwcust_serv_info b on a.user_id=b.user_id ");
		sb.append("  where 1=1 ");
		if(!StringUtil.IsEmpty(deviceId)){
			sb.append(" and  a.device_id='"+deviceId+"'");
		}
		logger.warn("查询网关业务信息sql:"+sb.getSQL());
		List<Map> netServLt  = jt.queryForList(sb.getSQL());
		//查询业务类型与业务Id
		PrepareSQL	sql = new PrepareSQL(" select serv_type_id,serv_type_name from tab_gw_serv_type");
		logger.debug("查tab_gw_serv_type表信息:"+sql.getSQL());
		List<Map<String,Object>> servLt = jt.queryForList(sql.getSQL());
		//遍历业务类型
		Map<String,Object> tempMap1 = null;

		if(null!=netServLt&&netServLt.size()>0){
			for(int i = 0 ;i< netServLt.size();i++){
				Map<String,Object> tempNetMap = new HashMap<String,Object>();
				   tempNetMap.put("user_id", netServLt.get(i).get("user_id"));
				   tempNetMap.put("username", netServLt.get(i).get("username"));
				   tempNetMap.put("serv_type_id", netServLt.get(i).get("serv_type_id"));
				   handleTime(tempNetMap,netServLt.get(i).get("dealdate"));
				   tempNetMap.put("open_status", netServLt.get(i).get("open_status"));

				   if(null!=servLt&&servLt.size()>0){
						for(int j = 0 ;j < servLt.size(); j++){
							tempMap1 = servLt.get(j);
							if(null!=tempMap1.get("serv_type_id")&&tempMap1.get("serv_type_id").equals(netServLt.get(i).get("serv_type_id"))){
								tempNetMap.put("serv_type_name", tempMap1.get("serv_type_name"));
								break;
							}
						}
					}
				   returnNetServLt.add(tempNetMap);
			}
		}
		return  returnNetServLt;
	}
	private void handleTime(Map<String,Object> map, Object time) {
		String tempTime ="";
		if (null != time ) {
			tempTime = DateUtil.format(Long.parseLong(time.toString()), "yyyy-MM-dd hh:mm:ss");
			map.put("dealdate", tempTime);

		}else{
			map.put("dealdate", null);
		}

	}
	/**
	 *
	 * @param deviceID 设备Id
	 * @return 网关告警信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryGatewayWarnInfo(String deviceId){
		logger.debug("查询网关告警信息RunReportQueryDAO.queryGatewayWarnInfo()");
		List<Map> returnList = new ArrayList<Map>();
		Map returnMap = new HashMap<String,Object>();
		List<Map> tempList = null;
		Map tempMap = null;

		PrepareSQL sb = new PrepareSQL();
		sb.append(" select type ,mlevel ,status ,possiblereason , \n" );
		sb.append(" occurtime ,cleartime ,clearsuggestion   \n");
		sb.append(" from tab_alarm where  1=1 ");
		if(!StringUtil.IsEmpty(deviceId)){
			sb.append(" and device_id='"+deviceId+"'");
		}
		logger.debug("查询网关告警信息sql:"+sb.toString());
		tempList = jt.queryForList(sb.getSQL());
		if(null!=tempList && tempList.size() >  0 ){
			for( int i = 0; i<tempList.size(); i++){
				tempMap =  tempList.get(i);
				Long occurTime = Long.valueOf(String.valueOf(tempMap.get("occurtime")));
				Long clearTime = StringUtil.getLongValue(tempMap.get("cleartime"));

				returnMap.put("type", tempMap.get("type"));
				returnMap.put("mlevel", tempMap.get("mlevel"));
				returnMap.put("status", tempMap.get("status"));
				returnMap.put("possiblereason", tempMap.get("possiblereason"));
				returnMap.put("occurtime", new DateTimeUtil(occurTime*1000).getLongDate());
				returnMap.put("cleartime", new DateTimeUtil(clearTime*1000).getLongDate());
				returnMap.put("clearsuggestion", tempMap.get("clearsuggestion"));
				returnList.add(returnMap);
			}
		}
		return  returnList;

	}
	/**
	 *
	 * @param deviceId 设备Id
	 * @return 网关流量信息表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> queryGatewayFluxInfo(String deviceId) {
		List<Map> returnList = new ArrayList<Map>();
		logger.debug("查询流量信息RunReportQueryDAO.queryGatewayFluxInfo");

		PrepareSQL sb = new PrepareSQL();
		sb.append(" select ifinoctetsbpsmax  , ifoutoctetsbpsmax  , \n ");
		sb.append(" ifinoctbps_avgmax  , ifoutoctbps_avgmax  , \n");
		sb.append(" round(ifinoctbps_avgmax/ifoutoctbps_avgmax*100,2)   avgmax_rate  \n");
		sb.append(" from flux_day_stat_y_m  where 1=1 \n");   //日报表
		if(!StringUtil.IsEmpty(deviceId)){
			sb.append(" and device_id='"+deviceId+"'");
		}
		List<Map> tempList =  jt.queryForList(sb.getSQL());
		if(null != tempList && tempList.size() > 0){
			Map returnMap = new HashMap();
			Map tempMap ;
			for( int i = 0; i < tempList.size();i++){
				tempMap  = tempList.get(i);
				returnMap.put("ifinoctetsbpsmax", tempMap.get("ifinoctetsbpsmax"));
				returnMap.put("ifoutoctetsbpsmax", tempMap.get("ifoutoctetsbpsmax"));
				returnMap.put("ifinoctbps_avgmax", tempMap.get("ifinoctbps_avgmax"));
				returnMap.put("ifoutoctbps_avgmax", tempMap.get("ifoutoctbps_avgmax"));

				double avgmaxRate = StringUtil.getDoubleValue(tempMap.get("avgmax_rate"));
				returnMap.put("avgmax_rate", avgmaxRate);
				returnList.add(returnMap);
			}
		}
		return returnList;
	}
	/**
	 *
	 * @param deviceId设备号
	 * @return 是否可以访问控制
	 */
	public String getByProperty(String deviceId) {
		PrepareSQL sql = new PrepareSQL("select enable from gw_sec_access_control_bbms where device_id='"+deviceId+"'");
		Map map = queryForMap(sql.getSQL());
		if(null!=map){
			return StringUtil.getStringValue(map.get("enable"));
		}
		return "";
	}
	/**
	 *
	 * @param deviceId 设备号
	 * @return 内容过滤(WEB,文件，日志)
	 */
	public Map getFilterContent(String deviceId) {
		PrepareSQL sql = new PrepareSQL("select http_filter_enabled,file_filter_enable,log_enable from gw_sec_content_filter_bbms where device_id='"+deviceId+"'");
		logger.debug("内功过滤SQL(WEB,文件，日志)："+sql.getSQL());
		return queryForMap(sql.getSQL());
	}
	/**
	 *
	 * @param deviceId 设备号
	 * @return 内容过滤(接收邮件)
	 */
	public Map getFilterMail(String deviceId) {
		PrepareSQL sql = new PrepareSQL("select smtp_filter_enabled,pop3_filter_enabled  from gw_sec_mail_filter_bbms where device_id='"+deviceId+"'");
		logger.debug("内容过滤SQL(接受邮件)："+sql.getSQL());
		return queryForMap(sql.getSQL());
	}
	/**
	 *
	 * @param deviceId 设备号
	 * @return  String 防入侵状态
	 */
	public String getNetSecurity(String deviceId) {

		PrepareSQL sql = new PrepareSQL("select enable  from gw_sec_intrusion_detect_bbms where device_id='"+deviceId+"'");
		Map map = queryForMap(sql.getSQL());
		if(null!=map){
			return StringUtil.getStringValue(map.get("enable"));
		}
		return "";
	}
	/**
	 *
	 * @param deviceId 设备号
	 * @return  String 防病毒状态
	 */
	public String getSerVir(String deviceId) {

		PrepareSQL sql = new PrepareSQL("select enable  from gw_sec_antivirus_bbms where device_id='"+deviceId+"'");
		Map map = queryForMap(sql.getSQL());
		if(null!=map){
			return StringUtil.getStringValue(map.get("enable"));
		}
		return "";
	}
	/**
	 *
	 * @param deviceId 设备号
	 * @return LAN口的信息
	 */
	public List<Map<String,Object>> getLanInfo(String deviceId) {
		PrepareSQL sql = new PrepareSQL(" select lan_id,lan_eth_id, enable,status,mac_address,max_bit_rate,byte_sent,byte_rece,pack_sent,pack_rece from gw_lan_eth_bbms where device_id ='"+deviceId+"'");
		logger.debug("LAN口信息语句:"+sql.getSQL());
		return jt.queryForList(sql.getSQL());
	}
	/**
	 *
	 * @param deviceId
	 * 信道为当前信道
	 * @return
	 */
	public List<Map<String, Object>> getWlanInfo(String deviceId) {
		PrepareSQL sql = new PrepareSQL(" select lan_id,  lan_wlan_id, ssid,ap_enable,hide, powerlevel ,channel_in_use,total_bytes_sent,total_bytes_received,total_packets_sent,total_packets_received ");
		sql.append("from gw_lan_wlan_bbms where device_id ='"+deviceId+"'");

		logger.debug("查询gw_lan_wlan信息语句:"+sql.getSQL());
		return jt.queryForList(sql.getSQL());
	}
	public List<Map<String,Object>> getWanLt(String deviceId) {
		logger.debug("获取WAN口信息 RunReportQueryDAO.getWanLt(String deviceId)");

		List<Map<String,Object>> wanInfoList = new ArrayList<Map<String,Object>>();
		Map<String,Object>  wanInfoMap = null;

		PrepareSQL sql = new PrepareSQL(" select wan_id ,access_type from gw_wan where device_id ='"+deviceId+"'");

		logger.debug("查询gw_wan表信息："+sql.getSQL());

		List<Map<String,Object>> wanLt = jt.queryForList(sql.getSQL());


		if(null != wanLt&&wanLt.size() > 0){
			String wanId = wanLt.get(0).get("wan_id").toString();
			sql = new PrepareSQL(" select wan_conn_id,vpi_id,vci_id,vlan_id,link_status from gw_wan_conn_bbms where device_id ='"+deviceId+"' and wan_id = "+Integer.parseInt(wanId));
			logger.debug("查询gw_wan表信息:"+sql.getSQL());
			List<Map<String,Object>> wanConnLt = jt.queryForList(sql.getSQL());
			if(null != wanConnLt&&wanConnLt.size() > 0){
				//遍历 wan_conn_id
				for(int i = 0; i< wanConnLt.size(); i++){
					int wanConnId = StringUtil.getIntegerValue(wanConnLt.get(i).get("wan_conn_id"));
					sql = new PrepareSQL(" select  wan_conn_sess_id,serv_list,ip,ip_type,conn_type,username,password from gw_wan_conn_session where device_id ='"+deviceId+"' and wan_id = "+Integer.parseInt(wanId)+" and wan_conn_id="+wanConnId);

					logger.warn("查询gw_wan_conn_session表信息:"+sql.getSQL());

					List<Map<String,Object>> wanSesLt =  jt.queryForList(sql.getSQL());
					if(null!=wanSesLt){
						for(int j = 0 ; j <wanSesLt.size(); j++){
							wanInfoMap = new HashMap<String,Object>();
						    wanInfoMap.put("access_type", wanLt.get(0).get("access_type"));

							wanInfoMap.put("wan_conn_id",wanConnLt.get(i).get("wan_conn_id"));
							wanInfoMap.put("link_status",wanConnLt.get(i).get("link_status"));
							wanInfoMap.put("vpi_id",wanConnLt.get(i).get("vpi_id"));
							wanInfoMap.put("vci_id",wanConnLt.get(i).get("vci_id"));
							wanInfoMap.put("vlan_id",wanConnLt.get(i).get("vlan_id"));

							wanInfoMap.put("wan_conn_sess_id", wanSesLt.get(j).get("wan_conn_sess_id"));
							wanInfoMap.put("serv_list", wanSesLt.get(j).get("serv_list"));
							wanInfoMap.put("ip", wanSesLt.get(j).get("ip"));
							wanInfoMap.put("ip_type", wanSesLt.get(j).get("ip_type"));
							wanInfoMap.put("conn_type", wanSesLt.get(j).get("conn_type"));
							wanInfoMap.put("username", wanSesLt.get(j).get("username"));
							wanInfoMap.put("password", wanSesLt.get(j).get("password"));
							wanInfoList.add(wanInfoMap);
						}
					}
				}
			}


		}
		return wanInfoList;
	}
	/**
	 *
	 * @param deviceId 设备ID
	 * @return Map<String,Oject> 防火墙信息
	 */
    public Map<String,Object> getFireWallInfo(String deviceId){
    	PrepareSQL sql = new PrepareSQL(" select enable,firewall_level from gw_broadcom_firewall_bbms where device_id = '"+deviceId+"'");
    	return queryForMap(sql.getSQL());
    }
    /**
     *
     * @param deviceId 设备的ID 查询gw_wan口第一个，gw_wan_conn，gw_wan_conn_session，gw_wan_conn_session_vpn的第一个记录
     * @return
     */
    public Map getVPNInfo(String deviceId){
		PrepareSQL sql = new PrepareSQL(" select wan_id from gw_wan where device_id ='"+deviceId+"'");
		logger.debug("查询gw_wan表信息："+sql.getSQL());
		List<Map<String,Object>> wanLt = jt.queryForList(sql.getSQL());
		if(null != wanLt&&wanLt.size() > 0){
			String wanId = wanLt.get(0).get("wan_id").toString();
			sql = new PrepareSQL(" select wan_conn_id  from gw_wan_conn_bbms where device_id ='"+deviceId+"' and wan_id = "+Integer.parseInt(wanId));
			List<Map<String,Object>> wanConnLt = jt.queryForList(sql.getSQL());

			if(null != wanConnLt&&wanConnLt.size() > 0){
				int wanConnId = StringUtil.getIntegerValue(wanConnLt.get(0).get("wan_conn_id"));
				sql = new PrepareSQL(" select wan_conn_sess_id ,sess_type from  gw_wan_conn_session where device_id ='"+deviceId+"'");
				sql.append(" and wan_id = "+Integer.parseInt(wanId));
				sql.append(" and wan_conn_id = " +wanConnId);
				List<Map<String,Object>> wanConnSess = jt.queryForList(sql.getSQL());

				if (null != wanConnSess&&wanConnSess.size() > 0 ){
					int wanConnSessId = StringUtil.getIntegerValue(wanConnSess.get(0).get("wan_conn_sess_id"));
					int sessType = StringUtil.getIntegerValue(wanConnSess.get(0).get("sess_type"));
					sql = new PrepareSQL(" select enable,type from gw_wan_conn_session_vpn_bbms where device_id ='"+deviceId+"'");
					sql.append(" and wan_id = "+Integer.parseInt(wanId));
					sql.append(" and wan_conn_id = " +wanConnId);
					sql.append(" and wan_conn_sess_id="+wanConnSessId);
					sql.append(" and sess_type="+sessType);
					return (Map) jt.queryForList(sql.getSQL()).get(0);
				}
			}
		}
		return null;
    }
}
