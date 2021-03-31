package com.linkage.module.gwms.obj.gw;

/**
 * @author Jason(3412)
 * @date 2009-10-21
 */
public class DeviceOBJ {

	
	// 设备ID
	private String deviceId;
	// OUI
	private String oui;
	// 设备序列号
	private String devSn;
	// 设备IP
	private String loopbackIp;
	// 端口
	private String crPort;
	// 路径
	private String crPath;
	// 采集点ID
	private String gatherId;
	// 设备名称
	private String deviceName;

	
	
	/** getter,setter methods */

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

	public String getLoopbackIp() {
		return loopbackIp;
	}

	public void setLoopbackIp(String loopbackIp) {
		this.loopbackIp = loopbackIp;
	}

	public String getCrPort() {
		return crPort;
	}

	public void setCrPort(String crPort) {
		this.crPort = crPort;
	}

	public String getCrPath() {
		return crPath;
	}

	public void setCrPath(String crPath) {
		this.crPath = crPath;
	}

	public String getGatherId() {
		return gatherId;
	}

	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
