package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.itms.service.bio.MonitorFlowInfoQueryBIO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-16
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorFlowInfoQueryACT extends splitPageAction implements SessionAware,
		ServletRequestAware
{
	private static Logger logger = LoggerFactory.getLogger(MonitorFlowInfoQueryACT.class);
	// session
	private Map session = null;
	private HttpServletRequest request;
	
	private MonitorFlowInfoQueryBIO bio;
	// 开始时间 
	private String startOpenDate = "";
	// 开始时间 
	private String startOpenDate1 = "";
	// 结束时间 
	private String endOpenDate = "";
	// 结束时间 
	private String endOpenDate1 = "";
	
	//设备序列号
	private String device_serialnumber;
	
	private String device_id;
	
	private String task_id;
	
	//定制任务详情信息
	private List<Map> monitorList;
	
	//查看采集详情
	private List<Map> eponLanList;
	
	private String ajax;
	
	@Override
	public String execute() throws Exception
	{
		startOpenDate = getStartDate();
		endOpenDate = getEndDate();
		return "init";
	}
	
	public String getMonitorFlowQuery(){
		setTime();
		monitorList = bio.getMonitorFlowQuery(startOpenDate1, endOpenDate1, device_serialnumber, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getMonitorFlowQueryCount(startOpenDate1, endOpenDate1, device_serialnumber, curPage_splitPage, num_splitPage);
		return "monitorList";
	}
	
	public String getePonLanInfo(){
		eponLanList = bio.getePonLanInfo(task_id, device_id, curPage_splitPage, num_splitPage);
		maxPage_splitPage = bio.getePonLanInfoCount(task_id, device_id, curPage_splitPage, num_splitPage);
		return "eponLanList";
	}
	
	public String deleteMonitorFlow(){
		ajax = bio.deleteMonitorFlow(task_id);
		return "ajax";
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}
	
	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + startOpenDate);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (startOpenDate == null || "".equals(startOpenDate))
		{
			startOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(startOpenDate);
			startOpenDate1 = String.valueOf(dt.getLongTime());
		}
		
		if (endOpenDate == null || "".equals(endOpenDate))
		{
			endOpenDate1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endOpenDate);
			endOpenDate1 = String.valueOf(dt.getLongTime());
		}
	}

	// 今天零点
	private String getStartDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	// 当前时间的23:59:59,如 2011-05-11 23:59:59
	private String getEndDate()
	{
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		return time;
	}

	
	public String getStartOpenDate()
	{
		return startOpenDate;
	}

	
	public void setStartOpenDate(String startOpenDate)
	{
		this.startOpenDate = startOpenDate;
	}

	
	public String getEndOpenDate()
	{
		return endOpenDate;
	}

	
	public void setEndOpenDate(String endOpenDate)
	{
		this.endOpenDate = endOpenDate;
	}

	
	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	
	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	
	public List<Map> getMonitorList()
	{
		return monitorList;
	}

	
	public void setMonitorList(List<Map> monitorList)
	{
		this.monitorList = monitorList;
	}

	
	public List<Map> getEponLanList()
	{
		return eponLanList;
	}

	
	public void setEponLanList(List<Map> eponLanList)
	{
		this.eponLanList = eponLanList;
	}

	
	public MonitorFlowInfoQueryBIO getBio()
	{
		return bio;
	}

	
	public void setBio(MonitorFlowInfoQueryBIO bio)
	{
		this.bio = bio;
	}

	
	public String getDevice_id()
	{
		return device_id;
	}

	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	
	public String getTask_id()
	{
		return task_id;
	}

	
	public void setTask_id(String task_id)
	{
		this.task_id = task_id;
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}
}
