<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
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
<%@ page import ="com.linkage.litms.paramConfig.WANConnDeviceAct"%>

<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
boolean flag = WANConnDeviceAct.createWANConnection(device_id,gather_id,ior);
String strMsg = null;
if(flag){
	strMsg = "创建WAN连接成功.";
}else{
	strMsg = "创建WAN连接失败.";
}
%>
<TABLE border=0 cellspacing=1 cellpadding=2 width="90%" align=center valign=middle bgcolor="#999999">
	<TR>
		<TH align="center">操作提示信息</TH>
	</TR>
		<TR  height="50" bgcolor="#ffffff">
			<TD align=center valign=middle class=column><%=strMsg%></TD>
		</TR>
	<TR bgcolor="#ffffff">
		<TD class=foot align="right">&nbsp;</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.closeMsgDlg();
//-->
</SCRIPT>