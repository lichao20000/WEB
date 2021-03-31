<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒零配置参数信息</title>
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
<table width="100%" class="querytable" align="center">
	<tr bgcolor="#FFFFFF"><td colspan="4" class="title_1" >机顶盒零配置参数信息</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备序列号</td>
		<td>
			<s:property value='paramMap.sn'/>
		</td>
		<td align="right" class="column" width="15%">属地</td>
		<td>
			<s:property value='paramMap.city_name'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">设备厂商</td>
		<td>
			<s:property value='paramMap.vendor_name'/>
		</td>
		<td align="right" class="column" width="15%">型号</td>
		<td>
			<s:property value='paramMap.device_model' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">MAC地址</td>
		<td>
			<s:property value='paramMap.mac'/>
		</td>
		<td align="right" class="column" width="15%">业务账号</td>
		<td>
			<s:property value='paramMap.serv_account' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">主认证服务地址</td>
		<td>
			<s:property value='paramMap.auth_server'/>
		</td>
		<td align="right" class="column" width="15%">备认证服务地址</td>
		<td>
			<s:property value='paramMap.auth_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">认证服务心跳周期</td>
		<td>
			<s:property value='paramMap.auth_server_conn_peroid'/>
		</td>
		<td align="right" class="column" width="15%">命令服务心跳周期</td>
		<td>
			<s:property value='paramMap.cmd_server_conn_peroid' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">主零配服务地址</td>
		<td>
			<s:property value='paramMap.zeroconf_server'/>
		</td>
		<td align="right" class="column" width="15%">备零配服务地址</td>
		<td>
			<s:property value='paramMap.zeroconfig_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">主命令服务地址</td>
		<td>
			<s:property value='paramMap.cmd_server'/>
		</td>
		<td align="right" class="column" width="15%">备命令服务地址</td>
		<td>
			<s:property value='paramMap.cmd_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">主时钟同步服务地址</td>
		<td>
			<s:property value='paramMap.ntp_server_main'/>
		</td>
		<td align="right" class="column" width="15%">备时钟同步服务地址</td>
		<td>
			<s:property value='paramMap.ntp_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">自动待机开关</td>
		<td>
			<s:property value='paramMap.auto_sleep_mode'/>
		</td>
		<td align="right" class="column" width="15%">自动待机关闭时长</td>
		<td>
			<s:property value='paramMap.auto_sleep_time' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">有线网络类型</td>
		<td>
			<s:property value='paramMap.ip_protocol_version_lan'/>
		</td>
		<td align="right" class="column" width="15%">无线网络类型</td>
		<td>
			<s:property value='paramMap.ip_protocol_version_wifi' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">最后修改时间</td>
		<td>
			<s:property value='paramMap.update_time'/>
		</td>
		<td align="right" class="column" width="20%">最后被零配置下发生效的时间</td>
		<td>
			<s:property value='paramMap.set_stb_time' />
		</td>
	</tr>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</table>
</body>