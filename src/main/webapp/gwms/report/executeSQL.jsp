<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<title> ҳ��ִ��SQL </title>
<script type="text/javascript">
<!--//
//���²���
function updateSQL(){
	var sql = $("textarea[@name='updateSql']").val();
	var diagUrl = '<s:url value="/gwms/report/executeSql!batchUpdate.action"/>';
	//����
	$.post(diagUrl,{
		updateSql:sql
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='updateResDiv']").html("");
		$("div[@id='updateResDiv']").append(ajaxMesg);
	});
}

//��ѯ����
function querySQL(){
	var sql = $("textarea[@name='querySql']").val();
	var diagUrl = '<s:url value="/gwms/report/executeSql!query.action"/>';
	//��ѯ
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
			<td>����SQL(���sql��;����)</td>
		</tr>
		<tr>
			<td><textarea name="updateSql" rows="10" cols="100"></textarea></td>
		</tr>
		<tr>
			<td><input type="button" value="�����ύ" onclick="updateSQL()"></td>
		</tr>
		<tr>
			<td><div id="updateResDiv" ></div></td>
		</tr>
		
		<tr>
			<td>��ѯSQL(ֻ�ܵ���)</td>
		</tr>
		<tr>
			<td><textarea name="querySql" rows="10" cols="100"></textarea></td>
		</tr>
		<tr>
			<td><input type="button" value="��ѯ�ύ" onclick="querySQL()"></td>
		</tr>
		<tr>
			<td><div id="queryResDiv" ></div></td>
		</tr>
	</table>
</form>
</body>
</html>