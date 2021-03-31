<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.paramConfig.warnAction" %>

<%
warnAction warnAct = new warnAction();

int result = 0;
String isBatch = (String)request.getParameter("isBatch");

if ("1".equals(isBatch)){
	result = warnAct.changeStatusBatch(request);
}
else{
	result = warnAct.changeStatus(request);
}

String action = request.getParameter("action");
%>

<script language="javascript">

var result = "<%=result%>";
var action = "<%=action%>";

if (action == "clean"){
	if (result != "null"){
		if (result == 1){
			alert('告警清除成功');
			parent.reloadForm();
		}
		else{
			alert('告警清除失败');
		}
	}
	else{
		alert('告警清除失败');
	}
}
else if (action == "confirm"){
	if (result != "null"){
		if (result == 1){
			alert('告警确认成功');
			parent.reloadForm();
		}
		else{
			alert('告警确认失败');
		}
	}
	else{
		alert('告警确认失败');
	}
}
else if (action == "cancelConfirm"){
	if (result != "null"){
		if (result == 1){
			alert('告警取消确认成功');
			parent.reloadForm();
		}
		else{
			alert('告警取消确认失败');
		}
	}
	else{
		alert('告警取消确认失败');
	}
}

else{
	alert('未知操作');
}

</script>
