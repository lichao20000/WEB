<%@ include file="../timelater.jsp"%>
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
	    datasource.AckAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
	    if(dwi.removeAlarmList(alarmList,session))
	        flag = 1;
	}catch(Exception ex){
	    ex.printStackTrace();
	    datasource = vpnScheduler.getDataSource(user.getAccount(),user.getPasswd());
	    try{
	        datasource.AckAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
	        if(dwi.removeAlarmList(alarmList,session))
		        flag = 1;
	    }catch(Exception e){
		    e.printStackTrace();	        
	    }
	}
	
	out.println("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
	out.println("<result>");
	out.println(flag);
	out.println("</result>");
%>