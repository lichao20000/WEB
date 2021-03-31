<%@ page language="java" contenttype="text/html; charset=utf-8"
	pageencoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%string abspath=request.getcontextpath(); %>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/js/jquerysplitpage-linkage.js"/>"></script>
<script type="text/javascript">
	</script>

</head>
<body>
	<table class="listtable" id="listtable">
	<table class="listtable" id="listtable">
		<thead>
			<tr>
				<th >工单编号</th>
				<th >用户帐户</th>
				<th >执行结果</th>
				<th >开始时间</th>
				<th >结束时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="historyoperationlist != null">
				<s:if test="historyoperationlist.size()>0">
					<s:iterator value="historyoperationlist">
						<tr>
						<td><s:property value="sheet_id" /></td>
							<td><s:property value="username" /></td>
							<td>
							<s:if test="'-1'==fault_code">连接不上</s:if>
							<s:elseif test="'0'==fault_code">未知错误 </s:elseif>
							<s:elseif test="'-2'==fault_code">连接超时</s:elseif>
							<s:elseif test="'-3'==fault_code">未找到相关工单</s:elseif>
							<s:elseif test="'-4'==fault_code">未找到相关设备</s:elseif>
							<s:elseif test="'-5'==fault_code">未找到相关rpc命令</s:elseif>
							<s:elseif test="'-6'==fault_code">设备正被操作</s:elseif>
							<s:else>成功</s:else>
							</td>
							<td><s:property value="start_time"/></td>
							<td><s:property value="end_time"/></td>
						</tr>
					</s:iterator>
				</s:if>
			</s:if>
		</tbody>
	</table>
	</table>
</body>
</html>