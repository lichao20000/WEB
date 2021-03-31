<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCDataSource,
				  com.linkage.litms.webtopo.warn.DeviceWarnInfo"%>
<%
	request.setCharacterEncoding("GBK");
	String alarmStr = request.getParameter("alarmStr");
	String m_ObserverName	   = user.getAccount();
	MCDataSource ms=new MCDataSource(user.getAccount(),user.getPasswd());
	String[] alarmList=null;
	if(alarmStr!=null)
	{	
		alarmList=alarmStr.split(",");
	}


	int flag=-1;
	flag = ms.RemoveAlarm(alarmList,m_ObserverName);	

	DeviceWarnInfo dwi=new DeviceWarnInfo();
	dwi.setOnlyWarnInfo(session);
	dwi.removeAlarmList(Arrays.asList(alarmList),session);

%>

<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<SCRIPT LANGUAGE="JavaScript">
<!--
	
	var flag="<%=flag%>";
	if(flag=="1")
	{
		parent.document.all("oList").deleteRow();
		alert("删除告警成功");
	}
	else
	{
		alert("删除告警失败，请重试！");
	}

-->
</SCRIPT>
</head>
<body>

</body>
</html>