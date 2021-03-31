<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//构造上传Bean
Upload  fupload = new Upload();
//初始化上传Bean
fupload.initialize(pageContext);
//设置上传文件大小
fupload.setMaxFileSize(1024*1024*10);
//上传数据
fupload.upload();
String fname = fupload.getRequest().getParameter("fname");

//java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);
Date nowc = new Date();
fname = ""+nowc.getTime()+".doc";
%>
<%@page import="com.linkage.litms.common.upload.Upload"%>
<%@page import="com.linkage.litms.common.upload.File"%>
<%@page import="java.util.Date"%>
<HTML>
<HEAD>
<style>
BODY {
	BACKGROUND-COLOR: #FFFFFF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>
<BODY BGCOLOR="white">
<%
String msg="";
boolean isSuccess = false;
try{
	int count=0; 
	for(int i=0;i<fupload.getFiles().getCount();i++){
		File file = fupload.getFiles().getFile(i);
		if(!file.isMissing()){
			//fname = file.getFileName();
			file.saveAs("/temp/"+fname,1);
			count++;
		}
	}
	msg += count +" 个文件上传成功";
	isSuccess = true;
}
catch(Exception e){
	out.println(e.toString());
	msg = "上传文件失败";
}
%>
<SPAN id="idMsg"><%=msg%></SPAN>
</BODY>
</HTML>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(eval("<%=isSuccess%>")){
	//parent.document.frm.cmdSend.disabled = false;
	parent.document.frm.filename.value = "<%=fname%>";
}
//-->
</SCRIPT>