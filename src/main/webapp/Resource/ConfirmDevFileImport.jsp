<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />

<%
request.setCharacterEncoding("GBK");
//构造上传Bean
//Upload  fupload = new Upload();
//初始化上传Bean
//fupload.initialize(pageContext);
//设置上传文件大小
//fupload.setMaxFileSize(1024*1024*10);
//上传数据
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
	alert("导入成功!");
	
	window.close();
//-->
</SCRIPT>
<%		
	} else {
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.idWait.innerHTML = "";
	alert("导入失败!");
//-->
</SCRIPT>
<%
	}
}

%>
















