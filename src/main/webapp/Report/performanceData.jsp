<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能报表</title>
<%
	/**
		 * 性能报表
		 *
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-08-12
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

</script>
</head>

<body>
<form name="frm" action="<s:url value="/bbms/CustomerInfo!addCustomer.action"/>" method="post">
<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						性能报表
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
			<s:property value="reportData" escapeHtml="flase"/>
		</td>
	</tr>
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
</table>
</form>
</body>
</html>
