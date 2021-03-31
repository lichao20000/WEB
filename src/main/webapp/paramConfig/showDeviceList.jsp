<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");

	String devicetype_id = request.getParameter("devicetype_id");
	//是否需要用设备ID进行过滤路径
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	//配置文件是否从本地上传的还是从设备上备份下来的 isFromLocal=true为本地上传 isFromLocal=false为设备上备份
	String isFromLocalStr = request.getParameter("isFromLocal");
	if (null == isFromLocalStr) {
		isFromLocalStr = "false";
	}
	boolean isFromLocal = Boolean.valueOf(isFromLocalStr).booleanValue();
	
	String strList= null;
	
	//网关类型
	String gw_type = request.getParameter("gw_type");
	strList = versionManage.getDeviceHtmlUseDeviceVersion(request, needFilter_b);
	String inst_area = LipossGlobals.getLipossProperty("InstArea.ShortName");
	String file_path = "";
	if(inst_area.equals("nx_dx"))
	{
		 file_path = versionManage.getFilePath_3("file_path",devicetype_id,"",isFromLocal,gw_type);
		
	}
	else
	{
		 file_path = versionManage.getFilePath_2("file_path",devicetype_id,"",isFromLocal);
	} 
	//String file_path = versionManage.getFilePath_3("file_path",devicetype_id,"",isFromLocal,gw_type);
	
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
	//if(!<%=needFilter_b%>){
		parent.document.all("filepath").innerHTML = "<%=file_path%>";
	//}
</SCRIPT>
</body>
</html>