<%@ include file="../timelater.jsp"%>
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
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
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
	
	//���ļ��е��������
	int result =act.fileAddServicecode(fileList);
	if(result==0)
	{
		msg="�ļ��ϴ��ɹ������Ҳ�ɹ�";
	}
	else if(result==-1)
	{
		msg="�ļ��ϴ��ɹ������ļ���ʽ����ȷ";
	}
	else
	{
		msg="�ļ��ϴ��ɹ��������ʧ��";
	}
	isSuccess = true;
}
catch(Exception e){
	e.printStackTrace();
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
	// parent.document.frm.filename.value = "<%=fname%>";
}
//-->
</SCRIPT>