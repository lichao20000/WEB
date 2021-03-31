<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>软件升级任务描述修改</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<%
	request.setCharacterEncoding("GBK");
	String task_id = request.getParameter("taskid");
	String task_desc = request.getParameter("taskDesc");
%>

<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1"  >软件升级任务描述修改</td></tr>
	<TR >
		<TD align="right" class="title_2" width="15%">任务描述</TD>
		<TD width="35%">
			<input type="hidden" value="<%=task_id%>" name="task_id">
			<input type="text" id="descOld" class="bk" readonly value="<%=task_desc%>">
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">新任务描述</TD>
		<TD align="left" width="35%">
			<input type="text" id="descNew" name="descNew" class="bk" value="">
		</TD>
	</TR>
	
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:updateDesc();" 
				name="addButton" style="CURSOR:hand" style="display:" > 修 改 </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">
function updateDesc(){
	var task_id = $.trim($("input[@name='task_id']").val());
	var descNew = $.trim($("input[@name='descNew']").val());
	if("" == descNew){
		alert("任务描述不可为空！");
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!updateTaskDesc.action'/>";
	$.post(url,{
		taskId : task_id,
		taskDesc : descNew 
	},function(ajax){
		if("1"== ajax){
			alert("修改成功！");
		}else{
			alert("修改失败！");
		}
		window.close();
	});
}

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}


</SCRIPT>