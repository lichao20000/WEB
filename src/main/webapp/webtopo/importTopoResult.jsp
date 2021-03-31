<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>导入topo图</title>
<%
	/**
		 * 导入topo图
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-05-22
		 * @category
		 */
%>
<%@ include file="../head.jsp"%>
<script type="text/javascript">
window.opener.refreshTopo();
</script>
</head>

<body>
<form action="">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="98%" align="center"
				bgcolor="#000000">
				<tr><th>导入topo结果</th></tr>
				<tr bgcolor="#FFFFFF">
					<td align="center" height="50"><s:property value="msg"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>