<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.TraceRoute" %>

<%
//String type= request.getParameter("type");
//String html = "";

//if ("2".equals(type)){
//	html += "该设备不支持以SNMP协议配置DNS信息";
//}
//else{
//	html = TraceRoute.traceRoute(request);
//}
String html = TraceRoute.traceRoute(request);
%>
<html>
<head>
<script type="text/javascript">
//parent.document.all("div_ping").innerHTML = "<%//=html%>";
parent.document.all("div_TraceRoute").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
