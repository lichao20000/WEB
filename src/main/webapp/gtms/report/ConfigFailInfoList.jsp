<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>失败明细报表查询</title>
<%
	/**
	 * 失败明细报表查询
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
	<thead>
		<tr>
			<th>属地名称</th>
			<th>设备型号</th>
			<th>设备序列号</th>
			<th>设备逻辑号</th>
			<th>下发时间</th>
			<th>错误描述</th>
			<th>错误详细描述</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="failInfoList!=null">
			<s:if test="failInfoList.size()>0">
				<s:iterator value="failInfoList">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="device_id_ex" /></td>
							<td><s:property value="times" /></td>
							<td><s:property value="fault_desc" /></td>
							<td><s:property value="fault_reason" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>系统没有该用户的下发失败信息!</td>
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
				url="/gtms/report/configFail!queryConfigInfo.action"
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