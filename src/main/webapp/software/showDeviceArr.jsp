<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");
	String devicetype_id = request.getParameter("devicetype_id");
	
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	String strList= null;

	strList = versionManage.getDeviceHtmlUseDeviceVersion(request, needFilter_b);
	//String file_path1 = versionManage.getFilePath_1("file_path_1");
	String file_path2 = versionManage.getVersionFilePathToConfigStock(devicetype_id);
	//String file_path3 = versionManage.getFilePath_2("file_path_3",devicetype_id,"");
	
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
	parent.document.all("path_2").innerHTML = "<%=file_path2%>";	
</SCRIPT>
</body>
</html>