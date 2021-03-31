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
String strTable = WANConnDeviceAct.getWANConnectionAttributeTale(device_id,gather_id,ior);
%>
<form target="childFrm2" method=post action="setWANConnectionParam.jsp" onsubmit="return CheckForm()">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" align=center valign=middle bgcolor="#999999">
		<TR>
		<TH align="center" colspan=4>请选择WAN连接编辑</TH>
	</TR>
	<TR bgcolor="#ffffff">
		<TD align=center valign=middle class=column colspan=4>
			<%=strTable%>
		</TD>
	</TR>
	<TR bgcolor="#ffffff">
		<TD align=center valign=middle class=column>
			连接类型:<select name="LinkType" id="LinkType"><option value="EoA" checked>EoA</option></select>
		</TD>
		<TD align=center valign=middle class=column>
			PVC:<input type="text" name="DestinationAddress" id="DestinationAddress" value="" class="form_kuang">
		</TD>
		<TD align=center valign=middle class=column>
			是否启用:<select name="Enable" id="Enable"><option value=1 checked>是</option><option value=0>否</option></select>
		</TD>
		<TD align=center valign=middle class=column>
			<input type="submit" value="确定">
		</TD>
	</TR>
	<TR bgcolor="#ffffff">
		<TD class=foot align="right" colspan=4>&nbsp;</TD>
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
function showPMsgDlg(){
	if(parent.showMsgDlg != null)
		parent.showMsgDlg();
}
function closePMsgDlg(){
	if(parent.closeMsgDlg != null)
		parent.closeMsgDlg();
}
function edit(){
	try{
		var e = event.srcElement.parentElement.parentElement;
		setTdColor(e);
		showPMsgDlg();
		$E("wanconnection").value = e.cells[0].innerHTML;
		/*
		$E("LinkType").value = e.cells[1].innerHTML;
		$E("DestinationAddress").value = e.cells[2].innerHTML;
		$E("Enable").value = e.cells[3].innerHTML;
		*/
		var page = "getWANConnectionAttrValue.jsp?device_id="+device_id+"&gather_id="+gather_id+"&tt="+new Date();
		page += "&wanconnection=" + $E("wanconnection").value
		$E("childFrm2").src = page

	}catch(e){
		alert("error:" + e.messge);
	}
}
function setWANAttrValue(_LinkType,_DestinationAddress,_Enable){
	try{
		if(oldTdObj == null) return ;
		
		$E("LinkType").value = oldTdObj.cells[1].innerHTML = _LinkType;
		$E("DestinationAddress").value = oldTdObj.cells[2].innerHTML = _DestinationAddress;
		$E("Enable").value = _Enable;
		oldTdObj.cells[3].innerHTML = (_Enable == "1" ?  "是" : "否");
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
	if($E("DestinationAddress").value == ""){
		alert("PVC不能为空！");
		return false;
	}else if($E("wanconnection").value == ""){
		alert("请选择WAN连接");
		return false;
	}
	showPMsgDlg();
	return true;
}

closePMsgDlg();
//-->
</SCRIPT>