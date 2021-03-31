<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<link href="<s:url value="/css/inmp/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<html xmlns ="http://www.w3.org/1999/xhtml"> 
<head> 
    <title> Struts 2 File Upload </title> 
</head> 
<body> 
   文件<s:property value ="fileName" />上传成功!
   <a href="javascript:reloadUrl();" style="CURSOR:hand">重新上传</a>
</body>
</html>
<SCRIPT LANGUAGE="JavaScript">
	var ss = "<s:property value ="imageFileName"/>";
	if(parent.document.forms[0].gwShare_fileName == undefined || parent.document.forms[0].gwShare_fileName == null)
	{
		parent.document.getElementsByName("gwShare_fileName")[0].value = ss;
	}else
	{
		parent.document.forms[0].gwShare_fileName.value = ss;
	}
	

	function reloadUrl(){
		if(parent.document.forms[0].gwShare_fileName == undefined || parent.document.forms[0].gwShare_fileName == null)
		{
			parent.document.getElementsByName("gwShare_fileName")[0].value = "";
		}else
		{
			parent.document.forms[0].gwShare_fileName.value = "";
		}
		window.location="FileUpload.jsp";
	}
</script>