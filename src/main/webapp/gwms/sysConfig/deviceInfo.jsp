<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<tr>
		<td class="green_title" width="5%">
			选择
		</td>
		<td class="green_title" width="15%">
			设备厂商
		</td>
		<td class="green_title" width="15%">
			型号
		</td>
		<td class="green_title" width="15%">
			软件版本
		</td>
		<td class="green_title" width="25%">
			设备序列号
		</td>
		<td class="green_title" width="15%">
			域名或IP
		</td>
		<td class="green_title" width="10%">
			详细信息
		</td>
	</tr>


	<s:if test="deviceList!=null">
		<s:iterator value="deviceList" var="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>')" />
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
					<s:property value="oui" />-<s:property value="device_serialnumber" />
				</td>
				<td class=column align="center">
					<s:property value="loopback_ip" />
				</td>
				<TD class=column align="center">
					<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
						onclick="DetailDevice('<s:property value="device_id"/>')"
						style="cursor: hand">
				</TD>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="7">
				<font color="red">未查到符合条件的设备！</font>
			</td>
		</tr>
	</s:else>

</table>
