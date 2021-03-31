<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.TraceRoute" %>

<%
String html = TraceRoute.traceRoute(request);
%>
<html>
<head>
<script type="text/javascript">
parent.document.all("div_TraceRoute").innerHTML = "<%=html%>";
</script>
</head>
<body></body>
</html>
