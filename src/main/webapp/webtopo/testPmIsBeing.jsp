<%@ include file="../timelater.jsp"%>

<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<SCRIPT LANGUAGE="JavaScript">
<!--
function ifupdatapm() {
	if (confirm('表达式已经定义过，确认要重新定义？'))
	{
		parent.frm.action="testDevPerDef.jsp";
		parent.frm.submit();
	}
	else
	{
		return;
	}
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%
		DevPerDef dpd = new DevPerDef(request);
		boolean b = dpd.is_Pmbeing();
		if (b) {
			out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
			out.println("ifupdatapm();");
			out.println("</SCRIPT>");
		}
		else {
			out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
			out.println("parent.frm.action=\"testDevPerDef.jsp\";");
			out.println("parent.frm.submit();");
			out.println("</SCRIPT>");
		}
	%>
