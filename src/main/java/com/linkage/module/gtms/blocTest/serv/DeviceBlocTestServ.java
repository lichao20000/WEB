package com.linkage.module.gtms.blocTest.serv;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since May 11, 2012
 * @category com.linkage.module.gtms.blocTest.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public interface DeviceBlocTestServ
{
	/**
	 * 根据设备ID查询设备信息
	 * @param deviceId
	 * @return list
	 */
	public List<Map> queryDeviceInfoById(String deviceId);
}
