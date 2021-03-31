<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<html xmlns ="http://www.w3.org/1999/xhtml"> 
<head> 
    <title> Struts 2 File Upload </title> 
</head> 
<body> 
   <table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
   <tr>
   		</td>
   </tr>
   <tr>
		<th align="center"><INPUT TYPE="button" NAME="cmdBack" onclick="javascript:reloadUrl();" value="重新上传 " class=btn>
		</th>
	</tr>
	<s:iterator value="nodeList">
	<tr bgcolor="#FFFFFF">
		<td class=column2><s:property value="node"/></td>
	</tr>
	</s:iterator>
</body>


</html>
<SCRIPT LANGUAGE="JavaScript">
	var nodeIds = "<s:property value ="deviceIds"/>";
	
	if(parent.document.forms[0].nodeIds == undefined || parent.document.forms[0].nodeIds == null)
	{
		parent.document.getElementsByName("nodeIds")[0].value = nodeIds;
	}else
	{
		parent.document.forms[0].nodeIds.value = nodeIds;
	}

	function reloadUrl(){
		if(parent.document.forms[0].nodeIds == undefined || parent.document.forms[0].nodeIds == null)
		{
			parent.document.getElementsByName("nodeIds")[0].value = "";
		}else
		{
			parent.document.forms[0].nodeIds.value = "";
		}
		window.location="NodeFileUpload.jsp";
	}
</script>