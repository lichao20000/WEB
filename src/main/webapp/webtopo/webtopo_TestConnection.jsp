<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");
// 测试连接  成功：1 失败： 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);

%>
<script language="JavaScript">
<!--
var flag = "<%=int_flag%>";
function init(){
	if(flag == 1){
		alert("此设备连接成功！");
	}else if (flag == 0){
		alert("发生未知连接错误！");
	}
	else if (flag == -1){
		alert("设备连接不上！");
	}
	else if (flag == -2){
		alert("设备参数为空！");
	}
	else if (flag == -3){
		alert("设备正被操作！");
	}
	else if (flag == -4){
		alert("未知错误原因！");
	}
	else {
		alert("发生未知连接错误！");
	}
}
//-->
</script>
<html>

</html>
<script language="JavaScript">
init();
</script>