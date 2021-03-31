<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>单台设备信息列表</title>
<style type="text/css">
	.col{
		width: 25%;
		font-size: 14px;
		background-color: #F4F4FF;
	}
	.col1{
		width: 25%;
		font-size: 14px;
	}
	.td{
		border-bottom:solid 1px #000;
	}
</style>
</head>
<body>
	<s:iterator value="deviceList">
		<input type="hidden" name="deviceSerialnumber" value="<s:property value="device_serialnumber"/>">
		<input type="hidden" name="deviceId" value="<s:property value="device_id"/>">
		<table align="center" width="100%" border="1" cellspacing="0" cellpadding="0" class="listtable" style="line-height: 22px;border-collapse:collapse;border-color: #F4F4FF;">
			<tr bgcolor="#FFFFFF" align="center">
				<th align="center" width="100%" colspan="6">
					基本信息
				</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">设备ID</td>
				<td colspan="2" class="col1">
					<s:property value="device_id"/>
				</td>
				<td colspan="1" class="col">设备型号</td>
				<td colspan="2" class="col1">
					<s:property value="device_model"/>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">设备厂商</td>
				<td colspan="2" class="col1">
					<s:property value="vendor_name"/>
				</td>
				<td colspan="1" class="col">设备序列号</td>
				<td colspan="2" class="col1">
					<input type="hidden" name="device_serialnumber" value="<s:property value='device_serialnumber'/>">
					<s:property value="device_serialnumber"/>
				</td>
			</tr >
			<tr>
				<td colspan="1" class="col">硬件版本</td>
				<td colspan="2" class="col1">
					<s:property value="hardwareversion"/>
				</td>
				<td colspan="1" class="col">软件版本</td>
				<td colspan="2" class="col1">
					<s:property value="softwareversion"/>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">关联LOID</td>
				<td colspan="2" class="col1">
					<s:property value="username"/>
				</td>
				<td colspan="1" class="col">属地</td>
				<td colspan="2" class="col1">
					<s:property value="city_name"/>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">协议类型</td>
				<td colspan="2" class="col1">
					<s:property value="protocol"/>
				</td>
				<td colspan="1" class="col">VLANID</td>
				<td colspan="2" class="col1">
					<s:property value="vlanid"/>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">主MGC服务器地址</td>
				<td colspan="2" class="col1">
					<s:property value="prox_serv"/>
				</td>
				<td colspan="1" class="col">端口</td>
				<td colspan="2" class="col1">
					<s:property value="prox_port"/>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="1" class="col">备MGC服务器地址</td>
				<td colspan="2" class="col1">
					<s:property value="stand_prox_serv"/>
				</td>
				<td colspan="1" class="col">端口</td>
				<td colspan="2" class="col1">
					<s:property value="stand_prox_port"/>
				</td>
			</tr>
		</table>
	</s:iterator>
</body>
</html>