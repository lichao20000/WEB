
package com.linkage.module.gtms.blocTest.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since May 11, 2012
 * @category com.linkage.module.gtms.blocTest.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public interface DeviceBlocTestDAO
{
	/**
	 * 根据设备ID查询设备信息
	 * @param deviceId
	 * @return
	 */
	public List<Map> queryDeviceInfoById(String deviceId);
}
