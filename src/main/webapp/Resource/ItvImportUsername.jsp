<%@ page contentType="text/html;charset=GBK"%>
<html>
<head>
	<title>�����û�</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<style>
BODY {
	FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
<SCRIPT LANGUAGE="JavaScript">
function doUpload(){
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		return false;
	}
	//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "�����ϴ�����......";
	//idWait.innerHTML = "�����ϴ�����......";
	document.importFrm.submit();
}

</SCRIPT>
</head>
<body bgcolor="#FFFFFF">
	<form name="importFrm" action="devVenderModelAction!getDeviceByImportUsername.action" method="POST" enctype="multipart/form-data">
		<table>
			<tr><td><input type="file" size="60" name="file"/>
		
		<!-- <input type="button" value="˵ ��" name="filedesc" onclick="parent.showDesc()" /> -->
		<input type="button" value=" �� �� " class=jianbian name="upload" onclick="doUpload()"/>
		</td></tr></table>
	</form>
</body>
</html>