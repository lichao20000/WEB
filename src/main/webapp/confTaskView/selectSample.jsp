<%--
FileName	: selectDevice.jsp
Date		: 2009��2��2��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸ѡ��</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">

<jsp:include page="../share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value="qixueqi"/>
</jsp:include>
<SCRIPT LANGUAGE="JavaScript">

	function qixueqi(deviceid){
		alert(deviceid);
	}
	
</SCRIPT>
</head>

<body>
<div id="selectDevice">
	<span>������....</span>
</div>
</body>
</html>