<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%
request.setCharacterEncoding("GBK");
Map mapDevVersion = DeviceAct.queryDeviceVersion(request);
String strData = "";
if(mapDevVersion != null){
	Set set = mapDevVersion.keySet();
	Iterator iterator = set.iterator();
	String device_name	= null;
	String version		= null;
	while(iterator.hasNext()){
		device_name = (String)iterator.next();
		version = (String)mapDevVersion.get(device_name);
		strData += "<tr bgcolor='#ffffff'>";
		strData += "<td class=column>" + device_name + "</td>";
		strData += "<td class=column>" + version + "</td>";
		strData += "</tr>";
	}
}else{
	strData = "<tr bgcolor='#ffffff'><td colspan=2>查询无数据.</td></td>";
}
%>
<TABLE border=0 cellspacing=0 cellpadding=0 align="center" width="95%">
	<TR>
		<TD bgcolor=#000000>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH>设备OUI-序列号</TH>
					<TH>版本</TH>
				</TR>
				<%=strData%>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<%
DeviceAct = null;
if(mapDevVersion != null){
	mapDevVersion.clear();
}
mapDevVersion = null;
%>