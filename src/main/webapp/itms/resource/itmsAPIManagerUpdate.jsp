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
function updateApiManager(){
	var servicenameZh = $.trim($("input[@name='servicenameZh']").val());
	var servicenameEn = $.trim($("input[@name='servicenameEn']").val());
	var functionDesc = document.getElementById('functionDesc').value;
	var apiListName = document.getElementById('apiListName').value;
	var classifyId = $.trim($("select[@name='classifyId']").val());
	var managerId = $.trim($("input[@name='managerId']").val());
	//var page="<s:url value='/itms/resource/apiManager!updateApiManager.action'/>?"+ "servicenameZh=" + servicenameZh 
	//+ "&classifyId=" + classifyId 
	//+ "&managerId=" + managerId 
	//+ "&servicenameEn=" + servicenameEn 
	//+ "&functionDesc=" +functionDesc
	///+ "&apiListName=" +apiListName; 
	//window.open(page,"","left=50,top=50,height=250,width=300,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	//window.close();
	var url = "<s:url value='/itms/resource/apiManager!updateApiManager.action'/>"; 
	$.post(url,{
		classifyId : classifyId,
		managerId : managerId,
		servicenameZh : servicenameZh,
		servicenameEn : servicenameEn,
		functionDesc : functionDesc,
		apiListName : apiListName
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
		String managerId = request.getParameter("managerId");
		String servicenameZh = request.getParameter("servicenameZh");
		String servicenameEn = request.getParameter("servicenameEn");
		String functionDesc = request.getParameter("functionDesc");
		String apiListName = request.getParameter("apiListName");
		
	%>
	<input type="hidden" name="managerId"
			value=<%=managerId%> />
	<form name="updatefrm" id="updatefrm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<tr>
				<td colspan="4" align="center" class="green_title">权限修改</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>服务类名(中文)：</td>
				<td width="35%" align="left"><input name="servicenameZh"
					value=<%=servicenameZh%> type="text" class="bk" /></td>
					<td width="15%" align="right" class=column>服务类名(英文)：</td>
				<td width="35%" align="left"><input name="servicenameEn"
					value=<%=servicenameEn%> type="text" class="bk" /></td>

			</tr>

			<tr bgcolor="#ffffff">
				<td class=column align="right">权限分类</td>
				<td colspan="3"><s:select list="classifyList" name="classifyId" listKey="id"
						listValue="classify_name" cssClass="select" style="width:100px;"></s:select></td>
			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>权限分类描述：</td>
				<td width="35%" align="left" colspan="3"><textarea rows="10" cols="100"
						name="functionDesc" id ="functionDesc" ><%=functionDesc%></textarea></td>

			</tr>
			<tr bgcolor=#ffffff>
				<td width="15%" align="right" class=column>API接口名：</td>
				<td width="35%" align="left" colspan="3"><textarea rows="10" cols="100"
						name="apiListName" id ="apiListName" ><%=apiListName%></textarea></td>

			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="4" align="right"><input type="button" value="修改"
					onclick="updateApiManager()" /></td>
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