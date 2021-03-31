<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
request.setCharacterEncoding("GBK");
String gwtype = "";
try{
	gwtype = request.getParameter("gw_type");
}catch(NumberFormatException e){
}

if(gwtype == "" || gwtype == null){
	gwtype = "1";
}

//String gwtype = ;
	String strMsg = DeviceAct.DSLList(request,gwtype);
%>

<body>
<SPAN ID="child"><%=strMsg%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_DSL").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>


