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
		// ������ӿ�
		if(parent.document.getElementById("addFile")){
			parent.document.getElementById("addFile").style.display="none";
		}
		// ���²�ѯ
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
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</head>
<body>
�ļ���������<s:property value="response"/>
</body>
</html>