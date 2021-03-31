/**
 * LINKAGE TECHNOLOGY (NANJING) CO.,LTD.<BR>
 * Copyright 2007-2010. All right reserved.
 */
package com.linkage.litms.host;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.chart.LineVolumeChart;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.util.StringUtil;

/**
 * 主机性能
 * 
 * @author HEMC,Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Aug 4, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class HGHostPerfmance {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(HGHostPerfmance.class);
	/**
	 * start_time
	 */
	private long s_time = 0;
	/**
	 * end_time
	 */
	private long e_time = 0;
	/**
	 * host ip
	 */
	private String hostip = null;
	/**
	 * 主机性能属性 1-cpu 2-内存 3-文件系统 4-进程
	 */
	private short attr_id = 0;
	/**
	 * 采集点
	 */
	private String gather_id = "";
	/**
	 * instanct array
	 */
	@SuppressWarnings("unused")
	private String[] instanceArr = null;
	/**
	 * request object
	 */
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	/** 性能数据查询sql */
	private String m_Host_Sql = "select instance,gettime,value1,value2"
			+ " from tab_performance where"
			+ " hostip=? and attr_id=? and gettime>=? and gettime<=? and gather_id=?"
			+ " order by gettime";
	/** 性能数据统计sql */
	private String m_HostAll_Sql = "select attr_id,instance,gettime,value1,value2 "
			+ " from tab_performance where"
			+ " hostip=? and attr_id<4 and gettime>=? and gettime<=? and gather_id=?"
			+ " order by gettime,attr_id,instance";

	/**
	 * construct function
	 * 
	 * @param request
	 */
	public HGHostPerfmance(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		gather_id = request.getParameter("gather_id");

		DateTimeUtil dateTime = new DateTimeUtil(start_time);
		s_time = dateTime.getLongTime();
		dateTime = new DateTimeUtil(end_time);
		e_time = dateTime.getLongTime();

		logger.debug(start_time + "=" + s_time);
		logger.debug(end_time + "=" + e_time);

		this.hostip = request.getParameter("hostip");
		String strAttr = request.getParameter("attr_id");
		if (strAttr == null)
			strAttr = "1";
		attr_id = Short.parseShort(strAttr);
		this.instanceArr = request.getParameterValues("instance");
		dateTime = null;
	}

	/**
	 * 性能展示 所有数据
	 * 
	 * @param request
	 * @return
	 */
	public String getHostPerfAll() {
		logger.debug("getHostPerfAll()");

		PrepareSQL pSQL = new PrepareSQL(m_HostAll_Sql);
		pSQL.setString(1, this.hostip);
		pSQL.setLong(2, this.s_time);
		pSQL.setLong(3, this.e_time);
		pSQL.setString(4, this.gather_id);

		Set tmpset = new HashSet();

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		StringBuffer sbAll = new StringBuffer();
		StringBuffer sbTH = new StringBuffer();
		StringBuffer sbTR = new StringBuffer();
		sbAll.append("<TABLE");
		sbAll.append(" border=0 cellspacing=1 cellpadding=2");
		sbAll.append(" width='98%' align=center bgcolor=#999999>");
		sbTH.append("<TR>");
		sbTH.append("<TH>时间</TH>");
		sbTH.append("<TH>CPU(%)</TH>");
		sbTH.append("<TH>内存(%)</TH>");

		Map map = cursor.getNext();
		long timeOld = 0;
		long time = 0;
		String inst = null;
		String free = null;
		String total = null;
		while (map != null) {
			if (StringUtil.getIntValue(map, "attr_id") == 3) {
				inst = StringUtil.getStringValue(map, "instance");
				if (tmpset.add(inst) == true) {
					sbTH.append("<TH>");
					sbTH.append(inst);
					sbTH.append("(%)</TH>");
				}
			}

			time = StringUtil.getLongValue(map, "gettime");
			if (timeOld != time) {
				sbTR.append("<TR bgcolor=#ffffff>");
				sbTR.append("<TD>");
				sbTR.append(formateTime(time));
				sbTR.append("</TD>");
				timeOld = time;
			}

			sbTR.append("<TD>");
			total = (String) map.get("value1");
			free = (String) map.get("value2");

			logger.debug("{},{}", total, free);
			if (StringUtil.getIntValue(map, "attr_id") == 1) {
				sbTR.append(total);
			} else if (StringUtil.getIntValue(map, "attr_id") == 2) {
				String rat = getMemValue(free, total);
				sbTR.append(rat);
			} else {
				String rat = getFSValue(free, total);
				sbTR.append(rat);
			}

			sbTR.append("</TD>");

			map = cursor.getNext();
		}

		sbTH.append("</TR>");

		sbAll.append(sbTH);
		sbAll.append(sbTR);
		sbAll.append("</TABLE>");

		return sbAll.toString();
	}

	/**
	 * 性能展示 表格数据 方法
	 * 
	 * @param request
	 * @return
	 */
	public String getHostPerfTable() {
		logger.debug("getHostPerfTable()");

		PrepareSQL pSQL = new PrepareSQL(m_Host_Sql);
		pSQL.setString(1, this.hostip);
		pSQL.setLong(2, this.attr_id);
		pSQL.setLong(3, this.s_time);
		pSQL.setLong(4, this.e_time);
		pSQL.setString(5, this.gather_id);

		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		StringBuffer sbAll = new StringBuffer();
		sbAll.append("<TABLE");
		sbAll.append(" border=0 cellspacing=1 cellpadding=2");
		sbAll.append(" width='98%' align=center bgcolor=#999999>");
		switch (attr_id) {
		case 1:
			sbAll.append(printCPUTable(cursor));
			break;
		case 2:
			sbAll.append(printMMTable(cursor));
			break;
		case 3:
			sbAll.append(printFSTable(cursor));
			break;
		case 4:
			sbAll.append(printProcess(cursor));
			break;
		case 5:
			sbAll.append(printPort(cursor));
			break;
		default:
			sbAll.append(printCPUTable(cursor));
		}
		sbAll.append("</TABLE>");

		return sbAll.toString();
	}

	/**
	 * 性能图形展示 方法
	 * 
	 * @param request
	 * @return
	 */
	public String getHostPerfMrtg(Cursor[] cursor, String title,
			String[] lineName, String xTitle, String yTitle, String xField,
			String yField) {
		LineVolumeChart chart = new LineVolumeChart();
		/*
		 * chart.setDataset_H(cursor); chart.setXField(xField);
		 * chart.setYField(yField); chart.setToolTip(false);
		 */
		chart.setTimeType(4);
		chart.setWidth(600);
		chart.setHeight(250);
		chart.setChartBaseinfo(title, xTitle, yTitle, xField, yField, 3);
		// chart.setDomainAxis("H",5,1);
		chart.setDomainAxis("dd H:mm");
		chart.setVertical(true);
		chart.setInverted(false);
		chart.setPositiveArrowVisible(true);
		chart.setChartDataset(cursor, lineName, null);
		String filename = null;
		try {
			filename = chart.createChart(request.getSession(), response
					.getWriter());
			chart = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	public String printCPUTable(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		double warmValue = getWarnValue();

		Map fields = cursor.getNext();
		sb
				.append("<th nowrap>CPU利用率(%)</th><th nowrap>CPU空闲率(%)</th><th nowrap>时间</th>");
		if (fields == null) {
			sb.append("<tr bgcolor=#ffffff><td colspan=3>查询无数据!</td></tr>");
		}
		while (fields != null) {
			String warmFlag = "0";

			String total = (String) fields.get("value1");

			if (total != null && !"".equals(total)) {
				if (Double.parseDouble(total) > warmValue) {
					warmFlag = "1";
				}
			}

			sb.append("<tr bgcolor=#ffffff>");
			if ("1".equals(warmFlag)) {
				sb.append("<td><font color=red>" + fields.get("value1")
						+ "</font></td>");
				sb.append("<td>" + fields.get("value2") + "</td>");
			} else {
				sb.append("<td>" + fields.get("value1") + "</td>");
				sb.append("<td>" + fields.get("value2") + "</td>");
			}

			sb.append("<td nowrap>" + formateTime(fields.get("gettime"))
					+ "</td>");
			sb.append("</tr>");
			fields = cursor.getNext();
		}
		cursor.Reset();
		try {
			// 暂时不需要图形显示
			String filename = getHostPerfMrtg(new Cursor[] { cursor },
					"CPU利用率曲线图", new String[] { "CPU" }, "时间", "CPU利用率(%)",
					"gettime", "value1");
			if (filename != null) {
				String graphURL = request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename;
				sb
						.append("<tr bgcolor=#ffffff><td colspan=3 align=center><img src='"
								+ graphURL
								+ "' border=0 usemap=\'#"
								+ filename
								+ "\'></td></tr>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("<tr class=green_foot><td colspan=3>&nbsp;</td></tr>");
		return sb.toString();
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	public String printTable(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		double warmValue = getWarnValue();

		Map fields = cursor.getNext();
		sb
				.append("<th nowrap>CPU利用率(%)</th><th nowrap>CPU空闲率(%)</th><th nowrap>时间</th>");
		if (fields == null) {
			sb.append("<tr bgcolor=#ffffff><td colspan=3>查询无数据!</td></tr>");
		}
		while (fields != null) {
			String warmFlag = "0";

			String total = (String) fields.get("value1");

			if (total != null && !"".equals(total)) {
				if (Double.parseDouble(total) > warmValue) {
					warmFlag = "1";
				}
			}

			sb.append("<tr bgcolor=#ffffff>");
			if ("1".equals(warmFlag)) {
				sb.append("<td><font color=red>" + fields.get("value1")
						+ "</font></td>");
				sb.append("<td>" + fields.get("value2") + "</td>");
			} else {
				sb.append("<td>" + fields.get("value1") + "</td>");
				sb.append("<td>" + fields.get("value2") + "</td>");
			}

			sb.append("<td nowrap>" + formateTime(fields.get("gettime"))
					+ "</td>");
			sb.append("</tr>");
			fields = cursor.getNext();
		}
		cursor.Reset();
		try {
			// 暂时不需要图形显示
			String filename = getHostPerfMrtg(new Cursor[] { cursor },
					"CPU利用率曲线图", new String[] { "CPU" }, "时间", "CPU利用率(%)",
					"gettime", "value1");
			if (filename != null) {
				String graphURL = request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename;
				sb
						.append("<tr bgcolor=#ffffff><td colspan=3 align=center><img src='"
								+ graphURL
								+ "' border=0 usemap=\'#"
								+ filename
								+ "\'></td></tr>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("<tr class=green_foot><td colspan=3>&nbsp;</td></tr>");
		return sb.toString();
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	public String printMMTable(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		double warmValue = getWarnValue();

		Map fields = cursor.getNext();
		sb
				.append("<th nowrap>总内存(K)</th><th nowrap>空闲内存(K)</th><th nowrap>内存使用率(%)</th><th nowrap>时间</th>");
		String total = null;
		String free = null;
		if (fields == null) {
			sb.append("<tr bgcolor=#ffffff><td colspan=4>查询无数据!</td></tr>");
		}
		String rate = null;
		while (fields != null) {
			total = (String) fields.get("value1");
			free = (String) fields.get("value2");
			String rat = getMemValue(free, total);

			String warmFlag = "0";

			if (rat != null && !"".equals(rat)) {
				if (Double.parseDouble(rat) > warmValue) {
					warmFlag = "1";
				}
			}

			sb.append("<tr bgcolor=#ffffff>");
			sb.append("<td>" + total + "</td>");
			sb.append("<td>" + free + "</td>");

			rate = getMemValue(free, total);
			if ("1".equals(warmFlag)) {
				sb.append("<td><font color=red>" + rate + "</font></td>");
			} else {
				sb.append("<td>" + rate + "</td>");
			}

			sb.append("<td nowrap>" + formateTime(fields.get("gettime"))
					+ "</td>");
			sb.append("</tr>");
			if (rate.equals("")) {
				fields.put("mrate", "0");
			} else {
				fields.put("mrate", rate);
			}

			fields = cursor.getNext();
		}
		cursor.Reset();
		try {
			String filename = getHostPerfMrtg(new Cursor[] { cursor },
					"内存利用率曲线图", new String[] { "Memory" }, "时间", "内存利用率(%)",
					"gettime", "mrate");
			if (filename != null) {
				String graphURL = request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename;
				sb
						.append("<tr bgcolor=#ffffff><td colspan=4 align=center><img src='"
								+ graphURL
								+ "' border=0 usemap=\'#"
								+ filename
								+ "\'></td></tr>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("<tr class=green_foot><td colspan=4>&nbsp;</td></tr>");
		return sb.toString();
	}

	public String printFSTable(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		double warmValue = getWarnValue();

		String total = null;
		String free = null;
		sb
				.append("<th nowrap>文件系统</th><th nowrap>文件系统总大小(K)</th><th nowrap>使用文件总大小(K)</th><th nowrap>文件使用率(%)</th>");
		if (cursor.getRecordSize() == 0) {
			sb.append("<tr bgcolor=#ffffff><td colspan=4>查询无数据!</td></tr>");
		}
		cursor.Reset();
		Cursor cursors[] = cursor.group("gettime");
		Object keyInstance = null;
		Map fields = null;
		Map instanceMap = new HashMap();
		Cursor instanceCursor = null;
		Map instanceField = null;
		if (cursors != null) {
			for (int i = 0; i < cursors.length; i++) {
				fields = cursors[i].getNext();
				if (fields != null) {
					sb.append("<tr bgcolor=#ffffff>");
					sb
							.append("<td nowrap class=column colspan=4>采集时间："
									+ formateTime(fields.get("gettime"))
									+ "</td></tr>");
				}
				while (fields != null) {
					instanceField = new HashMap();
					instanceField.put("gettime", fields.get("gettime"));
					total = (String) fields.get("value1");
					free = (String) fields.get("value2");
					keyInstance = fields.get("instance");
					String rat = getFSValue(free, total);

					String warmFlag = "0";

					if (rat != null && !"".equals(rat)) {
						if (Double.parseDouble(rat) > warmValue) {
							warmFlag = "1";
						}
					}

					sb.append("<tr bgcolor=#ffffff>");
					sb.append("<td nowrap>" + keyInstance + "</td>");
					sb.append("<td>" + total + "</td>");
					sb.append("<td>" + free + "</td>");

					if ("1".equals(warmFlag)) {
						sb
								.append("<td><font color=red>" + rat
										+ "</font></td>");
					} else {
						sb.append("<td>" + rat + "</td>");
					}

					sb.append("</tr>");

					instanceField.put("frate", rat);
					instanceCursor = (Cursor) instanceMap.get(keyInstance);
					if (instanceCursor == null) {
						instanceCursor = new Cursor();
					}
					instanceCursor.add(instanceField);
					instanceMap.put(keyInstance, instanceCursor);
					fields = cursors[i].getNext();
				}
			}
		}

		// instanceMap.keySet().toArray(instanceArr);
		// instanceMap.values().toArray(cursorArrF);
		try {
			Cursor[] cursorArrF = new Cursor[instanceMap.size()];
			String[] instanceArr = new String[instanceMap.size()];
			Iterator iterator = instanceMap.entrySet().iterator();
			Map.Entry entry = null;
			int i = 0;
			while (iterator.hasNext() && i < instanceMap.size()) {
				entry = (Map.Entry) iterator.next();
				instanceArr[i] = (String) entry.getKey();
				cursorArrF[i] = (Cursor) entry.getValue();
				i++;
			}
			String filename = getHostPerfMrtg(cursorArrF, "文件系统曲线图",
					instanceArr, "时间", "文件系统利用率(%)", "gettime", "frate");
			if (filename != null) {
				String graphURL = request.getContextPath()
						+ "/servlet/DisplayChartLinkage?filename=" + filename;
				sb
						.append("<tr bgcolor=#ffffff><td colspan=4 align=center><img src='"
								+ graphURL
								+ "' border=0 usemap=\'#"
								+ filename
								+ "\'></td></tr>");
			}
			cursorArrF = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("<tr class=green_foot><td colspan=4>&nbsp;</td></tr>");
		instanceArr = null;
		return sb.toString();
	}

	public String printProcess(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		Map fields = cursor.getNext();
		sb.append("<th nowrap>进程名</th><th nowrap>进程状态</th><th nowrap>时间</th>");
		if (fields == null) {
			sb.append("<tr bgcolor=#ffffff><td colspan=3>查询无数据!</td></tr>");
		}
		while (fields != null) {
			sb.append("<tr bgcolor=#ffffff>");
			sb.append("<td nowrap>" + fields.get("instance") + "</td>");
			sb.append("<td>" + getProeccState((String) fields.get("value1"))
					+ "</td>");
			sb.append("<td nowrap>" + formateTime(fields.get("gettime"))
					+ "</td>");
			sb.append("</tr>");
			fields = cursor.getNext();
		}
		sb.append("<tr class=green_foot><td colspan=3>&nbsp;</td></tr>");
		return sb.toString();
	}
	public String printPort(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		Map fields = cursor.getNext();
		sb.append("<th nowrap>进程名</th><th nowrap>进程并发数</th><th nowrap>时间</th>");
		if (fields == null) {
			sb.append("<tr bgcolor=#ffffff><td colspan=3>查询无数据!</td></tr>");
		}
		while (fields != null) {
			sb.append("<tr bgcolor=#ffffff>");
			sb.append("<td nowrap>" + fields.get("instance") + "</td>");
			sb.append("<td>" + (String) fields.get("value1")
					+ "</td>");
			sb.append("<td nowrap>" + formateTime(fields.get("gettime"))
					+ "</td>");
			sb.append("</tr>");
			fields = cursor.getNext();
		}
		sb.append("<tr class=green_foot><td colspan=3>&nbsp;</td></tr>");
		return sb.toString();
	}
	/**
	 * 格式化时间
	 * 
	 * @param longTime
	 * @return
	 */
	private String formateTime(String longTime) {
		long time = Long.parseLong(longTime);
		return StringUtils.formatDate("yyyy-MM-dd HH:mm:ss", time);
	}

	private String formateTime(Object longTime) {
		return formateTime(String.valueOf(longTime));
	}

	private String getProeccState(String value) {
		if ("1.00".equals(value) || "1".equals(value) || "1.0".equals(value)) {
			return "<font color=greed>up</font>";
		} else {
			return "<font color=red>down</font>";
		}
	}

	private String getFSValue(String free, String total) {
		double d_free = (Double.parseDouble(free) * 100);
		double d_total = Double.parseDouble(total);
		// modify by lizhaojun why return "&nbsp;";
		if (d_total == 0)
			return "0";
		Double rate = new Double(d_free / d_total);

		return StringUtils.formatNumber(rate, 2);
	}

	private String getMemValue(String free, String total) {
		double d_free = (Double.parseDouble(free) * 100);
		double d_total = Double.parseDouble(total);
		if (d_total == 0)
			return "";
		Double rate = new Double(100 - d_free / d_total);

		return StringUtils.formatNumber(rate, 2);
	}

	private double getWarnValue() {
		String sql = "select warmvalue from tab_attrwarconf where gather_id = '"
				+ gather_id
				+ "' and hostip = '"
				+ hostip
				+ "' and attr_id = "
				+ attr_id;

		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();

		String tempValue = "";
		double warnValue = 100;
		if (fields != null) {
			tempValue = (String) fields.get("warmvalue");
		}

		if (tempValue != null && !"".equals(tempValue)) {
			warnValue = Double.parseDouble(tempValue);
		}

		return warnValue;
	}
}
