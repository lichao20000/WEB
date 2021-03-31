<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 定制终端模板列表展示
 *
 * @author Duangr(5250) tel：13770931606
 * @version 1.0
 * @since 2008-6-11 上午09:50:07
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>定制终端模板列表</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<style type="text/css">
</style>
<script language="JavaScript">
<!--
	// 编辑模板
	function editTemplate(templateId){
		window.location="<s:url value='/Resource/terminalTemplate!editTemplate.action' />?templateId="+templateId;
	}
	// 删除模板
	function delTemplate(templateId){
		if(!confirm("确认删除该模板?"))
				return;
			$.ajax({
				type:"POST",
				url:"<s:url value='/Resource/terminalTemplate!delTemplate.action' />",
				data:{"templateId":templateId},
				success:function(data){
					if(data=="1"){
						alert("删除成功");
						window.location.reload();
					}else{
						alert("删除失败");
					}
				},
				error:function(xmlR,msg,other){alert("删除时异常,请与管理员联系");}
			});
	}
	// 新增模板
	function addTemplate(){
		window.location="<s:url value='/Resource/terminalTemplate.action' />";
	}
-->
</script>
</head>
<body>
<form name="speclinefrm" action="<s:url value="/Resource/terminalTemplate.action"/>" onSubmit="return CheckForm();"  method ="post" >
	<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0">
		<tr><td height=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							定制终端模板
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=1 width="100%">
					<tr>
						<th colspan="4" align="center">定制终端模板列表
						</th>
					</tr>
					<tr>
						<td class="column" height="17" align="center">模板名称</td>
						<td class="column" height="17" align="center">操作</td>
					</tr>
					<s:iterator value="resultList" var="rl"  status="status">
					<tr>
						<td class="column" height="17" align="center"><s:property value="#rl.template_name" /></td>
						<td class="column" height="17" align="center">
							<a href="#" onclick="editTemplate('<s:property value="#rl.template_id" />');">编辑</a>&nbsp;|&nbsp;
							<a href="#" onclick="delTemplate('<s:property value="#rl.template_id" />');">删除</a>

						</td>
					</tr>
					</s:iterator>
					<tr>
						<td class="column" colspan="2" class="green_foot" align="right">
							<input type="button" value="新增模板" onclick="addTemplate();" class="btn">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
