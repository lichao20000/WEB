<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * 规范版本查询列表页面
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备列表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	function ToExcel() {
		var countNum='<s:property value="countNum" />';
		if(countNum>100000){
			var choice = window.confirm("导出数量超过10W,不建议导出(确定继续)");
			if(choice==true){
				var task_id='<s:property value="task_id" />';
				var city_id='<s:property value="city_id" />';
				var type='<s:property value="type" />';
				var page="<s:url value='/gwms/resource/batchSoftUpACT!queryDevListExcel4CQ.action'/>"+"?task_id="+task_id+"&&city_id="+city_id+"&&type="+type;
				document.all("childFrm").src=page;
			}
			else{
				return false;
			}
		}
		else{
			var task_id='<s:property value="task_id" />';
			var city_id='<s:property value="city_id" />';
			var type='<s:property value="type" />';
			var page="<s:url value='/gwms/resource/batchSoftUpACT!queryDevListExcel4CQ.action'/>"+"?task_id="+task_id+"&&city_id="+city_id+"&&type="+type;
			document.all("childFrm").src=page;
		}
	}
	

</script>

</head>

<body >
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>厂商</th>
			<th>型号</th>
			<th>区域</th>
			<th>当前版本</th>
			<th>开始执行</th>
			<th>执行结束</th>
			<th>执行结果</th>
			<th>结果描述</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="device_serialnumber" /></td>
						<td align="center"><s:property value="vendor_id" /></td>
						<td align="center"><s:property value="device_model_id" /></td>
						<td align="center"><s:property value="city_name" /></td>
						<td align="center"><s:property value="version" /></td>
						<td align="center"><s:property value="start_time" /></td>
						<td align="center"><s:property value="end_time" /></td>
						<td align="center"><s:property value="result_id" /></td>
						<td align="center"><s:property value="result_desc" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>没有查询到相关信息！</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="10" align="right">
					<lk:pages url="/gwms/resource/batchSoftUpACT!queryDevList4CQ.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="10" align="left">
					<IMG
					SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()">导出
				</td>
			</tr>
			<tr STYLE="display: none">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>