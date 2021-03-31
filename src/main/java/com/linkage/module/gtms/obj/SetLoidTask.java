package com.linkage.module.gtms.obj;

public class SetLoidTask {
	
	private String clientId;
	
	private int taskId;
	
	private int action;
	
	public SetLoidTask(){
		
	}
	public SetLoidTask(String clientId, int taskId, int action) {
		super();
		this.clientId = clientId;
		this.taskId = taskId;
		this.action = action;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	
}
