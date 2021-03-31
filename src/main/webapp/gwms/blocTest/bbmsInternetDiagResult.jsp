<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">

<table border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
	<tr>
		<th colspan="7" align="center">
			诊断结果
		</th>
	</tr>
	<tr bgcolor="#FFFFFF">
		<th align="center" width="17%">
		</th>
		<th align="center" width="17%">
			宽带账号
		</th>
		<th align="center" width="8%">
			PVC/VLAN
		</th>
		<th align="center" width="14%">
			IP地址
		</th>
		<th align="center" width="14%">
			子网掩码
		</th>
		<th align="center" width="14%">
			默认网关
		</th>
		<th align="center" width="16%">
			DNS
		</th>
	</tr>

	<tr bgcolor="#FFFFFF">
		<td align="center">设备宽带业务</td>
		<td align="center" colspan="6">
		<s:if test="deviceMapList!=null">
			<table border=0 cellspacing=1 cellpadding=2 width="100%"
				bgcolor=#999999>
				<s:iterator value="deviceMapList" status="status">
				<tr bgcolor="#FFFFFF">
					<td align="center" width="20%">
						<s:property value="deviceMapList[#status.index].username" />
					</td>
					<td align="center" width="10%">
						<s:property value="deviceMapList[#status.index].pvc_vlan" />
					</td>
					<td align="center" width="17%">
						<s:property value="deviceMapList[#status.index].ip" />
					</td>
					<td align="center" width="17%">
						<s:property value="deviceMapList[#status.index].mask" />
					</td>
					<td align="center" width="17%">
						<s:property value="deviceMapList[#status.index].gateway" />
					</td>
					<td align="center" width="39%">
						<s:property value="deviceMapList[#status.index].dns" />
					</td>
				</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else>
			无设备宽带业务信息
		</s:else>
		</td>
	</tr>
	
	<tr bgcolor="#FFFFFF">
		<td align="center">用户宽带业务</td>
		<td align="center" colspan="6">
		<s:if test="userMap!=null">
			<table border=0 cellspacing=1 cellpadding=2 width="100%"
				bgcolor=#999999>
				<tr bgcolor="#FFFFFF">
					<td align="center" width="20%">
						<s:property value="userMap.username"/>
					</td>
					<td align="center" width="10%">
						<s:property value="userMap.pvc_vlan"/>
					</td>
					<td align="center" width="17%">
						<s:property value="userMap.ip"/>
					</td>
					<td align="center" width="17%">
						<s:property value="userMap.mask"/>
					</td>
					<td align="center" width="17%">
						<s:property value="userMap.gateway"/>
					</td>
					<td align="center" width="39%">
						<s:property value="userMap.dns"/>
					</td>
				</tr>
			</table>
		</s:if>
		<s:else>
			无用户宽带业务信息
		</s:else>
		</td>
	</tr>

	<tr bgcolor="#FFFFFF">
		<td align="center">对比</td>

		<td align="center">
		<s:if
			test="comparaResult.username_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>

		<td align="center">
		<s:if
			test="comparaResult.access_tag_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>

		<td align="center"><s:if test="comparaResult.ip_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>

		<td align="center"><s:if test="comparaResult.mask_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>

		<td align="center">
		<s:if
			test="comparaResult.gateway_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>

		<td align="center">
		<s:if test="comparaResult.dns_compare==1">
			<font color="green">正常</font>
		</s:if> <s:else>
			<font color="red">异常</font>
		</s:else></td>
	</tr>
</table>


