package com.linkage.module.gtms.system.obj;

/**
 * 日志管理对象
 * 
 * @author Jason(3412)
 * @date 2010-12-4
 */
public class LogOBJ {

	// 账号
	private String account;
	// 模块
	private String itemSelect;
	// 操作类型
	private String operType;
	// 操作名称
	private String operContent;
	// ip地址
	private String ipAddr;
	// 主机名
	private String hostname;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 日志时间
	private String logtime;
	
	//登录域
	private String area_id;
	//角色ID
	private String role_id;

	/** getter, setter methods */

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperContent() {
		return operContent;
	}

	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
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

	public String getItemSelect() {
		return itemSelect;
	}

	public void setItemSelect(String itemSelect) {
		this.itemSelect = itemSelect;
	}

	public String getLogtime() {
		return logtime;
	}

	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	
	
}
