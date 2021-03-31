/**
 * @(#)AbstractGetSheetList.java 2006-1-12
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.util.StringUtils;

/**
 * 通过开始时间、结束时间，报表类型参数动态获取表名
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public abstract class AbstractGetSheetList implements InterfaceSheetList {
	
	/**time:get the tablename*/
	public long time;
	/**表类型*/
	public int type;
	/**业务类型(int)*/
	public int productType;

	/**
	 * Constrator:抽象类获取表名
	 * 
	 */
	public AbstractGetSheetList() {
		this.time = 0;
		this.type = 0;
		this.productType = 0;
	}
	
	/**
	 * Constrator:抽象类获取表名
	 * 
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>工单表
	 *            <LI>错单表
	 *            <LI>97回单表
	 *            </UL>
	 * @param productType 
	 * 			  业务类型 整形
	 * 			  <UL>
	 * 			  <LI>adsl(3,31,5)
	 *            <LI>lan(4,41)
	 *            <LI>netBar(6)
	 *            </UL>
	 */
	public AbstractGetSheetList(int type, int productType) {
		this.type = type;
		this.productType = productType;
	}

	/**
	 * Constrator:抽象类获取表名
	 * 
	 * @param start
	 *            开始时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param end
	 *            结束时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>工单表
	 *            <LI>错单表
	 *            <LI>97回单表
	 *            </UL>
	 * @param productType 
	 * 			  业务类型 整形
	 * 			  <UL>
	 * 			  <LI>adsl(3,31,5)
	 *            <LI>lan(4,41)
	 *            <LI>netBar(6)
	 *            </UL>
	 */
	public AbstractGetSheetList(long _time, int _type, int _productType) {
		this.time = _time;
		this.type = _type;
		this.productType = _productType;
	}

	/**
	 * 获取表名 当类型为1:工单表(snmp_worksheet_report_yyyyMM)
	 * 当类型为2:错单表(snmp_conf_error_report) 当类型为3:97回单表(tab_97_ret_work)
	 * 
	 * @return String
	 */
	public String getTabName() {
		String tabName = null;

		switch (type) {
		case 1:
			tabName = "gw_worksheet_report_" + StringUtils.getYear((int)time)
					+ StringUtils.getMonthMore((int)time);
			break;
		case 2:
			tabName = "snmp_conf_error_report";
			break;
		case 3:
			tabName = "tab_97_ret_work";
			break;
		case 4:
			tabName = "tab_97work_original_" + StringUtils.getYear((int)time)
			+ StringUtils.getMonthMore((int)time);
			break;
		}

		return tabName;
	}
	
	/**
	 * 获取业务类型(producttype) 
	 *
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>工单表
	 *            <LI>错单表
	 *            <LI>97回单表
	 *            </UL>
	 * @param productType 
	 * 			  业务类型 整形
	 * 			  <UL>
	 * 			  <LI>adsl(3,31,5)
	 *            <LI>lan(4,41)
	 *            <LI>netBar(6)
	 *            </UL>
	 *            
	 * @return String[]
	 */
	public String getProducttype() {
		String tempType = "";

		switch (productType) {
		case 1:
			tempType = "3,31,5";
			break;
		case 2:
			tempType = "4,41";
			break;
		case 3:
			tempType = "6";
			break;
		case -1:
			tempType = "700,701,702,703,704,705,706,707,708,709,710," 
				+ "501,502,503,504,505,506,507,508,509,510";
			break;
		default :
			tempType = "0";
			break;
		}

		return tempType;
	}
	

	/*
	 * 返回工单列表.
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getSheetList();

}
