package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;
/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-3-14
 */
public class RouterTerminalListBak {
	private List<RouterTerminal> routerTerminals;
	
	private String RouteVer;
	
	public String getRouteVer() {
		return RouteVer;
	}
	public void setRouteVer(String routeVer) {
		RouteVer = routeVer;
	}
	
	public List<RouterTerminal> getRouterTerminals() {
		return routerTerminals;
	}
	public void setRouterTerminals(List<RouterTerminal> routerTerminals) {
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
		// 循环遍历多个terminal
		for(RouterTerminal flt:routerTerminals){
			xmlStrBuilder.append("<TerminalInfo>").append(NEWLINE);
			xmlStrBuilder.append("<Type>").append(flt.getType())
				.append("</Type>").append(NEWLINE);
			xmlStrBuilder.append("<VLANID>").append(flt.getVLANID())
				.append("</VLANID>").append(NEWLINE);
			xmlStrBuilder.append("<DNSSlect>").append(flt.getDNSSlect())
				.append("</DNSSlect>").append(NEWLINE);
			xmlStrBuilder.append("<Softver>").append(flt.getSoftver())
				.append("</Softver>").append(NEWLINE);
			// 循环遍历多个routers
			for(Router rou:flt.getRouters()){
				xmlStrBuilder.append("<Routes>").append(NEWLINE);
				xmlStrBuilder.append("<Destination>").append(rou.getDestination()).append("</Destination>").append(NEWLINE);
				xmlStrBuilder.append("<Netmask>").append(rou.getNetmask()).append("</Netmask>").append(NEWLINE);
				xmlStrBuilder.append("<Plane>").append(rou.getPlane()).append("</Plane>").append(NEWLINE);
				xmlStrBuilder.append("</Routes>").append(NEWLINE);
			}
			xmlStrBuilder.append("</TerminalInfo>").append(NEWLINE);
		}

		xmlStrBuilder.append("</JsdxIPTV>");
		
		return xmlStrBuilder.toString();
	}
}
