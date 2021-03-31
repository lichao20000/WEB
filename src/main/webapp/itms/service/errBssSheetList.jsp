<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>错单查询</title>

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
function showSheetContent(sheetContent) {
	$('#"sheetContent"', window.parent.document).show();
	var url = "<s:url value='/itms/service/sheetContent.jsp'/>?sheetContent="+sheetContent;
	$.post(url, {
	}, function(mesg) {
		$('#sheetContent', window.parent.document).show();
		$('#sheetContent', window.parent.document).html(mesg);
	});
}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>错单列表</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>属地</th>
			<th>接收时间</th>
			<th>工单类型</th>
			<th>回单信息</th>
			<th>工单内容</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="errSheetList!=null">
			<s:if test="errSheetList.size()>0">
				<s:iterator value="errSheetList">
					<tr>
						<td><s:property value="username" /></td>
						<td><s:property value="city_name" /></td>
						<td><s:property value="recieve_date" /></td>
						<td><s:property value="sheet_type" /></td>
						<td><s:property value="sheet_resp" /></td>
						<td><a
							href="javascript:showSheetContent('<s:property value="sheet_content" />')">查看</a></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>没有查询到相关错单信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

			<td colspan="8" align="right"><lk:pages
				url="/itms/service/errorSheet!query.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>
</body>

</html>