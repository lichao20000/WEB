<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="GetWarnInfo" scope="request" class="com.linkage.litms.uss.GetWarnInfo" />
<%
request.setCharacterEncoding("GBK");
String starttime = request.getParameter("start");
String username = request.getParameter("username");
String customerID = request.getParameter("customerID");

long startTimeLong = Long.parseLong(starttime);
long endTimeLong = startTimeLong + 24 * 60 * 60;

//²éÑ¯Êý¾Ý¿â
String html = GetWarnInfo.getWarnInfo(customerID, username, startTimeLong, endTimeLong);

%>
<div id="statDIV">
	<%= html%>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.statisticDIV.innerHTML = statDIV.innerHTML;
parent.closeMsgDlg();
//alert(parent.document.body.scrollHeight);
//parent.document.body.scrollHeight =document.statDIV.height;

//parent.changParentHeight();
//parent.wsState();
//-->
</SCRIPT>

