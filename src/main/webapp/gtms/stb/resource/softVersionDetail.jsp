<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>软件版本详细信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<style>
span
{
	position:static;
	border:0;
}
</style>
</head>

<body>
<TABLE width="100%" class="querytable" align="center">
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >软件版本详细信息</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">版本id</td>
		<td width="35%"><s:property value='data.id'/></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">定制账号</td>
		<td width="35%"><s:property value='data.acc_loginname'/></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">定制时间</td>
		<td width="35%"><s:property value='data.add_time'/></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">编辑时间</td>
		<td width="35%"><s:property value='data.update_time'/></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">适用厂商</td>
		<td width="35%"><s:property value='data.vendor_name' /></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">适用型号</td>
		<td width="35%"><s:property value='data.device_models' /></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">版本名称</td>
		<td align="left" width="35%"><s:property value='data.version_name' /></td>
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">版本描述</td>
		<td width="35%"><s:property value='data.version_desc' /></td>	
	</tr>	
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">版本路径</td>
		<td width="35%"><s:property value='data.version_path' /></td>	
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">版本大小</td>
		<td width="35%"><s:property value='data.file_size' /></td>	
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">MD5</td>
		<td width="35%"><s:property value='data.md5' /></td>	
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">EPG版本</td>
		<td width="35%"><s:property value='data.epg_version' /></td>	
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">适用网络类型</td>
		<td width="35%"><s:property value='data.net_type' /></td>
	</tr>
</table>
</body>