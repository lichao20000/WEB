<%--
Author		: Yan.HaiJian Card NO:5126
Date		: 2006-9-30
Desc		: config flux.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%@ page import ="com.linkage.litms.vipms.flux.FluxPortInfo"%>
<%@ page import ="com.linkage.litms.vipms.flux.PortJudgeAttr"%>
<SCRIPT LANGUAGE="JavaScript">
<%
try{
request.setCharacterEncoding("GBK");
ManagerFluxConfig mfc=new ManagerFluxConfig(request);
String action = request.getParameter("action");
String type= request.getParameter("type");
String device_id = request.getParameter("device_id");
if(null!=action&&"delete".equals(action))
{
	int resultCode = mfc.deleteDeviceFluxConfig();	
	if(0==resultCode)
	{
		out.println("alert(\"取消流量配置成功！\");");
	}
	else if(-1==resultCode)
	{
		out.println("alert(\"参数错误，删除失败！\");");
	}
	else if(-2==resultCode)
	{
		out.println("alert(\"数据库删除失败！\");");
	}
	else
	{
		out.println("alert(\"通知后台失败！\");");
	}
	out.println("this.location.href=\"webtop_ConfigFlux.jsp?device_id="+device_id+"&type="+type+"\";");
}
else
{
	//mfc.setSerial();
	int resultCode=mfc.saveDeviceOperator();
	if(0==resultCode)
	{
		out.println("window.alert(\"设备流量配置完成!\");");
		out.println("parent.window.close();");
	}
	else
	{
		out.println("alert(\"设备没有配置认证口令！\");");
	}	
}
}
catch(Exception e)
{
	e.printStackTrace();
}
%>
</SCRIPT>	




