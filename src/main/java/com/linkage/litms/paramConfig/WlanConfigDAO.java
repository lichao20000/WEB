/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.paramConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * dao for wlan config.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Dec 25, 2008
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class WlanConfigDAO 
{
	private static Logger logger = LoggerFactory.getLogger(WlanConfigDAO.class);
	public static String instArea=Global.instAreaShortName;
	
	/**
	 * 取得IOR
	 */
	public String getIOR(String gather_id)
	{
		String ior = null;
		String getIorSQL = "select ior from tab_ior where object_name='ACS_"
				+ gather_id + "' and object_poa='ACS_Poa_" + gather_id + "'";
		PrepareSQL psql = new PrepareSQL(getIorSQL);
		Map<String, String> map = DataSetBean.getRecord(psql.getSQL());
		if (null != map) {
			ior = (String) map.get("ior");
		}

		return ior;
	}

	/**
	 * delete wlan.
	 */
	public boolean delWlan(String deviceId, String lanId, String lanWlanId) 
	{
		boolean flag = true;

		String sql = "delete from gw_lan_wlan where device_id='" + deviceId
				+ "' and lan_id=" + lanId + " and lan_wlan_id=" + lanWlanId;
		PrepareSQL psql = new PrepareSQL(sql);
		if (DataSetBean.executeUpdate(psql.getSQL()) < 0) {
			flag = false;
		}

		return flag;
	}

	/**
	 * 获取WLAN
	 */
	public WlanObj[] getWlan(String deviceId)
	{
		WlanObj[] wlanObj = null;

		String sql = "select * from gw_lan_wlan where device_id='" + deviceId+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select lan_id, lan_wlan_id, gather_time, ap_enable, powerlevel, powervalue, enable, ssid, standard," +
					" beacontype, basic_auth_mode, wep_encr_level, wep_key_id, wep_key, wpa_auth_mode, wpa_encr_mode," +
					" wpa_key, radio_enable, hide, poss_channel, channel, channel_in_use from gw_lan_wlan where device_id='" + deviceId+ "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		if (cursor != null) {
			int size = cursor.getRecordSize();
			if (size == 0) {
				return wlanObj;
			}

			wlanObj = new WlanObj[size];
			Map<String, String> map = null;
			for (int i = 0; i < size; i++) {
				map = cursor.getRecord(i);
				if (map != null) {
					wlanObj[i] = new WlanObj();
					wlanObj[i].setDeviceId(deviceId);
					wlanObj[i].setLanId((String) map.get("lan_id"));
					wlanObj[i].setLanWlanId((String) map.get("lan_wlan_id"));
					wlanObj[i].setGatherTime((String) map.get("gather_time"));
					wlanObj[i].setApEnable((String) map.get("ap_enable"));
					wlanObj[i].setPowerLevel((String) map.get("powerlevel"));
					wlanObj[i].setPowerValue((String) map.get("powervalue"));
					wlanObj[i].setEnable((String) map.get("enable"));
					wlanObj[i].setSsid((String) map.get("ssid"));
					wlanObj[i].setStandard((String) map.get("standard"));
					wlanObj[i].setBeacontType((String) map.get("beacontype"));
					wlanObj[i].setBasicAuthMode((String) map.get("basic_auth_mode"));
					wlanObj[i].setWepEncrLevel((String) map.get("wep_encr_level"));
					wlanObj[i].setWepKeyId((String) map.get("wep_key_id"));
					wlanObj[i].setWepKey((String) map.get("wep_key"));
					wlanObj[i].setWpaAuthMode((String) map.get("wpa_auth_mode"));
					wlanObj[i].setWpaEncrMode((String) map.get("wpa_encr_mode"));
					wlanObj[i].setWpaKey((String) map.get("wpa_key"));
					wlanObj[i].setRadioEnable((String) map.get("radio_enable"));
					wlanObj[i].setHide((String) map.get("hide"));
					wlanObj[i].setPossChannel((String) map.get("poss_channel"));
					wlanObj[i].setChannel((String) map.get("channel"));
					wlanObj[i].setChannelInUse((String) map.get("channel_in_use"));
				}
			}
		}

		return wlanObj;
	}

	/**
	 * 获取一个WLAN
	 */
	public WlanObj getWlan(String deviceId, String lanId, String wlanId)
	{
		WlanObj wlanObj = null;

		String sql = "select * from gw_lan_wlan where device_id='" + deviceId
				+ "' and lan_id=" + lanId + " and lan_wlan_id=" + wlanId;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select lan_id, lan_wlan_id, gather_time, ap_enable, powerlevel, powervalue, enable, ssid, standard," +
					" beacontype, basic_auth_mode, wep_encr_level, wep_key_id, wep_key, wpa_auth_mode, wpa_encr_mode," +
					" wpa_key, radio_enable, hide, poss_channel, channel, channel_in_use from gw_lan_wlan where device_id='" + deviceId
					+ "' and lan_id=" + lanId + " and lan_wlan_id=" + wlanId;
		}
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String, String> map = DataSetBean.getRecord(psql.getSQL());
		if (map != null) 
		{
			wlanObj = new WlanObj();
			wlanObj.setDeviceId(deviceId);
			wlanObj.setLanId((String) map.get("lan_id"));
			wlanObj.setLanWlanId((String) map.get("lan_wlan_id"));
			wlanObj.setGatherTime((String) map.get("gather_time"));
			wlanObj.setApEnable((String) map.get("ap_enable"));
			wlanObj.setPowerLevel((String) map.get("powerlevel"));
			wlanObj.setPowerValue((String) map.get("powervalue"));
			wlanObj.setEnable((String) map.get("enable"));
			wlanObj.setSsid((String) map.get("ssid"));
			wlanObj.setStandard((String) map.get("standard"));
			wlanObj.setBeacontType((String) map.get("beacontype"));
			wlanObj.setBasicAuthMode((String) map.get("basic_auth_mode"));
			wlanObj.setWepEncrLevel((String) map.get("wep_encr_level"));
			wlanObj.setWepKeyId((String) map.get("wep_key_id"));
			wlanObj.setWepKey((String) map.get("wep_key"));
			wlanObj.setWpaAuthMode((String) map.get("wpa_auth_mode"));
			wlanObj.setWpaEncrMode((String) map.get("wpa_encr_mode"));
			wlanObj.setWpaKey((String) map.get("wpa_key"));
			wlanObj.setRadioEnable((String) map.get("radio_enable"));
			wlanObj.setHide((String) map.get("hide"));
			wlanObj.setPossChannel((String) map.get("poss_channel"));
			wlanObj.setChannel((String) map.get("channel"));
			wlanObj.setChannelInUse((String) map.get("channel_in_use"));
		}

		return wlanObj;
	}

	/**
	 * 更新单个WLAN
	 */
	public boolean setWlan(String deviceId, String lanId, String lanWlanId,
			String[] params_name, String[] params_value, String[] para_type_id) 
	{
		boolean flag = true;

		if (params_name == null || params_value == null || para_type_id == null) {
			flag = false;
			return flag;
		}

		String sql = "update gw_lan_wlan set gather_time="+ (new Date()).getTime() / 1000;

		for (int j = 0; j < params_name.length; j++) 
		{
			if (!StringUtil.IsEmpty(params_name[j])
					&& !StringUtil.IsEmpty(params_value[j])) {
				if("iee11i_auth_mode".equals(params_name[j])){
					continue;
				}
				
				if (para_type_id[j].equals("1")) {
					sql += "," + params_name[j] + " = '" + params_value[j]+ "'";
				} else {
					sql += "," + params_name[j] + " =" + params_value[j];
				}
			}

		}
		sql += " where device_id='" + deviceId + "' and lan_id=" + lanId
				+ " and lan_wlan_id=" + lanWlanId;

		sql = sql.replaceAll("'null'", "NULL").replaceAll("''", "NULL");

		PrepareSQL psql = new PrepareSQL(sql);
		if (DataSetBean.executeUpdate(psql.getSQL()) < 0) {
			flag = false;
		}
		sql = null;

		return flag;
	}

	/**
	 * 根据DEVICEID更新设备的全部WLAN
	 */
	public boolean setWlan(String deviceId, WlanObj[] wlanObj)
	{
		boolean flag = true;

		if (wlanObj == null || wlanObj.length == 0) {
			flag = false;
			return flag;
		}

		ArrayList<String> list = new ArrayList<String>();
		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from gw_lan_wlan where device_id=? ");
		psql.setString(1,deviceId);
		list.add(psql.getSQL());
		String tmp = "insert into gw_lan_wlan(device_id,lan_id,lan_wlan_id,"
				+ "gather_time,ap_enable,powerlevel,powervalue,enable,"
				+ "ssid,standard,beacontype,basic_auth_mode,wep_encr_level,";
		
		if(Global.XJDX.equals(instArea)){
			tmp += "wps_key_word,status,";
		}else{
			tmp += "wep_key_id,";
		}
		
		tmp += "wep_key,wpa_auth_mode,wpa_encr_mode,wpa_key"
				+ ",radio_enable,hide,poss_channel,channel,channel_in_use"
				+ ") values (";
		String sql=null;
		for (int i = 0; i < wlanObj.length; i++) 
		{
			StringBuffer sb=new StringBuffer();
			sb.append(tmp);
			sb.append("'" + wlanObj[i].getDeviceId());
			sb.append("',"+ wlanObj[i].getLanId());
			sb.append("," + wlanObj[i].getLanWlanId());
			sb.append("," + wlanObj[i].getGatherTime());
			sb.append(",'"+ wlanObj[i].getApEnable());
			sb.append("',"+ wlanObj[i].getPowerLevel());
			sb.append(",'"+ wlanObj[i].getPowerValue());
			sb.append("'," + wlanObj[i].getEnable());
			sb.append(",'" + wlanObj[i].getSsid());
			sb.append("','"+ wlanObj[i].getStandard());
			sb.append("','"+ wlanObj[i].getBeacontType());
			sb.append("','"+ wlanObj[i].getBasicAuthMode());
			sb.append("','"+ wlanObj[i].getWepEncrLevel());
			
			if(Global.XJDX.equals(instArea)){
				sb.append("','"+ wlanObj[i].getWpsKeyWord());
				sb.append("','"+ wlanObj[i].getStatus()+"'");
			}else{
				sb.append("',"+ wlanObj[i].getWepKeyId()); 
			}
			
			sb.append(",'" + wlanObj[i].getWepKey());
			sb.append("','" + wlanObj[i].getWpaAuthMode()); 
			sb.append("','"+ wlanObj[i].getWpaEncrMode()); 
			sb.append("','"+ wlanObj[i].getWpaKey()); 
			sb.append("',"+ wlanObj[i].getRadioEnable()); 
			sb.append(",'" + wlanObj[i].getHide());
			sb.append("','" + wlanObj[i].getPossChannel()); 
			sb.append("',"+ wlanObj[i].getChannel()); 
			sb.append(",'"+ wlanObj[i].getChannelInUse()); 
			sb.append("')");

			sql=sb.toString();
			sql = sql.replaceAll("'null'", "NULL").replaceAll("''", "NULL");
			PrepareSQL psql1 = new PrepareSQL(sql);
			list.add(psql1.getSQL());
			sql=null;
		}

		int[] code = DataSetBean.doBatch(list);
		if (code != null) {
			for (int j = 0; j < code.length; j++) {
				if (code[j] < 0) {
					flag = false;
					break;
				}
			}
		}

		// clear
		code = null;
		list = null;
		sql = null;
		tmp = null;

		return flag;
	}

	/**
	 * add new wlan.
	 */
	public boolean addWlan(WlanObj wlanObj)
	{
		logger.debug("boolean addWlan({})", wlanObj);
		boolean flag = true;
		if (wlanObj == null || wlanObj.getDeviceId() == null
				|| wlanObj.getLanId() == null || wlanObj.getLanWlanId() == null) {
			logger.debug("wlanObj == null");
			flag = false;
			
			return flag;
		}

		String sql = "insert into gw_lan_wlan(device_id,lan_id,lan_wlan_id,"
				+ "gather_time,ap_enable,powerlevel,powervalue,enable,"
				+ "ssid,standard,beacontype,basic_auth_mode,wep_encr_level,";
		
		if(!Global.XJDX.equals(instArea)){
			sql += "wep_key_id,";
		}
		
		sql += "wep_key,wpa_auth_mode,wpa_encr_mode,wpa_key,"
				+ "hide,poss_channel,channel,channel_in_use) "
				+ "values ('";
		
		//ApEnalbe, PowerLever因页面没有编辑，所以默认入1,1
		//RadioEnable因有些设备无此结点，所以不入，采用数据库默认的1
		//"+ wlanObj.getApEnable() + "," + wlanObj.getPowerLevel() + "
		
		sql += wlanObj.getDeviceId() + "'," + wlanObj.getLanId() + ","
				+ wlanObj.getLanWlanId() + "," + wlanObj.getGatherTime() + ",'1',1,'"
				+ wlanObj.getPowerValue() + "'," + wlanObj.getEnable() + ",'"
				+ wlanObj.getSsid() + "','" + wlanObj.getStandard() + "','"
				+ wlanObj.getBeacontType() + "','" + wlanObj.getBasicAuthMode()
				+ "','" + wlanObj.getWepEncrLevel();
				
		if(Global.XJDX.equals(instArea)){
			sql += "','";
		}else{
			sql+= "',"+wlanObj.getWepKeyId() + ",'"; 
		}
				
		sql	+= wlanObj.getWepKey() + "','"
			+ wlanObj.getWpaAuthMode() + "','" + wlanObj.getWpaEncrMode()
			+ "','" + wlanObj.getWpaKey() + "','" + wlanObj.getHide() + "','" + wlanObj.getPossChannel()
			+ "'," + wlanObj.getChannel() + ",'"
			+ wlanObj.getChannelInUse() + "')";

		sql = sql.replaceAll("'null'", "NULL");
		sql = sql.replaceAll("''", "NULL");
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		if (DataSetBean.executeUpdate(sql) < 0) {
			flag = false;
		}

		sql = null;
		return flag;
	}
	
	/**获取设备厂商*/
	public String getDevVendorId(String deviceId)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id from tab_gw_device where device_id=? ");
		psql.setString(1,deviceId);
		
		return StringUtil.getStringValue(DataSetBean.getRecord(psql.getSQL()),"vendor_id",null);
	}
}
