package com.linkage.module.gwms.obj.gw;

import java.util.Date;

/**
 * InternetGatewayDevice.Services.VoiceService.i.VoiceProfile.i.X_CT-COM_H248 
 * 
 * @author Administrator
 *
 */
public class VoiceServiceProfileH248OBJ {

	/**
	 * 设备ID
	 */
	String deviceId = null;
	
	/**
	 * VOIP ID
	 */
	String voipId = null;

	/**
	 * Profile ID
	 */
	String profId = null;
	
	/**
	 * 采集时间
	 */
	String gatherTime = null;
	
	/**
	 * 地址
	 */
	private String mediaGatewayControler = null;
	
	/**
	 * 端口
	 */
	private String mediaGatewayControlerPort = null;
	
	/**
	 * 备用地址
	 */
	private String mediaGatewayControler2 = null;
	
	/**
	 * 备用端口
	 */
	private String mediaGatewayControlerPort2 = null;

	/**
	 * UDP端口号
	 */
	private String mediaGatewayPort	= null;
	
	/**
	 * 全局唯一的标识
	 */
	private String h248DeviceId	= null;
	
	/**
	 * 标识的类型
	 */
	private String h248DeviceIdType	= null;
	
	/**
	 * RTP终结点标识前缀
	 */
	private String rtpPrefix	= null;
	
	/**
	 * h248端口状态
	 */
	private String interfaceState = null;
	
	

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVoipId() {
		return voipId;
	}

	public void setVoipId(String voipId) {
		this.voipId = voipId;
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}

	public String getGatherTime() {
		if (gatherTime == null || gatherTime.length() == 0) {
			gatherTime = "" + (new Date()).getTime() / 1000;
		}
		return gatherTime;
	}

	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getMediaGatewayControler() {
		return mediaGatewayControler;
	}

	public void setMediaGatewayControler(String mediaGatewayControler) {
		this.mediaGatewayControler = mediaGatewayControler;
	}

	public String getMediaGatewayControlerPort() {
		return mediaGatewayControlerPort;
	}

	public void setMediaGatewayControlerPort(String mediaGatewayControlerPort) {
		this.mediaGatewayControlerPort = mediaGatewayControlerPort;
	}

	public String getMediaGatewayControler2() {
		return mediaGatewayControler2;
	}

	public void setMediaGatewayControler2(String mediaGatewayControler2) {
		this.mediaGatewayControler2 = mediaGatewayControler2;
	}

	public String getMediaGatewayControlerPort2() {
		return mediaGatewayControlerPort2;
	}

	public void setMediaGatewayControlerPort2(String mediaGatewayControlerPort2) {
		this.mediaGatewayControlerPort2 = mediaGatewayControlerPort2;
	}

	public String getMediaGatewayPort() {
		return mediaGatewayPort;
	}

	public void setMediaGatewayPort(String mediaGatewayPort) {
		this.mediaGatewayPort = mediaGatewayPort;
	}

	public String getH248DeviceId() {
		return h248DeviceId;
	}

	public void setH248DeviceId(String deviceId) {
		h248DeviceId = deviceId;
	}

	public String getH248DeviceIdType() {
		return h248DeviceIdType;
	}

	public void setH248DeviceIdType(String deviceIdType) {
		h248DeviceIdType = deviceIdType;
	}

	public String getRtpPrefix() {
		return rtpPrefix;
	}

	public void setRtpPrefix(String rtpPrefix) {
		this.rtpPrefix = rtpPrefix;
	}

	public String getInterfaceState()
	{
		return interfaceState;
	}
	
	public void setInterfaceState(String interfaceState)
	{
		this.interfaceState = interfaceState;
	}
	
}
