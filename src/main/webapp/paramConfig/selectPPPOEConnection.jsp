<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import ="com.linkage.litms.paramConfig.WANConnDeviceAct"%>
<%@ include file="../head.jsp"%>
<script type="text/javascript">
<!--
function iframeAutoFit(){
	try{
		if(window!=parent){
			var a = parent.document.getElementsByTagName("IFRAME");
			for(var i=0; i<a.length; i++){
				if(a[i].contentWindow==window){
					var h1=0, h2=0;
					a[i].parentNode.style.height = a[i].offsetHeight +"px";
					a[i].style.height = "10px";
					if(document.documentElement&&document.documentElement.scrollHeight)
						h1=document.documentElement.scrollHeight;
					if(document.body) h2=document.body.scrollHeight;
					var h=Math.max(h1, h2);
					if(document.all) {h += 4;}
					if(window.opera) {h += 1;}
					a[i].style.height = a[i].parentNode.style.height = h +"px";
				}
			}
		}
	}
	catch (ex){}
}
if(window.attachEvent)
	window.attachEvent("onload",  iframeAutoFit);
else if(window.addEventListener)
	window.addEventListener('load',  iframeAutoFit,  false);
//-->
</script>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
String strTable = WANConnDeviceAct.getPPPOEConnectionAttributeTale(device_id,gather_id,ior);
%>
<form target="childFrm2" method=post action="setPPPOEConnectionParam.jsp" onsubmit="return CheckForm()">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" align=center valign=middle bgcolor="#999999">
	<TR>
		<TH align="center" colspan=3>请选择WAN连接创建PPPOE</TH>
	</TR>
	<TR bgcolor="#ffffff">
		<TD align=center valign=middle class=column colspan=3>
			<%=strTable%>
		</TD>
	</TR>
	<TR bgcolor="#ffffff">
		<TD class=column nowrap>
			连接类型:<select name="ConnectionType" id="ConnectionType"><option value="IP_Routed" checked>IP_Routed</option><option value="PPPoE_Bridged" checked>PPPoE_Bridged</option></select>
		</TD>
		<TD class=column>
			帐号:<input type="text" name="Username" id="Username" value="" class="form_kuang">
		</TD>
	</TR>
	<TR bgcolor="#ffffff" nowrap>
		<TD class=column nowrap>
			连接名称:<input type="text" name="Name" id="Name" value="" class="form_kuang">
		</TD>
		<TD class=column nowrap>
			密码:<input type="password" name="Password" id="Password" value="" class="form_kuang">
		</TD>
	</TR>
	<TR bgcolor="#ffffff" nowrap>
		<TD class=column nowrap>
			是否启用:<select name="Enable" id="Enable"><option value=1 checked>是</option><option value=0>否</option></select>
		</TD>
		<TD class=column nowrap>
			<input type="submit" value="确定">			
		</TD>
	</TR>
	<TR bgcolor="#ffffff">
		<TD colspan=3 class=foot align="right">&nbsp;</TD>
	</TR>
</TABLE>
<input type=hidden name="device_id" value="<%=device_id%>" >
<input type=hidden name="gather_id" value="<%=gather_id%>" >
<input type=hidden name="wanconnection" value="">
</form>
<iframe id="childFrm2" name="childFrm2" frameborder="0" width="0" style="display:none"></iframe>
<SCRIPT LANGUAGE="JavaScript">
<!--
var device_id = "<%=device_id%>";
var gather_id = "<%=gather_id%>";
var $E = document.getElementById;
var oldTdObj = null;
function closePMsgDlg(){
	if(parent.closeMsgDlg != null)
		parent.closeMsgDlg();
}
function showPMsgDlg(){
	if(parent.showMsgDlg != null)
		parent.showMsgDlg();
}
function edit(){
	try{
		var e = event.srcElement.parentElement.parentElement;
		setTdColor(e);
		showPMsgDlg();
		$E("wanconnection").value = e.cells[0].innerHTML;
		/*
		$E("ConnectionType").value = e.cells[1].innerHTML;
		$E("Name").value = e.cells[2].innerHTML;
		$E("Username").value = e.cells[3].innerHTML;
		*/
		//$E("Password").value = e.cells[4].innerHTML;
		var page = "getWANPPPConnectionAttrValue.jsp?device_id="+device_id+"&gather_id="+gather_id+"&tt="+new Date();
		page += "&wanconnection=" + $E("wanconnection").value;
		$E("childFrm2").src = page;
	}catch(e){
		alert("error:" + e.messge);
	}
}
function setWANAttrValue(ConnectionType,Name,Username,Enable){
	try{
		if(oldTdObj == null) return ;
		
		$E("ConnectionType").value = oldTdObj.cells[1].innerHTML = ConnectionType;
		$E("Name").value = oldTdObj.cells[2].innerHTML = Name;
		$E("Username").value = oldTdObj.cells[3].innerHTML = Username;
		$E("Enable").value = Enable;
		oldTdObj.cells[4].innerHTML = (Enable == "1" ?  "是" : "否");
	}catch(e){
		alert("error:" + e.messge);
	}
}
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.style.backgroundColor="#ffffff";
	}
	obj.style.backgroundColor="#f0f0f0";
	oldTdObj = obj;
}
function CheckForm(){
	if($E("ConnectionType").value != "PPPoE_Bridged"){
		if($E("Username").value == "" || $E("Password").value == ""){
			alert("帐号或者密码都不能为空！");
			return false;
		}else if($E("wanconnection").value == ""){
			alert("请选择PPPOE连接");
			return false;
		}
	}

	showPMsgDlg();
	return true;
}
closePMsgDlg();
//-->
</SCRIPT>