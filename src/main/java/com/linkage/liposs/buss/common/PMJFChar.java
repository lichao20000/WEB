package com.linkage.liposs.buss.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jfree.chart.JFreeChart;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.litms.common.util.DateTimeUtil;

public class PMJFChar {

	private JdbcTemplate jt;

	private DataSource dao;

	private String[] rowKeys;

	private int reportType = 1;

	private String valueColumn = "value";

	ArrayList<Map> list = null;

	private String title = "";

	private String x_pos_name = "";

	private String y_pos_name = "";

	private String strTime_end = null;

	private String strTime_start = null;

	private ChartUtil cu;

	private String sParam;

	PrepareSQL PSQL = null;

	/**
	 * 用于构造表明
	 * 日报表为：pm_raw_year_month、周：pm_hour_stats_year_month、月:pm_day_stat_year
	 */
	private String tableFirst = "pm_raw_year_month_";

	/**
	 * CPU性能图的方法
	 * 
	 * @return JFreeChart
	 */
	public JFreeChart getCpu(String deviceId, int type) {
		JFreeChart reJFC;
		long end = new Date().getTime() / 1000;
		ArrayList cpuList = new ArrayList();
		cpuList = this.getCpuInfo(deviceId);
		reJFC = this.getCursorReport(cpuList, new DateTimeUtil(end * 1000),
				type);
		return reJFC;
	}

