<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>???Ŵ?????Ϣ</title>
</head>
<body>
	<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%" bgcolor=#999999>
		<tr>
			<th width="15%">PVC/VLAN</th>
			<th width="25%">????????</th>
			<th width="30%">???Ŵ?????</th>
			<th width="15%">????????</th>
			<th width="15%">????????</th>
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
						<s:if test="'N/A' == pvc || 'null' == pvc">
							<td class="column" align="center">-</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<s:property value="pvc" escapeHtml="false"/>
							</td>
						</s:else>
						<s:if test="'N/A' == connType || 'null' == connType">
							<td class="column" align="center">-</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<s:property value="connType" escapeHtml="false"/>
							</td>
						</s:else>

						<s:if test="'N/A' == connErr || 'null' == connErr">
							<td class="column" align="center">-</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<s:property value="connErr" escapeHtml="false"/>
							</td>
						</s:else>

						<s:if test="diagResult.pass == '-3'">
						<td class="column" align="center">
							<FONT color="red"><s:property value="passMessage" escapeHtml="false" default=""/></FONT>
						</td>
						<td class="column" align="center">
							<FONT color="green"><s:property value="passSuggest" escapeHtml="false" default="??"/></FONT>
						</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<FONT color="green">????</FONT>
							</td>
							<td class="column" align="center">
								<FONT color="green">??</FONT>
							</td>
						</s:else>
					</tr>
				</s:iterator>
			</s:else>
		</s:else>
	</table>
</body>
</html>
