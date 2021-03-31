<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<title>导入用户</title>
<style>
BODY {
	FONT-FAMILY: "宋体","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
<SCRIPT LANGUAGE="JavaScript">
function doUpload(){
	var username=document.importFrm.file.value;; 
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("请先选择文件！");
		return false;
	}
	document.importFrm.submit();
	parent.idWait.innerHTML = "";
}
</SCRIPT>
</head>
<body>
	<form name="importFrm" action="/inmp/share/selectDeviceTag!getDeviceByImportUsername.action" method="POST" enctype="multipart/form-data">
		
	<input type="hidden" name="selectType" value="<s:property value="selectType"/>" class=bk/>
	<input type="hidden" name="jsFunctionName" value="<s:property value="jsFunctionName"/>" class=bk/>
	<input type="hidden" name="maxFileNum" value="<s:property value="maxFileNum"/>" class=bk/>
	<table>
			<tr style="display:">
				<td>
					<input type="file" size="60" name="file" class="bk"/>
					<input type="button" value="提交" onclick="doUpload()"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>