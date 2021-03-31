<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
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
	
	function ToExcel(){
			parent.ToExcel();
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>手工工单操作查询日志信息</caption>
	<thead>
		<tr>
			<th>操作工单loid</th>
			<th>绑定的设备序列号</th>
			<th>属地</th>
			<th>工单类型</th>
			<th>操作类型</th>
			<th>工单执行结果</th>
			<th>操作时间</th>
			<th>操作人</th>
			<th>操作人登录ip</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="operateList!=null">
			<s:if test="operateList.size()>0">
				<s:iterator value="operateList">
						<tr>
							<td><s:property value="user_Name" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="city_name" /></td>
							<td><s:property value="serv_type" /></td>
							<td><s:property value="oper_type" /></td>
							<td><s:property value="result_id" /></td>
							<td><s:property value="oper_time" /></td>
							<td><s:property value="acc_loginname" /></td>
							<td><s:property value="occ_ip" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>系统没有该用户的业务信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="9" align="right">
		 	<lk:pages
				url="/gtms/service/operateByHandQuery!getOperateByHandInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td colspan="9" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>