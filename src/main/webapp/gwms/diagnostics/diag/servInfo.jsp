<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title>业务信息</title>
</head>
<body>
	<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%" bgcolor=#999999>
		<tr>
			<th width="15%">PVC/VLAN</th>
			<th width="15%">宽带账号</th>
			<th width="10%">NAT</th>
			<th width="20%">DNS</th>
			<th width="10%">连接状态</th>
			<!-- <th width="15%">故障描述</th> 应要求去掉"故障描述" modify by zhangchy 2011-11-02-->
			<!-- <th width="15%">处理建议</th> 应要求去掉"处理建议" modify by zhangchy 2011-11-02-->
			<th width="15%">连接类型</th> <!-- 应要求增加"连接类型" modify by zhangchy 2011-11-02-->
		</tr>
	<s:if test="diagResult.pass == '-2'">
		<tr>
			<td colspan="7" class="column" >
				<s:property value="diagResult.failture" escapeHtml="false" default=""/>
			</td>
		</tr>
	</s:if>
	<s:else>
		<s:if test="diagResult.pass == '-1'">
			<tr>
				<td colspan="7" class="column" >
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

					<!-- 如果连接类型为路由 则显示其帐号  否则不显示 modify by zhangchy 2011-11-02  -->
					<s:if test="'路由' == connType">
						<s:if test="'N/A' == username || 'null' == username">
							<td class="column" align="center">-</td>
						</s:if>
						<s:else>
							<td class="column" align="center">
								<s:property value="username" escapeHtml="false"/>
							</td>
						</s:else>
					</s:if>
					<s:else>
						<td class="column" align="center">-</td>
					</s:else>

					<s:if test="'N/A' == nat || 'null' == nat">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="nat" escapeHtml="false"/>
						</td>
					</s:else>

					<s:if test="'N/A' == dns || 'null' == dns">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="dns" escapeHtml="false"/>
						</td>
					</s:else>

					<s:if test="'N/A' == status || 'null' == status">
						<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="status" escapeHtml="false"/>
						</td>
					</s:else>

					<!--<s:if test="diagResult.pass == '-3'">
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
					</s:else> -->
					<s:if test="'N/A' == connType || 'null' == connType">
							<td class="column" align="center">-</td>
					</s:if>
					<s:else>
						<td class="column" align="center">
							<s:property value="connType" escapeHtml="false"/>
						</td>
					</s:else>

				</tr>
			</s:iterator>
		</s:else>
	</s:else>
	</table>
</body>
</html>
