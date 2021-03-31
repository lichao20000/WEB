package com.linkage.litms.webtopo;

public class DevicePortInfo {

	public DevicePortInfo(int ifindex, String ifdesc, String ifnamedefined) {
		
		this.ifindex = 0;
		this.ifdesc = "";
		this.ifnamedefined = "";
		this.ifindex = ifindex;
		this.ifdesc = ifdesc;
		this.ifnamedefined = ifnamedefined;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getIfdesc() {
		return ifdesc;
	}

	public void setIfdesc(String ifdesc) {
		this.ifdesc = ifdesc;
	}

	public int getIfindex() {
		return ifindex;
	}

	public void setIfindex(int ifindex) {
		this.ifindex = ifindex;
	}

	public String getifnamedefined() {
		return ifnamedefined;
	}

	public void setifnamedefined(String ifnamedefined) {
		this.ifnamedefined = ifnamedefined;
	}

	private String device_id;

	private int ifindex;

	private String ifdesc;

	private String ifnamedefined;
}