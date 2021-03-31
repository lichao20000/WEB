/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.dao.gw.interf;

/**
 * get data from db.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 18, 2009
 * @see
 * @since 1.0
 */
public interface I_DAO {

	/**
	 * get data from device.
	 * 
	 * @param obj
	 * @return
	 */
	public Object getData(String deviceId);

}
