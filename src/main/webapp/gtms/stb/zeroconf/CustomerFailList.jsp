<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>零配置BSS工单统计</title>
<lk:res />
<script type="text/javascript">
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});

	function cusomerDetail(customerId, deviceId) {
		var url = "<s:url value='/gtms/stb/resource/stbCustomer!queryCustomerDetail.action'/>?customerId="
				+ customerId + "&deviceId=" + deviceId;
		window
				.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}

	function deviceDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceDetail.action'/>?deviceId="
				+ deviceId;
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
				<th class="title_1">接入帐号</th>
				<th class="title_1">接入类型</th>
				<th class="title_1">机顶盒序列号</th>
				<th class="title_1">开户时间</th>
				<th class="title_1">开通状态</th>
				<th class="title_1">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="dtoList.size()>0">
				<s:iterator var="list" value="dtoList">
					<tr>
						<td><s:property value="#list.cityName" /></td>
						<td><s:property value="#list.servAccount" /></td>
						<td><s:property value="#list.pppoeUser" /></td>
						<td><s:property value="#list.addressingType" /></td>
						<td><a onclick="deviceDetail('<s:property value="#list.deviceId" />')" style="cursor:hand">
							<s:property value="#list.deviceSN" /></a></td>
						<td><s:property value="#list.openUserdate" /></td>
						<td><s:property value="#list.userStatus" /></td>
						<td><label
							onclick="javascript:cusomerDetail('<s:property value="#list.customerId" />','<s:property value="#list.deviceId" />');">
								<IMG SRC="/lims/images/view.gif" BORDER='0' ALT='详细信息'
								style='cursor: hand'>
						</label></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="8">
						<div style="text-align: center">查询无数据</div>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="dtoList.size()>0">
				<tr>
					<td class="foot" colspan="8">
						<div style="float: right">
							<lk:pages url="/gtms/stb/zeroconf/zeroConfStatisticsReportQuery!failListCustomer.action"
								isGoTo="true" />
						</div>
					</td>
				</tr>
			</s:if>
		</tfoot>
	</table>
</body>
</html>
