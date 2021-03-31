package com.linkage.module.gwms;

/**
 * @author Jason(3412)
 * @date 2009-10-29
 */
public final class ConstantClass {

	/** ITMS/BBMS系统业务类型常量定义 */
	
	//ITMS上网业务类型
	public static int ITMS_NET = 10;
	//BBMS上网业务类型
	public static int BBMS_NET = 60;
	//ITMS IPTV业务类型
	public static int ITMS_IPTV = 11;
	//BBMS EVDO业务类型
	public static int BBMS_EVDO = 70;
	//ITMS 无线(上网)业务类型
	public static int ITMS_WLAN = 103;
	/**E8-C service type**/
	//ITMS VOIP业务类型
	public static int ITMS_VOIP = 14;
	//资料接口
	public static int E8C_INFO_INTER = 20;
	//E8-C上网业务
	public static int E8C_NET = 22;
	//E8-CIPTV
	public static int E8C_IPTV = 21;
	
	/** 调用采集模块结点常量定义 */
	//LANDevice
	public static int GATHER_LAN = 1;
	//LANDevice.LANEthernetInterfaceConfig
	public static int GATHER_LAN_ETHERNET = 11;
	//LANDevice.WLANConfiguration
	public static int GATHER_LAN_WLAN = 12;
	//LANDevice.Hosts
	public static int GATHER_LAN_HOST = 13;
	//LANDevice.LANHostConfigManagement
	public static int GATHER_LAN_HOSTMANAGE = 14;
	
	//WANDevice
	public static int GATHER_WAN = 2;
	
	//Services
	public static int GATHER_SERVICE = 3;
	//Services.X_CT-COM_MWBAND
	public static int GATHER_SERVICE_MWBAND = 31;
	//Services.X_CT-COM_IPTV
	public static int GATHER_SERVICE_IPTV = 32;
	//Services.X_ATP_UPnP
	public static int GATHER_SERVICE_ATP = 33;
	//Services.VoiceService
	public static int GATHER_SERVICE_VOIP = 34;
	//DeviceInfo
	public static int GATHER_DEVICEINFO = 4;
	//DeviceInfo.X_CT-COM_TeleComAccount
	public static int GATHER_DEVICEINFO_TELECOMACCOUNT = 41;
	//DeviceInfo.X_CT-COM_ALGAbility
	public static int GATHER_DEVICEINFO_ALG = 42;
	//DeviceInfo.X_CT-COM_WirelessCard
	public static int GATHER_DEVICEINFO_EVDO = 43;
	//DeviceInfo.X_CT-COM_ServiceManage
	public static int GATHER_DEVICEINFO_FTP = 44;
		
	//X_CT-COM_UplinkQoS
	public static int GATHER_QOS = 5;
	
	//Time
	public static int GATHER_TIME = 61;
	
	//ManagementServer
	public static int GATHER_MANAGE_SERVER = 62;
	
	//X_CT-COM_SNMP
	public static int GATHER_SNMP = 63;
	
	//DeviceInfo
	public static int GATHER_VLAN = 64;
	
	
	
	/** 单配置功能的模板ServiceId常量定义(入策略表使用到) */
	
	//Time(NTP)下结点
	public static int TIME = 110;
	
	//SNMP下结点
	public static int SNMP = 111;
	
	//VLAN下结点
	public static int VLAN = 112;
	
	//DHCP结点
	public static int DHCP = 113;
	
	//FTP结点
	public static int FTP = 114;
	
	//TR069结点
	public static int TR069 = 102;
	
	//新疆设备从ITMS转到BBMS中
	public static int ManagementServer = 102;
	
	//WLAN结点
	public static int WLAN = 103;
	
	/** 业务配置service_id定义 (入策略表使用到)*/
	//上网业务
	public static int NET_OPEN = 1001;
	//VOIP业务
	public static int VOIP_OPEN = 1401;
	
	
	/** 上网方式，接入方式常量定义 */
	//ADSL上行
	public static int ACCESS_ADSL = 1;
	//LAN上行
	public static int ACCESS_LAN = 2;
	//EPON上行
	public static int ACCESS_EPON = 3;

	//桥接
	public static int NET_BREDGE = 1;
	//路由
	public static int NET_ROUTE = 2;
	//静态IP
	public static int NET_STATIC = 3;
	//DHCP
	public static int NET_DHCP = 4;
	
	//操作类型
	public static int SERV_OPEN = 1;
	
	
	/**
	 * STB采集相关 paramType
	 */
	// 采集配置文件中所有参数
	public static int STB_GATHER_ALL = 0;
	// 1：STBDevice
	public static int STB_GATHER_STBDevice = 1;
	// 12：Capabilities
	public static int STB_GATHER_Capabilities = 12;
	// 2：UserInterface
	public static int STB_GATHER_UserInterface = 2;
	// 3：LAN
	public static int STB_GATHER_LAN = 3;
	// 4：X_CTC_IPTV
	public static int STB_GATHER_X_CTC_IPTV = 4;
	// 41：ServiceInfo
	public static int STB_GATHER_X_CTC_IPTV_ServiceInfo = 41;
	
	// 32: TraceRoute
	public static int STB_GATHER_TRACEROUTE = 32;
	
	
}
