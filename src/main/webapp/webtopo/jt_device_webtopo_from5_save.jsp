<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: �ʺ����ӡ��޸ġ�ɾ������
--%>
<html>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.Filedevice"/>
<%
	request.setCharacterEncoding("GBK");
	String strMsg = DeviceAct.DSLList(request);
%>

<body>
<SPAN ID="child"><%=strMsg%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_DSL").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>


