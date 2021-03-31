
package com.linkage.module.gwms.resource.model;

/**
 * 拓扑节点模型
 * 
 * @author yages
 */
public class TopoInfoModel
{

	private String par_mac; // 父节点MAC，格式为12个字节长度的字符串，来自于获取下挂终端的MAC地址
							// 如”0123456789AB”，其中“”代表根节点。
	private String mac; // 终端MAC信息，AP/SW/路由下挂设备需要虚构MAC信息，以保证拓扑层级关系，如AP001，SW001、ROUTE001，此时仅显示有数值字段即可，若值为””、-1代表无效
	private int acc_type; // 接入类型，0代表有线，1代表无线
	private String acc_typeStr; // 接入类型，0代表有线，1代表无线
	private String acc_port; // 接入端口,0/1/2/3,其他为SSID属性
	private String dev_type; // 设备类型，route/phone/AP/SW/elink，其中AP/SW为虚构设备，没有具体的MAC和IP,elink信息从dbus接口获取
	private String model; // 终端型号
	private String brand; // 终端品牌信息
	private String os; // 终端操作系统
	private String ip; // 终端IP地址信息
	private String last_active_time; // 最新上线时间，2018-09-18 10:02:03
	private String last_inactive_time; // 最新下线时间，2018-09-18 10:02:04
	private long online_time; // 在线时长，单位s
	private String online_timeStr; // 在线时长，单位s
	private String hostname; // 设备主机名
	private long rxbytes; // 接收字节数,B
	private long txbytes; // 发送字节数,B
	private String power_level; // 连接信号强度(单位dbm)
	private int acc_speed; // 协商速率Kbps
	private int active; // 是否在线，0/1，1代表在线，0代表离线
	private String activeStr; // 是否在线，0/1，1代表在线，0代表离线
	// elinkAP新增字段
	private String workmode; // 工作模式
	private String sn; // elink设备sn信息
	private String _2Gssid; // 2.4G无线SSID信息
	private int _2Gchannel; // 2.4G无线信道
	private String _5Gssid; // 5G无线SSID信息
	private int _5Gchannel; // 5无线信道
	// elinkAP下挂终端新增字段：
	private int uploadspeed; // 上行速度
	private int downloadspeed; // 下行速度
	private String port; // 无线连接elinkAP，该字段为2.4G和5G
	private long timestamp; // 时间戳
	
	private String vendor; // 下挂设备厂商

	public String getPar_mac()
	{
		return par_mac;
	}

	public void setPar_mac(String par_mac)
	{
		this.par_mac = par_mac;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public int getAcc_type()
	{
		return acc_type;
	}

	public void setAcc_type(int acc_type)
	{
		this.acc_type = acc_type;
	}

	public String getAcc_port()
	{
		return acc_port;
	}

	public void setAcc_port(String acc_port)
	{
		this.acc_port = acc_port;
	}

	public String getDev_type()
	{
		return dev_type;
	}

	public void setDev_type(String dev_type)
	{
		this.dev_type = dev_type;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel(String model)
	{
		this.model = model;
	}

	public String getBrand()
	{
		return brand;
	}

	public void setBrand(String brand)
	{
		this.brand = brand;
	}

	public String getOs()
	{
		return os;
	}

	public void setOs(String os)
	{
		this.os = os;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getLast_active_time()
	{
		return last_active_time;
	}

	public void setLast_active_time(String last_active_time)
	{
		this.last_active_time = last_active_time;
	}

	public String getLast_inactive_time()
	{
		return last_inactive_time;
	}

	public void setLast_inactive_time(String last_inactive_time)
	{
		this.last_inactive_time = last_inactive_time;
	}

	public long getOnline_time()
	{
		return online_time;
	}

	public void setOnline_time(long online_time)
	{
		this.online_time = online_time;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public long getRxbytes()
	{
		return rxbytes;
	}

	public void setRxbytes(long rxbytes)
	{
		this.rxbytes = rxbytes;
	}

	public long getTxbytes()
	{
		return txbytes;
	}

	public void setTxbytes(long txbytes)
	{
		this.txbytes = txbytes;
	}

	public String getPower_level()
	{
		return power_level;
	}

	public void setPower_level(String power_level)
	{
		this.power_level = power_level;
	}

	public int getAcc_speed()
	{
		return acc_speed;
	}

	public void setAcc_speed(int acc_speed)
	{
		this.acc_speed = acc_speed;
	}

	public int getActive()
	{
		return active;
	}

	public void setActive(int active)
	{
		this.active = active;
	}

	public String getWorkmode()
	{
		return workmode;
	}

	public void setWorkmode(String workmode)
	{
		this.workmode = workmode;
	}

	public String getSn()
	{
		return sn;
	}

	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public String get_2Gssid()
	{
		return _2Gssid;
	}

	public void set_2Gssid(String _2Gssid)
	{
		this._2Gssid = _2Gssid;
	}

	public int get_2Gchannel()
	{
		return _2Gchannel;
	}

	public void set_2Gchannel(int _2Gchannel)
	{
		this._2Gchannel = _2Gchannel;
	}

	public String get_5Gssid()
	{
		return _5Gssid;
	}

	public void set_5Gssid(String _5Gssid)
	{
		this._5Gssid = _5Gssid;
	}

	public int get_5Gchannel()
	{
		return _5Gchannel;
	}

	public void set_5Gchannel(int _5Gchannel)
	{
		this._5Gchannel = _5Gchannel;
	}

	public int getUploadspeed()
	{
		return uploadspeed;
	}

	public void setUploadspeed(int uploadspeed)
	{
		this.uploadspeed = uploadspeed;
	}

	public int getDownloadspeed()
	{
		return downloadspeed;
	}

	public void setDownloadspeed(int downloadspeed)
	{
		this.downloadspeed = downloadspeed;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getAcc_typeStr()
	{
		return acc_typeStr;
	}

	public void setAcc_typeStr(String acc_typeStr)
	{
		this.acc_typeStr = acc_typeStr;
	}

	public String getOnline_timeStr()
	{
		return online_timeStr;
	}

	public void setOnline_timeStr(String online_timeStr)
	{
		this.online_timeStr = online_timeStr;
	}

	public String getActiveStr()
	{
		return activeStr;
	}

	public void setActiveStr(String activeStr)
	{
		this.activeStr = activeStr;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	
	/**
	 * @return the vendor
	 */
	public String getVendor()
	{
		return vendor;
	}

	
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}

}
