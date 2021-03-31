package com.linkage.module.gtms.stb.obj.tr069;

public class stbBatchConTask {
	
	public stbBatchConTask(String clientId,String taskId,String action)
	{
		this.clientId=clientId;
		this.taskId=taskId;
		this.action=action;

	}
	private String clientId;
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	private String taskId;
	
	private String action;
}
