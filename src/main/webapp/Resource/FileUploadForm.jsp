<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
function doUpload(){
	if(document.frm.file1.value == ""){
		alert("请先选择CSV文件！");
		return false;
	}
	if (document.frm.file1.value.indexOf('.csv') == -1){
		alert('请选择csv格式的文件！');
		return false;
	}
	
	idWait.innerHTML = "正在上传......";
	document.frm.action = "../servlet/UploadFile";
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
<FORM NAME="frm" METHOD=POST ACTION="../servlet/UploadFile"  ENCTYPE="multipart/form-data">
<INPUT TYPE="file" NAME="file1">&nbsp;(规定格式的CSV文件)&nbsp;&nbsp;
<INPUT TYPE="button" value=" 上 传 " onclick="doUpload()" class=btn>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
<INPUT TYPE="hidden" value="<%=request.getParameter("type")%>" name="fname">
</FORM>
</BODY>
</HTML>
