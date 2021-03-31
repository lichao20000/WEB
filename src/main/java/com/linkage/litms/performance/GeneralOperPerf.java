/*
 * @(#)GeneralNetPerf.java	1.00 12/31/2005
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.performance;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;

/**
 * 继承AbstractNetPerf抽象类，实现其接口GeneralData的方法。<br>
 * 取出网络性能的文本数据和图形数据。
 * 
 * @author Shil
 * @version 1.00, 12/26/2007
 * @see AbstractNetPerf
 * @since Liposs 2.1
 */

public class GeneralOperPerf extends AbstractNetPerf {
	

	// 获取所有设备的业务性能查询报表的文本数据SQL
	// 通过时间 报表类型 设备号
//	private String allDeviceTxtSQL = "select a.device_id,a.device_name,b.username,c.oid_type,c.oid_desc,c.unit,d.* from tab_gw_device a  "
//			+ " left join tab_egwcustomer b on a.device_id=b.device_id "
//			+ "left join gw_device_model e on a.device_model_id=e.device_model_id and a.oui=e.oui "
//			+ "left join tab_gw_model_oper_oid c on e.device_model_id=c.device_model inner join ? d on d.device_id=a.device_id and d.oid_type=c.oid_type and c.oid_type>100  "
//			+ " where d.gathertime>? and d.gathertime<? and d.device_id in(?) order by a.device_id,c.oid_type,b.username";
	private String allDeviceTxtSQL = "select a.device_id,a.device_name,b.username,c.oid_type,c.oid_desc,c.unit,d.* from tab_gw_device a  "
		+ " left join tab_egwcustomer b on a.device_id = b.device_id "
		+ "left join gw_device_model e on a.device_model_id=e.device_model_id and a.vendor_id=e.vendor_id "
		+ "left join tab_gw_model_oper_oid c on e.device_model_id=c.device_model inner join ? d on d.device_id=a.device_id and d.oid_type=c.oid_type and c.oid_type>100  "
		+ " where d.gathertime>? and d.gathertime<? and d.device_id in(?) order by a.device_id,c.oid_type,b.username";	
	
	/**
	 * 查询指定设备支持的性能指标
	 */
	private static String deviceClassSQL="select a.device_id,a.loopback_ip,a.device_name,b.oid_type,b.oid_desc,b.unit from tab_gw_device a, tab_gw_model_oper_oid b ,gw_device_model c"
		+" where a.device_model_id=c.device_model_id and c.device_model_id=b.device_model and a.device_id in(?) and b.oid_type>100 order by a.device_id,b.oid_type";
	
	
	/**
	 * 查询指定设备指定性能指标的性能数据
	 */
	private static String deviceClassChartSQL="select * from ? where device_id =? and oid_type=? and gathertime>? and gathertime <?  ";

	/**
	 * 构造函数，设置业务性能报表参数
	 * 
	 * @param start
	 *            开始时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param end
	 *            结束时间 表示 1970 年1月1日 00:00:00 GMT 以后 time 秒的时间点 长整形
	 * @param type
	 *            报表类型 整形
	 *            <UL>
	 *            <LI>1 代表原始报表
	 *            <LI>2 代表周报表
	 *            <LI>3 代表月报表
	 *            </UL>
	 * @param arrId
	 *            查询条件，业务性能采集设备号数组
	 */
	public GeneralOperPerf(long start, long end, int searchType) {
		super(start, end, searchType);		
	}

	/**
	 * 获取表名
	 */
	public String getTxtTblName() {
		String tblName = null;
		DateTimeUtil dtUtil = new DateTimeUtil(start * 1000);
		String dateStr = dtUtil.getShortDate();
		switch (type) {
		// 日报表
		case 2:
			tblName = "gw_pm_raw_" + dtUtil.getYear() + "_"
					+ dtUtil.getMonth();
			break;
		// 周报表
		case 3:
			tblName = "gw_pm_week_stat_" + dateStr.substring(0, 4);
			break;
		// 月报表
		case 4:
			tblName = "gw_pm_month_stat";
			break;
		}
		return tblName;

	}
	
	/**
	 * 获取画图表
	 */
	public String getChartTblName()
	{
		String tblName = null;
		DateTimeUtil dtUtil = new DateTimeUtil(start * 1000);
		//String dateStr = dtUtil.getShortDate();		
		tblName="gw_pm_raw_" + dtUtil.getYear() + "_"	+ dtUtil.getMonth();
		return tblName;
	}
	
	/**
	 * 查询出指定设备支持的业务性能指标
	 * @param request
	 * @return
	 */
	public static Cursor getAllDeviceClassData(HttpServletRequest request)
	{
		//设置设备ID
		String device_id_str = request.getParameter("device_ids");
		String[] device_ids = device_id_str.split(",");
		String param = "";
		for (int i = 0; i < device_ids.length; i++) {
			if ("".equals(param)) {
				param = "'" + device_ids[i] + "'";
			} else {
				param += ",'" + device_ids[i] + "'";
			}
		}
		
		//构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(deviceClassSQL);
		pSQL.setStringExt(1,param,false);
		
		return DataSetBean.getCursor(pSQL.getSQL());	
	}

	/**
	 * 查询出指定设备的业务性能数据
	 * @param request
	 * @return
	 */
	public Cursor getAllDeviceGeneralTxtData(HttpServletRequest request) {

		//设置设备ID
		String device_id_str = request.getParameter("device_ids");
		String[] device_ids = device_id_str.split(",");
		String param = "";
		for (int i = 0; i < device_ids.length; i++) {
			if ("".equals(param)) {
				param = "'" + device_ids[i] + "'";
			} else {
				param += ",'" + device_ids[i] + "'";
			}
		}
		
		
		String tableName =getTxtTblName();

		// 构造预处理SQL
		PrepareSQL pSQL = new PrepareSQL(allDeviceTxtSQL);
		pSQL.setStringExt(1,tableName,false);
		pSQL.setLong(2, start);
		pSQL.setLong(3, end);
		pSQL.setStringExt(4,param,false);		
		
		return DataSetBean.getCursor(pSQL.getSQL());
	}
	
	
	
	
	
	/**
	 * 获得图形数据 每个设备一个cursor 可遍历设备编号字符串ids 对每个设备生成Cursor
	 * 
	 * @return Cursor
	 */
	public Cursor getAllDeviceGeneralChartData(HttpServletRequest request) {
		
		//设置设备ID
		String device_id = request.getParameter("device_id");		
		String class1 = request.getParameter("class1");

		//表名
		String tblName = getChartTblName();

		PrepareSQL pSQL = new PrepareSQL(deviceClassChartSQL);
		pSQL.setStringExt(1,tblName,false);
		pSQL.setStringExt(2,device_id,true);
		pSQL.setStringExt(3,class1,false);
		pSQL.setLong(4, start);
		pSQL.setLong(5, end);
		
		return DataSetBean.getCursor(pSQL.getSQL());		
	}

	@Override
	public Cursor getGeneralTxtData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor getGeneralChartData() {
		// TODO Auto-generated method stub
		return null;
	}

}
