<%--
FileName	: selectDevice.jsp
Date		: 2009年2月2日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备选择</title>
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
	<span>加载中....</span>
</div>
</body>
</html>