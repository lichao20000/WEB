<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������ϱ���¼��Ϣ</title>
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
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >�������ϱ���¼��Ϣ</td></tr>
	
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸���к�</td>
		<td width="35%">
			<s:property value='data.get(0).sn'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">MAC��ַ</td>
		<td width="35%">
			<s:property value='data.get(0).mac' />
		</td>
	</tr>
	
	<s:if test="data!=null && data.size()>0">
	<s:iterator value="data">
	<table width="100%" class="querytable" align="center">
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����IP��ַ</td>
			<td width="35%">
				<s:property value='login_ip' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����ʱ��</td>
			<td align="left" width="35%">
				<s:property value='request_time' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">	
			<td align="right" class="column" width="15%">�����־</td>
			<td width="35%">
				<s:property value='stbid' />
			</td>	
		</tr>	
		<tr bgcolor="#FFFFFF">	
			<td align="right" class="column" width="15%">��������</td>
			<td width="35%">
				<s:property value='event_id' />
			</td>	
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">��������������</td>
			<td width="35%">
				<s:property value='event_num' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">��֤APK�汾</td>
			<td width="35%">
				<s:property value='apk_version_name' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">���������</td>
			<td width="35%">
				<s:property value='server_host' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����˶˿�</td>
			<td width="35%">
				<s:property value='server_port' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����˰汾</td>
			<td width="35%">
				<s:property value='server_version' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����ҵ���˺�</td>
			<td width="35%">
				<s:property value='request_username' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">����ҵ���˺�</td>
			<td width="35%">
				<s:property value='result_username' />
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">���ش�����</td>
			<td width="35%">
				<s:property value='result_code' />
			</td>
		</tr>
	</table>
	<br/>
	</s:iterator>
	</s:if>
</table>
</body>