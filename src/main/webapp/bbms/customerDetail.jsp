<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="../head.jsp"%>

<link href="<s:url value='/css/css_green.css'/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

function DetailUserInfo(user_id){
	var strpage = "<s:url value='/gwms/blocTest/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function DetailDevice(device_id){
	var strpage = "<s:url value='/gwms/blocTest/DeviceShow.jsp'/>?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

</SCRIPT>

<form action="">
<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="99%" align="center"
				bgcolor="#999999">
			<tr><th colspan="4">��ϸ��Ϣ</th></tr>
				<tr bgcolor="#FFFFFF">
				<td class=column width="15%" nowrap align="right">�ͻ�����</td>
				<td width="35%"><s:property value="customerMap.customer_name"/></td>
				<td class=column nowrap align="right">����</td>
				<td><s:property value="customerMap.city_id"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column width="15%"  nowrap align="right">�ͻ��˺�</td>
				<td width="35%"><s:property value="customerMap.customer_account"/></td>
				<td class=column width="15%"  nowrap align="right">�ͻ�����</td>
				<td width="35%"><s:property value="customerMap.customer_pwd"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">�ͻ�״̬</td>
				<td><s:property value="customerMap.customer_state"/></td>
				<td class=column nowrap align="right">�ͻ�����</td>
				<td><s:property value="customerMap.customer_type"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">�ͻ���ģ</td>
				<td><s:property value="customerMap.customer_size"/></td>
				<td class=column nowrap align="right">E-mail</td>
				<td ><s:property value="customerMap.email"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">��ϵ��</td>
				<td><s:property value="customerMap.linkman"/></td>
				<td class=column nowrap align="right">��ϵ�绰</td>
				<td><s:property value="customerMap.linkphone"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">����</td>
				<td><s:property value="customerMap.office_id"/></td>
				<td class=column nowrap align="right">С��</td>
				<td><s:property value="customerMap.zone_id"/></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">�ͻ���ַ</td>
				<td><s:property value="customerMap.customer_address"/></td>
				<td class=column nowrap align="right">��ͨ��WAN����</td>
				<td><s:property value="user_num"/>������</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td class=column nowrap align="right">��ͨʱ��</td>
				<td><s:property value="customerMap.opendate"/></td>
				<td class=column nowrap align="right"> </td>
				<td> </td>
			</tr>
			</table>
		</td>
	</tr>
			<TR bgcolor="#FFFFFF">
				<TD height="20" colspan="4"></TD>
			</TR>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="99%" align="center" bgcolor="#999999">
			<tr bgcolor="#FFFFFF">
				<TH colspan=5 nowrap>�豸��Ϣ</TH>
			</tr>
				<tr bgcolor="#FFFFFF">
					<td class=green_title2 width="20%">�û��ʺ�</td>
					<td class=green_title2 width="10%">OUI</td>
					<td class=green_title2 width="20%">���к�</td>
					<td class=green_title2 width="15%">�豸�ͺ�</td>
					<td class=green_title2 width="15%">�豸IP</td>
					<!-- <td class=green_title2 width="20%">�豸��װ��ַ</td> -->
				</tr>
				<s:iterator value="userInfoList">
				<tr bgcolor="#FFFFFF">
					<td><a onclick="DetailUserInfo('<s:property value="user_id"/>')" href="javascript:"><s:property value="username"/></a></td>
					<td><s:property value="oui"/></td>
					<td><a onclick="DetailDevice('<s:property value="device_id"/>')" href="javascript:"><s:property value="device_serialnumber"/></a></td>
					<td><s:property value="device_model"/></td>
					<td><s:property value="loopback_ip"/></td>
					<!-- <td><s:property value="device_addr"/></td> -->
				</tr>
				</s:iterator>
				<tr bgcolor="#FFFFFF">
					<td colspan="5" align="right" class="green_foot"><input type="button" class="jianbian" value=" �� �� " onclick="window.close()"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>