<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
//begin add by w5221 �޸İ����û���ѯû�вɼ�����Ϣ�Ĳ���
//�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
String gather_id = (String)deviceInfo.get("gather_id");
//end add by w5221 �޸İ����û���ѯû�вɼ�����Ϣ�Ĳ���

String dev_serial = request.getParameter("dev_serial");
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var oldTdObj = null;
var device_id = "<%=device_id%>";
var gather_id = "<%=gather_id%>";
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.className="";
	}
	obj.className="trOver";
	oldTdObj = obj;
}
function CreateWANConn(obj){
	if(!confirm("ȷ��Ҫ����WAN������")) return 
	showMsgDlg();
	document.all("childFrm").src = "createWANConnection.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&t=" + new Date();
	setTdColor(obj);
}
function SetWANConnParam(obj){
	showMsgDlg();
	document.all("childFrm").src = "selectWANConnection.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&t=" + new Date();
	setTdColor(obj);
}
function CreatePPPOEConn(obj){
	showMsgDlg();
	document.all("childFrm").src = "selectWANConnCreatPPPConn.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&t=" + new Date();
	setTdColor(obj);
}
function SetPPPOEConnParam(obj){
	showMsgDlg();
	document.all("childFrm").src = "selectPPPOEConnection.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&t=" + new Date();
	setTdColor(obj);
}
function showMsgDlg(){
	var w = document.body.clientWidth;
	var h = document.body.clientHeight;

	var l = (w-250)/2;
	var t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
//-->
</SCRIPT>
<br>
<table width="95%" height="30" border="0" cellspacing="0" align="center" cellpadding="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">
			�ֹ���ͨ
		</td>
		<td>
			<img src="../images/attention_2.gif" width="15" height="12">
			������ѡ���ܽ��п�ͨ����(��ǰ�������豸��<%=dev_serial%>)
		</td>
	</tr>
</table>
<table width="95%" border=0 cellspacing=1 cellpadding=0 align="center" bgcolor="#999999">
	<tr bgcolor="#ffffff" >
		<td onclick="CreateWANConn(this)" width="120" height=25 valign=top><span ><a href="javascript://" >����WAN����</a></span></td>
		<td align=center valign=top rowspan=4>
			<iframe id="childFrm" name="childFrm" frameborder="0" width="100%"></iframe>
		</td>
	</tr>
	<tr bgcolor="#ffffff" height=25>
		<td onclick="CreatePPPOEConn(this)" width="120" valign=top><span><a href="javascript://" >����PPPOE����</a></span></td>
	</tr>
	<tr bgcolor="#ffffff"  height=25>
		<td onclick="SetWANConnParam(this)" width="120" valign=top><span><a href="javascript://" >����WAN���Ӳ���</a></span></td>
	</tr>
	<tr bgcolor="#ffffff" height=25>
		<td onclick="SetPPPOEConnParam(this)" width="120" valign=top><span><a href="javascript://" >����PPPOE����</a></span></td>
	</tr>
</table>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
	<table border="0">
		<tr>
			<td valign="middle"><img src="../images/cursor_hourglas.gif"
				border="0" WIDTH="30" HEIGHT="30"></td>
			<td>&nbsp;&nbsp;</td>
			<td valign="middle"><span id=txtLoading
				style="font-size:14px;font-family: ����">���Եȡ�����������</span></td>
		</tr>
	</table>
</center>
</div>
<%@ include file="../foot.jsp"%>