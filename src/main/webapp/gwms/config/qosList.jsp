<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>������qos�б�</title>
</head>
<body>
	<table border="0" cellspacing="1" cellpadding="1" id="myTable" width="100%">
		<tr bgcolor=#ffffff>
			<td class=green_title2 width="40%">��������</td>
			<td class=green_title2 width="30%">���ý��</td>
			<td class=green_title2 width="30%">����ʱ��</td>
		</tr>
		<s:iterator value="qosList">
			<tr bgcolor=#ffffff>
				<td class=column align="center"><s:property value="qosParam" escapeHtml="false"/></td>
				<td class=column align="center">
					<s:if test="status == '' || status == '2'">
						����ִ��...
					</s:if>
					<s:else>
						<s:property value="resultDesc" escapeHtml="false"/>
					</s:else>
				</td>
				<td class=column align="center"><s:property value="time" escapeHtml="false"/></td>
			</tr>
		</s:iterator>
	</table>
</body>
</html>
