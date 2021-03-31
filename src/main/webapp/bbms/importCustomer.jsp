<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>客户资料导入界面</title>
<%
	/**
		 * 客户资料导入界面
		 * 
		 * @author 陈仲民(5243)
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
			alert("文件格式不正确");
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
						客户资料
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
				<tr><th>客户资料导入</th></tr>
				<tr bgcolor="#FFFFFF">
					<td>
						<input type="file" name="customerFile">&nbsp;&nbsp;
						<input type="button" name="tijiao" value="提交" onclick="checkFileType()">
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td>
						提示：文件类型为excel文件。第一行为列名称，其中<br>
						客户名称，客户ID，联系人姓名，联系人电话 (必选)<br>
						客户密码，客户类型(企业单位,事业单位)，企业规模(小,中，大)，企业地址，客户状态，属地，局向，小区，E-mail (可选)
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>