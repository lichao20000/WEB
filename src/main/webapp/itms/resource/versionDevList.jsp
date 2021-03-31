<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>版本统计查询</title>
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
	
	function OpenDetail(softwareversion) {
		parent.OpenDetail(softwareversion);
	}
	
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>版本统计查询</caption>
		<thead>
			<tr>
				<th>厂商</th>
				<th>型号</th>
				<th>硬件版本</th>
				<th>软件版本</th>
				<th>注册时间</th>
				<th>定版时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="deployDevList!=null">
				<s:if test="deployDevList.size()>0">
					<s:iterator value="deployDevList">
						<tr>
							<td><s:property value="vendor_id" /></td>
							<td><s:property value="device_model_id" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><a
								href="javascript:OpenDetail('<s:property value="devicetype_id"/>');">
									<s:property value="softwareversion" />
							</a></td>
							<td><s:property value="complete_time" /></td>
							<td><s:property value="versionttime" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>暂无新增功能部署报表信息</td>
					</tr>
				</s:else>

			</s:if>
			<s:else>
				<tr>
					<td colspan=6>系统没有此用户!</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" align="right"><lk:pages
						url="/itms/resource/VersionQuery!queryVersionDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
			<tr>
				<td colspan="6" align="right"><IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>