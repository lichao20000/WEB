<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<jsp:useBean id="fileService" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%
	request.setCharacterEncoding("GBK");
	String devicetype_id = request.getParameter("softwareversion");
	String selDispStyle = request.getParameter("selDispStyle");
	String gw_type = request.getParameter("gw_type");
	int gwTypeInt = Integer.parseInt(gw_type);
	
	//是否需要用设备ID进行过滤路径
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	//strList = versionManage.getDeviceHtmlUseDeviceVersion(request, needFilter_b);
	String strList = fileService.getDeviceHtml(request,gwTypeInt,selDispStyle,needFilter_b);
	
	//查询升级目标软件版本
	String goal_softwareversion = versionManage.getSoftwareversion(devicetype_id);
	
	//String file_path = versionManage.getVersionFilePath(devicetype_id);
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
	parent.document.all("div_goal_softwareversion").innerHTML = "<%=goal_softwareversion%>";
</SCRIPT>
</body>
</html>