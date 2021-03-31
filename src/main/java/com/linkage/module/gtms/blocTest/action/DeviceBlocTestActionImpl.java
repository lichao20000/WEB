
package com.linkage.module.gtms.blocTest.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.blocTest.serv.DeviceBlocTestServ;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since May 14, 2012
 * @category com.linkage.module.gtms.blocTest.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceBlocTestActionImpl extends SplitPageAction implements
		DeviceBlocTestAction
{

	private static final long serialVersionUID = 2508466584775846343L;
	private static Logger logger = LoggerFactory.getLogger(DeviceBlocTestActionImpl.class);
	/**设备id*/
	private String deviceId = null;
	private List<Map> deviceInfoList = null;
	private DeviceBlocTestServ serv;

	/**
	 * AP设备采集信息展示
	 */
	@Override
	public String queryDeviceInfoById()
	{
		logger.debug("deviceId" + deviceId);
		deviceInfoList = serv.queryDeviceInfoById(deviceId);
		return "queryInfoList";
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public List<Map> getDeviceInfoList()
	{
		return deviceInfoList;
	}

	public void setDeviceInfoList(List<Map> deviceInfoList)
	{
		this.deviceInfoList = deviceInfoList;
	}

	public DeviceBlocTestServ getServ()
	{
		return serv;
	}

	public void setServ(DeviceBlocTestServ serv)
	{
		this.serv = serv;
	}
}
