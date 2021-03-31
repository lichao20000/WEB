package com.linkage.module.gtms.config.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.config.serv.ServerConfigServ;


public class ServerConfigActionImpl implements ServerConfigAction {
	private static Logger logger = LoggerFactory.getLogger(ServerConfigActionImpl.class);
	
	//DHCP Server配置模版 各参数
	private String  serverConfig="";
	private String  isOpenDHCP="";
	private String  isRelay="";
	private String  minAddress="";
	private String  maxAddress="";
	private String  reservedAddresses="";
	private String  subnetMask="";
	private String  DNSServers="";
	private String  DHCPLeaseTime="";
	
	//维护帐号配置
	private String adminName="";
	private String adminPassword="";
	
	
	//IGMP
	private String IGMPSnoopingEnable="";
	
	//ALG
	private String ftpEnable ="";
	private String sipEnable = "";
	
	//WLAN
	private String wlanEnable="";
	
	private ServerConfigServ bio;
	
	private String deviceId;
	private String ajax;
	private String gw_type;
	/**
	 * DHCP Server配置模版
	 * @return
	 */
	public String doConfigAll(){
		logger.warn("doConfigAll({},{},{},{},{},{},{},{},{},{},{},{},{})",
				new Object[]{serverConfig,isOpenDHCP,isRelay,minAddress,maxAddress,
				reservedAddresses,subnetMask,DNSServers,DHCPLeaseTime});
		ajax = bio.doConfigAll(serverConfig,isOpenDHCP,isRelay,minAddress,maxAddress,
				reservedAddresses,subnetMask,DNSServers,
				DHCPLeaseTime,deviceId,gw_type);
		return "ajax";
	}
	/**
	 * 维护帐号配置模版
	 */
	public String maintainAccConfig(){
		logger.warn("maintainAccConifig({},{},{},{})",new Object[]{deviceId,gw_type,adminName,adminPassword});
		ajax = bio.maintainAccConifig(deviceId,gw_type,adminName,adminPassword);
		return "ajax";
	}
	
	/**
	 * IGMP配置
	 * return
	 */
	public String igmpConfig(){
		logger.warn("igmpConifig({},{},{},{})",new Object[]{deviceId,gw_type,IGMPSnoopingEnable});
		ajax = bio.igmpConifig(deviceId,gw_type,IGMPSnoopingEnable);
		return "ajax";
	}
	/**
	 * ALG 配置
	 * @return
	 */
	public String algConfig(){
		logger.warn("algConifig({},{},{},{})",new Object[]{deviceId,gw_type,ftpEnable,sipEnable});
		ajax = bio.algConifig(deviceId,gw_type,ftpEnable,sipEnable);
		return "ajax";
	}
	/**
	 * WLAN配置
	 * @return
	 */
	public String wlanConfig(){
		logger.warn("wlanConfig({},{},{},{})",new Object[]{deviceId,gw_type,wlanEnable});
		ajax = bio.wlanConfig(deviceId,gw_type,wlanEnable);
		return "ajax";
	}
	public String getServerConfig() {
		return serverConfig;
	}
	
	public void setServerConfig(String serverConfig) {
		this.serverConfig = serverConfig;
	}

	public String getIsOpenDHCP() {
		return isOpenDHCP;
	}

	public void setIsOpenDHCP(String isOpenDHCP) {
		this.isOpenDHCP = isOpenDHCP;
	}

	public String getIsRelay() {
		return isRelay;
	}

	public void setIsRelay(String isRelay) {
		this.isRelay = isRelay;
	}

	public String getMinAddress() {
		return minAddress;
	}

	public void setMinAddress(String minAddress) {
		this.minAddress = minAddress;
	}

	public String getMaxAddress() {
		return maxAddress;
	}

	public void setMaxAddress(String maxAddress) {
		this.maxAddress = maxAddress;
	}

	public String getReservedAddresses() {
		return reservedAddresses;
	}

	public void setReservedAddresses(String reservedAddresses) {
		this.reservedAddresses = reservedAddresses;
	}

	public String getSubnetMask() {
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}

	public String getDNSServers() {
		return DNSServers;
	}

	public void setDNSServers(String dNSServers) {
		DNSServers = dNSServers;
	}

	

	public String getDHCPLeaseTime() {
		return DHCPLeaseTime;
	}

	public void setDHCPLeaseTime(String dHCPLeaseTime) {
		DHCPLeaseTime = dHCPLeaseTime;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public void setBio(ServerConfigServ bio) {
		this.bio = bio;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public String getIGMPSnoopingEnable() {
		return IGMPSnoopingEnable;
	}
	public void setIGMPSnoopingEnable(String iGMPSnoopingEnable) {
		IGMPSnoopingEnable = iGMPSnoopingEnable;
	}
	public String getFtpEnable() {
		return ftpEnable;
	}
	public void setFtpEnable(String ftpEnable) {
		this.ftpEnable = ftpEnable;
	}
	public String getSipEnable() {
		return sipEnable;
	}
	public void setSipEnable(String sipEnable) {
		this.sipEnable = sipEnable;
	}
	public String getWlanEnable() {
		return wlanEnable;
	}
	public void setWlanEnable(String wlanEnable) {
		this.wlanEnable = wlanEnable;
	}
	
}
