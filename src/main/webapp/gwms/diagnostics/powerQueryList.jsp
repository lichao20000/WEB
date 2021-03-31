<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>光功率采集结果查询</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}
	
	$(function(){
		$("button[@name='button']",parent.document).attr("disabled", false);
		$("#QueryData",parent.document).html("");
	});
	
	
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>光功率采集结果查询</caption>
		<thead>
			<tr>
				<th>序号</th>
				<th>属地</th>
				<th>区县</th>
				<th>逻辑ID</th>
				<th>厂家</th>
				<th>终端型号</th>
				<th>设备序列号</th>
				<th>发送光功率</th>
				<th>接收光功率</th>
				<th>采集时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="rlist!=null">
				<s:if test="rlist.size()>0">
					<s:iterator value="rlist">
						<tr bgcolor="#FFFFFF" align="center">
							<td class="column1"><s:property value="index"/></td>
							<td class="column1"><s:property value="parentName"/></td>
							<td class="column1"><s:property value="cityName"/></td>
							<td class="column1"><s:property value="username"/></td>
							<td class="column1"><s:property value="vendorName"/></td>
							<td class="column1"><s:property value="device_model"/></td>
							<td class="column1"><s:property value="deviceSn"/></td>
							<td class="column1"><s:property value="txPower"/></td>
							<td class="column1"><s:property value="rxPower"/></td>
							<td class="column1"><s:property value="colDate"/></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="10" align="left" style="background-color: white;">
							<font color="red">没有相关采集信息!</font>
						</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>没有相关采集信息!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages
						url="/itms/resource/VersionQuery!powerByQuery.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="10" align="right">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>