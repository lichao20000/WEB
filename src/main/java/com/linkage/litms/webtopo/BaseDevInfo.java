package com.linkage.litms.webtopo;

public class BaseDevInfo {
	private String device_id = null;
	private String device_name = null;
	private String loopback_ip = null;
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getLoopback_ip() {
		return loopback_ip;
	}
	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}
}
