<html>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String strList= null;
	//�õ���ҳ���id,�Ա㽫����html�׵�Ŀ�ĵ�.
	String service_id = request.getParameter("service_id");	
	strList = sheetManage.getServiceCode(service_id,"");
%>

<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_service").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>