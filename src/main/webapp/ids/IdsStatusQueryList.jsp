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
		function openHgw(taskId){
			var page="<s:url value='/ids/idsStatusQuery!getDevInfo.action'/>?taskId=" + taskId ;
			window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
		}
		function ToExcel(){
			var mainForm = parent.document.getElementById("selectForm");
			mainForm.action = "<s:url value='/ids/idsStatusQuery!doQueryStatusExcel.action' />";
			mainForm.submit();
			mainForm.action = "<s:url value='/ids/idsStatusQuery!doQueryStatus.action' />";
		}
	</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>状态信息任务定制信息列表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
</head>
<body>
	<table class="listtable">
		<caption>定制信息查询结果</caption>
		<thead>
			<tr>
				<th>定制人</th>
				<th>定制时间</th>
				<th>开启或关闭</th>
				<th>上报周期</th>
				<th>文件上传域名</th>
				<th>端口</th>
				<th>监控参数</th>
				<th>设备数</th>
			</tr>
		</thead>

		<tbody>
			<s:if test="IdsList.size()>0">
				<s:iterator value="IdsList" var="map1">
					<tr>
						<td align="center"><s:property value="acc_loginname" /></td>
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><s:property value="enable" /></td>
						<td align="center"><s:property value="timelist" /></td>
						<td align="center"><s:property value="serverurl" /></td>
						<td align="center"><s:property value="tftp_port" /></td>
						<td align="center"><s:property value="paralist" /></td>
						<td align="center"><a
							href="javascript:openHgw('<s:property value="taskId"/>')"> <s:property
									value="devCount" />
						</a></td>
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
					url="/ids/idsStatusQuery!doQueryStatus.action" styleClass=""
					showType="" isGoTo="true" changeNum="true" /></td>

		</tr>
		<tr>
			<td colspan="8"><IMG SRC="/itms/images/excel.gif" BORDER=0
				ALT="导出列表" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</table>
</body>
</html>
