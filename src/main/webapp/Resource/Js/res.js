var ICPMenuRes = new Array(8);

var SubICPMenuRes;

	SubICPMenuRes    = new Array(7);
	SubICPMenuRes[0] = new Menu("Device","添加设备资源","AddDeviceForm.jsp","设备资源管理--添加设备资源","DEVICE_ADD");
	SubICPMenuRes[1] = new Menu("Device","设备资源列表","DeviceList.jsp","设备资源管理--设备资源列表","DEVICE_EDIT");
	SubICPMenuRes[2] = new Menu("Device","待确认新增设备列表","DeviceList_topo.jsp","设备资源管理--待确认新增设备列表","DEVICE_TOPO_ADD");
	
	//SubICPMenuRes[3] = new Menu("Device","设备类型清单表","DeviceType_info.jsp","设备资源管理--设备类型清单表","DEVICE_TYPE_ALL");
	SubICPMenuRes[3] = new Menu('Device','-','','-','');
	SubICPMenuRes[4] = new Menu("Device","设备资源统计","DeviceState.jsp","设备资源管理--设备资源统计","DEVICE_RES_SUM");
	SubICPMenuRes[5] = new Menu("Device","待确认新增设备统计","DeviceTopoFind.jsp","设备资源管理--待确认新增设备统计","DEVICE_RES_SUM_TOPOFIND");
	SubICPMenuRes[6] = new Menu("Device","设备配置信息","Devicestru.jsp","设备资源管理--设备配置信息","DEVICE_STRUCATE");

