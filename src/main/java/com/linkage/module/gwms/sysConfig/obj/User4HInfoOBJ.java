/**
 * 
 */
package com.linkage.module.gwms.sysConfig.obj;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class User4HInfoOBJ implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private long userId = -65535;
	
	private String cityId = null;
	
	private String username= null;
	
	private int userline = -65535;
	
	private int isChkBind = -65535;
	
	private String deviceId = null;
	
	private String oui = null;
	
	private String deviceSerialnumber = null;

	private String type = null;
	private String accessTypeId = null;
	public String getCityId() {
		return cityId;
	}
	
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}
	
	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}
	
	public int getIsChkBind() {
		return isChkBind;
	}
	
	public void setIsChkBind(int isChkBind) {
		this.isChkBind = isChkBind;
	}
	
	public String getOui() {
		return oui;
	}
	
	public void setOui(String oui) {
		this.oui = oui;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public int getUserline() {
		return userline;
	}
	
	public void setUserline(int userline) {
		this.userline = userline;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getAccessTypeId() {
		return accessTypeId;
	}

	public void setAccessTypeId(String accessTypeId) {
		this.accessTypeId = accessTypeId;
	}
	
}
