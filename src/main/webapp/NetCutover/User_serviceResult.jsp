<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct" %>

<%
	request.setCharacterEncoding("GBK");
	
	//��ѯ�û��Ϳ�ͨҵ��Ķ�Ӧ��ϵ	
	String strList = ServiceAct.getUserService(request);
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("id_div").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>