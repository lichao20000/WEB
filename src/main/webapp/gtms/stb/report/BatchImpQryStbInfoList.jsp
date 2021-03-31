<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import= "java.util.*"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script>
$(function() {
		parent.dyniframesize();
		$("#querybtn",parent.document).attr("disabled","");
});
	



function ToExcel()
{
	var page="<s:url value='/gtms/stb/report/batchImpQryStbInfo!getExcelStb.action'/>"+"?importQueryField="+$("#importQueryField").val();
	document.all("childFrm").src=page;
}

// 获取父页面的查询条件
function getCondition()
{
	deviceType = window.parent.document.getElementsByName("deviceType")[0].value;
	startOpenDate = window.parent.document.getElementsByName("startOpenDate")[0].value;
	endOpenDate = window.parent.document.getElementsByName("endOpenDate")[0].value;
	servTypeId = window.parent.document.getElementsByName("servTypeId")[0].value;
}
</script>
<html>
<body>
<table class="listtable">
	<caption>查询结果</caption>
	<s:if test='msg=="succ"'>
	<thead>
		<tr>
			<th>账号</th>
			<th>设备序列号</th>
			<th>属地</th>
			<th>厂家</th>
			<th>型号</th>
			<th>软件版本</th>
			<th>硬件版本</th>
			<th>最近上线时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="statsReportList!=null">
			<s:if test="statsReportList.size()>0">
				<s:iterator value="statsReportList">
					<tr>
						<td align="center"><s:property value="serv_account" /></td>
						<td align="center"><s:property value="device_serialnumber" /></td>
						<td align="center"><s:property value="city_id" /></td>
						<td align="center"><s:property value="vendor_name" /></td>
						<td align="center"><s:property value="device_model" /></td>
						<td align="center"><s:property value="softwareversion" /></td>
						<td align="center"><s:property value="hardwareversion" /></td>
						<td align="center"><s:property value="cpe_currentupdatetime" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>没有查询到相关信息！</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages url="/gtms/stb/report/batchImpQryStbInfo!impStb.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="10" align="left">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">导出
				</td>
			</tr>
			<tr STYLE="display: none">
				<td>
					<iframe id="childFrm" src=""></iframe>
					<input id="importQueryField" value='<s:property value="importQueryField" />'>
				</td>
			</tr>
		</tfoot>
	</tbody>
	</s:if>
	<s:else>
		<tr>
				<td colspan=6>错误信息：<s:property value="msg" /></td>
		</tr>
	</s:else>
</table>
</body>
</html>
