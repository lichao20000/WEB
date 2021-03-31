/*
 * @(#)DataFilter.java	1.00 1/20/2006
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.core;

import com.linkage.litms.system.UserRes;



/**
 * 用户访问资源范围过滤接口
 * 
 * @author yuht
 * @version 1.00, 1/20/2006
 * @since Liposs 2.1
 */
public interface DataFilter {
	/**
	 * 根据用户资源数据对设备资源进行过滤
	 * @param userRes
	 * @return Object
	 */
	public Object doFilter(UserRes userRes);
}
