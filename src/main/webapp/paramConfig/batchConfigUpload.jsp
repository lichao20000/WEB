<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkForm(){
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("请选择文件!");
		return false;
	}
	if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3))){
		alert("请按照注意事项上传文件！");
		return false;
	}
	
	return true;
}

</script>
<head> 
    <title> Struts 2 File Upload </title> 
</head> 
<body> 
	<form id="batchform" action="/itms/gtms/config/paramNodeCfg!uploadFile.action" method="post" enctype="multipart/form-data" name="batchform" >
    	<input type="hidden" name ="upload" size="40" >
    	<input type="hidden" name ="uploadNodeId" value="">
    	<input type="hidden" name ="uploadNodeVal" value="">
   		<input type="file" id="upload" name ="upload" size="40" /> 
   		<input type="hidden" id="taskId" name="taskId" value="" >
   		<input type="hidden" name="uploadDoType" value="" >
    </form> 
</body> 
