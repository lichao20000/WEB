package com.linkage.module.intelspeaker.verconfigfile.entity;

import java.util.List;

/*<TerminalInfo>
<Type>EC5108</Type>
<Softver>V32.1.3<Softver>
    <FilterVer>1.2.5</FilterVer>
    <Filters>
		<Destination>114.90.53.153</Destination>
		<Destination>114.90.53.155-114.90.53.200</Destination>
		<Destination>…</Destination>
    </Filters>
</TerminalInfo>
<TerminalInfo>…</TerminalInfo>*/
public class FilterTerminalBak  implements Terminal{
	private String Type;
	private String Softver;
	private String FilterVer;
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
	public String getFilterVer() {
		return FilterVer;
	}
	public void setFilterVer(String filterVer) {
		FilterVer = filterVer;
	}
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	@Override
	public int getTermType() {
		return 2;
	}
	@Override
	public String getVersion() {
		return FilterVer;
	}
	@Override
	public String toXMLString() {
		String NEWLINE=System.getProperty("line.separator");
		StringBuilder xmlStrBuilder=new StringBuilder();
		xmlStrBuilder.append("<TerminalInfo>").append(NEWLINE);;
		xmlStrBuilder.append("<Type>").append(this.Type).append("</Type>").append(NEWLINE);
		xmlStrBuilder.append("<Softver>").append(this.Softver).append("</Softver>").append(NEWLINE);
		xmlStrBuilder.append("<FilterVer>").append(this.FilterVer).append("</FilterVer>").append(NEWLINE);
		for(Filter flt:filters){
			xmlStrBuilder.append("<Filters>").append(NEWLINE);
//			for(String destination:flt.getDestination()){
//				xmlStrBuilder.append("<Destination>").append(flt.getDestination()).append("</Destination>").append(NEWLINE);
//			}
			xmlStrBuilder.append("</Filters>").append(NEWLINE);
		}
		xmlStrBuilder.append("</TerminalInfo>");
		return xmlStrBuilder.toString();
	}

	@Override
	public String toString() {
		return "FilterTerminal [Type=" + Type + ", Softver=" + Softver
				+ ", FilterVer=" + FilterVer + ", filters=" + filters + "]";
	}
	
}
