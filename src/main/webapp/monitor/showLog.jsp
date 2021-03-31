<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>最新日志</title>
</head>

<body>
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
	<tr>
		<td align="left">
			<s:if test="monitorMapList!=null && monitorMapList.size()>0">
				<s:iterator value="monitorMapList" var="monitorMap">
					<s:property value="log" />
					<br>
				</s:iterator>
			</s:if>
		</td>
	</tr>
</table>
</body>
</html>