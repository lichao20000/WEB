package com.linkage.module.gwms.resource.act;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchResumeBIO;
import action.splitpage.splitPageAction;

public class BatchResumeACT	 extends splitPageAction implements SessionAware
{

	private static final long serialVersionUID = 235985415556L;
	
	private Map<String, Object> session;
	private BatchResumeBIO bio;
	
	private String gwShare_fileName = "";
	private String deviceIds = "";
	private String ajax = "";
	private String gw_type = "";
	private String task_name;
	private String param;
	
	
	
	@SuppressWarnings("deprecation")
	public String importTask()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long taskid = new DateTimeUtil().getLongTime();
		try {
			task_name=URLDecoder.decode(task_name,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ajax = bio.importTask(taskid,task_name, accoid, deviceIds, param);
		if("true".equals(ajax))
		{
			ajax = "批量定制任务执行成功，正在后台执行！";
		} 
		else if ("false".equals(ajax))
		{
			ajax = "批量定制任务执行失败！";
		}
		else if("false1000".equals(ajax))
		{
			ajax = "定制设备超过1000条，定制失败！";
		}
		
		return "ajax";
	}
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public BatchResumeBIO getBio() {
		return bio;
	}

	public void setBio(BatchResumeBIO bio) {
		this.bio = bio;
	}
	public static void main(String[] args) {
		String decode = URLDecoder.decode("%E6%B5%8B%E8%AF%95");
		System.out.println(decode);
	}
}
