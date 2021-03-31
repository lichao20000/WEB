package com.linkage.module.gtms.config.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.serv.BatchAddIptvNodeBIO;

import action.splitpage.splitPageAction;

/**
 * @author Administrator
 * 
 */
public class BatchAddIptvNodeACT extends splitPageAction implements
		SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(BatchAddIptvNodeACT.class);
	// 传参
	private String deviceIds = "";
	// Session
	private Map<String, Object> session;
	
	private String gwShare_fileName = "";
	private BatchAddIptvNodeBIO bio;
	private String ajax = "";
	private long userId;
	private String gw_type = "";
	private String strategy_type = "";
	private String task_id;
	private String task_name;
	/** sqlSpell*/
	private String param;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	
	
	public void setTime() {
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate(); // 获取当前时间
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000); // 精确到当前时间的23点59分59秒
		endtime = dt.getLongDate();
		starttime = dt.getFirtDayOfMonth(); // 获取开始时间，为当月时间的第一天
		dt = new DateTimeUtil(starttime);
		long start_time = dt.getLongTime();
		dt = new DateTimeUtil((start_time) * 1000);
		starttime = dt.getLongDate();
	}

	public String addTaskInfoHBLT(){
		
		long todayCount = bio.getTodayCount();
		if (500000 < todayCount) {
			ajax = "今日执行用户数已满，不能超过50万个";
			return "ajax";
		}
		
		this.setTime();
		UserRes curUser = (UserRes) session.get("curUser");
		long accoid = curUser.getUser().getId();
		long time = new DateTimeUtil().getLongTime();
		
		ajax = bio.addTaskHBLT(time, accoid, deviceIds, param, strategy_type);
		if("true".equals(ajax))
		{
			ajax = "批量定制任务执行成功！";
		} 
		else if ("false".equals(ajax))
		{
			ajax = "批量定制任务执行失败！";
		}
		else if("false10000".equals(ajax))
		{
			ajax = "定制设备超过10000条，定制失败！";
		}
		
		return "ajax";
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getStrategy_type() {
		return strategy_type;
	}

	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
	}

	public String getGwShare_fileName() {
		return gwShare_fileName;
	}

	public void setGwShare_fileName(String gwShare_fileName) {
		this.gwShare_fileName = gwShare_fileName;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}






	public BatchAddIptvNodeBIO getBio() {
		return bio;
	}






	public void setBio(BatchAddIptvNodeBIO bio) {
		this.bio = bio;
	}






	public String getParam() {
		return param;
	}






	public void setParam(String param) {
		this.param = param;
	}






	public String getStarttime() {
		return starttime;
	}






	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}






	public String getEndtime() {
		return endtime;
	}






	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	
}
