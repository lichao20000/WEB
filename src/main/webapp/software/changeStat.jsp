<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*" %>

<%
String stat = request.getParameter("stat");
String task_id = request.getParameter("task_id");

String sql = "update tab_device_autoupdate set is_over = " + stat + " where task_id = " + task_id;
int sqlCount = DataSetBean.executeUpdate(sql);

%>

<script language="javascript">
var sqlCount = "<%=sqlCount%>";

if (sqlCount == 1){
	alert("策略任务状态更新成功！");
}
else{
	alert("策略任务状态更新失败！");
}

parent.window.location.reload("autoUpdateTask.jsp?type=3");

</script>