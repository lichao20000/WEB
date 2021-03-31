<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />

<title>智能网关API权限管理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function updateApiPlugin(){
	var classifyName = $.trim($("input[@name='classifyName']").val());
	var classifyDesc = document.getElementById('classifyDesc').value;
	var status = $.trim($("select[@name='status']").val());
	var classifyId = $.trim($("input[@name='classifyId']").val());
	//var page="<s:url value='/itms/resource/apiPlugin!updateApiPlugin.action'/>?"+ "classifyName=" + classifyName 
	//+ "&classifyId=" + classifyId 
	//+ "&status=" +status
	//+ "&classifyDesc=" +classifyDesc; 
	//window.open(page,"","left=50,top=50,height=250,width=300,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	var url = "<s:url value='/itms/resource/apiPlugin!updateApiPlugin.action'/>"; 
	$.post(url,{
		classifyName :classifyName,
		classifyId : classifyId,
		status: status,
		classifyDesc:classifyDesc
	},function(ajax){
		if("1"== ajax){
			alert("修改成功！");
		}else{
			alert("修改失败！");
		}
	});
	
}
</script>
</head>
<body>
	<%
		request.setCharacterEncoding("GBK");
		String classifyId = request.getParameter("classifyId");
		String classifyName = request.getParameter("classifyName");
		String status = request.getParameter("status");
		String classifyDesc = request.getParameter("classifyDesc");
		
	%>
	<input type="hidden" name="classifyId"
			value=<%=classifyId%> />
	<form name="updatefrm" id="updatefrm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">权限修改</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类名称：</td>
				<td width="35%" align="left"  colspan="3"><input name="classifyName"
					value=<%=classifyName%> type="text" class="bk" /></td>

			</tr>

			<tr bgcolor="#ffffff">
				<td class=column align="right">分类状态</td>
				<td colspan="3"><select name="status" class="select" id="status"
					style="width: 85px;">
						<option value="1" >生效</option>
						<option value="2" >失效</option>
				</select></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类描述：</td>
				<td width="35%" align="left" colspan="3"><textarea rows="10" cols="100"
						name="classifyDesc" id ="classifyDesc" ><%=classifyDesc%></textarea></td>

			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="修改"
					onclick="updateApiPlugin()" /></td>
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