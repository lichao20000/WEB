<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
	<lk:res />

<script language="JavaScript">
function doQuery(){
	var relation_type = $.trim($("select[@name='relation_type']").val());
    var auth_code = $.trim($("input[@name='auth_code']").val());
    var auth_name = $.trim($("input[@name='auth_name']").val());
    var user_name = $.trim($("input[@name='user_name']").val());
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var url = "<s:url value='/gtms/system/superRoleManage!getAllRecords.action'/>";  
	$.post(url,{
		auth_code : auth_code,
		auth_name : auth_name,
		relation_type : relation_type,
		user_name : user_name
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
	 
}
function doAdd(){
	window.location.href="SuperRoleManage.jsp";
}
</script>
</head>
	
<br>
<TABLE>
		<tr>
		<td>
			<table class="green_gargtd">
				<tr>
				<th>����Ȩ�޲�ѯ </th>
				<td>
					<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
				</td>
				</tr>
			</table>
		</td>
		</tr>
		<tr>
		<td>
			<form name=frm>
			<table class="querytable">
				<tr> <th colspan=4>����Ȩ�޲�ѯ</th> </tr>
				<tr bgcolor="#ffffff">
					<td class=column align=center width="15%">Ȩ������ </td>
					<td width="35%">
						<input type=text id="auth_name" name="auth_name" class=bk size="40">
					</td>
					<td class=column align=center width="15%">Ȩ�޼���</td>
					<td  width="35%">
						<input type="text" id="auth_code" name="auth_code" class=bk size="40">
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td class=column align=center width="15%">���Ʒ�ʽ</td>
					<td width="35%">
						<select id="relation_type" name="relation_type">
							<!-- option value="-1">��ѡ��</option-->
							<option value="0">�û�����</option>
							<option value="1">��ɫ����</option>
						</select>
					</td>
					<td class=column align=center width="15%">�û�/��ɫ����</td>
					<td  width="35%">
						<input type="text" id="user_name" name="user_name" class=bk size="40">
					</td>
				</tr>	
				<tr bgcolor="#ffffff">
					<td class=foot colspan=4 align=right>
						<button name="button" onclick="doQuery();">&nbsp;��ѯ&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
						<button name="button" onclick="doAdd();">&nbsp;����&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
		</tr>
		<tr id="trData" style="display: none" >
			<td>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					����Ŭ����ѯ�����Ե�....
				</div>
			</td>
		</tr>
		<tr>
			<td height="20"> </td>
		</tr>
	</TABLE>
	
<%@ include file="../../foot.jsp"%>
</html>