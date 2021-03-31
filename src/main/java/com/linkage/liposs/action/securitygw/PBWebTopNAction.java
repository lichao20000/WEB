package com.linkage.liposs.action.securitygw;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Bruce(工号) tel：12345678 suixz(5253)
 * @version 1.0
 * @since Apr 2, 2008
 * @category com.linkage.liposs.action.securitygw
 * 版权：南京联创科技 网管科技部
 *
 */
public class PBWebTopNAction extends ActionSupport
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2424235586251L;
	private static Logger log= LoggerFactory.getLogger(PBWebTopNAction.class);
	private JFreeChart chart;// chart对象，提供给result
	private PBTopNDAO pbTopN;
	private ChartUtil cu;// jfreechart的封装类
	
	private String deviceid = "";
	private String date = "";
	private int topN = 10;
	private double angle = 0;
	private List<Map> data = null;

	private PBWebTopNAction() {
		angle = ChartYType.DOWN_45;
		initDate();
	}
	private void initDate()
	{
		initDate(new Date().getTime());
	}
	private void initDate(long millisecond)
	{
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
	 * 获取WebTopN日报表图表
	 * @return
	 * @throws Exception
	 */
	public String getWebTopNDay() throws Exception
	{
		data = pbTopN.getWebTopNDay(deviceid, date, topN);
		//add by suixz
		if(data!=null&&data.size()>topN){
			data = data.subList(0, topN);
		}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工WEB浏览Top" + topN + "日报表(" + date + ")", "员工IP", "访问次数", data, "times", "srcip", "yType", true);
		
		return "webTopNDay";
	}

	/**
	 * 获取WebTopN日报表图表(缩略图)
	 * @return
	 * @throws Exception
	 */
	public String getSmallWebTopNDay() throws Exception {
		data = pbTopN.getWebTopNDay(deviceid, date, topN);
		if(data!=null&&data.size()>topN){
			data = data.subList(0, topN);
		}
		cu.setTickLabelsVisible(false);
		chart = cu.createCategoryStackedBar3DP("员工WEB浏览", "员工IP", "访问次数", data, "times", "srcip", "yType", false);
		
		return "webSmall";
	}
	
	/**
	 * 获取WebTopN周报表图表
	 * @return
	 * @throws Exception
	 */
	public String getWebTopNWeek() throws Exception
	{
		data = pbTopN.getWebTopNWeek(deviceid, date, topN);
		if(data!=null&&data.size()>topN){
			data = data.subList(0, topN);
		}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工WEB浏览Top" + topN + "周报表(" + PBTopNDAO.getTimeStr(date, 7) + "~" + date + ")", "员工IP", "访问次数", data, "times", "srcip", "yType", true);
		
		return "webTopNWeek";
	}
	
	/**
	 * 获取WebTopN月报表图表
	 * @return
	 * @throws Exception
	 */
	public String getWebTopNMonth() throws Exception
	{
		data = pbTopN.getWebTopNMonth(deviceid, date, topN);
		if(data!=null&&data.size()>topN){
			data = data.subList(0, topN);
		}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工WEB浏览Top" + topN + "月报表(" + PBTopNDAO.getTimeStr(date, 30) + "~" + date + ")", "员工IP", "访问次数", data, "times", "srcip", "yType", true);
		
		return "webTopNMonth";
	}
	
	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public JFreeChart getChart() {
		return chart;
	}
	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}
	public PBTopNDAO getPbTopN()
	{
		return pbTopN;
	}
	public void setPbTopN(PBTopNDAO pbTopN)
	{
		this.pbTopN = pbTopN;
	}
	public ChartUtil getCu()
	{
		return cu;
	}
	public void setCu(ChartUtil cu)
	{
		this.cu = cu;
	}

	public List<Map> getData()
	{
		return data;
	}
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String getDeviceid()
	{
		return deviceid;
	}

	public void setDeviceid(String deviceid)
	{
		this.deviceid = deviceid;
	}

	public int getTopN()
	{
		return topN;
	}

	public void setTopN(int topN)
	{
		this.topN = topN;
	}

}
