<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����й�����Ϣ</title>
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
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >�����й�����Ϣ</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">����ID</td>
		<td>
			<s:property value='eserverInfo.bss_sheet_id'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">����ʱ��</td>
		<td>
			<s:property value='eserverInfo.receive_time'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">ҵ���˺�</td>
		<td>
			<s:property value='eserverInfo.username'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">����</td>
		<td>
			<s:property value='eserverInfo.city_name' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">MAC��ַ</td>
		<td>
			<s:property value='eserverInfo.mac'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">��������</td>
		<td>
			<s:property value='eserverInfo.server_type' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">������Դ</td>
		<td align="left">
			<s:property value='eserverInfo.from_id' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="10%">����</td>
		<td>
			<s:property value='eserverInfo.grid' />
		</td>	
	</tr>	
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="10%">����Ա</td>
		<td>
			<s:property value='eserverInfo.opertor' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">��������</td>
		<td>
			<s:property value='eserverInfo.sheet_context' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="10%">������Ϣ</td>
		<td>
			<s:property value='eserverInfo.returnt_context' />
		</td>
	</tr>
	<TR>
		<TD colspan="2" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> �� �� </button>
			</div>
		</TD>
	</TR>
</table>
</body>