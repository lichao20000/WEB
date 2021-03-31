/*
 * @(#)AbstractNetPerf.java	1.00 12/31/2005
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.performance;

import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.core.GeneralData;

/**
 * 通过开始时间、结束时间，报表类型参数动态获取表名，并从表中取出相关网络性能 报表类型的文本数据和图形数据。
 * 
 * @author Dolphin
 * @version 1.00, 12/31/2005
 * @see GeneralData
 * @since Liposs 2.1
 */

public abstract class AbstractNetPerf implements GeneralData {
	public long start;

	public long end;

	public int type;

	/**
	 * 构造函数，设置网络性能报表参数
	 * 
	 * @param start
	 *            开始时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param end
	 *            结束时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>1 代表小时报表
	 *            <LI>2 代表日报表
	 *            <LI>3 代表周报表
	 *            <LI>4 代表月报表
	 *            <LI>5 代表年报表
	 *            </UL>
	 */
	public AbstractNetPerf(long start, long end, int type) {
		this.start = start;
		this.end = end;
		this.type = type;
	}

	/**
	 * 缺省构造函数
	 * 
	 */
	public AbstractNetPerf() {
		this.start = 0;
		this.end   = 0;
		this.type  = 1;
	}

	/**
	 * 根据相关参数动态计算显示文本性能相关表名
	 * 
	 * @return 返回文本数据表名字符串
	 */
	public String getTxtTblName() {
		String tblName = null;
		DateTimeUtil dtUtil = new DateTimeUtil(start * 1000);
		switch (type) {
		case 0:
				tblName = "pm_raw_" + dtUtil.getYear() + "_"
						+ dtUtil.getMonth();
				break;
		case 1:
			tblName = "pm_hour_stats_" + dtUtil.getYear() + "_"
					+ dtUtil.getMonth();
			break;
		case 2:
			tblName = "pm_day_stats_" + dtUtil.getYear();
			break;
		case 3:
			tblName = "pm_week_stats_" + dtUtil.getYear();
			break;
		case 4:
			tblName = "pm_month_stats_" + dtUtil.getYear();
			break;
		case 5:
			tblName = "pm_month_stats_" + dtUtil.getYear();
			break;
		}
		return tblName;
	}

	/**
	 * 根据相关参数动态计算显示图形性能相关表名
	 * 小时 日报表在原始表中取数据
	 * 日周月报表在小时统计表中取数据
	 * @return 返回图形数据表名字符串
	 */
	public String getChartTblName() {
		String tblName = null;
		DateTimeUtil dtUtil = new DateTimeUtil(start * 1000);
		//暂都从原始数据中取数据
		switch (type) {
		case 1://小时报表
			//每5分钟一条
			tblName = "pm_raw_" + dtUtil.getYear() + "_" + dtUtil.getMonth();
			break;
		case 2://日报表
			//每小时一条
			//tblName = "pm_hour_stats_" + dtUtil.getYear() + "_"
			//		+ dtUtil.getMonth();
			tblName = "pm_raw_" + dtUtil.getYear() + "_" + dtUtil.getMonth();
			break;
		case 3://周报表
			//每天一条
			//tblName = "pm_day_stats_" + dtUtil.getYear();
			tblName = "pm_hour_stats_" + dtUtil.getYear() + "_"
			+ dtUtil.getMonth();
			break;
		case 4://月报表
			//每星期一条
			//tblName = "pm_week_stats_" + dtUtil.getYear();
			tblName = "pm_hour_stats_" + dtUtil.getYear() + "_"
			+ dtUtil.getMonth();
			break;
		case 5://年报表
			tblName = "pm_day_stats_" + dtUtil.getYear();
			break;
		}
		return tblName;
	}
	
	/**
	 * 根据访问数据库表的不同，查询不同的数据库列
	 * 除小时报表（pm_raw_）外 查询最大最小均值列
	 * @return
	 */
	public String getChartTblColume() {
		String cols = null;
		
		//小时报表 只有value列
		if(type==1||type==2)
			cols ="value";
		else
			cols = "maxvalue,minvalue,avgvalue";
		
		return cols;
	}
	
	/**
	 * 根据报表返回不同的日期显示格式
	 * @return
	 */
	public String getDateFormat()
	{
		String dateFormatStr="H:mm";
		switch (type) 
		{
			//小时报表
			case 1:
				dateFormatStr="HH:mm";
				break;
			//日报表
			case 2:
				dateFormatStr="HH:mm";
			    break;
			//周报表
			case 3:
				dateFormatStr="EE HH:mm";
				break;
			//月报表
			case 4:
				dateFormatStr="MM-dd";
				break;
			//年报表
			case 5:
				dateFormatStr="yyyy-MM";
				break;			
		}
		
		return dateFormatStr;
	}

	/**
	 * 获取网络性能报表的文本数据
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getGeneralTxtData();

	/**
	 * 获取网络性能报表的图形数据
	 * 
	 * @return 返回Cursor类型数据
	 */
	public abstract Cursor getGeneralChartData();
}
