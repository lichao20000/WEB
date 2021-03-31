<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%request.setCharacterEncoding("GBK");%>
<html>
<head>
<style>
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	var excuteResult = "<s:property value='ajax' />";
	$(function() {
		parent.document.getElementById("resultDIV").innerHTML = excuteResult;
		parent.dyniframesize();
	});
</script>
</head>
</html>