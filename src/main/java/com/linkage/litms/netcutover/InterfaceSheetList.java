/**
 * @(#)SheetListInterface.java 2006-1-12
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import com.linkage.litms.common.database.Cursor;

/**
 * 通过参数获取工单列表、错单列表.
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public interface InterfaceSheetList {

	/**
	 * 返回工单列表.
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getSheetList();

}
