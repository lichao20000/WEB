<%--
	��ҳ����������Ԫ���澯����
	������·
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
	//��ȡ�澯����
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
	//��main_control.js��idRefreshAlarmNumMain��ֵ
	parent.idRefreshAlarmNumMain = idRefreshAlarmNum.innerHTML;
	parent.removeAllWarnMain();//main_control.js
	//���澯���������Ҹ���·�Ÿ澯��ɫ
	parent.refreshAlarmNumMain();
}
//-->
</SCRIPT>
</HEAD>
<BODY onload="start();">
<span id=idRefreshAlarmNum><%=strWarnNum%></span>
</BODY>
</HTML>
