package com.linkage.module.gwms.obj.gw;

import java.util.Date;

/**
 * InternetGatewayDevice.X_CT-COM_UplinkQoS.Classification.i.Type.i.
 * @author gongsj
 * @date 2009-6-17
 */
public class QoSClassificationTypeOBJ {

	private String deviceId;

	private String gatherTime;
	
	private String classId;
	
	private String typeId;
	
	private String typeName;
	
	private String typeMax;
	
	private String typeMin;
	
	private String typeProt;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(String gatherTime) {
		if (gatherTime == null || gatherTime.length() == 0) {
			gatherTime = "" + (new Date()).getTime() / 1000;
		}
		this.gatherTime = gatherTime;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeMax() {
		return typeMax;
	}

	public void setTypeMax(String typeMax) {
		this.typeMax = typeMax;
	}

	public String getTypeMin() {
		return typeMin;
	}

	public void setTypeMin(String typeMin) {
		this.typeMin = typeMin;
	}

	public String getTypeProt() {
		return typeProt;
	}

	public void setTypeProt(String typeProt) {
		this.typeProt = typeProt;
	}
	
	
	
	
	
	
	
	
}
