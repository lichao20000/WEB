<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>Lan�������豸��Ϣ</title>
</head>
<body>
<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%">
	<tr>
		<th width="20%">�˿�</th>
		<th width="30%">�����豸����</th>
		<th width="30%">�����豸״̬</th>
		<th width="20%">������</th>
	</tr>
	<s:set var="pass" value="#parameters.pass"/>
	<s:if test="pass != '-2'">
		<s:iterator value="resultList">
			<tr>
				<s:if test="'N/A' == interf || 'null' == interf">
					<td class="column" align="center">-</td>
				</s:if>
				<s:else>
					<td class="column" align="center">
						<s:property value="interf" escapeHtml="false"/>
					</td>
				</s:else>

				<s:if test="'N/A' == hostname || 'null' == hostname">
					<td class="column" align="center">-</td>
				</s:if>
				<s:else>
					<td class="column" align="center">
						<s:property value="hostname" escapeHtml="false"/>
					</td>
				</s:else>

				<td class=column align="center">
					<s:property value="active" escapeHtml="false"/>
				</td>

				<s:if test="'' == passMessage || 'null' == passMessage">
					<td class="column" align="center">
						<FONT color="green">����</FONT>
					</td>
				</s:if>
				<s:else>
					<td class="column" align="center">
						<FONT color="green"><s:property value="passMessage" escapeHtml="false" default="����"/></FONT>
					</td>
				</s:else>
			</tr>
		</s:iterator>
			<tr>
				<td colspan="4">
					<FONT color="green"><s:property value="suggest" escapeHtml="false"/></FONT>
				</td>
			</tr>
	</s:if>
	<s:else>
		<tr>
			<td colspan="4">
				<!-- <font color="red">��ȡLAN�������豸��Ϣʧ��,�����豸�Ƿ�����</font> -->
				<s:property value="suggest" escapeHtml="false"/>
			</td>
		</tr>
	</s:else>
</table>
</body>
</html>
