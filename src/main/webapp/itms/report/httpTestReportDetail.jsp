<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>普通家庭用户测速成功率详细信息</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");
%>

<script type="text/javascript">
function ToExcel(){
	var page="<s:url value='/itms/report/httpTest!getDetailExcel.action'/>"
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<caption>普通家庭用户测速成功率详细信息</caption>
	<thead>
		<tr>
			<th align="center">LOID</th>
			<th align="center">厂家</th>
			<th align="center">属地</th>
			<th align="center">型号</th>
			<th align="center">硬件版本</th>
			<th align="center">软件版本</th>
			<th align="center">签约速率</th>
			<th align="center">测速速率</th>
			<th align="center">支持GE</th>
			<th align="center">上网方式</th>
			<th align="center">测速账户</th>
			<th align="center">测速时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="httpTestDetailList!=null">
			<s:if test="httpTestDetailList.size()>0">
				<s:iterator value="httpTestDetailList">
					<tr>
						<td class=column1 align="center"><s:property value="loid"/></td>
						<td class=column1 align="center"><s:property value="vendorName"/></td>
						<td class=column1 align="center"><s:property value="cityName"/></td>
						<td class=column1 align="center"><s:property value="deviceModel"/></td>
						<td class=column1 align="center"><s:property value="hardware"/></td>
						<td class=column1 align="center"><s:property value="software"/></td>
						<td class=column1 align="center"><s:property value="signingRate"/></td>
						<td class=column1 align="center"><s:property value="measureRate"/></td>
						<td class=column1 align="center"><s:property value="isGE"/></td>
						<td class=column1 align="center"><s:property value="wan_type"/></td>
						<td class=column1 align="center"><s:property value="testusername"/></td>
						<td class=column1 align="center"><s:property value="measureTime"/></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12>没有相关查询信息信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>没有相关查询信息</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="12" align="right"><lk:pages
						url="/itms/report/httpTest!qryDetailNext.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		 <tr>
				<td colspan="12" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr> 
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>