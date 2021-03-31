<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="session" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");

String[] m_ProcessList = request.getParameterValues("process");
String m_AreaId = request.getParameter("area_id");
DeviceAct.delGathersWithAreaId(m_AreaId);
boolean flag = DeviceAct.saveGathers(m_ProcessList,m_AreaId);
out.println("<script language=javascript>");
out.println("parent.saveGathersResult("+ flag +");");
out.println("</script>");
%>

