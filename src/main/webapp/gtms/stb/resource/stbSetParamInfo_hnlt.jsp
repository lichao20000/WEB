<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����������ò�����Ϣ</title>
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
	<tr bgcolor="#FFFFFF"><td colspan="4" class="title_1" >�����������ò�����Ϣ</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸���к�</td>
		<td>
			<s:property value='paramMap.sn'/>
		</td>
		<td align="right" class="column" width="15%">����</td>
		<td>
			<s:property value='paramMap.city_name'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸����</td>
		<td>
			<s:property value='paramMap.vendor_name'/>
		</td>
		<td align="right" class="column" width="15%">�ͺ�</td>
		<td>
			<s:property value='paramMap.device_model' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">MAC��ַ</td>
		<td>
			<s:property value='paramMap.mac'/>
		</td>
		<td align="right" class="column" width="15%">ҵ���˺�</td>
		<td>
			<s:property value='paramMap.serv_account' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����֤�����ַ</td>
		<td>
			<s:property value='paramMap.auth_server'/>
		</td>
		<td align="right" class="column" width="15%">����֤�����ַ</td>
		<td>
			<s:property value='paramMap.auth_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��֤������������</td>
		<td>
			<s:property value='paramMap.auth_server_conn_peroid'/>
		</td>
		<td align="right" class="column" width="15%">���������������</td>
		<td>
			<s:property value='paramMap.cmd_server_conn_peroid' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����������ַ</td>
		<td>
			<s:property value='paramMap.zeroconf_server'/>
		</td>
		<td align="right" class="column" width="15%">����������ַ</td>
		<td>
			<s:property value='paramMap.zeroconfig_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����������ַ</td>
		<td>
			<s:property value='paramMap.cmd_server'/>
		</td>
		<td align="right" class="column" width="15%">����������ַ</td>
		<td>
			<s:property value='paramMap.cmd_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��ʱ��ͬ�������ַ</td>
		<td>
			<s:property value='paramMap.ntp_server_main'/>
		</td>
		<td align="right" class="column" width="15%">��ʱ��ͬ�������ַ</td>
		<td>
			<s:property value='paramMap.ntp_server_bak' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�Զ���������</td>
		<td>
			<s:property value='paramMap.auto_sleep_mode'/>
		</td>
		<td align="right" class="column" width="15%">�Զ������ر�ʱ��</td>
		<td>
			<s:property value='paramMap.auto_sleep_time' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">������������</td>
		<td>
			<s:property value='paramMap.ip_protocol_version_lan'/>
		</td>
		<td align="right" class="column" width="15%">������������</td>
		<td>
			<s:property value='paramMap.ip_protocol_version_wifi' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����޸�ʱ��</td>
		<td>
			<s:property value='paramMap.update_time'/>
		</td>
		<td align="right" class="column" width="20%">����������·���Ч��ʱ��</td>
		<td>
			<s:property value='paramMap.set_stb_time' />
		</td>
	</tr>
	<TR>
		<TD colspan="4" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
</table>
</body>