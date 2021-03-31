<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");
	String devicetype_id = request.getParameter("devicetype_id");
	String device_id = request.getParameter("device_id");
	String gw_type = request.getParameter("gw_type");
	//配置文件是否从本地上传的还是从设备上备份下来的 isFromLocal=true为本地上传 isFromLocal=false为设备上备份
	String isFromLocalStr = request.getParameter("isFromLocal");
	if (null == isFromLocalStr) {
		isFromLocalStr = "false";
	}
	boolean isFromLocal = Boolean.valueOf(isFromLocalStr).booleanValue();
	String file_path = "";
	if("4".equals(gw_type)){
		System.out.println("getFilePath_stb");
		file_path = versionManage.getFilePath_stb("file_path","",device_id,isFromLocal);
	}
	else{
		file_path = versionManage.getFilePath_2("file_path",devicetype_id,device_id,isFromLocal);
	}
%>
<html>
<body>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("filepath").innerHTML = "<%=file_path%>";
</SCRIPT>
</body>
</html>