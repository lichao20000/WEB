<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒每月活跃用户统计</title>

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
$(function() {
	parent.dyniframesize();
});

function ToExcel() {
	parent.ToExcel();
}
</script>
</head>

<body>
<table  class="listtable" id="listTable">
				<caption>机顶盒活跃用户统计 </caption>
				<thead>
							<tr>
							<th>机顶盒序列号</th>
							<th>IPTV账号</th>
							<th>厂家</th>
							<th>型号</th>
							<th>属地</th>
							<th>硬件版本</th>
							<th>软件版本</th>
							<th>最近一次在线时间</th>
							</tr>
				</thead>
				<tbody>
			<s:if test="activeUserList!=null">
				<s:if test="activeUserList.size()>0">
					<s:iterator value="activeUserList">
						<tr>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="serv_account" /></td>
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="cityName" /></td>
							<td><s:property value="hardwareversion" /></td>
							<td><s:property value="softwareversion" /></td>
							<td><s:property value="lasttime" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="8">未统计到用户信息</td>
					</tr>
				</s:else>
						</s:if>
						<s:else>
			<tr>
				<td colspan=5>未统计到用户信息</td>
			</tr>
		</s:else>
		</tbody>
			<tfoot>
			<tr>
				<td colspan="6"  align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
				</td>
				<td colspan="2"  align="right">
				<lk:pages url="/itms/report/activeUserReport!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
			</tfoot>
	</table>
</body>
</html>