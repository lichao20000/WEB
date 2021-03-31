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
import com.linkage.liposs.buss.dao.securitygw.UserTopReportDAO;
import com.linkage.liposs.buss.dao.securitygw.VirusReportDAO;
import com.linkage.liposs.buss.util.ChartUtil;
import com.linkage.liposs.buss.util.ChartYType;
import com.linkage.litms.common.util.DateTimeUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @author 何茂才
 * @category 病毒事件统计Action
 */
public class VirusReportAction extends ActionSupport
{
	private static final long serialVersionUID = -2706952980663293890L;
	private static Logger log = LoggerFactory.getLogger(VirusReportAction.class);
	// 设备编码
	private String deviceid = null;
	// 备注
	private String remark = null;
	private String nowDateString = null;
	/**
	 * 病毒事件统计 数据来源对象
	 */
	private VirusReportDAO vrrDAO;
	private JFreeChart chart = null;
	private String startTime = null;
	private String endTime = null;
	/**
	 * 高级查询页面需要的时间变量
	 */
	private long adv_stime = 0l;
	private long adv_etime = 0l;
	
	// 设备信息
	private Map funInfo;
	
	private String ajax = "";
	
	private UserTopReportDAO utrDAO;
	/**
	 * jfreechart的封装类
	 */
	private ChartUtil chartUtil;
	private List<Map> result;
	public String execute()
	{
		// 调用DAO对象读取设备信息
		log.debug("病毒事件报表主页面 deviceid=" + deviceid);
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
	public String getTableString(List<Map> resultData){
		log.debug("size=" + resultData.size());
		StringBuilder sb = new StringBuilder();
		sb.append("<table width=\"670\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">");
		sb.append("<tr>");
		sb.append("<td class=\"tr_yellow\">开始时间</td>");
		sb.append("<td class=\"tr_yellow\">结束时间</td>");
		sb.append("<td class=\"tr_yellow\">病毒名称</td>");
		sb.append("<td class=\"tr_yellow\">次数</td>");
		sb.append("</tr>");
		if(resultData != null && resultData.size() != 0){
			Iterator<Map> it = resultData.iterator();
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
				
				sb.append("<td class=\"tr_white\">").append(map.get("virusname")).append("</td>");
				sb.append("<td class=\"tr_white\">").append(map.get("virustimes")).append("</td>");
				sb.append("</tr>");
			}
		}else{
			sb.append("<tr>").append("<td colspan=4 class=\"tr_white\">").append("<div align=\"center\">暂时无病毒数据.</div>").append("</td>").append("</tr>");
		}
		sb.append("</table>");
		//log.debug("表格数据：" + sb.toString());
		return sb.toString();
	}
	/**
	 * 病毒事件日报表表格
	 * @return
	 */
	public String getVirusDayTable(){
		// 获取时间,向前推24小时，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		long endtime = dateTime.getLongTime();
		long starttime = endtime - 24 * 3600;
		result = vrrDAO.getVirusDayData(deviceid, starttime, endtime);
		//log.debug("日报表表格数据：" + result);
		ajax = getTableString(result);
		
		return cst.AJAX;
	}
	/**
	 * 病毒事件周报表表格
	 * @return
	 */
	public String getVirusWeekTable(){
		// 获取时间,向前推7天，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		// 先向后推7天
		dateTime.getNextDate(-7);
		// 起始时间
		long starttime = dateTime.getLongTime();
		result = vrrDAO.getVirusWeekData(deviceid, starttime, endtime);
		ajax = getTableString(result);
		return cst.AJAX;
	}
	/**
	 * 病毒事件月报表表格
	 * @return
	 */
	public String getVirusMonthTable(){
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		dateTime.getNextMonth(-1);
		long starttime = dateTime.getLongTime();
		// 获取时间,向前推30天，调用virusReportBIO生成jfreechart对象
		result = vrrDAO.getVirusMonthData(deviceid, starttime, endtime);
		ajax = getTableString(result);
		return cst.AJAX;
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
		result = vrrDAO.getVirusDayData(deviceid, starttime, endtime);
		//startTime = vrrDAO.getStartTime();
		//endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 4, "H");
		//chartUtil.setFontSize(9);
		chart = chartUtil.createXYStackedBarP("病毒事件统计", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Hour, false);
		return "small";
	}
	/**
	 * 病毒事件天报表
	 * @return
	 */
	public String getVirusDay()
	{
		// 获取时间,向前推24小时，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		long endtime = dateTime.getLongTime();
		long starttime = endtime - 24 * 3600;
		result = vrrDAO.getVirusDayData(deviceid, starttime, endtime);
		startTime = vrrDAO.getStartTime();
		endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:mm");
		
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Hour, true);
		return "day";
	}
	/**
	 * 病毒事件周报表
	 * @return
	 */
	public String getVirusWeek()
	{
		// 获取时间,向前推7天，调用virusReportBIO生成jfreechart对象
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		// 先向后推7天
		dateTime.getNextDate(-7);
		// 起始时间
		long starttime = dateTime.getLongTime();
		result = vrrDAO.getVirusWeekData(deviceid, starttime, endtime);
		startTime = vrrDAO.getStartTime();
		endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 1, "E");
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Day, true);
		return "week";
	}
	/**
	 * 病毒事件月报表
	 * @return
	 */
	public String getVirusMonth()
	{
		DateTimeUtil dateTime = new DateTimeUtil(nowDateString);
		// 结束时间
		long endtime = dateTime.getLongTime();
		dateTime.getNextMonth(-1);
		long starttime = dateTime.getLongTime();
		// 获取时间,向前推30天，调用virusReportBIO生成jfreechart对象
		result = vrrDAO.getVirusMonthData(deviceid, starttime, endtime);
		startTime = vrrDAO.getStartTime();
		endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(starttime*1000),new Date(endtime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Day, true);
		return "month";
	}
	public String advQuery(){
	    execute();
	    return "adv";
	}
	/**
	 * 告警查询MRTG
	 * @return
	 */
	public String getVirusAdvQuery(){
	    //算出两个时间点之间的秒数
	    long second = adv_etime - adv_stime;
	    //如果时间是24小时之内,则直接查询小时表
	    if(second == 0){
		//如果两个时间选择的是同一个时间,如：2008-5-6 2008-5-6，则结束时间为起始时间+86400s
		adv_etime = adv_stime + 86400;
		result = vrrDAO.getVirusDayData(deviceid, adv_stime, adv_etime);
		startTime = vrrDAO.getStartTime();
		endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(adv_stime*1000),new Date(adv_etime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.HOUR, 2, "H:mm");
		
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Hour, true);
		return "day";
	    }else{
		result = vrrDAO.getVirusMonthData(deviceid, adv_stime, adv_etime);
		startTime = vrrDAO.getStartTime();
		endTime = vrrDAO.getEndTime();
		chartUtil.setXDate(new Date(adv_stime*1000),new Date(adv_etime*1000));
		chartUtil.setXAxisUnit(DateTickUnit.DAY, 2, "dd");
		chart = chartUtil.createXYStackedBarP("病毒事件统计(" + startTime + " 到 " + endTime
				+ ")", "时间", "次数", result, "virustimes", "stime", "virusname", ChartYType.Day, true);
		return "month";
	    }
	}
	/**
	 * 告警查询（表格数据）
	 * @return
	 */
	public String getVirusAdvTable(){
	  //算出两个时间点之间的秒数
	    long second = adv_etime - adv_stime;
	    //如果时间是24小时之内,则直接查询小时表
	    if(second == 0){
		//如果两个时间选择的是同一个时间,如：2008-5-6 2008-5-6，则结束时间为起始时间+86400s
		adv_etime = adv_stime + 86400;
		result = vrrDAO.getVirusDayData(deviceid, adv_stime, adv_etime);
	    }else{
		result = vrrDAO.getVirusMonthData(deviceid, adv_stime, adv_etime);
	    }
	    
	    ajax = getTableString(result);
	    
	    return cst.AJAX;
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
	public void setVrrDAO(VirusReportDAO vrrDAO)
	{
		this.vrrDAO = vrrDAO;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setUtrDAO(UserTopReportDAO utrDAO)
	{
		this.utrDAO = utrDAO;
	}
	public Map getFunInfo()
	{
		return funInfo;
	}
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
