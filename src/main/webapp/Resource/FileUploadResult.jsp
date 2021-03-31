<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String fileName = request.getParameter("fileName");

String flag = request.getParameter("flag");
String msg = "上传文件失败，请确认是否选择了正确的文件！";
if ("1".equals(flag)){
	msg = "上传文件成功！";
}
%>
<HTML>
<HEAD>
<style>
BODY {
	BACKGROUND-COLOR: #FFFFFF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>
<BODY BGCOLOR="white">
<SPAN id="idMsg"><%=msg %></SPAN>
</BODY>
</HTML>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = "<%=flag%>";

if(flag == 1){
	//parent.document.frm.cmdSend.disabled = false;
	parent.document.frm.filename.value = "<%=fileName%>";
}
//-->
</SCRIPT>