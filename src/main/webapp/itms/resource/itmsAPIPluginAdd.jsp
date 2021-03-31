<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />

<title>智能网关API权限管理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function getApiPluginList() {
	var classifyName = $.trim($("input[@name='classifyName']").val());
	//var creator = $.trim($("input[@name='creator']").val());
	var classifyDesc = document.getElementById('classifyDesc').value;
	var status = $.trim($("select[@name='status']").val());
	var addfrm = document.getElementById("addfrm");
	//addfrm.action = "<s:url value='/itms/resource/apiPlugin!addApiPlugin.action'/>";
	//addfrm.submit();
	//window.close();
	var url = "<s:url value='/itms/resource/apiPlugin!addApiPlugin.action'/>"; 
	$.post(url,{
		classifyName :classifyName,
		status: status,
		classifyDesc:classifyDesc
	},function(ajax){
		if("1"== ajax){
			alert("新增成功！");
		}else{
			alert("新增失败！");
		}
	});
}
</script>
</head>
<body>
	
	<form name="addfrm" id="addfrm" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">权限添加</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类名称：</td>
				<td width="35%" align="left"  colspan="3"><input name="classifyName"
					  type="text" class="bk" /></td>
			</tr>

			<tr bgcolor="#ffffff">
				<td class=column align="right">分类状态</td>
				<td colspan="3"><select name='status' class="select"
					style="width: 85px;">
						<option value="1" >生效</option>
						<option value="2" >失效</option>
				</select></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类描述：</td>
				<td width="35%" align="left" colspan="3"><textarea rows="10" cols="100"
						name="classifyDesc" id ="classifyDesc" ></textarea></td>

			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="新增"
					onclick="getApiPluginList()" /></td>
			</tr>

		</table>
	</form>
	<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>