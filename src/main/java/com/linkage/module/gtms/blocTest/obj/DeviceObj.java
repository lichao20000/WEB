package com.linkage.module.gtms.blocTest.obj;

/**
 * @author Jason(3412)
 * @date 2009-9-10
 */
public class DeviceObj {

	/** 设备ID */
	private String deviceId;
	/** 设备序列号 */
	private String devSn;
	/** OUI */
	private String oui;
	/** 属地ID */
	private String cityId;
	/** 采集点ID */
	private String gatherId;
	
	
	
	
	
	
	/** getter, setter methods*/
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	public String getOui() {
		return oui;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getGatherId() {
		return gatherId;
	}
	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}
	
}
