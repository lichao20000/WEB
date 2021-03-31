<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.software.CmdUpgrade,java.util.Map"%>

<%
request.setCharacterEncoding("GBK");

Map mapParamValue = request.getParameterMap();
String device_id = request.getParameter("device_id");
String strGatherId = request.getParameter("gather_id");
String object_name="ACS_" + strGatherId;
String object_Poaname="ACS_Poa_" + strGatherId;
String strIor = user.getIor(object_name,object_Poaname);
mapParamValue.remove("device_id");
mapParamValue.remove("tt");
mapParamValue.remove("gather_id");
CmdUpgrade cmdUpgrade = new CmdUpgrade(request);
boolean flag = cmdUpgrade.setParamValue(strGatherId,strIor,device_id,mapParamValue);
//json 格式的数据
out.println("result={'device_id':" + device_id + ",'flag': " + flag + " }");
cmdUpgrade = null;
%>