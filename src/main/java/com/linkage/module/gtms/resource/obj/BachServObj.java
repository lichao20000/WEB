package com.linkage.module.gtms.resource.obj;

import java.io.Serializable;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-3-24
 * @category com.linkage.module.gtms.resource.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BachServObj implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;
	private String deviceId;
	private String oui;
	private String device_serialnumber;
	
	
	private String loid = "";
	private String oldNetUserName = "";
	private String newNetUserName = "";
	private String newNetPwd = "";
	private String netWanType = "";
	private String netVlanId = "";
	
	private String oldItvUserName = "";
	private String newItvUserName = "";
	private String itvBindPort = "";
	private String itvVlanId = "";
	
	private String oldTelphone = "";
	private String newTelphone = "";
	private String regId = "";
	private String mgcIp = "";
	private String mgcPort = "";
	private String standMgcIp = "";
	private String standMgcPort = "";
	private String regIdType = "";
	private String voiceVlanId = "";
	private String voiceWanType = "";
	private String voiceIp = "";
	private String voiceIpMask = "";
	private String voiceGateway = "";
	private String voiceDns = "";
	private String voipPort = ""; //表格未提供
	private String protocol = "";//表格未提供
	private String cityId = "";
	
	public String getLoid()
	{
		return loid;
	}
	
	public void setLoid(String loid)
	{
		this.loid = loid;
	}
	
	public String getOldNetUserName()
	{
		return oldNetUserName;
	}
	
	public void setOldNetUserName(String oldNetUserName)
	{
		this.oldNetUserName = oldNetUserName;
	}
	
	public String getNewNetUserName()
	{
		return newNetUserName;
	}
	
	public void setNewNetUserName(String newNetUserName)
	{
		this.newNetUserName = newNetUserName;
	}
	
	public String getNewNetPwd()
	{
		return newNetPwd;
	}
	
	public void setNewNetPwd(String newNetPwd)
	{
		this.newNetPwd = newNetPwd;
	}
	
	public String getNetWanType()
	{
		return netWanType;
	}
	
	public void setNetWanType(String netWanType)
	{
		this.netWanType = netWanType;
	}
	
	public String getNetVlanId()
	{
		return netVlanId;
	}
	
	public void setNetVlanId(String netVlanId)
	{
		this.netVlanId = netVlanId;
	}
	
	public String getOldItvUserName()
	{
		return oldItvUserName;
	}
	
	public void setOldItvUserName(String oldItvUserName)
	{
		this.oldItvUserName = oldItvUserName;
	}
	
	public String getNewItvUserName()
	{
		return newItvUserName;
	}
	
	public void setNewItvUserName(String newItvUserName)
	{
		this.newItvUserName = newItvUserName;
	}
	
	public String getItvBindPort()
	{
		return itvBindPort;
	}
	
	public void setItvBindPort(String itvBindPort)
	{
		this.itvBindPort = itvBindPort;
	}
	
	public String getItvVlanId()
	{
		return itvVlanId;
	}
	
	public void setItvVlanId(String itvVlanId)
	{
		this.itvVlanId = itvVlanId;
	}
	
	public String getOldTelphone()
	{
		return oldTelphone;
	}
	
	public void setOldTelphone(String oldTelphone)
	{
		this.oldTelphone = oldTelphone;
	}
	
	public String getNewTelphone()
	{
		return newTelphone;
	}
	
	public void setNewTelphone(String newTelphone)
	{
		this.newTelphone = newTelphone;
	}
	
	public String getRegId()
	{
		return regId;
	}
	
	public void setRegId(String regId)
	{
		this.regId = regId;
	}
	
	public String getMgcIp()
	{
		return mgcIp;
	}
	
	public void setMgcIp(String mgcIp)
	{
		this.mgcIp = mgcIp;
	}
	
	public String getMgcPort()
	{
		return mgcPort;
	}
	
	public void setMgcPort(String mgcPort)
	{
		this.mgcPort = mgcPort;
	}
	
	public String getStandMgcIp()
	{
		return standMgcIp;
	}
	
	public void setStandMgcIp(String standMgcIp)
	{
		this.standMgcIp = standMgcIp;
	}
	
	public String getStandMgcPort()
	{
		return standMgcPort;
	}
	
	public void setStandMgcPort(String standMgcPort)
	{
		this.standMgcPort = standMgcPort;
	}
	
	public String getRegIdType()
	{
		return regIdType;
	}
	
	public void setRegIdType(String regIdType)
	{
		this.regIdType = regIdType;
	}
	
	public String getVoiceVlanId()
	{
		return voiceVlanId;
	}
	
	public void setVoiceVlanId(String voiceVlanId)
	{
		this.voiceVlanId = voiceVlanId;
	}
	
	public String getVoiceWanType()
	{
		return voiceWanType;
	}
	
	public void setVoiceWanType(String voiceWanType)
	{
		this.voiceWanType = voiceWanType;
	}
	
	public String getVoiceIp()
	{
		return voiceIp;
	}
	
	public void setVoiceIp(String voiceIp)
	{
		this.voiceIp = voiceIp;
	}
	
	public String getVoiceIpMask()
	{
		return voiceIpMask;
	}
	
	public void setVoiceIpMask(String voiceIpMask)
	{
		this.voiceIpMask = voiceIpMask;
	}
	
	public String getVoiceGateway()
	{
		return voiceGateway;
	}
	
	public void setVoiceGateway(String voiceGateway)
	{
		this.voiceGateway = voiceGateway;
	}
	
	public String getVoiceDns()
	{
		return voiceDns;
	}
	
	public void setVoiceDns(String voiceDns)
	{
		this.voiceDns = voiceDns;
	}
	
	public String getVoipPort()
	{
		return voipPort;
	}
	
	public void setVoipPort(String voipPort)
	{
		this.voipPort = voipPort;
	}
	
	public String getProtocol()
	{
		return protocol;
	}
	
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	
	public String getCityId()
	{
		return cityId;
	}

	
	
	public long getUserId()
	{
		return userId;
	}

	
	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public String getDeviceId()
	{
		return deviceId;
	}

	
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	
	public String getOui()
	{
		return oui;
	}

	
	public void setOui(String oui)
	{
		this.oui = oui;
	}

	
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	@Override
	public String toString()
	{
		return "BachServObj [loid=" + loid + ", oldNetUserName=" + oldNetUserName
				+ ", newNetUserName=" + newNetUserName + ", newNetPwd=" + newNetPwd
				+ ", netWanType=" + netWanType + ", netVlanId=" + netVlanId
				+ ", oldItvUserName=" + oldItvUserName + ", newItvUserName="
				+ newItvUserName + ", itvBindPort=" + itvBindPort + ", itvVlanId="
				+ itvVlanId + ", oldTelphone=" + oldTelphone + ", newTelphone="
				+ newTelphone + ", regId=" + regId + ", mgcIp=" + mgcIp + ", mgcPort="
				+ mgcPort + ", standMgcIp=" + standMgcIp + ", standMgcPort="
				+ standMgcPort + ", regIdType=" + regIdType + ", voiceVlanId="
				+ voiceVlanId + ", voiceWanType=" + voiceWanType + ", voiceIp=" + voiceIp
				+ ", voiceIpMask=" + voiceIpMask + ", voiceGateway=" + voiceGateway
				+ ", voiceDns=" + voiceDns + ", voipPort=" + voipPort + ", protocol="
				+ protocol + ", cityId=" + cityId + "]";
	}
	
}

