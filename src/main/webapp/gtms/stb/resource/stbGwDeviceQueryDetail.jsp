<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>

<style>
span
{
	position:static;
	border:0;
}
</style>
</head>

<body>
<form name="frm" id="frm" >
<TABLE width="100%" class="querytable" align="center">
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >机顶盒信息</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备序列号</td>
		<td width="35%">
			<s:property value='data.deviceSerialnumber'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">OUI</td>
		<td width="35%">
			<s:property value='data.oui'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">MAC地址</td>
		<td width="35%">
			<s:property value='data.cpe_mac' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">厂 商</td>
		<td width="35%">
			<s:property value='data.vendorAdd' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备型号</td>
		<td align="left" width="35%">
			<s:property value='data.deviceModel' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">硬件版本</td>
		<td width="35%">
			<s:property value='data.hardwareversion' />
		</td>	
	</tr>	
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">软件版本</td>
		<td width="35%">
			<s:property value='data.softwareversion' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">EPG版本</td>
		<td width="35%">
			<s:property value='data.epg_version' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">当前版本适用网络类型</td>
		<td width="35%">
			<s:if test="data.net_type=='public_net'">公网</s:if>
			<s:elseif test="data.net_type=='private_net'">专网</s:elseif>
			<s:else>未知</s:else>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">属地</td>
		<td width="35%">
			<s:property value='data.cityName' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">首次登录时间</td>
		<td width="35%">
			<s:property value='data.completeTime' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">最近登录时间</td>
		<td width="35%">
			<s:property value='data.cpe_currentupdatetime' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">备 注</td>
		<td width="35%">
			<s:property value='data.remark' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">认证APK版本号</td>
		<td width="35%">
			<s:property value='data.apk_version_code' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">认证APK版本名</td>
		<td width="35%">
			<s:property value='data.apk_version_name' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备IP</td>
		<td width="35%">
			<s:property value='data.loopback_ip' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">IP类型</td>
		<td width="35%">
			<s:if test="data.ip_type=='public_net'">公网</s:if>
			<s:elseif test="data.ip_type=='private_net'">专网</s:elseif>
			<s:else>未知</s:else>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">接入类型</td>
		<td width="35%">
			<s:property value='data.network_type' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">网络类型</td>
		<td width="35%">
			<s:property value='data.addressing_type' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">公网IP地址</td>
		<td width="35%">
			<s:property value='data.public_ip' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">公网IP属地</td>
		<td width="35%">
			<s:property value='data.public_area' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">公网IP运营商</td>
		<td width="35%">
			<s:property value='data.public_isp_name' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备类别</td>
		<td width="35%">
			<s:if test="data.category == 0">行</s:if>
			<s:if test="data.category == 1">串</s:if>
			<s:if test="data.category == 2">未知</s:if>
		</td>
	</tr>
</table>
</form>
</body>