ICPMenuRes[0]  = new Menu("Device","设备资源管理","","综合网管--资源管理系统--设备资源管理","DEVICE_RES_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(6);
	SubICPMenuRes[0] = new Menu("Customer","添加Lan客户资源","AddLanCustomerForm.jsp","客户资源管理--Lan客户资源","RES_CUS_LAN_ALL");
	SubICPMenuRes[1] = new Menu("Customer","添加ADSL客户资源","AddRadiusCustomerForm.jsp","客户资源管理--ADSL客户资源","RES_CUS_ADSL_ALL");
	SubICPMenuRes[2] = new Menu("Customer","添加大客户资源","AddUserInfoForm.jsp","客户资源管理--大客户资源","RES_LARGE_ALL");
	SubICPMenuRes[3] = new Menu("Customer","Lan客户资源列表","LanCustomerList.jsp","客户资源管理--Lan客户资源","RES_CUS_LAN_ALL");
	SubICPMenuRes[4] = new Menu("Customer","ADSL客户资源列表","RadiusCustomerList.jsp","客户资源管理--ADSL客户资源","RES_CUS_ADSL_ALL");
	SubICPMenuRes[5] = new Menu("Customer","大客户资源列表","UserInfoList.jsp","客户资源管理--大客户资源","RES_LARGE_ALL");

ICPMenuRes[1]  = new Menu("Customer","客户资源管理","","综合网管--资源管理系统--客户资源管理","RES_CUSTOMER_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu("Version","主控板软件版本","DeviceResourceSoftwareList.jsp","版本资源管理--软件版本","RES_VERSION_READ");
	SubICPMenuRes[1] = new Menu("Version","硬件版本","DeviceResourceHardwareList.jsp","版本资源管理--硬件版本","RES_VERSION_READ");
	SubICPMenuRes[2] = new Menu("Version","版本统计和综合查询","VersionAllSearchForm.jsp","版本资源管理--版本统计和综合查询","RES_VERSION_READ");
	SubICPMenuRes[3] = new Menu("Version","版本统计和等级查询","VersionLevelSearchForm.jsp","版本资源管理--版本统计和等级查询","RES_VERSION_READ");
ICPMenuRes[2]  = new Menu("Version","版本资源管理","","综合网管--资源管理系统--版本资源管理","RES_VERSION_READ",SubICPMenuRes);

	SubICPMenuRes    = new Array(9);
	SubICPMenuRes[0] = new Menu("Base","区域资源","AreaList.jsp","基础资源管理--区域资源","RES_BASE_AREA_ALL");
	SubICPMenuRes[1] = new Menu("Base","属地资源","CityList.jsp","基础资源管理--属地资源","RES_BASE_CITY_ALL");
	SubICPMenuRes[2] = new Menu("Base","局向资源","OfficeList.jsp","基础资源管理--局向资源","RES_BASE_OFFICE_ALL");
	SubICPMenuRes[3] = new Menu("Base","小区资源","ZoneList.jsp","基础资源管理--小区资源","RES_BASE_ZONE_ALL");
	SubICPMenuRes[4] = new Menu("Base","资源类型配置表","ResTypeToDeviceModelList.jsp","基础资源管理--资源类型资源","RES_BASE_TYPE_ALL");
	SubICPMenuRes[5] = new Menu('Base','-','','-','');
	SubICPMenuRes[6] = new Menu("Base","设备层次","ResourceTypeList.jsp","基础资源管理--设备层次","RES_BASE_RES_ALL");
	SubICPMenuRes[7] = new Menu("Base","设备厂商","VendorList.jsp","基础资源管理--设备厂商","RES_BASE_FACTORY_ALL");
	SubICPMenuRes[8] = new Menu("Base","设备型号","DeviceType_info.jsp","基础资源管理--设备型号","RES_BASE_FACTORY_ALL");	
ICPMenuRes[3]  = new Menu("Base","基础资源管理","","综合网管--资源管理系统--基础资源管理","RES_BASE_READ",SubICPMenuRes);

	//SubICPMenuRes    = new Array(1);
	//SubICPMenuRes[0] = new Menu("IPADDRESS","地址用途创建","../ipmanage/ip_addr_usecreate_form.jsp","IP地址管理-地址用途创建","RES_BASE_AREA_ALL");
ICPMenuRes[4]  = new Menu("IPADDRESS","IP地址管理","../ipmanage/","综合网管--资源管理系统--IP地址管理","IP_ADDR_MANAGE");

	SubICPMenuRes    = new Array(4);
	SubICPMenuRes[0] = new Menu("Bak","设置备份设备","bak_config_dev_form.jsp","设备配置备份--设置备份设备","RES_BAK_CONFIG_DEV");
	SubICPMenuRes[1] = new Menu("Bak","设置备份服务器","bak_config_serv_form.jsp","设备配置备份--设置备份服务器","RES_BAK_CONFIG_SERV");
	SubICPMenuRes[2] = new Menu("Bak","显示设备配置","bak_view_form.jsp","设备配置备份--显示设备配置","RES_BAK_VIEW");
	SubICPMenuRes[3] = new Menu("Bak","比较设备配置","bak_compare_form.jsp","设备配置备份--比较设备配置","RES_BAK_COMPARE");
ICPMenuRes[5]  = new Menu("Bak","设备配置备份","","综合网管--资源管理系统--设备配置备份","RES_BAK_READ",SubICPMenuRes);

ICPMenuRes[6]  = new Menu("Whois","Whois工具","","综合网管--资源管理系统--Whois工具","WHOIS_READ",SubICPMenuRes);

   SubICPMenuRes    = new Array(1);
	SubICPMenuRes[0] = new Menu("Circuit","电路配置","circuit_define.jsp","电路配置","RES_BASE_READ");
ICPMenuRes[7]  = new Menu("Circuit","电路资源","","综合网管--电路资源","RES_BASE_READ",SubICPMenuRes);
//设置主题LOGO
setICPBanner("../images/resource_banner.jpg","资源管理系统","index.jsp","_top");

//设置主题Banner
//setADSBanner(IMGStr,ALTStr,LINKStr,TARGETStr);

//设置ICPMenu颜色
//setICPMenuColor("#003399","#FFFFFF","#FF0000");

//设置公司LOGO
setLogo("images/logo.gif","联创科技股份有限公司","http://www.lianchuang.com/","_blank");

isFrameWork = true;