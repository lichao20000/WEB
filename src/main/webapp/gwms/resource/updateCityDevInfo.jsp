<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<s:if test="deviceList!=null">
		<tr>
			<td class="green_title" >
				选择
			</td>
			<td class="green_title" >
				设备序列号
			</td>
			<td class="green_title" >
				域名或IP
			</td>
			<td class="green_title" >
				属地
			</td>
			<td class="green_title" >
				绑定状态
			</td>

		</tr>

		<s:iterator value="deviceList" var="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="device_serialnumber"/>',
											'<s:property value="cpe_allocatedstatus"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="device_serialnumber" />
				</td>
				<td class=column align="center">
					<s:property value="loopback_ip" />
				</td>

				<td class=column align="center">
					<s:property value="city_name" />
				</td>
				<TD class=column align="center">
					<s:if test='cpe_allocatedstatus=="1"'>
					已绑定
					</s:if>
					<s:else>
					未绑定
					</s:else>
				</TD>

			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="center">
				<font color="red">你所查询的终端未注册到系统，请按照"设备未注册的处理建议"进行排障！</font>
				<%@include file="DevNotRegisterAdvice.jsp"%>
			</td>
		</tr>
	</s:else>

</table>
