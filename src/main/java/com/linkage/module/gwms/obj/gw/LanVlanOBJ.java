
package com.linkage.module.gwms.obj.gw;


/**
 * table:gw_lan_vlan_dhcp.
 * 
 * @author wangsenbo (wangsenbo@lianchuang.com)
 * @date 2009-10-28
 */
public class LanVlanOBJ
{

	private String deviceId;
	private int vlanI;
	private int vlanId;
	private long gatherTime;
	private String vlanName;
	private String portList;
	private String port;//端口转换后	
	private int ipEnable;
	private String ipAddress;
	private String ipMask;
	private String ipAddressType;
	private int dhcpEnable;
	private String dhcpMinAddr;
	private String dhcpMaxAddr;
	private String dhcpResAddr;
	private String dhcpMask;
	private String dhcpDns;
	private String dhcpDomain;
	private String dhcpGateway;
	private String dhcpLeaseTime;

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the vlanI
	 */
	public int getVlanI()
	{
		return vlanI;
	}

	/**
	 * @param vlanI
	 *            the vlanI to set
	 */
	public void setVlanI(int vlanI)
	{
		this.vlanI = vlanI;
	}

	/**
	 * @return the vlanId
	 */
	public int getVlanId()
	{
		return vlanId;
	}

	/**
	 * @param vlanId
	 *            the vlanId to set
	 */
	public void setVlanId(int vlanId)
	{
		this.vlanId = vlanId;
	}

	/**
	 * @return the vlanName
	 */
	public String getVlanName()
	{
		return vlanName;
	}

	/**
	 * @param vlanName
	 *            the vlanName to set
	 */
	public void setVlanName(String vlanName)
	{
		this.vlanName = vlanName;
	}

	/**
	 * @return the portList
	 */
	public String getPortList()
	{
		return portList;
	}

	/**
	 * @param portList
	 *            the portList to set
	 */
	public void setPortList(String portList)
	{
		this.portList = portList;
	}

	/**
	 * @return the ipEnable
	 */
	public int getIpEnable()
	{
		return ipEnable;
	}

	/**
	 * @param ipEnable
	 *            the ipEnable to set
	 */
	public void setIpEnable(int ipEnable)
	{
		this.ipEnable = ipEnable;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the ipMask
	 */
	public String getIpMask()
	{
		return ipMask;
	}

	/**
	 * @param ipMask
	 *            the ipMask to set
	 */
	public void setIpMask(String ipMask)
	{
		this.ipMask = ipMask;
	}

	/**
	 * @return the ipAddressType
	 */
	public String getIpAddressType()
	{
		return ipAddressType;
	}

	/**
	 * @param ipAddressType
	 *            the ipAddressType to set
	 */
	public void setIpAddressType(String ipAddressType)
	{
		this.ipAddressType = ipAddressType;
	}

	/**
	 * @return the dhcpEnable
	 */
	public int getDhcpEnable()
	{
		return dhcpEnable;
	}

	/**
	 * @param dhcpEnable
	 *            the dhcpEnable to set
	 */
	public void setDhcpEnable(int dhcpEnable)
	{
		this.dhcpEnable = dhcpEnable;
	}

	/**
	 * @return the dhcpMinAddr
	 */
	public String getDhcpMinAddr()
	{
		return dhcpMinAddr;
	}

	/**
	 * @param dhcpMinAddr
	 *            the dhcpMinAddr to set
	 */
	public void setDhcpMinAddr(String dhcpMinAddr)
	{
		this.dhcpMinAddr = dhcpMinAddr;
	}

	/**
	 * @return the dhcpMaxAddr
	 */
	public String getDhcpMaxAddr()
	{
		return dhcpMaxAddr;
	}

	/**
	 * @param dhcpMaxAddr
	 *            the dhcpMaxAddr to set
	 */
	public void setDhcpMaxAddr(String dhcpMaxAddr)
	{
		this.dhcpMaxAddr = dhcpMaxAddr;
	}

	/**
	 * @return the dhcpResAddr
	 */
	public String getDhcpResAddr()
	{
		return dhcpResAddr;
	}

	/**
	 * @param dhcpResAddr
	 *            the dhcpResAddr to set
	 */
	public void setDhcpResAddr(String dhcpResAddr)
	{
		this.dhcpResAddr = dhcpResAddr;
	}

	/**
	 * @return the dhcpMask
	 */
	public String getDhcpMask()
	{
		return dhcpMask;
	}

	/**
	 * @param dhcpMask
	 *            the dhcpMask to set
	 */
	public void setDhcpMask(String dhcpMask)
	{
		this.dhcpMask = dhcpMask;
	}

	/**
	 * @return the dhcpDns
	 */
	public String getDhcpDns()
	{
		return dhcpDns;
	}

	/**
	 * @param dhcpDns
	 *            the dhcpDns to set
	 */
	public void setDhcpDns(String dhcpDns)
	{
		this.dhcpDns = dhcpDns;
	}

	/**
	 * @return the dhcpDomain
	 */
	public String getDhcpDomain()
	{
		return dhcpDomain;
	}

	/**
	 * @param dhcpDomain
	 *            the dhcpDomain to set
	 */
	public void setDhcpDomain(String dhcpDomain)
	{
		this.dhcpDomain = dhcpDomain;
	}

	/**
	 * @return the dhcpGateway
	 */
	public String getDhcpGateway()
	{
		return dhcpGateway;
	}

	/**
	 * @param dhcpGateway
	 *            the dhcpGateway to set
	 */
	public void setDhcpGateway(String dhcpGateway)
	{
		this.dhcpGateway = dhcpGateway;
	}

	/**
	 * @return the dhcpLeaseTime
	 */
	public String getDhcpLeaseTime()
	{
		return dhcpLeaseTime;
	}

	/**
	 * @param dhcpLeaseTime
	 *            the dhcpLeaseTime to set
	 */
	public void setDhcpLeaseTime(String dhcpLeaseTime)
	{
		this.dhcpLeaseTime = dhcpLeaseTime;
	}

	/**
	 * @return the gatherTime
	 */
	public long getGatherTime()
	{
		return gatherTime;
	}

	/**
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(long gatherTime)
	{
		this.gatherTime = gatherTime;
	}

	
	/**
	 * @return the port
	 */
	public String getPort()
	{
		return port;
	}

	
	/**
	 * @param port the port to set
	 */
	public void setPort(String port)
	{
		this.port = port;
	}
}
