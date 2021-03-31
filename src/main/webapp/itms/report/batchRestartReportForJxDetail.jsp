<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������������������</title>
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
function ToExcel(){
	var taskId = $("#bind_taskId").val();
	var type = $("#bind_type").val();
	var start = $("#bind_start").val();
	var end = $("#bind_end").val();
	var page="<s:url value='/itms/report/batchRestart!getDetailExcel.action'/>?taskId="+taskId
			+"&type="+type
			+"&startTime="+start
			+"&endTime="+end
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
<input type="hidden" id="bind_taskId" value="<s:property value="taskId"/>" />
<input type="hidden" id="bind_type" value="<s:property value="type"/>" />
<input type="hidden" id="bind_start" value="<s:property value="startTime"/>" />
<input type="hidden" id="bind_end" value="<s:property value="endTime"/>" />
	<table class="listtable" id="listTable">
	<caption>������������������</caption>
	<thead>
		<tr>
			<th align="center">�豸����</th>
			<th align="center">�豸�ͺ�</th>
			<th align="center">����汾</th>
			<th align="center">����</th>
			<th align="center">�豸���к�</th>
			<th align="center">�豸IP</th>
			<th align="center">ҵ���˺�</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="restartDetailList!=null">
			<s:if test="restartDetailList.size()>0">
				<s:iterator value="restartDetailList">
					<tr>
						<td class=column1 align="center"><s:property value="vendorName"/></td>
						<td class=column1 align="center"><s:property value="deviceModel"/></td>
						<td class=column1 align="center"><s:property value="softwareversion"/></td>
						<td class=column1 align="center"><s:property value="cityName"/></td>
						<td class=column1 align="center"><s:property value="devSn"/></td>
						<td class=column1 align="center"><s:property value="devIp"/></td>
						<td class=column1 align="center"><s:property value="servAccount"/></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12>û����ز�ѯ��Ϣ��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>û����ز�ѯ��Ϣ</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="12" align="right"><lk:pages
						url="/itms/report/batchRestart!qryDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		 <tr>
				<td colspan="12" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr> 
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>