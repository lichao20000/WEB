<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ͻ����ϲ�ѯ����</title>
<%
	/**
		 * �ͻ����ϲ�ѯ����
		 * 
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-06-04
		 * @category
		 */
%>
<link href="<s:url value='/css/css_green.css'/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript">
//��ѯ
function queryCustomer(){
	parent.frames.dataForm.frm.customer_id.value = document.frm.customer_id.value;
	parent.frames.dataForm.frm.customer_name.value = document.frm.customer_name.value;
	parent.frames.dataForm.frm.action = "<s:url value='/bbms/CustomerInfo!queryData.action'/>";
	parent.frames.dataForm.frm.submit();
}
</script>
</head>

<body>
<form name="frm" action="">
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�ͻ�����
					</td>
					<td>
						&nbsp;<img src="../images/attention_2.gif" width="15" height="12">		
					</td>
					<td align="right">
						
					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#999999">
				<tr><th colspan="4">��ѯ</th></tr>
				<tr bgcolor="#FFFFFF">
					<td class=column align='right'>�ͻ�ID</td>
					<td><input type="input" name="customer_id" class="bk"></td>
					<td class=column width="15%" align='right'>�ͻ�����</td>
					<td><input type="input" name="customer_name" class="bk"></td>
				</tr>
				 <tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot">
						<input type="button" value=" �� ѯ " onclick="queryCustomer()">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>