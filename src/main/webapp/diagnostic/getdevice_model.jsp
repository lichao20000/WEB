<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileService" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
	request.setCharacterEncoding("GBK");

	//�Ƿ���Ҫ���豸ID���й���·��
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	String strList = strList = fileService.getDeviceHtml(request,LipossGlobals.SystemType(),"radio",needFilter_b);
	

%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>