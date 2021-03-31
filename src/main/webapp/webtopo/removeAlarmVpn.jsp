<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.warn.VpnDeviceWarnInfo"%>
<%
	request.setCharacterEncoding("GBK");
	String alarmStr = request.getParameter("alarmStr");
	String m_ObserverName	   = user.getAccount();
	VpnDeviceWarnInfo dwi=new VpnDeviceWarnInfo();
	dwi.setOnlyWarnInfo(session);
	VpnScheduler vpnScheduler = dwi.getVpnScheduler();//new VpnScheduler();
	RemoteDB.dataSource datasource = vpnScheduler.getDataSource(user.getAccount(),user.getPasswd());
	String[] alarmList=null;
	if(alarmStr!=null)
	{	
		alarmList=alarmStr.split(",");
	}


	int flag=-1;
	try{
	    datasource.RemoveAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
	    flag = 1;
	}catch(Exception ex){
	    ex.printStackTrace();
	    datasource = vpnScheduler.getDataSource(user.getAccount(),user.getPasswd());
	    try{
	        datasource.RemoveAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
	        flag = 1;
	    }catch(Exception e){
		    e.printStackTrace();	        
	    }
	}
	
	dwi.removeAlarmList(alarmList,session);

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
		alert("É¾³ý¸æ¾¯³É¹¦");
	}
	else
	{
		alert("É¾³ý¸æ¾¯Ê§°Ü£¬ÇëÖØÊÔ£¡");
	}

-->
</SCRIPT>
</head>
<body>

</body>
</html>