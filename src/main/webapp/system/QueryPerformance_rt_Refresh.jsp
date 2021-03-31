<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.*,java.util.*" %>
<%@ page import= "com.linkage.litms.common.util.*"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.litms.common.database.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>实时数据显示</title>

</head>

<body onload="loadData()">
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<% 
  	request.setCharacterEncoding("GBK");

	String curCity_ID = curUser.getCityId();
	String curGather_ID = "-1";
	Cursor cursorTmp = DataSetBean.getCursor("select gather_id,descr from tab_process_desc where city_id = '" + curCity_ID +"'");
	Map fieldTmp = cursorTmp.getNext();
	if (fieldTmp != null){
		curGather_ID = (String)fieldTmp.get("gather_id");
	}
	String strGatherList = DeviceAct.getGatherList(session,curGather_ID,"",true);	

%>
<form name="frm">
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


<table width="95%" border="0" cellspacing="0" cellpadding="0" align=center>
  <tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>
  <tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						主机性能实时监控
					</td>
				</tr>
			</table>
			</td>
		</tr>
  <tr>
  	<td>
  		<table width="100%" border="0" cellspacing="1" cellpadding="2" align=center bgcolor=#999999>
  			<tr>
				<TH>是否自动刷新：<INPUT TYPE="radio" NAME="isRefresh" onclick="changIsRefresh(true)" checked>是
				<INPUT TYPE="radio" NAME="isRefresh" onclick="changIsRefresh(false)">否&nbsp;&nbsp; &nbsp;&nbsp; 
					刷新频率选择：<INPUT TYPE="radio" NAME="polltime" value="3" onclick="refreshIframe(this.value)">3分钟&nbsp;&nbsp; 
							   <INPUT TYPE="radio" NAME="polltime" value="5" checked onclick="refreshIframe(this.value)">5分钟&nbsp;&nbsp; 
							   <INPUT TYPE="radio" NAME="polltime" value="10" onclick="refreshIframe(this.value)">10分钟&nbsp;&nbsp; 
							   <INPUT TYPE="radio" NAME="polltime" value="15" onclick="refreshIframe(this.value)">15分钟&nbsp;&nbsp; 
							   <INPUT TYPE="radio" NAME="polltime" value="30" onclick="refreshIframe(this.value)">30分钟</TH>
  			</tr>
  			<tr>
				<td align=center bgcolor=#ffffff>采集点：<%=strGatherList%>&nbsp;&nbsp;主机IP：<span id='divHostIP'>－请选择采集点－</span></td>
  			</tr>
  			<tr>
				<td width="100%%"  align=center bgcolor=#ffffff>
		 			<iframe id="childframe" FRAMEBORDER=0 style="overflow:auto;width:95%;height:400px"></iframe>
					<iframe id="childFrm" style="display:none"></iframe>
				</td>
 			 </tr>
 		</table>
 	</td>
 </tr>
</table>
</form>
<%@ include file="../foot.jsp"%>

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
	document.all("childframe").src = "getStatusHTML.jsp?hostip="+document.frm.hostip.value+"&polltime="+reloadtime+"&gather_id="+document.frm.gather_id.value+"&refresh="+Math.random();
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
		pos = page.indexOf("?");
		tmp = page.substring(0,pos);
		page = tmp + "?hostip="+document.frm.hostip.value+"&polltime="+ v+"&gather_id="+document.frm.gather_id.value+"&tt="+new Date().getTime();
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

function showChild(param)
{
	
	var page ="";
	if(param == "gather_id")
	{
		page = "getHostIPList.jsp?gather_id="+document.all.gather_id.value+"&refresh="+Math.random();
		document.all("childFrm").src = page;
	}
	if(param == "hostip")
	{
		refreshIframe(getReloadtime());
	}
}

showChild("gather_id");
</script>
</body>
</html>
