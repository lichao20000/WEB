package com.linkage.module.gwms.obj.gw;

/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class LanHostObj {

	// 对应于表gw_lan_host
	/** LANDevice实例值 */
	private int lan_inst;
	/** host实例值 */
	private int host_inst;
	/** PC机连接状态 */
	private String active;
	/** PC机地址 */
	private String iPAddress;
	/** 地址来源：DHCP,Static,AutoIP */
	private String addressSource;
	/** PC机MAC地址 */
	private String macAddress;
	/** PC主机名 */
	private String hostname;
	/** PC连接的LAN侧端口 */
	private String interf;
	
	public int getLan_inst() {
		return lan_inst;
	}

	public void setLan_inst(int lan_inst) {
		this.lan_inst = lan_inst;
	}

	public int getHost_inst() {
		return host_inst;
	}

	public void setHost_inst(int host_inst) {
		this.host_inst = host_inst;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getIPAddress() {
		return iPAddress;
	}

	public void setIPAddress(String address) {
		iPAddress = address;
	}

	public String getAddressSource() {
		return addressSource;
	}

	public void setAddressSource(String addressSource) {
		this.addressSource = addressSource;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getInterf() {
		return interf;
	}

	public void setInterf(String interf) {
		this.interf = interf;
	}

}
