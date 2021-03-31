<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");
	//查询升级目标软件版本
	String type = (String)request.getParameter("type");
	if(type == null){
		type = "1";
	}
	if("1".equals(type)){
		String goal_softList = versionManage.getSoftwareversion();
		out.println(goal_softList);
	}

%>
