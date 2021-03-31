package com.linkage.module.intelspeaker.verconfigfile.entity;

public class RouterTerminalList {
	private RouterTerminal routerTerminals;
	/** 本Routes标签所代表的路由表的版本号，形式为a.b.c，a为主版本号，b为次版本号，c为修订版本号；*/
	private String RouteVer;
	
	public String getRouteVer() {
		return RouteVer;
	}
	public void setRouteVer(String routeVer) {
		RouteVer = routeVer;
	}
	
	public RouterTerminal getRouterTerminals() {
		return routerTerminals;
	}
	public void setRouterTerminals(RouterTerminal routerTerminals) {
		this.routerTerminals = routerTerminals;
	}
	@Override
	public String toString() {
		return "RouterTerminalList [routerTerminals=" + routerTerminals + ", RouteVer="+RouteVer+"]";
	}
	public String toXMLString() {
		String NEWLINE=System.getProperty("line.separator");
		StringBuilder xmlStrBuilder=new StringBuilder();
		xmlStrBuilder.append("<JsdxIPTV  RouteVer= \"").append(RouteVer).append("\" >").append(NEWLINE);
			xmlStrBuilder.append("<TerminalInfo>").append(NEWLINE);
			xmlStrBuilder.append("<Type>").append(routerTerminals.getType())
				.append("</Type>").append(NEWLINE);
			xmlStrBuilder.append("<VLANID>").append(routerTerminals.getVLANID())
				.append("</VLANID>").append(NEWLINE);
			xmlStrBuilder.append("<DNSSlect>").append(routerTerminals.getDNSSlect())
				.append("</DNSSlect>").append(NEWLINE);
			xmlStrBuilder.append("<Softver>").append(routerTerminals.getSoftver())
				.append("</Softver>").append(NEWLINE);
//			xmlStrBuilder.append("<RouteVer>").append(routerTerminals.getRouteVer())
//				.append("</RouteVer>").append(NEWLINE);
			for(Router rou:routerTerminals.getRouters()){
				xmlStrBuilder.append("<Routes>").append(NEWLINE);
				xmlStrBuilder.append("<Destination>").append(rou.getDestination()).append("</Destination>").append(NEWLINE);
				xmlStrBuilder.append("<Netmask>").append(rou.getNetmask()).append("</Netmask>").append(NEWLINE);
				xmlStrBuilder.append("<Plane>").append(rou.getPlane()).append("</Plane>").append(NEWLINE);
				xmlStrBuilder.append("</Routes>").append(NEWLINE);
			}
			xmlStrBuilder.append("</TerminalInfo>").append(NEWLINE);
		xmlStrBuilder.append("</JsdxIPTV>");
		
		return xmlStrBuilder.toString();
	}
}
