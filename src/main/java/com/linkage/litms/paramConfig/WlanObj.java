package com.linkage.litms.paramConfig;

public class WlanObj 
{
	private String deviceId;
	private String lanId;
	private String lanWlanId;
	private String gatherTime;
	/** X_CT-COM_APModuleEnable 1：开启 0：未开*/
	private String apEnable;
	private String powerLevel;
	private String powerValue;
	/** 1:可用 0:不可用 */
	private String enable;
	private String ssid;
	private String standard;
	private String beacontType;
	private String basicAuthMode;
	private String wepEncrLevel;
	private String wepKeyId;
	private String wepKey;
	private String wpaAuthMode;
	private String wpaEncrMode;
	private String wpaKey;
	/** 1:可用 0:不可用 */
	private String radioEnable;
	/**1: 是 0: 否(默认) */
	private String hide;
	private String possChannel;
	/**Channel 0：表示自动选择信道（网关自动选择的信道值通过ChannelsInUse 读取）
	 * 1～255：实际信道值
	 */
	private String channel;
	private String channelInUse;
	/** WPS关键字 */
	private String wpsKeyWord;
	private String status;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLanId() {
		return lanId;
	}

	public void setLanId(String lanId) {
		this.lanId = lanId;
	}

	public String getLanWlanId() {
		return lanWlanId;
	}

	public void setLanWlanId(String lanWlanId) {
		this.lanWlanId = lanWlanId;
	}

	public String getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getApEnable() {
		return apEnable;
	}

	public void setApEnable(String apEnable) {
		this.apEnable = apEnable;
	}

	public String getPowerLevel() {
		return powerLevel;
	}

	public void setPowerLevel(String powerLevel) {
		this.powerLevel = powerLevel;
	}

	public String getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(String powerValue) {
		this.powerValue = powerValue;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getBeacontType() {
		return beacontType;
	}

	public void setBeacontType(String beacontType) {
		this.beacontType = beacontType;
	}

	public String getBasicAuthMode() {
		return basicAuthMode;
	}

	public void setBasicAuthMode(String basicAuthMode) {
		this.basicAuthMode = basicAuthMode;
	}

	public String getWepEncrLevel() {
		return wepEncrLevel;
	}

	public void setWepEncrLevel(String wepEncrLevel) {
		this.wepEncrLevel = wepEncrLevel;
	}

	public String getWepKeyId() {
		return wepKeyId;
	}

	public void setWepKeyId(String wepKeyId) {
		this.wepKeyId = wepKeyId;
	}

	public String getWepKey() {
		return wepKey;
	}

	public void setWepKey(String wepKey) {
		this.wepKey = wepKey;
	}

	public String getWpaAuthMode() {
		return wpaAuthMode;
	}

	public void setWpaAuthMode(String wpaAuthMode) {
		this.wpaAuthMode = wpaAuthMode;
	}

	public String getWpaEncrMode() {
		return wpaEncrMode;
	}

	public void setWpaEncrMode(String wpaEncrMode) {
		this.wpaEncrMode = wpaEncrMode;
	}

	public String getWpaKey() {
		return wpaKey;
	}

	public void setWpaKey(String wpaKey) {
		this.wpaKey = wpaKey;
	}

	public String getRadioEnable() {
		return radioEnable;
	}

	public void setRadioEnable(String radioEnable) {
		this.radioEnable = radioEnable;
	}

	public String getHide() {
		return hide;
	}

	public void setHide(String hide) {
		this.hide = hide;
	}

	public String getPossChannel() {
		return possChannel;
	}

	public void setPossChannel(String possChannel) {
		this.possChannel = possChannel;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelInUse() {
		return channelInUse;
	}

	public void setChannelInUse(String channelInUse) {
		this.channelInUse = channelInUse;
	}

	public String getWpsKeyWord() {
		return wpsKeyWord;
	}

	public void setWpsKeyWord(String wpsKeyWord) {
		this.wpsKeyWord = wpsKeyWord;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * get string Object.
	 */
	public String toString() {
		return deviceId + "_" + lanId + "_" + lanWlanId + "_" + ssid;
	}

}
