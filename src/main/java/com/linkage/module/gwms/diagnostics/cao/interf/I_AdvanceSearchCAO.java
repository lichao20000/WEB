/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.diagnostics.cao.interf;

/**
 * CAO Interface for advance search.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 25, 2009
 * @see
 * @since 1.0
 */
public interface I_AdvanceSearchCAO {

	/**
	 * add new strategy.
	 * 
	 * @param id
	 *            strategy id.
	 * @return
	 */
	public boolean addStrategyToPP(String id);

	/**
	 * get data from
	 * 
	 * @param deviceId
	 *            device_id
	 * @param type
	 *            data type.
	 * @return
	 */
	public int getDataFromSG(String deviceId, int type);
}
