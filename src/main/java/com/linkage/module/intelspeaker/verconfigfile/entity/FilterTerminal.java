package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;


/**
 * 白名单对象
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-3-22
 */
public class FilterTerminal{
	// 类型
	private String Type;
	// 版本
	private String Softver;
	private List<Filter> filters;
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
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
//	@Override
//	public int getTermType() {
//		return 2;
//	}
//	@Override
//	public String getVersion() {
//		return FilterVer;
//	}
//	@Override
//	public String toXMLString() {
//		String NEWLINE=System.getProperty("line.separator");
//		StringBuilder xmlStrBuilder=new StringBuilder();
//		xmlStrBuilder.append("<TerminalInfo>").append(NEWLINE);
//		xmlStrBuilder.append("<Type>").append(this.Type).append("</Type>").append(NEWLINE);
//		xmlStrBuilder.append("<Softver>").append(this.Softver).append("</Softver>").append(NEWLINE);
//		xmlStrBuilder.append("<FilterVer>").append(this.FilterVer).append("</FilterVer>").append(NEWLINE);
//		for(Filter flt:filters){
//			xmlStrBuilder.append("<Filters>").append(NEWLINE);
//				xmlStrBuilder.append("<Destination>").append(flt.getDestination()).append("</Destination>").append(NEWLINE);
//				xmlStrBuilder.append("<Netmask>").append(flt.getNetmask()).append("</Netmask>").append(NEWLINE);
//			xmlStrBuilder.append("</Filters>").append(NEWLINE);
//		}
//		xmlStrBuilder.append("</TerminalInfo>");
//		return xmlStrBuilder.toString();
//	}

	@Override
	public String toString() {
		return "FilterTerminal [Type=" + Type + ", Softver=" + Softver
				+ ", filters=" + filters + "]";
	}
	
}
