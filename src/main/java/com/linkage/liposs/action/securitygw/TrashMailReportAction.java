package com.linkage.liposs.action.securitygw;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateTickUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.action.cst;
import com.linkage.liposs.buss.dao.securitygw.TrashMailReportDAO;
import com.linkage.liposs.buss.dao.securitygw.UserTopReportDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.litms.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 何茂才(工号) tel：12345678
 * @version 1.0
 * @since 2008-4-1
 * @category 垃圾邮件
 * 版权：南京联创科技 网管科技部
 *
 */
public class TrashMailReportAction extends ActionSupport
{
	private static Logger log = LoggerFactory.getLogger(TrashMailReportAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 606595564886869506L;
	
	// 设备编码
	private String deviceid = null;
	// 备注
	private String remark = null;
	private String nowDateString = null;
	/**
	 * 病毒事件统计 数据来源对象
	 */
	private TrashMailReportDAO tmrDAO;
	
	private JFreeChart chart = null;
	private String startTime = null;
	private String endTime = null;
	private String ajax = "";
	// 设备信息
	private Map funInfo;
	
	private UserTopReportDAO utrDAO;
	/**
	 * jfreechart的封装类
	 */
	private ChartUtil chartUtil;
	private List<Map> result;
	public String execute()
	{
		// 调用DAO对象读取设备信息
		log.debug("垃圾事件报表主页面 deviceid=" + deviceid);
		DateTimeUtil date = new DateTimeUtil();
		nowDateString = date.getDate();
		try{
			funInfo = utrDAO.getFunInfo(deviceid);
		}catch(Exception e){
			log.error(e.getMessage());
			if(funInfo == null)
				funInfo = new HashMap();
			funInfo.put("customname", remark);
		}
		return SUCCESS;
	}
	/**
	 * 将数据源输出成表格数据
	 * @param result
	 * @return
	 */
	public String getTableString(List<Map> result){
		StringBuilder sb = new StringBuilder();
		sb.append("<table width=\"670\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">");
		sb.append("<tr>");
		sb.append("<td class=\"tr_yellow\" width=\"33%\">起始时间</td>");
		sb.append("<td class=\"tr_yellow\" width=\"33%\">结束时间</td>");
		sb.append("<td class=\"tr_yellow\" width=\"33%\">次数</td>");
		sb.append("</tr>");
		if(result != null && result.size() != 0){
			Iterator<Map> it = result.iterator();
			long time = 0l;
			DateTimeUtil dateUtil = null;
			while(it.hasNext()){
				Map map = it.next();
				time = Long.parseLong(String.valueOf(map.get("stime")));
				dateUtil = new DateTimeUtil(time * 1000);
				sb.append("<tr>");
				sb.append("<td class=\"tr_white\">").append(dateUtil.getLongDate()).append("</td>");
				
				time = Long.parseLong(String.valueOf(map.get("etime")));
				dateUtil = new DateTimeUtil(time * 1000);
				sb.append("<td class=\"tr_white\">").append(dateUtil.getLongDate()).append("</td>");
				
				sb.append("<td class=\"tr_white\">").append(map.get("mailtimes")).append("</td>");
				sb.append("</tr>");
			}
		}else{
			sb.append("<tr>").append("<td colspan=3 class=\"tr_white\">").append("<div align=\"center\">暂时无垃圾邮件数据.</div>").append("</td>").append("</tr>");
		}
		sb.append("</table>");
		//log.debug("表格数据:" + sb.toString());
		return sb.toString();
	}
	/**
	 * 获取病毒事件统计预览图
	 * @return
	 */
	public String getPreView(){
		// 获取时间,向前推24小时，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil();
		//获取当前日期
		dateTime = new DateTimeUtil(dateTime.getDate());
		long starttime = dateTime.getLongTime();
		long endtime = starttime + 24 * 3600;
		result = tmrDAO.getMailDayData(deviceid, starttime, endtime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 4, "H");
		//chartUtil.setFontSize(9);
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", ChartYType.Hour, false);
		return "small";
	}
	/**
	 * 垃圾邮件日报表表格数据
	 * @return
	 */
	public String getTrashMailDayTable(){
		// 获取时间,向前推24小时，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		long endtime = dateTime.getLongTime();
		long starttime = endtime - 24 * 3600;
		result = tmrDAO.getMailDayData(deviceid, starttime, endtime);
		ajax = getTableString(result);
		return cst.AJAX;
	}
	/**
	 * 垃圾邮件周报表表格数据
	 * @return
	 */
	public String getTrashMailWeekTable(){
		// 获取时间,向前推7天，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		// 先向后推7天
		dateTime.getNextDate(-7);
		// 起始时间
		long starttime = dateTime.getLongTime();
		result = tmrDAO.getMailWeekData(deviceid, starttime, endtime);
		ajax = getTableString(result);
		
		return cst.AJAX;
	}
	/**
	 * 垃圾邮件月报表表格数据
	 * @return
	 */
	public String getTrashMailMonthTable(){
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		dateTime.getNextMonth(-1);
		long starttime = dateTime.getLongTime();
		// 获取时间,向前推30天，调用virusReportBIO生成jfreechart对象
		result = tmrDAO.getMailMonthData(deviceid, starttime, endtime);
		ajax = getTableString(result);
		return cst.AJAX;
	}
	/**
	 * 垃圾邮件天报表
	 * @return
	 */
	public String getTrashMailDay()
	{
		// 获取时间,向前推24小时，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		long endtime = dateTime.getLongTime();
		long starttime = endtime - 24 * 3600;
		result = tmrDAO.getMailDayData(deviceid, starttime, endtime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:mm");
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", ChartYType.Hour, true);
		return "day";
	}
	
	/**
	 * 病毒周报表
	 * @return
	 */
	public String getTrashMailWeek()
	{
		// 获取时间,向前推7天，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		// 先向后推7天
		dateTime.getNextDate(-7);
		// 起始时间
		long starttime = dateTime.getLongTime();
		result = tmrDAO.getMailWeekData(deviceid, starttime, endtime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "E");
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", 3, true);
		/*
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", reulst, "virustimes", "stime", "virusname", 2, true);*/
		return "week";
	}
	/**
	 * 
	 * @return
	 */
	public String getTrashMailMonth()
	{
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		dateTime.getNextMonth(-1);
		long starttime = dateTime.getLongTime();
		// 获取时间,向前推30天，调用virusReportBIO生成jfreechart对象
		result = tmrDAO.getMailMonthData(deviceid, starttime, endtime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", 3, true);
		return "month";
	}
	public String getDeviceid()
	{
		return deviceid;
	}
	public void setDeviceid(String deviceid)
	{
		this.deviceid = deviceid;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public JFreeChart getChart()
	{
		return chart;
	}
	public void setChartUtil(ChartUtil chartUtil)
	{
		this.chartUtil = chartUtil;
	}
	public String getNowDateString()
	{
		return nowDateString;
	}
	public void setNowDateString(String nowDateString)
	{
		this.nowDateString = nowDateString;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setTmrDAO(TrashMailReportDAO tmrDAO)
	{
		this.tmrDAO = tmrDAO;
	}
	public List<Map> getResult()
	{
		return result;
	}
	public String getAjax()
	{
		return ajax;
	}
	public Map getFunInfo()
	{
		return funInfo;
	}
	public void setUtrDAO(UserTopReportDAO utrDAO)
	{
		this.utrDAO = utrDAO;
	}
	public String advQuery(){
	    execute();
	    return "adv";
	}
	/**
	 * 告警查询MRTG
	 * @return
	 */
	public String getMailAdvQuery(){
	    //算出两个时间点之间的秒数
	    long second = adv_etime - adv_stime;
	    //如果时间是24小时之内,则直接查询小时表
	    if(second == 0){
		//如果两个时间选择的是同一个时间,如：2008-5-6 2008-5-6，则结束时间为起始时间+86400s
		adv_etime = adv_stime + 86400;
		result = tmrDAO.getMailDayData(deviceid, adv_stime, adv_etime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(adv_stime*1000),new Date(adv_etime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:mm");
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", ChartYType.Hour, true);
		return "day";
	    }else{
		result = tmrDAO.getMailMonthData(deviceid, adv_stime, adv_etime);
		startTime = tmrDAO.getStartTime();
		endTime = tmrDAO.getEndTime();
		chartUtil.setXDate(new Date(adv_stime*1000),new Date(adv_etime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
		chart = chartUtil.createXYCategoryBarP("垃圾邮件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", "垃圾邮件", result, "stime", "mailtimes", 3, true);
		return "month";
	    }
	}
	/**
	 * 告警查询（表格数据）
	 * @return
	 */
	public String getMailAdvTable(){
	  //算出两个时间点之间的秒数
	    long second = adv_etime - adv_stime;
	    //如果时间是24小时之内,则直接查询小时表
	    if(second == 0){
		//如果两个时间选择的是同一个时间,如：2008-5-6 2008-5-6，则结束时间为起始时间+86400s
		adv_etime = adv_stime + 86400;
		result = tmrDAO.getMailDayData(deviceid, adv_stime, adv_etime);
	    }else{
		result = tmrDAO.getMailMonthData(deviceid, adv_stime, adv_etime);
	    }
	    
	    ajax = getTableString(result);
	    
	    return cst.AJAX;
	}
	/**
	 * 高级查询页面需要的时间变量
	 */
	private long adv_stime = 0l;
	private long adv_etime = 0l;
	public long getAdv_stime() {
	    return adv_stime;
	}
	public void setAdv_stime(long adv_stime) {
	    this.adv_stime = adv_stime;
	}
	public long getAdv_etime() {
	    return adv_etime;
	}
	public void setAdv_etime(long adv_etime) {
	    this.adv_etime = adv_etime;
	}
}
