<%@ include file="../timelater.jsp"%>
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
fname = ""+nowc.getTime()+".csv";
%>
<%@page import="com.linkage.litms.common.upload.Upload"%>
<%@page import="java.util.Date"%>
<%@page import="com.linkage.litms.netcutover.ServiceAct"%>
<%@page import="java.util.ArrayList" %>
<HTML>
<HEAD>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>
<BODY BGCOLOR="white">
<%
String msg="";

boolean isSuccess = false;
ServiceAct act = new ServiceAct();
try{
	int count=0; 
	ArrayList fileList = new ArrayList();
	for(int i=0;i<fupload.getFiles().getCount();i++){
		com.linkage.litms.common.upload.File file = fupload.getFiles().getFile(i);
		if(!file.isMissing()){
			file.saveAs("/temp/"+fname,1);
			fileList.add(fname);
			count++;			
		}
	}
	
	//把文件中的数据入库
	int result =act.fileAddServicecode(fileList);
	if(result==0)
	{
		msg="文件上传成功，入库也成功";
	}
	else if(result==-1)
	{
		msg="文件上传成功，但文件格式不正确";
	}
	else
	{
		msg="文件上传成功，但入库失败";
	}
	isSuccess = true;
}
catch(Exception e){
	e.printStackTrace();
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
	// parent.document.frm.filename.value = "<%=fname%>";
}
//-->
</SCRIPT>