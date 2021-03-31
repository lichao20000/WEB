
package com.linkage.module.gtms.stb.utils;


/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-11-27
 * @category com.linkage.module.gtms.stb.utils
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public interface StbStatics
{

	/**
	 * STB采集相关 paramType
	 */
	// 采集配置文件中所有参数
	int GATHER_ALL = 0;
	// 1：STBDevice
	int GATHER_STBDevice = 1;
	// 12：Capabilities
	int GATHER_Capabilities = 12;
	// 2：UserInterface
	int GATHER_UserInterface = 2;
	// 3：LAN
	int GATHER_LAN = 3;
	// 4：X_CTC_IPTV
	int GATHER_X_CTC_IPTV = 4;
	// 41：ServiceInfo
	int GATHER_X_CTC_IPTV_ServiceInfo = 41;
	// 32: TraceRoute
	int GATHER_TRACEROUTE = 32;
}
