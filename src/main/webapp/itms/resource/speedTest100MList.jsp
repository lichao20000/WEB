<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备批量重启统计</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<% 
	request.setCharacterEncoding("GBK");
%>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
	parent.isButn(true);
});

function ToExcel() {
	parent.ToExcel();
}
</script>
</head>

<body>
<table class="listtable" id="listTable">
	<caption>宽带测速结果统计</caption>	
	<thead>
		<tr>
			<th>属地</th>
			<th>营业区</th>
			<th>LOID</th>
			<th>宽带账号</th>
			<th>用户宽带带宽</th>
			<th>厂家</th>
			<th>型号</th>
			<th>测速结果</th>
			<th>测速结果</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="speedRetList!=null && speedRetList.size()>0">
			<s:iterator value="speedRetList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="county_name" /></td>
						<td><s:property value="loid" /></td>
						<td><s:property value="username" /></td>
						<td><s:property value="bandwidth" /></td>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="devicemodel" /></td>
						<td><s:property value="speed_ret" />MB/S</td>
						<td><s:property value="result" /></td>
					</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="9">没有测速结果信息</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right">
				<lk:pages url="/itms/resource/speedTest1000M!query.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
		<tr>
			<td colspan="9" align="right">
				<img SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()" >
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>