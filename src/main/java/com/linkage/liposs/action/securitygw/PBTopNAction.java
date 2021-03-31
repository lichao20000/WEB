package com.linkage.liposs.action.securitygw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.action.cst;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.litms.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Bruce(工号) tel：12345678
 * @version 1.0
 * @since Apr 2, 2008
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 *
 */
public class PBTopNAction extends ActionSupport {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 123547445776251L;
	private static Logger log= LoggerFactory.getLogger(PBTopNAction.class);
	private JFreeChart chart;// chart对象，提供给result
	private PBTopNDAO pbTopN;
	private ChartUtil cu;// jfreechart的封装类

	private String deviceid = "";
	private String date = "";
	private int topN = 10;
	private double angle = 0;
	private List<Map> data = null;
	private List<Map> dataWeek;
	private List<Map> dataMonth;
	private String ajax = "";

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map> getDataWeek() {
		return dataWeek;
	}

	public void setDataWeek(List<Map> dataWeek) {
		this.dataWeek = dataWeek;
	}

	public List<Map> getDataMonth() {
		return dataMonth;
	}

	public void setDataMonth(List<Map> dataMonth) {
		this.dataMonth = dataMonth;
	}

	private PBTopNAction() {
		angle = ChartYType.DOWN_45;
		initDate();
	}

	private void initDate() {
		initDate(new Date().getTime());
	}

	private void initDate(long millisecond) {
		Calendar cale = Calendar.getInstance(Locale.CHINESE);
		cale.setTimeInMillis(millisecond);
		String year = "" + cale.get(Calendar.YEAR);
		int m = cale.get(Calendar.MONTH) + 1;
		String month = m < 10 ? "0" + m : "" + m;
		int d = cale.get(Calendar.DATE);
		String day = d < 10 ? "0" + d : "" + d;
		date = year + "_" + month + "_" + day;
	}

	/**
	 * 获取上网行为TopN日报表图表
	 *
	 * @return
	 * @throws Exception
	 */
	public String getTopNDay() throws Exception {
		log.debug("getTopNDay="+date);
		data = pbTopN.getTopNDay(deviceid, date, topN);
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("上网行为分析Top" + topN + "日报表("
				+ date + ")", "员工IP", "访问次数", data, "times", "srcip", "yType",
				true);

		return "topNDay";
	}

	/**
	 * 获取上网行为TopN日报表图表(缩略图)
	 *
	 * @return
	 * @throws Exception
	 */
	public String getSmallTopNDay() throws Exception {
		data = pbTopN.getTopNDay(deviceid, date, topN);
		data = filter(data);
		cu.setTickLabelsVisible(false);
		chart = cu.createCategoryStackedBar3DP("上网行为分析", "", "访问次数", data,
				"times", "srcip", "yType", false);

		return "topSmall";
	}

	/**
	 * 获取上网行为TopN周报表图表
	 *
	 * @return
	 * @throws Exception
	 */
	public String getTopNWeek() throws Exception {
		date = date.replace("-", "_");
		log.debug("getTopNWeek="+date);
		data = pbTopN.getTopNWeek(deviceid, date, topN);
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("上网行为分析Top" + topN + "周报表("
				+ PBTopNDAO.getTimeStr(date, 7) + "~" + date + ")", "员工IP",
				"访问次数", data, "times", "srcip", "yType", true);

		return "topNWeek";
	}

	/**
	 * 获取上网行为TopN月报表图表
	 *
	 * @return
	 * @throws Exception
	 */
	public String getTopNMonth() throws Exception {
		date = date.replace("-", "_");
		log.debug("getTopNMonth="+date);
		data = pbTopN.getTopNMonth(deviceid, date, topN);
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("上网行为分析Top" + topN + "月报表("
				+ PBTopNDAO.getTimeStr(date, 30) + "~" + date + ")", "员工IP",
				"访问次数", data, "times", "srcip", "yType", true);

		return "topNMonth";
	}

	/**
	 * 获取周表静态文字信息
	 *
	 * @return
	 */
	public String getNetBehaviorTopNWeekTable() {
		date = date.replace("-", "_");
		log.debug("getNetBehaviorTopNWeekTable="+date);
		dataWeek = pbTopN.getTopNDayStatic(deviceid, date, topN, 7);

		ajax = getTableString(dataWeek);
		log.debug(ajax);
		return cst.AJAX;
	}

