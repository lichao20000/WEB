<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����û������б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	function deviceDetail(deviceId) {
		var page = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?"
				+ "deviceId=" + deviceId;
		window
				.open(
						page,
						"",
						"left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

</head>
<body>
	<table class="listtable" width="98%" align="center">
		<thead>
			<tr>
				<th align="center">�豸����</th>
				<th align="center">�豸�ͺ�</th>
				<th align="center">����汾</th>
				<th align="center">����</th>
				<th align="center">�豸���к�</th>
				<th align="center">ҵ���ʺ�</th>
				<th align="center">MAC</th>
				<th align="center">�豸IP</th>
				<th align="center">��������</th>
				<th align="center">�״��ϱ�ʱ��</th>
				<th align="center">������״̬</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="deviceList">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="vendor_add" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="device_serialnumber" /></td>
					<td><s:property value="serv_account" /></td>
					<td><s:property value="cpe_mac" /></td>
					<td><s:property value="loopback_ip" /></td>
					<td><s:property value="addressing_type" /></td>
					<td><s:property value="complete_time" /></td>
					<td><a
						onclick="deviceDetail('<s:property value="device_id" />')"
						style="cursor: hand;color: blue;"> <s:property
								value="reason_desc" /></a></td>
				</tr>
			</s:iterator>
		</tbody>

		<tfoot>
			<tr bgcolor="#FFFFFF">

				<td colspan="12" align="right" nowrap="nowrap"><lk:pages
						url="/gtms/stb/zeroconf/zeroFailReasonQuery!zeroqueryDeviceList.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr STYLE="display: none">
				<td nowrap="nowrap"><iframe id="childFrm" src=""></iframe></td>
			</tr>
		</tfoot>
	</table>
</body>