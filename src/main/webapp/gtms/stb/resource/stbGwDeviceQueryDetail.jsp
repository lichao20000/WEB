<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������Ϣ</title>
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
	<tr bgcolor="#FFFFFF"><td colspan="2" class="title_1" >��������Ϣ</td></tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸���к�</td>
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
		<td align="right" class="column" width="15%">MAC��ַ</td>
		<td width="35%">
			<s:property value='data.cpe_mac' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�� ��</td>
		<td width="35%">
			<s:property value='data.vendorAdd' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸�ͺ�</td>
		<td align="left" width="35%">
			<s:property value='data.deviceModel' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">Ӳ���汾</td>
		<td width="35%">
			<s:property value='data.hardwareversion' />
		</td>	
	</tr>	
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">����汾</td>
		<td width="35%">
			<s:property value='data.softwareversion' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">	
		<td align="right" class="column" width="15%">EPG�汾</td>
		<td width="35%">
			<s:property value='data.epg_version' />
		</td>	
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��ǰ�汾������������</td>
		<td width="35%">
			<s:if test="data.net_type=='public_net'">����</s:if>
			<s:elseif test="data.net_type=='private_net'">ר��</s:elseif>
			<s:else>δ֪</s:else>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����</td>
		<td width="35%">
			<s:property value='data.cityName' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�״ε�¼ʱ��</td>
		<td width="35%">
			<s:property value='data.completeTime' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�����¼ʱ��</td>
		<td width="35%">
			<s:property value='data.cpe_currentupdatetime' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�� ע</td>
		<td width="35%">
			<s:property value='data.remark' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��֤APK�汾��</td>
		<td width="35%">
			<s:property value='data.apk_version_code' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��֤APK�汾��</td>
		<td width="35%">
			<s:property value='data.apk_version_name' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸IP</td>
		<td width="35%">
			<s:property value='data.loopback_ip' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">IP����</td>
		<td width="35%">
			<s:if test="data.ip_type=='public_net'">����</s:if>
			<s:elseif test="data.ip_type=='private_net'">ר��</s:elseif>
			<s:else>δ֪</s:else>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��������</td>
		<td width="35%">
			<s:property value='data.network_type' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">��������</td>
		<td width="35%">
			<s:property value='data.addressing_type' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����IP��ַ</td>
		<td width="35%">
			<s:property value='data.public_ip' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����IP����</td>
		<td width="35%">
			<s:property value='data.public_area' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">����IP��Ӫ��</td>
		<td width="35%">
			<s:property value='data.public_isp_name' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">�豸���</td>
		<td width="35%">
			<s:if test="data.category == 0">��</s:if>
			<s:if test="data.category == 1">��</s:if>
			<s:if test="data.category == 2">δ֪</s:if>
		</td>
	</tr>
</table>
</form>
</body>