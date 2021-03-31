<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>手工业务下发结果展示</title>
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
				<th class="title_1">属地</th>
				<th class="title_1">业务账号</th>
				<th class="title_1">接入类型</th>
				<th class="title_1">机顶盒序列号</th>
				<th class="title_1">开户时间</th>
				<th class="title_1">开通状态</th>
				<th class="title_1">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="userList.size()>0">
				<s:iterator var="list" value="userList">
					<tr>
						<td align="center"><s:property value="#list.cityName" /></td>
						<td align="center"><s:property value="#list.serv_account" /></td>
						<td align="center"><s:property value="#list.addressing_type" /></td>
						<td align="center"><s:property value="#list.device_serialnumber" /></td>
						<td align="center"><s:property value="#list.openUserdate" /></td>
						<td align="center"><s:property value="#list.userStatus" /></td>
						<td align="center">
							<s:if test="#list.user_status==1">
								<a onclick="config('<s:property value="#list.device_id" />','<s:property value="#list.device_serialnumber" />')" style="cursor:hand">配置信息</a>
							</s:if>
							<s:elseif test="#list.user_status==-1">
								<a onclick="config('<s:property value="#list.device_id" />','<s:property value="#list.device_serialnumber" />')" style="cursor:hand">失败原因</a>
							</s:elseif>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="8">
						<div style="text-align: center">设备未绑定</div>
					</td>
				</tr>
			</s:else>
		</tbody>
	</table>
</body>
</html>
