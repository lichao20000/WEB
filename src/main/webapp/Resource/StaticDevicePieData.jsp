<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.VendorAct"%>

<%
request.setCharacterEncoding("GBK");
VendorAct vendorPer = new VendorAct(request,response);
String html = vendorPer.getVendorPerfTable();
vendorPer = null;
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