	/**
	 * 获取CPU的实例ID
	 * 
	 * @param deviceId
	 * @return ArrayList list中存放的是map
	 */
	public ArrayList getCpuInfo(String deviceId) {
		list = new ArrayList();
		String cupSQL = "select a.expressionid as expressionid, a.name as name,a.descr as descr,c.id as id,c.indexid as indexid from pm_expression a ,"
				+ "pm_map b,pm_map_instance c where a.expressionid =b.expressionid "
				+ "and c.device_id = b.device_id and a.class1=1 and b.isok= 1  "
				+ "and c.expressionid = a.expressionid and b.device_id='"
				+ deviceId + "'";
		list = new ArrayList();
		jt = new JdbcTemplate(dao);
		PrepareSQL psql = new PrepareSQL(cupSQL);
		list = (ArrayList) jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 内存性能图的方法
	 * 
	 * @return JFreeChart
	 */
	public JFreeChart getMem(String deviceId, int type) {
		JFreeChart reJFC;
		long end = new Date().getTime() / 1000;
		ArrayList memList = new ArrayList();
		memList = this.getMemInfo(deviceId);
		reJFC = this.getCursorReport(memList, new DateTimeUtil(end * 1000),
				type);
		return reJFC;
	}

	/**
	 * 获取内存实例ID
	 * 
	 * @param deviceId
	 * @return ArrayList list中存放的是map
	 */
	public ArrayList getMemInfo(String deviceId) {
		list = new ArrayList();
		String cupSQL = "select a.expressionid as expressionid, a.name as name,a.descr as descr,c.id as id,c.indexid as indexid from pm_expression a ,"
				+ "pm_map b,pm_map_instance c where a.expressionid =b.expressionid "
				+ "and c.device_id = b.device_id and a.class1=2 and b.isok= 1  "
				+ "and c.expressionid = a.expressionid and b.device_id='"
				+ deviceId + "'";
		list = new ArrayList();
		jt = new JdbcTemplate(dao);
		PrepareSQL psql = new PrepareSQL(cupSQL);
		list = (ArrayList) jt.queryForList(psql.getSQL());
		return list;
	}

	/**
	 * 获取图片数据
	 * 
	 * @param listID
	 *            设备实例Id数组
	 * @param dateTimeUtil
	 *            当前时间秒数
	 * @param type
	 *            报表的时间间隔类型
	 * @return JFreeChart
	 */
	public JFreeChart getCursorReport(List listID, DateTimeUtil dateTimeUtil,
			int type) {
		ArrayList<Map>[] reList = new ArrayList[listID.size()];
		int size = listID.size();
		Map tmpMap = null;
		rowKeys = new String[size];
		String timeType = "";
		String sReportType = "";
		String strSQL = null;
		String tablenameA = null;
		String tablenameB = null;
		// 根据报表类型 时间向前推
		if (this.reportType == 1) {
			timeType = "小时";
			sReportType = "[日线图]";
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextDate(-1);
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
		} else if (this.reportType == 2) {
			timeType = "日";
			sReportType = "[周线图]";
			DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
					.getLongTime() * 1000);
			// 周报表暂时从原始数据表中取数据
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextDate(-7);
			tmpTimeUtil.getNextDate(-7);
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
			// 遍历时间 再检验某个日期是否为星期一
			for (int i = 1; i <= 8; i++) {
				// 是否是星期一
				if (tmpTimeUtil.getNoDayOfWeek("CN") == 1) {
					// marker = tmpTimeUtil.getLongTime();
				}
				// 向前加一天
				tmpTimeUtil.getNextDate(1);
			}
		} else {
			timeType = "日";
			sReportType = "[月线图]";
			DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
					.getLongTime() * 1000);
			// 月报表暂时从原始数据表中取数据
			strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
			long _end = dateTimeUtil.getLongTime();
			tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			dateTimeUtil.getNextMonth(-1);
			tmpTimeUtil.getNextMonth(-1);
			long _start = dateTimeUtil.getLongTime();
			tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
					+ dateTimeUtil.getMonth();
			strTime_start = dateTimeUtil.getDate() + " "
					+ dateTimeUtil.getTime();
			long days = (_end - _start) / (24 * 3600);
			days += 1;
			// 遍历时间 再检验某个日期是否为月初第一天
			for (int i = 1; i <= days; i++) {
				// 是否是是月初第一天
				if (tmpTimeUtil.getDay() == 1) {
					// marker = tmpTimeUtil.getLongTime();
				}
				// 向前加一天
				tmpTimeUtil.getNextDate(1);
			}
		}
		// 得到当前月份
		long start = dateTimeUtil.getLongTime();
		// 判断昨天和今天是否在同一个月内
		// 如果相等，则只需要查询一张表
		if (tablenameA.equals(tablenameB)) {
			strSQL = "select ? ,gathertime from " + tablenameB
					+ " where gathertime > " + start + " and id = ?";
		} else {
			strSQL = "select ? ,gathertime from " + tablenameA
					+ " where gathertime > " + start + " and id = ?"
					+ " union  " + "select ? ,gathertime from " + tablenameB
					+ " where id = ?";
		}
		if (listID.size() != 0) {
			for (int i = 0; i < listID.size(); i++) {
				tmpMap = (Map) listID.get(i);
				PSQL.setSQL(strSQL);
				PSQL.setStringExt(1, reportType == 3 ? "avgvalue"
						: this.valueColumn, false);
				PSQL.setStringExt(2, (String) tmpMap.get("id"), true);
				PSQL.setStringExt(3, reportType == 3 ? "avgvalue"
						: this.valueColumn, false);
				PSQL.setStringExt(4, (String) tmpMap.get("id"), true);
				rowKeys[i] = sParam + (String) tmpMap.get("indexid");
				reList[i] = (ArrayList) jt.queryForList(PSQL.getSQL());
			}
			title = (String) tmpMap.get("name") + sReportType;
			x_pos_name = "时间  单位:" + "(" + timeType + ")";
			y_pos_name = "百分比(%)";

		}
		cu.setYMMT(ChartYType.PERCENT);
		JFreeChart jfc = cu.createSTP(title, x_pos_name, y_pos_name, rowKeys,
				reList, "gathertime", reportType == 3 ? "avgvalue"
						: this.valueColumn, type);
		return jfc;
	}

	/**
	 * 
	 * @param type
	 *            报表类型 日: 1, 周: 2, 月: 3
	 */
	public void setReportType(int type) {
		switch (type) {
		case 1:
			this.reportType = 1;
			this.tableFirst = "pm_raw_";
			break;
		case 2:
			this.reportType = 2;
			this.tableFirst = "pm_raw_";
			break;
		case 3:
			this.reportType = 3;
			this.tableFirst = "pm_hour_stats_";
			break;
		}
	}

	/**
	 * 此方法主要是用来处理图片生成的时候设置图表下面显示名称标识,
	 * 
	 * 1 表示 CPU , 2 表示 内存
	 * 
	 * @param param
	 */
	public void setParam(int param) {
		switch (param) {
		case 1:
			sParam = "CPU-";
			break;
		case 2:
			sParam = "MEM-";
			break;
		case 3:
			break;
		}
	}

	public String getValueColumn() {
		return valueColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public void setDao(DataSource dao) {
		this.dao = dao;
	}

	public void setPSQL(PrepareSQL psql) {
		PSQL = psql;
	}

	public void setCu(ChartUtil cu) {
		this.cu = cu;
	}

}
