<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%request.setCharacterEncoding("GBK");%>
<html>
<head>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
function init(){
	$("input[@name='upload']",parent.document).attr("disabled",false);
	$("input[@name='upload']",parent.document).val("�ϴ�");
}
</script>
</head>
<body onload="init()">
<SPAN id="idWait">�ļ���������<s:property value="response"/></SPAN>
</body>
</html>