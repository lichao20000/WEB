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
String filePath = "";
boolean isSuccess = false;
try{
	int count=0; 
	for(int i=0;i<fupload.getFiles().getCount();i++){
		com.linkage.litms.common.upload.File file = fupload.getFiles().getFile(i);
		if(!file.isMissing()){
			file.saveAs("/temp/"+fname,1);
			count++;
		}
		//filePath = file.getFilePathName();
	}
//	filePath = request.getRequestURL().toString();
	msg += count +" ���ļ��ϴ��ɹ�";
	isSuccess = true;
}
catch(Exception e){
	e.printStackTrace();
	out.println(e.toString());
	msg = "�ϴ��ļ�ʧ��";
}
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function doImport(){
	idWait.innerHTML = "���ڵ���......";
	var page = "ConfirmDevFileImport.jsp?refresh="+Math.random()+"&fileName="+"<%=fname%>";
	document.all("childFrm").src=page;
	//document.frm.submit();
}
//-->
</SCRIPT>

<SPAN id="idMsg"><%=msg%></SPAN>
<INPUT TYPE="button" value=" �����ļ� " onclick="doImport()" class=jianbian>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
<TABLE>
<TR><TD HEIGHT=20><IFRAME ID=childFrm name=childFrm SRC="" STYLE="display:"></IFRAME>&nbsp;</TD></TR>
</TABLE>
</BODY>
</HTML>















