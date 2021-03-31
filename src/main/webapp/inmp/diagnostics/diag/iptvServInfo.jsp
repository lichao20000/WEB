<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>业务信息</title>
</head>
<body>
<table border="0" cellspacing="1" cellpadding="2" id="myTable"
	width="100%" bgcolor=#999999>
	<tr>
		<th width="30%">PVC/WLAN</th>
		<th width="20%">NAT</th>
		<th width="20%">连接状态</th>
		<th width="15%">故障描述</th>
		<th width="15%">处理建议</th>
	</tr>
	<s:if test="diagResult.pass == '-2'">
		<tr>
			<td colspan="5" class="column"><s:property
				value="diagResult.failture" escapeHtml="false" default="" /></td>
		</tr>
	</s:if>
	<s:else>
		<s:if test="diagResult.pass == '-1'">
			<tr>
				<td colspan="5" class="column"><FONT color="red"><s:property
					value="diagResult.faultDesc" escapeHtml="false" default="" /></FONT></td>
			</tr>
			<tr>
				<td colspan="5" class="column"><FONT color="green"><s:property
					value="diagResult.suggest" escapeHtml="false" default="" /></FONT></td>
			</tr>
		</s:if>
		<s:else>
			<s:iterator value="diagResult.rList">
				<tr>
					<s:if test="'N/A' == pvc || 'null' == pvc">
						<td class=column align="center">-</td>
					</s:if>
					<s:else>
						<td class=column align="center"><s:property value="pvc"
							escapeHtml="false" /></td>
					</s:else>

					<s:if test="'N/A' == nat || 'null' == nat">
						<td class=column align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center"><s:property value="nat"
							escapeHtml="false" /></td>
					</s:else>

					<s:if test="'N/A' == status || 'null' == status">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center"><s:property value="status"
							escapeHtml="false" /></td>
					</s:else>

					<s:if test="diagResult.pass == '-3'">
						     <td class="column" align="center">
						     <FONT color="red"><s:property value="passMessage" escapeHtml="false" default="" /></FONT></td>
						<td class="column" align="center"><FONT color="green"><s:property value="passSuggest" escapeHtml="false" default="无" /></FONT></td>
					</s:if>
					<s:else>
                      <td class="column" align="center"><FONT color="green">正常</FONT>
						</td>
						<td class="column" align="center"><FONT color="green">无</FONT>
						</td>

               </s:else>
				</tr>
			</s:iterator>
		</s:else>
	</s:else>
</table>
</body>
</html>
