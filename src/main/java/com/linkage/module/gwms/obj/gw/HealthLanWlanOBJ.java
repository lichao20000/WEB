package com.linkage.module.gwms.obj.gw;

import java.util.Date;

public class HealthLanWlanOBJ {

	private String deviceId;
	
	private String lanId;
	
	private String lanWlanId;
	
	private String gatherTime;
	
	private String powerlevel;
	
	private String powervalue;

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
		if (gatherTime == null || gatherTime.length() == 0) {
			gatherTime = "" + (new Date()).getTime() / 1000;
		}
		return gatherTime;
	}

	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getPowerlevel() {
		return powerlevel;
	}

	public void setPowerlevel(String powerlevel) {
		this.powerlevel = powerlevel;
	}

	public String getPowervalue() {
		return powervalue;
	}

	public void setPowervalue(String powervalue) {
		this.powervalue = powervalue;
	}
	
	
}
