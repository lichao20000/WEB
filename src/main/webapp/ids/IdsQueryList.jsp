<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<html>
<script type="text/javascript">
	$(function() {
		$("#QueryData",parent.document).html("");
		$("#trData",parent.document).attr("disabled",true);
		$("#btn",parent.document).attr("disabled",false);
		parent.dyniframesize();
	});
		/* function openHgw(taskId){
			var page="<s:url value='/ids/idsStatusQuery!getDevInfo.action'/>?taskId=" + taskId ;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		} */
		function ToExcel(){
			var mainForm = parent.document.getElementById("selectForm");
			mainForm.action = "<s:url value='/ids/idsQuery!doQueryExcel.action'/>";
			mainForm.submit();
 			mainForm.action = "<s:url value='/ids/idsQuery!doQueryStatus.action'/>";
	}
	</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>上报信息任务定制信息列表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
</head>
<body>
	<table class="listtable">
		<caption>上报信息查询结果</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>设备序列号</th>
				<th>厂商</th>
				<th>型号</th>
				<th>软件版本</th>
				<th>硬件版本</th>
				<th>上报时间</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="IdsList.size()>0">
				<s:iterator value="IdsList" var="map1">
					<tr>
						<td align="center"><s:property value="loid" /></td>
						<td align="center"><s:property value="device_serialnumber" /></td>
						<td align="center"><s:property value="vendor_name" /></td>
						<td align="center"><s:property value="device_model" /></td>
						<td align="center"><s:property value="softwareversion" /></td>
						<td align="center"><s:property value="hardwareversion" /></td>
						<td align="center"><s:property value="upload_time" /></td>
						<td align="center"><s:property value="state" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tfoot>
					<tr>
						<td align="left" colspan="8">没有定制信息</td>
					</tr>
				</tfoot>
			</s:else>
		</tbody>
		<tfoot>
		</tfoot>
		<tr>
			<td colspan="8" align="right"><lk:pages
					url="/ids/idsQuery!doQueryStatus.action" styleClass=""
					showType="" isGoTo="false" changeNum="false" /></td>

		</tr>
		<tr>
			<td colspan="8"><IMG SRC="/itms/images/excel.gif" BORDER=0
				ALT="导出列表" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</table>
</body>
</html>
