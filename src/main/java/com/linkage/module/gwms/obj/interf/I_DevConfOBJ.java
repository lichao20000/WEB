/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.obj.interf;

/**
 * interface: the object of device_config.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 18, 2009
 * @see
 * @since 1.0
 */
public interface I_DevConfOBJ {

	/**
	 * get:deviceId
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId();

	/**
	 * get:gather_time
	 * 
	 * @return the gather_time
	 */
	public long getGatherTime();

	/**
	 * set:deviceId
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId);

	/**
	 * set:gather_time
	 * 
	 * @param gather_time
	 *            the gather_time to set
	 */
	public void setGatherTime(long gatherTime);

}
