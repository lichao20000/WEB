<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramManage" scope="request" class="com.linkage.litms.paramConfig.ParamManage"/>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

UserRes current_user = (UserRes) session.getAttribute("curUser");
long user_id = current_user.getUser().getId();

String[] device_list = request.getParameterValues("device_id");
//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfo=null;
if("4".equals(request.getParameter("gw_type"))){
	deviceInfo= act.getStbDeviceInfo(device_list[0]);
}else{
	deviceInfo= act.getDeviceInfo(device_list[0]);
}
String strGatherId = (String)deviceInfo.get("gather_id");
String devicetype_id = (String)deviceInfo.get("devicetype_id");

String execu_type = request.getParameter("excute_type");
/**
if("2".equals(execu_type))
{
	boolean operResult =versionManage.isAllowConfig(request);
	if(!operResult)
	{
		response.sendRedirect("./configrecover.jsp?operResult=notAllow");
		return;
	}
}
**/
//String strGatherId = request.getParameter("gather_id");


//----------------------------配置恢复参数数组-----------------------------------------------

String keyword = request.getParameter("keyword"); //关键字
String filetype = request.getParameter("filetype"); //文件类型
String[] file_path2 = request.getParameter("file_path").split("\\|");
String file_path = file_path2[0]; //文件路径
String username = request.getParameter("username"); //用户名
String passwd = request.getParameter("passwd"); //密码
String file_size = request.getParameter("file_size"); //文件大小
String filename = request.getParameter("filename"); //文件名称
String delay_time = request.getParameter("delay_time"); //延时
String sucess_url = request.getParameter("sucess_url"); //成功URL
String fail_url = request.getParameter("fail_url"); //失败URL
String gw_type = request.getParameter("gw_type");  //区分ITMS和BBMS  add zhangsb3

boolean result = paramManage.doStrategy(user_id,device_list, "0", file_path, 
				username, passwd, delay_time, filename, 2,gw_type);

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
