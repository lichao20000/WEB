package com.linkage.module.intelspeaker.verconfigfile.entity;
/**
 * 路由表信息结构体
 * @author jlp
 *
 */
public class Router {
	/**
	 * 本路由表所包含的目标地址，支持单个IP、采用半角连线符“-”连接的某IP地址范围，多个记录间采用半角都好“,”进行分割，且无空格连接；
	 */
	private String Destination;
	/**
	 * 仅填写单个子网掩码，即要求Destination标签所标识的所有目标地址的都是以本标签为子网掩码。
	 */
	private String Netmask;
	/**
	 * 本路由表所属平面标识，，目前仅支持以下两种选择：
		A：表示互联网A平面；
		B：表示IPTV专用网络B平面；
		本标签必须填写A或B，不允许为空字符串
	*/
	private String Plane;
	public Router(){
		
	}
	public Router(String childText, String childText2, String childText3) {
		this.Destination=childText;
		this.Netmask=childText2;
		this.Plane=childText3;
	}
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
	public String getPlane() {
		return Plane;
	}
	public void setPlane(String plane) {
		Plane = plane;
	}
	@Override
	public String toString() {
		return "Router [Destination=" + Destination + ", Netmask=" + Netmask
				+ ", Plane=" + Plane + "]";
	}
	
	
	
}
