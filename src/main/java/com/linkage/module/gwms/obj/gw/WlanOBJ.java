package com.linkage.module.gwms.obj.gw;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * object:gw_lan_wlan
 */
public class WlanOBJ implements I_DevConfOBJ 
{
	private static Logger logger = LoggerFactory.getLogger(WlanOBJ.class);

	/** device_id */
	private String deviceId;
	/** gather_time */
	private long gatherTime;
	/** lan_id */
	private String lanId;
	/** lan_wlan_id */
	private String lanWlanId;
	/** 模块开关 */
	private String apEnable;
	/** 模块功率 */
	private String powerlevel;
	/** 当前功率 */
	private String powervalue;
	/** 是否启用 */
	private String enable;
	/** SSID */
	private String ssid;
	/** 工作模式 */
	private String standard;
	/** 加密安全模式 */
	private String beacontype;
	/** 基本认证模式 */
	private String basicAuthMode;
	/** WEP密钥长度 */
	private String wepEncrLevel;
	/** WEP密码 */
	private String wepKey;
	/** WPA认证模式 */
	private String wpaAuthMode = "PSKAuthentication";
	/** WPA加密模式 */
	private String wpaEncrMode = "TKIPEncryption";
	/** WPA密码 */
	private String wpaKey;
	/** 是否广播 */
	private String radioEnable;
	/** 是否隐藏 */
	private String hide;
	/** 可选信道 */
	private String possChannel;
	/** 信道 */
	private String channel;
	/** 当前信道 */
	private String channelInUse;
	/** 关联设备数 */
	private String associatedNum;
	/** 状态 */
	private String status;
	/** WPS关键字 */
	private String wpsKeyWord;
	/** 11i认证模式 */
	private String ieeeAuthMode = "PSKAuthentication";
	/** 11i加密模式 */
	private String ieeeEncrMode = "TKIPEncryption";
	private String preSharedKey;
	private String keyPassphrase;
	/**wpa/wpa2加密模式*/
	private String wpaWpa2EncrMode = "TKIPEncryption";
	//1:不加密 2:WEP加密 3:WPA加密 4:11i加密5:WPA/WPA2加密
	private int encryptionType;
	/**厂商*/
	private String vendorId;
	
	public WlanOBJ() {}

