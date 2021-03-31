/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.module.gwms.interf.dao;

import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * gw_serv_strategy
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 18, 2009
 * @see
 * @since 1.0
 */
public interface I_StrategyDAO {

	/**
	 * new Strategy
	 * 
	 * @param obj
	 * @return
	 */
	public Object addStrategy(StrategyOBJ obj);
}
