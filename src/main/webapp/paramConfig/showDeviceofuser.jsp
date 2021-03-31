<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<jsp:useBean id="ParamTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%
	request.setCharacterEncoding("GBK");
	
	String flag = request.getParameter("flag");
	//是否需要用设备ID进行过滤路径
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	String strList= null;
	if(flag!=null && flag.equals("paramInstanceadd_Config")){
		strList = ParamTreeObject.getDeviceHtml(request,needFilter_b);
	}else if(flag!=null && flag.equals("getoui")){
	    strList = versionManage.getDeviceHtmlUseOUI(request, needFilter_b);
	 
	}else{
		strList = versionManage.getDeviceHtmlUseDeviceVersionforSearch(request, needFilter_b);
	}
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>