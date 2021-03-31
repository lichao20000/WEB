<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>������Ϣչʾ</title>
<lk:res />
<script type="text/javascript">

	function cusomerDetail(customerId, deviceId, status) {
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!queryCustomerDetail.action'/>?customerId="
				+ customerId + "&deviceId=" + deviceId + "&status=" + status;
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}

	function deviceDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="
				+ deviceId + "&gw_type=4";
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">
		<thead>
			<tr>
				<th colspan=5>�豸 <s:property value="deviceSN" /> ������Ϣ</th>
			</tr>
			<tr>
				<th>ִ��ʱ��</th>
				<th>�·�ʱ��</th>
				<th>����ִ��״̬</th>
				<th>�������</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="configList!=null">
			<s:if test="configList.size()>0">
				<s:iterator value="configList">
					<tr>
						<td align=center><s:property value="start_time" /></td>
						<td align=center><s:property value="end_time"/></td>
						<td align=center><s:property value="status" /></td>
						<td align=center><s:property value="result_desc" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>
						<s:if test='openstatus=="1"'>
							�豸��������Ҫ�󣬲����·�����!
						</s:if>
						<s:else>
							ϵͳû��������Ϣ!
						</s:else>
					</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д�ҵ����Ϣ!</td>
			</tr>
		</s:else>
		</tbody>
		<tfoot>
		<tr>
			<td colspan="6" align="right"><a
				href="javascript:configInfoClose()">�ر�</a></td>
		</tr>
	</tfoot>
	</table>
</body>
</html>
