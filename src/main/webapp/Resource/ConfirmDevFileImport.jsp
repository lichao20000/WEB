<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />

<%
request.setCharacterEncoding("GBK");
//�����ϴ�Bean
//Upload  fupload = new Upload();
//��ʼ���ϴ�Bean
//fupload.initialize(pageContext);
//�����ϴ��ļ���С
//fupload.setMaxFileSize(1024*1024*10);
//�ϴ�����
//fupload.upload();
String fileName = request.getParameter("fileName");


if (fileName != null && fileName.length() != 0) {
	//fvalue = fvalue.replaceAll("/",File.separator);
	boolean booRes = HGWUserInfoAct.importCSVFile(fileName);
	if (booRes) {
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.idWait.innerHTML = "";
	alert("����ɹ�!");
	
	window.close();
//-->
</SCRIPT>
<%		
	} else {
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.idWait.innerHTML = "";
	alert("����ʧ��!");
//-->
</SCRIPT>
<%
	}
}

%>
















