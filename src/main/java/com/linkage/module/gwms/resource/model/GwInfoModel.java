
package com.linkage.module.gwms.resource.model;

/**
 * 光猫参数模型
 * 
 * @author yages
 */
public class GwInfoModel
{

	private int _2g_channel; // 2.4G无线信道
	private String dns; // 域名
	private String loid; // 逻辑ID号
	private String mac; // mac
	private String _2g_ssid; // 2.4G SSID信息
	private int _5g_channel; // 5G无线信道
	private int online_time; // 在线时长，自上电启动时长，单位秒
	private String Devname; // 设备主机名
	private String vendor; // 网关厂商
	private String _5g_ssid; // 5G SSID信息
	private String pppoe; // 宽带账号
	private String lan_ip; // 内网管理IP（如192.168.1.1）
	private String wan_ip; // 网关wan侧IP地址
	private long rx_bytes; // internet连接接收字节数，单位B
	private String model; // 网关型号
	private String sn; // 网关sn信息
	private long tx_bytes; // internet连接发送字节数，单位B
	private String softwareversion; // 软件版本
	private String hardwareversion; // 硬件版本
	private String phone; // 关联电话
	private String loopback_ip; // ip

	public int get_2g_channel()
	{
		return _2g_channel;
	}

	public void set_2g_channel(int _2g_channel)
	{
		this._2g_channel = _2g_channel;
	}

	public String getDns()
	{
		return dns;
	}

	public void setDns(String dns)
	{
		this.dns = dns;
	}

	public String getLoid()
	{
		return loid;
	}

	public void setLoid(String loid)
	{
		this.loid = loid;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String get_2g_ssid()
	{
		return _2g_ssid;
	}

	public void set_2g_ssid(String _2g_ssid)
	{
		this._2g_ssid = _2g_ssid;
	}

	public int get_5g_channel()
	{
		return _5g_channel;
	}

	public void set_5g_channel(int _5g_channel)
	{
		this._5g_channel = _5g_channel;
	}

	public int getOnline_time()
	{
		return online_time;
	}

	public void setOnline_time(int online_time)
	{
		this.online_time = online_time;
	}

	public String getDevname()
	{
		return Devname;
	}

	public void setDevname(String devname)
	{
		Devname = devname;
	}

	public String getVendor()
	{
		return vendor;
	}

	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

	public String get_5g_ssid()
	{
		return _5g_ssid;
	}

	public void set_5g_ssid(String _5g_ssid)
	{
		this._5g_ssid = _5g_ssid;
	}

	public String getPppoe()
	{
		return pppoe;
	}

	public void setPppoe(String pppoe)
	{
		this.pppoe = pppoe;
	}

	public String getLan_ip()
	{
		return lan_ip;
	}

	public void setLan_ip(String lan_ip)
	{
		this.lan_ip = lan_ip;
	}

	public String getWan_ip()
	{
		return wan_ip;
	}

	public void setWan_ip(String wan_ip)
	{
		this.wan_ip = wan_ip;
	}

	public long getRx_bytes()
	{
		return rx_bytes;
	}

	public void setRx_bytes(long rx_bytes)
	{
		this.rx_bytes = rx_bytes;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public String getSn()
	{
		return sn;
	}

	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public long getTx_bytes()
	{
		return tx_bytes;
	}

	public void setTx_bytes(long tx_bytes)
	{
		this.tx_bytes = tx_bytes;
	}

	/**
	 * @return the softwareversion
	 */
	public String getSoftwareversion()
	{
		return softwareversion;
	}

	/**
	 * @param softwareversion
	 *            the softwareversion to set
	 */
	public void setSoftwareversion(String softwareversion)
	{
		this.softwareversion = softwareversion;
	}

	/**
	 * @return the hardwareversion
	 */
	public String getHardwareversion()
	{
		return hardwareversion;
	}

	/**
	 * @param hardwareversion
	 *            the hardwareversion to set
	 */
	public void setHardwareversion(String hardwareversion)
	{
		this.hardwareversion = hardwareversion;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the loopback_ip
	 */
	public String getLoopback_ip()
	{
		return loopback_ip;
	}

	/**
	 * @param loopback_ip
	 *            the loopback_ip to set
	 */
	public void setLoopback_ip(String loopback_ip)
	{
		this.loopback_ip = loopback_ip;
	}
}
