<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.host.HGHostPerfmance"%>
<%
request.setCharacterEncoding("GBK");
HGHostPerfmance perfmance = new HGHostPerfmance(request,response);
String html = perfmance.getHostPerfTable();
perfmance = null;
%>
<div id="divData">
<%=html%>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.SetQueryData != null){
	parent.SetQueryData(divData.innerHTML);
}
//-->
</SCRIPT>