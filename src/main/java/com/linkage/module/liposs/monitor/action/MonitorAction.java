/*
 * ASIAINFO-LINKAGE TECHNOLOGY (NANJING) CO.,LTD. Copyright 1996-2012,  All rights reserved.
 * 文件名     :Monitor.java
 * 创建人     :ZhangCong(zhangcong@asiainfo-linkage.com)
 * 创建日期:2015年12月1日
 */
package com.linkage.module.liposs.monitor.action;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import action.splitpage.splitPageAction;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.liposs.monitor.bio.MonitorBIO;

/**
 * <p>
 * [一句话功能简述]
 * </p>
 * <p>
 * [功能详细描述]
 * </p>
 * @author ZhangCong(zhangcong@asiainfo-linkage.com)
 * @version [revision], 2015年12月1日
 * @sinse webapp
 */
@SuppressWarnings("rawtypes")
public class MonitorAction extends splitPageAction implements ServletRequestAware,ServletResponseAware
{
    private static final long serialVersionUID = 2978942171925530648L;
    private static final Logger LOG = Logger.getLogger(MonitorAction.class);
    		
    /**
     * 监控点tab_monitor_host表的内容， <br>
     * map的键包括hostid、hostname和hostdesc,初始化页面
     */
    private List<Map> monitorHostList = null;
    private List<Map<String,String>> currProgressList = null;
    private List<Map<String,String>> monitorMapList = null;
    
    /**
     * 采集类型tab_monitor_host_type表的内容，<br>
     * map的键包括typeid,hostid,typedesc,typevalve,typeperoid
     */
    private List<Map> monitorTypeList = null;
    /**页面选择的监控点*/
    private String monitor_host = null;
    /**页面选择的监控类型*/
    private String monitor_type = null;
    /**时间控件类型*/
    private int rptType = 0;
    /**时间内容*/
    private String rptTime = null;
    /**开始时间*/
    private String phaseStart = null;
    /**结束时间*/
    private String phaseEnd = null;
    
    private int currHour = 0;
    private int preHour = 0;
    private String startTime = null;
    private String endTime = null;
    private List<Map> progressHistoryList = null;
    private String progress_type = null;
    private String logStr=null;
    
    private String ajax = null;
    protected HttpServletRequest request;
	protected HttpServletResponse response;
	private MonitorBIO mbio = null;
	
    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#execute() 初始化页面
     */
    public String execute() throws Exception
    {
        //初始化主机队列
        //monitorHostList = mbio.getAllMonitorHostList();
        return SUCCESS;
    }
    
    /**
     * [获取所有监控点]
     * [功能详细描述]
     */
    public String getAllMonitorIDList()
    {
    	LOG.warn("ACT-获取所有监控点");
        ajax = mbio.getAllMonitorHostListJson();
        return "ajax";
    }
    
    /**
     * [由监控点获取监控类型]
     * [功能详细描述]
     */
    public String getMonitorTypeListByHostID()
    {
    	LOG.warn("ACT-获取监控类型");
        ajax = mbio.getMonitorTypeListByHostIDJson(monitor_host);
        return "ajax";
    }
    
    public String mrtg()
    {
    	LOG.warn("ACT-画图"); 
    	if(null != LipossGlobals.getLipossProperty("alarmMonitor.allType") &&
    			LipossGlobals.getLipossProperty("alarmMonitor.allType").equals(monitor_type)){
    		ajax = mbio.paintMRTGAll(rptType, rptTime, monitor_host, monitor_type, request, phaseStart,  phaseEnd,400,200,"width=\"49%\"","style=\"float:left;\"");
    	}else{
    		ajax = mbio.paintMRTG(rptType, rptTime, monitor_host, monitor_type, request, phaseStart,  phaseEnd,800,400,"width=\"97%\"","");
    	}
        return "ajax";
    }
    
    /**
     * 初始化进程状态页面
     */
    public String progressMonitor()
    {
    	this.monitorHostList = mbio.getAllMonitorHostList();
    	return "currProgress";
    }
    
    /**
     * 查询该主机所有进程的当前状态
     */
    public String progressMonitorList()
    {
    	this.currProgressList = mbio.getHostProgressList(monitor_host);
    	return "currProgressList";
    }
    
    public String queryLog()
    {
    	monitorMapList=mbio.getHostLog(monitor_host,progress_type);
    	return "log";
    }
    
