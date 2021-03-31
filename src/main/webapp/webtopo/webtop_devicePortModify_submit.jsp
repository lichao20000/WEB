<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%
int resultCode =-2;
try
{
	ManagerFluxConfig mfc = new ManagerFluxConfig(request);
	resultCode =mfc.updateJSDevicePortInfo();
}catch(Exception e)
{ 	
	e.printStackTrace();
}
%>
<SCRIPT LANGUAGE="JavaScript">
<%if(0==resultCode){%>
alert("修改成功！");
<%}else if(-1==resultCode){%>
alert("参数错误，修改失败！");
<%}else if(-2==resultCode){%>
alert("数据库删除失败！");
<%}else{%>
alert("通知后台失败！");
<%}%>
</SCRIPT>