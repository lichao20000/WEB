<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>零配置失败原因统计</title>
<lk:res />
<script type="text/javascript">
    var starttime = "<s:property value='starttime'/>";
    var endtime = "<s:property value='endtime'/>";
	$(function() {
		parent.showIframe();
		var h = $("body").attr("scrollHeight");
		parent.setDataSize(h + 50);
	});

	function zeroConfigReasonDetail(reasonId,cityId){
		var reasonId = $("#reason_"+reasonId).val();
		var cityId = $("#cityId_"+cityId).val();
		var url = "<s:url value='/gtms/stb/resource/zeroConfigFailReason!queryZeroConfigFailReasonDetail.action'/>?reasonId=" + reasonId +
				"&cityId="+cityId + "&starttime=" + starttime + "&endtime=" + endtime;
		window.open(url, "","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>

	<table width="100%" class="listtable" align="center"
		style="margin-top: 10px;">

		<!-- 城市名称 -->
		<s:iterator var="list" value="dataString" begin="0" end="0">
		<thead>
			<tr>
			<s:iterator var="title" value="#list" status="status">
			<s:if test="#status.index != 1">
				<th class="title_1"><s:property value = "#title"/>
				</th>
			</s:if>
			</s:iterator>
			</tr>
		</thead>

		<!-- 城市ID -->
		<s:iterator var="list" value="dataString" begin="1" end="1">
		<s:iterator var="title" value="#list" status="status">
			<input type="hidden" id="cityId_<s:property value='#status.index'/>" value="<s:property value = '#title'/>">
		</s:iterator>
		</s:iterator>

		<!-- 数据 -->
		</s:iterator>
		<s:iterator var="list" value="dataString" begin="2" status="status">
		<tbody>
			<tr>
			<s:if test="#status.last">
			<s:iterator var="trData" value="#list" status="lastStatus">
			<s:if test="#lastStatus.index != 1">
			<td>
			<s:property value="#trData" />
			</td>
			</s:if>
			</s:iterator>
			</s:if>
			<s:else>
			<s:iterator var="trData" value="#list" status="childStatus">
			<s:if test="#childStatus.index == 1">
			<input type="hidden" id="reason_<s:property value='#status.index' />" value="<s:property value='#trData' />"/>
			</s:if>
			<s:else>
			<td>
			<s:if test="#childStatus.index == 0">
			<s:property value="#trData" />
			</s:if>
			<s:elseif test="#childStatus.index == 2">
			<s:property value="#trData" />
			</s:elseif>
			<s:elseif test="#childStatus.last">
			<s:property value="#trData" />
			</s:elseif>
			<s:else>
			<a href="javascript:zeroConfigReasonDetail(<s:property value='#status.index' />,<s:property value='#childStatus.index' />);"><s:property value="#trData" /></a>
			</s:else>
			</td>
			</s:else>
			</s:iterator>
			</s:else>
			</tr>
		</tbody>
		</s:iterator>
	</table>
</body>
</html>
