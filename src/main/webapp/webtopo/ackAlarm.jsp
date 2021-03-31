<%@ include file="../timelater.jsp"%>
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
	flag = ms.AckAlarm(alarmList,m_ObserverName);	
	
	DeviceWarnInfo dwi=new DeviceWarnInfo();
	dwi.setOnlyWarnInfo(session);
	dwi.removeAlarmList(Arrays.asList(alarmList),session);
	//使用XMLHTTP处理结果
	//main_control.js中ack_Warn ack_Warn_Result处理方法
	out.println("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
	out.println("<result>");
	out.println(flag);
	out.println("</result>");
%>