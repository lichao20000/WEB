<%--
	此页面用作给网元画告警数量
	增加链路
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="RemoteDB.AlarmNum" %>
<%@ page import="com.linkage.litms.webtopo.Scheduler" %>
<%
request.setCharacterEncoding("GBK");
String linkid = request.getParameter("curLayerObjList");
String [] linkArr = null;
if(linkid != null){
	linkArr = linkid.split(",");
}
String areaid = null;

String userDomain= user.getAccount();
if(userDomain != null && userDomain.equals("admin"))
	areaid = "0";
else
	areaid = Long.toString(user.getAreaId());

Scheduler scheduler = new Scheduler();

String[] arr = (String[])session.getAttribute("allobjs");
String strWarnNum = "";
if(arr != null && arr.length > 0){
	//获取告警数量
	AlarmNum[] alarmNumList = scheduler.getAllAlarmNum(areaid,arr);
	StringBuffer sb = new StringBuffer(200);
	for(int i = 0;i < alarmNumList.length; i++){
		if(i==0){
			sb.append(alarmNumList[i].id).append(",").append(alarmNumList[i].number);
			sb.append(",").append(alarmNumList[i].level);
		}
		else{
			sb.append(";").append(alarmNumList[i].id).append(",").append(alarmNumList[i].number);
			sb.append(",").append(alarmNumList[i].level);
		}
	}
	alarmNumList = null;
	strWarnNum = sb.toString();
	sb = null;
}
else{
}
scheduler = null;
%>
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<SCRIPT LANGUAGE="JavaScript">
<!--
function start(){
	//给main_control.js中idRefreshAlarmNumMain赋值
	parent.idRefreshAlarmNumMain = idRefreshAlarmNum.innerHTML;
	parent.removeAllWarnMain();//main_control.js
	//画告警数量，并且给链路着告警红色
	parent.refreshAlarmNumMain();
}
//-->
</SCRIPT>
</HEAD>
<BODY onload="start();">
<span id=idRefreshAlarmNum><%=strWarnNum%></span>
</BODY>
</HTML>
