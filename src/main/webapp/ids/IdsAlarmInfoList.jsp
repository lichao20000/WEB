<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>预检预修告警信息</title>
<%
	/**
	 *  预检预修告警信息
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
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

	function ToExcel() {
		parent.ToExcel();
	}
</script>
</head>
<body>
	<table class="listtable" id="listTable" style="width: 1400px">
		<caption>预检预修告警信息</caption>
		<thead>
			<tr>
				<th>区域</th>
				<th>LOID</th>
				<th>设备标识</th>
				<th>告警类别</th>
				<th>告警名称</th>
				<th>告警代码</th>
				<th>告警信息</th>
				<th>告警开始时间</th>
				<th>最近一次上报时间</th>
				<!-- <th>告警次数</th> -->
				<%--th>告警预解除</th>
			<th>告警解除</th--%>
				<th>告警解除时间</th>
				<th>告警历时</th>
				<!-- <th>是否派单</th>
				<th>派单对象</th> -->
			<th>OLT名称</th>
			<th>OLTIP</th>
			<th>PON口</th>
			<%--<th>ONUID</th> --%>
			</tr>
		</thead>
		<tbody>
			<s:if test="alarmList!=null">
				<s:if test="alarmList.size()>0">
					<s:iterator value="alarmList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="alarm_type" /></td>
							<td><s:property value="alarm_name" /></td>
							<td><s:property value="alarm_code" /></td>

							<td><s:property value="alarm_content" /></td>
							<td><s:property value="first_up_time" /></td>
							<td><s:property value="last_up_time" /></td>

							<%-- <td><s:property value="alarm_count" /></td> --%>
							<%--td><s:property value="is_pre_release" /></td>
							<td><s:property value="is_release" /></td--%>
							<td><s:property value="release_time" /></td>
							<td><s:property value="duration_time" /></td>
							<%-- <td><s:property value="is_send_sheet" /></td>
							<td><s:property value="send_sheet_obj" /></td>
							--%>
							<td><s:property value="olt_name" /></td>
							<td><s:property value="olt_ip" /></td>
							<td><s:property value="pon_intf" /></td>
							<%--<td><s:property value="onu_id" /></td>--%> 

						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=18>系统没有预检预修告警信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=18>系统没有此用户!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="18" align="right"><lk:pages
						url="/ids/IdsAlarmInfo!getIdsarmInfoList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

			<tr>
				<td colspan="18" align="right"><IMG
					SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>