<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：批量重启机顶盒任务定制结果查询
		</TD>
	</TR>
</TABLE>
<br>
<table class="querytable" width="98%" align="center">
	<tr>
		<td colspan="4" class="title_1">批量重启机顶盒任务定制结果</td>
	</tr>
	<tr>
		<td class="title_2" align="center" width="15%">定制结果</td>
		<td width="85%" colspan="3"><s:property value="ajax"/></td>
	</tr>
</table>
<br>
<br>
</body>