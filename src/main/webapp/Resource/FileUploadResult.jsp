<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String fileName = request.getParameter("fileName");

String flag = request.getParameter("flag");
String msg = "�ϴ��ļ�ʧ�ܣ���ȷ���Ƿ�ѡ������ȷ���ļ���";
if ("1".equals(flag)){
	msg = "�ϴ��ļ��ɹ���";
}
%>
<HTML>
<HEAD>
<style>
BODY {
	BACKGROUND-COLOR: #FFFFFF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
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