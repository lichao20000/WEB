package com.linkage.liposs.action.securitygw;

import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import com.linkage.liposs.buss.dao.securitygw.UserTopReportDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 王志猛(5194) tel：13701409234
 * @version 1.0
 * @since 2008-4-3
 * @category com.linkage.liposs.action.securitygw 版权：南京联创科技 网管科技部
 * 
 */
public class UserTopReportAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6174866868920883038L;
	private UserTopReportDAO utrDAO;
	private JFreeChart chart;
	private Map funInfo;// 功能信息
	private String device_id;// 设备的id
	private long strTime;// 如果是周期报表则为结束时间，如果是高级查询则为结束时间
	private long endTime;// 高级查询的结束时间
	private String ajax;// ajax结果集合
	private List<Map> virusType;// 用户类型集合
	private List<Map> attackType;// 攻击类型
	private int vt;// 用户选择的病毒类型
	private int at;// 用户选择的攻击类型
	@Override
	public String execute() throws Exception
	{
		return super.execute();
	}
	/**
	 * 获取用户病毒数的前十名排名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusTop() throws Exception
	{
		funInfo = utrDAO.getFunInfo(device_id);
		virusType = utrDAO.getVT();
		return "virus";
	}
	/**
	 * 获取病毒数前十名柱状图日统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusTopDayChart() throws Exception
	{
		chart = utrDAO.getVChart(UserTopReportDAO.DCHART, strTime, 0, vt, device_id);
		return "Chart";
	}
	public String virusTopDayTable() throws Exception
	{
		ajax = utrDAO.getVTable(UserTopReportDAO.DCHART, strTime, 0, vt, device_id);
		return "Table";
	}
	/**
	 * 获取病毒数前十名柱状图周统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusTopWeekChart() throws Exception
	{
		chart = utrDAO.getVChart(UserTopReportDAO.WCHART, strTime, 0, vt, device_id);
		return "Chart";
	}
	public String virusTopWeekTable() throws Exception
	{
		ajax = utrDAO.getVTable(UserTopReportDAO.WCHART, strTime, 0, vt, device_id);
		return "Table";
	}
	/**
	 * 获取病毒数前十名柱状图月统计表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusTopMonthChart() throws Exception
	{
		chart = utrDAO.getVChart(UserTopReportDAO.MCHART, strTime, 0, vt, device_id);
		return "Chart";
	}
	public String virusTopMonthTable() throws Exception
	{
		ajax = utrDAO.getVTable(UserTopReportDAO.MCHART, strTime, 0, vt, device_id);
		return "Table";
	}
	/**
	 * 病毒高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusAD() throws Exception
	{
		virusType = utrDAO.getVT();
		return "virusAD";
	}
	/**
	 * 获取病毒高级查询统计报表统计
	 * 
	 * @return
	 * @throws Exception
	 */
	public String virusTopADChart() throws Exception
	{
		chart = utrDAO.getVChart(UserTopReportDAO.AD, strTime, endTime, vt, device_id);
		return "Chart";
	}
	public String virusTopADTable() throws Exception
	{
		ajax = utrDAO.getVTable(UserTopReportDAO.AD, strTime, endTime, vt, device_id);
		return "Table";
	}
	/**
	 * 获得用户垃圾邮件前10名排名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String asmailTop() throws Exception
	{
		funInfo = utrDAO.getFunInfo(device_id);
		return "mail";
	}
	/**
	 * 获取垃圾邮件前十名日统计图
	 * 
	 * @return
	 */
	public String asmailTopDayChart() throws Exception
	{
		chart = utrDAO.getAMChart(UserTopReportDAO.DCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String asmailTopDayTable() throws Exception
	{
		ajax = utrDAO.getAMTable(UserTopReportDAO.DCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 获取垃圾邮件前十名周统计图
	 * 
	 * @return
	 */
	public String asmailTopWeekChart() throws Exception
	{
		chart = utrDAO.getAMChart(UserTopReportDAO.WCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String asmailTopWeekTable() throws Exception
	{
		ajax = utrDAO.getAMTable(UserTopReportDAO.WCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 获取垃圾邮件前十名月统计图
	 * 
	 * @return
	 * @throws Exception
	 */
	public String asmailTopMonthChart() throws Exception
	{
		chart = utrDAO.getAMChart(UserTopReportDAO.MCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String asmailTopMonthTable() throws Exception
	{
		ajax = utrDAO.getAMTable(UserTopReportDAO.MCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 获得用户垃圾邮件高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String asmailAD() throws Exception
	{
		return "asmailAD";
	}
	/**
	 * 垃圾邮件前十名月统计图高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String asmailTopADChart() throws Exception
	{
		chart = utrDAO.getAMChart(UserTopReportDAO.AD, strTime, endTime, device_id);
		return "Chart";
	}
	public String asmailTopADTable() throws Exception
	{
		ajax = utrDAO.getAMTable(UserTopReportDAO.AD, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 获得用户攻击事件的前十名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackTop() throws Exception
	{
		funInfo = utrDAO.getFunInfo(device_id);
		attackType = utrDAO.getAT();
		return "attack";
	}
	/**
	 * 获取攻击日排名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackTopDayChart() throws Exception
	{
		chart = utrDAO.getATChart(UserTopReportDAO.DCHART, strTime, endTime, at,
				device_id);
		return "Chart";
	}
	public String attackTopDayTable() throws Exception
	{
		ajax = utrDAO
				.getATTable(UserTopReportDAO.DCHART, strTime, endTime, at, device_id);
		return "Table";
	}
	/**
	 * 获取攻击周排名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackTopWeekChart() throws Exception
	{
		chart = utrDAO.getATChart(UserTopReportDAO.WCHART, strTime, endTime, at,
				device_id);
		return "Chart";
	}
	public String attackTopWeekTable() throws Exception
	{
		ajax = utrDAO
				.getATTable(UserTopReportDAO.WCHART, strTime, endTime, at, device_id);
		return "Table";
	}
	/**
	 * 获取攻击月排名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackTopMonthChart() throws Exception
	{
		chart = utrDAO.getATChart(UserTopReportDAO.MCHART, strTime, endTime, at,
				device_id);
		return "Chart";
	}
	public String attackTopMonthTable() throws Exception
	{
		ajax = utrDAO
				.getATTable(UserTopReportDAO.MCHART, strTime, endTime, at, device_id);
		return "Table";
	}
	/**
	 * 获得用户攻击事件的前十名高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackAD() throws Exception
	{
		attackType = utrDAO.getAT();
		return "attackAD";
	}
	/**
	 * 获取攻击排名高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String attackTopADChart() throws Exception
	{
		chart = utrDAO.getATChart(UserTopReportDAO.AD, strTime, endTime, at, device_id);
		return "Chart";
	}
	public String attackTopADTable() throws Exception
	{
		ajax = utrDAO.getATTable(UserTopReportDAO.AD, strTime, endTime, at, device_id);
		return "Table";
	}
	/**
	 * 过滤时间的前十名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterTop() throws Exception
	{
		funInfo = utrDAO.getFunInfo(device_id);
		return "filter";
	}
	/**
	 * 显示过滤前十名的日图
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterTopDayChart() throws Exception
	{
		chart = utrDAO.getFChart(UserTopReportDAO.DCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String filterTopDayTable() throws Exception
	{
		ajax = utrDAO.getFTable(UserTopReportDAO.DCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 显示过滤前十名的周图
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterTopWeekChart() throws Exception
	{
		chart = utrDAO.getFChart(UserTopReportDAO.WCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String filterTopWeekTable() throws Exception
	{
		ajax = utrDAO.getFTable(UserTopReportDAO.WCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 显示过滤前十名的月图
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterTopMonthChart() throws Exception
	{
		chart = utrDAO.getFChart(UserTopReportDAO.MCHART, strTime, endTime, device_id);
		return "Chart";
	}
	public String filterTopMonthTable() throws Exception
	{
		ajax = utrDAO.getFTable(UserTopReportDAO.MCHART, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 过滤时间的前十名高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterAD() throws Exception
	{
		return "filterAD";
	}
	/**
	 * 显示过滤前十名的高级查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String filterTopADChart() throws Exception
	{
		chart = utrDAO.getFChart(UserTopReportDAO.AD, strTime, endTime, device_id);
		return "Chart";
	}
	public String filterTopADTable() throws Exception
	{
		ajax = utrDAO.getFTable(UserTopReportDAO.AD, strTime, endTime, device_id);
		return "Table";
	}
	/**
	 * 获取信息的dao
	 * 
	 * @param utrDAO
	 */
	public void setUtrDAO(UserTopReportDAO utrDAO)
	{
		this.utrDAO = utrDAO;
	}
	public JFreeChart getChart()
	{
		return chart;
	}
	public Map getFunInfo()
	{
		return funInfo;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getDevice_id()
	{
		return device_id;
	}
	public void setStrTime(long strTime)
	{
		this.strTime = strTime;
	}
	public String getAjax()
	{
		return ajax;
	}
	public List<Map> getVirusType()
	{
		return virusType;
	}
	public void setVt(int vt)
	{
		this.vt = vt;
	}
	public List<Map> getAttackType()
	{
		return attackType;
	}
	public void setAt(int at)
	{
		this.at = at;
	}
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}
}
