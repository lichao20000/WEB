package com.linkage.module.liposs.system.moduleinterface;

public class ModuleManager {
	private String device_id;//设备ID
	private String serial;//设备型号ID
	private int configtype;//配置指标
	private String name;//中文名称
	private String vendor_id;//厂商ID
	private String atrrvalue;//指标
	
	/**
	 * 默认构造方法
	 * @param configtype:<font color='red'>【必须】</font>：配置指标
	 * @param name      :<font color='red'>【必须】</font>：配置名称
	 * @param vendor_id :<font color='red'>【必须】</font>：厂商ID
	 */
	public ModuleManager(int configtype,String name,String vendor_id){
		this.configtype=configtype;
		this.name=name;
		this.vendor_id=vendor_id;
	}
	
	
	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public int getConfigtype() {
		return configtype;
	}
	public void setConfigtype(int configtype) {
		this.configtype = configtype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}



	public String getAtrrvalue() {
		return atrrvalue;
	}



	public void setAtrrvalue(String atrrvalue) {
		this.atrrvalue = atrrvalue;
	}
}
