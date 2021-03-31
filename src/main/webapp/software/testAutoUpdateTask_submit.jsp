<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%
boolean result =versionManage.testAutoUpdateTask(request);
String task_id = request.getParameter("task_id");
String task_name= request.getParameter("task_name");
String type= request.getParameter("type");
if(result)
{
	response.sendRedirect("./testAutoUpdateTaskDetail.jsp?task_id="+task_id+"&task_name="
			+task_name+"&type="+type+"&result="+result);
	return;
}
%>
<SCRIPT LANGUAGE="JavaScript">
window.location.href="./testAutoUpdateTask.jsp?task_id=<%=task_id%>&task_name=<%=task_name%>&type=<%=type%>&result=<%=result%>";
</SCRIPT>