<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String[] gatherArray=request.getParameterValues("process");


//֪ͨ���½������˷���
VpnScheduler vpnscheduler = new VpnScheduler();
vpnscheduler.getVPNWebTopoManager().I_InformStartVPN(gatherArray);

//clear
gatherArray = null;
vpnscheduler = null;
%>
