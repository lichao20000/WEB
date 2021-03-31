package com.linkage.litms.resource;

public class DoBusinessObject {

	private long id;
	private String device_id;
	private String gather_id;
	private String oui;
	private String device_serialnumber;
	private String vpiid;
	private String vciid;
	private String username;
	private String passwd;
	private String service_id;
	private String ipaddress;
	private String ipmask;
	private String gateway;
	private String adsl_ser;
	private String bind_port;
	private String wan_type;

	public String getWan_type() {
		return wan_type;
	}

	public void setWan_type(String wan_type) {
		this.wan_type = wan_type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getGather_id() {
		return gather_id;
	}

	public void setGather_id(String gather_id) {
		this.gather_id = gather_id;
	}

	public String getOui() {
		return oui;
	}

	public void setOui(String oui) {
		this.oui = oui;
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getVpiid() {
		return vpiid;
	}

	public void setVpiid(String vpiid) {
		this.vpiid = vpiid;
	}

	public String getVciid() {
		return vciid;
	}

	public void setVciid(String vciid) {
		this.vciid = vciid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getIpmask() {
		return ipmask;
	}

	public void setIpmask(String ipmask) {
		this.ipmask = ipmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getAdsl_ser() {
		return adsl_ser;
	}

	public void setAdsl_ser(String adsl_ser) {
		this.adsl_ser = adsl_ser;
	}

	public String getBind_port() {
		return bind_port;
	}

	public void setBind_port(String bind_port) {
		this.bind_port = bind_port;
	}

}
