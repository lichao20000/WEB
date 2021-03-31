package com.linkage.module.gwms.obj.gw;

import java.util.Date;

/**
 * InternetGatewayDevice.X_CT-COM_UplinkQoS.Classification.i.
 * @author gongsj
 * @date 2009-6-17
 */
public class QoSClassificationOBJ {
	private String deviceId;

	private String gatherTime;
	
	private String classId;
	
	private String queueId;
	
	private String valueDscp;
	
	private String value8021p;
	
	private QoSClassificationTypeOBJ[] classType;

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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	public String getValueDscp() {
		return valueDscp;
	}

	public void setValueDscp(String valueDscp) {
		this.valueDscp = valueDscp;
	}

	public String getValue8021p() {
		return value8021p;
	}

	public void setValue8021p(String value8021p) {
		this.value8021p = value8021p;
	}

	public QoSClassificationTypeOBJ[] getClassType() {
		return classType;
	}

	public void setClassType(QoSClassificationTypeOBJ[] classType) {
		this.classType = classType;
	}
	
}
