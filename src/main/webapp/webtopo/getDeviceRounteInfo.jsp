<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.snmpgather.DeviceRouteInfo" %>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
DeviceRouteInfo deviceRounteInfo = new DeviceRouteInfo(device_id,user.getAccount(),user.getPasswd());
String[][] data = deviceRounteInfo.fnGetData();
String[] column = deviceRounteInfo.getColumnOID();
StringBuffer sb = new StringBuffer();
int dataSize = 0;
if(data != null){
	//String[][] m_strDatas = new String[data.length][data[0].length];
	String color = null;
	for (int i = 0; i < data.length; i++) {
		if(i%2 == 0)
				color = "#FFFFCC";
			else
				color = "#ffffff";
		sb.append("<TR bgcolor='"+ color +"'>");
		for (int j = 0;j < data[0].length;j++) {	
			if (j == 7) {
				if (data[i][j].trim().equals("1")) {
					sb.append("<TD>other(1)</TD>");
				} else if (data[i][j].trim().equals("2")) {
					sb.append("<TD>invalid(2)</TD>");
				} else if (data[i][j].trim().equals("3")) {
					sb.append("<TD>direct(3)</TD>");
				} else if (data[i][j].trim().equals("4")) {
					sb.append("<TD>indirect(4)</TD>");
				} else {
					sb.append("<TD></TD>");
				}
			} else if (j == 8) {
				if (data[i][j].trim().equals("1")) {
					sb.append("<TD>").append("other(1)").append("</TD>");
				} else if (data[i][j].trim().equals("2")) {
					sb.append("<TD>").append("local(2)").append("</TD>");
				} else if (data[i][j].trim().equals("3")) {
					sb.append("<TD>").append("netmgmt(3)").append("</TD>");
				} else if (data[i][j].trim().equals("4")) {
					sb.append("<TD>").append("icmp(4)").append("</TD>");
				} else if (data[i][j].trim().equals("5")) {
					sb.append("<TD>").append("egp(5)").append("</TD>");
				} else if (data[i][j].trim().equals("6")) {
					sb.append("<TD>").append("ggp(6)").append("</TD>");
				} else if (data[i][j].trim().equals("7")) {
					sb.append("<TD>").append("hello(7)").append("</TD>");
				} else if (data[i][j].trim().equals("8")) {
					sb.append("<TD>").append("rip(8)").append("</TD>");
				} else if (data[i][j].trim().equals("9")) {
					sb.append("<TD>").append("is-is(9)").append("</TD>");
				} else if (data[i][j].trim().equals("10")) {
					sb.append("<TD>es-is(10)</TD>");
				} else if (data[i][j].trim().equals("11")) {
					sb.append("<TD>ciscoIgrp(11)</TD>");
				} else if (data[i][j].trim().equals("12")) {
					sb.append("<TD>bbnSpfIgp(12)</TD>");
				} else if (data[i][j].trim().equals("13")) {
					sb.append("<TD>ospf(13)</TD>");
				} else if (data[i][j].trim().equals("14")) {
					sb.append("<TD>bgp(14)</TD>");
				} else {
					sb.append("<TD></TD>");
				}
			} else {
				sb.append("<TD>").append(data[i][j]).append("</TD>");
			}
			


//			sb.append("<TD>").append(data[i][j]).append("</TD>");
		}
		sb.append("</TR>");
		if (!"".equals(data[i][0])){
			dataSize ++;
		}
	}
}else{
    sb.append("<TR bgcolor=#ffffff>");
    sb.append("<TD colspan="+ column.length +">没有采集到路由数据.</TD>");
    sb.append("</TR>");
}

%>
<br>
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" align=center valign=middle bgcolor="#000000">
	<TR bgcolor=#ffffff>
		<TD align=center colspan=<%=column.length%>>设备IP地址：<%=deviceRounteInfo.getDeviceIP()%></TD>
	</TR>
	<TR>
		<%
			for(int i = 0; i < column.length; i ++){
				out.println("<TH>");
				out.println(column[i]);
				out.println("</TH>");				
			}
		%>
	</TR>
	<%=sb.toString()%>
	<TR bgcolor=#ffffff>
		<TD colspan=<%=column.length%> class=foot>当前设备有<%=dataSize %>条路由</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<%deviceRounteInfo = null;%>