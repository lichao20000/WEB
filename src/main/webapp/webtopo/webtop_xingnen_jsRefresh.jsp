<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.*,java.util.*"%>
<%@ page import="com.linkage.litms.common.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能实时显示</title>

</head>

<body onload="loadData()">
<%@ include file="../head.jsp"%>
<%request.setCharacterEncoding("GBK");
			String device_id = request.getParameter("device_id");
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String descr = request.getParameter("descr");
%>

<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">请稍等······</span></td>
	</tr>
</table>
</center>
</div>

<table width="780" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>是否自动刷新：<INPUT TYPE="radio" NAME="isRefresh"
			onclick="changIsRefresh(true)" checked>是 <INPUT TYPE="radio"
			NAME="isRefresh" onclick="changIsRefresh(false)">否&nbsp;&nbsp;
		&nbsp;&nbsp; 刷新频率选择：<INPUT TYPE="radio" NAME="polltime" value="3"
			onclick="refreshIframe(this.value)">3分钟&nbsp;&nbsp; <INPUT
			TYPE="radio" NAME="polltime" value="5" checked
			onclick="refreshIframe(this.value)">5分钟&nbsp;&nbsp; <INPUT
			TYPE="radio" NAME="polltime" value="10"
			onclick="refreshIframe(this.value)">10分钟&nbsp;&nbsp; <INPUT
			TYPE="radio" NAME="polltime" value="15"
			onclick="refreshIframe(this.value)">15分钟&nbsp;&nbsp; <INPUT
			TYPE="radio" NAME="polltime" value="30"
			onclick="refreshIframe(this.value)">30分钟</td>
	</tr>
	<tr>
		<td width="100%%"><iframe id="childframe"
			style="overflow:auto;width:790px;height:600px;"></iframe></td>
	</tr>
</table>

<script language="javascript">
var isRefresh = true;
var reloadtime = getReloadtime();

function getReloadtime(){
	var o = document.all('polltime');
	for(var i=0;i<o.length;i++){
		if(o.polltime[i].checked){
			return o.polltime[i].value;
			break;
		}
	}
	return 5;
}

function loadData(){
	document.all("childframe").src = "webtop_xinnenlist.jsp?device_id=<%=device_id%>&descr=<%=descr%>&id=<%=id%>&name=<%=name%>&polltime="+reloadtime;
	showMsgDlg();
}

function changIsRefresh(v){
		isRefresh = v;
		var tem_v = getReloadtime();
		refreshIframe(tem_v);
}

function refreshIframe(v){
	//用户选择刷新时
	//alert("reloadtime in with isRefresh "+isRefresh+" and reloadtime "+v);
	
	if(isRefresh==true){
		page = document.all("childframe").src;
		pos = page.indexOf("&polltime=");
		tmp = page.substring(0,pos);
		page = tmp + "&polltime="+ v+"&tt="+new Date().getTime();
		//alert("page "+page)
		//showMsgDlg();
		document.all("childframe").src = page;
		showMsgDlg();
	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}
</script>
</body>
</html>