    /**
     * 主机监控平台
     */
    public String monitorList()
    {
    	//this.monitorHostList = mbio.getAllMonitorHostList();
    	long currTime = System.currentTimeMillis();	
    	this.currHour = mbio.getHourValue(currTime);
    	if(this.currHour == 0){
    		this.currHour = 1;	
    	}else{
    		this.preHour = this.currHour -1;
    	}
    	this.monitorMapList = mbio.getHostMonitorList(currTime);	
    	return "monitotList";
    }
    
    public String monitorProgressHistory()
    {
    	DateTimeUtil dt = new DateTimeUtil();
    	startTime = dt.getDate() + " 00:00:00";
    	endTime = dt.getLongDate();
    	this.monitorHostList = mbio.getAllMonitorHostList();
    	return "monitorHost";
    }
    
    public String moitorProgressType()
    {
    	ajax = mbio.getHostProgressType(monitor_host);
    	return "ajax";
    }
    
    /**
     * 查询进程的历史信息详情
     */
    public String monitorProgressHistoryList()
    {
    	String starttime = String.valueOf(new DateTimeUtil(startTime).getLongTime());
		String endtime = String.valueOf(new DateTimeUtil(endTime).getLongTime());
		System.out.println("123333333");
		LOG.warn(monitor_host + "&&" + progress_type + "&&" + starttime + "&&" + endtime + "&&" + curPage_splitPage + "&&" + num_splitPage);
    	this.progressHistoryList = mbio.getProHistoryList(monitor_host,progress_type,starttime,endtime,curPage_splitPage,num_splitPage);
    	int total = mbio.getProHistoryCount(monitor_host,progress_type,starttime,endtime);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
    	return "proHistotyList";
    }
    
    
    public String query(){
        return "ajax";
    }

    public List<Map<String, String>> getMonitorMapList() {
		return monitorMapList;
	}

	public void setMonitorMapList(List<Map<String, String>> monitorMapList) {
		this.monitorMapList = monitorMapList;
	}

	public List<Map> getMonitorHostList(){
        return monitorHostList;
    }

    public void setMonitorHostList(List<Map> monitorHostList){
        this.monitorHostList = monitorHostList;
    }

    public String getAjax(){
        return ajax;
    }

    public void setAjax(String ajax){
        this.ajax = ajax;
    }

    public MonitorBIO getMbio(){
        return mbio;
    }

    public void setMbio(MonitorBIO mbio){
        this.mbio = mbio;
    }

    public List<Map> getMonitorTypeList(){
        return monitorTypeList;
    }

    public void setMonitorTypeList(List<Map> monitorTypeList){
        this.monitorTypeList = monitorTypeList;
    }

    public String getMonitor_host(){
        return monitor_host;
    }

    public void setMonitor_host(String monitor_host){
        this.monitor_host = monitor_host;
    }

    public String getMonitor_type(){
        return monitor_type;
    }

    public void setMonitor_type(String monitor_type){
        this.monitor_type = monitor_type;
    }

    public int getRptType(){
        return rptType;
    }

    public void setRptType(int rptType){
        this.rptType = rptType;
    }

    public String getRptTime(){
        return rptTime;
    }

    public void setRptTime(String rptTime){
        this.rptTime = rptTime;
    }

    public String getPhaseStart(){
        return phaseStart;
    }

    public void setPhaseStart(String phaseStart){
        this.phaseStart = phaseStart;
    }

    public String getPhaseEnd(){
        return phaseEnd;
    }

    public void setPhaseEnd(String phaseEnd){
        this.phaseEnd = phaseEnd;
    }

	public List<Map<String, String>> getCurrProgressList() {
		return currProgressList;
	}

	public void setCurrProgressList(List<Map<String, String>> currProgressList) {
		this.currProgressList = currProgressList;
	}

	public int getCurrHour() {
		return currHour;
	}

	public void setCurrHour(int currHour) {
		this.currHour = currHour;
	}

	public int getPreHour() {
		return preHour;
	}

	public void setPreHour(int preHour) {
		this.preHour = preHour;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<Map> getProgressHistoryList() {
		return progressHistoryList;
	}

	public void setProgressHistoryList(List<Map> progressHistoryList) {
		this.progressHistoryList = progressHistoryList;
	}

	public String getProgress_type() {
		return progress_type;
	}

	public void setProgress_type(String progress_type) {
		this.progress_type = progress_type;
	}
	
	public void setServletRequest(HttpServletRequest request){
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response){
		this.response = response;
	}

	public String getLogStr() {
		return logStr;
	}

	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}
}
