<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ͻ����ϵ������</title>
<%
	/**
		 * �ͻ����ϵ������
		 * 
		 * @author ������(5243)
		 * @version 1.0
		 * @since 2008-06-04
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
	
<script language="javascript">
<!--
	function checkFileType(){
		var fileName = frm.customerFile.value;
		var arry = fileName.split(".");
		var len = arry.length;
		if(len < 1 || arry[len-1] != "xls"){
			alert("�ļ���ʽ����ȷ");
			return;
		}
		frm.submit();
		frm.tijiao.disabled = true;
	} 
//-->
</script>
</head>

<body>
<form name="frm" action="<s:url value="/bbms/importCustomer.action"/>" method="post" enctype="multipart/form-data">
<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						�ͻ�����
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">		
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
				bgcolor="#000000">
				<tr><th>�ͻ����ϵ���</th></tr>
				<tr bgcolor="#FFFFFF">
					<td>
						<input type="file" name="customerFile">&nbsp;&nbsp;
						<input type="button" name="tijiao" value="�ύ" onclick="checkFileType()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td>
						��ʾ���ļ�����Ϊexcel�ļ�����һ��Ϊ�����ƣ�����<br>
						�ͻ����ƣ��ͻ�ID����ϵ����������ϵ�˵绰 (��ѡ)<br>
						�ͻ����룬�ͻ�����(��ҵ��λ,��ҵ��λ)����ҵ��ģ(С,�У���)����ҵ��ַ���ͻ�״̬�����أ�����С����E-mail (��ѡ)
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>