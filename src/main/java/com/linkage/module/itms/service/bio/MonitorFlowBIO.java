package com.linkage.module.itms.service.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.itms.service.dao.MonitorFlowDAO;
import com.linkage.module.itms.service.obj.FlowTask;
import com.linkage.module.itms.service.obj.MQPublisher;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-13
 * @category com.linkage.module.itms.service.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class MonitorFlowBIO
{
	private static Logger logger = LoggerFactory.getLogger(MonitorFlowBIO.class);
	private MonitorFlowDAO dao;
	
	public int customTask(String task_id, String device_id, String interval,
			String times, String start_time, String end_time, String status){
		logger.debug("MonitorFlowBIO=>customTask");
		
		//START  发送mq消息
//		MQPublisher publisher = new MQPublisher(LipossGlobals
//				.getLipossProperty("mqflowTask.enab"), LipossGlobals
//				.getLipossProperty("mqflowTask.url"), LipossGlobals
//				.getLipossProperty("mqflowTask.topic"));
		MQPublisher publisher = new MQPublisher("flow.task");
		FlowTask task = new FlowTask();
		task.setAction("1");
		task.setClientId("WEB");
		task.setTaskId(task_id);
		publisher.publishMQ(task);
		//end  发送mq消息
		return dao.customTask(task_id, device_id, interval, times, start_time, end_time, status);
	}

	
	public MonitorFlowDAO getDao()
	{
		return dao;
	}

	
	public void setDao(MonitorFlowDAO dao)
	{
		this.dao = dao;
	}
}
