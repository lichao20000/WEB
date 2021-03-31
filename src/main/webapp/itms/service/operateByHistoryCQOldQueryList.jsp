<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * 原始工单查询
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
	function go(id,isHistory){
		var strpage = "<s:url value='/itms/service/operateByHistoryCQOldQuery!getOperateMessage.action'/>?bss_sheet_id="+id+"&isHistory="+isHistory;
		window.open(strpage,"","left=20,top=20,height=550,width=800,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}


	$(function() {
		parent.dyniframesize();
	});
	
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>原始工单信息<font color="red"> (注：发送工单时请按从前往后顺序依次执行)</font></caption>
	<thead>
		<tr>
			<th>工单流水号</th>
			<th>业务类型</th>
			<th>工单类型</th>
			<th>用户账号</th>
			<th>设备序列号</th>
			<th>用户逻辑ID</th>
			<th>执行情况</th>
			<th>接受时间</th>
			<th>完成时间</th>
			<th>操作</th>
			<!-- <th>历时时长</th> -->
			<th>客户号</th>
			<th>属地</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="dataList!=null">
			<s:if test="dataList.size()>0">
				<s:iterator value="dataList">
						<tr>
							<td><s:property value="WORK_ASGN_ID" /></td>
							<td><s:property value="SERVICE_TYPE" /></td>
							<td><s:property value="SERVICE_OPT" /></td>
							<td><s:property value="ACCOUNT_NAME" /></td>
							<td><s:property value="SERIAL_NUMBER" /></td>
							<td><s:property value="LOID" /></td>
							<td> <font color="<s:property value='color'/>"><s:property value="STATUS" /></font> </td>
							<td><s:property value="ARRIVE_TIME" /></td>
							<td><s:property value="DEAL_TIME" /></td>
							<td><a href="javaScript:go('<s:property value='WORK_ASGN_ID'/>','<s:property value='isHistory'/>');" >详情</a>  </td>
							<%-- <td><s:property value="DURING_TIME" />秒</td> --%>
							<td><s:property value="CUSTOMER_ID" /></td>
							<td><s:property value="cityName" /></td>
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
				<td colspan=13>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<%-- <tfoot>
		<tr>
			<td colspan="13" align="right">
		 	<lk:pages
				url="/itms/service/operateByHistoryCQOldQuery!getOperateByHistoryInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot> --%>
</table>
	
</body>
</html>