<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>手机信息导入</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function insert()
{
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	var url="<s:url value='/gwms/blocTest/BatchAdditionPhone!insert.action'/> ";
	if(""==gwShare_fileName){
		alert("请先上传文件!");
		return;
	}
	$.post(url, {
		gwShare_fileName:gwShare_fileName
	}, function(ajax) {
		if (ajax > 0) {
			alert("导入成功!");
			return;
		}else if(ajax=='-100'){
			alert("导入失败!文件过大，最多单次导入五万条数据！");
		}else{
			alert("导入失败!");
		}
	});
}
</SCRIPT>

</head>
<form name="Form" action="" target="dataForm">
<TABLE width="100%" class="querytable" align="center">
	<tr>
		<td colspan="4" class="title_1" id="">提交文件</td>
	</tr>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  >
		<td align="right" width="15%">注意事项</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32" >
		<td CLASS="green_foot" align="right">注意事项</td>
		<td colspan="3" CLASS="green_foot">
			1、需要导入的文件格式：手机移动终端为Excel文件，即xls。
		 	<br>
			2、终端文件为txt文件,txt格式。 
			<br>
			3、文件行数不要太多，以免影响性能。
			<br>
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insert();" name="gwShare_queryButton" style="CURSOR:hand"  >导入</button>
			</div>
		</td>
	</tr>
</TABLE>
</form>