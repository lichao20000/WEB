package com.linkage.module.itms.service.obj;

import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;

public class SheetObj
{
  private String cmdId = "";
  private String authUser = "itms";
  private String authPwd = "123";
  private String userServTypeId = "";
  private String userOperateId = "";
  private String dealDate = "";
  private String userType = "";
  private String oltFactory = "";
  private String deviceType = "";
  private String netNum = "";
  private int iptv_Num = 0;
  private int voipNum = 0;
  private String loid = "";
  private String cityId = "";
  private String officeId = "";
  private String areaId = "";
  private String accessStyle = "";
  private String linkman = "";
  private String linkPhone = "";
  private String email = "";
  private String mobile = "";
  private String linkAddress = "";
  private String linkmanCredno = "";
  private String customerId = "";
  private String customerAccount = "";
  private String customerPwd = "";
  private String specId = "";
  private String netServTypeId = "";
  private String netOperateId = "";
  private String netUsername = "";
  private String netPasswd = "";
  private String netVlanId = "";
  private String netWanType = "";
  private String netIpaddress = "";
  private String netIpmask = "";
  private String netGateway = "";
  private String netIpdns = "";
  private String netPort = "";
  private String netSpeed = "";
  private String standNetIpdns = "";
  private String voipPwd = "";
  private String hvoipServTypeId = "";
  private String hvoipOperateId = "";
  private String hvoipPhone = "";
  private String hvoipRegId = "";
  private String hvoipRegIdType = "";
  private String hvoipMgcIp = "";
  private String hvoipMgcPort = "";
  private String hvoipStandMgcIp = "";
  private String hvoipStandMgcPort = "";
  private String hvoipPort = "";
  private String hvoipVlanId = "";
  private String hvoipWanType = "";
  private String hvoipIpaddress = "";
  private String hvoipIpmask = "";
  private String hvoipGateway = "";
  private String hvoipIpdns = "";
  private String hvoipEid = "";
  private String hvoipLineName= "";
  private String hvoipRtpPrefix = "";
  private String deviceName = "";
  private String dscpMark = "";
  private String sipServTypeId = "";
  private String sipOperateId = "";
  private String sipVoipPhone = "";
  private String sipVoipUsername = "";
  private String sipVoipPwd = "";
  private String sipProxServ = "";
  private String sipProxPort = "";
  private String sipStandProxServ = "";
  private String sipStandProxPort = "";
  private String sipVoipPort = "";
  private String sipRegiServ = "";
  private String sipRegiPort = "";
  private String sipStandRegiServ = "";
  private String sipStandRegiPort = "";
  private String sipOutBoundProxy = "";
  private String sipOutBoundPort = "";
  private String sipStandOutBoundProxy = "";
  private String sipStandOutBoundPort = "";
  private String sipProtocol = "";
  private String sipVlanId = "";
  private String sipWanType = "";
  private String sipIpaddress = "";
  private String sipIpmask = "";
  private String sipGateway = "";
  private String sipIpdns = "";
  private String sipVoipUri = "";
  private String sipPortNum = "";
  private String sipUserAgentDomain = "";
  private String iptvWanType = "";
  private String iptvServTypeId = "";
  private String iptvOperateId = "";
  private String iptvUserName = "";
  private String iptvPasswd = "";
  private String iptvPort = "";
  private String iptvVlanId = "";
  
  private String wifiServTypeId = "";
  private String wifiOperateId = "";
  private String ssidName = "";
  private String wifiNum = "";
  private String wifiUsername = "";
  private String wifiPassword = "";
  private String wifiVlanId = "";
  private String wifiWanType = "";
  
  
  
  private String vpdnServTypeId = "";
  private String vpdnOperateId = "";
  private String vpdnUserName = "";
  private String vpdnPort = "";
  private String vpdnVlanId = "";
  private String vpdnNum = "";
  
