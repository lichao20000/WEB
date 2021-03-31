package com.linkage.module.ids.act;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.bio.TaskMonitorBIO;

public class TaskMonitorACT{
	private static Logger logger = LoggerFactory
			.getLogger(TaskMonitorACT.class);
	
	// 开始时间格式 HH:mm 字符串
	private String task_starttime = "";
	// 结束时间
	private String modify_time = "";
	// 结束时间
	private String modify_time1 = "";
	// 监控周期
	private String task_peroid = "";
	//收件人
	private String mail_receiver = "";
	//邮件主题
	private String mail_subject = "";
	//邮件内容
	private String mail_content = "";
	//监控类型
	private String monitor_type = "";
	//监控内容 sql校验时，为sql内容。shell脚本校验时，为shell脚本保存路径
	private String monitor_content ="";
	
	private String ajax ="";
	
	private TaskMonitorBIO bio;
	
	public String addTaskMonitor(){
		logger.warn("addTaskMonitor({},{},{},{},{},{},{})",new Object[]{task_starttime,task_peroid,mail_receiver,mail_subject,mail_content,monitor_type,monitor_content});
	    int result = bio.addTaskMonitor(task_starttime,task_peroid,mail_receiver,mail_subject,mail_content,monitor_type,monitor_content);
		
	    if(result>0){
			ajax = "1,任务增加成功";
		}else{
			ajax = "0,任务增加失败";
		}
	    return "ajax";
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getTask_starttime() {
		return task_starttime;
	}

	public String getModify_time() {
		return modify_time;
	}

	public String getModify_time1() {
		return modify_time1;
	}

	public String getTask_peroid() {
		return task_peroid;
	}

	public String getMail_receiver() {
		return mail_receiver;
	}

	public String getMail_subject() {
		return mail_subject;
	}

	public String getMail_content() {
		return mail_content;
	}

	public String getMonitor_type() {
		return monitor_type;
	}

	public String getMonitor_content() {
		return monitor_content;
	}

	public void setBio(TaskMonitorBIO bio) {
		this.bio = bio;
	}

	public void setTask_starttime(String task_starttime) {
		this.task_starttime = task_starttime;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public void setModify_time1(String modify_time1) {
		this.modify_time1 = modify_time1;
	}

	public void setTask_peroid(String task_peroid) {
		this.task_peroid = task_peroid;
	}

	public void setMail_receiver(String mail_receiver) {
		this.mail_receiver = mail_receiver;
	}

	public void setMail_subject(String mail_subject) {
		this.mail_subject = mail_subject;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}

	public void setMonitor_type(String monitor_type) {
		this.monitor_type = monitor_type;
	}

	public void setMonitor_content(String monitor_content) {
		this.monitor_content = monitor_content;
	}
	
}
