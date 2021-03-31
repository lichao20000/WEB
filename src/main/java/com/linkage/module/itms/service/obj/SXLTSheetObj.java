package com.linkage.module.itms.service.obj;

import java.util.List;
import java.util.Map;

public class SXLTSheetObj {
	private String cmdId;
	private String authUser;
	private String authPwd;
	private String userServTypeId;
	private String userOperateId;
	private String dealDate;
	private String userType;
	private String oltFactory;
	private String deviceType;
	private String netNum;
	private String iptv_Num;
	private String voipNum;
	private String loid;
	private String cityId;
	private String officeId;
	private String areaId;
	private String accessStyle;
	private String linkman;
	private String linkPhone;
	private String email;
	private String mobile;
	private String linkAddress;
	private String linkmanCredno;
	private String customerId;
	private String customerAccount;
	private String customerPwd;
	private String specId;
	private String netServTypeId;
	private String netOperateId;
	private String netUsername;
	private String OldNetUsername;
	private String netPasswd;
	private String netVlanId;
	private String oldNetVlanId;
	private String netWanType;
	private String netIpaddress;
	private String netIpmask;
	private String netGateway;
	private String netIpdns;
	private String netPort;
	private String oldNetPort;
	private String netSpeed;
	private String standNetIpdns;
	private String hvoipServTypeId;
	private String hvoipOperateId;
	private String hvoipPhone;
	private String hvoipRegId;
	private String hvoipRegIdType;
	private String hvoipMgcIp;
	private String hvoipMgcPort;
	private String hvoipStandMgcIp;
	private String hvoipStandMgcPort;
	private String hvoipPort;
	private String hvoipVlanId;
	private String hvoipWanType;
	private String hvoipIpaddress;
	private String hvoipIpmask;
	private String hvoipGateway;
	private String hvoipIpdns;
	private String hvoipEid;
	private String deviceName;
	private String dscpMark;
	private String sipServTypeId;
	private String sipOperateId;
	private String sipVoipPhone;
	private String sipVoipUsername;
	private String sipVoipPwd;
	private String sipProxServ;
	private String sipProxPort;
	private String sipStandProxServ;
	private String sipStandProxPort;
	private String sipVoipPort;
	private String sipRegiServ;
	private String sipRegiPort;
	private String sipStandRegiServ;
	private String sipStandRegiPort;
	private String sipOutBoundProxy;
	private String sipOutBoundPort;
	private String sipStandOutBoundProxy;
	private String sipStandOutBoundPort;
	private String sipProtocol;
	private String sipVlanId;
	private String sipWanType;
	private String sipIpaddress;
	private String sipIpmask;
	private String sipGateway;
	private String sipIpdns;
	private String sipVoipUri;
	private String sipPortNum;
	private String sipUserAgentDomain;
	private String iptvWanType;
	private String iptvServTypeId;
	private String iptvOperateId;
	private String iptvUserName;
	private String iptvPasswd;
	private String iptvPort;
	private String iptvVlanId;

	private String wifiServTypeId;
	private String wifiOperateId;
	private String ssidName;
	private String wifiNum;
	private String wifiUsername;
	private String wifiPassword;
	private String wifiVlanId;
	private String wifiWanType;

	private String vpdnServTypeId;
	private String vpdnOperateId;
	private String vpdnUserName;
	private String vpdnPort;
	private String vpdnVlanId;
	private String vpdnNum;

	private String multicastVlan;
	private String iptvDestIp;
	private String iptvDestMark;
	private String iptvNum;
	private String netOltFactory;
	private String OldNetOltFactory;
	private String iptvOltFactory;
	private String hvoipOltFactory;

	private String stbUserID;
	private String stbUserPwd;
	private String stbNTP1;
	private String stbNTP2;
	private String stbBrowserURL1;
	private String stbServ;
	private List<Map<String, String>> lineId = null;
	private List lineList = null;

