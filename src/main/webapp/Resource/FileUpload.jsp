<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//�����ϴ�Bean
Upload  fupload = new Upload();
//��ʼ���ϴ�Bean
fupload.initialize(pageContext);
//�����ϴ��ļ���С
fupload.setMaxFileSize(1024*1024*10);
//�ϴ�����
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
	BACKGROUND-COLOR: #FFFFFF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
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
	msg += count +" ���ļ��ϴ��ɹ�";
	isSuccess = true;
}
catch(Exception e){
	out.println(e.toString());
	msg = "�ϴ��ļ�ʧ��";
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