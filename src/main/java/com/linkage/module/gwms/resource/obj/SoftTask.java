/**
 * 
 */
package com.linkage.module.gwms.resource.obj;

/**
 * MQ对象类
 * @author chenjie
 * @version 1.0
 * @since 2013-6-6
 */
public class SoftTask {

	private String clientId;
	
	private int taskId;
	
	private int action;

	public SoftTask(){
		
	}
	public SoftTask(String clientId,int taskId,int action){
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
