package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;

public class RouterTerminal{
	/**机顶盒类型，根据机顶盒请求中ModelName字段的值填写在此处。若为空字符串，则表示适用于所有机顶盒*/
	private String Type;
	/**机顶盒管理版本号，根据机顶盒请求中的机顶盒管理软件版本号填写在此处。若为空字符串，则表示适用于所有机顶盒。*/
	private String Softver;
	/**启用DNS服务器所属的平面，目前仅支持以下两种选择：
		A：表示互联网A平面；
		B：表示IPTV专用网络B平面；
		若本标签不填写，则认为默认选择A平面的DNS服务器
		*/
	private String DNSSlect;
	/**B平面接入的VLAN标识（VLANID为空，默认值为43）
	【注】：不填写VLANID的情况下，家庭网关默认将数据发往A平面，因此在此处无需定义A平面的VLANID；
	*/
	private String VLANID;
	
	/** 路由表信息结构体*/
	private List<Router> routers;
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getSoftver() {
		return Softver;
	}
	public void setSoftver(String softver) {
		Softver = softver;
	}
	public String getDNSSlect() {
		return DNSSlect;
	}
	public void setDNSSlect(String dNSSlect) {
		DNSSlect = dNSSlect;
	}
	public String getVLANID() {
		return VLANID;
	}
	public void setVLANID(String vLANID) {
		VLANID = vLANID;
	}
	public List<Router> getRouters() {
		return routers;
	}
	public void setRouters(List<Router> routers) {
		this.routers = routers;
	}
	@Override
	public String toString() {
		return "RouterTerminal [Type=" + Type + ", Softver=" + Softver
				+ ", DNSSlect=" + DNSSlect + ", VLANID=" + VLANID
				+ ", routers=" + routers + "]";
	}

}
