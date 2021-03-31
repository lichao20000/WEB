<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.Scheduler"%>
<%@ page import="com.linkage.litms.webtopo.MCControlManager"%>
<%@ include file="../head.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	
	String oper=request.getParameter("oper");
	
	
	int flag=-1;
	if(oper==null)
	{	
		int type=Integer.parseInt(request.getParameter("type"));
		if(type==5){
			VpnScheduler vpnscheduler = new VpnScheduler();
			flag = vpnscheduler.getVPNWebTopoManager().InformSaveTopo(type);
		}else{
			Scheduler scheduler=new Scheduler();
			flag=scheduler.InformSaveTopo(type);
		}
	}
	else
	{
		String[] gathers=request.getParameterValues("process");
		MCControlManager mc=new MCControlManager(user.getAccount(),user.getPasswd());
		flag=mc.NewTopo(2,0,"0",gathers);
	}

%>

<%@page import="com.linkage.litms.webtopo.VpnScheduler"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var flag="<%=flag%>";
	var oper="<%=oper%>";

	if(oper=="null")
	{
		if(flag=="-1")
		{
			window.alert("通知保存拓扑失败！");	
		}
		else
		{
			window.alert("通知保存拓扑成功");	
		}
	}
	else
	{	
		parent.isCall=flag;
	}

 
//-->
</SCRIPT>
