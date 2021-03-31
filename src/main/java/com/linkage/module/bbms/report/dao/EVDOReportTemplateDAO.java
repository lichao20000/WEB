/**
 *
 */
package com.linkage.module.bbms.report.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-11-20
 * @category com.linkage.module.bbms.report.dao
 *
 */
public class EVDOReportTemplateDAO {
	/** log */
	private Logger logger = LoggerFactory
			.getLogger(EVDOReportTemplateDAO.class);

	private JdbcTemplateExtend jt;

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * 查询所有的行业
	 * @return
	 */
	public List getCustomerType(){
		logger.debug("getCustomerType()");
		PrepareSQL psql = new PrepareSQL(" select cust_type_id,cust_type_name from tab_customer_type ");
		psql.getSQL();
		return jt.queryForList(" select cust_type_id,cust_type_name from tab_customer_type ");
	}

	/**
	 * 统计在线时长
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getTimeLengthData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getTimeLengthData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_time_day";
		if ("2".equals(reportType)) {
			tableName = "report_time_week";
		}
		if ("3".equals(reportType)) {
			tableName = "report_time_month";
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,wire_time,wireless_time from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 统计网关流量
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getFluxData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getFluxData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_flux_day";
		if ("2".equals(reportType)) {
			tableName = "report_flux_week";
		}
		if ("3".equals(reportType)) {
			tableName = "report_flux_month";
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,wire_up_flux,wire_down_flux,wireless_up_flux,wireless_down_flux from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 统计网关频率
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getFrequencyData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getFrequencyData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_evdo_frequency_day";
		if ("2".equals(reportType)) {
			tableName = "report_evdo_frequency_week";
		}
		if ("3".equals(reportType)) {
			tableName = "report_evdo_frequency_month";
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,frequency from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 统计网关时段
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getTmslotData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getTmslotData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_evdo_tmslot_day";
		if ("2".equals(reportType)) {
			tableName = "report_evdo_tmslot_week";
		}
		if ("3".equals(reportType)) {
			tableName = "report_evdo_tmslot_month";
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,tmslot_0,tmslot_1,tmslot_2,tmslot_3,tmslot_4,tmslot_5,tmslot_6,tmslot_7,tmslot_8,tmslot_9,tmslot_10,tmslot_11,tmslot_12,tmslot_13,tmslot_14,tmslot_15,tmslot_16,tmslot_17,tmslot_18,tmslot_19,tmslot_20,tmslot_21,tmslot_22,tmslot_23 from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 统计网关激活
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getActiveData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getActiveData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_evdo_active_day";
		if ("2".equals(reportType)) {
			tableName = "report_evdo_active_week";
		}
		if ("3".equals(reportType)) {
			tableName = "report_evdo_active_month";
		}
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,active_num,activeless_num from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}

	/**
	 * 统计网关类型
	 *
	 * @param reportType
	 *            报表类型 ‘1’日报表 ‘2’周报表 ‘3’月报表
	 * @param countDate
	 *            统计日期 日报表:20091119 周报表:200907 月报表:200907
	 * @param countType
	 *            统计类型 ‘1’表示按区域 ‘2’表示按行业
	 * @return
	 */
	public List getEvdoTypeData(String reportType, String countDate,
			String countType) {
		logger.debug(
				"getEvdoTypeData(reportType:{},countDate:{},countType:{})",
				new Object[] { reportType, countDate, countType });
		// 首先确定是什么报表，以此确定从哪张报表取数据
		String tableName = "report_evdo_type_week";
		PrepareSQL ppSQL = new PrepareSQL();
		ppSQL.setSQL(" select count_desc,main_num,standy_num from "
				+ tableName + " where count_date=? and count_type=? ");
		ppSQL.setInt(1, Integer.parseInt(countDate));
		ppSQL.setInt(2, Integer.parseInt(countType));
		return jt.queryForList(ppSQL.toString());
	}
}
