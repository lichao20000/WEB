<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>

<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />

<%
	request.setCharacterEncoding("GBK");
	
	String flag = request.getParameter("flag");
	//是否需要用设备ID进行过滤路径
	//String needFilter = request.getParameter("needFilter");
	//if (null == needFilter) {
	//	needFilter = "false";
	//}
	//boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	String strList= null;
	if(flag!=null && flag.equals("user")){
		//根据用户账号来进行查询
		strList = StrategyBean.getDev_user_HTML(request, true);
	}else{
		//根据设备序列号进行查询
		//strList = versionManage.getDeviceHtmlUseDeviceVersion(request, true);
	}
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
	parent.closeMsgDlg();
</SCRIPT>
</body>
</html>