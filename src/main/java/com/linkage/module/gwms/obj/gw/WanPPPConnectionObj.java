package com.linkage.module.gwms.obj.gw;

/**
 * @author Jason(3412)
 * @date 2009-6-16
 */
public class WanPPPConnectionObj {

	/** WANDevice结点的实例 */
	private int wanDeviceInstance;
	/** WANConnectionDevice结点的实例 */
	private int wanConnInstance;
	/** WANPPPConnection结点的实例 */
	private int pppConnInstance;
	/** pvc值 */
	private String pvc;
	/** 连接状态Unconfigured,Connecting,Authenticating,Connected,PendingDisconnect,Disconnecting,Disconnected */
	private String connectionStatus;
	/** 连接类型Unconfigured,IP_Routed,DHCP_Spoofed,PPPoE_Bridged,PPPoE_Relay,PPTP_Relay,L2TP_Relay */
	private String connectionType;
	/** PPPoE帐号 */
	private String pppUsername;
	/** 拨号错误代码 */
	private String lastConnError;
	/** DNSServers */
	private String dnsServers;
	/** 绑定端口 */
	private String bindInterface;
	/** NAT开关 */
	private String natEnabled;

	/**
	 * 获取指定PVC值连接的账号，状态，拨号错误代码等信息
	 * 
	 * @param pvc
	 */
	public WanPPPConnectionObj getSpecialWanPPPConnInfo(String pvc) {

		return null;

	}

	/**
	 * 获取设备一条连接的信息：PVC，用户账号等
	 * 
	 * @param instanceParam
	 *            "InternetGatewayDevice.WANDevice.i.WANConnectionDevice.j.WANPPPConnection.k"
	 */
	public WanPPPConnectionObj[] getWanPPPConnInfo(String instanceParam) {

		return null;
	}

	/** getter setter methods */
	public int getWanDeviceInstance() {
		return wanDeviceInstance;
	}

	public void setWanDeviceInstance(int wanDeviceInstance) {
		this.wanDeviceInstance = wanDeviceInstance;
	}

	public int getWanConnInstance() {
		return wanConnInstance;
	}

	public void setWanConnInstance(int wanConnInstance) {
		this.wanConnInstance = wanConnInstance;
	}

	public int getPppConnInstance() {
		return pppConnInstance;
	}

	public void setPppConnInstance(int pppConnInstance) {
		this.pppConnInstance = pppConnInstance;
	}

	public String getPvc() {
		return pvc;
	}

	public void setPvc(String pvc) {
		this.pvc = pvc;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getPppUsername() {
		return pppUsername;
	}

	public void setPppUsername(String pppUsername) {
		this.pppUsername = pppUsername;
	}

	public String getLastConnError() {
		return lastConnError;
	}

	public void setLastConnError(String lastConnError) {
		this.lastConnError = lastConnError;
	}

	public String getDnsServers() {
		return dnsServers;
	}

	public void setDnsServers(String dnsServers) {
		this.dnsServers = dnsServers;
	}

	public String getBindInterface() {
		return bindInterface;
	}

	public void setBindInterface(String bindInterface) {
		this.bindInterface = bindInterface;
	}

	public String getNatEnabled() {
		return natEnabled;
	}

	public void setNatEnabled(String natEnabled) {
		this.natEnabled = natEnabled;
	}

}