	@Override
	public String toString() {
		return "SXLTSheetObj{" + "cmdId='" + cmdId + '\'' + ", authUser='" + authUser + '\'' + ", authPwd='" + authPwd
				+ '\'' + ", userServTypeId='" + userServTypeId + '\'' + ", userOperateId='" + userOperateId + '\''
				+ ", dealDate='" + dealDate + '\'' + ", userType='" + userType + '\'' + ", oltFactory='" + oltFactory
				+ '\'' + ", deviceType='" + deviceType + '\'' + ", netNum='" + netNum + '\'' + ", iptv_Num=" + iptv_Num
				+ ", voipNum=" + voipNum + ", loid='" + loid + '\'' + ", cityId='" + cityId + '\'' + ", officeId='"
				+ officeId + '\'' + ", areaId='" + areaId + '\'' + ", accessStyle='" + accessStyle + '\''
				+ ", linkman='" + linkman + '\'' + ", linkPhone='" + linkPhone + '\'' + ", email='" + email + '\''
				+ ", mobile='" + mobile + '\'' + ", linkAddress='" + linkAddress + '\'' + ", linkmanCredno='"
				+ linkmanCredno + '\'' + ", customerId='" + customerId + '\'' + ", customerAccount='" + customerAccount
				+ '\'' + ", customerPwd='" + customerPwd + '\'' + ", specId='" + specId + '\'' + ", netServTypeId='"
				+ netServTypeId + '\'' + ", netOperateId='" + netOperateId + '\'' + ", netUsername='" + netUsername
				+ '\'' + ", netPasswd='" + netPasswd + '\'' + ", netVlanId='" + netVlanId + '\'' + ", netWanType='"
				+ netWanType + '\'' + ", netIpaddress='" + netIpaddress + '\'' + ", netIpmask='" + netIpmask + '\''
				+ ", netGateway='" + netGateway + '\'' + ", netIpdns='" + netIpdns + '\'' + ", netPort='" + netPort
				+ '\'' + ", netSpeed='" + netSpeed + '\'' + ", standNetIpdns='" + standNetIpdns + '\''
				+ ", hvoipServTypeId='" + hvoipServTypeId + '\'' + ", hvoipOperateId='" + hvoipOperateId + '\''
				+ ", hvoipPhone='" + hvoipPhone + '\'' + ", hvoipRegId='" + hvoipRegId + '\'' + ", hvoipRegIdType='"
				+ hvoipRegIdType + '\'' + ", hvoipMgcIp='" + hvoipMgcIp + '\'' + ", hvoipMgcPort='" + hvoipMgcPort
				+ '\'' + ", hvoipStandMgcIp='" + hvoipStandMgcIp + '\'' + ", hvoipStandMgcPort='" + hvoipStandMgcPort
				+ '\'' + ", hvoipPort='" + hvoipPort + '\'' + ", hvoipVlanId='" + hvoipVlanId + '\''
				+ ", hvoipWanType='" + hvoipWanType + '\'' + ", hvoipIpaddress='" + hvoipIpaddress + '\''
				+ ", hvoipIpmask='" + hvoipIpmask + '\'' + ", hvoipGateway='" + hvoipGateway + '\'' + ", hvoipIpdns='"
				+ hvoipIpdns + '\'' + ", hvoipEid='" + hvoipEid + '\'' + ", deviceName='" + deviceName + '\''
				+ ", dscpMark='" + dscpMark + '\'' + ", sipServTypeId='" + sipServTypeId + '\'' + ", sipOperateId='"
				+ sipOperateId + '\'' + ", sipVoipPhone='" + sipVoipPhone + '\'' + ", sipVoipUsername='"
				+ sipVoipUsername + '\'' + ", sipVoipPwd='" + sipVoipPwd + '\'' + ", sipProxServ='" + sipProxServ + '\''
				+ ", sipProxPort='" + sipProxPort + '\'' + ", sipStandProxServ='" + sipStandProxServ + '\''
				+ ", sipStandProxPort='" + sipStandProxPort + '\'' + ", sipVoipPort='" + sipVoipPort + '\''
				+ ", sipRegiServ='" + sipRegiServ + '\'' + ", sipRegiPort='" + sipRegiPort + '\''
				+ ", sipStandRegiServ='" + sipStandRegiServ + '\'' + ", sipStandRegiPort='" + sipStandRegiPort + '\''
				+ ", sipOutBoundProxy='" + sipOutBoundProxy + '\'' + ", sipOutBoundPort='" + sipOutBoundPort + '\''
				+ ", sipStandOutBoundProxy='" + sipStandOutBoundProxy + '\'' + ", sipStandOutBoundPort='"
				+ sipStandOutBoundPort + '\'' + ", sipProtocol='" + sipProtocol + '\'' + ", sipVlanId='" + sipVlanId
				+ '\'' + ", sipWanType='" + sipWanType + '\'' + ", sipIpaddress='" + sipIpaddress + '\''
				+ ", sipIpmask='" + sipIpmask + '\'' + ", sipGateway='" + sipGateway + '\'' + ", sipIpdns='" + sipIpdns
				+ '\'' + ", sipVoipUri='" + sipVoipUri + '\'' + ", sipPortNum='" + sipPortNum + '\''
				+ ", sipUserAgentDomain='" + sipUserAgentDomain + '\'' + ", iptvWanType='" + iptvWanType + '\''
				+ ", iptvServTypeId='" + iptvServTypeId + '\'' + ", iptvOperateId='" + iptvOperateId + '\''
				+ ", iptvUserName='" + iptvUserName + '\'' + ", iptvPasswd='" + iptvPasswd + '\'' + ", iptvPort='"
				+ iptvPort + '\'' + ", iptvVlanId='" + iptvVlanId + '\'' + ", wifiServTypeId='" + wifiServTypeId + '\''
				+ ", wifiOperateId='" + wifiOperateId + '\'' + ", ssidName='" + ssidName + '\'' + ", wifiNum='"
				+ wifiNum + '\'' + ", wifiUsername='" + wifiUsername + '\'' + ", wifiPassword='" + wifiPassword + '\''
				+ ", wifiVlanId='" + wifiVlanId + '\'' + ", wifiWanType='" + wifiWanType + '\'' + ", vpdnServTypeId='"
				+ vpdnServTypeId + '\'' + ", vpdnOperateId='" + vpdnOperateId + '\'' + ", vpdnUserName='" + vpdnUserName
				+ '\'' + ", vpdnPort='" + vpdnPort + '\'' + ", vpdnVlanId='" + vpdnVlanId + '\'' + ", vpdnNum='"
				+ vpdnNum + '\'' + ", multicastVlan='" + multicastVlan + '\'' + ", iptvDestIp='" + iptvDestIp + '\''
				+ ", iptvDestMark='" + iptvDestMark + '\'' + ", iptvNum='" + iptvNum + '\'' + ", netOltFactory='"
				+ netOltFactory + '\'' + ", iptvOltFactory='" + iptvOltFactory + '\'' + ", hvoipOltFactory='"
				+ hvoipOltFactory + '\'' + ", stbUserID='" + stbUserID + '\'' + ", stbUserPwd='" + stbUserPwd + '\''
				+ ", stbNTP1='" + stbNTP1 + '\'' + ", stbNTP2='" + stbNTP2 + '\'' + ", stbBrowserURL1='"
				+ stbBrowserURL1 + '\'' + ", stbServ='" + stbServ + '\'' + ", lineId=" + lineId + ", lineList="
				+ lineList + '}';
	}

