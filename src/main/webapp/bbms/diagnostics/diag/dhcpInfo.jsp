<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>Lan�������豸dhcp��IP��Ϣ</title>
</head>
<body>
<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%">
	<tr>
		<th width="10%">�˿�</th>
		<th width="10%">�����豸����</th>
		<th width="15%">�����豸״̬</th>
		<th width="15%">��ȡIP��ַ</th>
		<th width="20%">IP��ַ��Դ</th>
		<th width="15%">��������</th>
		<th width="15%">������</th>
	</tr>
	<s:if test="diagResult.pass == '-2'">
		<tr>
			<td colspan="8" class="column" >
				<s:property value="diagResult.failture" escapeHtml="false" default=""/>
			</td>
		</tr>
	</s:if>
	<s:else>
		<s:if test="diagResult.pass == '-1'">
			<tr>
				<td colspan="8" class="column" >
					<FONT color="red"><s:property value="diagResult.faultDesc" escapeHtml="false" default=""/></FONT>
				</td>
			</tr>
			<tr>
				<td colspan="8" class="column" >
					<FONT color="green"><s:property value="diagResult.suggest" escapeHtml="false" default=""/></FONT>
				</td>
			</tr>
		</s:if>
		<s:else>
			<s:iterator value="diagResult.rList">
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
					
					<s:if test="'N/A' == active || 'null' == active">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="active" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == hostIp || 'null' == hostIp">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="hostIp" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == addrSource || 'null' == addrSource">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="addrSource" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="diagResult.pass == '-3'">
						<td class="column" align="center">
							<FONT color="red"><s:property value="passMessage" escapeHtml="false" default=""/></FONT>
						</td>
						<td class="column" align="center">
							<FONT color="green"><s:property value="passSuggest" escapeHtml="false" default="��"/></FONT>
						</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<FONT color="green">����</FONT>
						</td>
						<td class="column" align="center">
							<FONT color="green">��</FONT>
						</td>
					</s:else>
				</tr>
			</s:iterator>
		</s:else>
	</s:else>
</table>
</body>
</html>