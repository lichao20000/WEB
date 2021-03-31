package com.linkage.module.itms.service.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.service.dao.MonitorFlowInfoQueryDAO;
import com.linkage.module.itms.service.obj.FlowTask;
import com.linkage.module.itms.service.obj.MQPublisher;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-16
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorFlowInfoQueryBIO
{
	private static Logger logger = LoggerFactory.getLogger(MonitorFlowInfoQueryBIO.class);
	
	private  MonitorFlowInfoQueryDAO dao;
	
	public MonitorFlowInfoQueryBIO(){
		
	}
	
	
	public List<Map> getMonitorFlowQuery(String start_time, String end_time,
			String device_serialnumber, int curPage_splitPage,
			int num_splitPage){
		logger.debug("MonitorFlowInfoQueryBIO=>getMonitorFlowQuery");
		String dev_sub_sn = null;
		if(!StringUtil.IsEmpty(device_serialnumber)){
			dev_sub_sn = device_serialnumber.substring(device_serialnumber.length()-6);
		}
		 return dao.getMonitorFlowQuery(start_time, end_time, device_serialnumber, dev_sub_sn, curPage_splitPage, num_splitPage);
	}
	
	public int getMonitorFlowQueryCount(String start_time, String end_time,
			String device_serialnumber, int curPage_splitPage,
			int num_splitPage){
		logger.debug("MonitorFlowInfoQueryBIO=>getMonitorFlowQueryCount");
		String dev_sub_sn = null;
		if(!StringUtil.IsEmpty(device_serialnumber)){
			dev_sub_sn = device_serialnumber.substring(device_serialnumber.length()-6);
		}
		return dao.getMonitorFlowQueryCount(start_time, end_time, device_serialnumber, dev_sub_sn, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getePonLanInfo(String task_id, String device_id,
			int curPage_splitPage, int num_splitPage){
		logger.debug("MonitorFlowInfoQueryBIO=>getePonLanInfo");
		return dao.getePonLanInfo(task_id, device_id, curPage_splitPage, num_splitPage);
	}
	public int getePonLanInfoCount(String task_id, String device_id,
			int curPage_splitPage, int num_splitPage){
		logger.debug("MonitorFlowInfoQueryBIO=>getePonLanInfoCount");
		return dao.getePonLanInfoCount(task_id, device_id, curPage_splitPage, num_splitPage);
	}
	
	public String deleteMonitorFlow(String task_id){
		logger.debug("MonitorFlowInfoQueryBIO=>deleteMonitorFlow");
		//START  发送mq消息
//		MQPublisher publisher = new MQPublisher(LipossGlobals
//				.getLipossProperty("mqflowTask.enab"), LipossGlobals
//				.getLipossProperty("mqflowTask.url"), LipossGlobals
//				.getLipossProperty("mqflowTask.topic"));
		MQPublisher publisher = new MQPublisher("flow.task");
		FlowTask task = new FlowTask();
		task.setAction("3");
		task.setClientId("WEB");
		task.setTaskId(task_id);
		publisher.publishMQ(task);
		//end  发送mq消息
		int[] num = dao.deleteMonitorFlow(task_id);
		return "删除成功!";
	}
		
	public MonitorFlowInfoQueryDAO getDao()
	{
		return dao;
	}

	
	public void setDao(MonitorFlowInfoQueryDAO dao)
	{
		this.dao = dao;
	}
	
}
