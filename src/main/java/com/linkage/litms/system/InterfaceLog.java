/**
 * @(#)InterfaceLog.java 2006-1-21
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.system;

import com.linkage.litms.common.database.Cursor;

/**
 * 获取数据库日志记录
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public interface InterfaceLog {
	
	/**
	 * 获取数据库日志记录
	 * 
	 * @param _value	字段值(Object)
	 * @param _filed	数据库字段名(String)
	 * @return 日志列表(Cursor).
	 */
	public Cursor getLog (Object _value, String _filed );
}
