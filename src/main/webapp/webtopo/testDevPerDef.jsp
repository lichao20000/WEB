<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<% 
DevPerDef dpd = new DevPerDef(request);
int retflag = dpd.actionPerformedOne();
if (retflag >=0) {
	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	out.println("window.alert(\"���ܶ���ɹ�,ϵͳ��̨��ʼʵ�����豸��\");");
	out.println("</SCRIPT>");
	dpd.actionPerformedTwo();
}
else {
	out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
	out.println("window.alert(\"���ܶ������ʧ�ܣ������²���!\");");
	out.println("</SCRIPT>");
}
%>
