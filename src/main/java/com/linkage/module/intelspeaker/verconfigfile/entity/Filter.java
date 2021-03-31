package com.linkage.module.intelspeaker.verconfigfile.entity;

/**
 * 白名单信息
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-3-22
 */
public class Filter {
	private String Destination;
	private String Netmask;

	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getNetmask() {
		return Netmask;
	}
	public void setNetmask(String netmask) {
		Netmask = netmask;
	}
	public Filter(){
		
	}
	public Filter(String childText,String childText2) {
		this.Destination=childText;
		this.Netmask=childText2;
	}
	@Override
	public String toString() {
		return "Filter [Destination=" + Destination + ", Netmask =" + Netmask + "]";
	}

}
