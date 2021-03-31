<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<title> 页面执行SQL </title>
<script type="text/javascript">
<!--//
//更新操作
function updateSQL(){
	var sql = $("textarea[@name='updateSql']").val();
	var diagUrl = '<s:url value="/gwms/report/executeSql!batchUpdate.action"/>';
	//更新
	$.post(diagUrl,{
		updateSql:sql
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='updateResDiv']").html("");
		$("div[@id='updateResDiv']").append(ajaxMesg);
	});
}

//查询操作
function querySQL(){
	var sql = $("textarea[@name='querySql']").val();
	var diagUrl = '<s:url value="/gwms/report/executeSql!query.action"/>';
	//查询
	$.post(diagUrl,{
		querySql:sql
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='queryResDiv']").html("");
		$("div[@id='queryResDiv']").append(ajaxMesg);
	});
}
//-->
</script>
<body>
<form action="" name="frm">
	<table>
		<tr>
			<td>更新SQL(多个sql以;隔开)</td>
		</tr>
		<tr>
			<td><textarea name="updateSql" rows="10" cols="100"></textarea></td>
		</tr>
		<tr>
			<td><input type="button" value="更新提交" onclick="updateSQL()"></td>
		</tr>
		<tr>
			<td><div id="updateResDiv" ></div></td>
		</tr>
		
		<tr>
			<td>查询SQL(只能单个)</td>
		</tr>
		<tr>
			<td><textarea name="querySql" rows="10" cols="100"></textarea></td>
		</tr>
		<tr>
			<td><input type="button" value="查询提交" onclick="querySQL()"></td>
		</tr>
		<tr>
			<td><div id="queryResDiv" ></div></td>
		</tr>
	</table>
</form>
</body>
</html>