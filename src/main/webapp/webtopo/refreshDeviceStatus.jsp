<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.Scheduler"%>
<%@ page import= "java.util.*"%>

<%
request.setCharacterEncoding("GBK");
String[] arr = (String[])session.getAttribute("allobjs");
Scheduler scheduler = new Scheduler();
String result = scheduler.getLayerDevStatus(arr);
%>
<html>
<head>

</head>
<body onload="start();">
<SCRIPT LANGUAGE="JavaScript">
<!--
function start(){
	if(parent != null){
		var s = "<%=result%>";
		parent.resetAllDeviceStatus();
		parent.refreshDeviceStatus(s);
	}
}
//-->
</SCRIPT>
</body>
</html>