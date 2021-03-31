<%--
设备查询界面（IPOSS现场安装）
Author: 王森博
Version: 1.0.0
Date: 2009-11-12
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
	<tr>
		<td class="green_title" width="5%">
			选择
		</td>
		<td class="green_title" width="20%">
			设备厂商
		</td>
		<td class="green_title" width="15%">
			设备oui
		</td>
		<td class="green_title" width="20%">
			型号
		</td>
		<td class="green_title" width="15%">
			软件版本
		</td>
		<td class="green_title" width="25%">
			设备序列号
		</td>
	</tr>

	<tr bgcolor="#FFFFFF">
		<s:if test="deviceList!=null">
		<s:iterator value="deviceList" var="deviceList">
			<td align="center">
				<input type=radio name="radioDeviceId" value="1"
					onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>')"/>
			</td>
			<td align="center">
				<s:property value="vendor_add" />
			</td>
			<td align="center">
				<s:property value="oui" />
			</td>
			<td align="center">
				<s:property value="device_model" />
			</td>
			<td align="center">
				<s:property value="softwareversion" />
			</td>
			<td align="center">
				<s:property value="device_serialnumber" />
			</td>
		</s:iterator>
		</s:if>
		<s:else>
			<td colspan="7">
				<font color="red">未查到符合条件的设备！该设备有可能不存在或者已绑定！</font>
			</td>
		</s:else>
	</tr>
</table>
