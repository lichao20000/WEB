<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.webtopo.IpBrowserAct"%>
<SCRIPT LANGUAGE="JavaScript">
<%
	IpBrowserAct act = new IpBrowserAct();
	int result =act.addDevices(request);
	if(result==0)
	{
		out.println("alert(\"生成拓扑成功！\");");
	}
	else if(result==-1)
	{
		out.println("alert(\"数据库操作失败!\");");
	}
	else if(result==-2)
	{
		out.println("alert(\"参数错误!\");");
	}
	else if(result==-3)
	{
		out.println("alert(\"通知后台失败\");");
	}
	else if(result==-5)
	{
		out.println("alert(\"设备已存在系统中或系统不支持此设备\");");
	}
	else
	{
		out.println("alert(\"后台操作失败\");");
	}
%>
</script>