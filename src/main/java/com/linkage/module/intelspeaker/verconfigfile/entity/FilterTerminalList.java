package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;

public class FilterTerminalList {
	// 包含多个白名单
	private List<FilterTerminal> filterTerminals;
	// 白名单版本号
	private String filterVer;
	
	public List<FilterTerminal> getFilterTerminals() {
		return filterTerminals;
	}
	public void setFilterTerminals(List<FilterTerminal> filterTerminals) {
		this.filterTerminals = filterTerminals;
	}
	public String getFilterVer() {
		return filterVer;
	}
	public void setFilterVer(String filterVer) {
		this.filterVer = filterVer;
	}
	@Override
	public String toString() {
		return "FilterTerminalList [filterTerminals=" + filterTerminals + ", filterVer=" + filterVer +"]";
	}
	public String toXMLString() {
		String NEWLINE=System.getProperty("line.separator");
		StringBuilder xmlStrBuilder=new StringBuilder();
		xmlStrBuilder.append("<JsdxIPTV  FilterVer= \"").append(filterVer).append("\" >").append(NEWLINE);
		// 循环遍历多个terminal
		for(FilterTerminal flt:filterTerminals){
			xmlStrBuilder.append("<TerminalInfo>").append(NEWLINE);
			xmlStrBuilder.append("<Type>").append(flt.getType())
				.append("</Type>").append(NEWLINE);
			xmlStrBuilder.append("<Softver>").append(flt.getSoftver())
				.append("</Softver>").append(NEWLINE);
			// 循环遍历多个routers
			for(Filter f:flt.getFilters()){
				xmlStrBuilder.append("<Filters>").append(NEWLINE);
				xmlStrBuilder.append("<Destination>").append(f.getDestination()).append("</Destination>").append(NEWLINE);
				xmlStrBuilder.append("<Netmask>").append(f.getNetmask()).append("</Netmask>").append(NEWLINE);
				xmlStrBuilder.append("</Filters>").append(NEWLINE);
			}
			xmlStrBuilder.append("</TerminalInfo>").append(NEWLINE);
		}

		xmlStrBuilder.append("</JsdxIPTV>");
		
		return xmlStrBuilder.toString();
	}
}
