package com.linkage.litms.flux;

public class BasePortInfo {
	
	private String ifindex = null;
	private String ifdescr = null;
	private String ifname = null;
	private String ifnamedefined = null;
	private String ifportip = null;
	private String if_real_speed = null;
	private String iftype = null;
	
	public BasePortInfo() {
		
	}

	public String getIf_real_speed() {
		return if_real_speed;
	}

	public void setIf_real_speed(String if_real_speed) {
		this.if_real_speed = if_real_speed;
	}

	public String getIfdescr() {
		return ifdescr;
	}

	public void setIfdescr(String ifdescr) {
		this.ifdescr = ifdescr;
	}

	public String getIfindex() {
		return ifindex;
	}

	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	public String getIfname() {
		return ifname;
	}

	public void setIfname(String ifname) {
		this.ifname = ifname;
	}

	public String getIfnamedefined() {
		return ifnamedefined;
	}

	public void setIfnamedefined(String ifnamedefined) {
		this.ifnamedefined = ifnamedefined;
	}

	public String getIfportip() {
		return ifportip;
	}

	public void setIfportip(String ifportip) {
		this.ifportip = ifportip;
	}

	public String getIftype() {
		return iftype;
	}

	public void setIftype(String iftype) {
		this.iftype = iftype;
	}
}
