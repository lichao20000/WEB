<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<% 
DevPerDef dpd = new DevPerDef(request);
int retflag = dpd.actionPerformedOne();
if (retflag >=0) {
	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	out.println("window.alert(\"性能定义成功,系统后台开始实例化设备！\");");
	out.println("</SCRIPT>");
	dpd.actionPerformedTwo();
}
else {
	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	out.println("window.alert(\"性能定义操作失败，请重新操作!\");");
	out.println("</SCRIPT>");
}
%>
