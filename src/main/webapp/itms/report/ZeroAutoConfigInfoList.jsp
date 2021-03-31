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
		$("#qy",parent.document).attr('disabled',false);
		parent.closeMsgDlg();
	});
	
	function ToExcel(){
			parent.ToExcel();
	}
	
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>BSS业务状态信息</caption>
	<thead>
		<tr>
			<th>逻辑SN</th>
			<th>属地</th>
			<th>工单受理时间</th>
			<th>业务类型</th>
			<th>设备序列号</th>
			<th>开通状态</th>
			<th>终端类型</th>
			<th>终端厂家</th>
			<th>终端型号</th>
			<th>接入方式</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="zeroAutoList!=null">
			<s:if test="zeroAutoList.size()>0">
				<s:iterator value="zeroAutoList">
						<tr>
							<td><s:property value="user_Name" /></td>
							<td><s:property value="city_id" /></td>
							<td><s:property value="dealdate" /></td>
							<td><s:property value="serv_type" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="open_status" /></td>
							<td><s:property value="zdType" /></td>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="access_style_relay_id" /></td>
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
				<td colspan=10>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="10" align="right">
		 	<lk:pages
				url="/itms/report/zeroAutoConfig!getZeroAutoConfigInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>

		<tr>
			<td colspan="10" align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>