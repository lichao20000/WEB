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
			alert('�澯����ɹ�');
			parent.reloadForm();
		}
		else{
			alert('�澯���ʧ��');
		}
	}
	else{
		alert('�澯���ʧ��');
	}
}
else if (action == "confirm"){
	if (result != "null"){
		if (result == 1){
			alert('�澯ȷ�ϳɹ�');
			parent.reloadForm();
		}
		else{
			alert('�澯ȷ��ʧ��');
		}
	}
	else{
		alert('�澯ȷ��ʧ��');
	}
}
else if (action == "cancelConfirm"){
	if (result != "null"){
		if (result == 1){
			alert('�澯ȡ��ȷ�ϳɹ�');
			parent.reloadForm();
		}
		else{
			alert('�澯ȡ��ȷ��ʧ��');
		}
	}
	else{
		alert('�澯ȡ��ȷ��ʧ��');
	}
}

else{
	alert('δ֪����');
}

</script>
