package com.linkage.module.gwms.dao.gw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.WlanOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * dao for wlan config.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class WlanDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(WlanDAO.class);

	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	
	
	/**
	 * 获取WLAN
	 */
	public List getData(String deviceId)
	{
		if (deviceId == null){
			logger.warn("deviceId == null");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select device_id,gather_time,lan_id,lan_wlan_id,");
			psql.append("ap_enable,powerlevel,powervalue,enable,ssid,standard,");
			psql.append("beacontype,basic_auth_mode,wep_encr_level,");
			psql.append("wep_key,wpa_auth_mode,wpa_encr_mode,wpa_key,radio_enable,");
			psql.append("hide,poss_channel,channel,channel_in_use,status,wps_key_word,");
			psql.append("associated_num,ieee_auth_mode,ieee_encr_mode from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(Global.getTabName(deviceId,"gw_lan_wlan") + " where device_id=? ");
		psql.setString(1, deviceId);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取一个WLAN
	 */
	public Map getWlan(String deviceId, String lanId, String wlanId)
	{
		if (deviceId == null){
			logger.warn("deviceId == null");
			return null;
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select ap_enable,basic_auth_mode,powerlevel,powervalue,");
			psql.append("enable,ssid,standard,beacontype,wep_encr_level,wep_key,");
			psql.append("wpa_auth_mode,wpa_encr_mode,wpa_key,radio_enable,hide,");
			psql.append("poss_channel,channel,channel_in_use,status,");
			psql.append("associated_num,ieee_auth_mode,ieee_encr_mode from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(Global.getTabName(deviceId,"gw_lan_wlan"));
		psql.append(" where device_id=? and lan_id=" + lanId + " and lan_wlan_id=" + wlanId);
		psql.setString(1, deviceId);
		return queryForMap(psql.getSQL());
	}
	
	/**
	 * 获取一个WLAN
	 */
	public List getWlanBbms(String deviceId, String lanId, String wlanId)
	{
		if (deviceId == null){
			logger.warn("deviceId == null");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select lan_id,lan_wlan_id,ipaddress from ");
		}else{
			psql.append("select * from ");
		}
		psql.append("gw_lan_wlan_bbms where device_id='" + deviceId + "' ");
		if(null!=lanId){
			psql.append(" and lan_id=" + lanId );
		}
		if(null!=wlanId){
			psql.append(" and lan_wlan_id=" + wlanId );
		}
		
		return jt.queryForList(psql.getSQL());
	}
	
	public List getWlanAsso(String deviceId)
	{
		if(StringUtil.IsEmpty(deviceId)){
			logger.warn("deviceId == null");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			//无引用的方法
		} */
		psql.append("select * from gw_wlan_asso ");
		psql.append("where device_id=? and lan_id=? and lan_wlan_id=? ");
		psql.setString(1, deviceId);
		psql.setInt(2, 1);
		psql.setInt(3, 1);
		return jt.queryForList(psql.getSQL());
	}

	public List getWlanAsso_bbms(String deviceId)
	{
		if(StringUtil.IsEmpty(deviceId)){
			logger.warn("deviceId == null");
			return null;
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select lan_id,lan_wlan_id,ip_address ");
		}else{
			psql.append("select * ");
		}
		psql.append("from gw_wlan_asso_bbms where device_id=? ");
		psql.setString(1, deviceId);
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取一个WLAN对象
	 */
	public WlanOBJ getWlanObj(String deviceId, String lanId, String wlanId)
	{
		WlanOBJ wlanObj = null;
		Map wlanMap = getWlan(deviceId, lanId, wlanId);
		if (null != wlanMap)
		{
			wlanObj = new WlanOBJ();
			wlanObj.setDeviceId(deviceId);
			wlanObj.setLanId(lanId);
			wlanObj.setLanWlanId(wlanId);
			wlanObj.setApEnable(StringUtil.getStringValue(wlanMap.get("ap_enable")));
			wlanObj.setBasicAuthMode(StringUtil.getStringValue(wlanMap.get("basic_auth_mode")));
			wlanObj.setPowerlevel(StringUtil.getStringValue(wlanMap.get("powerlevel")));
			wlanObj.setPowervalue(StringUtil.getStringValue(wlanMap.get("powervalue")));
			wlanObj.setEnable(StringUtil.getStringValue(wlanMap.get("enable")));
			wlanObj.setSsid(StringUtil.getStringValue(wlanMap.get("ssid")));
			wlanObj.setStandard(StringUtil.getStringValue(wlanMap.get("standard")));
			wlanObj.setBeacontype(StringUtil.getStringValue(wlanMap.get("beacontype")));
			wlanObj.setWepEncrLevel(StringUtil.getStringValue(wlanMap.get("wep_encr_level")));
			wlanObj.setWepKey(StringUtil.getStringValue(wlanMap.get("wep_key")));
			wlanObj.setWpaAuthMode(StringUtil.getStringValue(wlanMap.get("wpa_auth_mode")));
			wlanObj.setWpaEncrMode(StringUtil.getStringValue(wlanMap.get("wpa_encr_mode")));
			wlanObj.setWpaKey(StringUtil.getStringValue(wlanMap.get("wpa_key")));
			wlanObj.setRadioEnable(StringUtil.getStringValue(wlanMap.get("radio_enable")));
			wlanObj.setHide(StringUtil.getStringValue(wlanMap.get("hide")));
			wlanObj.setPossChannel(StringUtil.getStringValue(wlanMap.get("poss_channel")));
			wlanObj.setChannel(StringUtil.getStringValue(wlanMap.get("channel")));
			wlanObj.setChannelInUse(StringUtil.getStringValue(wlanMap.get("channel_in_use")));
			wlanObj.setStatus(StringUtil.getStringValue(wlanMap.get("status")));
			wlanObj.setAssociatedNum(StringUtil.getStringValue(wlanMap.get("associated_num")));
			wlanObj.setIeeeAuthMode(StringUtil.getStringValue(wlanMap.get("ieee_auth_mode")));
			wlanObj.setIeeeEncrMode(StringUtil.getStringValue(wlanMap.get("ieee_encr_mode")));
		}
		return wlanObj;
	}

	/**
	 * add strategy.
	 */
//	public Boolean addStrategy(StrategyOBJ obj)
//	{
//		logger.debug("addStrategy({})", obj);
//		if (obj == null)
//		{
//			logger.debug("obj == null");
//			return false;
//		}
//		StringBuilder sql = new StringBuilder();
//		sql.append("insert into gw_serv_strategy (");
//		sql.append("id,acc_oid,time,type" + ",device_id,oui,device_serialnumber,username"
//				+ ",sheet_para,service_id,task_id,order_id");
//		sql.append(") values (");
//		sql.append(obj.getId());
//		sql.append("," + obj.getAccOid());
//		sql.append("," + obj.getTime());
//		sql.append("," + obj.getType());
//		sql.append("," + StringUtil.getSQLString(obj.getDeviceId()));
//		sql.append("," + StringUtil.getSQLString(obj.getOui()));
//		sql.append("," + StringUtil.getSQLString(obj.getSn()));
//		sql.append("," + StringUtil.getSQLString(obj.getUsername()));
//		sql.append("," + StringUtil.getSQLString(obj.getSheetPara()));
//		sql.append("," + obj.getServiceId());
//		sql.append("," + StringUtil.getSQLString(obj.getTaskId()));
//		sql.append("," + obj.getOrderId());
//		sql.append(")");
//		try
//		{
//			jt.execute(sql.toString());
//			return true;
//		}
//		catch (DataAccessException e)
//		{
//			logger.error("DataAccessException:{}", e.getMessage());
//			return false;
//		}
//	}

	
	/**
	 * 获取wlan口的列表
	 */
	public List getWlan(String deviceId)
	{
		List wlanList = null;
		if (StringUtil.IsEmpty(deviceId))
		{
			logger.warn("getWlan(deviceId): is null or ''");
		}
		else
		{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select lan_id,lan_wlan_id from ");
			}else{
				psql.append("select * from ");
			}
			psql.append(Global.getTabName(deviceId,"gw_lan_wlan"));
			psql.append(" where device_id=? order by lan_wlan_id");
			psql.setString(1, deviceId);
			List rList = jt.queryForList(psql.getSQL());
			if (null == rList || rList.size() < 1)
			{
				logger.warn("getWlan(deviceId): 没有该deviceId对应的wlan结点记录");
			}
			else
			{
				int size = rList.size();
				wlanList = new ArrayList();				
				for (int i = 0; i < size; i++)
				{
					Map tMap = (Map) rList.get(i);	
					Map rmap = new HashMap<String, String>();
					StringBuffer key = new StringBuffer();
					key.append("InternetGatewayDevice.LANDevice.")
						.append(StringUtil.getIntegerValue(tMap.get("lan_id")))
						.append(".WLANConfiguration.")
						.append(StringUtil.getIntegerValue(tMap.get("lan_wlan_id")));
					StringBuffer value = new StringBuffer();
					value.append("WLAN")
						.append(StringUtil.getIntegerValue(tMap.get("lan_wlan_id")));
					rmap.put(key, value);
					wlanList.add(rmap);
				}
			}
		}
		return wlanList;
	}
	
	/**
	 * 删除节点数据
	 */
	public String delWlan(WlanOBJ obj) 
	{
		String sql = "delete from gw_lan_wlan where device_id='" + obj.getDeviceId()
				+ "' and lan_id=" + obj.getLanId()
				+ " and lan_wlan_id=" + obj.getLanWlanId();
		logger.info(sql);
		if (DataSetBean.executeUpdate(sql) < 0) {
			return "true";
		}

		return "false";
	}
	
	/**
	 * 编辑Wlan整体状态
	 */
	public String configWlan(WlanOBJ obj) 
	{
		String sql = "update gw_lan_wlan set ap_enable="+obj.getApEnable()
					+ ",powerlevel="+obj.getPowerlevel()
					+ ",wps_key_word="+obj.getWpsKeyWord()
					+ ",hide="+obj.getHide()
					+ " where device_id='"+obj.getDeviceId()
					+ "' and lan_id="+obj.getLanId()+" and lan_wlan_id="+obj.getLanWlanId();
		logger.info(sql);
		if (DataSetBean.executeUpdate(sql) < 0) {
			return "true";
		}

		return "false";
	}

	/**
	 * 新增节点数据
	 */
	public int add(WlanOBJ wlanObj) 
	{
		if (wlanObj == null || wlanObj.getDeviceId() == null
				|| wlanObj.getLanId() == null || wlanObj.getLanWlanId() == null) {
			return -1;
		}

		String beacontype=null;
		if("None".equals(wlanObj.getBeacontype())){
			beacontype="None";
		}else if("Basic".equals(wlanObj.getBeacontype())){
			beacontype="WEP";
		}else if("WPA".equals(wlanObj.getBeacontype())){
			beacontype="WPA-PSK";
		}
		
		if(VENDORID_HW.equals(wlanObj.getVendorId()) || VENDORID_ZX.equals(wlanObj.getVendorId())){
			if("WPA2".equals(wlanObj.getBeacontype())){
				beacontype="WPA2-PSK";
			}else if("WPA/WPA2".equals(wlanObj.getBeacontype())){
				beacontype="WPA-PSK/WPA2-PSK";
			}
		}else{
			if("11i".equals(wlanObj.getBeacontype())){
				beacontype="WPA2-PSK";
			}else if("WPAand11i".equals(wlanObj.getBeacontype())){
				beacontype="WPA-PSK/WPA2-PSK";
			}
		}
		
		String sql = "insert into gw_lan_wlan(device_id,lan_id,lan_wlan_id,"
				+ "radio_enable,enable,ssid,beacontype,basic_auth_mode,"
				+ "wep_key,wpa_auth_mode,wpa_encr_mode,wpa_key,hide,channel,channel_in_use) "
				+ "values ('";
		
		sql += wlanObj.getDeviceId() 
				+ "'," + wlanObj.getLanId() 
				+ "," + wlanObj.getLanWlanId() 
				+ ","+ wlanObj.getRadioEnable() 
				+ ","+ wlanObj.getEnable() 
				+ ",'" + wlanObj.getSsid() 
				+ "','" + beacontype 
				+ "','" + wlanObj.getBasicAuthMode()
				+ "','" + wlanObj.getWepKey() 
				+ "','" + wlanObj.getWpaAuthMode() 
				+ "','" + wlanObj.getWpaEncrMode()
				+ "','" + wlanObj.getWpaKey() 
				+ "'," + wlanObj.getHide() 
				+ "," + wlanObj.getChannel()
				+ ",'" + wlanObj.getChannelInUse()
				+ "')";

		sql = sql.replaceAll("'null'", "NULL");
		sql = sql.replaceAll("''", "NULL");
		logger.info(sql);

    	return DataSetBean.executeUpdate(sql);
	}
	
	/**
	 * 编辑节点数据
	 */
	public int edit(WlanOBJ wlanObj) 
	{
		if (wlanObj == null || wlanObj.getDeviceId() == null
				|| wlanObj.getLanId() == null || wlanObj.getLanWlanId() == null) {
			return -1;
		}
		
		String beacontype=null;
		if("None".equals(wlanObj.getBeacontype())){
			beacontype="None";
		}else if("Basic".equals(wlanObj.getBeacontype())){
			beacontype="WEP";
		}else if("WPA".equals(wlanObj.getBeacontype())){
			beacontype="WPA-PSK";
		}
		
		if(VENDORID_HW.equals(wlanObj.getVendorId()) || VENDORID_ZX.equals(wlanObj.getVendorId())){
			if("WPA2".equals(wlanObj.getBeacontype())){
				beacontype="WPA2-PSK";
			}else if("WPA/WPA2".equals(wlanObj.getBeacontype())){
				beacontype="WPA-PSK/WPA2-PSK";
			}
		}else{
			if("11i".equals(wlanObj.getBeacontype())){
				beacontype="WPA2-PSK";
			}else if("WPAand11i".equals(wlanObj.getBeacontype())){
				beacontype="WPA-PSK/WPA2-PSK";
			}
		}
		
		String sql = "update gw_lan_wlan set ssid='" + wlanObj.getSsid()
					+ "',channel=" + wlanObj.getChannel()
					+ ",enable=" + wlanObj.getEnable()
					+ ",beacontype='" + beacontype+"'";
		
		if ("Basic".equals(wlanObj.getBeacontype())) {
			sql+=",basic_auth_mode='" + wlanObj.getBasicAuthMode()
				+ "',wep_key='" + wlanObj.getWepKey()+"'";
		} else if ("WPA".equals(wlanObj.getBeacontype()) 
					|| ((VENDORID_HW.equals(wlanObj.getVendorId()) || VENDORID_ZX.equals(wlanObj.getVendorId())) 
							&& ("WPA2".equals(wlanObj.getBeacontype()) 
									|| "WPA/WPA2".equals(wlanObj.getBeacontype()))) 
					|| ((!VENDORID_HW.equals(wlanObj.getVendorId()) && !VENDORID_ZX.equals(wlanObj.getVendorId())) 
							&& ("11i".equals(wlanObj.getBeacontype()) 
									|| "WPAand11i".equals(wlanObj.getBeacontype())))) {
			sql+=",ieee_auth_mode='" + wlanObj.getIeeeAuthMode()
					+ "',wpa_encr_mode='" + wlanObj.getWpaEncrMode()
					+ "',wpa_key='" + wlanObj.getWpaKey()+"'";
		}else{
			sql+=",basic_auth_mode='" + wlanObj.getBasicAuthMode()+"'";
		}
		sql+=" where device_id='" + wlanObj.getDeviceId()
				+ "' and lan_id=" + wlanObj.getLanId()
				+ " and lan_wlan_id=" + wlanObj.getLanWlanId();
		logger.info(sql);
    	return DataSetBean.executeUpdate(sql);
	}
}
