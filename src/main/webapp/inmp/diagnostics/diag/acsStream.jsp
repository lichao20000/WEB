<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>�����������</title>
</head>
<body>
<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="800">
	<!--
	<tr>
		<td>
			<input type="radio" name="fresh" id="autoId"> �Զ�ˢ��
			<input type="radio" name="fresh" checked> �ֶ�ˢ��
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="ˢ ��" onclick="location.reload();">
		</td>
	</tr>
	 -->
	<tr>
		<td>
			<input type="button" value="ˢ ��" onclick="location.reload();">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="�� ��" onclick="window.close();">
			<input type="hidden" name="deviceId" value="<s:property value="deviceId"/>">
		</td>
	</tr>
	<tr>
		<td width="800">
			<div style="width:800px">
				<s:iterator value="streamList">
					<s:if test="1 == red">
						<font color="red">
							<s:property value="source" escapeHtml="false"/>
							<br>
							<s:property value="dest" escapeHtml="false"/>
							<br>
							<s:property value="time" escapeHtml="false"/>
							<br>
							<s:property value="content" />
						</font>
					</s:if>
					<s:else>
						<font color="green">
							<s:property value="source" escapeHtml="false"/>
							<br>
							<s:property value="dest" escapeHtml="false"/>
							<br>
							<s:property value="time" escapeHtml="false"/>
							<br>
							<s:property value="content" />
						</font>
					</s:else>
					<hr>
				</s:iterator>
			</div>
		</td>
	<tr>
</table>
</body>
</html>
