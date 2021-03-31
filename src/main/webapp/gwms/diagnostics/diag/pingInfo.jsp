<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>ping信息</title>
</head>
<body>
	<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%" bgcolor=#999999>
		<tr>
			<th width="12%">成功次数</th>
			<th width="12%">失败次数</th>
			<th width="16%">平均时延</th>
			<th width="15%">最大时延</th>
			<th width="15%">最小时延</th>
			<th width="15%">故障描述</th>
			<th width="15%">处理建议</th>
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
						<td class=column align="center"><s:property value="success" escapeHtml="false"/></td>
						<td class=column align="center"><s:property value="failure" escapeHtml="false"/></td>
						<td class=column align="center"><s:property value="averageTime" escapeHtml="false"/></td>
						<td class=column align="center"><s:property value="maxTime" escapeHtml="false"/></td>
						<td class=column align="center"><s:property value="minTime" escapeHtml="false"/></td>
						<s:if test="diagResult.pass == '-3'">
						<td class="column" align="center">
							<FONT color="red"><s:property value="passMessage" escapeHtml="false" default=""/></FONT>
						</td>
						<td class="column" align="center">
							<FONT color="green"><s:property value="passSuggest" escapeHtml="false" default="无"/></FONT>
						</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<FONT color="green">正常</FONT>
							</td>
							<td class="column" align="center">
								<FONT color="green">无</FONT>
							</td>
						</s:else>
					</tr>
				</s:iterator>
			</s:else>
		</s:else>
	</table>
</body>
</html>
