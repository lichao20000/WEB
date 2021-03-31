<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<SCRIPT LANGUAGE="JavaScript">
<%
String task_name= request.getParameter("task_name");
String type = request.getParameter("type");

//ÉóºËÈÎÎñ
versionManage.checkAutoUpdateTask(request);

out.println("alert('"+task_name+"ÒÑÉóºË£¡');");
%>
window.location.href="./checkAutoUpdateTask.jsp?type="+<%=type%>+"&action=check";
</SCRIPT>

