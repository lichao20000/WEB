<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>路由桥接修改记录查询</title>
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
		$("#qy",parent.document).attr('disabled',false);
		parent.closeMsgDlg();
	});
	
	function ToExcel(){
			parent.ToExcel();
	}
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>路由桥接修改记录查询结果</caption>
	<thead>
		<tr>
			<th>操作时间</th>
			<th>操作动作</th>
			<th>操作结果</th>
			<th>loid</th>
			<th>宽带账号</th>
			<th>操作来源</th>
			<th>操作人员</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="addtime" /></td>
							<td><s:property value="operaction" /></td>
							<td><s:property value="operresult" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="oper_origon" /></td>
							<td><s:property value="oper_staff" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>系统没有该用户的业务信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="10" align="right">
		 	<lk:pages
				url="/itms/report/bridgeAndRouteRecord!getRecord.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td colspan="10" align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>