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

<jsp:include page="selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value="selectbyonclick"/>
	<jsp:param name="byDeviceno" value="2"/>
	<jsp:param name="buUsername" value="1"/>
	<jsp:param name="byCity" value="0"/>
	<jsp:param name="byImport" value="0"/>
	<jsp:param name="div_device_height" value="60"/>
</jsp:include>

</head>

<body>
<div id="selectDevice">
	<span>������....</span>
</div>
</body>
</html>