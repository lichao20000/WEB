<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>流量性能统计</title>
<%
	/**
		 * 流量性能统计
		 *
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-07-11
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript">

</script>
</head>

<body>
<form action="">
<table border=0 cellspacing=0 cellpadding=0 width="95%">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						流量性能统计
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">多个设备流量性能统计日报表
					</td>
					<td align="right">

					</td>
				</tr>
			</table>
			</td>
		</tr>
	<tr>
		<td>
			<s:property value="msg" escapeHtml="false"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>
