<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<%
String[] device_id_Arr = request.getParameterValues("device_id");

DevPerDef dpd = new DevPerDef(request);

for(int i = 0; i < device_id_Arr.length; i++) {
	int retflag = dpd.actionPerformedOne(device_id_Arr[i]);
	if (retflag >=0) {
		out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
		out.println("window.alert(\"性能定义成功,系统后台开始调用PMEE！\");");
		out.println("parent.window.location.reload();");
		out.println("</SCRIPT>");
		//PmeeInterface.GetInstance().readDevices(device_id_arr);
		dpd.actionPerformedV2(device_id_Arr[i]);
	}
	else {
		out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
		out.println("window.alert(\"性能定义操作失败，请重新操作!\");");
		out.println("</SCRIPT>");
	}
}

%>
