/*
 * @(#)GeneralPerf.java	1.00 12/31/2005
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.core;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.host.GeneralHostPerf;
import com.linkage.litms.performance.GeneralNetPerf;

/**
 * 通过参数动态获取表名，并从表中取出相关性能报表类型的文本数据和图形数据。
 * 
 * @author Dolphin
 * @version 1.00, 12/31/2005
 * @see GeneralNetPerf
 * @see GeneralHostPerf
 * @see GeneralFluxPerf
 * @since Liposs 2.1
 */

public interface GeneralData {
	/**
	 * 获取性能报表的文本数据
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getGeneralTxtData();

	/**
	 * 获取性能报表的图形数据
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getGeneralChartData();
}
