<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
	request.setCharacterEncoding("GBK");
	String fileResponse = "";
	String fileAction = request.getParameter("action");
	
	if(fileAction.equals("getDevTypeByDevTypeId"))
	{
		fileResponse = fileManage.getDevTypeByDevTypeId(request);
	}
	else if(fileAction.equals("getVendorInfo"))
	{
		fileResponse = fileManage.getVendorInfo(request);
	}
	else if(fileAction.equals("getModelInfo"))
	{
		fileResponse = fileManage.getModelInfo(request);
	}
	else if(fileAction.equals("getFileMap"))
	{
		Map map = fileManage.getFileMap(request);
		fileResponse = JSONObject.toJSONString(map);
	}
	else if(fileAction.equals("getDevTypeId"))
	{
		fileResponse = fileManage.getDevTypeId(request);
	}
	

	PrintWriter pw = response.getWriter();
	pw.print(fileResponse);
	pw.flush();
	pw.close();
	return;
	
%>