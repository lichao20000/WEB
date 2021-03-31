<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
function openCust(){
		var customerId = $.trim($("input[@name='bind_customer_id']").val());
		if(customerId==null){
			alert("�豸û�а��û�!");
			return false;
		}
		var strpage="<s:url value='/bbms/CustomerInfo!detailInfo.action'/>?customer_id=" + customerId;
		window.open(strpage,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}
	
	function openDevice(){
		var deviceId = $.trim($("input[@name='bind_device_id']").val());
		if(deviceId==null){
			alert("�豸������!");
			return false;
		}
		var strpage="<s:url value='/gwms/blocTest/DeviceShow.jsp'/>?gw_type=2&device_id=" + deviceId;
		window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
	}
	
	function openUser(){
		var userId = $.trim($("input[@name='bind_user_id']").val());
		if(userId==null){
			alert("�豸û�а��û�!");
			return false;
		}
		var strpage="<s:url value='/gwms/blocTest/EGWUserRelatedInfo.jsp'/>?user_id=" + userId;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</script>

</head>

<body>
<table width="100%" border=0 cellspacing=1 cellpadding=2 align="center" bgcolor=#999999>

	<s:if test="customerList.size()>0">
		<s:iterator value="customerList">
			<tr bgcolor="#FFFFFF">
				<td width="15%" class="column" align="right">�豸���к�</td>
				<td width="35%"><a href="javascript:openDevice()"><s:property value="device_serialnumber" /></a></td>
				<td width="15%" class="column" align="right">�û�����</td>
				<td width="35%"><a href="javascript:openUser()"><s:property value="username" /></a></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="15%" class="column" align="right">�ͻ�����</td>
				<td width="35%"><a href="javascript:openCust()"><s:property value="customer_name" /></a></td>
				<td width="15%" class="column" align="right">��ϵ�绰</td>
				<td width="35%"><s:property value="linkphone" /></td>
			</tr>

			<tr bgcolor="#FFFFFF">
				<td width="15%" class="column" align="right">װ����ַ</td>
				<td width="35%"><s:property value="customer_address" /></td>
				<td width="15%" class="column" align="right">����Ϣ</td>
				<td width="35%"><s:property value="zone_id" /></td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="15%" class="column" align="right">����Ϣ</td>
				<td width="35%"><s:property value="office_id" /></td>
                <td width="15%" class="column" align="right">�������</td>
				<td width="35%"><s:property value="bandwidth" /><font size="2">kb</font></td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td id="td1" colspan=8>�豸û�а��û� ���û��Ϳͻ���Ϣ��</td>
		</tr>
	</s:else>


</table>
<form><s:iterator value="customerList">
	<input type="hidden" id="bind_device_id" name="bind_device_id"
		value="<s:property value="device_id" />"></input>
	<input type="hidden" id="bind_user_id" name="bind_user_id"
		value="<s:property value="user_id" />"></input>
	<input type="hidden" id="bind_customer_id" name="bind_customer_id"
		value="<s:property value="customer_id" />"></input>
</s:iterator></form>
</body>
<script>

</script>
</html>