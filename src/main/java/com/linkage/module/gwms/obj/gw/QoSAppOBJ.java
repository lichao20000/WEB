package com.linkage.module.gwms.obj.gw;

import java.util.Date;

/**
 * InternetGatewayDevice.X_CT-COM_UplinkQoS.App.i.
 * @author gongsj
 * @date 2009-6-17
 */
public class QoSAppOBJ {

	private String deviceId;

	private String gatherTime;
	
	private String appId;
	
	private String appName;
	
	private String queueId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}
	
	
}
