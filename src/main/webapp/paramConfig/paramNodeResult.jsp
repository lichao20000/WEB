<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%request.setCharacterEncoding("GBK");%>
<html>
<head>
<style>
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" >
	var result = "<s:property value='ajax' />";
	$(function() {
		parent.document.getElementById("trData5").style.display="";
		parent.document.getElementById("resultDIV").innerHTML = result;
	});
</SCRIPT>
</head>
</html>