/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.bio.interf;

import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;

/**
 * Interface.Advance search.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 18, 2009
 * @see
 * @since 1.0
 */
public interface I_AdvanceSearchBIO {

	/**
	 * get data from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public Object getData(String deviceId);

	/**
	 * set data for device.
	 * 
	 * @param obj
	 * @return
	 */
	public Object configDev(I_DevConfOBJ obj, long accOId, int type,
			int serviceId, String gw_type);

}
