<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模拟工单操作日志</title>
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
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>模拟工单操作日志</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>业务类型</th>
				<th>业务账号</th>
				<th>操作类型</th>
				<th>操作人</th>
				<th>操作IP</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="bsslogList!=null">
				<s:if test="bsslogList.size()>0">
					<s:iterator value="bsslogList">
						<tr>
							<td><s:property value="username" /></td>
							<td><s:property value="serv_type_id" /></td>
							<td><s:property value="serv_account" /></td>
							<td><s:property value="oper_type" /></td>
							<td><s:property value="acc_loginname" /></td>
							<td><s:property value="occ_ip" /></td>
							<td><s:property value="oper_time" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=7>系统没有模拟工单操作日志信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>系统没有模拟工单操作日志!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="7" align="right"><lk:pages
						url="/itms/report/bsslogQuery!bsslogQuery.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="7" align="right"><IMG SRC="/itms/images/excel.gif"
					BORDER='0' ALT='导出列表' style='cursor: hand'
					onclick="ToExcel('<s:property value="loid"/>','<s:property value="bussinessacount"/>','<s:property value="startOpenDate"/>','<s:property value="operationuser"/>','<s:property value="bssaccount"/>')"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>