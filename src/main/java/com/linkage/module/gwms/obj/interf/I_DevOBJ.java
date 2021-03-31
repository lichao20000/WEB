/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.obj.interf;

/**
 * interface: the object of device.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 18, 2009
 * @see
 * @since 1.0
 */
public interface I_DevOBJ {

	/**
	 * get:deviceId
	 * 
	 * @return the deviceId
	 */
	public String getDeviceId();

	/**
	 * get:oui
	 * 
	 * @return the oui
	 */
	public String getOui();

	/**
	 * get:device_serialnumber
	 * 
	 * @return the device_serialnumber
	 */
	public String getSn();

	/**
	 * set:deviceId
	 * 
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId);

	/**
	 * set:oui
	 * 
	 * @param oui
	 *            the oui to set
	 */
	public void setOui(String oui);

	/**
	 * set:device_serialnumber
	 * 
	 * @param device_serialnumber
	 *            the device_serialnumber to set
	 */
	public void setSn(String sn);

}
