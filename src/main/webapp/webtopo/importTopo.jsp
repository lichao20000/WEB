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
</head>

<body>
<form name="frm" action="<s:url value="/webtopo/importTopo.action"/>" method="post" enctype="multipart/form-data">
<input type="hidden" name="pid" value="<s:property value="pid" />">
<input type="hidden" name="x" value="<s:property value="x" />">
<input type="hidden" name="y" value="<s:property value="y" />">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="98%" align="center"
				bgcolor="#000000">
				<tr><th>导入topo图</th></tr>
				<tr bgcolor="#FFFFFF">
					<td><input type="file" name="file"><input type="submit" value="提交"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>