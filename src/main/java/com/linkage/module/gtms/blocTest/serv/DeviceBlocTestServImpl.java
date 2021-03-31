
package com.linkage.module.gtms.blocTest.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.blocTest.dao.DeviceBlocTestDAO;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since May 11, 2012
 * @category com.linkage.module.gtms.blocTest.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceBlocTestServImpl implements DeviceBlocTestServ
{

	private static Logger logger = LoggerFactory.getLogger(DeviceBlocTestServImpl.class);
	private DeviceBlocTestDAO deviceBlocTestDAO;
	/** SuperGather CORBA */
	private SuperGatherCorba sgCorba;

	/**
	 * 根据设备ID查询设备信息
	 * 
	 * @param deviceId
	 * @return list
	 */
	@Override
	public List<Map> queryDeviceInfoById(String deviceId)
	{
		sgCorba = new SuperGatherCorba(String.valueOf(LipossGlobals.getGw_Type(deviceId)));
		List<Map> list = null;
		// SG
		if (sgCorba.getCpeParams(deviceId, 66) == 1)
		{
			logger.warn("getAlg sg fail");
			list = deviceBlocTestDAO.queryDeviceInfoById(deviceId);
		}
		return list;
	}

	public DeviceBlocTestDAO getDeviceBlocTestDAO()
	{
		return deviceBlocTestDAO;
	}

	public void setDeviceBlocTestDAO(DeviceBlocTestDAO deviceBlocTestDAO)
	{
		this.deviceBlocTestDAO = deviceBlocTestDAO;
	}
}
