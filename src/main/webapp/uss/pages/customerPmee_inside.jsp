<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="GetPmeeInfo" scope="request" class="com.linkage.litms.uss.GetPmeeInfo" />
<%
request.setCharacterEncoding("GBK");
String starttime = request.getParameter("start");
String username = request.getParameter("username");
String customerID = request.getParameter("customerID");

long startTimeLong = Long.parseLong(starttime);
long endTimeLong = startTimeLong + 24 * 60 * 60;

//²éÑ¯Êý¾Ý¿â
String html = GetPmeeInfo.getPmeeInfo(customerID, username, startTimeLong, endTimeLong);

%>
<div id="statDIV">
	<%= html%>
</div>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.statisticDIV.innerHTML = statDIV.innerHTML;
parent.closeMsgDlg();
//parent.wsState();
//parent.changParentHeight();
//-->
</SCRIPT>

