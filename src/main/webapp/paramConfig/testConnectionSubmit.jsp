<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");


// 测试连接  成功：1 失败： 0
FileSevice  fileSevice = new  FileSevice();
int int_flag  = fileSevice.testConnection(request);

String flag = null;

if(int_flag == 1){
	flag = "此设备连接成功！";
}else if (int_flag == 0){
	flag = "发生未知连接错误！";
}
else if (int_flag == -1){
	flag = "设备连接不上！";
}
else if (int_flag == -2){
	flag = "设备参数为空！";
}
else if (int_flag == -3){
	flag = "设备正被操作！";
}
else if (int_flag == -4){
	flag = "未知错误原因！";
}
else {
	flag = "发生未知连接错误！";
}
//System.out.println("----"+flag);
%>
<%=flag%>