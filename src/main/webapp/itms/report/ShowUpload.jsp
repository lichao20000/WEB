<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<% 
response.setHeader("Pragma","No-cache");    
response.setHeader("Cache-Control","no-cache");    
response.setDateHeader("Expires", -10);   
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Struts 2 File Upload</title>
</head>
<body>
	文件
	<s:property value="fileName" />
	上传成功!
	<a href="javascript:reloadUrl();" style="CURSOR: hand">重新上传</a>
</body>
</html>
<SCRIPT LANGUAGE="JavaScript">
	var ss = "<s:property value ="imageFileName"/>";
	if(parent.document.forms[0].fileNames == undefined || parent.document.forms[0].fileNames == null)
	{
		parent.document.getElementsByName("fileNames")[0].value = ss;
	}else
	{
		parent.document.forms[0].fileNames.value = ss;
	}
	

	function reloadUrl(){
		if(parent.document.forms[0].fileNames == undefined || parent.document.forms[0].fileNames == null)
		{
			parent.document.getElementsByName("fileNames")[0].value = "";
		}else
		{
			parent.document.forms[0].fileNames.value = "";
		}
		window.location="FileUpload.jsp";
	}
</SCRIPT>