	/**
	 * constructor(Map)
	 */
	@SuppressWarnings("rawtypes")
	public WlanOBJ(Map map) 
	{
		if (map == null) {
			logger.warn("WlanOBJ is null");
			return;
		}

		this.deviceId = StringUtil.getStringValue(map, "device_id");
		this.gatherTime = StringUtil.getLongValue(map, "gather_time");
		this.lanId = StringUtil.getStringValue(map, "lan_id");
		this.lanWlanId = StringUtil.getStringValue(map, "lan_wlan_id");
		this.apEnable = StringUtil.getStringValue(map, "ap_enable");
		this.powerlevel = StringUtil.getStringValue(map, "powerlevel");
		this.powervalue = StringUtil.getStringValue(map, "powervalue");
		this.enable = StringUtil.getStringValue(map, "enable");
		this.ssid = StringUtil.getStringValue(map, "ssid");
		this.standard = StringUtil.getStringValue(map, "standard");
		this.beacontype = StringUtil.getStringValue(map, "beacontype");
		this.basicAuthMode = StringUtil.getStringValue(map, "basic_auth_mode");
		this.wepEncrLevel = StringUtil.getStringValue(map, "wep_encr_level");
		this.wepKey = StringUtil.getStringValue(map, "wep_key");
		this.wpaAuthMode = StringUtil.getStringValue(map, "wpa_auth_mode");
		this.wpaEncrMode = StringUtil.getStringValue(map, "wpa_encr_mode");
		this.wpaKey = StringUtil.getStringValue(map, "wpa_key");
		this.radioEnable = StringUtil.getStringValue(map, "radio_enable");
		this.hide = StringUtil.getStringValue(map, "hide");
		this.possChannel = StringUtil.getStringValue(map, "poss_channel");
		this.channel = StringUtil.getStringValue(map, "channel");
		this.channelInUse = StringUtil.getStringValue(map, "channel_in_use");
		this.status = StringUtil.getStringValue(map, "status");
		this.wpsKeyWord = StringUtil.getStringValue(map, "wps_key_word");
		this.associatedNum = StringUtil.getStringValue(map, "associated_num");
		this.ieeeAuthMode = StringUtil.getStringValue(map, "ieee_auth_mode");
		this.ieeeEncrMode = StringUtil.getStringValue(map, "ieee_encr_mode");

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(long gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getLanId() {
		return lanId;
	}

	public String getLanWlanId() {
		return lanWlanId;
	}

	public String getApEnable() {
		return apEnable;
	}

	public String getPowerlevel() {
		return powerlevel;
	}

	public String getPowervalue() {
		return powervalue;
	}

	public String getEnable() {
		return enable;
	}

	public String getSsid() {
		return ssid;
	}

	public String getStandard() {
		return standard;
	}

	public String getBeacontype() {
		return beacontype;
	}

	public String getBasicAuthMode() {
		return basicAuthMode;
	}

	public String getWepEncrLevel() {
		return wepEncrLevel;
	}

	public String getWepKey() {
		return wepKey;
	}

	public String getWpaAuthMode() {
		return wpaAuthMode;
	}

	public String getWpaEncrMode() {
		return wpaEncrMode;
	}

	public String getWpaKey() {
		return wpaKey;
	}

	public String getRadioEnable() {
		return radioEnable;
	}

	public String getHide() {
		return hide;
	}

	public String getPossChannel() {
		return possChannel;
	}

	public String getChannel() {
		return channel;
	}

	public String getChannelInUse() {
		return channelInUse;
	}

	public String getAssociatedNum() {
		return associatedNum;
	}

	public String getStatus() {
		return status;
	}

	public String getWpsKeyWord() {
		return wpsKeyWord;
	}

	public void setLanId(String lanId) {
		this.lanId = lanId;
	}

	public void setLanWlanId(String lanWlanId) {
		this.lanWlanId = lanWlanId;
	}

	public void setApEnable(String apEnable) {
		this.apEnable = apEnable;
	}

	public void setPowerlevel(String powerlevel) {
		this.powerlevel = powerlevel;
	}

	public void setPowervalue(String powervalue) {
		this.powervalue = powervalue;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setBeacontype(String beacontype) {
		this.beacontype = beacontype;
	}

	public void setBasicAuthMode(String basicAuthMode) {
		this.basicAuthMode = basicAuthMode;
	}

	public void setWepEncrLevel(String wepEncrLevel) {
		this.wepEncrLevel = wepEncrLevel;
	}

	public void setWepKey(String wepKey) {
		this.wepKey = wepKey;
	}

	public void setWpaAuthMode(String wpaAuthMode) {
		this.wpaAuthMode = wpaAuthMode;
	}

	public void setWpaEncrMode(String wpaEncrMode) {
		this.wpaEncrMode = wpaEncrMode;
	}

	public void setWpaKey(String wpaKey) {
		this.wpaKey = wpaKey;
	}

	public void setRadioEnable(String radioEnable) {
		this.radioEnable = radioEnable;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public void setPossChannel(String possChannel) {
		this.possChannel = possChannel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setChannelInUse(String channelInUse) {
		this.channelInUse = channelInUse;
	}

	public void setAssociatedNum(String associatedNum) {
		this.associatedNum = associatedNum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setWpsKeyWord(String wpsKeyWord) {
		this.wpsKeyWord = wpsKeyWord;
	}

	public String getIeeeAuthMode() {
		return ieeeAuthMode;
	}

	public String getIeeeEncrMode() {
		return ieeeEncrMode;
	}

	public void setIeeeAuthMode(String ieeeAuthMode) {
		this.ieeeAuthMode = ieeeAuthMode;
	}

	public void setIeeeEncrMode(String ieeeEncrMode) {
		this.ieeeEncrMode = ieeeEncrMode;
	}

	public String getPreSharedKey() {
		return preSharedKey;
	}

	public void setPreSharedKey(String preSharedKey) {
		this.preSharedKey = preSharedKey;
	}

	public String getKeyPassphrase() {
		return keyPassphrase;
	}
	
	public void setKeyPassphrase(String keyPassphrase) {
		this.keyPassphrase = keyPassphrase;
	}

	public int getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(int encryptionType) {
		this.encryptionType = encryptionType;
	}
	
	public String getWpaWpa2EncrMode() {
		return wpaWpa2EncrMode;
	}

	public void setWpaWpa2EncrMode(String wpaWpa2EncrMode) {
		this.wpaWpa2EncrMode = wpaWpa2EncrMode;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * get string Object.
	 */
	public String toString() {
		return "[" + deviceId + "] " + lanId + "_" + lanWlanId + "_" + ssid;
	}

}
