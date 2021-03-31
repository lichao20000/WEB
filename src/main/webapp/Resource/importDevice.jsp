<%@ page contentType="text/html;charset=GBK"%>

<html>
<head>
	<title>导入设备</title>
</head>

<body>
	<form name="importFrm" action="importDevice.action" method="POST" enctype="multipart/form-data">
		<input type="file" name="file">
		<input type="submit" value="提交">
	</form>
</body>
</html>
