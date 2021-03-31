<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>Lan侧下联设备信息</title>
</head>
<body>
<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%" bgcolor=#999999>
	<tr>
		<th width="15%">SSID</th>
		<th width="15%">状态</th>
		<th width="15%">802.11工作模式</th>
		<th width="15%">模块功率(dBm)</th>
		<th width="10%">关联设备数</th>
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
					<s:if test="'N/A' == ssid || 'null' == ssid">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="ssid" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == wlanStatus || 'null' == wlanStatus">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="wlanStatus" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == standard || 'null' == standard">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="standard" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == power || 'null' == power">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="power" escapeHtml="false"/>
						</td>
					</s:else>
					
					<s:if test="'N/A' == assoNum || 'null' == assoNum">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="assoNum" escapeHtml="false"/>
						</td>
					</s:else>
					
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