/**
 * 
 */
package com.linkage.module.gwms.service.obj;

import java.util.List;
import java.util.Map;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2010-6-9
 * @category com.linkage.module.gwms.service.obj
 * 
 */
public class ServStrategyConfigOBJ {

	private String id = null;
	
	private String taskId = null;
	
	private String deviceId = null;
	
	private String deviceSerialnumber = null;
	
	private String username = null;
	
	private int listSize = 1;
	
	private String oui = null;
	
	private List<Map<String,String>> list = null;

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceSerialnumber
	 */
	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	/**
	 * @param deviceSerialnumber the deviceSerialnumber to set
	 */
	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the list
	 */
	public List<Map<String, String>> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the oui
	 */
	public String getOui() {
		return oui;
	}

	/**
	 * @param oui the oui to set
	 */
	public void setOui(String oui) {
		this.oui = oui;
	}

	/**
	 * @return the listSize
	 */
	public int getListSize() {
		return listSize;
	}

	/**
	 * @param listSize the listSize to set
	 */
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
}
