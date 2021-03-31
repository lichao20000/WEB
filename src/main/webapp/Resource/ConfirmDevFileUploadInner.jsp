<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doUpload(){
	idWait.innerHTML = "正在上传......";
	document.frm.action = "ConfirmDevFileUpload.jsp?refresh="+Math.random();
	document.frm.submit();
}
//-->
</SCRIPT>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>

<BODY>
<FORM NAME="frm" METHOD=POST ACTION="ConfirmDevFileUpload.jsp"  ENCTYPE="multipart/form-data">
<INPUT TYPE="file" NAME="file1">&nbsp;&nbsp;&nbsp;
<INPUT TYPE="button" value=" 上 传 " onclick="doUpload()" class=jianbian>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
<INPUT TYPE="hidden" value="<%=request.getParameter("type")%>" name="fname">
</FORM>
</BODY>
</HTML>
