<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.netcutover.ServiceAct" %>

<%
	request.setCharacterEncoding("GBK");
	//根据条件获取 家庭网关，企业网关，snmp设备 用户列表
	//说明：0：家庭网关用户，1：企业网关用户，2：snmp 设备用户 ，3：所有用户
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