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
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class PBMailTopNAction extends ActionSupport
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6235545776251L;
	private static Logger log = LoggerFactory.getLogger(PBMailTopNAction.class);
	private JFreeChart chart;// chart对象，提供给result
	private PBTopNDAO pbTopN;
	private ChartUtil cu;// jfreechart的封装类
	private String deviceid = "";
	private String date = "";
	private int topN = 10;
	private double angle = 0;
	private List<Map> data = null;
	private PBMailTopNAction()
	{
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
	 * 获取MailTopN日报表图表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getMailTopNDay() throws Exception
	{
		data = pbTopN.getMailTopNDay(deviceid, date, topN);
		//add by suixz(5253) 2008-5-7
		if (data != null && data.size() > topN)
			{
				data = data.subList(0, topN);
			}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工邮件收发Top" + topN + "日报表(" + date + ")",
				"员工IP", "邮件数", data, "times", "srcip", "yType", true);
		return "mailTopNDay";
	}
	/**
	 * 获取MailTopN日报表图表(缩略图)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSmallMailTopNDay() throws Exception
	{
		data = pbTopN.getMailTopNDay(deviceid, date, topN);
		//add by suixz(5253) 2008-5-7
		if (data != null && data.size() > topN)
			{
				data = data.subList(0, topN);
			}
		cu.setTickLabelsVisible(false);
		chart = cu.createCategoryStackedBar3DP("员工邮件收发", "", "邮件数", data, "times",
				"srcip", "yType", false);
		return "mailSmall";
	}
	/**
	 * 获取MailTopN周报表图表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getMailTopNWeek() throws Exception
	{
		data = pbTopN.getMailTopNWeek(deviceid, date, topN);
		//add by suixz(5253) 2008-5-7
		if (data != null && data.size() > topN)
			{
				data = data.subList(0, topN);
			}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工邮件收发Top" + topN + "周报表("
				+ PBTopNDAO.getTimeStr(date, 7) + "~" + date + ")", "员工IP", "邮件数", data,
				"times", "srcip", "yType", true);
		return "mailTopNWeek";
	}
	/**
	 * 获取MailTopN月报表图表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getMailTopNMonth() throws Exception
	{
		data = pbTopN.getMailTopNMonth(deviceid, date, topN);
		//add by suixz(5253) 2008-5-7
		if (data != null && data.size() > topN)
			{
				data = data.subList(0, topN);
			}
		cu.setCLablePosition(angle);
		chart = cu.createCategoryStackedBar3DP("员工邮件收发Top" + topN + "月报表("
				+ PBTopNDAO.getTimeStr(date, 30) + "~" + date + ")", "员工IP", "邮件数", data,
				"times", "srcip", "yType", true);
		return "mailTopNMonth";
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public JFreeChart getChart()
	{
		return chart;
	}
	public void setChart(JFreeChart chart)
	{
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
