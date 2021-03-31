package com.linkage.module.gwms.config.obj;

public class DevAndUserObj {

	private String deviceId;
	
	private String deviceSn;

	private String userAccount;
	
	//根据设备序列号等查出来的设备个数
	private int devNum;
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public int getDevNum() {
		return devNum;
	}

	public void setDevNum(int devNum) {
		this.devNum = devNum;
	}

	
	
}
