<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>营销报告查询</title>

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
function sendMail(mail,topic,content,customerId,cust_manager_id) {
	alert(cust_manager_id);
	var url = "<s:url value='./gtms/blocTest/sellSupportCustomize!sendMail.action'/>";
	$.post(url, {
		recieveMailAddress:mail,
		mailTopic:topic,
		mailContent:content,
		customerId:customerId,
		custManagerId:cust_manager_id
	}, function(ajax) {
		alert(ajax);
	});
}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>营销报告列表</caption>
	<thead>
		<tr>
			<th>客户ID</th>
			<th>流量上限</th>
			<th>流量下限</th>
			<th>时长上限</th>
			<th>时长下限</th>
			<th>实际流量</th>
			<th>实际时长</th>
			<th>客户经理</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="sellSupportReportList!=null">
			<s:if test="sellSupportReportList.size()>0">
				<s:iterator value="sellSupportReportList">
					<tr>
						<td><s:property value="customer_id" /></td>
						<td><s:property value="flow_max" /></td>
						<td><s:property value="flow_min" /></td>
						<td><s:property value="time_max" /></td>
						<td><s:property value="time_min" /></td>
						<td>0</td>
						<td>0</td>
						<td><s:property value="cust_manager_name" /></td>
						<td><a
							href="javascript:sendMail('<s:property value="e_mail" />','<s:property value="mail_topic" />','<s:property value="mail_content" />','<s:property value="customer_id" />','<s:property value="cust_manager_id" />')">邮件发送</a></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>没有查询到相关营销信息!</td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>

</html>