  private String multicastVlan = "";
  private String iptvDestIp = "";
  private String iptvDestMark = "";
  private String iptvNum = "";
  private String netOltFactory = "";
  private String iptvOltFactory = "";
  private String hvoipOltFactory = "";
  
  private String stbUserID = "";
  private String stbUserPwd = "";
  private String stbNTP1 = "";
  private String stbNTP2 = "";
  private String stbBrowserURL1 = "";
  private String stbServ = "";
  private List<Map<String, String>> lineId = null;
  private List lineList = null;

  private String cloudNetAccount = "";
  private String cloudNetPass = "";
  private String cloudNetUpBandwidth = "";
  private String cloudNetDownBandwidth = "";
  private String cloudNetVlanId = "";
  private String cloudNetDscp = "";
  private String cloudNetAppType = "";
  private String cloudNetOperateId = "";
  private String cloudNetServTypeId="";


  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getCmdId()).append("|");//0
    buffer.append(getAuthUser()).append("|");
    buffer.append(getAuthPwd()).append("|");
    buffer.append(getUserServTypeId()).append("|");
    buffer.append(getUserOperateId()).append("|");
    buffer.append(getDealDate()).append("|");//5
    buffer.append(getUserType()).append("|");
    buffer.append(getLoid()).append("|");
    buffer.append(getCityId()).append("|");
    buffer.append(getOfficeId()).append("|");
    buffer.append(getAreaId()).append("|");//10
    buffer.append(getAccessStyle()).append("|");
    buffer.append(getLinkman()).append("|");
    buffer.append(getLinkPhone()).append("|");
    buffer.append(getEmail()).append("|");
    buffer.append(getMobile()).append("|");//15
    buffer.append(getLinkAddress()).append("|");
    buffer.append(getLinkmanCredno()).append("|");
    buffer.append(getCustomerId()).append("|");
    buffer.append(getCustomerAccount()).append("|");
    buffer.append(getSpecId()).append("|");//20
    
    buffer.append(getNetServTypeId()).append("|");
    buffer.append(getNetOperateId()).append("|");
    buffer.append(getNetUsername()).append("|");
    buffer.append(getNetPasswd()).append("|");
    buffer.append(getNetVlanId()).append("|");//25
    buffer.append(getNetWanType()).append("|");
    buffer.append(getNetIpaddress()).append("|");
    buffer.append(getNetIpmask()).append("|");
    buffer.append(getNetGateway()).append("|");
    buffer.append(getNetIpdns()).append("|");//30
    
    buffer.append(getHvoipServTypeId()).append("|");
    buffer.append(getHvoipOperateId()).append("|");
    buffer.append(getHvoipPhone()).append("|");
    buffer.append(getHvoipRegId()).append("|");
    buffer.append(getHvoipRegIdType()).append("|");//35
    buffer.append(getHvoipMgcIp()).append("|");
    buffer.append(getHvoipMgcPort()).append("|");
    buffer.append(getHvoipStandMgcIp()).append("|");
    buffer.append(getHvoipStandMgcPort()).append("|");
    buffer.append(getHvoipPort()).append("|");//40	
    buffer.append(getHvoipVlanId()).append("|");
    buffer.append(getHvoipWanType()).append("|");
    buffer.append(getHvoipIpaddress()).append("|");
    buffer.append(getHvoipIpmask()).append("|");
    buffer.append(getHvoipGateway()).append("|");//45
    buffer.append(getHvoipIpdns()).append("|");
    if(LipossGlobals.inArea(Global.ZJLT))
    {
        buffer.append(getHvoipLineName()).append("|");
    }
    buffer.append(getSipServTypeId()).append("|");
    buffer.append(getSipOperateId()).append("|");
    buffer.append(getSipVoipPhone()).append("|");//50
    buffer.append(getSipVoipUsername()).append("|");
    buffer.append("|");//隐藏密码getSipVoipPwd()
    buffer.append(getSipProxServ()).append("|");
    buffer.append(getSipProxPort()).append("|");
    buffer.append(getSipStandProxServ()).append("|");
    buffer.append(getSipStandProxPort()).append("|");//55
    buffer.append(getSipVoipPort()).append("|");
    buffer.append(getSipRegiServ()).append("|");
    buffer.append(getSipRegiPort()).append("|");
    buffer.append(getSipStandRegiServ()).append("|");
    buffer.append(getSipStandRegiPort()).append("|");//60
    buffer.append(getSipOutBoundProxy()).append("|");
    buffer.append(getSipOutBoundPort()).append("|");
    buffer.append(getSipStandOutBoundProxy()).append("|");
    buffer.append(getSipStandOutBoundPort()).append("|");
    buffer.append(getSipProtocol()).append("|");//65
    buffer.append(getSipVlanId()).append("|");
    buffer.append(getSipWanType()).append("|");
    buffer.append(getSipIpaddress()).append("|");
    buffer.append(getSipIpmask()).append("|");
    buffer.append(getSipGateway()).append("|");//70
    buffer.append(getSipIpdns()).append("|");
    buffer.append(getSipVoipUri()).append("|");
    buffer.append(getSipUserAgentDomain()).append("|");
    buffer.append(getStandNetIpdns()).append("|");
    
    buffer.append(getIptvServTypeId()).append("|");//75
    buffer.append(getIptvOperateId()).append("|");
    buffer.append(getIptvUserName()).append("|");
    buffer.append(getIptvPort()).append("|");
    buffer.append(getIptvVlanId()).append("|");
    buffer.append(getIptvNum()).append("|");//80

    buffer.append(getDeviceName()).append("|");
    buffer.append(getDscpMark()).append("|");
    buffer.append(getNetPort()).append("|");

    buffer.append(getDeviceType()).append("|");
    buffer.append(getOltFactory()).append("|");//85

    buffer.append(getIptvWanType()).append("|");
    buffer.append(getMulticastVlan()).append("|");
    buffer.append(getIptvPasswd()).append("|");
    buffer.append(getIptvDestIp()).append("|");
    buffer.append(getIptvDestMark()).append("|");//90

    buffer.append(getHvoipEid()).append("|");
    buffer.append(getNetNum()).append("|");
    buffer.append(getIptv_Num()).append("|");
    buffer.append(getVoipNum()).append("|");
    buffer.append(getNetOltFactory()).append("|");//95
    buffer.append(getIptvOltFactory()).append("|");
    buffer.append(getHvoipOltFactory()).append("|");
    buffer.append(getStbUserID()).append("|");
    buffer.append(getStbUserPwd()).append("|");
    buffer.append(getStbNTP1()).append("|");//100
    buffer.append(getStbNTP2()).append("|");
    buffer.append(getStbBrowserURL1()).append("|");
    buffer.append(getStbServ()).append("|");
    buffer.append(getLineList()).append("|");
    buffer.append(getNetSpeed()).append("|");//105
    
    buffer.append(getWifiServTypeId()).append("|");//106
    buffer.append(getWifiOperateId()).append("|");
    buffer.append(getWifiUsername()).append("|");
    buffer.append("|");//隐藏 密码getWifiPassword
    buffer.append(getWifiVlanId()).append("|");//110
    buffer.append(getWifiWanType());//111
    if(LipossGlobals.inArea(Global.NXLT)){
    	buffer.append("|");
    	buffer.append(getVoipPwd());
    }
    if(LipossGlobals.inArea(Global.GSDX))
    {
        buffer.append(getCloudNetAccount()).append("|");
        buffer.append(getCloudNetPass()).append("|");
        buffer.append(getCloudNetUpBandwidth()).append("|");
        buffer.append(getCloudNetDownBandwidth()).append("|");//115
        buffer.append(getCloudNetVlanId()).append("|");
        buffer.append(getCloudNetDscp()).append("|");
        buffer.append(getCloudNetAppType()).append("|");
        buffer.append(getCloudNetOperateId()).append("|");
        buffer.append(getCloudNetServTypeId());//120
    }


    return buffer.toString();
  }
  
  
  public String getIptvUserName(){
    return this.iptvUserName;
  }

  public void setIptvUserName(String iptvUserName){
    this.iptvUserName = iptvUserName;
  }

  public String getIptvPort(){
    return this.iptvPort;
  }

  public void setIptvPort(String iptvPort){
    this.iptvPort = iptvPort.replaceAll(
    		"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
  }

  public String getIptvVlanId(){
    return this.iptvVlanId;
  }

  public void setIptvVlanId(String iptvVlanId){
    this.iptvVlanId = iptvVlanId;
  }

  public String getIptvNum(){
    return this.iptvNum;
  }

  public void setIptvNum(String iptvNum){
    this.iptvNum = iptvNum;
  }

  public String getCmdId(){
    return this.cmdId;
  }

  public void setCmdId(String cmdId){
    this.cmdId = cmdId;
  }

  public String getAuthUser(){
    return this.authUser;
  }

  public void setAuthUser(String authUser){
    this.authUser = authUser;
  }

  public String getAuthPwd(){
    return this.authPwd;
  }

  public void setAuthPwd(String authPwd){
    this.authPwd = authPwd;
  }

  public String getUserServTypeId(){
    return this.userServTypeId;
  }

  public void setUserServTypeId(String userServTypeId){
    this.userServTypeId = userServTypeId;
  }

  public String getUserOperateId(){
    return this.userOperateId;
  }

  public void setUserOperateId(String userOperateId){
    this.userOperateId = userOperateId;
  }

  public String getDealDate(){
    return this.dealDate;
  }

  public void setDealDate(String dealDate){
    this.dealDate = dealDate;
  }

  public String getUserType(){
    return this.userType;
  }

  public void setUserType(String userType){
    this.userType = userType;
  }

  public String getLoid(){
    return this.loid;
  }

  public void setLoid(String loid){
    this.loid = loid;
  }

  public String getCityId(){
    return this.cityId;
  }

  public void setCityId(String cityId){
    this.cityId = cityId;
  }

  public String getOfficeId(){
    return this.officeId;
  }

  public void setOfficeId(String officeId){
    this.officeId = officeId;
  }

  public String getAreaId(){
    return this.areaId;
  }

  public void setAreaId(String areaId){
    this.areaId = areaId;
  }

  public String getAccessStyle(){
    return this.accessStyle;
  }

  public void setAccessStyle(String accessStyle){
    this.accessStyle = accessStyle;
  }

  public String getLinkman(){
    return this.linkman;
  }

  public void setLinkman(String linkman){
    this.linkman = linkman;
  }

  public String getLinkPhone(){
    return this.linkPhone;
  }

  public void setLinkPhone(String linkPhone){
    this.linkPhone = linkPhone;
  }

  public String getEmail(){
    return this.email;
  }

  public void setEmail(String email){
    this.email = email;
  }

  public String getMobile(){
    return this.mobile;
  }

  public void setMobile(String mobile){
    this.mobile = mobile;
  }

  public String getLinkAddress(){
    return this.linkAddress;
  }

  public void setLinkAddress(String linkAddress){
    this.linkAddress = linkAddress;
  }

  public String getLinkmanCredno(){
    return this.linkmanCredno;
  }

  public void setLinkmanCredno(String linkmanCredno){
    this.linkmanCredno = linkmanCredno;
  }

  public String getCustomerId(){
    return this.customerId;
  }

  public void setCustomerId(String customerId){
    this.customerId = customerId;
  }

  public String getCustomerAccount(){
    return this.customerAccount;
  }

  public void setCustomerAccount(String customerAccount){
    this.customerAccount = customerAccount;
  }

  public String getCustomerPwd(){
    return this.customerPwd;
  }

  public void setCustomerPwd(String customerPwd){
    this.customerPwd = customerPwd;
  }

  public String getSpecId(){
    return this.specId;
  }

  public void setSpecId(String specId){
    this.specId = specId;
  }

  public String getNetServTypeId(){
    return this.netServTypeId;
  }

  public void setNetServTypeId(String netServTypeId){
    this.netServTypeId = netServTypeId;
  }

  public String getNetOperateId(){
    return this.netOperateId;
  }

  public void setNetOperateId(String netOperateId){
    this.netOperateId = netOperateId;
  }

  public String getNetUsername(){
    return this.netUsername;
  }

  public void setNetUsername(String netUsername){
    this.netUsername = netUsername;
  }

  public String getNetPasswd(){
    return this.netPasswd;
  }

  public void setNetPasswd(String netPasswd){
    this.netPasswd = netPasswd;
  }

  public String getNetVlanId(){
    return this.netVlanId;
  }

  public void setNetVlanId(String netVlanId){
    this.netVlanId = netVlanId;
  }

  public String getNetSpeed(){
	return netSpeed;
  }

  public void setNetSpeed(String netSpeed){
	this.netSpeed = netSpeed;
  }

  public String getNetWanType(){
    return this.netWanType;
  }

  public void setNetWanType(String netWanType){
    this.netWanType = netWanType;
  }

  public String getNetIpaddress(){
    return this.netIpaddress;
  }

  public void setNetIpaddress(String netIpaddress){
    this.netIpaddress = netIpaddress;
  }

  public String getNetIpmask(){
    return this.netIpmask;
  }

  public void setNetIpmask(String netIpmask){
    this.netIpmask = netIpmask;
  }

  public String getNetGateway(){
    return this.netGateway;
  }

  public void setNetGateway(String netGateway){
    this.netGateway = netGateway;
  }

  public String getNetIpdns(){
    return this.netIpdns;
  }

  public void setNetIpdns(String netIpdns){
    this.netIpdns = netIpdns;
  }

  public String getHvoipServTypeId(){
    return this.hvoipServTypeId;
  }

  public void setHvoipServTypeId(String hvoipServTypeId){
    this.hvoipServTypeId = hvoipServTypeId;
  }

  public String getHvoipOperateId(){
    return this.hvoipOperateId;
  }

  public void setHvoipOperateId(String hvoipOperateId){
    this.hvoipOperateId = hvoipOperateId;
  }

  public String getHvoipPhone(){
    return this.hvoipPhone;
  }

  public void setHvoipPhone(String hvoipPhone){
    this.hvoipPhone = hvoipPhone;
  }

  public String getHvoipRegId(){
    return this.hvoipRegId;
  }

  public void setHvoipRegId(String hvoipRegId){
    this.hvoipRegId = hvoipRegId;
  }

  public String getHvoipRegIdType(){
    return this.hvoipRegIdType;
  }

  public void setHvoipRegIdType(String hvoipRegIdType){
    this.hvoipRegIdType = hvoipRegIdType;
  }

  public String getHvoipMgcIp(){
    return this.hvoipMgcIp;
  }

  public void setHvoipMgcIp(String hvoipMgcIp){
    this.hvoipMgcIp = hvoipMgcIp;
  }

  public String getHvoipMgcPort(){
    return this.hvoipMgcPort;
  }

  public void setHvoipMgcPort(String hvoipMgcPort){
    this.hvoipMgcPort = hvoipMgcPort;
  }

  public String getHvoipStandMgcIp(){
    return this.hvoipStandMgcIp;
  }

  public void setHvoipStandMgcIp(String hvoipStandMgcIp){
    this.hvoipStandMgcIp = hvoipStandMgcIp;
  }

  public String getHvoipStandMgcPort(){
    return this.hvoipStandMgcPort;
  }

  public void setHvoipStandMgcPort(String hvoipStandMgcPort){
    this.hvoipStandMgcPort = hvoipStandMgcPort;
  }

  public String getHvoipPort(){
    return this.hvoipPort;
  }

  public void setHvoipPort(String hvoipPort){
    this.hvoipPort = hvoipPort;
  }

  public String getHvoipVlanId(){
    return this.hvoipVlanId;
  }

  public void setHvoipVlanId(String hvoipVlanId){
    this.hvoipVlanId = hvoipVlanId;
  }

  public String getHvoipWanType(){
    return this.hvoipWanType;
  }

  public void setHvoipWanType(String hvoipWanType){
    this.hvoipWanType = hvoipWanType;
  }

  public String getHvoipIpaddress(){
    return this.hvoipIpaddress;
  }

  public void setHvoipIpaddress(String hvoipIpaddress){
    this.hvoipIpaddress = hvoipIpaddress;
  }

  public String getHvoipIpmask(){
    return this.hvoipIpmask;
  }

  public void setHvoipIpmask(String hvoipIpmask){
    this.hvoipIpmask = hvoipIpmask;
  }

  public String getHvoipGateway(){
    return this.hvoipGateway;
  }

  public void setHvoipGateway(String hvoipGateway){
    this.hvoipGateway = hvoipGateway;
  }

  public String getHvoipIpdns(){
    return this.hvoipIpdns;
  }

  public void setHvoipIpdns(String hvoipIpdns){
    this.hvoipIpdns = hvoipIpdns;
  }

  public String getSipServTypeId(){
    return this.sipServTypeId;
  }

  public void setSipServTypeId(String sipServTypeId){
    this.sipServTypeId = sipServTypeId;
  }

  public String getSipOperateId(){
    return this.sipOperateId;
  }

  public void setSipOperateId(String sipOperateId){
    this.sipOperateId = sipOperateId;
  }

  public String getSipVoipPhone(){
    return this.sipVoipPhone;
  }

  public void setSipVoipPhone(String sipVoipPhone){
    this.sipVoipPhone = sipVoipPhone;
  }

  public String getSipVoipPwd(){
    return this.sipVoipPwd;
  }

  public void setSipVoipPwd(String sipVoipPwd){
    this.sipVoipPwd = sipVoipPwd;
  }

  public String getSipProxServ(){
    return this.sipProxServ;
  }

  public void setSipProxServ(String sipProxServ){
    this.sipProxServ = sipProxServ;
  }

  public String getSipProxPort(){
    return this.sipProxPort;
  }

  public void setSipProxPort(String sipProxPort){
    this.sipProxPort = sipProxPort;
  }

  public String getSipStandProxServ(){
    return this.sipStandProxServ;
  }

  public void setSipStandProxServ(String sipStandProxServ){
    this.sipStandProxServ = sipStandProxServ;
  }

  public String getSipStandProxPort(){
    return this.sipStandProxPort;
  }

  public void setSipStandProxPort(String sipStandProxPort){
    this.sipStandProxPort = sipStandProxPort;
  }

  public String getSipVoipPort(){
    return this.sipVoipPort;
  }

  public void setSipVoipPort(String sipVoipPort){
    this.sipVoipPort = sipVoipPort;
  }

  public String getSipRegiServ(){
    return this.sipRegiServ;
  }

  public void setSipRegiServ(String sipRegiServ){
    this.sipRegiServ = sipRegiServ;
  }

  public String getSipRegiPort(){
    return this.sipRegiPort;
  }

  public void setSipRegiPort(String sipRegiPort){
    this.sipRegiPort = sipRegiPort;
  }

  public String getSipStandRegiServ(){
    return this.sipStandRegiServ;
  }

  public void setSipStandRegiServ(String sipStandRegiServ){
    this.sipStandRegiServ = sipStandRegiServ;
  }

  public String getSipStandRegiPort(){
    return this.sipStandRegiPort;
  }

  public void setSipStandRegiPort(String sipStandRegiPort){
    this.sipStandRegiPort = sipStandRegiPort;
  }

  public String getSipOutBoundProxy(){
    return this.sipOutBoundProxy;
  }

  public void setSipOutBoundProxy(String sipOutBoundProxy){
    this.sipOutBoundProxy = sipOutBoundProxy;
  }

  public String getSipOutBoundPort(){
    return this.sipOutBoundPort;
  }

  public void setSipOutBoundPort(String sipOutBoundPort){
    this.sipOutBoundPort = sipOutBoundPort;
  }

  public String getSipStandOutBoundProxy(){
    return this.sipStandOutBoundProxy;
  }

  public void setSipStandOutBoundProxy(String sipStandOutBoundProxy){
    this.sipStandOutBoundProxy = sipStandOutBoundProxy;
  }

  public String getSipStandOutBoundPort(){
    return this.sipStandOutBoundPort;
  }

  public void setSipStandOutBoundPort(String sipStandOutBoundPort){
    this.sipStandOutBoundPort = sipStandOutBoundPort;
  }

  public String getSipProtocol(){
    return this.sipProtocol;
  }

  public void setSipProtocol(String sipProtocol){
    this.sipProtocol = sipProtocol;
  }

  public String getSipVlanId(){
    return this.sipVlanId;
  }

  public void setSipVlanId(String sipVlanId){
    this.sipVlanId = sipVlanId;
  }

  public String getSipWanType(){
    return this.sipWanType;
  }

  public void setSipWanType(String sipWanType){
    this.sipWanType = sipWanType;
  }

  public String getSipIpaddress(){
    return this.sipIpaddress;
  }

  public void setSipIpaddress(String sipIpaddress){
    this.sipIpaddress = sipIpaddress;
  }

  public String getSipIpmask(){
    return this.sipIpmask;
  }

  public void setSipIpmask(String sipIpmask){
    this.sipIpmask = sipIpmask;
  }

  public String getSipGateway(){
    return this.sipGateway;
  }

  public void setSipGateway(String sipGateway){
    this.sipGateway = sipGateway;
  }

  public String getSipIpdns(){
    return this.sipIpdns;
  }

  public void setSipIpdns(String sipIpdns){
    this.sipIpdns = sipIpdns;
  }

  public String getSipVoipUri(){
    return this.sipVoipUri;
  }

  public void setSipVoipUri(String sipVoipUri){
    this.sipVoipUri = sipVoipUri;
  }

  public String getSipUserAgentDomain(){
    return this.sipUserAgentDomain;
  }

  public void setSipUserAgentDomain(String sipUserAgentDomain){
    this.sipUserAgentDomain = sipUserAgentDomain;
  }

  public String getSipVoipUsername(){
    return this.sipVoipUsername;
  }

  public void setSipVoipUsername(String sipVoipUsername){
    this.sipVoipUsername = sipVoipUsername;
  }

  public String getStandNetIpdns(){
    return this.standNetIpdns;
  }

  public void setStandNetIpdns(String standNetIpdns){
    this.standNetIpdns = standNetIpdns;
  }

  public String getIptvServTypeId(){
    return this.iptvServTypeId;
  }

  public void setIptvServTypeId(String iptvServTypeId){
    this.iptvServTypeId = iptvServTypeId;
  }

  public String getIptvOperateId(){
    return this.iptvOperateId;
  }

  public void setIptvOperateId(String iptvOperateId){
    this.iptvOperateId = iptvOperateId;
  }

  public String getDeviceName(){
    return this.deviceName;
  }

  public void setDeviceName(String deviceName){
    this.deviceName = deviceName;
  }

  public String getDscpMark(){
    return this.dscpMark;
  }

  public void setDscpMark(String dscpMark){
    this.dscpMark = dscpMark;
  }

  public String getNetPort() {
    return this.netPort;
  }

  public void setNetPort(String netPort) {
    this.netPort = netPort.replaceAll(
      "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
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

  public int getIptv_Num() {
    return this.iptv_Num;
  }

  public int getVoipNum() {
    return this.voipNum;
  }

  public void setNetNum(String netNum) {
    this.netNum = netNum;
  }

  public void setIptv_Num(int iptv_Num) {
    this.iptv_Num = iptv_Num;
  }

  public void setVoipNum(int voipNum) {
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

	public String getStbUserID(){
		return stbUserID;
	}
	
	public void setStbUserID(String stbUserID){
		this.stbUserID = stbUserID;
	}
	
	public String getStbUserPwd(){
		return stbUserPwd;
	}

	public void setStbUserPwd(String stbUserPwd){
		this.stbUserPwd = stbUserPwd;
	}
	
	public String getStbNTP1(){
		return stbNTP1;
	}
	
	public void setStbNTP1(String stbNTP1){
		this.stbNTP1 = stbNTP1;
	}
	
	public String getStbNTP2(){
		return stbNTP2;
	}
	
	public void setStbNTP2(String stbNTP2){
		this.stbNTP2 = stbNTP2;
	}
	
	public String getStbBrowserURL1(){
		return stbBrowserURL1;
	}

	public void setStbBrowserURL1(String stbBrowserURL1){
		this.stbBrowserURL1 = stbBrowserURL1;
	}
	
	public String getStbServ(){
		return stbServ;
	}
	
	public void setStbServ(String stbServ){
		this.stbServ = stbServ;
	}
	
	public List<Map<String, String>> getLineId(){
		return lineId;
	}
	
	public void setLineId(List<Map<String, String>> lineId){
		this.lineId = lineId;
	}
	
	public List getLineList(){
		return lineList;
	}

	public void setLineList(List lineList){
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
		this.vpdnPort = vpdnPort.replaceAll(
	    		"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.", "L");
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

  public String getHvoipRtpPrefix() {
    return hvoipRtpPrefix;
  }

  public void setHvoipRtpPrefix(String hvoipRtpPrefix) {
    this.hvoipRtpPrefix = hvoipRtpPrefix;
  }


public String getVoipPwd()
{
	return voipPwd;
}


public void setVoipPwd(String voipPwd)
{
	this.voipPwd = voipPwd;
}

  public String getHvoipLineName() {
    return hvoipLineName;
  }

  public void setHvoipLineName(String hvoipLineName) {
    this.hvoipLineName = hvoipLineName;
  }

  public String getCloudNetAccount() {
    return cloudNetAccount;
  }

  public void setCloudNetAccount(String cloudNetAccount) {
    this.cloudNetAccount = cloudNetAccount;
  }

  public String getCloudNetPass() {
    return cloudNetPass;
  }

  public void setCloudNetPass(String cloudNetPass) {
    this.cloudNetPass = cloudNetPass;
  }

  public String getCloudNetUpBandwidth() {
    return cloudNetUpBandwidth;
  }

  public void setCloudNetUpBandwidth(String cloudNetUpBandwidth) {
    this.cloudNetUpBandwidth = cloudNetUpBandwidth;
  }

  public String getCloudNetDownBandwidth() {
    return cloudNetDownBandwidth;
  }

  public void setCloudNetDownBandwidth(String cloudNetDownBandwidth) {
    this.cloudNetDownBandwidth = cloudNetDownBandwidth;
  }

  public String getCloudNetVlanId() {
    return cloudNetVlanId;
  }

  public void setCloudNetVlanId(String cloudNetVlanId) {
    this.cloudNetVlanId = cloudNetVlanId;
  }

  public String getCloudNetDscp() {
    return cloudNetDscp;
  }

  public void setCloudNetDscp(String cloudNetDscp) {
    this.cloudNetDscp = cloudNetDscp;
  }

  public String getCloudNetAppType() {
    return cloudNetAppType;
  }

  public void setCloudNetAppType(String cloudNetAppType) {
    this.cloudNetAppType = cloudNetAppType;
  }

  public String getCloudNetOperateId() {
    return cloudNetOperateId;
  }

  public void setCloudNetOperateId(String cloudNetOperateId) {
    this.cloudNetOperateId = cloudNetOperateId;
  }

  public String getCloudNetServTypeId() {
    return cloudNetServTypeId;
  }

  public void setCloudNetServTypeId(String cloudNetServTypeId) {
    this.cloudNetServTypeId = cloudNetServTypeId;
  }
}