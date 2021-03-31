package com.linkage.module.itms.service.obj;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-9-16
 * @category com.linkage.module.itms.service.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class FlowTask
{	
	
	//1,代表新增  3.代表删除
	private String action;
	
	//默认WEB
	private String clientId;
	
	private String taskId;
	
	
	
	public FlowTask()
	{
	}

	public FlowTask(String action, String clientId, String taskId)
	{
		super();
		this.action = action;
		this.clientId = clientId;
		this.taskId = taskId;
	}

	public String getAction()
	{
		return action;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public String getClientId()
	{
		return clientId;
	}
	
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	
	public String getTaskId()
	{
		return taskId;
	}
	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	
}
