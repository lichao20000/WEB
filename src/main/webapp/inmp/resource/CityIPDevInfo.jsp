<%--
设备查询界面（适用于ITMS现场安装）
Author: 王森博
Version: 1.0.1
Date: 2010-4-30
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<s:if test="deviceList!=null">
		<tr>
			<td class="green_title" width="5%">
				选择
			</td>
			<td class="green_title" width="10%">
				设备厂商
			</td>
			<td class="green_title" width="10%">
				型号
			</td>
			<td class="green_title" width="15%">
				软件版本
			</td>
			<td class="green_title" width="20%">
				设备序列号
			</td>
			<td class="green_title" width="10%">
				域名或IP
			</td>
			<td class="green_title" width="8%">
				终端类型
			</td>
			<td class="green_title" width="8%">
				属地
			</td>
			<td class="green_title" width="10%">
				绑定状态
			</td>
			<td class="green_title" width="10%">
				操作
			</td>
		</tr>



		<s:iterator value="deviceList" var="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>',
											'<s:property value="IpCity_id"/>',
											'<s:property value="cpe_allocatedstatus"/>',
											'<s:property value="flag"/>',
											'<s:property value="manage"/>',
											'<s:property value="device_type"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="vendor_add" />
				</td>
				<td class=column align="center">
					<s:property value="device_model" />
				</td>
				<td class=column align="center">
					<s:property value="softwareversion" />
				</td>
				<td class=column align="center">
					<s:property value="oui" />
					-
					<s:property value="device_serialnumber" />
				</td>
				<td class=column align="center">
					<s:property value="loopback_ip" />
				</td>
				<td class=column align="center">
					<s:property value="device_type" />
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
				<TD class=column align="center">
					<IMG SRC="<s:url value="../../images/inmp/view.gif"/>" BORDER="0" ALT="详细信息"
						onclick="DetailDevice('<s:property value="device_id"/>')"
						style="cursor: hand">
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
