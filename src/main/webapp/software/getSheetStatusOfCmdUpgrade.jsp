<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.software.CmdUpgrade"%>

<%
request.setCharacterEncoding("GBK");
String sheetIds = request.getParameter("sheetId");
String[] sheetArr = sheetIds.split(",");
String result = CmdUpgrade.getStatusOfSheet(sheetArr);
//���豸id�Ѿ�javascript�����ӡ��ҳ����
out.println(result);
%>