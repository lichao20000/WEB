<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
	request.setCharacterEncoding("GBK");
	//��ѯ����Ŀ������汾
	String type = request.getParameter("type");
	if(type == null){
		type = "1";
	}
	if("1".equals(type)){
		String deviceId = request.getParameter("device_id");
		String goal_softList = versionManage.getSoftwareversion(deviceId);
		out.println(goal_softList);
	}else{
		String goal_softwareversion = request.getParameter("goal_softwareversion");
		//��ѯ����Ŀ������汾
		String div_path = versionManage.getGoalVersionFilePath(goal_softwareversion);
		out.print(div_path);
	}

%>
