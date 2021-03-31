package com.linkage.module.gtms.stb.obj.tr069;

public class Task {
	
	private String clientId = null;
	private String taskId = null;
	private String action = null;
	private String priority  = null;
	private String city_id	 = null;
	private String trade_id  = null;
	private String group_id  = null;
	
	public Task(){
		
	}
	public Task(String clientId, String taskId, String action){
		this.clientId = clientId;
		this.taskId = taskId;
		this.action = action;
	}
	public Task(String clientId, String taskId, String action,String priority,String city_id,String trade_id,String group_id){
		this.clientId = clientId;
		this.taskId = taskId;
		this.action = action;
		this.priority = priority;
		this.city_id = city_id;
		this.trade_id = trade_id;
		this.group_id = group_id;
	}
	
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String cityId) {
		city_id = cityId;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String tradeId) {
		trade_id = tradeId;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String groupId) {
		group_id = groupId;
	}
	
}
