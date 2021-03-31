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
 * @author xj
 * @version 1.0
 * @since May 6, 2008
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class AdvNetBehaviorAction extends ActionSupport { 
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 123547445776251L;
	private static Logger log = LoggerFactory.getLogger(AdvNetBehaviorAction.class);
	private JFreeChart chart;// chart对象，提供给result
	private PBTopNDAO pbTopN;
	private ChartUtil cu;// jfreechart的封装类
	private String imgUrl;// 生成的表图片路径
	private String deviceid = "";
	private String date = "";
	private String stime = "", etime = "";
	private String ajax = "";
	private int topN = 10;
	private double angle = 0;
	private List<Map> data = null;
	private List<Map> tableDataList;
	private AdvNetBehaviorAction() {
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
	 * 获取上网行为TopN高级展现图表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAdvNetBehaviorTopN() throws Exception {
		log.debug("getAdvNetBehaviorTopN");
		String charTitle = "";
		log.debug("stime="+stime+",etime="+etime);
		if (stime.equals(etime)) {
			charTitle = "日报表(" + etime + ")";
		} else {
			charTitle = "区间(" + stime + "-" + etime + ")";
		}
		log.debug("getAdvNetBehaviorTopN charTitle="+charTitle);
		data = pbTopN.getAdvNetBehaviorData(deviceid, stime,etime, topN);
		data = filter(data);
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("上网行为分析Top" + topN + charTitle, "员工IP", "访问次数", data, "times", "srcip", "yType",
				true);

		return "advNetBehaviorChar";
	}

	/**
	 * 跳转至上网行为TopN高级展现图表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAdvNetBehaviorTopN() throws Exception {
		return SUCCESS;
	}
 
	
	public String getAdvNetBehaviorTopNTable(){ 
		 
		tableDataList = pbTopN.getAdvTopNStatic(deviceid, stime,etime, topN);
 
		ajax = getTableString(tableDataList);
		log.debug(ajax);
		return cst.AJAX;
	}
	
	public String getTableString(List<Map> resultData){
		log.debug("size=" + resultData.size());
		StringBuilder sb = new StringBuilder();
		Map map = null;
		sb.append("<table width=\"670\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">");
		sb.append("<tr>");
		sb.append("<td class=\"tr_yellow\">排名</td>");
		sb.append("<td class=\"tr_yellow\">用户</td>");
		sb.append("<td class=\"tr_yellow\">次数</td>");
		sb.append("<td class=\"tr_yellow\">占总量百分比</td>");
		sb.append("</tr>");
		int i = 0;
		if(resultData != null && resultData.size() != 0){
			
			Iterator<Map> it = resultData.iterator();
			long time = 0l;
			DateTimeUtil dateUtil = null;
			while(it.hasNext()){
				map = it.next();
				i++;

				sb.append("<tr>");
				sb.append("<td class=\"tr_yellow\">Top-").append(i).append("</td>");

				sb.append("<td class=\"tr_white\">").append(map.get("srcip")).append("</td>");
				
				sb.append("<td class=\"tr_white\">").append(map.get("times")).append("</td>");
				sb.append("<td class=\"tr_white\">").append(map.get("precent")).append("%</td>");
				sb.append("</tr>");
			}
		}else{
			sb.append("<tr>").append("<td colspan=4 class=\"tr_white\">").append("<div align=\"center\">暂时上网行为数据.</div>").append("</td>").append("</tr>");
		}
		sb.append("</table>");
		//log.debug("表格数据：" + sb.toString());
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
						m.put("yType", "其它");
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

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public List<Map> getTableDataList() {
		return tableDataList;
	}

	public void setTableDataList(List<Map> tableDataList) {
		this.tableDataList = tableDataList;
	}

}