	/**
	 * 获取月表静态文字信息
	 *
	 * @return
	 */
	public String getNetBehaviorTopNMonthTable() {
		date = date.replace("-", "_");
		log.debug("getNetBehaviorTopNMonthTable="+date);
		dataMonth = pbTopN.getTopNDayStatic(deviceid, date, topN, 30);

		ajax = getTableString(dataMonth);
		log.debug(ajax);
		return cst.AJAX;
	}

	public String getTableString(List<Map> resultData) {
		log.debug("size=" + resultData.size());
		StringBuilder sb = new StringBuilder();
		String temp = "";
		Map map = null;
		sb
				.append("<table width=\"670\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">");
		sb.append("<tr>");
		sb.append("<td class=\"tr_yellow\">排名</td>");
		sb.append("<td class=\"tr_yellow\">用户</td>");
		sb.append("<td class=\"tr_yellow\">次数</td>");
		sb.append("<td class=\"tr_yellow\">占总量百分比</td>");
		sb.append("</tr>");
		int i = 0;
		if (resultData != null && resultData.size() != 0) {

			Iterator<Map> it = resultData.iterator();
			long time = 0l;
			DateTimeUtil dateUtil = null;
			while (it.hasNext()) {
				map = it.next();
				i++;

				sb.append("<tr>");
				sb.append("<td class=\"tr_yellow\">Top-").append(i).append(
						"</td>");

				sb.append("<td class=\"tr_white\">").append(map.get("srcip"))
						.append("</td>");

				sb.append("<td class=\"tr_white\">").append(map.get("times"))
						.append("</td>");

				temp = (BigDecimal)map.get("precent")+"";

				sb.append("<td class=\"tr_white\">").append(temp.substring(0, temp.indexOf(".")+3))
						.append("%</td>");
				sb.append("</tr>");
			}
		} else {
			sb.append("<tr>").append("<td colspan=4 class=\"tr_white\">")
					.append("<div align=\"center\">暂时上网行为数据.</div>").append(
							"</td>").append("</tr>");
		}
		sb.append("</table>");
		// log.debug("表格数据：" + sb.toString());
		return sb.toString();
	}

	/**
	 * 将结果集中协议类型替换成协议名称
	 *
	 * @param list
	 * @return
	 */
	private List<Map> filter(List<Map> list) {
		List<Map> list_ = new ArrayList();
		if (list == null)
			return list_;
		List<Map> types = pbTopN.getProtTypes();
		// log.debug("--------list.size()=" + list.size());
		for (int i = 0; i < list.size(); i++) {
			Map m = list.get(i);
			Object o = m.get("yType");
			String yType = (o == null ? "" : ((BigDecimal) o).toString());

			for (int j = 0; j < types.size(); j++) {
				Map type = types.get(j);
				String protype = ((BigDecimal) type.get("protype")).toString();
				if (protype.equals(yType)) {
					// log.debug("----yType=" + yType + "----protype=" +
					// protype);
					m.put("yType", type.get("proname"));
					// log.debug("--------m1=====" + m);
					break;
				} else {
					if (j == types.size() - 1) {
						m.put("yType", "other");
						// log.debug("--------m2=====" + m);
					}
				}
			}
			list_.add(m);
		}
		log.debug("filter data 2= " + list_);
		return list_;
	}

	private List<Map> together(List<Map> d1, List<Map> d2) {
		if (d1 != null && d2 != null) {
			List<Map> d = new ArrayList();
			for (int i = 0; i < d1.size(); i++) {
				Map m1 = d1.get(i);

			}
			return d;
		} else {
			return d1 == null ? d2 : d1;
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	public PBTopNDAO getPbTopN() {
		return pbTopN;
	}

	public void setPbTopN(PBTopNDAO pbTopN) {
		this.pbTopN = pbTopN;
	}

	public ChartUtil getCu() {
		return cu;
	}

	public void setCu(ChartUtil cu) {
		this.cu = cu;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public int getTopN() {
		return topN;
	}

	public void setTopN(int topN) {
		this.topN = topN;
	}

}
