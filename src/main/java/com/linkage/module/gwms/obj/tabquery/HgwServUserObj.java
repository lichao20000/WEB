package com.linkage.module.gwms.obj.tabquery;

/**
 * @author Jason(3412)
 * @date 2009-6-26
 */
public class HgwServUserObj {

	private String userId;
	private String servTypeId;
	private String username;
	
	private String servStatus;
	
	private String passwd;
	
	private String wanType;
	
	private String vpiid;
	
	private String vciid;
	
	private String vlanid;
	
	private String ipAddress;
	
	private String ipMask;
	
	private String gateway;
	
	private String dns;
	
	private String bindPort;
	
	private String wanValue1;
	
	private String wanValue2;
	
	private String openStatus;
	/**IPV6  start*/
	/**地址分配机制*/
	private String addressOrigin ;
	/**Address Family Translation Router*/
	private String aftr  ;
	/**IP地址*/
	private String ipv6AddressIp ;
	/**DNS*/
	private String ipv6AddressDNS ;
	/**地址前缀*/
	private String ipv6AddressPrefix;
	/**IPV6  end*/
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServTypeId() {
		return servTypeId;
	}

	public void setServTypeId(String servTypeId) {
		this.servTypeId = servTypeId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVpiid() {
		return vpiid;
	}

	public void setVpiid(String vpiid) {
		this.vpiid = vpiid;
	}

	public String getVciid() {
		return vciid;
	}

	public void setVciid(String vciid) {
		this.vciid = vciid;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getServStatus() {
		return servStatus;
	}

	public void setServStatus(String servStatus) {
		this.servStatus = servStatus;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpMask() {
		return ipMask;
	}

	public void setIpMask(String ipMask) {
		this.ipMask = ipMask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getBindPort() {
		return bindPort;
	}

	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	public String getWanValue1() {
		return wanValue1;
	}

	public void setWanValue1(String wanValue1) {
		this.wanValue1 = wanValue1;
	}

	public String getWanValue2() {
		return wanValue2;
	}

	public void setWanValue2(String wanValue2) {
		this.wanValue2 = wanValue2;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	
	public String getAddressOrigin() {
		return addressOrigin;
	}

	public void setAddressOrigin(String addressOrigin) {
		this.addressOrigin = addressOrigin;
	}

	public String getAftr() {
		return aftr;
	}

	public void setAftr(String aftr) {
		this.aftr = aftr;
	}

	public String getIpv6AddressIp() {
		return ipv6AddressIp;
	}

	public void setIpv6AddressIp(String ipv6AddressIp) {
		this.ipv6AddressIp = ipv6AddressIp;
	}

	public String getIpv6AddressDNS() {
		return ipv6AddressDNS;
	}

	public void setIpv6AddressDNS(String ipv6AddressDNS) {
		this.ipv6AddressDNS = ipv6AddressDNS;
	}

	public String getIpv6AddressPrefix() {
		return ipv6AddressPrefix;
	}

	public void setIpv6AddressPrefix(String ipv6AddressPrefix) {
		this.ipv6AddressPrefix = ipv6AddressPrefix;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "userId:" + userId + " username:" + username + " servTypeId:" + servTypeId;
	}
}
