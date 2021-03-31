<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="BnetSheetOperator" scope="request" class="com.linkage.litms.netcutover.BnetSheetOperator"/>
<%
request.setCharacterEncoding("GBK");
boolean flag = true;
flag = BnetSheetOperator.saveBnetSheet(request);
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = "<%=flag%>";
if(parent){
	parent.success(flag);
}
//-->
</SCRIPT>