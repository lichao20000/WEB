<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.software.*"%>
<%@ page import="ACS.*"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.common.util.Base64" %>
<%@page import="com.linkage.litms.common.util.ToCode16" %>

<%
request.setCharacterEncoding("GBK");

UserRes current_user = (UserRes) session.getAttribute("curUser");
long user_id = current_user.getUser().getId();

String[] device_list = request.getParameter("deviceIds").split(",");

//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_list[0]);
String strGatherId = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

String keyword = request.getParameter("keyword"); //关键字
String filetype = request.getParameter("filetype"); //文件类型
String file_path = request.getParameter("hid_url_path");//文件路径
String username = request.getParameter("username"); //用户名
String passwd = request.getParameter("passwd"); //密码
String dir_id = request.getParameter("dir_id"); //目录ID


String filename = request.getParameter("filename") + ".log"; //文件名称
//file_path = file_path + "index.jsp?fileName=" +filename+"&userName="+username+"&passwd="+passwd+"&dir_id="+dir_id;
//----######################modify by zhaixf-----------
file_path += "/FILE?";
file_path += ToCode16.encode(Base64.encode("dir_id="+dir_id));

String delay_time = request.getParameter("delay_time"); // 延时

String execu_type = request.getParameter("excute_type");// 执行方式

String gwType = request.getParameter("gwType");   //区分IBMS和BBMS

boolean result = paramManage.doStrategy(user_id,device_list, execu_type, file_path, 
				username, passwd, delay_time, filename, 3 ,gwType);

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
var flag = "<%= result%>";
if(flag == "true")
{
	alert("调用后台成功");
}
else
{
	alert("调用后台失败");
}
</SCRIPT>
