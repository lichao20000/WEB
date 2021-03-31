<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
request.setCharacterEncoding("GBK");
String area1 = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script>
	
	function closeUpload(){
		// 隐藏添加框
		if(parent.document.getElementById("addFile")){
			parent.document.getElementById("addFile").style.display="none";
		}
		// 重新查询
		if (parent.document.frm1._vendor_id.value != "-1"){
			parent.queryfile();
		}
	}
	
	var area = '<%=area1%>';
	if(area == "sx_lt"){
		setTimeout("closeUpload()","500");
	}
</script>
<html>
<head>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</head>
<body>
文件传输结果：<s:property value="response"/>
</body>
</html>