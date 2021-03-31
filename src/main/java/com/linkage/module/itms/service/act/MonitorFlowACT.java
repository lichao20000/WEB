package com.linkage.module.itms.service.act;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.bio.MonitorFlowBIO;
import com.opensymphony.xwork2.ActionSupport;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-13
 * @category com.linkage.module.itms.service.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorFlowACT extends ActionSupport implements SessionAware,ServletRequestAware
{
	// session
	private Map session = null;
	private HttpServletRequest request;
	private MonitorFlowBIO bio;
	
	private String device_id;
	private String interval;
	private String count;
	
	
	private String ajax;
	
	@Override
	public String execute() throws Exception
	{
		return "init";
	}

	public String customTask(){
		String taskid = getId();
		int num = bio.customTask(taskid, device_id, interval, count, taskid, "0", "0");
		ajax = StringUtil.getStringValue(num);
		return "ajax";
	}
	
	private String getId(){
		GregorianCalendar now = new GregorianCalendar();
		SimpleDateFormat fmtrq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		String time = fmtrq.format(now.getTime());
		DateTimeUtil dt = new DateTimeUtil(time);
		return  String.valueOf(dt.getLongTime());
	}

	

	@Override
	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}



	
	public String getDevice_id()
	{
		return device_id;
	}



	
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}



	
	public String getInterval()
	{
		return interval;
	}



	
	public void setInterval(String interval)
	{
		this.interval = interval;
	}



	
	public String getCount()
	{
		return count;
	}



	
	public void setCount(String count)
	{
		this.count = count;
	}





	
	public String getAjax()
	{
		return ajax;
	}





	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


	
	public MonitorFlowBIO getBio()
	{
		return bio;
	}


	
	public void setBio(MonitorFlowBIO bio)
	{
		this.bio = bio;
	}
}
