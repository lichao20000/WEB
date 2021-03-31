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
String strList = WANConnDeviceAct.getWANConnctionListTable(device_id,gather_id,ior);
%>
<TABLE border=0 cellspacing=1 cellpadding=2 width="90%" align=center valign=middle bgcolor="#999999">
	<TR>
		<TH align="center">请选择WAN连接创建PPPOE</TH>
	</TR>
	<TR bgcolor="#ffffff">
		<TD align=center><%=strList%></TD>
	</TR>
	<TR bgcolor="#ffffff">
		<TD class=foot align="right">&nbsp;</TD>
	</TR>
</TABLE>
<iframe id="childFrm2" id="childFrm2" frameborder="0" width="0" style="display:none"></iframe>
<SCRIPT LANGUAGE="JavaScript">
<!--
var device_id = "<%=device_id%>";
var gather_id = "<%=gather_id%>";
var oldTdObj = null;
function closePMsgDlg(){
	if(parent.closeMsgDlg != null)
		parent.closeMsgDlg();
}
function Sel(value){
	if(value == -1) return ;
	if(confirm("确定要创建PPPOE连接吗？")){
		document.all("childFrm2").src = "createPPPOEConnection.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&wanconnection="+ value +"&t=" + new Date()
	}
}
function create(v){
	var path = null;
	try{
		var e = event.srcElement.parentElement.parentElement;
		path = e.cells[0].innerHTML;
	}catch(e){
		alert(e.message);
	}
	if(v == 0){
		/*
		if(confirm("该连接已经创建过PPPOE连接，确定需要再创建吗？")){
			
		}
		*/
		alert("该连接已经创建过PPPOE连接!");
		return false;
	}else{
		setTdColor(e);
		if(confirm("确定在该连接上创建PPPOE连接吗？")){
			document.all("childFrm2").src = "createPPPOEConnection.jsp?device_id=" + device_id + "&gather_id=" + gather_id + "&wanconnection="+ path +"&t=" + new Date()
		}
	}
}
function setTdColor(obj){
	if(oldTdObj != null){
		oldTdObj.style.backgroundColor="#ffffff";
	}
	obj.style.backgroundColor="#f0f0f0";
	oldTdObj = obj;
}
function resetSelect(){
	var obj = document.getElementById("wanconnection");
	obj.selectedIndex = 0;
}
closePMsgDlg();
//-->
</SCRIPT>