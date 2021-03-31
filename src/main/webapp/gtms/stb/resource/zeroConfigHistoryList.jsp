<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<title>零配置用户配置历史查询</title>
<script type="text/javascript">
	var starttime = "<s:property value='starttime'/>";
	var endtime = "<s:property value='endtime'/>";
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});
	function configDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/zeroConfigHistory!doDeviceZeroHistoryQuery.action'/>?deviceId=" + deviceId
				+ "&starttime=" + starttime + "&endtime=" + endtime;
		window.open(url, "","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
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
				<th class="title_1">设备序列号</th>
				<th class="title_1">绑定时间</th>
				<th class="title_1">业务类型</th>
				<th class="title_1">当前阶段</th>
				<th class="title_1">状态</th>
				<th class="title_1" colspan="1">流程跟踪</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data.size()>0">
				<s:iterator var="list" value="data">
					<tr>
						<td><s:property value="#list.city_name" /></td>
						<td><s:property value="#list.serv_account" /></td>
						<td><s:property value="#list.device_serialnumber" /></td>
						<td><s:property value="#list.bind_time" /></td>
						<td><s:property value="#list.config_type" /></td>
						<td><s:property value="#list.bind_way" /></td>
						<td><s:property value="#list.bind_state" /></td>
						<td align="center"><label
							onclick="javascript:configDetail('<s:property value="#list.device_id " />');">
								<IMG SRC="../../../images/view.gif" BORDER='0' ALT='详细信息'
								style='cursor: hand'/>
						</label></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="10">
						<div style="text-align: center">查询无数据</div>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<s:if test="data.size()>0">
				<tr>

					<td class="foot" colspan="10">
						<div style="float: right">
							<lk:pages url="/gtms/stb/resource/zeroConfigHistory!doZeroHistoryQuery.action?servAccount=<s:property value='servAccount'/>
							&deviceSerialnumber=<s:property value='deviceSerialnumber'/>&start=<s:property value='starttime'/>&end=<s:property value='endtime'/>"
								isGoTo="true"/>
						</div>
					</td>
				</tr>
			</s:if>
		</tfoot>
	</table>
</body>
</html>