	public String getIptvUserName() {
		return this.iptvUserName;
	}

	public void setIptvUserName(String iptvUserName) {
		this.iptvUserName = iptvUserName;
	}

	public String getIptvPort() {
		return this.iptvPort;
	}

	public void setIptvPort(String iptvPort) {
		this.iptvPort = iptvPort.replaceAll("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
	}

	public String getIptvVlanId() {
		return this.iptvVlanId;
	}

	public void setIptvVlanId(String iptvVlanId) {
		this.iptvVlanId = iptvVlanId;
	}

	public String getIptvNum() {
		return this.iptvNum;
	}

	public void setIptvNum(String iptvNum) {
		this.iptvNum = iptvNum;
	}

	public String getCmdId() {
		return this.cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public String getAuthUser() {
		return this.authUser;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	public String getAuthPwd() {
		return this.authPwd;
	}

	public void setAuthPwd(String authPwd) {
		this.authPwd = authPwd;
	}

	public String getUserServTypeId() {
		return this.userServTypeId;
	}

	public void setUserServTypeId(String userServTypeId) {
		this.userServTypeId = userServTypeId;
	}

	public String getUserOperateId() {
		return this.userOperateId;
	}

	public void setUserOperateId(String userOperateId) {
		this.userOperateId = userOperateId;
	}

	public String getDealDate() {
		return this.dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoid() {
		return this.loid;
	}

	public void setLoid(String loid) {
		this.loid = loid;
	}

	public String getCityId() {
		return this.cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAccessStyle() {
		return this.accessStyle;
	}

	public void setAccessStyle(String accessStyle) {
		this.accessStyle = accessStyle;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkPhone() {
		return this.linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLinkAddress() {
		return this.linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getLinkmanCredno() {
		return this.linkmanCredno;
	}

	public void setLinkmanCredno(String linkmanCredno) {
		this.linkmanCredno = linkmanCredno;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAccount() {
		return this.customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getCustomerPwd() {
		return this.customerPwd;
	}

	public void setCustomerPwd(String customerPwd) {
		this.customerPwd = customerPwd;
	}

	public String getSpecId() {
		return this.specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getNetServTypeId() {
		return this.netServTypeId;
	}

	public void setNetServTypeId(String netServTypeId) {
		this.netServTypeId = netServTypeId;
	}

	public String getNetOperateId() {
		return this.netOperateId;
	}

	public void setNetOperateId(String netOperateId) {
		this.netOperateId = netOperateId;
	}

	public String getNetUsername() {
		return this.netUsername;
	}

	public void setNetUsername(String netUsername) {
		this.netUsername = netUsername;
	}

	public String getNetPasswd() {
		return this.netPasswd;
	}

	public void setNetPasswd(String netPasswd) {
		this.netPasswd = netPasswd;
	}

	public String getNetVlanId() {
		return this.netVlanId;
	}

	public void setNetVlanId(String netVlanId) {
		this.netVlanId = netVlanId;
	}

	public String getNetSpeed() {
		return netSpeed;
	}

	public void setNetSpeed(String netSpeed) {
		this.netSpeed = netSpeed;
	}

	public String getNetWanType() {
		return this.netWanType;
	}

	public void setNetWanType(String netWanType) {
		this.netWanType = netWanType;
	}

	public String getNetIpaddress() {
		return this.netIpaddress;
	}

	public void setNetIpaddress(String netIpaddress) {
		this.netIpaddress = netIpaddress;
	}

	public String getNetIpmask() {
		return this.netIpmask;
	}

	public void setNetIpmask(String netIpmask) {
		this.netIpmask = netIpmask;
	}

	public String getNetGateway() {
		return this.netGateway;
	}

	public void setNetGateway(String netGateway) {
		this.netGateway = netGateway;
	}

	public String getNetIpdns() {
		return this.netIpdns;
	}

	public void setNetIpdns(String netIpdns) {
		this.netIpdns = netIpdns;
	}

	public String getHvoipServTypeId() {
		return this.hvoipServTypeId;
	}

	public void setHvoipServTypeId(String hvoipServTypeId) {
		this.hvoipServTypeId = hvoipServTypeId;
	}

	public String getHvoipOperateId() {
		return this.hvoipOperateId;
	}

	public void setHvoipOperateId(String hvoipOperateId) {
		this.hvoipOperateId = hvoipOperateId;
	}

	public String getHvoipPhone() {
		return this.hvoipPhone;
	}

	public void setHvoipPhone(String hvoipPhone) {
		this.hvoipPhone = hvoipPhone;
	}

	public String getHvoipRegId() {
		return this.hvoipRegId;
	}

	public void setHvoipRegId(String hvoipRegId) {
		this.hvoipRegId = hvoipRegId;
	}

	public String getHvoipRegIdType() {
		return this.hvoipRegIdType;
	}

	public void setHvoipRegIdType(String hvoipRegIdType) {
		this.hvoipRegIdType = hvoipRegIdType;
	}

	public String getHvoipMgcIp() {
		return this.hvoipMgcIp;
	}

	public void setHvoipMgcIp(String hvoipMgcIp) {
		this.hvoipMgcIp = hvoipMgcIp;
	}

	public String getHvoipMgcPort() {
		return this.hvoipMgcPort;
	}

	public void setHvoipMgcPort(String hvoipMgcPort) {
		this.hvoipMgcPort = hvoipMgcPort;
	}

	public String getHvoipStandMgcIp() {
		return this.hvoipStandMgcIp;
	}

	public void setHvoipStandMgcIp(String hvoipStandMgcIp) {
		this.hvoipStandMgcIp = hvoipStandMgcIp;
	}

	public String getHvoipStandMgcPort() {
		return this.hvoipStandMgcPort;
	}

	public void setHvoipStandMgcPort(String hvoipStandMgcPort) {
		this.hvoipStandMgcPort = hvoipStandMgcPort;
	}

	public String getHvoipPort() {
		return this.hvoipPort;
	}

	public void setHvoipPort(String hvoipPort) {
		this.hvoipPort = hvoipPort;
	}

	public String getHvoipVlanId() {
		return this.hvoipVlanId;
	}

	public void setHvoipVlanId(String hvoipVlanId) {
		this.hvoipVlanId = hvoipVlanId;
	}

	public String getHvoipWanType() {
		return this.hvoipWanType;
	}

	public void setHvoipWanType(String hvoipWanType) {
		this.hvoipWanType = hvoipWanType;
	}

	public String getHvoipIpaddress() {
		return this.hvoipIpaddress;
	}

	public void setHvoipIpaddress(String hvoipIpaddress) {
		this.hvoipIpaddress = hvoipIpaddress;
	}

	public String getHvoipIpmask() {
		return this.hvoipIpmask;
	}

	public void setHvoipIpmask(String hvoipIpmask) {
		this.hvoipIpmask = hvoipIpmask;
	}

	public String getHvoipGateway() {
		return this.hvoipGateway;
	}

	public void setHvoipGateway(String hvoipGateway) {
		this.hvoipGateway = hvoipGateway;
	}

	public String getHvoipIpdns() {
		return this.hvoipIpdns;
	}

	public void setHvoipIpdns(String hvoipIpdns) {
		this.hvoipIpdns = hvoipIpdns;
	}

	public String getSipServTypeId() {
		return this.sipServTypeId;
	}

	public void setSipServTypeId(String sipServTypeId) {
		this.sipServTypeId = sipServTypeId;
	}

	public String getSipOperateId() {
		return this.sipOperateId;
	}

	public void setSipOperateId(String sipOperateId) {
		this.sipOperateId = sipOperateId;
	}

	public String getSipVoipPhone() {
		return this.sipVoipPhone;
	}

	public void setSipVoipPhone(String sipVoipPhone) {
		this.sipVoipPhone = sipVoipPhone;
	}

	public String getSipVoipPwd() {
		return this.sipVoipPwd;
	}

	public void setSipVoipPwd(String sipVoipPwd) {
		this.sipVoipPwd = sipVoipPwd;
	}

	public String getSipProxServ() {
		return this.sipProxServ;
	}

	public void setSipProxServ(String sipProxServ) {
		this.sipProxServ = sipProxServ;
	}

	public String getSipProxPort() {
		return this.sipProxPort;
	}

	public void setSipProxPort(String sipProxPort) {
		this.sipProxPort = sipProxPort;
	}

	public String getSipStandProxServ() {
		return this.sipStandProxServ;
	}

	public void setSipStandProxServ(String sipStandProxServ) {
		this.sipStandProxServ = sipStandProxServ;
	}

	public String getSipStandProxPort() {
		return this.sipStandProxPort;
	}

	public void setSipStandProxPort(String sipStandProxPort) {
		this.sipStandProxPort = sipStandProxPort;
	}

	public String getSipVoipPort() {
		return this.sipVoipPort;
	}

	public void setSipVoipPort(String sipVoipPort) {
		this.sipVoipPort = sipVoipPort;
	}

	public String getSipRegiServ() {
		return this.sipRegiServ;
	}

	public void setSipRegiServ(String sipRegiServ) {
		this.sipRegiServ = sipRegiServ;
	}

	public String getSipRegiPort() {
		return this.sipRegiPort;
	}

	public void setSipRegiPort(String sipRegiPort) {
		this.sipRegiPort = sipRegiPort;
	}

	public String getSipStandRegiServ() {
		return this.sipStandRegiServ;
	}

	public void setSipStandRegiServ(String sipStandRegiServ) {
		this.sipStandRegiServ = sipStandRegiServ;
	}

	public String getSipStandRegiPort() {
		return this.sipStandRegiPort;
	}

	public void setSipStandRegiPort(String sipStandRegiPort) {
		this.sipStandRegiPort = sipStandRegiPort;
	}

	public String getSipOutBoundProxy() {
		return this.sipOutBoundProxy;
	}

	public void setSipOutBoundProxy(String sipOutBoundProxy) {
		this.sipOutBoundProxy = sipOutBoundProxy;
	}

	public String getSipOutBoundPort() {
		return this.sipOutBoundPort;
	}

	public void setSipOutBoundPort(String sipOutBoundPort) {
		this.sipOutBoundPort = sipOutBoundPort;
	}

	public String getSipStandOutBoundProxy() {
		return this.sipStandOutBoundProxy;
	}

	public void setSipStandOutBoundProxy(String sipStandOutBoundProxy) {
		this.sipStandOutBoundProxy = sipStandOutBoundProxy;
	}

	public String getSipStandOutBoundPort() {
		return this.sipStandOutBoundPort;
	}

	public void setSipStandOutBoundPort(String sipStandOutBoundPort) {
		this.sipStandOutBoundPort = sipStandOutBoundPort;
	}

	public String getSipProtocol() {
		return this.sipProtocol;
	}

	public void setSipProtocol(String sipProtocol) {
		this.sipProtocol = sipProtocol;
	}

	public String getSipVlanId() {
		return this.sipVlanId;
	}

	public void setSipVlanId(String sipVlanId) {
		this.sipVlanId = sipVlanId;
	}

	public String getSipWanType() {
		return this.sipWanType;
	}

	public void setSipWanType(String sipWanType) {
		this.sipWanType = sipWanType;
	}

	public String getSipIpaddress() {
		return this.sipIpaddress;
	}

	public void setSipIpaddress(String sipIpaddress) {
		this.sipIpaddress = sipIpaddress;
	}

	public String getSipIpmask() {
		return this.sipIpmask;
	}

	public void setSipIpmask(String sipIpmask) {
		this.sipIpmask = sipIpmask;
	}

	public String getSipGateway() {
		return this.sipGateway;
	}

	public void setSipGateway(String sipGateway) {
		this.sipGateway = sipGateway;
	}

	public String getSipIpdns() {
		return this.sipIpdns;
	}

	public void setSipIpdns(String sipIpdns) {
		this.sipIpdns = sipIpdns;
	}

	public String getSipVoipUri() {
		return this.sipVoipUri;
	}

	public void setSipVoipUri(String sipVoipUri) {
		this.sipVoipUri = sipVoipUri;
	}

	public String getSipUserAgentDomain() {
		return this.sipUserAgentDomain;
	}

	public void setSipUserAgentDomain(String sipUserAgentDomain) {
		this.sipUserAgentDomain = sipUserAgentDomain;
	}

	public String getSipVoipUsername() {
		return this.sipVoipUsername;
	}

	public void setSipVoipUsername(String sipVoipUsername) {
		this.sipVoipUsername = sipVoipUsername;
	}

	public String getStandNetIpdns() {
		return this.standNetIpdns;
	}

	public void setStandNetIpdns(String standNetIpdns) {
		this.standNetIpdns = standNetIpdns;
	}

	public String getIptvServTypeId() {
		return this.iptvServTypeId;
	}

	public void setIptvServTypeId(String iptvServTypeId) {
		this.iptvServTypeId = iptvServTypeId;
	}

	public String getIptvOperateId() {
		return this.iptvOperateId;
	}

	public void setIptvOperateId(String iptvOperateId) {
		this.iptvOperateId = iptvOperateId;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDscpMark() {
		return this.dscpMark;
	}

	public void setDscpMark(String dscpMark) {
		this.dscpMark = dscpMark;
	}

	public String getNetPort() {
		return this.netPort;
	}

	public void setNetPort(String netPort) {
		this.netPort = netPort.replaceAll("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
	}

	public String getOldNetPort() {
		return oldNetPort;
	}

	public void setOldNetPort(String oldNetPort) {
		this.oldNetPort = netPort.replaceAll("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
	}

	public String getOltFactory() {
		return this.oltFactory;
	}

	public void setOltFactory(String oltFactory) {
		this.oltFactory = oltFactory;
	}

	public String getIptvPasswd() {
		return this.iptvPasswd;
	}

	public void setIptvPasswd(String iptvPasswd) {
		this.iptvPasswd = iptvPasswd;
	}

	public String getMulticastVlan() {
		return this.multicastVlan;
	}

	public String getIptvDestIp() {
		return this.iptvDestIp;
	}

	public String getIptvDestMark() {
		return this.iptvDestMark;
	}

	public void setMulticastVlan(String multicastVlan) {
		this.multicastVlan = multicastVlan;
	}

	public void setIptvDestIp(String iptvDestIp) {
		this.iptvDestIp = iptvDestIp;
	}

	public void setIptvDestMark(String iptvDestMark) {
		this.iptvDestMark = iptvDestMark;
	}

	public String getIptvWanType() {
		return this.iptvWanType;
	}

	public void setIptvWanType(String iptvWanType) {
		this.iptvWanType = iptvWanType;
	}

	public String getHvoipEid() {
		return this.hvoipEid;
	}

	public void setHvoipEid(String hvoipEid) {
		this.hvoipEid = hvoipEid;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public String getNetNum() {
		return this.netNum;
	}

	public String getIptv_Num() {
		return this.iptv_Num;
	}

	public String getVoipNum() {
		return this.voipNum;
	}

	public void setNetNum(String netNum) {
		this.netNum = netNum;
	}

	public void setIptv_Num(String iptv_Num) {
		this.iptv_Num = iptv_Num;
	}

	public void setVoipNum(String voipNum) {
		this.voipNum = voipNum;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getNetOltFactory() {
		return this.netOltFactory;
	}

	public String getIptvOltFactory() {
		return this.iptvOltFactory;
	}

	public String getHvoipOltFactory() {
		return this.hvoipOltFactory;
	}

	public void setNetOltFactory(String netOltFactory) {
		this.netOltFactory = netOltFactory;
	}

	public void setIptvOltFactory(String iptvOltFactory) {
		this.iptvOltFactory = iptvOltFactory;
	}

	public void setHvoipOltFactory(String hvoipOltFactory) {
		this.hvoipOltFactory = hvoipOltFactory;
	}

	public String getStbUserID() {
		return stbUserID;
	}

	public void setStbUserID(String stbUserID) {
		this.stbUserID = stbUserID;
	}

	public String getStbUserPwd() {
		return stbUserPwd;
	}

	public void setStbUserPwd(String stbUserPwd) {
		this.stbUserPwd = stbUserPwd;
	}

	public String getStbNTP1() {
		return stbNTP1;
	}

	public void setStbNTP1(String stbNTP1) {
		this.stbNTP1 = stbNTP1;
	}

	public String getStbNTP2() {
		return stbNTP2;
	}

	public void setStbNTP2(String stbNTP2) {
		this.stbNTP2 = stbNTP2;
	}

	public String getStbBrowserURL1() {
		return stbBrowserURL1;
	}

	public void setStbBrowserURL1(String stbBrowserURL1) {
		this.stbBrowserURL1 = stbBrowserURL1;
	}

	public String getStbServ() {
		return stbServ;
	}

	public void setStbServ(String stbServ) {
		this.stbServ = stbServ;
	}

	public List<Map<String, String>> getLineId() {
		return lineId;
	}

	public void setLineId(List<Map<String, String>> lineId) {
		this.lineId = lineId;
	}

	public List getLineList() {
		return lineList;
	}

	public void setLineList(List lineList) {
		this.lineList = lineList;
	}

	public String getVpdnServTypeId() {
		return vpdnServTypeId;
	}

	public void setVpdnServTypeId(String vpdnServTypeId) {
		this.vpdnServTypeId = vpdnServTypeId;
	}

	public String getVpdnOperateId() {
		return vpdnOperateId;
	}

	public void setVpdnOperateId(String vpdnOperateId) {
		this.vpdnOperateId = vpdnOperateId;
	}

	public String getVpdnUserName() {
		return vpdnUserName;
	}

	public void setVpdnUserName(String vpdnUserName) {
		this.vpdnUserName = vpdnUserName;
	}

	public String getVpdnPort() {
		return vpdnPort;
	}

	public void setVpdnPort(String vpdnPort) {
		this.vpdnPort = vpdnPort.replaceAll("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
	}

	public String getVpdnVlanId() {
		return vpdnVlanId;
	}

	public void setVpdnVlanId(String vpdnVlanId) {
		this.vpdnVlanId = vpdnVlanId;
	}

	public String getVpdnNum() {
		return vpdnNum;
	}

	public void setVpdnNum(String vpdnNum) {
		this.vpdnNum = vpdnNum;
	}

	public String getSipPortNum() {
		return sipPortNum;
	}

	public void setSipPortNum(String sipPortNum) {
		this.sipPortNum = sipPortNum;
	}

	public String getWifiServTypeId() {
		return wifiServTypeId;
	}

	public void setWifiServTypeId(String wifiServTypeId) {
		this.wifiServTypeId = wifiServTypeId;
	}

	public String getWifiOperateId() {
		return wifiOperateId;
	}

	public void setWifiOperateId(String wifiOperateId) {
		this.wifiOperateId = wifiOperateId;
	}

	public String getSsidName() {
		return ssidName;
	}

	public void setSsidName(String ssidName) {
		this.ssidName = ssidName;
	}

	public String getWifiNum() {
		return wifiNum;
	}

	public void setWifiNum(String wifiNum) {
		this.wifiNum = wifiNum;
	}

	public String getWifiUsername() {
		return wifiUsername;
	}

	public void setWifiUsername(String wifiUsername) {
		this.wifiUsername = wifiUsername;
	}

	public String getWifiPassword() {
		return wifiPassword;
	}

	public void setWifiPassword(String wifiPassword) {
		this.wifiPassword = wifiPassword;
	}

	public String getWifiVlanId() {
		return wifiVlanId;
	}

	public void setWifiVlanId(String wifiVlanId) {
		this.wifiVlanId = wifiVlanId;
	}

	public String getWifiWanType() {
		return wifiWanType;
	}

	public void setWifiWanType(String wifiWanType) {
		this.wifiWanType = wifiWanType;
	}

	public String getOldNetUsername() {
		return OldNetUsername;
	}

	public void setOldNetUsername(String oldNetUsername) {
		OldNetUsername = oldNetUsername;
	}

	public String getOldNetVlanId() {
		return oldNetVlanId;
	}

	public void setOldNetVlanId(String oldNetVlanId) {
		this.oldNetVlanId = oldNetVlanId;
	}

	public String getOldNetOltFactory() {
		return OldNetOltFactory;
	}

	public void setOldNetOltFactory(String oldNetOltFactory) {
		OldNetOltFactory = oldNetOltFactory;
	}

}