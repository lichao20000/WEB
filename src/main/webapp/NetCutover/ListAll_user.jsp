<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct" %>

<%
	request.setCharacterEncoding("GBK");
	//����������ȡ ��ͥ���أ���ҵ���أ�snmp�豸 �û��б�
	//˵����0����ͥ�����û���1����ҵ�����û���2��snmp �豸�û� ��3�������û�
	String strList = ServiceAct.getAllUserList(request,3);
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("iduser").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>