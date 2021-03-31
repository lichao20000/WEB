<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
	<title>导入用户</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<style>
BODY {
	FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
<SCRIPT LANGUAGE="JavaScript">
function doUpload(){
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		return false;
	}
	//按钮按下的时候把按钮置灰 add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "正在上传解析......";
	//idWait.innerHTML = "正在上传解析......";
	document.importFrm.submit();
}

</SCRIPT>
</head>
<body bgcolor="#FFFFFF">
	<form name="importFrm" action="devVenderModelAction!getDeviceByImportUsername.action" method="POST" enctype="multipart/form-data">
		<table>
			<tr><td><input type="file" size="60" name="file"/>
		
		<!-- <input type="button" value="说 明" name="filedesc" onclick="parent.showDesc()" /> -->
		<input type="button" value=" 提 交 " class=jianbian name="upload" onclick="doUpload()"/>
		</td></tr></table>
	</form>
</body>
</html>